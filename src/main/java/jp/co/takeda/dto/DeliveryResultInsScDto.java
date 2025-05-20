package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * 過去実績参照画面(施設特約店別計画)の検索条件(Search Condition)を表すDTO
 * 
 * @author siwamoto
 */
public class DeliveryResultInsScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 従業員コード
	 */
	public final Integer jgiNo;

	/**
	 * 品目固定コード
	 */
	public final String prodCode;

	/**
	 * 施設コード
	 */
	public final String insNo;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員コード
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 */
	public DeliveryResultInsScDto(Integer jgiNo, String prodCode, String insNo) {
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insNo = insNo;
	}

	//---------------------
	// Getter
	// --------------------

	/**
	 * 従業員コードを取得する。
	 * 
	 * @return 従業員コード
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 施設コードを取得する。
	 * 
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
