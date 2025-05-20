package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlanStatusForVac;

/**
 * ワクチン用特約店別計画ステータスのステータスチェックを行なうサービスインターフェイス
 * 
 * @author nozaki
 */
public interface DpsWsStatusCheckForVacService {

	/**
	 * 品目を指定して、ワクチン用特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 * 
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableSlideStatusList 許可しないスライドステータスのリスト
	 * @return 現在DBに登録されているワクチン用特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	public List<WsPlanStatusForVac> execute(List<PlannedProd> plannedProdList, List<WsSlideStatusForCheck> unallowableSlideStatusList);

	/**
	 * 全ワクチン品を対象として、ワクチン用特約店別計画のチェックを行なう。 <br>
	 * 
	 * @param unallowableSlideStatusList 許可しないスライドステータスのリスト
	 * @return 現在DBに登録されているワクチン用特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	public List<WsPlanStatusForVac> execute(List<WsSlideStatusForCheck> unallowableSlideStatusList);

}
