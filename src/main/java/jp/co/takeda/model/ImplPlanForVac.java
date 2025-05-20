package jp.co.takeda.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用実施計画を表すクラス
 *
 * @author khashimoto
 */
public class ImplPlanForVac implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 実施計画１（YB価）
	 */
	private Long planned1ValueY;

	/**
	 * 実施計画1（T価）
	 */
	private Long planned1ValueT;

	/**
	 * 実施計画2（YB価）
	 */
	private Long planned2ValueY;

	/**
	 * 実施計画2（T価）
	 */
	private Long planned2ValueT;

	/**
	 * 変更前実施計画1（YB価）
	 */
	private Long befPlanned1ValueY;

	/**
	 * 変更前実施計画1（T価）
	 */
	private Long befPlanned1ValueT;

	/**
	 * 変更前実施計画2（YB価）
	 */
	private Long befPlanned2ValueY;

	/**
	 * 変更前実施計画2（T価）
	 */
	private Long befPlanned2ValueT;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 実施計画1（YB価）を取得する。
	 *
	 * @return 実施計画1（YB価）
	 */
	public Long getPlanned1ValueY() {
		return planned1ValueY;
	}

	/**
	 * 実施計画1（YB価）を設定する。
	 *
	 * @param planned1ValueY 実施計画1（YB価）
	 */
	public void setPlanned1ValueY(Long planned1ValueY) {
		this.planned1ValueY = planned1ValueY;
	}

	/**
	 * 実施計画1（T価）を取得する。
	 *
	 * @return 実施計画1（T価）
	 */
	public Long getPlanned1ValueT() {
		return planned1ValueT;
	}

	/**
	 * 実施計画1（T価）を設定する。
	 *
	 * @param planned1ValueT 実施計画1（T価）
	 */
	public void setPlanned1ValueT(Long planned1ValueT) {
		this.planned1ValueT = planned1ValueT;
	}

	/**
	 * 実施計画2（YB価）を取得する。
	 *
	 * @return 実施計画2（YB価）
	 */
	public Long getPlanned2ValueY() {
		return planned2ValueY;
	}

	/**
	 * 実施計画2（YB価）を設定する。
	 *
	 * @param planned2ValueY 実施計画2（YB価）
	 */
	public void setPlanned2ValueY(Long planned2ValueY) {
		this.planned2ValueY = planned2ValueY;
	}

	/**
	 * 実施計画2（T価）を取得する。
	 *
	 * @return 実施計画2（T価）
	 */
	public Long getPlanned2ValueT() {
		return planned2ValueT;
	}

	/**
	 * 実施計画2（T価）を設定する。
	 *
	 * @param planned2ValueT 実施計画2（T価）
	 */
	public void setPlanned2ValueT(Long planned2ValueT) {
		this.planned2ValueT = planned2ValueT;
	}

	/**
	 * 変更前実施計画1（YB価）を取得する。
	 *
	 * @return 変更前実施計画1（YB価）
	 */
	public Long getBefPlanned1ValueY() {
		return befPlanned1ValueY;
	}

	/**
	 * 変更前実施計画1（YB価）を設定する。
	 *
	 * @param befPlanned1ValueY 変更前実施計画1（YB価）
	 */
	public void setBefPlanned1ValueY(Long befPlanned1ValueY) {
		this.befPlanned1ValueY = befPlanned1ValueY;
	}

	/**
	 * 変更前実施計画1（T価）を取得する。
	 *
	 * @return 変更前実施計画1（T価）
	 */
	public Long getBefPlanned1ValueT() {
		return befPlanned1ValueT;
	}

	/**
	 * 変更前実施計画1（T価）を設定する。
	 *
	 * @param befPlanned1ValueT 変更前実施計画1（T価）
	 */
	public void setBefPlanned1ValueT(Long befPlanned1ValueT) {
		this.befPlanned1ValueT = befPlanned1ValueT;
	}

	/**
	 * 変更前実施計画2（YB価）を取得する。
	 *
	 * @return 変更前実施計画2（YB価）
	 */
	public Long getBefPlanned2ValueY() {
		return befPlanned2ValueY;
	}

	/**
	 * 変更前実施計画2（YB価）を設定する。
	 *
	 * @param befPlanned2ValueY 変更前実施計画2（YB価）
	 */
	public void setBefPlanned2ValueY(Long befPlanned2ValueY) {
		this.befPlanned2ValueY = befPlanned2ValueY;
	}

	/**
	 * 変更前実施計画2（T価）を取得する。
	 *
	 * @return 変更前実施計画2（T価）
	 */
	public Long getBefPlanned2ValueT() {
		return befPlanned2ValueT;
	}

	/**
	 * 変更前実施計画2（T価）を設定する。
	 *
	 * @param befPlanned2ValueT 変更前実施計画2（T価）
	 */
	public void setBefPlanned2ValueT(Long befPlanned2ValueT) {
		this.befPlanned2ValueT = befPlanned2ValueT;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
