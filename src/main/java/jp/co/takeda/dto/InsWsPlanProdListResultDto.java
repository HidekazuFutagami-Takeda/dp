package jp.co.takeda.dto;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.view.InsWsPlanStatSum;

/**
 * 施設特約店別計画 品目一覧画面の検索結果用DTO
 *
 * @author siwamoto
 */
public class InsWsPlanProdListResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 重点品目フラグ
	 */
	private final boolean planLevelInsDoc;

	/**
	 * 担当者別計画ステータス
	 */
	private final MrPlanStatus mrPlanStatus;

	/**
	 * 施設特約店別計画ステータスのサマリー
	 */
	private final InsWsPlanStatSum insWsPlanStatSum;

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
	 * 最終更新日
	 */
	private final Date lastUpdate;

	/**
	 * 計画表示可能か（上位計画が公開されているか）
	 */
	private final boolean dispPlan;

	/**
	 * 編集リンク表示可能か（上位計画が公開がされているか）<br>
	 * ・MMP品（重点品目）の場合、医師別計画がこうかいされているかも考慮
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
	 * @param mrPlanStatus 担当者別計画ステータス
	 * @param prodCode 施設特約店別計画立案ステータスサマリ
	 * @param mrPlanResultValueDtoUh 納入実績サマリUH
	 * @param mrPlanResultValueDtoP 納入実績サマリP
	 * @param plannedSummary 施設特約店別計画サマリ
	 * @param dispPlan 計画表示可能か
	 * @param dispEditLink 編集リンク表示可能か
	 * @param planLevelInsDoc 重点品目フラグ
	 * @param enableRehaibun 個別配分可能か
	 * @param delInsCount 削除施設数
	 * @param taigaiTytenCount 対象外特約店数
	 * @param exceptDistInsCount 配分除外施設数
	 */
	public InsWsPlanProdListResultDto(MrPlanStatus mrPlanStatus, ProdInsWsPlanStatSummaryDto statusSummary, MrPlanResultValueDto resultSummaryUh, MrPlanResultValueDto resultSummaryP,
		InsWsPlanPlannedSummaryDto plannedSummary, Map<String, Object> insChoseiResult, boolean dispPlan, boolean dispEditLink, boolean planLevelInsDoc, boolean enableRehaibun,
		Integer delInsCount, Integer taigaiTytenCount, Integer exceptDistInsCount) {

		this.prodCode = statusSummary.getProdCode();
		this.prodName = statusSummary.getProdName();
		this.mrPlanStatus = mrPlanStatus;
		this.insWsPlanStatSum = statusSummary.getInsWsPlanStatSum();
		this.resultSummaryUh = resultSummaryUh;
		this.resultSummaryP = resultSummaryP;
		this.plannedSummary = plannedSummary;
		this.lastUpdate = statusSummary.getLastUpdate();
		this.insChoseiResult = insChoseiResult;
		this.dispPlan = dispPlan;
		this.dispEditLink = dispEditLink;
		this.planLevelInsDoc = planLevelInsDoc;
		this.enableRehaibun = enableRehaibun;
		this.delInsCount = delInsCount;
		this.taigaiTytenCount = taigaiTytenCount;
		this.exceptDistInsCount = exceptDistInsCount;
	}

	/**
	 * 品目コードを取得する。
	 *
	 * @return prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 重点品目フラグを取得する。
	 *
	 * @return 重点品目フラグ
	 */
	public boolean isPlanLevelInsDoc() {
		return planLevelInsDoc;
	}

	/**
	 * 担当者別計画ステータスを取得する。
	 *
	 * @return 担当者別計画ステータス
	 */
	public MrPlanStatus getMrPlanStatus() {
		return mrPlanStatus;
	}

	/**
	 * 施設特約店別計画ステータスのサマリーを取得する。
	 *
	 * @return 施設特約店別計画ステータスのサマリー
	 */
	public InsWsPlanStatSum getInsWsPlanStatSum() {
		return insWsPlanStatSum;
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
	 * 最終更新日取得
	 *
	 * @return 最終更新日取得
	 */
	public Date getLastUpdate() {
		return lastUpdate;
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
	 * ・MMP品（重点品目）の場合、医師別計画がこうかいされているかも考慮
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
