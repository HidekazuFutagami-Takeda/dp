package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.model.WsPlanStatusForVac;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画立案ステータスチェック結果用DTO
 * 
 * @author nozaki
 */
public class WsStatusCheckResultForVacDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得したワクチン用特約店別計画立案ステータスのリスト
	 */
	private final List<WsPlanStatusForVac> wsPlanStatusForVacList;

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param wsPlanStatusForVacList DBから取得したワクチン用特約店別計画立案ステータスのリスト
	 */
	public WsStatusCheckResultForVacDto(List<MessageKey> errorMessageKeyList, List<WsPlanStatusForVac> wsPlanStatusForVacList) {
		super(errorMessageKeyList);
		this.wsPlanStatusForVacList = wsPlanStatusForVacList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param wsPlanStatusForVacList DBから取得したワクチン用特約店別計画立案ステータスのリスト
	 */
	public WsStatusCheckResultForVacDto(List<WsPlanStatusForVac> wsPlanStatusForVacList) {
		super();
		this.wsPlanStatusForVacList = wsPlanStatusForVacList;
	}

	/**
	 * DBから取得したワクチン用特約店別計画立案ステータスのリストを取得。
	 * 
	 * @return DBから取得したワクチン用特約店別計画立案ステータスのリスト
	 */
	public List<WsPlanStatusForVac> getWsPlanStatusForVac() {
		return wsPlanStatusForVacList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
