package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用計画対象品目の検索用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacJgiListScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 組織コード(特約店部)
	 */
	private final String sosCd2;

	/**
	 * 組織コード(エリア特約店G)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd2 組織コード(特約店部)
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 */
	public InsWsPlanForVacJgiListScDto(String sosCd2, String sosCd3, String sosCd4, String prodCode) {
		this.sosCd2 = sosCd2;
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.prodCode = prodCode;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 組織コード(特約店部)を取得する。
	 * 
	 * @return sosCd2 組織コード(特約店部)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(エリア特約店G)を取得する。
	 * 
	 * @return sosCd3 組織コード(エリア特約店G)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 * 
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
