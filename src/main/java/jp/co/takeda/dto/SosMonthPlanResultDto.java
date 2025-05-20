package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 組織別月別計画の検索結果DTO
 *
 * @author rna8405
 */
public class SosMonthPlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細合計行DTO
	 */
	private final SosMonthPlanResultDetailTotalDto detailTotal;

	/**
	 * 明細DTOリスト
	 */
	private final List<SosMonthPlanResultDetailDto> detailList;

	/**
	 * コンストラクタ
	 *
	 * @param detailTotal 明細合計DTO
	 * @param detailList 明細DTOリスト
	 */
	public SosMonthPlanResultDto(SosMonthPlanResultDetailTotalDto detailTotal,
			List<SosMonthPlanResultDetailDto> detailList) {
		this.detailTotal = detailTotal;
		this.detailList = detailList;
	}

	/**
	 * 明細合計DTOを取得する。
	 *
	 * @return detailList 明細合計リスト
	 */
	public SosMonthPlanResultDetailTotalDto getDetailTotal() {
		return detailTotal;
	}

	/**
	 * 明細DTOリストを取得する。
	 *
	 * @return detailList 明細DTOリスト
	 */
	public List<SosMonthPlanResultDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
