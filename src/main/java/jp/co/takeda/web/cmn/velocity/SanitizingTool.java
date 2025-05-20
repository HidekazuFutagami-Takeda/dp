package jp.co.takeda.web.cmn.velocity;

import java.util.Map.Entry;

import jp.co.takeda.bean.Constants;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * サニタイジングのためのVelocityツール
 * 
 * @author tkawabata
 */
public class SanitizingTool {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(SanitizingTool.class);

	/**
	 * 引数の文字列を無害化して返す。<br>
	 * 変換ルールは{@link Constants#SANITIZING_MAP}を参照。
	 * 
	 * @param target 対象文字列
	 */
	public static String escape(final String target) {

		LOG.debug("********" + target);
		if (StringUtils.isBlank(target)) {
			return null;
		}

		String value = target;
		for (final Entry<String, String> entry : Constants.SANITIZING_MAP.entrySet()) {
			if (StringUtils.contains(value, entry.getKey())) {
				value = StringUtils.replace(value, entry.getKey(), entry.getValue());
			}
		}
		return value;
	}
}
