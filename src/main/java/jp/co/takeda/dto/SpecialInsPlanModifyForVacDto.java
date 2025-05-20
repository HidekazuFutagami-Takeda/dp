package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特定施設個別計画（担当者立案）の登録用DTO
 * 
 * @author siwamoto
 */
public class SpecialInsPlanModifyForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設を担当する従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 登録対象の施設コード（親子ある場合は親のみ）
	 */
	private final String insNo;

	/**
	 * 特定施設個別計画の最終更新日時
	 */
	private final Date upDate;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 特定施設個別計画を表すDTOクラス
	 */
	private final List<SpecialInsPlanForVacDto> specialInsPlanDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo
	 * @param insNo
	 * @param specialInsPlanDtoList
	 * @param upDate
	 * @param sosCd3
	 */
	public SpecialInsPlanModifyForVacDto(Integer jgiNo, String insNo, List<SpecialInsPlanForVacDto> inputList, Date upDate, String sosCd3) {
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.specialInsPlanDtoList = inputList;
		this.upDate = upDate;
		this.sosCd3 = sosCd3;
	}

	/**
	 * 施設を担当する従業員番号を取得する。
	 * 
	 * @return 施設を担当する従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 登録対象の施設コード（親子ある場合は親のみ）を取得する。
	 * 
	 * @return 登録対象の施設コード（親子ある場合は親のみ）
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 最終更新日時を取得する。
	 * 
	 * @return 最終更新日時
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

	/**
	 * 特定施設個別計画を表すDTOクラスを取得する。
	 * 
	 * @return 特定施設個別計画を表すDTOクラス
	 */
	public List<SpecialInsPlanForVacDto> getSpecialInsPlanDtoList() {
		return specialInsPlanDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
