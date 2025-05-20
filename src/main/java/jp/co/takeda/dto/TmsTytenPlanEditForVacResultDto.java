package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.TmsTytenMstUn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画編集 検索結果DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanEditForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * TMS特約店基本統合
	 */
	private final TmsTytenMstUn tmsTytenMstUn;

	/**
	 * 計画対象品目
	 */
	private final PlannedProd plannedProd;

	/**
	 * 特約店別計画編集 明細リスト
	 */
	private final List<TmsTytenPlanEditForVacDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenMstUn TMS特約店基本統合
	 * @param plannedProd 計画対象品目
	 * @param detailList 特約店別計画編集 明細リスト
	 */
	public TmsTytenPlanEditForVacResultDto(TmsTytenMstUn tmsTytenMstUn, PlannedProd plannedProd, List<TmsTytenPlanEditForVacDetailDto> detailList) {
		this.tmsTytenMstUn = tmsTytenMstUn;
		this.plannedProd = plannedProd;
		this.detailList = detailList;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * TMS特約店基本統合を取得する
	 * 
	 * @return TMS特約店基本統合
	 */
	public TmsTytenMstUn getTmsTytenMstUn() {
		return tmsTytenMstUn;
	}

	/**
	 * 計画対象品目を取得する
	 * 
	 * @return 計画対象品目
	 */
	public PlannedProd getPlannedProd() {
		return plannedProd;
	}

	/**
	 * 特約店別計画編集 明細リストを取得する。
	 * 
	 * @return detailList 特約店別計画編集 明細リスト
	 */
	public List<TmsTytenPlanEditForVacDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
