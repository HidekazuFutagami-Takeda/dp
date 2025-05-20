package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TmsTytenMstUn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店別計画追加DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanAddResultDto extends DpDto {

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
	 * @param tmsTytenPlanAddDetailDtoList 特約店別計画追加 明細
	 * @param addFlg 特約店別計画追加 可能フラグ
	 * @param searchSosMst 検索対象の営業所
	 * @param searchTmsTytenMstUn 検索対象の特約店
	 * @return 特約店別計画追加DTO
	 */
	public TmsTytenPlanAddResultDto(List<TmsTytenPlanAddDetailDto> tmsTytenPlanAddDetailDtoList, boolean addFlg, SosMst searchSosMst, TmsTytenMstUn searchTmsTytenMstUn) {
		this.tmsTytenPlanAddDetailDtoList = tmsTytenPlanAddDetailDtoList;
		this.addFlg = addFlg;
		this.searchSosMst = searchSosMst;
		this.searchTmsTytenMstUn = searchTmsTytenMstUn;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画追加 明細
	 */
	private final List<TmsTytenPlanAddDetailDto> tmsTytenPlanAddDetailDtoList;

	/**
	 * 特約店別計画追加 可能フラグ
	 */
	private final boolean addFlg;

	/**
	 * 検索対象の営業所
	 */
	private final SosMst searchSosMst;

	/**
	 * 検索対象の特約店
	 */
	private final TmsTytenMstUn searchTmsTytenMstUn;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店別計画追加 明細を取得する
	 * 
	 * @return 特約店別計画追加 明細
	 */
	public List<TmsTytenPlanAddDetailDto> getTmsTytenPlanAddDetailDtoList() {
		return tmsTytenPlanAddDetailDtoList;
	}

	/**
	 * 特約店別計画追加 可能フラグを取得する
	 * 
	 * @return 特約店別計画追加 可能フラグ
	 */
	public boolean getAddFlg() {
		return addFlg;
	}

	/**
	 * 検索対象の営業所を取得する。
	 * 
	 * @return searchSosMst 検索対象の営業所
	 */
	public SosMst getSearchSosMst() {
		return searchSosMst;
	}

	/**
	 * 検索対象の特約店を取得する。
	 * 
	 * @return 検索対象の特約店
	 */
	public TmsTytenMstUn getSearchTmsTytenMstUn() {
		return searchTmsTytenMstUn;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
