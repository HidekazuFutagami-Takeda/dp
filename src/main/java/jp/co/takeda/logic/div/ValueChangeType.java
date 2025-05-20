package jp.co.takeda.logic.div;

/**
 * 計画値の変換区分を表す列挙
 * 
 * @author khashimoto
 */
public enum ValueChangeType {

	/**
	 * Y価(またはB価)→T価変換
	 */
	TO_T("1"),

	/**
	 * T価→Y価(またはB価)変換
	 */
	FROM_T("2");

	/**
	 * コード値
	 */
	private String code;

	/**
	 * コンストラクタ
	 * 
	 * @param value 処理名
	 */
	private ValueChangeType(String code) {
		this.code = code;
	}

	/**
	 * 処理名を取得する。
	 * 
	 * @return 処理名
	 */
	public String getCode() {
		return code;
	}
}
