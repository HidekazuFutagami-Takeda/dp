package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画担当者別品目別一覧 品目情報DTO
 * 
 * @author stakeuchi
 */
public class MrPlanMMPProdDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 対象区分
	 */
	private final InsType insType;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 計画値
	 * 
	 * <pre>
	 * Nullの場合はNull, 0の場合は0が挿入される。
	 * </pre>
	 */
	private final Long plannedValue;

	/**
	 * 前期実績
	 * 
	 * <pre>
	 * Nullの場合はNull, 0の場合は0が挿入される。
	 * </pre>
	 */
	private final Long advancePeriod;

	/**
	 * コンストラクタ
	 * 
	 * @param insType 対象区分
	 * @param prodCode 品目固定コード
	 * @param plannedValue 計画値
	 * @param advancePeriod 前期実績
	 */
	public MrPlanMMPProdDto(InsType insType, String prodCode, Long plannedValue, Long advancePeriod) {
		this.insType = insType;
		this.prodCode = prodCode;
		this.plannedValue = ConvertUtil.parseMoneyToThousandUnit(plannedValue);
		this.advancePeriod = ConvertUtil.parseMoneyToThousandUnit(advancePeriod);
	}

	/**
	 * 対象区分を取得する。
	 * 
	 * @return insType 対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 計画値を取得する。
	 * 
	 * @return plannedValue 計画値
	 */
	public Long getPlannedValue() {
		return plannedValue;
	}

	/**
	 * 前期実績を取得する。
	 * 
	 * @return advancePeriod 前期実績
	 */
	public Long getAdvancePeriod() {
		return advancePeriod;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
