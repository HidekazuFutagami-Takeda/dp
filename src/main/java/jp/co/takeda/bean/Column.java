package jp.co.takeda.bean;

import java.io.Serializable;

/**
 * カラムを表す。
 * 
 * @author tkawabata
 */
public class Column implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルト値
	 */
	private final String value;

	/**
	 * コンストラクタ
	 * 
	 * @param value 値
	 */
	public Column(final String value) {
		this.value = value;
	}

	/**
	 * 値を取得する。
	 * 
	 * @return 値
	 */
	public Object value() {
		return value;
	}

	/**
	 * このオブジェクトの文字列表現を取得する。
	 * 
	 * @return このオブジェクトの文字列表現
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.value;
	}
}
