package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.DB_DUPLICATE_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.WsPlanDao;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.StatusForWs;
import jp.co.takeda.model.div.StatusForWsSlide;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * (医)特約店別計画スライド処理サービス実装クラス
 * 
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenSlideExecuteService")
public class DpsTmsTytenSlideExecuteServiceImpl implements DpsTmsTytenSlideExecuteService {

	/**
	 * 特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusDao")
	protected WsPlanStatusDao wsPlanStatusDao;

	/**
	 * 特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanDao")
	protected WsPlanDao wsPlanDao;

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

	// 特約店別計画スライド処理を実行
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeSlide(List<String> sosCd3List, List<String> prodCodeList, List<WsPlanStatus> wsPlanStatusList, Date startTime) {

		// --------------------------------------------
		// 引数チェック
		// --------------------------------------------
		// 組織コード(営業所)チェック
		if (sosCd3List == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (sosCd3List.size() == 0) {
			final String errMsg = "組織コード(営業所)が0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 品目固定コードチェック
		if (prodCodeList == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (prodCodeList.size() == 0) {
			final String errMsg = "品目固定コードが0件";
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
			sysManageDb = sysManageDao.search(SysClass.DPS, SysType.IYAKU);

		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		if (!sysManageDb.getWsEndFlg()) {
			// ステータスチェックエラー
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3291E")));
		}

		// ----------------------
		// スライド実行
		// ----------------------
		wsPlanDao.updateCopyStackToPlanned(sosCd3List, prodCodeList);

		// ----------------------
		// 特約店別計画立案ステータス更新
		// ----------------------
		Date endTime = commonDao.getSystemTime();
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
	}
}
