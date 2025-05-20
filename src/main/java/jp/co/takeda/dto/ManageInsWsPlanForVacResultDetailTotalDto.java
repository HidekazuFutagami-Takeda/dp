package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画の検索結果 明細合計行を表すDTO
 * 
 * @author nozaki
 */
public class ManageInsWsPlanForVacResultDetailTotalDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * T/B変換率
	 */
	private final Double tbChangeRate;

	/**
	 * コンストラクタ
	 * 
	 * @param tyChangeRate T/B変換率
	 */
	public ManageInsWsPlanForVacResultDetailTotalDto(Double tbChangeRate) {
		this.tbChangeRate = tbChangeRate;
	}

	/**
	 * T/B変換率を取得する。
	 * 
	 * @return tbChangeRate T/B変換率
	 */
	public Double getTbChangeRate() {
		return tbChangeRate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
