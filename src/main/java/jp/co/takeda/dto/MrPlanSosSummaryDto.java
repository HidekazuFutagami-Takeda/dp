package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlan;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画組織単位のサマリーを表すDTO
 * 
 * @author khashimoto
 */
public class MrPlanSosSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 特定施設個別計画値UH(Y)
	 */
	private final Long specialInsPlanValueUhY;

	/**
	 * 理論値UH1
	 */
	private final Long theoreticalValueUh1;

	/**
	 * 理論値UH2
	 */
	private final Long theoreticalValueUh2;

	/**
	 * 営業所案UH(Y)
	 */
	private final Long officeValueUhY;

	/**
	 * 計画値UH(Y)
	 */
	private final Long plannedValueUhY;

	/**
	 * 特定施設個別計画値P(Y)
	 */
	private final Long specialInsPlanValuePY;

	/**
	 * 理論値P1
	 */
	private final Long theoreticalValueP1;

	/**
	 * 理論値P2
	 */
	private final Long theoreticalValueP2;

	/**
	 * 営業所案P(Y)
	 */
	private final Long officeValuePY;

	/**
	 * 計画値P(Y)
	 */
	private final Long plannedValuePY;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード
	 * @param mrPlan 検索結果の担当者別計画サマリー
	 */
	public MrPlanSosSummaryDto(String sosCd, MrPlan mrPlan) {

		this.sosCd = sosCd;
		this.prodCode = mrPlan.getProdCode();

		this.specialInsPlanValueUhY = mrPlan.getSpecialInsPlanValueUhY();
		this.theoreticalValueUh1 = mrPlan.getTheoreticalValueUh1();
		this.theoreticalValueUh2 = mrPlan.getTheoreticalValueUh2();
		this.officeValueUhY = mrPlan.getOfficeValueUhY();
		this.plannedValueUhY = mrPlan.getPlannedValueUhY();
		this.specialInsPlanValuePY = mrPlan.getSpecialInsPlanValuePY();
		this.theoreticalValueP1 = mrPlan.getTheoreticalValueP1();
		this.theoreticalValueP2 = mrPlan.getTheoreticalValueP2();
		this.officeValuePY = mrPlan.getOfficeValuePY();
		this.plannedValuePY = mrPlan.getPlannedValuePY();
	}

	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCde() {
		return sosCd;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 特定施設個別計画値UH(Y)を取得する。
	 * 
	 * @return 特定施設個別計画値UH(Y)
	 */
	public Long getSpecialInsPlanValueUhY() {
		return specialInsPlanValueUhY;
	}

	/**
	 * 理論値UH1を取得する。
	 * 
	 * @return 理論値UH1
	 */
	public Long getTheoreticalValueUh1() {
		return theoreticalValueUh1;
	}

	/**
	 * 理論値UH2を取得する。
	 * 
	 * @return 理論値UH2
	 */
	public Long getTheoreticalValueUh2() {
		return theoreticalValueUh2;
	}

	/**
	 * 営業所案UH(Y)を取得する。
	 * 
	 * @return 営業所案UH(Y)
	 */
	public Long getOfficeValueUhY() {
		return officeValueUhY;
	}

	/**
	 * 計画値UH(Y)を取得する。
	 * 
	 * @return 計画値UH(Y)
	 */
	public Long getPlannedValueUhY() {
		return plannedValueUhY;
	}

	/**
	 * 特定施設個別計画値P(Y)を取得する。
	 * 
	 * @return 特定施設個別計画値P(Y)
	 */
	public Long getSpecialInsPlanValuePY() {
		return specialInsPlanValuePY;
	}

	/**
	 * 理論値P1を取得する。
	 * 
	 * @return 理論値P1
	 */
	public Long getTheoreticalValueP1() {
		return theoreticalValueP1;
	}

	/**
	 * 理論値P2を取得する。
	 * 
	 * @return 理論値P2
	 */
	public Long getTheoreticalValueP2() {
		return theoreticalValueP2;
	}

	/**
	 * 営業所案P(Y)を取得する。
	 * 
	 * @return 営業所案P(Y)
	 */
	public Long getOfficeValuePY() {
		return officeValuePY;
	}

	/**
	 * 計画値P(Y)を取得する。
	 * 
	 * @return 計画値P(Y)
	 */
	public Long getPlannedValuePY() {
		return plannedValuePY;
	}

	/**
	 * 担当者別計画に変換する。
	 * 
	 * @return 担当者別計画
	 */
	public MrPlan convertMrPlan() {

		MrPlan mrPlan = new MrPlan();
		mrPlan.setProdCode(prodCode);
		mrPlan.setSpecialInsPlanValueUhY(specialInsPlanValueUhY);
		mrPlan.setTheoreticalValueUh1(theoreticalValueUh1);
		mrPlan.setTheoreticalValueUh2(theoreticalValueUh2);
		mrPlan.setOfficeValueUhY(officeValueUhY);
		mrPlan.setPlannedValueUhY(plannedValueUhY);
		mrPlan.setSpecialInsPlanValuePY(specialInsPlanValuePY);
		mrPlan.setTheoreticalValueP1(theoreticalValueP1);
		mrPlan.setTheoreticalValueP2(theoreticalValueP2);
		mrPlan.setOfficeValuePY(officeValuePY);
		mrPlan.setPlannedValuePY(plannedValuePY);

		return mrPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
