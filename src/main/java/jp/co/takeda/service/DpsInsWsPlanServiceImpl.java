package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.dao.DpsKakuteiErrMsgDao;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.InsPlanDao;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.InsWsPlanJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanJgiListScDto;
import jp.co.takeda.dto.InsWsPlanJgiListUpdateDto;
import jp.co.takeda.dto.InsWsPlanMrResultDto;
import jp.co.takeda.dto.InsWsPlanProdListUpdateDto;
import jp.co.takeda.dto.InsWsPlanTeamResultDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyRowDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.CreateChoseiMsgLogic;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.InsWsStatusUpdateType;
import jp.co.takeda.logic.div.MrStatusForCheck;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsKakuteiErrMsg;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.InsWsPlan;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設特約店別計画機能の更新に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsInsWsPlanService")
public class DpsInsWsPlanServiceImpl implements DpsInsWsPlanService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsInsWsPlanServiceImpl.class);

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
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanDao;

	/**
	 * 施設特約店別計画取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanDao")
	protected InsWsPlanDao insWsPlanDao;

	/**
	 * 施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insPlanDao")
	protected InsPlanDao insPlanDao;

	/**
	 * 施設特約店別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusDao")
	protected InsWsPlanStatusDao insWsPlanStatusDao;

	/**
	 * 担当者別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設医師別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocStatusCheckService")
	protected DpsInsDocStatusCheckService dpsInsDocStatusCheckService;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	/**
	 * 施設特約店別計画配分処理サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionExecuteService")
	protected DpsDistributionExecuteService dpsDistributionExecuteService;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画支援カテゴリ領域Dao
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	/**
	 * 施設特約店別計画機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanSearchService")
	protected DpsInsWsPlanSearchService dpsInsWsPlanSearchService;

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 一括確定エラー情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsKakuteiErrMsgDao")
	protected DpsKakuteiErrMsgDao dpsKakuteiErrMsgDao;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	// 施設特約店別計画の登録
	public void entryInsWsPlan(InsWsPlanUpDateModifyDto upDateDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (upDateDto == null) {
			final String errMsg = "施設特約店別計画の追加・更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		Integer jgiNo = upDateDto.getJgiNo();
		String prodCode = upDateDto.getProdCode();
		List<InsWsPlanUpDateModifyRowDto> rowList = upDateDto.getModifyRow();
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (rowList == null || rowList.size() == 0) {
			final String errMsg = "更新行、追加行DTOのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 計画対象品目取得
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// 従業員情報取得
		JgiMst jgiMst = null;
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
		} catch (DataNotFoundException e) {
			final String errMsg = "従業員情報取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ---------------------------
		// 施設特約店別計画〆チェック
		// ---------------------------
		SysManage sysManage = sysManageDao.search(SysClass.DPS, SysType.IYAKU);
		if (sysManage.getWsEndFlg()) {
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3282E")));
		}

		// ---------------------------
		// ステータスチェック
		// ---------------------------
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		jgiMstList.add(jgiMst);
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		plannedProdList.add(plannedProd);
		try {
			// 施設特約店別計画ステータスチェック(許可しないステータスリスト作成)
			// 配分済以外はだめ
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
			dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3281E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

		// ---------------------------
		// 登録処理
		// ---------------------------
		for (InsWsPlanUpDateModifyRowDto rowDto : rowList) {

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

				// 既存計画がなく、修正値の入力があった行のみ登録する
				if (insWsPlan.getModifyValueY() != null) {
					insWsPlanDao.insert(insWsPlan);
				}

			} else {

				try {

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

					// 更新
					insWsPlanDao.update(insWsPlan);

				} catch (DataNotFoundException e) {

					// 更新対象のデータが無い場合、楽観的排他エラー
					final String errMsg = "施設特約店更新時に更新対象が見つからないため、楽観的ロックエラーとする";
					throw new OptimisticLockingFailureException(errMsg, e);
				}
			}
		}
	}

	// 再配分(品目一覧)
	public void rehaibun(String sosCd3, Integer jgiNo, List<InsWsPlanProdListUpdateDto> insWsPlanUpdateDtoList) {

		// -----------------------------------
		// 引数チェック
		// -----------------------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
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
		for (InsWsPlanProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			plannedProdList.add(insWsPlanUpdateDto.convertPlannedProd());
		}

		// -----------------------------------
		// ステータス最終更新日チェック
		// -----------------------------------
		for (InsWsPlanProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			String prodCode = insWsPlanUpdateDto.getProdCode();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();
			insWsPlanStatusDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// -----------------------------------
		// 再配分
		// -----------------------------------
		doRehaibun(sosCd3, jgiMstList, plannedProdList);
	}

	// ステータス更新(品目一覧)
	public void updateStatus(String sosCd3, String sosCd4, Integer jgiNo, List<InsWsPlanProdListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType) {

		// -----------------------------------
		// 引数チェック
		// -----------------------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
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
		for (InsWsPlanProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			plannedProdList.add(insWsPlanUpdateDto.convertPlannedProd());
		}

		// -----------------------------------
		// ステータス最終更新日チェック
		// -----------------------------------
		for (InsWsPlanProdListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			String prodCode = insWsPlanUpdateDto.getProdCode();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();

			if (jgiNo != null) {
				insWsPlanStatusDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
			} else {
				insWsPlanStatusDao.checkLastUpDate(sosCd3, sosCd4, prodCode, orgLastUpdate);
			}
		}

		// -----------------------------------
		// 実行
		// -----------------------------------
		switch (updateType) {
			case FIX:
				doFix(sosCd3, sosCd4, jgiMstList, plannedProdList);
				break;
			case FIX_CANCEL:
				doFixCancel(sosCd3, sosCd4, jgiMstList, plannedProdList);
				break;
			default:
				break;
		}
	}

	// 再配分(担当者一覧)
	public void rehaibun(String sosCd3, String prodCode, List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList) {

		// -----------------------------------
		// 引数チェック
		// -----------------------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
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
		for (InsWsPlanJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
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

		// -----------------------------------
		// ステータス最終更新日チェック
		// -----------------------------------
		for (InsWsPlanJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			Integer jgiNo = insWsPlanUpdateDto.getJgiNo();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();
			insWsPlanStatusDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// -----------------------------------
		// 再配分
		// -----------------------------------
		doRehaibun(sosCd3, jgiMstList, plannedProdList);
	}

	// ステータス更新(担当者一覧)
	public void updateStatus(String sosCd3, String prodCode, List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
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
		for (InsWsPlanJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
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

		// -----------------------------------
		// ステータス最終更新日チェック
		// -----------------------------------
		for (InsWsPlanJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			Integer jgiNo = insWsPlanUpdateDto.getJgiNo();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();
			insWsPlanStatusDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// ----------------------
		// 実行
		// ----------------------
		switch (updateType) {
			case FIX:
				doFix(sosCd3, null, jgiMstList, plannedProdList);
				break;
			case FIX_CANCEL:
				doFixCancel(sosCd3, null, jgiMstList, plannedProdList);
				break;
			default:
				break;
		}
	}
	// ステータス更新 差分チェック付き
	@Override
	public void updateStatusWithDiffCheck(String sosCd3, String prodCode) throws LogicalException {

		InsWsPlanJgiListResultDto insWsPlanJgiListResultDto = dpsInsWsPlanSearchService.searchMrList(new InsWsPlanJgiListScDto(sosCd3, null, prodCode));
		List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList = convertViewDtoToUpdateDtoList(insWsPlanJgiListResultDto);

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員情報リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		for (InsWsPlanJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
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

		// -----------------------------------
		// ステータス最終更新日チェック
		// -----------------------------------
		for (InsWsPlanJgiListUpdateDto insWsPlanUpdateDto : insWsPlanUpdateDtoList) {
			Integer jgiNo = insWsPlanUpdateDto.getJgiNo();
			Date orgLastUpdate = insWsPlanUpdateDto.getUpDate();
			insWsPlanStatusDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// ----------------------
		// 実行
		// ----------------------
		doFixWithDiffCheck(sosCd3, null, jgiMstList, plannedProdList,insWsPlanJgiListResultDto);

	}
	private List<InsWsPlanJgiListUpdateDto> convertViewDtoToUpdateDtoList(InsWsPlanJgiListResultDto insWsPlanJgiListResultDto) {

		List<InsWsPlanTeamResultDto> teamResultList = insWsPlanJgiListResultDto.getTeamResultList();
		List<InsWsPlanJgiListUpdateDto> resultList = new ArrayList<InsWsPlanJgiListUpdateDto>();
	    for( InsWsPlanTeamResultDto team :teamResultList) {
	    	for ( InsWsPlanMrResultDto mr :team.getMrList()) {
	    		InsWsPlanJgiListUpdateDto e = new InsWsPlanJgiListUpdateDto(mr.getInsWsPlanStatus().getJgiNo(), mr.getInsWsPlanStatus().getJgiName(),mr.getInsWsPlanStatus().getUpDate());
	    		resultList.add(e);
	    	}
	    }
		return resultList;
	}

	/**
	 * 施設特約店別計画確定処理を行う。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 * @param insWsPlanJgiListResultDto
	 */
	private void doFixWithDiffCheck(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, InsWsPlanJgiListResultDto insWsPlanJgiListResultDto) {

		// -------------------------------------
		// 引数チェック
		// -------------------------------------
		if (sosCd3 == null && sosCd4 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String category = plannedProdList.get(0).getCategory();
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加すると言う意図で、仕入品以外という条件に変更
//		if (category == ProdCategory.MMP || category == ProdCategory.ONC ) {
		// 計画対象カテゴリ領域を取得
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		try {
			plannedCtg = dpsPlannedCtgDao.search(category);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象カテゴリ領域が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
//		if (category != null && !dpsCodeMasterSearchService.isSiire(category)) {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加すると言う意図で、仕入品以外という条件に変更

		// 計画立案レベル・担当者=1の場合
		if (plannedCtg.getPlanLevelMr().equals("1")) {
			// -------------------------------------
			// 担当者別計画ステータスチェック
			// -------------------------------------
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
			try {
				// ステータスチェック実行
				dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);
			} catch (UnallowableStatusException e) {
				// ステータスエラー
				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3333E"));
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
				List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList = new ArrayList<DpsKakuteiErrMsg>();
				DpsKakuteiErrMsg dpsKakuteiErrMsg = new DpsKakuteiErrMsg();
				dpsKakuteiErrMsg.setSosCd(sosCd3);
				//品目×組織で循環しているので、品目は１種のみで動作する
				dpsKakuteiErrMsg.setProdCode(plannedProdList.get(0).getProdCode());
				//従業員情報は取得できないので、存在しない従業員番号の0で設定
				dpsKakuteiErrMsg.setIsJgiNo(0);
				dpsKakuteiErrMsg.setMessageKey(new MessageKey("DPS3333E"));
				dpsKakuteiErrMsgList.add(dpsKakuteiErrMsg);
//				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
				throw new UnallowableStatusException(new Conveyance(dpsKakuteiErrMsgList, messageKeyList), e);
//mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
			}
		}

		// -------------------------------------
		// 施設特約店別計画ステータスチェック
		// -------------------------------------
		List<InsWsPlanStatus> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
			} else {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
			}

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
		//テーブル出力、エラー件数１に対して１件の登録にする為、共通ロジックから専用ロジックに修正
//				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
				insWsPlanStatusList = dpsInsWsStatusCheckService.executeKakutei(sosCd3, jgiMstList, plannedProdList, unallowableInsWsStatusList);
//			} else if (!StringUtils.isEmpty(sosCd4)) {
			} else if (!StringUtils.isEmpty(sosCd4)) {
//				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsWsStatusList);
				insWsPlanStatusList = dpsInsWsStatusCheckService.executeKakutei(sosCd3, sosCd4, plannedProdList, unallowableInsWsStatusList);
//			} else if (!StringUtils.isEmpty(sosCd3)) {
			} else if (!StringUtils.isEmpty(sosCd3)) {
//				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
				insWsPlanStatusList = dpsInsWsStatusCheckService.executeKakutei(sosCd3, plannedProdList, unallowableInsWsStatusList);
//mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
			} else {
				final String errMsg = "施設特約店別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3335E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
//			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
			throw new UnallowableStatusException(new Conveyance(e.getConveyance().getDpsKakuteiErrMsgList(), messageKeyList), e);
//mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
		}
		InsWsPlanTeamResultDto result = insWsPlanJgiListResultDto.getTeamResultList().get(0);
		List<InsWsPlanMrResultDto> mrList = result.getMrList();

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
		// -------------------------------------
		// 削除（予定）施設の計画立案チェック
		// -------------------------------------

		List<DpsKakuteiErrMsg> delInsCheckList = new ArrayList<DpsKakuteiErrMsg>();
		delInsCheckList = insWsPlanStatusDao.delInsCheck(insWsPlanStatusList);

		if(delInsCheckList.size() > 0) {
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList = new ArrayList<DpsKakuteiErrMsg>();
			for (DpsKakuteiErrMsg delInsErr : delInsCheckList) {
				messageKeyList.add(new MessageKey("DPS3350E", delInsErr.getInsName()));
				DpsKakuteiErrMsg dpsKakuteiErrMsg = new DpsKakuteiErrMsg();
				dpsKakuteiErrMsg.setSosCd(delInsErr.getSosCd());
				dpsKakuteiErrMsg.setProdCode(delInsErr.getProdCode());
				dpsKakuteiErrMsg.setIsJgiNo(delInsErr.getJgiNo());
				dpsKakuteiErrMsg.setMessageKey(new MessageKey("DPS3350E", delInsErr.getInsName()));
				dpsKakuteiErrMsgList.add(dpsKakuteiErrMsg);
			}
			throw new UnallowableStatusException(new Conveyance(dpsKakuteiErrMsgList, messageKeyList));
		}

		// -------------------------------------
		// 計画立案対象外特約店の計画立案チェック
		// -------------------------------------

		List<DpsKakuteiErrMsg> wsExceptPlanCheckList = new ArrayList<DpsKakuteiErrMsg>();
		wsExceptPlanCheckList = insWsPlanStatusDao.wsExceptPlanCheck(insWsPlanStatusList);

		if(wsExceptPlanCheckList.size() > 0) {
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList = new ArrayList<DpsKakuteiErrMsg>();
			for (DpsKakuteiErrMsg exceptErr : wsExceptPlanCheckList) {
				messageKeyList.add(new MessageKey("DPS3351E", exceptErr.getTytenMei()));
				DpsKakuteiErrMsg dpsKakuteiErrMsg = new DpsKakuteiErrMsg();
				dpsKakuteiErrMsg.setSosCd(exceptErr.getSosCd());
				dpsKakuteiErrMsg.setProdCode(exceptErr.getProdCode());
				dpsKakuteiErrMsg.setIsJgiNo(exceptErr.getJgiNo());
				dpsKakuteiErrMsg.setMessageKey(new MessageKey("DPS3351E", exceptErr.getTytenMei()));
				dpsKakuteiErrMsgList.add(dpsKakuteiErrMsg);
			}
			throw new UnallowableStatusException(new Conveyance(dpsKakuteiErrMsgList, messageKeyList));
		}

		// -------------------------------------
		// 配分除外施設の計画立案チェック
		// -------------------------------------

		List<DpsKakuteiErrMsg> exceptDistIncCheckList = new ArrayList<DpsKakuteiErrMsg>();
		exceptDistIncCheckList = insWsPlanStatusDao.exceptDistIncCheck(insWsPlanStatusList);

		if(exceptDistIncCheckList.size() > 0) {
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList = new ArrayList<DpsKakuteiErrMsg>();
			for (DpsKakuteiErrMsg distIncErr : exceptDistIncCheckList) {
				messageKeyList.add(new MessageKey("DPS3352E", distIncErr.getInsName()));
				DpsKakuteiErrMsg dpsKakuteiErrMsg = new DpsKakuteiErrMsg();
				dpsKakuteiErrMsg.setSosCd(distIncErr.getSosCd());
				dpsKakuteiErrMsg.setProdCode(distIncErr.getProdCode());
				dpsKakuteiErrMsg.setIsJgiNo(distIncErr.getJgiNo());
				dpsKakuteiErrMsg.setMessageKey(new MessageKey("DPS3352E", distIncErr.getInsName()));
				dpsKakuteiErrMsgList.add(dpsKakuteiErrMsg);
			}
			throw new UnallowableStatusException(new Conveyance(dpsKakuteiErrMsgList, messageKeyList));
		}

//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

		// ----------------------
		// ステータス更新
		// ----------------------
		// ステータスエラー
		List<MessageKey> messageKeyList = new ArrayList<MessageKey>();

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
		//一括確定エラー情報
		List<DpsKakuteiErrMsg> kakuteiErrMsgList = new ArrayList<DpsKakuteiErrMsg>();
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

		// 確定日時取得
		Date fixDate = commonDao.getSystemTime();
		for (InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {

			for (InsWsPlanMrResultDto e : mrList) {
				if (!e.getInsWsPlanStatus().getJgiNo().equals(insWsPlanStatus.getJgiNo())) {
					continue;
				}
				//差額チェック もし差額がなければ更新 あれば更新スキップし、UnAllowableExceptionに載せる
				if (e.getPlannedSummary().getSagakuP() + e.getPlannedSummary().getSagakuUh() != 0) {
					messageKeyList.add(new MessageKey("DPS3250E", e.getInsWsPlanStatus().getJgiName()));
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
					DpsKakuteiErrMsg dpsKakuteiErrMsg = new DpsKakuteiErrMsg();
					dpsKakuteiErrMsg.setSosCd(sosCd3);
					dpsKakuteiErrMsg.setProdCode(e.getInsWsPlanStatus().getProdCode());
					dpsKakuteiErrMsg.setIsJgiNo(e.getInsWsPlanStatus().getJgiNo());
					dpsKakuteiErrMsg.setMessageKey(new MessageKey("DPS3250E", e.getInsWsPlanStatus().getJgiName()));
					kakuteiErrMsgList.add(dpsKakuteiErrMsg);
//					dpsKakuteiErrMsgDao.insert(sosCd3, e.getInsWsPlanStatus().getJgiNo(), e.getInsWsPlanStatus().getProdCode(), new MessageKey("DPS3250E", e.getInsWsPlanStatus().getJgiName()));
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
					break;
				} else {
					insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_COMPLETED);
					insWsPlanStatus.setInsWsFixDate(fixDate);
					insWsPlanStatusDao.update(insWsPlanStatus);
					break;
				}
			}
		}
		if (messageKeyList.size() > 0) {
//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
//			throw new UnallowableStatusException(new Conveyance(messageKeyList));
			throw new UnallowableStatusException(new Conveyance(kakuteiErrMsgList, messageKeyList));
//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
		}
	}
	/**
	 * 再配分を実行する。<br>
	 * ・施設特約店別計画〆チェック<br>
	 * ・担当者別計画ステータスチェック<br>
	 * ・施設特約店別計画ステータスチェック<br>
	 * ・配分処理実行<br>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param category カテゴリ
	 * @param jgiMstList 配分対象の従業員情報のリスト
	 * @param plannedProdList 配分対象の品目情報のリスト
	 */
	private void doRehaibun(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// -------------------------------
		// 引数チェック
		// -------------------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiMstList == null || jgiMstList.size() == 0) {
			final String errMsg = "配分対象の従業員情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------------
		// 施設特約店別計画〆チェック
		// -------------------------------
		SysManage sysManage = null;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.IYAKU);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理情報が存在しない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		if (sysManage.getWsEndFlg()) {
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3263E")));
		}

//		ProdCategory category = plannedProdList.get(0).getCategory();
		String category = plannedProdList.get(0).getCategory();
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBU品も対象に含める
//		if (category == ProdCategory.MMP || category == ProdCategory.ONC ) {
//		if (category != null && !dpsCodeMasterSearchService.isSiire(category)) {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBU品も対象に含める
		// 計画対象カテゴリ領域を取得
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		try {
			plannedCtg = dpsPlannedCtgDao.search(category);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象カテゴリ領域が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		// 計画立案レベル・担当者=1の場合
		if (plannedCtg.getPlanLevelMr().equals("1")) {
			// -------------------------------
			// 担当者別計画ステータスチェック
			// -------------------------------
			try {
				// 許可しないステータスリスト作成
				List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
				unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);

				// ステータスチェック実行
				dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

			} catch (UnallowableStatusException e) {
				// ステータスエラー
				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3260E", "担当者別計画"));
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
			}
		}

		// -------------------------------------
		//  施設特約店別計画ステータスチェック
		// -------------------------------------
		List<InsWsPlanStatus> insWsPlanStatusList;
		String messageCode = "";
		try {

			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
//			if (category == ProdCategory.MMP) {
//				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
//				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
//				messageCode = "DPS3336E";
//			} else {
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_OPENED);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
				messageCode = "DPS3279E";
			} else {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
				messageCode = "DPS3337E";
			}
//			}

			// ステータスチェック実行
			insWsPlanStatusList = dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(messageCode));
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
				dpsDistributionExecuteService.executeAgain(sosCd3, jgiMst.getJgiNo(), plannedProd.getProdCode(), plannedProd.getCategory(), execDpUser);
			}
		}

		// 再配分終了日時取得
		Date distEndDate = commonDao.getSystemTime();

		// ----------------------
		// ステータス更新
		// ----------------------
		for (InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {
			if (insWsPlanStatus.getSeqKey() == null) {
				try {
					insWsPlanStatus.setAppServerKbn(null);
					insWsPlanStatus.setAsyncBefStatus(null);
					insWsPlanStatus.setAsyncBefDistStartDate(null);
					insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
					insWsPlanStatus.setDistStartDate(distStartDate);
					insWsPlanStatus.setDistEndDate(distEndDate);
					insWsPlanStatusDao.insert(insWsPlanStatus);
				} catch (DuplicateException e) {
					// 既に登録されていた場合、排他エラー
					final String errMsg = "登録済みのため、楽観的ロックエラーとする";
					throw new OptimisticLockingFailureException(errMsg);
				}
			} else {
				insWsPlanStatus.setDistStartDate(distStartDate);
				insWsPlanStatus.setDistEndDate(distEndDate);
				insWsPlanStatusDao.update(insWsPlanStatus);
			}

		}
	}

	/**
	 * 施設特約店別計画確定処理を行う。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doFix(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// -------------------------------------
		// 引数チェック
		// -------------------------------------
		if (sosCd3 == null && sosCd4 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String category = plannedProdList.get(0).getCategory();
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加すると言う意図で、仕入品以外という条件に変更
//		if (category == ProdCategory.MMP || category == ProdCategory.ONC ) {
		// 計画対象カテゴリ領域を取得
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		try {
			plannedCtg = dpsPlannedCtgDao.search(category);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象カテゴリ領域が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
//		if (category != null && !dpsCodeMasterSearchService.isSiire(category)) {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加すると言う意図で、仕入品以外という条件に変更
		// 計画立案レベル・担当者=1の場合
		if (plannedCtg.getPlanLevelMr().equals("1")) {
			// -------------------------------------
			// 担当者別計画ステータスチェック
			// -------------------------------------
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
			try {
				// ステータスチェック実行
				dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

			} catch (UnallowableStatusException e) {
				// ステータスエラー
				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3333E"));
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
			}
		}

		// -------------------------------------
		// 施設特約店別計画ステータスチェック
		// -------------------------------------
		List<InsWsPlanStatus> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
			} else {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
			}

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd4)) {
				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
			} else {
				final String errMsg = "施設特約店別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3335E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		// 確定日時取得
		Date fixDate = commonDao.getSystemTime();
		for (InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_COMPLETED);
			insWsPlanStatus.setInsWsFixDate(fixDate);
			insWsPlanStatusDao.update(insWsPlanStatus);
		}
	}

	/**
	 * 施設特約店別計画確定解除処理を行う。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doFixCancel(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------------
		// 施設特約店別計画〆チェック
		// -------------------------------
		SysManage sysManage = null;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.IYAKU);
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
		String category = plannedProdList.get(0).getCategory();
		// 施設特約店別計画ステータス
		List<InsWsPlanStatus> insWsPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_OPENED);
			} else {
				unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
				unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
			}

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd4)) {
				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insWsPlanStatusList = dpsInsWsStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
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
		for (InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
			if(insWsPlanStatus.getInsWsOpenDate() == null ) {
				insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
			}else {
				insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_OPENED);
			}
			insWsPlanStatusDao.update(insWsPlanStatus);
		}
	}

	// 調整金額用のメッセージを生成
	public String createChoseiMsg(InsWsPlanUpDateResultListDto dto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (dto == null) {
			final String errMsg = "検索結果がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		boolean uhChoseiFlg = false;
		boolean pChoseiFlg = false;

		// 2014年下期向け対応より、重点品も担当者別計画との調整を判定するよう変更
//		if(dto.isPlanLevelInsDoc()){
//
//			Integer jgiNo = dto.getJgiNo();
//			String prodCode = dto.getProdCode();
//			try {
//				Map<String, Object> choseiResult = insPlanDao.checkChoseiInsWs(null, null, jgiNo, prodCode);
//				uhChoseiFlg = (Boolean)choseiResult.get("existDiffUh");
//				pChoseiFlg = (Boolean)choseiResult.get("existDiffP");
//			} catch (DataNotFoundException e) {
//			}
//		} else {

			Long aUh = dto.getUhMrPlanAmount();
			Long aP = dto.getPMrPlanAmount();

			Long tUh = dto.getUhSpecialInsPlanAmount();
			Long tP = dto.getPSpecialInsPlanAmount();

			if (aUh == null) {
				aUh = 0L;
			}
			if (aP == null) {
				aP = 0L;
			}
			if (tUh == null) {
				tUh = 0L;
			}
			if (tP == null) {
				tP = 0L;
			}

			uhChoseiFlg = !(aUh.equals(tUh));
			pChoseiFlg = !(aP.equals(tP));
//		}

		return new CreateChoseiMsgLogic(uhChoseiFlg, pChoseiFlg).createMsg();
	}

}
