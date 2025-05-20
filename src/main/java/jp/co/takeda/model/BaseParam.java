package jp.co.takeda.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.model.div.RefPeriod;

/**
 * 試算配分共通パラメータを表すクラス
 * 
 * @author tkawabata
 */
public class BaseParam implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 試算/配分品目コード
	 */
	private String refProdCode;

	/**
	 * 参照期間(FROM)
	 */
	private RefPeriod refFrom;

	/**
	 * 参照期間(TO)
	 */
	private RefPeriod refTo;

	/**
	 * 実績参照品目コード1
	 */
	private String resultRefProdCode1;

	/**
	 * 実績参照品目名称1（Ref[ 計画対象品目].〔品目名称〕）
	 */
	private String resultRefProdName1;

	/**
	 * 実績参照品目製品区分1（Ref[ 計画対象品目].〔製品区分〕）
	 */
	private String resultRefProdType1;

	/**
	 * 実績参照品目コード2
	 */
	private String resultRefProdCode2;

	/**
	 * 実績参照品目名称2（Ref[ 計画対象品目].〔品目名称〕）
	 */
	private String resultRefProdName2;

	/**
	 * 実績参照品目製品区分2（Ref[ 計画対象品目].〔製品区分〕）
	 */
	private String resultRefProdType2;

	/**
	 * 実績参照品目コード3
	 */
	private String resultRefProdCode3;

	/**
	 * 実績参照品目名称3（Ref[ 計画対象品目].〔品目名称〕）
	 */
	private String resultRefProdName3;

	/**
	 * 実績参照品目製品区分3（Ref[ 計画対象品目].〔製品区分〕）
	 */
	private String resultRefProdType3;

	/**
	 * 実績参照品目コード4
	 */
	private String resultRefProdCode4;

	/**
	 * 実績参照品目名称4（Ref[ 計画対象品目].〔品目名称〕）
	 */
	private String resultRefProdName4;

	/**
	 * 実績参照品目製品区分4（Ref[ 計画対象品目].〔製品区分〕）
	 */
	private String resultRefProdType4;

	/**
	 * 試算/配分品目　品目共通情報
	 */
	private ProdInfo refProdInfo;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 参照期間(FROM)を取得する。
	 * 
	 * @return 参照期間(FROM)
	 */
	public RefPeriod getRefFrom() {
		return refFrom;
	}

	/**
	 * 参照期間(FROM)を設定する。
	 * 
	 * @param refFrom 参照期間(FROM)
	 */
	public void setRefFrom(RefPeriod refFrom) {
		this.refFrom = refFrom;
	}

	/**
	 * 参照期間(TO)を取得する。
	 * 
	 * @return 参照期間(TO)
	 */
	public RefPeriod getRefTo() {
		return refTo;
	}

	/**
	 * 参照期間(TO)を設定する。
	 * 
	 * @param refTo 参照期間(TO)
	 */
	public void setRefTo(RefPeriod refTo) {
		this.refTo = refTo;
	}

	/**
	 * 実績参照品目コード1を取得する。
	 * 
	 * @return 実績参照品目コード1
	 */
	public String getResultRefProdCode1() {
		return resultRefProdCode1;
	}

	/**
	 * 実績参照品目コード1を設定する。
	 * 
	 * @param resultRefProdCode1 実績参照品目コード1
	 */
	public void setResultRefProdCode1(String resultRefProdCode1) {
		this.resultRefProdCode1 = resultRefProdCode1;
	}

	/**
	 * 実績参照品目名称1を取得する。
	 * 
	 * @return 実績参照品目名称1
	 */
	public String getResultRefProdName1() {
		return resultRefProdName1;
	}

	/**
	 * 実績参照品目名称1を設定する。
	 * 
	 * @param resultRefProdName1 実績参照品目名称1
	 */
	public void setResultRefProdName1(String resultRefProdName1) {
		this.resultRefProdName1 = resultRefProdName1;
	}

	/**
	 * 実績参照品目製品区分1を取得する。
	 * 
	 * @return 実績参照品目製品区分1
	 */
	public String getResultRefProdType1() {
		return resultRefProdType1;
	}

	/**
	 * 実績参照品目製品区分1を設定する。
	 * 
	 * @param resultRefProdType1 実績参照品目製品区分1
	 */
	public void setResultRefProdType1(String resultRefProdType1) {
		this.resultRefProdType1 = resultRefProdType1;
	}

	/**
	 * 実績参照品目コード2を取得する。
	 * 
	 * @return 実績参照品目コード2
	 */
	public String getResultRefProdCode2() {
		return resultRefProdCode2;
	}

	/**
	 * 実績参照品目コード2を設定する。
	 * 
	 * @param resultRefProdCode2 実績参照品目コード2
	 */
	public void setResultRefProdCode2(String resultRefProdCode2) {
		this.resultRefProdCode2 = resultRefProdCode2;
	}

	/**
	 * 実績参照品目名称2を取得する。
	 * 
	 * @return 実績参照品目名称2
	 */
	public String getResultRefProdName2() {
		return resultRefProdName2;
	}

	/**
	 * 実績参照品目名称2を設定する。
	 * 
	 * @param resultRefProdName2 実績参照品目名称2
	 */
	public void setResultRefProdName2(String resultRefProdName2) {
		this.resultRefProdName2 = resultRefProdName2;
	}

	/**
	 * 実績参照品目製品区分2を取得する。
	 * 
	 * @return 実績参照品目製品区分2
	 */
	public String getResultRefProdType2() {
		return resultRefProdType2;
	}

	/**
	 * 実績参照品目製品区分2を設定する。
	 * 
	 * @param resultRefProdType2 実績参照品目製品区分2
	 */
	public void setResultRefProdType2(String resultRefProdType2) {
		this.resultRefProdType2 = resultRefProdType2;
	}

	/**
	 * 実績参照品目コード3を取得する。
	 * 
	 * @return 実績参照品目コード3
	 */
	public String getResultRefProdCode3() {
		return resultRefProdCode3;
	}

	/**
	 * 実績参照品目コード3を設定する。
	 * 
	 * @param resultRefProdCode3 実績参照品目コード3
	 */
	public void setResultRefProdCode3(String resultRefProdCode3) {
		this.resultRefProdCode3 = resultRefProdCode3;
	}

	/**
	 * 実績参照品目名称3を取得する。
	 * 
	 * @return 実績参照品目名称3
	 */
	public String getResultRefProdName3() {
		return resultRefProdName3;
	}

	/**
	 * 実績参照品目名称3を設定する。
	 * 
	 * @param resultRefProdName3 実績参照品目名称3
	 */
	public void setResultRefProdName3(String resultRefProdName3) {
		this.resultRefProdName3 = resultRefProdName3;
	}

	/**
	 * 実績参照品目製品区分3を取得する。
	 * 
	 * @return 実績参照品目製品区分3
	 */
	public String getResultRefProdType3() {
		return resultRefProdType3;
	}

	/**
	 * 実績参照品目製品区分3を設定する。
	 * 
	 * @param resultRefProdType3 実績参照品目製品区分3
	 */
	public void setResultRefProdType3(String resultRefProdType3) {
		this.resultRefProdType3 = resultRefProdType3;
	}

	/**
	 * 実績参照品目コード4を取得する。
	 * 
	 * @return 実績参照品目コード4
	 */
	public String getResultRefProdCode4() {
		return resultRefProdCode4;
	}

	/**
	 * 実績参照品目コード4を設定する。
	 * 
	 * @param resultRefProdCode4 実績参照品目コード4
	 */
	public void setResultRefProdCode4(String resultRefProdCode4) {
		this.resultRefProdCode4 = resultRefProdCode4;
	}

	/**
	 * 実績参照品目名称4を取得する。
	 * 
	 * @return 実績参照品目名称4
	 */
	public String getResultRefProdName4() {
		return resultRefProdName4;
	}

	/**
	 * 実績参照品目名称4を設定する。
	 * 
	 * @param resultRefProdName4 実績参照品目名称4
	 */
	public void setResultRefProdName4(String resultRefProdName4) {
		this.resultRefProdName4 = resultRefProdName4;
	}

	/**
	 * 実績参照品目製品区分4を取得する。
	 * 
	 * @return 実績参照品目製品区分4
	 */
	public String getResultRefProdType4() {
		return resultRefProdType4;
	}

	/**
	 * 実績参照品目製品区分4を設定する。
	 * 
	 * @param resultRefProdType4 実績参照品目製品区分4
	 */
	public void setResultRefProdType4(String resultRefProdType4) {
		this.resultRefProdType4 = resultRefProdType4;
	}

	/**
	 * 試算/配分品目　品目共通情報を取得する。
	 * 
	 * @return 試算/配分品目　品目共通情報
	 */
	public ProdInfo getRefProdInfo() {
		return refProdInfo;
	}

	/**
	 * 試算/配分品目　品目共通情報を設定する。
	 * 
	 * @param refProdInfo 試算/配分品目　品目共通情報
	 */
	public void setRefProdInfo(ProdInfo refProdInfo) {
		this.refProdInfo = refProdInfo;
	}

	/**
	 * 試算/配分品目コードを取得する。
	 * 
	 * @return 試算/配分品目コード
	 */
	public String getRefProdCode() {
		return refProdCode;
	}

	/**
	 * 試算/配分品目コードを設定する。
	 * 
	 * @param refProdCode 試算/配分品目コード
	 */
	public void setRefProdCode(String refProdCode) {
		this.refProdCode = refProdCode;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
