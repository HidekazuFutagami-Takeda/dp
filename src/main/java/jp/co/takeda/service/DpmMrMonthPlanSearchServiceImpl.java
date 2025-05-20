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
import jp.co.takeda.dao.DpmSyComInsOyakoDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageInsMonthPlanDao;
import jp.co.takeda.dao.ManageMrMonthPlanDao;
import jp.co.takeda.dao.ManageMrPlanDao;
import jp.co.takeda.dao.ManageOfficeMonthPlanDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.ProdCategoryDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.ProdMonthPlanResultDetailDto;
import jp.co.takeda.dto.ProdMonthPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.ProdPlanSummaryResultDetailDto;
import jp.co.takeda.dto.ProdPlanSummaryResultDto;
import jp.co.takeda.dto.ProdPlanSummaryScDto;
import jp.co.takeda.dto.SosMonthPlanResultDetailDto;
import jp.co.takeda.dto.SosMonthPlanResultDetailTotalDto;
import jp.co.takeda.dto.SosMonthPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.Cal;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.ImplMonthPlan;
import jp.co.takeda.model.ManageMrMonthPlan;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.model.ManageOfficeMonthPlan;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;

/**
 * 管理の組織別計画(担当者)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmMrMonthPlanSearchService")
public class DpmMrMonthPlanSearchServiceImpl implements DpmMrMonthPlanSearchService {

	/**
	 * 営業所別月別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageOfficeMonthPlanDao")
	protected ManageOfficeMonthPlanDao manageOfficeMonthPlanDao;

	/**
	 * 担当者別月別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageMrMonthPlanDao")
	protected ManageMrMonthPlanDao manageMrMonthPlanDao;

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageMrPlanDao")
	protected ManageMrPlanDao manageMrPlanDao;

	/**
	 * 施設別月別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsMonthPlanDao")
	protected ManageInsMonthPlanDao manageInsMonthPlanDao;

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
	 * 組織従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmSosJgiSearchService")
	protected DpmSosJgiSearchService dpmSosJgiSearchService;

	/**
	 * 計画対象カテゴリ領域Dao
	 */
	@Autowired(required = true)
	@Qualifier("plannedCtgDao")
	protected PlannedCtgDao plannedCtgDao;

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

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpmSyComInsOyakoDao")
	protected DpmSyComInsOyakoDao dpmSyComInsOyakoDao;

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
    /**
     * 管理の共通サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmCommonService")
    protected DpmCommonService dpmCommonService;
 // add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	// 組織別計画取得
	public SosMonthPlanResultDto searchSosPlan(SosPlanScDto scDto, String jgiNo) throws LogicalException {

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
		if (scDto.getSosCd3() == null) {
			final String errMsg = "検索対象の組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		final String prodCode = scDto.getProdCode();
		MasterManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

		// 組織とカテゴリの整合性チェック
		final String category = scDto.getProdCategory();

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
		// 計画立案レベル・担当者が設定されていない場合はエラー
		if (!plannedProd.getPlanLevelMr()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1001E", plannedProd.getProdName(), "担当者計画")));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
		plannedCtg = plannedCtgDao.search(scDto.getProdCategory());

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		int oyakoCount = 0;
		String oyakoKbProdCode = prodCode;
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpmSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
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
		boolean vacCheck = plannedCtg.equals(vaccineCode);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示


		// -----------------------------
		// 上位計画・担当者別計画 取得
		// -----------------------------

		// 雑組織フラグ
		boolean etcSosFlg = false;
		// 集計行
		SosMonthPlanResultDetailTotalDto detailTotal;
		// 明細担当者別月別計画
		List<ManageMrMonthPlan> mrPlanList;

		// -----------------------------
		// 営業所計画取得
		// -----------------------------
		final String sosCd3 = scDto.getSosCd3();
		ManageOfficeMonthPlan officePlan;
		try {
			officePlan = manageOfficeMonthPlanDao.searchTotalLine(InsType.UH, prodCode, sosCd3);

		} catch (DataNotFoundException e) {
			officePlan = new ManageOfficeMonthPlan();
		}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		if(officePlan.getImplMonthPlan() == null) {
			officePlan.setImplPlan(new ImplMonthPlan());
		}

		if(vacCheck) {
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

		// 上位立案(営業所別計画)に対する品目立案レベル取得
		boolean upperPlanLevel = plannedProd.getPlanLevelOffice();

		// 明細合計行作成(営業所計画)
		detailTotal = new SosMonthPlanResultDetailTotalDto(officePlan, upperPlanLevel);

		// ----------------------
		// 担当者別計画取得
		// ----------------------
		// 6ヶ月分の担当者計画を取得
		// mod Start 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
		//mrPlanList = manageMrMonthPlanDao.searchListByProd(prodCode, sosCd3, BumonRank.OFFICE_TOKUYAKUTEN_G, jgiNo, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode);
		mrPlanList = manageMrMonthPlanDao.searchListByProd(prodCode, sosCd3, BumonRank.OFFICE_TOKUYAKUTEN_G, jgiNo, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode, category);
		// mod End 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る

		// 下位立案(施設別計画)に対する品目立案レベル取得
		boolean canMovePlanLevel = plannedProd.getPlanLevelIns();

		// 明細行作成(担当者別計画)
		List<SosMonthPlanResultDetailDto> detailList = new ArrayList<SosMonthPlanResultDetailDto>();

		for (ManageMrMonthPlan monthPlan : mrPlanList) {

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			if(monthPlan.getImplMonthPlan() == null) {
				monthPlan.setImplMonthPlan(new ImplMonthPlan());
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

			SosMonthPlanResultDetailDto detail = new SosMonthPlanResultDetailDto(monthPlan, canMovePlanLevel, etcSosFlg);
			detailList.add(detail);
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new SosMonthPlanResultDto(detailTotal, detailList);
	}

	// 品目別月別計画(担当者)取得
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
		if (scDto.getJgiNo() == null) {
			final String errMsg = "検索対象の従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String category = scDto.getProdCategory();
		final Integer jgiNo = scDto.getJgiNo();

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

		PlannedCtg plannedCtg = new PlannedCtg();
		if (!isJrns) {
			plannedCtg = plannedCtgDao.search(category);
		} else {
			// JRNSの場合は、営業所のカテゴリ領域を使う
			searchList = new ArrayList<DpmCCdMst>();
			try {
				// カテゴリの検索
				searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.OFFICE.getDbValue());
			} catch (DataNotFoundException e) {
				final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.OFFICE.getDbValue() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
			// 営業所のカテゴリコード
			String officeCategory = searchList.get(0).getDataCd();
			plannedCtg = plannedCtgDao.search(officeCategory);
		}

		// ----------------------
		// 担当者別計画取得
		// ----------------------
		// 6ヶ月分の担当者計画を取得
		List<ManageMrMonthPlan> mrPlanList = manageMrMonthPlanDao.searchListByJgi(jgiNo, category, plannedCtg.getTgtInsKb(), isJrns, jrnsPcatCd, jrnsCtgList);

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
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.CV.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		String cvCode = searchList.get(0).getDataCd();

		// 別カテゴリの6ヶ月分の担当者計画の集計値を取得
		ManageMrMonthPlan otherPlanSum = new ManageMrMonthPlan();

		// カテゴリがワクチン・CVの場合は、もう一方の6ヶ月分の全社計画の集計を取得
		if (category.equals(vaccineCode)) {
			try {
				otherPlanSum = manageMrMonthPlanDao.searchPlanSum(jgiNo, cvCode, plannedCtg.getTgtInsKb());
			} catch (DataNotFoundException e) {
				// データ０件の場合は何もしない
			}
		} else if (category.equals(cvCode)) {
			try {
				otherPlanSum = manageMrMonthPlanDao.searchPlanSum(jgiNo, vaccineCode, plannedCtg.getTgtInsKb());
			} catch (DataNotFoundException e) {
				// データ０件の場合は何もしない
			}
		}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		int tougetuCount = TougetuHantei();

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

		for(ManageMrMonthPlan monthPlan : mrPlanList) {

			final String prodCode = monthPlan.getProdCode();

			// ----------------------------------------------------
			// 下位立案(施設別計画)に対する品目立案レベル取得
			// ----------------------------------------------------
			boolean canMovePlanLevel;
			try {
				MasterManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);
				canMovePlanLevel = plannedProd.getPlanLevelIns();
			} catch (DataNotFoundException e1) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "jgiNo=" + jgiNo + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			if(category.equals(vaccineCode)) {
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

			// DTO作成
			ProdMonthPlanResultDetailDto detail = new ProdMonthPlanResultDetailDto(monthPlan, canMovePlanLevel);
				detailList.add(detail);

		}
		return new ProdMonthPlanResultDto(prodCategoryName, otherPlanSumDto, detailList);
	}

	// 組織別品目別計画(施設積上)取得
	public ProdPlanSummaryResultDto searchSosProdPlanInsSummary(ProdPlanSummaryScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "組織別品目別計画(施設積上)検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getProdCategory() == null) {
			final String errMsg = "検索対象のカテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getJgiNo() == null) {
			final String errMsg = "検索対象の従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String category = scDto.getProdCategory();
		final Integer jgiNo = scDto.getJgiNo();

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
		plannedCtg = plannedCtgDao.search(scDto.getProdCategory());

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		int oyakoCount = 0;
		//品目コードが不明なので、Null
		String oyakoKbProdCode = null;
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpmSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}

		// ----------------------
		// 担当者別計画取得
		// ----------------------
		// UH・P・Zの順番に、1品目当たり最大3件ずつ取得（施設区分で判断して格納する）
		//
		List<ManageMrPlan> mrPlanList = manageMrPlanDao.searchListByJgi(jgiNo, category, plannedCtg.getTgtInsKb(), plannedCtg.getOyakoKb()
				, plannedCtg.getOyakoKb2(), oyakoKbProdCode, null);

		// 登録不可フラグ
		boolean disableUpdate = false;

		// 明細行作成
		List<ProdPlanSummaryResultDetailDto> detailList = new ArrayList<ProdPlanSummaryResultDetailDto>();

		ManageMrPlan mrPlanUh = new ManageMrPlan();
		ManageMrPlan mrPlanP = new ManageMrPlan();
		ManageMrPlan mrPlanZ = new ManageMrPlan();

		for(int i = 0; i < mrPlanList.size();i++) {

			switch(mrPlanList.get(i).getInsType()) {
				case UH:
					mrPlanUh = mrPlanList.get(i);
					break;
				case P:
					mrPlanUh = mrPlanList.get(i);
					break;
				case ZATU:
					mrPlanZ = mrPlanList.get(i);
					break;
			}

			// 次の要素がない もしくは 次の要素の品目固定コードが異なる場合
			if((i != mrPlanList.size() -1)
					&& (mrPlanList.get(i).getProdCode().equals(mrPlanList.get(i + 1).getProdCode()) == false)) {

				ProdPlanSummaryResultDetailDto detail = new ProdPlanSummaryResultDetailDto(mrPlanUh, mrPlanP, mrPlanZ);

				// 積上が登録可能最大値を超えている場合は、登録不可
				if (detail.getPlannedValueOver()) {
					disableUpdate = true;
				}

				detailList.add(detail);

				// 初期化
				mrPlanUh = new ManageMrPlan();
				mrPlanP = new ManageMrPlan();
				mrPlanZ = new ManageMrPlan();


			}

		}

		return new ProdPlanSummaryResultDto(detailList, disableUpdate);
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
