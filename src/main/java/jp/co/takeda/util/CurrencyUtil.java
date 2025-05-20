package jp.co.takeda.util;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.NumberUnit;
import jp.co.takeda.bean.Scale;

/**
 * 通貨に関連するユーティリティクラス<br>
 * 
 * @author wakamori
 */
public abstract class CurrencyUtil {

	/**
	 * 円単位の金額を指定した単位に丸める。<br>
	 * 
	 * <pre>
	 * スケールは0。小数点以下はなしとする。
	 * 丸めはmodeに従う。
	 * 
	 * 【使用例】:
	 * <code>
	 * long yen = 111456;
	 * long t   = CurrencyUtil.unitOf(value,NumberUnitImpl.THOUSAND,RoundingMode.HALF_UP);
	 *   (... t is 111 )
	 * </code>
	 * 例)
	 *   単位が千円単位、丸めモードが四捨五入の場合
	 *   12345→12
	 *   55555→56
	 *   
	 *   単位が千円単位、丸めモードが切捨ての場合
	 *   12345→12
	 *   55555→55
	 * </pre>
	 * 
	 * @param value 金額を表す数値。単位は円
	 * @param unit 単位
	 * @param mode 丸めモード。指定する値は java.math.RoundingMode を参照
	 * 
	 * @see java.math.RoundingMode
	 */
	public static long unitOf(final long value, final NumberUnit unit, final RoundingMode mode) {
		return (long) unitOf(value, unit, 0, mode);
	}

	/**
	 * 円単位の金額を指定した単位に丸める。<br>
	 * 
	 * <pre>
	 * placeは小数点以下の有効桁数になる。
	 * 
	 * 【使用例】:
	 * <code>
	 * long yen = 123455;
	 * long t   = CurrencyUtil.unitOf(value,NumberUnitImpl.THOUSAND, 2, RoundingMode.HALF_UP);
	 *   (... t is 123.46 )
	 * </code>
	 * 
	 * 例)
	 *   単位が千円単位、小数点以下の有効桁数2桁、丸めモードが四捨五入の場合
	 *   12345→12.35
	 *   11111→11.11
	 *   
	 *   単位が千円単位、小数点以下の有効桁数1桁、丸めモードが切捨ての場合
	 *   12345→12.3
	 *   11111→11.1
	 * </pre>
	 * 
	 * @param value 金額を表す数値。単位は円
	 * @param unit 単位
	 * @param place 小数点以下の有効桁数
	 * @param mode 丸めモード。指定する値は java.math.RoundingMode を参照
	 * 
	 * @see java.math.RoundingMode
	 */
	public static double unitOf(final long value, final NumberUnit unit, final int place, final RoundingMode mode) {
		if (unit == null || mode == null) {
			final String errMsg = "指定した単位の円単位の金額算出時の引数が不正";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		return MathUtil.divide(value, unit.unitValue(), place, mode);
	}

	/**
	 * 指定された単位のスケールで丸める。<br>
	 * 
	 * <pre>
	 * 【使用例】
	 * <code>
	 * long yen = 12345;
	 * long t   = CurrencyUtil.scale(value,ScaleImpl.THOUSAND, RoundingMode.HALF_UP);
	 *   (... t is 12000 )
	 * 
	 * 例)
	 *   スケールが千円単位のスケールで丸めモードが四捨五入の場合
	 *   12345→12000
	 *   55555→56000
	 *   
	 *   スケールが万円単位のスケールで丸めモードが切捨ての場合
	 *   12345→10000
	 *   55555→50000
	 * 
	 * </pre>
	 * 
	 * @param value 金額を表す数値。単位は円
	 * @param unit スケール
	 * @param mode 丸めモード。指定する値は java.math.RoundingMode を参照
	 * 
	 * @see java.math.RoundingMode
	 */
	public static Long scale(final Long value, final Scale unit, final RoundingMode mode) {

		if (value == null) {
			return null;
		}

		if (unit == null || mode == null) {
			final String errMsg = "指定したスケールで丸めた値を算出時の引数が不正";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		return BigDecimal.valueOf(value).setScale(unit.scaleValue(), mode).longValue();
	}
}
