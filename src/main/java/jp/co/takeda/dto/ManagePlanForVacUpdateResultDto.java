package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 管理の更新結果DTO(ワクチン用)
 * 
 * @author nozaki
 */
public class ManagePlanForVacUpdateResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 計画値の更新件数
	 */
	private final int updateTotalCount;

	/**
	 * コンストラクタ
	 * 
	 * @param updateCount 計画Pの更新件数
	 */
	public ManagePlanForVacUpdateResultDto(int updateTotalCount) {
		this.updateTotalCount = updateTotalCount;
	}

	/**
	 * 計画値の更新件数を取得する。
	 * 
	 * @return 計画値の更新件数
	 */
	public int getUpdateTotalCount() {
		return updateTotalCount;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
