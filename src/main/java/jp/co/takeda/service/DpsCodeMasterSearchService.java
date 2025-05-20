package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.div.JokenSet;

/**
 * カテゴリ・品目の検索に関するサービスインタフェース
 * @author rna8405
 */
public interface DpsCodeMasterSearchService {

	/**
	 * 検索条件をもとに、カテゴリを取得する。
	 * @param sosCategory 組織カテゴリ
	 * @return カテゴリ一覧
	 * @throws LogicalException
	 */
	List<DpsCCdMst> searchCategory(String sosCategory);

	/**
	 * 検索条件をもとに、カテゴリを取得する。
	 * @param sosCategory 組織カテゴリ
	 * @param dataValue 値
	 * @return カテゴリ一覧
	 * @throws LogicalException
	 */
	List<DpsCCdMst> searchCategoryList(String sosCategory, String dataValue);

	/**
	 * データ区分とカテゴリから、対象区分のタイトルを取得する。
	 * @param prodCategory 品目カテゴリ
	 * @return 対象区分タイトル
	 */
	List<DpsCCdMst> searchInsTypeTitle(String prodCategory);

	/**
	 * カテゴリから、対象区分のリストを取得する。
	 * @param prodCategory 品目カテゴリ
	 * @return 対象区分タイトル
	 */
	List<CodeAndValue> searchTgtInsKb(String prodCategory);

	/**
	 * カテゴリがワクチンかどうか判断する。
	 * @param prodCategory 品目カテゴリ
	 * @return ワクチンの場合true
	 */
	boolean isVaccine(String prodCategory);

	/**
	 * カテゴリが仕入かどうか判断する。
	 * @param prodCategory 品目カテゴリ
	 * @return 仕入の場合true
	 */
	boolean isSiire(String prodCategory);

	/**
	 * 計画管理汎用マスタからデータ区分をもとにデータを取得する。
	 * @param dataKbn データ区分
	 * @return コードマスタ一覧
	 */
	List<DpsCCdMst> searchCodeByDataKbn(String dataKbn);

	/**
	 * データ区分とコードから、名称を取得する。
	 * @param dataKbn データ区分
	 * @param dataCd コード
	 * @return 名称
	 */
	String searchDataName(String dataKbn, String dataCd);

	/**
	 * 組織コードと計画立案レベルを条件に、計画管理汎用マスタからカテゴリリストを取得する。
	 *
	 * @param sosCd
	 * @param planLevel
	 * @return
	 */
	List<DpsCCdMst> searchShienCategoryList(String sosCd, ProdPlanLevel planLevel);

	/**
	 * トップ画面 業務進捗状況 用のカテゴリを表示順序で取得する。
	 * @param dataKbn データ区分
	 * @return コードマスタ一覧
	 */
	List<DpsCCdMst> getCategoriesSortByProgressOrder();

	/**
	 * 特約店系条件セットコードのリストを返す
	 * @return
	 */
	List<JokenSet> searchTokuyakuJokenSetCd();
}
