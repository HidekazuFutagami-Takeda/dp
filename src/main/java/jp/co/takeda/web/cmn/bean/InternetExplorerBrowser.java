package jp.co.takeda.web.cmn.bean;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.UnsupportedEncodingException;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * InternetExplorerを表すBrowserクラス
 *
 * @author tkawabata
 */
public class InternetExplorerBrowser implements Browser {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ブラウザを識別する名称
	 */
	private static final String INTERNET_EXPLORER_NAME = "Internet Explorer";

	/**
	 * ブラウザのレスポンス解釈のキャラセット
	 */
	private static final String CHARSET = "UTF8";

	public String getBrowserName() {
		return INTERNET_EXPLORER_NAME;
	}

	public String getContentDisposition(final ContentDispositionMode mode, final String fileName) {

		final String encode = CHARSET;
		final StringBuilder convertFileName = new StringBuilder();
		byte[] bytes;
		try {
			bytes = fileName.getBytes(encode);
		} catch (final UnsupportedEncodingException e) {
			final String errMsg = "サポート外のエンコーディング方式が指定された：encode=";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + encode), e);
		}

		for (int i = 0; i < bytes.length; i++) {
			convertFileName.append((char) bytes[i]);
		}
		return mode.mode + "; filename=" + convertFileName.toString();
	}
}
