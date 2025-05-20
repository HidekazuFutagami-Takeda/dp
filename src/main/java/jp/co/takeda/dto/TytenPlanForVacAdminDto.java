package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * (ワ)施設特約店別計画編集画面を表すDTO
 * 
 * @author siwamoto
 */
public class TytenPlanForVacAdminDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店名称
	 */
	private final String tmsTytemName;

	/**
	 * ＴＭＳ特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * B価ベース
	 */
	private final Long baseB;

	/**
	 * T価ベース（編集前）
	 */
	private final Long beforeBaseT;

	/**
	 * T価ベース
	 */
	private final Long baseT;

	/**
	 * 最終更新者
	 */
	private final String upDateJgiName;

	/**
	 * 最終更新日
	 */
	private final Date upDate;

	/**
	 * 特約店中計フラグ
	 */
	private final boolean tytenSumRowFlg;

	/**
	 * T/B変換率
	 */
	private final Double tbChangeRate;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytemName 特約店名称
	 * @param tmsTytenCd ＴＭＳ特約店コード
	 * @param baseB B価ベース
	 * @param beforeBaseT T価ベース（編集前）
	 * @param baseT T価ベース
	 * @param upDateJgiName 最終更新者
	 * @param upDate 最終更新日
	 * @param tytenSumRowFlg 特約店中計フラグ
	 * @param tbChangeRate T/B変換率
	 */
	public TytenPlanForVacAdminDto(String tmsTytemName, String tmsTytenCd, Long baseB, Long beforeBaseT, Long baseT, String upDateJgiName, Date upDate, boolean tytenSumRowFlg,
		Double tbChangeRate) {
		super();
		this.tmsTytemName = tmsTytemName;
		this.tmsTytenCd = tmsTytenCd;
		this.baseB = baseB;
		this.beforeBaseT = beforeBaseT;
		this.baseT = baseT;
		this.upDateJgiName = upDateJgiName;
		this.upDate = upDate;
		this.tytenSumRowFlg = tytenSumRowFlg;
		this.tbChangeRate = tbChangeRate;
	}

	/**
	 * 特約店名称を取得する。
	 * 
	 * @return 特約店名称
	 */
	public String getTmsTytemName() {
		return tmsTytemName;
	}

	/**
	 * ＴＭＳ特約店コードを取得する。
	 * 
	 * @return ＴＭＳ特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * B価ベースを取得する。
	 * 
	 * @return B価ベース
	 */
	public Long getBaseB() {
		return baseB;
	}

	/**
	 * T価ベース（編集前）を取得する。
	 * 
	 * @return T価ベース（編集前）
	 */
	public Long getBeforeBaseT() {
		return beforeBaseT;
	}

	/**
	 * T価ベースを取得する。
	 * 
	 * @return T価ベース
	 */
	public Long getBaseT() {
		return baseT;
	}

	/**
	 * 最終更新者を取得する。
	 * 
	 * @return 最終更新者
	 */
	public String getUpDateJgiName() {
		return upDateJgiName;
	}

	/**
	 * 最終更新日を取得する。
	 * 
	 * @return 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 特約店中計フラグを取得する。
	 * 
	 * @return 特約店中計フラグ
	 */
	public boolean getTytenSumRowFlg() {
		return tytenSumRowFlg;
	}

	/**
	 * T/B変換率を取得する。
	 * 
	 * @return T/B変換率
	 */
	public Double getTbChangeRate() {
		return tbChangeRate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
