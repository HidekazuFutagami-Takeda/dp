package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 品目別計画の検索結果DTO
 * 
 * @author stakeuchi
 */
public class ProdPlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細DTOリスト
	 */
	private final List<ProdPlanResultDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param detailList 明細DTOリスト
	 */
	public ProdPlanResultDto(List<ProdPlanResultDetailDto> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 明細DTOリストを取得する。
	 * 
	 * @return detailList 明細DTOリスト
	 */
	public List<ProdPlanResultDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
