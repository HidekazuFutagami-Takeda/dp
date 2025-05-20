package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import jp.co.takeda.dao.ManageChangeParamBTDao;
import jp.co.takeda.dao.ManageInsWsPlanForVacDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.dto.ManageInsWsPlanProdForVacDetailDto;
import jp.co.takeda.dto.ManageInsWsPlanProdForVacResultDto;
import jp.co.takeda.dto.ManageInsWsProdPlanForVacScDto;
import jp.co.takeda.logic.CheckInsNoLogic;
import jp.co.takeda.logic.CreateInsMstScResultDtoLogic;
import jp.co.takeda.logic.CreateInsWsPlanForVacResultLogic;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.logic.MallLogic;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.ChangeParamBT;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.ManageInsWsPlanForVac;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.MrCat;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 管理のワクチン用施設特約店別計画の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmInsWsPlanForVacSearchService")
public class DpmInsWsPlanForVacSearchServiceImpl implements DpmInsWsPlanForVacSearchService {

	/**
	 * ワクチン用施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsWsPlanForVacDao")
	protected ManageInsWsPlanForVacDao manageInsWsPlanForVacDao;

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstRealDao")
	protected InsMstRealDao insMstRealDao;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnRealDao")
	protected TmsTytenMstUnRealDao tmsTytenMstUnRealDao;

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
	 * 汎用コードマスタDAO
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

	// 施設特約店別計画ヘッダー情報取得
	public ManageInsWsPlanForVacHeaderDto searchInsWsPlanHeader(String insNo, String tmsTytenCd) {

		// -----------------------------
		// 施設情報取得
		// -----------------------------
		InsMstResultDto insMstResultDto = null;
		if (insNo != null) {
			try {
				// 施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
				InsMst insMst = logic.execute(insNo, MrCat.VAC_MR);
				insMstResultDto = new CreateInsMstScResultDtoLogic(insMst).convert();
			} catch (LogicalException e) {
			}
		}

		// -----------------------------
		// 特約店情報取得
		// -----------------------------
		// 特約店コードを13桁に変換
		String tmsTytenName = null;
		boolean planTaiGaiFlgTok = false;
		if (tmsTytenCd != null) {
			final String tmsTytenCd13 = new CreateTmsTytenCdLogic(tmsTytenCd).execute();
			try {
				TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd13);
				tmsTytenName = tmsTytenMstUn.getTmsTytenMeiKj();
				planTaiGaiFlgTok = tmsTytenMstUn.getPlanTaiGaiFlgTok();
			} catch (DataNotFoundException e) {
			}
		}

		return new ManageInsWsPlanForVacHeaderDto(insMstResultDto, tmsTytenCd, tmsTytenName, planTaiGaiFlgTok);
	}

	// 施設特約店別計画(明細)取得
	public ManageInsWsPlanForVacDto searchInsWsPlan(ManageInsWsPlanForVacScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getInsNo() == null) {
			if (scDto.getJgiNo() == null && scDto.getSosCd3() == null) {
				final String errMsg = "検索対象の従業員番号(または組織コード(営業所))・施設コードが共にnull";
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
		String tmsTytenCd = scDto.getTmsTytenCd();

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

			// 施設特約店別計画取得 (特約店コードは指定しない)
			List<ManageInsWsPlanForVac> insWsPlanList = manageInsWsPlanForVacDao.searchListByProd(ManageInsWsPlanForVacDao.SORT_STRING, prodCode, null, jgiNo, activityType,
				addrCodePref, addrCodeCity, insNo, null,plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(),plannedCtg.getTgtInsKb(), oyakoKbProdCode);

			// DTO作成
			CreateInsWsPlanForVacResultLogic resultLogic = new CreateInsWsPlanForVacResultLogic(sysManageDao, manageChangeParamBTDao, tmsTytenMstUnRealDao);
			ManageInsWsPlanForVacDto resultDto = resultLogic.execute(prodCode, tmsTytenCd, insWsPlanList);

			return resultDto;

		} else {

			// 施設コードが設定されていない場合、検索条件の入力を使用
			Integer jgiNo = scDto.getJgiNo();
			String sosCd3 = scDto.getSosCd3();
			ActivityType activityType = scDto.getActivityType();
			Prefecture addrCodePref = scDto.getAddrCodePref();
			String addrCodeCity = scDto.getAddrCodeCity();

			// 施設特約店別計画取得 (特約店コードは指定しない)
			List<ManageInsWsPlanForVac> insWsPlanList = manageInsWsPlanForVacDao.searchListByProd(ManageInsWsPlanForVacDao.SORT_STRING, prodCode, sosCd3, jgiNo, activityType,
				addrCodePref, addrCodeCity, null, null,plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(),plannedCtg.getTgtInsKb(), oyakoKbProdCode);

			// DTO作成
			CreateInsWsPlanForVacResultLogic resultLogic = new CreateInsWsPlanForVacResultLogic(sysManageDao, manageChangeParamBTDao, tmsTytenMstUnRealDao);
			ManageInsWsPlanForVacDto resultDto = resultLogic.execute(prodCode, tmsTytenCd, insWsPlanList);

			return resultDto;
		}
	}

	// 施設特約店別計画(集計)取得
	public ManageInsWsPlanForVacResultDetailTotalDto searchInsWsPlanTotal(ManageInsWsPlanForVacScDto scDto) {

		String prodCode = scDto.getProdCode();

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

		// 明細合計行作成
		return new ManageInsWsPlanForVacResultDetailTotalDto(changeRate);
	}

	// 施設特約店別品目別計画取得
	public ManageInsWsPlanProdForVacResultDto searchInsWsProdPlan(ManageInsWsProdPlanForVacScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設特約店別品目別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// 施設コード
		String insNo = scDto.getInsNo();
		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// 特約店コード
		String tmsTytenCd = new CreateTmsTytenCdLogic(scDto.getTmsTytenCd()).execute();
		if (tmsTytenCd == null) {
			final String errMsg = "検索対象の特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getPlanData() == null) {
			final String errMsg = "検索対象の計画(計画あり・全施設)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 計画
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
		// 特約店情報取得
		// -----------------------------
		tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd);

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

		// 施設特約店別計画取得
		List<ManageInsWsPlanForVac> insWsPlanList = manageInsWsPlanForVacDao.searchListByInsWs(ManageInsWsPlanForVacDao.SORT_STRING2, insNo, tmsTytenCd, allProdFlg);

		// 明細行作成
		List<ManageInsWsPlanProdForVacDetailDto> detailList = new ArrayList<ManageInsWsPlanProdForVacDetailDto>();
		for (ManageInsWsPlanForVac insWsPlan : insWsPlanList) {

			String prodCode = insWsPlan.getProdCode();

			// T/B変換率取得 (特約店コードは設定しない)
			Double changeRate;
			try {
				ChangeParamBT changeParamBT = manageChangeParamBTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
				changeRate = changeParamBT.getChangeRate();

			} catch (DataNotFoundException e) {
				final String errMsg = "T/B変換パラメータの取得に失敗。prodCode=";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
			}

			// 明細行DTO作成
			ManageInsWsPlanProdForVacDetailDto detail = new ManageInsWsPlanProdForVacDetailDto(insWsPlan, changeRate, isDeletePlan);
			detailList.add(detail);
		}

		// 登録可能かチェック(編集可能明細が1件でもあれば登録可能)
		boolean enableEntry = false;
		for (ManageInsWsPlanProdForVacDetailDto detailDto : detailList) {
			if (detailDto.getEnableEdit()) {
				enableEntry = true;
				break;
			}
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new ManageInsWsPlanProdForVacResultDto(insNo, tmsTytenCd, detailList, enableEntry);
	}
}
