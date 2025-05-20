package jp.co.takeda.util;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Scale;

/**
 * 数学関係のユーティリティクラス<br>
 * 
 * @author wakamori
 */
public abstract class MathUtil {

	/**
	 * UP率を算出する。
	 * 
	 * @param numerator 割れられる値
	 * @param denominator 割る値
	 * @return UP率(00.0)
	 */
	public static Double calcUp(final Long numerator, final Long denominator) {
		if (numerator == null || denominator == null || denominator == 0L) {
			return null;
		}
		return ratio(divide(numerator, denominator, 3, RoundingMode.HALF_UP));
	}

	/**
	 * 比率の値を計算しパーセントに変換する。<br>
	 * 除算の引数(分子・分母)が0またはNULLの場合は、0またはNULLを返す。 割り算を行い、結果に対し第三位で四捨五入後、100をかける。
	 * 
	 * @param numerator 割れられる値
	 * @param denominator 割る値
	 * @param isNullForZero TRUE：0を返却、FALSE：NULLを返却
	 * @return 構成(%)
	 */
	public static Double calcRatio(final Long numerator, final Long denominator, final boolean isNullForZero) {
		if (numerator == null || denominator == null || denominator == 0L) {
			if (isNullForZero) {
				return 0D;
			}
			return null;
		}
		Double doubleValue = MathUtil.divide(numerator, denominator, 3, RoundingMode.HALF_UP);
		BigDecimal bdValue = new BigDecimal(doubleValue.toString());
		bdValue = bdValue.multiply(new BigDecimal(100));
		return bdValue.doubleValue();
	}

	/**
	 * 比率の値を計算しパーセントに変換する。<br>
	 * 割る値(分母)が0またはNULLの場合は、NULLを返す。 割り算を行い、結果に対し第三位で四捨五入後、100をかける。
	 * 
	 * @param numerator 割れられる値
	 * @param denominator 割る値
	 * @return 構成(%)
	 */
	public static Double calcRatio(final Long numerator, final Long denominator) {
		return calcRatio(numerator, denominator, false);
	}

	/**
	 * 比率をパーセントに変換する<br>
	 * 第三位で四捨五入後、100をかける。
	 * 
	 * @param doubleValue 変換元比率値(DB値)
	 * @return 変換後の比率
	 */
	public static Double ratio(final Double doubleValue) {
		if (doubleValue == null) {
			return null;
		}
		BigDecimal bdValue = new BigDecimal(doubleValue.toString());
		bdValue = bdValue.setScale(3, RoundingMode.HALF_UP);
		bdValue = bdValue.multiply(new BigDecimal(100));
		return bdValue.doubleValue();
	}

	/**
	 * 加算を実行する。<br>
	 * NULLの場合は、0として扱う。ただし、対象が両方ともNULLの場合は、NULLを返す。
	 * 
	 * @param add1 加算対象
	 * @param add2 加算対象
	 */
	public static Long add(final Long add1, final Long add2) {

		if (add1 == null && add2 == null) {
			return null;
		}
		long _add1 = 0L;
		long _add2 = 0L;
		if (add1 != null) {
			_add1 = add1;
		}
		if (add2 != null) {
			_add2 = add2;
		}

		return _add1 + _add2;
	}

	/**
	 * 減算を実行する。<br>
	 * 引かれる値がNULLの場合、引く値がNULLの場合は、NULLを返すか<br>
	 * 0に変換して計算する。
	 * 
	 * @param sub1 引かれる値
	 * @param sub2 引く値
	 * @param isNullForZero TRUE：0として計算、FALSE：NULLを返却
	 */
	public static Long sub(final Long sub1, final Long sub2, final boolean isNullForZero) {

		if (!isNullForZero && (sub1 == null || sub2 == null)) {
			return null;
		}

		long _sub1 = 0L;
		long _sub2 = 0L;
		if (sub1 != null) {
			_sub1 = sub1;
		}
		if (sub2 != null) {
			_sub2 = sub2;
		}

		return _sub1 - _sub2;
	}

	/**
	 * 減算を実行する。<br>
	 * 引かれる値がNULLの場合、引く値がNULLの場合は、NULLを返す
	 * 
	 * @param sub1 引かれる値
	 * @param sub2 引く値
	 */
	public static Long sub(final Long sub1, final Long sub2) {

		return sub(sub1, sub2, false);
	}

	/**
	 * 減算を実行する。<br>
	 * 引かれる値・引く値が共にNULLの場合はNULLを返す。<br>
	 * どちらかがNULLの場合は、0として計算する。
	 * 
	 * @param sub1 引かれる値
	 * @param sub2 引く値
	 */
	public static Long subEx(final Long sub1, final Long sub2) {

		if (sub1 == null && sub2 == null) {
			return null;
		}

		return sub(sub1, sub2, true);
	}

	/**
	 * 増分を算出する。<br>
	 * 計画値がNULLの場合は、NULL。<br>
	 * 計画値が0の場合は、算出。<br>
	 * 実績値の有無に関わらず、増分値は算出する
	 * 
	 * @param plan 計画値
	 * @param result 実績値
	 */
	public static Long planForUp(final Long plan, final Long result) {
		return sub(plan, result, true);
	}

	/**
	 * 除算を実行する。<br>
	 * 
	 * @param numerator 割られる数
	 * @param denominator 割る数
	 * @param scale 計算の有効桁数、小数scale位まで結果を返す。0の場合整数となる。
	 * @param mode 丸めモード。指定する値は java.math.RoundingMode を参照
	 * @return numerator / denominator を指定した有効桁、丸めで計算したもの
	 * 
	 * @see java.math.RoundingMode
	 */
	public static Double divide(final Number numerator, final Number denominator, final int scale, final RoundingMode mode) {

		if (numerator == null || denominator == null || mode == null) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "除算の引数が不正"));
		}
		try {
			return Double.valueOf(BigDecimal.valueOf(numerator.doubleValue()).divide(BigDecimal.valueOf(denominator.doubleValue()), scale, mode).doubleValue());
		} catch (ArithmeticException e) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "除算の引数が不正"), e);
		}
	}

	/**
	 * 増加率を求める<br>
	 * 
	 * <pre>
	 * leftのrightに対する増加率を求める。
	 * 計算式は
	 * (left-right) / abs(right)
	 *
	 * 例)
	 *   スケールが1で丸めモードが四捨五入の場合
	 *   left=125 , right= 100 → 0.3 (30%の増加)
	 *   left=100 , right= 200 → -0.5 (-50%の増加)
	 *
	 *   スケールが2で丸めモードが切捨ての場合
	 *   left=125 , right= 100 → 0.38 (38%の増加)
	 *   left=119 , right= 200 → -0.40 (-40%の増加)
	 * </pre>
	 * 
	 * @param left 値1
	 * @param right 値2
	 * @param scale 計算の有効桁数
	 * @param mode 丸めモード。指定する値は java.math.RoundingMode を参照
	 * @return 'left'の'right'に対する増加率
	 * 
	 * @see java.math.RoundingMode
	 */
	public static Double increaseRate(final Number left, final Number right, final int scale, final RoundingMode mode) {
		if (left == null || right == null) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "増加率を求める引数が不正"));
		}
		final BigDecimal d1 = BigDecimal.valueOf(left.doubleValue());
		final BigDecimal d2 = BigDecimal.valueOf(right.doubleValue());
		if (d1.compareTo(d2) == 0) {
			return new Double(0);
		}
		return divide(d1.subtract(d2), d2.abs(), scale, mode);
	}

	/**
	 * 配分結果を算出する。
	 * 
	 * <pre>
	 * 配分値 = 配分母数 * 分子/分母
	 * </pre>
	 * 
	 * @param paramValue 配分母数
	 * @param numerator 分子
	 * @param denominator 分母
	 * @return 配分値
	 */
	public static Long calcDistValue(Long paramValue, Long numerator, Long denominator) {
		if (paramValue == null || numerator == null || denominator == null) {
			return null;
		}
		if (denominator == 0L) {
			return 0L;
		}
		final BigDecimal d1 = BigDecimal.valueOf(paramValue.doubleValue());
		final BigDecimal d2 = BigDecimal.valueOf(numerator.doubleValue());
		final BigDecimal d3 = BigDecimal.valueOf(denominator.doubleValue());
		BigDecimal result = null;
		result = d1.multiply(d2.divide(d3, 10, RoundingMode.HALF_UP));
		return result.longValue();
	}

	/**
	 * 理論値を指定した単位で丸める。
	 * 
	 * 
	 * @param value 理論値
	 * @param scale スケール
	 * @return 理論値
	 */
	public static Long calcTheory(Long value, Scale scale) {
		if (value == null) {
			return null;
		}
		Double result = MathUtil.divide(value, 1, scale.scaleValue(), RoundingMode.HALF_UP);
		return result.longValue();
	}

}
