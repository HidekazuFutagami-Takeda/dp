package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分ミスを表すDTO
 * 
 * @author khashimoto
 */
public class DistributionMissDto extends DpDto {

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
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 施設出力対象区分
	 */
	private final InsType insType;

	/**
	 * 計画値
	 */
	private final Long plannedValue;

	/**
	 * 差額
	 */
	private final Long diffValue;

	/**
	 * メッセージコード
	 */
	private final String messageCode;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * <pre>
	 * 従業員番号がnull指定の場合、組織別配分ミス
	 * null以外の場合、担当者別配分ミス
	 * </pre>
	 * 
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param plannedValue 計画値
	 * @param diffValue
	 * @param messageCode メッセージコード
	 */
	public DistributionMissDto(String sosCd, Integer jgiNo, String prodCode, InsType insType, Long plannedValue, Long diffValue, String messageCode) {
		this.sosCd = sosCd;
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insType = insType;
		this.plannedValue = plannedValue;
		this.diffValue = diffValue;
		this.messageCode = messageCode;
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
	 * 施設区分を取得する。
	 * 
	 * @return 施設区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 計画値を取得する。
	 * 
	 * @return 計画値
	 */
	public Long getPlannedValue() {
		return plannedValue;
	}

	/**
	 * 差額を取得する。
	 * 
	 * @return 差額
	 */
	public Long getDiffValue() {
		return diffValue;
	}

	/**
	 * メッセージコードを取得する。
	 * 
	 * @return メッセージコード
	 */
	public String getMessageCode() {
		return messageCode;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
