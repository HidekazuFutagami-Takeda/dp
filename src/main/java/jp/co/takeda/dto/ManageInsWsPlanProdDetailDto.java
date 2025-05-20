package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * （医）施設特約店品目別計画編集を表すDTO
 * 
 * @author siwamoto
 */
public class ManageInsWsPlanProdDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

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
	private final String upDateJgiName;

	/**
	 * 最終更新日
	 */
	private final Date upDate;

	/**
	 * T/Y変換率
	 */
	private final Double tyChangeRate;

	/**
	 * 削除予定施設フラグ
	 * <ul>
	 * <li>TRUE = 削除予定</li>
	 * <li>FALSE = 削除予定でない</li>
	 * </ul>
	 */
	private final boolean isDeletePlan;

	/**
	 * 編集可能フラグ
	 * <ul>
	 * <li>TRUE = 編集可能</li>
	 * <li>FALSE = 編集可能でない</li>
	 * </ul>
	 */
	private final boolean enableEdit;

	/**
	 * コンストラクタ
	 * 
	 * @param prodName 品目名称
	 * @param prodCode 品目固定コード
	 * @param baseY Y価ベース
	 * @param beforeBaseT T価ベース（編集前）
	 * @param baseT T価ベース
	 * @param seqKey シーケンスキー
	 * @param upDateJgiName 最終更新者
	 * @param upDate 最終更新日
	 * @param tyChangeRate T/Y変換率
	 * @param isDeletePlan 削除予定施設フラグ
	 * @param enableEdit 編集可能フラグ
	 */
	public ManageInsWsPlanProdDetailDto(String prodName, String prodCode, Long baseY, Long beforeBaseT, Long baseT, Long seqKey, String upDateJgiName, Date upDate,
		Double tyChangeRate, boolean isDeletePlan, boolean enableEdit) {
		this.prodName = prodName;
		this.prodCode = prodCode;
		this.baseY = baseY;
		this.beforeBaseT = beforeBaseT;
		this.baseT = baseT;
		this.seqKey = seqKey;
		this.upDateJgiName = upDateJgiName;
		this.upDate = upDate;
		this.tyChangeRate = tyChangeRate;
		this.isDeletePlan = isDeletePlan;
		this.enableEdit = enableEdit;
	}

	/**
	 * 品目名称を取得する。
	 * 
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
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
	 * T/Y変換率を取得する。
	 * 
	 * @return T/Y変換率
	 */
	public Double getTyChangeRate() {
		return tyChangeRate;
	}

	/**
	 * 削除予定施設フラグを取得する。
	 * 
	 * @return isDeletePlan 削除予定施設フラグ
	 */
	public boolean isDeletePlan() {
		return isDeletePlan;
	}

	/**
	 * 編集可能フラグ を取得する。
	 * 
	 * @return 編集可能フラグ
	 */
	public boolean getEnableEdit() {
		return enableEdit;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
