package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.WsPlan;

/**
 * 特約店別計画の配分値(UH)比較
 * 
 * @author yokokawa
 */
public class WsPlanDistValueUhComparator implements Comparator<WsPlan> {
	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static WsPlanDistValueUhComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static WsPlanDistValueUhComparator getInstance() {
		if (_comparator == null) {
			_comparator = new WsPlanDistValueUhComparator();
		}
		return _comparator;
	}

	public int compare(WsPlan o1, WsPlan o2) {
		return o1.getDistValueUh().compareTo(o2.getDistValueUh());
	}
}
