package jp.co.takeda.task;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DocDistributionParamsDto;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsDistributionBatchMainService;
import jp.co.takeda.service.DpsDocDistributionProdService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * (医)施設医師別計画配分処理を管理するＪＯＢクラス
 * 
 * @author nozaki
 */
@Controller("docDistributionJob")
public class DocDistributionJob extends AbstractOnlineJob<DocDistributionParamsDto> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DocDistributionJob.class);

	/**
	 * 配分機能 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDocDistributionProdService")
	protected DpsDocDistributionProdService dpsDocDistributionProdService;

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

		List<DocDistributionParamsDto> paramDtoList = null;
		try {
			paramDtoList = dpsAsyncProcessSearchService.searchInsDocDistPrameter(sKbn);
		} catch (DataNotFoundException e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("配分中の施設医師別計画がないため、非同期処理再実行処理終了");
			}
			return;
		}
		for (DocDistributionParamsDto dto : paramDtoList) {
			try {
				// ダミーユーザ作成
				DpUserInfo.setDpUserInfo(new DpUserInfo(dto.getDpUser(), SysClass.DPS, SysType.IYAKU));
				if (LOG.isInfoEnabled()) {
					LOG.info("配分中の施設医師別計画があるため非同期処理再実行");
					LOG.info("DistributionParamsDto..." + dto.toString());
				}
				dpsDistributionBatchMainService.executeInsDocBatch(dto);
			} finally {
				// ダミーユーザ削除
				DpUserInfo.clearDpUserInfo();
			}
		}
	}

	@Override
	protected void canFire(DocDistributionParamsDto dto) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}

		// 配分処理前チェック・ステータス更新（ → 配分中）
		dpsDocDistributionProdService.distributionPreparation(dto);
	}

	@Override
	protected void process(DocDistributionParamsDto dto) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}

		// 配分実行
		dpsDistributionBatchMainService.executeInsDocBatch(dto);
	}
}
