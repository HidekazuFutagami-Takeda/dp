package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 過去実績参照画面（担当者別計画）の検索条件(Search Condition)を表すDTO
 * 
 * @author siwamoto
 */
public class DeliveryResultVacMrScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 組織コード（特約店部）
	 */
	private final String sosCd2;

	/**
	 * 組織コード（特約店）
	 */
	private final String sosCd3;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode
	 * @param sosCd2
	 * @param sosCd3
	 * @param insType
	 */
	public DeliveryResultVacMrScDto(String prodCode, String sosCd2, String sosCd3) {
		super();
		this.prodCode = prodCode;
		this.sosCd2 = sosCd2;
		this.sosCd3 = sosCd3;
	}

	/**
	 * prodCodeを取得する。
	 * 
	 * @return prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * sosCd2を取得する。
	 * 
	 * @return sosCd2
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * sosCd3を取得する。
	 * 
	 * @return sosCd3
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
