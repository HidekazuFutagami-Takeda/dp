package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.OfficePlanStatusForBranch;
import jp.co.takeda.model.view.TmsTytenPlanDistProd;

/**
 * 特約店別計画配分対象品目一覧の検索結果格納DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanDistProdResultDto extends DpDto {

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
	 * @param tmsTytenPlanDistProdDtoList 特約店別計画配分対象品目の一覧
	 * @param officePlanStatus 営業所計画確定状態
	 */
	public TmsTytenPlanDistProdResultDto(List<TmsTytenPlanDistProd> tmsTytenPlanDistProdList, OfficePlanStatusForBranch officePlanStatus) {
		this.tmsTytenPlanDistProdList = tmsTytenPlanDistProdList;
		this.officePlanStatusForBranch = officePlanStatus;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画配分対象品目の一覧
	 */
	private List<TmsTytenPlanDistProd> tmsTytenPlanDistProdList;

	/**
	 * 営業所計画確定状態
	 */
	private OfficePlanStatusForBranch officePlanStatusForBranch;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店別計画配分対象品目の一覧を取得する。
	 * 
	 * @return 特約店別計画配分対象品目の一覧
	 */
	public List<TmsTytenPlanDistProd> getTmsTytenPlanDistProdList() {
		return tmsTytenPlanDistProdList;
	}

	/**
	 * 営業所計画確定状態を取得する。
	 * 
	 * @return 業所計画確定状態
	 */
	public OfficePlanStatusForBranch getOfficePlanStatusForBranch() {
		return officePlanStatusForBranch;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
