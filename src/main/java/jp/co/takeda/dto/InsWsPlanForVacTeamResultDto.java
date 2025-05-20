package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * ワクチン用施設特約店別計画 担当者一覧画面の検索結果用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacTeamResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd;

	/**
	 * 組織名称（チーム）
	 */
	private final String sosName;

	/**
	 * 担当者一覧
	 */
	private final List<InsWsPlanForVacMrResultDto> mrList;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード(チーム)
	 * @param sosName 組織名称（チーム）
	 * @param mrList 担当者一覧
	 */
	public InsWsPlanForVacTeamResultDto(String sosCd, String sosName, List<InsWsPlanForVacMrResultDto> mrList) {

		this.sosCd = sosCd;
		this.sosName = sosName;
		this.mrList = mrList;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 * 
	 * @return 組織コード(チーム)
	 */
	public String getsosCd() {
		return sosCd;
	}

	/**
	 * 組織名称(チーム)を取得する。
	 * 
	 * @return 組織名称(チーム)
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * 担当者一覧を取得する。
	 * 
	 * @return 担当者一覧
	 */
	public List<InsWsPlanForVacMrResultDto> getMrList() {
		return mrList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
