package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.dto.InsWsPlanImpProdSummaryDto;
import jp.co.takeda.dto.InsWsPlanJgiSummaryDto;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.model.InsWsCount;
import jp.co.takeda.model.InsWsPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 施設特約店別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface InsWsPlanDao {

	/**
	 * ソート順<br>
	 * 特定施設個別計画フラグ 降順<br>
	 * 施設ソート 昇順<br>
	 * 特約店ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY SPECIAL_INS_PLAN_FLG DESC, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順<br>
	 * 配分値 降順
	 */
	static final String SORT_STRING1 = "ORDER BY DIST_VALUE_Y DESC";

	/**
	 * 施設特約店別計画を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 施設特約店別計画
	 * @throws DataNotFoundException
	 */
	InsWsPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーで施設特約店別計画を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @return 施設特約店別計画
	 * @throws DataNotFoundException
	 */
	InsWsPlan searchUk(Integer jgiNo, String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException;

	/**
	 * 施設特約店別計画のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分【NULL可】
	 * @return 施設特約店別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<InsWsPlan> searchList(String sortString, Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException;

	/**
	 * 施設特約店別計画を登録する。
	 *
	 * @param record 施設特約店別計画
	 * @throws DuplicateException
	 */
	void insert(InsWsPlan record) throws DuplicateException;

	/**
	 * 施設特約店別計画の計画値を更新する。
	 *
	 * <pre>
	 * 更新対象は下記カラム
	 * 計画値(Y)
	 * </pre>
	 *
	 * @param record 施設特約店別計画
	 * @return 更新件数
	 */
	int update(InsWsPlan record);

	/**
	 * 施設特約店別計画を削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);

	/**
	 * 施設特約店別計画を従業員・品目単位で一括削除する。
	 *
	 * <pre>
	 * 従業員・品目単位で一括削除する。
	 * 楽観的排他チェックは行わない。
	 * </pre>
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 削除件数
	 */
	int deleteByJgi(Integer jgiNo, String prodCode);

	/**
	 * 施設特約店別計画を品目単位で一括削除する。
	 *
	 * <pre>
	 * 品目単位で一括削除する。
	 * 楽観的排他チェックは行わない。
	 * </pre>
	 *
	 * @param prodCode 品目固定コード
	 * @return 削除件数
	 */
	int deleteByProdCd(String prodCode);

	/**
	 * 施設特約店別計画を組織コード(営業所)・品目単位で一括削除する。
	 *
	 * <pre>
	 * 組織コード・品目単位で一括削除する。
	 * 楽観的排他チェックは行わない。
	 * </pre>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 削除件数
	 */
	int deleteBySos(String sosCd3, String prodCode);

	/**
	 * 品目単位の施設特約店別計画サマリーを取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param category カテゴリ
	 * @return 施設特約店別計画サマリー
	 * @throws DataNotFoundException
	 */
	InsWsPlanProdSummaryDto searchProdSummaryCtg(String sosCd3, String sosCd4, Integer jgiNo, String prodCode, String category) throws DataNotFoundException;

	/**
	 * 品目単位の施設特約店別計画サマリーを取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分２
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 施設特約店別計画サマリー
	 * @throws DataNotFoundException
	 */
	InsWsPlanProdSummaryDto searchProdSummary(String sosCd3, String sosCd4, Integer jgiNo, String prodCode, String oyakoKb,String oyakoKb2, String oyakoKbProdCode) throws DataNotFoundException;

	/**
	 * 品目単位の施設特約店別計画サマリーを取得する。(ワクチン)
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 施設特約店別計画サマリー
	 * @throws DataNotFoundException
	 */
	InsWsPlanProdSummaryDto searchProdSummaryVac(String sosCd3, String sosCd4, Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 対象品目・担当者単位の施設特約店別計画サマリーリストを取得する。<br>
	 * ソート順は組織ソート・従業員ソート順
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @return 施設特約店別計画サマリー
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanJgiSummaryDto> searchJgiSummary(String prodCode, String sosCd3, String sosCd4, Integer jgiNo) throws DataNotFoundException;

	/**
	 * 配分情報を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param refFrom 参照期間From
	 * @param refTo 参照期間To
	 * @param refProdCode 配分品目コード
	 * @param insType 施設タイプ
	 * @param zeroDistFld ゼロ配分フラグ
	 * @param execJgiNo 実行者従業員番号
	 * @param execJgiName 実行者従業員氏名
	 * @return 配分情報
	 * @throws DataNotFoundException
	 */
	Map<String, List<InsWsPlanImpProdSummaryDto>> selectImportantHbn(Integer jgiNo, String prodCode, String refFrom, String refTo, String refProdCode, String insType, String zeroDistFld, Integer execJgiNo, String execJgiName) throws DataNotFoundException;

	/**
	 * 配分ミス基本情報を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 配分ミス情報
	 * @throws DataNotFoundException
	 */
	Map<String, Object> selectHbnMissBase(Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 施設特約店別計画の削除施設の数を取得する。
	 *
	 * @param sosCd3 営業所コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param prodList 品目一覧ならtrue
	 * @return 削除施設数
	 * @throws DataNotFoundException
	 */
	List<InsWsCount> searchDelInsCount(String sosCd3, Integer jgiNo, String prodCode, boolean prodList);

	/**
	 * 施設特約店別計画の計画対象外特約店の数を取得する。
	 *
	 * @param sosCd3 営業所コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param prodList 品目一覧ならtrue
	 * @return 削除施設数
	 * @throws DataNotFoundException
	 */
	List<InsWsCount> searchTaiGaiTytenCount(String sosCd3, Integer jgiNo, String prodCode, boolean prodList);

	/**
	 * 施設特約店別計画の配分除外施設の数を取得する。
	 *
	 * @param sosCd3 営業所コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param prodList 品目一覧ならtrue
	 * @return 削除施設数
	 * @throws DataNotFoundException
	 */
	List<InsWsCount> searchExceptDistInsCount(String sosCd3, Integer jgiNo, String prodCode, boolean prodList);
}
