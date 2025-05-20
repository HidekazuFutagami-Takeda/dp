package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;

/**
 * 施設品目別計画の検索用DTO
 *
 * @author stakeuchi
 */
public class InsProdPlanScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 品目カテゴリ
	 */
	private final String prodCategory;

	/**
	 * 計画検索範囲
	 */
	private final PlanData planData;

	/**
	 * ワクチンのカテゴリコード
	 */
	private final String vaccineCode;

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param prodCategory 品目カテゴリ
	 * @param planData 計画検索範囲
	 * @param vaccineCode ワクチンのカテゴリコード
	 */
	public InsProdPlanScDto(Integer jgiNo, String insNo, String prodCategory, PlanData planData, String vaccineCode) {
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.prodCategory = prodCategory;
		this.planData = planData;
		this.vaccineCode = vaccineCode;
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
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 計画検索範囲を取得する。
	 *
	 * @return planData 計画検索範囲
	 */
	public PlanData getPlanData() {
		return planData;
	}

	/**
	 * ワクチンのカテゴリコードを取得する。
	 *
	 * @return vaccineCode ワクチンのカテゴリコード
	 */
	public String getVaccineCode() {
		return vaccineCode;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
