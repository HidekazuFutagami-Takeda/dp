package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * 特約店別計画追加 入力値DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanAddInputDto extends DpDto {
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
	 * @param brCode 支店コード
	 * @param distCode 営業所コード
	 * @param kaBaseKb 価ベース区分
	 * @param tmsTytenPlanAddDetailDto 特約店別計画入力情報
	 */
	public TmsTytenPlanAddInputDto(String tmsTytenCd, String brCode, String distCode, KaBaseKb kaBaseKb, List<TmsTytenPlanAddDetailDto> tmsTytenPlanAddDetailDto) {
		this.tmsTytenCd = tmsTytenCd;
		this.brCode = brCode;
		this.distCode = distCode;
		this.kaBaseKb = kaBaseKb;
		this.tmsTytenPlanAddDetailDto = tmsTytenPlanAddDetailDto;
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
	private String brCode;

	/**
	 * 営業所コード
	 */
	private String distCode;

	/**
	 * 特約店別計画入力情報
	 */
	private List<TmsTytenPlanAddDetailDto> tmsTytenPlanAddDetailDto;

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
	public String getBrCode() {
		return brCode;
	}

	/**
	 * 営業所コードを取得する
	 * 
	 * @return 営業所コード
	 */
	public String getDistCode() {
		return distCode;
	}

	/**
	 * 特約店別計画入力情報を取得する
	 * 
	 * @return 特約店別計画入力情報
	 */
	public List<TmsTytenPlanAddDetailDto> getTmsTytenPlanAddDetailDto() {
		return tmsTytenPlanAddDetailDto;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
