package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanForVacSummaryDao;
import jp.co.takeda.dto.TmsTytenPlanReferenceForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderForVacResultDto;
import jp.co.takeda.dto.WsPlanForVacSummaryScDto;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.logic.CreateTmsTytenPlanReferenceForVacLogic;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.WsPlanForVacSummary2;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.security.DpUserInfo;

/**
 * ワクチン特約店別計画参照サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsTmsTytenPlanReferenceForVacService")
public class DpsTmsTytenPlanReferenceForVacServiceImpl implements DpsTmsTytenPlanReferenceForVacService {

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
	 * ワクチン特約店別計画のサマリ情報
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanForVacSummaryDao")
	protected WsPlanForVacSummaryDao wsPlanForVacSummaryDao;

	/**
	 * 組織情報
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 特約店情報
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	// 画面ヘッダ情報を取得
	public TmsTytenPlanReferenceHeaderForVacResultDto searchHeaderInfo(String tytenCdPart) {

		// T/Y変換指定率登録日を取得
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		SysManage sysManage = dpUserInfo.getSysManage();
		if (sysManage == null) {
			final String errMsg = "ユーザ情報からシステム管理情報が取得出来ない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
//		Date date = changeParamBTDao.getLastUpDate(sysManage.getSysYear(), sysManage.getSysTerm());
		Date date = changeParamYTDao.getLastUpDate(sysManage.getSysYear(), sysManage.getSysTerm());

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

		return new TmsTytenPlanReferenceHeaderForVacResultDto(date, tytenName);
	}

	// 特約店別計画サマリーを取得
	public TmsTytenPlanReferenceForVacResultDto searchTmsTytenPlanSummary(WsPlanReferenceForVacScDto wsPlanReferenceScDto) throws LogicalException {

		// --------------------------------------
		// 引数チェック
		// --------------------------------------
		if (wsPlanReferenceScDto == null) {
			final String errMsg = "(ワ)特約店別計画サマリー情報検索DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------------------
		// 検索処理(実績無で検索)
		// --------------------------------------
		String tmsCdPart = wsPlanReferenceScDto.getTmsTytenCdPart();
		TytenKisLevel level = wsPlanReferenceScDto.getTytenKisLevel();
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = wsPlanReferenceScDto.getKaBaseKb();
		WsPlanForVacSummaryScDto scDto = new WsPlanForVacSummaryScDto(tmsCdPart, level, kaBaseKb);
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		List<WsPlanForVacSummary2> searchList = null;
		try {
			searchList = wsPlanForVacSummaryDao.searchList(WsPlanForVacSummaryDao.SORT_STRING, scDto, false);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		// --------------------------------------
		// 集計処理(明細を品目毎に集計)
		// --------------------------------------
		return new TmsTytenPlanReferenceForVacResultDto(new CreateTmsTytenPlanReferenceForVacLogic(searchList).execute());
	}
}
