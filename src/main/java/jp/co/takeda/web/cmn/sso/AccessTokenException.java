package jp.co.takeda.web.cmn.sso;

/**
 * アクセストークン異常を示す例外クラス
 *
 * @author nakashima
 */
public class AccessTokenException extends Exception{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 *
	 * @param message エラー内容を示す文字列
	 */
	public AccessTokenException(final String message) {
		super(message);
	}
}



