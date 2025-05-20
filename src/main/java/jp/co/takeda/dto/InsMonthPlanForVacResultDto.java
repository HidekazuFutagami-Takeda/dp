package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 施設別計画の検索結果を表すDTO
 *
 * @author stakeuchi
 */
public class InsMonthPlanForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細合計行DTO
	 */
	private final InsMonthPlanForVacResultDetailTotalDto detailTotal;

	/**
	 * 明細DTOリスト
	 */
	private final List<InsMonthPlanForVacResultDetailAddrDto> detailList;

	/**
	 * コンストラクタ
	 *
	 * @param detailList 明細DTOリスト
	 */
	public InsMonthPlanForVacResultDto(InsMonthPlanForVacResultDetailTotalDto detailTotal, List<InsMonthPlanForVacResultDetailAddrDto> detailList) {
		this.detailTotal = detailTotal;
		this.detailList = detailList;
	}

	/**
	 * 明細合計行DTOを取得する。
	 *
	 * @return detailTotal 明細合計行DTO
	 */
	public InsMonthPlanForVacResultDetailTotalDto getDetailTotal() {
		return detailTotal;
	}

	/**
	 * 明細DTOリストを取得する。
	 *
	 * @return detailList 明細DTOリスト
	 */
	public List<InsMonthPlanForVacResultDetailAddrDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
