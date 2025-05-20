package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.AttSystemKey;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * [外部直接参照]Attention基本I/Fを表すモデルクラス
 * 
 * @author tkawabata
 */
public class AttBaseIf extends Model<AttBaseIf> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * バージョン番号
	 */
	public static final Integer IF_VERSION_NO = 1;

	/**
	 * 状態区分
	 */
	public static final String STATUS_KB = "3";

	/**
	 * 発信文書フラグ
	 */
	public static final String FRML_DOCU_FLG = "0";

	/**
	 * 重要区分
	 */
	public static final String JUYO_KB = "0";

	/**
	 * 転送先・支店
	 */
	public static final String FW_KB_BR = "0";

	/**
	 * 転送先・営業
	 */
	public static final String FW_KB_DIST = "0";

	/**
	 * 転送先・病院G
	 */
	public static final String FW_KB_HOSP_G = "0";

	/**
	 * 転送先・特約店部（G)
	 */
	public static final String FW_KB_TOKU_G = "0";

	/**
	 * 転送先・学術部（G)
	 */
	public static final String FW_KB_ACD_G = "0";

	/**
	 * 転送先・APM
	 */
	public static final String FW_KB_APM = "0";

	/**
	 * 転送先・予備１
	 */
	public static final String FW_KB_YOBI1 = "0";

	/**
	 * 転送先・予備２
	 */
	public static final String FW_KB_YOBI2 = "0";

	/**
	 * 転送先・予備３
	 */
	public static final String FW_KB_YOBI3 = "0";

	/**
	 * 処理区分
	 */
	public static final String PROC_KB = "I";

	/**
	 * 反映済みフラグ
	 */
	public static final String DONE_FLG = "0";

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 外部システム一意キー
	 */
	private AttSystemKey ifPKey;

	/**
	 * 外部システムID
	 */
	private Integer ifSysKey;

	/**
	 * 件名
	 */
	private String subject;

	/**
	 * 内容
	 */
	private String conTxt;

	/**
	 * 掲示開始日
	 */
	private Date naviStartDay;

	/**
	 * 掲示終了日
	 */
	private Date naviEndDay;

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

	//---------------------
	// Getter & Setter
	// --------------------

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
	 * 件名を取得する。
	 * 
	 * @return 件名
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 件名を設定する。
	 * 
	 * @param subject 件名
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 内容を取得する。
	 * 
	 * @return 内容
	 */
	public String getConTxt() {
		return conTxt;
	}

	/**
	 * 内容を設定する。
	 * 
	 * @param conTxt 内容
	 */
	public void setConTxt(String conTxt) {
		this.conTxt = conTxt;
	}

	/**
	 * 掲示開始日を取得する。
	 * 
	 * @return 掲示開始日
	 */
	public Date getNaviStartDay() {
		return naviStartDay;
	}

	/**
	 * 掲示開始日を設定する。
	 * 
	 * @param naviStartDay 掲示開始日
	 */
	public void setNaviStartDay(Date naviStartDay) {
		this.naviStartDay = naviStartDay;
	}

	/**
	 * 掲示終了日を取得する。
	 * 
	 * @return 掲示終了日
	 */
	public Date getNaviEndDay() {
		return naviEndDay;
	}

	/**
	 * 掲示終了日を設定する。
	 * 
	 * @param naviEndDay 掲示終了日
	 */
	public void setNaviEndDay(Date naviEndDay) {
		this.naviEndDay = naviEndDay;
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
	public int compareTo(AttBaseIf obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.ifPKey, obj.ifPKey).append(this.ifSysKey, obj.ifSysKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && AttBaseIf.class.isAssignableFrom(entry.getClass())) {
			AttBaseIf obj = (AttBaseIf) entry;
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
