package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.Conveyance;

/**
 * アクセス拒否を表す例外クラス
 * 
 * @author tkawabata
 */
public class AccessDeniedException extends SystemException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public AccessDeniedException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public AccessDeniedException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
