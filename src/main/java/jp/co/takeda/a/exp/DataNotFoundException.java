package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.Conveyance;

/**
 * データが存在しないことを表す例外クラス
 * 
 * @author tkawabata
 */
public class DataNotFoundException extends LogicalException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 948737681680439208L;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public DataNotFoundException(final Conveyance conveyance) {
		super(conveyance);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public DataNotFoundException(final Conveyance conveyance, final Exception e) {
		super(conveyance, e);
	}
}
