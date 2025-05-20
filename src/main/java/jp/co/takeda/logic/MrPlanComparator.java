package jp.co.takeda.logic;

import java.util.Comparator;

import jp.co.takeda.model.MrPlan;

/**
 * 担当者別計画の比較を行うロジッククラス
 * 
 * @author nozaki
 */
public class MrPlanComparator implements Comparator<MrPlan> {

	/**
	 * ソート用コンパレータクラスインスタンス
	 */
	private static MrPlanComparator _comparator = null;

	/**
	 * ソート用コンパレータクラスの生成.
	 * 
	 * @return ソート用コンパレータクラス
	 */
	public static MrPlanComparator getInstance() {
		if (_comparator == null) {
			_comparator = new MrPlanComparator();
		}
		return _comparator;
	}

	public int compare(MrPlan o1, MrPlan o2) {
		return o1.compareTo(o2);
	}
}
