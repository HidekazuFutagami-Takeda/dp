package jp.co.takeda.service;

import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUserInfo;

/**
 * 納入計画システムのユーザを扱うサービスインターフェイス
 *
 * @author tkawabata
 */
public interface DpcUserSearchService {

	/**
	 * 正規従業員区分配列
	 */
	public static final JgiKb[] OFFICIAL_JGI_KB_ARRAY = { JgiKb.JGI, JgiKb.CONTRACT_MR };

	/**
	 * 運営従業員区分配列
	 */
	public static final JgiKb[] OPERATOR_JGI_KB_ARRAY = { JgiKb.TAKEDA_JIMU};

	/**
	 * 代行従業員区分配列
	 */
	public static final JgiKb[] ALT_JGI_KB_ARRAY = { JgiKb.DAIKOU_USER };

	/**
	 * デフォルト組織従業員の設定を変更する。
	 *
	 * @param defaultFlg デフォルトフラグの設定(NULL可) 組織従業員をデフォルトの状態に戻すか
	 * @param initDispFlg 初期表示フラグの設定(NULL可) 初期表示設定にするか
	 * @param sosCd 組織コード(NULL可)
	 * @param jgiNo 従業員番号文字列(NULL可)
	 */
	void changeDefaultSosJgi(String defaultFlg, String initDispFlg, String sosCd, String jgiNo);

//	/**
//	 * 設定ユーザを指定の従業員番号の従業員に変更する。
//	 *
//	 * @param jgiNo 変更対象の従業員番号
//	 * @param dpMetaInfo メタ情報
//	 */
//	void switchUser(Integer jgiNo, DpMetaInfo dpMetaInfo);
//
	/**
	 * データアクセス認証が許可されているＩＤかを検証する。<br>
	 * 許可されないＩＤの場合、<code>AccessDeniedException</code>をスローする。
	 *
	 * @param permitIdList 許可ＩＤのリスト
	 * @param dpMetaInfo許可されないＩＤの場合
	 * @throws AccessDeniedException 許可されないＩＤの場合
	 */
	void dataAccessPermitCheck(String[] permitIdList, DpMetaInfo dpMetaInfo) throws AccessDeniedException;

	/**
	 * 認証されているかを検証する。
	 *
	 * @param dpUserInfo ユーザ管理情報
	 * @param dpMetaInfo メタ情報
	 * @return 認証されている場合は真を返す
	 */
	boolean isAuthorized(DpUserInfo dpUserInfo, DpMetaInfo dpMetaInfo);

	/**
	 * 代行ユーザとしてログイン可能かを判定する。
	 *
	 * @param altJgiNo 代行ユーザの従業員番号
	 * @param dpMetaInfo メタ情報
	 * @return 代行ユーザとしてログイン可能な場合は真を返す
	 */
	boolean canAltLogin(Integer altJgiNo, DpMetaInfo dpMetaInfo);

	/**
	 * 納入計画システムのユーザ情報を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param altJgiNo 代行ユーザの従業員番号
	 * @param メタ情報
	 * @return ユーザ情報
	 * @throws DataNotFoundException 対象ユーザが見つからない場合にスロー
	 */
	DpUserInfo searchDpUserInfo(Integer jgiNo, Integer altJgiNo, DpMetaInfo dpMetaInfo) throws DataNotFoundException;


	/**
	 * ユーザＩＤから従業員番号を取り出す
	 * @param userId 画面のユーザＩＤ
	 * @return 従業員番号
	 * @throws DataNotFoundException  従業員番号が見つからない場合にスロー
	 */
	int selectLoginJgiNoByUserId(String userId) throws DataNotFoundException;

	/**
	 * ログインした従業員に関連するデフォルトの品目カテゴリを取得する。
	 *
	 * <pre>
	 * 支店長、支店スタッフの場合⇒仕入(一般・麻薬)
	 * 営業所長、営業所スタッフ、ＡＬ、ＭＲの場合⇒(ＭＭＰ品)
	 * その他の場合⇒null
	 * </pre>
	 *
	 * @return デフォルトの品目カテゴリ
	 */
	String searchDefaultProdCategoryCode();
}
