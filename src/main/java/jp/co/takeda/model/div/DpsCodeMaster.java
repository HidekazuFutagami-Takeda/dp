package jp.co.takeda.model.div;

import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;

/**
 * 計画支援のコードマスタ用パラメータ
 *
 * @author khashimoto
 */
public enum DpsCodeMaster implements DbValue<String> {

	/**
	 * カテゴリ
	 */
	CAT("category"),

	/**
	 * 活動先
	 */
	ACT("activity_type"),

	/**
	 * 対象区分
	 */
	IT("ins_type"),

	/**
	 * 対象区分（雑含む）
	 */
	ITZ("ins_type_z"),

	/**
	 * 対象区分（ワクチン含む）
	 */
	ITV("ins_type_v"),

	/**
	 * ワクチンのカテゴリコード
	 */
	VAC("vaccine"),

	/**
	 * 仕入のカテゴリコード
	 */
	SII("siire"),

	/**
	 * CVのカテゴリコード
	 */
	CV("cv"),

	// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	/**
	 * チェックツールのカテゴリコード
	 */
	CHE("check_name"),
	;
	// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	/**
	 * コンストラクタ
	 *
	 * @param value コードマスタのデータ区分
	 */
	private DpsCodeMaster(String value) {
		this.value = value;
	}

	/**
	 * コードマスタのデータ区分
	 */
	private String value;

	/**
	 * データ区分を表す文字を取得する。
	 *
	 * @return データ区分を表す文字
	 */
	public String getDbValue() {
		return value;
	}

	/**
	 * DBとのマッピングを行うタイプハンドラークラス
	 *
	 * @author khashimoto
	 */
	public static class DpsCodeMasterTypeHandler extends AbstractEnumTypeHandler {

		public DpsCodeMasterTypeHandler() {
			super(new EnumType(DpsCodeMaster.class, java.sql.Types.VARCHAR));
		}

	}

}
