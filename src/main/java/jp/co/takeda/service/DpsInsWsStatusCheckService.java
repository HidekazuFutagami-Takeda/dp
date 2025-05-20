package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;

/**
 * 施設特約店別計画ステータスのステータスチェックを行なうサービスインターフェイス
 *
 * @author nozaki
 */
public interface DpsInsWsStatusCheckService {

	/**
	 * 組織コード(営業所)、計画対象品目を指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatus> execute(String sosCd3, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード(営業所)、計画対象品目を指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProd 計画対象品目(品目固定コード、品目名称は必須)
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatus> execute(String sosCd3, PlannedProd plannedProd, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード(営業所)、カテゴリを指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
//	List<InsWsPlanStatus> execute(String sosCd3, ProdCategory category, List<InsWsStatusForCheck> unallowableStatusList);
	List<InsWsPlanStatus> execute(String sosCd3, String category, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード(営業所)、組織コード(チーム)、計画対象品目を指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatus> execute(String sosCd3, String sosCd4, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 従業員情報、計画対象品目を指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 従業員情報の、従業員番号、氏名は必須。 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param jgiMstList 従業員情報(従業員番号、氏名は必須)のリスト
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatus> execute(List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 従業員情報、カテゴリを指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 従業員情報の、従業員番号、氏名は必須。
	 *
	 * @param jgiMstList 従業員情報(従業員番号、氏名は必須)のリスト
	 * @param category カテゴリ（NULLの場合は、全品目
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
//	List<InsWsPlanStatus> execute(List<JgiMst> jgiMstList, ProdCategory category, List<InsWsStatusForCheck> unallowableStatusList);
	List<InsWsPlanStatus> execute(List<JgiMst> jgiMstList, String category, List<InsWsStatusForCheck> unallowableStatusList);

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 従業員情報、計画対象品目を指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 従業員情報の、従業員番号、氏名は必須。 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param jgiMstList 従業員情報(従業員番号、氏名は必須)のリスト
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatus> executeKakutei(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード(営業所)、組織コード(チーム)、計画対象品目を指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatus> executeKakutei(String sosCd3, String sosCd4, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード(営業所)、計画対象品目を指定して、施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatus> executeKakutei(String sosCd3, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応


}
