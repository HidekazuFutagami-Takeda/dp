package jp.co.takeda.bean;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyStringImpl;

/**
 * 定数を定義するクラス
 *
 * @author tkawabata
 */
public class Constants {

	/**
	 * セッションへのアクセス許可フラグのキー値
	 */
	public static final BoxKey SESSION_ACCESS_PERMISSION_KEY_R = new BoxKeyStringImpl("SESSION_ACCESS_PERMISSION_KEY_R");

	/**
	 * セッションへのアクセス許可フラグの値
	 */
	public static final Boolean SESSION_ACCESS_PERMISSION_FLG_R = Boolean.valueOf(true);

	/**
	 * ログイン情報をセッションに格納するボックスキー
	 */
	public static final BoxKey DP_LOGIN_USER_INFO_KEY_S = new BoxKeyStringImpl("DP_LOGIN_USER_INFO_S");

	/**
	 * アプリケーションディレクトリを表す文字列
	 */
	public static final String APPLICATION_DIRECTORY = "app";

	/**
	 * サービス停止前のメッセージキー値
	 */
	public static final String SERVICE_STOP_MSG_KEY = "DPC0001S";

	/**
	 * デフォルト比率フォーマット
	 */
	public static final String DEFAULT_RATE_FORMAT = "##########0.0";

	/**
	 * 変換マップ(初期化後変更不可)
	 */
	public static final Map<String, String> SANITIZING_MAP;

	/**
	 * 計画立案対象外表示ラベル
	 */
	public static final String PLAN_TAIGAI_LABEL = "X";

	//add Start 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
	/**
	 * VxBUのカテゴリ
	 */
	public static final String CATEGORY_VXBU = "04";

	/**
	 * 全て（医薬）のカテゴリ
	 */
	public static final String CATEGORY_IYAKU_ALL = "99";

	/**
	 * 全て（医薬）のカテゴリ名称
	 */
	public static final String CATEGORY_IYAKU_ALL_NM = "全て（医薬）";
	//add End 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加

	static {

		// 変換順序を意識する
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("&", "&amp;");
		map.put("<", "&lt;");
		map.put(">", "&gt;");
		map.put("\"", "&quot;");
		map.put("\'", "&#39;");

		// 変更不可として生成
		SANITIZING_MAP = Collections.unmodifiableMap(map);
	}

	/**
	 * Spring管理Beanの名前を定義する列挙
	 *
	 * @author tkawabata
	 */
	public enum SpringBeanName {

		/**
		 * リクエストボックス名
		 */
		REQUEST_BOX("requestBox"),

		/**
		 * セッションボックス名
		 */
		SESSION_BOX("sessionBox");

		/**
		 * コンストラクタ
		 *
		 * @param value
		 */
		private SpringBeanName(String value) {
			this.value = value;
		}

		/**
		 * value
		 */
		public final String value;
	}

	/**
	 * テンプレートファイル情報を表すクラス
	 *
	 * @author stakeuchi
	 */
	public enum TemplateInfo {

		/**
		 * EXCELテンプレートが配置されたアプリケーションパス
		 */
		EXCEL_TEMPLATE_PATH("/WEB-INF/conf/poi/"),

		/**
		 * ④チーム担当者計画検討表 ファイル名
		 */
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		EXCEL_TEMPLETE_DPS302P0002("TEMPLETE_DPS302P0002.xls"),
		EXCEL_TEMPLETE_DPS302P0002("TEMPLETE_DPS302P0002.xlsx"),

		/**
		 * ①担当者別品目別計画一覧 ファイル名(35品目パターン)
		 */
//		EXCEL_TEMPLATE_DPS302P0003_1("TEMPLETE_DPS302P0003_1.xls"),
		EXCEL_TEMPLATE_DPS302P0003_1("TEMPLETE_DPS302P0003_1.xlsx"),

		/**
		 * ①担当者別品目別計画一覧 ファイル名(50品目パターン)
		 */
//		EXCEL_TEMPLATE_DPS302P0003_2("TEMPLETE_DPS302P0003_2.xls"),
		EXCEL_TEMPLATE_DPS302P0003_2("TEMPLETE_DPS302P0003_2.xlsx"),

		/**
		 * ②品目別担当者別計画検討表 ファイル名
		 */
//		EXCEL_TEMPLATE_DPS302P0004("TEMPLETE_DPS302P0004.xls"),
		EXCEL_TEMPLATE_DPS302P0004("TEMPLETE_DPS302P0004.xlsx"),

		/**
		 * ③担当者別品目別計画検討表 ファイル名
		 */
//		EXCEL_TEMPLATE_DPS302P0005("TEMPLETE_DPS302P0005.xls"),
		EXCEL_TEMPLATE_DPS302P0005("TEMPLETE_DPS302P0005.xlsx"),

		/**
		 * (医)施設特約店別計画配分ミスリスト(営業所計画) ファイル名
		 */
//		EXCEL_TEMPLATE_DPS400P0001("TEMPLETE_DPS400P0001.xls"),
		EXCEL_TEMPLATE_DPS400P0001("TEMPLETE_DPS400P0001.xlsx"),

		/**
		 * (医)施設特約店別計画配分ミスリスト(担当者計画) ファイル名
		 */
//		EXCEL_TEMPLATE_DPS400P0002("TEMPLETE_DPS400P0002.xls"),
		EXCEL_TEMPLATE_DPS400P0002("TEMPLETE_DPS400P0002.xlsx"),

		/**
		 * (ワ)施設特約店別計画配分ミスリスト(担当者計画) ファイル名
		 */
//		EXCEL_TEMPLATE_DPS400P0003("TEMPLETE_DPS400P0003.xls"),
		EXCEL_TEMPLATE_DPS400P0003("TEMPLETE_DPS400P0003.xlsx"),

		/**
		 * (仕)関係会社別施設特約店別一覧 ファイル名
		 */
//		EXCEL_TEMPLATE_DPS401P0001("TEMPLETE_DPS401P0001.xls"),
		EXCEL_TEMPLATE_DPS401P0001("TEMPLETE_DPS401P0001.xlsx"),

		/**
		 * (ワ)施設計画市区郡町村別集計結果 ファイル名
		 */
//		EXCEL_TEMPLATE_DPS401P0002("TEMPLETE_DPS401P0002.xls"),
		EXCEL_TEMPLATE_DPS401P0002("TEMPLETE_DPS401P0002.xlsx"),

		/**
		 * (医)特約店別計画 ファイル名
		 */
//		EXCEL_TEMPLATE_DPS502P0001("TEMPLETE_DPS502P0001.xls"),
		EXCEL_TEMPLATE_DPS502P0001("TEMPLETE_DPS502P0001.xlsx"),

		/**
		 * (ワ)特約店別計画 ファイル名
		 */
//		EXCEL_TEMPLATE_DPS502P0002("TEMPLETE_DPS502P0002.xls"),
		EXCEL_TEMPLATE_DPS502P0002("TEMPLETE_DPS502P0002.xlsx"),

		/**
		 * (医)特約店別計画 ファイル名【実績なし用】
		 */
//		EXCEL_TEMPLATE_DPS502P0003("TEMPLETE_DPS502P0003.xls"),
		EXCEL_TEMPLATE_DPS502P0003("TEMPLETE_DPS502P0003.xlsx"),

		/**
		 * (ワ)特約店別計画 ファイル名【実績なし用】
		 */
//		EXCEL_TEMPLATE_DPS502P0004("TEMPLETE_DPS502P0004.xls"),
		EXCEL_TEMPLATE_DPS502P0004("TEMPLETE_DPS502P0004.xlsx"),

		/**
		 * (医)施設医師別計画配分ミスリストファイル名
		 */
//		EXCEL_TEMPLETE_DPS600P0001("TEMPLETE_DPS600P0001.xls"),
		EXCEL_TEMPLETE_DPS600P0001("TEMPLETE_DPS600P0001.xlsx"),

		/**
		 * (医)計画立案準備営業所計画アップロード用のフォーマットファイル名
		 */
//		EXCEL_TEMPLETE_DPS920P0001("TEMPLETE_DPS920P0001.xls"),
		EXCEL_TEMPLETE_DPS920P0001("TEMPLETE_DPS920P0001.xlsx"),


		// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		/**
		 * 削除施設・調整あり計画データファイル名（仕入以外）
		 */
//		EXCEL_TEMPLETE_DPS920P0002("TEMPLETE_DPS920P0002.xls"),
		EXCEL_TEMPLETE_DPS920P0002("TEMPLETE_DPS920P0002.xlsx"),

		/**
		 * 削除施設・調整あり計画データファイル名（仕入）
		 */
//		EXCEL_TEMPLETE_DPS920P0002SHIIRE("TEMPLETE_DPS920P0002_shiire.xls"),
		EXCEL_TEMPLETE_DPS920P0002SHIIRE("TEMPLETE_DPS920P0002_shiire.xlsx"),
		// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

		// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
		/**
		 * 施設特約店計画ファイル名
		 */
		EXCEL_TEMPLETE_DPS920P0003("TEMPLETE_DPS920P0003.xlsx"),
		// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加

		/**
		 * (医)組織別計画編集 ファイル名
		 */
//		EXCEL_TEMPLATE_DPM100P01("TEMPLETE_DPM100P01.xls"),
		EXCEL_TEMPLATE_DPM100P01("TEMPLETE_DPM100P01.xlsx"),

		/**
		 * (医)組織品目別計画編集 ファイル名
		 */
//		EXCEL_TEMPLATE_DPM101P01("TEMPLETE_DPM101P01.xls"),
		EXCEL_TEMPLATE_DPM101P01("TEMPLETE_DPM101P01.xlsx"),


		/**
		* (医)施設別計画編集画面
		 */
//		EXCEL_TEMPLATE_DPM200P01("TEMPLETE_DPM200P01.xls"),
		EXCEL_TEMPLATE_DPM200P01("TEMPLETE_DPM200P01.xlsx"),

		/**
		 * (ワ)施設別計画編集画面
		 */
//		EXCEL_TEMPLATE_DPM200P02("TEMPLETE_DPM200P02.xls"),
		EXCEL_TEMPLATE_DPM200P02("TEMPLETE_DPM200P02.xlsx"),

		/**
		 * (医)施設品目別計画編集画面
		 */
//		EXCEL_TEMPLATE_DPM201P01("TEMPLETE_DPM201P01.xls"),
		EXCEL_TEMPLATE_DPM201P01("TEMPLETE_DPM201P01.xlsx"),

		/**
		 * (医)施設特約店別計画編集画面
		 */
//		EXCEL_TEMPLATE_DPM400P01("TEMPLETE_DPM400P01.xls"),
		EXCEL_TEMPLATE_DPM400P01("TEMPLETE_DPM400P01.xlsx"),

		/**
		 * (ワ)施設特約店別計画編集画面
		 */
//		EXCEL_TEMPLATE_DPM400P02("TEMPLETE_DPM400P02.xls"),
		EXCEL_TEMPLATE_DPM400P02("TEMPLETE_DPM400P02.xlsx"),

		/**
		 * (医)施設特約店別品目計画編集 ファイル名
		 */
//		EXCEL_TEMPLATE_DPM401P01("TEMPLETE_DPM401P01.xls"),
		EXCEL_TEMPLATE_DPM401P01("TEMPLETE_DPM401P01.xlsx"),

		/**
		 * (医特約店別計画編集 ファイル名
		 */
//		EXCEL_TEMPLATE_DPM300P01("TEMPLETE_DPM300P01.xls"),
		EXCEL_TEMPLATE_DPM300P01("TEMPLETE_DPM300P01.xlsx"),

        /**
         * 特約店品目別計画編集 ファイル名
         */
//        EXCEL_TEMPLATE_DPM301P01("TEMPLETE_DPM301P01.xls"),
		EXCEL_TEMPLATE_DPM301P01("TEMPLETE_DPM301P01.xlsx"),

		/**
		 * 組織別月別計画編集 ファイル名
		 */
//		EXCEL_TEMPLATE_DPM500P01("TEMPLETE_DPM500P01.xls"),
		EXCEL_TEMPLATE_DPM500P01("TEMPLETE_DPM500P01.xlsx"),

		/**
		 * 品目別月別計画編集 ファイル名
		 */
//		EXCEL_TEMPLATE_DPM501P01("TEMPLETE_DPM501P01.xls"),
		EXCEL_TEMPLATE_DPM501P01("TEMPLETE_DPM501P01.xlsx"),
        ;
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応




		/**
		 * 値
		 */
		private String value;

		/**
		 * コンストラクタ
		 *
		 * @param value 値
		 */
		private TemplateInfo(String value) {
			this.value = value;
		}

		/**
		 * 値を取得する。
		 *
		 * @return 値
		 */
		public String getValue() {
			return value;
		}

		/**
		 * テンプレートパスを取得する。
		 *
		 * @param templateRootPath テンプレート配置パス(絶対パス名)
		 * @return テンプレートパス(絶対パス名)
		 */
		public String getTemplatePath(String templateRootPath) {
			return templateRootPath + value;
		}
	}
}
