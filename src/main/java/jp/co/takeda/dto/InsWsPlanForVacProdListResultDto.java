package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.view.InsWsPlanStatSum;

/**
 * ワクチン用施設特約店別計画 品目一覧画面の検索結果用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacProdListResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 施設特約店別計画ステータスのサマリー
	 */
	private final InsWsPlanStatSum insWsPlanStatSum;

	/**
	 * 納入実績サマリ
	 */
	private final MrPlanResultValueDto resultSummary;

	/**
	 * 施設特約店別計画サマリ
	 */
	private final InsWsPlanForVacPlannedSummaryDto plannedSummary;

	/**
	 * 最終更新日
	 */
	private final Date lastUpdate;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 施設特約店別計画立案ステータスサマリ
	 * @param resultSummaryUh 納入実績サマリ
	 * @param plannedSummary 施設特約店別計画サマリ
	 */
	public InsWsPlanForVacProdListResultDto(ProdInsWsPlanStatSummaryDto statusSummary, MrPlanResultValueDto resultSummary, InsWsPlanForVacPlannedSummaryDto plannedSummary) {

		this.prodCode = statusSummary.getProdCode();
		this.prodName = statusSummary.getProdName();
		this.insWsPlanStatSum = statusSummary.getInsWsPlanStatSum();
		this.resultSummary = resultSummary;
		this.plannedSummary = plannedSummary;
		this.lastUpdate = statusSummary.getLastUpdate();
	}

	/**
	 * 品目コードを取得する。
	 * 
	 * @return prodCode
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
	 * 施設特約店別計画ステータスのサマリーを取得する。
	 * 
	 * @return 施設特約店別計画ステータスのサマリー
	 */
	public InsWsPlanStatSum getInsWsPlanStatSum() {
		return insWsPlanStatSum;
	}

	/**
	 * 納入実績サマリを取得する。
	 * 
	 * @return 納入実績サマリ
	 */
	public MrPlanResultValueDto getResultSummary() {
		return resultSummary;
	}

	/**
	 * 施設特約店別計画サマリを取得する。
	 * 
	 * @return 施設特約店別計画サマリ
	 */
	public InsWsPlanForVacPlannedSummaryDto getPlannedSummary() {
		return plannedSummary;
	}

	/**
	 * 最終更新日取得
	 * 
	 * @return 最終更新日取得
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
