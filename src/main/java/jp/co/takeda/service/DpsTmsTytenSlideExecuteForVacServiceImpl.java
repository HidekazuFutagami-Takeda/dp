package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.WsPlanForVacDao;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dao.WsPlanStatusForVacDao;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.StatusForWs;
import jp.co.takeda.model.div.StatusForWsSlide;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * (ワ)特約店別計画スライド処理サービス実装クラス
 *
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenSlideExecuteForVacService")
public class DpsTmsTytenSlideExecuteForVacServiceImpl implements DpsTmsTytenSlideExecuteForVacService {

	/**
	 * ワクチン用特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusForVacDao")
	protected WsPlanStatusForVacDao wsPlanStatusForVacDao;

	/**
	 * ワクチン用特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanForVacDao")
	protected WsPlanForVacDao wsPlanForVacDao;

	/**
	 * DB共通情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 納入計画システム管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusDao")
	protected WsPlanStatusDao wsPlanStatusDao;

	// 特約店別計画スライド処理を実行
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeSlide(List<String> prodCodeList, List<WsPlanStatusForVac> wsPlanStatusForVacList, Date startTime, List<WsPlanStatus> wsPlanStatusList) {

		// --------------------------------------------
		// 引数チェック
		// --------------------------------------------

		// 品目固定コードチェック
		if (prodCodeList == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (prodCodeList.size() == 0) {
			final String errMsg = "品目固定コードが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ワクチン用特約店別計画立案ステータス
		if (wsPlanStatusForVacList == null) {
			final String errMsg = "ワクチン用特約店別計画立案ステータスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (wsPlanStatusForVacList.size() == 0) {
			final String errMsg = "ワクチン用特約店別計画立案ステータスが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 特約店別計画立案ステータス
		if (wsPlanStatusList == null) {
			final String errMsg = "特約店別計画立案ステータスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (wsPlanStatusList.size() == 0) {
			final String errMsg = "特約店別計画立案ステータスが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------------------------
		// 施設特約店別計画〆フラグチェック
		// --------------------------------------------
		SysManage sysManageDb = null;
		try {
			sysManageDb = sysManageDao.search(SysClass.DPS, SysType.VACCINE);

		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		if (!sysManageDb.getWsEndFlg()) {
			// ステータスチェックエラー
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3291E")));
		}

		// --------------------------------------------
		// スライド実行
		// --------------------------------------------
		wsPlanForVacDao.updateCopyStackToPlanned(prodCodeList);

		// --------------------------------------------
		// 特約店別計画立案ステータス更新
		// --------------------------------------------
		Date endTime = commonDao.getSystemTime();
		for (WsPlanStatusForVac wsPlanStatusForVac : wsPlanStatusForVacList) {
			try {
				WsPlanStatusForVac newWsPlanStatusForVac = wsPlanStatusForVac;
				newWsPlanStatusForVac.setStatusSlideForWs(StatusForWsSlide.SLIDED);
				newWsPlanStatusForVac.setSlideStartDate(startTime);
				newWsPlanStatusForVac.setSlideEndDate(endTime);

				// 特約店別計画立案ステータスが存在するか判定
				if (wsPlanStatusForVac.getSeqKey() != null) {
					// 存在する場合、更新
					wsPlanStatusForVacDao.update(newWsPlanStatusForVac);

				} else {
					// 存在しない場合、登録
					wsPlanStatusForVacDao.insert(newWsPlanStatusForVac);
				}
			} catch (DuplicateException e) {
				// 一意制約違反が発生した場合
				final String errMsg = "ワクチン用特約店別計画立案ステータスの登録時エラー発生";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
			}
		}

		// --> ADD start 2021/07/28 Top画面表示のため、DPS_I_WS_PLAN_STATUSに追加更新をする。
		// ----------------------------------
		// 特約店別計画立案ステータス更新
		// ----------------------------------
		//Top画面表示のため追加
		endTime = commonDao.getSystemTime();
		for (WsPlanStatus wsPlanStatus : wsPlanStatusList) {
			try {
				// 特約店別計画立案ステータスが存在するか判定
				WsPlanStatus newWsPlanStatus = null;
				if (wsPlanStatus.getSeqKey() != null) {
					// 存在する場合、更新
					newWsPlanStatus = wsPlanStatus;
					newWsPlanStatus.setStatusSlideForWs(StatusForWsSlide.SLIDED);
					newWsPlanStatus.setSlideStartDate(startTime);
					newWsPlanStatus.setSlideEndDate(endTime);

					wsPlanStatusDao.update(newWsPlanStatus);

				} else {
					// 存在しない場合、登録
					newWsPlanStatus = wsPlanStatus;
					newWsPlanStatus.setStatusDistForWs(StatusForWs.NONE);

					newWsPlanStatus.setStatusSlideForWs(StatusForWsSlide.SLIDED);
					newWsPlanStatus.setSlideStartDate(startTime);
					newWsPlanStatus.setSlideEndDate(endTime);

					wsPlanStatusDao.insert(newWsPlanStatus);
				}
			} catch (DuplicateException e) {
				// 一意制約違反が発生した場合
				final String errMsg = "特約店別計画立案ステータスの登録時エラー発生";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
			}
		}
		// <-- ADD end 2021/07/28
	}
}
