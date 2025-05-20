package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画参照画面ヘッダー 結果DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanReferenceHeaderForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 変換パラメータ(B→T価)更新日時
	 */
	private final Date changeParamBTUpDate;

	/**
	 * 特約店名称
	 */
	private final String tmsTytenName;

	/**
	 * コンストラクタ
	 * 
	 * @param changeParamBTUpDate 変換パラメータ(B→T価)更新日時
	 * @param tmsTytenName 特約店名称
	 */
	public TmsTytenPlanReferenceHeaderForVacResultDto(Date changeParamBTUpDate, String tmsTytenName) {
		this.changeParamBTUpDate = changeParamBTUpDate;
		this.tmsTytenName = tmsTytenName;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 変換パラメータ(B→T価)更新日時を取得する。
	 * 
	 * @return 変換パラメータ(B→T価)更新日時
	 */
	public Date getChangeParamBTUpDate() {
		return changeParamBTUpDate;
	}

	/**
	 * 特約店名称を取得する。
	 * 
	 * @return 特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
