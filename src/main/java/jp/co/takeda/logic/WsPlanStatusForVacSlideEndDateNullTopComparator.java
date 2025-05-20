package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.WsPlanStatusForVac;

/**
 * ワクチン用特約店別計画立案ステータスのスライド終了日比較
 * 
 * @author yokokawa
 */
public class WsPlanStatusForVacSlideEndDateNullTopComparator implements Comparator<WsPlanStatusForVac> {
	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static WsPlanStatusForVacSlideEndDateNullTopComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static WsPlanStatusForVacSlideEndDateNullTopComparator getInstance() {
		if (_comparator == null) {
			_comparator = new WsPlanStatusForVacSlideEndDateNullTopComparator();
		}
		return _comparator;
	}

	public int compare(WsPlanStatusForVac o1, WsPlanStatusForVac o2) {
		// NULLデータを先頭にソート
		if (o1.getSlideEndDate() == null) {
			if (o2.getSlideEndDate() == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (o2.getSlideEndDate() == null) {
				return 1;
			}
		}

		return o1.getSlideEndDate().compareTo(o2.getSlideEndDate());
	}
}
