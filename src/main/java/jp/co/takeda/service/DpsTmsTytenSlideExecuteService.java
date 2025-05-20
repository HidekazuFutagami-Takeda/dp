package jp.co.takeda.service;

import java.util.Date;
import java.util.List;

import jp.co.takeda.model.WsPlanStatus;

/**
 * 特約店別計画スライド処理サービスインターフェイス
 * 
 * @author yokokawa
 */
public interface DpsTmsTytenSlideExecuteService {
	/**
	 * 特約店別計画スライド処理を実行する。
	 * 
	 * @param sosCd3List 組織コード(支店)リスト
	 * @param prodCodeList 品目固定コードリスト
	 * @param wsPlanStatusList 特約店別計画立案ステータス
	 * @param startTime 開始日時
	 */
	public void executeSlide(List<String> sosCd3List, List<String> prodCodeList, List<WsPlanStatus> wsPlanStatusList, Date startTime);
}
