package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * (簡略版)施設出力対象区分を表す列挙
 * 
 * @author tkawabata
 */
public enum InsType implements DbValue<String> {


	/**
	 * UHP
	 */
	UHP("0"),

	/**
	 * UH
	 */
	UH("1"),

	/**
	 * P
	 */
	P("2"),

	/**
	 * 雑
	 */
	ZATU("3");

	/**
	 * コンストラクタ
	 * 
	 * @param value 施設区分(UH/P)を表す文字
	 */
	private InsType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 施設区分(UH/P)を表す文字を取得する。
	 * 
	 * @return 施設区分(UH/P)を表す文字
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
	public static InsType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (InsType entry : InsType.values()) {
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
	 * @author khashimoto
	 */
	public static class InsTypeTypeHandler extends AbstractEnumTypeHandler {

		public InsTypeTypeHandler() {
			super(new EnumType(InsType.class, java.sql.Types.CHAR));
		}

	}

	/**
	 * (詳細版)対象区分を(簡易版)施設出力対象区分に変換する。
	 * 
	 * 
	 * @param hoInsType 対象区分
	 * @return 施設出力対象区分
	 */
	public static InsType convertInsType(HoInsType hoInsType) {

		// 引数がNullの場合はNULLを返す
		if (hoInsType == null) {
			return null;
		}

		InsType insType = null;
		switch (hoInsType) {
			case U:
			case H:
				insType = InsType.UH;
				break;
			case P:
				insType = InsType.P;
				break;
			case Z:
				insType = InsType.ZATU;
				break;
		}

		return insType;
	}

	/**
	 * (簡易版)施設出力対象区分を(詳細版)対象区分のリストに変換する。
	 * 
	 * 
	 * @param insType 施設出力対象区分
	 * @return 対象区分のリスト
	 */
	public static List<HoInsType> convertHoInsType(InsType insType) {

		// 引数がNullの場合はNULLを返す
		if (insType == null) {
			return null;
		}

		List<HoInsType> hoInsTypeList = new ArrayList<HoInsType>();
		switch (insType) {
			case UHP:
				hoInsTypeList.add(HoInsType.U);
				hoInsTypeList.add(HoInsType.H);
				hoInsTypeList.add(HoInsType.P);
				hoInsTypeList.add(HoInsType.Z);
				break;
			case UH:
				hoInsTypeList.add(HoInsType.U);
				hoInsTypeList.add(HoInsType.H);
				break;
			case P:
				hoInsTypeList.add(HoInsType.P);
				break;
			case ZATU:
				hoInsTypeList.add(HoInsType.Z);
				break;
		}

		return hoInsTypeList;
	}
}
