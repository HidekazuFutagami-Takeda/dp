package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.TmsTytenMstUn;

/**
 * 特約店別計画編集 検索結果DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanEditResultDto extends DpDto {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// constructor
	// -------------------------------
	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenPlanEditDetailList 特約店別計画編集 明細リスト
	 */
	public TmsTytenPlanEditResultDto(TmsTytenMstUn tmsTytenMstUn, PlannedProd plannedProd, List<TmsTytenPlanEditDetailDto> tmsTytenPlanEditDetailList) {
		this.tmsTytenMstUn = tmsTytenMstUn;
		this.plannedProd = plannedProd;
		this.tmsTytenPlanEditDetailList = tmsTytenPlanEditDetailList;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * TMS特約店基本統合
	 */
	private TmsTytenMstUn tmsTytenMstUn;

	/**
	 * 計画対象品目
	 */
	private PlannedProd plannedProd;

	/**
	 * 特約店別計画編集 明細リスト
	 */
	private List<TmsTytenPlanEditDetailDto> tmsTytenPlanEditDetailList;

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
	 * 特約店別計画編集 明細リストを取得する
	 * 
	 * @return 特約店別計画編集 明細リスト
	 */
	public List<TmsTytenPlanEditDetailDto> getTmsTytenPlanEditDetailList() {
		return tmsTytenPlanEditDetailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
