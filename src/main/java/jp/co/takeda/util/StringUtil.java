package jp.co.takeda.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文字列操作関連のユーティリティクラス
 * 
 * @author tkawabata
 */
public abstract class StringUtil {

	/**
	 * ロガー
	 */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(StringUtil.class);

	/**
	 * 全角文字リスト
	 */
	public static final String ZENKAKU_LIST = "！”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨ" + "ＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［￥" + "］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝￣。「」、・ァィゥェォャュョッーアイエオナニヌネノ"
		+ "マミムメモヤユヨラリルレロン゛゜　";

	/**
	 * 全角カナリスト(カ、サ、タ、ハ)行とウ
	 */
	public static final String ZENKAKU_KASATAHA_LIST = "カキクケコサシスセソタチツテトハヒフヘホウ";

	/**
	 * 全角カナリスト(ガ、ザ、ダ、バ)行とヴ
	 */
	public static final String ZENKAKU_GAZADABA_LIST = "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ";

	/**
	 * 全角カナ(ワ"[&yen;30f7])
	 */
	public static final Character ZENKAKU_WA_DAKUTEN = Character.valueOf('\u30f7');

	/**
	 * 全角カナ(ヲ"[&yen;30fa])
	 */
	public static final Character ZENKAKU_WO_DAKUTEN = Character.valueOf('\u30fa');

	/**
	 * 全角カナリスト(パ)行
	 */
	public static final String ZENKAKU_PA_LIST = "パピプペポ";

	/**
	 * 半角文字リスト
	 */
	public static final String HANKAKU_LIST = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGH" + "IJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnop" + "qrstuvwxyz{|}~｡｢｣､･ｧｨｩｪｫｬｭｮｯｰｱｲｴｵﾅﾆﾇﾈﾉ"
		+ "ﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝﾞﾟ ";

	/**
	 * 半角カナリスト(ｶ､ｻ､ﾀ､ﾊ)行とｳ
	 */
	public static final String HANKAKU_KASATAHA_LIST = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ";

	/**
	 * 半角カナリスト(ﾊ)行
	 */
	public static final String HANKAKU_HA_LIST = "ﾊﾋﾌﾍﾎ";

	/**
	 * LIKE述語のパターン文字列で用いるエスケープ文字
	 */
	public static final String LIKE_ESC_CHAR = "~";

	/**
	 * ORACLE_TEXT述語のパターン文字列で用いるエスケープ文字
	 */
	public static final String ORACLE_TEXT_ESC_CHAR = "}";

	/**
	 * 半角データ
	 */
	public static final List<Character> HANKAKU_DATA_LIST;

	static {
		List<Character> tmpList = new ArrayList<Character>();
		// 小文字英字の生成
		tmpList.add(Character.valueOf('a'));
		tmpList.add(Character.valueOf('b'));
		tmpList.add(Character.valueOf('c'));
		tmpList.add(Character.valueOf('d'));
		tmpList.add(Character.valueOf('e'));
		tmpList.add(Character.valueOf('f'));
		tmpList.add(Character.valueOf('g'));
		tmpList.add(Character.valueOf('h'));
		tmpList.add(Character.valueOf('i'));
		tmpList.add(Character.valueOf('j'));
		tmpList.add(Character.valueOf('k'));
		tmpList.add(Character.valueOf('l'));
		tmpList.add(Character.valueOf('m'));
		tmpList.add(Character.valueOf('n'));
		tmpList.add(Character.valueOf('o'));
		tmpList.add(Character.valueOf('p'));
		tmpList.add(Character.valueOf('q'));
		tmpList.add(Character.valueOf('r'));
		tmpList.add(Character.valueOf('s'));
		tmpList.add(Character.valueOf('t'));
		tmpList.add(Character.valueOf('u'));
		tmpList.add(Character.valueOf('v'));
		tmpList.add(Character.valueOf('w'));
		tmpList.add(Character.valueOf('x'));
		tmpList.add(Character.valueOf('y'));
		tmpList.add(Character.valueOf('z'));

		// 大文字英字の生成
		tmpList.add(Character.valueOf('A'));
		tmpList.add(Character.valueOf('B'));
		tmpList.add(Character.valueOf('C'));
		tmpList.add(Character.valueOf('D'));
		tmpList.add(Character.valueOf('E'));
		tmpList.add(Character.valueOf('F'));
		tmpList.add(Character.valueOf('G'));
		tmpList.add(Character.valueOf('H'));
		tmpList.add(Character.valueOf('I'));
		tmpList.add(Character.valueOf('J'));
		tmpList.add(Character.valueOf('K'));
		tmpList.add(Character.valueOf('L'));
		tmpList.add(Character.valueOf('M'));
		tmpList.add(Character.valueOf('N'));
		tmpList.add(Character.valueOf('O'));
		tmpList.add(Character.valueOf('P'));
		tmpList.add(Character.valueOf('Q'));
		tmpList.add(Character.valueOf('R'));
		tmpList.add(Character.valueOf('S'));
		tmpList.add(Character.valueOf('T'));
		tmpList.add(Character.valueOf('U'));
		tmpList.add(Character.valueOf('V'));
		tmpList.add(Character.valueOf('W'));
		tmpList.add(Character.valueOf('X'));
		tmpList.add(Character.valueOf('Y'));
		tmpList.add(Character.valueOf('Z'));

		// 半角数字の生成
		tmpList.add(Character.valueOf('1'));
		tmpList.add(Character.valueOf('2'));
		tmpList.add(Character.valueOf('3'));
		tmpList.add(Character.valueOf('4'));
		tmpList.add(Character.valueOf('5'));
		tmpList.add(Character.valueOf('6'));
		tmpList.add(Character.valueOf('7'));
		tmpList.add(Character.valueOf('8'));
		tmpList.add(Character.valueOf('9'));
		tmpList.add(Character.valueOf('0'));

		// その他半角文字の生成
		tmpList.add(Character.valueOf('!'));
		tmpList.add(Character.valueOf('"'));
		tmpList.add(Character.valueOf('#'));
		tmpList.add(Character.valueOf('$'));
		tmpList.add(Character.valueOf('%'));
		tmpList.add(Character.valueOf('&'));
		tmpList.add(Character.valueOf('\''));
		tmpList.add(Character.valueOf('('));
		tmpList.add(Character.valueOf(')'));
		tmpList.add(Character.valueOf('*'));
		tmpList.add(Character.valueOf('+'));
		tmpList.add(Character.valueOf('.'));
		tmpList.add(Character.valueOf('-'));
		tmpList.add(Character.valueOf('/'));
		tmpList.add(Character.valueOf(':'));
		tmpList.add(Character.valueOf(';'));
		tmpList.add(Character.valueOf('<'));
		tmpList.add(Character.valueOf('>'));
		tmpList.add(Character.valueOf('='));
		tmpList.add(Character.valueOf('?'));
		tmpList.add(Character.valueOf('@'));
		tmpList.add(Character.valueOf('['));
		tmpList.add(Character.valueOf(']'));
		tmpList.add(Character.valueOf('\\'));
		tmpList.add(Character.valueOf('^'));
		tmpList.add(Character.valueOf('_'));
		tmpList.add(Character.valueOf('`'));
		tmpList.add(Character.valueOf('{'));
		tmpList.add(Character.valueOf('}'));
		tmpList.add(Character.valueOf('|'));
		tmpList.add(Character.valueOf('~'));

		HANKAKU_DATA_LIST = Collections.unmodifiableList(tmpList);
	}

	/**
	 * 文字列の右側のホワイトスペースを削除する。引数が null のときは null を返す。<br>
	 * 
	 * <pre>
	 * 例えば Oracle の場合、 CHAR 型の列の値を ResultSet.getString() で取得すると、
	 * 定義長までスペースで パディングされた文字列が返される。
	 * 一方、 VARCHAR2 の場合は右端のスペースはトリミングされるため、
	 * そのままでは両者を正しく文字列 比較することが出来ない。
	 * また、画面入力された文字列の右端にスペースが 含まれている場合に、
	 * これを VARCHAR2 の列に挿入するとスペースもそのまま格納されるが、
	 * 右端のスペースをトリミングする動作が 妥当な場合も多い。このようなときにこのメソッドを利用する。
	 * ※ 全角スペースはトリミングされない。
	 * </pre>
	 * 
	 * @param str 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String rtrim(final String str) {
		if (str == null) {
			return null;
		}

		int length = str.length();
		while ((0 < length) && ValidationUtil.isWhitespace(str.charAt(length - 1))) {
			length--;
		}

		return length < str.length() ? str.substring(0, length) : str;
	}

	/**
	 * 文字列の左側のホワイトスペースを削除する。<br>
	 * 
	 * <pre>
	 * 引数が null のときは null を返す。
	 * 全角スペースはトリミングされない。
	 * </pre>
	 * 
	 * @param str 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String ltrim(final String str) {
		if (str == null) {
			return null;
		}

		int start = 0;
		final int length = str.length();
		while ((start < length) && ValidationUtil.isWhitespace(str.charAt(start))) {
			start++;
		}

		return start > 0 ? str.substring(start, length) : str;
	}

	/**
	 * 文字列の両側のホワイトスペースを削除する。<br>
	 * 
	 * <pre>
	 * 引数が null のときは null を返す。
	 * ※ 全角スペースはトリミングされない。
	 * </pre>
	 * 
	 * @param str 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String trim(final String str) {
		return StringUtils.trim(str);
	}

	/**
	 * 文字列の右側の全角および半角スペースを削除する。引数が null のときは null を返す。<br>
	 * 
	 * <pre>
	 * 例えば Oracle の場合、 CHAR 型の列の値を ResultSet.getString() で取得すると、
	 * 定義長までスペースで パディングされた文字列が返される。
	 * 一方、 VARCHAR2 の場合は右端のスペースはトリミングされるため、
	 * そのままでは両者を正しく文字列 比較することが出来ない。
	 * また、画面入力された文字列の右端にスペースが 含まれている場合に、これを VARCHAR2 の列に挿入すると
	 * スペースもそのまま格納されるが、右端のスペースをトリミングする動作が 妥当な場合も多い。
	 * このようなときにこのメソッドを利用する。
	 * </pre>
	 * 
	 * @param str 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String rtrimZ(final String str) {
		if (str == null) {
			return null;
		}

		int length = str.length();
		while ((0 < length) && ValidationUtil.isZenHankakuSpace(str.charAt(length - 1))) {
			length--;
		}

		return length < str.length() ? str.substring(0, length) : str;
	}

	/**
	 * 文字列の左側の全角および半角スペースを削除する。<br>
	 * 引数が null のときは null を返す。
	 * 
	 * @param str 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String ltrimZ(final String str) {
		if (str == null) {
			return null;
		}

		int start = 0;
		final int length = str.length();
		while ((start < length) && ValidationUtil.isZenHankakuSpace(str.charAt(start))) {
			start++;
		}

		return start > 0 ? str.substring(start, length) : str;
	}

	/**
	 * 文字列の両側の全角および半角スペースを削除する。<br>
	 * 引数が null のときは null を返す。
	 * 
	 * @param str 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String trimZ(final String str) {
		return ltrimZ(rtrimZ(str));
	}

	/**
	 * 指定されたクラス名から短縮クラス名(パッケージ修飾なし)を取得する。<br>
	 * 
	 * @param longClassName クラス名
	 * @return 短縮クラス名
	 */
	public static String toShortClassName(final String longClassName) {
		return ClassUtils.getShortClassName(longClassName);
	}

	/**
	 * 指定された文字列から末尾の拡張子を取得する。<br>
	 * 拡張子がない場合は空文字列を返す。
	 * 
	 * @param name 拡張子つきの名前
	 * @return 拡張子
	 */
	public static String getExtension(final String name) {
		final int index = name.lastIndexOf('.');
		return (index < 0) ? "" : name.substring(index);
	}

	/**
	 * 半角文字列を全角文字列に変換する。<br>
	 * 
	 * <pre>
	 * カナ文字に濁点または半濁点が続く場合は、可能な限り１文字に変換する。
	 * (例) 'ｶ' + 'ﾞ' =&gt; 'ガ'
	 * またこの変換処理では以下の全角文字も変換先文字とされる。
	 *
	 * <ul>
	 * <li>「ヴ」</li>
	 * <li>「ワ"」('ワ'に濁点：&yen;u30f7)</li>
	 * <li>「ヲ"」('ヲ'に濁点：&yen;30fa)</li>
	 * </ul>
	 * </pre>
	 * 
	 * @param value 半角文字列
	 * @return 全角文字列
	 */
	public static String hankakuToZenkaku(final String value) {

		if ((value == null) || "".equals(value)) {
			return value;
		}

		final char[] chars = value.toCharArray();
		final StringBuilder returnValue = new StringBuilder();
		String getValue = null;
		Character nextvalue = null;

		for (int i = 0; i < chars.length; i++) {

			getValue = getZenkakuMoji(chars[i]);

			if (getValue != null) {
				returnValue.append(getValue);
			} else if (i == (chars.length - 1)) {
				// 最後の文字
				getValue = getZenkakuKasatahaMoji(chars[i]);
				if (getValue != null) {
					// ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
					returnValue.append(getValue);
				} else if (Character.valueOf(chars[i]).equals(Character.valueOf('ﾜ'))) {
					returnValue.append("ワ");
				} else if (Character.valueOf(chars[i]).equals(Character.valueOf('ｦ'))) {
					returnValue.append("ヲ");
				} else {
					returnValue.append(String.valueOf(chars[i]));
				}
			} else {
				nextvalue = Character.valueOf(chars[i + 1]);
				if (nextvalue.equals(Character.valueOf('ﾞ'))) {
					getValue = getZenkakuDakuMoji(chars[i]);
					if (getValue != null) {
						// ｶﾞｷﾞｸﾞｹﾞｺﾞｻﾞｼﾞｽﾞｾﾞｿﾞﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞｳﾞ
						returnValue.append(getValue);
						i++;
					} else if (Character.valueOf(chars[i]).equals(Character.valueOf('ﾜ'))) {
						// ﾜﾞ
						returnValue.append(ZENKAKU_WA_DAKUTEN);
						i++;
					} else if (Character.valueOf(chars[i]).equals(Character.valueOf('ｦ'))) {
						// ｦﾞ
						returnValue.append(ZENKAKU_WO_DAKUTEN);
						i++;
					} else {
						returnValue.append((String.valueOf(chars[i]) + "゛"));
						i++;
					}
				} else if (nextvalue.equals(Character.valueOf('ﾟ'))) {
					getValue = getZenkakuHandakuMoji(chars[i]);
					if (getValue != null) {
						// ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ
						returnValue.append(getValue);
						i++;
					} else {
						// ｶﾟｷﾟｸﾟｹﾟｺﾟｻﾟｼﾟｽﾟｾﾟｿﾟﾀﾟﾁﾟﾂﾟﾃﾟﾄﾟｳﾟ
						getValue = getZenkakuKasatahaMoji(chars[i]);
						returnValue.append((String.valueOf(getValue) + "゜"));
						i++;
					}
				} else {
					getValue = getZenkakuKasatahaMoji(chars[i]);
					if (getValue != null) {
						// ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
						returnValue.append(getValue);
					} else if (Character.valueOf(chars[i]).equals(Character.valueOf('ﾜ'))) {
						returnValue.append("ワ");
					} else if (Character.valueOf(chars[i]).equals(Character.valueOf('ｦ'))) {
						returnValue.append("ヲ");
					} else {
						returnValue.append(String.valueOf(chars[i]));
					}
				}
			}
		}
		return returnValue.toString();
	}

	/**
	 * 全角文字列を半角文字列に変換する。<br>
	 * 
	 * <pre>
	 * 濁点または半濁点を持つカナ文字は、２文字に分解される。
	 * (例) 'ガ' =&gt; 'ｶ' + 'ﾞ'
	 * またこの変換処理では以下の全角文字も変換元文字と受け付ける。
	 * <ul>
	 * <li>「ヴ」</li>
	 * <li>「ワ"」('ワ'に濁点：&yen;u30f7)</li>
	 * <li>「ヲ"」('ヲ'に濁点：&yen;30fa)</li>
	 * </ul>
	 * </pre>
	 * 
	 * @param value 全角文字列
	 * @return 半角文字列
	 */
	public static String zenkakuToHankaku(final String value) {

		if ((value == null) || "".equals(value)) {
			return value;
		}

		final char[] chars = value.toCharArray();
		final StringBuilder returnValue = new StringBuilder();
		String getValue = null;

		for (int i = 0; i < chars.length; i++) {

			getValue = getHankakuMoji(chars[i]);

			if (getValue != null) {
				returnValue.append(getValue);
			} else {
				returnValue.append(String.valueOf(chars[i]));
			}
		}
		return returnValue.toString();
	}

	/**
	 * 半角文字を全角文字に変換する。<br>
	 * 全角文字リストの変換処理を行う。
	 * 
	 * @param c 半角文字
	 * @return 全角文字
	 */
	private static String getZenkakuMoji(final char c) {

		final int index = HANKAKU_LIST.indexOf(c);

		if (index >= 0) {
			return String.valueOf(ZENKAKU_LIST.charAt(index));
		}
		return null;
	}

	/**
	 * 半角文字を全角文字に変換する。<br>
	 * 全角カナ(ガ、ザ、ダ、バ)行とヴの変換処理を行う。
	 * 
	 * @param c 半角文字
	 * @return 全角文字
	 */
	private static String getZenkakuDakuMoji(final char c) {

		final int index = HANKAKU_KASATAHA_LIST.indexOf(c);
		if (index >= 0) {
			return String.valueOf(ZENKAKU_GAZADABA_LIST.charAt(index));
		}
		return null;
	}

	/**
	 * 半角文字を全角文字に変換する。<br>
	 * 全角カナ(パ)行の変換処理を行う。
	 * 
	 * @param c 半角文字
	 * @return 全角文字
	 */
	private static String getZenkakuHandakuMoji(final char c) {

		final int index = HANKAKU_HA_LIST.indexOf(c);
		if (index >= 0) {
			return String.valueOf(ZENKAKU_PA_LIST.charAt(index));
		}
		return null;
	}

	/**
	 * 半角文字を全角文字に変換する。<br>
	 * 全角カナ(カ、サ、タ、ハ)行とウの変換処理を行う。
	 * 
	 * @param c 半角文字
	 * @return 全角文字
	 */
	private static String getZenkakuKasatahaMoji(final char c) {

		final int index = HANKAKU_KASATAHA_LIST.indexOf(c);
		if (index >= 0) {
			return String.valueOf(ZENKAKU_KASATAHA_LIST.charAt(index));
		}
		return null;
	}

	/**
	 * 全角文字を半角文字に変換する。<br>
	 * 
	 * <pre>
	 * このメソッドでは以下の文字を対象とした変換処理を行う。
	 * <ul>
	 * <li>半角文字リスト</li>
	 * <li>半角カナ(ｶ､ｻ､ﾀ､ﾊ)行とｳ</li>
	 * <li>半角カナ(ｶﾞ､ｻﾞ､ﾀﾞ､ﾊﾞ)行とｳﾞ</li>
	 * <li>半角カナ(ﾊﾟ)行</li>
	 * <li>半角カナ(ﾜﾞ､ｦﾞ)</li>
	 * </ul>
	 * </pre>
	 * 
	 * @param c 全角文字
	 * @return 半角文字
	 */
	private static String getHankakuMoji(final char c) {

		int index = 0;
		String value = null;

		index = ZENKAKU_LIST.indexOf(c);
		if (index >= 0) {
			return String.valueOf(HANKAKU_LIST.charAt(index));
		}

		index = ZENKAKU_KASATAHA_LIST.indexOf(c);
		if (index >= 0) {
			// カキクケコサシスセソタチツテトハヒフヘホウ
			return String.valueOf(HANKAKU_KASATAHA_LIST.charAt(index));
		}

		index = ZENKAKU_GAZADABA_LIST.indexOf(c);
		if (index >= 0) {
			// ガギグゲゴザジズゼゾ"ダヂヅデドバビブベボヴ
			value = String.valueOf(HANKAKU_KASATAHA_LIST.charAt(index));
			return value + "ﾞ";
		}

		index = ZENKAKU_PA_LIST.indexOf(c);
		if (index >= 0) {
			// パピプペポ
			value = String.valueOf(HANKAKU_HA_LIST.charAt(index));
			return value + "ﾟ";
		} else if ((Character.valueOf(c)).equals(Character.valueOf('ワ'))) {
			// ワ
			return "ﾜ";
		} else if ((Character.valueOf(c)).equals(Character.valueOf('ヲ'))) {
			// ヲ
			return "ｦ";
		} else if ((Character.valueOf(c)).equals(ZENKAKU_WA_DAKUTEN)) {
			// ワ"[\u30f7]
			return "ﾜﾞ";
		} else if ((Character.valueOf(c)).equals(ZENKAKU_WO_DAKUTEN)) {
			// ヲ"[\u30fa]
			return "ｦﾞ";
		} else {
			// 該当なし
			return null;
		}
	}

	/**
	 * HTMLメタ文字列変換を行う。<br>
	 * 
	 * <pre>
	 * "&lt;"、"&gt;"、"&amp;"、"&quot;"といった、
	 * HTML中に そのまま出力すると問題がある文字を
	 * "&amp;lt;"、"&amp;gt;"、"&amp;amp;"、"&amp;quot;" に変換する。
	 * </pre>
	 * 
	 * @param str 変換する文字列
	 * @return 変換後の文字列
	 */
	public static String filter(final String str) {
		final char[] cbuf = str.toCharArray();
		final StringBuilder sbui = new StringBuilder();
		for (int i = 0; i < cbuf.length; i++) {
			if (cbuf[i] == '&') {
				sbui.append("&amp;");
			} else if (cbuf[i] == '<') {
				sbui.append("&lt;");
			} else if (cbuf[i] == '>') {
				sbui.append("&gt;");
			} else if (cbuf[i] == '"') {
				sbui.append("&quot;");
			} else {
				sbui.append(cbuf[i]);
			}
		}
		return sbui.toString();
	}

	/**
	 * 検索条件文字列を広報一致LIKE述語のパターン文字列に変換する。<br>
	 * 
	 * <pre>
	 * 変換ルールは以下の通り
	 * <ol>
	 * <li><code>LIKE_ESC_CHAR</code>を<code>LIKE_ESC_CHAR</code>でエスケープする。</li>
	 * <li>'%'と'_'を <code>LIKE_ESC_CHAR</code> でエスケープする。</li>
	 * <li>末尾に'%'を追加する。</li>
	 * </ol>
	 * </pre>
	 * 
	 * @param condition 検索条件文字列
	 * @return 変換後の検索条件文字列(paramがEmptyの場合、nullを返す)
	 */
	public static String toLikeCondition(final String condition) {
		if (StringUtils.isEmpty(condition)) {
			return null;
		}
		final char esc = (LIKE_ESC_CHAR.toCharArray())[0];
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < condition.length(); i++) {
			final char c = condition.charAt(i);
			if (c == esc) {
				result.append(esc);
				result.append(esc);
			} else if ((c == '%') || (c == '_')) {
				result.append(esc);
				result.append(c);
			} else {
				result.append(c);
			}
		}
		result.append('%');
		return result.toString();
	}

	/**
	 * 検索条件文字列を部分一致LIKE述語のパターン文字列に変換する。<br>
	 * 
	 * <pre>
	 * 変換ルールは以下の通り
	 * <ol>
	 * <li><code>LIKE_ESC_CHAR</code>を<code>LIKE_ESC_CHAR</code>でエスケープする。</li>
	 * <li>'%'と'_'を <code>LIKE_ESC_CHAR</code> でエスケープする。</li>
	 * <li>先頭と末尾に'%'を追加する。</li>
	 * </ol>
	 * </pre>
	 * 
	 * @param condition 検索条件文字列
	 * @return 変換後の検索条件文字列(paramがEmptyの場合、nullを返す)
	 * @see jp.co.techmatrix.core.util.StringUtil.toLikeCondition
	 */
	public static String toLikeConditionBoth(final String condition) {
		if (StringUtils.isEmpty(condition)) {
			return null;
		}
		final char esc = (LIKE_ESC_CHAR.toCharArray())[0];
		final StringBuilder result = new StringBuilder();
		result.append("%");
		for (int i = 0; i < condition.length(); i++) {
			final char c = condition.charAt(i);
			if (c == esc) {
				result.append(esc);
				result.append(esc);
			} else if ((c == '%') || (c == '_')) {
				result.append(esc);
				result.append(c);
			} else {
				result.append(c);
			}
		}
		result.append('%');
		return result.toString();
	}

	/**
	 * 文字の半角チェック
	 * 
	 * @param c 比較対象文字
	 * @return 比較結果
	 */
	public static boolean isHankaku(Character c) {
		for (Character tmp : HANKAKU_DATA_LIST) {
			if (tmp.equals(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 文字列の半角チェック
	 * 
	 * @param entry 比較対象文字列
	 * @return 比較結果
	 */
	public static boolean isAllHankaku(String entry) {
		int strLeng = entry.length();
		for (int i = 0; i < strLeng; i++) {
			if (isHankaku(entry.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 検索条件文字列にOracle Textのエスケープ文字を付与する。<br>
	 * 
	 * <pre>
	 * 変換ルールは以下の通り
	 * <ol>
	 * <li>中カッコを使用して、文字または記号の文字列をエスケープします。</li>
	 * <li>中カッコで囲まれたものが、エスケープ・シーケンスの部分とみなされます。</li>
	 * </ol>
	 * </pre>
	 * 
	 * @param condition 検索条件文字列
	 * @return 変換後の検索条件文字列
	 */
	public static String toOracleTextEsc(final String condition) {
		if (StringUtils.isEmpty(condition)) {
			return condition;
		}
		final char esc = (ORACLE_TEXT_ESC_CHAR.toCharArray())[0];
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < condition.length(); i++) {
			final char c = condition.charAt(i);
			if (c == esc) {
				result.append(esc);
				result.append(esc);
			} else {
				result.append(c);
			}
		}
		return "{" + result.toString() + "}";
	}
}
