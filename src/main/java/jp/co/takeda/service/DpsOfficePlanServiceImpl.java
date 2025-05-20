package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dto.OfficePlanUpdateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.StatusForOffice;
import jp.co.takeda.util.ConvertUtil;

/**
 * 営業所計画の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsOfficePlanService")
public class DpsOfficePlanServiceImpl implements DpsOfficePlanService {

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * 営業所計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

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
	 * 施設特約店別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	// 営業所計画を下書保存する
//	public void updateOfficePlanToDraft(String sosCd, ProdCategory category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException {
	public void updateOfficePlanToDraft(String sosCd, String category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateOfficePlanList == null || updateOfficePlanList.size() == 0) {
			final String errMsg = "営業所計画がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (OfficePlanUpdateDto officePlanUpdateDto : updateOfficePlanList) {
			if (officePlanUpdateDto.getSosCd() == null) {
				final String errMsg = "営業所計画の組織コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (officePlanUpdateDto.getProdCode() == null) {
				final String errMsg = "営業所計画の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ステータス検証のため、試算タイプを取得
		CalcType calcType = null;
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd, category);
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 担当者別計画ステータスチェック(試算タイプによって検証内容が異なる)
		try {
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();

			// 未試算、または、営→チ→担の場合
			if (calcType == null || CalcType.OFFICE_TEAM_MR.equals(calcType)) {
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
			}

			// 営→担の場合
			else {
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
			}

			dpsMrStatusCheckService.execute(sosCd, category, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3200E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という条件に変更
//		// 施設医師別計画ステータスチェック(MMP品、ONC品)
//		if(category == ProdCategory.MMP || category == ProdCategory.ONC){
		// 施設医師別計画ステータスチェック(仕入品以外)
//		if(category != ProdCategory.SHIIRE ){
//		if(!dpsCodeMasterSearchService.isSiire(category)){
//// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という条件に変更
//			try {
//				List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
//				unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
////				unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_OPENED);
////				unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_COMPLETED);
//				dpsInsDocStatusCheckService.execute(sosCd, category, unallowableInsDocStatusList);
//
//			} catch (UnallowableStatusException e) {
//
//				// メッセージ作成
//				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
//				messageKeyList.add(new MessageKey("DPS3202E"));
//				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
//				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
//			}
//		}

		// 施設特約店別計画ステータスチェック(カテゴリ、試算タイプによらず検証内容は同じ)
		try {
			List<InsWsStatusForCheck> unallowableInsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
			dpsInsWsStatusCheckService.execute(sosCd, category, unallowableInsStatusList);

		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3202E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// 更新
		doUpdate(StatusForOffice.DRAFT, sosCd, category, updateOfficePlanList);
	}

	// 営業所計画を登録する
//	public void updateOfficePlanToComplete(String sosCd, ProdCategory category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException {
	public void updateOfficePlanToComplete(String sosCd, String category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateOfficePlanList == null || updateOfficePlanList.size() == 0) {
			final String errMsg = "営業所計画がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (OfficePlanUpdateDto officePlanUpdateDto : updateOfficePlanList) {
			if (officePlanUpdateDto.getSosCd() == null) {
				final String errMsg = "営業所計画の組織コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (officePlanUpdateDto.getProdCode() == null) {
				final String errMsg = "営業所計画の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 担当者別計画ステータスチェック(試算中はだめ)
		try {
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			dpsMrStatusCheckService.execute(sosCd, category, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3201E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という条件に変更
		// 施設医師別計画ステータスチェック(配分中はだめ)
//		if(category == ProdCategory.MMP || category == ProdCategory.ONC ){
//		if(category != ProdCategory.SHIIRE ){
//		if(!dpsCodeMasterSearchService.isSiire(category)){
//// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という条件に変更
//			try {
//				List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
//				unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
//				dpsInsDocStatusCheckService.execute(sosCd, category, unallowableInsDocStatusList);
//
//			} catch (UnallowableStatusException e) {
//
//				// メッセージ作成
//				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
//				messageKeyList.add(new MessageKey("DPS3203E"));
//				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
//				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
//			}
//		}

		// 施設特約店別計画ステータスチェック(配分中はだめ)
		try {
			List<InsWsStatusForCheck> unallowableInsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsStatusList.add(InsWsStatusForCheck.DISTING);
			dpsInsWsStatusCheckService.execute(sosCd, category, unallowableInsStatusList);

		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3203E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// 更新
		doUpdate(StatusForOffice.COMPLETED, sosCd, category, updateOfficePlanList);
	}

	/**
	 * 営業所計画ステータスおよび営業所計画を更新する。
	 *
	 * @param status 更新ステータス
	 * @param sosCd 組織コード
	 * @param category カテゴリ
	 * @param updateOfficePlanList 更新対象の営業所計画
	 */
//	private void doUpdate(StatusForOffice status, String sosCd, ProdCategory category, List<OfficePlanUpdateDto> updateOfficePlanList) throws DuplicateException {
	private void doUpdate(StatusForOffice status, String sosCd, String category, List<OfficePlanUpdateDto> updateOfficePlanList) throws DuplicateException {

		// ----------------------
		// ステータス更新
		// ----------------------
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd, category);
			officePlanStatus.setStatusForOffice(status);
			officePlanStatusDao.update(officePlanStatus);

		} catch (DataNotFoundException e) {
			OfficePlanStatus officePlanStatus = new OfficePlanStatus();
			officePlanStatus.setSosCd(sosCd);
			officePlanStatus.setProdCategory(category);
			officePlanStatus.setStatusForOffice(status);
			officePlanStatusDao.insert(officePlanStatus);
		}

		// ----------------------
		// 営業所計画更新
		// ----------------------
		for (OfficePlanUpdateDto updateOfficePlan : updateOfficePlanList) {

			OfficePlan officePlan = new OfficePlan();
			officePlan.setSeqKey(updateOfficePlan.getSeqKey());
			officePlan.setSosCd(sosCd);
			officePlan.setProdCode(updateOfficePlan.getProdCode());
			officePlan.setPlannedValueUhY(ConvertUtil.parseMoneyToNormalUnit(updateOfficePlan.getPlannedValueUhY()));
			officePlan.setPlannedValuePY(ConvertUtil.parseMoneyToNormalUnit(updateOfficePlan.getPlannedValuePY()));
			officePlan.setUpDate(updateOfficePlan.getUpDate());

			if (officePlan.getSeqKey() == null) {
				officePlanDao.insert(officePlan);
			} else {
				officePlanDao.update(officePlan);
			}
		}
	}
}
