package jp.co.takeda.web.cmn.velocity;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * レンダリングを高速化するためのライタークラス
 *
 * @author tkawabata
 */
public class PerformanceWriter extends FilterWriter {

	/**
	 * LOGGER
	 */
	protected static final Log LOG = LogFactory.getLog(PerformanceWriter.class);

	/**
	 * コンストラクタ
	 *
	 * @param writer 大本のライターオブジェクト
	 */
	protected PerformanceWriter(final Writer writer) {
		super(writer);
	}

	@Override
	public void write(final char cbuf[], final int off, final int len) throws IOException {
		if ((cbuf != null) && (cbuf.length > 0)) {
			final String value = filter(new String(cbuf, off, len));
			if (StringUtils.isNotBlank(value)) {
				write(value, 0, value.length());
			}
		} else {
			super.write(cbuf, off, len);
		}
	}

	/**
	 * フィルター対象辞書
	 */
	private static final Map<String, String> REPLACE_MAP;

	// staticイニシャライザ
	static {

		final Map<String, String> map = new LinkedHashMap<String, String>();

		// タブ
		map.put("\t", " ");

		// スペース4回
		map.put("    ", "");

		// 改行2回 ⇒ 改行1回
		map.put("\r\n\r\n", "\r\n");

		// </userdata>
		map.put("</userdata>\r\n", "</userdata>");

		// <row>
		map.put("<row>\r\n", "<row>");

		// </cell>
		map.put("</cell>\r\n", "</cell>");

		// </row>
		map.put("</row>\r\n", "</row>");

		// タグの途中の改行
		map.put("\r\n'", "'");

		// 括弧＋改行
		map.put("{\r\n", "{");

		// 改行＋括弧
		map.put("\r\n}", "}");

		// 括弧＋改行
		map.put("}\r\n", "}");

		// セミコロン＋改行
		map.put(";\r\n", ";");

		// タグ後ろの改行
		map.put("'>\r\n", "'>");

		REPLACE_MAP = Collections.unmodifiableMap(map);
	}

	/**
	 * フィルタ処理を行う。<br>
	 *
	 * @param value フィルタリング対象文字列
	 * @return フィルタリング結果
	 */
	protected static String filter(String value) {
		for (final Entry<String, String> entry : REPLACE_MAP.entrySet()) {
			if (StringUtils.contains(value, entry.getKey())) {
				value = StringUtils.replace(value, entry.getKey(), entry.getValue());
			}
		}
		return value;
	}
}
