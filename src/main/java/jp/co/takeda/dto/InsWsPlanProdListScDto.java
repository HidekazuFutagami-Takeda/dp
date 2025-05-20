package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 計画対象品目の検索用DTO
 *
 * @author siwamoto
 */
public class InsWsPlanProdListScDto extends DpDto {

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
	 * 従業員コード
	 */
	private final Integer jgiNo;

	/**
	 * 品目カテゴリ
	 */
	private final String category;

	public InsWsPlanProdListScDto(String sosCd3, String sosCd4, Integer jgiNo, String category) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.category = category;
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
	 * 従業員コードを取得する。
	 *
	 * @return jgiNo 従業員コード
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return category 品目カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
