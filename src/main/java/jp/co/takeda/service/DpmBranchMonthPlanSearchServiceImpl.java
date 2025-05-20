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
import jp.co.takeda.dao.ManageIyakuMonthPlanDao;
import jp.co.takeda.dao.ManageOfficeMonthPlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
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
import jp.co.takeda.model.ManageIyakuMonthPlan;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;

/**
 * 管理の組織別計画(支店)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmBranchMonthPlanSearchService")
public class DpmBranchMonthPlanSearchServiceImpl implements DpmBranchMonthPlanSearchService {

	/**
	 * 全社計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageIyakuMonthPlanDao")
	protected ManageIyakuMonthPlanDao manageIyakuMonthPlanDao;

	/**
	 * 支店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageBranchMonthPlanDao")
	protected ManageBranchMonthPlanDao manageBranchMonthPlanDao;

	/**
	 * 営業所別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageOfficeMonthPlanDao")
	protected ManageOfficeMonthPlanDao manageOfficeMonthPlanDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	protected ManagePlannedProdDao managePlannedProdDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("masterManagePlannedProdDao")
	protected MasterManagePlannedProdDao masterManagePlannedProdDao;

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

	// 組織別月別計画(支店)取得
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

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		final String prodCode = scDto.getProdCode();
		MasterManagePlannedProd plannedProd = masterManagePlannedProdDao.search(prodCode);

		// -----------------------------
		// 品目の立案レベルチェック
		// -----------------------------
		// 計画立案レベル・支店が設定されていない場合はエラー
		if (!plannedProd.getPlanLevelSiten()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1001E", plannedProd.getProdName(), "支店計画")));
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
		boolean vacCheck = plannedProd.getCategory().equals(vaccineCode);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		// -----------------------------
		// 全社月別計画取得
		// -----------------------------
		ManageIyakuMonthPlan iyakuPlan;
		try {
			iyakuPlan = manageIyakuMonthPlanDao.searchTotalLine(prodCode);
		} catch (DataNotFoundException e) {
			iyakuPlan = new ManageIyakuMonthPlan();
		}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		if(iyakuPlan.getImplMonthPlan() == null) {
			iyakuPlan.setImplMonthPlan(new ImplMonthPlan());
		}

		if(vacCheck) {
			iyakuPlan.getImplMonthPlan().setRecordTotalValueYb(iyakuPlan.getImplMonthPlan().getRecordTotalValueB());
			iyakuPlan.getImplMonthPlan().setRecord1ValueYb(iyakuPlan.getImplMonthPlan().getRecord1ValueB());
			iyakuPlan.getImplMonthPlan().setRecord2ValueYb(iyakuPlan.getImplMonthPlan().getRecord2ValueB());
			iyakuPlan.getImplMonthPlan().setRecord3ValueYb(iyakuPlan.getImplMonthPlan().getRecord3ValueB());
			iyakuPlan.getImplMonthPlan().setRecord4ValueYb(iyakuPlan.getImplMonthPlan().getRecord4ValueB());
			iyakuPlan.getImplMonthPlan().setRecord5ValueYb(iyakuPlan.getImplMonthPlan().getRecord5ValueB());
			iyakuPlan.getImplMonthPlan().setRecord6ValueYb(iyakuPlan.getImplMonthPlan().getRecord6ValueB());

		} else {
			iyakuPlan.getImplMonthPlan().setRecordTotalValueYb(iyakuPlan.getImplMonthPlan().getRecordTotalValueY());
			iyakuPlan.getImplMonthPlan().setRecord1ValueYb(iyakuPlan.getImplMonthPlan().getRecord1ValueY());
			iyakuPlan.getImplMonthPlan().setRecord2ValueYb(iyakuPlan.getImplMonthPlan().getRecord2ValueY());
			iyakuPlan.getImplMonthPlan().setRecord3ValueYb(iyakuPlan.getImplMonthPlan().getRecord3ValueY());
			iyakuPlan.getImplMonthPlan().setRecord4ValueYb(iyakuPlan.getImplMonthPlan().getRecord4ValueY());
			iyakuPlan.getImplMonthPlan().setRecord5ValueYb(iyakuPlan.getImplMonthPlan().getRecord5ValueY());
			iyakuPlan.getImplMonthPlan().setRecord6ValueYb(iyakuPlan.getImplMonthPlan().getRecord6ValueY());
		}

		switch (tougetuCount){
		case 0:
			iyakuPlan.getImplMonthPlan().setRecordTougetuValueYb(iyakuPlan.getImplMonthPlan().getRecord1ValueYb());
			break;
		case 1:
			iyakuPlan.getImplMonthPlan().setRecordTougetuValueYb(iyakuPlan.getImplMonthPlan().getRecord2ValueYb());
			break;
		case 2:
			iyakuPlan.getImplMonthPlan().setRecordTougetuValueYb(iyakuPlan.getImplMonthPlan().getRecord3ValueYb());
			break;
		case 3:
			iyakuPlan.getImplMonthPlan().setRecordTougetuValueYb(iyakuPlan.getImplMonthPlan().getRecord4ValueYb());
			break;
		case 4:
			iyakuPlan.getImplMonthPlan().setRecordTougetuValueYb(iyakuPlan.getImplMonthPlan().getRecord5ValueYb());
			break;
		case 5:
			iyakuPlan.getImplMonthPlan().setRecordTougetuValueYb(iyakuPlan.getImplMonthPlan().getRecord6ValueYb());
			break;
		default:
			break;
		}
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		// 上位立案(全社計画)に対する品目立案レベル取得
		boolean upperPlanLevel = plannedProd.getPlanLevelZensha();

		// 明細合計行作成(全社計画)
		SosMonthPlanResultDetailTotalDto detailTotal = new SosMonthPlanResultDetailTotalDto(iyakuPlan, upperPlanLevel);

		// ----------------------
		// 支店別計画取得
		// ----------------------
		// 6ヶ月分の支店計画を取得
		List<ManageBranchMonthPlan> branchPlanList = manageBranchMonthPlanDao.searchListByProd(ManageBranchMonthPlanDao.SORT_STRING, prodCode, plannedProd.getCategory());

		// 下位立案(営業所別計画)に対する品目立案レベル取得
		boolean canMovePlanLevel = plannedProd.getPlanLevelOffice();

		// 明細行作成(支店別計画)
		List<SosMonthPlanResultDetailDto> detailList = new ArrayList<SosMonthPlanResultDetailDto>();

		for (ManageBranchMonthPlan branchPlan : branchPlanList) {

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

			SosMonthPlanResultDetailDto detail = new SosMonthPlanResultDetailDto(branchPlan, canMovePlanLevel);
			detailList.add(detail);
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new SosMonthPlanResultDto(detailTotal, detailList);
	}

	// 組織別品目別計画(支店)取得
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
		if (scDto.getSosCd2() == null) {
			final String errMsg = "検索対象の組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String category = scDto.getProdCategory();
		final String sosCd2 = scDto.getSosCd2();

		// JRNS判定
		List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.JRNS.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
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
		// 支店別計画取得
		// ----------------------
		// 6ヶ月分の支店計画を取得
		List<ManageBranchMonthPlan> branchPlanList = manageBranchMonthPlanDao.searchListBySos(ManageBranchMonthPlanDao.SORT_STRING2, sosCd2, category, isJrns, jrnsPcatCd, jrnsCtgList);

		// ワクチンコードの検索
		searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		String vaccineCode = searchList.get(0).getDataCd();

		// CVコードの検索
		searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.CV.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		String cvCode = searchList.get(0).getDataCd();

		// 別カテゴリの6ヶ月分の支店計画の合計値を取得
		ManageBranchMonthPlan otherPlanSum = new ManageBranchMonthPlan();

		if (category.equals(vaccineCode)) {
			try {
				otherPlanSum = manageBranchMonthPlanDao.searchPlanSum(sosCd2, cvCode);
			} catch (DataNotFoundException e) {
				// データ０件の場合は何もしない
			}
		} else if (category.equals(cvCode)) {
			try {
				otherPlanSum = manageBranchMonthPlanDao.searchPlanSum(sosCd2, vaccineCode);
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

		for (ManageBranchMonthPlan branchPlan : branchPlanList) {

			final String prodCode = branchPlan.getProdCode();

			// ----------------------------------------------------
			// 下位立案(営業所別計画)に対する品目立案レベル取得
			// ----------------------------------------------------
			boolean canMovePlanLevel;
			try {
				ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);
				canMovePlanLevel = plannedProd.getPlanLevelOffice();
			} catch (DataNotFoundException e1) {
				final String msg = "支店別計画取得に失敗(データ不整合)。" + "sosCd2=" + sosCd2 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			if(branchPlan.getImplMonthPlan() == null) {
				branchPlan.setImplPlan(new ImplMonthPlan());
			}

			if(category.equals(vaccineCode)) {
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

			// DTO作成
			ProdMonthPlanResultDetailDto detail = new ProdMonthPlanResultDetailDto(branchPlan, canMovePlanLevel);
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
