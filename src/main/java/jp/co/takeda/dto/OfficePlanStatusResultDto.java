package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 営業所計画ステータスの検索結果用DTO
 * 
 * @author stakeuchi
 */
public class OfficePlanStatusResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 下書フラグ TRUE=下書 FALSE=下書でない
	 */
	private final Boolean isDraft;

	/**
	 * 確定フラグ TRUE=確定 FALSE=確定でない
	 */
	private final Boolean isCompleted;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * コンストラクタ
	 * 
	 * @param isDraft 下書フラグ TRUE=下書 FALSE=下書でない
	 * @param isCompleted 確定フラグ TRUE=確定 FALSE=確定でない
	 * @param upDate 最終更新日時
	 */
	public OfficePlanStatusResultDto(Boolean isDraft, Boolean isCompleted, Date upDate) {
		this.isDraft = isDraft;
		this.isCompleted = isCompleted;
		this.upDate = upDate;
	}

	/**
	 * 下書フラグを取得する。
	 * 
	 * @return isDraft 下書フラグ
	 */
	public Boolean getIsDraft() {
		return isDraft;
	}

	/**
	 * 確定フラグを取得する。
	 * 
	 * @return isCompleted 確定フラグ
	 */
	public Boolean getIsCompleted() {
		return isCompleted;
	}

	/**
	 * 最終更新日時を取得する。
	 * 
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
