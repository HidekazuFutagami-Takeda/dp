package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.JgiMst;

/**
 * 施設主副担当関連にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface MainSubRelationDAO {

	/**
	 * ソート順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO";

	/**
	 * 従業員情報を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 従業員情報
	 * @throws DataNotFoundException
	 */
	public List<JgiMst> searchList(Integer mainMrNo) throws DataNotFoundException;
}
