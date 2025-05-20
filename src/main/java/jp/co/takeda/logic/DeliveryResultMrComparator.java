package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.DeliveryResultMr;

/**
 * 担当者別納入実績の比較を行うロジッククラス
 * 
 * @author nozaki
 */
public class DeliveryResultMrComparator implements Comparator<DeliveryResultMr> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static DeliveryResultMrComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static DeliveryResultMrComparator getInstance() {
		if (_comparator == null) {
			_comparator = new DeliveryResultMrComparator();
		}
		return _comparator;
	}

	public int compare(DeliveryResultMr o1, DeliveryResultMr o2) {
		return o1.compareTo(o2);
	}
}
