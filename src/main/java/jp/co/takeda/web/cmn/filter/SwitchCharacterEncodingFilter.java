package jp.co.takeda.web.cmn.filter;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;

/**
 * エンコーディング方式を切り替えられるエンコードフィルタ
 * 
 * @author tkawabata
 */
public class SwitchCharacterEncodingFilter extends FilterTemplete {

	/**
	 * エンコード方式
	 */
	private String encoding;

	/**
	 * エンコード方式(切り替え用)
	 */
	private String switchEncoding;

	/**
	 * 初期化処理
	 * 
	 * @param filterConfig FilterConfig
	 * @throws ServletException サーブレット例外
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		this.encoding = filterConfig.getInitParameter("encoding");
		this.switchEncoding = filterConfig.getInitParameter("switchEncoding");
	}

	/**
	 * クエストパラメータ"sFlg"に"true"が指定されていた場合、指定のエンコーディング方式でエンコードする。
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setLocale(Locale.JAPANESE);
			boolean switchFlg = isSwitch(request);
			if (this.switchEncoding != null && switchFlg) {
				request.setCharacterEncoding(this.switchEncoding);
				response.setCharacterEncoding(this.switchEncoding);
			} else if (this.encoding != null) {
				request.setCharacterEncoding(this.encoding);
				response.setCharacterEncoding(this.encoding);
			}
		} catch (UnsupportedEncodingException e) {
			final String errMsg = "UnsupportedEncodingException";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg), e);
		}
	}

	/**
	 * 終了処理
	 */
	public void destroy() {
		super.destroy();
		this.encoding = null;
		this.switchEncoding = null;
	}

	/**
	 * エンコード方式を切り替えるかのフラグを取得する。
	 * 
	 * @param request リクエスト情報
	 * @return エンコード方式を切り替えるかのフラグ
	 */
	public static boolean isSwitch(HttpServletRequest request) {
		String quertyString = request.getQueryString();
		if (quertyString != null && quertyString.contains("sFlg=true")) {
			return true;
		} else {
			return false;
		}
	}
}
