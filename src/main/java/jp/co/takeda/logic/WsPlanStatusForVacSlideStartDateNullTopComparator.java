package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.WsPlanStatusForVac;

/**
 * ワクチン用特約店別計画立案ステータスのスライド開始日比較
 * 
 * @author yokokawa
 */
public class WsPlanStatusForVacSlideStartDateNullTopComparator implements Comparator<WsPlanStatusForVac> {
	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static WsPlanStatusForVacSlideStartDateNullTopComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static WsPlanStatusForVacSlideStartDateNullTopComparator getInstance() {
		if (_comparator == null) {
			_comparator = new WsPlanStatusForVacSlideStartDateNullTopComparator();
		}
		return _comparator;
	}

	public int compare(WsPlanStatusForVac o1, WsPlanStatusForVac o2) {
		// NULLデータを最後にソート
		if (o1.getSlideStartDate() == null) {
			if (o2.getSlideStartDate() == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (o2.getSlideStartDate() == null) {
				return 1;
			}
		}

		return o1.getSlideStartDate().compareTo(o2.getSlideStartDate());
	}
}
