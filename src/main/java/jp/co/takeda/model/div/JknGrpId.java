package jp.co.takeda.model.div;

import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;

/**
 * 条件グループIDを表す列挙
 *
 * @author yyoshino
 */
public enum JknGrpId implements DbValue<String> {

	// ----------------------------------
	// 管理
	// ----------------------------------
	/**
	 * メニュー
	 */
	MENU("DPM00", "メニュー"),

	/**
	 * 期別_全社-担当者計画
	 */
	TERM_ALL_MR("DPM10", "期別_全社-担当者計画"),

	/**
	 * 期別_施設別計画
	 */
	TERM_INS("DPM20", "期別_施設別計画"),

	/**
	 * 期別_施設特約店別計画
	 */
	TERM_INS_WS("DPM30", "期別_施設特約店別計画"),

	/**
	 * 期別_特約店別計画
	 */
	TERM_WS("DPM40", "期別_特約店別計画"),

	/**
	 * 月別_全社-担当者計画
	 */
	MON_ALL_MR("DPM50", "月別_全社-担当者計画"),

	/**
	 * 月別_施設別計画
	 */
	MON_INS("DPM60", "月別_施設別計画"),

	// ----------------------------------
	// 支援
	// ----------------------------------
	MENU_DPS("DPS000", "メニュー"),

	/**
	 * 配分除外施設
	 */
	HAIBUN_JYOGAI("DPS201", "配分除外施設"),

	/**
	 * 特定施設個別計画 営業所案
	 */
	TOKUTEI_SISETU_OFFICE_PLAN("DPS202C00", "特定施設個別計画 エリア案"),
//	TOKUTEI_SISETU_OFFICE_PLAN("DPS202C00", "特定施設個別計画 営業所案"),

	/**
	 * 特定施設個別計画立案 営業所案
	 */
	TOKUTEI_SISETU_OFFICE_PLAN_C01("DPS202C01", "特定施設個別計画立案 エリア案"),
//	TOKUTEI_SISETU_OFFICE_PLAN_C01("DPS202C01", "特定施設個別計画立案 営業所案"),

	/**
	 * 特定施設個別計画 担当者案
	 */
	TOKUTEI_SISETU_MR_PLAN("DPS202C02", "特定施設個別計画 担当者案"),

	/**
	 * 特定施設個別計画立案 担当者案
	 */
	TOKUTEI_SISETU_MR_PLAN_C03("DPS202C03", "特定施設個別計画立案 担当者案"),

	/**
	 * 未獲得市場
	 */
	MIKAKUTOKU("DPS203", "未獲得市場"),

	/**
	 * 営業所計画
	 */
	OFFICE_PLAN("DPS300", "エリア計画"),
//	OFFICE_PLAN("DPS300", "営業所計画"),

	/**
	 * 試算処理
	 */
	SISAN("DPS301", "試算処理"),

	/**
	 * 担当者別計画 メニュー用
	 */
	MR_PLAN("DPS302", "担当者別計画"),

	/**
	 * 担当者別計画 計画対象品目選択
	 */
	MR_PLAN_SELECT("DPS302C00", "担当者別計画 計画対象品目選択"),

	/**
	 * 担当者別計画 試算結果詳細
	 */
	MR_PLAN_EST_DETAIL("DPS302C01", "担当者別計画 試算結果詳細"),

	/**
	 * 担当者別計画 編集
	 */
	MR_PLAN_EDIT("DPS302C03", "担当者別計画編集"),

	/**
	 * 施設特約店別計画配分対象品目
	 */
	INS_TYTEN_PLAN_PROD_DIST_TARGET("DPS400", "施設特約店別計画配分対象品目"),

	/**
	 * 施設特約店別計画配分対象品目一覧
	 */
	INS_TYTEN_PLAN_PROD_DIST_TARGET_LIST("DPS400C00", "施設特約店別計画配分対象品目一覧"),

	/**
	 * 施設特約店別計画配分基準編集
	 */
	INS_TYTEN_PLAN_PROD_DIST_STANDARD_EDIT("DPS400C01", "施設特約店別計画配分基準編集"),

	/**
	 * 施設特約店別計画
	 */
	INS_TYTEN_PLAN("DPS401", "施設特約店別計画"),

	/**
	 * 施設特約店別計画担当者一覧
	 */
	INS_TYTEN_PLAN_MR_LIST("DPS401C00","施設特約店別計画担当者一覧"),

	/**
	 * 施設特約店別計画品目一覧
	 */
	INS_TYTEN_PLAN_PROD_LIST("DPS401C01","施設特約店別計画品目一覧"),

	/**
	 * 施設特約店別計画編集
	 */
	INS_TYTEN_PLAN_EDIT("DPS401C02","施設特約店別計画編集"),

	/**
	 * 特約店別計画配分対象品目
	 */
	TYTEN_PLAN_PROD("DPS500", "特約店別計画配分対象品目"),

	/**
	 * 特約店計画スライド
	 */
	TYTEN_PLAN_SLIDE("DPS501", "特約店計画スライド"),

	/**
	 * 特約店別計画
	 */
	TYTEN_PLAN("DPS502", "特約店別計画"),

	/**
	 * ダウンロード
	 */
	DOWNLOAD("DPS920", "ダウンロード"),

	/**
	 * 計画アップロード
	 */
	PLAN_UPLOAD("DPS930", "計画アップロード"),

	/**
	 * エラー判定用
	 */
	ERROR("ERROR", "エラー")

	;

	/**
	 * コンストラクタ
	 *
	 * @param businessName 業務名
	 */
	private JknGrpId(String dbValue, String jknGrpName) {
		this.dbValue = dbValue;
		this.jknGrpName = jknGrpName;
	}

	/**
	 * 業務ID
	 */
	private final String dbValue;

	/**
	 * 業務名
	 */
	private final String jknGrpName;


	@Override
	public String getDbValue() {
		return dbValue;
	}

	/**
	 * @return jknGrpName
	 */
	public String getJknGrpName() {
		return jknGrpName;
	}


	/**
	 * コード値から列挙を逆引きする。
	 *
	 * @param code コード値
	 * @return 列挙
	 */
	public static JknGrpId getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (JknGrpId entry : JknGrpId.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		// 未定義の条件セットの場合、エラーとしない
		return null;
	}

	/**
	 * DBとのマッピングを行うタイプハンドラークラス
	 *
	 * @author khashimoto
	 */
	public static class JknGrpIdTypeHandler extends AbstractEnumTypeHandler {

		public JknGrpIdTypeHandler() {
			super(new EnumType(JknGrpId.class, java.sql.Types.VARCHAR));
		}
	}

}
