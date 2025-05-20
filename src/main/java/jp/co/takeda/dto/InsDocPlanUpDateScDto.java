package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画の検索用DTO
 * 
 * @author siwamoto
 */
public class InsDocPlanUpDateScDto extends DpDto {

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
	 * 施設出力対象区分
	 */
	private final InsType insType;

	/**
	 * 表示区分（0:実績計画あり、1:全施設医師）
	 */
	private String planDispType;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 */
	public InsDocPlanUpDateScDto(String jgiNo, String prodCode, String insType, String planDispType) {
		this.jgiNo = ConvertUtil.parseInteger(jgiNo);
		this.prodCode = prodCode;
		this.insType = InsType.getInstance(insType);
		this.planDispType = planDispType;
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
	 * @return prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 施設出力対象区分を取得する。
	 * 
	 * @return 施設出力対象区分
	 */
	public InsType getInsType() {
		return insType;
	}
	
	/**
	 * 表示区分（実績計画あり、全施設医師）を取得する。
	 * @return 表示区分（実績計画あり、全施設医師）
	 */
	public String getPlanDispType() {
		return planDispType;
	}
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
