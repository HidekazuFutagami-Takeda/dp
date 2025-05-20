package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.model.InsWsPlanStatusForVac;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用施設特約店別計画立案ステータスチェック結果用DTO
 * 
 * @author nozaki
 */
public class InsWsStatusCheckResultForVacDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得した施設特約店別計画立案ステータスのリスト
	 */
	private final List<InsWsPlanStatusForVac> insWsPlanStatusForVacList;

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param insWsPlanStatusForVacList DBから取得したワクチン用施設特約店別計画立案ステータスのリスト
	 */
	public InsWsStatusCheckResultForVacDto(List<MessageKey> errorMessageKeyList, List<InsWsPlanStatusForVac> insWsPlanStatusForVacList) {
		super(errorMessageKeyList);
		this.insWsPlanStatusForVacList = insWsPlanStatusForVacList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param insWsPlanStatusForVacList DBから取得したワクチン用施設特約店別計画立案ステータスのリスト
	 */
	public InsWsStatusCheckResultForVacDto(List<InsWsPlanStatusForVac> insWsPlanStatusForVacList) {
		super();
		this.insWsPlanStatusForVacList = insWsPlanStatusForVacList;
	}

	/**
	 * DBから取得した施設特約店別計画立案ステータスのリストを取得。
	 * 
	 * @return DBから取得したワクチン用施設特約店別計画立案ステータスのリスト
	 */
	public List<InsWsPlanStatusForVac> getInsWsPlanStatusForVac() {
		return insWsPlanStatusForVacList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
