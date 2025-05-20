package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.model.SosGrp;

/**
 * 組織グループ検索サービスインターフェース
 *
 * @author rna8405
 *
 */
public interface DpmSosGrpSearchService {

	/**
	 * 組織のグループコードを取得する
	 * @param sosCd 組織コード
	 * @return グループコード
	 */
	public List<SosGrp> searchSosGrp(String sosCd);
}
