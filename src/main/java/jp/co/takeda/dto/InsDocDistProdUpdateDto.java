package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設医師別計画配分対象品目 更新用DTO
 * 
 * @author stakeuchi
 */
public class InsDocDistProdUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 施設医師別計画立案ステータス最終更新日
	 */
	private final Date statusLastUpdate;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称
	 * @param statusLastUpdate 施設特約店別計画立案ステータス最終更新日
	 */
	public InsDocDistProdUpdateDto(String prodCode, String prodName, Date statusLastUpdate) {
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.statusLastUpdate = statusLastUpdate;
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
	 * 品目名称を取得する。
	 * 
	 * @return prodName 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 施設医師別計画立案ステータス最終更新日を取得する。
	 * 
	 * @return 施設医師別計画立案ステータス最終更新日
	 */
	public Date getStatusLastUpdate() {
		return statusLastUpdate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
