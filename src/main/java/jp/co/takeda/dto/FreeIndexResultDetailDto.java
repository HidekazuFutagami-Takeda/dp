package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * フリー項目検索結果詳細DTO(チーム単位)
 * 
 * @author nozaki
 */
public class FreeIndexResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * チーム名
	 */
	private final String teamName;

	/**
	 * 担当者フリー項目情報
	 */
	private final List<FreeIndexDto> mrPlanFreeIndexList;

	/**
	 * コンストラクタ
	 * 
	 * @param teamName チーム名
	 * @param mrPlanFreeIndexList 担当者別フリー項目
	 */
	public FreeIndexResultDetailDto(String teamName, List<FreeIndexDto> mrPlanFreeIndexList) {

		this.teamName = teamName;
		this.mrPlanFreeIndexList = mrPlanFreeIndexList;
	}

	/**
	 * チーム名を取得する。
	 * 
	 * @return チーム名
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * 担当者フリー項目情報を取得する。
	 * 
	 * @return 担当者フリー項目情報
	 */
	public List<FreeIndexDto> getMrPlanFreeIndexList() {
		return mrPlanFreeIndexList;
	}

	/**
	 * 担当者フリー項目情報の件数を取得する。
	 * 
	 * @return 担当者フリー項目情報の件数
	 */
	public int getFreeIndexListCount() {
		if (mrPlanFreeIndexList == null) {
			return 0;
		} else {
			return mrPlanFreeIndexList.size();
		}
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
