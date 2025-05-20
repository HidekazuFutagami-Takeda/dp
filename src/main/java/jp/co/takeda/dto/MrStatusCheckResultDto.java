package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.model.MrPlanStatus;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画立案ステータスチェック結果用DTO
 * 
 * @author nozaki
 */
public class MrStatusCheckResultDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得した担当者別計画立案ステータスのリスト
	 */
	private final List<MrPlanStatus> mrPlanStatusList;

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param mrPlanStatusList DBから取得した担当者別計画立案ステータスのリスト
	 */
	public MrStatusCheckResultDto(List<MessageKey> errorMessageKeyList, List<MrPlanStatus> mrPlanStatusList) {
		super(errorMessageKeyList);
		this.mrPlanStatusList = mrPlanStatusList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param mrPlanStatusList DBから取得した担当者別計画立案ステータスのリスト
	 */
	public MrStatusCheckResultDto(List<MrPlanStatus> mrPlanStatusList) {
		super();
		this.mrPlanStatusList = mrPlanStatusList;
	}

	/**
	 * DBから取得した担当者別計画立案ステータスのリストを取得。
	 * 
	 * @return DBから取得した担当者別計画立案ステータスのリスト
	 */
	public List<MrPlanStatus> getMrPlanStatus() {
		return mrPlanStatusList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
