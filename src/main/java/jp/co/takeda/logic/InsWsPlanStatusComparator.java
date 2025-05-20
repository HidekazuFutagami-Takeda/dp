package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.InsWsPlanStatus;

/**
 * 施設特約店別計画立案ステータスの比較を行うロジッククラス
 * 
 * @author nozaki
 */
public class InsWsPlanStatusComparator implements Comparator<InsWsPlanStatus> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static InsWsPlanStatusComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static InsWsPlanStatusComparator getInstance() {
		if (_comparator == null) {
			_comparator = new InsWsPlanStatusComparator();
		}
		return _comparator;
	}

	public int compare(InsWsPlanStatus o1, InsWsPlanStatus o2) {
		return o1.compareTo(o2);
	}
}
