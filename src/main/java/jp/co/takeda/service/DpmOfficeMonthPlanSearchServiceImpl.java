package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.ManageBranchMonthPlanDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageOfficeMonthPlanDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.ProdCategoryDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.ProdMonthPlanResultDetailDto;
import jp.co.takeda.dto.ProdMonthPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosMonthPlanResultDetailDto;
import jp.co.takeda.dto.SosMonthPlanResultDetailTotalDto;
import jp.co.takeda.dto.SosMonthPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.Cal;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.ImplMonthPlan;
import jp.co.takeda.model.ManageBranchMonthPlan;
import jp.co.takeda.model.ManageOfficeMonthPlan;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;

/**
 * 管理の組織別計画(営業所)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmOfficeMonthPlanSearchService")
public class DpmOfficeMonthPlanSearchServiceImpl implements DpmOfficeMonthPlanSearchService {

	/**
	 * 支店別月別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageBranchMonthPlanDao")
	protected ManageBranchMonthPlanDao manageBranchMonthPlanDao;

	/**
	 * 営業所別月別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageOfficeMonthPlanDao")
	protected ManageOfficeMonthPlanDao manageOfficeMonthPlanDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("masterManagePlannedProdDao")
	protected MasterManagePlannedProdDao managePlannedProdDao;

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamYTDao")
	protected ManageChangeParamYTDao manageChangeParamYTDao;

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 計画管理汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("codeMasterDao")
	protected CodeMasterDao codeMasterDao;

	/**
	 * 品目分類DAO
	 */
	@Autowired(required = true)
	@Qualifier("prodCategoryDao")
	protected ProdCategoryDao prodCategoryDao;

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
    /**
     * 管理の共通サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmCommonService")
    protected DpmCommonService dpmCommonService;
 // add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示


	// 組織別計画取得
	public SosMonthPlanResultDto searchSosPlan(SosPlanScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "組織別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getProdCode() == null) {
			final String errMsg = "検索対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd2() == null) {
			final String errMsg = "検索対象の組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		final String prodCode = scDto.getProdCode();
		MasterManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

		// 組織とカテゴリの整合性チェック
		String category = scDto.getProdCategory();
		if (StringUtils.isEmpty(scDto.getSosCategory())) {
			throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR));
		}
		String[] sosCategory = scDto.getSosCategory().split(",");
		if (sosCategory.length != 0) {
			if(!Arrays.asList(sosCategory).contains(category)){
				throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR));
			}
		} else {
			throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR));
		}

		// -----------------------------
		// 品目の立案レベルチェック
		// -----------------------------
		// 計画立案レベル・営業所が設定されていない場合はエラー
		if (!plannedProd.getPlanLevelOffice()) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new LogicalException(new Conveyance(new MessageKey("DPM1001E", plannedProd.getProdName(), "エリア計画")));
//			throw new LogicalException(new Conveyance(new MessageKey("DPM1001E", plannedProd.getProdName(), "営業所計画")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		// -----------------------------
		//当月が当期の何か月目のカウントを取得
		// -----------------------------
		int tougetuCount = TougetuHantei();

		// -----------------------------
		//カテゴリのワクチン判定
		// -----------------------------
		List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();
		boolean vacCheck = category.equals(vaccineCode);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		// -----------------------------
		// 支店別月別計画取得
		// -----------------------------
		final String sosCd2 = scDto.getSosCd2();
		ManageBranchMonthPlan branchPlan;
		try {
			branchPlan = manageBranchMonthPlanDao.searchTotalLine(prodCode, sosCd2);
		} catch (DataNotFoundException e) {
			branchPlan = new ManageBranchMonthPlan();
		}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		if(branchPlan.getImplMonthPlan() == null) {
			branchPlan.setImplPlan(new ImplMonthPlan());
		}

		if(vacCheck) {
			branchPlan.getImplMonthPlan().setRecordTotalValueYb(branchPlan.getImplMonthPlan().getRecordTotalValueB());
			branchPlan.getImplMonthPlan().setRecord1ValueYb(branchPlan.getImplMonthPlan().getRecord1ValueB());
			branchPlan.getImplMonthPlan().setRecord2ValueYb(branchPlan.getImplMonthPlan().getRecord2ValueB());
			branchPlan.getImplMonthPlan().setRecord3ValueYb(branchPlan.getImplMonthPlan().getRecord3ValueB());
			branchPlan.getImplMonthPlan().setRecord4ValueYb(branchPlan.getImplMonthPlan().getRecord4ValueB());
			branchPlan.getImplMonthPlan().setRecord5ValueYb(branchPlan.getImplMonthPlan().getRecord5ValueB());
			branchPlan.getImplMonthPlan().setRecord6ValueYb(branchPlan.getImplMonthPlan().getRecord6ValueB());

		} else {
			branchPlan.getImplMonthPlan().setRecordTotalValueYb(branchPlan.getImplMonthPlan().getRecordTotalValueY());
			branchPlan.getImplMonthPlan().setRecord1ValueYb(branchPlan.getImplMonthPlan().getRecord1ValueY());
			branchPlan.getImplMonthPlan().setRecord2ValueYb(branchPlan.getImplMonthPlan().getRecord2ValueY());
			branchPlan.getImplMonthPlan().setRecord3ValueYb(branchPlan.getImplMonthPlan().getRecord3ValueY());
			branchPlan.getImplMonthPlan().setRecord4ValueYb(branchPlan.getImplMonthPlan().getRecord4ValueY());
			branchPlan.getImplMonthPlan().setRecord5ValueYb(branchPlan.getImplMonthPlan().getRecord5ValueY());
			branchPlan.getImplMonthPlan().setRecord6ValueYb(branchPlan.getImplMonthPlan().getRecord6ValueY());
		}

		switch (tougetuCount){
		case 0:
			branchPlan.getImplMonthPlan().setRecordTougetuValueYb(branchPlan.getImplMonthPlan().getRecord1ValueYb());
			break;
		case 1:
			branchPlan.getImplMonthPlan().setRecordTougetuValueYb(branchPlan.getImplMonthPlan().getRecord2ValueYb());
			break;
		case 2:
			branchPlan.getImplMonthPlan().setRecordTougetuValueYb(branchPlan.getImplMonthPlan().getRecord3ValueYb());
			break;
		case 3:
			branchPlan.getImplMonthPlan().setRecordTougetuValueYb(branchPlan.getImplMonthPlan().getRecord4ValueYb());
			break;
		case 4:
			branchPlan.getImplMonthPlan().setRecordTougetuValueYb(branchPlan.getImplMonthPlan().getRecord5ValueYb());
			break;
		case 5:
			branchPlan.getImplMonthPlan().setRecordTougetuValueYb(branchPlan.getImplMonthPlan().getRecord6ValueYb());
			break;
		default:
			break;
		}
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		// 上位立案(支店計画)に対する品目立案レベル取得
		boolean upperPlanLevel = plannedProd.getPlanLevelSiten();

		// 明細合計行作成(支店計画)
		SosMonthPlanResultDetailTotalDto detailTotal = new SosMonthPlanResultDetailTotalDto(branchPlan, upperPlanLevel);

		// ----------------------
		// 営業所別計画取得
		// ----------------------
		// 6ヶ月分の営業所計画を取得
		List<ManageOfficeMonthPlan> officePlanList = manageOfficeMonthPlanDao.searchListByProd(ManageOfficeMonthPlanDao.SORT_STRING, prodCode, plannedProd.getCategory(), sosCd2);

		// 下位立案(担当者別計画)に対する品目立案レベル取得
		boolean canMovePlanLevel = plannedProd.getPlanLevelMr();

		// 明細行作成(営業所別計画)
		List<SosMonthPlanResultDetailDto> detailList = new ArrayList<SosMonthPlanResultDetailDto>();

		for (ManageOfficeMonthPlan monthPlan : officePlanList) {
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			if(monthPlan.getImplMonthPlan() == null) {
				monthPlan.setImplPlan(new ImplMonthPlan());
			}
			if(vacCheck) {
				monthPlan.getImplMonthPlan().setRecordTotalValueYb(monthPlan.getImplMonthPlan().getRecordTotalValueB());
				monthPlan.getImplMonthPlan().setRecord1ValueYb(monthPlan.getImplMonthPlan().getRecord1ValueB());
				monthPlan.getImplMonthPlan().setRecord2ValueYb(monthPlan.getImplMonthPlan().getRecord2ValueB());
				monthPlan.getImplMonthPlan().setRecord3ValueYb(monthPlan.getImplMonthPlan().getRecord3ValueB());
				monthPlan.getImplMonthPlan().setRecord4ValueYb(monthPlan.getImplMonthPlan().getRecord4ValueB());
				monthPlan.getImplMonthPlan().setRecord5ValueYb(monthPlan.getImplMonthPlan().getRecord5ValueB());
				monthPlan.getImplMonthPlan().setRecord6ValueYb(monthPlan.getImplMonthPlan().getRecord6ValueB());
			} else {
				monthPlan.getImplMonthPlan().setRecordTotalValueYb(monthPlan.getImplMonthPlan().getRecordTotalValueY());
				monthPlan.getImplMonthPlan().setRecord1ValueYb(monthPlan.getImplMonthPlan().getRecord1ValueY());
				monthPlan.getImplMonthPlan().setRecord2ValueYb(monthPlan.getImplMonthPlan().getRecord2ValueY());
				monthPlan.getImplMonthPlan().setRecord3ValueYb(monthPlan.getImplMonthPlan().getRecord3ValueY());
				monthPlan.getImplMonthPlan().setRecord4ValueYb(monthPlan.getImplMonthPlan().getRecord4ValueY());
				monthPlan.getImplMonthPlan().setRecord5ValueYb(monthPlan.getImplMonthPlan().getRecord5ValueY());
				monthPlan.getImplMonthPlan().setRecord6ValueYb(monthPlan.getImplMonthPlan().getRecord6ValueY());
			}


			switch (tougetuCount){
			case 0:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord1ValueYb());
				break;
			case 1:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord2ValueYb());
				break;
			case 2:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord3ValueYb());
				break;
			case 3:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord4ValueYb());
				break;
			case 4:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord5ValueYb());
				break;
			case 5:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord6ValueYb());
				break;
			default:
				break;
			}
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

			SosMonthPlanResultDetailDto detail = new SosMonthPlanResultDetailDto(monthPlan, canMovePlanLevel);
			detailList.add(detail);
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new SosMonthPlanResultDto(detailTotal, detailList);
	}

	// 組織別品目別計画(営業所)取得
	public ProdMonthPlanResultDto searchSosProdPlan(ProdPlanScDto scDto, List<String> jrnsCtgList) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "組織別品目別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getProdCategory() == null) {
			final String errMsg = "検索対象のカテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd3() == null) {
			final String errMsg = "検索対象の組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String category = scDto.getProdCategory();
		final String sosCd3 = scDto.getSosCd3();
		String vaccineCode = scDto.getVaccineCode();

		// リンクからの遷移時はワクチンコードがnullになるので、DBから取得
		if (StringUtils.isEmpty(vaccineCode)) {
			// ワクチンコードの検索
			List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
			try {
				// カテゴリの検索
				searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
			} catch (DataNotFoundException e) {
				final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
			vaccineCode = searchList.get(0).getDataCd();
		}

		// JRNS判定
		List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.JRNS.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.JRNS.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		// JRNSのカテゴリコード
		String jrnsCategory = searchList.get(0).getDataCd();
		// JRNSの品目分類コード
		String jrnsPcatCd = String.valueOf(searchList.get(0).getDataValue());
		// カテゴリがJRNSかどうか
		boolean isJrns = false;
		if (category.equals(jrnsCategory)) {
			isJrns = true;
		}

		// ----------------------
		// 組織とカテゴリの整合性チェック
		// ----------------------
		if (StringUtils.isEmpty(scDto.getSosCategory())) {
			throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR));
		}
		if (!isJrns) {
			String[] sosCategory = scDto.getSosCategory().split(",");
			if (sosCategory.length != 0) {
				if(!Arrays.asList(sosCategory).contains(category)){
					throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR));
				}
			} else {
				throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR));
			}
		}

		// ----------------------
		// 営業所別計画取得
		// ----------------------
		// 6ヶ月分の営業所計画を取得
		List<ManageOfficeMonthPlan> officePlanList = manageOfficeMonthPlanDao.searchListBySos(ManageOfficeMonthPlanDao.SORT_STRING2, sosCd3, category, vaccineCode, isJrns, jrnsPcatCd, jrnsCtgList);

		// CVコードの検索
		searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.CV.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.CV.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		String cvCode = searchList.get(0).getDataCd();

		// 別カテゴリの6ヶ月分の支店計画の合計値を取得
		ManageOfficeMonthPlan otherPlanSum = new ManageOfficeMonthPlan();

		// カテゴリがワクチン・CVの場合は、もう一方の6ヶ月分の全社計画の集計を取得
		if (category.equals(vaccineCode)) {
			try {
				otherPlanSum = manageOfficeMonthPlanDao.searchPlanSum(sosCd3, cvCode, vaccineCode);
			} catch (DataNotFoundException e) {
				// データ０件の場合は何もしない
			}
		} else if (category.equals(cvCode)) {
			try {
				otherPlanSum = manageOfficeMonthPlanDao.searchPlanSum(sosCd3, vaccineCode, vaccineCode);
			} catch (DataNotFoundException e) {
				// データ０件の場合は何もしない
			}
		}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		int tougetuCount = TougetuHantei();

		if(otherPlanSum.getImplMonthPlan() == null) {
			otherPlanSum.setImplPlan(new ImplMonthPlan());
		}

		if(category.equals(vaccineCode)) {
			otherPlanSum.getImplMonthPlan().setRecord1ValueYb(otherPlanSum.getImplMonthPlan().getRecord1ValueB());
			otherPlanSum.getImplMonthPlan().setRecord2ValueYb(otherPlanSum.getImplMonthPlan().getRecord2ValueB());
			otherPlanSum.getImplMonthPlan().setRecord3ValueYb(otherPlanSum.getImplMonthPlan().getRecord3ValueB());
			otherPlanSum.getImplMonthPlan().setRecord4ValueYb(otherPlanSum.getImplMonthPlan().getRecord4ValueB());
			otherPlanSum.getImplMonthPlan().setRecord5ValueYb(otherPlanSum.getImplMonthPlan().getRecord5ValueB());
			otherPlanSum.getImplMonthPlan().setRecord6ValueYb(otherPlanSum.getImplMonthPlan().getRecord6ValueB());
			otherPlanSum.getImplMonthPlan().setRecordTotalValueYb(otherPlanSum.getImplMonthPlan().getRecordTotalValueB());
		} else {
			otherPlanSum.getImplMonthPlan().setRecord1ValueYb(otherPlanSum.getImplMonthPlan().getRecord1ValueY());
			otherPlanSum.getImplMonthPlan().setRecord2ValueYb(otherPlanSum.getImplMonthPlan().getRecord2ValueY());
			otherPlanSum.getImplMonthPlan().setRecord3ValueYb(otherPlanSum.getImplMonthPlan().getRecord3ValueY());
			otherPlanSum.getImplMonthPlan().setRecord4ValueYb(otherPlanSum.getImplMonthPlan().getRecord4ValueY());
			otherPlanSum.getImplMonthPlan().setRecord5ValueYb(otherPlanSum.getImplMonthPlan().getRecord5ValueY());
			otherPlanSum.getImplMonthPlan().setRecord6ValueYb(otherPlanSum.getImplMonthPlan().getRecord6ValueY());
			otherPlanSum.getImplMonthPlan().setRecordTotalValueYb(otherPlanSum.getImplMonthPlan().getRecordTotalValueY());
		}

		switch (tougetuCount) {
		case 0:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord1ValueYb());
			break;
		case 1:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord2ValueYb());
			break;
		case 2:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord3ValueYb());
			break;
		case 3:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord4ValueYb());
			break;
		case 4:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord5ValueYb());
			break;
		case 5:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord6ValueYb());
			break;
		default:
			break;
		}
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		// 計画集計DTO作成
		ProdMonthPlanResultDetailDto otherPlanSumDto = new ProdMonthPlanResultDetailDto(otherPlanSum, Boolean.FALSE);

		// 品目分類名
		String prodCategoryName = prodCategoryDao.searchProdCategoryName(category, isJrns, jrnsPcatCd);

		// 明細行作成
		List<ProdMonthPlanResultDetailDto> detailList = new ArrayList<ProdMonthPlanResultDetailDto>();
		for (ManageOfficeMonthPlan officePlan : officePlanList) {

			final String prodCode = officePlan.getProdCode();

			// ----------------------------------------------------
			// 下位立案(担当者別計画)に対する品目立案レベル取得
			// ----------------------------------------------------
			boolean canMovePlanLevel;
			try {
				MasterManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);
				// 下位計画は担当者別計画
				canMovePlanLevel = plannedProd.getPlanLevelMr();
			} catch (DataNotFoundException e1) {
				final String msg = "営業所別計画取得に失敗(データ不整合)。" + "sosCd3=" + sosCd3 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			if(officePlan.getImplMonthPlan() == null) {
				officePlan.setImplPlan(new ImplMonthPlan());
			}

			if(category.equals(vaccineCode)) {
				officePlan.getImplMonthPlan().setRecordTotalValueYb(officePlan.getImplMonthPlan().getRecordTotalValueB());
				officePlan.getImplMonthPlan().setRecord1ValueYb(officePlan.getImplMonthPlan().getRecord1ValueB());
				officePlan.getImplMonthPlan().setRecord2ValueYb(officePlan.getImplMonthPlan().getRecord2ValueB());
				officePlan.getImplMonthPlan().setRecord3ValueYb(officePlan.getImplMonthPlan().getRecord3ValueB());
				officePlan.getImplMonthPlan().setRecord4ValueYb(officePlan.getImplMonthPlan().getRecord4ValueB());
				officePlan.getImplMonthPlan().setRecord5ValueYb(officePlan.getImplMonthPlan().getRecord5ValueB());
				officePlan.getImplMonthPlan().setRecord6ValueYb(officePlan.getImplMonthPlan().getRecord6ValueB());
			} else {
				officePlan.getImplMonthPlan().setRecordTotalValueYb(officePlan.getImplMonthPlan().getRecordTotalValueY());
				officePlan.getImplMonthPlan().setRecord1ValueYb(officePlan.getImplMonthPlan().getRecord1ValueY());
				officePlan.getImplMonthPlan().setRecord2ValueYb(officePlan.getImplMonthPlan().getRecord2ValueY());
				officePlan.getImplMonthPlan().setRecord3ValueYb(officePlan.getImplMonthPlan().getRecord3ValueY());
				officePlan.getImplMonthPlan().setRecord4ValueYb(officePlan.getImplMonthPlan().getRecord4ValueY());
				officePlan.getImplMonthPlan().setRecord5ValueYb(officePlan.getImplMonthPlan().getRecord5ValueY());
				officePlan.getImplMonthPlan().setRecord6ValueYb(officePlan.getImplMonthPlan().getRecord6ValueY());
			}

			switch (tougetuCount){
			case 0:
				officePlan.getImplMonthPlan().setRecordTougetuValueYb(officePlan.getImplMonthPlan().getRecord1ValueYb());
				break;
			case 1:
				officePlan.getImplMonthPlan().setRecordTougetuValueYb(officePlan.getImplMonthPlan().getRecord2ValueYb());
				break;
			case 2:
				officePlan.getImplMonthPlan().setRecordTougetuValueYb(officePlan.getImplMonthPlan().getRecord3ValueYb());
				break;
			case 3:
				officePlan.getImplMonthPlan().setRecordTougetuValueYb(officePlan.getImplMonthPlan().getRecord4ValueYb());
				break;
			case 4:
				officePlan.getImplMonthPlan().setRecordTougetuValueYb(officePlan.getImplMonthPlan().getRecord5ValueYb());
				break;
			case 5:
				officePlan.getImplMonthPlan().setRecordTougetuValueYb(officePlan.getImplMonthPlan().getRecord6ValueYb());
				break;
			default:
				break;
			}
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

			// DTO作成
			ProdMonthPlanResultDetailDto detail = new ProdMonthPlanResultDetailDto(officePlan, canMovePlanLevel);
			detailList.add(detail);
		}

		return new ProdMonthPlanResultDto(prodCategoryName, otherPlanSumDto, detailList);
	}

//  add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	private int TougetuHantei() {
		// カレンダーから今期の何月目かを設定（falseCountに何月目かを記録（1月目なら0が入る）
		Cal today = dpmCommonService.searchToday();
		String calYear = today.getCalYear();
		String calMonth = today.getCalMonth();

		int calYearInt = Integer.parseInt(calYear);
		int calMonthInt = Integer.parseInt(calMonth);

		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		String sysYear = sysManage.getSysYear();
		int sysYearInt = Integer.parseInt(sysYear);
		Term sysTerm = sysManage.getSysTerm();

		int falseCount = 6;

		switch (sysTerm) {
		case FIRST: // 4,5,6,7,8,9
			if(!sysYear.equals(calYear)) {
				break;
			}
			falseCount = calMonthInt - 4;
			break;
		case SECOND:// 10,11,12,1,2,3
			Integer.parseInt(sysYear);
			if(calYearInt == sysYearInt || calYearInt == sysYearInt + 1) {
				/* do nothing */
			}else {
				break;
			}
			if (calMonthInt < 10) calMonthInt = calMonthInt + 6;
			falseCount = calMonthInt - 10;
			break;
		default:
			break;
		}
		return falseCount;
	}
//  add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

}
