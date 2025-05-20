package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DpsInsMstScDto;
import jp.co.takeda.model.DpsInsMst;

/**
 * 施設情報にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface InsMstDAO {

	/**
	 * ソート順<br>
	 * 対象区分 昇順<br>
	 * 施設内部コード 昇順<br>
	 * 施設内部コード(サブコード)昇順
	 *
	 */
	static final String SORT_STRING = "ORDER BY HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順<br>
	 * 対象区分 昇順<br>
	 * 施設内部コード 昇順<br>
	 * 施設内部コード(サブコード)昇順
	 *
	 */
	static final String SORT_STRING2 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, " + "SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 市区町村ソート 昇順<br>
	 * 対象区分 昇順<br>
	 * 施設内部コード 昇順<br>
	 * 施設内部コード(サブコード)昇順
	 *
	 */
	static final String SORT_STRING3 = "ORDER BY ADDR_CODE_PREF, ADDR_CODE_CITY,HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * 施設情報を取得する。
	 *
	 * @param insNo 施設コード（必須）
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	DpsInsMst search(String insNo) throws DataNotFoundException;

	/**
	 * 施設情報を取得する。
	 *
	 * @param insNo 施設コード（必須）
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目コード
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
// mod Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
//	DpsInsMst search(String insNo, Integer jgiNo, String prodCode, String oyakoKb, String oyakoKb2, String category) throws DataNotFoundException;
	List<DpsInsMst> search(String insNo, Integer jgiNo, String prodCode, String oyakoKb, String oyakoKb2, String category) throws DataNotFoundException;
// mod Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

	/**
	 * 施設情報を取得する。<br>
	 * Ａ調が紐付く場合、Ａ調も取得する。
	 *
	 * @param sortString ソート順
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	List<DpsInsMst> searchIncludeA(String sortString, Integer jgiNo, String insNo) throws DataNotFoundException;

	/**
	 * 施設情報の検索条件DTOを元にMRの担当する施設情報のリストを取得する。<br>
	 * 組織コードまたは、従業員番号の設定必須。<br>
	 * 雑担当の施設は含まない。
	 *
	 * @param sortString ソート順(NULL可)
	 * @param scDto 施設情報の検索条件
	 * @return 施設情報のリスト
	 * @throws DataNotFoundException
	 */
	List<DpsInsMst> searchList(String sortString, DpsInsMstScDto scDto, String oyakoKb, String oyakoKb2, String category) throws DataNotFoundException;

	/**
	 * 指定された施設コード、従業員番号、品目固定コードから関連する親子施設を取得する。
	 *
	 * @param insNo 施設固定コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 該当する施設リスト
	 * @throws DataNotFoundException
	 */
	List<DpsInsMst> searchFamilyInsList(String insNo, Integer jgiNo, String prodCode) throws DataNotFoundException;
}
