package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 管理の更新結果DTO
 * 
 * @author nozaki
 */
public class ManagePlanUpdateResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 計画値UHの更新件数
	 */
	private final int updateCountUh;

	/**
	 * 計画値Pの更新件数
	 */
	private final int updateCountP;

	/**
	 * 計画値Zの更新件数
	 */
	private final int updateCountZ;

	/**
	 * 計画値UHPの更新件数
	 */
	private final int updateCountUhp;

	/**
	 * 計画値UH + Pの更新件数
	 */
	private final int updateTotalCount;

	/**
	 * コンストラクタ
	 * 
	 * @param updateCountUh 計画値UHの更新件数
	 * @param updateCountP 計画値Pの更新件数
	 * @param updateCountZ 計画値Zの更新件数
	 */
	public ManagePlanUpdateResultDto(int updateCountUh, int updateCountP, int updateCountZ) {
		this.updateCountUh = updateCountUh;
		this.updateCountP = updateCountP;
		this.updateCountZ = updateCountZ;
		this.updateCountUhp = 0; // 使用しない
		this.updateTotalCount = updateCountUh + updateCountP + updateCountZ;
	}

	/**
	 * コンストラクタ UHP付き
	 *
	 * @param updateCountUh 計画値UHの更新件数
	 * @param updateCountP 計画値Pの更新件数
	 * @param updateCountZ 計画値Zの更新件数
	 * @param updateCountUhp 計画値UHPの更新件数
	 */
	public ManagePlanUpdateResultDto(int updateCountUh, int updateCountP, int updateCountZ, int updateCountUhp) {
		this.updateCountUh = updateCountUh;
		this.updateCountP = updateCountP;
		this.updateCountZ = updateCountZ;
		this.updateCountUhp = updateCountUhp;
		this.updateTotalCount = updateCountUh + updateCountP + updateCountZ + updateCountUhp;
	}
	/**
	 * 計画値UHの更新件数を取得する。
	 * 
	 * @return 計画値UHの更新件数
	 */
	public int getUpdateCountUh() {
		return updateCountUh;
	}

	/**
	 * 計画値Pの更新件数を取得する。
	 * 
	 * @return 計画値Pの更新件数
	 */
	public int getUpdateCountP() {
		return updateCountP;
	}

	/**
	 * 計画値Zの更新件数を取得する。
	 *
	 * @return 計画値Zの更新件数
	 */
	public int getUpdateCountZ() {
		return updateCountZ;
	}

	/**
	 * 計画値UHPの更新件数を取得する。
	 *
	 * @return 計画値UHPの更新件数
	 */
	public int getUpdateCountUhp() {
		return updateCountUhp;
	}

	/**
	 * 計画値の更新件数を取得する。
	 * 
	 * @return 計画値の更新件数
	 */
	public int getUpdateTotalCount() {
		return updateTotalCount;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
