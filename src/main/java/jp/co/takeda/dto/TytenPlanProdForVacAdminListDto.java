package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特約店品目別計画編集を表すDTO
 * 
 * @author siwamoto
 */
public class TytenPlanProdForVacAdminListDto extends DpDto {

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
	private final List<TytenPlanProdForVacAdminDto> tytenPlanProdForVacAdminDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param planSaveArea 実施計画保存領域
	 * @param prodCategory カテゴリ
	 * @param tytenPlanProdForVacAdminDtoList 検索結果リスト
	 */
	public TytenPlanProdForVacAdminListDto(String planSaveArea, List<TytenPlanProdForVacAdminDto> tytenPlanProdForVacAdminDtoList) {
		super();
		this.planSaveArea = planSaveArea;
		this.tytenPlanProdForVacAdminDtoList = tytenPlanProdForVacAdminDtoList;
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
	public List<TytenPlanProdForVacAdminDto> getTytenPlanProdForVacAdminDtoList() {
		return tytenPlanProdForVacAdminDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
