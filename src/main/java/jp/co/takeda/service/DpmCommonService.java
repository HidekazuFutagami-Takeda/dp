package jp.co.takeda.service;

import jp.co.takeda.model.Cal;

/**
 * 管理の共通サービスインターフェース
 *
 * @author rna8405
 */
public interface DpmCommonService {

	/**
	 * 本日のカレンダーを取得する
	 * @return 本日のカレンダー
	 */
	public Cal searchToday();

	//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	/**
	 * 月内営業日順位で検索
	 * @param calYear 年
	 * @param calMonth 月
	 * @param bizDays 月内営業日順位
	 * @return カレンダー
	 */
	public Cal searchBizDays(String calYear, String calMonth, Integer bizDays);
	//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
}
