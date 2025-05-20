package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画配分結果を表すDTO
 * 
 * @author khashimoto
 */
public class InsWsPlanDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * TMS特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 配分値
	 */
	private final Long distValue;

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
	 * 
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @param distValue 配分値
	 * @param specialInsPlanFlg 特定施設個別計画かを示すフラグ
	 * @param delInsFlg 削除施設かを示すフラグ
	 */
	public InsWsPlanDto(Integer jgiNo, String prodCode, String insNo, String tmsTytenCd, Long distValue, Boolean specialInsPlanFlg, Boolean exceptDistInsFlg, Boolean delInsFlg) {
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insNo = insNo;
		this.tmsTytenCd = tmsTytenCd;
		this.distValue = distValue;
		this.specialInsPlanFlg = specialInsPlanFlg;
		this.exceptDistInsFlg = exceptDistInsFlg;
		this.delInsFlg = delInsFlg;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param dto 施設特約店別計画を表すDTO
	 * @param distValue 配分値
	 */
	public InsWsPlanDto(InsWsPlanDto dto, Long distValue) {
		this.jgiNo = dto.getJgiNo();
		this.prodCode = dto.getProdCode();
		this.insNo = dto.getInsNo();
		this.tmsTytenCd = dto.getTmsTytenCd();
		this.distValue = distValue;
		this.specialInsPlanFlg = dto.getSpecialInsPlanFlg();
		this.exceptDistInsFlg = dto.getExceptDistInsFlg();
		this.delInsFlg = dto.getDelInsFlg();
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
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
	 * 施設コードを取得する。
	 * 
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * TMS特約店コードを取得する。
	 * 
	 * @return TMS特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 配分値を取得する。
	 * 
	 * @return 配分値
	 */
	public Long getDistValue() {
		return distValue;
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
