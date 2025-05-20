package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 組織別計画の検索用DTO
 *
 * @author stakeuchi
 */
public class SosPlanScDto extends DpDto {

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
	 * ONC組織フラグ
	 */
	private final boolean oncSosFlg;

	/**
	 * 品目カテゴリ
	 */
	private final String prodCategory;

	/**
	 * 品目コード
	 */
	private final String prodCode;

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリ
	 */
	private final String sosCategory;

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * コンストラクタ
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param oncSosFlg ONC組織フラグ
	 * @param prodCode 品目コード
	 */
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//	public SosPlanScDto(String sosCd2, String sosCd3, String sosCd4, String prodCode, boolean oncSosFlg) {
	public SosPlanScDto(String sosCd2, String sosCd3, String sosCd4, String prodCategory, String prodCode, boolean oncSosFlg, String sosCategory) {
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCd2 = sosCd2;
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.oncSosFlg = oncSosFlg;
		this.prodCode = prodCode;
		this.prodCategory = prodCategory;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCategory = sosCategory;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
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

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
