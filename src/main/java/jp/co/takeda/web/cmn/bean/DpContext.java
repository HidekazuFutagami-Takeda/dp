package jp.co.takeda.web.cmn.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.a.web.bean.Context;
import jp.co.takeda.security.DpMetaInfo;

/**
 * 納入計画システムのコンテキスト情報
 *
 * @author tkawabata
 */
public class DpContext extends Context {

	/**
	 * コンストラクタ
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	public DpContext(final HttpServletRequest request, final HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * メタ情報
	 */
	private DpMetaInfo dpMetaInfo;

	/**
	 * メタ情報を取得する。
	 *
	 * @return メタ情報
	 */
	public DpMetaInfo getDpMetaInfo() {
		return dpMetaInfo;
	}

	/**
	 * メタ情報を設定する。
	 *
	 * @param dpMetaInfo メタ情報
	 */
	public void setDpMetaInfo(DpMetaInfo dpMetaInfo) {
		this.dpMetaInfo = dpMetaInfo;
	}
}
