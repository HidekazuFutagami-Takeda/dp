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
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SpecialInsPlanForVacDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.SpecialInsPlanDeleteDto;
import jp.co.takeda.dto.SpecialInsPlanForVacDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForVacDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.ExDistInsCheckForSpecialInsPlanVacLogic;
import jp.co.takeda.logic.SpecialInsPlanModifyForVacLogic;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DpsHoInsType;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * ワクチン用特定施設個別計画の関するサービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsSpecialInsPlanForVacService")
public class DpsSpecialInsPlanForVacServiceImpl implements DpsSpecialInsPlanForVacService {

	/**
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanForVacDao")
	protected SpecialInsPlanForVacDao specialInsPlanForVacDao;

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
	 * 担当者別計画ステータスサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckForVacService")
	protected DpsInsWsStatusCheckForVacService dpsInsWsStatusCheckForVacService;

	/**
	 * ワクチン用担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanForVacDao")
	protected MrPlanForVacDao mrPlanForVacDao;

	/**
	 * 医薬用担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 配分除外施設DAO
	 */
	@Autowired(required = true)
	@Qualifier("exceptDistInsDao")
	protected ExceptDistInsDao exceptDistInsDao;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// ワクチン用特定施設個別計画の削除用DTOを元に特定施設個別計画を削除する。
	public void deleteSpecialInsPlanForVac(List<SpecialInsPlanDeleteDto> deleteDtoList, PlanType planType) {

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
		// 削除処理
		// ----------------------
		for (SpecialInsPlanDeleteDto deleteDto : deleteDtoList) {

			final Integer jgiNo = deleteDto.getJgiNo();
			if (jgiNo == null) {
				final String errMsg = "従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
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
			specialInsPlanForVacDao.delete(jgiNo, insNo, planType, upDate);
		}
	}

	// ワクチン用特定施設個別計画の削除用DTOを元に特定施設個別計画を登録する。
	public void modifyVac(SpecialInsPlanModifyForVacDto modifyDto, PlanType planType, DpsHoInsType hoInsType) throws LogicalException {

		List<SpecialInsPlanForVacDto> SpecialInsPlanDto = modifyDto.getSpecialInsPlanDtoList();
		List<SpecialInsPlanForVac> inputList = new ArrayList<SpecialInsPlanForVac>();
		for (int i = 0; i < modifyDto.getSpecialInsPlanDtoList().size(); i++) {
			SpecialInsPlanForVac specialInsPlan = new SpecialInsPlanForVac();
			specialInsPlan.setJgiNo(SpecialInsPlanDto.get(i).getJgiNo());
			specialInsPlan.setInsNo(SpecialInsPlanDto.get(i).getInsNo());
			specialInsPlan.setProdCode(SpecialInsPlanDto.get(i).getProdCode());
			specialInsPlan.setTmsTytenCd(SpecialInsPlanDto.get(i).getTmsTytenCd());
			specialInsPlan.setPlanType(planType);
			specialInsPlan.setPlannedValueB(SpecialInsPlanDto.get(i).getPlannedValueB());
			specialInsPlan.setPlannedValueB2(SpecialInsPlanDto.get(i).getPlannedValueB2());
			specialInsPlan.setUpDate(modifyDto.getUpDate());
			specialInsPlan.setJgiName(SpecialInsPlanDto.get(i).getJgiName());
			inputList.add(specialInsPlan);
		}

		// ----------------------
		// 引数チェック
		// ----------------------
		modifyParamCheck(modifyDto);

		// ----------------------
		// 配分除外チェック
		// ----------------------
		ExDistInsCheckForSpecialInsPlanVacLogic checkLogic = new ExDistInsCheckForSpecialInsPlanVacLogic(insMstDAO,
				plannedProdDAO, exceptDistInsDao, inputList);
		checkLogic.check(planType);

		// ----------------------
		// 逆鞘チェック
		// ----------------------
		if (planType.equals(PlanType.OFFICE)) {
			//*** 特定施設個別計画（営業所案）は逆鞘チェックが不要
			//*** 試算前に登録する機能で、担当者別計画がない状態のため
		}else {
			List<String> prodNameList = new ArrayList<String>();

			// ワクチンコードの取得
			String vaccineCd = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();

			// 品目リスト取得
			List<PlannedProd> plannedProdList = plannedProdDAO.searchListByPlanLevel(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, vaccineCd, ProdPlanLevel.INS);
			for (PlannedProd plannedProd : plannedProdList) {
				String prodCode = plannedProd.getProdCode();
//				// 担当者別計画取得
//				MrPlanForVac mrPlanForVac = null;
//				try {
//					mrPlanForVac = mrPlanForVacDao.searchUk(modifyDto.getJgiNo(), prodCode);
//				} catch (DataNotFoundException e) {
//					// 担当者別計画が存在しない場合でも0の計画としてエラーチェックする
//					mrPlanForVac = new MrPlanForVac();
//				}
//				Long plannedValue = MathUtil.add(mrPlanForVac.getPlannedValueB(), 0L);
				// 担当者別計画取得(統合済ワクチン)
				MrPlan mrPlan = null;
				try {
					mrPlan = mrPlanDao.searchUk(modifyDto.getJgiNo(), prodCode);
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
				List<SpecialInsPlanForVac> oldSpInsList = new ArrayList<SpecialInsPlanForVac>();
				try {
					oldSpInsList = specialInsPlanForVacDao.searchByJgiNo(null, modifyDto.getJgiNo(), prodCode, planType);
				} catch (DataNotFoundException e) {
					// 特定施設個別計画が未登録でもエラーにはしない
				}
				// 特定施設個別計画値
				Long spInsValue = 0L;
				// チェック対象フラグ
				boolean chkFlg = false;
				// 入力した特定施設個別計画のサマリー
				for (SpecialInsPlanForVac specialInsPlan : inputList) {
					if (!specialInsPlan.getProdCode().equals(prodCode)) {
						continue;
					}
					// 更新対象の計画は登録済み特定施設個別計画リストから消去
					if (oldSpInsList.contains(specialInsPlan)) {
						oldSpInsList.remove(specialInsPlan);
					}
					spInsValue = MathUtil.add(spInsValue, ConvertUtil.parseMoneyToNormalUnit(specialInsPlan.getPlannedValueB()));
					chkFlg = true;

				}
				// チェック対象外の品目の場合、次品目へ処理移動
				if (!chkFlg) {
					continue;
				}
				// 登録済み特定施設個別計画のサマリー
				for (SpecialInsPlanForVac oldSpecialInsPlan : oldSpInsList) {
					if (!oldSpecialInsPlan.getProdCode().equals(prodCode)) {
						continue;
					}
					spInsValue = MathUtil.add(spInsValue, oldSpecialInsPlan.getPlannedValueB());
				}
				if (spInsValue > plannedValue) {
					prodNameList.add(plannedProd.getProdName());
				}
			}
			if (prodNameList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (String prodName : prodNameList) {
					sb.append("【" + prodName + "】");
				}
				MessageKey messageKey = new MessageKey("DPS3301E", sb.toString());
				final String errMsg = "担当者別計画と特定施設個別計画の逆ザヤチェックで検証ＮＧ";
				throw new LogicalException(new Conveyance(messageKey, errMsg));
			}
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		checkStatusForEntry(modifyDto.getJgiNo(), modifyDto.getSosCd3());

		// ----------------------
		// 登録処理
		// ----------------------
		SpecialInsPlanModifyForVacLogic modifyLogic;

		if (planType == PlanType.OFFICE) {
			// 営業所案の場合、営業所案を登録する
			modifyLogic = new SpecialInsPlanModifyForVacLogic(specialInsPlanForVacDao, modifyDto, PlanType.OFFICE);
			try {
				modifyLogic.modify();
			} catch (DuplicateException e) {
				final String errMsg = "特定施設個別計画(営業所案)の削除、登録に失敗";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
			}
		}

		// 担当者立案を登録する
		modifyLogic = new SpecialInsPlanModifyForVacLogic(specialInsPlanForVacDao, modifyDto, PlanType.MR);
		try {
			modifyLogic.modify();
		} catch (DuplicateException e) {
			final String errMsg = "特定施設個別計画(担当者立案)の削除、登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	/**
	 * 引数を検証する。
	 *
	 * @param modifyDto 引数
	 */
	private void modifyParamCheck(SpecialInsPlanModifyForVacDto modifyDto) {
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
		if (modifyDto.getSpecialInsPlanDtoList() == null || modifyDto.getSpecialInsPlanDtoList().size() == 0) {
			final String errMsg = "特定施設個別計画のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
	}

	/**
	 * 登録時用のステータスチェック
	 *
	 * @param jgiNo 従業員番号
	 * @param sosCd3 営業所コード
	 */
	protected void checkStatusForEntry(Integer jgiNo, String sosCd3) {
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

		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, null, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "特定施設個別計画の削除対象品目が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// 「配分中」は許可しない
		List<InsWsStatusForCheck> unallowableInsStatusList = new ArrayList<InsWsStatusForCheck>();
		unallowableInsStatusList.add(InsWsStatusForCheck.DISTING);
		try {
			dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsStatusList);
		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(msgCode));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}
	}
}
