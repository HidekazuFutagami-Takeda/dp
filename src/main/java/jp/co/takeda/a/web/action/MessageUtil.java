package jp.co.takeda.a.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;

/**
 * メッセージのユーティリティクラス
 *
 * @author tkawabata
 */
public abstract class MessageUtil {

	/**
	 * メッセージをリクエスト情報に設定する。
	 *
	 * @param request リクエスト情報
	 * @param messageKey メッセージキー
	 */
	public static void addMessage(final HttpServletRequest request, final MessageKey messageKey) {
		final ActionMessages actionMessages = getActionMessages(request);
		ActionMessage actionMessage = null;
		String key = messageKey.getKey();
		List<String> values = messageKey.getPlaceFolderList();
		if ((values != null) && (values.size() > 0)) {
			String[] arrays = convertToArray(values);
			actionMessage = new ActionMessage(key, arrays);
		} else {
			actionMessage = new ActionMessage(key);
		}

		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
		request.setAttribute(Globals.MESSAGE_KEY, actionMessages);
	}

	/**
	 * メッセージをリクエスト情報に設定する。
	 *
	 * @param request リクエスト情報
	 * @param conveyance 伝達オブジェクト
	 * @param altKey 代替メッセージキー
	 */
	public static void addMessage(final HttpServletRequest request, final Conveyance conveyance, final MessageKey altKey) {
		List<MessageKey> msgKeyList = conveyance.getMessageKeyList();
		if (msgKeyList != null && msgKeyList.size() > 0) {
			for (MessageKey msgKey : msgKeyList) {
				addMessage(request, msgKey);
			}
		} else {
			if (altKey != null) {
				addMessage(request, altKey);
			}
		}
	}

	/**
	 * エラーメッセージをリクエスト情報に設定する。
	 *
	 * @param request リクエスト情報
	 * @param messageKey メッセージキー
	 */
	public static void addErrorMessage(final HttpServletRequest request, final MessageKey messageKey) {
		final ActionErrors actionErrors = MessageUtil.getActionErrors(request);
		ActionMessage actionMessage = null;
		String key = messageKey.getKey();
		List<String> values = messageKey.getPlaceFolderList();
		if ((values != null) && (values.size() > 0)) {
			String[] arrays = MessageUtil.convertToArray(values);
			actionMessage = new ActionMessage(key, arrays);
		} else {
			actionMessage = new ActionMessage(key);
		}
		actionErrors.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
		request.setAttribute(Globals.ERROR_KEY, actionErrors);
	}

	/**
	 * エラーメッセージをリクエスト情報に設定する。
	 *
	 * @param request リクエスト情報
	 * @param conveyance 伝達オブジェクト
	 * @param altKey 代替メッセージキー
	 */
	public static void addErrorMessage(final HttpServletRequest request, Conveyance conveyance, MessageKey altKey) {
		List<MessageKey> msgKeyList = conveyance.getMessageKeyList();
		if (msgKeyList != null && msgKeyList.size() > 0) {
			for (MessageKey msgKey : msgKeyList) {
				addErrorMessage(request, msgKey);
			}
		} else {
			addErrorMessage(request, altKey);
		}
	}

	/**
	 * メッセージをリクエスト情報から取得する。
	 *
	 * @param request リクエスト情報
	 * @return ActionMessages アクションメッセージリスト
	 */
	private static ActionMessages getActionMessages(final HttpServletRequest request) {
		ActionMessages actionMessages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
		if (actionMessages == null) {
			actionMessages = new ActionMessages();
		}
		return actionMessages;
	}

	/**
	 * エラーメッセージをリクエスト情報から取得する。
	 *
	 * @param request リクエスト情報
	 * @return ActionErrors アクションエラーリスト
	 */
	private static ActionErrors getActionErrors(final HttpServletRequest request) {
		ActionErrors actionErrors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (actionErrors == null) {
			actionErrors = new ActionErrors();
		}
		return actionErrors;
	}

	/**
	 * 引数のリスト(文字列型)を配列(文字列型)に変換する。
	 *
	 * @param list 変換対象のリスト
	 * @return Array(String[])
	 */
	private static String[] convertToArray(List<String> list) {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}


	/**
	 * メッセージキーからメッセージ文字列を取得する
	 * @param resources
	 * @param messageKey
	 * @return メッセージ文字列
	 */
	public static String getMessageString(MessageResources resources, MessageKey messageKey) {
		String key = messageKey.getKey();
		List<String> values = messageKey.getPlaceFolderList();
		if ((values != null) && (values.size() > 0)) {
			String[] arrays = MessageUtil.convertToArray(values);
			return resources.getMessage(key, arrays);
		} else {
			return resources.getMessage(key);
		}
	}

}
