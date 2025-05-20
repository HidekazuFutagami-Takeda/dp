package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * 特約店別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface WsPlanDao {

	/**
	 * ソート順<br>
	 * 組織ソート順<br>
	 */
	static final String SORT_STRING = "ORDER BY TMS_TYTEN_CD, BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート順<br>
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 特約店別計画を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 特約店別計画
	 * @throws DataNotFoundException
	 */
	WsPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーで特約店別計画を取得する。
	 *
	 * @param tmsTytenCd TMS特約店コード
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード
	 * @param kaBaseKb 価ベース区分
	 * @return 特約店別計画
	 * @throws DataNotFoundException
	 */
	WsPlan searchUk(String tmsTytenCd, String prodCode, String sosCd, KaBaseKb kaBaseKb) throws DataNotFoundException;

	/**
	 * 特約店別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param tmsTytenCd TMS特約店コード
	 * @param prodCode 品目固定コード【NULL可】
	 * @param sosCd 組織コード(営業所)【NULL可】
	 * @param kaBaseKb 価ベース区分
	 * @return 特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<WsPlan> searchList(String sortString, String tmsTytenCd, String tmsTytenCdPart, String prodCode, String sosCd, String tytenKisLevel, KaBaseKb kaBaseKb) throws DataNotFoundException;

	/**
	 * 組織(営業所)と品目固定コードを元に特約店別計画の積上値を計画値にコピーする。
	 *
	 * @param sosCd 組織コード(営業所)リスト
	 * @param prodCode 品目固定コードリスト
	 * @param kaBaseKb 価ベース
	 * @return 更新件数
	 */
	int updateCopyStackToPlanned(List<String> sosCd, List<String> prodCode);

	/**
	 * 特約店別計画を登録する。
	 *
	 * @param record 特約店別計画
	 * @throws DuplicateException
	 */
	void insert(WsPlan record) throws DuplicateException;

	/**
	 * 特約店別計画を更新する。
	 *
	 * <pre>
	 * 更新対象は下記カラム
	 * 配分値UH
	 * 配分値P
	 * 計画値UH
	 * 計画値P
	 * </pre>
	 *
	 * @param record 特約店別計画
	 * @return 更新件数
	 */
	int update(WsPlan record);

	/**
	 * 特約店別計画を削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);

	/**
	 * 組織(営業所)と品目を元に特約店別計画を削除する。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(営業所)
	 */
	int deleteSosProd(String prodCode, String sosCd);

	/**
	 * 特約店別計画リストを取得する。カテゴリでフィルタする
	 *
	 * @param sortString ソート条件
	 * @param tmsTytenCd TMS特約店コード
	 * @param sosCd 組織コード(営業所)
	 * @param kaBaseKb 価ベース区分
	 * @param category カテゴリ
	 * @return 特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<WsPlan> searchListFilterByCategory(String sortString, String tmsTytenCd, String sosCd, KaBaseKb kaBaseKb,
			String category) throws DataNotFoundException;
}
