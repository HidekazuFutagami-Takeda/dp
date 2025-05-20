package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 営業所計画の更新用DTO
 * 
 * @author nozaki
 */
public class OfficePlanUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 更新日
	 */
	private final Date upDate;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 計画値UH(Y)
	 */
	private final Long plannedValueUhY;

	/**
	 * 計画値P(Y)
	 */
	private final Long plannedValuePY;

	/**
	 * コンストラクタ
	 * 
	 * @param seqKey 更新元の営業所計画のシーケンスキー(未登録時はnull)
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @param plannedValueUhY 計画値UH
	 * @param plannedValuePY 計画値P
	 * @param upDate 更新元の営業所計画の最終更新日(未登録時はnull)
	 */
	public OfficePlanUpdateDto(Long seqKey, String sosCd, String prodCode, Long plannedValueUhY, Long plannedValuePY, Date upDate) {

		this.seqKey = seqKey;
		this.sosCd = sosCd;
		this.prodCode = prodCode;
		this.plannedValueUhY = plannedValueUhY;
		this.plannedValuePY = plannedValuePY;
		this.upDate = upDate;
	}

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * シーケンスキーを取得する。
	 * 
	 * @return シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 更新日を取得する。
	 * 
	 * @return 更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 計画値UH(Y)を取得する。
	 * 
	 * @return 計画値UH(Y)
	 */
	public Long getPlannedValueUhY() {
		return plannedValueUhY;
	}

	/**
	 * 計画値P(Y)を取得する。
	 * 
	 * @return 計画値P(Y)
	 */
	public Long getPlannedValuePY() {
		return plannedValuePY;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
