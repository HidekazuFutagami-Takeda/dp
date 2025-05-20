package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画の検索結果を表すDTO
 * 
 * @author stakeuchi
 */
public class InsPlanForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細DTOリスト
	 */
	private final List<InsPlanForVacResultDetailAddrDto> detailList;

	/**
	 * 表示施設積上
	 */
	private final Long searchPlanTotal;

	/**
	 * コンストラクタ
	 * 
	 * @param detailList 明細DTOリスト
	 */
	public InsPlanForVacResultDto(List<InsPlanForVacResultDetailAddrDto> detailList, Long searchPlanTotal) {
		this.detailList = detailList;
		this.searchPlanTotal = searchPlanTotal;
	}

	/**
	 * 明細DTOリストを取得する。
	 * 
	 * @return detailList 明細DTOリスト
	 */
	public List<InsPlanForVacResultDetailAddrDto> getDetailList() {
		return detailList;
	}

	/**
	 * 表示施設積上を取得する。
	 * 
	 * @return searchPlanTotal 表示施設積上
	 */
	public Long getSearchPlanTotal() {
		return searchPlanTotal;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
