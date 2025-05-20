package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DistParamResultDto;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.InsWsDistProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;

/**
 * 配分機能の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsDistributionProdSearchService {

	/**
	 * カテゴリ、組織コード(営業所)をもとに、配分対象品目一覧を取得する。
	 *
	 * @param category カテゴリ
	 * @param sosCd 組織コード(営業所)
	 * @return 配分対象品目検索結果DTOのリスト
	 */
	//TODO:shi 20210219 検索時エラーの対応　最後問題なければTODO消す
//	List<InsWsDistProdResultDto> searchDistributionProdList(ProdCategory category, String sosCd) throws DataNotFoundException;
	List<InsWsDistProdResultDto> searchDistributionProdList(String category, String sosCd) throws DataNotFoundException;

	/**
	 * 配分基準（本部案、営業所案）を取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 配分基準検索結果DTOのリスト
	 */
	DistParamResultDto searchDistributionParam(String sosCd, String prodCode, String category);

	/**
	 * 配分処理実行前の担当者別計画立案ステータスを取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param insWsDistProdUpdateDtoList 配分品目一覧更新DTOのリスト
	 * @return 配分実行用DTOのリスト
	 */
	List<DistributionExecOrgDto> searchDistributionPreparation(String sosCd, List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList);

}
