package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.InsPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 施設計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface InsPlanDao {

	/**
	 * ソート順<br>
	 * 施設ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * 施設計画を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 施設計画
	 * @throws DataNotFoundException
	 */
	InsPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーで施設計画を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @return 施設計画
	 * @throws DataNotFoundException
	 */
	InsPlan searchUk(Integer jgiNo, String prodCode, String insNo) throws DataNotFoundException;

	/**
	 * 施設計画のリストを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分【NULL可】
	 * @return 施設計画のリスト
	 * @throws DataNotFoundException
	 */
	List<InsPlan> searchList(String sortString, Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException;

	/**
	 * 施設～施設特約間の計画調整有無を取得する。
	 * 営業所コード、チームコード、従業員番号のいずれか必須。
	 * 
	 * @param sosCd3 営業所コード
	 * @param sosCd4 チームコード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 調整ありなしのMAP
	 * @throws DataNotFoundException
	 */
	Map<String, Object> checkChoseiInsWs(String sosCd3, String sosCd4, Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 施設計画を登録する。
	 * 
	 * @param record 施設計画
	 * @throws DuplicateException
	 */
	void insert(InsPlan record) throws DuplicateException;

	/**
	 * 施設計画を登録（医師別計画の積上げ）
	 * 排他制御はしない。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * 
	 * @throws DuplicateException
	 */
	void insertTumiage(Integer jgiNo, String prodCode, InsType insType) ;


	/**
	 * 施設計画の計画値を更新する。
	 * 
	 * <pre>
	 * 更新対象は下記カラム
	 * 計画値(Y)
	 * </pre>
	 * 
	 * @param record 施設計画
	 * @return 更新件数
	 */
	int update(InsPlan record) throws DuplicateException;

	/**
	 * 施設計画を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate) throws DuplicateException;

	/**
	 * 施設計画を削除（医師別計画の積上げ）
	 * 排他制御はしない。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @return
	 * @throws DuplicateException
	 */
	int deleteTumiage(Integer jgiNo, String prodCode, InsType insType) ;

}
