package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.InsDocPlanStatus;

/**
 * 施設医師別計画立案ステータスの比較を行うロジッククラス
 * 
 * @author nozaki
 */
public class InsDocPlanStatusComparator implements Comparator<InsDocPlanStatus> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static InsDocPlanStatusComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static InsDocPlanStatusComparator getInstance() {
		if (_comparator == null) {
			_comparator = new InsDocPlanStatusComparator();
		}
		return _comparator;
	}

	public int compare(InsDocPlanStatus o1, InsDocPlanStatus o2) {
		return o1.compareTo(o2);
	}
}
