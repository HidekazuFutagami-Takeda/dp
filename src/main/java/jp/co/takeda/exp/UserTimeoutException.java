package jp.co.takeda.exp;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * タイムアウトエラーを表す例外クラス
 * 
 * @author tkawabata
 */
public class UserTimeoutException extends SystemException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public UserTimeoutException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public UserTimeoutException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
