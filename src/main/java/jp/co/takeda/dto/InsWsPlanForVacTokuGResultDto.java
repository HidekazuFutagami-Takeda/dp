package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用施設特約店別計画 担当者一覧画面の検索結果用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacTokuGResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(エリア特約店G)
	 */
	private final String sosCd;

	/**
	 * 組織名称
	 */
	private final String sosName;

	/**
	 * チーム一覧
	 */
	private final List<InsWsPlanForVacTeamResultDto> teamList;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード(エリア特約店G)
	 * @param sosName 組織名称
	 * @param teamList チーム一覧
	 */
	public InsWsPlanForVacTokuGResultDto(String sosCd, String sosName, List<InsWsPlanForVacTeamResultDto> teamList) {

		this.sosCd = sosCd;
		this.sosName = sosName;
		this.teamList = teamList;
	}

	/**
	 * 組織コード(エリア特約店G)を取得する。
	 * 
	 * @return 組織コード(エリア特約店G)
	 */
	public String getsosCd() {
		return sosCd;
	}

	/**
	 * 組織名称を取得する。
	 * 
	 * @return 組織名称
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * チーム一覧を取得する。
	 * 
	 * @return チーム一覧
	 */
	public List<InsWsPlanForVacTeamResultDto> getTeamList() {
		return teamList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
