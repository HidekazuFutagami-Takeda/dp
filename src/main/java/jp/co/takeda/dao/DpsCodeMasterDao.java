package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.div.JokenSet;

/**
 *
 *
 * @author
 */
public interface DpsCodeMasterDao {

	/**
	 * カテゴリ情報を取得する。
	 *
	 * @param dataKbn データ区分
	 * @param dataCd コード
	 * @return カテゴリ情報
	 */
	List<DpsCCdMst> searchCategory(String dataKbn, List<String> dataCds) throws DataNotFoundException;

	/**
	 * カテゴリ情報を取得する。
	 *
	 * @param dataKbn データ区分
	 * @param dataCd コード
	 * @param dataValue 値
	 * @return カテゴリ情報
	 */
	List<DpsCCdMst> searchCategoryList(String dataKbn, List<String> dataCds, String dataValue) throws DataNotFoundException;

	/**
	 * カテゴリ情報を取得する。
	 * @param dataKbn データ区分
	 * @param category カテゴリコード
	 * @return カテゴリ情報
	 * @throws DataNotFoundException
	 */
	DpsCCdMst searchOneCategory(String dataKbn, String categoryCd) throws DataNotFoundException;

	/**
	 * 計画管理汎用マスタからデータ区分をもとにデータを取得する
	 * @param dataKbn
	 * @return 汎用マスタデータ
	 * @throws DataNotFoundException
	 */
	List<DpsCCdMst> searchCodeByDataKbn(String dataKbn) throws DataNotFoundException;


	/**
	 * 計画管理汎用マスタからデータ区分とコードをもとにデータを取得する
	 * @param dataKbn データ区分
	 * @param dataCd コード
	 * @return 汎用マスタデータ
	 * @throws DataNotFoundException
	 */
	DpsCCdMst searchDataKbnAndCd(String dataKbn, String dataCd) throws DataNotFoundException;

	/**
	 * 組織コードと計画立案レベルを条件に、計画管理汎用マスタから支援用カテゴリリストを取得する。
	 * @param sosCd
	 * @param planLevel
	 * @return
	 * @throws DataNotFoundException
	 */
	List<DpsCCdMst> searchShienCategoryList(String sosCd, ProdPlanLevel planLevel) throws DataNotFoundException;

	/**
	 * 計画管理汎用マスタからデータ区分をもとにデータを取得する データバリューでのソート版
	 * @param dataKbn
	 * @return 汎用マスタデータ
	 * @throws DataNotFoundException
	 */
	List<DpsCCdMst> searchCodeByDataKbnOrderByDataValue(String dataKbn) throws DataNotFoundException;

	/**
	 * 特約店系条件セットコードのリストを返す
	 * @return
	 */
	List<JokenSet> searchTokuyakuJokenSetCd() throws DataNotFoundException;
}
