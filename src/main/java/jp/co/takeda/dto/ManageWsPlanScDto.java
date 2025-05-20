package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店別計画の検索用DTO
 * 
 * @author siwamoto
 */
public class ManageWsPlanScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * カテゴリ
	 */
	private final String prodCategory;

	/**
	 * 品目
	 */
	private final String prodCode;

	/**
	 * 計画
	 */
	private final PlanData planData;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenCd 特約店コード
	 * @param prodCategory カテゴリ
	 * @param prodCode 品目
	 * @param planData 計画
	 */
	public ManageWsPlanScDto(String tmsTytenCd, String prodCategory, String prodCode, PlanData planData) {
		super();
		this.tmsTytenCd = tmsTytenCd;
		this.prodCategory = prodCategory;
		this.prodCode = prodCode;
		this.planData = planData;
	}

	/**
	 * 特約店コードを取得する。
	 * 
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * カテゴリを取得する。
	 * 
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 品目を取得する。
	 * 
	 * @return prodCode 品目
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 計画を取得する。
	 * 
	 * @return planData 計画
	 */
	public PlanData getPlanData() {
		return planData;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
