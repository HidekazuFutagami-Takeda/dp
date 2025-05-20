package jp.co.takeda.model;

import java.io.Serializable;

/**
 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
 * 都道府県を表すモデルクラス
 *
 * @author yTaniguchi
 */
public class Prefectures implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 都道府県コード
	 */
	private String code;

	/**
	 * 都道府県名
	 */
	private String value;


	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 都道府県コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 都道府県コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 都道府県名を取得する。
	 *
	 * @return 施設コード
	 */
	public String getvalue() {
		return value;
	}

	/**
	 * 都道府県名を設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setvalue(String value) {
		this.value = value;
	}
}
