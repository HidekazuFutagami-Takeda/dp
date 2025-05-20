package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 品目別計画積上結果の検索用DTO
 *
 * @author stakeuchi
 */
public class ProdPlanSummaryScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目カテゴリ
	 */
	private final String prodCategory;

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCategory 品目カテゴリ
	 */
	public ProdPlanSummaryScDto(Integer jgiNo, String prodCategory) {
		this.jgiNo = jgiNo;
		this.prodCategory = prodCategory;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
