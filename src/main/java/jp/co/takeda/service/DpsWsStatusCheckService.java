package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;

/**
 * 特約店別計画ステータスのステータスチェックを行なうサービスインターフェイス
 *
 * @author nozaki
 */
public interface DpsWsStatusCheckService {

	/**
	 * 支店、品目を指定して、特約店別計画のチェックを行なう。 <br>
	 * 計画対象品目の、品目固定コード、品目名称は必須。
	 *
	 * @param sosMst 支店の組織情報(組織コード、部門名正式は必須)
	 * @param plannedProdList 計画対象品目(品目固定コード、品目名称は必須)のリスト
	 * @param unallowableDistStatusList 許可しない配分ステータスのリスト
	 * @param unallowableSlideStatusList 許可しないスライドステータスのリスト
	 * @return 現在DBに登録されている特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	public List<WsPlanStatus> execute(SosMst sosMst, List<PlannedProd> plannedProdList, List<WsDistStatusForCheck> unallowableDistStatusList,
		List<WsSlideStatusForCheck> unallowableSlideStatusList);

	/**
	 * カテゴリを指定して、特約店別計画のチェックを行なう。 全支店を対象とする。 <br>
	 * カテゴリにnullを指定した場合は、MMP品・仕入品の両方を対象とする。
	 *
	 * @param category カテゴリ(null可)
	 * @param unallowableDistStatusList 許可しない配分ステータスのリスト
	 * @param unallowableSlideStatusList 許可しないスライドステータスのリスト
	 * @return 現在DBに登録されている特約店別計画立案ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
	public List<WsPlanStatus> execute(String category, List<WsDistStatusForCheck> unallowableDistStatusList, List<WsSlideStatusForCheck> unallowableSlideStatusList);
}
