package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.Conveyance;

/**
 * データ重複を表す例外クラス
 * 
 * @author tkawabata
 */
public class DuplicateException extends LogicalException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public DuplicateException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public DuplicateException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
