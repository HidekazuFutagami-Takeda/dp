package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dto.TmsTytenDistParamDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.StatusForWs;
import jp.co.takeda.model.div.StatusForWsSlide;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 特約店別計画配分サービス実装クラス
 *
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenDistService")
public class DpsTmsTytenDistServiceImpl implements DpsTmsTytenDistService {

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
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * DB共通情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 営業所ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsOfficeStatusCheckService")
	protected DpsOfficeStatusCheckService dpsOfficeStatusCheckService;

	/**
	 * 特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckService")
	protected DpsWsStatusCheckService dpsWsStatusCheckService;

	// 特約店別計画配分の前処理を実行
	public void distPreparation(TmsTytenDistParamDto dto) {

		// ----------------------
		// 引数チェック
		// ----------------------

		// 特約店別計画配分パラメータチェック
		if (dto == null) {
			final String errMsg = "特約店別計画配分パラメータがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 特約店別計画配分パラメータチェック
		if (dto.getSosMst2() == null) {
			final String errMsg = "支店組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 実行者従業員情報チェック
		if (dto.getDpUser() == null) {
			final String errMsg = "実行者従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 品目情報リストが空チェック
		if (dto.getPlannedProdList() == null) {
			final String errMsg = "品目情報リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (dto.getPlannedProdList().size() == 0) {
			final String errMsg = "品目情報リストが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<PlannedProd> plannedProdList = dto.getPlannedProdList();

		// 実行前ステータス情報リストが空チェック
		if (dto.getBeforeWsPlanStatusList() == null) {
			final String errMsg = "実行前ステータス情報リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (dto.getBeforeWsPlanStatusList().size() == 0) {
			final String errMsg = "実行前ステータス情報リストが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 営業所の組織一覧チェック
		if (dto.getSosMst3List() == null) {
			final String errMsg = "営業所の組織一覧パラメータがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (dto.getSosMst3List().size() == 0) {
			final String errMsg = "営業所の組織一覧パラメータが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分基準(UH)チェック
		if (dto.getDistParamOfficeUHList() == null) {
			final String errMsg = "配分基準(UH)パラメータがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (dto.getDistParamOfficeUHList().size() == 0) {
			final String errMsg = "配分基準(UH)パラメータが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分基準(P)チェック
		if (dto.getDistParamOfficePList() == null) {
			final String errMsg = "配分基準(P)パラメータがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (dto.getDistParamOfficePList().size() == 0) {
			final String errMsg = "配分基準(P)パラメータが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------------------------
		// 納入計画システム管理ステータスチェック
		// --------------------------------------------
		SysManage sysManage = null;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.IYAKU);
		} catch (DataNotFoundException e) {
			// 納入計画システム管理情報が存在しない場合
			final String errMsg = "納入計画システム管理情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// T価変換バッチ実施フラグが実行済みか判定
		if (sysManage.getTransTFlg()) {
			// T価変換バッチ実施フラグが実行済の場合 ステータスチェックエラー
			MessageKey messageKey = new MessageKey("DPS3271E");
			throw new UnallowableStatusException(new Conveyance(messageKey));
		}

		// --------------------------------------------
		// 営業所計画ステータスチェック
		// --------------------------------------------
		// 品目カテゴリの取得(品目リストの中から1件目の品目のカテゴリを取得)
//		ProdCategory prodCategory = plannedProdList.get(0).getCategory();
		String prodCategory = plannedProdList.get(0).getCategory();

		// 全営業所の営業所確定状態をチェック
		try {

			// 許可しないステータスリスト作成
			List<OfficeStatusForCheck> unallowableStatusList = new ArrayList<OfficeStatusForCheck>();
			unallowableStatusList.add(OfficeStatusForCheck.NOTHING);
			unallowableStatusList.add(OfficeStatusForCheck.DRAFT);

			// チェック実行
			dpsOfficeStatusCheckService.executeForShiten(dto.getSosMst2(), prodCategory, unallowableStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			messageKeyList.add(new MessageKey("DPS3260E", "エリア計画"));
//			messageKeyList.add(new MessageKey("DPS3260E", "営業所計画"));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// --------------------------------------------
		// 特約店別計画立案ステータスチェック
		// --------------------------------------------
		List<WsPlanStatus> wsPlanStatusList = null;
		try {
			// 許可しない配分ステータスリスト作成
			List<WsDistStatusForCheck> unallowableDistStatusList = new ArrayList<WsDistStatusForCheck>();
			unallowableDistStatusList.add(WsDistStatusForCheck.DISTING);

			// 許可しないスライド状況リスト作成
			List<WsSlideStatusForCheck> unallowableSlideStatusList = new ArrayList<WsSlideStatusForCheck>();
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDING);
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDED);

			// ステータスチェック実行
			SosMst sosMst2 = dto.getSosMst2();
			wsPlanStatusList = dpsWsStatusCheckService.execute(sosMst2, plannedProdList, unallowableDistStatusList, unallowableSlideStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスチェックエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3270E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			final String errMsg = "特約店別計画配分ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(messageKeyList, errMsg), e);
		}

		// --------------------------------------------
		// 特約店別計画立案ステータス更新
		// --------------------------------------------
		Integer jgiNo = dto.getDpUser().getJgiNo();
		String jgiName = dto.getDpUser().getJgiName();
		String serverKbn = dto.getAppServerKbn();
		for (WsPlanStatus wsPlanStatus : wsPlanStatusList) {
			Date systemTime = commonDao.getSystemTime();

			try {
				// 特約店別計画立案ステータスが存在するか判定
				WsPlanStatus newWsPlanStatus = null;
				if (wsPlanStatus.getSeqKey() != null) {
					// 存在する場合、更新

					newWsPlanStatus = wsPlanStatus;
					newWsPlanStatus.setAppServerKbn(serverKbn);
					newWsPlanStatus.setAsyncBefStatus(wsPlanStatus.getStatusDistForWs());
					newWsPlanStatus.setAsyncBefDistStartDate(wsPlanStatus.getDistStartDate());
					newWsPlanStatus.setStatusDistForWs(StatusForWs.DISTING);
					newWsPlanStatus.setDistStartDate(systemTime);
					newWsPlanStatus.setUpJgiNo(jgiNo);
					newWsPlanStatus.setUpJgiName(jgiName);
					wsPlanStatusDao.update(newWsPlanStatus);

				} else {
					// 存在しない場合、登録
					newWsPlanStatus = wsPlanStatus;
					newWsPlanStatus.setAppServerKbn(serverKbn);
					newWsPlanStatus.setAsyncBefStatus(null);
					newWsPlanStatus.setAsyncBefDistStartDate(null);
					newWsPlanStatus.setStatusSlideForWs(StatusForWsSlide.NONE);
					newWsPlanStatus.setIsJgiNo(jgiNo);
					newWsPlanStatus.setIsJgiName(jgiName);
					newWsPlanStatus.setStatusDistForWs(StatusForWs.DISTING);
					newWsPlanStatus.setDistStartDate(systemTime);
					newWsPlanStatus.setUpJgiNo(jgiNo);
					newWsPlanStatus.setUpJgiName(jgiName);
					wsPlanStatusDao.insert(newWsPlanStatus);
				}
			} catch (DuplicateException e) {
				// 一意制約違反が発生した場合
				final String errMsg = "特約店別計画立案ステータスの登録時エラー発生";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
			}
		}
	}
}
