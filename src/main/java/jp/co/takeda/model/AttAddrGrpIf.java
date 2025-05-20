package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.AttSystemKey;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Attention宛先グループ指定I/F.
 * 
 * @author khashimoto
 */
public class AttAddrGrpIf extends Model<AttAddrGrpIf> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * バージョン番号
	 */
	public static final Integer IF_VERSION_NO = 1;

	/**
	 * 処理区分
	 */
	public static final String PROC_KB = "I";

	/**
	 * 反映済みフラグ
	 */
	public static final String DONE_FLG = "0";

	/**
	 * 外部システム一意キー
	 */
	private AttSystemKey ifPKey;

	/**
	 * 外部システム宛先一意キー
	 */
	private Integer ifGropSeq;

	/**
	 * 外部システムID
	 */
	private Integer ifSysKey;

	/**
	 * 宛先従業員コード
	 */
	private Integer addrJgiNo;

	/**
	 * 最終更新者
	 */
	private Integer upJgiNo;

	/**
	 * 登録日
	 */
	private Date isDate;

	/**
	 * 更新日
	 */
	private Date upDate;

	/**
	 * 更新画面ＩＤ
	 */
	private String upScrnId;

	/**
	 * 更新機能ＩＤ
	 */
	private String upFuncId;

	/**
	 * 外部システム一意キーを取得する。
	 * 
	 * @return 外部システム一意キー
	 */
	public AttSystemKey getIfPKey() {
		return ifPKey;
	}

	/**
	 * 外部システム一意キーを設定する。
	 * 
	 * @param ifPKey 外部システム一意キー
	 */
	public void setIfPKey(AttSystemKey ifPKey) {
		this.ifPKey = ifPKey;
	}

	/**
	 * 外部システム宛先一意キーを取得する。
	 * 
	 * @return 外部システム宛先一意キー
	 */
	public Integer getIfGropSeq() {
		return ifGropSeq;
	}

	/**
	 * 外部システム宛先一意キーを設定する。
	 * 
	 * @param ifGropSeq 外部システム宛先一意キー
	 */
	public void setIfGropSeq(Integer ifGropSeq) {
		this.ifGropSeq = ifGropSeq;
	}

	/**
	 * 外部システムIDを取得する。
	 * 
	 * @return 外部システムID
	 */
	public Integer getIfSysKey() {
		return ifSysKey;
	}

	/**
	 * 外部システムIDを設定する。
	 * 
	 * @param ifSysKey 外部システムID
	 */
	public void setIfSysKey(Integer ifSysKey) {
		this.ifSysKey = ifSysKey;
	}

	/**
	 * 宛先従業員コードを取得する。
	 * 
	 * @return 宛先従業員コード
	 */
	public Integer getAddrJgiNo() {
		return addrJgiNo;
	}

	/**
	 * 宛先従業員コードを設定する。
	 * 
	 * @param addrJgiNo 宛先従業員コード
	 */
	public void setAddrJgiNo(Integer addrJgiNo) {
		this.addrJgiNo = addrJgiNo;
	}

	/**
	 * 最終更新者を取得する。
	 * 
	 * @return 最終更新者
	 */
	public Integer getUpJgiNo() {
		return upJgiNo;
	}

	/**
	 * 最終更新者を設定する。
	 * 
	 * @param upJgiNo 最終更新者
	 */
	public void setUpJgiNo(Integer upJgiNo) {
		this.upJgiNo = upJgiNo;
	}

	/**
	 * 登録日を取得する。
	 * 
	 * @return 登録日
	 */
	public Date getIsDate() {
		return isDate;
	}

	/**
	 * 登録日を設定する。
	 * 
	 * @param isDate 登録日
	 */
	public void setIsDate(Date isDate) {
		this.isDate = isDate;
	}

	/**
	 * 更新日を取得する。
	 * 
	 * @return 更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 更新日を設定する。
	 * 
	 * @param upDate 更新日
	 */
	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}

	/**
	 * 更新画面ＩＤを取得する。
	 * 
	 * @return 更新画面ＩＤ
	 */
	public String getUpScrnId() {
		return upScrnId;
	}

	/**
	 * 更新画面ＩＤを設定する。
	 * 
	 * @param upScrnId 更新画面ＩＤ
	 */
	public void setUpScrnId(String upScrnId) {
		this.upScrnId = upScrnId;
	}

	/**
	 * 更新機能ＩＤを取得する。
	 * 
	 * @return 更新機能ＩＤ
	 */
	public String getUpFuncId() {
		return upFuncId;
	}

	/**
	 * 更新機能ＩＤを設定する。
	 * 
	 * @param upFuncId 更新機能ＩＤ
	 */
	public void setUpFuncId(String upFuncId) {
		this.upFuncId = upFuncId;
	}

	@Override
	public int compareTo(AttAddrGrpIf obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.ifPKey, obj.ifPKey).append(this.ifSysKey, obj.ifSysKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && AttAddrGrpIf.class.isAssignableFrom(entry.getClass())) {
			AttAddrGrpIf obj = (AttAddrGrpIf) entry;
			return new EqualsBuilder().append(this.ifPKey, obj.ifPKey).append(this.ifSysKey, obj.ifSysKey).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.ifPKey).append(this.ifSysKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
