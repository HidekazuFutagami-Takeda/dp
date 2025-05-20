package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.InsWsPlanForVacDao;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.InsWsPlanForVacJgiListUpdateDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListUpdateDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateModifyAllDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyIppanRowDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyRowDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.InsWsPlanForVacIppanUpdateLogic;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.InsWsStatusUpdateType;
import jp.co.takeda.model.InsWsPlan;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;

/**
 * ワクチン用施設特約店別計画機能の更新に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsInsWsPlanForVacService")
public class DpsInsWsPlanForVacServiceImpl implements DpsInsWsPlanForVacService {

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 従業員情報
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画対象品目
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * ワクチン用施設特約店別計画取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForVacDao")
	protected InsWsPlanForVacDao insWsPlanForVacDao;

	/**
	 * ワクチン用施設特約店別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusForVacDao")
	protected InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	/**
	 * ワクチン用施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckForVacService")
	protected DpsInsWsStatusCheckForVacService dpsInsWsStatusCheckForVacService;

	/**
	 * ワクチン用施設特約店別計画配分処理サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacExecuteService")
	protected DpsDistributionForVacExecuteService dpsDistributionForVacExecuteService;

	/**
	 * 施設特約店別計画取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanDao")
	protected InsWsPlanDao insWsPlanDao;

	// 施設特約店別計画の一括登録
	public void entryAllInsWsPlan(InsWsPlanForVacUpDateModifyAllDto upDateDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (upDateDto == null) {
			final String errMsg = "ワクチン用施設特約店別計画の一括登録用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員番号
		Integer jgiNo = upDateDto.getJgiNo();
		// 品目固定コード
		String prodCode = upDateDto.getProdCode();
		// 更新行・追加行リスト
		List<InsWsPlanUpDateModifyRowDto> rowList = upDateDto.getModifyRow();
		// 一般計の更新行リスト
		List<InsWsPlanUpDateModifyIppanRowDto> ippanRowList = upDateDto.getModifyIppanRow();

		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if ((rowList == null || rowList.size() == 0) && (ippanRowList == null || ippanRowList.size() == 0)) {
			final String errMsg = "更新対象がなし";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 重点先の登録
		update(jgiNo, prodCode, rowList);

		// 一般先の登録
		if (ippanRowList != null) {
			for (InsWsPlanUpDateModifyIppanRowDto ippanRowdto : ippanRowList) {
				// 一般先の登録
				new InsWsPlanForVacIppanUpdateLogic(insWsPlanDao, insWsPlanForVacDao, jgiNo, prodCode, ippanRowdto.getAddrCodePref(), ippanRowdto.getAddrCodeCity(), ConvertUtil
					.parseMoneyToNormalUnit(ippanRowdto.getIppanPlannedValue()), ippanRowdto.getIppanUpDate()).execute();
			}
		}
	}

	/**
	 * 重点先の登録を行う。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param rowList 重点先のリスト
	 * @throws LogicalException
	 */
	private void update(Integer jgiNo, String prodCode, List<InsWsPlanUpDateModifyRowDto> rowList) throws LogicalException {
		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// 従業員情報取得
		// ----------------------
		JgiMst jgiMst = null;
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
		} catch (DataNotFoundException e) {
			final String errMsg = "従業員情報取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 設定中のログイン情報
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		// エラーコード
		String errCode;
		// 施設特約店別計画ステータスチェック
		// 許可しないステータスリスト作成
		List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
		if (dpUser.isMatch(JokenSet.WAKUTIN_AL, JokenSet.WAKUTIN_MR)) {
			// (小児科AC,小児科MRの場合、公開中以外はだめ) (J19-0010 対応・コメントのみ修正)
			errCode = "DPS3280E";
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
		} else {
			// (配分済・公開中以外はだめ)
			errCode = "DPS3281E";
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
		}
		// チェック実行
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		jgiMstList.add(jgiMst);
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		plannedProdList.add(plannedProd);
		try {
			dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(errCode));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

		// 施設特約店別計画〆フラグ
		SysManage sysManage = sysManageDao.search(SysClass.DPS, SysType.VACCINE);
		if (sysManage.getWsEndFlg()) {
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3282E")));
		}


		// ----------------------
		// 登録処理
		// ----------------------
		// 重点先の登録
		for (InsWsPlanUpDateModifyRowDto rowDto : rowList) {
//			InsWsPlanForVac insWsPlan = new InsWsPlanForVac();
//			insWsPlan.setSeqKey(rowDto.getSeqKey());
//			insWsPlan.setJgiNo(jgiNo);
//			insWsPlan.setProdCode(prodCode);
//			insWsPlan.setInsNo(rowDto.getInsNo());
//			insWsPlan.setTmsTytenCd(rowDto.getTmsTytenCd());
//			insWsPlan.setPlannedValueB(ConvertUtil.parseMoneyToNormalUnit(rowDto.getPlannedValue()));
//			insWsPlan.setSpecialInsPlanFlg(rowDto.getSpecialInsPlanFlg());
//			insWsPlan.setExpectDistInsFlg(rowDto.getExceptDistInsFlg());
//			insWsPlan.setDelInsFlg(rowDto.getDelInsFlg());
//			insWsPlan.setUpDate(rowDto.getPlannedUpDate());

			// 修正値（修正値＝確定値）
			Long modifyValue = ConvertUtil.parseMoneyToNormalUnit(rowDto.getPlannedValue());

			InsWsPlan insWsPlan = new InsWsPlan();
			insWsPlan.setSeqKey(rowDto.getSeqKey());
			insWsPlan.setJgiNo(jgiNo);
			insWsPlan.setProdCode(prodCode);
			insWsPlan.setInsNo(rowDto.getInsNo());
			insWsPlan.setTmsTytenCd(rowDto.getTmsTytenCd());
			insWsPlan.setModifyValueY(modifyValue);
			insWsPlan.setPlannedValueY(modifyValue);
			insWsPlan.setSpecialInsPlanFlg(rowDto.getSpecialInsPlanFlg());
			insWsPlan.setExpectDistInsFlg(rowDto.getExceptDistInsFlg());
			insWsPlan.setDelInsFlg(rowDto.getDelInsFlg());
			insWsPlan.setUpDate(rowDto.getPlannedUpDate());


			if (rowDto.getSeqKey() == null) {
//				// 新規登録の場合、計画値の入力があった行のみ登録する。
//				if (insWsPlan.getPlannedValueB() != null) {
//					insWsPlanForVacDao.insert(insWsPlan);
//				}
				// 既存計画がなく、修正値の入力があった行のみ登録する
				if (insWsPlan.getModifyValueY() != null) {
					insWsPlanDao.insert(insWsPlan);
				}
			} else {
				try {
//					InsWsPlanForVac tmpInsWsPlan = insWsPlanForVacDao.search(insWsPlan.getSeqKey());
//					// 配分値NULL、計画値NULLの登録情報の場合、削除する。
//					if (tmpInsWsPlan.getDistValueB() == null && insWsPlan.getPlannedValueB() == null) {
//						insWsPlanForVacDao.delete(insWsPlan.getSeqKey(), insWsPlan.getUpDate());
//						continue;
//					}

					// 既存計画がある場合
					InsWsPlan tmpInsWsPlan = insWsPlanDao.search(insWsPlan.getSeqKey());

					// 配分値および修正値がない場合は、削除する
					if (tmpInsWsPlan.getDistValueY() == null && insWsPlan.getModifyValueY() == null) {
						insWsPlanDao.delete(insWsPlan.getSeqKey(), insWsPlan.getUpDate());
						continue;
					}

					// 修正値がない場合は、確定値＝配分値
					if(insWsPlan.getModifyValueY() == null){
						insWsPlan.setPlannedValueY(tmpInsWsPlan.getDistValueY());
					}

					// 更新対象のデータが無い場合、楽観的排他エラー
				} catch (DataNotFoundException e) {
					final String errMsg = "ワクチン施設特約店更新時に更新対象が見つからないため、楽観的ロックエラーとする";
					throw new OptimisticLockingFailureException(errMsg, e);
				}
//				insWsPlanForVacDao.update(insWsPlan);

				// 更新
				insWsPlanDao.update(insWsPlan);
			}
		}
	}

	// 再配分(品目一覧)
	public void rehaibun(Integer jgiNo, List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsPlanUpdateDtoList == null || insWsPlanUpdateDtoList.size() == 0) {
			final String errMsg = "施設特約店別計画 品目一覧更新用DTOがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員情報リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		try {
			JgiMst jgiMst = jgiMstDAO.search(jgiNo);
			jgiMstList.add(jgiMst);
		} catch (DataNotFoundException e) {
			final String errMsg = "再配分対象の従業員情報が存在しない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + jgiNo), e);
		}

		// 品目情報リスト作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (InsWsPlanForVacProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			plannedProdList.add(insWsPlanUpdateDto.convertPlannedProd());
		}

		// ----------------------
		// ステータス最終更新日チェック
		// ----------------------
		for (InsWsPlanForVacProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			String prodCode = insWsPlanUpdateDto.getProdCode();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();
			insWsPlanStatusForVacDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// ----------------------
		// 再配分
		// ----------------------
		doRehaibun(jgiMstList, plannedProdList);
	}

	// ステータス更新(品目一覧)
	public void updateStatus(String sosCd3, Integer jgiNo, List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "検索条件の組織コード・従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsPlanUpdateDtoList == null || insWsPlanUpdateDtoList.size() == 0) {
			final String errMsg = "施設特約店別計画 品目一覧更新用DTOがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateType == null) {
			final String errMsg = "施設特約店別計画ステータス更新種別がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員情報リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		if (jgiNo != null) {
			try {
				JgiMst jgiMst = jgiMstDAO.search(jgiNo);
				jgiMstList.add(jgiMst);
			} catch (DataNotFoundException e) {
				final String errMsg = "再配分対象の従業員情報が存在しない:";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + jgiNo), e);
			}
		}

		// 品目情報リスト作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (InsWsPlanForVacProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			plannedProdList.add(insWsPlanUpdateDto.convertPlannedProd());
		}

		// ----------------------
		// ステータス最終更新日チェック
		// ----------------------
		for (InsWsPlanForVacProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			String prodCode = insWsPlanUpdateDto.getProdCode();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();

			if (jgiNo != null) {
				insWsPlanStatusForVacDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
			} else {
				insWsPlanStatusForVacDao.checkLastUpDate(sosCd3, null, prodCode, orgLastUpdate);
			}
		}

		// ----------------------
		// 実行
		// ----------------------
		switch (updateType) {
			case OPEN:
				doOpen(sosCd3, jgiMstList, plannedProdList);
				break;
			case OPEN_CANCEL:
				doOpenCancel(sosCd3, jgiMstList, plannedProdList);
				break;
			case FIX:
				doFix(sosCd3, jgiMstList, plannedProdList);
				break;
			case FIX_CANCEL:
				doFixCancel(sosCd3, jgiMstList, plannedProdList);
				break;
			default:
				break;
		}
	}

	// 再配分(担当者一覧)
	public void rehaibun(String prodCode, List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsPlanUpdateDtoList == null || insWsPlanUpdateDtoList.size() == 0) {
			final String errMsg = "施設特約店別計画 品目一覧更新用DTOがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員情報リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		for (InsWsPlanForVacJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			jgiMstList.add(insWsPlanUpdateDto.convertJgiMst());
		}

		// 品目情報リスト作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		try {
			PlannedProd plannedProd = plannedProdDAO.search(prodCode);
			plannedProdList.add(plannedProd);
		} catch (DataNotFoundException e) {
			final String errMsg = "再配分対象の計画品目が存在しない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// ステータス最終更新日チェック
		// ----------------------
		for (InsWsPlanForVacJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			Integer jgiNo = insWsPlanUpdateDto.getJgiNo();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();
			insWsPlanStatusForVacDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// ----------------------
		// 再配分
		// ----------------------
		doRehaibun(jgiMstList, plannedProdList);
	}

	// ステータス更新(担当者一覧)
	public void updateStatus(String prodCode, List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insWsPlanUpdateDtoList == null || insWsPlanUpdateDtoList.size() == 0) {
			final String errMsg = "施設特約店別計画 品目一覧更新用DTOがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateType == null) {
			final String errMsg = "施設特約店別計画ステータス更新種別がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員情報リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		for (InsWsPlanForVacJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			jgiMstList.add(insWsPlanUpdateDto.convertJgiMst());
		}

		// 品目情報リスト作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		try {
			PlannedProd plannedProd = plannedProdDAO.search(prodCode);
			plannedProdList.add(plannedProd);
		} catch (DataNotFoundException e) {
			final String errMsg = "再配分対象の計画品目が存在しない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// ステータス最終更新日チェック
		// ----------------------
		for (InsWsPlanForVacJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			Integer jgiNo = insWsPlanUpdateDto.getJgiNo();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();
			insWsPlanStatusForVacDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// ----------------------
		// 実行
		// ----------------------
		switch (updateType) {
			case OPEN:
				doOpen(null, jgiMstList, plannedProdList);
				break;
			case OPEN_CANCEL:
				doOpenCancel(null, jgiMstList, plannedProdList);
				break;
			case FIX:
				doFix(null, jgiMstList, plannedProdList);
				break;
			case FIX_CANCEL:
				doFixCancel(null, jgiMstList, plannedProdList);
				break;
			default:
				break;
		}
	}

	/**
	 * 再配分を実行する。<br>
	 * ・施設特約店別計画〆チェック<br>
	 * ・施設特約店別計画ステータスチェック<br>
	 * ・配分処理実行<br>
	 *
	 * @param jgiMstList 配分対象の従業員情報のリスト
	 * @param plannedProdList 配分対象の品目情報のリスト
	 */
	private void doRehaibun(List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiMstList == null || jgiMstList.size() == 0) {
			final String errMsg = "配分対象の従業員情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 施設特約店別計画〆チェック
		SysManage sysManage = null;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.VACCINE);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理情報が存在しない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		if (sysManage.getWsEndFlg()) {
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3263E")));
		}

		// 施設特約店別計画ステータス
		List<InsWsPlanStatusForVac> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_OPENED);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3279E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 再配分
		// ----------------------

		// 再配分開始日時取得
		Date distStartDate = commonDao.getSystemTime();

		// 再配分処理実行
		final DpUser execDpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		for (PlannedProd plannedProd : plannedProdList) {
			for (JgiMst jgiMst : jgiMstList) {
				dpsDistributionForVacExecuteService.executeAgain(jgiMst.getJgiNo(), plannedProd.getProdCode(), execDpUser);
			}
		}

		// 再配分終了日時取得
		Date distEndDate = commonDao.getSystemTime();

		// ----------------------
		// ステータス更新
		// ----------------------
		for (InsWsPlanStatusForVac insWsPlanStatus : insWsPlanStatusList) {
			insWsPlanStatus.setDistStartDate(distStartDate);
			insWsPlanStatus.setDistEndDate(distEndDate);
			insWsPlanStatusForVacDao.update(insWsPlanStatus);
		}

	}

	/**
	 * 公開処理を行なう。
	 *
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doOpen(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設特約店別計画ステータス
		List<InsWsPlanStatusForVac> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_OPENED);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
			} else {
				final String errMsg = "施設特約店別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3275E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		// 公開日時取得
		Date openDate = commonDao.getSystemTime();
		for (InsWsPlanStatusForVac insWsPlanStatus : insWsPlanStatusList) {
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_OPENED);
			insWsPlanStatus.setInsWsOpenDate(openDate);
			insWsPlanStatusForVacDao.update(insWsPlanStatus);
		}
	}

	/**
	 * 公開解除処理を行なう。
	 *
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doOpenCancel(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 施設特約店別計画ステータス
		List<InsWsPlanStatusForVac> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
			} else {
				final String errMsg = "施設特約店別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3276E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		for (InsWsPlanStatusForVac insWsPlanStatus : insWsPlanStatusList) {
			insWsPlanStatus.setInsWsOpenDate(null);
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
			insWsPlanStatusForVacDao.update(insWsPlanStatus);
		}
	}

	/**
	 * 確定処理を行なう。
	 *
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doFix(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 施設特約店別計画ステータス
		List<InsWsPlanStatusForVac> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
			} else {
				final String errMsg = "施設特約店別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3277E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		// 公開日時取得
		Date fixDate = commonDao.getSystemTime();
		for (InsWsPlanStatusForVac insWsPlanStatus : insWsPlanStatusList) {
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_COMPLETED);
			insWsPlanStatus.setInsWsFixDate(fixDate);
			insWsPlanStatusForVacDao.update(insWsPlanStatus);
		}
	}

	/**
	 * 確定解除処理を行なう
	 *
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doFixCancel(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 施設特約店別計画〆チェック
		SysManage sysManage = null;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.VACCINE);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理情報が存在しない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		if (sysManage.getWsEndFlg()) {
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3265E")));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 施設特約店別計画ステータス
		List<InsWsPlanStatusForVac> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_OPENED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insWsPlanStatusList = dpsInsWsStatusCheckForVacService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
			} else {
				final String errMsg = "施設特約店別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3278E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		for (InsWsPlanStatusForVac insWsPlanStatus : insWsPlanStatusList) {
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_OPENED);
			insWsPlanStatusForVacDao.update(insWsPlanStatus);
		}
	}
}
