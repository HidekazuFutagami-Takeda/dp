package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 施設特約店別計画 担当者一覧画面の検索結果用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanTeamResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 組織名称
	 */
	private final String sosName;

	/**
	 * 担当者一覧
	 */
	private final List<InsWsPlanMrResultDto> mrList;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd4 組織コード(チーム)
	 * @param sosName 組織名称
	 * @param mrList 担当者一覧
	 */
	public InsWsPlanTeamResultDto(String sosCd4, String sosName, List<InsWsPlanMrResultDto> mrList) {

		this.sosCd4 = sosCd4;
		this.sosName = sosName;
		this.mrList = mrList;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 * 
	 * @return 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
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
	 * 担当者一覧を取得する。
	 * 
	 * @return 担当者一覧
	 */
	public List<InsWsPlanMrResultDto> getMrList() {
		return mrList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
