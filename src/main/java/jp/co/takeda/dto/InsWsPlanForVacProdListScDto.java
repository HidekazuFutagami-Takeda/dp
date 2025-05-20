package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用計画対象品目の検索用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacProdListScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(エリア特約店G)
	 */
	private final String sosCd;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード(エリア特約店G)
	 * @param jgiNo 従業員番号
	 * @param category
	 */
	public InsWsPlanForVacProdListScDto(String sosCd, Integer jgiNo) {
		this.sosCd = sosCd;
		this.jgiNo = jgiNo;
	}

	/**
	 * 組織コード(エリア特約店G)を取得する。
	 * 
	 * @return sosCd4 組織コード(エリア特約店G)
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 従業員コードを取得する。
	 * 
	 * @return jgiNo 従業員コード
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
