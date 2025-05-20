package jp.co.takeda.a.web.action;

import org.apache.struts.action.ActionMapping;

/**
 * 拡張マッピングクラス<br>
 * 
 * <pre>
 * Struts標準のActionMappingクラスに以下のプロパティを追加する。
 * <li>アクションメソッド(名)</li>
 * <li>バリデーションメソッド(名)</li>
 * <li>トークンを生成するかを示すフラグ</li>
 * <li>トークンチェックを行うかを示すフラグ</li>
 * </pre>
 * 
 * @author shiota
 */
public class ActionMappingExt extends ActionMapping {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * アクションメソッド(名)
	 */
	private String actionMethod;

	/**
	 * バリデーションメソッド(名)
	 */
	private String validationMethod;

	/**
	 * トークンを生成するかを示すフラグ
	 */
	private boolean generateToken;

	/**
	 * トークンチェックを行うかを示すフラグ
	 */
	private boolean checkToken;

	/**
	 * 設定ファイルをオープンする。
	 */
	public void openConfigured() {
		configured = false;
	}

	/**
	 * トークンを生成するかを示すフラグを取得する。
	 * 
	 * @return トークンを生成するかを示すフラグ
	 */
	public boolean isGenerateToken() {
		return generateToken;
	}

	/**
	 * トークンを生成するかを示すフラグを設定する。
	 * 
	 * @param generateToken トークンを生成するかを示すフラグ
	 */
	public void setGenerateToken(final boolean generateToken) {
		this.generateToken = generateToken;
	}

	/**
	 * トークンチェックを行うかを示すフラグを取得する。
	 * 
	 * @return トークンチェックを行うかを示すフラグ
	 */
	public boolean isCheckToken() {
		return checkToken;
	}

	/**
	 * トークンチェックを行うかを示すフラグを設定する。
	 * 
	 * @param checkToken トークンチェックを行うかを示すフラグ
	 */
	public void setCheckToken(final boolean checkToken) {
		this.checkToken = checkToken;
	}

	/**
	 * アクションメソッド(名)を取得する。
	 * 
	 * @return アクションメソッド(名)
	 */
	public String getActionMethod() {
		return actionMethod;
	}

	/**
	 * アクションメソッド(名)を設定する。
	 * 
	 * @param actionMethod アクションメソッド(名)
	 */
	public void setActionMethod(final String actionMethod) {
		this.actionMethod = actionMethod;
	}

	/**
	 * バリデーションメソッド(名)を取得する。
	 * 
	 * @return バリデーションメソッド(名)
	 */
	public String getValidationMethod() {
		return validationMethod;
	}

	/**
	 * バリデーションメソッド(名)を設定する。
	 * 
	 * @param validationMethod バリデーションメソッド(名)
	 */
	public void setValidationMethod(final String validationMethod) {
		this.validationMethod = validationMethod;
	}
}
