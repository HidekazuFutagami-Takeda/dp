package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.logic.div.TeamInputStatusForCheck.NOTHING;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.TeamInputStatusDao;
import jp.co.takeda.dto.TeamInputStatusCheckDto;
import jp.co.takeda.dto.TeamInputStatusCheckResultDto;
import jp.co.takeda.logic.div.TeamInputStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TeamInputStatus;
import jp.co.takeda.model.div.InputStateForMrPlan;

/**
 * チーム別入力状況をチェックするロジッククラス
 * 
 * @author nozaki
 */
public class TeamInputPlanStatusCheckLogic {

	/**
	 * チーム別入力状況DAO
	 */
	private final TeamInputStatusDao teamInputStatusDao;

	/**
	 * チェック対象のチームの組織情報
	 */
	private SosMst sosMst;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しないチーム別入力状況のリスト
	 */
	private final List<TeamInputStatusForCheck> teamInputStatusForCheckList;

	/**
	 * ステータスなしエラーフラグ
	 * <ul>
	 * <li>true：ステータスなしをエラーとする</li>
	 * <li>false：ステータスなしエラーとしない</li>
	 * <ul>
	 */
	private final boolean nothingStatusError;

	/**
	 * コンストラクタ
	 * 
	 * @param teamInputStatusDao チーム別入力状況DAO
	 * @param unallowableStatusDto チーム別入力状況チェック用DTO
	 */
	public TeamInputPlanStatusCheckLogic(TeamInputStatusDao teamInputStatusDao, TeamInputStatusCheckDto teamInputStatusCheckDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (teamInputStatusDao == null) {
			final String errMsg = "チーム別入力状況にアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (teamInputStatusCheckDto == null) {
			final String errMsg = "チーム別入力状況チェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (teamInputStatusCheckDto.getSosMst() == null) {
			final String errMsg = "チェック対象のチームの組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (teamInputStatusCheckDto.getSosMst().getSosCd() == null) {
			final String errMsg = "チェック対象のチームの組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (teamInputStatusCheckDto.getSosMst().getBumonSeiName() == null) {
			final String errMsg = "チェック対象のチームの部門名正式がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (teamInputStatusCheckDto.getPlannedProdList() == null || teamInputStatusCheckDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : teamInputStatusCheckDto.getPlannedProdList()) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		if (teamInputStatusCheckDto.getTeamInputStatusForCheck() == null || teamInputStatusCheckDto.getTeamInputStatusForCheck().size() == 0) {
			final String errMsg = "許可しないチーム別入力状況のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.teamInputStatusDao = teamInputStatusDao;
		this.sosMst = teamInputStatusCheckDto.getSosMst();
		this.plannedProdList = teamInputStatusCheckDto.getPlannedProdList();
		this.teamInputStatusForCheckList = teamInputStatusCheckDto.getTeamInputStatusForCheck();

		// 許可しないチーム別入力状況に「ステータスなし」がある場合
		if (teamInputStatusCheckDto.getTeamInputStatusForCheck().contains(NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}
	}

	/**
	 * ステータスチェック実行
	 */
	public TeamInputStatusCheckResultDto execute() {

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得したチーム別入力状況のリスト
		List<TeamInputStatus> teamInputStatusList = new ArrayList<TeamInputStatus>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 組織コード、品目固定コードを指定してステータスを取得
			TeamInputStatus resultTeamInputStatus = null;
			try {
				resultTeamInputStatus = teamInputStatusDao.search(sosMst.getSosCd(), plannedProd.getProdCode());
				teamInputStatusList.add(resultTeamInputStatus);

			} catch (DataNotFoundException e) {

				if (nothingStatusError) {

					// ステータスなしをエラーとする場合は、エラーメッセージ追加
					messageKeyList.add(new MessageKey("DPS3111E", sosMst.getBumonSeiName(), plannedProd.getProdName()));

				} else {

					// エラーにしない場合は、新規作成してリストに追加
					resultTeamInputStatus = new TeamInputStatus();
					resultTeamInputStatus.setSosCd(sosMst.getSosCd());
					resultTeamInputStatus.setProdCode(plannedProd.getProdCode());
					teamInputStatusList.add(resultTeamInputStatus);
				}
				continue;
			}

			// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
			InputStateForMrPlan resultStatus = resultTeamInputStatus.getInputStateForMrPlan();
			if (teamInputStatusForCheckList.contains(TeamInputStatusForCheck.getInstance(resultStatus))) {

				switch (resultStatus) {
					case MR_PLAN_INPUTTING:
						messageKeyList.add(new MessageKey("DPS3112E", sosMst.getBumonSeiName(), plannedProd.getProdName()));
						break;
					case MR_PLAN_INPUT_FINISHED:
						messageKeyList.add(new MessageKey("DPS3113E", sosMst.getBumonSeiName(), plannedProd.getProdName()));
						break;
				}
			}
		}

		if (messageKeyList.size() > 0) {
			return new TeamInputStatusCheckResultDto(messageKeyList, teamInputStatusList);

		} else {
			return new TeamInputStatusCheckResultDto(teamInputStatusList);
		}
	}
}
