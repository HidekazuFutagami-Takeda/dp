package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageBranchPlan;
import jp.co.takeda.model.ManageIyakuPlan;
import jp.co.takeda.model.ManageOfficePlan;
import jp.co.takeda.model.ManageTeamPlan;
import jp.co.takeda.util.ConvertUtil;

/**
 * 組織別計画の検索結果 明細合計行を表すDTO
 *
 * @author nozaki
 */
public class SosPlanResultDetailTotalDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * [UH] Y価ベース
	 */
	private final Long yBaseValueUH;

	/**
	 * [UH] T価ベース
	 */
	private final Long tBaseValueUH;

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
	 * コンストラクタ(全社計画)<br>
	 *
	 * @param iyakuPlanUh 全社計画UH
	 * @param iyakuPlanP 全社計画P
	 * @param iyakuPlanZ 全社計画Z
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 */
	public SosPlanResultDetailTotalDto(ManageIyakuPlan iyakuPlanUh, ManageIyakuPlan iyakuPlanP, ManageIyakuPlan iyakuPlanZ, boolean canMovePlanLevel) {
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(iyakuPlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(iyakuPlanUh.getImplPlan().getPlanned2ValueT());
		this.upDateUH = iyakuPlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = iyakuPlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(iyakuPlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(iyakuPlanP.getImplPlan().getPlanned2ValueT());
		this.upDateP = iyakuPlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = iyakuPlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(iyakuPlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(iyakuPlanZ.getImplPlan().getPlanned2ValueT());
		this.upDateZ = iyakuPlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ = iyakuPlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}
		this.canMovePlanLevel = canMovePlanLevel;
	}

	/**
	 * コンストラクタ(支店別計画)<br>
	 *
	 * @param branchPlanUh 支店別計画UH
	 * @param branchPlanP 支店別計画P
	 * @param branchPlanZ 支店別計画Z
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 */
	public SosPlanResultDetailTotalDto(ManageBranchPlan branchPlanUh, ManageBranchPlan branchPlanP, ManageBranchPlan branchPlanZ, boolean canMovePlanLevel) {
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(branchPlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(branchPlanUh.getImplPlan().getPlanned2ValueT());
		this.upDateUH = branchPlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = branchPlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(branchPlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(branchPlanP.getImplPlan().getPlanned2ValueT());
		this.upDateP = branchPlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = branchPlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(branchPlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(branchPlanZ.getImplPlan().getPlanned2ValueT());
		this.upDateZ = branchPlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ = branchPlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}
		this.canMovePlanLevel = canMovePlanLevel;
	}

	/**
	 * コンストラクタ(営業所別計画)<br>
	 *
	 * @param officePlanUh 営業所別計画UH
	 * @param officePlanP 営業所別計画P
	 * @param officePlanZ 営業所別計画Z
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 */
	public SosPlanResultDetailTotalDto(ManageOfficePlan officePlanUh, ManageOfficePlan officePlanP, ManageOfficePlan officePlanZ, boolean canMovePlanLevel) {
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(officePlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(officePlanUh.getImplPlan().getPlanned2ValueT());
		this.upDateUH = officePlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = officePlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(officePlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(officePlanP.getImplPlan().getPlanned2ValueT());
		this.upDateP = officePlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = officePlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(officePlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(officePlanZ.getImplPlan().getPlanned2ValueT());
		this.upDateZ = officePlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ = officePlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}
		this.canMovePlanLevel = canMovePlanLevel;
	}

	/**
	 * コンストラクタ(チーム別計画)<br>
	 *
	 * @param teamPlanUh チーム別計画UH
	 * @param teamPlanP チーム別計画P
	 * @param teamPlanZ チーム別計画Z
	 * @param canMovePlanLevel プランレベル移行可否フラグ
	 */
	public SosPlanResultDetailTotalDto(ManageTeamPlan teamPlanUh, ManageTeamPlan teamPlanP, ManageTeamPlan teamPlanZ, boolean canMovePlanLevel) {
		this.yBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(teamPlanUh.getImplPlan().getPlanned2ValueY());
		this.tBaseValueUH = ConvertUtil.parseMoneyToThousandUnit(teamPlanUh.getImplPlan().getPlanned2ValueT());
		this.upDateUH = teamPlanUh.getUpDate();
		if (upDateUH != null) {
			this.upJgiNameUH = teamPlanUh.getUpJgiName();
		} else {
			this.upJgiNameUH = null;
		}
		this.yBaseValueP = ConvertUtil.parseMoneyToThousandUnit(teamPlanP.getImplPlan().getPlanned2ValueY());
		this.tBaseValueP = ConvertUtil.parseMoneyToThousandUnit(teamPlanP.getImplPlan().getPlanned2ValueT());
		this.upDateP = teamPlanP.getUpDate();
		if (upDateP != null) {
			this.upJgiNameP = teamPlanP.getUpJgiName();
		} else {
			this.upJgiNameP = null;
		}
		this.yBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(teamPlanZ.getImplPlan().getPlanned2ValueY());
		this.tBaseValueZ = ConvertUtil.parseMoneyToThousandUnit(teamPlanZ.getImplPlan().getPlanned2ValueT());
		this.upDateZ = teamPlanZ.getUpDate();
		if (upDateZ != null) {
			this.upJgiNameZ = teamPlanZ.getUpJgiName();
		} else {
			this.upJgiNameZ = null;
		}
		this.canMovePlanLevel = canMovePlanLevel;
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
	 * @return yBaseValueZ
	 */
	public Long getyBaseValueZ() {
		return yBaseValueZ;
	}

	/**
	 * @return tBaseValueZ
	 */
	public Long gettBaseValueZ() {
		return tBaseValueZ;
	}

	/**
	 * @return upDateZ
	 */
	public Date getUpDateZ() {
		return upDateZ;
	}

	/**
	 * @return upJgiNameZ
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
