package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * チーム別計画　更新用DTO
 * 
 * @author nozaki
 */
public class TeamPlanUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 品目情報
	 */
	private final PlannedProd plannedProd;

	/**
	 * 施設出力対象区分
	 */
	private final InsType insType;

	/**
	 * 更新対象のチーム別計画リスト
	 */
	private final List<TeamPlanDto> teamPlanDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param plannedProd 品目情報
	 * @param mrPlanList 更新対象のチーム別計画リスト
	 */
	public TeamPlanUpdateDto(PlannedProd plannedProd, InsType insType, List<TeamPlanDto> teamPlanDtoList) {

		this.plannedProd = plannedProd;
		this.insType = insType;
		this.teamPlanDtoList = teamPlanDtoList;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public PlannedProd getPlannedProd() {
		return plannedProd;
	}

	/**
	 * 施設出力対象区分を取得する。
	 * 
	 * @return 施設対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 更新対象のチーム別計画リストを取得する。
	 * 
	 * @return 更新対象のチーム別計画リスト
	 */
	public List<TeamPlanDto> getTeamPlanDtoList() {
		return teamPlanDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
