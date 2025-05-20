package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.WsPlanStatusForVac;

/**
 * 業務進捗表(ワ)[特約店別計画]を表すDTOクラス
 * 
 * @author tkawabata
 */
public class ProgressPlanWsStatusForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * (ワ)特約店別計画ステータスのサマリ
	 */
	private final WsPlanStatusForVac wsPlanStatusForVac;

	/**
	 * コンストラクタ
	 * 
	 * @param wsPlanStatusForVacList (ワ)特約店別計画ステータスのサマリ
	 */
	public ProgressPlanWsStatusForVacDto(WsPlanStatusForVac wsPlanStatusForVac) {
		this.wsPlanStatusForVac = wsPlanStatusForVac;
	}

	/**
	 * (ワ)特約店別計画ステータスのサマリを取得する。
	 * 
	 * @return (ワ)特約店別計画ステータスのサマリ
	 */
	public WsPlanStatusForVac getWsPlanStatusForVac() {
		return wsPlanStatusForVac;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
