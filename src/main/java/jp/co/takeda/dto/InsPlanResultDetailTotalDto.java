package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画の検索結果 明細合計行を表すDTO
 * 
 * @author stakeuchi
 */
public class InsPlanResultDetailTotalDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Y価ベース
	 */
	private final Long yBaseValue;

	/**
	 * T価ベース
	 */
	private final Long tBaseValue;

	/**
	 * 施設別計画 非表示分
	 */
	private final Long hideValue;

	/**
	 * T/Y変換率
	 */
	private final Double tyChangeRate;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 最終更新者
	 */
	private final String upJgiName;

	/**
	 * 組織別計画編集画面への移行可否フラグ
	 * <ul>
	 * <li>TRUE = 組織別計画へ移行可能</li>
	 * <li>FALSE = 組織別計画へ移行不可</li>
	 * </ul>
	 */
	private final boolean canMoveSosPlan;

	/**
	 * コンストラクタ
	 * 
	 * @param yBaseValue Y価ベース
	 * @param tBaseValue T価ベース
	 * @param hideValue 施設別計画 非表示分
	 * @param tyChangeRate T/Y変換率
	 * @param upDate 最終更新日時
	 * @param upJgiName 最終更新者
	 * @param canMoveSosPlan 組織別計画編集画面への移行可否フラグ
	 */
	public InsPlanResultDetailTotalDto(ManageMrPlan mrPlan, Long hideValue, Double tyChangeRate, boolean canMoveSosPlan) {
		this.yBaseValue = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplPlan().getPlanned2ValueY());
		this.tBaseValue = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplPlan().getPlanned2ValueT());
		this.hideValue = ConvertUtil.parseMoneyToThousandUnit(hideValue);
		this.tyChangeRate = tyChangeRate;
		this.upDate = mrPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = mrPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.canMoveSosPlan = canMoveSosPlan;
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
	 * 施設別計画 非表示分を取得する。
	 * 
	 * @return hideValue 施設別計画 非表示分
	 */
	public Long getHideValue() {
		return hideValue;
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
	 * 組織別計画編集画面への移行可否フラグを取得する。
	 * 
	 * @return canMoveSosPlan 組織別計画編集画面への移行可否フラグ
	 */
	public boolean isCanMoveSosPlan() {
		return canMoveSosPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
