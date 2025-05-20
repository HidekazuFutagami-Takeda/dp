package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特約店品目別計画編集を表すDTO
 * 
 * @author khashimoto
 */
public class ManageWsPlanForVacProdDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果リスト
	 */
	private final List<ManageWsPlanForVacProdDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param tytenPlanProdAdminDtoList 検索結果リスト
	 */
	public ManageWsPlanForVacProdDto(List<ManageWsPlanForVacProdDetailDto> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 検索結果リストを取得する。
	 * 
	 * @return 検索結果リスト
	 */
	public List<ManageWsPlanForVacProdDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
