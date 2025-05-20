package jp.co.takeda.logic;

import jp.co.takeda.model.view.MonNnuSummary;
import jp.co.takeda.util.MathUtil;

/**
 * 納入実績の加算を表すロジッククラス
 * 
 * @author khashimoto
 */
public class AddMonNnuSummaryLogic {

	/**
	 * 納入実績1
	 */
	private final MonNnuSummary summary1;

	/**
	 * 納入実績2
	 */
	private final MonNnuSummary summary2;

	/**
	 * コンストラクタ
	 * 
	 * @param summary1 納入実績1
	 * @param summary2 納入実績2
	 */
	public AddMonNnuSummaryLogic(final MonNnuSummary summary1, final MonNnuSummary summary2) {
		this.summary1 = summary1;
		this.summary2 = summary2;
	}

	/**
	 * 加算処理実行する。
	 * 
	 * @return 納入実績
	 */
	public MonNnuSummary execute() {
		MonNnuSummary summary = new MonNnuSummary();
		summary.setPreFarAdvancePeriod(MathUtil.add(summary1.getPreFarAdvancePeriod(), summary2.getPreFarAdvancePeriod()));
		summary.setFarAdvancePeriod(MathUtil.add(summary1.getFarAdvancePeriod(), summary2.getFarAdvancePeriod()));
		summary.setAdvancePeriod(MathUtil.add(summary1.getAdvancePeriod(), summary2.getAdvancePeriod()));
		summary.setCurrentPlanValue(MathUtil.add(summary1.getCurrentPlanValue(), summary2.getCurrentPlanValue()));
		summary.setCurrentPeriod(MathUtil.add(summary1.getCurrentPeriod(), summary2.getCurrentPeriod()));
		return summary;
	}
}
