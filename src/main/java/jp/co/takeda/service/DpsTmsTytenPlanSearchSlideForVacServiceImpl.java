package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.WsPlanStatusForVacDao;
import jp.co.takeda.dto.TmsTytenPlanSlideForVacResultDto;
import jp.co.takeda.logic.WsPlanStatusForVacSlideEndDateNullTopComparator;
import jp.co.takeda.logic.WsPlanStatusForVacSlideStartDateNullBottomComparator;
import jp.co.takeda.logic.WsPlanStatusForVacSlideStartDateNullTopComparator;
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.StatusForWsSlide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ワクチン用特約店別計画スライドサービス実装クラス
 * 
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenPlanSearchSlideForVacService")
public class DpsTmsTytenPlanSearchSlideForVacServiceImpl implements DpsTmsTytenPlanSearchSlideForVacService {
	/**
	 * ワクチン用特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusForVacDao")
	protected WsPlanStatusForVacDao wsPlanStatusForVacDao;

	// 特約店別計画スライド状態を検索する。
	public TmsTytenPlanSlideForVacResultDto searchSlideStatus() {
		// ----------------------
		// ワクチン用特約店別計画を取得
		// ----------------------
		List<WsPlanStatusForVac> wsPlanStatusForVacList = null;
		try {
			wsPlanStatusForVacList = wsPlanStatusForVacDao.searchList();

		} catch (DataNotFoundException e) {
			// ワクチン用特約店別計画が存在しない場合(外部結合で取得しているので取れるはず)
			final String errMsg = "ワクチン用特約店別計画が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ----------------------
		// スライドステータスチェック
		// ----------------------
		int slideCount = 0;
		int slidingCount = 0;
		for (WsPlanStatusForVac wsPlanStatusForVac : wsPlanStatusForVacList) {
			// スライド済みとスライド中のカウントを行う
			if (StatusForWsSlide.SLIDED.equals(wsPlanStatusForVac.getStatusSlideForWs())) {
				// スライド済みの場合、スライド済みカウントをインクリメント
				slideCount++;

			} else if (StatusForWsSlide.SLIDING.equals(wsPlanStatusForVac.getStatusSlideForWs())) {
				// スライド中の場合、スライド中カウントをインクリメント
				slidingCount++;
			}
		}

		StatusForWsSlide statusSlideForWs = null;
		if (slideCount == wsPlanStatusForVacList.size()) {
			// すべてスライド済みの場合、スライド済みを設定
			statusSlideForWs = StatusForWsSlide.SLIDED;

		} else if ((slideCount + slidingCount) > 0) {
			// スライド中、スライド済みが１件以上ある場合、スライド中を設定
			statusSlideForWs = StatusForWsSlide.SLIDING;

		} else {
			// すべてスライド前の場合、スライド前を設定
			statusSlideForWs = StatusForWsSlide.NONE;
		}

		// ----------------------
		// スライド開始・終了日取得
		// ----------------------
		Date slideStartDate = null;
		Date slideEndDate = null;
		String upJgiName = null;
		Date upDate = null;
		if (wsPlanStatusForVacList.size() > 0) {
			// 一番古いスライド開始日を取得
			Comparator<WsPlanStatusForVac> planStatusForVacSlideStartDateNullBottomComparator = WsPlanStatusForVacSlideStartDateNullBottomComparator.getInstance();
			Collections.sort(wsPlanStatusForVacList, planStatusForVacSlideStartDateNullBottomComparator);
			slideStartDate = wsPlanStatusForVacList.get(0).getSlideStartDate();

			// 一番新しいスライド開始日の更新者を取得
			Comparator<WsPlanStatusForVac> planStatusForVacSlideStartDateNullTopComparator = WsPlanStatusForVacSlideStartDateNullTopComparator.getInstance();
			Collections.sort(wsPlanStatusForVacList, planStatusForVacSlideStartDateNullTopComparator);
			Collections.reverse(wsPlanStatusForVacList);

			if (wsPlanStatusForVacList.get(0).getSlideStartDate() != null) {
				upJgiName = wsPlanStatusForVacList.get(0).getUpJgiName();
				upDate = wsPlanStatusForVacList.get(0).getSlideStartDate();
			}

			// 一番新しいスライド終了日を取得
			Comparator<WsPlanStatusForVac> planStatusForVacSlideEndDateNullTopComparator = WsPlanStatusForVacSlideEndDateNullTopComparator.getInstance();
			Collections.sort(wsPlanStatusForVacList, planStatusForVacSlideEndDateNullTopComparator);
			Collections.reverse(wsPlanStatusForVacList);
			slideEndDate = wsPlanStatusForVacList.get(0).getSlideEndDate();
		}

		return new TmsTytenPlanSlideForVacResultDto(statusSlideForWs, slideStartDate, slideEndDate, upJgiName, upDate);
	}
}
