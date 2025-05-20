package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特定施設個別計画の検索結果用DTO
 * 
 * @author stakeuchi
 */
public class SpecialInsPlanForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設名
	 */
	private final String insName;

	/**
	 * 重点先/一般先
	 */
	private final String activityType;

	/**
	 * 施設分類
	 */
	private final String insClass;

	/**
	 * 担当者
	 */
	private final String jgiName;

	/**
	 * 特定施設個別計画立案済
	 */
	private final Boolean isRegOn;

	/**
	 * 最終更新者名
	 */
	private final String upJgiName;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 削除フラグ
	 */
	private final Boolean delFlg;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param insName 施設名
	 * @param activityType 重点先/一般先
	 * @param insClass 施設分類
	 * @param jgiName 担当者
	 * @param isRegOn 特定施設個別計画立案済
	 * @param upJgiName 最終更新者名
	 * @param upDate 最終更新日時
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param delFlg 削除フラグ
	 */
	public SpecialInsPlanForVacResultDto(String insName, String activityType, String insClass, String jgiName, Boolean isRegOn, String upJgiName, Date upDate, String insNo,
		Integer jgiNo, Boolean delFlg) {
		this.insName = insName;
		this.activityType = activityType;
		this.insClass = insClass;
		this.jgiName = jgiName;
		this.isRegOn = isRegOn;
		this.upJgiName = upJgiName;
		this.upDate = upDate;
		this.insNo = insNo;
		this.jgiNo = jgiNo;
		this.delFlg = delFlg;
	}

	/**
	 * 施設名を取得する。
	 * 
	 * @return insName 施設名
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 重点先/一般先を取得する。
	 * 
	 * @return activityType 重点先/一般先
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * 施設分類を取得する。
	 * 
	 * @return insClass 施設分類
	 */
	public String getInsClass() {
		return insClass;
	}

	/**
	 * 担当者を取得する。
	 * 
	 * @return jgiName 担当者
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 特定施設個別計画立案済を取得する。
	 * 
	 * @return isRegOn 特定施設個別計画立案済
	 */
	public Boolean getIsRegOn() {
		return isRegOn;
	}

	/**
	 * 最終更新者名を取得する。
	 * 
	 * @return upJgiName 最終更新者名
	 */
	public String getUpJgiName() {
		return upJgiName;
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
	 * 削除フラグを取得する。
	 * 
	 * @return delFlg 削除フラグ
	 */
	public Boolean getDelFlg() {
		return delFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
