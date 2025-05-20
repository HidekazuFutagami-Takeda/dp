package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 品目別計画積上結果の更新用DTO
 */
public class ProdPlanSummaryUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 施設区分
	 */
	private final InsType insType;

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
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目コード
	 * @param insType 施設区分
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param yBaseValueBefore Y価ベース 更新前
	 * @param yBaseValueAfter Y価ベース 更新後
	 */
	public ProdPlanSummaryUpdateDto(Integer jgiNo, String prodCode, InsType insType, Long seqKey, Date upDate, Long yBaseValueBefore, Long yBaseValueAfter) {
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insType = insType;
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.yBaseValueBefore = yBaseValueBefore;
		this.yBaseValueAfter = yBaseValueAfter;
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
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
	 * 施設区分を取得する。
	 * 
	 * @return insType 施設区分
	 */
	public InsType getInsType() {
		return insType;
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
