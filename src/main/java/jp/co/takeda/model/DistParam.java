package jp.co.takeda.model;

import java.io.Serializable;

import jp.co.takeda.model.div.DistributionType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分パラメータを表すクラス
 * 
 * @author tkawabata
 */
public class DistParam implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 配分先区分
	 */
	private DistributionType distributionType;

	/**
	 * ゼロ配分かを示すフラグ
	 */
	private Boolean zeroDistFlg;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 配分先区分を取得する。
	 * 
	 * @return 配分先区分
	 */
	public DistributionType getDistributionType() {
		return distributionType;
	}

	/**
	 * 配分先区分を設定する。
	 * 
	 * @param distributionType 配分先区分
	 */
	public void setDistributionType(DistributionType distributionType) {
		this.distributionType = distributionType;
	}

	/**
	 * ゼロ配分かを示すフラグを取得する。
	 * 
	 * @return ゼロ配分かを示すフラグ
	 */
	public Boolean getZeroDistFlg() {
		return zeroDistFlg;
	}

	/**
	 * ゼロ配分かを示すフラグを設定する。
	 * 
	 * @param zeroDistFlg ゼロ配分かを示すフラグ
	 */
	public void setZeroDistFlg(Boolean zeroDistFlg) {
		this.zeroDistFlg = zeroDistFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
