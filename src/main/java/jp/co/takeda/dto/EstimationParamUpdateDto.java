package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.EstParamOffice;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算パラメータ更新用DTO
 * 
 * @author nozaki
 */
public class EstimationParamUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 対象品目名称
	 */
	private final String prodName;

	/**
	 * 営業所案 試算パラメータ情報
	 */
	private final EstParamOffice estParamOffice;

	/**
	 * コンストラクタ
	 * 
	 * @param estParamOffice 営業所案
	 */
	public EstimationParamUpdateDto(EstParamOffice estParamOffice, String prodName) {

		this.estParamOffice = estParamOffice;
		this.prodName = prodName;
	}

	/**
	 * 営業所案 試算パラメータ情報を取得する。
	 * 
	 * @return 営業所案 試算パラメータ情報
	 */
	public EstParamOffice getEstParamOffice() {
		return estParamOffice;
	}

	/**
	 * 対象品目名称を取得する。
	 * 
	 * @return 対象品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
