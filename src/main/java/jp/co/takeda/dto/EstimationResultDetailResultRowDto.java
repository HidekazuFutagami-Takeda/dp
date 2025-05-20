package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算結果詳細の明細一覧の1行を表すDTO
 *
 * @author stakeuchi
 */
public class EstimationResultDetailResultRowDto extends DpDto {

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
	 * 組織名
	 */
	private final String sosName;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * 職種名
	 */
	private final String shokushuName;

	/**
	 * 特定施設個別計画
	 */
	private final Long specialInsPlanValue;

	/**
	 * [理論値1] 増分
	 */
	private final Long planForUp1;

	/**
	 * [理論値1] 計画
	 */
	private final Long theoreticalValue1;

	/**
	 * [理論値1] 前同比
	 */
	private final Double lastSameRatio1;

	/**
	 * [理論値2] 増分
	 */
	private final Long planForUp2;

	/**
	 * [理論値2] 計画
	 */
	private final Long theoreticalValue2;

	/**
	 * [理論値2] 前同比
	 */
	private final Double lastSameRatio2;

	/**
	 * [試算指数合計] 所内構成比
	 */
	private final Double sumOfficeRatio;

	/**
	 * [試算指数合計] チーム内構成比
	 */
	private final Double sumTeamRatio;

	/**
	 * [未獲得市場] 未獲得市場
	 */
	private final Long mikakutokuValue;

	/**
	 * [未獲得市場] 所内構成比
	 */
	private final Double mikakutokuRatioOffice;

	/**
	 * [未獲得市場] チーム内構成比
	 */
	private final Double mikakutokuRatioTeam;

	/**
	 * [納入実績] 納入実績
	 */
	private final Long deliveryValue;

	/**
	 * [納入実績] 所内構成比
	 */
	private final Double deliveryRatioOffice;

	/**
	 * [納入実績] チーム内構成比
	 */
	private final Double deliveryRatioTeam;

	/**
	 * [フリー項目1] フリー項目1
	 */
	private final Long free1Value;

	/**
	 * [フリー項目1] 所内構成比
	 */
	private final Double free1RatioOffice;

	/**
	 * [フリー項目1] チーム内構成比
	 */
	private final Double free1RatioTeam;

	/**
	 * [フリー項目2] フリー項目2
	 */
	private final Long free2Value;

	/**
	 * [フリー項目2] 所内構成比
	 */
	private final Double free2RatioOffice;

	/**
	 * [フリー項目2] チーム内構成比
	 */
	private final Double free2RatioTeam;

	/**
	 * [フリー項目3] フリー項目3
	 */
	private final Long free3Value;

	/**
	 * [フリー項目3] 所内構成比
	 */
	private final Double free3RatioOffice;

	/**
	 * [フリー項目3] チーム内構成比
	 */
	private final Double free3RatioTeam;

	/**
	 * 営業所計画部フラグ TRUE=営業所計画部 FALSE=営業所計画部でない
	 */
	private final Boolean isOfficeRow;

	/**
	 * チーム別計画部フラグ TRUE=チーム別計画部 FALSE=チーム別計画部でない
	 */
	private final Boolean isTeamRow;

	/**
	 * チーム別計画部合計行フラグ TRUE=チーム別計画部合計行 FALSE=チーム別計画部合計行でない
	 */
	private final Boolean isTeamSumRow;

	/**
	 * 担当者別計画部フラグ TRUE=担当者別計画部 FALSE=担当者別計画部でない
	 */
	private final Boolean isMrRow;

	/**
	 * 担当者別計画部合計行フラグ TRUE=担当者別計画部合計行 FALSE=担当者別計画部合計行でない
	 */
	private final Boolean isMrSumRow;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd 組織コード
	 * @param sosName 組織名
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param specialInsPlanValue 特定施設個別計画
	 * @param planForUp1 [理論値1] 増分
	 * @param theoreticalValue1 [理論値1] 計画
	 * @param lastSameRatio1 [理論値1] 前同比
	 * @param planForUp2 [理論値2] 増分
	 * @param theoreticalValue2 [理論値2] 計画
	 * @param lastSameRatio2 [理論値2] 増分
	 * @param sumOfficeRatio [試算指数合計] 所内構成比
	 * @param sumTeamRatio [試算指数合計] チーム内構成比
	 * @param mikakutokuValue [未獲得市場] 未獲得市場
	 * @param mikakutokuRatioOffice [未獲得市場] 所内構成比
	 * @param mikakutokuRatioTeam [未獲得市場] チーム内構成比
	 * @param deliveryValue [納入実績] 納入実績
	 * @param deliveryRatioOffice [納入実績] 所内構成比
	 * @param deliveryRatioTeam [納入実績] チーム内構成比
	 * @param free1Value [フリー項目1] フリー項目1
	 * @param free1RatioOffice [フリー項目1] 所内構成比
	 * @param free1RatioTeam [フリー項目1] チーム内構成比
	 * @param free2Value [フリー項目2] フリー項目2
	 * @param free2RatioOffice [フリー項目2] 所内構成比
	 * @param free2RatioTeam [フリー項目2] チーム内構成比
	 * @param free3Value [フリー項目3] フリー項目3
	 * @param free3RatioOffice [フリー項目3] 所内構成比
	 * @param free3RatioTeam [フリー項目3] チーム内構成比
	 * @param isOfficeRow 営業所計画部フラグ
	 * @param isTeamRow チーム別計画部フラグ
	 * @param isTeamSumRow チーム別計画部合計行フラグ
	 * @param isMrRow 担当者別計画部フラグ
	 * @param isMrSumRow 担当者別計画部合計行フラグ
	 */
	public EstimationResultDetailResultRowDto(String sosCd, String sosName, Integer jgiNo, String jgiName, Long specialInsPlanValue, Long planForUp1, Long theoreticalValue1,
		Double lastSameRatio1, Long planForUp2, Long theoreticalValue2, Double lastSameRatio2, Double sumOfficeRatio, Double sumTeamRatio, Long mikakutokuValue,
		Double mikakutokuRatioOffice, Double mikakutokuRatioTeam, Long deliveryValue, Double deliveryRatioOffice, Double deliveryRatioTeam, Long free1Value,
		Double free1RatioOffice, Double free1RatioTeam, Long free2Value, Double free2RatioOffice, Double free2RatioTeam, Long free3Value, Double free3RatioOffice,
		Double free3RatioTeam, Boolean isOfficeRow, Boolean isTeamRow, Boolean isTeamSumRow, Boolean isMrRow, Boolean isMrSumRow, String shokushuName) {
		this.sosCd = sosCd;
		this.sosName = sosName;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.specialInsPlanValue = specialInsPlanValue;
		this.planForUp1 = planForUp1;
		this.theoreticalValue1 = theoreticalValue1;
		this.lastSameRatio1 = lastSameRatio1;
		this.planForUp2 = planForUp2;
		this.theoreticalValue2 = theoreticalValue2;
		this.lastSameRatio2 = lastSameRatio2;
		this.sumOfficeRatio = sumOfficeRatio;
		this.sumTeamRatio = sumTeamRatio;
		this.mikakutokuValue = mikakutokuValue;
		this.mikakutokuRatioOffice = mikakutokuRatioOffice;
		this.mikakutokuRatioTeam = mikakutokuRatioTeam;
		this.deliveryValue = deliveryValue;
		this.deliveryRatioOffice = deliveryRatioOffice;
		this.deliveryRatioTeam = deliveryRatioTeam;
		this.free1Value = free1Value;
		this.free1RatioOffice = free1RatioOffice;
		this.free1RatioTeam = free1RatioTeam;
		this.free2Value = free2Value;
		this.free2RatioOffice = free2RatioOffice;
		this.free2RatioTeam = free2RatioTeam;
		this.free3Value = free3Value;
		this.free3RatioOffice = free3RatioOffice;
		this.free3RatioTeam = free3RatioTeam;
		this.isOfficeRow = isOfficeRow;
		this.isTeamRow = isTeamRow;
		this.isTeamSumRow = isTeamSumRow;
		this.isMrRow = isMrRow;
		this.isMrSumRow = isMrSumRow;
		this.shokushuName = shokushuName;
	}

	/**
	 * 組織コードを取得する。
	 *
	 * @return sosCd 組織コード
	 */
	public String getSosCd() {
		return sosCd;
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
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
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
	 * 特定施設個別計画を取得する。
	 *
	 * @return specialInsPlanValue 特定施設個別計画
	 */
	public Long getSpecialInsPlanValue() {
		return specialInsPlanValue;
	}

	/**
	 * [理論値1] 増分を取得する。
	 *
	 * @return planForUp1 [理論値1] 増分
	 */
	public Long getPlanForUp1() {
		return planForUp1;
	}

	/**
	 * [理論値1] 計画を取得する。
	 *
	 * @return theoreticalValue1 [理論値1] 計画
	 */
	public Long getTheoreticalValue1() {
		return theoreticalValue1;
	}

	/**
	 * [理論値1] 前同比を取得する。
	 *
	 * @return lastSameRatio1 [理論値1] 前同比
	 */
	public Double getLastSameRatio1() {
		return lastSameRatio1;
	}

	/**
	 * [理論値2] 増分を取得する。
	 *
	 * @return planForUp2 [理論値2] 増分
	 */
	public Long getPlanForUp2() {
		return planForUp2;
	}

	/**
	 * [理論値2] 計画を取得する。
	 *
	 * @return theoreticalValue2 [理論値2] 計画
	 */
	public Long getTheoreticalValue2() {
		return theoreticalValue2;
	}

	/**
	 * [理論値2] 前同比を取得する。
	 *
	 * @return lastSameRatio2 [理論値2] 前同比
	 */
	public Double getLastSameRatio2() {
		return lastSameRatio2;
	}

	/**
	 * [試算指数合計] 所内構成比を取得する。
	 *
	 * @return sumOfficeRatio [試算指数合計] 所内構成比
	 */
	public Double getSumOfficeRatio() {
		return sumOfficeRatio;
	}

	/**
	 * [試算指数合計] チーム内構成比を取得する。
	 *
	 * @return sumTeamRatio [試算指数合計] チーム内構成比
	 */
	public Double getSumTeamRatio() {
		return sumTeamRatio;
	}

	/**
	 * [未獲得市場] 未獲得市場を取得する。
	 *
	 * @return mikakutokuValue [未獲得市場] 未獲得市場
	 */
	public Long getMikakutokuValue() {
		return mikakutokuValue;
	}

	/**
	 * [未獲得市場] 所内構成比を取得する。
	 *
	 * @return mikakutokuRatioOffice [未獲得市場] 所内構成比
	 */
	public Double getMikakutokuRatioOffice() {
		return mikakutokuRatioOffice;
	}

	/**
	 * [未獲得市場] チーム内構成比を取得する。
	 *
	 * @return mikakutokuRatioTeam [未獲得市場] チーム内構成比
	 */
	public Double getMikakutokuRatioTeam() {
		return mikakutokuRatioTeam;
	}

	/**
	 * [納入実績] 納入実績を取得する。
	 *
	 * @return deliveryValue [納入実績] 納入実績
	 */
	public Long getDeliveryValue() {
		return deliveryValue;
	}

	/**
	 * [納入実績] 所内構成比を取得する。
	 *
	 * @return deliveryRatioOffice [納入実績] 所内構成比
	 */
	public Double getDeliveryRatioOffice() {
		return deliveryRatioOffice;
	}

	/**
	 * [納入実績] チーム内構成比を取得する。
	 *
	 * @return deliveryRatioTeam [納入実績] チーム内構成比
	 */
	public Double getDeliveryRatioTeam() {
		return deliveryRatioTeam;
	}

	/**
	 * [フリー項目1] フリー項目1を取得する。
	 *
	 * @return free1Value [フリー項目1] フリー項目1
	 */
	public Long getFree1Value() {
		return free1Value;
	}

	/**
	 * [フリー項目1] 所内構成比を取得する。
	 *
	 * @return free1RatioOffice [フリー項目1] 所内構成比
	 */
	public Double getFree1RatioOffice() {
		return free1RatioOffice;
	}

	/**
	 * [フリー項目1] チーム内構成比を取得する。
	 *
	 * @return free1RatioTeam [フリー項目1] チーム内構成比
	 */
	public Double getFree1RatioTeam() {
		return free1RatioTeam;
	}

	/**
	 * [フリー項目2] フリー項目2を取得する。
	 *
	 * @return free2Value [フリー項目2] フリー項目2
	 */
	public Long getFree2Value() {
		return free2Value;
	}

	/**
	 * [フリー項目2] 所内構成比を取得する。
	 *
	 * @return free2RatioOffice [フリー項目2] 所内構成比
	 */
	public Double getFree2RatioOffice() {
		return free2RatioOffice;
	}

	/**
	 * [フリー項目2] チーム内構成比を取得する。
	 *
	 * @return free2RatioTeam [フリー項目2] チーム内構成比
	 */
	public Double getFree2RatioTeam() {
		return free2RatioTeam;
	}

	/**
	 * [フリー項目3] フリー項目3を取得する。
	 *
	 * @return free3Value [フリー項目3] フリー項目3
	 */
	public Long getFree3Value() {
		return free3Value;
	}

	/**
	 * [フリー項目3] 所内構成比を取得する。
	 *
	 * @return free3RatioOffice [フリー項目3] 所内構成比
	 */
	public Double getFree3RatioOffice() {
		return free3RatioOffice;
	}

	/**
	 * [フリー項目3] チーム内構成比を取得する。
	 *
	 * @return free3RatioTeam [フリー項目3] チーム内構成比
	 */
	public Double getFree3RatioTeam() {
		return free3RatioTeam;
	}

	/**
	 * 営業所計画部フラグを取得する。
	 *
	 * @return isOfficeRow 営業所計画部フラグ
	 */
	public Boolean getIsOfficeRow() {
		return isOfficeRow;
	}

	/**
	 * チーム別計画部フラグを取得する。
	 *
	 * @return isTeamRow チーム別計画部フラグ
	 */
	public Boolean getIsTeamRow() {
		return isTeamRow;
	}

	/**
	 * チーム別計画部合計行フラグを取得する。
	 *
	 * @return isTeamSumRow チーム別計画部合計行フラグ
	 */
	public Boolean getIsTeamSumRow() {
		return isTeamSumRow;
	}

	/**
	 * 担当者別計画部フラグを取得する。
	 *
	 * @return isMrRow 担当者別計画部フラグ
	 */
	public Boolean getIsMrRow() {
		return isMrRow;
	}

	/**
	 * 担当者別計画部合計行フラグを取得する。
	 *
	 * @return isMrSumRow 担当者別計画部合計行フラグ
	 */
	public Boolean getIsMrSumRow() {
		return isMrSumRow;
	}

	/**
	 * 職種名を取得する
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
