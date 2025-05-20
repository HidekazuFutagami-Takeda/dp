package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageInsWsPlanForVac;
import jp.co.takeda.util.ConvertUtil;

/**
 *
 * （ワ）施設特約店品目別計画編集を表すDTO
 *
 * @author nozaki
 */
public class ManageInsWsPlanProdForVacDetailDto extends DpDto {

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
	 * B価ベース
	 */
	private final Long baseB;

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
	 * T/B変換率
	 */
	private final Double tbChangeRate;

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
	 * @param insWsPlan 施設特約店別計画
	 * @param tbChangeRate T/B変換率
	 * @param isDeletePlan 削除予定施設フラグ
	 */
	public ManageInsWsPlanProdForVacDetailDto(ManageInsWsPlanForVac insWsPlan, Double tbChangeRate, boolean isDeletePlan) {

		this.prodCode = insWsPlan.getProdCode();
		this.prodName = insWsPlan.getProdName();
		this.baseB = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getImplPlanForVac().getPlanned2ValueY());
		this.beforeBaseT = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getImplPlanForVac().getPlanned2ValueT());
		this.baseT = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getImplPlanForVac().getPlanned2ValueT());
		this.tbChangeRate = tbChangeRate;
		this.seqKey = insWsPlan.getSeqKey();
		this.upDate = insWsPlan.getUpDate();
		if (upDate != null) {
			this.upDateJgiName = insWsPlan.getUpJgiName();
		} else {
			this.upDateJgiName = null;
		}
		this.isDeletePlan = isDeletePlan;

		// 削除施設 かつ シーケンスがnullの場合は「編集不可」、それ以外は「編集可能」
		if (isDeletePlan && insWsPlan.getSeqKey() == null) {
			this.enableEdit = false;
		} else {
			this.enableEdit = true;
		}
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
	 * B価ベースを取得する。
	 *
	 * @return B価ベース
	 */
	public Long getBaseB() {
		return baseB;
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
	 * T/B変換率を取得する。
	 *
	 * @return T/B変換率
	 */
	public Double getTbChangeRate() {
		return tbChangeRate;
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
