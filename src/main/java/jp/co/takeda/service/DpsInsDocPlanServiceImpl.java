package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsDocPlanDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.InsPlanDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dto.InsDocPlanJgiListUpdateDto;
import jp.co.takeda.dto.InsDocPlanProdListUpdateDto;
import jp.co.takeda.dto.InsDocPlanUpDateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsDocStatusForCheck;
import jp.co.takeda.logic.div.InsDocStatusUpdateType;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.InsDocPlan;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.StatusForInsDocPlan;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 施設医師別計画機能の更新に関するサービス実装クラス
 * 
 * @author nozaki
 */
@Transactional
@Service("dpsInsDocPlanService")
public class DpsInsDocPlanServiceImpl implements DpsInsDocPlanService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsInsDocPlanServiceImpl.class);

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 計画品目ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 従業員情報
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 施設医師別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanDao")
	protected InsDocPlanDao insDocPlanDao;

	/**
	 * 施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insPlanDao")
	protected InsPlanDao insPlanDao;

	/**
	 * 施設医師別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanStatusDao")
	protected InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * 担当者別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設医師別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocStatusCheckService")
	protected DpsInsDocStatusCheckService dpsInsDocStatusCheckService;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	// 施設医師別計画の登録
	public void entryInsDocPlan(List<InsDocPlanUpDateDto> upDateList, InsType insType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (upDateList == null || upDateList.size() == 0) {
			final String errMsg = "更新行、追加行DTOのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (InsDocPlanUpDateDto upDateDto : upDateList) {
			if (upDateDto.getJgiNo() == null) {
				final String errMsg = "従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (upDateDto.getProdCode() == null) {
				final String errMsg = "品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		String prodCode = upDateList.get(0).getProdCode();
		Integer jgiNo = upDateList.get(0).getJgiNo();

		// 計画対象品目取得
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// 従業員情報取得
		JgiMst jgiMst = null;
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
		} catch (DataNotFoundException e) {
			final String errMsg = "従業員情報取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ---------------------------
		// ステータスチェック
		// ---------------------------
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		jgiMstList.add(jgiMst);
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		plannedProdList.add(plannedProd);
		try {
			// 施設医師別計画ステータスチェック(許可しないステータスリスト作成)
			// 配分済、公開中 以外はだめ
			List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
			unallowableInsDocStatusList.add(InsDocStatusForCheck.NOTHING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_COMPLETED);
			dpsInsDocStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsDocStatusList);
		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3345E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

		// ---------------------------
		// 登録処理
		// ---------------------------
		for (InsDocPlanUpDateDto rowDto : upDateList) {
			
			// 増分修正値
			Long plannedIncValue = ConvertUtil.parseMoneyToNormalUnit(rowDto.getPlannedIncValue());
			// 計画確定値
			Long plannedValue = ConvertUtil.parseMoneyToNormalUnit(rowDto.getPlannedValue());
			
			InsDocPlan insDocPlan = new InsDocPlan();
			insDocPlan.setSeqKey(rowDto.getSeqKey());
			insDocPlan.setJgiNo(jgiNo);
			insDocPlan.setProdCode(prodCode);
			insDocPlan.setInsNo(rowDto.getInsNo());
			insDocPlan.setMainInsNo(rowDto.getMainInsNo());
			insDocPlan.setDocNo(rowDto.getDocNo());
			insDocPlan.setExceptDistInsFlg(rowDto.getExceptDistInsFlg());
			insDocPlan.setDelInsFlg(rowDto.getDelInsFlg());
			insDocPlan.setNonPatientCnt(null);
			insDocPlan.setTotPatientCnt(null);
			insDocPlan.setTkdPatientCnt(null);
			insDocPlan.setCurrentPeriod(null);
			insDocPlan.setAdvancePeriod(null);
			insDocPlan.setTheoreticalIncValueY(null);
			insDocPlan.setPlannedIncValueY(plannedIncValue);
			insDocPlan.setTheoreticalValueY(null);
			insDocPlan.setPlannedValueY(plannedValue);
			insDocPlan.setUpDate(rowDto.getPlannedUpDate());
			
			try {
				if (rowDto.getSeqKey() == null) {
					
					// 既存計画がなく、修正値の入力があった行のみ登録する
					if (insDocPlan.getPlannedIncValueY() != null) {
						insDocPlanDao.insert(insDocPlan);
					}
					
				} else {

					// 既存計画がある場合
					InsDocPlan tmpInsDocPlan = insDocPlanDao.search(insDocPlan.getSeqKey());
					
					// 修正値がない、かつ、計画理論値がない場合
					if (insDocPlan.getPlannedIncValueY() == null && tmpInsDocPlan.getTheoreticalValueY() == null) {
						insDocPlanDao.delete(insDocPlan.getSeqKey(), insDocPlan.getUpDate());
						continue;
					}

//					// 修正値が変わらない場合は何もしない
//					if(insDocPlan.getPlannedIncValueY() != null && insDocPlan.getPlannedIncValueY().equals( tmpInsDocPlan.getPlannedIncValueY())){
//						continue;
//					}
//					if(insDocPlan.getPlannedIncValueY() == null && tmpInsDocPlan.getPlannedIncValueY() == null){
//						continue;
//					}

					// 更新
					insDocPlanDao.update(insDocPlan);
				}
				
			} catch (DataNotFoundException e) {
				// 更新対象のデータが無い場合、楽観的排他エラー
				final String errMsg = "施設特約店更新時に更新対象が見つからないため、楽観的ロックエラーとする";
				throw new OptimisticLockingFailureException(errMsg, e);
				
			} catch (DuplicateException e) {
				// 更新対象のデータがすでに存在する場合、楽観的排他エラー
				final String errMsg = "施設特約店更新時に更新対象が見つからないため、楽観的ロックエラーとする";
				throw new OptimisticLockingFailureException(errMsg, e);
			}
		}
		
		// 施設別計画を削除、積上げ登録
		insPlanDao.deleteTumiage(jgiNo, prodCode, insType);
		insPlanDao.insertTumiage(jgiNo, prodCode, insType);
	}

	// ステータス更新(品目一覧)
	public void updateStatus(String sosCd3, String sosCd4, Integer jgiNo, List<InsDocPlanProdListUpdateDto> insDocPlanUpdateDtoList, InsDocStatusUpdateType updateType) {

		// -----------------------------------
		// 引数チェック
		// -----------------------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "検索条件の組織コード・従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocPlanUpdateDtoList == null || insDocPlanUpdateDtoList.size() == 0) {
			final String errMsg = "施設医師別計画 品目一覧更新用DTOがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateType == null) {
			final String errMsg = "施設医師別計画ステータス更新種別がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員情報リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		if (jgiNo != null) {
			try {
				JgiMst jgiMst = jgiMstDAO.search(jgiNo);
				jgiMstList.add(jgiMst);
			} catch (DataNotFoundException e) {
				final String errMsg = "再配分対象の従業員情報が存在しない:";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + jgiNo), e);
			}
		}

		// 品目情報リスト作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (InsDocPlanProdListUpdateDto insDocPlanUpdateDto : insDocPlanUpdateDtoList) {
			PlannedProd plannedProd = insDocPlanUpdateDto.convertPlannedProd();
			plannedProd.setPlanLevelInsDoc(true);
			plannedProdList.add(plannedProd);
		}

		// -----------------------------------
		// ステータス最終更新日チェック
		// -----------------------------------
		for (InsDocPlanProdListUpdateDto insDocPlanUpdateDto : insDocPlanUpdateDtoList) {
			String prodCode = insDocPlanUpdateDto.getProdCode();
			Date orgLastUpdate = insDocPlanUpdateDto.getUpDate();

			if (jgiNo != null) {
				insDocPlanStatusDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
			} else {
				insDocPlanStatusDao.checkLastUpDate(sosCd3, sosCd4, null, prodCode, orgLastUpdate);
			}
		}

		// -----------------------------------
		// 実行
		// -----------------------------------
		switch (updateType) {
			case OPEN:
				doOpen(sosCd3, sosCd4, jgiMstList, plannedProdList);
				break;
			case OPEN_CANCEL:
				doOpenCancel(sosCd3, sosCd4, jgiMstList, plannedProdList);
				break;
			case FIX:
				doFix(sosCd3, sosCd4, jgiMstList, plannedProdList);
				break;
			case FIX_CANCEL:
				doFixCancel(sosCd3, sosCd4, jgiMstList, plannedProdList);
				break;
			default:
				break;
		}
	}

	// ステータス更新(担当者一覧)
	public void updateStatus(String sosCd3, String prodCode, List<InsDocPlanJgiListUpdateDto> insDocPlanUpdateDtoList, InsDocStatusUpdateType updateType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocPlanUpdateDtoList == null || insDocPlanUpdateDtoList.size() == 0) {
			final String errMsg = "施設医師別計画 品目一覧更新用DTOがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateType == null) {
			final String errMsg = "施設医師別計画ステータス更新種別がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 従業員情報リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		for (InsDocPlanJgiListUpdateDto insDocPlanUpdateDto : insDocPlanUpdateDtoList) {
			jgiMstList.add(insDocPlanUpdateDto.convertJgiMst());
		}

		// 品目情報リスト作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		try {
			PlannedProd plannedProd = plannedProdDAO.search(prodCode);
			plannedProdList.add(plannedProd);
		} catch (DataNotFoundException e) {
			final String errMsg = "再配分対象の計画品目が存在しない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// -----------------------------------
		// ステータス最終更新日チェック
		// -----------------------------------
		for (InsDocPlanJgiListUpdateDto insDocPlanUpdateDto : insDocPlanUpdateDtoList) {
			Integer jgiNo = insDocPlanUpdateDto.getJgiNo();
			Date orgLastUpdate = insDocPlanUpdateDto.getUpDate();
			insDocPlanStatusDao.checkLastUpDate(jgiNo, prodCode, orgLastUpdate);
		}

		// ----------------------
		// 実行
		// ----------------------
		switch (updateType) {
			case OPEN:
				doOpen(sosCd3, null, jgiMstList, plannedProdList);
				break;
			case OPEN_CANCEL:
				doOpenCancel(sosCd3, null, jgiMstList, plannedProdList);
				break;
			case FIX:
				doFix(sosCd3, null, jgiMstList, plannedProdList);
				break;
			case FIX_CANCEL:
				doFixCancel(sosCd3, null, jgiMstList, plannedProdList);
				break;
			default:
				break;
		}

	}

	/**
	 * 施設医師別計画公開処理を行う。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doOpen(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// -------------------------------------
		// 引数チェック
		// -------------------------------------
		if (sosCd3 == null && sosCd4 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------------------
		// 担当者別計画ステータスチェック
		// -------------------------------------
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		try {
			// ステータスチェック実行
			dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3340E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}
		
		// -------------------------------------
		//  施設医師別計画ステータスチェック
		// -------------------------------------
		List<InsDocPlanStatus> insDocPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
			unallowableInsDocStatusList.add(InsDocStatusForCheck.NOTHING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_OPENED);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd4)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsDocStatusList);
			} else {
				final String errMsg = "施設医師別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3340E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		// 公開日時取得
		Date openDate = commonDao.getSystemTime();
		for (InsDocPlanStatus insDocPlanStatus : insDocPlanStatusList) {
			insDocPlanStatus.setStatusForInsDocPlan(StatusForInsDocPlan.PLAN_OPENED);
			insDocPlanStatus.setInsDocOpenDate(openDate);
			insDocPlanStatusDao.update(insDocPlanStatus);
		}
	}

	/**
	 * 施設医師別計画公開解除処理を行う。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doOpenCancel(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// -------------------------------------
		// 引数チェック
		// -------------------------------------
		if (sosCd3 == null && sosCd4 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------------------
		//  施設医師別計画ステータスチェック
		// -------------------------------------
		List<InsDocPlanStatus> insDocPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
			unallowableInsDocStatusList.add(InsDocStatusForCheck.NOTHING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTED);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd4)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsDocStatusList);
			} else {
				final String errMsg = "施設医師別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3341E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		for (InsDocPlanStatus insDocPlanStatus : insDocPlanStatusList) {
			insDocPlanStatus.setStatusForInsDocPlan(StatusForInsDocPlan.DISTED);
			insDocPlanStatusDao.update(insDocPlanStatus);
		}
	}

	/**
	 * 施設医師別計画確定処理を行う。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doFix(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// -------------------------------------
		// 引数チェック
		// -------------------------------------
		if (sosCd3 == null && sosCd4 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------------------
		//  施設医師別計画ステータスチェック
		// -------------------------------------
		List<InsDocPlanStatus> insDocPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
			unallowableInsDocStatusList.add(InsDocStatusForCheck.NOTHING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTED);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd4)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsDocStatusList);
			} else {
				final String errMsg = "施設医師別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3342E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		Date fixDate = commonDao.getSystemTime();
		for (InsDocPlanStatus insDocPlanStatus : insDocPlanStatusList) {
			insDocPlanStatus.setStatusForInsDocPlan(StatusForInsDocPlan.PLAN_COMPLETED);
			insDocPlanStatus.setInsDocFixDate(fixDate);
			insDocPlanStatusDao.update(insDocPlanStatus);
		}
	}

	/**
	 * 施設特約店別計画確定解除処理を行う。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiMstList 対象の従業員情報のリスト
	 * @param plannedProdList 対象の品目情報のリスト
	 */
	private void doFixCancel(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && (jgiMstList == null || jgiMstList.size() == 0)) {
			final String errMsg = "公開対象の組織コードまたは従業員情報がない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "配分対象の計画品目情報がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------------------
		// 施設特約店別計画ステータス
		// -------------------------------------
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd4)) {
				dpsInsWsStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsWsStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				dpsInsWsStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);
			} else {
				final String errMsg = "施設特約店別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3343E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// -------------------------------------
		//  施設医師別計画ステータスチェック
		// -------------------------------------
		List<InsDocPlanStatus> insDocPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
			unallowableInsDocStatusList.add(InsDocStatusForCheck.NOTHING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTED);
			unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_OPENED);

			// ステータスチェック実行
			if (jgiMstList != null && jgiMstList.size() != 0) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd4)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, sosCd4, plannedProdList, unallowableInsDocStatusList);
			} else if (!StringUtils.isEmpty(sosCd3)) {
				insDocPlanStatusList = dpsInsDocStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsDocStatusList);
			} else {
				final String errMsg = "施設医師別計画ステータス更新条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3343E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------
		for (InsDocPlanStatus insDocPlanStatus : insDocPlanStatusList) {
			insDocPlanStatus.setStatusForInsDocPlan(StatusForInsDocPlan.PLAN_OPENED);
			insDocPlanStatusDao.update(insDocPlanStatus);
		}
	}
}
