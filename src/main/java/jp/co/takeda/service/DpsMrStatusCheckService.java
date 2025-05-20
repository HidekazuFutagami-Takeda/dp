package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;

/**
 * 担当者別計画ステータスのステータスチェックを行なうサービスインターフェイス
 *
 * @author nozaki
 */
public interface DpsMrStatusCheckService {

	/**
	 * 組織コード（営業所）、カテゴリを指定して、担当者別計画ステータスチェックを行なう <br>
	 * 医薬品を対象とする。
	 *
	 * @param sosCd 組織コード（営業所）
	 * @param category カテゴリ
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている担当者別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	//List<MrPlanStatus> execute(String sosCd, ProdCategory category, List<MrStatusForCheck> unallowableStatusList);
	List<MrPlanStatus> execute(String sosCd, String category, List<MrStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード（営業所）、品目を指定して、担当者別計画ステータスチェックを行なう
	 *
	 * @param sosCd 組織コード（営業所）
	 * @param plannedProdList 計画対象品目（品目固定コード、品目名称は必須）
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている担当者別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<MrPlanStatus> execute(String sosCd, List<PlannedProd> plannedProdList, List<MrStatusForCheck> unallowableStatusList);

	/**
	 * 組織コード（営業所）、品目を指定して、担当者別計画ステータスチェックを行なう
	 *
	 * @param sosCd 組織コード（営業所）
	 * @param plannedProdList 計画対象品目（品目固定コード、品目名称は必須）
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている担当者別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	List<MrPlanStatus> execute(String sosCd, PlannedProd plannedProd, List<MrStatusForCheck> unallowableStatusList);

}
