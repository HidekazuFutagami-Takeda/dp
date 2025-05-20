package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 組織別計画の検索結果DTO
 * 
 * @author stakeuchi
 */
public class SosPlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細合計行DTO
	 */
	private final SosPlanResultDetailTotalDto detailTotal;

	/**
	 * 明細DTOリスト
	 */
	private final List<SosPlanResultDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param detailTotal 明細合計DTO
	 * @param detailList 明細DTOリスト
	 */
	public SosPlanResultDto(SosPlanResultDetailTotalDto detailTotal, List<SosPlanResultDetailDto> detailList) {
		this.detailTotal = detailTotal;
		this.detailList = detailList;
	}

	/**
	 * 明細合計DTOを取得する。
	 * 
	 * @return detailList 明細合計リスト
	 */
	public SosPlanResultDetailTotalDto getDetailTotal() {
		return detailTotal;
	}

	/**
	 * 明細DTOリストを取得する。
	 * 
	 * @return detailList 明細DTOリスト
	 */
	public List<SosPlanResultDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
