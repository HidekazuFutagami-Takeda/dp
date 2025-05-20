package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TmsTytenMstUn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画追加 検索結果DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanAddForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * ワクチン用特約店別計画追加 明細DTOリスト
	 */
	private final List<TmsTytenPlanAddForVacDetailDto> detailList;

	/**
	 * 特約店別計画追加 可能フラグ
	 */
	private final boolean addFlg;

	/**
	 * 検索対象のエリア特約店Ｇ
	 */
	private final SosMst searchSosMst;

	/**
	 * 検索対象の特約店
	 */
	private final TmsTytenMstUn searchTmsTytenMstUn;

	/**
	 * コンストラクタ
	 * 
	 * @param detailList ワクチン用特約店別計画追加 明細DTOリスト
	 * @param addFlg 特約店別計画追加 可能フラグ
	 * @param searchSosMst 検索対象の営業所
	 * @param searchTmsTytenMstUn 検索対象の特約店
	 * @return 特約店別計画追加DTO
	 */
	public TmsTytenPlanAddForVacResultDto(List<TmsTytenPlanAddForVacDetailDto> detailList, boolean addFlg, SosMst searchSosMst, TmsTytenMstUn searchTmsTytenMstUn) {
		this.detailList = detailList;
		this.addFlg = addFlg;
		this.searchSosMst = searchSosMst;
		this.searchTmsTytenMstUn = searchTmsTytenMstUn;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * ワクチン用特約店別計画追加 明細DTOリストを取得する
	 * 
	 * @return ワクチン用特約店別計画追加 明細DTOリスト
	 */
	public List<TmsTytenPlanAddForVacDetailDto> getDetailList() {
		return detailList;
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
