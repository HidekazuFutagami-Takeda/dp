package jp.co.takeda.a.web.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * コンテキストを表すクラス
 * 
 * @author tkawabata
 */
public class Context {

	/**
	 * リクエスト情報
	 */
	private final HttpServletRequest request;

	/**
	 * レスポンス情報
	 */
	private final HttpServletResponse response;

	/**
	 * コンストラクタ
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	public Context(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * リクエスト情報を取得する。
	 * 
	 * @return リクエスト情報
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * レスポンス情報を取得する。
	 * 
	 * @return レスポンス情報
	 */
	public HttpServletResponse getResponse() {
		return response;
	}
}
