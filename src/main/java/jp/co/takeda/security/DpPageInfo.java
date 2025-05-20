package jp.co.takeda.security;

import java.io.Serializable;

/**
 * 画面情報を表すクラス
 *
 * @author tkawabata
 */
public class DpPageInfo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 *
	 * @param height 高さ
	 */
	public DpPageInfo(String height) {
		this.height = height;
	}

	/**
	 * 画面高
	 */
	private final String height;

	/**
	 * 画面高を取得する。
	 *
	 * @return 画面高
	 */
	public String getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "height:" + height;
	}
}
