package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特約店品目別計画の検索用DTO
 * 
 * @author khashimoto
 */
public class ManageWsPlanForVacProdScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 計画
	 */
	private final PlanData planData;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenCd 特約店コード
	 * @param planData 計画
	 */
	public ManageWsPlanForVacProdScDto(String tmsTytenCd, PlanData planData) {
		super();
		this.tmsTytenCd = tmsTytenCd;
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
