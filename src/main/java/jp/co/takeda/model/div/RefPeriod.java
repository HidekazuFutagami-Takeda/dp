package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

import org.apache.commons.lang.StringUtils;

/**
 * 参照期間を表す列挙
 * 
 * @author tkawabata
 */
public enum RefPeriod implements DbValue<String> {

	/**
	 * 前々々期1ヶ月目
	 */
	REF_01("01"),

	/**
	 * 前々々期2ヶ月目
	 */
	REF_02("02"),

	/**
	 * 前々々期3ヶ月目
	 */
	REF_03("03"),

	/**
	 * 前々々期4ヶ月目
	 */
	REF_04("04"),

	/**
	 * 前々々期5ヶ月目
	 */
	REF_05("05"),

	/**
	 * 前々々期6ヶ月目
	 */
	REF_06("06"),

	/**
	 * 前々期1ヶ月目
	 */
	REF_07("07"),

	/**
	 * 前々期2ヶ月目
	 */
	REF_08("08"),

	/**
	 * 前々期3ヶ月目
	 */
	REF_09("09"),

	/**
	 * 前々期4ヶ月目
	 */
	REF_10("10"),

	/**
	 * 前々期5ヶ月目
	 */
	REF_11("11"),

	/**
	 * 前々期6ヶ月目
	 */
	REF_12("12"),

	/**
	 * 前期1ヶ月目
	 */
	REF_13("13"),

	/**
	 * 前期2ヶ月目
	 */
	REF_14("14"),

	/**
	 * 前期3ヶ月目
	 */
	REF_15("15"),

	/**
	 * 前期4ヶ月目
	 */
	REF_16("16"),

	/**
	 * 前期5ヶ月目
	 */
	REF_17("17"),

	/**
	 * 前期6ヶ月目
	 */
	REF_18("18"),

	/**
	 * 当期1ヶ月目
	 */
	REF_19("19"),

	/**
	 * 当期2ヶ月目
	 */
	REF_20("20"),

	/**
	 * 当期3ヶ月目
	 */
	REF_21("21"),

	/**
	 * 当期4ヶ月目
	 */
	REF_22("22"),

	/**
	 * 当期5ヶ月目
	 */
	REF_23("23"),

	/**
	 * 当期6ヶ月目
	 */
	REF_24("24");

	/**
	 * Static initializer <br>
	 * 参照期間列挙をリストで保持する。
	 */
	static {

		List<RefPeriod> temp = new ArrayList<RefPeriod>();
		//		temp.add(RefPeriod.REF_24);
		//		temp.add(RefPeriod.REF_23);
		temp.add(RefPeriod.REF_22);
		temp.add(RefPeriod.REF_21);
		temp.add(RefPeriod.REF_20);
		temp.add(RefPeriod.REF_19);
		temp.add(RefPeriod.REF_18);
		temp.add(RefPeriod.REF_17);
		temp.add(RefPeriod.REF_16);
		temp.add(RefPeriod.REF_15);
		temp.add(RefPeriod.REF_14);
		temp.add(RefPeriod.REF_13);
		temp.add(RefPeriod.REF_12);
		temp.add(RefPeriod.REF_11);
		temp.add(RefPeriod.REF_10);
		temp.add(RefPeriod.REF_09);
		temp.add(RefPeriod.REF_08);
		temp.add(RefPeriod.REF_07);
		temp.add(RefPeriod.REF_06);
		temp.add(RefPeriod.REF_05);
		temp.add(RefPeriod.REF_04);
		temp.add(RefPeriod.REF_03);
		temp.add(RefPeriod.REF_02);
		temp.add(RefPeriod.REF_01);
		ALL_REF_PERIOD_LIST = Collections.unmodifiableList(temp);

	}

	/**
	 * 全期間の参照期間列挙のリスト
	 */
	public static List<RefPeriod> ALL_REF_PERIOD_LIST;

	/**
	 * コンストラクタ
	 * 
	 * @param value 参照期間を表す文字
	 */
	private RefPeriod(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 参照期間を表す文字を取得する。
	 * 
	 * @return 参照期間を表す文字
	 */
	public String getDbValue() {
		return value;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static RefPeriod getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (RefPeriod entry : RefPeriod.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

	/**
	 * DBとのマッピングを行うタイプハンドラークラス
	 * 
	 * @author nozaki
	 */
	public static class RefPeriodTypeHandler extends AbstractEnumTypeHandler {

		public RefPeriodTypeHandler() {
			super(new EnumType(RefPeriod.class, java.sql.Types.CHAR));
		}

	}

	/**
	 * 参照期間・年度・期を元に、参照期間文字列(YYYYMM形式)を算出する。
	 * 
	 * @param refPeriod 参照期間を表す列挙
	 * @param fisicalYear 当期年度を表す文字列(YYYY形式)
	 * @param calTerm 期を表す列挙
	 * @return 参照期間(YYYYMM形式)
	 */
	public static Date convertRefPeriod(RefPeriod refPeriod, String fisicalYear, Term calTerm) {

		// ------------------------------
		// 引数検証
		// ------------------------------
		if (refPeriod == null) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "参照期間が指定されていない"));
		}

		if (StringUtils.isBlank(fisicalYear)) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "年度が指定されていない"));
		}

		if (calTerm == null) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "期が指定されていない"));
		}

		// ------------------------------
		// 処理開始
		// ------------------------------
		Calendar cal = Calendar.getInstance();

		// 年の設定
		cal.set(Calendar.YEAR, Integer.parseInt(fisicalYear));

		// 月の設定
		switch (calTerm) {

			case FIRST:
				cal.set(Calendar.MONTH, Term.FIRST_MONTH - 1);
				break;

			case SECOND:
				cal.set(Calendar.MONTH, Term.SECOND_MONTH - 1);
				break;
			default:
				throw new SystemException(new Conveyance(PARAMETER_ERROR, "想定外の期が指定された。calTerm=" + calTerm));
		}

		// 日の設定
		cal.set(Calendar.DATE, 1);

		// Calendarの仕様上、setメソッドでは反映されないため、getTime()を呼び出しを行う
		cal.getTime();

		switch (refPeriod) {

			case REF_01:
				cal.add(Calendar.MONTH, -24);
				break;
			case REF_02:
				cal.add(Calendar.MONTH, -23);
				break;
			case REF_03:
				cal.add(Calendar.MONTH, -22);
				break;
			case REF_04:
				cal.add(Calendar.MONTH, -21);
				break;
			case REF_05:
				cal.add(Calendar.MONTH, -20);
				break;
			case REF_06:
				cal.add(Calendar.MONTH, -19);
				break;
			case REF_07:
				cal.add(Calendar.MONTH, -18);
				break;
			case REF_08:
				cal.add(Calendar.MONTH, -17);
				break;
			case REF_09:
				cal.add(Calendar.MONTH, -16);
				break;
			case REF_10:
				cal.add(Calendar.MONTH, -15);
				break;
			case REF_11:
				cal.add(Calendar.MONTH, -14);
				break;
			case REF_12:
				cal.add(Calendar.MONTH, -13);
				break;
			case REF_13:
				cal.add(Calendar.MONTH, -12);
				break;
			case REF_14:
				cal.add(Calendar.MONTH, -11);
				break;
			case REF_15:
				cal.add(Calendar.MONTH, -10);
				break;
			case REF_16:
				cal.add(Calendar.MONTH, -9);
				break;
			case REF_17:
				cal.add(Calendar.MONTH, -8);
				break;
			case REF_18:
				cal.add(Calendar.MONTH, -7);
				break;
			case REF_19:
				cal.add(Calendar.MONTH, -6);
				break;
			case REF_20:
				cal.add(Calendar.MONTH, -5);
				break;
			case REF_21:
				cal.add(Calendar.MONTH, -4);
				break;
			case REF_22:
				cal.add(Calendar.MONTH, -3);
				break;
			case REF_23:
				cal.add(Calendar.MONTH, -2);
				break;
			case REF_24:
				cal.add(Calendar.MONTH, -1);
				break;

		}
		// Calendarの仕様上、setメソッドでは反映されないため、getTime()を呼び出しを行う
		return cal.getTime();
	}
}
