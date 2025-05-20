package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 *
 * 過去実績参照画面(担当者別計画)の検索条件(Search Condition)を表すDTO
 *
 * @author siwamoto
 */
public class DeliveryResultMrScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * カテゴリ
	 */
	private final String category;

	/**
	 * コンストラクタ
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 */
	public DeliveryResultMrScDto(String prodCode, String sosCd3, String sosCd4, String category) {
		super();
		this.prodCode = prodCode;
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.category = category;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
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
