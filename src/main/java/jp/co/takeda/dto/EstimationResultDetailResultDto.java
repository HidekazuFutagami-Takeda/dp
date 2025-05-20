package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算結果詳細の検索結果用DTO
 * 
 * @author stakeuchi
 */
public class EstimationResultDetailResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 検索結果の共通情報を表すDTO
	 */
	private final EstimationResultDetailResultInfoDto infoDto;

	/**
	 * 検索結果の一覧情報を表すDTO
	 */
	private final List<EstimationResultDetailResultRowDto> rowDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param infoDto 検索結果の共通情報を表すDTO
	 * @param rowDtoList 検索結果の一覧情報を表すDTOのリスト
	 */
	public EstimationResultDetailResultDto(EstimationResultDetailResultInfoDto infoDto, List<EstimationResultDetailResultRowDto> rowDtoList) {
		this.infoDto = infoDto;
		this.rowDtoList = rowDtoList;
	}

	/**
	 * 検索結果の共通情報を表すDTOを取得する。
	 * 
	 * @return infoDto 検索結果の共通情報を表すDTO
	 */
	public EstimationResultDetailResultInfoDto getInfoDto() {
		return infoDto;
	}

	/**
	 * 検索結果のテーブル行を表すDTOを取得する。
	 * 
	 * @return rowDtoList 検索結果のテーブル行を表すDTO
	 */
	public List<EstimationResultDetailResultRowDto> getRowDtoList() {
		return rowDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
