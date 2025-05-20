package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.bean.DpModel;


/**
 * 一括確定エラー情報を表すモデルクラス
 *
 */
public class DpsKakuteiErrMsg extends DpModel<DpsKakuteiErrMsg> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	*シーケンスキー
	*/
	private Long seqKey;

	/**
	*組織コード
	*/
	private String sosCd;

	/**
	*品目固定コード
	*/
	private String prodCode;

	/**
	*メッセージ
	*/
	private String message;

	/**
	*登録者
	*/
	private Integer isJgiNo;

	/**
	*登録者名
	*/
	private String isJgiName;

	/**
	*登録日
	*/
	private Date isDate;

	/**
	*更新者
	*/
	private Integer upJgiNo;

	/**
	*更新者名
	*/
	private String upJgiName;

	/**
	*更新日
	*/
	private Date upDate;

	/**
	*施設コード
	*/
	private String insNo;

	/**
	*施設名
	*/
	private String insName;

	/**
	*特約店コード
	*/
	private String tytenCd;

	/**
	*特約名
	*/
	private String tytenMei;

	/**
	*品目名
	*/
	private String prodName;

	/**
	*メッセージキー
	*/
	private MessageKey messageKey;


	/**
	*リュージョンコード
	*/
	private String sosCd2;

	/**
	*リュージョン名
	*/
	private String bumonRyakuName2;

	/**
	*エリアコード
	*/
	private String sosCd3;

	/**
	*エリア名
	*/
	private String bumonRyakuName3;

	/**
	*従業員名前
	*/
	private String jgiName;

	/**
	*従業員番号
	*/
	private Integer jgiNo;

	/**
	*エラーメッセージ
	*/
	private String errMessage;

	/**
	*品目メッセージ
	*/
	private String prodMessage;


	//---------------------
	// Getter & Setter
	// --------------------

	/**
	*シーケンスキーを取得する
	*/
	public Long getSeqKey() {
	return seqKey;
	}

	/**
	*シーケンスキーを設定する
	*/
	public void setSeqKey(Long seqKey) {
	this.seqKey = seqKey;
	}

	/**
	*組織コードを取得する
	*/
	public String getSosCd() {
	return sosCd;
	}

	/**
	*組織コードを設定する
	*/
	public void setSosCd(String sosCd) {
	this.sosCd = sosCd;
	}

	/**
	*品目固定コードを取得する
	*/
	public String getProdCode() {
	return prodCode;
	}

	/**
	*品目固定コードを設定する
	*/
	public void setProdCode(String prodCode) {
	this.prodCode = prodCode;
	}

	/**
	*メッセージを取得する
	*/
	public String getMessage() {
	return message;
	}

	/**
	*メッセージを設定する
	*/
	public void setMessage(String message) {
	this.message = message;
	}

	/**
	*登録者を取得する
	*/
	public Integer getIsJgiNo() {
	return isJgiNo;
	}

	/**
	*登録者を設定する
	*/
	public void setIsJgiNo(Integer isJgiNo) {
	this.isJgiNo = isJgiNo;
	}

	/**
	*登録者名を取得する
	*/
	public String getIsJgiName() {
	return isJgiName;
	}

	/**
	*登録者名を設定する
	*/
	public void setIsJgiName(String isJgiName) {
	this.isJgiName = isJgiName;
	}

	/**
	*登録日を取得する
	*/
	public Date getIsDate() {
	return isDate;
	}

	/**
	*登録日を設定する
	*/
	public void setIsDate(Date isDate) {
	this.isDate = isDate;
	}

	/**
	*更新者を取得する
	*/
	public Integer getUpJgiNo() {
	return upJgiNo;
	}

	/**
	*更新者を設定する
	*/
	public void setUpJgiNo(Integer upJgiNo) {
	this.upJgiNo = upJgiNo;
	}

	/**
	*更新者名を取得する
	*/
	public String getUpJgiName() {
	return upJgiName;
	}

	/**
	*更新者名を設定する
	*/
	public void setUpJgiName(String upJgiName) {
	this.upJgiName = upJgiName;
	}

	/**
	*更新日を取得する
	*/
	public Date getUpDate() {
	return upDate;
	}

	/**
	*更新日を設定する
	*/
	public void setUpDate(Date upDate) {
	this.upDate = upDate;
	}

	/**
	 * 施設コードを取得する。
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 施設名を取得する。
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 施設名を設定する。
	 */
	public void setInsName(String insName) {
		this.insName = insName;
	}

	/**
	*特約店コードを取得する。
	*/
	public String getTytenCd() {
		return tytenCd;
	}

	/**
	*特約店コードを設定する。
	*/
	public void  setTytenCd(String tytenCd) {
		this.tytenCd = tytenCd;
	}

	/**
	*特約名を取得する。
	*/
	public String getTytenMei() {
		return tytenMei;
	}

	/**
	*特約名を設定する。
	*/
	public void setTytenMei(String tytenMei) {
		this.tytenMei = tytenMei;
	}

	/**
	*品目名を取得する
	*/
	public String getProdName() {
	return prodName;
	}

	/**
	*品目名を設定する
	*/
	public void setProdName(String prodName) {
	this.prodName = prodName;
	}

	/**
	*メッセージキーを取得する
	*/
	public MessageKey getMessageKey() {
	return messageKey;
	}

	/**
	*メッセージキーを設定する
	*/
	public void setMessageKey(MessageKey messageKey) {
	this.messageKey = messageKey;
	}

	/**
	*リュージョンコードを取得する
	*/
	public String getSosCd2() {
	return sosCd2;
	}

	/**
	*リュージョンコードを設定する
	*/
	public void setSosCd2(String sosCd2) {
	this.sosCd2 = sosCd2;
	}

	/**
	*リュージョン名を取得する
	*/
	public String getBumonRyakuName2() {
	return bumonRyakuName2;
	}

	/**
	*リュージョン名を設定する
	*/
	public void setBumonRyakuName2(String bumonRyakuName2) {
		this.bumonRyakuName2 = bumonRyakuName2;
	}

	/**
	*エリアコードを取得する
	*/
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	*エリアコードを設定する
	*/
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	*エリア名を取得する
	*/
	public String getBumonRyakuName3() {
		return bumonRyakuName3;
	}

	/**
	*エリア名を設定する
	*/
	public void setBumonRyakuName3(String bumonRyakuName3) {
		this.bumonRyakuName3 = bumonRyakuName3;
	}

	/**
	*従業員番号を取得する
	*/
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	*従業員番号を設定する
	*/
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	*従業員名前を取得する
	*/
	public String getJgiName() {
		return jgiName;
	}

	/**
	*従業員名前を設定する
	*/
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	*エラーメッセージを取得する
	*/
	public String getErrMessage() {
		return errMessage;
	}

	/**
	*エラーメッセージを設定する
	*/
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	/**
	*品目メッセージを取得する
	*/
	public String getProdMessage() {
		return prodMessage;
	}

	/**
	 *品目メッセージを設定する
	 */
	public void setProdMessage(String prodMessage) {
		this.prodMessage = prodMessage;
	}


	@Override
	public int compareTo(DpsKakuteiErrMsg obj) {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}




}
