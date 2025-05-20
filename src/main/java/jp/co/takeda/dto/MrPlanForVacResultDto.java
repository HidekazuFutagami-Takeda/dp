package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlanForVac;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用担当者別計画検索結果用DTO
 * 
 * @author stakeuchi
 */
public class MrPlanForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織名
	 */
	private final String sosName;

	/**
	 * チーム名
	 */
	private final String teamName;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * ワクチン用担当者別計画
	 */
	private final MrPlanForVac mrPlanForVac;

	/**
	 * UP率
	 */
	private final Double upRate;

	/**
	 * 実績情報
	 */
	private final MrPlanResultValueDto resultValueDto;

	/**
	 * チーム合計フラグ
	 * <ul>
	 * <li>TRUE =チーム合計</li>
	 * <li>FALSE=チーム合計でない</li>
	 * </ul>
	 */
	private final boolean isTeamSum;

	/**
	 * 組織合計フラグ
	 * <ul>
	 * <li>TRUE =組織合計</li>
	 * <li>FALSE=組織合計でない</li>
	 * </ul>
	 */
	private final boolean isSosSum;

	/**
	 * 全合計フラグ
	 * <ul>
	 * <li>TRUE =全合計</li>
	 * <li>FALSE=全合計でない</li>
	 * </ul>
	 */
	private final boolean isAllSum;

	/**
	 * コンストラクタ
	 * 
	 * @param sosName 組織名
	 * @param teamName チーム名(NULL可)
	 * @param jgiName 従業員名
	 * @param mrPlanForVac ワクチン用担当者別計画
	 * @param resultValueDto 実績情報
	 * @param isTeamSum チーム合計フラグ
	 * @param isSosSum 組織合計フラグ
	 * @param isAllSum 全合計フラグ
	 */
	public MrPlanForVacResultDto(String sosName, String teamName, String jgiName, MrPlanForVac mrPlanForVac, MrPlanResultValueDto resultValueDto, boolean isTeamSum,
		boolean isSosSum, boolean isAllSum) {
		this.sosName = sosName;
		this.teamName = teamName;
		this.jgiName = jgiName;

		Long plannedValue = null;
		Long addvancePeriodValue = null;
		
		// その他
		MrPlanForVac tempMrPlanForVac = new MrPlanForVac();
		if (mrPlanForVac != null) {

			// UP率の計算
			if (mrPlanForVac.getPlannedValueB() != null) {
				plannedValue = Long.valueOf(mrPlanForVac.getPlannedValueB());
			}
			if (mrPlanForVac.getMonNnuSummary() != null && mrPlanForVac.getMonNnuSummary().getAdvancePeriod() != null) {
				addvancePeriodValue = Long.valueOf(mrPlanForVac.getMonNnuSummary().getAdvancePeriod());
			}
			
			tempMrPlanForVac.setSeqKey(mrPlanForVac.getSeqKey());
			tempMrPlanForVac.setUpDate(mrPlanForVac.getUpDate());
			tempMrPlanForVac.setPlannedValueB(ConvertUtil.parseMoneyToThousandUnit(mrPlanForVac.getPlannedValueB()));
		}

		this.upRate = MathUtil.calcUp(plannedValue, addvancePeriodValue);
		this.mrPlanForVac = tempMrPlanForVac;
		this.resultValueDto = resultValueDto;
		this.isTeamSum = isTeamSum;
		this.isSosSum = isSosSum;
		this.isAllSum = isAllSum;
	}

	/**
	 * 組織名を取得する。
	 * 
	 * @return sosName 組織名
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * チーム名を取得する。
	 * 
	 * @return teamName チーム名
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * 従業員名を取得する。
	 * 
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * ワクチン用担当者別計画を取得する。
	 * 
	 * @return mrPlanForVac ワクチン用担当者別計画
	 */
	public MrPlanForVac getMrPlanForVac() {
		return mrPlanForVac;
	}

	/**
	 * UP率を取得する。
	 * 
	 * @return upRate UP率
	 */
	public Double getUpRate() {
		return upRate;
	}

	/**
	 * 実績情報を取得する。
	 * 
	 * @return resultValueDto 実績情報
	 */
	public MrPlanResultValueDto getResultValueDto() {
		return resultValueDto;
	}

	/**
	 * チーム合計フラグを取得する。
	 * 
	 * @return isTeamSum チーム合計フラグ
	 */
	public boolean isTeamSum() {
		return isTeamSum;
	}

	/**
	 * 組織合計フラグを取得する。
	 * 
	 * @return isSosSum 組織合計フラグ
	 */
	public boolean isSosSum() {
		return isSosSum;
	}

	/**
	 * 全合計フラグを取得する。
	 * 
	 * @return isAllSum 全合計フラグ
	 */
	public boolean isAllSum() {
		return isAllSum;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
