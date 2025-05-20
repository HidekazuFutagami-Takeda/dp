package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.WsPlanStatus;

/**
 * 特約店別計画立案ステータスの比較ロジッククラス
 * 
 * @author nozaki
 */
public class WsPlanStatusComparator implements Comparator<WsPlanStatus> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static WsPlanStatusComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static WsPlanStatusComparator getInstance() {
		if (_comparator == null) {
			_comparator = new WsPlanStatusComparator();
		}
		return _comparator;
	}

	public int compare(WsPlanStatus o1, WsPlanStatus o2) {
		return o1.compareTo(o2);
	}
}
