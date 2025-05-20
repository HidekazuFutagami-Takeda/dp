package jp.co.takeda.dto;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.view.WsPlanStatusSummary;

/**
 * 業務進捗表(医)[特約店別計画(MMP品・仕入品(一般・麻薬))]を表すDTOクラス
 *
 * @author tkawabata
 */
public class ProgressPlanWsStatusDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 部門ランク
	 */
	private final BumonRank bumonRank;

	/**
	 * 特約店別計画ステータスのサマリリスト
	 */
	private final HashMap<String, WsPlanStatusSummary> wsPlanStatusSummaryMap;

	/**
	 * 特約店別計画ステータスのサマリリスト
	 */
	private final List<HashMap<String, WsPlanStatusSummary>> wsPlanStatusSummaryMapList;


	/**
	 * コンストラクタ
	 *
	 * @param bumonRank 部門ランク(NULL可)
	 * @param wsPlanStatusSummaryList 特約店別計画ステータスのサマリリスト
	 */
	public ProgressPlanWsStatusDto(BumonRank bumonRank, HashMap<String, WsPlanStatusSummary> wsPlanStatusSummaryMap) {
		this.bumonRank = bumonRank;
		this.wsPlanStatusSummaryMap = wsPlanStatusSummaryMap;
		this. wsPlanStatusSummaryMapList = null;
	}

	/**
	 * コンストラクタ
	 *
	 * @param bumonRank 部門ランク(NULL可)
	 * @param wsPlanStatusSummaryMapList 特約店別計画ステータスのサマリマップのリスト
	 */
	public ProgressPlanWsStatusDto(BumonRank bumonRank, List<HashMap<String, WsPlanStatusSummary>> wsPlanStatusSummaryMapList) {
		this.bumonRank = bumonRank;
		this.wsPlanStatusSummaryMapList = wsPlanStatusSummaryMapList;
		this.wsPlanStatusSummaryMap = null;
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
	 * 特約店別計画ステータスのサマリリストを取得する。
	 *
	 * @return wsPlanStatusONCSummaryList 特約店別計画ステータスのサマリリスト
	 */
	public HashMap<String, WsPlanStatusSummary> getWsPlanStatusSummaryMap() {
		return wsPlanStatusSummaryMap;
	}

	/**
	 *
	 * @return wsPlanStatusSummaryMapList 特約店別計画ステータスのサマリマップのリスト
	 */
	public List<HashMap<String, WsPlanStatusSummary>> getWsPlanStatusSummaryMapList() {
		return wsPlanStatusSummaryMapList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
