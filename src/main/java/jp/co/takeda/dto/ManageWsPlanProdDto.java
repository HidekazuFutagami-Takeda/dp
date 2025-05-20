package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (医)特約店品目別計画編集を表すDTO
 * 
 * @author siwamoto
 */
public class ManageWsPlanProdDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果リスト
	 */
	private final List<ManageWsPlanProdDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param tytenPlanProdAdminDtoList 検索結果リスト
	 */
	public ManageWsPlanProdDto(List<ManageWsPlanProdDetailDto> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 検索結果リストを取得する。
	 * 
	 * @return 検索結果リスト
	 */
	public List<ManageWsPlanProdDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
