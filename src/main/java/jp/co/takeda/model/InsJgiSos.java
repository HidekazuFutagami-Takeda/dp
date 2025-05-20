package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.a.bean.Model;

/**
 * 管理時の施設別計画を表すモデルクラス
 *
 * @author khashimoto
 */
public class InsJgiSos extends Model<InsJgiSos> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	*施設固定コード
	*/
	private String insNo;

	/**
	*従業員番号
	*/
	private String mrNo;

	/**
	*品目固定コード
	*/
	private String prodCode;

	/**
	*MR種類
	*/
	private String mrCat;

	/**
	*主副担当区分
	*/
	private String mainMr;

	/**
	*登録者
	*/
	private String isJgiNo;

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
	private String upJgiNo;

	/**
	*更新者名
	*/
	private String upJgiName;

	/**
	*更新日
	*/
	private Date upDate;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	*施設固定コードを取得する
	*/
	public String getInsNo() {
	return insNo;
	}

	/**
	*施設固定コードを設定する
	*/
	public void setInsNo() {
	this.insNo = insNo;
	}

	/**
	*従業員番号を取得する
	*/
	public String getMrNo() {
	return mrNo;
	}

	/**
	*従業員番号を設定する
	*/
	public void setMrNo() {
	this.mrNo = mrNo;
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
	public void setProdCode() {
	this.prodCode = prodCode;
	}

	/**
	*MR種類を取得する
	*/
	public String getMrCat() {
	return mrCat;
	}

	/**
	*MR種類を設定する
	*/
	public void setMrCat() {
	this.mrCat = mrCat;
	}

	/**
	*主副担当区分を取得する
	*/
	public String getMainMr() {
	return mainMr;
	}

	/**
	*主副担当区分を設定する
	*/
	public void setMainMr() {
	this.mainMr = mainMr;
	}

	/**
	*登録者を取得する
	*/
	public String getIsJgiNo() {
	return isJgiNo;
	}

	/**
	*登録者を設定する
	*/
	public void setIsJgiNo() {
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
	public void setIsJgiName() {
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
	public void setIsDate() {
	this.isDate = isDate;
	}

	/**
	*更新者を取得する
	*/
	public String getUpJgiNo() {
	return upJgiNo;
	}

	/**
	*更新者を設定する
	*/
	public void setUpJgiNo() {
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
	public void setUpJgiName() {
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
	public void setUpDate() {
	this.upDate = upDate;
	}

	@Override
	public int compareTo(InsJgiSos obj) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public int hashCode() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
