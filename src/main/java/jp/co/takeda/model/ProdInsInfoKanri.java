package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.ProdInsInfoDistKbn;
import jp.co.takeda.model.div.ProdInsInfoErrKbn;
import jp.co.takeda.model.div.ProdInsInfoImpInsKbn;
import jp.co.takeda.model.div.ProdInsInfoScanDispKbn;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 品目施設情報管理を表すモデルクラス
 *
 * @author tkawabata
 */
public class ProdInsInfoKanri extends DpModel<ProdInsInfoKanri> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 品目シーケンス番号
	 */
	private String prodSeq;

	/**
	 * 施設情報名称
	 */
	private String insInfoName;

	/**
	 * 表示文字カラーコード
	 */
	private String dispFontColCd;

	/**
	 * エラー区分
	 */
	private ProdInsInfoErrKbn prodInsInfoErrKbn;

	/**
	 * 画面表示区分
	 */
	private ProdInsInfoScanDispKbn prodInsInfoScanDispKbn;

	/**
	 * 配分対象区分
	 */
	private ProdInsInfoDistKbn prodInsInfoDistKbn;

	/**
	 * 重点先区分
	 */
	private ProdInsInfoImpInsKbn prodInsInfoImpInsKbn;

	/**
	 * 対象施設抽出プログラムID
	 */
	private String execPgId;

	/**
	 * 備考
	 */
	private String remarks;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 品目シーケンス番号を取得する。
	 *
	 * @return 品目シーケンス番号
	 */
	public String getProdSeq() {
		return prodSeq;
	}

	/**
	 * 品目シーケンス番号を設定する。
	 *
	 * @param prodSeq 品目シーケンス番号
	 */
	public void setProdSeq(String prodSeq) {
		this.prodSeq = prodSeq;
	}

	/**
	 * 施設情報名称を取得する。
	 *
	 * @return 施設情報名称
	 */
	public String getInsInfoName() {
		return insInfoName;
	}

	/**
	 * 施設情報名称を設定する。
	 *
	 * @param insInfoName 施設情報名称
	 */
	public void setInsInfoName(String insInfoName) {
		this.insInfoName = insInfoName;
	}

	/**
	 * 表示文字カラーコードを取得する。
	 *
	 * @return 表示文字カラーコード
	 */
	public String getDispFontColCd() {
		return dispFontColCd;
	}

	/**
	 * 表示文字カラーコードを設定する。
	 *
	 * @param dispFontColCd 表示文字カラーコード
	 */
	public void setDispFontColCd(String dispFontColCd) {
		this.dispFontColCd = dispFontColCd;
	}

	/**
	 * エラー区分を取得する。
	 *
	 * @return エラー区分
	 */
	public ProdInsInfoErrKbn getProdInsInfoErrKbn() {
		return prodInsInfoErrKbn;
	}

	/**
	 * エラー区分を設定する。
	 *
	 * @param prodInsInfoErrKbn エラー区分
	 */
	public void setProdInsInfoErrKbn(ProdInsInfoErrKbn prodInsInfoErrKbn) {
		this.prodInsInfoErrKbn = prodInsInfoErrKbn;
	}

	/**
	 * 画面表示区分を取得する。
	 *
	 * @return 画面表示区分
	 */
	public ProdInsInfoScanDispKbn getProdInsInfoScanDispKbn() {
		return prodInsInfoScanDispKbn;
	}

	/**
	 * 画面表示区分を設定する。
	 *
	 * @param prodInsInfoScanDispKbn 画面表示区分
	 */
	public void setProdInsInfoScanDispKbn(ProdInsInfoScanDispKbn prodInsInfoScanDispKbn) {
		this.prodInsInfoScanDispKbn = prodInsInfoScanDispKbn;
	}

	/**
	 * 配分対象区分を取得する。
	 *
	 * @return 配分対象区分
	 */
	public ProdInsInfoDistKbn getProdInsInfoDistKbn() {
		return prodInsInfoDistKbn;
	}

	/**
	 * 配分対象区分を設定する。
	 *
	 * @param prodInsInfoDistKbn 配分対象区分
	 */
	public void setProdInsInfoDistKbn(ProdInsInfoDistKbn prodInsInfoDistKbn) {
		this.prodInsInfoDistKbn = prodInsInfoDistKbn;
	}

	/**
	 * 対象施設抽出プログラムIDを取得する。
	 *
	 * @return 対象施設抽出プログラムID
	 */
	public ProdInsInfoImpInsKbn getProdInsInfoImpInsKbn() {
		return prodInsInfoImpInsKbn;
	}

	/**
	 * 対象施設抽出プログラムIDを設定する。
	 *
	 * @param prodInsInfoImpInsKbn 対象施設抽出プログラムID
	 */
	public void setProdInsInfoImpInsKbn(ProdInsInfoImpInsKbn prodInsInfoImpInsKbn) {
		this.prodInsInfoImpInsKbn = prodInsInfoImpInsKbn;
	}

	/**
	 * 対象施設抽出プログラムIDを取得する。
	 *
	 * @return 対象施設抽出プログラムID
	 */
	public String getExecPgId() {
		return execPgId;
	}

	/**
	 * 対象施設抽出プログラムIDを設定する。
	 *
	 * @param execPgId 対象施設抽出プログラムID
	 */
	public void setExecPgId(String execPgId) {
		this.execPgId = execPgId;
	}

	/**
	 * 備考を取得する。
	 *
	 * @return 備考
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 備考を設定する。
	 *
	 * @param remarks 備考
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public int compareTo(ProdInsInfoKanri obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).append(this.prodSeq, obj.prodSeq).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ProdInsInfoKanri.class.isAssignableFrom(entry.getClass())) {
			ProdInsInfoKanri obj = (ProdInsInfoKanri) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).append(this.prodSeq, obj.prodSeq).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).append(this.prodSeq).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
