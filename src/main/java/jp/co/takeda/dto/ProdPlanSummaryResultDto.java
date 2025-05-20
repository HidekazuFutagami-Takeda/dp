package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 品目別計画積上結果の検索結果DTO
 * 
 * @author stakeuchi
 */
public class ProdPlanSummaryResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細DTOリスト
	 */
	private final List<ProdPlanSummaryResultDetailDto> detailList;

	/**
	 * 登録不可フラグ<br>
	 * true：登録不可、false：登録可能
	 */
	private final Boolean disableUpdate;

	/**
	 * コンストラクタ
	 * 
	 * @param detailList 明細DTOリスト
	 */
	public ProdPlanSummaryResultDto(List<ProdPlanSummaryResultDetailDto> detailList, Boolean disableUpdate) {
		this.detailList = detailList;
		this.disableUpdate = disableUpdate;
	}

	/**
	 * 明細DTOリストを取得する。
	 * 
	 * @return detailList 明細DTOリスト
	 */
	public List<ProdPlanSummaryResultDetailDto> getDetailList() {
		return detailList;
	}

	/**
	 * 登録不可フラグを取得する。
	 * 
	 * @return true：登録不可、false：登録可能
	 */
	public Boolean getDisableUpdate() {
		return disableUpdate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
