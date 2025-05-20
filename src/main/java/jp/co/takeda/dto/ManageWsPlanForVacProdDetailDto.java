package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * (ワ)特約店品目別計画編集画面明細を表すDTO
 * 
 * @author khashimoto
 */
public class ManageWsPlanForVacProdDetailDto extends DpDto {

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
	 * B価ベース（編集前）
	 */
	private final Long baseB;

	/**
	 * T価ベース
	 */
	private final Long baseT;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新者
	 */
	private final String upDateJgiName;

	/**
	 * 最終更新日
	 */
	private final Date upDate;

	/**
	 * T/B変換率
	 */
	private final Double tbChangeRate;

	/**
	 * コンストラクタ
	 * 
	 * @param prodName 品目名称
	 * @param prodCode 品目固定コード
	 * @param baseB B価ベース
	 * @param baseT T価ベース
	 * @param seqKey シーケンスキー
	 * @param upDateJgiName 最終更新者
	 * @param upDate 最終更新日
	 * @param tbChangeRate T/B変換率
	 */
	public ManageWsPlanForVacProdDetailDto(String prodName, String prodCode, Long baseB, Long baseT, Long seqKey, String upDateJgiName, Date upDate, Double tbChangeRate) {
		this.prodName = prodName;
		this.prodCode = prodCode;
		this.baseB = baseB;
		this.baseT = baseT;
		this.seqKey = seqKey;
		this.upDateJgiName = upDateJgiName;
		this.upDate = upDate;
		this.tbChangeRate = tbChangeRate;
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
	 * B価ベースを取得する。
	 * 
	 * @return baseB B価ベース
	 */
	public Long getBaseB() {
		return baseB;
	}

	/**
	 * T価ベースを取得する。
	 * 
	 * @return baseT T価ベース
	 */
	public Long getBaseT() {
		return baseT;
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
	 * 最終更新者を取得する。
	 * 
	 * @return upDateJgiName 最終更新者
	 */
	public String getUpDateJgiName() {
		return upDateJgiName;
	}

	/**
	 * 最終更新日を取得する。
	 * 
	 * @return upDate 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * T/B変換率を取得する。
	 * 
	 * @return T/B変換率
	 */
	public Double getTbChangeRate() {
		return tbChangeRate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
