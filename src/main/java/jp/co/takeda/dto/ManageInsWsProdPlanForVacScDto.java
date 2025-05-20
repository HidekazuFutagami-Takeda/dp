package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン施設特約店別計画の検索用DTO
 * 
 * @author nozaki
 */
public class ManageInsWsProdPlanForVacScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

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
	 * @param insNo 施設コード
	 * @param tmsTytenCd 特約店コード
	 * @param prodCode 計画
	 * 
	 */
	public ManageInsWsProdPlanForVacScDto(String insNo, String tmsTytenCd, PlanData planData) {
		this.insNo = insNo;
		this.tmsTytenCd = tmsTytenCd;
		this.planData = planData;
	}

	/**
	 * 施設コードを取得する。
	 * 
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 特約店コードを取得する。
	 * 
	 * @return 特約店コード
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
