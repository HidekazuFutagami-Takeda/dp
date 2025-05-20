package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用施設特約店別計画の一括登録用DTO
 * 
 * @author khashimoto
 */
public class InsWsPlanForVacUpDateModifyAllDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 更新行、追加行DTOのリスト<br>
	 * シーケンスキー(追加時null)・更新日時(追加時null)・施設コード・特約店コード・修正計画
	 */
	private final List<InsWsPlanUpDateModifyRowDto> modifyRow;

	/**
	 * 一般先計の更新行
	 */
	private final List<InsWsPlanUpDateModifyIppanRowDto> modifyIppanRow;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param modifyRow 更新行、追加行DTOのリスト
	 * @param modifyIppanRow 一般先計の更新行
	 * 
	 */
	public InsWsPlanForVacUpDateModifyAllDto(Integer jgiNo, String prodCode, List<InsWsPlanUpDateModifyRowDto> modifyRow, List<InsWsPlanUpDateModifyIppanRowDto> modifyIppanRow) {
		super();
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.modifyRow = modifyRow;
		this.modifyIppanRow = modifyIppanRow;
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
	 * 品目固定コードを取得する。
	 * 
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 更新行、追加行DTOのリストを取得する。
	 * 
	 * @return modifyRow 更新行、追加行DTOのリスト
	 */
	public List<InsWsPlanUpDateModifyRowDto> getModifyRow() {
		return modifyRow;
	}

	/**
	 * 一般先計の更新行を取得する。
	 * 
	 * @return 一般先計の更新行
	 */
	public List<InsWsPlanUpDateModifyIppanRowDto> getModifyIppanRow() {
		return modifyIppanRow;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
