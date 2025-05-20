package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画担当者別品目別一覧 従業員情報DTO
 * 
 * @author stakeuchi
 */
public class MrPlanMMPMrDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * 品目情報リスト
	 */
	private final List<MrPlanMMPProdDto> prodDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param prodDtoList 品目情報DTOリスト
	 */
	public MrPlanMMPMrDto(Integer jgiNo, String jgiName, List<MrPlanMMPProdDto> prodDtoList) {
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.prodDtoList = prodDtoList;
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 * 
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 品目情報リストを取得する。
	 * 
	 * @return prodDtoList 品目情報リスト
	 */
	public List<MrPlanMMPProdDto> getProdDtoList() {
		return prodDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
