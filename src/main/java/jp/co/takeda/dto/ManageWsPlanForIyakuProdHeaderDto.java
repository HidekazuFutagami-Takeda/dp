package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店品目別計画編集画面ヘッダを表すDTO
 *
 * @author siwamoto
 */
public class ManageWsPlanForIyakuProdHeaderDto extends ManageWsPlanHeaderDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 *
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenName 特約店名称
	 * @param prodCategoryName カテゴリ名
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public ManageWsPlanForIyakuProdHeaderDto(String tmsTytenCd, String tmsTytenName, String prodCategoryName, boolean planTaiGaiFlgTok) {
		super(tmsTytenCd, tmsTytenName);
		this.prodCategoryName = prodCategoryName;
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * カテゴリ名
	 */
	private final String prodCategoryName;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private boolean planTaiGaiFlgTok;

	/**
	 * 品目カテゴリ名を取得する。
	 *
	 * @return 品目カテゴリ名
	 */
	public String getProdCategoryName() {
		return prodCategoryName;
	}

	/**
	 * 計画立案対象外フラグ(当期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
