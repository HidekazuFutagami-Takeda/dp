package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.TeamInputStatusDao;
import jp.co.takeda.dto.PlannedProdResultDto;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.TeamInputStatus;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.InputStateForMrPlan;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 計画対象品目の検索結果用DTOを生成するロジッククラス
 *
 * @author stakeuchi
 */
public class PlannedProdResultDtoCreateLogic {

	/**
	 * 計画対象品目リスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 担当者別計画ステータスリスト
	 */
	private final List<MrPlanStatus> mrPlanStatusList;

	/**
	 * 営業所計画リスト
	 */
	private final List<OfficePlan> officePlanList;

	/**
	 * チーム計画リスト
	 */
	private final List<TeamPlan> teamPlanList;

	/**
	 * 担当者計画リスト
	 */
	private final List<MrPlan> mrPlanList;

	/**
	 * チーム組織コード
	 */
	private final String sosCd4;

	/**
	 * チーム別入力状況DAO
	 */
	private final TeamInputStatusDao teamInputStatusDao;

	/**
	 * 試算タイプ
	 */
	private final CalcType calcType;

	/**
	 * コンストラクタ
	 *
	 * @param plannedProdList 計画対象品目リスト
	 * @param mrPlanStatusList 担当者別計画ステータスリスト
	 * @param officePlanList 営業所計画リスト
	 * @param teamPlanList チーム計画リスト
	 * @param mrPlanList 担当者計画リスト
	 * @param sosCd4 チーム組織コード（検索対象としない場合はNULLを指定）
	 * @param teamInputStatusDao チーム別入力状況DAO（検索対象としない場合はNULLを指定）
	 */
	public PlannedProdResultDtoCreateLogic(List<PlannedProd> plannedProdList, List<MrPlanStatus> mrPlanStatusList, List<OfficePlan> officePlanList, List<TeamPlan> teamPlanList,
		List<MrPlan> mrPlanList, String sosCd4, TeamInputStatusDao teamInputStatusDao,CalcType calcType ) {
		this.plannedProdList = plannedProdList;
		this.mrPlanStatusList = mrPlanStatusList;
		this.officePlanList = officePlanList;
		this.teamPlanList = teamPlanList;
		this.mrPlanList = mrPlanList;
		this.sosCd4 = sosCd4;
		this.teamInputStatusDao = teamInputStatusDao;
		this.calcType = calcType;
	}

	/**
	 * 検索結果用DTOリストの生成<br>
	 * DAO検索結果の各リストから検索結果用DTOを生成する。
	 */
	public List<PlannedProdResultDto> createResultDtoList() {

		List<PlannedProdResultDto> resultDtoList = new ArrayList<PlannedProdResultDto>();

		if (plannedProdList != null) {

			for (PlannedProd plannedProd : plannedProdList) {

				final String prodCode = plannedProd.getProdCode();

				final String prodName = plannedProd.getProdName();

				// ステータスを挿入したDTOの取得
				PlannedProdResultDto mrPlanStatusDto = getMrPlanStatusDto(prodCode);

				// 営業所計画を挿入したDTOの取得
				PlannedProdResultDto officePlanDto = getOfficePlanDto(prodCode);

				// チーム別計画を挿入したDTOの取得
				PlannedProdResultDto teamPlanDto = getTeamPlanDto(prodCode);

				// 担当者別計画を挿入したDTOの取得
				PlannedProdResultDto mrPlanDto = getMrPlanDto(prodCode);

				// 上記で分割セットしたDTOを結合してリストに追加
				resultDtoList.add(createResultDto(prodCode, prodName, mrPlanStatusDto, officePlanDto, teamPlanDto, mrPlanDto, calcType));
			}

		}

		return resultDtoList;
	}

	/**
	 * 引数の品目コードに紐付く担当者別計画ステータスのみを格納した検索結果用DTOを取得する。
	 *
	 * @param prodCode 品目コード
	 * @return 品目コードに紐付く担当者別計画ステータスを格納した検索結果用DTO
	 */
	private PlannedProdResultDto getMrPlanStatusDto(String prodCode) {
		if (mrPlanStatusList != null) {
			for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {
				if (StringUtils.equals(mrPlanStatus.getProdCode(), prodCode)) {

					// シーケンスキー
					final Long seqKey = mrPlanStatus.getSeqKey();

					// 最終更新日時
					final Date upDate = mrPlanStatus.getUpDate();

					// 「試算中」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusEstimating = false;

					// 「試算済」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusEstimated = false;

					// 「チーム別計画公開」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusTeamPlanOpened = false;

					// 「チーム別計画確定」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusTeamPlanCompleted = false;

					// 「担当者別計画公開」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusMrPlanOpened = false;

					// 「担当者別計画入力中(AL修正)」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusMrPlanInputting = false;

					// 「担当者別計画入力完了(AL修正)」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusMrPlanInputFinished = false;

					// 「担当者別計画確定(所長確定)」フラグ TRUE=ON, FALSE=OFF
					boolean isStatusMrPlanCompleted = false;

					switch (mrPlanStatus.getStatusForMrPlan()) {
						case ESTIMATING:
							isStatusEstimating = true;
							break;

						case ESTIMATED:
							isStatusEstimated = true;
							break;

						case TEAM_PLAN_OPENED:
							isStatusTeamPlanOpened = true;
							break;

						case TEAM_PLAN_COMPLETED:
							isStatusTeamPlanCompleted = true;
							break;

						case MR_PLAN_OPENED:
							isStatusMrPlanOpened = true;
							break;

						case MR_PLAN_INPUT_FINISHED:
							isStatusMrPlanInputFinished = true;
							break;

						case MR_PLAN_COMPLETED:
							isStatusMrPlanCompleted = true;
							break;

						default:
							break;
					}

					// 試算開始日時
					final Date estStartDate = mrPlanStatus.getEstStartDate();

					// 試算終了日時
					final Date estEndDate = mrPlanStatus.getEstEndDate();

					// チーム別計画公開日時
					final Date teamPlanOpenDate = mrPlanStatus.getTeamPlanOpenDate();

					// チーム別計画確定日時
					final Date teamPlanFixDate = mrPlanStatus.getTeamPlanFixDate();

					// 担当者別計画公開日時
					final Date mrPlanOpenDate = mrPlanStatus.getMrPlanOpenDate();

					// 担当者別計画入力日時
					Date mrPlanInputDate = mrPlanStatus.getMrPlanInputDate();

					// 担当者別計画確定日時
					final Date mrPlanFixDate = mrPlanStatus.getMrPlanFixDate();

					DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
					DpUser dpUser = dpUserInfo.getSettingUser();

					// チーム別入力状況を検索
					if (sosCd4 != null) {
						try {
							TeamInputStatus teamInputStatus = teamInputStatusDao.search(sosCd4, prodCode);
							switch (teamInputStatus.getInputStateForMrPlan()) {
								case MR_PLAN_INPUT_FINISHED:
									// 担当者別計画公開中の場合、
									// 担当者別計画入力完了(AL修正)フラグを書き換える
									if (isStatusMrPlanOpened) {
										isStatusMrPlanOpened = false;
										isStatusMrPlanInputFinished = true;
									}
									// 担当者別計画入力完了(AL修正)または担当者別計画確定中の場合、
									// 担当者別計画入力完了日時を書き換える
									if (isStatusMrPlanInputFinished || isStatusMrPlanCompleted) {
										mrPlanInputDate = teamInputStatus.getMrPlanInputDate();
									}
									break;
								default:
									isStatusMrPlanInputFinished = false;
									mrPlanInputDate = null;
									break;
							}
						} catch (DataNotFoundException e) {
							isStatusMrPlanInputFinished = false;
							mrPlanInputDate = null;
						}
					}

					// ----------------------------------------
					// 担当者別計画のＡＬ立案状況を取得
					// ----------------------------------------

					// [利用者が営業所以上]
					else if (dpUser != null && !dpUser.isMatch(JokenSet.IYAKU_AL, JokenSet.IYAKU_MR)) {

						// [担当者別計画が公開中である]
						if (isStatusMrPlanOpened) {

							Date lastDate = null;
							List<String> teamSosCdList = new ArrayList<String>();
							for (TeamPlan teamPlan : teamPlanList) {
								if (teamPlan.getProdCode().equals(prodCode)) {
									if (!teamSosCdList.contains(teamPlan.getSosCd())) {
										teamSosCdList.add(teamPlan.getSosCd());
									}
								}
							}
							// teamSosCdListにチームの組織コードが入っているので、チーム分、担当者別計画のＡＬ立案状況を検索する
							for (String sosCd : teamSosCdList) {
								try {
									TeamInputStatus teamInputStatus = teamInputStatusDao.search(sosCd, prodCode);
									InputStateForMrPlan state = teamInputStatus.getInputStateForMrPlan();
									if (InputStateForMrPlan.MR_PLAN_INPUT_FINISHED == state) {
										isStatusMrPlanInputting = true;

										// 一番新しい日付を取得してlastDateにいれる処理
										if (teamInputStatus.getMrPlanInputDate() != null) {
											if (lastDate == null) {
												lastDate = teamInputStatus.getMrPlanInputDate();
											} else if (teamInputStatus.getMrPlanInputDate().after(lastDate)) {
												lastDate = teamInputStatus.getMrPlanInputDate();
											}
										}
									}
								} catch (DataNotFoundException e) {
									// 担当者別計画のＡＬ立案状況が無い場合も論理的にありえるので、エラーではない
								}
							}
							if (isStatusMrPlanInputting && lastDate != null) {
								mrPlanInputDate = lastDate;
							}
						}
					}

					return new PlannedProdResultDto(null, null, seqKey, upDate, isStatusEstimating, isStatusEstimated, isStatusTeamPlanOpened, isStatusTeamPlanCompleted,
						isStatusMrPlanOpened, isStatusMrPlanInputting, isStatusMrPlanInputFinished, isStatusMrPlanCompleted, estStartDate, estEndDate, teamPlanOpenDate,
						teamPlanFixDate, mrPlanOpenDate, mrPlanInputDate, mrPlanFixDate, null, null, null, null, null, null);
				}
			}
		}
		return null;
	}

	/**
	 * 引数の品目コードに紐付く営業所計画のみを格納した検索結果用DTOを取得する。
	 *
	 * @param prodCode 品目コード
	 * @return 品目コードに紐付く営業所計画を格納した検索結果用DTO
	 */
	private PlannedProdResultDto getOfficePlanDto(String prodCode) {
		if (officePlanList != null) {
			for (OfficePlan officePlan : officePlanList) {
				if (StringUtils.equals(officePlan.getProdCode(), prodCode)) {

					// UH 営業所計画金額
					final Long uhOfficePlanAmount = officePlan.getPlannedValueUhY();

					// P  営業所計画金額
					final Long pOfficePlanAmount = officePlan.getPlannedValuePY();

					return new PlannedProdResultDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
						uhOfficePlanAmount, null, null, pOfficePlanAmount, null, null);
				}
			}
		}
		return null;
	}

	/**
	 * 引数の品目コードに紐付くチーム計画のみを格納した検索結果用DTOを取得する。
	 *
	 * @param prodCode 品目コード
	 * @return 品目コードに紐付くチーム計画を格納した検索結果用DTO
	 */
	private PlannedProdResultDto getTeamPlanDto(String prodCode) {
		if (teamPlanList != null) {
			boolean teamFlg = false;
			// UH チーム別計画金額
			Long uhTeamPlanAmount = null;
			// P  チーム別計画金額
			Long pTeamPlanAmount = null;
			for (TeamPlan teamPlan : teamPlanList) {
				if (StringUtils.equals(teamPlan.getProdCode(), prodCode)) {
					teamFlg = true;
					// UH チーム別計画金額
					uhTeamPlanAmount = MathUtil.add(uhTeamPlanAmount, teamPlan.getPlannedValueUhY());
					// P  チーム別計画金額
					pTeamPlanAmount = MathUtil.add(pTeamPlanAmount, teamPlan.getPlannedValuePY());
				}
			}
			if (teamFlg) {
				return new PlannedProdResultDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					uhTeamPlanAmount, null, null, pTeamPlanAmount, null);
			}
		}
		return null;
	}

	/**
	 * 引数の品目コードに紐付く担当者計画のみを格納した検索結果用DTOを取得する。
	 *
	 * @param prodCode 品目コード
	 * @return 品目コードに紐付く担当者計画を格納した検索結果用DTO
	 */
	private PlannedProdResultDto getMrPlanDto(String prodCode) {
		if (mrPlanList != null) {

			// UH 担当者別計画金額
			Long uhMrPlanAmount = null;

			// P  担当者別計画金額
			Long pMrPlanAmount = null;

			for (MrPlan mrPlan : mrPlanList) {
				if (StringUtils.equals(mrPlan.getProdCode(), prodCode)) {
					if (mrPlan.getPlannedValueUhY() != null) {
						if (uhMrPlanAmount == null) {
							uhMrPlanAmount = 0L;
						}
						uhMrPlanAmount += mrPlan.getPlannedValueUhY();
					}
					if (mrPlan.getPlannedValuePY() != null) {
						if (pMrPlanAmount == null) {
							pMrPlanAmount = 0L;
						}
						pMrPlanAmount += mrPlan.getPlannedValuePY();
					}
				}
			}

			return new PlannedProdResultDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				uhMrPlanAmount, null, null, pMrPlanAmount);
		}
		return null;
	}

	/**
	 * 各データを格納したDTOを全て結合した完全な検索結果用DTOを生成する。
	 *
	 * @param prodCode 格納する品目コード
	 * @param prodName 格納する品目名
	 * @param mrPlanStatusDto 担当者別計画ステータスを格納した検索結果用DTO
	 * @param officePlanDto 営業所計画を格納した検索結果用DTO
	 * @param teamPlanDto チーム計画を格納した検索結果用DTO
	 * @param mrPlanDto 担当者計画を格納した検索結果用DTO
	 * @return 完全な検索結果用DTO
	 */
	private PlannedProdResultDto createResultDto(String prodCode, String prodName, PlannedProdResultDto mrPlanStatusDto, PlannedProdResultDto officePlanDto,
		PlannedProdResultDto teamPlanDto, PlannedProdResultDto mrPlanDto, CalcType calcType) {

		// Nullチェック省略のためNullの場合は新規インスタンスを生成
		if (mrPlanStatusDto == null) {
			mrPlanStatusDto = new PlannedProdResultDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
		}
		if (officePlanDto == null) {
			officePlanDto = new PlannedProdResultDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		}
		if (teamPlanDto == null) {
			teamPlanDto = new PlannedProdResultDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		}
		if (mrPlanDto == null) {
			mrPlanDto = new PlannedProdResultDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null);
		}

		// 担当者別計画ステータス シーケンスキー
		final Long seqKey = mrPlanStatusDto.getStatusSeqKey();

		// 担当者別計画ステータス 最終更新日時
		final Date upDate = mrPlanStatusDto.getStatusUpDate();

		// 「試算中」フラグ
		final Boolean isStatusEstimating = mrPlanStatusDto.getIsStatusEstimating();

		// 「試算済」フラグ
		final Boolean isStatusEstimated = mrPlanStatusDto.getIsStatusEstimated();

		// 「チーム別計画公開」フラグ
		final Boolean isStatusTeamPlanOpened = mrPlanStatusDto.getIsStatusTeamPlanOpened();

		// 「チーム別計画確定」フラグ
		final Boolean isStatusTeamPlanCompleted = mrPlanStatusDto.getIsStatusTeamPlanCompleted();

		// 「担当者別計画公開」フラグ
		final Boolean isStatusMrPlanOpened = mrPlanStatusDto.getIsStatusMrPlanOpened();

		// 「担当者別計画入力中(AL修正)」フラグ TRUE=ON, FALSE=OFF
		final Boolean isStatusMrPlanInputting = mrPlanStatusDto.getIsStatusMrPlanInputting();

		// 「担当者別計画入力完了(AL修正)」フラグ
		final Boolean isStatusMrPlanInputFinished = mrPlanStatusDto.getIsStatusMrPlanInputFinished();

		// 「担当者別計画確定(所長確定)」フラグ
		final Boolean isStatusMrPlanCompleted = mrPlanStatusDto.getIsStatusMrPlanCompleted();

		// 試算開始日時
		final Date estStartDate = mrPlanStatusDto.getEstStartDate();

		// 試算終了日時
		final Date estEndDate = mrPlanStatusDto.getEstEndDate();

		// チーム別計画公開日時
		final Date teamPlanOpenDate = mrPlanStatusDto.getTeamPlanOpenDate();

		// チーム別計画確定日時
		final Date teamPlanFixDate = mrPlanStatusDto.getTeamPlanFixDate();

		// 担当者別計画公開日時
		final Date mrPlanOpenDate = mrPlanStatusDto.getMrPlanOpenDate();

		// 担当者別計画入力日時
		Date mrPlanInputDate = mrPlanStatusDto.getMrPlanInputDate();

		// 担当者別計画確定日時
		final Date mrPlanFixDate = mrPlanStatusDto.getMrPlanFixDate();

		// UH 営業所計画金額
		final Long uhOfficePlanAmount = ConvertUtil.parseMoneyToThousandUnit(officePlanDto.getUhOfficePlanAmount());

		// P  営業所計画金額
		final Long pOfficePlanAmount = ConvertUtil.parseMoneyToThousandUnit(officePlanDto.getPOfficePlanAmount());

		// UH チーム別計画金額
		Long uhTeamPlanAmount = ConvertUtil.parseMoneyToThousandUnit(teamPlanDto.getUhTeamPlanAmount());

		// P  チーム別計画金額
		Long pTeamPlanAmount = ConvertUtil.parseMoneyToThousandUnit(teamPlanDto.getPTeamPlanAmount());

		// UH 担当者別計画金額
		Long uhMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(mrPlanDto.getUhMrPlanAmount());

		// P  担当者別計画金額
		Long pMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(mrPlanDto.getPMrPlanAmount());

		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		if (dpUser != null && mrPlanStatusDto.getStatusUpDate() != null) {

			// 利用者がALの場合
			if(dpUser.isMatch(JokenSet.IYAKU_AL)){

				// チーム別計画公開前の場合はチーム別金額にNullをセット
				final boolean isBeforeTeamOpen = !isStatusTeamPlanOpened && !isStatusTeamPlanCompleted && !isStatusMrPlanOpened && !isStatusMrPlanInputFinished && !isStatusMrPlanCompleted;
				if (isBeforeTeamOpen) {
					uhTeamPlanAmount = null;
					pTeamPlanAmount = null;
				}

				// 担当者別計画公開前の場合は担当者別金額にNullをセット
				final boolean isBeforeMrOpen = !isStatusMrPlanOpened && !isStatusMrPlanInputFinished && !isStatusMrPlanCompleted;
				if (isBeforeMrOpen) {
					uhMrPlanAmount = null;
					pMrPlanAmount = null;
				}

				// 試算タイプが（営→担）かつ担当者別計画公開前の場合はチーム別金額にNullをセット
				if(calcType != null && calcType == CalcType.OFFICE_MR  && isBeforeMrOpen){
					uhTeamPlanAmount = null;
					pTeamPlanAmount = null;
				}
			}

			// 利用者がMRかつ担当者別計画確定前の場合は担当者別金額にNullをセット
			if (dpUser.isMatch(JokenSet.IYAKU_MR) && !isStatusMrPlanCompleted) {
				uhMrPlanAmount = null;
				pMrPlanAmount = null;
			}
		}

		return new PlannedProdResultDto(prodCode, prodName, seqKey, upDate, isStatusEstimating, isStatusEstimated, isStatusTeamPlanOpened, isStatusTeamPlanCompleted,
			isStatusMrPlanOpened, isStatusMrPlanInputting, isStatusMrPlanInputFinished, isStatusMrPlanCompleted, estStartDate, estEndDate, teamPlanOpenDate, teamPlanFixDate,
			mrPlanOpenDate, mrPlanInputDate, mrPlanFixDate, uhOfficePlanAmount, uhTeamPlanAmount, uhMrPlanAmount, pOfficePlanAmount, pTeamPlanAmount, pMrPlanAmount);
	}
}
