package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ConvertInsKana;

/**
 * DB共通情報にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface CommonDao {

	/**
	 * DBサーバのシステム日付(SYSTIMESTAMP)を取得する。
	 * 
	 * @return システム日付(SYSTIMESTAMP)
	 */
	java.util.Date getSystemTime();

	/**
	 * 施設名変換テーブルを全件取得する。
	 * 
	 * @return 施設名変換テーブル全件
	 * @throws DataNotFoundException データがない場合にスロー
	 */
	List<ConvertInsKana> findAll() throws DataNotFoundException;
}
