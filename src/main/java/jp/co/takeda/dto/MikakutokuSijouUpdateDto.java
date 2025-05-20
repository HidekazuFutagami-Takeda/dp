package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 未獲得市場の更新用DTO
 * 
 * @author stakeuchi
 */
public class MikakutokuSijouUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 未獲得市場(計算用)
	 */
	private final Long yakkouSijouMikakutoku;

	/**
	 * 増減金額(DB登録値)
	 */
	private final Long modifyAmount;

	/**
	 * コンストラクタ
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param yakkouSijouMikakutoku 未獲得市場(計算用)
	 * @param modifyAmount 増減金額(DB登録値)
	 */
	public MikakutokuSijouUpdateDto(Long seqKey, Date upDate, Long yakkouSijouMikakutoku, Long modifyAmount) {
		this.seqKey = seqKey;
		this.upDate = (Date) upDate.clone();
		this.yakkouSijouMikakutoku = yakkouSijouMikakutoku;
		this.modifyAmount = modifyAmount;
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
		return (Date) upDate.clone();
	}

	/**
	 * 未獲得市場(計算用)を取得する。
	 * 
	 * @return yakkouSijouMikakutoku 未獲得市場(計算用)
	 */
	public Long getYakkouSijouMikakutoku() {
		return yakkouSijouMikakutoku;
	}

	/**
	 * 増減金額(DB登録値)を取得する。
	 * 
	 * @return modifyAmount 増減金額(DB登録値)
	 */
	public Long getModifyAmount() {
		return modifyAmount;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
