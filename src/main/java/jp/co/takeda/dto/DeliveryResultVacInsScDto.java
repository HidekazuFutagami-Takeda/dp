package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 過去実績参照画面（施設特約店別計画）の検索条件(Search Condition)を表すDTO
 * 
 * @author siwamoto
 */
public class DeliveryResultVacInsScDto extends DpDto {
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
	 * @param jgiNo
	 * @param prodCode
	 */
	public DeliveryResultVacInsScDto(Integer jgiNo, String prodCode, String insNo) {
		super();
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
	 * @return jgiNo
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 施設コードを取得する。
	 * 
	 * @return insNo
	 */
	public String getInsNo() {
		return insNo;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
