package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特定施設個別計画の削除用DTO
 * 
 * @author stakeuchi
 */
public class SpecialInsPlanDeleteDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 従業員番号(施設担当者)
	 */
	private final Integer jgiNo;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param upDate 最終更新日時
	 * @param sosCd3 組織コード(営業所)
	 */
	public SpecialInsPlanDeleteDto(String insNo, Integer jgiNo, Date upDate, String sosCd3) {
		this.insNo = insNo;
		this.jgiNo = jgiNo;
		this.upDate = upDate;
		this.sosCd3 = sosCd3;
	}

	/**
	 * 施設コードを取得する。
	 * 
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 従業員番号(施設担当者)を取得する。
	 * 
	 * @return jgiNo 従業員番号(施設担当者)
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 最終更新日時を取得する。
	 * 
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 * 
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
