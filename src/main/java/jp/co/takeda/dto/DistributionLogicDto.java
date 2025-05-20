package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分ロジッククラスの結果格納DTO
 * 
 * @author khashimoto
 */
public class DistributionLogicDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 配分ミス情報
	 */
	protected final DistributionMissDto distributionMissDto;

	/**
	 * 施設特約店別計画を表すDTOリスト
	 */
	protected final List<InsWsPlanDto> insWsPlanDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param distributionMissDto 配分ミス情報
	 * @param insWsPlanDtoList 施設特約店別計画を表すDTOリスト
	 */
	public DistributionLogicDto(final DistributionMissDto distributionMissDto, final List<InsWsPlanDto> insWsPlanDtoList) {
		this.distributionMissDto = distributionMissDto;
		this.insWsPlanDtoList = insWsPlanDtoList;
	}

	/**
	 * 配分ミス情報を取得する。
	 * 
	 * @return 配分ミス情報
	 */
	public DistributionMissDto getDistributionMissDto() {
		return distributionMissDto;
	}

	/**
	 * 施設特約店別計画を表すDTOリストを取得する。
	 * 
	 * @return 施設特約店別計画を表すDTOリスト
	 */
	public List<InsWsPlanDto> getInsWsPlanDtoList() {
		return insWsPlanDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
