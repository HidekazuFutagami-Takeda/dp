package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenDistParamResultDto;
import jp.co.takeda.dto.TmsTytenPlanDistProdResultDto;

/**
 * 特約店別計画配分検索サービスインターフェイス
 *
 * @author yokokawa
 */
public interface DpsTmsTytenPlanSearchService {

	/**
	 * 特約店別計画配分対象品目一覧を検索する。
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param category 品目カテゴリ
	 * @return 特約店別計画配分対象品目一覧
	 */
	TmsTytenPlanDistProdResultDto searchDistProdList(String sosCd2, String category) throws LogicalException;

	/**
	 * 特約店別計画配分パラメータを取得する
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param prodCodeList 固定品目コードリスト
	 * @return 特約店別計画配分パラメータ
	 */
	TmsTytenDistParamResultDto searchTmsTytenDistParam(String sosCd2, List<String> prodCodeList, String category);
}
