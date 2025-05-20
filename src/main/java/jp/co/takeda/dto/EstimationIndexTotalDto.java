package jp.co.takeda.dto;

import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 試算指数合計用ＤＴＯクラス
 * 
 * @author tkawabata
 */
public class EstimationIndexTotalDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員別所内構成比マップ
	 */
	private final Map<Integer, Double> officeEstimationIndexTotalMapByJgiNoMap;

	/**
	 * チーム別所内構成比マップ
	 */
	private final Map<String, Double> officeEstimationIndexTotalMapBySosCd4Map;

	/**
	 * 従業員別チーム内構成比マップ
	 */
	private final Map<Integer, Double> teamEstimationIndexTotalMapByJgiNoMap;

	/**
	 * コンストラクタ
	 * 
	 * @param officeEstimationIndexTotalMapByJgiNoMap 従業員別所内構成比マップ
	 * @param officeEstimationIndexTotalMapBySosCd4Map チーム別所内構成比マップ
	 * @param teamEstimationIndexTotalMapByJgiNoMap 従業員別チーム内構成比マップ
	 * @param teamEstimationIndexTotalMapBySosCd4Map チーム別チーム構成比マップ
	 */
	public EstimationIndexTotalDto(Map<Integer, Double> officeEstimationIndexTotalMapByJgiNoMap, Map<String, Double> officeEstimationIndexTotalMapBySosCd4Map,
		Map<Integer, Double> teamEstimationIndexTotalMapByJgiNoMap) {
		this.officeEstimationIndexTotalMapByJgiNoMap = officeEstimationIndexTotalMapByJgiNoMap;
		this.officeEstimationIndexTotalMapBySosCd4Map = officeEstimationIndexTotalMapBySosCd4Map;
		this.teamEstimationIndexTotalMapByJgiNoMap = teamEstimationIndexTotalMapByJgiNoMap;
	}

	/**
	 * 従業員別所内構成比マップを取得する。
	 * 
	 * @return 従業員別所内構成比マップ
	 */
	public Map<Integer, Double> getOfficeEstimationIndexTotalMapByJgiNoMap() {
		return officeEstimationIndexTotalMapByJgiNoMap;
	}

	/**
	 * チーム別所内構成比マップを取得する。
	 * 
	 * @return チーム別所内構成比マップ
	 */
	public Map<String, Double> getOfficeEstimationIndexTotalMapBySosCd4Map() {
		return officeEstimationIndexTotalMapBySosCd4Map;
	}

	/**
	 * 従業員別チーム内構成比マップを取得する。
	 * 
	 * @return 従業員別チーム内構成比マップ
	 */
	public Map<Integer, Double> getTeamEstimationIndexTotalMapByJgiNoMap() {
		return teamEstimationIndexTotalMapByJgiNoMap;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
