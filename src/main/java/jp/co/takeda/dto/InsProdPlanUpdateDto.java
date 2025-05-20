package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設品目別計画の更新用DTO
 * 
 * @author stakeuchi
 */
public class InsProdPlanUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * Y価ベース 更新前
	 */
	private final Long yBaseValueBefore;

	/**
	 * Y価ベース 更新後
	 */
	private final Long yBaseValueAfter;

	/**
	 * コンストラクタ
	 * 
	 * @param insNo 施設コード
	 * @param prodCode 品目コード
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param yBaseValueBefore Y価ベース 更新前
	 * @param yBaseValueAfter Y価ベース 更新後
	 */
	public InsProdPlanUpdateDto(String insNo, String prodCode, Long seqKey, Date upDate, Long yBaseValueBefore, Long yBaseValueAfter) {
		this.insNo = insNo;
		this.prodCode = prodCode;
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.yBaseValueBefore = yBaseValueBefore;
		this.yBaseValueAfter = yBaseValueAfter;
	}

	/**
	 * 施設コードを取得する。
	 * 
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 品目コードを取得する。
	 * 
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
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
	 * 最終更新日時を取得する。
	 * 
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * Y価ベース 更新前を取得する。
	 * 
	 * @return yBaseValueBefore Y価ベース 更新前
	 */
	public Long getYBaseValueBefore() {
		return yBaseValueBefore;
	}

	/**
	 * Y価ベース 更新後を取得する。
	 * 
	 * @return yBaseValueAfter Y価ベース 更新後
	 */
	public Long getYBaseValueAfter() {
		return yBaseValueAfter;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
