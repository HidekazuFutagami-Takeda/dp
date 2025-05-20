package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画担当者別品目別一覧 チーム情報DTO
 * 
 * @author stakeuchi
 */
public class MrPlanMMPTeamDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 組織名(チーム)
	 */
	private final String sosName;

	/**
	 * 担当者情報リスト
	 */
	private final List<MrPlanMMPMrDto> mrDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd4 組織コード(チーム)
	 * @param sosName 組織名(チーム)
	 * @param mrDtoList 担当者情報リスト
	 */
	public MrPlanMMPTeamDto(String sosCd4, String sosName, List<MrPlanMMPMrDto> mrDtoList) {
		this.sosCd4 = sosCd4;
		this.sosName = sosName;
		this.mrDtoList = mrDtoList;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 * 
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 組織名(チーム)を取得する。
	 * 
	 * @return sosName 組織名(チーム)
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * 担当者情報リストを取得する。
	 * 
	 * @return mrDtoList 担当者情報リスト
	 */
	public List<MrPlanMMPMrDto> getMrDtoList() {
		return mrDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
