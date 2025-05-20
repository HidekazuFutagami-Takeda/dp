package jp.co.takeda.logic;

import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.util.ConvertUtil;

/**
 * 納入実績積み上げ集計ロジッククラス
 * 
 * @author nozaki
 */
public class DeliveryResultSummaryLogic {

	/**
	 * 前々々期実績
	 */
	private Long preFarAdvancePeriod;

	/**
	 * 前々期実績
	 */
	private Long farAdvancePeriod;

	/**
	 * 前期実績
	 */
	private Long advancePeriod;

	/**
	 * 当期実績
	 */
	private Long currentPeriod;

	/**
	 * 当期計画値
	 */
	private Long currentPlanValue;

	/**
	 * コンストラクタ
	 * 
	 * @param 品目情報
	 */
	public DeliveryResultSummaryLogic() {
		preFarAdvancePeriod = 0L;
		farAdvancePeriod = 0L;
		advancePeriod = 0L;
		currentPeriod = 0L;
		currentPlanValue = 0L;
	}

	/**
	 * 納入実績積み上げを作成する。
	 * 
	 * @return 納入実績積み上げのデータ
	 */
	public DeliveryResultMr execute() {

		// 納入実績積み上げ作成
		MonNnu monNnu = new MonNnu();
		monNnu.setPreFarAdvancePeriod(preFarAdvancePeriod);
		monNnu.setFarAdvancePeriod(farAdvancePeriod);
		monNnu.setAdvancePeriod(advancePeriod);
		monNnu.setCurrentPeriod(currentPeriod);
		monNnu.setCurrentPlanValue(currentPlanValue);

		DeliveryResultMr deliveryResultMr = new DeliveryResultMr();
		deliveryResultMr.setMonNnu(monNnu);

		return deliveryResultMr;
	}

	/**
	 * 納入実績値を加算する。
	 * 
	 * @param targetDeliveryResultMr 納入実績
	 */
	public void addDeliveryResultMr(DeliveryResultMr targetDeliveryResultMr) {
		if (targetDeliveryResultMr == null) {
			return;
		}
		MonNnu monNnu = targetDeliveryResultMr.getMonNnu();
		if (monNnu == null) {
			return;
		}

		preFarAdvancePeriod = preFarAdvancePeriod + ConvertUtil.parseLong(monNnu.getPreFarAdvancePeriod(), 0L);
		farAdvancePeriod = farAdvancePeriod + ConvertUtil.parseLong(monNnu.getFarAdvancePeriod(), 0L);
		advancePeriod = advancePeriod + ConvertUtil.parseLong(monNnu.getAdvancePeriod(), 0L);
		currentPeriod = currentPeriod + ConvertUtil.parseLong(monNnu.getCurrentPeriod(), 0L);
		currentPlanValue = currentPlanValue + ConvertUtil.parseLong(monNnu.getCurrentPlanValue(), 0L);
	}
}
