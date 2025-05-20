package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.WsPlanSummaryScDto;
import jp.co.takeda.model.WsPlanSummary2;

/**
 * 特約店別計画のサマリー情報取得DAOインターフェイス
 *
 * @author khashimoto
 */
public interface WsPlanSummaryDao {

	/**
	 * ソート順<br>
	 * 特約店ソート順<br>
	 * 品目ソート順
	 */
	static final String SORT_STRING = "ORDER BY TMS_TYTEN_CD, DATA_SEQ, GROUP_CODE, STAT_PROD_CODE";
	static final String SORT_STRING_EXCEL = "ORDER BY TMS_TYTEN_CD, DATA_SEQ, GROUP_CODE, STAT_PROD_CODE";

	/**
	 * 特約店別計画サマリー情報リストを取得する。
	 *
	 * <pre>
	 * 【納入実績取得フラグ=true】
	 * 納入実績も合わせて取得する。
	 *
	 * 【納入実績取得フラグ=fasle】
	 * 納入実績を取得しない。
	 * </pre>
	 *
	 * @param sortString ソート条件
	 * @param scDto 検索条件
	 * @param nnuFlg 納入実績取得フラグ
	 * @return 特約店別計画のサマリー情報リスト
	 * @throws DataNotFoundException 検索結果が0件の場合にスロー
	 */
	List<WsPlanSummary2> searchList(String sortString, WsPlanSummaryScDto scDto, boolean nnuFlg) throws DataNotFoundException;
}
