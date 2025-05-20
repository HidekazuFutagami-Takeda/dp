package jp.co.takeda.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 調整計画の有無を保持するオブジェクト
 * 
 * @author khashimoto
 */
public class OfficeTeamPlanChoseiDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 調整金額UH有無フラグ
	 */
	private final Boolean choseiUhFlg;

	/**
	 * 調整金額P有無フラグ
	 */
	private final Boolean choseiPFlg;

	/**
	 * コンストラクタ
	 * 
	 * @param choseiUhFlg 値(NULL不可)
	 * @param choseiPFlg 値(NULL不可)
	 */
	public OfficeTeamPlanChoseiDto(final Boolean choseiUhFlg, final Boolean choseiPFlg) {
		this.choseiUhFlg = choseiUhFlg;
		this.choseiPFlg = choseiPFlg;
	}

	/**
	 * 調整金額UH有無フラグを取得する。
	 * 
	 * @return 調整金額UH有無フラグ
	 */
	public Boolean getChoseiUhFlg() {
		return choseiUhFlg;
	}

	/**
	 * 調整金額P有無フラグを取得する。
	 * 
	 * @return 調整金額P有無フラグ
	 */
	public Boolean getChoseiPFlg() {
		return choseiPFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
