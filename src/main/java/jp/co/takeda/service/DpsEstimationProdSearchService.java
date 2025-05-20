package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.EstimationExecDto;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.dto.EstimationParamResultDto;
import jp.co.takeda.dto.EstimationParamUpdateDto;
import jp.co.takeda.dto.EstimationProdListResultDto;
import jp.co.takeda.dto.FreeIndexResultDto;

/**
 * 試算機能の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsEstimationProdSearchService {

	/**
	 * 組織コード(営業所)をもとに、試算対象品目一覧を取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @return 試算対象品目検索結果DTOのリスト
	 */
//	EstimationProdListResultDto searchEstimationProdList(String sosCd) throws LogicalException;
	EstimationProdListResultDto searchEstimationProdList(String sosCd, String category) throws LogicalException;
//	EstimationProdListResultDto searchEstimationProdList(String sosCd, List<CodeAndValue> categorylist) throws LogicalException;


	/**
	 * 試算パラメータ（本部案、営業所案）を取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 試算パラメータ検索結果DTOのリスト
	 */
	EstimationParamResultDto searchEstimationParam(String sosCd, String prodCode);

	/**
	 * 試算パラメータ（営業所案）の事前登録チェック。
	 * @param estimationParamUpdateDto
	 * @return メッセージリスト
	 */
	List<String> paramUpdateCheck(EstimationParamUpdateDto estimationParamUpdateDto);

	/**
	 * 試算前警告チェック
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param prodCodeArr 品目固定コードの配列
	 * @return メッセージリスト
	 */
	List<String> searchNoResultProdList(String sosCd, String[] prodCodeArr);

	/**
	 * 試算処理実行前の担当者別計画立案ステータスを取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @return 担当者別計画立案ステータス更新前のステータスリスト
	 */
	List<EstimationExecOrgDto> searchEstimationPreparation(String sosCd, List<EstimationExecDto> estimationExecDtoList);

	/**
	 * 担当者別フリー項目を取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 担当者別フリー項目検索結果DTOのリスト
	 * @throws LogicalException
	 */
	FreeIndexResultDto searchFreeIndex(String sosCd, String prodCode) throws LogicalException;

}
