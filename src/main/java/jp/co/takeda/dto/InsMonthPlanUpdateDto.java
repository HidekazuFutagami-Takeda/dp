package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;

/**
 * 施設別計画の更新用DTO
 *
 * @author stakeuchi
 */
public class InsMonthPlanUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 対象区分
	 */
	private final InsType insType;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 更新前月初計画1（ＹB価）
	 */
	private final Long planned1ValueBefore;

	/**
	 * 更新前月初計画2（ＹB価）
	 */
	private final Long planned2ValueBefore;

	/**
	 * 更新前月初計画3（ＹB価）
	 */
	private final Long planned3ValueBefore;

	/**
	 * 更新前月初計画4（ＹB価）
	 */
	private final Long planned4ValueBefore;

	/**
	 * 更新前月初計画5（ＹB価）
	 */
	private final Long planned5ValueBefore;

	/**
	 * 更新前月初計画6（ＹB価）
	 */
	private final Long planned6ValueBefore;

	/**
	 * 更新前月末見込1（ＹB価）
	 */
	private final Long expected1ValueBefore;

	/**
	 * 更新前月末見込2（ＹB価）
	 */
	private final Long expected2ValueBefore;

	/**
	 * 更新前月末見込3（ＹB価）
	 */
	private final Long expected3ValueBefore;

	/**
	 * 更新前月末見込4（ＹB価）
	 */
	private final Long expected4ValueBefore;

	/**
	 * 更新前月末見込5（ＹB価）
	 */
	private final Long expected5ValueBefore;

	/**
	 * 更新前月末見込6（ＹB価）
	 */
	private final Long expected6ValueBefore;

	// 月初計画1（ＹB価）
	/**
	 * Y価ベース 更新前
	 */
	private final Long planned1Value;
	// 月初計画2（ＹB価）
	private final Long planned2Value;
	// 月初計画3（ＹB価）
	private final Long planned3Value;
	// 月初計画4（ＹB価）
	private final Long planned4Value;
	// 月初計画5（ＹB価）
	private final Long planned5Value;
	// 月初計画6（ＹB価）
	private final Long planned6Value;

	// 月末見込1（ＹB価）
	/**
	 * Y価ベース 更新前
	 */
	private final Long expected1Value;
	// 月末見込2（ＹB価）
	private final Long expected2Value;
	// 月末見込3（ＹB価）
	private final Long expected3Value;
	// 月末見込4（ＹB価）
	private final Long expected4Value;
	// 月末見込5（ＹB価）
	private final Long expected5Value;
	// 月末見込6（ＹB価）
	private final Long expected6Value;


	/**
	 *
	 * コンストラクタ
	 *
	 * @param insNo 施設コード
	 * @param insType 対象区分
	 * @param prodCode 品目コード
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param yBaseValueBefore Y価ベース 更新前
	 * @param yBaseValueAfter Y価ベース 更新後
	 */
	public InsMonthPlanUpdateDto(String insNo, InsType insType, String prodCode, Long seqKey, Date upDate,
			Long planned1ValueBefore, Long planned2ValueBefore, Long planned3ValueBefore, Long planned4ValueBefore, Long planned5ValueBefore, Long planned6ValueBefore,
			Long expected1ValueBefore, Long expected2ValueBefore, Long expected3ValueBefore, Long expected4ValueBefore, Long expected5ValueBefore, Long expected6ValueBefore,
			Long planned1Value, Long planned2Value, Long planned3Value, Long planned4Value, Long planned5Value, Long planned6Value,
			Long expected1Value, Long expected2Value, Long expected3Value, Long expected4Value, Long expected5Value, Long expected6Value) {
		this.insNo = insNo;
		this.insType = insType;
		this.prodCode = prodCode;
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.planned1ValueBefore = planned1ValueBefore;
		this.planned2ValueBefore = planned2ValueBefore;
		this.planned3ValueBefore = planned3ValueBefore;
		this.planned4ValueBefore = planned4ValueBefore;
		this.planned5ValueBefore = planned5ValueBefore;
		this.planned6ValueBefore = planned6ValueBefore;

		this.expected1ValueBefore = expected1ValueBefore;
		this.expected2ValueBefore = expected2ValueBefore;
		this.expected3ValueBefore = expected3ValueBefore;
		this.expected4ValueBefore = expected4ValueBefore;
		this.expected5ValueBefore = expected5ValueBefore;
		this.expected6ValueBefore = expected6ValueBefore;

		this.planned1Value = planned1Value;
		this.planned2Value = planned2Value;
		this.planned3Value = planned3Value;
		this.planned4Value = planned4Value;
		this.planned5Value = planned5Value;
		this.planned6Value = planned6Value;

		this.expected1Value = expected1Value;
		this.expected2Value = expected2Value;
		this.expected3Value = expected3Value;
		this.expected4Value = expected4Value;
		this.expected5Value = expected5Value;
		this.expected6Value = expected6Value;

//		this.yBaseValueBefore = yBaseValueBefore;
//		this.yBaseValueAfter = yBaseValueAfter;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
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
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return seqKey シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 更新前月初計画1（ＹB価）を取得する。
	 *
	 * @return planned1ValueBefore 更新前月初計画1（ＹB価）
	 */
	public Long getPlanned1ValueBefore() {
		return planned1ValueBefore;
	}

	/**
	 * 更新前月初計画2（ＹB価）を取得する。
	 *
	 * @return planned2ValueBefore 更新前月初計画1（ＹB価）
	 */
	public Long getPlanned2ValueBefore() {
		return planned2ValueBefore;
	}

	/**
	 * 更新前月初計画3（ＹB価）を取得する。
	 *
	 * @return planned3ValueBefore 更新前月初計画1（ＹB価）
	 */
	public Long getPlanned3ValueBefore() {
		return planned3ValueBefore;
	}

	/**
	 * 更新前月初計画4（ＹB価）を取得する。
	 *
	 * @return planned4ValueBefore 更新前月初計画1（ＹB価）
	 */
	public Long getPlanned4ValueBefore() {
		return planned4ValueBefore;
	}

	/**
	 * 更新前月初計画5（ＹB価）を取得する。
	 *
	 * @return planned5ValueBefore 更新前月初計画1（ＹB価）
	 */
	public Long getPlanned5ValueBefore() {
		return planned5ValueBefore;
	}

	/**
	 * 更新前月初計画6（ＹB価）を取得する。
	 *
	 * @return planned6ValueBefore 更新前月初計画1（ＹB価）
	 */
	public Long getPlanned6ValueBefore() {
		return planned6ValueBefore;
	}

	public Long getExpected1ValueBefore() {
		return expected1ValueBefore;
	}

	public Long getExpected2ValueBefore() {
		return expected2ValueBefore;
	}

	public Long getExpected3ValueBefore() {
		return expected3ValueBefore;
	}

	public Long getExpected4ValueBefore() {
		return expected4ValueBefore;
	}

	public Long getExpected5ValueBefore() {
		return expected5ValueBefore;
	}

	public Long getExpected6ValueBefore() {
		return expected6ValueBefore;
	}

	public Long getPlanned1Value() {
		return planned1Value;
	}

	public Long getPlanned2Value() {
		return planned2Value;
	}

	public Long getPlanned3Value() {
		return planned3Value;
	}

	public Long getPlanned4Value() {
		return planned4Value;
	}

	public Long getPlanned5Value() {
		return planned5Value;
	}

	public Long getPlanned6Value() {
		return planned6Value;
	}

	public Long getExpected1Value() {
		return expected1Value;
	}

	public Long getExpected2Value() {
		return expected2Value;
	}

	public Long getExpected3Value() {
		return expected3Value;
	}

	public Long getExpected4Value() {
		return expected4Value;
	}

	public Long getExpected5Value() {
		return expected5Value;
	}

	public Long getExpected6Value() {
		return expected6Value;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
