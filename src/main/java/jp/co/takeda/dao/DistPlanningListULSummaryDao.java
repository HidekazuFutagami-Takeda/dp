package jp.co.takeda.dao;

import java.io.InputStream;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.DistPlanningListULSummary;

/**
 * 営業所計画アップロード用のフォーマットファイルのサマリー情報取得DAOインターフェイス
 *
 * @author khashimoto
 */
public interface DistPlanningListULSummaryDao {

	/**
	 * 営業所計画サマリー情報リストを取得する。
	 *
	 * <pre>
	 *   支店・営業所コード	  営業所計画．組織コード
     *   支店・営業所名	      組織情報．部門名正式（支店名）＋組織情報．部門名正式（営業所名）
     *   統計品名コード	      営業所計画．品目固定コード
     *   品目名	              計画対象品目．品目名称
     *   計画値UH(Y・B)       営業所計画.計画値UH（Y）
     *   計画値P(Y・B)        営業所計画.計画値P（Y）
	 * </pre>
	 *
	 * @param 営業所計画アップロード用のフォーマットファイル
	 * @return 営業所計画サマリー情報リスト
	 * @throws DataNotFoundException 検索結果が0件の場合にスロー
	 */
	List<DistPlanningListULSummary> excelList(InputStream fileIn) throws DataNotFoundException;
}
