package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用施設特約店別計画 担当者一覧画面の検索結果用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacJgiListResultDto extends DpDto {

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
	 * 担当者一覧画面の特約店G情報DTOのリスト
	 */
	private final List<InsWsPlanForVacTokuGResultDto> teamResultList;

	/**
	 * コンストラクタ
	 * 
	 * @param plannedProd 品目情報
	 * @param teamResultList 担当者一覧画面の特約店G情報DTOのリスト
	 */
	public InsWsPlanForVacJgiListResultDto(PlannedProd plannedProd, List<InsWsPlanForVacTokuGResultDto> teamResultList) {

		this.prodCode = plannedProd.getProdCode();
		this.prodName = plannedProd.getProdName();
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
	 * 担当者一覧画面のチーム情報DTOのリストを取得する。
	 * 
	 * @return 担当者一覧画面のチーム情報DTOのリスト
	 */
	public List<InsWsPlanForVacTokuGResultDto> getTeamResultList() {
		return teamResultList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
