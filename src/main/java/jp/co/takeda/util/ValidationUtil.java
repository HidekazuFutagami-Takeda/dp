package jp.co.takeda.util;

import java.math.BigDecimal;
import java.util.Date;

import jp.co.takeda.bean.Regexp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.UrlValidator;

/**
 * バリデート関連のユーティリティクラス<br>
 * 
 * <pre>
 * 戻り値がtrue/falseになる関数を定義する。
 * ロジック部分は別のユーティリティクラスに定義し、重複がないようにする。
 * </pre>
 * 
 * @author tkawabata
 */
public abstract class ValidationUtil {

	/**
	 * ロガー
	 */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(ValidationUtil.class);

	/**
	 * 指定された文字が半角スペースかどうかを判別する。<br>
	 * 
	 * @param c 対象文字
	 * @return ホワイトスペースであるときに true
	 */
	public static boolean isWhitespace(final char c) {
		return c == ' ';
	}

	/**
	 * 指定された文字が全角または半角スペースかどうかを判別する。 <br>
	 * 
	 * @param c 対象文字
	 * @return 全角または半角スペースであるときに true
	 */
	public static boolean isZenHankakuSpace(final char c) {
		return ((c == '　') || (c == ' '));
	}

	/**
	 * 検査対象文字列の文字列の桁数がminLength以上かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @param minLength 桁数(最小)
	 * @return minLength以上ならば真
	 */
	public static boolean isMinLength(final String entry, final int minLength) {
		return (GenericValidator.minLength(entry, minLength));
	}

	/**
	 * 検査対象文字列の文字列の桁数がmaxLength以内かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @param maxLength 桁数(最大)
	 * @return maxLength以内ならば真
	 */
	public static boolean isMaxLength(final String entry, final int maxLength) {
		return (GenericValidator.maxLength(entry, maxLength));
	}

	/**
	 * 検査対象文字列がInteger型に変換可能かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @return 変換可能ならば真
	 */
	public static boolean isInteger(final String entry) {
		return (GenericValidator.isInt(entry));
	}

	/**
	 * 検査対象文字列がLong型に変換可能かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @return 変換可能ならば真
	 */
	public static boolean isLong(final String entry) {
		return (GenericValidator.isLong(entry));
	}

	/**
	 * 検査対象文字列がDouble型に変換可能かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @return 変換可能ならば真
	 */
	public static boolean isDouble(String entry) {
		return GenericValidator.isDouble(entry);
	}

	/**
	 * 検査対象文字列がShort型に変換可能かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @return 変換可能ならば真
	 */
	public static boolean isShort(String entry) {
		return GenericValidator.isShort(entry);
	}

	/**
	 * 検査対象文字列がmin以上、max以内のInteger型数値形式かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @param min 最小値
	 * @param max 最大値
	 * @return min以上、max以内ならば真
	 */
	public static boolean isIntRange(final String entry, final int min, final int max) {
		return (GenericValidator.isInRange(Integer.parseInt(entry), min, max));
	}

	/**
	 * 検査対象文字列がmin以上、max以内のLong型数値形式かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @param min 最小値
	 * @param max 最大値
	 * @return min以上、max以内ならば真
	 */
	public static boolean isLongRange(final String entry, final long min, final long max) {
		return (GenericValidator.isInRange(Long.parseLong(entry), min, max));
	}

	/**
	 * 検査対象文字列が指定の日付書式フォーマットかを検証する。<br>
	 * 例：日付の書式フォーマット yyyy/MM/dd
	 * 
	 * @param entry 検証対象の文字列
	 * @return 指定の日付書式フォーマットならば真
	 */
	public static boolean isDate(final String entry, final String datePattern) {
		return (GenericValidator.isDate(entry, datePattern, true));
	}

	/**
	 * 検査対象文字列がe-Mail形式かを検証する。<br>
	 * 
	 * @param entry 検証対象の文字列
	 * @return e-Mail形式ならば真
	 */
	public static boolean isEmail(final String entry) {
		return (GenericValidator.isEmail(entry));
	}

	/**
	 * 検査対象文字列が指定の正規表現と一致するかを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @param regexp 正規表現
	 * @return 正規表現形式ならば真
	 */
	public static boolean isMask(final String entry, final Regexp regexp) {
		return (GenericValidator.matchRegexp(entry, regexp.regexpValue()));
	}

	/**
	 * クレジットカードの形式かを検証する。<br>
	 * 
	 * @param entry 検査対象文字列
	 * @return クレジットカードの形式ならば真
	 */
	public static boolean isCreditCard(final String entry) {
		return (GenericValidator.isCreditCard(entry));
	}

	/**
	 * 検査対象文字列がURL形式かを検証する。<br>
	 * 
	 * @param entry 検証対象の文字列
	 * @return URL形式ならば真
	 */
	public static boolean isUrl(final String entry) {
		return (GenericValidator.isUrl(entry));
	}

	/**
	 * 検査対象文字列が指定のスキーマのうちのいずれかのURLフォーマットかを検証する。<br>
	 * 
	 * <pre>
	 *   例：検証対象のスキーマ
	 * <code>
	 *  String[] schemes = &quot;{&quot;http&quot;, &quot;https&quot;, &quot;ftp&quot;}
	 * </code>
	 * </pre>
	 * 
	 * @param entry 検証対象の文字列
	 * @param schemes 検証対象のスキーマ
	 * @return 指定のスキーマのURL形式ならば真
	 */
	public static boolean isUrl(final String entry, final String[] schemes) {
		final UrlValidator urlValidator = new UrlValidator(schemes);
		return (urlValidator.isValid(entry));
	}

	/**
	 * 開始日付、終了日付の大小関係を検査する。<br>
	 * 開始日付、終了日付のいずれかがnullもしくは空の場合は常に真を返す。
	 * 
	 * @param from 開始日付文字列
	 * @param to 終了日付文字列
	 * @param pattern 日付パターン
	 * @param equal form=toを許可するか。tureの場合許可、falseの場合許可しない。
	 * @return formまたはtoが空、equalがtrueならform<=to、equalがfalseならform<toの場合true、それ以外はfalse
	 */
	public static boolean isDateIntervalCheck(final String from, final String to, final String pattern, final boolean equal) {

		if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {
			return true;
		}
		try {

			final Date fromDate = DateUtil.toDate(from, pattern);
			final Date toDate = DateUtil.toDate(to, pattern);

			final int compare = toDate.compareTo(fromDate);

			if (equal) {
				return compare >= 0 ? true : false;
			} else {
				return compare > 0 ? true : false;
			}
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * 文字列が指定された精度以内の数値であることを検証する。<br>
	 * 
	 * <pre>
	 * 文字列中にカンマが含まれている場合はトリミング後の値に対して検証する。
	 * 精度の指定は precision と scale で行う。
	 * 例：123.45 の場合 precision = 5 scale = 2
	 * </pre>
	 * 
	 * @param s 検査対象の文字列
	 * @param precision 検査対象の全体の桁数
	 * @param scale 有効な精度 (小数点以下)
	 * @return 指定した精度に納まる数値の場合は true、数値でない場合と精度を 超えているいる場合は false
	 */
	static public boolean isNumberScale(String s, final int precision, final int scale) {

		// 金額などのカンマは許容
		s = s.trim().replaceAll(",", "");

		// BigDecimal での 123E-2 の様な記述を許可しない
		if ((s.indexOf("e") > 0) || (s.indexOf("E") > 0)) {
			return false;
		}

		try {

			final String match = "[0-9\\.-]+";
			if (!s.matches(match)) {
				return false;
			}

			final BigDecimal num = new BigDecimal(s);

			if ((num.precision() - num.scale()) > (precision - scale)) {
				// 整数部が over
				return false;
			}
			if (num.scale() > scale) {
				// 小数部が over
				return false;
			}
			return true;

		} catch (final NumberFormatException e) {
			return false;
		}
	}
}
