package jp.co.takeda.exp;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * ステータスエラーを表す例外クラス
 * 
 * @author nozaki
 */
public class UnallowableStatusException extends SystemException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public UnallowableStatusException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public UnallowableStatusException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
