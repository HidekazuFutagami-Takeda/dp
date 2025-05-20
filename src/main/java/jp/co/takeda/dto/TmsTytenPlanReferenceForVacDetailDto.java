package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.WsPlanForVacSummary2;

/**
 * ワクチン用特約店別計画参照画面 明細行DTO
 *
 * @author stakeuchi
 */
public class TmsTytenPlanReferenceForVacDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画サマリー
	 */
	private final WsPlanForVacSummary2 wsPlanForVacSummary;

	/**
	 * 組織合計フラグ
	 * <ul>
	 * <li>TRUE =組織合計</li>
	 * <li>FALSE=組織合計でない</li>
	 * </ul>
	 */
	private final boolean isSosSum;

	/**
	 * 全合計フラグ
	 * <ul>
	 * <li>TRUE =全合計</li>
	 * <li>FALSE=全合計でない</li>
	 * </ul>
	 */
	private final boolean isAllSum;

	/**
	 * コンストラクタ
	 *
	 * @param wsPlanForVacSummary 特約店別計画サマリー
	 * @param isSosSum 組織合計フラグ
	 * @param isAllSum 全合計フラグ
	 */
	public TmsTytenPlanReferenceForVacDetailDto(WsPlanForVacSummary2 wsPlanForVacSummary, boolean isSosSum, boolean isAllSum) {
		this.wsPlanForVacSummary = wsPlanForVacSummary;
		this.isSosSum = isSosSum;
		this.isAllSum = isAllSum;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店別計画サマリーを取得する。
	 *
	 * @return wsPlanForVacSummary 特約店別計画サマリー
	 */
	public WsPlanForVacSummary2 getWsPlanForVacSummary() {
		return wsPlanForVacSummary;
	}

	/**
	 * 全合計フラグを取得する。
	 *
	 * @return isSosSum 全合計フラグ
	 */
	public boolean isSosSum() {
		return isSosSum;
	}

	/**
	 * 全合計フラグを取得する。
	 *
	 * @return isAllSum 全合計フラグ
	 */
	public boolean isAllSum() {
		return isAllSum;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
