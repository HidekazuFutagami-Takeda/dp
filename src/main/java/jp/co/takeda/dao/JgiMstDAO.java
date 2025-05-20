package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;

/**
 * 従業員情報にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface JgiMstDAO {

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
	JgiMst search(Integer jgiNo) throws DataNotFoundException;

	/**
	 * 組織コードを元に、従業員情報のリスト(MRのみ)を取得する。<br>
	 * 指定した部門ランク・組織コード配下の従業員を全て取得する。
	 *
	 * @param sortString ソート順(NULL可)
	 * @param sosCd 組織コード(NULL可)
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 部門ランク(NULL可)
	 * @return 従業員情報のリスト(MRのみ)
	 * @throws DataNotFoundException
	 */
	List<JgiMst> searchBySosCd(String sortString, String sosCd, SosListType sosListType, BumonRank bumonRank) throws DataNotFoundException;

	/**
	 * 最新の従業員情報を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @return 最新の従業員情報
	 * @throws DataNotFoundException
	 */
	JgiMst searchReal(Integer jgiNo) throws DataNotFoundException;

	/**
	 * 組織コード・条件セットを元に、最新の従業員情報のリストを取得する。
	 *
	 * <pre>
	 * 指定した組織コード(標準組織背番号Ｃ)に属する従業員情報を取得する。
	 * </pre>
	 *
	 * @param sortString ソート順
	 * @param sosCd 組織コード
	 * @param jokenSetArray 条件セットリスト
	 * @return 従業員情報のリスト
	 * @throws DataNotFoundException 検索結果0件の場合にスロー
	 */
	List<JgiMst> searchBySosCd(String sortString, String sosCd, JokenSet... jokenSetList) throws DataNotFoundException;

	/**
	 * 最新の従業員情報を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param jgiKbList 取得対象の従業員区分候補の配列(いずれかに一致)
	 * @return 従業員情報
	 * @throws DataNotFoundException
	 */
	JgiMst searchByJgiKb(Integer jgiNo, JgiKb[] jgiKbList) throws DataNotFoundException;

	/**
	 * 最新の代行ユーザ従業員のリストを取得する。
	 *
	 * @param jgiNo 正規の従業員の従業員番号
	 * @param jokenSetList 必要な条件セット配列(いずれかに一致)
	 * @return 代行ユーザの従業員番号と従業員名を格納したリスト
	 */
	List<CodeAndValue> searchAltJgi(Integer jgiNo, JokenSet[] jokenSetList) throws DataNotFoundException;

	/**
	 * 代行ユーザが設定されている場合、代行ユーザの従業員情報を取得する。
	 *
	 * @return 代行ユーザが設定されている場合、代行ユーザの従業員情報
	 * @throws DataNotFoundException 代行ユーザが設定されていない場合や代行ユーザが見つからない場合にスロー
	 */
	JgiMst searchAltJgi() throws DataNotFoundException;

	/**
	 * 副担当従業員情報（主担当施設を持たない従業員）を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @return 従業員情報
	 * @throws DataNotFoundException
	 */
	JgiMst searchSubJgi(Integer jgiNo) throws DataNotFoundException;

	/**
	 * 指定された従業員と所属内での条件セットに合致する従業員情報を取得
	 *
	 * <pre>
	 * 自組織の条件セットに合致する従業員情報を取得する。
	 * </pre>
	 *
	 * @param jokenSetArray 条件セットリスト
	 * @return 従業員情報のリスト
	 * @throws DataNotFoundException 検索結果0件の場合にスロー
	 */
	List<JgiMst> searchByJokenSetList(Integer jgiNo, JokenSet... jokenSetList) throws DataNotFoundException;

	/**
	 * 組織コードを元に、従業員情報のリストを取得する。<br>
	 * 組織コード配下の従業員を全て取得する。
	 *
	 * @param sosCd 組織コード(NULL可)
	 * @throws DataNotFoundException
	 */
	List<JgiMst> searchJgiList(String sosCd) throws DataNotFoundException;

	//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
	/**
	 * 組織コードを元に、従業員情報のリスト(MRのみ)を取得する。<br>
	 * 指定した部門ランク・組織コード配下の従業員を全て取得する。
	 *
	 * @param sortString ソート順(NULL可)
	 * @param sosCd 組織コード(NULL可)
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 部門ランク(NULL可)
	 * @param category カテゴリ(NULL不可)
	 * @return 従業員情報のリスト(MRのみ)
	 * @throws DataNotFoundException
	 */
	List<JgiMst> searchBySosCdCategory(String sortString, String sosCd, SosListType sosListType, BumonRank bumonRank, String category) throws DataNotFoundException;
	//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）

}
