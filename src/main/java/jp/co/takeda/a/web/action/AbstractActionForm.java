package jp.co.takeda.a.web.action;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 抽象アクションフォームクラス
 * 
 * @author tkawabata
 */
public abstract class AbstractActionForm extends ActionForm implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 引数付きのresetメソッドのオーバーライドを無効化する。<br>
	 * 
	 * @param mapping マッピング情報
	 * @param request リクエスト情報
	 */
	@Override
	public final void reset(final ActionMapping mapping, final ServletRequest request) {
		this.reset();
	}

	/**
	 * 引数付きのresetメソッドのオーバーライドを無効化する。<br>
	 * 
	 * @param mapping マッピング情報
	 * @param request リクエスト情報
	 */
	@Override
	public final void reset(final ActionMapping mapping, final HttpServletRequest request) {
		this.reset();
	}

	/**
	 * Validationメソッドを無効化する。<br>
	 * 
	 * @param mapping マッピング情報
	 * @param request リクエスト情報
	 * @return ActionErrors
	 */
	@Override
	public final ActionErrors validate(final ActionMapping mapping, final ServletRequest request) {
		return null;
	}

	/**
	 * Validationメソッドを無効化する。<br>
	 * 
	 * @param mapping マッピング情報
	 * @param request リクエスト情報
	 * @return ActionErrors
	 */
	@Override
	public final ActionErrors validate(final ActionMapping mapping, final HttpServletRequest request) {
		return null;
	}

	/**
	 * プロパティのリセット処理を行う。<br>
	 * 
	 * <pre>
	 * Strutsは以下の順序で処理を実行する。
	 * reset() → populate() → validate()
	 * HTTP-POSTの仕様上、画面のチェックボックスのチェックがはずされている場合、値は送信されない。
	 * よってActionFormクラスをsessionスコープにすると、一度チェックボックスのチェックがON(true)に設定されると
	 * 画面側でチェックをOFF(false)にしても設定が反映されない。
	 * これをOFF(false)にするためには、当関数をオーバーライドし、チェックボックス部分のプロパティ値明示的にOFF(false)にする。
	 * </pre>
	 */
	protected void reset() {
	}
}
