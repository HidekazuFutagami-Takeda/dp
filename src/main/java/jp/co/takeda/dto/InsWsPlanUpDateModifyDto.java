package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画の追加・更新用DTO
 * 
 * @author siwamoto
 */
public class InsWsPlanUpDateModifyDto extends DpDto {

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
	 * 更新行、追加行DTOのリスト シーケンスキー(追加時null)・更新日時(追加時null)・施設コード・特約店コード・修正計画
	 */
	private final List<InsWsPlanUpDateModifyRowDto> modifyRow;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param modifyRow 更新行、追加行DTOのリスト
	 */
	public InsWsPlanUpDateModifyDto(Integer jgiNo, String prodCode, List<InsWsPlanUpDateModifyRowDto> modifyRow) {
		super();
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.modifyRow = modifyRow;
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
