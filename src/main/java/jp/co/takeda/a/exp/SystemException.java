package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.Conveyancable;
import jp.co.takeda.a.bean.Conveyance;

import org.springframework.util.Assert;

/**
 * システム例外を表すクラス<br>
 * 
 * <p>
 * システムとして復旧出来ない場合に投げられる例外クラス<br>
 * 当クラスは<code>RuntimeException</code>を継承しており、呼び出し元は例外に対する 処理を記述する必要がない。<br>
 * 以下に当クラスの作成方法を示す。
 * </p>
 * <fieldset> <legend> 例外の作成方法 </legend> <code>
 * final String ERROR_MSG = "○×処理で×○のため、システム例外が発生";<br>
 * throw new SystemException(new Conveyance(new MessageKey("hoge.messageKey"), ERROR_MSG));<br>
 * </code> </fieldset> <br>
 * <p>
 * なお汎用的なMessageKeyは、{@link jp.co.takeda.a.exp.ErrMessageKey}に宣言されている。
 * </p>
 * <fieldset> <legend> ErrMessageKeyの活用 </legend> <code>
 * final String ERROR_MSG = "引数が不正なため、システム例外が発生";<br>
 * throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, ERROR_MSG));<br>
 * </code> </fieldset> <br>
 * <br>
 * 
 * @see jp.co.takeda.a.exp.ErrMessageKey
 * @author tkawabata
 */
public class SystemException extends RuntimeException implements Conveyancable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8615068270283921339L;

	/**
	 * 伝達オブジェクトがないことを示すエラーメッセージ
	 */
	protected static final String ERROR_MSG = "伝達オブジェクトが指定されていない";

	/**
	 * 伝達オブジェクト
	 */
	private final Conveyance conveyance;

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 */
	public SystemException(final Conveyance conveyance) {
		Assert.notNull(conveyance, ERROR_MSG);
		this.conveyance = conveyance;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public SystemException(final Conveyance conveyance, final Exception e) {
		super(e);
		Assert.notNull(conveyance, ERROR_MSG);
		this.conveyance = conveyance;
	}

	/**
	 * 伝達オブジェクトを取得する。
	 * 
	 * @return 伝達オブジェクト
	 * @see jp.co.takeda.a.bean.Conveyancable#getConveyance()
	 */
	public Conveyance getConveyance() {
		return conveyance;
	}
}
