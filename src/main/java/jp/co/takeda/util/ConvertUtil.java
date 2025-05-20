package jp.co.takeda.util;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.math.RoundingMode;
import java.util.Date;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.RefPeriod;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 変換を行うためのユーティリティクラス<br>
 *
 * @author tkawabata
 */
public abstract class ConvertUtil {

	/**
	 * ロガー
	 */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(ConvertUtil.class);

	/**
	 * 金額単位の変換(千円単位→一円単位)を行なう。<br>
	 *
	 * @param entry 金額(千円単位)
	 * @return 金額(一円単位)。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Long parseMoneyToNormalUnit(final Long entry) throws SystemException {

		if (entry == null) {
			return null;
		} else {
			return entry * 1000;
		}
	}

	/**
	 * 金額単位の変換(千円単位→一円単位)を行なう。<br>
	 * 文字列の一円単位金額(カンマ有無、マイナス有無)をLongの千円単位に変換する。 変換不可能の場合は例外をスローする。
	 *
	 * @param entry 整数型が想定される文字列
	 * @return 整数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Long parseMoneyToNormalUnit(final String entry) throws SystemException {

		if (StringUtils.isEmpty(entry)) {
			return null;
		}

		return parseLong(entry.replaceAll(",", "")) * 1000;

	}

	/**
	 * 金額単位の変換(一円単位→千円単位)を行なう。<br>
	 * 端数が生じた場合は、四捨五入処理を行います。
	 *
	 * @param entry 整数型が想定される文字列
	 * @return 整数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Long parseMoneyToThousandUnit(final Long entry) throws SystemException {

		if (entry == null) {
			return null;
		} else {

			// 1000で割る
			Double result = MathUtil.divide(entry, 1000, 0, RoundingMode.HALF_UP);

			try {
				return result.longValue();

			} catch (final NumberFormatException e) {
				final String errMsg = "整数型変換エラー：1000円単位に変換後の数値がLong型に変換失敗。変換後数値：";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + result), e);
			}

		}
	}

	/**
	 * 金額単位の変換(一円単位→千円単位)を行なう。<br>
	 * 端数が生じた場合は、小数点表示
	 *
	 * @param entry 整数型が想定される文字列
	 * @return 整数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Double parseMoneyToThousandUnitDouble(final Long entry) throws SystemException {

		if (entry == null) {
			return null;
		} else {

			// 1000で割る
			Double result = (double)entry / 1000;
			try {
				return result.doubleValue();

			} catch (final NumberFormatException e) {
				final String errMsg = "整数型変換エラー：1000円単位に変換後の数値がDouble型に変換失敗。変換後数値：";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + result), e);
			}

		}
	}

	/**
	 * 金額単位の変換(一円単位→千円単位)を行なう。<br>
	 * 端数が生じた場合の処理モードを指定します。
	 *
	 * @param entry 整数型が想定される文字列
	 * @param mode 端数処理モード
	 * @return 整数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Long parseMoneyToThousandUnit(final Long entry, RoundingMode mode) throws SystemException {

		if (entry == null) {
			return null;
		} else {
			Double result = MathUtil.divide(entry, 1000, 0, mode);
			try {
				return result.longValue();

			} catch (final NumberFormatException e) {
				final String errMsg = "整数型変換エラー：Longの数値が想定されている箇所で整数(Long)以外が入力された。入力値：";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + result), e);
			}

		}
	}

	/**
	 * 実績値(または計画値)の1期分(6ヶ月)への変換を行なう。<br>
	 *
	 * @param entry 実績値(または計画値)
	 * @param refPeriodFrom 参照期間(FROM)
	 * @param refPeriodTo 参照期間(TO)
	 * @return 1期分実績値(または計画値)。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Long parseTermValue(final Long entry, final RefPeriod refPeriodFrom, final RefPeriod refPeriodTo) throws SystemException {

		if (refPeriodFrom == null || refPeriodTo == null) {
			final String errMsg = "参照期間がNULL";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (entry == null) {
			return null;
		}

		int termCount = refPeriodTo.ordinal() - refPeriodFrom.ordinal() + 1;
		if (termCount <= 0) {
			final String errMsg = "参照期間が不正";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 参照期間が6ヶ月の場合、引数の値をそのままかえす。
		if (termCount == 6) {
			return entry;
		}

		return MathUtil.divide(entry * 6, termCount, 0, RoundingMode.HALF_UP).longValue();
	}

	/**
	 * 整数値(Integer)の文字列の型変換を行う。<br>
	 *
	 * @param entry 整数型が想定される文字列
	 * @return 整数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Integer parseInteger(final String entry) throws SystemException {

		if (StringUtils.isBlank(entry)) {
			return null;
		}

		Integer result;
		try {

			result = Integer.parseInt(entry);

		} catch (final NumberFormatException e) {
			final String errMsg = "整数型変換エラー：Integerの数値が想定されている箇所で整数(Integer)以外が入力された。入力値=";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + entry), e);
		}

		return result;
	}

	/**
	 * 整数値(Long)の文字列の型変換を行う。<br>
	 *
	 * @param entry 整数型が想定される文字列
	 * @return 整数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Long parseLong(final String entry) throws SystemException {

		if (StringUtils.isBlank(entry)) {
			return null;
		}

		Long result;
		try {

			result = Long.parseLong(entry);

		} catch (final NumberFormatException e) {
			final String errMsg = "整数型変換エラー：Longの数値が想定されている箇所で整数(Long)以外が入力された。入力値：";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + entry), e);
		}
		return result;
	}

	/**
	 * 整数値(Long)の文字列の型変換を行う。<br>
	 *
	 * @param entry 整数型が想定されるオブジェクト
	 * @param defaultValue オブジェクトがnullまたは、変換できない場合のデフォルト値
	 * @return 整数値。引数が空もしくはnullの場合はdefaultValueを返す。
	 */
	public static Long parseLong(final Object entry, Long defaultValue) throws SystemException {

		String value = toString(entry);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}

		try {
			return Long.parseLong(value);

		} catch (final NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 実数値(Short)の文字列の型変換を行う。<br>
	 *
	 * @param entry 実数型が想定される文字列
	 * @return 実数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Short parseShort(final String entry) throws SystemException {

		if (StringUtils.isBlank(entry)) {
			return null;
		}

		Short result;
		try {

			result = Short.parseShort(entry);

		} catch (final NumberFormatException e) {
			final String errMsg = "実数型変換エラー：Shortの数値が想定されている箇所で実数(Short)以外が入力された。入力値：";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + entry), e);
		}
		return result;
	}

	/**
	 * 実数値(Double)の文字列の型変換を行う。<br>
	 *
	 * @param entry 実数型が想定される文字列
	 * @return 実数値。引数が空もしくはnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Double parseDouble(final String entry) throws SystemException {

		if (StringUtils.isBlank(entry)) {
			return null;
		}

		Double result;
		try {

			result = Double.parseDouble(entry);

		} catch (final NumberFormatException e) {
			final String errMsg = "実数型変換エラー：Doubleの数値が想定されている箇所で実数(Double)以外が入力された。入力値：";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + entry), e);
		}
		return result;
	}

	/**
	 * 日付数値文字列の型変換を行う。<br>
	 *
	 * @param entry 日付数値文字列
	 * @return 日付型
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static Date parseDate(final String entry) throws SystemException {
		if (StringUtils.isBlank(entry)) {
			return null;
		}
		Long entryLong = parseLong(entry);
		Date date = new Date();
		date.setTime(entryLong);
		date.getTime(); // 適用されるようにgetTimeを呼んでおく
		return date;
	}

	/**
	 * Bool値(Boolean)の文字列の型変換を行う。<br>
	 *
	 * @param entry Bool値が想定される文字列
	 * @return Bool値。引数が空もしくはnullの場合はfalseを返す。
	 */
	public static Boolean parseBoolean(final String entry) {
		return Boolean.parseBoolean(entry);
	}

	/**
	 * 列挙を表す文字列を列挙型に型変換を行う。<br>
	 *
	 * @param <T> 取得したい列挙型
	 * @param enumType 取得したい列挙型クラス
	 * @param entry 列挙を表す文字列
	 * @return 指定した型の列挙型。第二引数がnullの場合はnull値を返す。
	 * @throws SystemException 変換できない場合にスロー
	 */
	public static <T extends Enum<T>> T valueOf(final Class<T> enumType, final String entry) throws SystemException {

		if (enumType == null) {
			final String errMsg = "列挙型変換エラー：列挙の型が指定されていない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (entry == null) {
			return null;
		}

		T result;
		try {

			result = Enum.valueOf(enumType, entry);

		} catch (final IllegalArgumentException e) {
			final String errMsg2 = "列挙型変換エラー：指定の文字列に該当する列挙型がない。指定型：";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg2 + ",入力値：" + entry), e);
		}
		return result;
	}

	/**
	 * 指定の文字列をカンマで分割する。<br>
	 *
	 * <pre>
	 * カンマ区切りの文字列をカンマを区切文字としてString要素に分割する。
	 * 以下に例を示す。
	 * "123,123,123,123,123" ⇒ {"123", "123", "123", "123", "123"}
	 * "123,123,123,,123" ⇒ {"123", "123", "123", "", "123"}
	 * "123,123,123,," ⇒ {"123", "123", "123", "", ""}
	 * ",,,," ⇒ {"", "", "", "", ""}
	 * ",,123,,123" ⇒ {"", "", "123", "", "123"}
	 * ※ 単純にカンマで区切りで分割するので、要素の中にカンマが含まれていないことを大前提とする。
	 * </pre>
	 *
	 * @param entry 分割対象の文字列
	 * @param expectEntry 期待する要素数
	 * @return カンマで分割した文字列
	 */
	public static String[] splitConmma(final String entry) {
		if (entry == null) {
			final String errMsg = "文字列がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String CONMMA = ",";
		return entry.split(CONMMA, -1);
	}

	/**
	 * 指定されたオブジェクトを文字列に変換する。<br>
	 *
	 * @param o 文字列に変換するオブジェクト
	 * @return パラメタに対するtoString()結果値
	 */
	public static String toString(Object o) {
		if (o == null) {
			return null;
		}
		return o.toString();
	}
}
