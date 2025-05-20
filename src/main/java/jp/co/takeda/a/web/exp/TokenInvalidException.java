package jp.co.takeda.a.web.exp;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;

/**
 * トークンが不正であることをを表す例外クラス
 * 
 * @author shiota
 */
public class TokenInvalidException extends LogicalException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public TokenInvalidException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public TokenInvalidException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
