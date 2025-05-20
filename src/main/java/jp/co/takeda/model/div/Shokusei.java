package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 職制を表す列挙
 * 
 * @author tkawabata
 */
public enum Shokusei implements DbValue<String> {

	/**
	 * 取締役会長
	 */
	KAICHO("105"),

	/**
	 * 取締役社長
	 */
	SYACHO("110"),

	/**
	 * 専務取締役
	 */
	SENMU("120"),

	/**
	 * 常務取締役
	 */
	JYOMU("125"),

	/**
	 * 取締役
	 */
	TORISHIMARIYAKU("130"),

	/**
	 * 常勤監査役
	 */
	JYOKIN_KANSA("135"),

	/**
	 * 監査役
	 */
	KANSA("140"),

	/**
	 * 顧問
	 */
	KOMON("150"),

	/**
	 * プレジデント
	 */
	PRESIDENT("207"),

	/**
	 * 本部長
	 */
	HONBU_CHO("210"),

	/**
	 * 研究所長
	 */
	KENKYUSYO_CHO("220"),

	/**
	 * 支店長
	 */
	SITEN_CHO("225"),

	/**
	 * 工場長
	 */
	KOJYO_CHO("230"),

	/**
	 * センタ－所長
	 */
	CENTAR_CHO("245"),

	/**
	 * 副本部長
	 */
	FUKU_HONBU_CHO("310"),

	/**
	 * 園長
	 */
	EN_CHO("427"),

	/**
	 * 農場長
	 */
	NOJYO_CHO("430"),

	/**
	 * 部長
	 */
	BU_CHO("435"),

	/**
	 * 営業所長
	 */
	EIGYOSHO_CHO("505"),

	/**
	 * 事務所長
	 */
	JIMUSHO_CHO("508"),

	/**
	 * 室長
	 */
	SHITU_CHO("510"),

	/**
	 * シニアマネジャー
	 */
	SENIOR_M("512"),

	/**
	 * メディカルダイレクター
	 */
	MEDICAL_M("514"),

	/**
	 * グル－プマネジャ－
	 */
	GROUP_M("515"),

	/**
	 * リサーチマネジャー
	 */
	RESEARCH_M("517"),

	/**
	 * ＭＰＤＲＡＰ領域Ｌ
	 */
	MPDRAP("527"),

	/**
	 * プロダクトマネジャ－
	 */
	PRODUCT_M("530"),

	/**
	 * シニアＭＲ
	 */
	SENIOR_MR("534"),

	/**
	 * チーフモニター
	 */
	CHIEF_MONITER("550"),

	/**
	 * AL
	 */
	AL("575"),

	/**
	 * セ－ルスマネジャ－
	 */
	SALES_M("578"),

	/**
	 * ユニットリーダー
	 */
	UNIT_L("900"),

	/**
	 * 主席部員
	 */
	SYUSEKI_BUIN("960"),

	/**
	 * 職制999
	 */
	NONE("999");

	/**
	 * コンストラクタ
	 * 
	 * @param code 職制を表すコード
	 */
	private Shokusei(String code) {
		this.code = code;
	}

	/**
	 * RDBのコード値
	 */
	private String code;

	/**
	 * 職制を表す文字を取得する。
	 * 
	 * @return 職制を表す文字
	 */
	public String getDbValue() {
		return code;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static Shokusei getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (Shokusei entry : Shokusei.values()) {
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
	public static class ShokuseiTypeHandler extends AbstractEnumTypeHandler {

		public ShokuseiTypeHandler() {
			super(new EnumType(Shokusei.class, java.sql.Types.VARCHAR));
		}

	}
}
