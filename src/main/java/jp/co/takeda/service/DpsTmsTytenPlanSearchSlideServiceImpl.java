package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dto.TmsTytenPlanSlideResultDto;
import jp.co.takeda.logic.WsPlanStatusSlideEndDateNullTopComparator;
import jp.co.takeda.logic.WsPlanStatusSlideStartDateNullBottomComparator;
import jp.co.takeda.logic.WsPlanStatusSlideStartDateNullTopComparator;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.StatusForWsSlide;

/**
 * 特約店別計画スライド検索サービス実装クラス
 *
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenPlanSearchSlideService")
public class DpsTmsTytenPlanSearchSlideServiceImpl implements DpsTmsTytenPlanSearchSlideService {

	/**
	 * 特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusDao")
	protected WsPlanStatusDao wsPlanStatusDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	// 特約店別計画スライド状態を検索
	public TmsTytenPlanSlideResultDto searchSlideStatus(String sosCd2, String category) throws LogicalException {

		// -------------------------------------
		// 引数チェック
		// -------------------------------------
		// 組織コード(支店)のチェック
		if (sosCd2 == null) {
			final String errMsg = "組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// カテゴリーのチェック
		if (category == null) {
			final String errMsg = "カテゴリーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//		ProdCategory prodCategory = ProdCategory.getInstance(category);
// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//		String prodCategory = ProdCategory.getInstance(category);

		// ----------------------
		// 支店組織
		// ----------------------
		SosMst branchSos ;
		try {
			branchSos = sosMstDAO.search(sosCd2);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd2;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

// mod start 2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//		// ONC組織の場合はエラー
//		if(BooleanUtils.isTrue(branchSos.getOncSosFlg())){
		// MMP組織以外はエラー　※特約店別計画のスライドは、全カテゴリをMMP組織で行う
//		if(branchSos.getSosCategory() != ProdCategory.MMP){
// mod end   2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//			throw new LogicalException(new Conveyance(new MessageKey("DPS3292E")));
//		}

		// -------------------------------------
		// 特約店別計画を取得
		// -------------------------------------
		List<WsPlanStatus> wsPlanStatusList = null;
		try {
			wsPlanStatusList = wsPlanStatusDao.searchListBySosCategory(WsPlanStatusDao.SORT_STRING, Sales.IYAKU, null, category);
		} catch (DataNotFoundException e) {
			// 特約店別計画が存在しない場合(外部結合で取得しているので取れるはず)
			final String errMsg = "特約店別計画が存在しない";
// mod start 2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援） ※2018上期SPBUカテゴリ品0件対応。システムエラー画面ではなく、データが見つかりませんエラーに変更。
//			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
// mod end   2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援） ※2018上期SPBUカテゴリ品0件対応。システムエラー画面ではなく、データが見つかりませんエラーに変更。
		}

		// -------------------------------------
		// スライドステータスチェック
		// -------------------------------------
		int slideCount = 0;
		int slidingCount = 0;
		for (WsPlanStatus wsPlanStatus : wsPlanStatusList) {
			// スライド済みとスライド中のカウントを行う
			if (StatusForWsSlide.SLIDED.equals(wsPlanStatus.getStatusSlideForWs())) {
				// スライド済みの場合、スライド済みカウントをインクリメント
				slideCount++;

			} else if (StatusForWsSlide.SLIDING.equals(wsPlanStatus.getStatusSlideForWs())) {
				// スライド中の場合、スライド中カウントをインクリメント
				slidingCount++;
			}
		}

		StatusForWsSlide statusSlideForWs = null;
		if (slideCount == wsPlanStatusList.size()) {
			// すべてスライド済みの場合、スライド済みを設定
			statusSlideForWs = StatusForWsSlide.SLIDED;

		} else if ((slideCount + slidingCount) > 0) {
			// スライド中、スライド済みが１件以上ある場合、スライド中を設定
			statusSlideForWs = StatusForWsSlide.SLIDING;

		} else {
			// すべてスライド前の場合、スライド前を設定
			statusSlideForWs = StatusForWsSlide.NONE;
		}

		// -------------------------------------
		// スライド開始・終了日取得
		// -------------------------------------
		Date slideStartDate = null;
		Date slideEndDate = null;
		String upJgiName = null;
		Date upDate = null;
		if (wsPlanStatusList.size() > 0) {
			// 一番古いスライド開始日を取得
			Comparator<WsPlanStatus> planStatusSlideStartDateNullBottomComparator = WsPlanStatusSlideStartDateNullBottomComparator.getInstance();
			Collections.sort(wsPlanStatusList, planStatusSlideStartDateNullBottomComparator);
			slideStartDate = wsPlanStatusList.get(0).getSlideStartDate();

			// 一番新しいスライド開始日の更新者を取得
			Comparator<WsPlanStatus> planStatusSlideStartDateNullTopComparator = WsPlanStatusSlideStartDateNullTopComparator.getInstance();
			Collections.sort(wsPlanStatusList, planStatusSlideStartDateNullTopComparator);
			Collections.reverse(wsPlanStatusList);

			if (wsPlanStatusList.get(0).getSlideStartDate() != null) {
				upJgiName = wsPlanStatusList.get(0).getUpJgiName();
				upDate = wsPlanStatusList.get(0).getSlideStartDate();
			}

			// 一番新しいスライド終了日を取得
			Comparator<WsPlanStatus> planStatusSlideEndDateNullTopComparator = WsPlanStatusSlideEndDateNullTopComparator.getInstance();
			Collections.sort(wsPlanStatusList, planStatusSlideEndDateNullTopComparator);
			Collections.reverse(wsPlanStatusList);
			slideEndDate = wsPlanStatusList.get(0).getSlideEndDate();
		}

		return new TmsTytenPlanSlideResultDto(statusSlideForWs, slideStartDate, slideEndDate, upJgiName, upDate);
	}
}
