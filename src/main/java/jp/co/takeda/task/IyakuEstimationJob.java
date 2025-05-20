package jp.co.takeda.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.EstimationParamsDto;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsEstimationBatchMainService;
import jp.co.takeda.service.DpsEstimationProdService;

/**
 * (医)担当者別計画試算処理を管理するＪＯＢクラス
 *
 * @author tkawabata
 */
@Controller("iyakuEstimationJob")
public class IyakuEstimationJob extends AbstractOnlineJob<EstimationParamsDto> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(IyakuEstimationJob.class);

	/**
	 * 試算機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationProdService")
	protected DpsEstimationProdService dpsEstimationProdService;

	/**
	 * 試算実行バッチメインサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationBatchMainService")
	protected DpsEstimationBatchMainService dpsEstimationBatchMainService;

	@Override
	protected void initilize(final String sKbn) throws Exception {
		if (LOG.isInfoEnabled()) {
			LOG.info("appServerKbn=" + sKbn);
		}
		List<EstimationParamsDto> paramDtoList = null;
		try {
			paramDtoList = dpsAsyncProcessSearchService.searchEstimationPrameter(sKbn);
		} catch (DataNotFoundException e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("試算中の営業所計画がないため、非同期処理再実行処理終了");
			}
			return;
		}
		for (EstimationParamsDto dto : paramDtoList) {
			try {
				// ダミーユーザ作成
				DpUserInfo.setDpUserInfo(new DpUserInfo(dto.getDpUser(), SysClass.DPS, SysType.IYAKU));
				if (LOG.isInfoEnabled()) {
					LOG.info("試算中の営業所計画があるため非同期処理再実行");
					LOG.info("EstimationParamsDto..." + dto.toString());
				}
				dpsEstimationBatchMainService.executeBatch(dto);
			} finally {
				// ダミーユーザ削除
				DpUserInfo.clearDpUserInfo();
			}
		}
	}

	@Override
	protected void canFire(EstimationParamsDto dto) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}
		// 試算処理前チェック・ステータス更新
		String appServerKbn = dto.getAppServerKbn();
//		dpsEstimationProdService.estimationPreparation(dto.getSosCd3(), dto.getOrgMrPlanStatusList(), dto.getCalcType(), dto.getCategory(), appServerKbn);
		// mod Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
		//dpsEstimationProdService.estimationPreparation(dto.getSosCd3(), dto.getOrgMrPlanStatusList(), dto.getCalcType(), appServerKbn);
		dpsEstimationProdService.estimationPreparation(dto.getSosCd3(), dto.getOrgMrPlanStatusList(), dto.getCalcType(), appServerKbn, dto.getCategorySearch(), dto.getCategorySelect());
		// mod End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
	}

	@Override
	protected void process(EstimationParamsDto dto) throws LogicalException {

		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}
		// 試算実行
		dpsEstimationBatchMainService.executeBatch(dto);
	}
}
