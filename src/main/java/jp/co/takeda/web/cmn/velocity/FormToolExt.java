package jp.co.takeda.web.cmn.velocity;

import javax.servlet.ServletContext;

import jp.co.takeda.a.bean.Box;
import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.bean.Constants.SpringBeanName;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.tools.struts.FormTool;
import org.apache.velocity.tools.view.context.ViewContext;

/**
 * FormToolの拡張クラス
 * 
 * @author tkawabata
 */
public class FormToolExt extends FormTool {

	/**
	 * A reference to the ServletContext.
	 */
	protected ServletContext servletContext;

	/**
	 * Constractor
	 */
	public FormToolExt() {
	}

	/**
	 * Initializes this tool.
	 * 
	 * @param obj the current ViewContext
	 * @throws IllegalArgumentException if the param is not a ViewContext
	 */
	public void init(Object obj) {
		if (!(obj instanceof ViewContext)) {
			throw new IllegalArgumentException("Tool can only be initialized with a ViewContext");
		}

		ViewContext context = (ViewContext) obj;
		this.servletContext = context.getServletContext();
		this.request = context.getRequest();
		this.session = request.getSession(false);
	}

	/**
	 * ActionFormの静的フィールド(staticフィールド)を取得する。
	 * 
	 * @param fieldName フィールド
	 * @return フィールド
	 * @throws Exception
	 */
	public Object getField(String fieldName) throws Exception {
		if (StringUtils.isBlank(fieldName)) {
			return null;
		}
		Object form = this.getBean();
		if (form != null) {
			return form.getClass().getField(fieldName).get(null);
		} else {
			return null;
		}
	}

	/**
	 * リクエスト情報から指定キーのオブジェクトを取得する。
	 * 
	 * @param boxKey ボックスキー
	 * @return リクエスト情報の中のオブジェクト
	 */
	public Object getRequestDataByBoxKey(BoxKey boxKey) throws Exception {
		return this.getBox(SpringBeanName.REQUEST_BOX.value).get(boxKey);
	}

	/**
	 * リクエスト情報から指定キーのオブジェクトを取得する。
	 * 
	 * @param keyName キー名称
	 * @return セッション情報の中のオブジェクト
	 */
	public Object getRequestData(String keyName) throws Exception {
		return this.getRequestDataByBoxKey((BoxKey) this.getField(keyName));
	}

	/**
	 * セッション情報から指定キーのオブジェクトを取得する。
	 * 
	 * @param boxKey ボックスキー
	 * @return リクエスト情報の中のオブジェクト
	 */
	public Object getSessionDataByBoxKey(BoxKey boxKey) throws Exception {
		return this.getBox(SpringBeanName.SESSION_BOX.value).get(boxKey);
	}

	/**
	 * セッション情報から指定キーのオブジェクトを取得する。
	 * 
	 * @param keyName キー名称
	 * @return セッション情報の中のオブジェクト
	 */
	public Object getSessionData(String keyName) throws Exception {
		return this.getSessionDataByBoxKey((BoxKey) this.getField(keyName));
	}

	/**
	 * ダイアログレイアウト名
	 */
	public static final String DIALOGUE_LAYOUT_NAME = "dialogueLayout.vm";

	// ---------------------------------
	// PRIVATE METHOD
	// ---------------------------------
	/**
	 * ボックスオブジェクトを取得する。
	 * 
	 * @param beanId Box
	 * @return Box
	 */
	private Box getBox(String beanId) {
		return SpringUtil.getBean(beanId, this.servletContext);
	}
}
