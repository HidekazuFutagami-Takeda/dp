package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.LOGICAL_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

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
import jp.co.takeda.dao.ManageBranchPlanDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageIyakuPlanDao;
import jp.co.takeda.dao.ManageOfficePlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.ProdPlanResultDetailDto;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosPlanResultDetailDto;
import jp.co.takeda.dto.SosPlanResultDetailTotalDto;
import jp.co.takeda.dto.SosPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.ManageBranchPlan;
import jp.co.takeda.model.ManageIyakuPlan;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 管理の組織別計画(支店)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmBranchPlanSearchService")
public class DpmBranchPlanSearchServiceImpl implements DpmBranchPlanSearchService {

	/**
	 * 全社計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageIyakuPlanDao")
	protected ManageIyakuPlanDao manageIyakuPlanDao;

	/**
	 * 支店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageBranchPlanDao")
	protected ManageBranchPlanDao manageBranchPlanDao;

	/**
	 * 営業所別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageOfficePlanDao")
	protected ManageOfficePlanDao manageOfficePlanDao;

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

	// 組織別計画(支店)取得
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

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		final String prodCode = scDto.getProdCode();
		MasterManagePlannedProd plannedProd = masterManagePlannedProdDao.search(prodCode);

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
		// 計画立案レベル・支店が設定されていない場合はエラー
		if (!plannedProd.getPlanLevelSiten()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1001E", plannedProd.getProdName(), "支店計画")));
		}

		// -----------------------------
		// 全社計画取得
		// -----------------------------
		// UH取得
		ManageIyakuPlan iyakuPlanUh;
		try {
			iyakuPlanUh = manageIyakuPlanDao.searchUk(InsType.UH, prodCode);
		} catch (DataNotFoundException e) {
			iyakuPlanUh = new ManageIyakuPlan();
		}

		// P取得
		ManageIyakuPlan iyakuPlanP;
		try {
			iyakuPlanP = manageIyakuPlanDao.searchUk(InsType.P, prodCode);
		} catch (DataNotFoundException e) {
			iyakuPlanP = new ManageIyakuPlan();
		}

		// Z取得
		ManageIyakuPlan iyakuPlanZ;
		try {
			iyakuPlanZ = manageIyakuPlanDao.searchUk(InsType.ZATU, prodCode);
		} catch (DataNotFoundException e) {
			iyakuPlanZ = new ManageIyakuPlan();
		}

		// 上位立案(全社計画)に対する品目立案レベル取得
		boolean upperPlanLevel = plannedProd.getPlanLevelZensha();

		// 明細合計行作成(全社計画)
		SosPlanResultDetailTotalDto detailTotal = new SosPlanResultDetailTotalDto(iyakuPlanUh, iyakuPlanP, iyakuPlanZ, upperPlanLevel);

		// ----------------------
		// 支店別計画取得
		// ----------------------
		// UH・P・Zの順番に、1支店当たり必ず3件ずつ取得できるはず(計画が登録されていない場合でも)
		List<ManageBranchPlan> branchPlanList = manageBranchPlanDao.searchListByProd(ManageBranchPlanDao.SORT_STRING, prodCode, plannedProd.getCategory());

		// 下位立案(営業所別計画)に対する品目立案レベル取得
		boolean canMovePlanLevel = plannedProd.getPlanLevelOffice();

		// 明細行作成(支店別計画)
		List<SosPlanResultDetailDto> detailList = new ArrayList<SosPlanResultDetailDto>();
		Iterator<ManageBranchPlan> itr = branchPlanList.iterator();
		while (itr.hasNext()) {

			// 1件目(UH)取得
			ManageBranchPlan branchPlanUh = (ManageBranchPlan) itr.next();

			// 2件目(P)がない場合はデータ不整合
			if (!itr.hasNext()) {
				final String msg = "支店別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// 2件目(P)取得
			ManageBranchPlan branchPlanP = (ManageBranchPlan) itr.next();

			// 3件名(Z)取得
			ManageBranchPlan branchPlanZ = new ManageBranchPlan();
			if (itr.hasNext()) {
				branchPlanZ = (ManageBranchPlan) itr.next();
			}

			// データ不整合チェック
			if (branchPlanUh.getInsType() != InsType.UH || branchPlanP.getInsType() != InsType.P || branchPlanZ.getInsType() != InsType.ZATU) {
				final String msg = "支店別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}
			if (!StringUtils.equals(branchPlanUh.getSosCd(), branchPlanP.getSosCd()) || !StringUtils.equals(branchPlanUh.getSosCd(), branchPlanZ.getSosCd())) {
				final String msg = "支店別計画取得に失敗(データ不整合)。" + "prodCode=" + prodCode;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			SosPlanResultDetailDto detail = new SosPlanResultDetailDto(branchPlanUh, branchPlanP, branchPlanZ, changeParamYT, canMovePlanLevel);
			detailList.add(detail);
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new SosPlanResultDto(detailTotal, detailList);
	}

	// 組織別品目別計画(支店)取得
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
		if (scDto.getSosCd2() == null) {
			final String errMsg = "検索対象の組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String category = scDto.getProdCategory();
		final String sosCd2 = scDto.getSosCd2();

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
		// 支店別計画取得
		// ----------------------
		// UH・P・Zの順番に、1品目当たり必ず3件ずつ取得できるはず(計画が登録されていない場合でも)
		List<ManageBranchPlan> branchPlanList = manageBranchPlanDao.searchListBySos(ManageBranchPlanDao.SORT_STRING2, sosCd2, category);

		// 明細行作成
		List<ProdPlanResultDetailDto> detailList = new ArrayList<ProdPlanResultDetailDto>();
		Iterator<ManageBranchPlan> itr = branchPlanList.iterator();
		while (itr.hasNext()) {

			// 1件目(UH)取得
			ManageBranchPlan branchPlanUh = (ManageBranchPlan) itr.next();

			// 2件目(P)がない場合はデータ不整合
			if (!itr.hasNext()) {
				final String msg = "支店別計画取得に失敗(データ不整合)。" + "sosCd2=" + sosCd2 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// 2件目(P)取得
			ManageBranchPlan branchPlanP = (ManageBranchPlan) itr.next();

			// 3件名(Z)取得
			ManageBranchPlan branchPlanZ = new ManageBranchPlan();
			if (itr.hasNext()) {
				branchPlanZ = (ManageBranchPlan) itr.next();
			}

			// データ不整合チェック
			if (branchPlanUh.getInsType() != InsType.UH || branchPlanP.getInsType() != InsType.P || branchPlanZ.getInsType() != InsType.ZATU) {
				final String msg = "支店別計画取得に失敗(データ不整合)。" + "sosCd2=" + sosCd2 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}
			if (!StringUtils.equals(branchPlanUh.getProdCode(), branchPlanP.getProdCode()) || !StringUtils.equals(branchPlanUh.getProdCode(), branchPlanZ.getProdCode())) {
				final String msg = "支店別計画取得に失敗(データ不整合)。" + "sosCd2=" + sosCd2 + ",category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			final String prodCode = branchPlanUh.getProdCode();

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
			ProdPlanResultDetailDto detail = new ProdPlanResultDetailDto(branchPlanUh, branchPlanP, branchPlanZ, changeParamYT, canMovePlanLevel);
			detailList.add(detail);
		}

		return new ProdPlanResultDto(detailList);
	}
}
