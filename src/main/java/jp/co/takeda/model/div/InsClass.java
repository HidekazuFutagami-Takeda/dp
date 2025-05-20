package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 施設分類を表す列挙
 * 
 * @author tkawabata
 */
public enum InsClass implements DbValue<String> {

	/**
	 * 病院・診療所
	 */
	BYOUIN("01"),

	/**
	 * 調剤薬局
	 */
	THOUZAI("02"),

	/**
	 * 介護福祉施設
	 */
	KAIGO("03"),

	/**
	 * 保健所・役所
	 */
	HOKENSHO("04"),

	/**
	 * 二次店・雑他
	 */
	NIJITEN("05"),

	/**
	 * 医療モール
	 */
	IRYO_MOLL("06");

	/**
	 * コンストラクタ
	 * 
	 * @param value 施設分類を表す文字
	 */
	private InsClass(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 施設分類を表す文字を取得する。
	 * 
	 * @return 施設分類を表す文字
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
	public static InsClass getInstance(String code) {
		if (code == null) {
			return null;
		}

		for (InsClass entry : InsClass.values()) {
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
	public static class InsClassTypeHandler extends AbstractEnumTypeHandler {

		public InsClassTypeHandler() {
			super(new EnumType(InsClass.class, java.sql.Types.VARCHAR));
		}
	}

	/**
	 * 施設分類コードとサブコードから「本院」・「A調」・「B調」の<br>
	 * いずれかの文字列を取得する。
	 * 
	 * @param insClass 施設分類を表す列挙
	 * @param oldInsFlg サブコード分類を表す列挙
	 * @return 「本院」・「A調」・「B調」のいずれかの文字列
	 */
	public static String getInsClassName(InsClass insClass, OldInsrFlg oldInsFlg) {

		String insClassName = "";

		// 引数がNullの場合は空文字をRETURN
		if (insClass == null || oldInsFlg == null) {
			return insClassName;
		}

		switch (insClass) {
			case THOUZAI:
				switch (oldInsFlg) {
					case A_THOUZAI:
						insClassName = "A調";
						break;
					default:
						insClassName = "B調";
						break;
				}
				break;
			default:
				insClassName = "本院";
				break;
		}
		return insClassName;
	}
}
