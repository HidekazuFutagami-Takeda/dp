package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * ワクチン用特約店別計画追加 入力値DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanAddForVacInputDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分
	 */
	private KaBaseKb kaBaseKb;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 特約店部コード
	 */
	private final String brCode;

	/**
	 * エリア特約店Gコード
	 */
	private final String distCode;

	/**
	 * ワクチン用特約店別計画追加 明細DTOリスト
	 */
	private final List<TmsTytenPlanAddForVacDetailDto> detailList;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenCd 特約店コード
	 * @param brCode 特約店部コード
	 * @param distCode エリア特約店Gコード
	 * @param kaBaseKb 価ベース区分
	 * @param detailList ワクチン用特約店別計画追加 明細DTOリスト
	 */
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	public TmsTytenPlanAddForVacInputDto(String tmsTytenCd, String brCode, String distCode, KaBaseKb kaBaseKb, List<TmsTytenPlanAddForVacDetailDto> detailList) {
		this.tmsTytenCd = tmsTytenCd;
		this.brCode = brCode;
		this.distCode = distCode;
		this.kaBaseKb = kaBaseKb;
		this.detailList = detailList;
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分を取得する
	 * 
	 * @return 価ベース区分
	 */
	public KaBaseKb getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 特約店コードを取得する
	 * 
	 * @return 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店部コードを取得する
	 * 
	 * @return 特約店部コード
	 */
	public String getBrCode() {
		return brCode;
	}

	/**
	 * エリア特約店Gコードを取得する
	 * 
	 * @return エリア特約店Gコード
	 */
	public String getDistCode() {
		return distCode;
	}

	/**
	 * ワクチン用特約店別計画追加 明細DTOリストを取得する
	 * 
	 * @return ワクチン用特約店別計画追加 明細DTOリスト
	 */
	public List<TmsTytenPlanAddForVacDetailDto> getDetailList() {
		return detailList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
