package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;

/**
 * ワクチン用施設特約店別計画ステータスのステータスチェックを行なうサービスインターフェイス
 * 
 * @author nozaki
 */
public interface DpsInsWsStatusCheckForVacService {

	/**
	 * 組織コード(特約店G)、計画対象品目を指定して、ワクチン用施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 * 
	 * @param sosCd3 組織コード(特約店G)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されているワクチン用施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatusForVac> execute(String sosCd3, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード(特約店G)、組織コード(チーム)、計画対象品目を指定して、ワクチン用施設特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 * 
	 * @param sosCd3 組織コード(特約店G)
	 * @param sosCd4 組織コード(チーム)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されているワクチン用施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatusForVac> execute(String sosCd3, String sosCd4, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 従業員情報、計画対象品目を指定して、ワクチン用施設特約店別計画のチェックを行なう。 <br>
	 * 従業員情報の、従業員番号、氏名は必須。 計画対象品目の、品目固定コード、品目名称は必須。
	 * 
	 * @param jgiMstList 従業員情報(従業員番号、氏名は必須)のリスト
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されているワクチン用施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatusForVac> execute(List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

	/**
	 * 計画対象品目を指定して、全小児科MRのワクチン用施設特約店別計画のチェックを行なう。 (J19-0010 対応・コメントのみ修正)<br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 * 
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されているワクチン用施設特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<InsWsPlanStatusForVac> execute(List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList);

}
