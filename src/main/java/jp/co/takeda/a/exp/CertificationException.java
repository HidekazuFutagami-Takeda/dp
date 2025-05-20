package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.Conveyance;

/**
 * 認証失敗を表す例外クラス
 * 
 * @author tkawabata
 */
public class CertificationException extends SystemException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public CertificationException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public CertificationException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
