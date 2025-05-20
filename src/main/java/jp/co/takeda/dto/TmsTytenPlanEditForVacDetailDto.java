package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.WsPlanForVac;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画編集 明細DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanEditForVacDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * エリア特約店部名
	 */
	private final String areaTytenName;

	/**
	 * ワクチン用特約店別計画
	 */
	private final WsPlanForVac wsPlanForVac;

	/**
	 * 組織合計フラグ
	 * <ul>
	 * <li>TRUE =組織合計</li>
	 * <li>FALSE=組織合計でない</li>
	 * </ul>
	 */
	private final boolean isSosSum;

	/**
	 * 全社合計フラグ
	 * <ul>
	 * <li>TRUE =全社合計</li>
	 * <li>FALSE=全社合計でない</li>
	 * </ul>
	 */
	private final boolean isAllSum;

	/**
	 * コンストラクタ
	 * 
	 * @param areaTytenName エリア特約店部名
	 * @param wsPlanForVac ワクチン用特約店別計画
	 * @param isSosSum 組織合計フラグ
	 * @param isAllSum 全社合計フラグ
	 */
	public TmsTytenPlanEditForVacDetailDto(String areaTytenName, WsPlanForVac wsPlanForVac, boolean isSosSum, boolean isAllSum) {
		this.areaTytenName = areaTytenName;
		this.wsPlanForVac = wsPlanForVac;
		this.isSosSum = isSosSum;
		this.isAllSum = isAllSum;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * エリア特約店部名を取得する。
	 * 
	 * @return areaTytenName エリア特約店部名
	 */
	public String getAreaTytenName() {
		return areaTytenName;
	}

	/**
	 * ワクチン用特約店別計画を取得する
	 */
	public WsPlanForVac getWsPlanForVac() {
		return wsPlanForVac;
	}

	/**
	 * 組織合計フラグを取得する。
	 * 
	 * @return isSosSum 組織合計フラグ
	 */
	public boolean isSosSum() {
		return isSosSum;
	}

	/**
	 * 全社合計フラグを取得する。
	 * 
	 * @return isAllSum 全社合計フラグ
	 */
	public boolean isAllSum() {
		return isAllSum;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
