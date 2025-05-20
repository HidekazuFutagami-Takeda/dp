package jp.co.takeda.dto;

import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.InsWsPlanStatus;

/**
 * 施設特約店別計画 担当者一覧画面の検索結果用DTO
 *
 * @author nozaki
 */
public class InsWsPlanMrResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設特約店別計画立案ステータス
	 */
	private final InsWsPlanStatus insWsPlanStatus;

	/**
	 * 納入実績サマリUH
	 */
	private final MrPlanResultValueDto resultSummaryUh;

	/**
	 * 納入実績サマリP
	 */
	private final MrPlanResultValueDto resultSummaryP;

	/**
	 * 施設特約店別計画サマリ
	 */
	private final InsWsPlanPlannedSummaryDto plannedSummary;

	/**
	 * 施医-施特 合算施設間調整有無（キー：existDiffUh、existDiffP）
	 */
	private final Map<String, Object> insChoseiResult;

	/**
	 * 計画表示可能か（上位計画が公開されているか）
	 */
	private final boolean dispPlan;

	/**
	 * 編集リンク表示可能か（上位計画が公開がされているか）<br>
	 * ・MMP品（重点品目）の場合、医師別計画が公開されているかも考慮
	 */
	private final boolean dispEditLink;

	/**
	 * 個別配分可能か<br>
	 * ・MMP品（重点品目）の場合、医師別計画が確定されているかも考慮
	 */
	private final boolean enableRehaibun;

	/**
	 * 削除施設数
	 */
	private final Integer delInsCount;

	/**
	 * 計画対象外特約店数
	 */
	private final Integer taigaiTytenCount;

	/**
	 * 配分除外施設数
	 */
	private final Integer exceptDistInsCount;

	/**
	 * コンストラクタ
	 *
	 * @param insWsPlanStatus 施設特約店別計画ステータス
	 * @param mrPlanResultValueDtoUh 納入実績サマリUH
	 * @param mrPlanResultValueDtoP 納入実績サマリP
	 * @param plannedSummary 施設特約店別計画サマリ
	 * @param dispPlan 計画表示可能か
	 * @param dispEditLink 編集リンク表示可能か
	 * @param enableRehaibun 個別配分可能か
	 * @param delInsCount 削除施設数
	 * @param taigaiTytenCount 計画対象外特約店数
	 * @param exceptDistInsCount 配分除外施設数
	 */
	public InsWsPlanMrResultDto(InsWsPlanStatus insWsPlanStatus, MrPlanResultValueDto resultSummaryUh, MrPlanResultValueDto resultSummaryP,
		InsWsPlanPlannedSummaryDto plannedSummary, Map<String, Object> insChoseiResult, boolean dispPlan, boolean dispEditLink, boolean enableRehaibun,
		Integer delInsCount, Integer taigaiTytenCount, Integer exceptDistInsCount) {
		this.insWsPlanStatus = insWsPlanStatus;
		this.resultSummaryUh = resultSummaryUh;
		this.resultSummaryP = resultSummaryP;
		this.plannedSummary = plannedSummary;
		this.insChoseiResult = insChoseiResult;
		this.dispPlan = dispPlan;
		this.dispEditLink = dispEditLink;
		this.enableRehaibun = enableRehaibun;
		this.delInsCount = delInsCount;
		this.taigaiTytenCount = taigaiTytenCount;
		this.exceptDistInsCount = exceptDistInsCount;
	}

	/**
	 * 施設特約店別計画立案ステータスを取得する。
	 *
	 * @return 施設特約店別計画立案ステータス
	 */
	public InsWsPlanStatus getInsWsPlanStatus() {
		return insWsPlanStatus;
	}

	/**
	 * 納入実績サマリUHを取得する。
	 *
	 * @return 納入実績UH
	 */
	public MrPlanResultValueDto getResultSummaryUh() {
		return resultSummaryUh;
	}

	/**
	 * 納入実績サマリPを取得する。
	 *
	 * @return 納入実績P
	 */
	public MrPlanResultValueDto getResultSummaryP() {
		return resultSummaryP;
	}

	/**
	 * 施設特約店別計画サマリを取得する。
	 *
	 * @return 施設特約店別計画サマリ
	 */
	public InsWsPlanPlannedSummaryDto getPlannedSummary() {
		return plannedSummary;
	}

	/**
	 * 施医-施特 合算施設間調整有無を取得する。
	 *
	 * @return 施医-施特 合算施設間調整有無
	 */
	public Map<String, Object> getInsChoseiResult() {
		return insChoseiResult;
	}

	/**
	 * 計画表示可能か（上位計画が公開されているか）
	 * @return
	 */
	public boolean isDispPlan() {
		return dispPlan;
	}

	/**
	 * 編集リンク表示可能か（上位計画が公開がされているか）<br>
	 * ・MMP品（重点品目）の場合、医師別計画が公開されているかも考慮
	 * @return
	 */
	public boolean isDispEditLink() {
		return dispEditLink;
	}

	/**
	 * 個別配分可能か<br>
	 * ・MMP品（重点品目）の場合、医師別計画が確定されているかも考慮
	 * @return
	 */
	public boolean isEnableRehaibun() {
		return enableRehaibun;
	}

	/**
	 * 削除施設数を取得する。
	 *
	 * @return 削除施設数
	 */
	public Integer getDelInsCount() {
		return delInsCount;
	}

	/**
	 * 計画対象外特約店数を取得する。
	 *
	 * @return 計画対象外特約店数
	 */
	public Integer getTaigaiTytenCount() {
		return taigaiTytenCount;
	}

	/**
	 * 配分除外施設数を取得する
	 *
	 * @return 配分除外施設数
	 */
	public Integer getExceptDistInsCount() {
		return exceptDistInsCount;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
