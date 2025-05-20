package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画の更新用DTO
 * 
 * @author stakeuchi
 */
public class InsPlanForVacUpdateDto extends DpDto {

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
	 * B価ベース 更新前
	 */
	private final Long bBaseValueBefore;

	/**
	 * B価ベース 更新後
	 */
	private final Long bBaseValueAfter;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param insNo 施設コード
	 * @param prodCode 品目コード
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param bBaseValueBefore B価ベース 更新前
	 * @param bBaseValueAfter B価ベース 更新後
	 */
	public InsPlanForVacUpdateDto(String insNo, String prodCode, Long seqKey, Date upDate, Long bBaseValueBefore, Long bBaseValueAfter) {
		this.insNo = insNo;
		this.prodCode = prodCode;
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.bBaseValueBefore = bBaseValueBefore;
		this.bBaseValueAfter = bBaseValueAfter;
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
	 * B価ベース 更新前を取得する。
	 * 
	 * @return bBaseValueBefore B価ベース 更新前
	 */
	public Long getBBaseValueBefore() {
		return bBaseValueBefore;
	}

	/**
	 * B価ベース 更新後を取得する。
	 * 
	 * @return bBaseValueAfter B価ベース 更新後
	 */
	public Long getBBaseValueAfter() {
		return bBaseValueAfter;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
