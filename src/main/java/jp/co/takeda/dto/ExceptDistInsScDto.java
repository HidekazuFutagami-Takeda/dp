package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 登録済み配分除外施設の検索条件(Search Condition)を表すDTO
 *
 * @author siwamoto
 */
public class ExceptDistInsScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード（営業所）
	 */
	private final String sosCd3;

	/**
	 * 組織コード（チーム）
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * カテゴリ
	 */
	private final String category;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd3 組織コード（営業所）
	 * @param sosCd4 組織コード（チーム）
	 * @param jgiNo 従業員番号
	 * @param category カテゴリ
	 */
	public ExceptDistInsScDto(String sosCd3, String sosCd4, Integer jgiNo, String category) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.category = category;
	}

	/**
	 * 組織コード（営業所）を取得する。
	 *
	 * @return 組織コード（営業所）
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード（チーム）を取得する。
	 *
	 * @return 組織コード（チーム）
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
