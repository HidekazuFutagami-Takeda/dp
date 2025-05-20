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
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.DocDistributionJgiDto;
import jp.co.takeda.dto.DocDistributionParamsDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsDocStatusForCheck;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.security.DpUser;

/**
 * (医)医師別計画配分機能の実行処理に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsDocDistributionProdService")
public class DpsDocDistributionProdServiceImpl implements DpsDocDistributionProdService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsDocDistributionProdServiceImpl.class);

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 施設医師別計画立案ステータスDAO
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
	 * 施設医師別計画立案ステータスチェックサービス
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

	// 配分処理実行 前処理
	public void distributionPreparation(DocDistributionParamsDto dto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (dto == null) {
			final String errMsg = "配分パラメータDTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dto.getSosCd3() == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dto.getAppServerKbn() == null) {
			final String errMsg = "サーバ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dto.getJgiList() != null) {
			for (DocDistributionJgiDto jgiDto : dto.getJgiList()) {
				if (jgiDto.getJgiMst() == null) {
					final String errMsg = "従業員情報がnull";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
				}
			}
		}

		String sosCd = dto.getSosCd3();
		String appServerKbn = dto.getAppServerKbn();
		Date statusLastUpdate = dto.getStatusLastUpdates();
		DpUser dpUser = dto.getDpUser();
		boolean isMainHaibun = dto.isMainHaibun();

		// --------------------------------------------
		// ステータスチェック
		// --------------------------------------------
		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 組織からカテゴリ判定（MMP or ONC）
		// TODO 本来このクラス自体廃止すべきだが、エラーを抑止するために以下のような対応をしている
		//ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
		ProdCategory category = ProdCategory.MMP;
		// TODO ↑

		// チェック対象の品目情報作成
		List<PlannedProd> plannedProdList = null;
		try {
			// TODO 本来このクラス自体廃止すべきだが、エラーを抑止するために以下のような対応をしている
			//plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU,category, true);
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU,"01", true);
			// TODO ↑

		} catch (DataNotFoundException e) {
			final String errMsg = "配分対象品目がない（重点品）";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 担当者別計画ステータスチェック(確定していないとだめ)
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

		// 従業員リスト作成
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		for (DocDistributionJgiDto jgiDto : dto.getJgiList()) {
			jgiMstList.add(jgiDto.getJgiMst());
		}

		// 施設医師別計画ステータスチェック
		String msgCode = "";
		try {

			if(isMainHaibun){

				// -------------------
				// 営業所指定の場合
				// -------------------
				// 許可しないステータスリスト作成(配分中はだめ)
				List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
				unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
				msgCode = "DPS3346E";

				// ステータスチェック
				dpsInsDocStatusCheckService.execute(sosCd, plannedProdList, unallowableInsDocStatusList);

			} else {

				// -------------------
				// 担当者指定の場合
				// -------------------
				// 許可しないステータスリスト作成(確定以外はだめ)
				List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
				unallowableInsDocStatusList.add(InsDocStatusForCheck.NOTHING);
				unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
				msgCode = "DPS3344E";

				// ステータスチェック
				dpsInsDocStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsDocStatusList);
			}

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(msgCode));
//			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// -------------------------------------
		// 施設特約店別計画ステータス
		// -------------------------------------
		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// ステータスチェック
			dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3349E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}


		// ----------------------
		// ステータス更新
		// ----------------------

		if(isMainHaibun){

			// -------------------
			// 営業所指定の場合
			// -------------------
			// 配下のフラグはすべて同じはず
			boolean doMrOpen = dto.getJgiList().get(0).getDoMrOpen();
			boolean doPlanClear = dto.getJgiList().get(0).getDoPlanClear();

			// 最終更新日が変わっている場合は、排他エラーがスローされる
			insDocPlanStatusDao.checkLastUpDate(sosCd, null, null, null, statusLastUpdate);

			// 配分中に更新
			Date distStartDate = commonDao.getSystemTime();
			int result = insDocPlanStatusDao.haibunStart(sosCd, null, category, appServerKbn, distStartDate, doPlanClear, doMrOpen, dpUser);
			if(result == 0) {
				final String errMsg = "施設医師別計画ステータスの登録または更新に失敗（対象件数0件）";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
			}

		} else {

			// -------------------
			// 担当者指定の場合
			// -------------------
			for (DocDistributionJgiDto jgiDto : dto.getJgiList()) {

				Integer jgiNo = jgiDto.getJgiMst().getJgiNo();
				boolean doMrOpen = jgiDto.getDoMrOpen();
				boolean doPlanClear = jgiDto.getDoPlanClear();

				// 最終更新日が変わっている場合は、排他エラーがスローされる
				insDocPlanStatusDao.checkLastUpDate(sosCd, null, jgiNo, null, statusLastUpdate);

				// 配分中に更新
				Date distStartDate = commonDao.getSystemTime();
				int result = insDocPlanStatusDao.haibunStart(sosCd, jgiNo, category, appServerKbn, distStartDate, doPlanClear, doMrOpen, dpUser);
				if(result == 0) {
					final String errMsg = "施設医師別計画ステータスの登録または更新に失敗（対象件数0件）";
					throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
				}
			}
		}
	}
}
