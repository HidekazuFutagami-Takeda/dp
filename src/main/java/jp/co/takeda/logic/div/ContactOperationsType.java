package jp.co.takeda.logic.div;

/**
 * 業務連絡の処理区分を表す列挙
 *
 * @author khashimoto
 */
public enum ContactOperationsType {

	/**
	 * (医)試算処理
	 */
	MR_PLAN_EST("試算処理"),

	/**
	 * (ワ)試算処理
	 */
	MR_PLAN_EST_VAC("試算処理ワクチン"),

	/**
	 * (医)施設医師配分処理
	 */
	INS_DOC_DIST_MMP("施設医師配分処理"),

	/**
	 * (医)施設医師配分処理(再配分)
	 */
	INS_DOC_RE_DIST_MMP("施設医師配分処理(再配分)"),

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理のMMPとONCとSPBUを”INS_WS_DIST”というタイプにまとめる。カテゴリ名は別ルートで取得する。
//	/**
//	 * (医)施設特約店配分処理(MMP品目)
//	 */
//	INS_WS_DIST_MMP("(医)施設特約店配分処理(JPBU(STARS))"),
//
//	/**
//	 * (医)施設特約店配分処理(ONC品目)
//	 */
//	INS_WS_DIST_ONC("(医)施設特約店配分処理(ONC品目)"),
	/**
	 * (医)施設特約店配分処理　（仕入品以外）※2018上期時点でGMBU品目、SPBU品目、ONC品目用
	 */
	INS_WS_DIST("施設特約店配分処理"),

//	/**
//	 * (医)施設特約店配分処理(一般・麻薬)
//	 */
//	INS_WS_DIST_SHIIRE("(医)施設特約店配分処理(仕入品(一般・麻薬))"),

	/**
	 * (医)施設特約店配分処理(一般・麻薬)
	 */
	INS_WS_DIST_SHIIRE("施設特約店配分処理"), //仕入品は処理の分岐が必要なため、enumはMMP等とは別のままにしておく。カテゴリ名は別ルートで取得するため削除。
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理のMMPとONCとSPBUを”INS_WS_DIST”というタイプにまとめる。カテゴリ名は別ルートで取得する。

	/**
	 * (ワ)施設特約店配分処理
	 */
	INS_WS_DIST_VAC("施設特約店配分処理"),

	/**
	 * (医)特約店別計画配分処理
	 */
	WS_DIST("特約店別計画配分処理"),

	/**
	 * (医)スライド処理
	 */
	WS_SLIDE_IYAKU("スライド処理"),

	/**
	 * (ワ)スライド処理
	 */
	WS_SLIDE_VAC("スライド処理");

	/**
	 * 処理名
	 */
	private String value;

	/**
	 * コンストラクタ
	 *
	 * @param value 処理名
	 */
	private ContactOperationsType(String value) {
		this.value = value;
	}

	/**
	 * 処理名を取得する。
	 *
	 * @return 処理名
	 */
	public String getValue() {
		return value;
	}
}
