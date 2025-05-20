package jp.co.takeda.util;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.text.DecimalFormat;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

import org.apache.commons.lang.StringUtils;

/**
 * 数値処理のユーティリティクラス
 * 
 * @author tomita
 */
public abstract class NumberUtil {

	/**
	 * 引数の値を元に数値文字列を生成する。
	 * 
	 * <pre>
	 *  小数の変換は、値の小数点以下の桁数が文字列型への変換形式で指定した桁数よりも多い場合、変換形式で指定した桁数分しか表示されない。
	 *  値の小数点以下の桁数が変換形式で指定した桁数よりも多いと、その値には「丸め」が施される。
	 *  DecimalFormatでは、端数が出た場合、その上下の近いほうの数字に合わせる。
	 *  例えば、「0.128」を小数点以下1ケタに丸める場合は、「0.2」よりも「0.1」に近いため「0.1」になる。
	 *  また、「0.128」を小数点以下2ケタに丸める場合は、「0.12」よりも「0.13」に近いため「0.13」になる。
	 *
	 *  ただし、上下の数字までの“距離”が等しい場合(つまり、端数がちょうど5の場合)は偶数側の数字に合わせられる。
	 *  例えば、「1.35」を小数点以下2ケタに丸める場合は「1.3」と「1.4」への距離が等しいため、偶数である「1.4」に丸められる。
	 *  一方、「1.45」を丸める場合は偶数である「1.4」に丸められる。
	 *
	 *  文字列型への変換形式(pattern)の設定は以下の通りである。
	 *  <li>"#" : 数を表す。なお、値が0の場合はその値は表示されない。</li>
	 *  <li>"0" : 数を表す。なお、値が0の場合はその値に0が表示される。(0補完)</li>
	 *  <li>".(ドット)" : 整数値と小数値の区切りを表す。</li>
	 *  <li>",(カンマ)" : 数データのグループセパレータを表す。</li>
	 *  <li>"%" : 数データを100倍し、「%」を付与する。</li>
	 *  <li>\u00A5 : 通貨記号を表す。</li>
	 *  </pre>
	 * 
	 * @param number 数値文字列変換対象の数値
	 * @param pattern 文字列型への変換形式(例:"########.##","0000")
	 * @return 数値文字列
	 */
	public static String toString(Number number, String pattern) {

		// -----------------
		// 引数チェック
		// -----------------
		if (number == null) {
			final String errMsg = "数値文字列を生成するためのDouble型の数値が指定されていない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(pattern) == true) {
			final String errMsg = "String型への変換形式が指定されていない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------
		// 変換処理の実施
		// ----------------
		String stringNumber = null;
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		stringNumber = decimalFormat.format(number);
		return stringNumber;
	}
}
