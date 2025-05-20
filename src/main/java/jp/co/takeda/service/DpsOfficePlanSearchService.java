package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.OfficePlanResultListDto;

/**
 * 営業所計画の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsOfficePlanSearchService {

	/**
	 * 組織コード(営業所)とカテゴリをもとに、営業所計画を取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param category カテゴリ
	 * @param updateFlg 調整金額を更新するかを示すフラグ(真の場合、調整金額テーブルを更新する)
	 * @return 営業所計画検索結果DTO
	 */
//	OfficePlanResultListDto searchOfficePlan(String sosCd3, ProdCategory category, boolean updateFlg) throws LogicalException;
	OfficePlanResultListDto searchOfficePlan(String sosCd3, String category, boolean updateFlg) throws LogicalException;

}
