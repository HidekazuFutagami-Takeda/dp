package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.model.DpmCCdMst;

/**
 * カテゴリ・品目の検索に関するサービスインタフェース
 * @author rna8405
 */
public interface DpmCodeMasterSearchService {

	/**
	 * 検索条件をもとに、カテゴリを取得する。
	 * @param sosCategory 組織カテゴリ
	 * @return カテゴリ一覧
	 * @throws LogicalException
	 */
	List<DpmCCdMst> searchCategory(String sosCategory);

	/**
	 * 検索条件をもとに、カテゴリを取得する。
	 * @param sosCategory 組織カテゴリ
	 * @param dataValue 値
	 * @return カテゴリ一覧
	 * @throws LogicalException
	 */
	List<DpmCCdMst> searchCategorylist(String sosCategory, String dataValue);

	/**
	 * データ区分とカテゴリから、対象区分のタイトルを取得する。
	 * @param prodCategory 品目カテゴリ
	 * @return 対象区分タイトル
	 */
	List<DpmCCdMst> searchInsTypeTitle(String prodCategory);

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
	 * 計画管理汎用マスタからデータ区分をもとにデータを取得する。
	 * @param dataKbn データ区分
	 * @return コードマスタ一覧
	 */
	List<DpmCCdMst> searchCodeByDataKbn(String dataKbn);

}
