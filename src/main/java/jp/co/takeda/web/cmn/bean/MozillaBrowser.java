package jp.co.takeda.web.cmn.bean;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * MozillaBrowserクラス
 * 
 * @author tkawabata
 */
public class MozillaBrowser implements Browser {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ブラウザを識別する名称
	 */
	private static final String NETSCAPE_NAVIGATOR_NAME = "Netscape Navigator";

	/**
	 * ブラウザのレスポンス解釈のキャラセット
	 */
	private static final String CHARSET = "ISO-2002-JP";

	/**
	 * 判定文字
	 */
	private static final String DELIM = "%";

	/**
	 * ファイル名部分１
	 */
	private static final String FILE_NAME1 = "; filename*=ISO-2022-JP'ja'";

	/**
	 * ファイル名部分２
	 */
	private static final String FILE_NAME2 = "; filename=";

	public String getBrowserName() {
		return NETSCAPE_NAVIGATOR_NAME;
	}

	public String getContentDisposition(final ContentDispositionMode mode, final String fileName) {

		final String encode = CHARSET;
		try {
			final String str = URLEncoder.encode(fileName, encode);
			String cd;
			if (str.indexOf(DELIM) >= 0) {
				cd = mode.mode + FILE_NAME1 + str;
			} else {
				cd = mode.mode + FILE_NAME2 + str;
			}
			return cd;
		} catch (final UnsupportedEncodingException e) {
			final String errMsg = "サポート外のエンコーディング方式が指定された：encode=";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + encode), e);
		}
	}
}
