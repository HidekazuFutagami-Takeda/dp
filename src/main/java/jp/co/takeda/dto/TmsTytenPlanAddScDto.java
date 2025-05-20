package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * 特約店別計画追加 検索条件DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanAddScDto extends DpDto {
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
	 * @param tmsTytenCd 特約店コード
	 * @param brCodeInput 支店コード
	 * @param distCodeInput 営業所コード
	 * @param kaBaseKb 価ベース区分
	 */
	public TmsTytenPlanAddScDto(String tmsTytenCd, String brCodeInput, String distCodeInput, KaBaseKb kaBaseKb) {
		this.tmsTytenCd = tmsTytenCd;
		this.brCodeInput = brCodeInput;
		this.distCodeInput = distCodeInput;
		this.kaBaseKb = kaBaseKb;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 価ベース区分
	 */
	private KaBaseKb kaBaseKb;

	/**
	 * 特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 支店コード
	 */
	private String brCodeInput;

	/**
	 * 営業所コード
	 */
	private String distCodeInput;

	// -------------------------------
	// getter
	// -------------------------------
	/**
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
	 * 支店コードを取得する
	 * 
	 * @return 支店コード
	 */
	public String getBrCodeInput() {
		return brCodeInput;
	}

	/**
	 * 営業所コードを取得する
	 * 
	 * @return 営業所コード
	 */
	public String getDistCodeInput() {
		return distCodeInput;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
