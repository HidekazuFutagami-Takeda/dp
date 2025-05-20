package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.WsPlanStatus;

/**
 * 特約店別計画立案ステータスのスライド終了日比較
 * 
 * @author yokokawa
 */
public class WsPlanStatusSlideEndDateNullTopComparator implements Comparator<WsPlanStatus> {
	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static WsPlanStatusSlideEndDateNullTopComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static WsPlanStatusSlideEndDateNullTopComparator getInstance() {
		if (_comparator == null) {
			_comparator = new WsPlanStatusSlideEndDateNullTopComparator();
		}
		return _comparator;
	}

	public int compare(WsPlanStatus o1, WsPlanStatus o2) {
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
