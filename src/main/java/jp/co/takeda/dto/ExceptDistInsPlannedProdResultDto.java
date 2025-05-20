package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 配分除外施設品目の検索結果用DTO
 *
 * @author siwamoto
 */
public class ExceptDistInsPlannedProdResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * カテゴリ
	 */
	private final String category;

	/**
	 * カテゴリ名称
	 */
	private final String categoryName;

	/**
	 * 試算除外フラグ
	 */
	private final String strEstimationFlg;

	/**
	 * 配分除外フラグ
	 */
	private final String strExceptFlg;

	/**
	 * コンストラクタ
	 *
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称
	 * @param category カテゴリ
	 * @param categoryName カテゴリ名称
	 */
	public ExceptDistInsPlannedProdResultDto(String prodCode, String prodName, String category, String categoryName, String strEstimationFlg, String strExceptFlg) {
		super();
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.category = category;
		this.categoryName = categoryName;
		this.strEstimationFlg = strEstimationFlg;
		this.strExceptFlg = strExceptFlg;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return prodName
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリ名称を取得する。
	 *
	 * @return category
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * 試算除外フラグを取得する。
	 *
	 * @return category
	 */
	public String getStrEstimationFlg() {
		return strEstimationFlg;
	}

	/**
	 * 配分除外フラグを取得する。
	 *
	 * @return category
	 */
	public String getStrExceptFlg() {
		return strExceptFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
