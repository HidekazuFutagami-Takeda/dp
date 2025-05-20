package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 *
 * (ワ)特約店別計画編集画面明細を表すDTO
 *
 * @author khashimoto
 */
public class ManageWsPlanForVacDetailDto extends DpDto {

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
	 * T価ベース
	 */
	private final Long baseT;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

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
	 * 計画立案対象外フラグ(当期用)
	 */
	private final boolean planTaiGaiFlgTok;

	/**
	 * コンストラクタ
	 *
	 * @param tmsTytemName 特約店名称
	 * @param tmsTytenCd 特約店コード
	 * @param baseB B価ベース
	 * @param baseT T価ベース
	 * @param seqKey シーケンスキー
	 * @param upDateJgiName 最終更新者
	 * @param upDate 最終更新日
	 * @param tytenSumRowFlg 特約店中計フラグ
	 * @param tbChangeRate T/B変換率
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public ManageWsPlanForVacDetailDto(
		String tmsTytemName, String tmsTytenCd, Long baseB, Long baseT, Long seqKey, String upDateJgiName,
		Date upDate, boolean tytenSumRowFlg, Double tbChangeRate, boolean planTaiGaiFlgTok) {
		this.tmsTytemName = tmsTytemName;
		this.tmsTytenCd = tmsTytenCd;
		this.baseB = baseB;
		this.baseT = baseT;
		this.seqKey = seqKey;
		this.upDateJgiName = upDateJgiName;
		this.upDate = upDate;
		this.tytenSumRowFlg = tytenSumRowFlg;
		this.tbChangeRate = tbChangeRate;
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * 特約店名称を取得する。
	 *
	 * @return tmsTytemName 特約店名称
	 */
	public String getTmsTytemName() {
		return tmsTytemName;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * B価ベースを取得する。
	 *
	 * @return baseB B価ベース
	 */
	public Long getBaseB() {
		return baseB;
	}

	/**
	 * T価ベースを取得する。
	 *
	 * @return baseT T価ベース
	 */
	public Long getBaseT() {
		return baseT;
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return baseT シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return upDateJgiName 最終更新者
	 */
	public String getUpDateJgiName() {
		return upDateJgiName;
	}

	/**
	 * 最終更新日を取得する。
	 *
	 * @return upDate 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 特約店中計フラグを取得する。
	 *
	 * @return tytenSumRowFlg 特約店中計フラグ
	 */
	public boolean isTytenSumRowFlg() {
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

	/**
	 * 計画立案対象外フラグ(当期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
