package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.DpmCCdMst;

/**
 * 計画管理汎用マスタにアクセスするDAOインターフェイス
 * @author rna8405
 *
 */
public interface CodeMasterDao {

	/**
	 * カテゴリ情報を取得する。
	 *
	 * @param dataKbn データ区分
	 * @param dataCd コード
	 * @return カテゴリ情報
	 */
	List<DpmCCdMst> searchCategory(String dataKbn, List<String> dataCds) throws DataNotFoundException;

	/**
	 * カテゴリ情報を取得する。
	 *
	 * @param dataKbn データ区分
	 * @param dataCd コード
	 * @param dataValue 値
	 * @return カテゴリ情報
	 */
	List<DpmCCdMst> searchCategoryList(String dataKbn, List<String> dataCds, String dataValue) throws DataNotFoundException;

	/**
	 * カテゴリ情報を取得する。
	 * @param dataKbn データ区分
	 * @param category カテゴリコード
	 * @return カテゴリ情報
	 * @throws DataNotFoundException
	 */
	DpmCCdMst searchOneCategory(String dataKbn, String categoryCd) throws DataNotFoundException;

		/**
	 * 計画管理汎用マスタからデータ区分をもとにデータを取得する
	 * @param dataKbn
	 * @return 汎用マスタデータ
	 * @throws DataNotFoundException
	 */
	List<DpmCCdMst> searchCodeByDataKbn(String dataKbn) throws DataNotFoundException;


	/**
	 * 計画管理汎用マスタからデータ区分とコードをもとにデータを取得する
	 * @param dataKbn データ区分
	 * @param dataCd コード
	 * @return 汎用マスタデータ
	 * @throws DataNotFoundException
	 */
	DpmCCdMst searchCategoryByKbnAndCd(String dataKbn, String dataCd) throws DataNotFoundException;

}
