package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CodeMasterDao;
//add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.dao.DpmInsJgiSosDao;
//add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.dao.DpmSyComInsOyakoDao;
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.JgiMstRealDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageInsMonthPlanDao;
import jp.co.takeda.dao.ManageInsPlanDao;
import jp.co.takeda.dao.ManageMrPlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.InsPlanResultDetailDto;
import jp.co.takeda.dto.InsPlanResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanResultDto;
import jp.co.takeda.dto.InsPlanScDto;
import jp.co.takeda.dto.InsProdMonthPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdPlanResultDetailDto;
import jp.co.takeda.dto.InsProdPlanResultDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdPlanScDto;
import jp.co.takeda.logic.CheckInsNoLogic;
import jp.co.takeda.logic.GetChangeParamYTLogic;
import jp.co.takeda.logic.MallLogic;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.ManageInsPlan;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.util.MathUtil;

/**
 * 管理の施設別計画の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmInsPlanSearchService")
public class DpmInsPlanSearchServiceImpl implements DpmInsPlanSearchService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpmInsPlanSearchServiceImpl.class);

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageMrPlanDao")
	protected ManageMrPlanDao manageMrPlanDao;

	/**
	 * 施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsPlanDao")
	protected ManageInsPlanDao manageInsPlanDao;

	/**
	 * 施設別計画（月別）DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsMonthPlanDao")
	protected ManageInsMonthPlanDao manageInsMonthPlanDao;


	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstRealDao")
	protected InsMstRealDao insMstRealDao;

	/**
	 * 従業員DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstRealDao")
	protected JgiMstRealDao jgiMstRealDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	protected ManagePlannedProdDao managePlannedProdDao;

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
	 * 計画対象カテゴリ領域DAO
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
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpmSyComInsOyakoDao")
	protected DpmSyComInsOyakoDao dpmSyComInsOyakoDao;

// add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
	/**
	 * 同じ組織配下の施設担当者DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsJgiSosDao")
	protected DpmInsJgiSosDao dpmInsJgiSosDao;
// add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応

	// 施設別計画ヘッダー情報取得
	public InsProdPlanResultHeaderDto searchInsPlanHeader(String insNo,String prodCode) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		try {
			// 施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
			InsMst insMst = logic.executeInsProd(insNo, prodCode);
			return new InsProdPlanResultHeaderDto(insMst);

		} catch (LogicalException e) {
			return null;
		}
	}

	// 施設別計画ヘッダー情報取得
	public InsProdPlanResultHeaderDto searchInsPlanHeaderOyako(String insNo,String prodCode, String category) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "検索対象の品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "検索対象のカテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
		try {
			plannedCtg = plannedCtgDao.search(category);
		} catch (DataNotFoundException e1) {
			final String errMsg = "計画対象カテゴリ領域の取得に失敗。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e1);
		}

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		//親子区分1が'ZZZZZ'の時、セットする親子区分1（品目コード）を設定する。
		int oyakoCount = 0;
		String oyakoKbProdCode = prodCode;
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			try {
				oyakoCount = dpmSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			} catch (DataNotFoundException e) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}

		try {
			// 施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
			InsMst insMst = logic.executeInsProdOyako(insNo, prodCode, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode);
			return new InsProdPlanResultHeaderDto(insMst);

		} catch (LogicalException e) {
			return null;
		}
	}

	// 施設別計画ヘッダー情報取得（月別）
	public InsProdMonthPlanResultHeaderDto searchInsMonthPlanHeader(String insNo,String prodCode) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		try {
			// 施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
			InsMst insMst = logic.executeInsProd(insNo, prodCode);
			return new InsProdMonthPlanResultHeaderDto(insMst);

		} catch (LogicalException e) {
			return null;
		}
	}


	// 施設別計画取得
	public InsPlanResultDto searchInsPlan(InsPlanScDto scDto, boolean detailErrFlg) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getInsNo() == null) {
			if (scDto.getJgiNo() == null) {
				final String errMsg = "検索対象の従業員番号・施設コードが共にnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (scDto.getInsType() == null) {
				final String errMsg = "検索対象の対象区分がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		if (scDto.getProdCode() == null) {
			final String errMsg = "検索対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getPlanData() == null) {
			final String errMsg = "検索対象の計画(計画あり・全施設)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
		plannedCtg = plannedCtgDao.search(scDto.getProdCategory());

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		//親子区分1が'ZZZZZ'の時、セットする親子区分1（品目コード）を設定する。
		int oyakoCount = 0;
		String oyakoKbProdCode = scDto.getProdCode();
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpmSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}

		String prodCode = scDto.getProdCode();
		PlanData planData = scDto.getPlanData();
		boolean allInsFlg = false;
		if (planData == PlanData.ALL) {
			allInsFlg = true;
		}

		Integer jgiNo = null;
		InsType insType = null;
		String relnInsNo = null;

		// 施設指定
		if (scDto.getInsNo() != null) {

			// 施設コードが設定されている場合、施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
			InsMst insMst = logic.executeInsProdOyako(scDto.getInsNo(), prodCode, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode);

			// 取得した施設情報の担当者・対象区分・施設内部コードを取得
			jgiNo = insMst.getJgiNo();
			insType = InsType.convertInsType(insMst.getHoInsType());
			relnInsNo = insMst.getRelnInsNo();
		}

		// 全担当施設
		else {

			// 施設コードが設定されていない場合、検索条件の入力を使用
			jgiNo = scDto.getJgiNo();
			insType = scDto.getInsType();
		}

		// 施設別計画取得
		List<ManageInsPlan> insPlanList = null;
		try {
			insPlanList = manageInsPlanDao.searchListByProd(ManageInsPlanDao.SORT_STRING, prodCode, jgiNo, insType, relnInsNo, allInsFlg,plannedCtg.getOyakoKb(),
					plannedCtg.getOyakoKb2(),plannedCtg.getTgtInsKb(), oyakoKbProdCode);
		} catch (DataNotFoundException e) {
			if (detailErrFlg) {
				throw e;
			}
			insPlanList = new ArrayList<ManageInsPlan>();
		}

		// -----------------------------
		// モール施設のフィルター
		// -----------------------------
		MallLogic mollLogic = new MallLogic();
		insPlanList = mollLogic.filterManageInsPlanList(insPlanList);
		// [エラーメッセージ表示モード]、かつ、[フィルター後0件]となった場合、データがない旨を表示
		if (detailErrFlg && (insPlanList.size() < 1)) {
			throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR));
		}

		return createInsPlanResultDto(prodCode, jgiNo, insType, insPlanList, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode);
	}

	/**
	 * 施設別計画検索結果DTOを作成する。
	 *
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param insType 対象区分
	 * @param insPlanList 対象施設計画のリスト
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分２
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 施設別計画検索結果DTO
	 * @throws LogicalException
	 */
	public InsPlanResultDto createInsPlanResultDto(String prodCode, Integer jgiNo, InsType insType, List<ManageInsPlan> insPlanList, String oyakoKb,
			String oyakoKb2, String oyakoKbProdCode) throws LogicalException {

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

		// 品目の立案レベルチェック
		if (!plannedProd.getPlanLevelIns()) {

			// 計画立案レベル・施設が設定されていない場合はエラー
			throw new LogicalException(new Conveyance(new MessageKey("DPM1001E", plannedProd.getProdName(), "施設計画")));
		}

		// -----------------------------
		// 編集可能な担当者かチェック
		// -----------------------------
		// 施設品目担当者の従業員情報取得
		JgiMst jgiMst;
		try {
			jgiMst = jgiMstRealDao.searchReal(jgiNo);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1004E")));
		}

		// 設定中のユーザの配下の担当者か
		boolean mySosMr = true;
		try {
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
			logic.validateMr(jgiMst);
		} catch (LogicalException e) {
			mySosMr = false;
		}

		// ----------------------
		// T/Y変換率取得
		// ----------------------

		// 納入計画システム管理取得
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPM, SysType.IYAKU);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理の取得に失敗。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// T/Y変換率取得
		GetChangeParamYTLogic tylogic = new GetChangeParamYTLogic(sysManage, manageChangeParamYTDao);
		Double changeRate = tylogic.execute(insType, prodCode);

		// -----------------------------
		// 明細行作成
		// -----------------------------

		// 表示対象施設の計画値合計
		Long searchPlanTotal = null;

		List<InsPlanResultDetailDto> detailList = new ArrayList<InsPlanResultDetailDto>();
		for (ManageInsPlan insPlan : insPlanList) {

			// 明細行作成
			// 設定中のユーザの配下であれば編集可能
			InsPlanResultDetailDto detail = new InsPlanResultDetailDto(insPlan, changeRate, mySosMr);
			detailList.add(detail);

			// 表示対象施設の計画値を加算する
			searchPlanTotal = MathUtil.add(searchPlanTotal, insPlan.getImplPlan().getPlanned2ValueY());
		}

		// 登録可能かチェック(編集可能明細が1件でもあれば登録可能)
		boolean enableEntry = false;
		for (InsPlanResultDetailDto detailDto : detailList) {
			if (detailDto.getEnableEdit()) {
				enableEntry = true;
				break;
			}
		}

		// -----------------------------
		// 集計行作成
		// -----------------------------

		// 担当者別計画取得
		ManageMrPlan mrPlan;
		try {
			mrPlan = manageMrPlanDao.searchUk(insType, prodCode, jgiNo);
		} catch (DataNotFoundException e) {
			mrPlan = new ManageMrPlan();
		}

		// 施設別計画積上取得(担当全施設)
		Long stackedValue = manageInsPlanDao.searchSumByProd(prodCode, jgiNo, insType, oyakoKb, oyakoKb2, oyakoKbProdCode);

		// 上位立案(担当者計画)に対する品目立案レベル取得
		boolean upperPlanLevel = plannedProd.getPlanLevelMr();

		// 非表示施設積上  ( ＝ 担当全施設積上 － 表示施設積上 )
		Long hideValue = MathUtil.subEx(stackedValue, searchPlanTotal);

		if (LOG.isDebugEnabled()) {
			LOG.debug("非表示の施設積上：" + hideValue);
		}

		// 明細合計行作成
		InsPlanResultDetailTotalDto detailTotal = new InsPlanResultDetailTotalDto(mrPlan, hideValue, changeRate, upperPlanLevel);

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new InsPlanResultDto(detailTotal, detailList, enableEntry, mySosMr);

	}

	// 施設別品目別計画取得
	public InsProdPlanResultDto searchInsProdPlan(InsProdPlanScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設別品目別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getInsNo() == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getProdCategory() == null) {
			final String errMsg = "検索対象のカテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getPlanData() == null) {
			final String errMsg = "検索対象の計画(計画あり・全施設)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String insNo = scDto.getInsNo();
		String category = scDto.getProdCategory();
		PlanData planData = scDto.getPlanData();
		String vaccineCode = scDto.getVaccineCode();
		boolean allProdFlg = false;
		if (planData == PlanData.ALL) {
			allProdFlg = true;
		}
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

		// -----------------------------
		// 施設コードチェック
		// -----------------------------

		// チェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//		CheckInsNoLogic insChecklogic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
		CheckInsNoLogic insChecklogic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
//mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
		InsMst insMst = insChecklogic.executeInsProd(insNo, null);

		// 対象区分取得
		InsType insType = InsType.convertInsType(insMst.getHoInsType());

		// 削除フラグ取得
		boolean isDeletePlan = false;
		if (insMst.getDelFlg() || insMst.getReqFlg()) {
			isDeletePlan = true;
		}

		// -----------------------------
		// モール施設のチェック
		// -----------------------------
		MallLogic mallLogic = new MallLogic();
		if (mallLogic.isMall(insMst)) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1007E")));
		}

		// -----------------------------
		// 納入計画システム管理取得
		// -----------------------------
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPM, SysType.IYAKU);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理の取得に失敗。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
		plannedCtg = plannedCtgDao.search(scDto.getProdCategory());

		// -----------------------------
		// 明細行作成
		// -----------------------------

		// 施設別計画取得
		List<ManageInsPlan> insPlanList = manageInsPlanDao.searchListByIns(ManageInsPlanDao.SORT_STRING2, insNo, category, allProdFlg, plannedCtg.getTgtInsKb(), vaccineCode);

		// T/Y変換取得ロジック
		GetChangeParamYTLogic tylogic = new GetChangeParamYTLogic(sysManage, manageChangeParamYTDao);

		// 明細行作成
		List<InsProdPlanResultDetailDto> detailList = new ArrayList<InsProdPlanResultDetailDto>();
		for (ManageInsPlan insPlan : insPlanList) {

			// T/Y変換率取得
			Double changeRate = tylogic.execute(insType, insPlan.getProdCode());

			// 設定中のユーザの配下であれば編集可能
			boolean enableEdit = true;
			try {
				insChecklogic.validateMr(insPlan.getJgiMst());
			} catch (LogicalException e) {
				enableEdit = false;
			}

			// 明細行DTO作成
			InsProdPlanResultDetailDto detail = new InsProdPlanResultDetailDto(insPlan, changeRate, isDeletePlan, enableEdit);
			detailList.add(detail);
		}

		// 登録可能かチェック(編集可能明細が1件でもあれば登録可能)
		boolean enableEntry = false;
		for (InsProdPlanResultDetailDto detailDto : detailList) {
			if (detailDto.getEnableEdit()) {
				enableEntry = true;
				break;
			}
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new InsProdPlanResultDto(detailList, enableEntry);
	}

}
