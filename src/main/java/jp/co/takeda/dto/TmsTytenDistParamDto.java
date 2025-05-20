package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpAsyncDto;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUser;

/**
 * 特約店別計画配分パラメータ(非同期処理用)
 * 
 * @author yokokawa
 */
public class TmsTytenDistParamDto extends DpAsyncDto {
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
	 * @param metaInfo メタ情報
	 * @param dpUser 実行者従業員情報
	 * @param tmsTytenDistParamResultDto 特約店別計画配分パラメータ検索結果DTO
	 */
	public TmsTytenDistParamDto(DpMetaInfo metaInfo, DpUser dpUser, TmsTytenDistParamResultDto tmsTytenDistParamResultDto) {
		super(metaInfo);
		this.dpUser = dpUser;
		this.sosMst2 = tmsTytenDistParamResultDto.getSosMst2();
		this.plannedProdList = tmsTytenDistParamResultDto.getPlannedProdList();
		this.sosMst3List = tmsTytenDistParamResultDto.getSosMst3List();
		this.beforeWsPlanStatusList = tmsTytenDistParamResultDto.getBeforeWsPlanStatusList();
		this.distParamOfficeUHList = tmsTytenDistParamResultDto.getDistParamOfficeUHList();
		this.distParamOfficePList = tmsTytenDistParamResultDto.getDistParamOfficePList();
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織（支店）
	 */
	private SosMst sosMst2;

	/**
	 * 品目情報リスト
	 */
	private List<PlannedProd> plannedProdList;

	/**
	 * 実行者従業員情報
	 */
	private final DpUser dpUser;

	/**
	 * 組織情報(営業所)リスト
	 */
	private List<SosMst> sosMst3List;

	/**
	 * 実行前ステータス情報リスト
	 */
	private List<WsPlanStatus> beforeWsPlanStatusList;

	/**
	 * 配分基準（UH）
	 */
	private List<DistParamOffice> distParamOfficeUHList;

	/**
	 * 配分基準（P）
	 */
	private List<DistParamOffice> distParamOfficePList;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 組織（支店）を取得する
	 * 
	 * @return 組織（支店）
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
	 * 実行者従業員情報リストを取得する
	 * 
	 * @return 実行者従業員情報リスト
	 */
	public DpUser getDpUser() {
		return dpUser;
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
	 * 配分基準(UH)を取得する
	 * 
	 * @return 配分基準(UH)
	 */
	public List<DistParamOffice> getDistParamOfficeUHList() {
		return distParamOfficeUHList;
	}

	/**
	 * 配分基準(P)を取得する
	 * 
	 * @return 配分基準(P)
	 */
	public List<DistParamOffice> getDistParamOfficePList() {
		return distParamOfficePList;
	}
}
