package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
// mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
//import jp.co.takeda.dto.SupInfoDto;
import jp.co.takeda.dto.DpsKakuteiErrMsgDto;
// mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps940C00(補足情報)のフォームクラス
 *
 */
public class Dps940C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * DPS940C00F01_DATA_R 補足情報
	 */
// mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
//	public static final BoxKey DPS940C00F01_DATA_R = new BoxKeyPerClassImpl(Dps940C00Form.class, SupInfoDto.class);
	public static final BoxKey DPS940C00F01_DATA_R = new BoxKeyPerClassImpl(Dps940C00Form.class, DpsKakuteiErrMsgDto.class);
// mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	// -------------------------------
	// field
	// -------------------------------
// mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

//	/**
//	 * 組織コード
//	 */
//	private String sosCd;

//	/**
//	 * カテゴリ
//	 */
//	private String category;

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
	*従業員番号
	*/
	private Integer jgiNo;

	/**
	*従業員名前
	*/
	private String jgiName;

	/**
	*エラーメッセージ
	*/
	private String errMessage;

	/**
	*品目メッセージ
	*/
	private String prodMessage;

	// -------------------------------
	// getter/setter
	// -------------------------------
//	/**
//	 * 組織コードを取得する。
//	 *
//	 * @return 組織コード
//	 */
//	public String getSosCd() {
//		return sosCd;
//	}
//
//	/**
//	 * 組織コードを設定する。
//	 *
//	 * @param sosCd
//	 */
//	public void setSosCd(String sosCd) {
//		this.sosCd = sosCd;
//	}
//
//	/**
//	 * カテゴリを取得する。
//	 *
//	 * @return カテゴリ
//	 */
//	public String getCategory() {
//		return category;
//	}
//
//	/**
//	 * カテゴリを設定する。
//	 *
//	 * @param category カテゴリ
//	 */
//	public void setCategory(String category) {
//		this.category = category;
//	}

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

// mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理のため、不要
	}
}
