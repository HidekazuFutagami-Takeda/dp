package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * サブコード分類を表す列挙
 * 
 * @author tkawabata
 */
public enum OldInsrFlg implements DbValue<String> {

	/**
	 * 一般施設
	 */
	IPPAN_INS("0"),

	/**
	 * A調剤薬局
	 */
	A_THOUZAI("1"),

	/**
	 * 特老人
	 */
	TOKU_ROUJIN("2"),

	/**
	 * 医療モール
	 */
	IRYO_MOLL("3"),

	/**
	 * 医療モール内診療所
	 */
	IRYO_MOLL_BYOUIN("4"),

	/**
	 * 医療モール内調剤薬局
	 */
	IRYO_MOLL_THOUZAI("5");

	/**
	 * コンストラクタ
	 * 
	 * @param value サブコード分類を表す文字
	 */
	private OldInsrFlg(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * サブコード分類を表す文字を取得する。
	 * 
	 * @return サブコード分類を表す文字
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
	public static OldInsrFlg getInstance(String code) {
		if (code == null) {
			return null;
		}

		for (OldInsrFlg entry : OldInsrFlg.values()) {
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
	public static class OldInsrFlgTypeHandler extends AbstractEnumTypeHandler {

		public OldInsrFlgTypeHandler() {
			super(new EnumType(OldInsrFlg.class, java.sql.Types.VARCHAR));
		}

	}
}
