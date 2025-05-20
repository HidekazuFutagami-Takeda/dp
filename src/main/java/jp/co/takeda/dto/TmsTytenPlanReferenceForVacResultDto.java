package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画参照画面 結果DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanReferenceForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画参照画面 明細行
	 */
	private final List<TmsTytenPlanReferenceForVacDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param detailList 特約店別計画参照画面 明細行
	 */
	public TmsTytenPlanReferenceForVacResultDto(List<TmsTytenPlanReferenceForVacDetailDto> detailList) {
		this.detailList = detailList;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店別計画参照画面 明細行を取得する。
	 * 
	 * @return detailList 特約店別計画参照画面 明細行
	 */
	public List<TmsTytenPlanReferenceForVacDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
