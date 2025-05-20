package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;

/**
 * 施設特約店別計画 担当者一覧画面の検索結果用DTO
 *
 * @author nozaki
 */
public class InsWsPlanJgiListResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * カテゴリ
	 */
	private final String category;

	/**
	 * 重点品フラグ
	 */
	private final boolean planLevelInsDoc;

	/**
	 * 担当者一覧画面のチーム情報DTOのリスト
	 */
	private final List<InsWsPlanTeamResultDto> teamResultList;

	/**
	 * コンストラクタ
	 *
	 * @param plannedProd 品目情報
	 * @param teamResultList 担当者一覧画面のチーム情報DTOのリスト
	 */
	public InsWsPlanJgiListResultDto(PlannedProd plannedProd, List<InsWsPlanTeamResultDto> teamResultList) {

		this.prodCode = plannedProd.getProdCode();
		this.prodName = plannedProd.getProdName();
		this.category = plannedProd.getCategory();
		this.planLevelInsDoc = plannedProd.getPlanLevelInsDoc();
		this.teamResultList = teamResultList;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 重点品目フラグを取得する。
	 *
	 * @return 重点品
	 */
	public boolean isPlanLevelInsDoc() {
		return planLevelInsDoc;
	}

	/**
	 * 担当者一覧画面のチーム情報DTOのリストを取得する。
	 *
	 * @return 担当者一覧画面のチーム情報DTOのリスト
	 */
	public List<InsWsPlanTeamResultDto> getTeamResultList() {
		return teamResultList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
