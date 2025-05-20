package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 施設別計画の検索結果を表すDTO
 *
 * @author stakeuchi
 */
public class InsMonthPlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細合計行DTO
	 */
	private final InsMonthPlanResultDetailTotalDto detailTotal;

	/**
	 * 明細DTOリスト
	 */
	private final List<InsMonthPlanResultDetailDto> detailList;

	/**
	 * 登録可能フラグ
	 * <ul>
	 * <li>TRUE = 登録可能</li>
	 * <li>FALSE = 登録可能でない</li>
	 * </ul>
	 */
	private final boolean enableEntry;

	/**
	 * 指定されている担当者が自分の組織か
	 * <ul>
	 * <li>TRUE = 自分の組織配下</li>
	 * <li>FALSE = 自分の組織配下ではない</li>
	 * </ul>
	 */
	private final boolean mySosMr;

	/**
	 * コンストラクタ
	 *
	 * @param detailTotal 明細合計行DTO
	 * @param detailList 明細DTOリスト
	 */
	public InsMonthPlanResultDto(InsMonthPlanResultDetailTotalDto detailTotal, List<InsMonthPlanResultDetailDto> detailList, boolean enableEntry,boolean mySosMr) {
		this.detailTotal = detailTotal;
		this.detailList = detailList;
		this.enableEntry = enableEntry;
		this.mySosMr = mySosMr;
	}

	/**
	 * 明細合計行DTOを取得する。
	 *
	 * @return detailTotal 明細合計行DTO
	 */
	public InsMonthPlanResultDetailTotalDto getDetailTotal() {
		return detailTotal;
	}

	/**
	 * 明細DTOリストを取得する。
	 *
	 * @return detailList 明細DTOリスト
	 */
	public List<InsMonthPlanResultDetailDto> getDetailList() {
		return detailList;
	}

	/**
	 * 登録可能フラグ を取得する。
	 *
	 * @return 登録可能フラグ
	 */
	public boolean getEnableEntry() {
		return enableEntry;
	}

	/**
	 * 指定されている担当者が自分の組織か
	 *
	 * @return 指定されている担当者が自分の組織か
	 */
	public boolean isMySosMr() {
		return mySosMr;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
