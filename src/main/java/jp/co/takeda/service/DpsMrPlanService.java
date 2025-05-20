package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.EstimationResultDetailExecDto;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.MrPlanStatusUpdateDto;
import jp.co.takeda.dto.MrPlanUpdateDto;
import jp.co.takeda.dto.OfficeTeamPlanChoseiDto;
import jp.co.takeda.dto.PlannedValueCopyDto;
import jp.co.takeda.dto.TeamPlanUpdateDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.StatusForMrPlan;

/**
 * 担当者別計画機能の更新に関するサービスインタフェース
 *
 * @author stakeuchi
 */
public interface DpsMrPlanService {

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「公開解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusTeamOpenRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「公開」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusTeamOpen(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「確定解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusTeamFixRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「確定」状態に更新する。
	 * <ul>
	 * <li>営業所計画のステータスチェック</li>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusTeamFix(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「公開解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusMrOpenRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「公開」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusMrOpen(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「確定解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>施設特約店別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusMrFixRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「確定」状態に更新する。
	 * <ul>
	 * <li>営業所計画のステータスチェック</li>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	void updateStatusMrFix(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException;

	/**
	 * 担当者別計画(営業所)の登録・更新を行う。<br>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param mrPlanUpdateDto 担当者別計画更新用DTO
	 */
//	void updateMrPlan(String sosCd3, MrPlanUpdateDto mrPlanUpdateDto) throws LogicalException;
	void updateMrPlan(String sosCd3, MrPlanUpdateDto mrPlanUpdateDto, String category, StatusForMrPlan statusForMrPlan) throws LogicalException;

	/**
	 * 担当者別計画(AL)の登録・更新を行う。<br>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param fixFlg 確定フラグ true:入力完了 false:入力中
	 * @param mrPlanUpdateDto 担当者別計画更新用DTO
	 */
//	void updateMrPlan(String sosCd3, String sosCd4, boolean fixFlg, MrPlanUpdateDto mrPlanUpdateDto) throws LogicalException;
	void updateMrPlan(String sosCd3, String sosCd4, boolean fixFlg, MrPlanUpdateDto mrPlanUpdateDto, String category, StatusForMrPlan statusForMrPlan) throws LogicalException;

	/**
	 * チーム別計画の登録・更新を行う。<br>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param mrPlanUpdateDto 担当者別計画更新用DTO
	 */
	void updateTeamPlan(String sosCd3, TeamPlanUpdateDto teamPlanUpdateDto) throws LogicalException;

	/**
	 * 調整金額用のメッセージを生成する。
	 *
	 * @param dto 検索結果DTO
	 * @return 調整金額用のメッセージ
	 */
	String createChoseiMsg(MrPlanResultDto dto);

	/**
	 * 調整金額用のメッセージを生成する。
	 *
	 * @param dto 検索結果DTO
	 * @return 調整金額用のメッセージ
	 */
	String createChoseiMsg(OfficeTeamPlanChoseiDto dto);

	/**
	 * 営業所案の試算パラメータを更新後に試算処理を実行する。
	 *
	 * @param estimationParamUpdateDto 試算処理実行用DTO
	 */
	void executeEsimationUpdateOffice(EstimationResultDetailExecDto estimationResultDetailExecDto) throws LogicalException;

	/**
	 * 営業所案の試算パラメータを削除後に試算処理を実行する。
	 *
	 * @param estimationParamUpdateDto 試算処理実行用DTO
	 */
	void executeEsimationDeleteOffice(EstimationResultDetailExecDto estimationResultDetailExecDto) throws LogicalException;

	/**
	 * 計画値の一括コピーを行う。
	 *
	 *
	 * @param copyDto 計画値の一括コピー処理実行用DTO
	 * @throws LogicalException
	 */
	void executePlannedValueCopy(PlannedValueCopyDto copyDto) throws LogicalException;

}
