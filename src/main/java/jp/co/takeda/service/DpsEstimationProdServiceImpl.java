package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.EstParamOfficeDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanFreeIndexDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.dto.EstimationParamUpdateDto;
import jp.co.takeda.dto.FreeIndexDto;
import jp.co.takeda.dto.FreeIndexUpdateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.EstimationBasePlan;
import jp.co.takeda.model.div.StatusForMrPlan;

/**
 * 試算機能の実行処理に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsEstimationProdService")
public class DpsEstimationProdServiceImpl implements DpsEstimationProdService {

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 営業所計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 担当者別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 試算パラメータ（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("estParamOfficeDao")
	protected EstParamOfficeDao estParamOfficeDao;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 組織情報(取込)DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * フリー項目DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanFreeIndexDao")
	protected MrPlanFreeIndexDao mrPlanFreeIndexDao;

	/**
	 * 営業所ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsOfficeStatusCheckService")
	protected DpsOfficeStatusCheckService dpsOfficeStatusCheckService;

	/**
	 * 担当者別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 試算実行バッチサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationBatchService")
	protected DpsEstimationBatchService dpsEstimationBatchService;

	/**
	 * 計画支援の計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	// 試算処理実行 前処理
	// mod Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
	//public List<MrPlanStatus> estimationPreparation(String sosCd, List<EstimationExecOrgDto> estimationExecDtoList, CalcType calcType, String appServerKbn) {
	public List<MrPlanStatus> estimationPreparation(String sosCd, List<EstimationExecOrgDto> estimationExecDtoList, CalcType calcType, String appServerKbn, String categorySearch, String categorySelect) {
	// mod end 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationExecDtoList == null || estimationExecDtoList.size() == 0) {
			final String errMsg = "試算実行DTOがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (EstimationExecOrgDto estimationExecDto : estimationExecDtoList) {
			if (estimationExecDto.getProdCode() == null) {
				final String errMsg = "試算実行対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (estimationExecDto.getProdName() == null) {
				final String errMsg = "試算実行対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// 組織情報
		// ----------------------
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd);

		} catch (DataNotFoundException e) {
			final String errMsg = "対象営業所が取得できない：" + sosCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// カテゴリを品目コードから取得
		String category = null;
		try {
			category = plannedProdDAO.search(estimationExecDtoList.get(0).getProdCode()).getCategory();
		} catch (DataNotFoundException e) {
			final String errMsg = "指定された品目のカテゴリが取得できない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + estimationExecDtoList.get(0).getProdCode()), e);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 営業所計画ステータスチェック(確定していないとだめ)
		OfficePlanStatus officePlanStatus = null;
		try {

			// 許可しないステータスリスト作成
			List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
			unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
			unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);

			// チェック実行
			List<OfficePlanStatus> officePlanStatusList = dpsOfficeStatusCheckService.executeForOffice(sosMst, category, unallowableOfficeStatusList);
			officePlanStatus = officePlanStatusList.get(0);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3210E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// 担当者別計画ステータスチェック
		List<MrPlanStatus> orgMrPlanStatusList = null;
		try {
			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();

			// 試算タイプによって、ステータスチェックを変える
			switch (calcType) {
				// 試算中、チーム公開以降はだめ
				// ただし、更新前の試算タイプが「営⇒担」の場合、チーム公開・確定は許可する。
				case OFFICE_TEAM_MR:
					if (officePlanStatus.getCalcType() != null && officePlanStatus.getCalcType().equals(CalcType.OFFICE_MR)) {
						unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
						unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
						unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
						unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
					} else {
						unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
						unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
						unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
						unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
						unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
						unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
					}
					break;
				// 試算中、担当者別公開以降はだめ
				case OFFICE_MR:
					unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
					unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
					unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
					unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
					break;
			}

			// チェック対象の品目情報作成
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			for (EstimationExecOrgDto estimationExecDto : estimationExecDtoList) {

				PlannedProd plannedProd = new PlannedProd();
				plannedProd.setProdCode(estimationExecDto.getProdCode());
				plannedProd.setProdName(estimationExecDto.getProdName());
				plannedProdList.add(plannedProd);
			}

			// チェック実行
			dpsMrStatusCheckService.execute(sosCd, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3211E"));
			// add Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
			if (!categorySearch.equals(categorySelect)) {
				messageKeyList.add(new MessageKey("DPS3214E"));
			}
			// add end 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------

		// 営業所計画ステータスへ試算タイプを設定
		officePlanStatus.setCalcType(calcType);
		officePlanStatusDao.update(officePlanStatus);

		// 試算中に更新、試算開始日時を設定、試算終了日時を初期化

		Date estStartDate = commonDao.getSystemTime();
		try {
			for (EstimationExecOrgDto estimationExecOrgDto : estimationExecDtoList) {
				MrPlanStatus orgMrPlanStatus = estimationExecOrgDto.getMrPlanStatus();
				MrPlanStatus mrPlanStatus = orgMrPlanStatus.clone();
				if (mrPlanStatus.getSeqKey() == null) {
					mrPlanStatus.setAppServerKbn(appServerKbn);
					mrPlanStatus.setAsyncBefStatus(null);
					mrPlanStatus.setAsyncBefEstStartDate(null);
					mrPlanStatus.setEstimationBasePlan(EstimationBasePlan.OFFICE_PLAN);
					mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.ESTIMATING);
					mrPlanStatus.setEstStartDate(estStartDate);
					mrPlanStatus.setEstEndDate(null);
					mrPlanStatusDao.insert(mrPlanStatus);
				} else {
					mrPlanStatus.setAppServerKbn(appServerKbn);
					mrPlanStatus.setAsyncBefStatus(orgMrPlanStatus.getStatusForMrPlan());
					mrPlanStatus.setAsyncBefEstStartDate(orgMrPlanStatus.getEstStartDate());
					mrPlanStatus.setEstimationBasePlan(EstimationBasePlan.OFFICE_PLAN);
					mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.ESTIMATING);
					mrPlanStatus.setEstStartDate(estStartDate);
					mrPlanStatus.setEstEndDate(null);
					mrPlanStatusDao.update(mrPlanStatus);
				}
			}
		} catch (DuplicateException e) {
			final String errMsg = "担当者別計画立案ステータス登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}

		// ----------------------
		// 担当者情報チェック
		// ----------------------
		// 営業所配下の従業員取得
		try {
			jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
		} catch (DataNotFoundException e) {
			final String errMsg = "指定された営業所配下の担当者がいない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd), e);
		}


		return orgMrPlanStatusList;
	}

	// 試算パラメータ 営業所案削除
	public void deleteEstParamOffice(EstimationParamUpdateDto estimationParamUpdateDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (estimationParamUpdateDto == null) {
			final String errMsg = "試算パラメータ更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice() == null) {
			final String errMsg = "試算パラメータ　営業所案がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice().getSosCd() == null) {
			final String errMsg = "試算パラメータ　営業所案がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice().getProdCode() == null) {
			final String errMsg = "試算パラメータ　営業所案がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String sosCd = estimationParamUpdateDto.getEstParamOffice().getSosCd();
		String prodCode = estimationParamUpdateDto.getEstParamOffice().getProdCode();
		String prodName = estimationParamUpdateDto.getProdName();

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 担当者別計画ステータスチェック(試算中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);

			// チェック対象の品目情報作成
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(prodCode);
			plannedProd.setProdName(prodName);
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(plannedProd);

			// チェック実行、現在のステータス情報取得
			dpsMrStatusCheckService.execute(sosCd, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3212E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 営業所計画削除
		// ----------------------
		EstParamOffice estParamOffice = estimationParamUpdateDto.getEstParamOffice();
		estParamOfficeDao.delete(estParamOffice.getSeqKey(), estParamOffice.getUpDate());
	}

	// 試算パラメータ 営業所案登録・更新
	public void updateEstParamOffice(EstimationParamUpdateDto estimationParamUpdateDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (estimationParamUpdateDto == null) {
			final String errMsg = "試算パラメータ更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice() == null) {
			final String errMsg = "試算パラメータ　営業所案がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice().getSosCd() == null) {
			final String errMsg = "試算パラメータ　営業所案がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice().getProdCode() == null) {
			final String errMsg = "試算パラメータ　品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String sosCd = estimationParamUpdateDto.getEstParamOffice().getSosCd();
		String prodCode = estimationParamUpdateDto.getEstParamOffice().getProdCode();
		String prodName = estimationParamUpdateDto.getProdName();

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 担当者別計画ステータスチェック(試算中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);

			// チェック対象の品目情報作成
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(prodCode);
			plannedProd.setProdName(prodName);
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(plannedProd);

			// チェック実行、現在のステータス情報取得
			dpsMrStatusCheckService.execute(sosCd, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3212E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 営業所計画更新
		// ----------------------
		EstParamOffice estParamOffice = estimationParamUpdateDto.getEstParamOffice();
		try {
			if (estParamOffice.getSeqKey() != null) {
				estParamOfficeDao.update(estParamOffice);
			} else {
				estParamOfficeDao.insert(estParamOffice);
			}
		} catch (DuplicateException e) {
			final String errMsg = "試算パラメータ(営業所案)の登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	// フリー項目登録・更新
	public void updateIndexFree(String sosCd, FreeIndexUpdateDto freeIndexUpdateDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (freeIndexUpdateDto == null) {
			final String errMsg = "フリー項目更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		String prodCode = freeIndexUpdateDto.getProdCode();
		String prodName = freeIndexUpdateDto.getProdName();

		// 担当者別計画ステータスチェック(試算中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);

			// チェック対象の品目情報作成
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(prodCode);
			plannedProd.setProdName(prodName);
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(plannedProd);

			// チェック実行、現在のステータス情報取得
			dpsMrStatusCheckService.execute(sosCd, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3213E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// フリー項目更新
		// ----------------------
		List<FreeIndexDto> freeIndexDtoList = freeIndexUpdateDto.getUpdateFreeIndexDtoList();
		for (FreeIndexDto freeIndexDto : freeIndexDtoList) {
			try {
				if (freeIndexDto.getSeqKey() != null) {
					mrPlanFreeIndexDao.update(freeIndexDto.getMrPlanFreeIndex());
				} else {
					mrPlanFreeIndexDao.insert(freeIndexDto.getMrPlanFreeIndex());
				}
			} catch (DuplicateException e) {
				final String errMsg = "フリー項目の登録に失敗";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
			}
		}
	}
}
