package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 組織別月別計画の更新用DTO
 *
 * @author rna8405
 */
public class SosMonthPlanUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

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
	 * 月初計画1（YB価）更新後
	 */
	private final Long planned1ValueYbAfter;

	/**
	 * 月初計画2（YB価）更新後
	 */
	private final Long planned2ValueYbAfter;

	/**
	 * 月初計画3（YB価）更新後
	 */
	private final Long planned3ValueYbAfter;

	/**
	 * 月初計画4（YB価）更新後
	 */
	private final Long planned4ValueYbAfter;

	/**
	 * 月初計画5（YB価）更新後
	 */
	private final Long planned5ValueYbAfter;

	/**
	 * 月初計画6（YB価）更新後
	 */
	private final Long planned6ValueYbAfter;

	/**
	 * 月末見込1（YB価）更新後
	 */
	private final Long expected1ValueYbAfter;

	/**
	 * 月末見込2（YB価）更新後
	 */
	private final Long expected2ValueYbAfter;

	/**
	 * 月末見込3（YB価）更新後
	 */
	private final Long expected3ValueYbAfter;

	/**
	 * 月末見込4（YB価）更新後
	 */
	private final Long expected4ValueYbAfter;

	/**
	 * 月末見込5（YB価）更新後
	 */
	private final Long expected5ValueYbAfter;

	/**
	 * 月末見込6（YB価）更新後
	 */
	private final Long expected6ValueYbAfter;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目コード
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param planned1ValueYbAfter 月初計画1(YB価) 更新後
	 * @param planned2ValueYbAfter 月初計画2(YB価) 更新後
	 * @param planned3ValueYbAfter 月初計画3(YB価) 更新後
	 * @param planned4ValueYbAfter 月初計画4(YB価) 更新後
	 * @param planned5ValueYbAfter 月初計画5(YB価) 更新後
	 * @param planned6ValueYbAfter 月初計画6(YB価) 更新後
	 * @param expected1ValueYbAfter 月末見込1(YB価) 更新後
	 * @param expected2ValueYbAfter 月末見込2(YB価) 更新後
	 * @param expected3ValueYbAfter 月末見込3(YB価) 更新後
	 * @param expected4ValueYbAfter 月末見込4(YB価) 更新後
	 * @param expected5ValueYbAfter 月末見込5(YB価) 更新後
	 * @param expected6ValueYbAfter 月末見込6(YB価) 更新後
	 */
	public SosMonthPlanUpdateDto(String sosCd, Integer jgiNo, String prodCode, Long seqKey, Date upDate,
			Long planned1ValueYbAfter, Long planned2ValueYbAfter, Long planned3ValueYbAfter, Long planned4ValueYbAfter,
			Long planned5ValueYbAfter, Long planned6ValueYbAfter, Long expected1ValueYbAfter,
			Long expected2ValueYbAfter, Long expected3ValueYbAfter, Long expected4ValueYbAfter,
			Long expected5ValueYbAfter, Long expected6ValueYbAfter) {
		this.sosCd = sosCd;
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.planned1ValueYbAfter = planned1ValueYbAfter;
		this.planned2ValueYbAfter = planned2ValueYbAfter;
		this.planned3ValueYbAfter = planned3ValueYbAfter;
		this.planned4ValueYbAfter = planned4ValueYbAfter;
		this.planned5ValueYbAfter = planned5ValueYbAfter;
		this.planned6ValueYbAfter = planned6ValueYbAfter;
		this.expected1ValueYbAfter = expected1ValueYbAfter;
		this.expected2ValueYbAfter = expected2ValueYbAfter;
		this.expected3ValueYbAfter = expected3ValueYbAfter;
		this.expected4ValueYbAfter = expected4ValueYbAfter;
		this.expected5ValueYbAfter = expected5ValueYbAfter;
		this.expected6ValueYbAfter = expected6ValueYbAfter;
	}

	/**
	 * 組織コードを取得する。
	 *
	 * @return sosCd 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
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
	 * 月初計画1(YB価) 更新後を取得する。
	 *
	 * @return planned1ValueYbAfter 月初計画1(YB価) 更新後
	 */
	public Long getPlanned1ValueYbAfter() {
		return planned1ValueYbAfter;
	}

	/**
	 * 月初計画2(YB価) 更新後を取得する。
	 *
	 * @return planned2ValueYbAfter 月初計画2(YB価) 更新後
	 */
	public Long getPlanned2ValueYbAfter() {
		return planned2ValueYbAfter;
	}

	/**
	 * 月初計画3(YB価) 更新後を取得する。
	 *
	 * @return planned3ValueYbAfter 月初計画3(YB価) 更新後
	 */
	public Long getPlanned3ValueYbAfter() {
		return planned3ValueYbAfter;
	}

	/**
	 * 月初計画4(YB価) 更新後を取得する。
	 *
	 * @return planned4ValueYbAfter 月初計画4(YB価) 更新後
	 */
	public Long getPlanned4ValueYbAfter() {
		return planned4ValueYbAfter;
	}

	/**
	 * 月初計画5(YB価) 更新後を取得する。
	 *
	 * @return planned5ValueYbAfter 月初計画5(YB価) 更新後
	 */
	public Long getPlanned5ValueYbAfter() {
		return planned5ValueYbAfter;
	}

	/**
	 * 月初計画6(YB価) 更新後を取得する。
	 *
	 * @return planned6ValueYbAfter 月初計画6(YB価) 更新後
	 */
	public Long getPlanned6ValueYbAfter() {
		return planned6ValueYbAfter;
	}

	/**
	 * 月末見込1(YB価) 更新後を取得する。
	 *
	 * @return expected1ValueYbAfter 月末見込1(YB価) 更新後
	 */
	public Long getExpected1ValueYbAfter() {
		return expected1ValueYbAfter;
	}

	/**
	 * 月末見込2(YB価) 更新後を取得する。
	 *
	 * @return expected2ValueYbAfter 月末見込2(YB価) 更新後
	 */
	public Long getExpected2ValueYbAfter() {
		return expected2ValueYbAfter;
	}

	/**
	 *
	 * 月末見込3(YB価) 更新後を取得する。
	 *
	 * @return expected3ValueYbAfter 月末見込3(YB価) 更新後
	 */
	public Long getExpected3ValueYbAfter() {
		return expected3ValueYbAfter;
	}

	/**
	 * 月末見込4(YB価) 更新後を取得する。
	 *
	 * @return expected4ValueYbAfter 月末見込4(YB価) 更新後
	 */
	public Long getExpected4ValueYbAfter() {
		return expected4ValueYbAfter;
	}

	/**
	 * 月末見込5(YB価) 更新後を取得する。
	 *
	 * @return expected5ValueYbAfter 月末見込5(YB価) 更新後
	 */
	public Long getExpected5ValueYbAfter() {
		return expected5ValueYbAfter;
	}

	/**
	 * 月末見込6(YB価) 更新後を取得する。
	 *
	 * @return expected6ValueYbAfter 月末見込6(YB価) 更新後
	 */
	public Long getExpected6ValueYbAfter() {
		return expected6ValueYbAfter;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
