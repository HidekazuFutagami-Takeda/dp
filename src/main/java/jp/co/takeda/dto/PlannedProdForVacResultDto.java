package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用計画対象品目選択画面の検索結果用DTO
 * 
 * <pre>
 * 組織・従業員別の情報を表す
 * </pre>
 * 
 * @author stakeuchi
 */
public class PlannedProdForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織名
	 */
	private final String sosName;

	/**
	 * チーム名
	 */
	private final String teamName;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * 品目情報DTOリスト
	 */
	private final List<PlannedProdForVacResultProdInfoDto> prodInfoDtoList;

	/**
	 * 組織合計フラグ
	 * <ul>
	 * <li>TRUE =組織合計</li>
	 * <li>FALSE=組織合計でない</li>
	 * </ul>
	 */
	private final boolean isSosSum;

	/**
	 * チーム合計フラグ
	 * <ul>
	 * <li>TRUE =チーム合計</li>
	 * <li>FALSE=チーム合計でない</li>
	 * </ul>
	 */
	private final boolean isTeamSum;

	/**
	 * 全合計フラグ<br>
	 * <ul>
	 * <li>TRUE =全合計</li>
	 * <li>FALSE=全合計でない</li>
	 * </ul>
	 */
	private final boolean isAllSum;

	/**
	 * コンストラクタ
	 * 
	 * @param sosName 組織名
	 * @param teamName チーム名(NULL可)
	 * @param jgiName 従業員名
	 * @param prodDtoList 品目情報DTOリスト
	 * @param isTeamSum チーム合計フラグ
	 * @param isSosSum 組織合計フラグ
	 * @param isAllSum 全合計フラグ
	 */
	public PlannedProdForVacResultDto(String sosName, String teamName, String jgiName, List<PlannedProdForVacResultProdInfoDto> prodInfoDtoList, boolean isTeamSum,
		boolean isSosSum, boolean isAllSum) {
		this.sosName = sosName;
		this.teamName = teamName;
		this.jgiName = jgiName;
		this.prodInfoDtoList = prodInfoDtoList;
		this.isTeamSum = isTeamSum;
		this.isSosSum = isSosSum;
		this.isAllSum = isAllSum;
	}

	/**
	 * 組織名を取得する。
	 * 
	 * @return sosName 組織名
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * チーム名を取得する。
	 * 
	 * @return teamName チーム名
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * 従業員名を取得する。
	 * 
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 品目情報DTOリストを取得する。
	 * 
	 * @return prodInfoDtoList 品目情報DTOリスト
	 */
	public List<PlannedProdForVacResultProdInfoDto> getProdInfoDtoList() {
		return prodInfoDtoList;
	}

	/**
	 * チーム合計フラグを取得する。
	 * 
	 * @return isTeamSum チーム合計フラグ
	 */
	public boolean isTeamSum() {
		return isTeamSum;
	}

	/**
	 * 組織合計フラグを取得する。
	 * 
	 * @return isSosSum 組織合計フラグ
	 */
	public boolean isSosSum() {
		return isSosSum;
	}

	/**
	 * 全合計フラグを取得する。
	 * 
	 * @return isAllSum 全合計フラグ
	 */
	public boolean isAllSum() {
		return isAllSum;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
