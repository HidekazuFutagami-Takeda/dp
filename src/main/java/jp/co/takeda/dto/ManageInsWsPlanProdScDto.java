package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;

/**
 * 施設特約店別計画の検索用DTO
 *
 * @author siwamoto
 */
public class ManageInsWsPlanProdScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * カテゴリ
	 */
	private final String prodCategory;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

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
	 * @param sosCd3 従業員番号組織コード(営業所)
	 * @param sosCd4 従業員番号組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param planData 計画検索範囲
	 * @param vaccineCode ワクチンのカテゴリコード
	 */
	public ManageInsWsPlanProdScDto(String sosCd3, String sosCd4, Integer jgiNo, String insNo, String prodCategory, String tmsTytenCd, PlanData planData, String vaccineCode) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.prodCategory = prodCategory;
		this.tmsTytenCd = tmsTytenCd;
		this.planData = planData;
		this.vaccineCode = vaccineCode;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
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
	 * カテゴリを取得する。
	 *
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
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
