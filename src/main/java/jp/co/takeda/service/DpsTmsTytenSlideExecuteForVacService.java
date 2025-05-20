package jp.co.takeda.service;

import java.util.Date;
import java.util.List;

import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.WsPlanStatusForVac;

/**
 * ワクチン用特約店別計画スライド処理サービスインターフェイス
 *
 * @author yokokawa
 */
public interface DpsTmsTytenSlideExecuteForVacService {
	/**
	 * ワクチン用特約店別計画スライド処理を実行する。
	 *
	 * @param prodCodeList 品目固定コードリスト
	 * @param wsPlanStatusList ワクチン用特約店別計画立案ステータス
	 * @param startTime 開始日時
	 */
	public void executeSlide(List<String> prodCodeList, List<WsPlanStatusForVac> wsPlanStatusForVacList, Date startTime, List<WsPlanStatus> wsPlanStatusList);
}
