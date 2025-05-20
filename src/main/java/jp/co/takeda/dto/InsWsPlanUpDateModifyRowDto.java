package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画の追加・更新用行DTO
 * 
 * @author siwamoto
 */
public class InsWsPlanUpDateModifyRowDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * シーケンスキー(追加時null)
	 */
	private final Long seqKey;

	/**
	 * 更新日時(追加時null)
	 */
	private final Date plannedUpDate;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 修正計画
	 */
	private final Long plannedValue;

	/**
	 * 特定施設個別計画かを示すフラグ
	 */
	private final Boolean specialInsPlanFlg;

	/**
	 * 配分除外施設かを示すフラグ
	 */
	private final Boolean exceptDistInsFlg;

	/**
	 * 削除施設かを示すフラグ
	 */
	private final Boolean delInsFlg;

	/**
	 * コンストラクタ
	 * 
	 * @param seqKey シーケンスキー
	 * @param plannedUpDate 更新日時
	 * @param insNo 施設コード
	 * @param tmsTytenCd 特約店コード
	 * @param plannedValue 修正計画
	 * @param specialInsPlanFlg 特定施設個別計画かを示すフラグ
	 * @param exceptDistInsFlg 配分除外施設かを示すフラグ
	 * @param delInsFlg 削除施設かを示すフラグ
	 */
	public InsWsPlanUpDateModifyRowDto(String seqKey, Date plannedUpDate, String insNo, String tmsTytenCd, Long plannedValue, Boolean specialInsPlanFlg, Boolean exceptDistInsFlg,
		Boolean delInsFlg) {
		this.seqKey = ConvertUtil.parseLong(seqKey);
		this.plannedUpDate = plannedUpDate;
		this.insNo = insNo;
		this.tmsTytenCd = tmsTytenCd;
		this.plannedValue = plannedValue;
		this.specialInsPlanFlg = specialInsPlanFlg;
		this.exceptDistInsFlg = exceptDistInsFlg;
		this.delInsFlg = delInsFlg;
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
	 * 更新日時を取得する。
	 * 
	 * @return plannedUpDate 更新日時
	 */
	public Date getPlannedUpDate() {
		return plannedUpDate;
	}

	/**
	 * 特約店コードを取得する。
	 * 
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
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
	 * 修正計画を取得する。
	 * 
	 * @return plannedValue 修正計画
	 */
	public Long getPlannedValue() {
		return plannedValue;
	}

	/**
	 * 特定施設個別計画かを示すフラグを取得する。
	 * 
	 * @return 特定施設個別計画かを示すフラグ
	 */
	public Boolean getSpecialInsPlanFlg() {
		return specialInsPlanFlg;
	}

	/**
	 * 配分除外施設かを示すフラグを取得する。
	 * 
	 * @return 配分除外施設かを示すフラグ
	 */
	public Boolean getExceptDistInsFlg() {
		return exceptDistInsFlg;
	}

	/**
	 * 削除施設かを示すフラグを取得する。
	 * 
	 * @return 削除施設かを示すフラグ
	 */
	public Boolean getDelInsFlg() {
		return delInsFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
