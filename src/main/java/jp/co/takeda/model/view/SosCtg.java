package jp.co.takeda.model.view;

import java.io.Serializable;

/**
 * 参照用の組織カテゴリを表すクラス
 *
 * @author rna8405
 *
 */
public class SosCtg implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * カテゴリコード
	 */
	private String category;

	/**
	 * @return sosCd
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param sosCd セットする sosCd
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * @param category セットする category
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}
