package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.Conveyance;

/**
 * 入力値が不正であることを表す例外クラス
 * 
 * @author tkawabata
 */
public class ValidateException extends LogicalException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public ValidateException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public ValidateException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
