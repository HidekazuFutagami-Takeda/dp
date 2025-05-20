package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.model.OfficePlanStatus;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 営業所計画ステータスチェック結果用DTO
 * 
 * @author nozaki
 */
public class OfficeStatusCheckResultDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得した営業所計画ステータスのリスト
	 */
	private final List<OfficePlanStatus> officePlanStatusList;

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param officePlanStatusList DBから取得した営業所計画ステータスのリスト
	 */
	public OfficeStatusCheckResultDto(List<MessageKey> errorMessageKeyList, List<OfficePlanStatus> officePlanStatusList) {
		super(errorMessageKeyList);
		this.officePlanStatusList = officePlanStatusList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param officePlanStatusList DBから取得した営業所計画ステータスのリスト
	 */
	public OfficeStatusCheckResultDto(List<OfficePlanStatus> officePlanStatusList) {
		super();
		this.officePlanStatusList = officePlanStatusList;
	}

	/**
	 * DBから取得した営業所計画ステータスのリストを取得。
	 * 
	 * @return 営業所計画ステータスのリスト
	 */
	public List<OfficePlanStatus> getOfficePlanStatus() {
		return officePlanStatusList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
