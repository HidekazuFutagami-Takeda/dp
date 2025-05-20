package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * (医)特約店別計画編集画面登録用DTO
 * 
 * @author siwamoto
 */
public class ManageWsPlanForVacEntryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * ＴＭＳ特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * T価ベース 更新前
	 */
	private final Long baseTBefore;

	/**
	 * T価ベース 更新後（入力値）
	 */
	private final Long baseTAfter;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd 特約店コード
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param baseTBefore T価ベース 更新前
	 * @param baseTAfter T価ベース 更新後（入力値）
	 */
	public ManageWsPlanForVacEntryDto(String prodCode, String tmsTytenCd, Long seqKey, Date upDate, Long baseTBefore, Long baseTAfter) {
		super();
		this.prodCode = prodCode;
		this.tmsTytenCd = tmsTytenCd;
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.baseTBefore = baseTBefore;
		this.baseTAfter = baseTAfter;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
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
	 * シーケンスキーを取得する。
	 * 
	 * @return seqKey シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
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
	 * T価ベース 更新前を取得する。
	 * 
	 * @return T価ベース 更新前
	 */
	public Long getBaseTBefore() {
		return baseTBefore;
	}

	/**
	 * T価ベース 更新後を取得する。
	 * 
	 * @return baseTAfter T価ベース 更新後
	 */
	public Long getBaseTAfter() {
		return baseTAfter;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
