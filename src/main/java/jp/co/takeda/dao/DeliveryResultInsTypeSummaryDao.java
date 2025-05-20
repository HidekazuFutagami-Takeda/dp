package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.view.DeliveryResultInsTypeSummary;

/**
 * 納入実績の施設出力対象区分別集計を取得するDAOインターフェイス
 * 
 * @author nozaki
 */
public interface DeliveryResultInsTypeSummaryDao {

	/**
	 * 組織コード・試算品目コードより、納入実績を施設出力対象区分別に集計し、取得する。<br>
	 * 試算用の実績集計のため、削除施設を含まない。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param refProdCode 試算品目の品目固定コード
	 * @param refFrom 参照期間FROM
	 * @param refTo 参照期間TO
	 * @return 納入実績の施設出力対象区分別集計のリスト
	 * @throws DataNotFoundException
	 */
	public List<DeliveryResultInsTypeSummary> searchSummaryList(String sosCd3, String refProdCode, RefPeriod refFrom, RefPeriod refTo) throws DataNotFoundException;
}
