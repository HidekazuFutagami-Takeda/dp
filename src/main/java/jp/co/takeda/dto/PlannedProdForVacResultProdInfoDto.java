package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用計画対象品目選択画面の検索結果用DTO
 * 
 * @author stakeuchi
 */
public class PlannedProdForVacResultProdInfoDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 計画値
	 */
	private final Long plannedValue;

	/**
	 * UP率
	 */
	private final Double upRate;

	/**
	 * 品目合計フラグ
	 * <ul>
	 * <li>TRUE =品目合計</li>
	 * <li>FALSE=品目別</li>
	 * </ul>
	 */
	private final boolean isProdSum;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 品目コード
	 * @param plannedValue 計画値(円単位)
	 * @param advancePeriod 前期実績(円単位)
	 * @param isProdSum 品目合計フラグ
	 */
	public PlannedProdForVacResultProdInfoDto(String prodCode, Long plannedValue, Long advancePeriod, boolean isProdSum) {
		this.prodCode = prodCode;
		this.plannedValue = ConvertUtil.parseMoneyToThousandUnit(plannedValue);
		this.upRate = MathUtil.calcUp(plannedValue, advancePeriod);
		this.isProdSum = isProdSum;
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
	 * 計画値を取得する。
	 * 
	 * @return plannedValue 計画値
	 */
	public Long getPlannedValue() {
		return plannedValue;
	}

	/**
	 * UP率を取得する。
	 * 
	 * @return upRate UP率
	 */
	public Double getUpRate() {
		return upRate;
	}

	/**
	 * 品目合計フラグを取得する。
	 * 
	 * @return isProdSum 品目合計フラグ
	 */
	public boolean isProdSum() {
		return isProdSum;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
