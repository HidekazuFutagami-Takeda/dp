package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.div.ProdCategory;

/**
 * 営業所計画立案ステータスの検索に関するサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsOfficePlanStatusSearchService {

	/**
	 * 営業所計画立案ステータスを検索する
	 * MMP or ONC を対象とする。
	 * 
	 * @param sosCd 組織コード
	 * @throws LogicalException
	 */
	OfficePlanStatus searchMMP(String sosCd) throws LogicalException;

}
