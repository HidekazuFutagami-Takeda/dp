package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.MikakutokuSijou;

/**
 * 未獲得市場にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface MikakutokuSijouDao {

	/**
	 * ソート順<br>
	 * 施設出力対象区分 昇順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順<br>
	 * 施設ソート順
	 */
	static final String SORT_STRING1 = "ORDER BY INS_TYPE, BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE," + " SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 施設出力対象区分 昇順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順
	 */
	static final String SORT_STRING2 = "ORDER BY INS_TYPE, BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE," + " SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO";

	/**
	 * ソート順<br>
	 * 施設出力対象区分 昇順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順
	 */
	static final String SORT_STRING3 = "ORDER BY INS_TYPE, BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD,"  + " JGI_NO, OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * 未獲得市場を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 未獲得市場
	 */
	MikakutokuSijou search(Long seqKey) throws DataNotFoundException;

	/**
	 * 未獲得市場を更新する。<br>
	 * 増減金額(Y)、営業所案未獲得市場、営業所案構成比を更新する。
	 *
	 * @param record 未獲得市場
	 * @return 更新件数
	 */
	int update(MikakutokuSijou record);

	/**
	 * 組織コード・薬効市場コードより、未獲得市場のサマリーデータを取得する。<br>
	 * サマリーデータは従業員・施設出力対象区分ごとのサマリー。<br>
	 * シーケンスキー、施設コードはNULL。
	 *
	 * @param sortString ソート条件
	 * @param sosCD3 組織コード(営業所)
	 * @param yakkouSijouCode (薬効市場コード)
	 * @return 未獲得市場のサマリーデータリスト
	 * @throws DataNotFoundException
	 */
	List<MikakutokuSijou> searchSumList(String sortString, String sosCd3, String yakkouSijouCode) throws DataNotFoundException;

	/**
	 * 組織コード・薬効市場コードより、未獲得市場の全リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCD3 組織コード(営業所)
	 * @param yakkouSijouCode (薬効市場コード)
	 * @return 未獲得市場のリスト
	 * @throws DataNotFoundException
	 */
	List<MikakutokuSijou> searchList(String sortString, String sosCd3, String yakkouSijouCode, String oyakoKb,String oyakoKb2, String prodCode) throws DataNotFoundException;
}
