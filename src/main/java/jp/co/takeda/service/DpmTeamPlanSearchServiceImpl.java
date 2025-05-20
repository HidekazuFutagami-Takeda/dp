package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageMrPlanDao;
import jp.co.takeda.dao.ManageOfficePlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.ManageTeamPlanDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.ProdPlanResultDetailDto;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosPlanResultDetailDto;
import jp.co.takeda.dto.SosPlanResultDetailTotalDto;
import jp.co.takeda.dto.SosPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.ManageOfficePlan;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.ManageTeamPlan;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 管理の組織別計画(チーム)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmTeamPlanSearchService")
public class DpmTeamPlanSearchServiceImpl implements DpmTeamPlanSearchService {

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

	// 組織別計画取得
	public SosPlanResultDto searchSosPlan(SosPlanScDto scDto) throws LogicalException {

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
		if (scDto.getProdCode() == null) {
			final String errMsg = "検索対象の組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		final String prodCode = scDto.getProdCode();
		ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
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
		// 計画立案レベル・チームが設定されていない場合はエラー
		if (!plannedProd.getPlanLevelTeam()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1001E", plannedProd.getProdName(), "チーム計画")));
		}

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
		SosPlanResultDetailTotalDto detailTotal = new SosPlanResultDetailTotalDto(officePlanUh, officePlanP, officePlanZ, upperPlanLevel);

		// ----------------------
		// チーム別計画取得
		// ----------------------
		// UH・Pの順番に、1チーム当たり必ず2件ずつ取得できるはず(計画が登録されていない場合でも)
		List<ManageTeamPlan> teamPlanList = manageTeamPlanDao.searchListByProd(ManageTeamPlanDao.SORT_STRING, prodCode, sosCd3);

		// 下位立案(担当者別計画)に対する品目立案レベル取得
		boolean canMovePlanLevel = plannedProd.getPlanLevelMr();

		// 明細行作成(チーム別計画)
		List<SosPlanResultDetailDto> detailList = new ArrayList<SosPlanResultDetailDto>();
		Iterator<ManageTeamPlan> itr = teamPlanList.iterator();
		while (itr.hasNext()) {

			// 1件目(UH)取得
			ManageTeamPlan teamPlanUh = (ManageTeamPlan) itr.next();

			// 2件目(P)がない場合はデータ不整合
			if (!itr.hasNext()) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode + " sosCd3=" + sosCd3;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// 2件目(P)取得
			ManageTeamPlan teamPlanP = (ManageTeamPlan) itr.next();

			// 3件名(Z)取得
			ManageTeamPlan teamPlanZ = new ManageTeamPlan();
			if (itr.hasNext()) {
				teamPlanZ = (ManageTeamPlan) itr.next();
			}

			// データ不整合チェック
			if (teamPlanUh.getInsType() != InsType.UH || teamPlanP.getInsType() != InsType.P) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode + " sosCd3=" + sosCd3;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}
			if (!StringUtils.equals(teamPlanUh.getSosCd(), teamPlanP.getSosCd())) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode + " sosCd3=" + sosCd3;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			SosPlanResultDetailDto detail = new SosPlanResultDetailDto(teamPlanUh, teamPlanP, teamPlanZ, changeParamYT, canMovePlanLevel);
			detailList.add(detail);
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new SosPlanResultDto(detailTotal, detailList);
	}

	// 組織別品目別計画(チーム)取得
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
		if (scDto.getSosCd4() == null) {
			final String errMsg = "検索対象の組織コード(チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String category = scDto.getProdCategory();
		final String sosCd4 = scDto.getSosCd4();

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
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

		// ----------------------
		// チーム別計画取得
		// ----------------------
		// UH・Pの順番に、1品目当たり必ず2件ずつ取得できるはず(計画が登録されていない場合でも)
		List<ManageTeamPlan> teamPlanList = manageTeamPlanDao.searchListBySos(ManageTeamPlanDao.SORT_STRING2, sosCd4, category);

		// 明細行作成
		List<ProdPlanResultDetailDto> detailList = new ArrayList<ProdPlanResultDetailDto>();
		Iterator<ManageTeamPlan> itr = teamPlanList.iterator();
		while (itr.hasNext()) {

			// 1件目(UH)取得
			ManageTeamPlan teamPlanUh = (ManageTeamPlan) itr.next();

			// 2件目(P)がない場合はデータ不整合
			if (!itr.hasNext()) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "sosCd4=" + sosCd4 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// 2件目(P)取得
			ManageTeamPlan teamPlanP = (ManageTeamPlan) itr.next();

			// 3件目(Z)取得
			ManageTeamPlan teamPlanZ = new ManageTeamPlan();
			if (itr.hasNext()) {
				teamPlanZ = itr.next();
			}

			// データ不整合チェック
			if (teamPlanUh.getInsType() != InsType.UH || teamPlanP.getInsType() != InsType.P || teamPlanZ.getInsType() != InsType.ZATU) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "sosCd4=" + sosCd4 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}
			if (!StringUtils.equals(teamPlanUh.getProdCode(), teamPlanP.getProdCode()) || !StringUtils.equals(teamPlanUh.getProdCode(), teamPlanZ.getProdCode())) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "sosCd4=" + sosCd4 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			final String prodCode = teamPlanUh.getProdCode();

			// ----------------------------------------------------
			// 下位立案(担当者別計画)に対する品目立案レベル取得
			// ----------------------------------------------------
			boolean canMovePlanLevel;
			try {
				ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);
				canMovePlanLevel = plannedProd.getPlanLevelMr();
			} catch (DataNotFoundException e1) {
				final String msg = "チーム別計画取得に失敗(データ不整合)。" + "sosCd4=" + sosCd4 + ",category" + category;
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
			ProdPlanResultDetailDto detail = new ProdPlanResultDetailDto(teamPlanUh, teamPlanP, teamPlanZ, changeParamYT, canMovePlanLevel);
			detailList.add(detail);
		}

		return new ProdPlanResultDto(detailList);
	}

}
