package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設品目別計画の検索結果DTO
 * 
 * @author stakeuchi
 */
public class InsProdPlanForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 明細DTOリスト
	 */
	private final List<InsProdPlanForVacResultDetailDto> detailList;

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
	 * @param detailList 明細DTOリスト
	 * @param enableEntry 登録可能フラグ
	 */
	public InsProdPlanForVacResultDto(List<InsProdPlanForVacResultDetailDto> detailList, boolean enableEntry) {
		this.detailList = detailList;
		this.enableEntry = enableEntry;
	}

	/**
	 * 明細DTOリストを取得する。
	 * 
	 * @return detailList 明細DTOリスト
	 */
	public List<InsProdPlanForVacResultDetailDto> getDetailList() {
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
