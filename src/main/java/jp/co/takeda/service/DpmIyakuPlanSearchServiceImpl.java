package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.LOGICAL_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageIyakuPlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.ProdPlanResultDetailDto;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.ManageIyakuPlan;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 管理の組織別計画(全社)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmIyakuPlanSearchService")
public class DpmIyakuPlanSearchServiceImpl implements DpmIyakuPlanSearchService {

	/**
	 * 全社計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageIyakuPlanDao")
	protected ManageIyakuPlanDao manageIyakuPlanDao;

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
	 * 計画管理汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("codeMasterDao")
	protected CodeMasterDao codeMasterDao;

	// 組織別品目別計画(全社)取得
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

		final String category = scDto.getProdCategory();
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
		// 全社別計画取得
		// ----------------------
		// UH・P・Zの順番に、1品目当たり必ず3件ずつ取得できるはず(計画が登録されていない場合でも)
		List<ManageIyakuPlan> iyakuPlanList = manageIyakuPlanDao.searchList(ManageIyakuPlanDao.SORT_STRING, category, vaccineCode);

		// 明細行作成
		List<ProdPlanResultDetailDto> detailList = new ArrayList<ProdPlanResultDetailDto>();
		Iterator<ManageIyakuPlan> itr = iyakuPlanList.iterator();
		while (itr.hasNext()) {

			// 1件目(UH)取得
			ManageIyakuPlan iyakuPlanUh = (ManageIyakuPlan) itr.next();

			// 2件目(P)がない場合はデータ不整合
			if (!itr.hasNext()) {
				final String msg = "全社別計画取得に失敗(データ不整合)。" + "category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			// 2件目(P)取得
			ManageIyakuPlan iyakuPlanP = (ManageIyakuPlan) itr.next();

			// 3件目(Z)取得
			ManageIyakuPlan iyakuPlanZ = new ManageIyakuPlan();
			if (itr.hasNext()) {
				iyakuPlanZ = itr.next();
			}

			// データ不整合チェック
			if (iyakuPlanUh.getInsType() != InsType.UH || iyakuPlanP.getInsType() != InsType.P || iyakuPlanZ.getInsType() != InsType.ZATU) {
				final String msg = "全社別計画取得に失敗(データ不整合)。" + "category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}
			if (!StringUtils.equals(iyakuPlanUh.getProdCode(), iyakuPlanP.getProdCode()) || !StringUtils.equals(iyakuPlanUh.getProdCode(), iyakuPlanZ.getProdCode())) {
				final String msg = "全社別計画取得に失敗(データ不整合)。" + "category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

			final String prodCode = iyakuPlanUh.getProdCode();

			// ----------------------------------------------------
			// 下位立案(支店別計画)に対する品目立案レベル取得
			// ----------------------------------------------------
			boolean canMovePlanLevel;
			try {
				ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);
				canMovePlanLevel = plannedProd.getPlanLevelSiten();
			} catch (DataNotFoundException e1) {
				final String msg = "全社別計画取得に失敗(データ不整合)。" + "category" + category;
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
			ProdPlanResultDetailDto detail = new ProdPlanResultDetailDto(iyakuPlanUh, iyakuPlanP, iyakuPlanZ, changeParamYT, canMovePlanLevel);
			detailList.add(detail);
		}
		return new ProdPlanResultDto(detailList);
	}
}
