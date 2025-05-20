package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ChoseiTeam;
import jp.co.takeda.model.div.InsType;

/**
 * 調整額（チーム）にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ChoseiTeamDao {

	/**
	 * ユニークキーを元に管理の調整額（チーム）を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(チーム)
	 * @return 調整額（チーム）
	 * @throws DataNotFoundException
	 */
	ChoseiTeam searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException;

}
