package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.InsWsPlanStatusForVac;

/**
 * ワクチン用施設特約店別計画立案ステータスの比較を行うロジッククラス
 * 
 * @author nozaki
 */
public class InsWsPlanStatusForVacComparator implements Comparator<InsWsPlanStatusForVac> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static InsWsPlanStatusForVacComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static InsWsPlanStatusForVacComparator getInstance() {
		if (_comparator == null) {
			_comparator = new InsWsPlanStatusForVacComparator();
		}
		return _comparator;
	}

	public int compare(InsWsPlanStatusForVac o1, InsWsPlanStatusForVac o2) {
		return o1.compareTo(o2);
	}
}
