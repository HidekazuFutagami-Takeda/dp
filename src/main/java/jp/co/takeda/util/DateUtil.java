package jp.co.takeda.util;

import static jp.co.takeda.a.exp.ErrMessageKey.PARSE_ERROR;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.SystemException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日付・時刻・カレンダー関連のユーティリティクラス
 * 
 * @author tkawabata
 */
public abstract class DateUtil {

	/**
	 * ロガー
	 */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(DateUtil.class);

	// -------------------------------------------------
	// 日付型 → 日付文字列
	// -------------------------------------------------

	/**
	 * <code>java.util.Date</code>型のオブジェクトを指定のパターンで文字列に変換する。<br>
	 * 
	 * @param date Dateオブジェクト
	 * @param datePattern パターン文字列(例：yyyy-MM-dd HH:mm:ss SSS)
	 * @return 日付文字列(例：2008-01-12 13:17:17 123
	 */
	public static String toString(final java.util.Date date, final String datePattern) {

		toStringMethodParamChk(date, datePattern);

		final Format format = FastDateFormat.getInstance(datePattern);
		return format.format(date);
	}

	/**
	 * <code>Calendar</code>型のオブジェクトを指定のパターンで文字列に変換する。<br>
	 * 
	 * @param cal Calendarオブジェクト
	 * @param datePattern パターン文字列(例：yyyy-MM-dd HH:mm:ss SSS)
	 * @return 日付文字列(例：2008-01-12 13:17:17 123
	 */
	public static String toString(final Calendar cal, final String datePattern) {

		toStringMethodParamChk(cal, datePattern);

		final Format format = FastDateFormat.getInstance(datePattern);
		return format.format(cal);
	}

	/**
	 * 日付型のオブジェクトを文字列に変換する際の引数チェックを行う。<br>
	 * 
	 * @param target 日付型のオブジェクト
	 * @param datePattern パターン文字列
	 */
	private static void toStringMethodParamChk(final Object target, final String datePattern) {

		if (target == null) {
			final String errMsg = "日付文字列に変換時に対象が指定されていない";
			throw new SystemException(new Conveyance(PARSE_ERROR, errMsg));
		}

		if (StringUtils.isBlank(datePattern)) {
			final String errMsg = "日付文字列に変換時にパターン文字列が指定されていない";
			throw new SystemException(new Conveyance(PARSE_ERROR, errMsg));
		}
	}

	// -------------------------------------------------
	// 日付文字列 → 日付型
	// -------------------------------------------------

	/**
	 * 日付文字列を指定のパターンで<code>java.util.Date</code>に変換する。<br>
	 * 
	 * @param target 日付文字列
	 * @param datePattern パターン文字列(例：yyyy-MM-dd HH:mm:ss SSS)
	 * @return java.util.Dateオブジェクト
	 */
	public static java.util.Date toDate(final String target, final String datePattern) {

		toObjectMethodParamChk(target, datePattern);
		try {
			return new SimpleDateFormat(datePattern).parse(target);
		} catch (final ParseException e) {
			final String errMsg = "文字列を日付型に解析出来ない。datePartern=" + datePattern;
			throw new SystemException(new Conveyance(PARSE_ERROR, errMsg + "target=" + target), e);
		}
	}

	/**
	 * 日付文字列を指定のパターンで<code>java.util.Calendar</code>に変換する。<br>
	 * 
	 * @param target 日付文字列
	 * @param datePattern パターン文字列(例：yyyy-MM-dd HH:mm:ss SSS)
	 * @return Calendarオブジェクト
	 */
	public static Calendar toCalendar(final String target, final String datePattern) {

		toObjectMethodParamChk(target, datePattern);
		final java.util.Date date = toDate(target, datePattern);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.getTime(); // 変更を確実に反映するために呼びだす
		return cal;
	}

	/**
	 * 日付型のオブジェクトを文字列に変換する際の引数チェックを行う。<br>
	 * 
	 * @param target 日付型のオブジェクト
	 * @param datePattern パターン文字列
	 */
	private static void toObjectMethodParamChk(final String target, final String datePattern) {

		if (StringUtils.isBlank(target)) {
			final String errMsg = "日付型のオブジェクトを文字列に変換時に対象が指定されていない";
			throw new SystemException(new Conveyance(PARSE_ERROR, errMsg));
		}

		if (StringUtils.isBlank(datePattern)) {
			final String errMsg = "日付型のオブジェクトを文字列に変換時にパターン文字列が指定されていない";
			throw new SystemException(new Conveyance(PARSE_ERROR, errMsg));
		}
	}

	// -------------------------------------------------
	// その他汎用日付関数
	// -------------------------------------------------

	/**
	 * システム時刻を取得する。<br>
	 * システム日付取得には必ずこのメソッドを利用する。
	 * 
	 * @return システム時刻
	 */
	public static java.util.Date getSystemTime() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * [日付]の時間部分以下を切り捨てて返す。<br>
	 * 切り捨てた部分にはデフォルト値が挿入される。
	 * 
	 * @param cal 対象の日付
	 * @return 時間部分以下を切り捨てた日付
	 */
	public static Calendar truncateTime(final Calendar cal) {
		return DateUtils.truncate(cal, Calendar.DATE);
	}

	/**
	 * 指定の日付の[year]年[後/前]の日付を返す。<br>
	 * 
	 * @param cal 元になる日付
	 * @param year [year]年 プラスの場合は[後]、マイナスの場合は[前]
	 * @return 日付
	 */
	public static Calendar getXYear(final Calendar cal, final int year) {
		final Calendar c = (Calendar) cal.clone();
		c.add(Calendar.YEAR, year);
		c.getTime(); // 変更を確実に反映するために呼びだす
		return c;
	}

	/**
	 * 指定の日付の[month]ヶ月[後/前]の日付を返す。<br>
	 * 
	 * @param cal 元になる日付
	 * @param month [month]ヶ月 プラスの場合は[後]、マイナスの場合は[前]
	 * @return 日付
	 */
	public static Calendar getXMonth(final Calendar cal, final int month) {
		final Calendar c = (Calendar) cal.clone();
		c.add(Calendar.MONTH, month);
		c.getTime(); // 変更を確実に反映するために呼びだす
		return c;
	}

	/**
	 * 指定の日付の[day]日[後/前]の日付を返す。<br>
	 * 
	 * @param cal 元になる日付
	 * @param day [day]日後 プラスの場合は[後]、マイナスの場合は[前]
	 * @return 日付
	 */
	public static Calendar getXDay(final Calendar cal, final int day) {
		final Calendar c = (Calendar) cal.clone();
		c.add(Calendar.DATE, day);
		c.getTime(); // 変更を確実に反映するために呼びだす
		return c;
	}

	/**
	 * 引数の値を元に日付文字列を生成する。
	 * 
	 * <pre>
	 * 日付文字列は次の形式で返す。
	 * "yyyyMMdd HHmmss"
	 * (例) "20080318 1914"
	 * なお、月日の妥当性については検証しない。
	 * </pre>
	 * 
	 * @param yyyymmdd 日付を表す数値
	 * @param hhmmss 時分秒を表す数値
	 * @return 日付文字列
	 */
	public static String toDateFormat(int yyyymmdd, int hhmmss) {
		if (yyyymmdd < 10000000) {
			final String errMsg = "指定年が「1000年」より前に設定されている";
			throw new SystemException(new Conveyance(new MessageKey(errMsg)));
		}
		final String space = " ";
		final String zero6 = "000000";
		return String.valueOf(yyyymmdd) + space + StringUtils.right(zero6 + String.valueOf(hhmmss), 6);
	}

	/**
	 * 引数の値を元に日付文字列を生成する。 <br>
	 * 
	 * <pre>
	 * 日付文字列は次の形式で返す。
	 * "yyyyMMdd HHmmss SSS"
	 *  (例)"20080318 1915 010"
	 * なお、月日の妥当性については検証しない。
	 * </pre>
	 * 
	 * @param yyyymmdd 日付を表す数値
	 * @param hhmmss 時分秒を表す数値
	 * @param sss ミリ秒を表す数値
	 * @return 日付文字列
	 */
	public static String toDateFormat(int yyyymmdd, int hhmmss, int sss) {
		if (yyyymmdd < 10000000) {
			final String errMsg = "指定年が「1000年」より前に設定されている";
			throw new SystemException(new Conveyance(new MessageKey(errMsg)));
		}
		final String space = " ";
		final String zero3 = "000";
		String yyyymmdd_hhmmss = toDateFormat(yyyymmdd, hhmmss);
		return yyyymmdd_hhmmss + space + StringUtils.right(zero3 + String.valueOf(sss), 3);
	}
}
