package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.model.WsPlanStatus;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店別計画立案ステータスチェック結果用DTO
 * 
 * @author nozaki
 */
public class WsStatusCheckResultDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得した特約店別計画立案ステータスのリスト
	 */
	private final List<WsPlanStatus> wsPlanStatusList;

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param wsPlanStatusList DBから取得した特約店別計画立案ステータスのリスト
	 */
	public WsStatusCheckResultDto(List<MessageKey> errorMessageKeyList, List<WsPlanStatus> wsPlanStatusList) {
		super(errorMessageKeyList);
		this.wsPlanStatusList = wsPlanStatusList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param wsPlanStatusList DBから取得した特約店別計画立案ステータスのリスト
	 */
	public WsStatusCheckResultDto(List<WsPlanStatus> wsPlanStatusList) {
		super();
		this.wsPlanStatusList = wsPlanStatusList;
	}

	/**
	 * DBから取得した特約店別計画立案ステータスのリストを取得。
	 * 
	 * @return DBから取得した特約店別計画立案ステータスのリスト
	 */
	public List<WsPlanStatus> getWsPlanStatus() {
		return wsPlanStatusList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
