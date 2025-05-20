package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.BumonRank;

/**
 * 最新の従業員情報にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface JgiMstRealDao {

	/**
	 * ソート順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, DECODE(JGI_KB, '1','0', JGI_KB), JGI_NO";

	/**
	 * 最新の従業員情報を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 最新の従業員情報
	 * @throws DataNotFoundException
	 */
	JgiMst searchReal(Integer jgiNo) throws DataNotFoundException;

	/**
	 * 組織コードを元に、従業員情報のリスト(MRのみ)を取得する。<br>
	 * 指定した部門ランク・組織コード配下の従業員を全て取得する。<br>
	 * 営業所雑担当を含む
	 * 
	 * @param sortString ソート順(NULL可)
	 * @param sosCd 組織コード(NULL可)
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 部門ランク(NULL可)
	 * @return 従業員情報のリスト(MRのみ)
	 * @throws DataNotFoundException
	 */
	List<JgiMst> searchRealBySosCd(String sosCd, SosListType sosListType, BumonRank bumonRank) throws DataNotFoundException;

}
