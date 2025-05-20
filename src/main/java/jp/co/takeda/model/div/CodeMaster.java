package jp.co.takeda.model.div;

import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;

/**
 * 計画管理のコードマスタ用パラメータ
 *
 * @author yyoshino
 */
public enum CodeMaster implements DbValue<String> {

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
	 * CVのカテゴリコード
	 */
	CV("cv"),

	/**
	 * 営業所のカテゴリコード
	 */
	OFFICE("office"),

	/**
	 * NSGのカテゴリコード
	 */
	NSG("nsg"),

	/**
	 * 仕入のカテゴリコード
	 */
	SIIRE("siire"),

	/**
	 * S価表示用（本部・流通政策部）の条件セットコード
	 */
	RYUTSU("jkn_ryutsu"),

	/**
	 * JRNSの品目分類コード
	 */
	JRNS("pcat_jrns")

	;

	/**
	 * コンストラクタ
	 *
	 * @param value コードマスタのデータ区分
	 */
	private CodeMaster(String value) {
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
			super(new EnumType(CodeMaster.class, java.sql.Types.VARCHAR));
		}

	}



}
