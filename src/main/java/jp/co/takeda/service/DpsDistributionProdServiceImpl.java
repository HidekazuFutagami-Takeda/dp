package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.DistParamOfficeDao;
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DistParamUpdateDto;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.logic.div.PlanLevel;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * (医)配分機能の実行処理に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsDistributionProdService")
public class DpsDistributionProdServiceImpl implements DpsDistributionProdService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsDistributionProdServiceImpl.class);

	/**
	 * 共通DAO
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
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanDao;

	/**
	 * 配分基準（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamOfficeDao")
	protected DistParamOfficeDao distParamOfficeDao;

	/**
	 * 施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusDao")
	protected InsWsPlanStatusDao insWsPlanStatusDao;

	/**
	 * 営業所ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsOfficeStatusCheckService")
	protected DpsOfficeStatusCheckService dpsOfficeStatusCheckService;

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
	 * 施設特約店別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	/**
	 * 納入計画管理検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcSystemManageSearchService")
	protected DpcSystemManageSearchService dpsSystemManageSearchService;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画対象カテゴリ領域の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgSearchService")
	protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

	// 配分処理実行 前処理
	public void distributionPreparation(String sosCd, String category, List<DistributionExecOrgDto> distExecOrgDtoList, String appServerKbn) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distExecOrgDtoList == null || distExecOrgDtoList.size() == 0) {
			final String errMsg = "配分実行DTOがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (DistributionExecOrgDto distributionExecOrgDto : distExecOrgDtoList) {
			if (distributionExecOrgDto.getProdCode() == null) {
				final String errMsg = "配分実行対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (distributionExecOrgDto.getProdName() == null) {
				final String errMsg = "配分実行対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		SysType stype = null;
		if(dpsCodeMasterSearchService.isVaccine(category)) {
			stype = SysType.VACCINE;
		}else{
			stype = SysType.IYAKU;
		}

		// --------------------------------------------
		// 施設特約店別計画〆フラグチェック
		// --------------------------------------------

		// 施設特約店別計画〆フラグ
		SysManage sysManage = dpsSystemManageSearchService.searchSysManage(SysClass.DPS, stype);
		if (sysManage.getWsEndFlg()) {
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3263E")));
		}

		// --------------------------------------------
		// ステータスチェック
		// --------------------------------------------

		// チェック対象の品目情報作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (DistributionExecOrgDto distributionExecOrgDto : distExecOrgDtoList) {

			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(distributionExecOrgDto.getProdCode());
			plannedProd.setProdName(distributionExecOrgDto.getProdName());
			plannedProdList.add(plannedProd);
		}

		DpsPlannedCtg plannedCtg = dpsPlannedCtgSearchService.searchPlannedCtg(category);
		String planLevelMr = plannedCtg.getPlanLevelMr();
		// 営業所計画ステータスチェック(確定していないとだめ)
		if (planLevelMr.equals(PlanLevel.FALSE.getDbValue())) {
			try {

				// 許可しないステータスリスト作成
				List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
				unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
				unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);

				// チェック対象の組織情報作成
				SosMst sosMst = new SosMst();
				sosMst.setSosCd(sosCd);

				// チェック実行
				dpsOfficeStatusCheckService.executeForOffice(sosMst, category, unallowableOfficeStatusList);

			} catch (UnallowableStatusException e) {

				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				messageKeyList.add(new MessageKey("DPS3260E", "エリア計画"));
//				messageKeyList.add(new MessageKey("DPS3260E", "営業所計画"));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
			}

		// 担当者別計画ステータスチェック(確定していないとだめ) MMP品・ONC品
		} else if (planLevelMr.equals(PlanLevel.TRUE.getDbValue())) {
			// -------------------------------
			// 担当者別計画ステータスチェック
			// -------------------------------
			try {

				// 許可しないステータスリスト作成
				List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
				unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);

				// チェック実行
				dpsMrStatusCheckService.execute(sosCd, plannedProdList, unallowableMrStatusList);

			} catch (UnallowableStatusException e) {

				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3260E", "担当者別計画"));
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
			}
		}

		// 施設特約店別計画ステータスチェック(配分中、公開以降はだめ)
		try {

			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// チェック実行
			dpsInsWsStatusCheckService.execute(sosCd, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3261E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------

		Date distStartDate = commonDao.getSystemTime();
		try {
			// 品目ごとに処理
			for (DistributionExecOrgDto distributionExecOrgDto : distExecOrgDtoList) {

				// 施設特約店別計画立案ステータスの最終更新日が変わっていないかチェック
				// 変わっている場合は、排他エラーがスローされる
				String prodCode = distributionExecOrgDto.getProdCode();
				Date orgLastUpdate = distributionExecOrgDto.getInsWsPlanStatusLastUpdate();
				insWsPlanStatusDao.checkLastUpDate(sosCd, null, prodCode, orgLastUpdate);

				// 担当者ごとに、施設特約店別計画立案ステータスを配分中に更新、配分開始日時を設定、配分終了日時を初期化
				List<InsWsPlanStatus> insWsPlanStatusList = distributionExecOrgDto.getInsWsPlanStatusList();
				for (InsWsPlanStatus orgInsWsPlanStatus : insWsPlanStatusList) {
					InsWsPlanStatus insWsPlanStatus = orgInsWsPlanStatus.clone();
					if (insWsPlanStatus.getSeqKey() == null) {
						insWsPlanStatus.setAppServerKbn(appServerKbn);
						insWsPlanStatus.setAsyncBefStatus(null);
						insWsPlanStatus.setAsyncBefDistStartDate(null);
						insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTING);
						insWsPlanStatus.setDistStartDate(distStartDate);
						insWsPlanStatus.setDistEndDate(null);
						insWsPlanStatusDao.insert(insWsPlanStatus);
					} else {
						insWsPlanStatus.setAppServerKbn(appServerKbn);
						insWsPlanStatus.setAsyncBefStatus(orgInsWsPlanStatus.getStatusForInsWsPlan());
						insWsPlanStatus.setAsyncBefDistStartDate(orgInsWsPlanStatus.getDistStartDate());
						insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTING);
						insWsPlanStatus.setDistStartDate(distStartDate);
						insWsPlanStatus.setDistEndDate(null);
						insWsPlanStatusDao.update(insWsPlanStatus);
					}
				}
			}
		} catch (DuplicateException e) {
			final String errMsg = "担当者別計画立案ステータス登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}

		// ----------------------
		// 担当者情報チェック
		// ----------------------
		// 営業所配下の従業員取得
		try {
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				if (sosCd == null) {
					jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, SosMst.SOS_CD1, SosListType.TOKUYAKUTEN_LIST, BumonRank.IEIHON);
				}else {
					jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.TOKUYAKUTEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
				}
			} else {
				jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "指定された営業所配下の担当者がいない:";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd), e);
		}
	}

	// 特定施設個別計画を営業所案に戻す(配分品目一覧)
	public void updateSpecialInsPlan(String sosCd, List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsDistProdUpdateDtoList == null || insWsDistProdUpdateDtoList.size() == 0) {
			final String errMsg = "配分対象品目一覧更新用DTOがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// チェック対象の品目情報作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (InsWsDistProdUpdateDto insWsDistProdUpdateDto : insWsDistProdUpdateDtoList) {
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(insWsDistProdUpdateDto.getProdCode());
			plannedProd.setProdName(insWsDistProdUpdateDto.getProdName());
			plannedProdList.add(plannedProd);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 担当者別計画ステータスチェック(試算中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);

			// チェック実行
			dpsMrStatusCheckService.execute(sosCd, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3264E", "試算"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// 施設特約店別計画ステータスチェック(配分中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);

			// チェック実行
			dpsInsWsStatusCheckService.execute(sosCd, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3264E", "配分"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 更新処理
		// ----------------------
		for (PlannedProd plannedProd : plannedProdList) {
			final String prodCode = plannedProd.getProdCode();

			// ----------------------
			// 営業所案を取得
			// ----------------------
			List<SpecialInsPlan> specialInsPlanList = new ArrayList<SpecialInsPlan>();
			try {
				specialInsPlanList = specialInsPlanDao.searchBySosCd(SpecialInsPlanDao.SORT_STRING4, sosCd, prodCode, PlanType.OFFICE, null, false, plannedProd.getCategory());
			} catch (DataNotFoundException e) {
				// エラーにしない
			}

			// ----------------------
			// 削除処理(担当立者案)
			// ----------------------
			int deleteCount = specialInsPlanDao.deleteBySosCd(sosCd, prodCode, PlanType.MR);
			if (LOG.isDebugEnabled()) {
				LOG.debug("削除件数：" + deleteCount);
			}

			// ----------------------
			// 登録処理(営業所案を担当立者案として登録)
			// ----------------------
			try {
				for (SpecialInsPlan specialInsPlan : specialInsPlanList) {

					specialInsPlan.setPlanType(PlanType.MR);
					specialInsPlanDao.insert(specialInsPlan);
				}
			} catch (DuplicateException e) {
				final String errMsg = "特定施設個別計画(担当者立案)の登録に失敗";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
			}
		}
	}

	// 配分基準 営業所案登録・更新
	public void updateDistParamOffice(DistParamUpdateDto distributionParamUpdateDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (distributionParamUpdateDto == null) {
			final String errMsg = "配分基準更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeUH() == null) {
			final String errMsg = "配分基準　営業所案UHがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeP() == null) {
			final String errMsg = "配分基準　営業所案Pがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeUH().getSosCd() == null) {
			final String errMsg = "配分基準　営業所案UH組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeUH().getProdCode() == null) {
			final String errMsg = "配分基準　営業所案UH品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeP().getSosCd() == null) {
			final String errMsg = "配分基準P　営業所案P組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeP().getProdCode() == null) {
			final String errMsg = "配分基準P　営業所案P品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String sosCd = distributionParamUpdateDto.getDistParamOfficeUH().getSosCd();
		String prodCode = distributionParamUpdateDto.getDistParamOfficeUH().getProdCode();
		String prodName = distributionParamUpdateDto.getProdName();

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 施設特約店別計画ステータスチェック(配分中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);

			// チェック対象の品目情報作成
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(prodCode);
			plannedProd.setProdName(prodName);
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(plannedProd);

			// チェック実行、現在のステータス情報取得
			dpsInsWsStatusCheckService.execute(sosCd, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3262E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 配分基準更新
		// ----------------------

		// UH
		DistParamOffice distParamOfficeUH = distributionParamUpdateDto.getDistParamOfficeUH();
		try {
			if (distParamOfficeUH.getSeqKey() != null) {
				distParamOfficeDao.update(distParamOfficeUH);
			} else {
				distParamOfficeDao.insert(distParamOfficeUH);
			}
		} catch (DuplicateException e) {
			final String errMsg = "配分基準UH(営業所案)登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}

		// P
		DistParamOffice distParamOfficeP = distributionParamUpdateDto.getDistParamOfficeP();
		try {
			if (distParamOfficeP.getSeqKey() != null) {
				distParamOfficeDao.update(distParamOfficeP);
			} else {
				distParamOfficeDao.insert(distParamOfficeP);
			}
		} catch (DuplicateException e) {
			final String errMsg = "配分基準P(営業所案)登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	// 配分基準 営業所案削除
	public void deleteDistParamOffice(DistParamUpdateDto distributionParamUpdateDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (distributionParamUpdateDto == null) {
			final String errMsg = "配分基準更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeUH() == null) {
			final String errMsg = "配分基準　営業所案UHがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeP() == null) {
			final String errMsg = "配分基準　営業所案Pがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeUH().getSosCd() == null) {
			final String errMsg = "配分基準　営業所案UH組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeUH().getProdCode() == null) {
			final String errMsg = "配分基準　営業所案UH品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeP().getSosCd() == null) {
			final String errMsg = "配分基準P　営業所案P組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionParamUpdateDto.getDistParamOfficeP().getProdCode() == null) {
			final String errMsg = "配分基準P　営業所案P品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String sosCd = distributionParamUpdateDto.getDistParamOfficeUH().getSosCd();
		String prodCode = distributionParamUpdateDto.getDistParamOfficeUH().getProdCode();
		String prodName = distributionParamUpdateDto.getProdName();

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 施設特約店別計画ステータスチェック(配分中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);

			// チェック対象の品目情報作成
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(prodCode);
			plannedProd.setProdName(prodName);
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(plannedProd);

			// チェック実行、現在のステータス情報取得
			dpsInsWsStatusCheckService.execute(sosCd, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3262E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 営業所案削除
		// ----------------------

		// UH
		DistParamOffice distParamOfficeUH = distributionParamUpdateDto.getDistParamOfficeUH();
		distParamOfficeDao.delete(distParamOfficeUH.getSeqKey(), distParamOfficeUH.getUpDate());

		// P
		DistParamOffice distParamOfficeP = distributionParamUpdateDto.getDistParamOfficeP();
		distParamOfficeDao.delete(distParamOfficeP.getSeqKey(), distParamOfficeP.getUpDate());
	}
}
