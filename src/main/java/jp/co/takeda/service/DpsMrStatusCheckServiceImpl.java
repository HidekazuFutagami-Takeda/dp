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
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.MrStatusCheckDto;
import jp.co.takeda.dto.MrStatusCheckResultDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.MrPlanStatusCheckLogic;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;

/**
 * 担当者別計画ステータスのステータスチェックを行なうサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsMrStatusCheckService")
public class DpsMrStatusCheckServiceImpl implements DpsMrStatusCheckService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsMrStatusCheckServiceImpl.class);

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	//public List<MrPlanStatus> execute(String sosCd, ProdCategory category, List<MrStatusForCheck> unallowableStatusList) {
	public List<MrPlanStatus> execute(String sosCd, String category, List<MrStatusForCheck> unallowableStatusList) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 営業所コードから組織情報取得
		// ----------------------
		SosMst sosMst;

// del 2022/08/08 R.takamoto ダミーユーザー営業所コードなし対応
//		try {
//			sosMst = sosMstDAO.search(sosCd);
//		} catch (DataNotFoundException e) {
//			final String errMsg = "対象組織がない：" + sosCd;
//			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
//		}
// del 2022/08/08 R.takamoto ダミーユーザー営業所コードなし対応

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
			if(vaccineCode.equals(category)) {
				sales = Sales.VACCHIN;
			}else{
				sales = Sales.IYAKU;
			}
		}

		// ----------------------
		// 医薬・カテゴリを指定して品目を取得
		// ----------------------
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(MMP品)が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 担当者別計画ステータスチェックDTO作成
		MrStatusCheckDto mrStatusCheckDto = new MrStatusCheckDto(sosCd, plannedProdList, unallowableStatusList);

		// 担当者別計画ステータスチェック実行
		MrPlanStatusCheckLogic statusCheckLogic = new MrPlanStatusCheckLogic(mrPlanStatusDao, mrStatusCheckDto);
		MrStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "担当者別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("担当者別計画ステータスOK");
		}

		return resultDto.getMrPlanStatus();
	}

	public List<MrPlanStatus> execute(String sosCd, PlannedProd plannedProd, List<MrStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProd.getProdCode() == null) {
			final String errMsg = "チェック対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProd.getProdName() == null) {
			final String errMsg = "チェック対象の品目名称がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		plannedProdList.add(plannedProd);

		// 実行
		return execute(sosCd, plannedProdList, unallowableStatusList);
	}

	public List<MrPlanStatus> execute(String sosCd, List<PlannedProd> plannedProdList, List<MrStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
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
		// ステータスチェック
		// ----------------------

		// 担当者別計画ステータスチェックDTO作成
		MrStatusCheckDto mrStatusCheckDto = new MrStatusCheckDto(sosCd, plannedProdList, unallowableStatusList);

		// 担当者別計画ステータスチェック実行
		MrPlanStatusCheckLogic statusCheckLogic = new MrPlanStatusCheckLogic(mrPlanStatusDao, mrStatusCheckDto);
		MrStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "担当者別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("担当者別計画ステータスOK");
		}

		return resultDto.getMrPlanStatus();
	}
}
