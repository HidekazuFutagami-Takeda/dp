package jp.co.takeda.model;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.PlannedType;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.div.Term;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 納入計画システム管理を表すモデルクラス
 * 
 * @author khashimoto
 */
public class SysManage extends Model<SysManage> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 管理対象業務分類
	 */
	private SysClass sysClass;

	/**
	 * 管理対象業務区分
	 */
	private SysType sysType;

	/**
	 * 管理対象年度(YYYY形式)
	 */
	private String sysYear;

	/**
	 * 管理対象期
	 */
	private Term sysTerm;

	/**
	 * サービス停止フラグ
	 */
	private Boolean serviceStopFlg;

	/**
	 * サービス停止メッセージコード
	 */
	private String messageCode;

	/**
	 * 施設特約店別計画薬価改定フラグ
	 */
	private Boolean yakkaInsWsFlg;

	/**
	 * 特約店別計画薬価改定フラグ
	 */
	private Boolean yakkaWsFlg;

	/**
	 * T価変換バッチ実施フラグ
	 */
	private Boolean transTFlg;

	/**
	 * 施設特約店別計画〆フラグ
	 */
	private Boolean wsEndFlg;

	/**
	 * 実施計画区分
	 */
	private PlannedType plannedType;

	/**
	 * システムクローズ時刻１
	 */
	private String serviceStopTime1;

	/**
	 * システムクローズ時刻２
	 */
	private String serviceStopTime2;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 管理対象業務分類を設定する。
	 * 
	 * @param sysClass 管理対象業務分類
	 */
	public void setSysClass(SysClass sysClass) {
		this.sysClass = sysClass;
	}

	/**
	 * 管理対象業務区分を取得する。
	 * 
	 * @return 管理対象業務区分
	 */
	public SysType getSysType() {
		return sysType;
	}

	/**
	 * 管理対象業務区分を設定する。
	 * 
	 * @param sysType 管理対象業務区分
	 */
	public void setSysType(SysType sysType) {
		this.sysType = sysType;
	}

	/**
	 * 管理対象年度(YYYY形式)を取得する。
	 * 
	 * @return 管理対象年度(YYYY形式)
	 */
	public String getSysYear() {
		return sysYear;
	}

	/**
	 * 管理対象年度(YYYY形式)を設定する。
	 * 
	 * @param sysYear 管理対象年度(YYYY形式)
	 */
	public void setSysYear(String sysYear) {
		this.sysYear = sysYear;
	}

	/**
	 * 管理対象期を取得する。
	 * 
	 * @return 管理対象期
	 */
	public Term getSysTerm() {
		return sysTerm;
	}

	/**
	 * 管理対象期を設定する。
	 * 
	 * @param sysTerm 管理対象期
	 */
	public void setSysTerm(Term sysTerm) {
		this.sysTerm = sysTerm;
	}

	/**
	 * サービス停止フラグを取得する。
	 * 
	 * @return サービス停止フラグ
	 */
	public Boolean getServiceStopFlg() {
		return serviceStopFlg;
	}

	/**
	 * サービス停止フラグを設定する。
	 * 
	 * @param serviceStopFlg サービス停止フラグ
	 */
	public void setServiceStopFlg(Boolean serviceStopFlg) {
		this.serviceStopFlg = serviceStopFlg;
	}

	/**
	 * メッセージコードを取得する。
	 * 
	 * @return メッセージコード
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * メッセージコードを設定する。
	 * 
	 * @param messageCode メッセージコード
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/**
	 * 施設特約店別計画薬価改定フラグを取得する。
	 * 
	 * @return 施設特約店別計画薬価改定フラグ
	 */
	public Boolean getYakkaInsWsFlg() {
		return yakkaInsWsFlg;
	}

	/**
	 * 施設特約店別計画薬価改定フラグを設定する。
	 * 
	 * @param yakkaInsWsFlg 施設特約店別計画薬価改定フラグ
	 */
	public void setYakkaInsWsFlg(Boolean yakkaInsWsFlg) {
		this.yakkaInsWsFlg = yakkaInsWsFlg;
	}

	/**
	 * 特約店別計画薬価改定フラグを取得する。
	 * 
	 * @return 特約店別計画薬価改定フラグ
	 */
	public Boolean getYakkaWsFlg() {
		return yakkaWsFlg;
	}

	/**
	 * 特約店別計画薬価改定フラグを設定する。
	 * 
	 * @param yakkaWsFlg 特約店別計画薬価改定フラグ
	 */
	public void setYakkaWsFlg(Boolean yakkaWsFlg) {
		this.yakkaWsFlg = yakkaWsFlg;
	}

	/**
	 * T価変換バッチ実施フラグを取得する。
	 * 
	 * @return T価変換バッチ実施フラグ
	 */
	public Boolean getTransTFlg() {
		return transTFlg;
	}

	/**
	 * T価変換バッチ実施フラグを設定する。
	 * 
	 * @param transTFlg T価変換バッチ実施フラグ
	 */
	public void setTransTFlg(Boolean transTFlg) {
		this.transTFlg = transTFlg;
	}

	/**
	 * 施設特約店別計画〆フラグを取得する。
	 * 
	 * @return 施設特約店別計画〆フラグ
	 */
	public Boolean getWsEndFlg() {
		return wsEndFlg;
	}

	/**
	 * 施設特約店別計画〆フラグを設定する。
	 * 
	 * @param wsEndFlg 施設特約店別計画〆フラグ
	 */
	public void setWsEndFlg(Boolean wsEndFlg) {
		this.wsEndFlg = wsEndFlg;
	}

	/**
	 * 管理対象業務分類を取得する。
	 * 
	 * @return 管理対象業務分類
	 */
	public SysClass getSysClass() {
		return sysClass;
	}

	/**
	 * 実施計画区分を取得する。
	 * 
	 * @return 実施計画区分
	 */
	public PlannedType getPlannedType() {
		return plannedType;
	}

	/**
	 * 実施計画区分を設定する。
	 * 
	 * @param plannedType 実施計画区分
	 */
	public void setPlannedType(PlannedType plannedType) {
		this.plannedType = plannedType;
	}

	/**
	 * システムクローズ時刻１を取得する。
	 * 
	 * @return システムクローズ時刻１
	 */
	public String getServiceStopTime1() {
		return serviceStopTime1;
	}

	/**
	 * システムクローズ時刻１を設定する。
	 * 
	 * @param serviceStopTime1 システムクローズ時刻１
	 */
	public void setServiceStopTime1(String serviceStopTime1) {
		this.serviceStopTime1 = serviceStopTime1;
	}

	/**
	 * システムクローズ時刻２を取得する。
	 * 
	 * @return システムクローズ時刻２
	 */
	public String getServiceStopTime2() {
		return serviceStopTime2;
	}

	/**
	 * システムクローズ時刻２を設定する。
	 * 
	 * @param serviceStopTime2 システムクローズ時刻２
	 */
	public void setServiceStopTime2(String serviceStopTime2) {
		this.serviceStopTime2 = serviceStopTime2;
	}

	@Override
	public int compareTo(SysManage obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sysClass, obj.sysClass).append(this.sysType, obj.sysType).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SysManage.class.isAssignableFrom(entry.getClass())) {
			SysManage obj = (SysManage) entry;
			return new EqualsBuilder().append(this.sysClass, obj.sysClass).append(this.sysType, obj.sysType).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sysClass).append(this.sysType).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
