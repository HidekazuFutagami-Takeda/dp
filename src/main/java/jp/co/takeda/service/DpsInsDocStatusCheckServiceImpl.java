package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.InsDocStatusCheckDto;
import jp.co.takeda.dto.InsDocStatusCheckResultDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.InsDocPlanStatusCheckByMrLogic;
import jp.co.takeda.logic.InsDocPlanStatusCheckBySosLogic;
import jp.co.takeda.logic.div.InsDocStatusForCheck;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;

/**
 * 施設医師別計画ステータスのステータスチェックを行なうサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsInsDocStatusCheckService")
public class DpsInsDocStatusCheckServiceImpl implements DpsInsDocStatusCheckService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsInsDocStatusCheckServiceImpl.class);

	/**
	 * 施設医師別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanStatusDao")
	protected InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 従業員DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	// 組織コード(営業所)、計画対象品目のリストを指定して、施設医師別計画のチェックを行なう。
	public List<InsDocPlanStatus> execute(String sosCd3, List<PlannedProd> plannedProdList, List<InsDocStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : plannedProdList) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の品目を取得（重点フラグ含めるため）
		// ----------------------
		List<PlannedProd> newPlannedProdList = new ArrayList<PlannedProd>();
		for (PlannedProd plannedProd : plannedProdList) {
			try {
				newPlannedProdList.add(plannedProdDAO.search(plannedProd.getProdCode()));
			} catch (DataNotFoundException e) {
				final String errMsg = "品目固定コードが不明:" + plannedProd.getProdCode();
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の従業員取得
		// ----------------------
		// 営業所配下の従業員取得
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
		} catch (DataNotFoundException e) {
			return new ArrayList<InsDocPlanStatus>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設医師別計画ステータスチェックDTO作成
		InsDocStatusCheckDto insDocStatusCheckDto = new InsDocStatusCheckDto(sosCd3, jgiMstList, newPlannedProdList, unallowableStatusList);

		// 施設医師別計画ステータスチェック実行
		InsDocPlanStatusCheckBySosLogic statusCheckLogic = new InsDocPlanStatusCheckBySosLogic(insDocPlanStatusDao, insDocStatusCheckDto);
		InsDocStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設医師別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設医師別計画ステータスOK");
		}

		return resultDto.getInsDocPlanStatus();
	}

	// 組織コード(営業所)、計画対象品目を指定して、施設医師別計画のチェックを行なう。
	public List<InsDocPlanStatus> execute(String sosCd3, PlannedProd plannedProd, List<InsDocStatusForCheck> unallowableStatusList) {
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		plannedProdList.add(plannedProd);
		return execute(sosCd3, plannedProdList, unallowableStatusList);
	}

//	public List<InsDocPlanStatus> execute(String sosCd3, ProdCategory category, List<InsDocStatusForCheck> unallowableStatusList) {
	public List<InsDocPlanStatus> execute(String sosCd3, String category, List<InsDocStatusForCheck> unallowableStatusList) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// チェック対象の従業員取得
		// ----------------------
		// 営業所配下の従業員取得
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
		} catch (DataNotFoundException e) {
			return new ArrayList<InsDocPlanStatus>();
		}

		// ----------------------
		// チェック対象の組織情報取得
		// ----------------------
		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			return new ArrayList<InsDocPlanStatus>();
		}

		// ----------------------
		// チェック対象の品目取得（MMP重点品）
		// ----------------------
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, category, true);
		} catch (DataNotFoundException e) {
			// 重点品目がない場合は、医師別計画のチェックOK扱い
			return new ArrayList<InsDocPlanStatus>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設医師別計画ステータスチェックDTO作成
		InsDocStatusCheckDto insDocStatusCheckDto = new InsDocStatusCheckDto(sosCd3, jgiMstList, plannedProdList, unallowableStatusList);

		// 施設医師別計画ステータスチェック実行
		InsDocPlanStatusCheckBySosLogic statusCheckLogic = new InsDocPlanStatusCheckBySosLogic(insDocPlanStatusDao, insDocStatusCheckDto);
		InsDocStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設医師別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設医師別計画ステータスOK");
		}

		return resultDto.getInsDocPlanStatus();
	}

	public List<InsDocPlanStatus> execute(String sosCd3, String sosCd4, List<PlannedProd> plannedProdList, List<InsDocStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd4 == null) {
			final String errMsg = "組織コード(チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : plannedProdList) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の品目を取得（重点フラグ含めるため）
		// ----------------------
		List<PlannedProd> newPlannedProdList = new ArrayList<PlannedProd>();
		for (PlannedProd plannedProd : plannedProdList) {
			try {
				newPlannedProdList.add(plannedProdDAO.search(plannedProd.getProdCode()));
			} catch (DataNotFoundException e) {
				final String errMsg = "品目固定コードが不明:" + plannedProd.getProdCode();
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の従業員取得
		// ----------------------
		// チーム配下の従業員取得
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, BumonRank.TEAM);
		} catch (DataNotFoundException e) {
			return new ArrayList<InsDocPlanStatus>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設医師別計画ステータスチェックDTO作成
		InsDocStatusCheckDto insDocStatusCheckDto = new InsDocStatusCheckDto(sosCd3, sosCd4, jgiMstList, newPlannedProdList, unallowableStatusList);

		// 施設医師別計画ステータスチェック実行
		InsDocPlanStatusCheckBySosLogic statusCheckLogic = new InsDocPlanStatusCheckBySosLogic(insDocPlanStatusDao, insDocStatusCheckDto);
		InsDocStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設医師別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設医師別計画ステータスOK");
		}

		return resultDto.getInsDocPlanStatus();
	}

	public List<InsDocPlanStatus> execute(List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsDocStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiMstList == null || jgiMstList.size() == 0) {
			final String errMsg = "従業員情報のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (JgiMst jgiMst : jgiMstList) {
			if (jgiMst.getJgiNo() == null) {
				final String errMsg = "チェック対象の従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (jgiMst.getJgiName() == null) {
				final String errMsg = "チェック対象の氏名がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		for (PlannedProd plannedProd : plannedProdList) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の品目を取得（重点フラグ含めるため）
		// ----------------------
		List<PlannedProd> newPlannedProdList = new ArrayList<PlannedProd>();
		for (PlannedProd plannedProd : plannedProdList) {
			try {
				newPlannedProdList.add(plannedProdDAO.search(plannedProd.getProdCode()));
			} catch (DataNotFoundException e) {
				final String errMsg = "品目固定コードが不明:" + plannedProd.getProdCode();
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設医師別計画ステータスチェックDTO作成
		InsDocStatusCheckDto insDocStatusCheckDto = new InsDocStatusCheckDto(jgiMstList, newPlannedProdList, unallowableStatusList);

		// 施設医師別計画ステータスチェック実行
		InsDocPlanStatusCheckByMrLogic statusCheckLogic = new InsDocPlanStatusCheckByMrLogic(insDocPlanStatusDao, insDocStatusCheckDto);
		InsDocStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設医師別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設医師別計画ステータスOK");
		}

		return resultDto.getInsDocPlanStatus();
	}

//	public List<InsDocPlanStatus> execute(List<JgiMst> jgiMstList, ProdCategory category, List<InsDocStatusForCheck> unallowableStatusList) {
	public List<InsDocPlanStatus> execute(List<JgiMst> jgiMstList, String category, List<InsDocStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiMstList == null || jgiMstList.size() == 0) {
			final String errMsg = "従業員情報のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// チェック対象の組織情報取得
		// ----------------------
		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(jgiMstList.get(0).getSosCd3());
		} catch (DataNotFoundException e) {
			return new ArrayList<InsDocPlanStatus>();
		}

		// -----------------------------
		// ワクチンのカテゴリコード取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		if(category != null) {
			if(category.equals(vaccineCode)) {
				sales = Sales.VACCHIN;
			}else{
				sales = Sales.IYAKU;
			}
		}

		// ----------------------
		// チェック対象の品目取得（重点品のみ）
		// ----------------------
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, category, true);
		} catch (DataNotFoundException e) {
			// 重点品目がない場合は、医師別計画のチェックOK扱い
			return new ArrayList<InsDocPlanStatus>();
		}

		// チェック実行
		return execute(jgiMstList, plannedProdList, unallowableStatusList);
	}
}
