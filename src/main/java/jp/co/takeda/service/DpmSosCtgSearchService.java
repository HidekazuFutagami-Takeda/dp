package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosCtg;

/**
 * 組織カテゴリコード検索サービスインターフェース
 *
 * @author rna8405
 *
 */
public interface DpmSosCtgSearchService {

	/**
	 * 組織コードからカテゴリコード一覧を取得する。
	 * @param sosCd 組織コード
	 * @param screenId 画面ID
	 * @return カテゴリコード一覧
	 * @throws DataNotFoundException
	 */
	public List<SosCtg> searchDpmSosCtgList(String sosCd, String screenId);
}
