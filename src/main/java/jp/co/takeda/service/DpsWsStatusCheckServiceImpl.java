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
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.WsStatusCheckDto;
import jp.co.takeda.dto.WsStatusCheckResultDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.WsPlanStatusCheckByAllLogic;
import jp.co.takeda.logic.WsPlanStatusCheckBySosLogic;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;

/**
 * 特約店別計画ステータスのステータスチェックを行なうサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsWsStatusCheckService")
public class DpsWsStatusCheckServiceImpl implements DpsWsStatusCheckService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsWsStatusCheckServiceImpl.class);

	/**
	 * 特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusDao")
	protected WsPlanStatusDao wsPlanStatusDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

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

	public List<WsPlanStatus> execute(SosMst sosMst, List<PlannedProd> plannedProdList, List<WsDistStatusForCheck> unallowableDistStatusList,
		List<WsSlideStatusForCheck> unallowableSlideStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosMst == null) {
			final String errMsg = "組織情報(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMst.getSosCd() == null) {
			final String errMsg = "組織情報(支店)の組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMst.getBumonSeiName() == null) {
			final String errMsg = "組織情報(支店)の部門名正式がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
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
		// 特約店別計画ステータスチェックDTO作成
		List<SosMst> sosMstList = new ArrayList<SosMst>();
		sosMstList.add(sosMst);
		WsStatusCheckDto wsStatusCheckDto = new WsStatusCheckDto(sosMstList, plannedProdList, unallowableDistStatusList, unallowableSlideStatusList);

		// 特約店別計画ステータスチェック実行
		WsPlanStatusCheckBySosLogic statusCheckLogic = new WsPlanStatusCheckBySosLogic(wsPlanStatusDao, wsStatusCheckDto);
		WsStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("特約店別計画ステータスOK");
		}

		return resultDto.getWsPlanStatus();
	}

	public List<WsPlanStatus> execute(String category, List<WsDistStatusForCheck> unallowableDistStatusList, List<WsSlideStatusForCheck> unallowableSlideStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------

		// ----------------------
		// チェック対象の支店一覧取得
		// ----------------------
		List<SosMst> sosMstList = null;
		try {
			sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.IEIHON, SosMst.SOS_CD1);
		} catch (DataNotFoundException e) {
			return new ArrayList<WsPlanStatus>();
		}

		// -----------------------------
		// ワクチンコードの取得
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
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		if (category == null) {
			sales = Sales.IYAKU;
		} else if(category.equals(vaccineCode)) {
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			sales = Sales.VACCHIN;
		}else{
			sales = Sales.IYAKU;
		}

		// ----------------------
		// チェック対象の品目一覧取得
		// ----------------------
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 特約店別計画ステータスチェックDTO作成
		WsStatusCheckDto wsStatusCheckDto = new WsStatusCheckDto(sosMstList, plannedProdList, unallowableDistStatusList, unallowableSlideStatusList);

		// 特約店別計画ステータスチェック実行
		WsPlanStatusCheckByAllLogic statusCheckLogic = new WsPlanStatusCheckByAllLogic(wsPlanStatusDao, wsStatusCheckDto);
		WsStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("特約店別計画ステータスOK");
		}

		return resultDto.getWsPlanStatus();
	}
}
