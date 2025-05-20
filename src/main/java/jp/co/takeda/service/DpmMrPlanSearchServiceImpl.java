package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
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
import jp.co.takeda.dao.DpmSyComInsOyakoDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageInsPlanDao;
import jp.co.takeda.dao.ManageMrPlanDao;
import jp.co.takeda.dao.ManageOfficePlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.ManageTeamPlanDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.ProdPlanResultDetailDto;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.ProdPlanSummaryResultDetailDto;
import jp.co.takeda.dto.ProdPlanSummaryResultDto;
import jp.co.takeda.dto.ProdPlanSummaryScDto;
import jp.co.takeda.dto.SosPlanResultDetailDto;
import jp.co.takeda.dto.SosPlanResultDetailTotalDto;
import jp.co.takeda.dto.SosPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.model.ManageOfficePlan;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.ManageTeamPlan;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 管理の組織別計画(担当者)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmMrPlanSearchService")
public class DpmMrPlanSearchServiceImpl implements DpmMrPlanSearchService {

	/**
	 * 営業所別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageOfficePlanDao")
	protected ManageOfficePlanDao manageOfficePlanDao;

	/**
	 * チーム別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageTeamPlanDao")
	protected ManageTeamPlanDao manageTeamPlanDao;

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
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpmSyComInsOyakoDao")
	protected DpmSyComInsOyakoDao dpmSyComInsOyakoDao;

	// 組織別計画取得
	public SosPlanResultDto searchSosPlan(SosPlanScDto scDto, String jgiNo) throws LogicalException {

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
		if (scDto.getSosCd3() == null && scDto.getSosCd4() == null) {
			final String errMsg = "検索対象の組織コード(営業所 or チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		final String prodCode = scDto.getProdCode();
		ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

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

		// ----------------------
		// T/Y変換率取得
		// ----------------------
		ChangeParamYT changeParamYT;
		try {
			SysManage sysManage = sysManageDao.search(SysClass.DPM, SysType.IYAKU);
			changeParamYT = manageChangeParamYTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理、または、T/Y変換パラメータの取得に失敗。prodCode=";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
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

		// -----------------------------
		// 上位計画・担当者別計画 取得
		// -----------------------------

		// 雑組織フラグ
		boolean etcSosFlg = false;
		// 集計行
		SosPlanResultDetailTotalDto detailTotal;
		// 明細担当者別計画
		List<ManageMrPlan> mrPlanList;

		if(BooleanUtils.isFalse(scDto.getOncSosFlg())){

			// 営業所所属チーム（4階層）の場合

			// -----------------------------
			// 雑組織フラグ取得
			// -----------------------------
			final String sosCd4 = scDto.getSosCd4();

			// 組織コード(チーム)から雑組織かどうかを取得
			SosMst sosMst = dpmSosJgiSearchService.searchSosMstWithEtcSos(sosCd4, BumonRank.TEAM.getDbValue().toString());
			if (sosMst != null && sosMst.getEtcSosFlg() != null) {
				etcSosFlg = sosMst.getEtcSosFlg();
			}

			// -----------------------------
			// チーム別計画取得
			// -----------------------------

			// UH取得
			ManageTeamPlan teamPlanUh;
			try {
				teamPlanUh = manageTeamPlanDao.searchUk(InsType.UH, prodCode, sosCd4);
			} catch (DataNotFoundException e) {
				teamPlanUh = new ManageTeamPlan();
			}

			// P取得
			ManageTeamPlan teamPlanP;
			try {
				teamPlanP = manageTeamPlanDao.searchUk(InsType.P, prodCode, sosCd4);
			} catch (DataNotFoundException e) {
				teamPlanP = new ManageTeamPlan();
			}

			// Z取得
			ManageTeamPlan teamPlanZ;
			try {
				teamPlanZ = manageTeamPlanDao.searchUk(InsType.ZATU, prodCode, sosCd4);
			} catch (DataNotFoundException e) {
				teamPlanZ = new ManageTeamPlan();
			}

			// 上位立案(チーム別計画)に対する品目立案レベル取得
			boolean upperPlanLevel = plannedProd.getPlanLevelTeam();

			// 明細合計行作成(チーム別計画)
			detailTotal = new SosPlanResultDetailTotalDto(teamPlanUh, teamPlanP, teamPlanZ, upperPlanLevel);

			// ----------------------
			// 担当者別計画取得
			// ----------------------
			// UH・Pの順番に、1担当者当たり必ず2件ずつ取得できるはず(計画が登録されていない場合でも)
			// mod Start 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
			//mrPlanList = manageMrPlanDao.searchListByProd(prodCode, sosCd4, BumonRank.TEAM, jgiNo, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode);
			mrPlanList = manageMrPlanDao.searchListByProd(prodCode, sosCd4, BumonRank.TEAM, jgiNo, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode, category);
            // mod End 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る

		} else {

			// ONC所属チーム（3階層）の場合

			// -----------------------------
			// 営業所計画取得
			// -----------------------------
			final String sosCd3 = scDto.getSosCd3();

			// UH取得
			ManageOfficePlan officePlanUh;
			try {
				officePlanUh = manageOfficePlanDao.searchUk(InsType.UH, prodCode, sosCd3);
			} catch (DataNotFoundException e) {
				officePlanUh = new ManageOfficePlan();
			}

			// P取得
			ManageOfficePlan officePlanP;
			try {
				officePlanP = manageOfficePlanDao.searchUk(InsType.P, prodCode, sosCd3);
			} catch (DataNotFoundException e) {
				officePlanP = new ManageOfficePlan();
			}

			// Z取得
			ManageOfficePlan officePlanZ;
			try {
				officePlanZ = manageOfficePlanDao.searchUk(InsType.ZATU, prodCode, sosCd3);
			} catch (DataNotFoundException e) {
				officePlanZ = new ManageOfficePlan();
			}

			// 上位立案(営業所別計画)に対する品目立案レベル取得
			boolean upperPlanLevel = plannedProd.getPlanLevelOffice();

			// 明細合計行作成(営業所計画)
			detailTotal = new SosPlanResultDetailTotalDto(officePlanUh, officePlanP, officePlanZ, upperPlanLevel);

			// ----------------------
			// 担当者別計画取得
			// ----------------------
			// UH・Pの順番に、1担当者当たり必ず3件ずつ取得できるはず(計画が登録されていない場合でも)
			// mod Start 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
			//mrPlanList = manageMrPlanDao.searchListByProd(prodCode, sosCd3, BumonRank.OFFICE_TOKUYAKUTEN_G, jgiNo, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode);
			mrPlanList = manageMrPlanDao.searchListByProd(prodCode, sosCd3, BumonRank.OFFICE_TOKUYAKUTEN_G, jgiNo, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(), oyakoKbProdCode, category);
            // mod End 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
		}

		// 下位立案(施設別計画)に対する品目立案レベル取得
		boolean canMovePlanLevel = plannedProd.getPlanLevelIns();

		// 明細行作成(担当者別計画)
		List<SosPlanResultDetailDto> detailList = new ArrayList<SosPlanResultDetailDto>();
		Iterator<ManageMrPlan> itr = mrPlanList.iterator();
		while (itr.hasNext()) {

			// 1件目(UH)取得
			ManageMrPlan mrPlanUh = (ManageMrPlan) itr.next();

			// 2件目(P)がない場合はデータ不整合
			if (!itr.hasNext()) {
				final String msg = "担当者別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode + " sosCd3=" + scDto.getSosCd3() + " sosCd4=" + scDto.getSosCd4();
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// 2件目(P)取得
			ManageMrPlan mrPlanP = (ManageMrPlan) itr.next();

			// 3件名(Z)取得
			ManageMrPlan mrPlanZ = new ManageMrPlan();
			if (itr.hasNext()) {
				mrPlanZ = (ManageMrPlan) itr.next();
			}

			// データ不整合チェック
			if (mrPlanUh.getInsType() != InsType.UH || mrPlanP.getInsType() != InsType.P || mrPlanZ.getInsType() != InsType.ZATU) {
				final String msg = "担当者別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode + " sosCd3=" + scDto.getSosCd3() + " sosCd4=" + scDto.getSosCd4();
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}
			if (!mrPlanUh.getJgiNo().equals(mrPlanP.getJgiNo()) || !mrPlanUh.getJgiNo().equals(mrPlanZ.getJgiNo())) {
				final String msg = "担当者別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode  + " sosCd3=" + scDto.getSosCd3() + " sosCd4=" + scDto.getSosCd4();
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			SosPlanResultDetailDto detail = new SosPlanResultDetailDto(mrPlanUh, mrPlanP, mrPlanZ, changeParamYT, canMovePlanLevel, etcSosFlg);
			detailList.add(detail);
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new SosPlanResultDto(detailTotal, detailList);
	}

	// 組織別品目別計画(担当者)取得
	public ProdPlanResultDto searchSosProdPlan(ProdPlanScDto scDto) throws LogicalException {

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

		// ----------------------
		// 組織とカテゴリの整合性チェック
		// ----------------------
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

		// ----------------------
		// 納入計画システム管理情取得
		// ----------------------
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPM, SysType.IYAKU);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理情報の取得に失敗。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
		plannedCtg = plannedCtgDao.search(category);

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		List<ManagePlannedProd> plannedProdList = managePlannedProdDao.searchListByCateDataSeq(category);

//		// ----------------------
//		// 担当者別計画取得
//		// ----------------------
//		// UH・P・Zの順番に、1品目当たり最大3件ずつ取得（施設区分で判断して格納する）
//		List<ManageMrPlan> mrPlanList = manageMrPlanDao.searchListByJgi(jgiNo, category, plannedCtg.getTgtInsKb(), plannedCtg.getOyakoKb()
//				, plannedCtg.getOyakoKb2(), oyakoKbProdCode, null);

		// 明細行作成
		List<ProdPlanResultDetailDto> detailList = new ArrayList<ProdPlanResultDetailDto>();
		ManageMrPlan mrPlanUh = new ManageMrPlan();
		ManageMrPlan mrPlanP = new ManageMrPlan();
		ManageMrPlan mrPlanZ = new ManageMrPlan();

		for(int h = 0; h < plannedProdList.size();h++) {

			final String prodCode = plannedProdList.get(h).getProdCode();

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

			// ----------------------
			// 担当者別計画取得
			// ----------------------
			// 1品目ごとに、UH・P・Zの順番に、1品目当たり最大3件ずつ取得（施設区分で判断して格納する）
			List<ManageMrPlan> mrPlanList = manageMrPlanDao.searchListByJgi(jgiNo, null, plannedCtg.getTgtInsKb(), plannedCtg.getOyakoKb()
					, plannedCtg.getOyakoKb2(), oyakoKbProdCode, prodCode);

			for(int i = 0; i < mrPlanList.size();i++) {

				switch(mrPlanList.get(i).getInsType()) {
					case UH:
						mrPlanUh = mrPlanList.get(i);
						break;
					case P:
						mrPlanP = mrPlanList.get(i);
						break;
					case ZATU:
						mrPlanZ = mrPlanList.get(i);
						break;
				}

				// 次の要素がない もしくは 次の要素の品目固定コードが異なる場合
				if(((i != mrPlanList.size() -1)
						&& (mrPlanList.get(i).getProdCode().equals(mrPlanList.get(i + 1).getProdCode()) == false))
					|| (i == mrPlanList.size() -1)) {

//					final String prodCode = mrPlanUh.getProdCode();

					// ----------------------------------------------------
					// 下位立案(施設別計画)に対する品目立案レベル取得
					// ----------------------------------------------------
					boolean canMovePlanLevel;
					try {
						ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);
						canMovePlanLevel = plannedProd.getPlanLevelIns();
					} catch (DataNotFoundException e1) {
						final String msg = "チーム別計画取得に失敗(データ不整合)。" + "jgiNo=" + jgiNo + ",category" + category;
						throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
					}

					// ----------------------
					// T/Y変換率取得
					// ----------------------
					ChangeParamYT changeParamYT;
					try {
						changeParamYT = manageChangeParamYTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
					} catch (DataNotFoundException e) {
						final String errMsg = "T/Y変換パラメータの取得に失敗。prodCode=";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
					}

					// DTO作成
					ProdPlanResultDetailDto detail = new ProdPlanResultDetailDto(mrPlanUh, mrPlanP, mrPlanZ, changeParamYT, canMovePlanLevel);
					detailList.add(detail);


					// 初期化
					mrPlanUh = new ManageMrPlan();
					mrPlanP = new ManageMrPlan();
					mrPlanZ = new ManageMrPlan();

				}

			}
		}


/*
 * バリデート部分が不明であるため、いったんコメントアウト。
 * 単体完了後には削除する
 		Iterator<ManageMrPlan> itr = mrPlanList.iterator();

		while (itr.hasNext()) {

			// 1件目(UH)取得
			ManageMrPlan mrPlanUh = (ManageMrPlan) itr.next();

			// 2件目(P)がない場合はデータ不整合
			if (!itr.hasNext()) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "jgiNo=" + jgiNo + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// 2件目(P)取得
			ManageMrPlan mrPlanP = (ManageMrPlan) itr.next();

			// データ不整合チェック
			if (mrPlanUh.getInsType() != InsType.UH || mrPlanP.getInsType() != InsType.P) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "jgiNo=" + jgiNo + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}
			if (!StringUtils.equals(mrPlanUh.getProdCode(), mrPlanP.getProdCode())) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "jgiNo=" + jgiNo + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			final String prodCode = mrPlanUh.getProdCode();

			// ----------------------------------------------------
			// 下位立案(施設別計画)に対する品目立案レベル取得
			// ----------------------------------------------------
			boolean canMovePlanLevel;
			try {
				ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);
				canMovePlanLevel = plannedProd.getPlanLevelIns();
			} catch (DataNotFoundException e1) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "jgiNo=" + jgiNo + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// ----------------------
			// T/Y変換率取得
			// ----------------------
			ChangeParamYT changeParamYT;
			try {
				changeParamYT = manageChangeParamYTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
			} catch (DataNotFoundException e) {
				final String errMsg = "T/Y変換パラメータの取得に失敗。prodCode=";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
			}

			// DTO作成
			ProdPlanResultDetailDto detail = new ProdPlanResultDetailDto(mrPlanUh, mrPlanP, changeParamYT, canMovePlanLevel);
			detailList.add(detail);
		}
*/
		return new ProdPlanResultDto(detailList);
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
					mrPlanP = mrPlanList.get(i);
					break;
				case ZATU:
					mrPlanZ = mrPlanList.get(i);
					break;
			}

			// 次の要素がない もしくは 次の要素の品目固定コードが異なる場合
			if((i == mrPlanList.size() -1)
					|| (mrPlanList.get(i).getProdCode().equals(mrPlanList.get(i + 1).getProdCode()) == false)) {

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

}
