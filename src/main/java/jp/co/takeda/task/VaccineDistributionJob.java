package jp.co.takeda.task;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistributionForVacParamsDto;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsDistributionForVacBatchMainService;
import jp.co.takeda.service.DpsDistributionForVacProdService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * (ワ)施設特約店別計画配分処理を管理するＪＯＢクラス
 * 
 * @author khashimoto
 */
@Controller("vaccineDistributionJob")
public class VaccineDistributionJob extends AbstractOnlineJob<DistributionForVacParamsDto> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(VaccineDistributionJob.class);

	/**
	 * (ワクチン)配分機能 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacProdService")
	protected DpsDistributionForVacProdService dpsDistributionForVacProdService;

	/**
	 * (ワクチン)配分バッチサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacBatchMainService")
	protected DpsDistributionForVacBatchMainService dpsDistributionForVacBatchMainService;

	@Override
	protected void initilize(final String sKbn) throws Exception {
		if (LOG.isInfoEnabled()) {
			LOG.info("appServerKbn=" + sKbn);
		}
		List<DistributionForVacParamsDto> paramDtoList = null;
		try {
			paramDtoList = dpsAsyncProcessSearchService.searchInsWsDistForVacPrameter(sKbn);
		} catch (DataNotFoundException e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("配分中の施設特約店別計画がないため、非同期処理再実行処理終了");
			}
			return;
		}
		for (DistributionForVacParamsDto dto : paramDtoList) {
			try {
				// ダミーユーザ作成
				DpUserInfo.setDpUserInfo(new DpUserInfo(dto.getDpUser(), SysClass.DPS, SysType.IYAKU));
				if (LOG.isInfoEnabled()) {
					LOG.info("配分中の施設特約店別計画があるため非同期処理再実行");
					LOG.info("DistributionParamsDto..." + dto.toString());
				}
				dpsDistributionForVacBatchMainService.executeBatch(dto);
			} finally {
				// ダミーユーザ削除
				DpUserInfo.clearDpUserInfo();
			}
		}
	}

	@Override
	protected void canFire(DistributionForVacParamsDto dto) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}

		// ワクチン配分処理前チェック・ステータス更新
		String appServerKbn = dto.getAppServerKbn();
		dpsDistributionForVacProdService.distributionPreparation(dto.getDistributionForVacExecOrgDtoList(), appServerKbn);

	}

	@Override
	protected void process(DistributionForVacParamsDto dto) throws LogicalException {

		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}

		// 配分実行
		dpsDistributionForVacBatchMainService.executeBatch(dto);
	}
}
