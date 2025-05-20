package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.WsPlanStatusForVacDao;
import jp.co.takeda.dto.WsStatusCheckForVacDto;
import jp.co.takeda.dto.WsStatusCheckResultForVacDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.WsPlanStatusCheckForVacLogic;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.Sales;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ワクチン用特約店別計画ステータスのステータスチェックを行なうサービス実装クラス
 * 
 * @author nozaki
 */
@Transactional
@Service("dpsWsStatusCheckForVacService")
public class DpsWsStatusCheckForVacServiceImpl implements DpsWsStatusCheckForVacService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsWsStatusCheckForVacServiceImpl.class);

	/**
	 * ワクチン用特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusForVacDao")
	protected WsPlanStatusForVacDao wsPlanStatusForVacDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	public List<WsPlanStatusForVac> execute(List<PlannedProd> plannedProdList, List<WsSlideStatusForCheck> unallowableSlideStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
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
		if (unallowableSlideStatusList == null || unallowableSlideStatusList.size() == 0) {
			final String errMsg = "許可しないスライドステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// ワクチン用特約店別計画ステータスチェックDTO作成
		WsStatusCheckForVacDto wsStatusCheckDto = new WsStatusCheckForVacDto(plannedProdList, unallowableSlideStatusList);

		// ワクチン用特約店別計画ステータスチェック実行
		WsPlanStatusCheckForVacLogic statusCheckLogic = new WsPlanStatusCheckForVacLogic(wsPlanStatusForVacDao, wsStatusCheckDto);
		WsStatusCheckResultForVacDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "ワクチン用特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ワクチン用特約店別計画ステータスOK");
		}

		return resultDto.getWsPlanStatusForVac();
	}

	public List<WsPlanStatusForVac> execute(List<WsSlideStatusForCheck> unallowableSlideStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (unallowableSlideStatusList == null || unallowableSlideStatusList.size() == 0) {
			final String errMsg = "許可しないスライドステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// ワクチン品を取得
		// ----------------------
		List<PlannedProd> tmpList;
		try {
			tmpList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, null, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(ワクチン)が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// --------------------------------------------
		// 特約店別計画立案レベルが真の品目のみ格納
		// --------------------------------------------
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (PlannedProd plannedProd : tmpList) {
			Boolean planLevelWs = plannedProd.getPlanLevelWs();
			if (planLevelWs != null && planLevelWs) {
				plannedProdList.add(plannedProd);
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("特約店別計画立案レベルが真ではないので、除外。prodCode=" + plannedProd.getProdCode());
				}
			}
		}
		if (plannedProdList.isEmpty()) {
			final String errMsg = "計画対象品目(ワクチン)が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// ワクチン用特約店別計画ステータスチェックDTO作成
		WsStatusCheckForVacDto wsStatusCheckDto = new WsStatusCheckForVacDto(plannedProdList, unallowableSlideStatusList);

		// ワクチン用特約店別計画ステータスチェック実行
		WsPlanStatusCheckForVacLogic statusCheckLogic = new WsPlanStatusCheckForVacLogic(wsPlanStatusForVacDao, wsStatusCheckDto);
		WsStatusCheckResultForVacDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "ワクチン用特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ワクチン用特約店別計画ステータスOK");
		}

		return resultDto.getWsPlanStatusForVac();
	}
}
