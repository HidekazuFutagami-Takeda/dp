package jp.co.takeda.dto;

import java.util.Map;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.view.ProdSummary;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 過去実績サマリDTOクラス
 * 
 * @author tkawabata
 */
public class DeliveryResultSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 簡易版品目情報
	 */
	private final ProdSummary prodSummary;

	/**
	 * 営業所サマリ
	 */
	private final DeliveryResultSummaryDetailDto officeSummary;

	/**
	 * チームサマリ(キーはチームの組織コード)マップ
	 */
	private final Map<String, DeliveryResultSummaryDetailDto> teamSummaryMap;

	/**
	 * 全チーム合計サマリ
	 */
	private final DeliveryResultSummaryDetailDto allTeamSummary;

	/**
	 * ＭＲサマリ(キーはチームの従業員番号)マップ
	 */
	private final Map<Integer, DeliveryResultSummaryDetailDto> mrSummaryMap;

	/**
	 * コンストラクタ
	 * 
	 * @param prodSummary 簡易版品目情報
	 * @param officeSummary 営業所サマリ
	 * @param teamSummaryMap チームサマリ(キーはチームの組織コード)マップ
	 * @param allTeamSummary 全チーム合計サマリ
	 * @param mrSummaryMap ＭＲサマリ(キーは従業員番号)マップ
	 */
	public DeliveryResultSummaryDto(ProdSummary prodSummary, DeliveryResultSummaryDetailDto officeSummary, Map<String, DeliveryResultSummaryDetailDto> teamSummaryMap,
		DeliveryResultSummaryDetailDto allTeamSummary, Map<Integer, DeliveryResultSummaryDetailDto> mrSummaryMap) {
		this.prodSummary = prodSummary;
		this.officeSummary = officeSummary;
		this.teamSummaryMap = teamSummaryMap;
		this.allTeamSummary = allTeamSummary;
		this.mrSummaryMap = mrSummaryMap;
	}

	//---------------------
	// Getter
	// --------------------

	/**
	 * 簡易版品目情報を取得する。
	 * 
	 * @return 簡易版品目情報
	 */
	public ProdSummary getProdSummary() {
		return prodSummary;
	}

	/**
	 * 営業所サマリを取得する。
	 * 
	 * @return 営業所サマリ
	 */
	public DeliveryResultSummaryDetailDto getOfficeSummary() {
		return officeSummary;
	}

	/**
	 * チームサマリ(キーはチームの組織コード)マップを取得する。
	 * 
	 * @return チームサマリ(キーはチームの組織コード)マップ
	 */
	public Map<String, DeliveryResultSummaryDetailDto> getTeamSummaryMap() {
		return teamSummaryMap;
	}

	/**
	 * 全チーム合計サマリを取得する。
	 * 
	 * @return 全チーム合計サマリ
	 */
	public DeliveryResultSummaryDetailDto getAllTeamSummary() {
		return allTeamSummary;
	}

	/**
	 * ＭＲサマリ(キーはチームの従業員番号)マップを取得する。
	 * 
	 * @return ＭＲサマリ(キーはチームの従業員番号)マップ
	 */
	public Map<Integer, DeliveryResultSummaryDetailDto> getMrSummaryMap() {
		return mrSummaryMap;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
