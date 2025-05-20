package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 *
 * (医)特約店品目別計画編集画面明細を表すDTO
 *
 * @author siwamoto
 */
public class ManageWsPlanProdDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * [UH] S価ベース
	 */
	private final Long baseSUH;

	/**
	 * [UH] Y価ベース
	 */
	private final Long baseYUH;

	/**
	 * [UH] シーケンスキー
	 */
	private final Long seqKeyUH;

	/**
	 * [UH] 最終更新者
	 */
	private final String upDateJgiNameUH;

	/**
	 * [UH] 最終更新日
	 */
	private final Date upDateUH;

	/**
	 * [P] S価ベース
	 */
	private final Long baseSP;

	/**
	 * [P] Y価ベース
	 */
	private final Long baseYP;

	/**
	 * [P] シーケンスキー
	 */
	private final Long seqKeyP;

	/**
	 * [P] 最終更新者
	 */
	private final String upDateJgiNameP;

	/**
	 * [P] 最終更新日
	 */
	private final Date upDateP;

	/**
	 * [Z] S価ベース
	 */
	private final Long baseSZ;

	/**
	 * [Z] Y価ベース
	 */
	private final Long baseYZ;

	/**
	 * [Z] シーケンスキー
	 */
	private final Long seqKeyZ;

	/**
	 * [Z] 最終更新者
	 */
	private final String upDateJgiNameZ;

	/**
	 * [Z] 最終更新日
	 */
	private final Date upDateZ;


	/**
	 * [UH] T/Y変換率
	 */
	private final Double tyChangeRateUH;

	/**
	 * [P] T/Y変換率
	 */
	private final Double tyChangeRateP;

	/**
	 * [P] T/Y変換率
	 */
	private final Double tyChangeRateZ;

	/**
	 * コンストラクタ
	 *
	 * @param prodName 品目名称
	 * @param prodCode 品目固定コード
	 * @param baseSTH [UH] S価ベース
	 * @param baseYUH [UH] Y価ベース
	 * @param seqKeyUH [UH] シーケンスキー
	 * @param upDateJgiNameUH [UH] 最終更新者
	 * @param upDateUH [UH] 最終更新日
	 * @param baseSP [P] S価ベース
	 * @param baseYP [P] Y価ベース
	 * @param seqKeyP [P] シーケンスキー
	 * @param upDateJgiNameP [P] 最終更新者
	 * @param upDateP [P] 最終更新日
	 * @param baseSZ [Z] S価ベース
	 * @param baseYZ [Z] Y価ベース
	 * @param seqKeyZ [Z] シーケンスキー
	 * @param upDateJgiNameZ [Z] 最終更新者
	 * @param upDateZ [Z] 最終更新日
	 * @param tyChangeRateUH [UH] T/Y変換率
	 * @param tyChangeRateP [P] T/Y変換率
	 * @param tyChangeRateZ [Z] T/Y変換率
	 */
	public ManageWsPlanProdDetailDto(String prodName, String prodCode, Long baseSUH, Long baseYUH, Long seqKeyUH, String upDateJgiNameUH, Date upDateUH, Long baseSP,
		Long baseYP, Long seqKeyP, String upDateJgiNameP, Date upDateP, Long baseSZ, Long baseYZ, Long seqKeyZ, String upDateJgiNameZ, Date upDateZ, Double tyChangeRateUH,
		Double tyChangeRateP, Double tyChangeRateZ) {
		this.prodName = prodName;
		this.prodCode = prodCode;
		this.baseSUH = baseSUH;
		this.baseYUH = baseYUH;
		this.seqKeyUH = seqKeyUH;
		this.upDateJgiNameUH = upDateJgiNameUH;
		this.upDateUH = upDateUH;
		this.baseYP = baseYP;
		this.baseSP = baseSP;
		this.seqKeyP = seqKeyP;
		this.upDateJgiNameP = upDateJgiNameP;
		this.upDateP = upDateP;
		this.baseYZ = baseYZ;
		this.baseSZ = baseSZ;
		this.seqKeyZ = seqKeyZ;
		this.upDateJgiNameZ = upDateJgiNameZ;
		this.upDateZ = upDateZ;
		this.tyChangeRateUH = tyChangeRateUH;
		this.tyChangeRateP = tyChangeRateP;
		this.tyChangeRateZ = tyChangeRateZ;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return prodName 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * [UH] S価ベースを取得する。
	 *
	 * @return beforeBaseYUH S価ベース
	 */
	public Long getBaseSUH() {
		return baseSUH;
	}

	/**
	 * [UH] Y価ベースを取得する。
	 *
	 * @return baseYUH [UH] T価ベース
	 */
	public Long getBaseYUH() {
		return baseYUH;
	}

	/**
	 * [UH] シーケンスキーを取得する。
	 *
	 * @return seqKey [UH] シーケンスキー
	 */
	public Long getSeqKeyUH() {
		return seqKeyUH;
	}

	/**
	 * [UH] 最終更新者を取得する。
	 *
	 * @return upDateJgiNameUH [UH] 最終更新者
	 */
	public String getUpDateJgiNameUH() {
		return upDateJgiNameUH;
	}

	/**
	 * [UH] 最終更新日を取得する。
	 *
	 * @return upDateUH [UH] 最終更新日
	 */
	public Date getUpDateUH() {
		return upDateUH;
	}

	/**
	 * [P] Y価ベースを取得する。
	 *
	 * @return baseYP [P] Y価ベース
	 */
	public Long getBaseYP() {
		return baseYP;
	}

	/**
	 * [P] S価ベースを取得する。
	 *
	 * @return beforeBaseYP [P] S価ベース
	 */
	public Long getBaseSP() {
		return baseSP;
	}

	/**
	 * [P] シーケンスキーを取得する。
	 *
	 * @return seqKeyP [P] シーケンスキー
	 */
	public Long getSeqKeyP() {
		return seqKeyP;
	}

	/**
	 * [P] 最終更新者を取得する。
	 *
	 * @return upDateJgiNameP [P] 最終更新者
	 */
	public String getUpDateJgiNameP() {
		return upDateJgiNameP;
	}

	/**
	 * [P] 最終更新日を取得する。
	 *
	 * @return upDateP [P] 最終更新日
	 */
	public Date getUpDateP() {
		return upDateP;
	}

	/**
	 * [UH] T/Y変換率を取得する。
	 *
	 * @return [UH] T/Y変換率
	 */
	public Double getTyChangeRateUH() {
		return tyChangeRateUH;
	}

	/**
	 * [P] T/Y変換率を取得する。
	 *
	 * @return [P] T/Y変換率
	 */
	public Double getTyChangeRateP() {
		return tyChangeRateP;
	}

	/**
	 * @return baseYZ
	 */
	public Long getBaseYZ() {
		return baseYZ;
	}

	/**
	 * @return beforeBaseYZ
	 */
	public Long getBaseSZ() {
		return baseSZ;
	}

	/**
	 * @return seqKeyZ
	 */
	public Long getSeqKeyZ() {
		return seqKeyZ;
	}

	/**
	 * @return upDateJgiNameZ
	 */
	public String getUpDateJgiNameZ() {
		return upDateJgiNameZ;
	}

	/**
	 * @return upDateZ
	 */
	public Date getUpDateZ() {
		return upDateZ;
	}

	/**
	 * @return tyChangeRateZ
	 */
	public Double getTyChangeRateZ() {
		return tyChangeRateZ;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
