package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.Conveyancable;
import jp.co.takeda.a.bean.Conveyance;

import org.springframework.util.Assert;

/**
 * 論理例外を表すクラス<br>
 * 
 * <p>
 * 呼び出し側に例外が起こった旨を通知する場合に投げられる例外クラス<br>
 * 当クラスは<code>Exception</code>を継承しており、呼び出し元は例外に対する 処理を記述する必要がある。<br>
 * 以下に当クラスの作成方法を示す。
 * </p>
 * <fieldset> <legend> 例外の作成方法 </legend> <code>
 * final String ERROR_MSG = "○×処理で×○のため、論理例外が発生";<br>
 * throw new LogicalException(new Conveyance(new MessageKey("hoge.messageKey"), ERROR_MSG));<br>
 * </code> </fieldset> <br>
 * <br>
 * 
 * @author tkawabata
 */
public class LogicalException extends Exception implements Conveyancable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3230677447675069017L;

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
	public LogicalException(final Conveyance conveyance) {
		Assert.notNull(conveyance, ERROR_MSG);
		this.conveyance = conveyance;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param conveyance 伝達オブジェクト
	 * @param e 上位の例外
	 */
	public LogicalException(final Conveyance conveyance, final Exception e) {
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
