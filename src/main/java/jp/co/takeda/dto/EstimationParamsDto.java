package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpAsyncDto;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUser;

/**
 * 試算処理パラメータクラス
 *
 * @author tkawabata
 */
public class EstimationParamsDto extends DpAsyncDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 実行者従業員情報
	 */
	private final DpUser dpUser;

	/**
	 * 更新前の担当者別計画ステータスリスト
	 */
	private final List<EstimationExecOrgDto> orgMrPlanStatusList;

	/**
	 * 試算タイプ
	 */
	private final CalcType calcType;

// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	/**
	 * カテゴリーコード
	 */
	private final String category;
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
// add Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
	/**
	 * 検索時のカテゴリーコード
	 */
	private final String categorySearch;

	/**
	 * 選択しているカテゴリーコード
	 */
	private final String categorySelect;
// add End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。

	/**
	 * コンストラクタ
	 *
	 * @param context メタ情報
	 * @param sosCd3 組織コード(営業所)
	 * @param dpUser 実行者従業員情報
	 * @param orgMrPlanStatusList 更新前の担当者別計画ステータスリスト
	 * @param calcType 試算タイプ (※本パラメータは、試算バッチ実行前の更新時に使用する。<br>
	 * そのため、バッチ起動のみであれば、NULL可とする）
	 */
// mod Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
	//public EstimationParamsDto(DpMetaInfo dpMetaInfo, String sosCd3, DpUser dpUser, List<EstimationExecOrgDto> orgMrPlanStatusList, CalcType calcType, String category) {
	public EstimationParamsDto(DpMetaInfo dpMetaInfo, String sosCd3, DpUser dpUser, List<EstimationExecOrgDto> orgMrPlanStatusList, CalcType calcType
			, String category, String categorySearch, String categorySelect) {

// mod End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。

		super(dpMetaInfo);
		this.sosCd3 = sosCd3;
		this.dpUser = dpUser;
		this.orgMrPlanStatusList = orgMrPlanStatusList;
		this.calcType = calcType;
// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
		this.category = category;
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
// add Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
		this.categorySearch = categorySearch;
		this.categorySelect = categorySelect;
// add End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。

	}

	//---------------------
	// Getter & Setter
	// --------------------

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 実行者従業員情報を取得する。
	 *
	 * @return 実行者従業員情報
	 */
	public DpUser getDpUser() {
		return dpUser;
	}

	/**
	 * 更新前の担当者別計画ステータスリストを取得する。
	 *
	 * @return 更新前の担当者別計画ステータスリスト
	 */
	public List<EstimationExecOrgDto> getOrgMrPlanStatusList() {
		return orgMrPlanStatusList;
	}

	/**
	 * 試算タイプを取得する。
	 *
	 * @return 試算タイプ
	 */
	public CalcType getCalcType() {
		return calcType;
	}

// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	/**
	 * カテゴリーコードを取得する。
	 *
	 * @return カテゴリーコード
	 */
	public String getCategory() {
		return category;
	}
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更

// add Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
	/**
	 * 検索時のカテゴリーコードを取得する。
	 *
	 * @return 検索時のカテゴリーコード
	 */
	public String getCategorySearch() {
		return categorySearch;
	}

	/**
	 * 選択しているカテゴリーコードを取得する。
	 *
	 * @return 選択しているカテゴリーコード
	 */
	public String getCategorySelect() {
		return categorySelect;
	}
// add End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
}
