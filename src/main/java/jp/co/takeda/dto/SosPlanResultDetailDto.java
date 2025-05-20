package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.ManageBranchPlan;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.model.ManageOfficePlan;
import jp.co.takeda.model.ManageTeamPlan;
import jp.co.takeda.util.ConvertUtil;

/**
 * 組織別計画の検索結果 明細行を表すDTO
 *
 * @author stakeuchi
 */
public class SosPlanResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 組織名
	 */
	private final String sosName;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * [UH] Y価ベース
	 */
	private final Long yBaseValueUH;

	/**
	 * [UH] T価ベース
	 */
	private final Long tBaseValueUH;

	/**
	 * [UH] Y価ベース下位計画レベル合計値(調整金額算出用)
	 */
	private final Long yBaseValueUHLowerLevelPlanSummary;

	/**
	 * [UH] T/Y変換率
	 */
	private final Double tyChangeRateUH;

	/**
	 * [UH] シーケンスキー
	 */
	private final Long seqKeyUH;

	/**
	 * [UH] 最終更新日時
	 */
	private final Date upDateUH;

	/**
	 * [UH] 最終更新者
	 */
	private final String upJgiNameUH;

	/**
	 * [P] Y価ベース
	 */
	private final Long yBaseValueP;

	/**
	 * [P] T価ベース
	 */
	private final Long tBaseValueP;

	/**
	 * [P] Y価ベース下位計画レベル合計値(調整金額算出用)
	 */
	private final Long yBaseValuePLowerLevelPlanSummary;

	/**
	 * [P] T/Y変換率
	 */
	private final Double tyChangeRateP;

	/**
	 * [P] シーケンスキー
	 */
	private final Long seqKeyP;

	/**
	 * [P] 最終更新日時
	 */
	private final Date upDateP;

	/**
	 * [P] 最終更新者
	 */
	private final String upJgiNameP;

	/**
	 * [Z] Y価ベース
	 */
	private final Long yBaseValueZ;

	/**
	 * [Z] T価ベース
	 */
	private final Long tBaseValueZ;

	/**
	 * [Z] Y価ベース下位計画レベル合計値(調整金額算出用)
	 */
	private final Long yBaseValueZLowerLevelPlanSummary;

	/**
	 * [Z] T/Y変換率
	 */
	private final Double tyChangeRateZ;

	/**
	 * [Z] シーケンスキー
	 */
	private final Long seqKeyZ;

	/**
	 * [Z] 最終更新日時
	 */
	private final Date upDateZ;

	/**
	 * [Z] 最終更新者
	 */
	private final String upJgiNameZ;

	/**
	 * プランレベル移行可否フラグ
	 * <ul>
	 * <li>TRUE = 別計画レベルへ移行可能</li>
	 * <li>FALSE = 別計画レベルへ移行不可</li>
	 * </ul>
	 */
	private final boolean canMovePlanLevel;

	/**
	 * 雑組織フラグ(true：雑組織、false：雑組織でない)
	 */
	private final Boolean etcSosFlg;

	/**
	 * コンストラクタ(支店別計画)
	 *
	 * @param branchPlanUh [UH] 支店別計画
	 * @param branchPlanP [P] 支店別計画
	 * @param branchPlanZ [Z] 支店別計画
	 * @param tyChangeRate T/Y変換率
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 */
	public SosPlanResultDetailDto(ManageBranchPlan branchPlanUh, ManageBranchPlan branchPlanP, ManageBranchPlan branchPlanZ, ChangeParamYT changeParamYT, boolean canMovePlanLevel) {
		this.sosCd = branchPlanUh.getSosCd();
		this.sosName = branchPlanUh.getBumonSeiName();
		this.jgiNo = null;
		this.jgiName = null;
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(branchPlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(branchPlanUh.getImplPlan().getPlanned2ValueT());
		this.yBaseValueUHLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(branchPlanUh.getStackedValue());
		this.tyChangeRateUH = changeParamYT.getChangeRateUh();
		this.seqKeyUH = branchPlanUh.getSeqKey();
		this.upDateUH = branchPlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = branchPlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(branchPlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(branchPlanP.getImplPlan().getPlanned2ValueT());
		this.yBaseValuePLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(branchPlanP.getStackedValue());
		this.tyChangeRateP = changeParamYT.getChangeRateP();
		this.seqKeyP = branchPlanP.getSeqKey();
		this.upDateP = branchPlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = branchPlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(branchPlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(branchPlanZ.getImplPlan().getPlanned2ValueT());
		this.yBaseValueZLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(branchPlanZ.getStackedValue());
		this.tyChangeRateZ = changeParamYT.getChangeRateZ();
		this.seqKeyZ = branchPlanZ.getSeqKey();
		this.upDateZ = branchPlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ = branchPlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}

		this.canMovePlanLevel = canMovePlanLevel;
		this.etcSosFlg = Boolean.FALSE;
	}

	/**
	 * コンストラクタ(営業所別計画)
	 *
	 * @param officePlanUh [UH] 営業所別計画
	 * @param officePlanP [P] 営業所別計画
	 * @param officePlanZ [Z] 営業所別計画
	 * @param tyChangeRate T/Y変換率
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 */
	public SosPlanResultDetailDto(ManageOfficePlan officePlanUh, ManageOfficePlan officePlanP, ManageOfficePlan officePlanZ, ChangeParamYT changeParamYT, boolean canMovePlanLevel) {
		this.sosCd = officePlanUh.getSosCd();
		this.sosName = officePlanUh.getBumonSeiName();
		this.jgiNo = null;
		this.jgiName = null;
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(officePlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(officePlanUh.getImplPlan().getPlanned2ValueT());
		this.yBaseValueUHLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(officePlanUh.getStackedValue());
		this.tyChangeRateUH = changeParamYT.getChangeRateUh();
		this.seqKeyUH = officePlanUh.getSeqKey();
		this.upDateUH = officePlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = officePlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(officePlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(officePlanP.getImplPlan().getPlanned2ValueT());
		this.yBaseValuePLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(officePlanP.getStackedValue());
		this.tyChangeRateP = changeParamYT.getChangeRateP();
		this.seqKeyP = officePlanP.getSeqKey();
		this.upDateP = officePlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = officePlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		// Z追加
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(officePlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(officePlanZ.getImplPlan().getPlanned2ValueT());
		this.yBaseValueZLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(officePlanZ.getStackedValue());
		this.tyChangeRateZ = changeParamYT.getChangeRateZ();
		this.seqKeyZ = officePlanZ.getSeqKey();
		this.upDateZ = officePlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ= officePlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}
		this.canMovePlanLevel = canMovePlanLevel;
		this.etcSosFlg = Boolean.FALSE;
	}

	/**
	 * コンストラクタ(チーム別計画)
	 *
	 * @param teamPlanUh [UH] チーム別計画
	 * @param teamPlanP [P] チーム別計画
	 * @param teamPlanZ [Z] チーム別計画
	 * @param tyChangeRate T/Y変換率
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 */
	public SosPlanResultDetailDto(ManageTeamPlan teamPlanUh, ManageTeamPlan teamPlanP, ManageTeamPlan teamPlanZ, ChangeParamYT changeParamYT, boolean canMovePlanLevel) {
		this.sosCd = teamPlanUh.getSosCd();
		this.sosName = teamPlanUh.getBumonSeiName();
		this.jgiNo = null;
		this.jgiName = null;
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(teamPlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(teamPlanUh.getImplPlan().getPlanned2ValueT());
		this.yBaseValueUHLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(teamPlanUh.getStackedValue());
		this.tyChangeRateUH = changeParamYT.getChangeRateUh();
		this.seqKeyUH = teamPlanUh.getSeqKey();
		this.upDateUH = teamPlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = teamPlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(teamPlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(teamPlanP.getImplPlan().getPlanned2ValueT());
		this.yBaseValuePLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(teamPlanP.getStackedValue());
		this.tyChangeRateP = changeParamYT.getChangeRateP();
		this.seqKeyP = teamPlanP.getSeqKey();
		this.upDateP = teamPlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = teamPlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(teamPlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(teamPlanZ.getImplPlan().getPlanned2ValueT());
		this.yBaseValueZLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(teamPlanZ.getStackedValue());
		this.tyChangeRateZ = changeParamYT.getChangeRateZ();
		this.seqKeyZ = teamPlanZ.getSeqKey();
		this.upDateZ = teamPlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ = teamPlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}
		this.canMovePlanLevel = canMovePlanLevel;
		this.etcSosFlg = teamPlanUh.getEtcSosFlg();
	}

	/**
	 * コンストラクタ(担当者別計画)
	 *
	 * @param mrPlanUh [UH] 担当者別計画
	 * @param mrPlanP [P] 担当者別計画
	 * @param mrPlanZ [Z] 担当者別計画
	 * @param tyChangeRate T/Y変換率
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 * @param etcSosFlg 雑組織フラグ
	 */
	public SosPlanResultDetailDto(ManageMrPlan mrPlanUh, ManageMrPlan mrPlanP, ManageMrPlan mrPlanZ, ChangeParamYT changeParamYT, boolean canMovePlanLevel, boolean etcSosFlg) {
		this.sosCd = null;
		this.sosName = null;
		this.jgiNo = mrPlanUh.getJgiNo();
		this.jgiName = mrPlanUh.getJgiName();
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(mrPlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(mrPlanUh.getImplPlan().getPlanned2ValueT());
		this.yBaseValueUHLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(mrPlanUh.getStackedValue());
		this.tyChangeRateUH = changeParamYT.getChangeRateUh();
		this.seqKeyUH = mrPlanUh.getSeqKey();
		this.upDateUH = mrPlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = mrPlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(mrPlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(mrPlanP.getImplPlan().getPlanned2ValueT());
		this.yBaseValuePLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(mrPlanP.getStackedValue());
		this.tyChangeRateP = changeParamYT.getChangeRateP();
		this.seqKeyP = mrPlanP.getSeqKey();
		this.upDateP = mrPlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = mrPlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(mrPlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(mrPlanZ.getImplPlan().getPlanned2ValueT());
		this.yBaseValueZLowerLevelPlanSummary = ConvertUtil.parseMoneyToThousandUnit(mrPlanZ.getStackedValue());
		this.tyChangeRateZ = changeParamYT.getChangeRateZ();
		this.seqKeyZ = mrPlanZ.getSeqKey();
		this.upDateZ = mrPlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ = mrPlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}
		this.canMovePlanLevel = canMovePlanLevel;
		this.etcSosFlg = etcSosFlg;
	}

	/**
	 * 組織コードを取得する。
	 *
	 * @return sosCd 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織名を取得する。
	 *
	 * @return sosName 組織名
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * [UH] Y価ベースを取得する。
	 *
	 * @return yBaseValueUH [UH] Y価ベース
	 */
	public Long getYBaseValueUH() {
		return yBaseValueUH;
	}

	/**
	 * [UH] T価ベースを取得する。
	 *
	 * @return tBaseValueUH [UH] T価ベース
	 */
	public Long getTBaseValueUH() {
		return tBaseValueUH;
	}

	/**
	 * [UH] Y価ベース下位計画レベル合計値(調整金額算出用)を取得する。
	 *
	 * @return yBaseValueUHLowerLevelPlanSummary [UH] Y価ベース下位計画レベル合計値(調整金額算出用)
	 */
	public Long getYBaseValueUHLowerLevelPlanSummary() {
		return yBaseValueUHLowerLevelPlanSummary;
	}

	/**
	 * [UH] T/Y変換率を取得する。
	 *
	 * @return tyChangeRateUH [UH] T/Y変換率
	 */
	public Double getTyChangeRateUH() {
		return tyChangeRateUH;
	}

	/**
	 * [UH] シーケンスキーを取得する。
	 *
	 * @return seqKeyUH [UH] シーケンスキー
	 */
	public Long getSeqKeyUH() {
		return seqKeyUH;
	}

	/**
	 * [UH] 最終更新日時を取得する。
	 *
	 * @return upDateUH [UH] 最終更新日時
	 */
	public Date getUpDateUH() {
		return upDateUH;
	}

	/**
	 * [UH] 最終更新者を取得する。
	 *
	 * @return upJgiNameUH [UH] 最終更新者
	 */
	public String getUpJgiNameUH() {
		return upJgiNameUH;
	}

	/**
	 * [P] Y価ベースを取得する。
	 *
	 * @return yBaseValueP [P] Y価ベース
	 */
	public Long getYBaseValueP() {
		return yBaseValueP;
	}

	/**
	 * [P] T価ベースを取得する。
	 *
	 * @return tBaseValueP [P] T価ベース
	 */
	public Long getTBaseValueP() {
		return tBaseValueP;
	}

	/**
	 * [P] Y価ベース下位計画レベル合計値(調整金額算出用)を取得する。
	 *
	 * @return yBaseValuePLowerLevelPlanSummary [P] Y価ベース下位計画レベル合計値(調整金額算出用)
	 */
	public Long getYBaseValuePLowerLevelPlanSummary() {
		return yBaseValuePLowerLevelPlanSummary;
	}

	/**
	 * [P] T/Y変換率を取得する。
	 *
	 * @return tyChangeRateP [P] T/Y変換率
	 */
	public Double getTyChangeRateP() {
		return tyChangeRateP;
	}

	/**
	 * [P] シーケンスキーを取得する。
	 *
	 * @return seqKeyP [P] シーケンスキー
	 */
	public Long getSeqKeyP() {
		return seqKeyP;
	}

	/**
	 * [P] 最終更新日時を取得する。
	 *
	 * @return upDateP [P] 最終更新日時
	 */
	public Date getUpDateP() {
		return upDateP;
	}

	/**
	 * [P] 最終更新者を取得する。
	 *
	 * @return upJgiNameP [P] 最終更新者
	 */
	public String getUpJgiNameP() {
		return upJgiNameP;
	}

	/**
	 * [Z] Y価ベースを取得する。
	 *
	 * @return yBaseValueZ [Z] Y価ベース
	 */
	public Long getYBaseValueZ() {
		return yBaseValueZ;
	}

	/**
	 * [Z] T価ベースを取得する。
	 *
	 * @return tBaseValueZ [Z] T価ベース
	 */
	public Long getTBaseValueZ() {
		return tBaseValueZ;
	}

	/**
	 * [Z] Y価ベース下位計画レベル合計値(調整金額算出用)を取得する。
	 *
	 * @return yBaseValueZLowerLevelPlanSummary [Z] Y価ベース下位計画レベル合計値(調整金額算出用)
	 */
	public Long getYBaseValueZLowerLevelPlanSummary() {
		return yBaseValueZLowerLevelPlanSummary;
	}

	/**
	 * [Z] T/Y変換率を取得する。
	 *
	 * @return tyChangeRateZ [Z] T/Y変換率
	 */
	public Double getTyChangeRateZ() {
		return tyChangeRateZ;
	}

	/**
	 * [Z] シーケンスキーを取得する。
	 *
	 * @return seqKeyZ [Z] シーケンスキー
	 */
	public Long getSeqKeyZ() {
		return seqKeyZ;
	}

	/**
	 * [Z] 最終更新日時を取得する。
	 *
	 * @return upDateZ [Z] 最終更新日時
	 */
	public Date getUpDateZ() {
		return upDateZ;
	}

	/**
	 * [Z] 最終更新日時を取得する。
	 *
	 * @return upJgiNameZ [Z] 最終更新日時
	 */
	public String getUpJgiNameZ() {
		return upJgiNameZ;
	}

	/**
	 * プランレベル移行可否フラグを取得する。
	 *
	 * @return canMovePlanLevel プランレベル移行可否フラグ
	 */
	public boolean isCanMovePlanLevel() {
		return canMovePlanLevel;
	}

	/**
	 * 雑組織フラグを取得する。
	 *
	 * @return 雑組織フラグ
	 */
	public Boolean getEtcSosFlg() {
		return etcSosFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
