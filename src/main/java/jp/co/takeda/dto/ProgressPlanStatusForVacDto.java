package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.view.JgiStatusSummaryForVac;
import jp.co.takeda.model.view.SosStatusSummaryForVac;

/**
 * 業務進捗表(ワ)[施設特約店別計画]を表すDTOクラス
 * 
 * @author tkawabata
 */
public class ProgressPlanStatusForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * (ワ)組織単位のステータスのサマリ
	 */
	private final SosStatusSummaryForVac sosStatusSummaryForVac;

	/**
	 * (ワ)担当者単位のステータスのサマリ
	 */
	private final List<JgiStatusSummaryForVac> jgiStatusSummaryForVac;

	/**
	 * コンストラクタ
	 * 
	 * @param sosStatusSummaryForVac (ワ)組織単位のステータスのサマリ(NULL可)
	 * @param jgiStatusSummaryForVac (ワ)担当者単位のステータスのサマリ(NULL可)
	 */
	public ProgressPlanStatusForVacDto(SosStatusSummaryForVac sosStatusSummaryForVac, List<JgiStatusSummaryForVac> jgiStatusSummaryForVac) {
		this.sosStatusSummaryForVac = sosStatusSummaryForVac;
		this.jgiStatusSummaryForVac = jgiStatusSummaryForVac;
	}

	/**
	 * (ワ)組織単位のステータスのサマリーを取得する。
	 * 
	 * @return (ワ)組織単位のステータスのサマリ
	 */
	public SosStatusSummaryForVac getSosStatusSummaryForVac() {
		return sosStatusSummaryForVac;
	}

	/**
	 * jgiStatusSummaryForVacを取得する。
	 * 
	 * @return (ワ)担当者単位のステータスのサマリ
	 */
	public List<JgiStatusSummaryForVac> getJgiStatusSummaryForVac() {
		return jgiStatusSummaryForVac;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
