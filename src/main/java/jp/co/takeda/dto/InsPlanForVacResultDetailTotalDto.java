package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画の検索結果 明細合計行を表すDTO
 * 
 * @author stakeuchi
 */
public class InsPlanForVacResultDetailTotalDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設別計画 非表示分
	 */
	private final Long hideValue;

	/**
	 * T/B変換率
	 */
	private final Double tyChangeRate;

	/**
	 * コンストラクタ
	 * 
	 * @param mrPlan 担当者計画
	 * @param hideValue 施設別計画 非表示分
	 * @param tyChangeRate T/B変換率
	 */
	public InsPlanForVacResultDetailTotalDto(Long hideValue, Double tyChangeRate) {
		this.hideValue = ConvertUtil.parseMoneyToThousandUnit(hideValue);
		this.tyChangeRate = tyChangeRate;
	}

	/**
	 * 施設別計画 非表示分を取得する。
	 * 
	 * @return hideValue 施設別計画 非表示分
	 */
	public Long getHideValue() {
		return hideValue;
	}

	/**
	 * T/B変換率を取得する。
	 * 
	 * @return tyChangeRate T/B変換率
	 */
	public Double getTyChangeRate() {
		return tyChangeRate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
