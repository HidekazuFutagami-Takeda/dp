package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;

/**
 * 特約店別計画配分パラメータ検索結果DTO
 * 
 * @author yokokawa
 */
public class TmsTytenDistParamResultDto extends DpDto {
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
	 * @param sosMst2 組織情報(支店)
	 * @param plannedProdList 品目情報リスト
	 * @param sosMst3List 組織情報(営業所)リスト
	 * @param beforeWsPlanStatusList 実行前ステータス情報リスト
	 * @param distParamOfficeUHList 配分基準(UH)情報リスト
	 * @param distParamOfficePList 配分基準(P)情報リスト
	 */
	public TmsTytenDistParamResultDto(SosMst sosMst2, List<PlannedProd> plannedProdList, List<SosMst> sosMst3List, List<WsPlanStatus> beforeWsPlanStatusList,
		List<DistParamOffice> distParamOfficeUHList, List<DistParamOffice> distParamOfficePList) {
		this.sosMst2 = sosMst2;
		this.plannedProdList = plannedProdList;
		this.sosMst3List = sosMst3List;
		this.beforeWsPlanStatusList = beforeWsPlanStatusList;
		this.distParamOfficeUHList = distParamOfficeUHList;
		this.distParamOfficePList = distParamOfficePList;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織情報(支店)
	 */
	private SosMst sosMst2;

	/**
	 * 品目情報リスト
	 */
	private List<PlannedProd> plannedProdList;

	/**
	 * 組織情報(営業所)リスト
	 */
	private List<SosMst> sosMst3List;

	/**
	 * 実行前ステータス情報リスト
	 */
	private List<WsPlanStatus> beforeWsPlanStatusList;

	/**
	 * 配分基準(UH)情報リスト
	 */
	List<DistParamOffice> distParamOfficeUHList;

	/**
	 * 配分基準(P)情報リスト
	 */
	List<DistParamOffice> distParamOfficePList;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 組織情報(支店)を取得する
	 */
	public SosMst getSosMst2() {
		return sosMst2;
	}

	/**
	 * 品目情報リストを取得する
	 * 
	 * @return 品目情報リスト
	 */
	public List<PlannedProd> getPlannedProdList() {
		return plannedProdList;
	}

	/**
	 * 組織情報(営業所)リストを取得する
	 * 
	 * @return 組織情報(営業所)リスト
	 */
	public List<SosMst> getSosMst3List() {
		return sosMst3List;
	}

	/**
	 * 実行前ステータス情報リストを取得する
	 * 
	 * @return 実行前ステータス情報リスト
	 */
	public List<WsPlanStatus> getBeforeWsPlanStatusList() {
		return beforeWsPlanStatusList;
	}

	/**
	 * 配分基準(UH)情報リストを取得する
	 * 
	 * @return 配分基準(UH)情報リスト
	 */
	public List<DistParamOffice> getDistParamOfficeUHList() {
		return distParamOfficeUHList;
	}

	/**
	 * 配分基準(P)情報リストを取得する
	 * 
	 * @return 配分基準(P)情報リスト
	 */
	public List<DistParamOffice> getDistParamOfficePList() {
		return distParamOfficePList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
