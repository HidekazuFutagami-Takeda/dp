package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * ワクチン用特約店別計画編集 入力値DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanEditForVacInputDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * ワクチン用特約店別計画リスト
	 */
	private final List<WsPlanForVac> wsPlanForVacList;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分
	 */
	private final KaBaseKb kaBaseKb;

	/**
	 * コンストラクタ
	 * 
	 * @param wsPlanForVacList ワクチン用特約店別計画リスト
	 * @param kaBaseKb 価ベース区分
	 */
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	public TmsTytenPlanEditForVacInputDto(List<WsPlanForVac> wsPlanForVacList, KaBaseKb kaBaseKb) {
		this.wsPlanForVacList = wsPlanForVacList;
		this.kaBaseKb = kaBaseKb;
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * ワクチン用特約店別計画リストを取得する。
	 * 
	 * @return wsPlanForVacList ワクチン用特約店別計画リスト
	 */
	public List<WsPlanForVac> getWsPlanForVacList() {
		return wsPlanForVacList;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分を取得する。
	 *
	 * @return 価ベース区分
	 */
	public KaBaseKb getKaBaseKb() {
		return kaBaseKb;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
