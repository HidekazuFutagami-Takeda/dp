package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.InsWsPlanForVac;

/**
 * 施設特約店別計画の計画値降順比較を行うロジッククラス
 * 
 * @author khashimoto
 */
public class InsWsPlanForVacValueComparator implements Comparator<InsWsPlanForVac> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static InsWsPlanForVacValueComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static InsWsPlanForVacValueComparator getInstance() {
		if (_comparator == null) {
			_comparator = new InsWsPlanForVacValueComparator();
		}
		return _comparator;
	}

	public int compare(InsWsPlanForVac o1, InsWsPlanForVac o2) {
		return -o1.getPlannedValueB().compareTo(o2.getPlannedValueB());
	}
}
