package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosEachKindInfo;

/**
 * 組織単位の各種登録状況を取得するDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface SosEachKindInfoDao {

	/**
	 * 組織コード(支店)を基に、各種登録状況を取得する。
	 * 
	 * @param sosCd2 組織コード(支店)
	 * @return 組織単位の各種登録状況
	 * @throws DataNotFoundException
	 */
	SosEachKindInfo searchShiten(String sosCd2) throws DataNotFoundException;

	/**
	 * 組織コード(営業所)を基に、各種登録状況を取得する。
	 * 
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @return 組織単位の各種登録状況
	 * @throws DataNotFoundException
	 */
	SosEachKindInfo searchEigyo(String sosCd3) throws DataNotFoundException;

	/**
	 * 組織コード(チーム)を基に、各種登録状況を取得する。
	 * 
	 * @param sosCd4 組織コード(チーム)
	 * @return 組織単位の各種登録状況
	 * @throws DataNotFoundException
	 */
	SosEachKindInfo searchTeam(String sosCd4) throws DataNotFoundException;

}
