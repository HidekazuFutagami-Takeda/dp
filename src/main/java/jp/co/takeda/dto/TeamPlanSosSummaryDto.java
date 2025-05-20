package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.TeamPlan;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * チーム別計画組織単位のサマリーを表すDTO
 * 
 * @author khashimoto
 */
public class TeamPlanSosSummaryDto extends DpDto {

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
	 * @param mrPlan 検索結果のチーム別計画サマリー
	 */
	public TeamPlanSosSummaryDto(String sosCd, TeamPlan teamPlan) {

		this.sosCd = sosCd;
		this.prodCode = teamPlan.getProdCode();

		this.specialInsPlanValueUhY = teamPlan.getSpecialInsPlanValueUhY();
		this.theoreticalValueUh1 = teamPlan.getTheoreticalValueUh1();
		this.theoreticalValueUh2 = teamPlan.getTheoreticalValueUh2();
		this.officeValueUhY = teamPlan.getOfficeValueUhY();
		this.plannedValueUhY = teamPlan.getPlannedValueUhY();
		this.specialInsPlanValuePY = teamPlan.getSpecialInsPlanValuePY();
		this.theoreticalValueP1 = teamPlan.getTheoreticalValueP1();
		this.theoreticalValueP2 = teamPlan.getTheoreticalValueP2();
		this.officeValuePY = teamPlan.getOfficeValuePY();
		this.plannedValuePY = teamPlan.getPlannedValuePY();
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
	 * チーム別計画に変換する。
	 * 
	 * @return チーム別計画
	 */
	public TeamPlan convertTeamPlan() {

		TeamPlan teamPlan = new TeamPlan();
		teamPlan.setSosCd(sosCd);
		teamPlan.setProdCode(prodCode);
		teamPlan.setSpecialInsPlanValueUhY(specialInsPlanValueUhY);
		teamPlan.setTheoreticalValueUh1(theoreticalValueUh1);
		teamPlan.setTheoreticalValueUh2(theoreticalValueUh2);
		teamPlan.setOfficeValueUhY(officeValueUhY);
		teamPlan.setPlannedValueUhY(plannedValueUhY);
		teamPlan.setSpecialInsPlanValuePY(specialInsPlanValuePY);
		teamPlan.setTheoreticalValueP1(theoreticalValueP1);
		teamPlan.setTheoreticalValueP2(theoreticalValueP2);
		teamPlan.setOfficeValuePY(officeValuePY);
		teamPlan.setPlannedValuePY(plannedValuePY);

		return teamPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
