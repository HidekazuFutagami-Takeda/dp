package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 品目別計画の検索用DTO
 *
 * @author stakeuchi
 */
public class ProdPlanScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(支店)
	 */
	private final String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * ONC組織フラグ
	 */
	private final boolean oncSosFlg;

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリ
	 */
	private final String sosCategory;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

	/**
	 * 品目カテゴリ
	 */
	private final String prodCategory;

	/**
	 * ワクチンのカテゴリコード
	 */
	private final String vaccineCode;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param prodCategory 品目カテゴリ
	 * @param category カテゴリ
	 */
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//		public ProdPlanScDto(String sosCd2, String sosCd3, String sosCd4, Integer jgiNo, ProdCategory prodCategory, boolean oncSosFlg) {
		public ProdPlanScDto(String sosCd2, String sosCd3, String sosCd4, Integer jgiNo,String prodCategory, boolean oncSosFlg, String sosCategory, String vaccineCode) {
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCd2 = sosCd2;
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.prodCategory = prodCategory;
		this.oncSosFlg = oncSosFlg;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCategory = sosCategory;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.vaccineCode = vaccineCode;
	}

	/**
	 * 組織コード(支店)を取得する。
	 *
	 * @return sosCd2 組織コード(支店)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
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
	 * ONC組織フラグを取得する。
	 *
	 * @return ONC組織フラグ
	 */
	public boolean getOncSosFlg() {
		return oncSosFlg;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getSosCategory() {
		return sosCategory;
	}
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * ワクチンのカテゴリコードを取得する。
	 *
	 * @return vaccineCode ワクチンのカテゴリコード
	 */
	public String getVaccineCode() {
		return vaccineCode;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
