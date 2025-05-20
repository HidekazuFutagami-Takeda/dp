package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * (ワ)施設特約店別計画編集画面を表すDTO
 * 
 * @author siwamoto
 */
public class TytenPlanProdForVacAdminDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * B価ベース
	 */
	private final Long baseB;

	/**
	 * T価ベース（編集前）
	 */
	private final Long beforeBaseT;

	/**
	 * T価ベース
	 */
	private final Long baseT;

	/**
	 * 最終更新者
	 */
	private final String upDateJgiName;

	/**
	 * 最終更新日
	 */
	private final Date upDate;

	/**
	 * 品目計フラグ
	 */
	private final boolean prodSumRowFlg;

	/**
	 * コンストラクタ
	 * 
	 * @param prodName
	 * @param prodCode
	 * @param beforeBaseT
	 * @param baseT
	 * @param upDateJgiName
	 * @param upDate
	 * @param prodSumRowFlg
	 */
	public TytenPlanProdForVacAdminDto(String prodName, String prodCode, Long baseB, Long beforeBaseT, Long baseT, String upDateJgiName, Date upDate, boolean prodSumRowFlg) {
		this.prodName = prodName;
		this.prodCode = prodCode;
		this.baseB = baseB;
		this.beforeBaseT = beforeBaseT;
		this.baseT = baseT;
		this.upDateJgiName = upDateJgiName;
		this.upDate = upDate;
		this.prodSumRowFlg = prodSumRowFlg;
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
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * B価ベースを取得する。
	 * 
	 * @return B価ベース
	 */
	public Long getBaseB() {
		return baseB;
	}

	/**
	 * T価ベース（編集前）を取得する。
	 * 
	 * @return T価ベース（編集前）
	 */
	public Long getBeforeBaseT() {
		return beforeBaseT;
	}

	/**
	 * T価ベースを取得する。
	 * 
	 * @return T価ベース
	 */
	public Long getBaseT() {
		return baseT;
	}

	/**
	 * 最終更新者を取得する。
	 * 
	 * @return 最終更新者
	 */
	public String getUpDateJgiName() {
		return upDateJgiName;
	}

	/**
	 * 最終更新日を取得する。
	 * 
	 * @return 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 品目計フラグを取得する。
	 * 
	 * @return 品目計フラグ
	 */
	public boolean getProdSumRowFlg() {
		return prodSumRowFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
