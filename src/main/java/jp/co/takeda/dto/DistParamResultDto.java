package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DistParamHonbu;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.PlannedProd;

/**
 * 配分パラメータ検索結果用DTO
 *
 * @author stakeuchi
 */
public class DistParamResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 計画対象品目
	 */
	private final PlannedProd plannedProd;

	/**
	 * [UH]本部案配分パラメータ
	 */
	private final DistParamHonbu distParamHonbuUH;

	/**
	 * [P]本部案配分パラメータ
	 */
	private final DistParamHonbu distParamHonbuP;

	/**
	 * [UH]営業所案配分パラメータ <br>
	 * 営業所案が存在しない場合は、本部案のデータが設定されている。
	 */
	private final DistParamOffice distParamOfficeUH;

	/**
	 * [P]営業所案配分パラメータ <br>
	 * 営業所案が存在しない場合は、本部案のデータが設定されている。
	 */
	private final DistParamOffice distParamOfficeP;

	/**
	 * カテゴリ名
	 */
	private final String categoryName;

	/**
	 * コンストラクタ
	 *
	 * @param plannedProd 計画対象品目
	 * @param distParamHonbuUH [UH]本部案配分パラメータ
	 * @param distParamHonbuP [P]本部案配分パラメータ
	 * @param distParamOfficeUH [UH]営業所案配分パラメータ
	 * @param distParamOfficeP [P]営業所案配分パラメータ
	 */
	public DistParamResultDto(PlannedProd plannedProd, DistParamHonbu distParamHonbuUH, DistParamHonbu distParamHonbuP, DistParamOffice distParamOfficeUH,
		DistParamOffice distParamOfficeP, String categoryName) {
		this.plannedProd = plannedProd;
		this.distParamHonbuUH = distParamHonbuUH;
		this.distParamHonbuP = distParamHonbuP;
		this.distParamOfficeUH = distParamOfficeUH;
		this.distParamOfficeP = distParamOfficeP;
		this.categoryName = categoryName;
	}

	/**
	 * 計画対象品目を取得する。
	 *
	 * @return plannedProd 計画対象品目
	 */
	public PlannedProd getPlannedProd() {
		return plannedProd;
	}

	/**
	 * [UH]本部案配分パラメータを取得する。
	 *
	 * @return distParamHonbuUH [UH]本部案配分パラメータ
	 */
	public DistParamHonbu getDistParamHonbuUH() {
		return distParamHonbuUH;
	}

	/**
	 * [P]本部案配分パラメータを取得する。
	 *
	 * @return distParamHonbuP [P]本部案配分パラメータ
	 */
	public DistParamHonbu getDistParamHonbuP() {
		return distParamHonbuP;
	}

	/**
	 * [UH]営業所案配分パラメータを取得する。
	 *
	 * @return distParamOfficeUH [UH]営業所案配分パラメータ
	 */
	public DistParamOffice getDistParamOfficeUH() {
		return distParamOfficeUH;
	}

	/**
	 * [P]営業所案配分パラメータを取得する。
	 *
	 * @return distParamOfficeP [P]営業所案配分パラメータ
	 */
	public DistParamOffice getDistParamOfficeP() {
		return distParamOfficeP;
	}

	/**
	 * カテゴリ名を取得する。
	 *
	 * @return String カテゴリ名
	 */
	public String getCategoryName() {
		return categoryName;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
