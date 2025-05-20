package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.Cal;

/**
 * カレンダーにアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface CalDao {

	/**
	 * カレンダーを取得する。
	 *
	 * @param calYear 年
	 * @param calMonth 月
	 * @param calDay 日
	 * @return カレンダー
	 * @throws DataNotFoundException
	 */
	Cal search(String calYear, String calMonth, String calDay) throws DataNotFoundException;

	/**
	 * 指定した営業日後の日付を取得する。
	 *
	 * <pre>
	 * 基準日付を含まない、指定した営業日数後の営業日を取得する。
	 * を取得する。
	 * </pre>
	 *
	 * @param baseDate 基準日付
	 * @param days 営業日数
	 * @return 指定した営業日後の日付
	 * @throws DataNotFoundException
	 */
	java.util.Date searchBusinessDay(Date baseDate, Integer days) throws DataNotFoundException;

	/**
	 * 本日の日付を取得する。
	 *
	 * @return 本日日付
	 * @throws DataNotFoundException
	 */
	Cal searchToday() throws DataNotFoundException;

	//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	/**
	 * 月内営業日順位で検索する。
	 *
	 * @param calYear 年
	 * @param calMonth 月
	 * @param bizDays 月内営業日順位
	 * @return カレンダー
	 * @throws DataNotFoundException
	 */
	Cal searchBizDays(String calYear, String calMonth, Integer bizDays) throws DataNotFoundException;
	//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
}
