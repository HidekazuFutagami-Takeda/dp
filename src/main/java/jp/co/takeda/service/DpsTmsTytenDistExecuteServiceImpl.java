package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Scale;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.DeliveryResultDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosDistMissDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.WsPlanDao;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.dto.TmsTytenDistDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.DelInsLogic;
import jp.co.takeda.logic.WsPlanDistValuePComparator;
import jp.co.takeda.logic.WsPlanDistValueUhComparator;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.DeliveryResult;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosDistMiss;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.OutputType;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.StatusForWs;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.CurrencyUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 特約店別計画配分処理 サービス実装クラス
 *
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenDistExecuteService")
public class DpsTmsTytenDistExecuteServiceImpl implements DpsTmsTytenDistExecuteService {
	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsTmsTytenDistExecuteServiceImpl.class);
	/**
	 * 特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusDao")
	protected WsPlanStatusDao wsPlanStatusDao;

	/**
	 * 納入計画システム管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * DB共通情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultDao")
	DeliveryResultDao deliveryResultDao;

	/**
	 * 出力ファイル情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("outputFileDao")
	protected OutputFileDao outputFileDao;

	/**
	 * 組織別配分ミス情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosDistMissDao")
	protected SosDistMissDao sosDistMissDao;

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

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
	 * 業務連絡サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsContactOperationsService")
	protected DpsContactOperationsService dpsContactOperationsService;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// 品目単位の配分処理を実行する
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public TmsTytenDistDto executeDistProd(SosMst sosMst2, PlannedProd plannedProd, List<SosMst> sosMst3List, List<DistParamOffice> distParamOfficeUHList,
		List<DistParamOffice> distParamOfficePList, DpUser dpUser, Date startTime, TmsTytenDistDto tmsTytenDistDto) {
		// ----------------------
		// 引数チェック
		// ----------------------
		// 組織情報(支店)チェック
		if (sosMst2 == null) {
			final String errMsg = "組織情報(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 品目情報チェック
		if (plannedProd == null) {
			final String errMsg = "品目情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織コード(営業所)リストチェック
		if (sosMst3List == null) {
			final String errMsg = "組織コード(営業所)リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (sosMst3List.size() == 0) {
			final String errMsg = "組織コード(営業所)リストが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分基準(UH)チェック
		if (distParamOfficeUHList == null) {
			final String errMsg = "配分基準(UH)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (distParamOfficeUHList.size() == 0) {
			final String errMsg = "配分基準(UH)が0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分基準(P)チェック
		if (distParamOfficePList == null) {
			final String errMsg = "配分基準(P)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (distParamOfficePList.size() == 0) {
			final String errMsg = "配分基準(P)が0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 実行者従業員情報チェック
		if (dpUser == null) {
			final String errMsg = "実行者従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------------------
		// 特約店別計画立案ステータスチェック
		// -----------------------------------------
		try {
			// 許可しない配分ステータスリスト作成
			List<WsDistStatusForCheck> unallowableDistStatusList = new ArrayList<WsDistStatusForCheck>();
			unallowableDistStatusList.add(WsDistStatusForCheck.DISTED);
			unallowableDistStatusList.add(WsDistStatusForCheck.NONE);
			unallowableDistStatusList.add(WsDistStatusForCheck.NOTHING);

			// 許可しないスライド状況リスト作成
			List<WsSlideStatusForCheck> unallowableSlideStatusList = new ArrayList<WsSlideStatusForCheck>();
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDING);
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDED);

			// ステータスチェック実行
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(plannedProd);
			dpsWsStatusCheckService.execute(sosMst2, plannedProdList, unallowableDistStatusList, unallowableSlideStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスチェックエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3270E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ------------------------------------------
		// 納入計画システム管理ステータスチェック
		// ------------------------------------------
		SysManage sysManage = null;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.IYAKU);
		} catch (DataNotFoundException e) {
			// 計画対象品目が存在しない場合
			final String errMsg = "納入計画システム管理が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// T価変換バッチ実施フラグが実行済みか判定
		if (sysManage.getTransTFlg()) {
			// T価変換バッチ実施フラグが実行済の場合 ステータスチェックエラー
			MessageKey messageKey = new MessageKey("DPS3271E");
			throw new UnallowableStatusException(new Conveyance(messageKey));
		}

		// ----------------------
		// 全営業所を配分
		// ----------------------
		for (SosMst sosMst3 : sosMst3List) {

// mod start 2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//			// メモ：2015年上期組織変更で、ONC組織は支店配下から移動、かつ、ONC品の特約店別計画配分は実施されないので、下記条件がくることはない
//			// ONC-G、かつ、ONC品以外の場合はスキップ
//			if(plannedProd.getCategory() != ProdCategory.ONC && BooleanUtils.isTrue(sosMst3.getOncSosFlg())){
//				continue;
//			}

// del start 2021/04/20 組織のカテゴリ・サブカテゴリを使用しないので削除
//			//配分対象の品目のカテゴリーが、対象組織のカテゴリ・サブカテゴリに設定されていない場合はスキップする　※2018上期時点でスキップされるものは存在しない　※スキップの例 ： 過去あったように、同一支店配下にMMP組織とONC組織が混在する場合等にスキップが発生する。
//			if(plannedProd.getCategory() != sosMst3.getSosCategory()
//			&& plannedProd.getCategory() != sosMst3.getSosSubCategory()) {
//				continue;
//			}
// del end 2021/04/20

// mod end   2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// 配分基準(UH)検索
			DistParamOffice distParamOfficeUH = searchDistParamOffice(sosMst3.getSosCd(), plannedProd.getProdCode(), distParamOfficeUHList);

			// 配分基準(P)検索
			DistParamOffice distParamOfficeP = searchDistParamOffice(sosMst3.getSosCd(), plannedProd.getProdCode(), distParamOfficePList);

			// ----------------------
			// 特約店別納入実績取得【配分品目】
			// ----------------------
			// 納入実績取得
			List<DeliveryResult> deliveryResultList = searchDeliveryResultList(sosMst3.getSosCd(), distParamOfficeUH.getBaseParam().getRefProdCode());

			// 納入実績振り分け
			List<DeliveryResult> deliveryResultUHList = new ArrayList<DeliveryResult>();
			for (DeliveryResult deliveryResult : deliveryResultList) {
				// 削除施設は配分対象外
				if (new DelInsLogic(deliveryResult.getReqFlg(), deliveryResult.getDelFlg()).getDelFlg()) {
					continue;
				}
				//*** 2021/05/11 現行のまま、施設情報を使用する為、コメント削除 ***
//				//配分除外施設は配分対象外
//				if (deliveryResult.getExceptFlg()) {
//					continue;
//				}
				if (InsType.UH.equals(deliveryResult.getInsType())) {
					deliveryResultUHList.add(deliveryResult);
				}
			}

			// 納入実績取得
			deliveryResultList = searchDeliveryResultList(sosMst3.getSosCd(), distParamOfficeP.getBaseParam().getRefProdCode());

			// 納入実績振り分け
			List<DeliveryResult> deliveryResultPList = new ArrayList<DeliveryResult>();
			for (DeliveryResult deliveryResult : deliveryResultList) {
				// 削除施設は配分対象外
				if (new DelInsLogic(deliveryResult.getReqFlg(), deliveryResult.getDelFlg()).getDelFlg()) {
					continue;
				}
				//*** 2021/05/11 現行のまま、施設情報を使用する為、コメント削除 ***
//				//配分除外施設は配分対象外
//				if (deliveryResult.getExceptFlg()) {
//					continue;
//				}
				if (InsType.P.equals(deliveryResult.getInsType())) {
					deliveryResultPList.add(deliveryResult);
				}
				// ワクチンの場合、Pに雑を含める
				if (dpsCodeMasterSearchService.isVaccine(plannedProd.getCategory()) && InsType.ZATU.equals(deliveryResult.getInsType())) {
					deliveryResultPList.add(deliveryResult);
				}
			}

			// ----------------------
			// 特約店別計画削除
			// ----------------------
			wsPlanDao.deleteSosProd(plannedProd.getProdCode(), sosMst3.getSosCd());

			// ----------------------
			// 配分実行(UH)
			// ----------------------
			// 配分実行(UH)
			tmsTytenDistDto = executeDist(sosMst3, plannedProd, InsType.UH, distParamOfficeUH, deliveryResultUHList, dpUser, startTime, tmsTytenDistDto, sosMst2);

			// ----------------------
			// 配分実行(P)
			// ----------------------
			// 配分実行(P)
			tmsTytenDistDto = executeDist(sosMst3, plannedProd, InsType.P, distParamOfficeP, deliveryResultPList, dpUser, startTime, tmsTytenDistDto, sosMst2);
		}

		// -------------------------------------
		// 特約店別計画立案ステータス更新
		// -------------------------------------
		// 特約店別計画立案ステータス検索
		WsPlanStatus wsPlanStatus = null;
		try {
			wsPlanStatus = wsPlanStatusDao.search(sosMst2.getSosCd(), plannedProd.getProdCode());
		} catch (DataNotFoundException e) {
			// 特約店別計画立案ステータスが存在していない場合
			// 実行前に特約店別計画立案ステータスを登録しているので存在しているはずなので、
			// 存在していない場合はエラー
			final String errMsg = "特約店別計画立案ステータスが存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 特約店別計画立案ステータス更新情報設定
		Date systemTime = commonDao.getSystemTime();
		wsPlanStatus.setStatusDistForWs(StatusForWs.DISTED);
		wsPlanStatus.setDistEndDate(systemTime);

		// 特約店別計画立案ステータス更新実行
		wsPlanStatusDao.update(wsPlanStatus);

		return tmsTytenDistDto;
	}

	// 特約店別計画立案ステータスを実行前の状態に戻す
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void rollBackStatus(SosMst sosMst2, PlannedProd plannedProd, List<WsPlanStatus> beforeWsPlanStatusList) throws LogicalException {
		// ------------------------------------------
		// 特約店別計画立案ステータスロールバック
		// ------------------------------------------
		// 対象の特約店別計画立案ステータスを検索
		WsPlanStatus beforeWsPlanStatus = searchWsPlanStatus(sosMst2.getSosCd(), plannedProd.getProdCode(), beforeWsPlanStatusList);

		// 更新対象の特約店別計画立案ステータスを取得
		WsPlanStatus wsPlanStatus = wsPlanStatusDao.search(sosMst2.getSosCd(), plannedProd.getProdCode());

		// 実行前に特約店別計画立案ステータスが存在していたか判定
		if (beforeWsPlanStatus.getSeqKey() != null) {
			// 実行前に特約店別計画立案ステータスが存在していた場合
			// ステータスを戻す
			beforeWsPlanStatus.setUpDate(wsPlanStatus.getUpDate());
			wsPlanStatusDao.update(beforeWsPlanStatus);

		} else {
			// 実行前に特約店別計画立案ステータスが存在していなかった場合
			// 特約店別計画立案ステータスを削除する
			wsPlanStatusDao.delete(wsPlanStatus.getSeqKey(), wsPlanStatus.getUpDate());
		}
	}

	/**
	 * 配分処理を実行する。
	 *
	 * @param sosMst3 組織情報(営業所)
	 * @param plannedProd 品目情報
	 * @param insType 施設出力対象区分
	 * @param distParamOffice 配分基準
	 * @param deliveryResultList 配分品目の納入実績
	 * @param dpUser 実行者従業員情報
	 * @param startTime 配信開始日時
	 * @param missListFileId 配分ミスリストファイルID
	 * @param errorPlannedProdList エラー品目リスト
	 * @return 特約店別計画配分DTO
	 */
	protected TmsTytenDistDto executeDist(SosMst sosMst3, PlannedProd plannedProd, InsType insType, DistParamOffice distParamOffice, List<DeliveryResult> deliveryResultList,
		DpUser dpUser, Date startTime, TmsTytenDistDto tmsTytenDistDto, SosMst sosMst2) {
		// ------------------------------
		// 特約店毎の納入実績を集計
		// ------------------------------
		// 担当者毎の納入実績を特約店毎に集計
		// 特約店コードをキーとした、集計MAPを生成
		RefPeriod fromRefPeriod = distParamOffice.getBaseParam().getRefFrom();
		RefPeriod toRefPeriod = distParamOffice.getBaseParam().getRefTo();
		Map<String, Long> tytenDeliverySum = new HashMap<String, Long>();
		Long officeDeliverySum = 0L;
		for (DeliveryResult deliveryResult : deliveryResultList) {
			String tmsTytenCd = deliveryResult.getTmsTytenCd();
			Long deliveryRecordSum = summaryDeliveryRecord(deliveryResult.getMonNnu(), fromRefPeriod, toRefPeriod);
			// 実績値がマイナスの場合、0とする
			if (deliveryRecordSum < 0) {
				deliveryRecordSum = 0L;
			}

			// 集計MAPに特約店コード存在するか判定
			if (tytenDeliverySum.containsKey(tmsTytenCd)) {
				// 集計MAPに特約店コードが既に存在する
				Long summary = tytenDeliverySum.get(tmsTytenCd) + deliveryRecordSum;
				tytenDeliverySum.put(tmsTytenCd, summary);

			} else {
				// 集計MAPに特約店コードが存在しない
				tytenDeliverySum.put(tmsTytenCd, deliveryRecordSum);
			}

			// 営業所納入実績に加算
			if (officeDeliverySum != null) {
				officeDeliverySum += deliveryRecordSum;
			} else {
				officeDeliverySum = deliveryRecordSum;
			}
		}

		// ----------------------
		// 営業所計画取得
		// ----------------------
		OfficePlan officePlan = null;
		try {
			officePlan = officePlanDao.searchUk(sosMst3.getSosCd(), plannedProd.getProdCode());

		} catch (DataNotFoundException e) {
			// 営業所計画が存在しない場合
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			final String errMsg = "エリア計画が存在しない";
//			final String errMsg = "営業所計画が存在しない";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		// 計画値取得
		Long officePlannedValueY = null;
		if (InsType.UH.equals(insType)) {
			officePlannedValueY = officePlan.getPlannedValueUhY();

		} else if (InsType.P.equals(insType)) {
			officePlannedValueY = officePlan.getPlannedValuePY();
		}

		// (1)	配分基準に設定されているゼロ配分フラグを取得
		if (distParamOffice.getDistParam().getZeroDistFlg()) {

			// (2)	ゼロ配分フラグが真(「ゼロ配分を実行する」に設定されている)の場合、ゼロ配分モードで配分する
			if (LOG.isDebugEnabled()) {
				LOG.debug("ゼロ配分処理実行");
			}

			List<WsPlan> wsPlanList = new ArrayList<WsPlan>();
			for (String tytenCd : tytenDeliverySum.keySet()) {
				WsPlan wsPlan = createWsPlan(sosMst3.getSosCd(), plannedProd.getProdCode(), tytenCd, insType, 0L);
				wsPlanList.add(wsPlan);
			}

			// 特約店別計画 登録・更新
			updateWsPlan(wsPlanList);

		} else {

			// (3)	ゼロ配分フラグが偽(「ゼロ配分を実行しない」に設定されている)の場合、通常配分モードで配分する
			if (LOG.isDebugEnabled()) {
				LOG.debug("通常配分処理実行");
			}

			Long distValueSum = 0L;

			// 全特約店の配分データを生成する
			List<WsPlan> wsPlanList = new ArrayList<WsPlan>();
			for (String tytenCd : tytenDeliverySum.keySet()) {

				// (3)(4)	配分母数、および、納入実績構成比から、配分値を算出
				Long deliverySum = tytenDeliverySum.get(tytenCd);
				Long distValue = calcDistValue(officePlannedValueY, officeDeliverySum, deliverySum);

				// 配分値合計
				if (distValue != null) {
					distValueSum += distValue;
				}

				// 特約店別計画データ生成
				WsPlan wsPlan = createWsPlan(sosMst3.getSosCd(), plannedProd.getProdCode(), tytenCd, insType, distValue);
				wsPlanList.add(wsPlan);
			}

			// (5)	丸めによる誤差をもっとも理論値が大きい配分結果に足しこむ
			if (deliveryResultList != null && deliveryResultList.size() != 0 && officeDeliverySum > 0) {
				wsPlanList = deltaCorrection(wsPlanList, officePlannedValueY, distValueSum, insType);
			}

			// (7)	上位計画と計画値との差額を算出する
			Long allPlannedValue = 0L;
			for (WsPlan wsPlan : wsPlanList) {

				Long wsPlannedValue = null;
				if (InsType.UH.equals(insType)) {
					wsPlannedValue = wsPlan.getPlannedValueUh();
				} else if (InsType.P.equals(insType)) {
					wsPlannedValue = wsPlan.getPlannedValueP();
				}

				// 配分値が0より大きい場合は計画値合計に加算する
				if (wsPlannedValue != null && wsPlannedValue > 0) {
					allPlannedValue = MathUtil.add(allPlannedValue, wsPlannedValue);
				}
			}

			// 差額 ＝ 営業所計画 - 計画値集計
			Long totalDiffValue = MathUtil.sub(officePlannedValueY, allPlannedValue);

			// (8)	差額が0でない場合は配分ミスとする。
			if (totalDiffValue == null || totalDiffValue != 0) {
				tmsTytenDistDto = insertDsitMiss(sosMst3.getSosCd(), plannedProd.getProdCode(), insType, officePlannedValueY, totalDiffValue, startTime, tmsTytenDistDto, dpUser,
					"DPS2015W", sosMst2);
			}

			// -----------------------------
			// 特約店別計画 登録・更新
			// -----------------------------
			updateWsPlan(wsPlanList);
		}

		return tmsTytenDistDto;
	}

	/**
	 * 納入実績を取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 納入実績リスト
	 */
	protected List<DeliveryResult> searchDeliveryResultList(String sosCd3, String prodCode) {
		// 担当者一覧取得
		List<JgiMst> jgiMstList = null;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);

		} catch (DataNotFoundException e) {
			// 担当者一覧が取得できなかった場合
			// 担当者が取得できない場合でもエラーにしない
			jgiMstList = new ArrayList<JgiMst>();
		}

		// 担当者毎の納入実績取得
		List<DeliveryResult> deliveryResultList = new ArrayList<DeliveryResult>();
		for (JgiMst jgiMst : jgiMstList) {
			try {
				// 納入実績取得
				List<DeliveryResult> deliveryResultListTemp = deliveryResultDao.searchByProd(DeliveryResultDao.SORT_STRING2, prodCode, jgiMst.getJgiNo(), null);

				deliveryResultList.addAll(deliveryResultListTemp);

			} catch (DataNotFoundException e) {
				// 納入実績が存在しない場合
				// 納入実績が存在しない場合でもエラーにしない
			}
		}

		return deliveryResultList;
	}

	/**
	 * 配分ミス情報を登録する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param plannedValue 計画値
	 * @param diffValue 差分
	 * @param startTime 配信開始日時
	 * @param missListFileId 配分ミスリストファイルID
	 * @param dpUser 実行者従業員情報
	 * @param messageCode メッセージコード
	 */
	protected TmsTytenDistDto insertDsitMiss(String sosCd3, String prodCode, InsType insType, Long plannedValue, Long diffValue, Date startTime, TmsTytenDistDto tmsTytenDistDto,
		DpUser dpUser, String messageCode, SosMst sosMst2) {
		// 配分ミスリストファイルIDが生成済みか判定
		Long missListFileId = tmsTytenDistDto.getMissListFileId();
		List<String> errorProdNameList = tmsTytenDistDto.getErrorProdNameList();
		if (missListFileId == null) {
			// 配分ミスリストファイルIDが未生成の場合
			// ----------------------
			// ファイル出力情報登録
			// ----------------------
			missListFileId = outputFileDao.getSeqKey();
			OutputFile outputFile = new OutputFile();
			outputFile.setSeqKey(missListFileId);
			outputFile.setOutputType(OutputType.WS_PLAN_DIST);
			outputFile.setFreeData(DateUtil.toString(startTime, "yyyy/MM/dd HH:mm"));
			// ファイル名(ファイル種類/医薬支店C/現在日付)
			String nowTime = DateUtil.toString(commonDao.getSystemTime(), "yyyyMMddHHmm");
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			outputFile.setOutputFileName(OutputType.convertFileName(SysType.IYAKU, OutputType.WS_PLAN_DIST) + "_" + sosMst2.getBrCode() + "_" + nowTime + ".xls");
			outputFile.setOutputFileName(OutputType.convertFileName(SysType.IYAKU, OutputType.WS_PLAN_DIST) + "_" + sosMst2.getBrCode() + "_" + nowTime + ".xlsx");
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			// ファイル出力情報登録実行
			try {
				outputFileDao.insert(outputFile, dpUser);
			} catch (DuplicateException e) {
				final String errMsg = "ファイル出力情報登録時に重複データが存在する";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
			}
		}

		// ----------------------
		// 組織別配分ミス情報登録
		// ----------------------
		SosDistMiss sosDistMiss = new SosDistMiss();
		sosDistMiss.setSosCd(sosCd3);
		sosDistMiss.setProdCode(prodCode);
		sosDistMiss.setInsType(insType);
		sosDistMiss.setPlannedValue(plannedValue);
		sosDistMiss.setDiffValue(diffValue);
		sosDistMiss.setOutputFileId(missListFileId);
		sosDistMiss.setMessageCode(messageCode);

		// 組織別配分ミス情報登録実行
		try {
			sosDistMissDao.insert(sosDistMiss, dpUser);
		} catch (DuplicateException e) {
			final String errMsg = "組織別配分ミス情報登録に重複データが存在する";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		}

		if (!errorProdNameList.contains(prodCode)) {
			errorProdNameList.add(prodCode);
		}

		return new TmsTytenDistDto(missListFileId, errorProdNameList);
	}

	/**
	 * 特約店別計画と営業所計画の差分補正を行う。
	 *
	 * @param wsPlanList 特約店別計画
	 * @param officePlannedValueY 営業所計画値
	 * @param distValue 特約店別計画値
	 * @param insType 施設出力対象区分
	 * @return 補正済み特約店別計画
	 */
	protected List<WsPlan> deltaCorrection(List<WsPlan> wsPlanList, Long officePlannedValueY, Long distValue, InsType insType) {
		// ----------------------
		// 営業所計画と特約店別計画の差分補正
		// ----------------------
		// 営業所計画と特約店別計画の差分算出
		Long deltaValue = officePlannedValueY - distValue;

		// 特約店別計画を配分値でソート
		Comparator<WsPlan> comparator = null;
		if (InsType.UH.equals(insType)) {
			comparator = WsPlanDistValueUhComparator.getInstance();

		} else if (InsType.P.equals(insType)) {
			comparator = WsPlanDistValuePComparator.getInstance();
		}

		Collections.sort(wsPlanList, comparator);
		Collections.reverse(wsPlanList);

		// 差分を配分値上位の特約店に加算
		WsPlan wsPlan = wsPlanList.get(0);
		if (InsType.UH.equals(insType)) {
			Long plannedValue = wsPlan.getPlannedValueUh() + deltaValue;
			wsPlan.setDistValueUh(plannedValue);
			wsPlan.setPlannedValueUh(plannedValue);

		} else if (InsType.P.equals(insType)) {
			Long plannedValue = wsPlan.getPlannedValueP() + deltaValue;
			wsPlan.setDistValueP(plannedValue);
			wsPlan.setPlannedValueP(plannedValue);
		}

		// 補正済みの特約店別計画生成
		List<WsPlan> result = new ArrayList<WsPlan>();
		result.addAll(wsPlanList);
		result.set(0, wsPlan);

		return result;
	}

	/**
	 * 特約店別計画の登録・更新を行う。
	 *
	 * @param wsPlanList 特約店別計画リスト
	 */
	protected void updateWsPlan(List<WsPlan> wsPlanList) {
		// ----------------------
		// 特約店別計画 登録・更新
		// ----------------------
		for (WsPlan newWsPlan : wsPlanList) {
			// 配分値が0の場合、計画値はnullとする。
			if (newWsPlan.getDistValueUh() != null && newWsPlan.getDistValueUh() == 0) {
				newWsPlan.setPlannedValueUh(null);
			}
			if (newWsPlan.getDistValueP() != null && newWsPlan.getDistValueP() == 0) {
				newWsPlan.setPlannedValueP(null);
			}
			if (newWsPlan.getSeqKey() == null) {
				// 特約店別計画が存在しない場合は登録する
				try {
					wsPlanDao.insert(newWsPlan);
				} catch (DuplicateException e) {
					// 一意制約違反が発生した場合
					final String errMsg = "特約店別計画登録時に重複データが存在する";
					throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
				}
			} else {
				// 特約店別計画が存在する場合は更新する
				wsPlanDao.update(newWsPlan);
			}
		}
	}

	/**
	 * 特約店別計画データ生成を行う。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd TMS特約店コード
	 * @param insType 施設出力対象区分
	 * @param distValue 配分値
	 * @return 特約店別計画データ
	 */
	protected WsPlan createWsPlan(String sosCd3, String prodCode, String tmsTytenCd, InsType insType, Long distValue) {
		// ----------------------
		// 特約店別計画データ生成
		// ----------------------
		WsPlan wsPlan = null;
		try {
			wsPlan = wsPlanDao.searchUk(tmsTytenCd, prodCode, sosCd3, KaBaseKb.Y_KA_BASE);

		} catch (DataNotFoundException e) {
			// 特約店別計画データが存在しない場合
			wsPlan = new WsPlan();
			wsPlan.setSosCd(sosCd3);
			wsPlan.setProdCode(prodCode);
			wsPlan.setTmsTytenCd(tmsTytenCd);
			wsPlan.setKaBaseKb(KaBaseKb.Y_KA_BASE);
		}

		// 施設出力対象区分によって設定するカラムを分ける
		if (InsType.UH.equals(insType)) {
			wsPlan.setDistValueUh(distValue);
			wsPlan.setPlannedValueUh(distValue);

		} else if (InsType.P.equals(insType)) {
			wsPlan.setDistValueP(distValue);
			wsPlan.setPlannedValueP(distValue);
		}
		return wsPlan;
	}

	/**
	 * 配分値を算出する。
	 *
	 * @param officePlannedValueY 営業所計画
	 * @param officeDeliverySum 営業所納入実績
	 * @param deliverySum 特約店納入実績
	 */
	protected Long calcDistValue(Long officePlannedValueY, Long officeDeliverySum, Long deliverySum) {
		if (officePlannedValueY == null || officeDeliverySum == null || deliverySum == null) {
			return null;
		}
		if (officeDeliverySum == 0L) {
			return 0L;
		}
		// 特約店の割合を算出
		Double rate = MathUtil.divide(deliverySum, officeDeliverySum, 11, RoundingMode.HALF_UP);
		Long distValue = Double.valueOf(officePlannedValueY * rate).longValue();

		return CurrencyUtil.scale(distValue, Scale.TEN_THOUSAND, RoundingMode.HALF_UP);
	}

	/**
	 * 配分基準を検索する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param distParamOfficeList 配分基準リスト
	 * @return 配分基準
	 */
	protected DistParamOffice searchDistParamOffice(String sosCd3, String prodCode, List<DistParamOffice> distParamOfficeList) {
		DistParamOffice result = null;

		for (DistParamOffice distParamOffice : distParamOfficeList) {
			// 品目固定コードと組織コード(営業所)が一致しているか判定
			if (prodCode.equals(distParamOffice.getProdCode()) && sosCd3.equals(distParamOffice.getSosCd())) {
				// 品目固定コードと組織コード(営業所)が一致している場合
				result = distParamOffice;
				break;
			}
		}

		return result;
	}

	/**
	 * 特約店別計画立案ステータスを検索する。
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param prodCode 品目固定コード
	 * @param wsPlanStatusList 特約店別計画立案ステータスリスト
	 * @return 特約店別計画立案ステータス
	 */
	protected WsPlanStatus searchWsPlanStatus(String sosCd2, String prodCode, List<WsPlanStatus> wsPlanStatusList) {
		WsPlanStatus result = null;

		for (WsPlanStatus wsPlanStatus : wsPlanStatusList) {
			// 品目固定コードと組織コード(支店)が一致しているか判定
			if (prodCode.equals(wsPlanStatus.getProdCode()) && sosCd2.equals(wsPlanStatus.getSosCd())) {
				// 品目固定コードと組織コード(支店)が一致している場合
				result = wsPlanStatus;
				break;
			}
		}

		return result;
	}

	/**
	 * 指定期間の納入実績のサマリーを求める。
	 *
	 * @param monNnu 納入実績
	 * @param fromRefPeriod From期間
	 * @param toRefPeriod To期間
	 */
	protected Long summaryDeliveryRecord(MonNnu monNnu, RefPeriod fromRefPeriod, RefPeriod toRefPeriod) {
		Long summary = 0L;

		for (RefPeriod refPeriod : RefPeriod.values()) {
			// Fromより小さいか判定
			if (refPeriod.compareTo(fromRefPeriod) < 0) {
				// Fromより小さい場合、期間外なので読み飛ばす
				continue;
			}

			// Fromより大きいか判定
			if (refPeriod.compareTo(toRefPeriod) > 0) {
				// Fromより大きい場合、期間外なので終了する
				break;
			}

			// 期間の納入実績を加算する
			switch (refPeriod) {
				case REF_01:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord01());
					break;

				case REF_02:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord02());
					break;

				case REF_03:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord03());
					break;

				case REF_04:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord04());
					break;

				case REF_05:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord05());
					break;

				case REF_06:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord06());
					break;

				case REF_07:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord07());
					break;

				case REF_08:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord08());
					break;

				case REF_09:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord09());
					break;

				case REF_10:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord10());
					break;

				case REF_11:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord11());
					break;

				case REF_12:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord12());
					break;

				case REF_13:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord13());
					break;

				case REF_14:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord14());
					break;

				case REF_15:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord15());
					break;

				case REF_16:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord16());
					break;

				case REF_17:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord17());
					break;

				case REF_18:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord18());
					break;

				case REF_19:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord19());
					break;

				case REF_20:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord20());
					break;

				case REF_21:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord21());
					break;

				case REF_22:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord22());
					break;

				case REF_23:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord23());
					break;

				case REF_24:
					summary = MathUtil.add(summary, monNnu.getDeliveryRecord24());
					break;
			}
		}
		return summary;
	}

	// 業務連絡情報登録
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void entryContactOperations(ContactOperationsEntryDto contactOperationsEntryDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (contactOperationsEntryDto == null) {
			final String errMsg = "登録用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------
		// アテンション・お知らせ登録
		// --------------------------
		dpsContactOperationsService.entryAtt(contactOperationsEntryDto);
		dpsContactOperationsService.entryAnnounce(contactOperationsEntryDto);
	}
}
