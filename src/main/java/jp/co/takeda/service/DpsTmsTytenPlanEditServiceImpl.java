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
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanDao;
import jp.co.takeda.dto.TmsTytenPlanEditDetailDto;
import jp.co.takeda.dto.TmsTytenPlanEditInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditScDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;

/**
 * 特約店別計画編集サービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsTmsTytenPlanEditService")
public class DpsTmsTytenPlanEditServiceImpl implements DpsTmsTytenPlanEditService {

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
	 * 特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanDao")
	protected WsPlanDao wsPlanDao;

	/**
	 * 特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckService")
	protected DpsWsStatusCheckService dpsWsStatusCheckService;

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

	// 特約店別計画 編集対象検索
	public TmsTytenPlanEditResultDto searchEditWsPlan(TmsTytenPlanEditScDto tmsTytenPlanEditScDto) {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanEditScDto == null) {
			final String errMsg = "特約店別計画編集対象検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		KaBaseKb kaBaseKb = tmsTytenPlanEditScDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "特約店別計画編集対象検索時に価格区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
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
		List<TmsTytenPlanEditDetailDto> detailDtoList = new ArrayList<TmsTytenPlanEditDetailDto>();
		List<WsPlan> wsPlanList = null;
		try {
			wsPlanList = wsPlanDao.searchList(WsPlanDao.SORT_STRING, null, tmsTytenCdPart, prodCode, null, tytenKisLevel, kaBaseKb);
		} catch (DataNotFoundException e) {
			WsPlan totalWsPlan = new WsPlan();
			totalWsPlan.setDistValueUh(0L);
			totalWsPlan.setDistValueP(0L);
			totalWsPlan.setPlannedValueUh(0L);
			totalWsPlan.setPlannedValueP(0L);
			totalWsPlan.setStackValueUh(0L);
			totalWsPlan.setStackValueP(0L);
			TmsTytenPlanEditDetailDto detailDto = new TmsTytenPlanEditDetailDto(totalWsPlan, true);
			detailDtoList.add(detailDto);
			return new TmsTytenPlanEditResultDto(tmsTytenMstUn, plannedProd, detailDtoList);
		}

		// 集計行用の変数
		Long distValueUh = 0L;
		Long distValueP = 0L;
		Long plannedValueUh = 0L;
		Long plannedValueP = 0L;
		Long stackValueUh = 0L;
		Long stackValueP = 0L;
		for (WsPlan wsPlan : wsPlanList) {

			// 集計行用の処理
			if (wsPlan.getDistValueUh() != null) {
				distValueUh += wsPlan.getDistValueUh();
				wsPlan.setDistValueUh(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getDistValueUh()));
			}
			if (wsPlan.getDistValueP() != null) {
				distValueP += wsPlan.getDistValueP();
				wsPlan.setDistValueP(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getDistValueP()));
			}
			if (wsPlan.getPlannedValueUh() != null) {
				plannedValueUh += wsPlan.getPlannedValueUh();
				wsPlan.setPlannedValueUh(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getPlannedValueUh()));
			}
			if (wsPlan.getPlannedValueP() != null) {
				plannedValueP += wsPlan.getPlannedValueP();
				wsPlan.setPlannedValueP(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getPlannedValueP()));
			}
			if (wsPlan.getStackValueUh() != null) {
				stackValueUh += wsPlan.getStackValueUh();
				wsPlan.setStackValueUh(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getStackValueUh()));
			}
			if (wsPlan.getStackValueP() != null) {
				stackValueP += wsPlan.getStackValueP();
				wsPlan.setStackValueP(ConvertUtil.parseMoneyToThousandUnit(wsPlan.getStackValueP()));
			}

			// 明細行追加
			detailDtoList.add(new TmsTytenPlanEditDetailDto(wsPlan, false));
		}

		// 集計行の作成
		WsPlan totalWsPlan = new WsPlan();
		totalWsPlan.setDistValueUh(ConvertUtil.parseMoneyToThousandUnit(distValueUh));
		totalWsPlan.setDistValueP(ConvertUtil.parseMoneyToThousandUnit(distValueP));
		totalWsPlan.setPlannedValueUh(ConvertUtil.parseMoneyToThousandUnit(plannedValueUh));
		totalWsPlan.setPlannedValueP(ConvertUtil.parseMoneyToThousandUnit(plannedValueP));
		totalWsPlan.setStackValueUh(ConvertUtil.parseMoneyToThousandUnit(stackValueUh));
		totalWsPlan.setStackValueP(ConvertUtil.parseMoneyToThousandUnit(stackValueP));
		TmsTytenPlanEditDetailDto detailDto = new TmsTytenPlanEditDetailDto(totalWsPlan, true);
		detailDtoList.add(detailDto);
		return new TmsTytenPlanEditResultDto(tmsTytenMstUn, plannedProd, detailDtoList);
	}

	// 特約店別計画 編集
	public void editWsPlan(TmsTytenPlanEditInputDto tmsTytenPlanEditInputDto) throws LogicalException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanEditInputDto == null) {
			final String errMsg = "特約店別計画編集対象DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<WsPlan> wsPlanList = tmsTytenPlanEditInputDto.getWsPlanList();
		if (wsPlanList == null || wsPlanList.isEmpty()) {
			final String errMsg = "特約店別計画編集対象リストがnullまたは空";
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
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.IYAKU);
		if (sysManage == null) {
			final String errMsg = "ユーザ情報からシステム管理情報が取得出来ない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// S価の追加処理
		// 配分中は実行不可
		// 許可しないステータスリスト作成
		List<WsDistStatusForCheck> unallowableDistStatusList = new ArrayList<WsDistStatusForCheck>();
		unallowableDistStatusList.add(WsDistStatusForCheck.DISTING);
		// カテゴリを品目コードから取得
		String category;
		try {
			category = plannedProdDAO.search(tmsTytenPlanEditInputDto.getWsPlanList().get(0).getProdCode()).getCategory();
		} catch (DataNotFoundException e) {
			final String errMsg = "対象品目のカテゴリが取得できない：" + tmsTytenPlanEditInputDto.getWsPlanList().get(0).getProdCode();
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

// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// Y/B価の追加処理
		// S変換後はY/Bの追加処理不可
		if (kaBaseKb.equals(KaBaseKb.Y_KA_BASE) && sysManage.getTransTFlg()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3317E")));
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// S変換前はS価の追加処理不可
		if (kaBaseKb.equals(KaBaseKb.S_KA_BASE) && !sysManage.getTransTFlg()) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3315E")));
		}

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		for (WsPlan wsPlan : wsPlanList) {
			WsPlan record = null;
			try {
				record = wsPlanDao.search(wsPlan.getSeqKey());
				record.setSeqKey(wsPlan.getSeqKey());
				record.setSosCd(wsPlan.getSosCd());
				record.setUpDate(wsPlan.getUpDate());
				record.setPlannedValueUh(wsPlan.getPlannedValueUh());
				record.setPlannedValueP(wsPlan.getPlannedValueP());
				wsPlanDao.update(record);
			} catch (DataNotFoundException e) {
				final String errMsg = "：特約店別計画編集処理で、更新対象が見つからないため、楽観的ロックエラーとする";
				throw new OptimisticLockingFailureException(errMsg);
			}
		}
	}

	// 特約店別計画 参照登録
	public void editWsPlanRefer(TmsTytenPlanEditInputDto tmsTytenPlanEditInputDto) throws LogicalException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanEditInputDto == null) {
			final String errMsg = "特約店別計画編集対象DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<WsPlan> wsPlanList = tmsTytenPlanEditInputDto.getWsPlanList();
		if (wsPlanList == null || wsPlanList.isEmpty()) {
			final String errMsg = "特約店別計画編集対象リストがnullまたは空";
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
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
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
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		for (WsPlan wsPlan : wsPlanList) {
			WsPlan record = null;
			try {
				record = wsPlanDao.search(wsPlan.getSeqKey());
				record.setSeqKey(wsPlan.getSeqKey());
				record.setSosCd(wsPlan.getSosCd());
				record.setUpDate(wsPlan.getUpDate());
				record.setPlannedValueUh(wsPlan.getPlannedValueUh());
				record.setPlannedValueP(wsPlan.getPlannedValueP());
				wsPlanDao.update(record);
			} catch (DataNotFoundException e) {
				final String errMsg = "：特約店別計画編集処理で、更新対象が見つからないため、楽観的ロックエラーとする";
				throw new OptimisticLockingFailureException(errMsg);
			}
		}
	}

}
