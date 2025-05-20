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
//add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.dao.DpmInsJgiSosDao;
//add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.dao.DpmSyComInsOyakoDao;
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.JgiMstRealDao;
import jp.co.takeda.dao.ManageChangeParamBTDao;
import jp.co.takeda.dao.ManageInsPlanForVacDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsProdPlanForVacResultDetailDto;
import jp.co.takeda.dto.InsProdPlanForVacResultDto;
import jp.co.takeda.dto.InsProdPlanForVacScDto;
import jp.co.takeda.logic.CheckInsNoLogic;
import jp.co.takeda.logic.CreateInsPlanForVacResultLogic;
import jp.co.takeda.logic.MallLogic;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.ChangeParamBT;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.ManageInsPlanForVac;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.MrCat;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.util.MathUtil;

/**
 * 管理のワクチン用施設別計画の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmInsPlanForVacSearchService")
public class DpmInsPlanForVacSearchServiceImpl implements DpmInsPlanForVacSearchService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpmInsPlanForVacSearchServiceImpl.class);

	/**
	 * ワクチン用施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsPlanForVacDao")
	protected ManageInsPlanForVacDao manageInsPlanForVacDao;

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
	 * 変換パラメータ(B→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamBTDao")
	protected ManageChangeParamBTDao manageChangeParamBTDao;

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
	public InsPlanForVacHeaderDto searchInsPlanHeader(String insNo) {

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
			InsMst insMst = logic.execute(insNo, MrCat.VAC_MR);
			return new InsPlanForVacHeaderDto(insMst);

		} catch (LogicalException e) {

			return null;
		}
	}

	// 施設別計画(明細)取得
	public InsPlanForVacResultDto searchInsPlan(InsPlanForVacScDto scDto) throws LogicalException {

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
			if (scDto.getActivityType() == null) {
				final String errMsg = "検索対象の活動区分がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		if (scDto.getProdCode() == null) {
			final String errMsg = "検索対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String prodCode = scDto.getProdCode();


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

		if (scDto.getInsNo() != null) {

			// 施設コードが設定されている場合、施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
			CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
			InsMst insMst = logic.execute(scDto.getInsNo(), MrCat.VAC_MR);

			// -----------------------------
			// モール施設のチェック
			// -----------------------------
			MallLogic mallLogic = new MallLogic();
			if (mallLogic.isMall(insMst)) {
				throw new LogicalException(new Conveyance(new MessageKey("DPM1007E")));
			}

			// 取得した施設情報の担当者・活動区分・府県コード・市区町村コードを取得
			String insNo = insMst.getInsNo();
			Integer jgiNo = insMst.getJgiNo();
			ActivityType activityType = insMst.getActivityType();
			Prefecture addrCodePref = insMst.getAddrCodePref();
			String addrCodeCity = insMst.getAddrCodeCity();

			// 施設別計画取得
			List<ManageInsPlanForVac> insPlanList = manageInsPlanForVacDao.searchListByProd(ManageInsPlanForVacDao.SORT_STRING, prodCode, jgiNo, activityType, addrCodePref,
				addrCodeCity, insNo, plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(), plannedCtg.getTgtInsKb(), oyakoKbProdCode);

			// DTO作成
			CreateInsPlanForVacResultLogic resultLogic = new CreateInsPlanForVacResultLogic(sysManageDao, manageChangeParamBTDao);
			InsPlanForVacResultDto resultDto = resultLogic.execute(prodCode, insPlanList);

			return resultDto;

		} else {

			// 施設コードが設定されていない場合、検索条件の入力を使用
			Integer jgiNo = scDto.getJgiNo();
			ActivityType activityType = scDto.getActivityType();
			Prefecture addrCodePref = scDto.getAddrCodePref();
			String addrCodeCity = scDto.getAddrCodeCity();

			// 施設別計画取得
			List<ManageInsPlanForVac> insPlanList = manageInsPlanForVacDao.searchListByProd(ManageInsPlanForVacDao.SORT_STRING, prodCode, jgiNo, activityType, addrCodePref,
				addrCodeCity, null, plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(), plannedCtg.getTgtInsKb(), oyakoKbProdCode);

			// DTO作成
			CreateInsPlanForVacResultLogic resultLogic = new CreateInsPlanForVacResultLogic(sysManageDao, manageChangeParamBTDao);
			InsPlanForVacResultDto resultDto = resultLogic.execute(prodCode, insPlanList);

			return resultDto;
		}
	}

	// 施設別計画(集計)取得
	public InsPlanForVacResultDetailTotalDto searchInsPlanTotal(InsPlanForVacScDto scDto, InsPlanForVacResultDto detailResult) {

		String prodCode = scDto.getProdCode();

		// ----------------------
		// 従業員番号取得
		// ----------------------
		Integer jgiNo;
		if (scDto.getInsNo() != null) {

			// 施設コードが設定されている場合、施設情報から従業員を取得
			try {
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
				InsMst insMst = logic.execute(scDto.getInsNo(), MrCat.VAC_MR);
				jgiNo = insMst.getJgiNo();
			} catch (LogicalException e) {
				return null;
			}

		} else {

			// 施設コードが設定されていない場合、検索条件から取得
			jgiNo = scDto.getJgiNo();
		}

		// ----------------------
		// T/B変換率取得
		// ----------------------

		// 納入計画システム管理取得
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPM, SysType.VACCINE);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理の取得に失敗。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// T/B変換率取得
		Double changeRate;
		try {
			ChangeParamBT changeParamBT = manageChangeParamBTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
			changeRate = changeParamBT.getChangeRate();

		} catch (DataNotFoundException e) {
			final String errMsg = "T/B変換パラメータの取得に失敗。prodCode=";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// -----------------------------
		// 集計行作成
		// -----------------------------

		// 施設別計画積上取得(担当全施設)
		Long stackedValue = manageInsPlanForVacDao.searchSumByProd(prodCode, jgiNo, null);

		// 表示施設積上
		Long searchPlanTotal = null;
		if (detailResult != null) {
			searchPlanTotal = detailResult.getSearchPlanTotal();
		}

		// 非表示施設積上  ( ＝ 担当全施設積上 － 表示施設積上 )
		Long hideValue = MathUtil.subEx(stackedValue, searchPlanTotal);
		if (LOG.isDebugEnabled()) {
			LOG.debug("非表示の施設積上：" + hideValue);
		}

		// 明細合計行作成
		return new InsPlanForVacResultDetailTotalDto(hideValue, changeRate);
	}

	// 施設別品目別計画取得
	public InsProdPlanForVacResultDto searchInsProdPlan(InsProdPlanForVacScDto scDto) throws LogicalException {

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
		if (scDto.getPlanData() == null) {
			final String errMsg = "検索対象の計画(計画あり・全施設)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String insNo = scDto.getInsNo();
		PlanData planData = scDto.getPlanData();
		boolean allProdFlg = false;
		if (planData == PlanData.ALL) {
			allProdFlg = true;
		}

		// -----------------------------
		// 施設コードチェック
		// -----------------------------

		// チェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//		CheckInsNoLogic insChecklogic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
		CheckInsNoLogic insChecklogic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
//mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
		InsMst insMst = insChecklogic.execute(insNo, MrCat.VAC_MR);

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
			sysManage = sysManageDao.search(SysClass.DPM, SysType.VACCINE);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理の取得に失敗。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -----------------------------
		// 明細行作成
		// -----------------------------

		// 施設別計画取得
		List<ManageInsPlanForVac> insPlanList = manageInsPlanForVacDao.searchListByIns(ManageInsPlanForVacDao.SORT_STRING2, insNo, allProdFlg);

		// 明細行作成
		List<InsProdPlanForVacResultDetailDto> detailList = new ArrayList<InsProdPlanForVacResultDetailDto>();
		for (ManageInsPlanForVac insPlan : insPlanList) {

			String prodCode = insPlan.getProdCode();

			// T/B変換率取得
			Double changeRate;
			try {
				ChangeParamBT changeParamBT = manageChangeParamBTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
				changeRate = changeParamBT.getChangeRate();

			} catch (DataNotFoundException e) {
				final String errMsg = "T/B変換パラメータの取得に失敗。prodCode=";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
			}

			// 明細行DTO作成
			InsProdPlanForVacResultDetailDto detail = new InsProdPlanForVacResultDetailDto(insPlan, changeRate, isDeletePlan);
			detailList.add(detail);
		}

		// 登録可能かチェック(編集可能明細が1件でもあれば登録可能)
		boolean enableEntry = false;
		for (InsProdPlanForVacResultDetailDto detailDto : detailList) {
			if (detailDto.getEnableEdit()) {
				enableEntry = true;
				break;
			}
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new InsProdPlanForVacResultDto(detailList, enableEntry);
	}

}
