package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ExceptDistInsDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.SpecialInsPlanDeleteDto;
import jp.co.takeda.dto.SpecialInsPlanDto;
import jp.co.takeda.dto.SpecialInsPlanModifyDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForMrDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForOfficeDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.ExDistInsCheckForSpecialInsPlanLogic;
import jp.co.takeda.logic.SpecialInsPlanModifyLogic;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.div.DpsHoInsType;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 特定施設個別計画の更新に関するサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsSpecialInsPlanService")
public class DpsSpecialInsPlanServiceImpl implements DpsSpecialInsPlanService {

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画対象品目DAO
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
	 * 配分除外施設DAO
	 */
	@Autowired(required = true)
	@Qualifier("exceptDistInsDao")
	protected ExceptDistInsDao exceptDistInsDao;

	/**
	 * 医薬用担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 担当者別計画ステータスサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	public void modifyOffice(SpecialInsPlanModifyForOfficeDto modifyDto) throws LogicalException {

		List<SpecialInsPlanDto> SpecialInsPlanDto = modifyDto.getSpecialInsPlanDtoList();
		List<SpecialInsPlan> inputList = new ArrayList<SpecialInsPlan>();
		for (int i = 0; i < modifyDto.getSpecialInsPlanDtoList().size(); i++) {
			SpecialInsPlan specialInsPlan = new SpecialInsPlan();
			specialInsPlan.setJgiNo(SpecialInsPlanDto.get(i).getJgiNo());
			specialInsPlan.setInsNo(SpecialInsPlanDto.get(i).getInsNo());
			specialInsPlan.setProdCode(SpecialInsPlanDto.get(i).getProdCode());
			specialInsPlan.setTmsTytenCd(SpecialInsPlanDto.get(i).getTmsTytenCd());
			specialInsPlan.setPlanType(PlanType.OFFICE);
			specialInsPlan.setPlannedValueY(ConvertUtil.parseMoneyToNormalUnit(SpecialInsPlanDto.get(i).getPlannedValueY()));
			specialInsPlan.setUpDate(modifyDto.getUpDate());
			specialInsPlan.setJgiName(SpecialInsPlanDto.get(i).getJgiName());
			specialInsPlan.setSeqKey(SpecialInsPlanDto.get(i).getSeqKey());
			inputList.add(specialInsPlan);
		}

		SpecialInsPlanModifyDto modifyDtoModel = new SpecialInsPlanModifyDto(modifyDto.getJgiNo(), modifyDto.getInsNo(), inputList, null, modifyDto.getSosCd3(), modifyDto
			.getProdCategory());

		// ----------------------
		// 引数チェック
		// ----------------------
		modifyParamCheck(modifyDtoModel);

		// ----------------------
		// 配分除外チェック
		// ----------------------
		ExDistInsCheckForSpecialInsPlanLogic checkLogic = new ExDistInsCheckForSpecialInsPlanLogic(insMstDAO, plannedProdDAO, exceptDistInsDao, inputList);
		checkLogic.check(PlanType.OFFICE);

		//*** 特定施設個別計画（営業所案）は逆鞘チェックが不要
		//*** 試算前に登録する機能で、担当者別計画がない状態のため
//		// ----------------------
//		// 逆鞘チェック
//		// ----------------------
//		// 品目リスト取得
//		List<PlannedProd> plannedProdList = plannedProdDAO.searchListByPlanLevel(PlannedProdDAO.SORT_STRING, Sales.IYAKU, prodCategory, ProdPlanLevel.INS);
//		List<String> prodNameList = modifyReverseSheathCheck(inputList, modifyDto.getJgiNo(), PlanType.OFFICE, plannedProdList, hoInsType);
//		if (prodNameList.size() > 0) {
//			StringBuilder sb = new StringBuilder();
//			for (String prodName : prodNameList) {
//				sb.append("【" + prodName + "】");
//			}
//			MessageKey messageKey = new MessageKey("DPS3301E", sb.toString());
//			final String errMsg = "担当者別計画と特定施設個別計画の逆ザヤチェックで検証ＮＧ";
//			throw new LogicalException(new Conveyance(messageKey, errMsg));
//		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		checkStatusForEntry(modifyDto.getSosCd3(), modifyDto.getJgiNo());

		// ----------------------
		// 登録処理
		// ----------------------
		// 営業所案・担当者立案を削除する。
		if (modifyDto.getUpDate() != null) {
			specialInsPlanDao.delete(modifyDto.getJgiNo(), modifyDto.getInsNo(), null, modifyDto.getProdCategory(), modifyDto.getUpDate());
		}

		// 営業所案を登録
		SpecialInsPlanModifyLogic modifyLogic = new SpecialInsPlanModifyLogic(specialInsPlanDao, modifyDtoModel, PlanType.OFFICE, modifyDto.getProdCategory());
		try {
			modifyLogic.modify();
		} catch (DuplicateException e) {
			final String errMsg = "特定施設個別計画(営業所案)の削除、登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
		// 営業所案と同じレコードを担当者立案にも登録する

		modifyLogic = new SpecialInsPlanModifyLogic(specialInsPlanDao, modifyDtoModel, PlanType.MR);
		try {
			modifyLogic.modify();
		} catch (DuplicateException e) {
			final String errMsg = "特定施設個別計画(担当者立案)の削除、登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	private void modifyParamCheck(SpecialInsPlanModifyDto modifyDto) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (modifyDto == null) {
			final String errMsg = "特定施設個別計画の登録/更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (modifyDto.getInsNo() == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (modifyDto.getJgiNo() == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (modifyDto.getSpecialInsPlanList() == null || modifyDto.getSpecialInsPlanList().size() == 0) {
			final String errMsg = "特定施設個別計画のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
	}

	/**
	 * 逆鞘チェック
	 *
	 *
	 * @param inputList 特定施設個別計画のリスト
	 * @param jgiNo 施設を担当する従業員番号
	 * @param planType 営業所案or担当者案
	 * @param plannedProdList 品目リスト
	 * @param hoInsType 画面に表示中の対象区分
	 */
	private List<String> modifyReverseSheathCheck(List<SpecialInsPlan> inputList, Integer jgiNo,
			PlanType planType, List<PlannedProd> plannedProdList, DpsHoInsType hoInsType) {

		List<String> prodNameList = new ArrayList<String>();

		for (PlannedProd plannedProd : plannedProdList) {
			String prodCode = plannedProd.getProdCode();
			// 担当者別計画取得(統合済医薬)
			MrPlan mrPlan = null;
			try {
				mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
			} catch (DataNotFoundException e) {
				// 担当者別計画が存在しない場合でも0の計画としてエラーチェックする
				mrPlan = new MrPlan();
			}
			Long plannedValue = 0L;
			//施設の対象区分によって、計画値を取得する
			if(hoInsType.equals(DpsHoInsType.P)) {
				plannedValue = MathUtil.add(mrPlan.getPlannedValuePY(), 0L);
			}else {
				plannedValue = MathUtil.add(mrPlan.getPlannedValueUhY(), 0L);
			}
			// 更新前の登録済み特定施設個別計画取得
			List<SpecialInsPlan> oldSpInsList = new ArrayList<SpecialInsPlan>();
			try {
				oldSpInsList = specialInsPlanDao.searchByJgiNo(null, jgiNo, prodCode, planType);
			} catch (DataNotFoundException e) {
				// 特定施設個別計画が未登録でもエラーにはしない
			}
			// 特定施設個別計画値
			Long spInsValue = 0L;
			// チェック対象フラグ
			boolean chkFlg = false;
			// 入力した特定施設個別計画のサマリー
			for (SpecialInsPlan specialInsPlan : inputList) {
				if (!specialInsPlan.getProdCode().equals(prodCode)) {
					continue;
				}
				// 更新対象の計画は登録済み特定施設個別計画リストから消去
				if (oldSpInsList.contains(specialInsPlan)) {
					oldSpInsList.remove(specialInsPlan);
				}
				spInsValue = MathUtil.add(spInsValue, ConvertUtil.parseMoneyToNormalUnit(specialInsPlan.getPlannedValueY()));
				chkFlg = true;

			}
			// チェック対象外の品目の場合、次品目へ処理移動
			if (!chkFlg) {
				continue;
			}
			// 登録済み特定施設個別計画のサマリー
			for (SpecialInsPlan oldSpecialInsPlan : oldSpInsList) {
				if (!oldSpecialInsPlan.getProdCode().equals(prodCode)) {
					continue;
				}
				spInsValue = MathUtil.add(spInsValue, oldSpecialInsPlan.getPlannedValueY());
			}
			if (spInsValue > plannedValue) {
				prodNameList.add(plannedProd.getProdName());
			}
		}

		return prodNameList;
	}

	public void modifyMr(SpecialInsPlanModifyForMrDto modifyDto, String prodCategory, DpsHoInsType hoInsType) throws LogicalException {
		List<SpecialInsPlanDto> SpecialInsPlanDto = modifyDto.getSpecialInsPlanDtoList();
		List<SpecialInsPlan> inputList = new ArrayList<SpecialInsPlan>();
		for (int i = 0; i < modifyDto.getSpecialInsPlanDtoList().size(); i++) {
			SpecialInsPlan specialInsPlan = new SpecialInsPlan();
			specialInsPlan.setJgiNo(SpecialInsPlanDto.get(i).getJgiNo());
			specialInsPlan.setInsNo(SpecialInsPlanDto.get(i).getInsNo());
			specialInsPlan.setProdCode(SpecialInsPlanDto.get(i).getProdCode());
			specialInsPlan.setTmsTytenCd(SpecialInsPlanDto.get(i).getTmsTytenCd());
			specialInsPlan.setPlanType(PlanType.MR);
			specialInsPlan.setPlannedValueY(ConvertUtil.parseMoneyToNormalUnit(SpecialInsPlanDto.get(i).getPlannedValueY()));
			specialInsPlan.setUpDate(modifyDto.getUpDate());
			specialInsPlan.setJgiName(SpecialInsPlanDto.get(i).getJgiName());
			specialInsPlan.setSeqKey(SpecialInsPlanDto.get(i).getSeqKey());
			inputList.add(specialInsPlan);
		}

		SpecialInsPlanModifyDto modifyDtoModel = new SpecialInsPlanModifyDto(modifyDto.getJgiNo(), modifyDto.getInsNo(), inputList, modifyDto.getUpDate(), modifyDto.getSosCd3(),
			modifyDto.getProdCategory());

		// ----------------------
		// 引数チェック
		// ----------------------
		modifyParamCheck(modifyDtoModel);

		// ----------------------
		// 配分除外チェック
		// ----------------------
		ExDistInsCheckForSpecialInsPlanLogic checkLogic = new ExDistInsCheckForSpecialInsPlanLogic(insMstDAO, plannedProdDAO, exceptDistInsDao, inputList);
		checkLogic.check(PlanType.MR);

		// ----------------------
		// 逆鞘チェック
		// ----------------------
		// 品目リスト取得
		List<PlannedProd> plannedProdList = plannedProdDAO.searchListByPlanLevel(PlannedProdDAO.SORT_STRING, Sales.IYAKU, prodCategory, ProdPlanLevel.INS);
		List<String> prodNameList = modifyReverseSheathCheck(inputList, modifyDto.getJgiNo(), PlanType.MR, plannedProdList, hoInsType);
		if (prodNameList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String prodName : prodNameList) {
				sb.append("【" + prodName + "】");
			}
			MessageKey messageKey = new MessageKey("DPS3301E", sb.toString());
			final String errMsg = "担当者別計画と特定施設個別計画の逆ザヤチェックで検証ＮＧ";
			throw new LogicalException(new Conveyance(messageKey, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		checkStatusForEntry(modifyDto.getSosCd3(), modifyDto.getJgiNo());

		// ----------------------
		// 登録処理
		// ----------------------
		SpecialInsPlanModifyLogic modifyLogic = new SpecialInsPlanModifyLogic(specialInsPlanDao, modifyDtoModel, PlanType.MR, modifyDto.getProdCategory());
		try {
			modifyLogic.modify();
		} catch (DuplicateException e) {
			final String errMsg = "特定施設個別計画(担当者立案)の削除、登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	public void deleteSpecialInsPlan(List<SpecialInsPlanDeleteDto> deleteDtoList, PlanType planType, boolean isVaccine) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (deleteDtoList == null) {
			final String errMsg = "特定施設個別計画の削除用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		checkStatusForDelete(deleteDtoList);

		for (SpecialInsPlanDeleteDto deleteDto : deleteDtoList) {
			// ----------------------
			// DTOチェック
			// ----------------------
			final String insNo = deleteDto.getInsNo();
			if (StringUtils.isEmpty(insNo)) {
				final String errMsg = "施設コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			final Date upDate = deleteDto.getUpDate();
			if (upDate == null) {
				final String errMsg = "最終更新日時がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// ----------------------
			// 削除処理
			// ----------------------
			if (!isVaccine) {
				specialInsPlanDao.delete(deleteDto.getJgiNo(), insNo, planType, null, upDate);
			} else {
				specialInsPlanDao.deleteVaccine(deleteDto.getJgiNo(), insNo, planType, null, upDate);
			}
		}
	}

	public void updateOfficeToMr(List<SpecialInsPlanDeleteDto> deleteDtoList, boolean isVaccine) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (deleteDtoList == null) {
			final String errMsg = "特定施設個別計画の削除用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		checkStatusForDelete(deleteDtoList);

		// ----------------------
		// 更新処理
		// ----------------------
		for (SpecialInsPlanDeleteDto deleteDto : deleteDtoList) {
			final String insNo = deleteDto.getInsNo();
			final Integer jgiNo = deleteDto.getJgiNo();
			final String sosCd3 = deleteDto.getSosCd3();
			final String prodCode = null;
			final PlanType planType = PlanType.OFFICE;
			final String sortString = SpecialInsPlanDao.SORT_STRING2;
			// ----------------------
			// パラメータチェック
			// ----------------------
			if (insNo == null) {
				final String errMsg = "施設コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (jgiNo == null) {
				final String errMsg = "従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (sosCd3 == null) {
				final String errMsg = "組織コード(営業所)がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			// 営業所案を再検索
			List<SpecialInsPlan> specialInsPlanList = null;
			try {
				if (!isVaccine) {
					specialInsPlanList = specialInsPlanDao.searchByInsNo(sortString, jgiNo, insNo, null, prodCode, planType);
				} else {
					specialInsPlanList = specialInsPlanDao.searchByInsNoForVac(sortString, jgiNo, insNo, prodCode, planType);
				}
			} catch (DataNotFoundException e) {
				// 取得件数0件に対応
			}

			// ----------------------
			// 削除処理
			// ----------------------
			// 全データを削除
			if (!isVaccine) {
				specialInsPlanDao.delete(deleteDto.getJgiNo(), insNo, null, null, deleteDto.getUpDate());
			} else {
				specialInsPlanDao.deleteVaccine(deleteDto.getJgiNo(), insNo, null, null, deleteDto.getUpDate());
			}

			// 営業所案/担当者立案として保存
			if (specialInsPlanList != null) {
				SpecialInsPlanModifyDto modifyDto = new SpecialInsPlanModifyDto(jgiNo, insNo, specialInsPlanList, null, sosCd3);
				SpecialInsPlanModifyLogic modifyLogic = new SpecialInsPlanModifyLogic(specialInsPlanDao, modifyDto, PlanType.OFFICE);
				SpecialInsPlanModifyLogic modifyLogic2 = new SpecialInsPlanModifyLogic(specialInsPlanDao, modifyDto, PlanType.MR);
				try {
					modifyLogic.modify();
					modifyLogic2.modify();
				} catch (DuplicateException e) {
					final String errMsg = "特定施設個別計画(担当者立案)の登録に失敗";
					throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
				}
			}
		}
	}

	/**
	 * 登録時用のステータスチェック
	 *
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiNo 従業員番号
	 */
	protected void checkStatusForEntry(String sosCd3, Integer jgiNo) {
		// 従業員情報取得
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		try {
			JgiMst jgiMst = jgiMstDAO.search(jgiNo);
			jgiMstList.add(jgiMst);
		} catch (DataNotFoundException e) {
			final String errMsg = "従業員情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		checkMrStatus(sosCd3, "DPS3238E");
		checkInsWsStatus(jgiMstList, "DPS3239E");
	}

	/**
	 * 削除処理時のステータスチェック
	 *
	 *
	 * @param deleteDtoList 特定施設個別計画の削除用DTOのリスト
	 */
	protected void checkStatusForDelete(List<SpecialInsPlanDeleteDto> deleteDtoList) {
		if (!deleteDtoList.isEmpty()) {
			// 従業員情報取得
			List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
			// チェック済み従業員格納リスト
			List<Integer> jgiNoList = new ArrayList<Integer>();
			for (SpecialInsPlanDeleteDto deleteDto : deleteDtoList) {
				if (!jgiNoList.contains(deleteDto.getJgiNo())) {
					try {
						JgiMst jgiMst = jgiMstDAO.search(deleteDto.getJgiNo());
						jgiMstList.add(jgiMst);
						jgiNoList.add(deleteDto.getJgiNo());
					} catch (DataNotFoundException e) {
						final String errMsg = "従業員情報が存在しない";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
					}
				}
			}
			// チェック実行
			checkMrStatus(deleteDtoList.get(0).getSosCd3(), "DPS3236E");
			checkInsWsStatus(jgiMstList, "DPS3237E");
		}
	}

	/**
	 * 特定施設個別計画変更時の担当者別計画ステータスチェック
	 *
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param msgCode エラーメッセージコード
	 */
	protected void checkMrStatus(String sosCd3, String msgCode) {
		// 全品目がチェック対象
		String category = null;

		// 「試算中」は許可しない
		List<MrStatusForCheck> unallowableStatusList = new ArrayList<MrStatusForCheck>();
		unallowableStatusList.add(MrStatusForCheck.ESTIMATING);
		try {
			dpsMrStatusCheckService.execute(sosCd3, category, unallowableStatusList);
		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(msgCode));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

	}

	/**
	 * 特定施設個別計画変更時の施設特約店別計画ステータスチェック
	 *
	 * @param jgiMstList 従業員情報のリスト
	 * @param msgCode エラーメッセージコード
	 */
	protected void checkInsWsStatus(List<JgiMst> jgiMstList, String msgCode) {
		// 全品目がチェック対象
		String category = null;

		// 「配分中」は許可しない
		List<InsWsStatusForCheck> unallowableInsStatusList = new ArrayList<InsWsStatusForCheck>();
		unallowableInsStatusList.add(InsWsStatusForCheck.DISTING);
		try {
			dpsInsWsStatusCheckService.execute(jgiMstList, category, unallowableInsStatusList);
		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(msgCode));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}
	}
}
