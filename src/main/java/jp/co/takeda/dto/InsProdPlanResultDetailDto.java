package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageInsPlan;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設品目別計画の検索結果 明細行を表すDTO
 * 
 * @author stakeuchi
 */
public class InsProdPlanResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 品目名
	 */
	private final String prodName;

	/**
	 * Y価ベース
	 */
	private final Long yBaseValue;

	/**
	 * T価ベース
	 */
	private final Long tBaseValue;

	/**
	 * T/Y変換率
	 */
	private final Double tyChangeRate;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 最終更新者
	 */
	private final String upJgiName;

	/**
	 * 施設品目の担当者従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設品目の担当者雑組織フラグ
	 */
	private final boolean etcSosFlg;

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
	 * @param insPlan 施設別計画
	 * @param tyChangeRate T/Y変換率
	 */
	public InsProdPlanResultDetailDto(ManageInsPlan insPlan, Double tyChangeRate, boolean isDeletePlan, boolean enableEdit) {
		this.prodCode = insPlan.getProdCode();
		this.prodName = insPlan.getProdName();
		this.yBaseValue = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplPlan().getPlanned2ValueY());
		this.tBaseValue = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplPlan().getPlanned2ValueT());
		this.tyChangeRate = tyChangeRate;
		this.seqKey = insPlan.getSeqKey();

		// 施設品目の担当者従業員番号
		if(insPlan.getJgiMst() != null){
			this.jgiNo = insPlan.getJgiMst().getJgiNo();
		} else {
			this.jgiNo = null;
		}

		// 施設品目の担当者雑組織フラグ
		if(insPlan.getSosMst() != null){
			this.etcSosFlg = insPlan.getSosMst().getEtcSosFlg();
		} else {
			this.etcSosFlg = false;
		}

		this.upDate = insPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = insPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.isDeletePlan = isDeletePlan;

		// 削除施設 かつ シーケンスがnullの場合は「編集不可」、それ以外は指定された状態
		if (isDeletePlan && insPlan.getSeqKey() == null) {
			this.enableEdit = false;
		} else {
			this.enableEdit = enableEdit;
		}
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
	 * 品目名を取得する。
	 * 
	 * @return prodName 品目名
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * Y価ベースを取得する。
	 * 
	 * @return yBaseValue Y価ベース
	 */
	public Long getYBaseValue() {
		return yBaseValue;
	}

	/**
	 * T価ベースを取得する。
	 * 
	 * @return tBaseValue T価ベース
	 */
	public Long getTBaseValue() {
		return tBaseValue;
	}

	/**
	 * T/Y変換率を取得する。
	 * 
	 * @return tyChangeRate T/Y変換率
	 */
	public Double getTyChangeRate() {
		return tyChangeRate;
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
	 * 最終更新者を取得する。
	 * 
	 * @return upJgiName 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
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

	/**
	 * 施設品目の担当者従業員番号 を取得する。
	 * 
	 * @return 施設品目の担当者従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 施設品目の担当者従業員番号 を取得する。
	 * 
	 * @return 施設品目の担当者従業員番号
	 */
	public boolean isEtcSosFlg() {
		return etcSosFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
