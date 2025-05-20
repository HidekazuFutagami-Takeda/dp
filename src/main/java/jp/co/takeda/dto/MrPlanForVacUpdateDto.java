package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用担当者別計画更新用DTO
 * 
 * @author stakeuchi
 */
public class MrPlanForVacUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 計画値(B)
	 */
	private final Long plannedValueB;

	/**
	 * コンストラクタ
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param plannedValueB 計画値(B)
	 */
	public MrPlanForVacUpdateDto(Long seqKey, Date upDate, Long plannedValueB) {
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.plannedValueB = plannedValueB;
	}

	/**
	 * シーケンスキーを取得する。
	 * 
	 * @return seqKey シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
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
	 * 計画値(B)を取得する。
	 * 
	 * @return plannedValueB 計画値(B)
	 */
	public Long getPlannedValueB() {
		return plannedValueB;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
