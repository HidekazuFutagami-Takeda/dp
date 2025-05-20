package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設特約店別計画の検索用DTO
 *
 * @author siwamoto
 */
public class InsWsPlanUpDateScDto extends DpDto {

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
	 * 参照品目固定コード
	 */
	private final String refProdCode;

	/**
	 * 参照品目全表示フラグ
	 */
	private final boolean refProdAllFlg;

	/**
	 * カテゴリ
	 */
	private final String category;

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param refProdCode 参照品目固定コード
	 * @param refProdAllFlg 参照品目全表示フラグ
	 * @param category カテゴリ
	 */
	public InsWsPlanUpDateScDto(String jgiNo, String prodCode, String insType, String refProdCode, boolean refProdAllFlg, String category) {
		this.jgiNo = ConvertUtil.parseInteger(jgiNo);
		this.prodCode = prodCode;
		this.insType = InsType.getInstance(insType);
		this.refProdCode = refProdCode;
		this.refProdAllFlg = refProdAllFlg;
		this.category = category;
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
	 * 参照品目固定コードを取得する。
	 *
	 * @return 参照品目固定コード
	 */
	public String getRefProdCode() {
		return refProdCode;
	}

	/**
	 * 参照品目全表示フラグを取得する。
	 *
	 * @return 参照品目全表示フラグ
	 */
	public boolean getRefProdAllFlg() {
		return refProdAllFlg;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
