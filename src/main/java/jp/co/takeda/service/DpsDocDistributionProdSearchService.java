package jp.co.takeda.service;

import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DocDistributionJgiDto;

/**
 * 配分機能の検索に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpsDocDistributionProdSearchService {

	/**
	 * 組織コード(営業所)をもとに、医師別計画配分対象品目一覧を取得する。
	 * 
	 * @param category カテゴリ
	 * @param sosCd 組織コード(営業所)
	 * @return 配分対象品目検索結果DTO
	 */
	Map<String, Object> searchDistributionProdList(String sosCd) throws DataNotFoundException;

	/**
	 * 配分対象の従業員情報を作成する。
	 * 
	 * @param sosCd 組織コード（営業所）
	 * @param jgiNo 従業員番号（組織指定の場合はNULL）
	 * @param doMrOpen 同時公開するか
	 * @param doPlanClear 計画をクリアするか
	 * @return
	 */
	List<DocDistributionJgiDto> createDocDistributionJgiList(String sosCd, Integer jgiNo, boolean doMrOpen, boolean doPlanClear);
}
