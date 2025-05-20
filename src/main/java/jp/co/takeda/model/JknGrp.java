package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.SosLvl;

/**
 * 計画管理条件セットグループを表すモデルクラス
 *
 * @author yyoshino
 */
public class JknGrp extends DpManageModel<JknGrp> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6648271165571014519L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * ID
	 */
	private JknGrpId jknGrpId;

	/**
	 * 条件セットコード
	 */
	private String jokenSetCd;

	/**
	 * 条件区分
	 */
	private JokenKbn jokenKbn;

	/**
	 * 参照可能組織レベル
	 */
	private SosLvl sosLvl;

	/**
	 * プライオリティ
	 */
	private int priority;

	/**
	 * 備考
	 */
	private String biko;


	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * @return jknGrpId
	 */
	public JknGrpId getJknGrpId() {
		if(jknGrpId == null) {
			return JknGrpId.ERROR;
		}
		return jknGrpId;
	}

	/**
	 * @param jknGrpId
	 */
	public void setJknGrpId(JknGrpId jknGrpId) {
		this.jknGrpId = jknGrpId;
	}

	/**
	 * @return jokenSetCd
	 */
	public String getJokenSetCd() {
		return jokenSetCd;
	}

	/**
	 * @param jokenSetCd
	 */
	public void setJokenSetCd(String jokenSetCd) {
		this.jokenSetCd = jokenSetCd;
	}


	/**
	 * @return jokenKbn
	 */
	public JokenKbn getJokenKbn() {
		return jokenKbn;
	}

	/**
	 * @param jokenKbn
	 */
	public void setJokenKbn(JokenKbn jokenKbn) {
		this.jokenKbn = jokenKbn;
	}

	/**
	 * @return sosLvl
	 */
	public SosLvl getSosLvl() {
		return sosLvl;
	}

	/**
	 * @param sosLvl
	 */
	public void setSosLvl(SosLvl sosLvl) {
		this.sosLvl = sosLvl;
	}

	/**
	 * @return priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return biko
	 */
	public String getBiko() {
		return biko;
	}

	/**
	 * @param biko
	 */
	public void setBiko(String biko) {
		this.biko = biko;
	}

	@Override
	public int compareTo(JknGrp obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jknGrpId, obj.jknGrpId).append(this.jokenSetCd, obj.jokenSetCd).append(this.jokenKbn, obj.jokenKbn).append(this.sosLvl, obj.sosLvl).append(this.priority, obj.priority).append(this.biko, obj.biko).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && JknGrp.class.isAssignableFrom(entry.getClass())) {
			JknGrp obj = (JknGrp) entry;
			return new EqualsBuilder().append(this.jknGrpId, obj.jknGrpId).append(this.jokenSetCd, obj.jokenSetCd).append(this.jokenKbn, obj.jokenKbn).append(this.sosLvl, obj.sosLvl).append(this.priority, obj.priority).append(this.biko, obj.biko).isEquals();
		}
		return false;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((biko == null) ? 0 : biko.hashCode());
		result = prime * result + ((jknGrpId == null) ? 0 : jknGrpId.hashCode());
		result = prime * result + jokenKbn.hashCode();
		result = prime * result + ((jokenSetCd == null) ? 0 : jokenSetCd.hashCode());
		result = prime * result + priority;
		result = prime * result + sosLvl.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
