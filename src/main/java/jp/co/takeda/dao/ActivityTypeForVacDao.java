package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ActivityTypeForVac;

/**
 * 活動区分情報にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ActivityTypeForVacDao {

	/**
	 * 施設コードも元に、活動区分情報を取得する。
	 * 
	 * @param insNo 施設コード
	 * @return 活動区分情報
	 * @throws DataNotFoundException
	 */
	ActivityTypeForVac search(String insNo) throws DataNotFoundException;
}
