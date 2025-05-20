package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画追加施設過去実績検索用DTO
 *
 * @author siwamoto
 */
public class InsWsPlanUpDateMonNnuScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 参照品目固定コード1
	 */
	private final String refProdCode1;

	/**
	 * 参照品目固定コード2
	 */
	private final String refProdCode2;

	/**
	 * 参照品目固定コード3
	 */
	private final String refProdCode3;

	/**
	 * 従業員番号
	 */
	private final String jgiNo;

	/**
	 * 対象区分
	 */
	private final String insType;

	/**
	 * コンストラクタ
	 *
	 * @param insNo 施設コード
	 * @param tmsTytenCd 特約店コード
	 * @param prodCode 品目固定コード
	 * @param refProdCode1 参照品目固定コード1
	 * @param refProdCode2 参照品目固定コード2
	 * @param refProdCode3 参照品目固定コード3
	 * @param jginNo 従業員番号
	 * @param insType 対象区分
	 */
	public InsWsPlanUpDateMonNnuScDto(String insNo, String tmsTytenCd, String prodCode, String refProdCode1, String refProdCode2, String refProdCode3, String jgiNo, String insType) {
		this.insNo = insNo;
		this.tmsTytenCd = tmsTytenCd;
		this.prodCode = prodCode;
		this.refProdCode1 = refProdCode1;
		this.refProdCode2 = refProdCode2;
		this.refProdCode3 = refProdCode3;
		this.jgiNo = jgiNo;
		this.insType = insType;
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
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
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
	 * 参照品目固定コード1を取得する。
	 *
	 * @return refProdCode1
	 */
	public String getRefProdCode1() {
		return refProdCode1;
	}

	/**
	 * 参照品目固定コード2を取得する。
	 *
	 * @return refProdCode2
	 */
	public String getRefProdCode2() {
		return refProdCode2;
	}

	/**
	 * 参照品目固定コード3を取得する。
	 *
	 * @return refProdCode3
	 */
	public String getRefProdCode3() {
		return refProdCode3;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 対象区分を取得する
	 *
	 * @return
	 */
	public String getInsType() {
		return insType;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
