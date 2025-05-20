package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanForVacDao;
import jp.co.takeda.dto.TmsTytenPlanEditForVacDetailDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacScDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;

/**
 * (ワ)特約店別計画編集サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsTmsTytenPlanEditForVacService")
public class DpsTmsTytenPlanEditForVacServiceImpl implements DpsTmsTytenPlanEditForVacService {

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	/**
	 * 計画品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * ワクチン特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanForVacDao")
	protected WsPlanForVacDao wsPlanForVacDao;

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckService")
	protected DpsWsStatusCheckService dpsWsStatusCheckService;

	// ワクチン特約店別計画 編集対象検索
	public TmsTytenPlanEditForVacResultDto searchEditWsPlan(TmsTytenPlanEditForVacScDto tmsTytenPlanEditScDto) throws LogicalException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanEditScDto == null) {
			final String errMsg = "特約店別計画編集対象検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = tmsTytenPlanEditScDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "特約店別計画編集対象検索時に価格区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// 13桁で送信されてくる
		String tmsTytenCd = tmsTytenPlanEditScDto.getTmsTytenCd();
		if (StringUtils.isBlank(tmsTytenCd)) {
			final String errMsg = "特約店別計画編集対象検索時に特約店コードがnullまたは空。tmsTytenCd=" + tmsTytenCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String prodCode = tmsTytenPlanEditScDto.getProdCode();
		if (StringUtils.isBlank(prodCode)) {
			final String errMsg = "特約店別計画編集対象検索時に品目固定コードがnullまたは空。prodCode=" + prodCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String tytenKisLevel = tmsTytenPlanEditScDto.getTytenKisLevel();
		if (StringUtils.isBlank(prodCode)) {
			final String errMsg = "特約店別計画編集対象検索時に集約方法がnullまたは空。prodCode=" + prodCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String tmsTytenCdPart = tmsTytenPlanEditScDto.getTmsTytenCdPart();

		// ---------------------------------
		// 組織情報取得処理(特約店部)
		// ---------------------------------
// mod Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		List<SosMst> sosMstList = null;
//		try {
//			sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, BumonRank.IEIHON, SosMst.SOS_CD1);
//		} catch (DataNotFoundException e) {
//			final String errMsg = "特約店部の取得失敗";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
// mod End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		// ---------------------------------
		// 特約店情報取得処理
		// ---------------------------------
		TmsTytenMstUn tmsTytenMstUn = null;
		try {
			tmsTytenMstUn = tmsTytenMstUnDAO.search(tmsTytenCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "特約店別計画編集対象検索時に特約店コードに一致する特約店情報がない。tmsTytenCd=" + tmsTytenCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// 品目情報取得処理
		// ---------------------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
			Boolean planLevelWs = plannedProd.getPlanLevelWs();
			if (planLevelWs == null || planLevelWs == false) {
				final String errMsg = "特約店別計画編集対象検索時に対象品目の計画立案レベル・特約店が'1'でない。prodCode=" + prodCode;
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "特約店別計画編集対象検索時に品目固定コードに一致する品目情報がない。prodCode=" + prodCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// 明細情報
		// ---------------------------------
		List<TmsTytenPlanEditForVacDetailDto> detailDtoList = new ArrayList<TmsTytenPlanEditForVacDetailDto>();

		// 全集計行用の変数
		Long plannedValueAll = 0L;
		Long insStackValueAll = 0L;

		// 特約店部ごとに、明細情報取得
// mod Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		for (SosMst sosMst : sosMstList) {
// mod End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

			// 集計行用の変数
			Long plannedValue = 0L;
			Long insStackValue = 0L;

			List<WsPlanForVac> wsPlanList = null;
// mod Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//			try {
//				wsPlanList = wsPlanForVacDao.searchList(WsPlanForVacDao.SORT_STRING, sosMst.getSosCd(), null, tmsTytenCdPart, prodCode, null, kaBaseKb);
				wsPlanList = wsPlanForVacDao.searchList(WsPlanForVacDao.SORT_STRING, null, null, tmsTytenCdPart, prodCode, null, kaBaseKb);
//			} catch (DataNotFoundException e) {
//				continue;
//			}
// mod End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

			for (WsPlanForVac wsPlan : wsPlanList) {

				// 集計行用の処理
				if (wsPlan.getPlannedValue() != null) {
					plannedValue += wsPlan.getPlannedValue();
					wsPlan.setPlannedValue(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getPlannedValue()));
				}
				if (wsPlan.getInsStackValue() != null) {
					insStackValue += wsPlan.getInsStackValue();
					wsPlan.setInsStackValue(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getInsStackValue()));
				}

				// 明細行追加
// add Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//				detailDtoList.add(new TmsTytenPlanEditForVacDetailDto(sosMst.getBumonSeiName(), wsPlan, false, false));
				detailDtoList.add(new TmsTytenPlanEditForVacDetailDto(null, wsPlan, false, false));
// add End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			}

			// 集計行の作成
			WsPlanForVac subTotalWsPlan = new WsPlanForVac();
			subTotalWsPlan.setPlannedValue(ConvertUtil.parseMoneyToThousandUnit(plannedValue));
			subTotalWsPlan.setInsStackValue(ConvertUtil.parseMoneyToThousandUnit(insStackValue));
// add Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//			TmsTytenPlanEditForVacDetailDto detailDto = new TmsTytenPlanEditForVacDetailDto(sosMst.getBumonSeiName(), subTotalWsPlan, true, false);
			TmsTytenPlanEditForVacDetailDto detailDto = new TmsTytenPlanEditForVacDetailDto(null, subTotalWsPlan, true, false);
// add End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			detailDtoList.add(detailDto);
			// 全集計に加算
			plannedValueAll += plannedValue;
			insStackValueAll += insStackValue;
// mod Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		}
// mod End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		if (detailDtoList.size() == 0) {
			throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR));
		}

		// 全集計行の作成
		WsPlanForVac totalWsPlan = new WsPlanForVac();
		totalWsPlan.setPlannedValue(ConvertUtil.parseMoneyToThousandUnit(plannedValueAll));
		totalWsPlan.setInsStackValue(ConvertUtil.parseMoneyToThousandUnit(insStackValueAll));
// mod Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする;
		TmsTytenPlanEditForVacDetailDto detailDtoAll = new TmsTytenPlanEditForVacDetailDto(null, totalWsPlan, false, true);
		detailDtoList.add(0, detailDtoAll);
// mod End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		return new TmsTytenPlanEditForVacResultDto(tmsTytenMstUn, plannedProd, detailDtoList);
	}

	// ワクチン特約店別計画 編集
	public void editWsPlan(TmsTytenPlanEditForVacInputDto tmsTytenPlanEditInputDto) throws LogicalException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanEditInputDto == null) {
			final String errMsg = "特約店別計画編集対象DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<WsPlanForVac> wsPlanList = tmsTytenPlanEditInputDto.getWsPlanForVacList();
		if (wsPlanList == null) {
			final String errMsg = "特約店別計画編集対象リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = tmsTytenPlanEditInputDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "価ベース区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		// ---------------------------------
		// ステータスチェック
		// ---------------------------------
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		// 施設特約店別計画〆前は更新処理不可
//		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.VACCINE);
//		if (sysManage == null) {
//			final String errMsg = "ユーザ情報からシステム管理情報が取得出来ない";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
//		if (!sysManage.getWsEndFlg()) {
//			throw new LogicalException(new Conveyance(new MessageKey("DPS3325E")));
//		}
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.IYAKU);
		if (sysManage == null) {
			final String errMsg = "ユーザ情報からシステム管理情報が取得出来ない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// Y/B価の追加処理
		// S変換後はY/Bの追加処理不可
		if (kaBaseKb.equals(KaBaseKb.Y_KA_BASE) && sysManage.getTransTFlg()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3317E")));
		}
		// S価の追加処理
		// 配分中は実行不可
		// 許可しないステータスリスト作成
		List<WsDistStatusForCheck> unallowableDistStatusList = new ArrayList<WsDistStatusForCheck>();
		unallowableDistStatusList.add(WsDistStatusForCheck.DISTING);
		// カテゴリを品目コードから取得
		String category;
		try {
			category = plannedProdDAO.search(tmsTytenPlanEditInputDto.getWsPlanForVacList().get(0).getProdCode()).getCategory();
		} catch (DataNotFoundException e) {
			final String errMsg = "対象品目のカテゴリが取得できない：" + tmsTytenPlanEditInputDto.getWsPlanForVacList().get(0).getProdCode();
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// チェック実行
		try {
			dpsWsStatusCheckService.execute(category, unallowableDistStatusList, null);
		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3321E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}
		// S変換前はS価の追加処理不可
		if (kaBaseKb.equals(KaBaseKb.S_KA_BASE) && !sysManage.getTransTFlg()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3315E")));
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		for (WsPlanForVac wsPlan : wsPlanList) {
			WsPlanForVac record = null;
			try {
				record = wsPlanForVacDao.search(wsPlan.getSeqKey());
				record.setSeqKey(wsPlan.getSeqKey());
				record.setSosCd(wsPlan.getSosCd());
				record.setUpDate(wsPlan.getUpDate());
				record.setPlannedValue(wsPlan.getPlannedValue());
				wsPlanForVacDao.update(record);
			} catch (DataNotFoundException e) {
				final String errMsg = "：ワクチン特約店別計画編集処理で、更新対象が見つからないため、楽観的ロックエラーとする";
				throw new OptimisticLockingFailureException(errMsg);
			}
		}
	}
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	// ワクチン特約店別計画 編集
	public void editWsPlanRefer(TmsTytenPlanEditForVacInputDto tmsTytenPlanEditInputDto) throws LogicalException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanEditInputDto == null) {
			final String errMsg = "特約店別計画編集対象DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<WsPlanForVac> wsPlanList = tmsTytenPlanEditInputDto.getWsPlanForVacList();
		if (wsPlanList == null) {
			final String errMsg = "特約店別計画編集対象リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		KaBaseKb kaBaseKb = tmsTytenPlanEditInputDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "価ベース区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// ステータスチェック
		// ---------------------------------
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		if (sysManage == null) {
			final String errMsg = "ユーザ情報からシステム管理情報が取得出来ない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// Y/B価の追加処理
		if (kaBaseKb.equals(KaBaseKb.Y_KA_BASE)) {

			// S変換後はY/Bの追加処理不可
			if (sysManage.getTransTFlg()) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS3317E")));
			}

			// 配分中は実行不可
			// 許可しないステータスリスト作成
			List<WsDistStatusForCheck> unallowableDistStatusList = new ArrayList<WsDistStatusForCheck>();
			unallowableDistStatusList.add(WsDistStatusForCheck.DISTING);
			// チェック実行
			try {
				dpsWsStatusCheckService.execute(null, unallowableDistStatusList, null);
			} catch (UnallowableStatusException e) {
				// メッセージ作成
				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3321E"));
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
				throw new UnallowableStatusException(new Conveyance(messageKeyList));
			}

		}
		// S変換前はS価の追加処理不可
		if (kaBaseKb.equals(KaBaseKb.S_KA_BASE) && !sysManage.getTransTFlg()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3315E")));
		}

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		for (WsPlanForVac wsPlan : wsPlanList) {
			WsPlanForVac record = null;
			try {
				record = wsPlanForVacDao.search(wsPlan.getSeqKey());
				record.setSeqKey(wsPlan.getSeqKey());
				record.setSosCd(wsPlan.getSosCd());
				record.setUpDate(wsPlan.getUpDate());
				record.setPlannedValue(wsPlan.getPlannedValue());
				wsPlanForVacDao.update(record);
			} catch (DataNotFoundException e) {
				final String errMsg = "：ワクチン特約店別計画編集処理で、更新対象が見つからないため、楽観的ロックエラーとする";
				throw new OptimisticLockingFailureException(errMsg);
			}
		}
	}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
}
