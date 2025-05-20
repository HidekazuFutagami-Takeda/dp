package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.takeda.dto.EstimationIndexTotalDto;
import jp.co.takeda.dto.EstimationResultDetailResultDto;
import jp.co.takeda.dto.EstimationResultDetailResultInfoDto;
import jp.co.takeda.dto.EstimationResultDetailResultRowDto;
import jp.co.takeda.dto.MrPlanDto;
import jp.co.takeda.dto.MrPlanPlannedValueDto;
import jp.co.takeda.dto.MrPlanResultValueDto;
import jp.co.takeda.dto.OfficePlanDto;
import jp.co.takeda.dto.TeamPlanDto;
import jp.co.takeda.model.EstParamHonbu;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.ProdInfo;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.view.EstimationRatio;
import jp.co.takeda.model.view.MrEstimationRatio;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 試算結果詳細の検索結果用DTOを生成するロジッククラス
 *
 * <pre>
 * DAOの検索結果からは試算結果詳細の共通情報と一覧(計画値情報と構成比情報)が全て別々に取得される。
 * 上記をひとつのDTOにまとめることがこのクラスの役割となる。
 *
 * このクラスの役割を以下に示す。
 * <li>試算結果詳細の共通情報DTOの生成</li>
 * <li>試算結果詳細の明細情報DTOの生成</li>
 * <li>比率の算出</li>
 *
 * このロジッククラスは呼び出し元に依存せず、DTO生成に必要な初期設定値がNullだった場合は
 * Nullチェックを行うのみとし、例外処理は呼び出し元で管理するものとする。
 * </pre>
 *
 * @author stakeuchi
 */
public class EstimationResultDetailResultDtoCreateLogic {

	/**
	 * 計画対象品目モデル
	 */
	private final PlannedProd plannedProd;

	/**
	 * 試算パラメータ(営業所)モデル
	 */
	private final EstParamOffice estParamOffice;

	/**
	 * 試算パラメータ(本部)モデル
	 */
	private final EstParamHonbu estParamHonbu;

	/**
	 * 担当者別計画ステータスモデル
	 */
	private final MrPlanStatus mrPlanStatus;

	/**
	 * 営業所計画DTO(検索結果用DTOの基準値)
	 */
	private final OfficePlanDto officePlanDto;

	/**
	 * チーム別営業所構成比を保持するMAP<br>
	 * キー組織コード、値：MAP<従業員番号, 営業所計画を母数とした担当者別試算構成比オブジェクト>)>
	 */
	private final Map<String, Map<Integer, MrEstimationRatio>> officeMrEstimationRatioMap;

	/**
	 * チーム別チーム構成比を保持するMAP<br>
	 * キー組織コード、値：MAP<従業員番号, チーム別計画を母数とした担当者別試算構成比オブジェクト>)>
	 */
	private final Map<String, Map<Integer, MrEstimationRatio>> teamMrEstimationRatioMap;

	/**
	 * 担当者別計画の最新レコード
	 */
	private final MrPlan mrPlan;

	/**
	 * 組織コード(チーム)<br>
	 * チームを指定する場合は設定する。チームを指定しない場合はNULLとする。
	 */
	private final String sosCd4;

	/**
	 * 試算指数合計用ＤＴＯクラス
	 */
	private final EstimationIndexTotalDto estimationIndexTotalDto;

	/**
	 * コンストラクタ
	 *
	 * @param plannedProd 計画対象品目モデル
	 * @param estParamOffice 試算パラメータ(営業所)モデル
	 * @param estParamHonbu 試算パラメータ(本部)モデル
	 * @param mrPlanStatus 担当者別計画ステータスモデル
	 * @param officePlanDto 営業所計画DTO
	 * @param officeMrEstimationRatioList 営業所を母数とした構成比を保持するオブジェクトリスト
	 * @param teamMrEstimationRatioList チームを母数とした構成比を保持するオブジェクトリスト
	 * @param mrPlan 担当者別計画の最新レコード
	 * @param sosCd4 組織コード(チーム) NULL可
	 * @param estimationIndexTotalDto 試算指数合計用ＤＴＯクラス
	 */
	public EstimationResultDetailResultDtoCreateLogic(PlannedProd plannedProd, EstParamOffice estParamOffice, EstParamHonbu estParamHonbu, MrPlanStatus mrPlanStatus,
		OfficePlanDto officePlanDto, List<MrEstimationRatio> officeMrEstimationRatioList, List<MrEstimationRatio> teamMrEstimationRatioList, MrPlan mrPlan, String sosCd4,
		EstimationIndexTotalDto estimationIndexTotalDto) {
		this.plannedProd = plannedProd;
		this.estParamOffice = estParamOffice;
		this.estParamHonbu = estParamHonbu;
		this.mrPlanStatus = mrPlanStatus;
		this.officePlanDto = officePlanDto;
		this.officeMrEstimationRatioMap = createMrEstimationRatioMap(officeMrEstimationRatioList);
		this.teamMrEstimationRatioMap = createMrEstimationRatioMap(teamMrEstimationRatioList);
		this.mrPlan = mrPlan;
		this.sosCd4 = sosCd4;
		this.estimationIndexTotalDto = estimationIndexTotalDto;
	}

	/**
	 * 検索結果用DTOの生成
	 *
	 * <pre>
	 * コンストラクタでの初期設定値を元に検索結果用のDTOを生成し取得する。
	 * </pre>
	 *
	 * @return 検索結果用DTO
	 */
	public EstimationResultDetailResultDto createResultDto() {
		// フィールド値が正常でない場合はNullでReturn(例外処理は呼び出し元に委譲する)
		if (!isFieldValid()) {
			return null;
		}
		// 共通情報の生成
		final EstimationResultDetailResultInfoDto resultInfoDto = createResultInfoDto();
		// 一覧情報の生成
		final List<EstimationResultDetailResultRowDto> resultRowDtoList = createResultRowDtoList();
		// 検索結果の生成
		EstimationResultDetailResultDto resultData = new EstimationResultDetailResultDto(resultInfoDto, resultRowDtoList);
		// 絞込処理
		resultData = createTargetData(resultData, sosCd4);
		// 結果の返却
		return resultData;
	}

	/**
	 * フィールド値の正当性チェック
	 *
	 * @return TRUE=正当 FALSE=不正
	 */
	private boolean isFieldValid() {
		// 検索結果用DTOの基本情報となる値がNullの場合は不正
		if (plannedProd == null || officePlanDto == null) {
			return false;
		}
		// 試算パラメータが本部・営業所の両方がNullの場合は不正
		if (estParamHonbu == null && estParamOffice == null) {
			return false;
		}
		// 試算構成比情報がNullの場合は不正(コンストラクタにて生成済みである必要がある))
		if (officeMrEstimationRatioMap == null || teamMrEstimationRatioMap == null) {
			return false;
		}
		return true;
	}

	/**
	 * 共通情報を表すDTOの生成
	 *
	 * @return 共通情報を表すDTO
	 */
	private EstimationResultDetailResultInfoDto createResultInfoDto() {

		// 試算パラメータのデータセット
		String estProdCode = null;
		ProdInfo estProdInfo = null;
		EstimationParam estParam = null;
		Long paramSeqKey = null;
		String paramUpJgiName = null;
		Date paramUpDate = null;
		boolean isParamHonbu = false;
		boolean isParamOffice = false;
		// 試算パラメータ（営業所案）
		if (estParamOffice != null) {
			estProdCode = estParamOffice.getBaseParam().getRefProdCode();
			estProdInfo = estParamOffice.getBaseParam().getRefProdInfo();
			estParam = estParamOffice.getEstimationParam();
			paramSeqKey = estParamOffice.getSeqKey();
			paramUpJgiName = estParamOffice.getUpJgiName();
			paramUpDate = estParamOffice.getUpDate();
			isParamOffice = true;
		}
		// 試算パラメータ（本部案）
		else if (estParamHonbu != null) {
			estProdCode = estParamHonbu.getBaseParam().getRefProdCode();
			estProdInfo = estParamHonbu.getBaseParam().getRefProdInfo();
			estParam = estParamHonbu.getEstimationParam();
			paramSeqKey = null;
			paramUpJgiName = estParamHonbu.getUpJgiName();
			paramUpDate = estParamHonbu.getUpDate();
			isParamHonbu = true;
		}

		// Null値の場合はNullチェック省略のため新規インスタンス生成
		if (estProdInfo == null) {
			estProdInfo = new ProdInfo();
		}
		if (estParam == null) {
			estParam = new EstimationParam();
		}

		// フリー項目名が設定されていない場合は、デフォルト値を使用する
		if (StringUtils.isEmpty(estParam.getIndexFreeName1())) {
			estParam.setIndexFreeName1(EstimationParam.DEFAULT_INDEX_FREE_NAME1);
		}
		if (StringUtils.isEmpty(estParam.getIndexFreeName2())) {
			estParam.setIndexFreeName2(EstimationParam.DEFAULT_INDEX_FREE_NAME2);
		}
		if (StringUtils.isEmpty(estParam.getIndexFreeName3())) {
			estParam.setIndexFreeName3(EstimationParam.DEFAULT_INDEX_FREE_NAME3);
		}

		// 編集権限フラグのセット
		boolean canTeamPlanEdit = false;
		boolean canMrPlanEdit = false;
		if (mrPlanStatus != null) {
			if (mrPlanStatus.getStatusForMrPlan() != null) {
				switch (mrPlanStatus.getStatusForMrPlan()) {
					case ESTIMATED:
						// チーム別計画編集フラグON
						canTeamPlanEdit = true;
						break;
					case TEAM_PLAN_OPENED:
						// チーム別計画編集フラグON
						canTeamPlanEdit = true;
						break;
					case TEAM_PLAN_COMPLETED:
						// 担当別計画編集フラグON
						canMrPlanEdit = true;
						break;
					case MR_PLAN_OPENED:
						// 担当別計画編集フラグON
						canMrPlanEdit = true;
						break;
					case MR_PLAN_INPUT_FINISHED:
						// 担当別計画編集フラグON
						canMrPlanEdit = true;
						break;
					case MR_PLAN_COMPLETED:
						// 担当別計画編集フラグON
						canMrPlanEdit = true;
						break;
					default:
						break;
				}
			}
		}

		// ----------------------
		// 共通情報を表すDTOを生成
		// ----------------------
		final String prodCode = plannedProd.getProdCode();
		final String prodName = plannedProd.getProdName();
		final String prodType = plannedProd.getProdType();
		final String estProdName = estProdInfo.getProdName();
		final String estProdType = estProdInfo.getProdType();
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		final Integer indexRyhRtsu = estParam.getIndexRyhRtsu();
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		final Integer indexMikakutoku = estParam.getIndexMikakutoku();
		final Integer indexDelivery = estParam.getIndexDelivery();
		final Integer indexFree1 = estParam.getIndexFree1();
		final Integer indexFree2 = estParam.getIndexFree2();
		final Integer indexFree3 = estParam.getIndexFree3();
		final String indexFreeName1 = estParam.getIndexFreeName1();
		final String indexFreeName2 = estParam.getIndexFreeName2();
		final String indexFreeName3 = estParam.getIndexFreeName3();
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//		return new EstimationResultDetailResultInfoDto(prodCode, prodName, prodType, estProdCode, estProdName, estProdType, paramSeqKey, paramUpJgiName, paramUpDate,
//				indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, indexFreeName1, indexFreeName2, indexFreeName3, isParamHonbu, isParamOffice, canTeamPlanEdit,
//				canMrPlanEdit, mrPlan);
		return new EstimationResultDetailResultInfoDto(prodCode, prodName, prodType, estProdCode, estProdName, estProdType, paramSeqKey, paramUpJgiName, paramUpDate,
				indexRyhRtsu, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, indexFreeName1, indexFreeName2, indexFreeName3, isParamHonbu, isParamOffice, canTeamPlanEdit,
				canMrPlanEdit, mrPlan);
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	}

	/**
	 * 明細一覧情報を表すDTOのリスト生成
	 *
	 * @return 明細一覧情報を表すDTOのリスト
	 */
	private List<EstimationResultDetailResultRowDto> createResultRowDtoList() {
		List<EstimationResultDetailResultRowDto> rowDtoList = new ArrayList<EstimationResultDetailResultRowDto>();

		// 営業所計画部の生成
		EstimationResultDetailResultRowDto officeRow = createResultRowDtoOfficeRow();

		// チーム別計画部の生成
		List<EstimationResultDetailResultRowDto> teamRowList = createResultRowDtoTeamRowList();

		// チーム別計画部合計行の生成
		EstimationResultDetailResultRowDto teamSumRow = createResultRowDtoTeamSumRow();

		// 担当者別計画部(合計行含む)の生成
		List<EstimationResultDetailResultRowDto> mrRowList = createResultRowDtoMrRowList();

		// 画面の一覧テーブル表示順に挿入
		rowDtoList.add(officeRow);
		rowDtoList.addAll(teamRowList);
		rowDtoList.add(teamSumRow);
		rowDtoList.addAll(mrRowList);

		return rowDtoList;
	}

	/**
	 * 指定チームに絞込みを行う。
	 *
	 * @param resultDto 絞込み対象の結果オブジェクト
	 * @param sosCd4 組織コード(チーム)
	 * @return 絞り込んだ後の結果オブジェクト
	 */
	private EstimationResultDetailResultDto createTargetData(EstimationResultDetailResultDto resultDto, String sosCd4) {

		// 組織コード(チーム)が指定されていない場合は絞り込み処理を行わない
		if (sosCd4 == null) {
			return resultDto;
		}
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ＡＬではない場合はそのままリターン
		if (!dpUser.isMatch(JokenSet.IYAKU_AL)) {
			return resultDto;
		}

		// 組織コード(チーム)が指定されている かつ ＡＬである場合、絞り込みを行う
		EstimationResultDetailResultInfoDto infoDto = resultDto.getInfoDto();
		List<EstimationResultDetailResultRowDto> rowAllDtoList = resultDto.getRowDtoList();
		List<EstimationResultDetailResultRowDto> rowResultDtoList = new ArrayList<EstimationResultDetailResultRowDto>();
		for (int i = 0; i < rowAllDtoList.size(); i++) {
			EstimationResultDetailResultRowDto rowDto = rowAllDtoList.get(i);

			// 営業所行：追加
			boolean isOfficeRow = rowDto.getIsOfficeRow();
			if (isOfficeRow) {
				rowResultDtoList.add(rowDto);
			}

			// チーム行：組織コードが一致すれば追加
			boolean isTeamRow = rowDto.getIsTeamRow();
			if (isTeamRow && sosCd4.equals(rowDto.getSosCd())) {
				rowResultDtoList.add(rowDto);
				EstimationResultDetailResultRowDto teamSumRow = new EstimationResultDetailResultRowDto(rowDto.getSosCd(), null, null, null, rowDto.getSpecialInsPlanValue(), rowDto
					.getPlanForUp1(), rowDto.getTheoreticalValue1(), rowDto.getLastSameRatio1(), rowDto.getPlanForUp2(), rowDto.getTheoreticalValue2(), rowDto.getLastSameRatio2(),
					rowDto.getSumOfficeRatio(), rowDto.getSumTeamRatio(), rowDto.getMikakutokuValue(), rowDto.getMikakutokuRatioOffice(), rowDto.getMikakutokuRatioTeam(), rowDto
						.getDeliveryValue(), rowDto.getDeliveryRatioOffice(), rowDto.getDeliveryRatioTeam(), rowDto.getFree1Value(), rowDto.getFree1RatioOffice(), rowDto
						.getFree1RatioTeam(), rowDto.getFree2Value(), rowDto.getFree2RatioOffice(), rowDto.getFree2RatioTeam(), rowDto.getFree3Value(), rowDto
						.getFree3RatioOffice(), rowDto.getFree3RatioTeam(), false, false, true, false, false, null);
				rowResultDtoList.add(teamSumRow);
			}

			// MR行：組織コードが一致すれば追加
			boolean isMrRow = rowDto.getIsMrRow();
			if (isMrRow && sosCd4.equals(rowDto.getSosCd())) {
				rowResultDtoList.add(rowDto);
			}
			// MR集計行：組織コードが一致すれば追加
			boolean isMrSumRow = rowDto.getIsMrSumRow();
			if (isMrSumRow && sosCd4.equals(rowDto.getSosCd())) {
				rowResultDtoList.add(rowDto);
			}
		}
		return new EstimationResultDetailResultDto(infoDto, rowResultDtoList);
	}

	/**
	 * 営業所計画部を表すDTO生成
	 *
	 * @return 営業所計画部を表すDTO生成(営業所単位となるため1オブジェクト)
	 */
	private EstimationResultDetailResultRowDto createResultRowDtoOfficeRow() {
		// 計画値を挿入したDTOを取得
		final MrPlanPlannedValueDto plannedValueDto = officePlanDto.getPlannedValueDto();
		final MrPlanResultValueDto resultValueDto = officePlanDto.getResultValueDto();
		final EstimationResultDetailResultRowDto planValueDto = createPlanValueDto(plannedValueDto, resultValueDto, false);
		// 構成比を挿入したDTOを取得
		final EstimationResultDetailResultRowDto rateValueDto = createOfficeRateValueDto();
		// DTOを生成してReturn
		return new EstimationResultDetailResultRowDto(planValueDto.getSosCd(), null, null, null, planValueDto.getSpecialInsPlanValue(), planValueDto.getPlanForUp1(), planValueDto
			.getTheoreticalValue1(), planValueDto.getLastSameRatio1(), planValueDto.getPlanForUp2(), planValueDto.getTheoreticalValue2(), planValueDto.getLastSameRatio2(),
			rateValueDto.getSumOfficeRatio(), rateValueDto.getSumTeamRatio(), rateValueDto.getMikakutokuValue(), rateValueDto.getMikakutokuRatioOffice(), rateValueDto
				.getMikakutokuRatioTeam(), rateValueDto.getDeliveryValue(), rateValueDto.getDeliveryRatioOffice(), rateValueDto.getDeliveryRatioTeam(), rateValueDto
				.getFree1Value(), rateValueDto.getFree1RatioOffice(), rateValueDto.getFree1RatioTeam(), rateValueDto.getFree2Value(), rateValueDto.getFree2RatioOffice(),
			rateValueDto.getFree2RatioTeam(), rateValueDto.getFree3Value(), rateValueDto.getFree3RatioOffice(), rateValueDto.getFree3RatioTeam(), true, false, false, false, false, null);
	}

	/**
	 * チーム別計画部を表すDTOのリスト生成
	 *
	 * @return チーム別計画部を表すDTOのリスト
	 */
	private List<EstimationResultDetailResultRowDto> createResultRowDtoTeamRowList() {
		List<EstimationResultDetailResultRowDto> teamRowList = new ArrayList<EstimationResultDetailResultRowDto>();
		List<TeamPlanDto> teamPlanDtoList = officePlanDto.getTeamPlanDtoList();
		if (teamPlanDtoList != null) {
			for (TeamPlanDto teamPlanDto : teamPlanDtoList) {
				final String sosCd = teamPlanDto.getSosCd();
				final String sosName = teamPlanDto.getSosName();
				// 計画値を挿入したDTOを取得
				final MrPlanPlannedValueDto plannedValueDto = teamPlanDto.getPlannedValueDto();
				final MrPlanResultValueDto resultValueDto = teamPlanDto.getResultValueDto();
				final EstimationResultDetailResultRowDto planValueDto = createPlanValueDto(plannedValueDto, resultValueDto, false);
				// 構成比を挿入したDTOを取得
				final EstimationResultDetailResultRowDto rateValueDto = createTeamRateValueDto(sosCd);
				// DTOを生成してリストに追加
				teamRowList.add(new EstimationResultDetailResultRowDto(sosCd, sosName, null, null, planValueDto.getSpecialInsPlanValue(), planValueDto.getPlanForUp1(),
					planValueDto.getTheoreticalValue1(), planValueDto.getLastSameRatio1(), planValueDto.getPlanForUp2(), planValueDto.getTheoreticalValue2(), planValueDto
						.getLastSameRatio2(), rateValueDto.getSumOfficeRatio(), rateValueDto.getSumTeamRatio(), rateValueDto.getMikakutokuValue(), rateValueDto
						.getMikakutokuRatioOffice(), rateValueDto.getMikakutokuRatioTeam(), rateValueDto.getDeliveryValue(), rateValueDto.getDeliveryRatioOffice(), rateValueDto
						.getDeliveryRatioTeam(), rateValueDto.getFree1Value(), rateValueDto.getFree1RatioOffice(), rateValueDto.getFree1RatioTeam(), rateValueDto.getFree2Value(),
					rateValueDto.getFree2RatioOffice(), rateValueDto.getFree2RatioTeam(), rateValueDto.getFree3Value(), rateValueDto.getFree3RatioOffice(), rateValueDto
						.getFree3RatioTeam(), false, true, false, false, false, null));
			}
		}
		return teamRowList;
	}

	/**
	 * チーム別計画部の合計行を表すDTO生成
	 *
	 * @return チーム別計画部の合計行を表すDTO生成(営業所単位となるため1オブジェクト)
	 */
	private EstimationResultDetailResultRowDto createResultRowDtoTeamSumRow() {
		final TeamPlanDto totalTeamPlanDto = officePlanDto.getTotalTeamPlanDto();
		if (totalTeamPlanDto == null) {
			return createResultRowDtoEmpty();
		}
		// 計画値を挿入したDTOを取得
		final MrPlanPlannedValueDto plannedValueDto = totalTeamPlanDto.getPlannedValueDto();
		final MrPlanResultValueDto resultValueDto = totalTeamPlanDto.getResultValueDto();
		final EstimationResultDetailResultRowDto planValueDto = createPlanValueDto(plannedValueDto, resultValueDto, false);
		// 構成比を挿入したDTOを取得
		final EstimationResultDetailResultRowDto rateValueDto = createOfficeRateValueDto();
		// DTOを生成してReturn
		return new EstimationResultDetailResultRowDto(totalTeamPlanDto.getSosCd(), null, null, null, planValueDto.getSpecialInsPlanValue(), planValueDto.getPlanForUp1(),
			planValueDto.getTheoreticalValue1(), planValueDto.getLastSameRatio1(), planValueDto.getPlanForUp2(), planValueDto.getTheoreticalValue2(), planValueDto
				.getLastSameRatio2(), rateValueDto.getSumOfficeRatio(), rateValueDto.getSumTeamRatio(), rateValueDto.getMikakutokuValue(), rateValueDto.getMikakutokuRatioOffice(),
			rateValueDto.getMikakutokuRatioTeam(), rateValueDto.getDeliveryValue(), rateValueDto.getDeliveryRatioOffice(), rateValueDto.getDeliveryRatioTeam(), rateValueDto
				.getFree1Value(), rateValueDto.getFree1RatioOffice(), rateValueDto.getFree1RatioTeam(), rateValueDto.getFree2Value(), rateValueDto.getFree2RatioOffice(),
			rateValueDto.getFree2RatioTeam(), rateValueDto.getFree3Value(), rateValueDto.getFree3RatioOffice(), rateValueDto.getFree3RatioTeam(), false, false, true, false, false, null);
	}

	/**
	 * 担当者別計画部を表すDTOのリスト生成
	 *
	 * @return 担当者別計画部を表すDTOのリスト
	 */
	private List<EstimationResultDetailResultRowDto> createResultRowDtoMrRowList() {
		List<EstimationResultDetailResultRowDto> mrRowList = new ArrayList<EstimationResultDetailResultRowDto>();
		List<TeamPlanDto> teamPlanDtoList = officePlanDto.getTeamPlanDtoList();
		if (teamPlanDtoList != null) {
			for (TeamPlanDto teamPlanDto : teamPlanDtoList) {
				List<MrPlanDto> mrPlanDtoList = teamPlanDto.getMrPlanDtoList();
				if (mrPlanDtoList != null) {
					final String sosCd = teamPlanDto.getSosCd();
					final String sosName = teamPlanDto.getSosName();
					for (MrPlanDto mrPlanDto : mrPlanDtoList) {
						final Integer jgiNo = mrPlanDto.getJgiNo();
						final String jgiName = mrPlanDto.getJgiName();
						final String shokushuName = mrPlanDto.getShokushuName();
						// 計画値を挿入したDTOを取得
						final MrPlanPlannedValueDto plannedValueDto = mrPlanDto.getPlannedValueDto();
						final MrPlanResultValueDto resultValueDto = mrPlanDto.getResultValueDto();
						final EstimationResultDetailResultRowDto planValueDto = createPlanValueDto(plannedValueDto, resultValueDto, false);
						// 構成比を挿入したDTOを取得
						final EstimationResultDetailResultRowDto rateValueDto = createMrRateValueDto(sosCd, jgiNo);
						// DTOを生成してリストに追加
						mrRowList.add(new EstimationResultDetailResultRowDto(sosCd, sosName, jgiNo, jgiName, planValueDto.getSpecialInsPlanValue(), planValueDto.getPlanForUp1(),
							planValueDto.getTheoreticalValue1(), planValueDto.getLastSameRatio1(), planValueDto.getPlanForUp2(), planValueDto.getTheoreticalValue2(), planValueDto
								.getLastSameRatio2(), rateValueDto.getSumOfficeRatio(), rateValueDto.getSumTeamRatio(), rateValueDto.getMikakutokuValue(), rateValueDto
								.getMikakutokuRatioOffice(), rateValueDto.getMikakutokuRatioTeam(), rateValueDto.getDeliveryValue(), rateValueDto.getDeliveryRatioOffice(),
							rateValueDto.getDeliveryRatioTeam(), rateValueDto.getFree1Value(), rateValueDto.getFree1RatioOffice(), rateValueDto.getFree1RatioTeam(), rateValueDto
								.getFree2Value(), rateValueDto.getFree2RatioOffice(), rateValueDto.getFree2RatioTeam(), rateValueDto.getFree3Value(), rateValueDto
								.getFree3RatioOffice(), rateValueDto.getFree3RatioTeam(), false, false, false, true, false, shokushuName));
					}
					mrRowList.add(createResultRowDtoMrSumRow(teamPlanDto));
				}
			}
		}
		return mrRowList;
	}

	/**
	 * 担当者別計画部合計行を表すDTOの生成
	 *
	 * @param teamPlanDto 対象チームの計画情報を保持したDTO
	 * @return 担当者別計画部合計行を表すDTO
	 */
	private EstimationResultDetailResultRowDto createResultRowDtoMrSumRow(TeamPlanDto teamPlanDto) {
		if (teamPlanDto == null) {
			return createResultRowDtoEmpty();
		}
		final String sosCd = teamPlanDto.getSosCd();
		// 計画値を挿入したDTOを取得
		final MrPlanPlannedValueDto plannedValueDto = teamPlanDto.getTotalMrPlanDto().getPlannedValueDto();
		final MrPlanResultValueDto resultValueDto = teamPlanDto.getTotalMrPlanDto().getResultValueDto();
		final EstimationResultDetailResultRowDto planValueDto = createPlanValueDto(plannedValueDto, resultValueDto, false);
		// 構成比を挿入したDTOを取得
		final EstimationResultDetailResultRowDto rateValueDto = createTeamRateValueDto(sosCd);
		// DTOを生成してReturn
		return new EstimationResultDetailResultRowDto(sosCd, null, null, null, planValueDto.getSpecialInsPlanValue(), planValueDto.getPlanForUp1(), planValueDto
			.getTheoreticalValue1(), planValueDto.getLastSameRatio1(), planValueDto.getPlanForUp2(), planValueDto.getTheoreticalValue2(), planValueDto.getLastSameRatio2(),
			rateValueDto.getSumOfficeRatio(), rateValueDto.getSumTeamRatio(), rateValueDto.getMikakutokuValue(), rateValueDto.getMikakutokuRatioOffice(), rateValueDto
				.getMikakutokuRatioTeam(), rateValueDto.getDeliveryValue(), rateValueDto.getDeliveryRatioOffice(), rateValueDto.getDeliveryRatioTeam(), rateValueDto
				.getFree1Value(), rateValueDto.getFree1RatioOffice(), rateValueDto.getFree1RatioTeam(), rateValueDto.getFree2Value(), rateValueDto.getFree2RatioOffice(),
			rateValueDto.getFree2RatioTeam(), rateValueDto.getFree3Value(), rateValueDto.getFree3RatioOffice(), rateValueDto.getFree3RatioTeam(), false, false, false, false, true, null);
	}

	/**
	 * 担当者別計画の計画値DTO・担当者別計画の実績情報DTOを元に、計画値を挿入したDTOを生成する。
	 *
	 * @param plannedValueDto 担当者別計画の計画値DTO
	 * @param resultValueDto 担当者別計画の実績情報DTO
	 * @param isNullForZero TRUE=Null値を0に変換, FALSE=Null値の場合はNull値
	 * @return 計画値を挿入したDTO(計画値以外はNULL)
	 */
	private EstimationResultDetailResultRowDto createPlanValueDto(MrPlanPlannedValueDto plannedValueDto, MrPlanResultValueDto resultValueDto, boolean isNullForZero) {
		if (plannedValueDto == null || resultValueDto == null) {
			return createResultRowDtoEmpty();
		}
		// 特定施設個別計画
		Long specialInsPlanValue = plannedValueDto.getSpecialInsPlanValueY();
		// 理論計画1 計画(理論値)
		Long theoreticalValue1 = plannedValueDto.getTheoreticalValue1();
		// 理論計画1 増分(計画－前期実績)
		Long planForUp1 = calcSubtraction(theoreticalValue1, resultValueDto.getAdvancePeriod());
		// 理論計画1 前同比(計画÷前期実績×100)
		Double lastSameRatio1 = calcRatio(theoreticalValue1, resultValueDto.getAdvancePeriod(), isNullForZero);
		// 理論計画2 計画(理論値)
		Long theoreticalValue2 = plannedValueDto.getTheoreticalValue2();
		// 理論計画2 増分(計画－前期実績)
		Long planForUp2 = calcSubtraction(theoreticalValue2, resultValueDto.getAdvancePeriod());
		// 理論計画2 前同比(計画÷前期実績×100)
		Double lastSameRatio2 = calcRatio(theoreticalValue2, resultValueDto.getAdvancePeriod(), isNullForZero);
		if (isNullForZero) {
			if (specialInsPlanValue == null) {
				specialInsPlanValue = 0L;
			}
			if (theoreticalValue1 == null) {
				theoreticalValue1 = 0L;
			}
			if (theoreticalValue2 == null) {
				theoreticalValue2 = 0L;
			}
		}
		return new EstimationResultDetailResultRowDto(null, null, null, null, specialInsPlanValue, planForUp1, theoreticalValue1, lastSameRatio1, planForUp2, theoreticalValue2,
			lastSameRatio2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	/**
	 * 営業所計画部の構成比を挿入したDTOを取得
	 *
	 * @return 営業所計画部の構成比を挿入したDTO
	 */
	private EstimationResultDetailResultRowDto createOfficeRateValueDto() {
		// 所内構成比(100固定)
		Double sumOfficeRatio = 100.0D;
		// チーム内構成比(空白)
		Double sumTeamRatio = null;
		// 未獲得市場
		Long mikakutokuValue = 0L;
		// 未獲得市場 所内構成比(100固定)
		Double mikakutokuRatioOffice = 100.0D;
		// 未獲得市場 チーム構成比(空白)
		Double mikakutokuRatioTeam = null;
		// 納入実績
		Long deliveryValue = 0L;
		// 納入実績 所内構成比(100固定)
		Double deliveryRatioOffice = 100.0D;
		// 納入実績 チーム内構成比(空白)
		Double deliveryRatioTeam = null;
		// フリー項目1
		Long free1Value = 0L;
		// フリー項目1 所内構成比(100固定)
		Double free1RatioOffice = 100.0D;
		// フリー項目1 チーム内構成比(空白)
		Double free1RatioTeam = null;
		// フリー項目2
		Long free2Value = 0L;
		// フリー項目2 所内構成比(100固定)
		Double free2RatioOffice = 100.0D;
		// フリー項目2 チーム内構成比(空白)
		Double free2RatioTeam = null;
		// フリー項目3
		Long free3Value = 0L;
		// フリー項目3 所内構成比(100固定)
		Double free3RatioOffice = 100.0D;
		// フリー項目3 チーム内構成比(空白)
		Double free3RatioTeam = null;
		for (Entry<String, Map<Integer, MrEstimationRatio>> entry : officeMrEstimationRatioMap.entrySet()) {
			Map<Integer, MrEstimationRatio> subMap = officeMrEstimationRatioMap.get(entry.getKey());
			for (Entry<Integer, MrEstimationRatio> subEntry : subMap.entrySet()) {
				MrEstimationRatio mrEstimationRatio = subMap.get(subEntry.getKey());
				List<EstimationRatio> estimationRatioList = mrEstimationRatio.getEstimationRatioList();
				if (estimationRatioList != null) {
					for (EstimationRatio estimationRatio : estimationRatioList) {
						if (estimationRatio.getMikakutokuValue() != null) {
							mikakutokuValue += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getMikakutokuValue());
						}
						if (estimationRatio.getKakoJissekiValue() != null) {
							deliveryValue += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getKakoJissekiValue());
						}
						if (estimationRatio.getFree1Value() != null) {
							free1Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree1Value());
						}
						if (estimationRatio.getFree2Value() != null) {
							free2Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree2Value());
						}
						if (estimationRatio.getFree3Value() != null) {
							free3Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree3Value());
						}
					}
				}
			}
		}
		return new EstimationResultDetailResultRowDto(null, null, null, null, null, null, null, null, null, null, null, sumOfficeRatio, sumTeamRatio, mikakutokuValue,
			mikakutokuRatioOffice, mikakutokuRatioTeam, deliveryValue, deliveryRatioOffice, deliveryRatioTeam, free1Value, free1RatioOffice, free1RatioTeam, free2Value,
			free2RatioOffice, free2RatioTeam, free3Value, free3RatioOffice, free3RatioTeam, null, null, null, null, null, null);
	}

	/**
	 * チーム別計画部の構成比を挿入したDTOを取得
	 *
	 * @param sosCd4 組織コード(チーム)
	 * @return 試算結果詳細の明細一覧の1行を表すDTO
	 */
	private EstimationResultDetailResultRowDto createTeamRateValueDto(String sosCd4) {
		if (StringUtils.isEmpty(sosCd4)) {
			return createResultRowDtoEmpty();
		}
		// 所内構成比
		Double sumOfficeRatio = 0D;
		// チーム内構成比(100固定)
		Double sumTeamRatio = 100.0D;
		// 未獲得市場
		Long mikakutokuValue = 0L;
		// 未獲得市場 所内構成比
		Double mikakutokuRatioOffice = 0D;
		// 未獲得市場 チーム構成比(100固定)
		Double mikakutokuRatioTeam = 100.0D;
		// 納入実績
		Long deliveryValue = 0L;
		// 納入実績 所内構成比
		Double deliveryRatioOffice = 0D;
		// 納入実績 チーム内構成比(100固定)
		Double deliveryRatioTeam = 100.0D;
		// フリー項目1
		Long free1Value = 0L;
		// フリー項目1 所内構成比
		Double free1RatioOffice = 0D;
		// フリー項目1 チーム内構成比(100固定)
		Double free1RatioTeam = 100.0D;
		// フリー項目2
		Long free2Value = 0L;
		// フリー項目2 所内構成比
		Double free2RatioOffice = 0D;
		// フリー項目2 チーム内構成比(100固定)
		Double free2RatioTeam = 100.0D;
		// フリー項目3
		Long free3Value = 0L;
		// フリー項目3 所内構成比
		Double free3RatioOffice = 0D;
		// フリー項目3 チーム内構成比(100固定)
		Double free3RatioTeam = 100.0D;

		// チーム別の構成比を出すために合計値を算出
		Map<Integer, MrEstimationRatio> subMap = officeMrEstimationRatioMap.get(sosCd4);
		for (Entry<Integer, MrEstimationRatio> subEntry : subMap.entrySet()) {
			MrEstimationRatio mrEstimationRatio = subMap.get(subEntry.getKey());
			List<EstimationRatio> estimationRatioList = mrEstimationRatio.getEstimationRatioList();
			if (estimationRatioList != null) {
				for (EstimationRatio estimationRatio : estimationRatioList) {
					if (estimationRatio.getMikakutokuValue() != null) {
						mikakutokuValue += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getMikakutokuValue());
					}
					if (estimationRatio.getKakoJissekiValue() != null) {
						deliveryValue += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getKakoJissekiValue());
					}
					if (estimationRatio.getFree1Value() != null) {
						free1Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree1Value());
					}
					if (estimationRatio.getFree2Value() != null) {
						free2Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree2Value());
					}
					if (estimationRatio.getFree3Value() != null) {
						free3Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree3Value());
					}
				}
			}
		}

		// 所内構成比
		sumOfficeRatio = this.estimationIndexTotalDto.getOfficeEstimationIndexTotalMapBySosCd4Map().get(sosCd4);

		// ---------------------------------
		// 各値
		// ---------------------------------
		EstimationResultDetailResultRowDto officeRateValueDto = createOfficeRateValueDto();

		// 未獲得市場 所内構成比の算出
		mikakutokuRatioOffice = calcRatio(mikakutokuValue, officeRateValueDto.getMikakutokuValue(), true);
		// 納入実績 所内構成比の算出
		deliveryRatioOffice = calcRatio(deliveryValue, officeRateValueDto.getDeliveryValue(), true);
		// フリー項目1 所内構成比の算出
		free1RatioOffice = calcRatio(free1Value, officeRateValueDto.getFree1Value(), true);
		// フリー項目2 所内構成比の算出
		free2RatioOffice = calcRatio(free2Value, officeRateValueDto.getFree2Value(), true);
		// フリー項目3 所内構成比の算出
		free3RatioOffice = calcRatio(free3Value, officeRateValueDto.getFree3Value(), true);

		return new EstimationResultDetailResultRowDto(null, null, null, null, null, null, null, null, null, null, null, sumOfficeRatio, sumTeamRatio, mikakutokuValue,
			mikakutokuRatioOffice, mikakutokuRatioTeam, deliveryValue, deliveryRatioOffice, deliveryRatioTeam, free1Value, free1RatioOffice, free1RatioTeam, free2Value,
			free2RatioOffice, free2RatioTeam, free3Value, free3RatioOffice, free3RatioTeam, null, null, null, null, null, null);
	}

	/**
	 * 担当者別計画部の構成比を挿入したDTOを取得
	 *
	 * @param sosCd4 組織コード(営業所)
	 * @param jgiNo 従業員番号
	 * @return 担当者別計画部の構成比を挿入したDTO
	 */
	private EstimationResultDetailResultRowDto createMrRateValueDto(String sosCd4, Integer jgiNo) {
		if (StringUtils.isEmpty(sosCd4) || jgiNo == null) {
			return createResultRowDtoEmpty();
		}

		// ----------------------------
		// 構成比の算出
		// ----------------------------
		// 未獲得市場
		Long mikakutokuValue = 0L;
		// 未獲得市場 所内構成比
		Double mikakutokuRatioOffice = 0D;
		// 未獲得市場 チーム構成比
		Double mikakutokuRatioTeam = 0D;
		// 納入実績
		Long deliveryValue = 0L;
		// 納入実績 所内構成比
		Double deliveryRatioOffice = 0D;
		// 納入実績 チーム内構成比
		Double deliveryRatioTeam = 0D;
		// フリー項目1
		Long free1Value = 0L;
		// フリー項目1 所内構成比
		Double free1RatioOffice = 0D;
		// フリー項目1 チーム内構成比
		Double free1RatioTeam = 0D;
		// フリー項目2
		Long free2Value = 0L;
		// フリー項目2 所内構成比
		Double free2RatioOffice = 0D;
		// フリー項目2 チーム内構成比
		Double free2RatioTeam = 0D;
		// フリー項目3
		Long free3Value = 0L;
		// フリー項目3 所内構成比
		Double free3RatioOffice = 0D;
		// フリー項目3 チーム内構成比
		Double free3RatioTeam = 0D;
		// 所内構成比を保持する担当者別試算構成比
		MrEstimationRatio mrEstimationRatio = officeMrEstimationRatioMap.get(sosCd4).get(jgiNo);
		List<EstimationRatio> estimationRatioList = mrEstimationRatio.getEstimationRatioList();

		// -------------------------------------------------
		// mrEstimationRatio.getEstimationRatioList()には
		// UH/P/合計のいずれかしか入っていない
		// 検索条件で指定したカテゴリが施設区分が入っている
		// -------------------------------------------------
		for (EstimationRatio estimationRatio : estimationRatioList) {
			if (estimationRatio.getMikakutokuValue() != null) {
				mikakutokuValue += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getMikakutokuValue());
			}
			if (estimationRatio.getMikakutokuRatio() != null) {
				mikakutokuRatioOffice = convertPerRatio(estimationRatio.getMikakutokuRatio());
			}
			if (estimationRatio.getKakoJissekiValue() != null) {
				deliveryValue += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getKakoJissekiValue());
			}
			if (estimationRatio.getKakoJissekiRatio() != null) {
				deliveryRatioOffice = convertPerRatio(estimationRatio.getKakoJissekiRatio());
			}
			if (estimationRatio.getFree1Value() != null) {
				free1Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree1Value());
			}
			if (estimationRatio.getFree1Ratio() != null) {
				free1RatioOffice = convertPerRatio(estimationRatio.getFree1Ratio());
			}
			if (estimationRatio.getFree2Value() != null) {
				free2Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree2Value());
			}
			if (estimationRatio.getFree2Ratio() != null) {
				free2RatioOffice = convertPerRatio(estimationRatio.getFree2Ratio());
			}
			if (estimationRatio.getFree3Value() != null) {
				free3Value += ConvertUtil.parseMoneyToThousandUnit(estimationRatio.getFree3Value());
			}
			if (estimationRatio.getFree3Ratio() != null) {
				free3RatioOffice = convertPerRatio(estimationRatio.getFree3Ratio());
			}
			break; // 必ず「UH」・「P」・「UH+P」のどれかひとつとなる
		}

		// 所内構成比
		Double sumOfficeRatioRate = this.estimationIndexTotalDto.getOfficeEstimationIndexTotalMapByJgiNoMap().get(jgiNo);

		// チーム内構成比
		Double sumTeamRatioRate = this.estimationIndexTotalDto.getTeamEstimationIndexTotalMapByJgiNoMap().get(jgiNo);

		// チーム内構成比を保持する担当者別試算構成比
		mrEstimationRatio = teamMrEstimationRatioMap.get(sosCd4).get(jgiNo);
		estimationRatioList = mrEstimationRatio.getEstimationRatioList();
		for (EstimationRatio estimationRatio : estimationRatioList) {
			if (estimationRatio.getMikakutokuRatio() != null) {
				mikakutokuRatioTeam = convertPerRatio(estimationRatio.getMikakutokuRatio());
			}
			if (estimationRatio.getKakoJissekiRatio() != null) {
				deliveryRatioTeam = convertPerRatio(estimationRatio.getKakoJissekiRatio());
			}
			if (estimationRatio.getFree1Ratio() != null) {
				free1RatioTeam = convertPerRatio(estimationRatio.getFree1Ratio());
			}
			if (estimationRatio.getFree2Ratio() != null) {
				free2RatioTeam = convertPerRatio(estimationRatio.getFree2Ratio());
			}
			if (estimationRatio.getFree3Ratio() != null) {
				free3RatioTeam = convertPerRatio(estimationRatio.getFree3Ratio());
			}
			break; // 必ず「UH」・「P」・「UH+P」のどれかひとつとなる
		}

		return new EstimationResultDetailResultRowDto(null, null, null, null, null, null, null, null, null, null, null, sumOfficeRatioRate, sumTeamRatioRate, mikakutokuValue,
			mikakutokuRatioOffice, mikakutokuRatioTeam, deliveryValue, deliveryRatioOffice, deliveryRatioTeam, free1Value, free1RatioOffice, free1RatioTeam, free2Value,
			free2RatioOffice, free2RatioTeam, free3Value, free3RatioOffice, free3RatioTeam, null, null, null, null, null, null);
	}

	/**
	 * 比率の値を計算し取得する。
	 *
	 * <pre>
	 * 計算式
	 *  (割れられる値÷割る値)×100
	 *  (除算は小数点第三位以下を四捨五入とする)
	 * </pre>
	 *
	 * @param numerator 割れられる値
	 * @param denominator 割る値
	 * @param isNullForZero numerator == null || denominator == null || denominator == 0L を 0とする場合、true
	 * @return 構成(%)
	 */
	private Double calcRatio(final Long numerator, final Long denominator, final boolean isNullForZero) {
		return MathUtil.calcRatio(numerator, denominator, isNullForZero);
	}

	/**
	 * 比率をパーセントに変換する。
	 *
	 * <pre>
	 * DB値からは単純除算された値が送られるため四捨五入を行う。
	 * DB値=0.123456789... ⇒ 0.123(第三位で四捨五入) ⇒ 100を掛けて12.3(%)
	 * </pre>
	 *
	 * @param doubleValue 変換元比率値(DB値)
	 * @return 変換後の比率
	 */
	private Double convertPerRatio(final Double doubleValue) {
		return MathUtil.ratio(doubleValue);
	}

	/**
	 * 増分値を取得する。
	 *
	 * @param value1 引かれる値
	 * @param value2 引く値
	 * @return 増分値
	 */
	private Long calcSubtraction(Long value1, Long value2) {
		return MathUtil.planForUp(value1, value2);
	}

	/**
	 * チーム別構成比を保持するMAPを生成する。<br>
	 *
	 * @param mrEstimationRatioList 担当者別試算構成比を表すオブジェクトのリスト
	 * @return 担当者別試算構成比のオブジェクトMAP
	 */
	private Map<String, Map<Integer, MrEstimationRatio>> createMrEstimationRatioMap(List<MrEstimationRatio> mrEstimationRatioList) {
		if (officePlanDto == null || mrEstimationRatioList == null) {
			return null;
		}
		List<TeamPlanDto> teamPlanDtoList = officePlanDto.getTeamPlanDtoList();
		if (teamPlanDtoList == null) {
			return null;
		}
		Map<String, Map<Integer, MrEstimationRatio>> mrEstimationRatioMap = new HashMap<String, Map<Integer, MrEstimationRatio>>();
		for (TeamPlanDto teamPlanDto : teamPlanDtoList) {
			if (teamPlanDto == null) {
				return null;
			}
			List<MrPlanDto> mrPlanDtoList = teamPlanDto.getMrPlanDtoList();
			if (mrPlanDtoList == null) {
				return null;
			}
			final String sosCd4 = teamPlanDto.getSosCd();
			Map<Integer, MrEstimationRatio> jgiMap = new HashMap<Integer, MrEstimationRatio>();
			for (MrPlanDto mrPlanDto : mrPlanDtoList) {
				for (MrEstimationRatio mrEstimationRatio : mrEstimationRatioList) {
					if (mrPlanDto.getJgiNo().equals(mrEstimationRatio.getJgiNo())) {
						jgiMap.put(mrPlanDto.getJgiNo(), mrEstimationRatio);
					}
				}
			}
			mrEstimationRatioMap.put(sosCd4, jgiMap);
		}
		return mrEstimationRatioMap;
	}

	/**
	 * Nullチェック省略用の空インスタンスを生成する。
	 *
	 * @return 空のEstimationResultDetailResultRowDto
	 */
	private EstimationResultDetailResultRowDto createResultRowDtoEmpty() {
		return new EstimationResultDetailResultRowDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
}
