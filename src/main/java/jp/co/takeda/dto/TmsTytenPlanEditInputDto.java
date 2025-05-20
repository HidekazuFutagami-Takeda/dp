package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * 特約店別計画編集 入力値DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanEditInputDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// constructor
	// -------------------------------
	/**
	 * コンストラクタ
	 * 
	 * @param wsPlanList 特約店別計画リスト
	 * @param kaBaseKb 価ベース区分
	 */
	public TmsTytenPlanEditInputDto(List<WsPlan> wsPlanList, KaBaseKb kaBaseKb) {
		this.wsPlanList = wsPlanList;
		this.kaBaseKb = kaBaseKb;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画リスト
	 */
	private final List<WsPlan> wsPlanList;

	/**
	 * 価ベース区分
	 */
	private final KaBaseKb kaBaseKb;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店別計画リストを取得する。
	 * 
	 * @return 特約店別計画リスト
	 */
	public List<WsPlan> getWsPlanList() {
		return wsPlanList;
	}

	/**
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
