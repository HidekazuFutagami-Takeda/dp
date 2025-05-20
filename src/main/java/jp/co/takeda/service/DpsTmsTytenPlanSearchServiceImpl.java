package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DistParamOfficeDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenPlanDistProdDao;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.TmsTytenDistParamResultDto;
import jp.co.takeda.dto.TmsTytenPlanDistProdResultDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.OfficePlanStatusForBranch;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.StatusForOffice;
import jp.co.takeda.model.view.TmsTytenPlanDistProd;
import jp.co.takeda.util.ConvertUtil;

/**
 * 特約店別計画配分対象品目一覧検索サービス実装クラス
 *
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenPlanSearchService")
public class DpsTmsTytenPlanSearchServiceImpl implements DpsTmsTytenPlanSearchService {
	/**
	 * 特約店別計画配分対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenPlanDistProdDao")
	protected TmsTytenPlanDistProdDao tmsTytenPlanDistProdDao;

	/**
	 * 営業所計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusDao")
	protected WsPlanStatusDao wsPlanStatusDao;

	/**
	 * 配分基準（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamOfficeDao")
	protected DistParamOfficeDao distParamOfficeDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	// 特約店別計画配分対象品目一覧を検索する
	public TmsTytenPlanDistProdResultDto searchDistProdList(String sosCd2, String category) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		// 組織コード(支店)のチェック
		if (sosCd2 == null) {
			final String errMsg = "組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// カテゴリーのチェック
		if (category == null) {
			final String errMsg = "カテゴリーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		//ProdCategory prodCategory = ProdCategory.getInstance(category);
//		String  prodCategory = ProdCategory.getInstance(category);
//		String  prodCategory = category;

		// ----------------------
		// 支店組織
		// ----------------------
		SosMst branchSos ;
		try {
			branchSos = sosMstDAO.search(sosCd2);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd2;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

// mod start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//		// ONC組織または、カテゴリがONCの場合（現状ありえない）はエラー
//		if(BooleanUtils.isTrue(branchSos.getOncSosFlg()) || prodCategory == ProdCategory.ONC){
		// 組織カテゴリと選択カテゴリが不一致の場合エラー。　画面のカテゴリ選択プルダウンにMMPと仕入れのみが表示されている仕様のため、この条件だけでONC/SPBU組織は常にエラーとなる。
//		if(!(ProdCategory.getInstance(branchSos.getSosCategory()) == category || ProdCategory.getInstance(branchSos.getSosSubCategory()) == category)) {
//// mod end   2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//			throw new LogicalException(new Conveyance(new MessageKey("DPS3326E")));
//		}

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		if(category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
		}else{
			sales = Sales.IYAKU;
		}

		// ----------------------
		// 特約店別計画配分対象品目検索
		// ----------------------
		List<TmsTytenPlanDistProd> tmsTytenPlanDistProdList;
		try {
			tmsTytenPlanDistProdList = tmsTytenPlanDistProdDao.searchTmsTytenPlanDistProd(TmsTytenPlanDistProdDao.SORT_STRING, sosCd2, category, sales.getDbValue());

		} catch (DataNotFoundException e) {
			// 特約店別計画配分対象品目が存在しない場合
			final String errMsg = "特約店別計画配分対象品目が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 金額を1000円単位に変換
		for (TmsTytenPlanDistProd tmsTytenPlanDistProd : tmsTytenPlanDistProdList) {
			Long sumPlannedValuePY = tmsTytenPlanDistProd.getSumPlannedValuePY();
			Long sumPlannedValueUhY = tmsTytenPlanDistProd.getSumPlannedValueUhY();

			tmsTytenPlanDistProd.setSumPlannedValuePY(ConvertUtil.parseMoneyToThousandUnit(sumPlannedValuePY));
			tmsTytenPlanDistProd.setSumPlannedValueUhY(ConvertUtil.parseMoneyToThousandUnit(sumPlannedValueUhY));
		}

		// ----------------------
		// 営業所確定状態検索
		// ----------------------
		// 営業所の一覧を取得
		List<SosMst> sosMstList;
		try {
			sosMstList = sosMstDAO.searchListFilterBySosCategory(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, sosCd2, category);

		} catch (DataNotFoundException e) {
			// 営業所一覧が存在しない場合
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3326E")));

		}

		// 全営業所の営業所確定状態をチェック
		int completeCount = 0;
		boolean existOncSos = false;
		for (SosMst sosMst : sosMstList) {
			try {
				// 営業所計画ステータスを取得
				OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosMst.getSosCd(), category);
				// 営業所計画ステータスが確定か判定
				if (StatusForOffice.COMPLETED.equals(officePlanStatus.getStatusForOffice())) {
					// 確定の場合は、確定カウンターをインクリメント(何個の営業所が確定しているかを調べる為)
					completeCount++;
				}

			} catch (DataNotFoundException e) {
				// 営業所計画ステータスが存在しない場合
				// 何もしない
			}

// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　同一支店内にMMP組織とONC組織が混在していたときのコードと思われるため削除する
//			// 配下にONC組織があるか
//			if(BooleanUtils.isTrue(sosMst.getOncSosFlg())){
//				existOncSos = true;
//			}
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　同一支店内にMMP組織とONC組織が混在していたときのコードと思われるため削除する
		}

		// 対象営業所数
		int targetOfficeCount = sosMstList.size();
// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　同一支店内にMMP組織とONC組織が混在していたときのコードと思われるため削除する
//		// 仕入品、かつ、ONC組織がある場合は、対象営業所数をマイナス1
//		if(prodCategory == ProdCategory.SHIIRE && existOncSos ){
//			targetOfficeCount = targetOfficeCount - 1;
//		}
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　同一支店内にMMP組織とONC組織が混在していたときのコードと思われるため削除する

		// 支店に紐づく営業所計画確定状態をチェック
		OfficePlanStatusForBranch officePlanStatusForBranch;
		if (completeCount == 0) {
			// 全営業所が確定していない場合
			// 支店に紐づく営業所計画確定状態を確定前に設定
			officePlanStatusForBranch = OfficePlanStatusForBranch.NONE_COMPLETED;

		} else if (completeCount == targetOfficeCount) {
			// 全営業所が確定している場合
			// 支店に紐づく営業所計画確定状態を確定済に設定
			officePlanStatusForBranch = OfficePlanStatusForBranch.COMPLETED;

		} else {
			// 一部の営業所が確定している場合
			// 支店に紐づく営業所計画確定状態を一部確定に設定
			officePlanStatusForBranch = OfficePlanStatusForBranch.PORTION_COMPLETED;

		}

		return new TmsTytenPlanDistProdResultDto(tmsTytenPlanDistProdList, officePlanStatusForBranch);
	}

	// 特約店別計画配分パラメータを取得する
	public TmsTytenDistParamResultDto searchTmsTytenDistParam(String sosCd2, List<String> prodCodeList, String category) {
		// ----------------------
		// 引数チェック
		// ----------------------
		// 組織コード(支店)のチェック
		if (sosCd2 == null) {
			final String errMsg = "組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 品目固定コードリストのチェック
		if (prodCodeList == null) {
			final String errMsg = "品目固定コードリストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (prodCodeList.size() == 0) {
			final String errMsg = "品目固定コードリストが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 支店情報取得
		// ----------------------
		SosMst sosMst = null;
		try {
			sosMst = sosMstDAO.search(sosCd2);

		} catch (DataNotFoundException e) {
			// 組織情報（支店）が存在しない場合
			final String errMsg = "組織情報（支店）が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ----------------------
		// 品目情報取得
		// ----------------------
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (String prodCode : prodCodeList) {
			try {
				PlannedProd plannedProd = plannedProdDAO.search(prodCode);
				plannedProdList.add(plannedProd);

			} catch (DataNotFoundException e) {
				// 計画対象品目が存在しない場合
				final String errMsg = "計画対象品目が存在しない";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}

		// ----------------------
		// 営業所情報取得
		// ----------------------
		List<SosMst> sosMstList;
		try {
			// 営業所一覧検索
//			sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, sosCd2);
			sosMstList = sosMstDAO.searchListFilterBySosCategory(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST,
					BumonRank.SITEN_TOKUYAKUTEN_BU, sosCd2, category);

		} catch (DataNotFoundException e) {
			// 営業所一覧が存在しない場合
			final String errMsg = "営業所一覧が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ----------------------
		// 特約店別計画立案ステータス取得
		// ----------------------
		List<WsPlanStatus> wsPlanStatusListTemp = null;
		try {
			wsPlanStatusListTemp = wsPlanStatusDao.searchList(WsPlanStatusDao.SORT_STRING, sosCd2, prodCodeList);
		} catch (DataNotFoundException e) {
			// 特約店別計画立案ステータスが存在しない場合
			wsPlanStatusListTemp = new ArrayList<WsPlanStatus>();
		}

		// 取得できなかった、特約店別計画立案ステータスを補完する
		List<WsPlanStatus> wsPlanStatusList = new ArrayList<WsPlanStatus>();
		for (String prodCode : prodCodeList) {
			boolean isExist = false;
			for (WsPlanStatus wsPlanStatus : wsPlanStatusListTemp) {
				// 品目コードが一致するか判定
				if (prodCode.equals(wsPlanStatus.getProdCode())) {
					// 品目コードが一致した場合、次の品目チェック
					wsPlanStatusList.add(wsPlanStatus);
					isExist = true;
					break;
				}
			}

			// 品目の特約店別計画立案ステータスが存在したか判定
			if (!isExist) {
				// 存在しない場合補完する。
				WsPlanStatus wsPlanStatus = new WsPlanStatus();
				wsPlanStatus.setSosCd(sosCd2);
				wsPlanStatus.setProdCode(prodCode);
				wsPlanStatusList.add(wsPlanStatus);
			}
		}

		// ----------------------
		// 配分基準取得
		// ----------------------
		List<DistParamOffice> distParamOfficeUHList = null;
		List<DistParamOffice> distParamOfficePList = null;
		try {
			distParamOfficeUHList = distParamOfficeDao.searchWsParamList(DistParamOfficeDao.SORT_STRING2, sosCd2, prodCodeList, InsType.UH, category);
			distParamOfficePList = distParamOfficeDao.searchWsParamList(DistParamOfficeDao.SORT_STRING2, sosCd2, prodCodeList, InsType.P, category);

		} catch (DataNotFoundException e) {
			// 配分基準が存在しない場合
			final String errMsg = "配分基準が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 全品目の配分基準が取得できたか判定
		int distParamOfficeSize = sosMstList.size() * prodCodeList.size();
		if (distParamOfficeUHList.size() != distParamOfficeSize || distParamOfficePList.size() != distParamOfficeSize) {
			// 配分基準が存在しない場合
			StringBuffer sb = new StringBuffer("[組織数×品目数]と取得した配分基準数が一致しない。");
			sb.append("組織数=" + distParamOfficeSize);
			sb.append(",配分基準(UH)数=" + distParamOfficeUHList.size());
			sb.append(",配分基準(P)数=" + distParamOfficePList.size());
			throw new SystemException(new Conveyance(PARAMETER_ERROR, sb.toString()));
		}

		return new TmsTytenDistParamResultDto(sosMst, plannedProdList, sosMstList, wsPlanStatusList, distParamOfficeUHList, distParamOfficePList);
	}
}
