package jp.co.takeda.model;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.RegistType;

/**
 * 配分除外施設を表すモデルクラス
 *
 * @author tkawabata
 */
public class ExceptDistIns extends DpModel<ExceptDistIns> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 配分除外施設の場合の、品目固定コードの値
	 */
	public static final String FOR_INS_PROD_CODE = null;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 配分除外品目のリスト
	 */
	private List<ExceptProd> exceptProd;

	/**
	 * 試算除外フラグ
	 */
	private Boolean estimationFlg;

	/**
	 * 配分除外フラグ
	 */
	private Boolean exceptFlg;

	/**
	 * 登録区分
	 */
	private RegistType registType;

	/**
	 * 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	private Integer jgiNo;

	/**
	 * 氏名（Ref[従業員情報].〔氏名〕）
	 */
	private String jgiName;

	/**
	 * 施設分類（Ref[施設情報].〔対象分類〕）
	 */
	private InsClass insClass;

	/**
	 * サブコード分類（Ref[施設情報].〔サブコード分類〕）
	 */
	private OldInsrFlg oldInsrFlg;

	/**
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private String insAbbrName;

	/**
	 * 対象区分（Ref[施設情報].〔対象区分〕）
	 */
	private HoInsType hoInsType;

// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//	/**
//	 * COM-氏名（Ref[従業員情報].〔氏名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String comJgiName;
//
//	/**
//	 * COM-職種名（Ref[従業員情報].〔職種名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String comShokushuName;
//
//	/**
//	 * CVM-氏名（Ref[従業員情報].〔氏名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String cvmJgiName;
//
//	/**
//	 * CVM-職種名（Ref[従業員情報].〔職種名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String cvmShokushuName;
//
//	/**
//	 * RS-氏名（Ref[従業員情報].〔氏名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String rsJgiName;
//
//	/**
//	 * RS-職種名（Ref[従業員情報].〔職種名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String rsShokushuName;
//
//	/**
//	 * ONC-氏名（Ref[従業員情報].〔氏名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String oncJgiName;
//
//	/**
//	 * ONC-職種名（Ref[従業員情報].〔職種名〕）<br>
//	 * MR-施設による紐付け
//	 */
//	private String oncShokushuName;
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * 担当者のリスト
	 */
	private List<JgiMst> dispJgi;
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 施設コードを取得する。
	 *
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 配分除外品目のリストを取得する。
	 *
	 * @return 配分除外品目のリスト
	 */
	public List<ExceptProd> getExceptProd() {
		return exceptProd;
	}

	/**
	 * 配分除外品目のリストを設定する。
	 *
	 * @param prodCode 配分除外品目のリスト
	 */
	public void setExceptProd(List<ExceptProd> exceptProd) {
		this.exceptProd = exceptProd;
	}

	/**
	 * 試算除外フラグを取得する。
	 *
	 * @return 試算除外フラグ
	 */
	public Boolean getEstimationFlg() {
		return estimationFlg;
	}

	/**
	 * 試算除外フラグを設定する。
	 *
	 * @param estimationFlg 試算除外フラグ
	 */
	public void setEstimationFlg(Boolean estimationFlg) {
		this.estimationFlg = estimationFlg;
	}

	/**
	 * 配分除外フラグを取得する。
	 *
	 * @return 配分除外フラグ
	 */
	public Boolean getExceptFlg() {
		return exceptFlg;
	}

	/**
	 * 配分除外フラグを設定する。
	 *
	 * @param exceptFlg 配分除外フラグ
	 */
	public void setExceptFlg(Boolean exceptFlg) {
		this.exceptFlg = exceptFlg;
	}

	/**
	 * 登録区分を取得する。
	 *
	 * @return 登録区分
	 */
	public RegistType getRegistType() {
		return registType;
	}

	/**
	 * 登録区分を設定する。
	 *
	 * @param registType 登録区分
	 */
	public void setRegistType(RegistType registType) {
		this.registType = registType;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 *
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 氏名を取得する。
	 *
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 *
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 施設分類を取得する。
	 *
	 * @return 施設分類
	 */
	public InsClass getInsClass() {
		return insClass;
	}

	/**
	 * 施設分類を設定する。
	 *
	 * @param insClass 施設分類
	 */
	public void setInsClass(InsClass insClass) {
		this.insClass = insClass;
	}

	/**
	 * サブコード分類を取得する。
	 *
	 * @return サブコード分類
	 */
	public OldInsrFlg getOldInsrFlg() {
		return oldInsrFlg;
	}

	/**
	 * サブコード分類を設定する。
	 *
	 * @param oldInsrFlg サブコード分類
	 */
	public void setOldInsrFlg(OldInsrFlg oldInsrFlg) {
		this.oldInsrFlg = oldInsrFlg;
	}

	/**
	 * 施設略式漢字名を取得する。
	 *
	 * @return 施設略式漢字名
	 */
	public String getInsAbbrName() {
		return insAbbrName;
	}

	/**
	 * 施設略式漢字名を設定する。
	 *
	 * @param insAbbrName 施設略式漢字名
	 */
	public void setInsAbbrName(String insAbbrName) {
		this.insAbbrName = insAbbrName;
	}

	/**
	 * 対象区分を取得する。
	 *
	 * @return 対象区分
	 */
	public HoInsType getHoInsType() {
		return hoInsType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param hoInsType 対象区分
	 */
	public void setHoInsType(HoInsType hoInsType) {
		this.hoInsType = hoInsType;
	}

// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//	/**
//	 * （COM領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
//	 *
//	 * @return 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public String getComJgiName() {
//		return comJgiName;
//	}
//
//	/**
//	 * （COM領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
//	 *
//	 * @param comJgiName 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public void setComJgiName(String comJgiName) {
//		this.comJgiName = comJgiName;
//	}
//
//	/**
//	 * （COM領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
//	 *
//	 * @return 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public String getComShokushuName() {
//		return comShokushuName;
//	}
//
//	/**
//	 * （COM領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
//	 *
//	 * @param comShokushuName 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public void setComShokushuName(String comShokushuName) {
//		this.comShokushuName = comShokushuName;
//	}
//
//	/**
//	 * （CVM領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
//	 *
//	 * @return 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public String getCvmJgiName() {
//		return cvmJgiName;
//	}
//
//	/**
//	 * （CVM領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
//	 *
//	 * @param cvmJgiName 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public void setCvmJgiName(String cvmJgiName) {
//		this.cvmJgiName = cvmJgiName;
//	}
//
//	/**
//	 * （CVM領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
//	 *
//	 * @return 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public String getCvmShokushuName() {
//		return cvmShokushuName;
//	}
//
//	/**
//	 * （CVM領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
//	 *
//	 * @param cvmShokushuName 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public void setCvmShokushuName(String cvmShokushuName) {
//		this.cvmShokushuName = cvmShokushuName;
//	}
//
//	/**
//	 * （RS領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
//	 *
//	 * @return 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public String getRsJgiName() {
//		return rsJgiName;
//	}
//
//	/**
//	 * （RS領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
//	 *
//	 * @param rsJgiName 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public void setRsJgiName(String rsJgiName) {
//		this.rsJgiName = rsJgiName;
//	}
//
//	/**
//	 * （RS領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
//	 *
//	 * @return 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public String getRsShokushuName() {
//		return rsShokushuName;
//	}
//
//	/**
//	 * （RS領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
//	 *
//	 * @param rsShokushuName 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public void setRsShokushuName(String rsShokushuName) {
//		this.rsShokushuName = rsShokushuName;
//	}
//
//	/**
//	 * （ONC領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
//	 *
//	 * @return 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public String getOncJgiName() {
//		return oncJgiName;
//	}
//
//	/**
//	 * （ONC領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
//	 *
//	 * @param oncJgiName 氏名（Ref[従業員情報].〔氏名〕）
//	 */
//	public void setOncJgiName(String oncJgiName) {
//		this.oncJgiName = oncJgiName;
//	}
//
//	/**
//	 * （ONC領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
//	 *
//	 * @return 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public String getOncShokushuName() {
//		return oncShokushuName;
//	}
//
//	/**
//	 * （ONC領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
//	 *
//	 * @param oncShokushuName 職種名（Ref[従業員情報].〔職種名〕）
//	 */
//	public void setOncShokushuName(String oncShokushuName) {
//		this.oncShokushuName = oncShokushuName;
//	}
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * 担当者のリストを取得する。
	 *
	 * @return 担当者のリスト
	 */
	public List<JgiMst> getDispJgi() {
		return dispJgi;
	}

	/**
	 * 担当者のリストを設定する。
	 *
	 * @param dispJgi 担当者のリスト
	 */
	public void setDispJgi(List<JgiMst> dispJgi) {
		this.dispJgi = dispJgi;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//	/**
//	 * 表示用担当者名を取得する
//	 * @return dispJgiName
//	 */
//	public String getDispJgiName() {
//
//		StringBuilder sb = new StringBuilder();
//
//		if(StringUtils.isNotEmpty(cvmJgiName)){
//			if(sb.length() > 0){
//				sb.append("、");
//			}
//			sb.append(cvmJgiName).append("（").append(cvmShokushuName).append("）");
//		}
//		if(StringUtils.isNotEmpty(rsJgiName)){
//			if(sb.length() > 0){
//				sb.append("、");
//			}
//			sb.append(rsJgiName).append("（").append(rsShokushuName).append("）");
//		}
//		if(StringUtils.isNotEmpty(oncJgiName)){
//			if(sb.length() > 0){
//				sb.append("、");
//			}
//			sb.append(oncJgiName).append("（").append(oncShokushuName).append("）");
//		}
//
//		return sb.toString();
//	}
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	@Override
	public int compareTo(ExceptDistIns obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ExceptDistIns.class.isAssignableFrom(entry.getClass())) {
			ExceptDistIns obj = (ExceptDistIns) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).append(this.insNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
