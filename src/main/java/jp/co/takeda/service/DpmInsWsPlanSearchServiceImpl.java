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
import jp.co.takeda.dao.ManageInsWsPlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.ManageInsWsPlanDetailInsDto;
import jp.co.takeda.dto.ManageInsWsPlanDetailWsDto;
import jp.co.takeda.dto.ManageInsWsPlanDto;
import jp.co.takeda.dto.ManageInsWsPlanHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdDetailDto;
import jp.co.takeda.dto.ManageInsWsPlanProdDto;
import jp.co.takeda.dto.ManageInsWsPlanProdHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdScDto;
import jp.co.takeda.dto.ManageInsWsPlanScDto;
import jp.co.takeda.logic.CheckInsNoLogic;
import jp.co.takeda.logic.CreateInsMstScResultDtoLogic;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.logic.DelInsLogic;
import jp.co.takeda.logic.GetChangeParamYTLogic;
import jp.co.takeda.logic.MallLogic;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.ManageInsWsPlan;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;

/**
 * 【管理】特約店別計画検索サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmInsWsPlanSearchService")
public class DpmInsWsPlanSearchServiceImpl implements DpmInsWsPlanSearchService {

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	protected ManagePlannedProdDao managePlannedProdDao;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstRealDao")
	protected JgiMstRealDao jgiMstRealDao;

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstRealDao")
	protected InsMstRealDao insMstRealDao;

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamYTDao")
	protected ManageChangeParamYTDao manageChangeParamYTDao;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnRealDao")
	protected TmsTytenMstUnRealDao tmsTytenMstUnRealDao;

	/**
	 * 管理の施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsWsPlanDao")
	protected ManageInsWsPlanDao manageInsWsPlanDao;

	/**
	 * 計画対象カテゴリ領域DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedCtgDao")
	protected PlannedCtgDao plannedCtgDao;

	/**
	 * 汎用コードマスターDAO
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

	// ヘッダ検索
	public ManageInsWsPlanHeaderDto searchHeader(ManageInsWsPlanScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		InsType insType = scDto.getInsType();
		if (insType == null) {
			final String errMsg = "検索対象の施設出力対象区分がnull";
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

		// -----------------------------
		// 施設情報取得
		// -----------------------------
		String insNo = scDto.getInsNo();
		InsMstResultDto insMstResultDto = null;
		JgiMst jgi = null;
		if (insNo != null) {
			try {
				// 施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
				InsMst insMst = logic.executeInsProdOyako(insNo, scDto.getProdCode(),plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(), oyakoKbProdCode);
				// 対象区分を施設の対象区分に置き換える。
				insType = InsType.convertInsType(insMst.getHoInsType());
				insMstResultDto = new CreateInsMstScResultDtoLogic(insMst).convert();
				// 担当従業員取得
				jgi = jgiMstRealDao.searchReal(insMstResultDto.getJgiNo());
			} catch (LogicalException e) {
			}
		}

		// -----------------------------
		// 特約店情報取得
		// -----------------------------
		// 特約店コードを13桁に変換
		final String tmsTytenCd = scDto.getTmsTytenCd();
		String tmsTytenName = null;
		if (tmsTytenCd != null) {
			final String tmsTytenCd13 = new CreateTmsTytenCdLogic(tmsTytenCd).execute();
			try {
				TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd13);
				tmsTytenName = tmsTytenMstUn.getTmsTytenMeiKj();
			} catch (DataNotFoundException e) {
			}
		}

		return new ManageInsWsPlanHeaderDto(insMstResultDto, tmsTytenCd, tmsTytenName, insType, jgi);
	}

	// 施設特約店別計画一覧検索
	public ManageInsWsPlanDto searchList(ManageInsWsPlanScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodCode = scDto.getProdCode();
		if (prodCode == null) {
			final String errMsg = "検索対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		InsType insType = scDto.getInsType();
		if (insType == null) {
			final String errMsg = "検索対象の施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getPlanData() == null) {
			final String errMsg = "検索対象の計画検索範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// 従業員番号
		Integer jgiNo = scDto.getJgiNo();
		// 組織コード(営業所)
		String sosCd3 = scDto.getSosCd3();
		// 組織コード(チーム)
		String sosCd4 = scDto.getSosCd4();
		// 特約店コード（前方一致）
		String tmsTytenCdPart = scDto.getTmsTytenCd();

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

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

		// -----------------------------
		// 施設情報取得
		// -----------------------------
		// 施設コード
		String insNo = scDto.getInsNo();
		// 施設内部コード
		String relnInsNo = null;
		// 設定中のユーザの配下の担当者か
		boolean mySosMr = true;

// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//		CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
		CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
//mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応

		// 施設情報のチェック
		if (insNo != null) {
			InsMst insMst = logic.executeInsProdOyako(insNo, prodCode,plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(), oyakoKbProdCode);
			// 対象区分、従業員番号を施設の情報に置き換える。
			insType = InsType.convertInsType(insMst.getHoInsType());
			jgiNo = insMst.getJgiNo();
			relnInsNo = insMst.getRelnInsNo();
			sosCd3 = null;
			sosCd4 = null;
		}

		// 設定されている担当者の従業員情報取得
		if(jgiNo != null) {

			JgiMst jgiMst;
			try {
				jgiMst = jgiMstRealDao.searchReal(jgiNo);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPM1004E")));
			}

			// 編集可能な担当者かチェック
			try {
				logic.validateMr(jgiMst);
			} catch (LogicalException e) {
				mySosMr = false;
			}
		}

		// -----------------------------
		// T/Y変換情報取得
		// -----------------------------
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		GetChangeParamYTLogic tylogic = new GetChangeParamYTLogic(sysManage, manageChangeParamYTDao);
		Double param = tylogic.execute(insType, prodCode);

		// -----------------------------
		// 施設特約店別計画取得
		// -----------------------------
		boolean allInsFlg = false;
		if (scDto.getPlanData().equals(PlanData.ALL)) {
			allInsFlg = true;
		}
		List<ManageInsWsPlan> insWsPlanList = new ArrayList<ManageInsWsPlan>();
		insWsPlanList = manageInsWsPlanDao.searchListByProd(ManageInsWsPlanDao.SORT_STRING, prodCode, sosCd3, sosCd4, jgiNo, insType, relnInsNo, allInsFlg, null,plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(),plannedCtg.getTgtInsKb(), oyakoKbProdCode);

		// -----------------------------
		// モール施設のフィルター
		// -----------------------------
		MallLogic mollLogic = new MallLogic();
		insWsPlanList = mollLogic.filterManageInsWsPlanList(insWsPlanList);

		// -----------------------------
		// 結果リスト生成取得
		// -----------------------------
		// 登録可能フラグ
		boolean enableEntry = false;
		// 明細行の生成
		List<ManageInsWsPlanDetailInsDto> detailList = new ArrayList<ManageInsWsPlanDetailInsDto>();
		for (int i = 0; i < insWsPlanList.size(); i++) {
			ManageInsWsPlan manageInsWsPlan = insWsPlanList.get(i);
			String oldInsNo = manageInsWsPlan.getInsNo();
			String newInsNo = manageInsWsPlan.getInsNo();
			// 特約店明細リスト
			List<ManageInsWsPlanDetailWsDto> wsList = new ArrayList<ManageInsWsPlanDetailWsDto>();
			// 特約店コードリスト
			List<String> tmsCodeList = new ArrayList<String>();
			// 施設名称
			String _insName = manageInsWsPlan.getInsAbbrName();
			// 施設固定コード
			String _insNo = manageInsWsPlan.getInsNo();
			// 削除予定施設フラグ
			boolean _delInsFlg = new DelInsLogic(manageInsWsPlan.getReqFlg(), manageInsWsPlan.getInsDelFlg()).getDelFlg();
			if (manageInsWsPlan.getSeqKey() != null) {
				boolean roopFlg = true;
				while (roopFlg) {
					String tmsTytenCd = manageInsWsPlan.getTmsTytenCd();
					if (tmsTytenCdPart == null || (tmsTytenCdPart != null && tmsTytenCd.startsWith(tmsTytenCdPart))) {
						// 特約店明細行生成
						wsList.add(createDetailDto(manageInsWsPlan));
					}
					// 特約店コードリスト追加
					tmsCodeList.add(tmsTytenCd);
					i++;
					// 最大サイズまで達した場合、終了
					if (i == insWsPlanList.size()) {
						roopFlg = false;
						break;
					}
					// 自レコード取得
					manageInsWsPlan = insWsPlanList.get(i);
					newInsNo = manageInsWsPlan.getInsNo();
					// 施設コードが一致しない場合、ループ終了
					// レコードを前に戻す
					if (!oldInsNo.equals(newInsNo)) {
						roopFlg = false;
						i--;
					}
				}
				if (wsList.size() == 0) {
					wsList = null;
				}
			} else {
				wsList = null;
				tmsCodeList = null;
			}
			// 計画ありのみが検索対象で、明細が無い場合は追加しない。
			if (scDto.getPlanData().equals(PlanData.PLAN) && wsList == null) {
				continue;
			}
			// 編集可能フラグ
			boolean enableEdit = true;
			if (tmsCodeList == null && _delInsFlg) {
				// 削除施設の場合、かつ特約店コードリストがない場合
				enableEdit = false;
			} else if(!mySosMr) {
				// 施設担当者が設定ユーザの組織配下ではない場合
				enableEdit = false;
			} else {
				// 1件でも編集可能な場合、登録可能
				enableEntry = true;
			}
			detailList.add(new ManageInsWsPlanDetailInsDto(_insName, _insNo, wsList, tmsCodeList, _delInsFlg, enableEdit));
		}
		// 明細が無い場合は、データなし。
		if (detailList.size() == 0) {
			throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR));
		}

		return new ManageInsWsPlanDto(plannedProd.getProdName(), param, detailList, enableEntry, mySosMr);
	}

	/**
	 * 明細DTO生成
	 *
	 *
	 * @param manageInsWsPlan
	 * @return 明細DTO
	 */
	private ManageInsWsPlanDetailWsDto createDetailDto(ManageInsWsPlan manageInsWsPlan) {
		// 特約店名称
		String tmsTytemName = manageInsWsPlan.getTmsTytenName();
		// ＴＭＳ特約店コード
		String tmsTytenCd = manageInsWsPlan.getTmsTytenCd();
		// Y価ベース
		Long baseY = ConvertUtil.parseMoneyToThousandUnit(manageInsWsPlan.getImplPlan().getPlanned2ValueY());
		// T価ベース（編集前）
		Long beforeBaseT = ConvertUtil.parseMoneyToThousandUnit(manageInsWsPlan.getImplPlan().getPlanned2ValueT());
		// T価ベース
		Long baseT = ConvertUtil.parseMoneyToThousandUnit(manageInsWsPlan.getImplPlan().getPlanned2ValueT());
		// シーケンスキー
		Long seqKey = manageInsWsPlan.getSeqKey();
		// 最終更新者
		String upJgiName = null;
		if (manageInsWsPlan.getUpDate() != null) {
			upJgiName = manageInsWsPlan.getUpJgiName();
		}
		// 最終更新日時
		Date upDate = manageInsWsPlan.getUpDate();

		boolean planTaiGaiFlgTok = false;
		if (StringUtils.isNotBlank(tmsTytenCd)) {
			try {
				TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd);
				planTaiGaiFlgTok = tmsTytenMstUn.getPlanTaiGaiFlgTok();
			} catch (DataNotFoundException e) {
			}
		}

		return new ManageInsWsPlanDetailWsDto(tmsTytemName, tmsTytenCd, baseY, beforeBaseT, baseT, seqKey, upJgiName, upDate, planTaiGaiFlgTok);
	}

	// ヘッダ検索
	public ManageInsWsPlanProdHeaderDto searchHeader(ManageInsWsPlanProdScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 施設情報取得
		// -----------------------------
		String insNo = scDto.getInsNo();
		InsMstResultDto insMstResultDto = null;
		if (insNo != null) {
			try {
				// 施設情報のチェック
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao);
				CheckInsNoLogic logic = new CheckInsNoLogic(jgiMstRealDao, insMstRealDao, dpmInsJgiSosDao);
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
				InsMst insMst = logic.executeInsProd(insNo, null);
				insMstResultDto = new CreateInsMstScResultDtoLogic(insMst).convert();
			} catch (LogicalException e) {
			}
		}

		// -----------------------------
		// 特約店情報取得
		// -----------------------------
		// 特約店コードを13桁に変換
		final String tmsTytenCd = scDto.getTmsTytenCd();
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

		String prodCategoryName = null;
		if (StringUtils.isNotBlank(scDto.getProdCategory())) {
			//prodCategoryName = ProdCategory.getProdCategoryName(ProdCategory.getInstance(scDto.getProdCategory()));
			prodCategoryName = codeMasterDao.searchOneCategory(CodeMaster.CAT.getDbValue(), scDto.getProdCategory()).getDataName();
		}
		return new ManageInsWsPlanProdHeaderDto(insMstResultDto, tmsTytenCd, tmsTytenName, prodCategoryName, planTaiGaiFlgTok);
	}

	// 品目一覧検索
	public ManageInsWsPlanProdDto searchList(ManageInsWsPlanProdScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// 施設コード
		String insNo = scDto.getInsNo();
		// 特約店コード
		String tmsTytenCd = new CreateTmsTytenCdLogic(scDto.getTmsTytenCd()).execute();;
		// 品目カテゴリ
		DpmCCdMst category = codeMasterDao.searchOneCategory(CodeMaster.CAT.getDbValue(), scDto.getProdCategory());
		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "検索対象の特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "検索対象の品目カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getPlanData() == null) {
			final String errMsg = "検索対象の計画検索範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ログイン情報
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

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
		// 特約店情報取得
		// -----------------------------
		tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd);

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
		plannedCtg = plannedCtgDao.search(scDto.getProdCategory());

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
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
		String sales = null;
		if(scDto.getProdCategory().equals(vaccineCode)) {
			sales = Sales.VACCHIN.getDbValue();
		}else{
			sales = Sales.IYAKU.getDbValue();
		}

		// -----------------------------
		// 施設特約店別計画取得
		// -----------------------------
		boolean allProdFlg = false;
		if (scDto.getPlanData().equals(PlanData.ALL)) {
			allProdFlg = true;
		}
		List<ManageInsWsPlan> insWsPlanList = manageInsWsPlanDao.searchListByInsWs(ManageInsWsPlanDao.SORT_STRING2, insNo, tmsTytenCd, category.getDataCd(), allProdFlg, plannedCtg.getTgtInsKb(), sales);

		// -----------------------------
		// 結果リスト生成
		// -----------------------------
		// 登録可能フラグ
		boolean enableEntry = false;
		// 明細生成
		List<ManageInsWsPlanProdDetailDto> detailList = new ArrayList<ManageInsWsPlanProdDetailDto>();
		for (ManageInsWsPlan manageInsWsPlan : insWsPlanList) {
			// 品目名
			String prodName = manageInsWsPlan.getProdName();
			// 品目コード
			String prodCode = manageInsWsPlan.getProdCode();
			// Y価ベース
			Long baseY = ConvertUtil.parseMoneyToThousandUnit(manageInsWsPlan.getImplPlan().getPlanned2ValueY());
			// T価ベース
			Long baseT = ConvertUtil.parseMoneyToThousandUnit(manageInsWsPlan.getImplPlan().getPlanned2ValueT());
			// シーケンスキー
			Long seqKey = manageInsWsPlan.getSeqKey();
			// 最終更新者
			String upJgiName = null;
			if (manageInsWsPlan.getUpDate() != null) {
				upJgiName = manageInsWsPlan.getUpJgiName();
			}
			// 最終更新日時
			Date upDate = manageInsWsPlan.getUpDate();
			// T/Y変換率取得
			GetChangeParamYTLogic tylogic = new GetChangeParamYTLogic(sysManage, manageChangeParamYTDao);
			Double changeRate = tylogic.execute(insType, prodCode);

			// 設定中のユーザの配下であれば編集可能
			boolean mySosMr = true;
			try {
				insChecklogic.validateMr(manageInsWsPlan.getJgiMst());
			} catch (LogicalException e) {
				mySosMr = false;
			}

			boolean enableEdit = true;
			if (isDeletePlan && seqKey == null) {
				// 削除施設 かつ シーケンスがnullの場合は「編集不可」
				enableEdit = false;
			} else if(!mySosMr) {
				// 施設担当者が設定ユーザの組織配下ではない場合は「編集不可」
				enableEdit = false;
			} else {
				enableEdit = true;
				// 1件でも編集可能であれば、登録可能
				enableEntry = true;
			}
			detailList.add(new ManageInsWsPlanProdDetailDto(prodName, prodCode, baseY, baseT, baseT, seqKey, upJgiName, upDate, changeRate, isDeletePlan, enableEdit));
		}

		return new ManageInsWsPlanProdDto(insNo, tmsTytenCd, category.getDataName(), detailList, enableEntry);
	}

}
