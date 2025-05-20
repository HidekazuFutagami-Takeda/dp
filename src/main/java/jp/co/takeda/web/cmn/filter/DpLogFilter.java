package jp.co.takeda.web.cmn.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.bean.DpLoggingInfo;

/**
 * 納入計画システムのログフィルタークラス<br>
 * Log4Jが出力するログに識別情報を付与するサーブレットフィルター<br>
 * Log4j.xmlに%xと記述することで出力可能。
 * 
 * @author tkawabata
 */
public class DpLogFilter extends AbstractHttpLogFilter {

	/**
	 * ログに出力する文字列を生成する。<br>
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @return ログに出力する文字列
	 */
	@Override
	protected String logging(HttpServletRequest request, HttpServletResponse response) {
		return DpLoggingInfo.LOG_ONLINE_STRING;
	}
}
