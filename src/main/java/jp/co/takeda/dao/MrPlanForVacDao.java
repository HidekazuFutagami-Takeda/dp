package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.dto.MrPlanForVacSosSummaryDto;
import jp.co.takeda.model.MrPlanForVac;

/**
 * ワクチン用担当者別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface MrPlanForVacDao {

	/**
	 * ソート順<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO";

	/**
	 * ソート順<br>
	 * 品目ソート　昇順<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE, BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO";

	/**
	 * ソート順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 * 品目ソート 昇順<br>
	 */
	static final String SORT_STRING3 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * ワクチン用担当者別計画を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return ワクチン用担当者別計画
	 * @throws DataNotFoundException
	 */
	MrPlanForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * ワクチン用担当者別計画を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return ワクチン用担当者別計画
	 * @throws DataNotFoundException
	 */
	MrPlanForVac searchUk(Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * ワクチン用担当者別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号【NULL可】
	 * @param sosCd3 エリアコード【NULL可】
	 * @param sosCd4 チームコード【NULL可】
	 * @param prodCode 品目固定コード【NULL可】
	 * @return ワクチン用担当者別計画リスト
	 * @throws DataNotFoundException
	 */
	List<MrPlanForVac> searchList(String sortString, Integer jgiNo, String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 対象品目・営業所の担当者別計画サマリーを取得する。<br>
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param sosCd4 チームコード【NULL可】
	 * @return 担当者別計画サマリー
	 * @throws DataNotFoundException
	 */
	MrPlanForVacSosSummaryDto searchSosSummary(String prodCode, String sosCd3, String sosCd4) throws DataNotFoundException;

	/**
	 * ワクチン用担当者別計画を登録する。
	 *
	 * @param record ワクチン用担当者別計画
	 * @throws DuplicateException
	 */
	void insert(MrPlanForVac record) throws DuplicateException;

	/**
	 * ワクチン用担当者別計画を更新する。
	 *
	 * <pre>
	 * 更新対象は下記の通り。
	 * 計画値(B)
	 * </pre>
	 *
	 * @param record ワクチン用担当者別計画
	 * @return 更新件数
	 */
	int update(MrPlanForVac record);

	/**
	 * ワクチン用担当者別計画を削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
