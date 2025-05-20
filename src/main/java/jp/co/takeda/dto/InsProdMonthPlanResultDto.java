package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 施設品目別計画の検索結果DTO
 *
 * @author stakeuchi
 */
public class InsProdMonthPlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目分類名
	 */
	private final String prodCategory;

	/**
	 * 計画集計
	 */
	private final InsProdMonthPlanResultDetailDto planSum;

	/**
	 * 明細DTOリスト
	 */
	private final List<InsProdMonthPlanResultDetailDto> detailList;

	/**
	 * 登録可能フラグ
	 * <ul>
	 * <li>TRUE = 登録可能</li>
	 * <li>FALSE = 登録可能でない</li>
	 * </ul>
	 */
	private final boolean enableEntry;

	/**
	 * コンストラクタ
	 *
	 * @param prodCategory 品目分類名
	 * @param planSum 計画集計
	 * @param detailList 明細DTOリスト
	 * @param enableEntry 登録可能フラグ
	 */
	public InsProdMonthPlanResultDto(String prodCategory, InsProdMonthPlanResultDetailDto planSum,
			List<InsProdMonthPlanResultDetailDto> detailList, boolean enableEntry) {
		this.prodCategory = prodCategory;
		this.planSum = planSum;
		this.detailList = detailList;
		this.enableEntry = enableEntry;
	}

	/**
	 * 品目分類名を取得する
	 *
	 * @return prodCategory 品目分類名
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 計画集計を取得する
	 *
	 * @return planSum 計画集計
	 */
	public InsProdMonthPlanResultDetailDto getPlanSum() {
		return planSum;
	}

	/**
	 * 明細DTOリストを取得する。
	 *
	 * @return detailList 明細DTOリスト
	 */
	public List<InsProdMonthPlanResultDetailDto> getDetailList() {
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
