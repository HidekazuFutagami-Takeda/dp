package jp.co.takeda.task;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenDistParamDto;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsTmsTytenDistMainService;
import jp.co.takeda.service.DpsTmsTytenDistService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * (医)特約店別計画配分処理を管理するＪＯＢクラス
 * 
 * @author yokokawa
 */
@Controller("iyakuTmsTytenDistJob")
public class IyakuTmsTytenDistJob extends AbstractOnlineJob<TmsTytenDistParamDto> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(IyakuTmsTytenDistJob.class);

	/**
	 * 特約店別計画配分サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenDistService")
	protected DpsTmsTytenDistService dpsTmsTytenDistService;

	/**
	 * 特約店別計画配分メインサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenDistMainService")
	protected DpsTmsTytenDistMainService dpsTmsTytenDistMainService;

	@Override
	protected void initilize(final String sKbn) throws Exception {
		if (LOG.isInfoEnabled()) {
			LOG.info("appServerKbn=" + sKbn);
		}
		List<TmsTytenDistParamDto> paramDtoList = null;
		try {
			paramDtoList = dpsAsyncProcessSearchService.searchWsDistPrameter(sKbn);
		} catch (DataNotFoundException e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("配分中の特約店別計画がないため、非同期処理再実行処理終了");
			}
			return;
		}
		for (TmsTytenDistParamDto dto : paramDtoList) {
			try {
				// ダミーユーザ作成
				DpUserInfo.setDpUserInfo(new DpUserInfo(dto.getDpUser(), SysClass.DPS, SysType.IYAKU));
				if (LOG.isInfoEnabled()) {
					LOG.info("配分中の特約店別計画があるため非同期処理再実行");
					LOG.info("TmsTytenDistParamDto..." + dto.toString());
				}
				dpsTmsTytenDistMainService.execute(dto);
			} finally {
				// ダミーユーザ削除
				DpUserInfo.clearDpUserInfo();
			}
		}
	}

	@Override
	protected void canFire(TmsTytenDistParamDto dto) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}
		// 事前処理
		dpsTmsTytenDistService.distPreparation(dto);
	}

	@Override
	protected void process(TmsTytenDistParamDto dto) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(dto.toString());
		}
		// 特約店別計画配分処理
		dpsTmsTytenDistMainService.execute(dto);
	}
}
