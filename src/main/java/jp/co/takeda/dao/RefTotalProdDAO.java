package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.RefTotalProd;

/**
 * TMS特約店基本統合にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface RefTotalProdDAO {

	/**
	 * 参照用集計品目情報を取得する。
	 *
	 * @param prodCd 品目コード
	 * @return 参照用集計品目名情報
	 * @throws DataNotFoundException
	 */
	RefTotalProd search(String prodCd) throws DataNotFoundException;
//
//	/**
//	 * 参照用集計品目情報を取得する。<br>
//	 * 抽出条件として、階層レベル・本店・支社・支店コード・組織コード(特約店部)が指定可能。
//	 *
//	 * @param sortString ソート条件
//	 * @param scDto 特約店情報の検索条件(Search Condition)を表すDTO
//	 * @return 参照用集計品目情報のリスト
//	 * @throws DataNotFoundException
//	 */
//	List<RefTotalProd> searchList(String sortString, TmsTytenMstScDto scDto) throws DataNotFoundException;
}
