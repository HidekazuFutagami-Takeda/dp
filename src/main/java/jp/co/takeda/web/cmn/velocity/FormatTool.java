package jp.co.takeda.web.cmn.velocity;

import static jp.co.takeda.a.exp.ErrMessageKey.SYSTEM_ERROR;

import java.math.BigDecimal;
import java.util.Calendar;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.MathUtil;
import jp.co.takeda.util.NumberUtil;

/**
 * 描画のためのVelocityツール
 * 
 * @author tkawabata
 */
public class FormatTool {

	/**
	 * <code>Calendar</code>型のオブジェクトを指定のパターンで文字列に変換する。<br>
	 * 引数がnullの場合は、空文字列を返却する。
	 * 
	 * @param cal Calendarオブジェクト
	 * @param datePattern パターン文字列(例：yyyy-MM-dd HH:mm:ss SSS)
	 * @return 日付文字列(例：2008-01-12 13:17:17 123
	 */
	public String toString(final Calendar cal, final String datePattern) {
		if (cal == null) {
			return "";
		}
		return DateUtil.toString(cal, datePattern);
	}

	/**
	 * <code>java.util.Date</code>型のオブジェクトを指定のパターンで文字列に変換する。<br>
	 * 引数がnullの場合は、空文字列を返却する。
	 * 
	 * @param date 日付
	 * @param datePattern フォーマット
	 * @return 日付文字列
	 */
	public String toString(final java.util.Date date, final String datePattern) {
		if (date == null) {
			return "";
		}
		return DateUtil.toString(date, datePattern);
	}

	/**
	 * <code>java.util.Date</code>型のオブジェクトを指定のパターンで文字列に変換する。<br>
	 * 引数がnullの場合は、空文字列を返却する。
	 * 
	 * @param date 日付
	 * @param datePattern フォーマット
	 * @return 日付文字列
	 */
	public String sysDate(final String datePattern) {
		java.util.Date date = new java.util.Date();
		return DateUtil.toString(date, datePattern);
	}

	/**
	 * <code>Integer</code>型で指定された日付を指定のパターンで文字列に変換する。<br>
	 * 引数がnullの場合は、空文字列を返却する。
	 * 
	 * @param target Integer型で指定された日付
	 * @param targetPattern targetのパターン文字列
	 * @param datePattern パターン文字列(例：yyyy-MM-dd HH:mm:ss SSS)
	 * @return 日付文字列(例：2008-01-12 13:17:17 123
	 */
	public String toString(final Integer target, final String targetPattern, final String datePattern) {
		if (target == null) {
			return "";
		}
		Calendar cal = DateUtil.toCalendar(target.toString(), targetPattern);
		return DateUtil.toString(cal, datePattern);
	}

	/**
	 * <code>Double</code>型のオブジェクトを指定のパターンで文字列に変換する。<br>
	 * 引数がnullの場合は、空文字列を返却する。
	 * 
	 * @param doubleNumber Doubleオブジェクト
	 * @param pattern pattern パターン文字列(例:"########.##")
	 * @return 数値文字列(例：12345678.12)
	 */
	public String doubleToString(Double doubleNumber, final String pattern) {
		if (doubleNumber == null) {
			return "";
		}
		if (pattern.equals(Constants.DEFAULT_RATE_FORMAT)) {
			BigDecimal bi = new BigDecimal(doubleNumber);
			doubleNumber = bi.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return NumberUtil.toString(doubleNumber, pattern);
	}

	/**
	 * 指定の日付の[month]ヶ月[後/前]の日付を返す。<br>
	 * 
	 * <pre>
	 * 引数[cal]がnullの場合は、空文字列を返却する。
	 * 引数[month]がnullの場合は[cal]を[datePattern]でフォーマットした日付を返却する。
	 * </pre>
	 * 
	 * @param call 元になる日付
	 * @param month month [month]ヶ月 プラスの場合は[後]、マイナスの場合は[前]
	 * @param datePattern パターン文字列(例：yyyy-MM-dd HH:mm:ss SSS)
	 * @return 日付
	 */
	public String getXMonth(final Calendar cal, final Integer month, final String datePattern) {
		if (cal == null) {
			return "";
		}
		if (month == null) {
			return toString(cal, datePattern);
		}
		return toString(DateUtil.getXMonth(cal, month), datePattern);
	}

	/**
	 * 整数値(Integer)の文字列の型変換を行う。<br>
	 * 引数がnullまたは空文字の場合は、nullを返却する。
	 * 
	 * @param entry 整数型が想定される文字列
	 * @return 整数値
	 */
	public Integer parseInteger(final String entry) {
		Integer value = null;
		try {
			value = ConvertUtil.parseInteger(entry);
		} catch (Exception e) {
			final String errMsg = "整数型(Intger)に変換できない";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
		}
		return value;
	}

	/**
	 * 整数値(Long)の文字列の型変換を行う。<br>
	 * 引数がnullまたは空文字の場合は、nullを返却する。
	 * 
	 * @param entry 整数型が想定される文字列
	 * @return 整数値
	 */
	public Long parseLong(final String entry) {
		Long value = null;
		try {
			value = ConvertUtil.parseLong(entry);
		} catch (Exception e) {
			final String errMsg = "整数型(Long)に変換できない";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
		}
		return value;
	}

	/**
	 * 比率の値を計算しパーセントに変換する。<br>
	 * 除算の引数（分子・分母）が0またはNULLの場合は、0またはNULLを返す。 割り算を行い、結果に対し第四位で四捨五入後、100をかける。
	 * 
	 * @param numerator 割られる値
	 * @param denominator 割る値
	 * @param isNullForZero TRUE：0を返却、FALSE：NULLを返却
	 * @return 構成(%)
	 */
	public Double calcRatio(final Long numerator, final Long denominator, final boolean isNullForZero) {
		if (numerator == null || denominator == null || numerator == 0L) {
			return null;
		}
		return MathUtil.calcRatio(numerator, denominator, isNullForZero);
	}

	/**
	 * 金額単位の変換（一円単位→千円単位）を行なう。<br>
	 * 端数が生じた場合は、四捨五入処理を行います。
	 * 
	 * @param entry 整数型が想定される文字列
	 * @return 整数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static String parseMoneyToThousandUnit(final Long entry) {
		if (entry == null) {
			return "";
		}
		return ConvertUtil.toString(ConvertUtil.parseMoneyToThousandUnit(entry));
	}

	/**
	 * 指定オブジェクトがマイナス値かを判定する。
	 * 
	 * @param entry オブジェクト
	 * @return マイナス値の場合に真
	 */
	public static boolean isMinus(final Object entry) {
		if (entry == null) {
			return false;
		}
		Number number = null;
		if (entry instanceof String) {
			number = toNumber((String) entry);
		} else if (entry instanceof Number) {
			number = (Number) entry;
		}
		if (number == null) {
			return false;
		}
		double dValue = number.doubleValue();
		if (dValue < 0d) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 指定オブジェクトを数字型に変換して返す。
	 * 
	 * @param entry オブジェクト
	 * @return 数値型
	 */
	public static Number toNumber(final Object entry) {
		if (entry == null) {
			return null;
		}
		String _target = null;
		if (entry instanceof String) {
			_target = ((String) entry).replaceAll(",", "");
		} else {
			_target = entry.toString();
		}
		if (!org.apache.commons.lang.math.NumberUtils.isNumber(_target)) {
			return null;
		}
		return org.apache.commons.lang.math.NumberUtils.createNumber(_target);
	}

	/**
	 * 指定文字列の末尾に全角空白を追加し、指定文字数まで調整する。<br>
	 * (例)山田太郎を10文字にしたい場合、adjustZenkakuBlank("山田太郎", 10)で"山田太郎　　　　　　"返す。
	 * 
	 * @param entry 調整対象の文字列
	 * @param length 調整後の文字列数(entry分を含む)
	 * @return 空白を追加した調整済み文字列を返す
	 */
	public static String adjustZenkakuBlank(String entry, int length) {

		if (entry == null) {
			return null;
		}
		if (length < 1) {
			return entry;
		}
		int addCount = length - entry.length();
		if (addCount < 1) {
			return entry;
		}
		final String ZENKAKU_BLANK = "　";
		StringBuilder sb = new StringBuilder(entry);
		for (int i = 0; i < addCount; i++) {
			sb.append(ZENKAKU_BLANK);
		}
		return sb.toString();
	}
}
