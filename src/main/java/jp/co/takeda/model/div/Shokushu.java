package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 職種を表す列挙
 * 
 * @author tkawabata
 */
public enum Shokushu implements DbValue<String> {

	/**
	 * 事務
	 */
	JIMU("100"),

	/**
	 * 事務(出向)
	 */
	JIMU_SYUKO("101"),

	/**
	 * 事務(組合)
	 */
	JIMU_KUMIAI("109"),

	/**
	 * 営業
	 */
	EIGYO("300"),

	/**
	 * 営業(出向)
	 */
	EIGYO_SYUKO("301"),

	/**
	 * ワクチン
	 */
	WAKUTIN("350"),

	/**
	 * MR
	 */
	MR("400"),

	/**
	 * 整形MR
	 */
	SEIKEI("430"),

	/**
	 * SC（癌免疫）
	 */
	SC_GAN("410"),

	/**
	 * SC（循糖中）
	 */
	SC_JYUN("425"),

	/**
	 * 研究
	 */
	KENKYU("500"),

	/**
	 * 研究（出向）
	 */
	KENKYU_SYUKO("501"),

	/**
	 * 技術
	 */
	GIJYUTU("550"),

	/**
	 * 特殊技能
	 */
	TOKUSYU_GINO("600"),

	/**
	 * 製造
	 */
	SEIZO("700"),

	/**
	 * 製造（出向）
	 */
	SEIZO_SYUKO("701");

	/**
	 * コンストラクタ
	 * 
	 * @param code 職種を表すコード
	 */
	private Shokushu(String code) {
		this.code = code;
	}

	/**
	 * RDBのコード値
	 */
	private String code;

	/**
	 * 職種を表す文字を取得する。
	 * 
	 * @return 職種を表す文字
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
	public static Shokushu getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (Shokushu entry : Shokushu.values()) {
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
	public static class ShokushuTypeHandler extends AbstractEnumTypeHandler {

		public ShokushuTypeHandler() {
			super(new EnumType(Shokushu.class, java.sql.Types.VARCHAR));
		}

	}
}
