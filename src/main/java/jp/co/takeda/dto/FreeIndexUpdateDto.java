package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * フリー項目　更新用DTO
 * 
 * @author nozaki
 */
public class FreeIndexUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 更新対象フリー項目情報のリスト
	 */
	private final List<FreeIndexDto> updateFreeIndexDtoList;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称
	 * @param updateFreeIndexDtoList 更新対象フリー項目情報のリスト
	 */
	public FreeIndexUpdateDto(String prodCode, String prodName, List<FreeIndexDto> updateFreeIndexDtoList) {

		this.prodCode = prodCode;
		this.prodName = prodName;
		this.updateFreeIndexDtoList = updateFreeIndexDtoList;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名称を取得する。
	 * 
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 新対象フリー項目情報のリストを取得する。
	 * 
	 * @return 新対象フリー項目情報のリスト
	 */
	public List<FreeIndexDto> getUpdateFreeIndexDtoList() {
		return updateFreeIndexDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
