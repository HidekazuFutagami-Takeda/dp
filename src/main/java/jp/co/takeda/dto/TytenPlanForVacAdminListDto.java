package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特約店別計画編集画面を表すDTO
 * 
 * @author siwamoto
 */
public class TytenPlanForVacAdminListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 検索結果リスト
	 */
	private final List<TytenPlanForVacAdminDto> tytenPlanForVacAdminDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param prodName 品目名称
	 * @param tytenPlanForVacAdminDtoList 検索結果リスト
	 */
	public TytenPlanForVacAdminListDto(String prodName, List<TytenPlanForVacAdminDto> tytenPlanForVacAdminDtoList) {
		super();
		this.prodName = prodName;
		this.tytenPlanForVacAdminDtoList = tytenPlanForVacAdminDtoList;
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
	 * 検索結果リストを取得する。
	 * 
	 * @return 検索結果リスト
	 */
	public List<TytenPlanForVacAdminDto> getTytenPlanForVacAdminDtoList() {
		return tytenPlanForVacAdminDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
