package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DeliveryResultSosSummaryDto;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;

/**
 * 担当者別納入実績にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DeliveryResultMrDao {

	/**
	 * ソート順<br>
	 * 施設出力対象区分ソート 昇順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY INS_TYPE, BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO";

	/**
	 * ソート順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 * 品目ソート 昇順<br>
	 */
	static final String SORT_STRING2 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 従業員番号、品目固定コード、施設出力対象区分を指定して納入実績を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @return 納入実績
	 * @throws DataNotFoundException
	 */
	DeliveryResultMr search(Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException;

	/**
	 * 対象品目・営業所の納入実績を取得する。(雑担当を含む)
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)(NULL可)
	 * @param insType 施設出力対象区分(NULL可 ※NULLの場合、UH/Pを別レコードとして取得)
	 * @param category カテゴリ(NULL不可)
	 * @return 担当者単位の納入実績(サマリー)のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultMr> searchByProd(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException;

	/**
	 * 帳票用対象品目・営業所の納入実績を取得する。(雑担当を含む)
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)(NULL可)
	 * @param insType 施設出力対象区分(NULL可 ※NULLの場合、UH/Pを別レコードとして取得)
	 * @param sales 売上所属(NULL可)
	 * @param category カテゴリ(NULL不可)
	 * @return 担当者単位の納入実績(サマリー)のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultMr> searchByProdReport(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, Sales sales, String category) throws DataNotFoundException;

	/**
	 * 対象品目・営業所の納入実績を取得する。(雑担当を含まない)
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)(NULL可)
	 * @param insType 施設出力対象区分(NULL可 ※NULLの場合、UH/Pを別レコードとして取得)
	 * @param category カテゴリ(NULL不可)
	 * @return 担当者単位の納入実績(サマリー)のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultMr> searchByProdNonZatu(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException;

	/**
	 * 対象品目・営業所の納入実績サマリーを取得する。<br>
	 * 施設出力対象区分がNULL指定の場合、合計を返す。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param zatuFlg 雑担当を含む場合、trueを指定
	 * @param insType 施設出力対象区分(NULL可:UHP合計)
	 * @param category カテゴリ(NULL不可)
	 * @return 納入実績サマリー
	 * @throws DataNotFoundException
	 */
	DeliveryResultSosSummaryDto searchSosSummary(String prodCode, String sosCd3, boolean zatuFlg, InsType insType, String category) throws DataNotFoundException;

	/**
	 * 対象品目・チームの納入実績サマリーを取得する。 <br>
	 * 施設出力対象区分がNULL指定の場合、合計を返す。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd4 組織コード(チーム)
	 * @param insType 施設出力対象区分(NULL可:UHP合計)
	 * @param category カテゴリ(NULL不可)
	 * @return 納入実績サマリー
	 * @throws DataNotFoundException
	 */
	DeliveryResultSosSummaryDto searchTeamSummary(String prodCode, String sosCd4, InsType insType, String category) throws DataNotFoundException;

	/**
	 * 対象組織の納入実績を取得する。(雑担当を含まない)<br>
	 * 計画対象品目のみを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)(NULL可)
	 * @param insType 施設出力対象区分(NULL可:UHP合計)
	 * @param category カテゴリ(NULL不可)
	 * @return 担当者単位の納入実績のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultMr> searchBySosNonZatu(String sortString, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException;

// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 *　帳票用対象組織の納入実績を取得する。(雑担当を含まない)<br>
	 * 計画対象品目のみを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)(NULL可)
	 * @param insType 施設出力対象区分(NULL可:UHP合計)
	 * @param category カテゴリ(NULL不可)
	 * @return 担当者単位の納入実績のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultMr> searchBySosNonZatuReport(String sortString, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException;
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
}
