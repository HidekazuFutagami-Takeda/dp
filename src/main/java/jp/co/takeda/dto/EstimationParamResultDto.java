package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.EstParamHonbu;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.PlannedProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算パラメータ検索結果用DTO
 * 
 * @author nozaki
 */
public class EstimationParamResultDto extends DpDto {

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
	 * 本部案試算パラメータ
	 */
	private final EstParamHonbu estParamHonbu;

	/**
	 * 営業所案試算パラメータ <br>
	 * 営業所案が存在しない場合は、本部案のデータが設定されている。
	 */
	private final EstParamOffice estParamOffice;

	/**
	 * コンストラクタ
	 * 
	 * @param plannedProd 計画対象品目
	 * @param estParamHonbu 本部案試算パラメータ
	 * @param estParamOffice 営業所案試算パラメータ
	 */
	public EstimationParamResultDto(PlannedProd plannedProd, EstParamHonbu estParamHonbu, EstParamOffice estParamOffice) {

		this.plannedProd = plannedProd;
		this.estParamHonbu = estParamHonbu;
		this.estParamOffice = estParamOffice;
	}

	/**
	 * 計画対象品目を取得する。
	 * 
	 * @return 計画対象品目
	 */
	public PlannedProd getPlannedProd() {
		return plannedProd;
	}

	/**
	 * 本部案試算パラメータを取得する。
	 * 
	 * @return 本部案試算パラメータ
	 */
	public EstParamHonbu getEstParamHonbu() {
		return estParamHonbu;
	}

	/**
	 * 営業所案試算パラメータを取得する。
	 * 
	 * @return 営業所案試算パラメータ
	 */
	public EstParamOffice getEstParamOffice() {
		return estParamOffice;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
