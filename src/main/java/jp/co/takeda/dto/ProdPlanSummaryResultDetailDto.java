package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.util.ConvertUtil;

/**
 * 品目別計画積上結果の検索結果 明細行を表すDTO
 *
 * @author stakeuchi
 */
public class ProdPlanSummaryResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 登録可能最大値(9,999,999,999千円)
	 */
	private static final Long UPDATE_ENABLE_MAX_VALUE = 9999999999L;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 品目名
	 */
	private final String prodName;

	/**
	 * [UH] Y価ベース(積上前)
	 */
	private final Long yBaseValueUHBefore;

	/**
	 * [UH] シーケンスキー(積上前)
	 */
	private final Long seqKeyUH;

	/**
	 * [UH] 最終更新日時(積上前)
	 */
	private final Date upDateUH;

	/**
	 * [P] Y価ベース(積上前)
	 */
	private final Long yBaseValuePBefore;

	/**
	 * [P] シーケンスキー(積上前)
	 */
	private final Long seqKeyP;

	/**
	 * [P] 最終更新日時(積上前)
	 */
	private final Date upDateP;

	/**
	 * [Z] Y価ベース(積上前)
	 */
	private final Long yBaseValueZBefore;

	/**
	 * [Z] シーケンスキー(積上前)
	 */
	private final Long seqKeyZ;

	/**
	 * [Z] 最終更新日時(積上前)
	 */
	private final Date upDateZ;

	/**
	 * [UH] Y価ベース(積上後)
	 */
	private final Long yBaseValueUHAfter;

	/**
	 * [P] Y価ベース(積上後)
	 */
	private final Long yBaseValuePAfter;

	/**
	 * [Z] Y価ベース(積上後)
	 */
	private final Long yBaseValueZAfter;

	/**
	 * 積上後金額オーバーフラグ<br>
	 * true：金額オーバー、false：金額オーバーでない
	 */
	private final Boolean plannedValueOver;

	/**
	 * コンストラクタ
	 *
	 * @param mrPlanUh 担当者別計画(UH)
	 * @param mrPlanP 担当者別計画(P)
	 * @param mrPlanZ 担当者別計画(Z) ワクチン以外の場合はnull
	 */
	public ProdPlanSummaryResultDetailDto(ManageMrPlan mrPlanUh, ManageMrPlan mrPlanP, ManageMrPlan mrPlanZ) {
		this.prodCode = mrPlanUh.getProdCode();
		this.prodName = mrPlanUh.getProdName();
		this.yBaseValueUHBefore = ConvertUtil.parseMoneyToThousandUnit(mrPlanUh.getImplPlan().getPlanned2ValueY());
		this.seqKeyUH = mrPlanUh.getSeqKey();
		this.upDateUH = mrPlanUh.getUpDate();
		this.yBaseValuePBefore = ConvertUtil.parseMoneyToThousandUnit(mrPlanP.getImplPlan().getPlanned2ValueY());
		this.seqKeyP = mrPlanP.getSeqKey();
		this.upDateP = mrPlanP.getUpDate();
		this.yBaseValueUHAfter = ConvertUtil.parseMoneyToThousandUnit(mrPlanUh.getStackedValue());
		this.yBaseValuePAfter = ConvertUtil.parseMoneyToThousandUnit(mrPlanP.getStackedValue());

		if(mrPlanZ.equals(new ManageMrPlan()) == false) {
			this.yBaseValueZBefore = ConvertUtil.parseMoneyToThousandUnit(mrPlanZ.getImplPlan().getPlanned2ValueY());
			this.seqKeyZ = mrPlanZ.getSeqKey();
			this.upDateZ = mrPlanZ.getUpDate();
			this.yBaseValueZAfter = ConvertUtil.parseMoneyToThousandUnit(mrPlanZ.getStackedValue());
		}
		else {
			this.yBaseValueZBefore = 0L;
			this.seqKeyZ = 0L;
			this.upDateZ = null;
			this.yBaseValueZAfter = 0L;
		}

		boolean _plannedValueOver = false;
		if (yBaseValueUHAfter != null && yBaseValueUHAfter > UPDATE_ENABLE_MAX_VALUE) {
			_plannedValueOver = true;
		}
		if (yBaseValuePAfter != null && yBaseValuePAfter > UPDATE_ENABLE_MAX_VALUE) {
			_plannedValueOver = true;
		}
		if (yBaseValueZAfter != null && yBaseValueZAfter > UPDATE_ENABLE_MAX_VALUE) {
			_plannedValueOver = true;
		}
		plannedValueOver = _plannedValueOver;
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
	 * [UH] Y価ベース(積上前)を取得する。
	 *
	 * @return yBaseValueUHBefore [UH] Y価ベース(積上前)
	 */
	public Long getYBaseValueUHBefore() {
		return yBaseValueUHBefore;
	}

	/**
	 * [UH] シーケンスキー(積上前)を取得する。
	 *
	 * @return seqKeyUH [UH] シーケンスキー(積上前)
	 */
	public Long getSeqKeyUH() {
		return seqKeyUH;
	}

	/**
	 * [UH] 最終更新日時(積上前)を取得する。
	 *
	 * @return upDateUH [UH] 最終更新日時(積上前)
	 */
	public Date getUpDateUH() {
		return upDateUH;
	}

	/**
	 * [P] Y価ベース(積上前)を取得する。
	 *
	 * @return yBaseValuePBefore [P] Y価ベース(積上前)
	 */
	public Long getYBaseValuePBefore() {
		return yBaseValuePBefore;
	}

	/**
	 * [P] シーケンスキー(積上前)を取得する。
	 *
	 * @return seqKeyP [P] シーケンスキー(積上前)
	 */
	public Long getSeqKeyP() {
		return seqKeyP;
	}

	/**
	 * [P] 最終更新日時(積上前)を取得する。
	 *
	 * @return upDateP [P] 最終更新日時(積上前)
	 */
	public Date getUpDateP() {
		return upDateP;
	}

	/**
	 * [Z] Y価ベース(積上前)を取得する。
	 *
	 * @return yBaseValueZBefore [Z] Y価ベース(積上前)
	 */
	public Long getYBaseValueZBefore() {
		return yBaseValueZBefore;
	}

	/**
	 * [Z] シーケンスキー(積上前)を取得する。
	 *
	 * @return seqKeyZ [Z] シーケンスキー(積上前)
	 */
	public Long getSeqKeyZ() {
		return seqKeyZ;
	}

	/**
	 * [Z] 最終更新日時(積上前)を取得する。
	 *
	 * @return upDateZ [Z] 最終更新日時(積上前)
	 */
	public Date getUpDateZ() {
		return upDateZ;
	}

	/**
	 * [UH] Y価ベース(積上後)を取得する。
	 *
	 * @return yBaseValueUHAfter [UH] Y価ベース(積上後)
	 */
	public Long getYBaseValueUHAfter() {
		return yBaseValueUHAfter;
	}

	/**
	 * [P] Y価ベース(積上後)を取得する。
	 *
	 * @return yBaseValuePAfter [P] Y価ベース(積上後)
	 */
	public Long getYBaseValuePAfter() {
		return yBaseValuePAfter;
	}

	/**
	 * [Z] Y価ベース(積上後)を取得する。
	 *
	 * @return yBaseValueZAfter [Z] Y価ベース(積上後)
	 */
	public Long getYBaseValueZAfter() {
		return yBaseValueZAfter;
	}

	/**
	 * 積上後金額オーバーフラグを取得する。
	 *
	 * @return true：金額オーバー、false：金額オーバーでない
	 */
	public Boolean getPlannedValueOver() {
		return plannedValueOver;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
