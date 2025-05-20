package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.model.InsDocPlanStatus;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設医師別計画立案ステータスチェック結果用DTO
 * 
 * @author nozaki
 */
public class InsDocStatusCheckResultDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得した施設医師別計画立案ステータスのリスト
	 */
	private final List<InsDocPlanStatus> insDocPlanStatusList;

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param insDocPlanStatusList DBから取得した施設医師別計画立案ステータスのリスト
	 */
	public InsDocStatusCheckResultDto(List<MessageKey> errorMessageKeyList, List<InsDocPlanStatus> insDocPlanStatusList) {
		super(errorMessageKeyList);
		this.insDocPlanStatusList = insDocPlanStatusList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param insDocPlanStatusList 施設医師別計画立案ステータスのリスト
	 */
	public InsDocStatusCheckResultDto(List<InsDocPlanStatus> insDocPlanStatusList) {
		super();
		this.insDocPlanStatusList = insDocPlanStatusList;
	}

	/**
	 * DBから取得した施設医師別計画立案ステータスのリストを取得。
	 * 
	 * @return DBから取得した施設医師別計画立案ステータスのリスト
	 */
	public List<InsDocPlanStatus> getInsDocPlanStatus() {
		return insDocPlanStatusList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
