package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画立案ステータス更新用DTO
 * 
 * @author stakeuchi
 */
public class MrPlanStatusUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 担当者別計画ステータス シーケンスキー
	 */
	private final Long statusSeqKey;

	/**
	 * 担当者別計画ステータス 最終更新日時
	 */
	private final Date statusUpDate;

	/**
	 * コンストラクタ
	 * 
	 * @param statusSeqKey 担当者別計画ステータス シーケンスキー
	 * @param statusUpDate 担当者別計画ステータス 最終更新日時
	 */
	public MrPlanStatusUpdateDto(Long statusSeqKey, Date statusUpDate) {
		this.statusSeqKey = statusSeqKey;
		this.statusUpDate = statusUpDate;
	}

	/**
	 * 担当者別計画ステータス シーケンスキーを取得する。
	 * 
	 * @return statusSeqKey 担当者別計画ステータス シーケンスキー
	 */
	public Long getStatusSeqKey() {
		return statusSeqKey;
	}

	/**
	 * 担当者別計画ステータス 最終更新日時を取得する。
	 * 
	 * @return statusUpDate 担当者別計画ステータス 最終更新日時
	 */
	public Date getStatusUpDate() {
		return statusUpDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
