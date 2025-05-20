package jp.co.takeda.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.DistributionParamsDto;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsDistributionBatchMainService;
import jp.co.takeda.service.DpsDistributionProdService;

/**
 * (医)施設特約店別計画配分処理を管理するＪＯＢクラス
 *
 * @author nozaki
 */
@Controller("iyakuDistributionJob")
public class IyakuDistributionJob extends AbstractOnlineJob<DistributionParamsDto> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(IyakuDistributionJob.class);

	/**
	 * 配分機能 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionProdService")
	protected DpsDistributionProdService dpsDistributionProdService;

	/**
	 * 配分バッチサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionBatchMainService")
	protected DpsDistributionBatchMainService dpsDistributionBatchMainService;

	@Override
	protected void initilize(final String sKbn) throws Exception {
		if (LOG.isInfoEnabled()) {
			LOG.info("appServerKbn=" + sKbn);
		}
		List<DistributionParamsDto> paramDtoList = null;
		try {
			paramDtoList = dpsAsyncProcessSearchService.searchInsWsDistPrameter(sKbn);
		} catch (DataNotFoundException e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("配分中の施設特約店別計画がないため、非同期処理再実行処理終了");
			}
			return;
		}
		for (DistributionParamsDto dto : paramDtoList) {
			try {
				// ダミーユーザ作成
				DpUserInfo.setDpUserInfo(new DpUserInfo(dto.getDpUser(), SysClass.DPS, SysType.IYAKU));
				if (LOG.isInfoEnabled()) {
					LOG.info("配分中の施設特約店別計画があるため非同期処理再実行");
					LOG.info("DistributionParamsDto..." + dto.toString());
				}
				dpsDistributionBatchMainService.executeInsWsBatch(dto);
			} finally {
				// ダミーユーザ削除
				DpUserInfo.clearDpUserInfo();
			}
		}
	}

	@Override
	protected void canFire(DistributionParamsDto dto) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}

		// 配分処理前チェック・ステータス更新
		String sosCd = dto.getSosCd3();
		String category = dto.getCategory();
		List<DistributionExecOrgDto> distExecOrgDtoList = dto.getDistributionExecOrgDtoList();
		String appServerKbn = dto.getAppServerKbn();
		dpsDistributionProdService.distributionPreparation(sosCd, category, distExecOrgDtoList, appServerKbn);

	}

	@Override
	protected void process(DistributionParamsDto dto) throws LogicalException {

		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}

		// 配分実行
		dpsDistributionBatchMainService.executeInsWsBatch(dto);
	}
}
