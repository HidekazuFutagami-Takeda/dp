package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * ワクチン用特約店別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface WsPlanForVacDao {

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
	 * ワクチン用特約店別計画を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return ワクチン用特約店別計画
	 * @throws DataNotFoundException
	 */
	WsPlanForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーで特約店別計画を取得する。
	 *
	 * @param tmsTytenCd TMS特約店コード
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード
	 * @param kaBaseKb (ワクチン)価ベース区分
	 * @return 特約店別計画
	 * @throws DataNotFoundException
	 */
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	WsPlanForVac searchUk(String tmsTytenCd, String prodCode, String sosCd, KaBaseKb kaBaseKb) throws DataNotFoundException;

	/**
	 * 特約店別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd2 組織コード(特約店部)【NULL可】
	 * @param tmsTytenCd TMS特約店コード
	 * @param prodCode 品目固定コード【NULL可】
	 * @param sosCd 組織コード【NULL可】
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * @param kaBaseKb 価ベース
	 * @return 特約店別計画リスト
	 * @throws DataNotFoundException
	 */
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	List<WsPlanForVac> searchList(String sortString, String sosCd2, String tmsTytenCd, String tmsTytenCdPart, String prodCode, String sosCd, KaBaseKb kaBaseKb) throws DataNotFoundException;

	/**
	 * 品目固定コードを元に特約店別計画の積上値を計画値にコピーする。
	 *
	 * @param prodCode 品目固定コードリスト
	 * @return 更新件数
	 */
	int updateCopyStackToPlanned(List<String> prodCode);

	/**
	 * ワクチン用特約店別計画を登録する。
	 *
	 * @param record ワクチン用特約店別計画
	 * @throws DuplicateException
	 */
	void insert(WsPlanForVac record) throws DuplicateException;

	/**
	 * ワクチン用特約店別計画を更新する。
	 *
	 * <pre>
	 * 更新対象は下記カラム
	 * 計画値
	 * </pre>
	 *
	 * @param record ワクチン用特約店別計画
	 * @return 更新件数
	 */
	int update(WsPlanForVac record);

	/**
	 * ワクチン用特約店別計画を削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
