package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 品目別月別計画の検索結果DTO
 *
 * @author stakeuchi
 */
public class ProdMonthPlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目分類名
	 */
	private final String prodCategory;

	/**
	 * 計画集計
	 */
	private final ProdMonthPlanResultDetailDto planSum;

	/**
	 * 明細DTOリスト
	 */
	private final List<ProdMonthPlanResultDetailDto> detailList;

	/**
	 * コンストラクタ
	 *
	 * @param prodCategory 品目分類名
	 * @param planSum 計画集計
	 * @param detailList 明細DTOリスト
	 */
	public ProdMonthPlanResultDto(String prodCategory, ProdMonthPlanResultDetailDto planSum,
			List<ProdMonthPlanResultDetailDto> detailList) {
		this.prodCategory = prodCategory;
		this.planSum = planSum;
		this.detailList = detailList;
	}

	/**
	 * 品目分類名を取得する
	 *
	 * @return prodCategory 品目分類名
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 計画集計を取得する
	 *
	 * @return planSum 計画集計
	 */
	public ProdMonthPlanResultDetailDto getPlanSum() {
		return planSum;
	}

	/**
	 * 明細DTOリストを取得する。
	 *
	 * @return detailList 明細DTOリスト
	 */
	public List<ProdMonthPlanResultDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
