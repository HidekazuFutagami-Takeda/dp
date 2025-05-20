package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)施設特約店品目別計画編集画面を表すDTO
 * 
 * @author siwamoto
 */
public class InsWsPlanForVacProdAdminListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 実施計画保存領域
	 */
	private final String planSaveArea;

	/**
	 * 検索結果リスト
	 */
	private final List<InsWsPlanForVacProdAdminDto> insWsPlanForVacProdAdminDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param planSaveArea 実施計画保存領域
	 * @param insWsPlanForVacProdAdminDtoList 検索結果リスト
	 */
	public InsWsPlanForVacProdAdminListDto(String planSaveArea, List<InsWsPlanForVacProdAdminDto> insWsPlanForVacProdAdminDtoList) {
		this.planSaveArea = planSaveArea;
		this.insWsPlanForVacProdAdminDtoList = insWsPlanForVacProdAdminDtoList;
	}

	/**
	 * 実施計画保存領域を取得する。
	 * 
	 * @return 実施計画保存領域
	 */
	public String getPlanSaveArea() {
		return planSaveArea;
	}

	/**
	 * 検索結果リストを取得する。
	 * 
	 * @return 検索結果リスト
	 */
	public List<InsWsPlanForVacProdAdminDto> getInsWsPlanForVacProdAdminDtoList() {
		return insWsPlanForVacProdAdminDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
