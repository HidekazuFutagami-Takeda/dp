package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ExceptDistIns;
import jp.co.takeda.model.div.PlanType;

/**
 * 配分除外施設にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ExceptDistInsDao {

	/**
	 * ソート順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順<br>
	 * 施設ソート順<br>
	 * 品目ソート順
	 *
	 */
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO"
//			+ ", HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";
	static final String SORT_STRING = "ORDER BY SM.BR_CODE, SM.DIST_SEQ, SM.DIST_CODE, SM.TEAM_CODE, JM.SHOKUSEI_CD, JM.SHOKUSHU_CD, JM.JGI_NO"
			+ ", HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, GROUP_CODE, STAT_PROD_CODE, PROD_CODE, DISP_TAN.SEQNO";
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * ソート順<br>
	 * 親子区分ソート順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順<br>
	 * 施設ソート順<br>
	 * 品目ソート順
	 *
	 */
	static final String SORT_STRING_OYAKO = "ORDER BY SM.BR_CODE, SM.DIST_SEQ, SM.DIST_CODE, SM.TEAM_CODE, JM.SHOKUSEI_CD, JM.SHOKUSHU_CD, JM.JGI_NO"
			+ ", OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, GROUP_CODE"
			+ ", STAT_PROD_CODE, PROD_CODE, DISP_TAN.SEQNO";

	/**
	 * 配分除外施設を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 配分除外施設
	 * @throws DataNotFoundException
	 */
	ExceptDistIns search(Long seqKey) throws DataNotFoundException;

	/**
	 * 施設コードで配分除外施設を取得する。<br>
	 * (品目は品目ソート順で格納)
	 *
	 * @param insNo 施設コード
	 * @return 配分除外施設
	 * @throws DataNotFoundException
	 */
	ExceptDistIns searchByInsNo(String insNo) throws DataNotFoundException;

	/**
	 * 施設コード、品目固定コードで配分除外施設を取得する。<br>
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @param planType 計画立案区分
	 * @return 配分除外施設
	 * @throws DataNotFoundException
	 */
	ExceptDistIns search(String insNo, String prodCode, PlanType planType) throws DataNotFoundException;

	/**
	 * 施設コード、品目固定コードで配分除外施設かどうかを取得（施設指定の場合でも判定）
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @return true：配分除外施設、false：配分除外施設ではない
	 */
	boolean isExceptDistIns(String insNo, String prodCode);

	/**
	 * 配分除外施設を登録する。<br>
	 * 配分除外施設品目の場合、品目数分登録する。
	 *
	 * @param record 配分除外施設
	 * @throws DuplicateException
	 */
	void insert(ExceptDistIns record) throws DuplicateException;

	/**
	 * 施設コードに紐付く配分除外施設を削除する。
	 *
	 * @param prodCodeList 削除対象の品目コードリスト
	 * @param insNo 施設コード
	 * @param upDate 最終更新日時
	 * @return 削除件数
	 */
	int delete(String insNo, Date upDate, List<String> prodCodeList);

	/**
	 * 施設指定レコード（品目NULLレコード）の削除
	 *
	 * @param insNo 施設コード
	 * @param upDate 最終更新日時
	 * @return 削除件数
	 */
	int deleteByProdNull(String insNo, Date upDate);

	/**
	 * 施設コードに紐付く配分除外施設を論理削除する。
	 *
	 * @param prodCodeList 削除対象の品目コードリスト
	 * @param insNo 施設コード
	 * @param upDate 最終更新日時
	 * @return 削除件数
	 */
	int update(String insNo, Date upDate, List<String> prodCodeList);

	/**
	 * 配分除外施設のリストを取得する。
	 *
	 * @param sortString ソート順(NULL可)
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @return 配分除外施設のリスト
	 * @throws DataNotFoundException
	 */
	List<ExceptDistIns> searchList(String sortString, String sosCd3, String sosCd4, Integer jgiNo, String oyakoKb, String oyakoKb2) throws DataNotFoundException;
}
