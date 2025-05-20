package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.dto.EstimationParamUpdateDto;
import jp.co.takeda.dto.FreeIndexUpdateDto;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.div.CalcType;

/**
 * 試算機能の実行処理に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsEstimationProdService {

	/**
	 * 試算処理実行の前処理を実行する。
	 * <ul>
	 * <li>ステータスチェック</li>
	 * <li>担当者別計画立案ステータス更新</li>
	 * </ul>
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param estimationExecDtoList 試算実行用DTO
	 * @param calcType 試算タイプ
	 * @param appServerKbn サーバ区分
	 * @return 担当者別計画立案ステータス更新前のステータスリスト
	 */
	// mod Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
	//List<MrPlanStatus> estimationPreparation(String sosCd, List<EstimationExecOrgDto> estimationExecDtoList, CalcType calcType, String appServerKbn);
//	List<MrPlanStatus> estimationPreparation(String sosCd, List<EstimationExecOrgDto> estimationExecDtoList, CalcType calcType, String category, String appServerKbn);
	List<MrPlanStatus> estimationPreparation(String sosCd, List<EstimationExecOrgDto> estimationExecDtoList, CalcType calcType, String appServerKbn, String categorySearch, String categorySelect);
	// mod End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
	/**
	 * 試算パラメータ(営業所案)の更新・登録処理を実行する。
	 *
	 * @param estimationParamUpdateDto 試算パラメータ更新用DTO
	 */
	void updateEstParamOffice(EstimationParamUpdateDto estimationParamUpdateDto);

	/**
	 * 試算パラメータを本部案に戻す処理実行する。 <br>
	 * 営業所案の削除を行なう。
	 *
	 * @param estimationParamUpdateDto 試算パラメータ更新用DTO
	 */
	void deleteEstParamOffice(EstimationParamUpdateDto estimationParamUpdateDto);

	/**
	 * フリー項目の更新処理を実行する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param freeIndexUpdateDto フリー項目更新用DTO
	 */
	void updateIndexFree(String sosCd, FreeIndexUpdateDto freeIndexUpdateDto);

}
