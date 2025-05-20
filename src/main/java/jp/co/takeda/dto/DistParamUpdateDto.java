package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DistParamOffice;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分基準更新用DTO
 * 
 * @author nozaki
 */
public class DistParamUpdateDto extends DpDto {

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
	 * 営業所案 配分基準情報UH
	 */
	private final DistParamOffice distParamOfficeUH;

	/**
	 * 営業所案 配分基準情報P
	 */
	private final DistParamOffice distParamOfficeP;

	/**
	 * コンストラクタ
	 * 
	 * @param estParamOfficeUH 営業所案UH
	 * @param estParamOfficeP 営業所案P
	 * @param prodName 品目名称
	 * @param prodCode 品目コード
	 */
	public DistParamUpdateDto(DistParamOffice distParamOfficeUH, DistParamOffice distParamOfficeP, String prodName) {

		this.distParamOfficeUH = distParamOfficeUH;
		this.distParamOfficeP = distParamOfficeP;
		this.prodName = prodName;
	}

	/**
	 * 営業所案 配分基準情報UHを取得する。
	 * 
	 * @return 営業所案 配分基準情報UH
	 */
	public DistParamOffice getDistParamOfficeUH() {
		return distParamOfficeUH;
	}

	/**
	 * 営業所案 配分基準情報Pを取得する。
	 * 
	 * @return 営業所案 配分基準情報P
	 */
	public DistParamOffice getDistParamOfficeP() {
		return distParamOfficeP;
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
