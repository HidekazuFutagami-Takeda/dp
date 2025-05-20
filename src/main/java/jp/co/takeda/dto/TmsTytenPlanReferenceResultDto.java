package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 特約店別計画参照画面 結果DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanReferenceResultDto extends DpDto {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// constructor
	// -------------------------------
	/**
	 * コンストラクタ
	 */
	public TmsTytenPlanReferenceResultDto(List<TmsTytenPlanReferenceDetailDto> tmsTytenPlanReferenceDetailList) {
		this.tmsTytenPlanReferenceDetailList = tmsTytenPlanReferenceDetailList;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画参照画面 明細行
	 */
	private List<TmsTytenPlanReferenceDetailDto> tmsTytenPlanReferenceDetailList;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店別計画参照画面 明細行を取得する
	 */
	public List<TmsTytenPlanReferenceDetailDto> getTmsTytenPlanReferenceDetailList() {
		return tmsTytenPlanReferenceDetailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
