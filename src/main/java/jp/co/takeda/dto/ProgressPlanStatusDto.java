package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.view.JgiStatusSummary;
import jp.co.takeda.model.view.SosStatusSummary;

/**
 * 業務進捗表(医)[営業所計画-施設特約店別計画]を表すDTOクラス
 *
 * @author tkawabata
 */
//mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//public abstract class ProgressPlanStatusDto extends DpDto {
public class ProgressPlanStatusDto extends DpDto {
//mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 部門ランク
	 */
	private final BumonRank bumonRank;

	/**
	 * 組織単位のステータスのサマリリスト
	 */
	private final List<SosStatusSummary> sosStatusSummaryList;

	/**
	 * 担当者単位のステータスのサマリリスト
	 */
	private final List<JgiStatusSummary> jgiStatusSummaryList;

	/**
	 * コンストラクタ
	 *
	 * @param bumonRank 部門ランク(NULL可)
	 * @param sosStatusSummaryList 組織単位のステータスのサマリリスト(NULL可)
	 * @param jgiStatusSummaryList 担当者単位のステータスのサマリリスト(NULL可)
	 */
	public ProgressPlanStatusDto(BumonRank bumonRank, List<SosStatusSummary> sosStatusSummaryList, List<JgiStatusSummary> jgiStatusSummaryList) {
		this.bumonRank = bumonRank;
		this.sosStatusSummaryList = sosStatusSummaryList;
		this.jgiStatusSummaryList = jgiStatusSummaryList;
	}

	/**
	 * 部門ランクを取得する。
	 *
	 * @return bumonRank 部門ランク
	 */
	public BumonRank getBumonRank() {
		return bumonRank;
	}

	/**
	 * 組織単位のステータスのサマリリストを取得する。
	 *
	 * @return sosStatusSummaryList 組織単位のステータスのサマリリスト
	 */
	public List<SosStatusSummary> getSosStatusSummaryList() {
		return sosStatusSummaryList;
	}

	/**
	 * 担当者単位のステータスのサマリリストを取得する。
	 *
	 * @return jgiStatusSummaryList 担当者単位のステータスのサマリリスト
	 */
	public List<JgiStatusSummary> getJgiStatusSummaryList() {
		return jgiStatusSummaryList;
	}
	
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
}
