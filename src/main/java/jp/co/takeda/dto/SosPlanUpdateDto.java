package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 組織別計画の更新用DTO
 * 
 * @author stakeuchi
 */
public class SosPlanUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 雑組織フラグ(true：雑組織、false：雑組織でない)
	 */
	private final Boolean etcSosFlg;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 施設区分
	 */
	private final InsType insType;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * Y価ベース 更新前
	 */
	private final Long yBaseValueBefore;

	/**
	 * Y価ベース 更新後
	 */
	private final Long yBaseValueAfter;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード
	 * @param etcSosFlg 雑組織フラグ
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目コード
	 * @param insType 施設区分
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日時
	 * @param yBaseValueBefore Y価ベース 更新前
	 * @param yBaseValueAfter Y価ベース 更新後
	 */
	public SosPlanUpdateDto(String sosCd, Boolean etcSosFlg, Integer jgiNo, String prodCode, InsType insType, Long seqKey, Date upDate, Long yBaseValueBefore, Long yBaseValueAfter) {
		this.sosCd = sosCd;
		this.etcSosFlg = etcSosFlg;
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insType = insType;
		this.seqKey = seqKey;
		this.upDate = upDate;
		this.yBaseValueBefore = yBaseValueBefore;
		this.yBaseValueAfter = yBaseValueAfter;
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
	 * 雑組織フラグを取得する。
	 * 
	 * @return 雑組織フラグ
	 */
	public Boolean getEtcSosFlg() {
		return etcSosFlg;
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
	 * 施設区分を取得する。
	 * 
	 * @return insType 施設区分
	 */
	public InsType getInsType() {
		return insType;
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
	 * Y価ベース 更新前を取得する。
	 * 
	 * @return yBaseValueBefore Y価ベース 更新前
	 */
	public Long getYBaseValueBefore() {
		return yBaseValueBefore;
	}

	/**
	 * Y価ベース 更新後を取得する。
	 * 
	 * @return yBaseValueAfter Y価ベース 更新後
	 */
	public Long getYBaseValueAfter() {
		return yBaseValueAfter;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
