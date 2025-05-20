package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.AddMonNnuSummaryLogic;
import jp.co.takeda.model.view.InsWsPlanForRef;
import jp.co.takeda.model.view.InsWsPlanForVacForRef;
import jp.co.takeda.model.view.MonNnuSummary;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画のサマリー行を表すDTO
 *
 * @author khashimoto
 */
public class InsWsSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 配分値
	 */
	private final Long distSum;

	/**
	 * 修正値
	 */
	private final Long modifySum;

	/**
	 * 確定値
	 */
	private final Long planSum;

	/**
	 * 立案品目の納入実績
	 */
	private final MonNnuSummary monNnuSummary;

	/**
	 * 参照品目1の納入実績
	 */
	private final MonNnuSummary monNnuSummary1;

	/**
	 * 参照品目2の納入実績
	 */
	private final MonNnuSummary monNnuSummary2;

	/**
	 * 参照品目3の納入実績
	 */
	private final MonNnuSummary monNnuSummary3;

	/**
	 * コンストラクタ
	 */
	public InsWsSummaryDto() {
		this.distSum = 0L;
		this.modifySum = 0L;
		this.planSum = 0L;
		this.monNnuSummary = new MonNnuSummary(0L);
		this.monNnuSummary1 = new MonNnuSummary(0L);
		this.monNnuSummary2 = new MonNnuSummary(0L);
		this.monNnuSummary3 = new MonNnuSummary(0L);
	}

	/**
	 * コンストラクタ
	 *
	 * @param left サマリー行を表すDTO
	 * @param right 参照用の施設特約店別計画
	 */
	public InsWsSummaryDto(InsWsSummaryDto left, InsWsPlanForRef right) {
		this.distSum = MathUtil.add(left.getDistSum(), right.getDistValueY());
		this.modifySum = MathUtil.add(left.getModifySum(), right.getModifyValueY());
		this.planSum = MathUtil.add(left.getPlanSum(), right.getPlannedValueY());
		this.monNnuSummary = new AddMonNnuSummaryLogic(left.getMonNnuSummary(), right.getMonNnuSummary()).execute();
		this.monNnuSummary1 = new AddMonNnuSummaryLogic(left.getMonNnuSummary1(), right.getMonNnuSummary1()).execute();
		this.monNnuSummary2 = new AddMonNnuSummaryLogic(left.getMonNnuSummary2(), right.getMonNnuSummary2()).execute();
		this.monNnuSummary3 = new AddMonNnuSummaryLogic(left.getMonNnuSummary3(), right.getMonNnuSummary3()).execute();
	}

	/**
	 * コンストラクタ
	 *
	 * @param left サマリー行を表すDTO
	 * @param right 参照用のワクチン用施設特約店別計画
	 */
	public InsWsSummaryDto(InsWsSummaryDto left, InsWsPlanForVacForRef right) {
		this.distSum = MathUtil.add(left.getDistSum(), right.getDistValueB());
		// mod Start 2023/8/08 H.Futagami 不具合対応No.04　施設特約店別計画：参照、更新処理修正
		//this.modifySum = null;
		if (right.getModifyValueB() == null) {
			this.modifySum = null;
		}else {
			this.modifySum = MathUtil.add(left.getModifySum(), right.getModifyValueB());
		}
		// mod End 2023/8/08 H.Futagami 不具合対応No.04　施設特約店別計画：参照、更新処理修正
		this.planSum = MathUtil.add(left.getPlanSum(), right.getPlannedValueB());
		this.monNnuSummary = new AddMonNnuSummaryLogic(left.getMonNnuSummary(), right.getMonNnuSummary()).execute();
		this.monNnuSummary1 = new AddMonNnuSummaryLogic(left.getMonNnuSummary1(), right.getMonNnuSummary1()).execute();
		this.monNnuSummary2 = new AddMonNnuSummaryLogic(left.getMonNnuSummary2(), right.getMonNnuSummary2()).execute();
		this.monNnuSummary3 = new AddMonNnuSummaryLogic(left.getMonNnuSummary3(), right.getMonNnuSummary3()).execute();
	}

	/**
	 * コンストラクタ
	 *
	 * @param left サマリー行を表すDTO
	 * @param right サマリー行を表すDTO
	 */
	public InsWsSummaryDto(InsWsSummaryDto left, InsWsSummaryDto right) {
		this.distSum = MathUtil.add(left.getDistSum(), right.getDistSum());
		this.planSum = MathUtil.add(left.getPlanSum(), right.getPlanSum());
		this.modifySum = MathUtil.add(left.getModifySum(), right.getModifySum());
		this.monNnuSummary = new AddMonNnuSummaryLogic(left.getMonNnuSummary(), right.getMonNnuSummary()).execute();
		this.monNnuSummary1 = new AddMonNnuSummaryLogic(left.getMonNnuSummary1(), right.getMonNnuSummary1()).execute();
		this.monNnuSummary2 = new AddMonNnuSummaryLogic(left.getMonNnuSummary2(), right.getMonNnuSummary2()).execute();
		this.monNnuSummary3 = new AddMonNnuSummaryLogic(left.getMonNnuSummary3(), right.getMonNnuSummary3()).execute();
	}

	/**
	 * 配分値を取得する。
	 *
	 * @return 配分値
	 */
	public Long getDistSum() {
		return distSum;
	}

	/**
	 * 修正値を取得する。
	 *
	 * @return 修正値
	 */
	public Long getModifySum() {
		return modifySum;
	}

	/**
	 * 確定値を取得する。
	 *
	 * @return 確定値
	 */
	public Long getPlanSum() {
		return planSum;
	}

	/**
	 * 立案品目の納入実績を取得する。
	 *
	 * @return 立案品目の納入実績
	 */
	public MonNnuSummary getMonNnuSummary() {
		return monNnuSummary;
	}

	/**
	 * 参照品目1の納入実績を取得する。
	 *
	 * @return 参照品目1の納入実績
	 */
	public MonNnuSummary getMonNnuSummary1() {
		return monNnuSummary1;
	}

	/**
	 * 参照品目2の納入実績を取得する。
	 *
	 * @return 参照品目2の納入実績
	 */
	public MonNnuSummary getMonNnuSummary2() {
		return monNnuSummary2;
	}

	/**
	 * 参照品目3の納入実績を取得する。
	 *
	 * @return 参照品目3の納入実績
	 */
	public MonNnuSummary getMonNnuSummary3() {
		return monNnuSummary3;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
