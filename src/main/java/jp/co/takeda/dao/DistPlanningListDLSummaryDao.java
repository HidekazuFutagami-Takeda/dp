package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DistPlanningListSummaryScDto;
import jp.co.takeda.model.DistPlanningListDLSummary;

/**
 * 営業所計画アップロード用のフォーマットファイルのサマリー情報取得DAOインターフェイス
 *
 * @author khashimoto
 */
public interface DistPlanningListDLSummaryDao {

	/**
	 * ソート順<br>
	 * 支店コードソート順<br>
	 * 営業所コードソート順<br>
	 * 品目並び順ソート順
	 */
	static final String SORT_STRING_EXCEL = "ORDER BY SOS3.BR_CODE, SOS3.DIST_CODE, CMST.DATA_SEQ";

	/**
	 * 営業所計画アップロード用のフォーマットファイルサマリー情報リストを取得する。
	 *
	 * <pre>
	 *   支店・営業所コード	  組織情報．医薬支店Ｃ＋組織情報．医薬営業所Ｃ
     *   支店・営業所名	      組織情報．部門名正式（支店名）＋組織情報．部門名正式（営業所名）
     *   統計品名コード	      計画対象品目．統計品名コード(品目)
     *   品目名	              計画対象品目．品目名称
	 * </pre>
	 *
	 * @param sortString ソート条件
	 * @param scDto 検索条件
	 * @return 営業所計画アップロード用のフォーマットファイルのサマリー情報リスト
	 * @throws DataNotFoundException 検索結果が0件の場合にスロー
	 */
	List<DistPlanningListDLSummary> searchList(String sortString, DistPlanningListSummaryScDto scDto) throws DataNotFoundException;

	List<DistPlanningListDLSummary> searchExceptCategoryList(String sortStringExcel,
			DistPlanningListSummaryScDto scDto) throws DataNotFoundException;

}
