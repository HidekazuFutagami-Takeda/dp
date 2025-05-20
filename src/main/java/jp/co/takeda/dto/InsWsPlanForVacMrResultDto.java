package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.InsWsPlanStatusForVac;

/**
 * ワクチン用施設特約店別計画 担当者一覧画面の検索結果用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacMrResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ワクチン用施設特約店別計画立案ステータス
	 */
	private final InsWsPlanStatusForVac insWsPlanStatus;

	/**
	 * 納入実績サマリ
	 */
	private final MrPlanResultValueDto resultSummary;

	/**
	 * 施設特約店別計画サマリ
	 */
	private final InsWsPlanForVacPlannedSummaryDto plannedSummary;

	/**
	 * コンストラクタ
	 * 
	 * @param insWsPlanStatus ワクチン用施設特約店別計画ステータス
	 * @param result 納入実績
	 * @param plannedSummary ワクチン用施設特約店別計画サマリ
	 */
	public InsWsPlanForVacMrResultDto(InsWsPlanStatusForVac insWsPlanStatus, MrPlanResultValueDto resultSummary, InsWsPlanForVacPlannedSummaryDto plannedSummary) {

		this.insWsPlanStatus = insWsPlanStatus;
		this.resultSummary = resultSummary;
		this.plannedSummary = plannedSummary;
	}

	/**
	 * ワクチン用施設特約店別計画立案ステータスを取得する。
	 * 
	 * @return ワクチン用施設特約店別計画立案ステータス
	 */
	public InsWsPlanStatusForVac getInsWsPlanStatus() {
		return insWsPlanStatus;
	}

	/**
	 * 納入実績サマリを取得する。
	 * 
	 * @return 納入実績
	 */
	public MrPlanResultValueDto getResultSummary() {
		return resultSummary;
	}

	/**
	 * ワクチン用施設特約店別計画サマリを取得する。
	 * 
	 * @return ワクチン用施設特約店別計画サマリ
	 */
	public InsWsPlanForVacPlannedSummaryDto getPlannedSummary() {
		return plannedSummary;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
