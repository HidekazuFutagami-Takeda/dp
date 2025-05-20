package jp.co.takeda.web.cmn.velocity;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.web.cmn.delegate.ServiceControlDelegate;

/**
 * 画面側定数にアクセスするためのVelocityTool<br>
 *
 * <pre>
 * <li>汎用性のある定数だけを定義する。</li>
 * <li>定義する場合、管理者に確認する。(定義しすぎない）</li>
 * <li>htmlのタグは含めない。タグはテンプレート(.vm)に記載する。</li>
 * </pre>
 *
 * @author tkawabata
 */
public class ConstantsTool {

	// ---------------------------------------
	// CONSTANTS FIELD
	// ---------------------------------------

	/**
	 * サービス停止予告メッセージを取得するキー値
	 */
	private static final BoxKey SERVICE_STOP_ANNOUNCE_MSG_KEY = ServiceControlDelegate.ServiceStopAnnounce_DATA_R;

	/**
	 * ヘッダ情報(ベース無)
	 */
	private static final String TABLE_HEADER = "単位:千円";

	/**
	 * ヘッダ情報(Ｙベース)
	 */
	private static final String TABLE_HEADER_Y = "単位:千円　価格:Ｙ価ベース";

	/**
	 * ヘッダ情報(Ｔベース)
	 */
	private static final String TABLE_HEADER_T = "単位:千円　価格:Ｔ価ベース";

	/**
	 * ヘッダ情報(Ｂベース)
	 */
	private static final String TABLE_HEADER_B = "単位:千円　価格:Ｂ価ベース";

	/**
	 * ヘッダ情報(Ｓベース)
	 */
	private static final String TABLE_HEADER_S = "単位:千円　価格:Ｓ価ベース";

	/**
	 * DHTMLX Gridのxmlヘッダ
	 */
	private static final String DHTMLX_GRID_XML_HEADER = "<?xml version='1.0' encoding='UTF-8'?>";

	/**
	 * フリー項目１名称（デフォルト）
	 */
	public static final String DEFAULT_INDEX_FREE_NAME1 = EstimationParam.DEFAULT_INDEX_FREE_NAME1;

	/**
	 * フリー項目２名称（デフォルト）
	 */
	public static final String DEFAULT_INDEX_FREE_NAME2 = EstimationParam.DEFAULT_INDEX_FREE_NAME2;

	/**
	 * フリー項目３名称（デフォルト）
	 */
	public static final String DEFAULT_INDEX_FREE_NAME3 = EstimationParam.DEFAULT_INDEX_FREE_NAME3;

	/**
	 * 帳票ヘッダ情報(Ｙベース)
	 */
	public static final String FILE_TABLE_HEADER_Y = "（単位:千円　価格:Ｙ価ベース）";

	/**
	 * 帳票ヘッダ情報(Ｂベース)
	 */
	public static final String FILE_TABLE_HEADER_B = "（単位:千円　価格:Ｂ価ベース）";

	/**
	 * 1ページ毎の表示件数
	 */
	private static final int PAGE_COUNT = 20;

	// ---------------------------------------
	// GETTER & SETTER
	// ---------------------------------------
	/**
	 * サービス停止予告メッセージを取得するキー値を取得する。
	 *
	 * @return サービス停止予告メッセージを取得するキー値
	 */
	public static BoxKey getServiceStopAnnounceMSGKey() {
		return SERVICE_STOP_ANNOUNCE_MSG_KEY;
	}

	/**
	 * ヘッダ情報(ベース無)を取得する。
	 *
	 * @return ヘッダ情報(ベース無)
	 */
	public String getTableHeader() {
		return TABLE_HEADER;
	}

	/**
	 * ヘッダ情報(Ｙベース)を取得する。
	 *
	 * @return ヘッダ情報(Ｙベース)
	 */
	public String getTableHeaderY() {
		return TABLE_HEADER_Y;
	}

	/**
	 * ヘッダ情報(Ｔベース)を取得する。
	 *
	 * @return ヘッダ情報(Ｙベース)
	 */
	public String getTableHeaderT() {
		return TABLE_HEADER_T;
	}

	/**
	 * ヘッダ情報(Ｂベース)を取得する。
	 *
	 * @return ヘッダ情報(Ｂベース)
	 */
	public String getTableHeaderB() {
		return TABLE_HEADER_B;
	}

	/**
	 * ヘッダ情報(Ｓベース)を取得する。
	 *
	 * @return ヘッダ情報(Ｓベース)
	 */
	public String getTableHeaderS() {
		return TABLE_HEADER_S;
	}


	/**
	 * DHTMLX Gridのxmlヘッダを取得する。
	 *
	 * @return DHTMLX Gridのxmlヘッダ
	 */
	public String getDhtmlxGridXmlHeader() {
		return DHTMLX_GRID_XML_HEADER;
	}

	/**
	 * 1ページ毎の表示件数を取得する。
	 *
	 * @return 1ページ毎の表示件数
	 */
	public int getPageCount() {
		return PAGE_COUNT;
	}

	/**
	 * フリー項目１名称（デフォルト）を取得する。
	 *
	 * @return フリー項目１名称（デフォルト）
	 */
	public String getDefaultIndexFreeName1() {
		return DEFAULT_INDEX_FREE_NAME1;
	}

	/**
	 * フリー項目２名称（デフォルト）を取得する。
	 *
	 * @return フリー項目２名称（デフォルト）
	 */
	public String getDefaultIndexFreeName2() {
		return DEFAULT_INDEX_FREE_NAME2;
	}

	/**
	 * フリー項目３名称（デフォルト）を取得する。
	 *
	 * @return フリー項目３名称（デフォルト）
	 */
	public String getDefaultIndexFreeName3() {
		return DEFAULT_INDEX_FREE_NAME3;
	}


	// ----------------------------------------
	// PRIVATE METHOD
	// ----------------------------------------
}
