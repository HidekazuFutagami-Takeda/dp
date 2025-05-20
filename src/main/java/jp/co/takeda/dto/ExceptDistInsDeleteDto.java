package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分除外施設の削除用DTO
 * 
 * @author siwamoto
 */
public class ExceptDistInsDeleteDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param upDate 最終更新日時
	 */
	public ExceptDistInsDeleteDto(String insNo, Integer jgiNo, Date upDate) {
		this.insNo = insNo;
		this.jgiNo = jgiNo;
		this.upDate = upDate;
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
	 * 従業員番号を取得する。
	 * 
	 * @return jgiNo 従業員番号
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
