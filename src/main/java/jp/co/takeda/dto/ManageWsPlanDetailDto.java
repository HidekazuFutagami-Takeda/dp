package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 *
 * (医)特約店別計画編集画面明細を表すDTO
 *
 * @author siwamoto
 */
public class ManageWsPlanDetailDto extends DpDto {

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
	 * [UH] Y価ベース
	 */
	private final Long baseYUH;

	/**
	 * [UH] T価ベース（編集前）
	 */
	private final Long beforeBaseTUH;

	/**
	 * [UH] T価ベース
	 */
	private final Long baseTUH;

	/**
	 * [UH] シーケンスキー
	 */
	private final Long seqKeyUH;

	/**
	 * [UH] 最終更新者
	 */
	private final String upDateJgiNameUH;

	/**
	 * [UH] 最終更新日
	 */
	private final Date upDateUH;

	/**
	 * [P] Y価ベース
	 */
	private final Long baseYP;

	/**
	 * [P] T価ベース（編集前）
	 */
	private final Long beforeBaseTP;

	/**
	 * [P] T価ベース
	 */
	private final Long baseTP;

	/**
	 * [P] シーケンスキー
	 */
	private final Long seqKeyP;

	/**
	 * [P] 最終更新者
	 */
	private final String upDateJgiNameP;

	/**
	 * [P] 最終更新日
	 */
	private final Date upDateP;

	/**
	 * [Z] Y価ベース
	 */
	private final Long baseYZ;

	/**
	 * [Z] T価ベース（編集前）
	 */
	private final Long beforeBaseTZ;

	/**
	 * [Z] T価ベース
	 */
	private final Long baseTZ;

	/**
	 * [Z] シーケンスキー
	 */
	private final Long seqKeyZ;

	/**
	 * [Z] 最終更新者
	 */
	private final String upDateJgiNameZ;

	/**
	 * [Z] 最終更新日
	 */
	private final Date upDateZ;

	/**
	 * 特約店中計フラグ
	 */
	private final boolean tytenSumRowFlg;

	/**
	 * [UH] T/Y変換率
	 */
	private final Double tyChangeRateUH;

	/**
	 * [P] T/Y変換率
	 */
	private final Double tyChangeRateP;

	/**
	 * [Z] T/Y変換率
	 */
	private final Double tyChangeRateZ;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private final boolean planTaiGaiFlgTok;

	/**
	 * コンストラクタ
	 *
	 * @param tmsTytemName 特約店名称
	 * @param tmsTytenCd 特約店コード
	 * @param baseYUH [UH] Y価ベース
	 * @param beforeBaseTUH [UH] T価ベース（編集前）
	 * @param baseTUH [UH] T価ベース
	 * @param seqKeyUH [UH] シーケンスキー
	 * @param upDateJgiNameUH [UH] 最終更新者
	 * @param upDateUH [UH] 最終更新日
	 * @param baseYP [P] Y価ベース
	 * @param beforeBaseTP [P] T価ベース（編集前）
	 * @param baseTP [P] T価ベース
	 * @param seqKeyP [P] シーケンスキー
	 * @param upDateJgiNameP [P] 最終更新者
	 * @param upDateP [P] 最終更新日
	 * @param baseYZ [Z] Y価ベース
	 * @param beforeBaseTZ [Z] T価ベース（編集前）
	 * @param baseTZ [Z] T価ベース
	 * @param seqKeyZ [Z] シーケンスキー
	 * @param upDateJgiNameZ [Z] 最終更新者
	 * @param upDateZ [Z] 最終更新日
	 * @param tytenSumRowFlg 特約店中計フラグ
	 * @param tyChangeRateUH [UH] T/Y変換率
	 * @param tyChangeRateP [P] T/Y変換率
	 * @param tyChangeRateZ [Z] T/Y変換率
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public ManageWsPlanDetailDto(String tmsTytemName, String tmsTytenCd,
			Long baseYUH, Long beforeBaseTUH, Long baseTUH, Long seqKeyUH, String upDateJgiNameUH, Date upDateUH,
			Long baseYP, Long beforeBaseTP, Long baseTP, Long seqKeyP, String upDateJgiNameP, Date upDateP,
			Long baseYZ, Long beforeBaseTZ, Long baseTZ, Long seqKeyZ, String upDateJgiNameZ, Date upDateZ,
			boolean tytenSumRowFlg, Double tyChangeRateUH, Double tyChangeRateP, Double tyChangeRateZ, boolean planTaiGaiFlgTok) {
		this.tmsTytemName = tmsTytemName;
		this.tmsTytenCd = tmsTytenCd;
		this.baseYUH = baseYUH;
		this.beforeBaseTUH = beforeBaseTUH;
		this.baseTUH = baseTUH;
		this.seqKeyUH = seqKeyUH;
		this.upDateJgiNameUH = upDateJgiNameUH;
		this.upDateUH = upDateUH;
		this.baseYP = baseYP;
		this.beforeBaseTP = beforeBaseTP;
		this.baseTP = baseTP;
		this.seqKeyP = seqKeyP;
		this.upDateJgiNameP = upDateJgiNameP;
		this.upDateP = upDateP;
		this.baseYZ = baseYZ;
		this.beforeBaseTZ = beforeBaseTZ;
		this.baseTZ = baseTZ;
		this.seqKeyZ = seqKeyZ;
		this.upDateJgiNameZ = upDateJgiNameZ;
		this.upDateZ = upDateZ;
		this.tytenSumRowFlg = tytenSumRowFlg;
		this.tyChangeRateUH = tyChangeRateUH;
		this.tyChangeRateP = tyChangeRateP;
		this.tyChangeRateZ = tyChangeRateZ;
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
	 * [UH] Y価ベースを取得する。
	 *
	 * @return baseTUH [UH] Y価ベース
	 */
	public Long getBaseYUH() {
		return baseYUH;
	}

	/**
	 * [UH] T価ベース（編集前）を取得する。
	 *
	 * @return beforeBaseTUH T価ベース（編集前）
	 */
	public Long getBeforeBaseTUH() {
		return beforeBaseTUH;
	}

	/**
	 * [UH] T価ベースを取得する。
	 *
	 * @return baseTUH [UH] T価ベース
	 */
	public Long getBaseTUH() {
		return baseTUH;
	}

	/**
	 * [UH] シーケンスキーを取得する。
	 *
	 * @return baseTUH [UH] シーケンスキー
	 */
	public Long getSeqKeyUH() {
		return seqKeyUH;
	}

	/**
	 * [UH] 最終更新者を取得する。
	 *
	 * @return upDateJgiNameUH [UH] 最終更新者
	 */
	public String getUpDateJgiNameUH() {
		return upDateJgiNameUH;
	}

	/**
	 * [UH] 最終更新日を取得する。
	 *
	 * @return upDateUH [UH] 最終更新日
	 */
	public Date getUpDateUH() {
		return upDateUH;
	}

	/**
	 * [P] Y価ベースを取得する。
	 *
	 * @return baseTP [P] Y価ベース
	 */
	public Long getBaseYP() {
		return baseYP;
	}

	/**
	 * [P] T価ベース（編集前）を取得する。
	 *
	 * @return beforeBaseTP [P] T価ベース
	 */
	public Long getBeforeBaseTP() {
		return beforeBaseTP;
	}

	/**
	 * [P] T価ベースを取得する。
	 *
	 * @return baseTP [P] T価ベース（編集前）
	 */
	public Long getBaseTP() {
		return baseTP;
	}

	/**
	 * [P] シーケンスキーを取得する。
	 *
	 * @return seqKeyP [P] シーケンスキー
	 */
	public Long getSeqKeyP() {
		return seqKeyP;
	}

	/**
	 * [P] 最終更新者を取得する。
	 *
	 * @return upDateJgiNameP [P] 最終更新者
	 */
	public String getUpDateJgiNameP() {
		return upDateJgiNameP;
	}

	/**
	 * [P] 最終更新日を取得する。
	 *
	 * @return upDateP [P] 最終更新日
	 */
	public Date getUpDateP() {
		return upDateP;
	}

	/**
	 * [Z] Y価ベースを取得する。
	 *
	 * @return baseTZ [Z] Y価ベース
	 */
	public Long getBaseYZ() {
		return baseYZ;
	}

	/**
	 * [Z] T価ベース（編集前）を取得する。
	 *
	 * @return beforeBaseTZ [Z] T価ベース
	 */
	public Long getBeforeBaseTZ() {
		return beforeBaseTZ;
	}

	/**
	 * [Z] T価ベースを取得する。
	 *
	 * @return baseTZ [Z] T価ベース（編集前）
	 */
	public Long getBaseTZ() {
		return baseTZ;
	}

	/**
	 * [Z] シーケンスキーを取得する。
	 *
	 * @return seqKeyZ [Z] シーケンスキー
	 */
	public Long getSeqKeyZ() {
		return seqKeyZ;
	}

	/**
	 * [Z] 最終更新者を取得する。
	 *
	 * @return upDateJgiNameZ [Z] 最終更新者
	 */
	public String getUpDateJgiNameZ() {
		return upDateJgiNameZ;
	}

	/**
	 * [Z] 最終更新日を取得する。
	 *
	 * @return upDateZ [Z] 最終更新日
	 */
	public Date getUpDateZ() {
		return upDateZ;
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
	 * [UH] T/Y変換率を取得する。
	 *
	 * @return [UH] T/Y変換率
	 */
	public Double getTyChangeRateUH() {
		return tyChangeRateUH;
	}

	/**
	 * [P] T/Y変換率を取得する。
	 *
	 * @return [P] T/Y変換率
	 */
	public Double getTyChangeRateP() {
		return tyChangeRateP;
	}

	/**
	 * [Z] T/Y変換率を取得する。
	 *
	 * @return [Z] T/Y変換率
	 */
	public Double getTyChangeRateZ() {
		return tyChangeRateZ;
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
