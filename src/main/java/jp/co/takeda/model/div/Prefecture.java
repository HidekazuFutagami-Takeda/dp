package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 都道府県を表す列挙
 * 
 * @author khashimoto
 */
public enum Prefecture implements DbValue<String> {

	HOKAIDO("01", "北海道")

	, AOMORI("02", "青森県")

	, IWATE("03", "岩手県")

	, MIYAGI("04", "宮城県")

	, AKITA("05", "秋田県")

	, YAMAGATA("06", "山形県")

	, FUKUSHIMA("07", "福島県")

	, IBARAKI("08", "茨城県")

	, TOCHIGI("09", "栃木県")

	, GUNMA("10", "群馬県")

	, SAITAMA("11", "埼玉県")

	, CHIBA("12", "千葉県")

	, TOKYO("13", "東京都")

	, KANAGAWA("14", "神奈川県")

	, NIGATA("15", "新潟県")

	, TOYAMA("16", "富山県")

	, ISHIKAWA("17", "石川県")

	, FUKUI("18", "福井県")

	, YAMANASHI("19", "山梨県")

	, NAGANO("20", "長野県")

	, GIFU("21", "岐阜県")

	, SIZUOKA("22", "静岡県")

	, AICHI("23", "愛知県")

	, MIE("24", "三重県")

	, SIGA("25", "滋賀県")

	, KYOTO("26", "京都府")

	, OSAKA("27", "大阪府")

	, HYOGO("28", "兵庫県")

	, NARA("29", "奈良県")

	, WAKAYAMA("30", "和歌山県")

	, TOTORI("31", "鳥取県")

	, SHIMANE("32", "島根県")

	, OKAYAMA("33", "岡山県")

	, HIROSHIMA("34", "広島県")

	, YAMAGUCHI("35", "山口県")

	, TOKUSHIMA("36", "徳島県")

	, KAGAWA("37", "香川県")

	, EHIME("38", "愛媛県")

	, KOCHI("39", "高知県")

	, FUKUOKA("40", "福岡県")

	, SAGA("41", "佐賀県")

	, NAGASAKI("42", "長崎県")

	, KUMAMOTO("43", "熊本県")

	, OITA("44", "大分県")

	, MIYAZAKI("45", "宮崎県")

	, KAGOSHIMA("46", "鹿児島県")

	, OKINAWA("47", "沖縄県");

	/**
	 * コンストラクタ
	 * 
	 * @param value 都道府県を表すコード値
	 * @param value 都道府県の名称
	 */
	private Prefecture(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * RDBの値
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 都道府県を表すコード値を取得する。
	 * 
	 * @return 都道府県を表すコード値
	 */
	public String getDbValue() {
		return code;
	}

	/**
	 * 都道府県を表す名称を取得する。
	 * 
	 * @return 都道府県を表す名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static Prefecture getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (Prefecture entry : Prefecture.values()) {
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
	public static class PrefectureTypeHandler extends AbstractEnumTypeHandler {

		public PrefectureTypeHandler() {
			super(new EnumType(Prefecture.class, java.sql.Types.CHAR));
		}

	}
}
