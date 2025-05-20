package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ChangeParamBTDao;
import jp.co.takeda.dao.ChangeParamYTDao;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanSummaryDao;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceResultDto;
import jp.co.takeda.dto.WsPlanReferenceScDto;
import jp.co.takeda.dto.WsPlanSummaryScDto;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.logic.CreateTmsTytenPlanReferenceLogic;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.WsPlanSummary2;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.security.DpUserInfo;

/**
 * 特約店別計画参照サービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsTmsTytenPlanReferenceService")
public class DpsTmsTytenPlanReferenceServiceImpl implements DpsTmsTytenPlanReferenceService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsTmsTytenPlanReferenceServiceImpl.class);

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("changeParamYTDao")
	protected ChangeParamYTDao changeParamYTDao;

	/**
	 * 変換パラメータ(B→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("changeParamBTDao")
	protected ChangeParamBTDao changeParamBTDao;

	/**
	 * 特約店別計画のサマリ情報
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanSummaryDao")
	protected WsPlanSummaryDao wsPlanSummaryDao;

	/**
	 * 特約店情報
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	// 画面ヘッダ情報を取得
	public TmsTytenPlanReferenceHeaderResultDto searchHeaderInfo(String tytenCdPart) {

		// T/Y変換指定率登録日を取得
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		SysManage sysManage = dpUserInfo.getSysManage();
		if (sysManage == null) {
			final String errMsg = "ユーザ情報からシステム管理情報が取得出来ない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		Date date = changeParamYTDao.getLastUpDate(sysManage.getSysYear(), sysManage.getSysTerm());

		// T/Y変換指定率登録日(ワクチン用)を取得
//		Date btDate = changeParamBTDao.getLastUpDate(sysManage.getSysYear(), sysManage.getSysTerm());
		Date btDate = changeParamYTDao.getLastUpDate(sysManage.getSysYear(), sysManage.getSysTerm());

// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// S価変換バッチ実施フラグを取得する。
		Boolean transTFlg = sysManage.getTransTFlg();

		// 特約店情報を取得
		String tytenName = "";
		if (StringUtils.isNotBlank(tytenCdPart)) {
			String tytenCd = new CreateTmsTytenCdLogic(tytenCdPart).execute();
			try {
				TmsTytenMstUn tytenMst = tmsTytenMstUnDAO.search(tytenCd);
				tytenName = tytenMst.getTmsTytenMeiKj();
			} catch (DataNotFoundException e) {
			}
		}

		// 施設特約店別計画〆フラグを取得する。
		Boolean wsEndFlg = sysManage.getWsEndFlg();

		return new TmsTytenPlanReferenceHeaderResultDto(date, btDate, tytenName, transTFlg, wsEndFlg);
	}

	// 特約店別計画サマリーを取得
	public TmsTytenPlanReferenceResultDto searchTmsTytenPlanSummary(WsPlanReferenceScDto wsPlanReferenceScDto) throws LogicalException {

		String tmsCdPart = wsPlanReferenceScDto.getTmsTytenCdPart();
		TytenKisLevel level = wsPlanReferenceScDto.getTytenKisLevel();
//		ProdCategory pCategory = wsPlanReferenceScDto.getCategory();
		String pCategory = wsPlanReferenceScDto.getCategory();
		KaBaseKb kaBaseKb = wsPlanReferenceScDto.getKaBaseKb();
		WsPlanSummaryScDto param = new WsPlanSummaryScDto(tmsCdPart, null, level, pCategory, kaBaseKb);
		if (LOG.isDebugEnabled()) {
			LOG.debug("tmsCdPart=" + tmsCdPart);
			LOG.debug("level=" + level);
			LOG.debug("pCategory=" + pCategory);
			LOG.debug("kaBaseKb=" + kaBaseKb);
		}

		// 検索処理(実績無で検索)
		List<WsPlanSummary2> searchList = null;
		try {
			searchList = wsPlanSummaryDao.searchList(WsPlanSummaryDao.SORT_STRING, param, false);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		// 集計処理(明細を品目毎に集計)
		return new TmsTytenPlanReferenceResultDto(new CreateTmsTytenPlanReferenceLogic(searchList).execute());
	}
}
