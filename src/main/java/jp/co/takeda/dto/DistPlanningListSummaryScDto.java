package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 計画立案準備営業所計画アップロード用サマリーの検索条件(Search Condition)を表すDTO
 *
 * @author khashimoto
 */
public class DistPlanningListSummaryScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード１(本部)
	 */
	private final String sos_Cd1;

	/**
	 * 組織コード２(支店)
	 */
	private final String sos_Cd2;

	/**
	 * 組織コード３(営業所)
	 */
	private final String sos_Cd3;

	/**
	 * カテゴリ
	 */
	private final String category;

	// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 選択されたカテゴリ
	 */
	private final String selectCategory;
	// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	/**
	 *
	 * コンストラクタ
	 *
	 * @param sos_Cd1 組織コード１(本部)
	 * @param sos_Cd2 組織コード２(支店)
	 * @param sos_Cd3 組織コード３(営業所)
	 * @param category 品目のカテゴリ
	 * @param selectCategory 選択されたカテゴリ
	 */
	public DistPlanningListSummaryScDto(String sos_Cd1, String sos_Cd2, String sos_Cd3, String category, String selectCategory) {
		this.sos_Cd1 = sos_Cd1;
		this.sos_Cd2 = sos_Cd2;
		this.sos_Cd3 = sos_Cd3;
		this.category = category;
		// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		this.selectCategory = selectCategory;
		// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	}

	/**
	 * 組織コード１(本部)を取得する。
	 *
	 * @return 組織コード１(本部)
	 */
	public String getSos_Cd1() {
		return sos_Cd1;
	}

	/**
	 * 組織コード２(支店)を取得する。
	 *
	 * @return 組織コード２(支店)
	 */
	public String getSos_Cd2() {
		return sos_Cd2;
	}

	/**
	 * 組織コード３(営業所)を取得する。
	 *
	 * @return 組織コード３(営業所)
	 */
	public String getSos_Cd3() {
		return sos_Cd3;
	}

	/**
	 * 品目のカテゴリを取得する。
	 *
	 * @return 品目のカテゴリ
	 */
	public String getCategory() {
		return category;
	}

	// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 選択されたカテゴリを取得する。
	 *
	 * @return 選択されたカテゴリ
	 */
	public String getSelectCategory() {
		return selectCategory;
	}
	// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
