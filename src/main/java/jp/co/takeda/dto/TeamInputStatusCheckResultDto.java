package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.model.TeamInputStatus;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * チーム別入力状況チェック結果用DTO
 * 
 * @author nozaki
 */
public class TeamInputStatusCheckResultDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得したチーム別入力状況のリスト
	 */
	private final List<TeamInputStatus> teamInputStatusList;

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param teamInputStatusList DBから取得したチーム別入力状況のリスト
	 */
	public TeamInputStatusCheckResultDto(List<MessageKey> errorMessageKeyList, List<TeamInputStatus> teamInputStatusList) {
		super(errorMessageKeyList);
		this.teamInputStatusList = teamInputStatusList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param teamInputStatusList DBから取得したチーム別入力状況のリスト
	 */
	public TeamInputStatusCheckResultDto(List<TeamInputStatus> teamInputStatusList) {
		super();
		this.teamInputStatusList = teamInputStatusList;
	}

	/**
	 * DBから取得したチーム別入力状況のリストを取得。
	 * 
	 * @return DBから取得したチーム別入力状況のリスト
	 */
	public List<TeamInputStatus> getTeamInputStatus() {
		return teamInputStatusList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
