package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 *
 * (医)施設特約店別計画編集画面を表すDTO
 *
 * @author siwamoto
 */
public class ManageInsWsPlanDetailWsDto extends DpDto {

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
	 * Y価ベース
	 */
	private final Long baseY;

	/**
	 * T価ベース（編集前）
	 */
	private final Long beforeBaseT;

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
	private final String upJgiName;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private final boolean planTaiGaiFlgTok;

	/**
	 * コンストラクタ
	 *
	 * @param tmsTytemName 特約店名称
	 * @param tmsTytenCd ＴＭＳ特約店コード
	 * @param baseY Y価ベース
	 * @param beforeBaseT T価ベース（編集前）
	 * @param baseT T価ベース
	 * @param tyChangeRate T/Y変換率
	 * @param seqKey シーケンスキー
	 * @param upDateJgiName 最終更新日時
	 * @param upDate 最終更新日時
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public ManageInsWsPlanDetailWsDto(String tmsTytemName, String tmsTytenCd, Long baseY, Long beforeBaseT, Long baseT, Long seqKey, String upJgiName, Date upDate, boolean planTaiGaiFlgTok) {
		this.tmsTytemName = tmsTytemName;
		this.tmsTytenCd = tmsTytenCd;
		this.baseY = baseY;
		this.beforeBaseT = beforeBaseT;
		this.baseT = baseT;
		this.seqKey = seqKey;
		this.upJgiName = upJgiName;
		this.upDate = upDate;
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
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
	 * Y価ベースを取得する。
	 *
	 * @return Y価ベース
	 */
	public Long getBaseY() {
		return baseY;
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
	 * シーケンスキーを取得する。
	 *
	 * @return seqKey シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
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
