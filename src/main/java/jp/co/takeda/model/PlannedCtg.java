package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;

/**
 * 計画対象カテゴリ領域を表すモデルクラス
 *
 * @author yyoshino
 */
public class PlannedCtg extends DpModel<PlannedCtg> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 9155306166523211873L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 *  年度
	 */
	private String calYear;
	/**
	 *  期
	 */
	private String calTrm;
	/**
	 *  領域グループコード
	 */
	private String trtGrpCd;
	/**
	 * 領域コード
	 */
	private String trtCd;
	/**
	 * カテゴリ
	 */
	private String category;
	/**
	 * 親子区分
	 */
	private String oyakoKb;
	/**
	 * 親子区分2
	 */
	private String oyakoKb2;
	/**
	 * 対象施設区分
	 */
	private String tgtInsKb;
	/**
	 * 計画立案レベル・全社
	 */
	private String planLevelZensha;
	/**
	 * 計画立案レベル・支店
	 */
	private String planLevelSiten;
	/**
	 * 計画立案レベル・営業所
	 */
	private String planLevelOffice;
	/**
	 * 計画立案レベル・チーム
	 */
	private String planLevelTeam;
	/**
	 * 計画立案レベル・担当者
	 */
	private String planLevelMr;
	/**
	 * 計画立案レベル・施設
	 */
	private String planLevelIns;
	/**
	 * 計画立案レベル・施設特約店
	 */
	private String planLevelInsWs;
	/**
	 * 計画立案レベル・特約店
	 */
	private String planLevelWs;


	//---------------------
	// Getter & Setter
	// --------------------

	public String getCalYear() {
		return calYear;
	}

	public void setCalYear(String calYear) {
		this.calYear = calYear;
	}

	public String getCalTrm() {
		return calTrm;
	}

	public void setCalTrm(String calTrm) {
		this.calTrm = calTrm;
	}

	public String getTrtGrpCd() {
		return trtGrpCd;
	}

	public void setTrtGrpCd(String trtGrpCd) {
		this.trtGrpCd = trtGrpCd;
	}

	public String getTrtCd() {
		return trtCd;
	}

	public void setTrtCd(String trtCd) {
		this.trtCd = trtCd;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOyakoKb() {
		return oyakoKb;
	}

	public void setOyakoKb(String oyakoKb) {
		this.oyakoKb = oyakoKb;
	}

	public String getOyakoKb2() {
		return oyakoKb2;
	}

	public void setOyakoKb2(String oyakoKb2) {
		this.oyakoKb2 = oyakoKb2;
	}

	public String getTgtInsKb() {
		return tgtInsKb;
	}

	public void setTgtInsKb(String tgtInsKb) {
		this.tgtInsKb = tgtInsKb;
	}

	public String getPlanLevelZensha() {
		return planLevelZensha;
	}

	public void setPlanLevelZensha(String planLevelZensha) {
		this.planLevelZensha = planLevelZensha;
	}

	public String getPlanLevelSiten() {
		return planLevelSiten;
	}

	public void setPlanLevelSiten(String planLevelSiten) {
		this.planLevelSiten = planLevelSiten;
	}

	public String getPlanLevelOffice() {
		return planLevelOffice;
	}

	public void setPlanLevelOffice(String planLevelOffice) {
		this.planLevelOffice = planLevelOffice;
	}

	public String getPlanLevelTeam() {
		return planLevelTeam;
	}

	public void setPlanLevelTeam(String planLevelTeam) {
		this.planLevelTeam = planLevelTeam;
	}

	public String getPlanLevelMr() {
		return planLevelMr;
	}

	public void setPlanLevelMr(String planLevelMr) {
		this.planLevelMr = planLevelMr;
	}

	public String getPlanLevelIns() {
		return planLevelIns;
	}

	public void setPlanLevelIns(String planLevelIns) {
		this.planLevelIns = planLevelIns;
	}

	public String getPlanLevelInsWs() {
		return planLevelInsWs;
	}

	public void setPlanLevelInsWs(String planLevelInsWs) {
		this.planLevelInsWs = planLevelInsWs;
	}

	public String getPlanLevelWs() {
		return planLevelWs;
	}

	public void setPlanLevelWs(String planLevelWs) {
		this.planLevelWs = planLevelWs;
	}



	@Override
	public int compareTo(PlannedCtg obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.calYear,obj.calYear).append(this.calTrm,obj.calTrm).append(this.trtGrpCd,obj.trtGrpCd)
					.append(this.trtCd,obj.trtCd).append(this.category,obj.category).append(this.oyakoKb,obj.oyakoKb).append(this.tgtInsKb,obj.tgtInsKb)
					.append(this.planLevelZensha,obj.planLevelZensha).append(this.planLevelSiten,obj.planLevelSiten)
					.append(this.planLevelOffice,obj.planLevelOffice).append(this.planLevelTeam,obj.planLevelTeam).append(this.planLevelMr,obj.planLevelMr)
					.append(this.planLevelIns,obj.planLevelIns).append(this.planLevelInsWs,obj.planLevelInsWs).append(this.planLevelWs,obj.planLevelWs).toComparison();
		}
		return -1;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlannedCtg other = (PlannedCtg) obj;
		if (calTrm == null) {
			if (other.calTrm != null)
				return false;
		} else if (!calTrm.equals(other.calTrm))
			return false;
		if (calYear == null) {
			if (other.calYear != null)
				return false;
		} else if (!calYear.equals(other.calYear))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (oyakoKb == null) {
			if (other.oyakoKb != null)
				return false;
		} else if (!oyakoKb.equals(other.oyakoKb))
			return false;
		if (planLevelIns == null) {
			if (other.planLevelIns != null)
				return false;
		} else if (!planLevelIns.equals(other.planLevelIns))
			return false;
		if (planLevelInsWs == null) {
			if (other.planLevelInsWs != null)
				return false;
		} else if (!planLevelInsWs.equals(other.planLevelInsWs))
			return false;
		if (planLevelMr == null) {
			if (other.planLevelMr != null)
				return false;
		} else if (!planLevelMr.equals(other.planLevelMr))
			return false;
		if (planLevelOffice == null) {
			if (other.planLevelOffice != null)
				return false;
		} else if (!planLevelOffice.equals(other.planLevelOffice))
			return false;
		if (planLevelSiten == null) {
			if (other.planLevelSiten != null)
				return false;
		} else if (!planLevelSiten.equals(other.planLevelSiten))
			return false;
		if (planLevelTeam == null) {
			if (other.planLevelTeam != null)
				return false;
		} else if (!planLevelTeam.equals(other.planLevelTeam))
			return false;
		if (planLevelWs == null) {
			if (other.planLevelWs != null)
				return false;
		} else if (!planLevelWs.equals(other.planLevelWs))
			return false;
		if (planLevelZensha == null) {
			if (other.planLevelZensha != null)
				return false;
		} else if (!planLevelZensha.equals(other.planLevelZensha))
			return false;
		if (tgtInsKb == null) {
			if (other.tgtInsKb != null)
				return false;
		} else if (!tgtInsKb.equals(other.tgtInsKb))
			return false;
		if (trtCd == null) {
			if (other.trtCd != null)
				return false;
		} else if (!trtCd.equals(other.trtCd))
			return false;
		if (trtGrpCd == null) {
			if (other.trtGrpCd != null)
				return false;
		} else if (!trtGrpCd.equals(other.trtGrpCd))
			return false;
		return true;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calTrm == null) ? 0 : calTrm.hashCode());
		result = prime * result + ((calYear == null) ? 0 : calYear.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((oyakoKb == null) ? 0 : oyakoKb.hashCode());
		result = prime * result + ((planLevelIns == null) ? 0 : planLevelIns.hashCode());
		result = prime * result + ((planLevelInsWs == null) ? 0 : planLevelInsWs.hashCode());
		result = prime * result + ((planLevelMr == null) ? 0 : planLevelMr.hashCode());
		result = prime * result + ((planLevelOffice == null) ? 0 : planLevelOffice.hashCode());
		result = prime * result + ((planLevelSiten == null) ? 0 : planLevelSiten.hashCode());
		result = prime * result + ((planLevelTeam == null) ? 0 : planLevelTeam.hashCode());
		result = prime * result + ((planLevelWs == null) ? 0 : planLevelWs.hashCode());
		result = prime * result + ((planLevelZensha == null) ? 0 : planLevelZensha.hashCode());
		result = prime * result + ((tgtInsKb == null) ? 0 : tgtInsKb.hashCode());
		result = prime * result + ((trtCd == null) ? 0 : trtCd.hashCode());
		result = prime * result + ((trtGrpCd == null) ? 0 : trtGrpCd.hashCode());
		return result;
	}


	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
