package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageInsPlanForVac;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設品目別計画の検索結果 明細行を表すDTO
 *
 * @author stakeuchi
 */
public class InsProdPlanForVacResultDetailDto extends DpDto {

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
	private final Long bBaseValue;

	/**
	 * T価ベース
	 */
	private final Long tBaseValue;

	/**
	 * T/B変換率
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
	 * @param tyChangeRate T/B変換率
	 */
	public InsProdPlanForVacResultDetailDto(ManageInsPlanForVac insPlan, Double tyChangeRate, boolean isDeletePlan) {
		this.prodCode = insPlan.getProdCode();
		this.prodName = insPlan.getProdName();
		this.bBaseValue = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplPlanForVac().getPlanned2ValueY());
		this.tBaseValue = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplPlanForVac().getPlanned2ValueT());
		this.tyChangeRate = tyChangeRate;
		this.seqKey = insPlan.getSeqKey();
		this.upDate = insPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = insPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.isDeletePlan = isDeletePlan;

		// 削除施設 かつ シーケンスがnullの場合は「編集不可」、それ以外は「編集可能」
		if (isDeletePlan && insPlan.getSeqKey() == null) {
			this.enableEdit = false;
		} else {
			this.enableEdit = true;
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
	 * B価ベースを取得する。
	 *
	 * @return bBaseValue Y価ベース
	 */
	public Long getBBaseValue() {
		return bBaseValue;
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
