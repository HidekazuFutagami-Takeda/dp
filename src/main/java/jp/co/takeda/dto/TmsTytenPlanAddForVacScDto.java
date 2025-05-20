package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * ワクチン用特約店別計画追加 検索条件DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanAddForVacScDto extends DpDto {

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
	private final String brCodeInput;

	/**
	 * エリア特約店Gコード
	 */
	private final String distCodeInput;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenCd 特約店コード
	 * @param brCodeInput 特約店部コード
	 * @param distCodeInput エリア特約店Gコード
	 * @param kaBaseKb 価ベース区分
	 */
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	public TmsTytenPlanAddForVacScDto(String tmsTytenCd, String brCodeInput, String distCodeInput, KaBaseKb kaBaseKb) {
		this.tmsTytenCd = tmsTytenCd;
		this.brCodeInput = brCodeInput;
		this.distCodeInput = distCodeInput;
		this.kaBaseKb = kaBaseKb;
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
	public String getBrCodeInput() {
		return brCodeInput;
	}

	/**
	 * エリア特約店Gコードを取得する
	 * 
	 * @return エリア特約店Gコード
	 */
	public String getDistCodeInput() {
		return distCodeInput;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
