package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特約店別計画編集画面を表すDTO
 * 
 * @author khashimoto
 */
public class ManageWsPlanForVacDto extends DpDto {

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
	private final List<ManageWsPlanForVacDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param prodName 品目名称
	 * @param detailList 検索結果リスト
	 */
	public ManageWsPlanForVacDto(String prodName, List<ManageWsPlanForVacDetailDto> detailList) {
		this.prodName = prodName;
		this.detailList = detailList;
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
	public List<ManageWsPlanForVacDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
