package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 計画対象品目の検索結果用DTO
 * 
 * @author stakeuchi
 */
public class PlannedProdResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 品目名
	 */
	private final String prodName;

	/**
	 * 担当者別計画ステータス シーケンスキー
	 */
	private final Long statusSeqKey;

	/**
	 * 担当者別計画ステータス 最終更新日時
	 */
	private final Date statusUpDate;

	/**
	 * 「試算中」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusEstimating;

	/**
	 * 「試算済」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusEstimated;

	/**
	 * 「チーム別計画公開」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusTeamPlanOpened;

	/**
	 * 「チーム別計画確定」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusTeamPlanCompleted;

	/**
	 * 「担当者別計画公開」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusMrPlanOpened;

	/**
	 * 「担当者別計画入力中(AL修正)」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusMrPlanInputting;

	/**
	 * 「担当者別計画入力完了(AL修正)」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusMrPlanInputFinished;

	/**
	 * 「担当者別計画確定(所長確定)」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusMrPlanCompleted;

	/**
	 * 試算開始日時
	 */
	private final Date estStartDate;

	/**
	 * 試算終了日時
	 */
	private final Date estEndDate;

	/**
	 * チーム別計画公開日時
	 */
	private final Date teamPlanOpenDate;

	/**
	 * チーム別計画確定日時
	 */
	private final Date teamPlanFixDate;

	/**
	 * 担当者別計画公開日時
	 */
	private final Date mrPlanOpenDate;

	/**
	 * 担当者別計画入力日時
	 */
	private final Date mrPlanInputDate;

	/**
	 * 担当者別計画確定日時
	 */
	private final Date mrPlanFixDate;

	/**
	 * UH 営業所計画金額
	 */
	private final Long uhOfficePlanAmount;

	/**
	 * UH チーム別計画金額
	 */
	private final Long uhTeamPlanAmount;

	/**
	 * UH 担当者別計画金額
	 */
	private final Long uhMrPlanAmount;

	/**
	 * P 営業所計画金額
	 */
	private final Long pOfficePlanAmount;

	/**
	 * P チーム別計画金額
	 */
	private final Long pTeamPlanAmount;

	/**
	 * P 担当者別計画金額
	 */
	private final Long pMrPlanAmount;

	/**
	 * 調整金額UH(営業所計画金額 - 担当者別計画金額)
	 */
	private final Long uhOfficeSagaku;

	/**
	 * 調整金額UH(チーム別計画金額 - 担当者別計画金額)
	 */
	private final Long uhTeamSagaku;

	/**
	 * 調整金額P(営業所計画金額 - 担当者別計画金額)
	 */
	private final Long pOfficeSagaku;

	/**
	 * 調整金額P(チーム別計画金額 - 担当者別計画金額)
	 */
	private final Long pTeamSagaku;

	/**
	 * 調整金額UH(営業所計画金額 - チーム別計画金額)
	 */
	private final Long uhTeamOfficeSagaku;

	/**
	 * 調整金額P(営業所計画金額 - チーム別計画金額)
	 */
	private final Long pTeamOfficeSagaku;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 品目コード
	 * @param prodName 品目名
	 * @param statusSeqKey 担当者別計画ステータス シーケンスキー
	 * @param statusUpDate 担当者別計画ステータス 最終更新日時
	 * @param isStatusEstimating 「試算中」フラグ
	 * @param isStatusEstimated 「試算済」フラグ
	 * @param isStatusTeamPlanOpened 「チーム別計画公開」フラグ
	 * @param isStatusTeamPlanCompleted 「チーム別計画確定」フラグ
	 * @param isStatusMrPlanOpened 「担当者別計画公開」フラグ
	 * @param isStatusMrPlanInputting 「担当者別計画入力中(AL修正)」フラグ
	 * @param isStatusMrPlanInputFinished 「担当者別計画入力完了(AL修正)」フラグ
	 * @param isStatusMrPlanCompleted 「担当者別計画確定(所長確定)」フラグ
	 * @param estStartDate 試算開始日時
	 * @param estEndDate 試算終了日時
	 * @param teamPlanOpenDate チーム別計画公開日時
	 * @param teamPlanFixDate チーム別計画確定日時
	 * @param mrPlanOpenDate 担当者別計画公開日時
	 * @param mrPlanInputDate 担当者別計画入力日時
	 * @param mrPlanFixDate 担当者別計画確定日時
	 * @param uhOfficePlanAmount UH 営業所計画金額
	 * @param uhTeamPlanAmount UH チーム別計画金額
	 * @param uhMrPlanAmount UH 担当者別計画金額
	 * @param pOfficePlanAmount P 営業所計画金額
	 * @param pTeamPlanAmount P チーム別計画金額
	 * @param pMrPlanAmount P 営業所計画金額
	 */
	public PlannedProdResultDto(String prodCode, String prodName, Long statusSeqKey, Date statusUpDate, Boolean isStatusEstimating, Boolean isStatusEstimated,
		Boolean isStatusTeamPlanOpened, Boolean isStatusTeamPlanCompleted, Boolean isStatusMrPlanOpened, Boolean isStatusMrPlanInputting, Boolean isStatusMrPlanInputFinished,
		Boolean isStatusMrPlanCompleted, Date estStartDate, Date estEndDate, Date teamPlanOpenDate, Date teamPlanFixDate, Date mrPlanOpenDate, Date mrPlanInputDate,
		Date mrPlanFixDate, Long uhOfficePlanAmount, Long uhTeamPlanAmount, Long uhMrPlanAmount, Long pOfficePlanAmount, Long pTeamPlanAmount, Long pMrPlanAmount) {
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.statusSeqKey = statusSeqKey;
		this.statusUpDate = statusUpDate;
		this.isStatusEstimating = isStatusEstimating;
		this.isStatusEstimated = isStatusEstimated;
		this.isStatusTeamPlanOpened = isStatusTeamPlanOpened;
		this.isStatusTeamPlanCompleted = isStatusTeamPlanCompleted;
		this.isStatusMrPlanOpened = isStatusMrPlanOpened;
		this.isStatusMrPlanInputting = isStatusMrPlanInputting;
		this.isStatusMrPlanInputFinished = isStatusMrPlanInputFinished;
		this.isStatusMrPlanCompleted = isStatusMrPlanCompleted;
		this.estStartDate = estStartDate;
		this.estEndDate = estEndDate;
		this.teamPlanOpenDate = teamPlanOpenDate;
		this.teamPlanFixDate = teamPlanFixDate;
		this.mrPlanOpenDate = mrPlanOpenDate;
		this.mrPlanInputDate = mrPlanInputDate;
		this.mrPlanFixDate = mrPlanFixDate;
		this.uhOfficePlanAmount = uhOfficePlanAmount;
		this.uhTeamPlanAmount = uhTeamPlanAmount;
		this.uhMrPlanAmount = uhMrPlanAmount;
		this.pOfficePlanAmount = pOfficePlanAmount;
		this.pTeamPlanAmount = pTeamPlanAmount;
		this.pMrPlanAmount = pMrPlanAmount;
		this.uhOfficeSagaku = MathUtil.sub(uhOfficePlanAmount, uhMrPlanAmount, true);
		this.pOfficeSagaku = MathUtil.sub(pOfficePlanAmount, pMrPlanAmount, true);
		this.uhTeamSagaku = MathUtil.sub(uhTeamPlanAmount, uhMrPlanAmount, true);
		this.pTeamSagaku = MathUtil.sub(pTeamPlanAmount, pMrPlanAmount, true);
		this.uhTeamOfficeSagaku = MathUtil.sub(uhOfficePlanAmount, uhTeamPlanAmount, true);
		this.pTeamOfficeSagaku = MathUtil.sub(pOfficePlanAmount, pTeamPlanAmount, true);
	}

	/**
	 * 品目コードを取得する。
	 * 
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名を取得する。
	 * 
	 * @return prodName 品目名
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 担当者別計画ステータス シーケンスキーを取得する。
	 * 
	 * @return statusSeqKey 担当者別計画ステータス シーケンスキー
	 */
	public Long getStatusSeqKey() {
		return statusSeqKey;
	}

	/**
	 * 担当者別計画ステータス 最終更新日時を取得する。
	 * 
	 * @return statusUpDate 担当者別計画ステータス 最終更新日時
	 */
	public Date getStatusUpDate() {
		return statusUpDate;
	}

	/**
	 * 「試算中」フラグを取得する。
	 * 
	 * @return isStatusEstimating 「試算中」フラグ
	 */
	public Boolean getIsStatusEstimating() {
		return isStatusEstimating;
	}

	/**
	 * 「試算済」フラグを取得する。
	 * 
	 * @return isStatusEstimated 「試算済」フラグ
	 */
	public Boolean getIsStatusEstimated() {
		return isStatusEstimated;
	}

	/**
	 * 「チーム別計画公開」フラグを取得する。
	 * 
	 * @return isStatusTeamPlanOpened 「チーム別計画公開」フラグ
	 */
	public Boolean getIsStatusTeamPlanOpened() {
		return isStatusTeamPlanOpened;
	}

	/**
	 * 「チーム別計画確定」フラグを取得する。
	 * 
	 * @return isStatusTeamPlanCompleted 「チーム別計画確定」フラグ
	 */
	public Boolean getIsStatusTeamPlanCompleted() {
		return isStatusTeamPlanCompleted;
	}

	/**
	 * 「担当者別計画公開」フラグを取得する。
	 * 
	 * @return isStatusMrPlanOpened 「担当者別計画公開」フラグ
	 */
	public Boolean getIsStatusMrPlanOpened() {
		return isStatusMrPlanOpened;
	}

	/**
	 * 「担当者別計画入力中(AL修正)」フラグ TRUE=ON, FALSE=OFFを取得する。
	 * 
	 * @return 「担当者別計画入力中(AL修正)」フラグ TRUE=ON, FALSE=OFF
	 */
	public Boolean getIsStatusMrPlanInputting() {
		return isStatusMrPlanInputting;
	}

	/**
	 * 「担当者別計画入力完了(AL修正)」フラグを取得する。
	 * 
	 * @return isStatusMrPlanInputFinished 「担当者別計画入力完了(AL修正)」フラグ
	 */
	public Boolean getIsStatusMrPlanInputFinished() {
		return isStatusMrPlanInputFinished;
	}

	/**
	 * 「担当者別計画確定(所長確定)」フラグを取得する。
	 * 
	 * @return isStatusMrPlanCompleted 「担当者別計画確定(所長確定)」フラグ
	 */
	public Boolean getIsStatusMrPlanCompleted() {
		return isStatusMrPlanCompleted;
	}

	/**
	 * 試算開始日時を取得する。
	 * 
	 * @return estStartDate 試算開始日時
	 */
	public Date getEstStartDate() {
		return estStartDate;
	}

	/**
	 * 試算終了日時を取得する。
	 * 
	 * @return estEndDate 試算終了日時
	 */
	public Date getEstEndDate() {
		return estEndDate;
	}

	/**
	 * チーム別計画公開日時を取得する。
	 * 
	 * @return teamPlanOpenDate チーム別計画公開日時
	 */
	public Date getTeamPlanOpenDate() {
		return teamPlanOpenDate;
	}

	/**
	 * チーム別計画確定日時を取得する。
	 * 
	 * @return teamPlanFixDate チーム別計画確定日時
	 */
	public Date getTeamPlanFixDate() {
		return teamPlanFixDate;
	}

	/**
	 * 担当者別計画公開日時を取得する。
	 * 
	 * @return mrPlanOpenDate 担当者別計画公開日時
	 */
	public Date getMrPlanOpenDate() {
		return mrPlanOpenDate;
	}

	/**
	 * 担当者別計画入力日時を取得する。
	 * 
	 * @return mrPlanInputDate 担当者別計画入力日時
	 */
	public Date getMrPlanInputDate() {
		return mrPlanInputDate;
	}

	/**
	 * 担当者別計画確定日時を取得する。
	 * 
	 * @return mrPlanFixDate 担当者別計画確定日時
	 */
	public Date getMrPlanFixDate() {
		return mrPlanFixDate;
	}

	/**
	 * UH 営業所計画金額を取得する。
	 * 
	 * @return uhOfficePlanAmount UH 営業所計画金額
	 */
	public Long getUhOfficePlanAmount() {
		return uhOfficePlanAmount;
	}

	/**
	 * UH チーム別計画金額を取得する。
	 * 
	 * @return uhTeamPlanAmount UH チーム別計画金額
	 */
	public Long getUhTeamPlanAmount() {
		return uhTeamPlanAmount;
	}

	/**
	 * UH 担当者別計画金額を取得する。
	 * 
	 * @return uhMrPlanAmount UH 担当者別計画金額
	 */
	public Long getUhMrPlanAmount() {
		return uhMrPlanAmount;
	}

	/**
	 * P 営業所計画金額を取得する。
	 * 
	 * @return pOfficePlanAmount P 営業所計画金額
	 */
	public Long getPOfficePlanAmount() {
		return pOfficePlanAmount;
	}

	/**
	 * P チーム別計画金額を取得する。
	 * 
	 * @return pTeamPlanAmount P チーム別計画金額
	 */
	public Long getPTeamPlanAmount() {
		return pTeamPlanAmount;
	}

	/**
	 * P 営業所計画金額を取得する。
	 * 
	 * @return pMrPlanAmount P 営業所計画金額
	 */
	public Long getPMrPlanAmount() {
		return pMrPlanAmount;
	}

	/**
	 * 調整金額UH(担当者別計画金額 - 営業所計画金額)を取得する。
	 * 
	 * @return 調整金額UH(担当者別計画金額 - 営業所計画金額)
	 */
	public Long getUhOfficeSagaku() {
		return uhOfficeSagaku;
	}

	/**
	 * 調整金額UH(担当者別計画金額 - チーム別計画金額)を取得する。
	 * 
	 * @return 調整金額UH(担当者別計画金額 - チーム別計画金額)
	 */
	public Long getUhTeamSagaku() {
		return uhTeamSagaku;
	}

	/**
	 * 調整金額P(担当者別計画金額 - 営業所計画金額)を取得する。
	 * 
	 * @return 調整金額P(担当者別計画金額 - 営業所計画金額)
	 */
	public Long getPOfficeSagaku() {
		return pOfficeSagaku;
	}

	/**
	 * 調整金額P(担当者別計画金額 - チーム別計画金額)を取得する。
	 * 
	 * @return 調整金額P(担当者別計画金額 - チーム別計画金額)
	 */
	public Long getPTeamSagaku() {
		return pTeamSagaku;
	}

	/**
	 * 調整金額UH(チーム別計画金額 - 営業所計画金額)を取得する。
	 * 
	 * @return 調整金額UH(チーム別計画金額 - 営業所計画金額)
	 */
	public Long getUhTeamOfficeSagaku() {
		return uhTeamOfficeSagaku;
	}

	/**
	 * 調整金額P(チーム別計画金額 - 営業所計画金額)を取得する。
	 * 
	 * @return 調整金額P(チーム別計画金額 - 営業所計画金額)
	 */
	public Long getPTeamOfficeSagaku() {
		return pTeamOfficeSagaku;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
