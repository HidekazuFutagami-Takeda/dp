package jp.co.takeda.model.div;

import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;

/**
 * 対象施設区分用パラメータ
 *
 * @author yyoshino
 */
public enum TgtInsKbn implements DbValue<String> {

	/**
	 * 雑含まない（UH/P）
	 */
	ZATSU_NASHI("1"),

	/**
	 * 雑含まない（UH/P）
	 */
	ZATSU_ARI("2"),
	;

	/**
	 * コンストラクタ
	 *
	 * @param value コードマスタのデータ区分
	 */
	private TgtInsKbn(String value) {
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
	public static class CodeMasterTypeHandler extends AbstractEnumTypeHandler {

		public CodeMasterTypeHandler() {
			super(new EnumType(TgtInsKbn.class, java.sql.Types.VARCHAR));
		}

	}



}
