package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.dto.InsWsPlanDto;

/**
 * 施設特約店別計画の配分値降順比較を行うロジッククラス
 * 
 * @author khashimoto
 */
public class InsWsPlanDistValueComparator implements Comparator<InsWsPlanDto> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static InsWsPlanDistValueComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static InsWsPlanDistValueComparator getInstance() {
		if (_comparator == null) {
			_comparator = new InsWsPlanDistValueComparator();
		}
		return _comparator;
	}

	public int compare(InsWsPlanDto o1, InsWsPlanDto o2) {
		return -o1.getDistValue().compareTo(o2.getDistValue());
	}
}
