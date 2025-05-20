package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.WsPlan;

/**
 * 特約店別計画の配分値(P)比較
 * 
 * @author yokokawa
 */
public class WsPlanDistValuePComparator implements Comparator<WsPlan> {
	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static WsPlanDistValuePComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static WsPlanDistValuePComparator getInstance() {
		if (_comparator == null) {
			_comparator = new WsPlanDistValuePComparator();
		}
		return _comparator;
	}

	public int compare(WsPlan o1, WsPlan o2) {
		return o1.getDistValueP().compareTo(o2.getDistValueP());
	}
}
