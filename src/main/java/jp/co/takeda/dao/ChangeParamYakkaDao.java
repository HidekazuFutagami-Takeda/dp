package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ChangeParamYakka;
import jp.co.takeda.model.div.Term;

/**
 * 薬価改定指定率にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ChangeParamYakkaDao {

	/**
	 * 薬価改定指定率を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 薬価改定指定率
	 */
	ChangeParamYakka search(Long seqKey) throws DataNotFoundException;

	/**
	 * 薬価改定指定率を登録する。
	 * 
	 * @param record 薬価改定指定率
	 * @throws DuplicateException
	 */
	void insert(ChangeParamYakka record) throws DuplicateException;

	/**
	 * 薬価改定指定率を更新する。
	 * 
	 * @param record 薬価改定指定率
	 * @return 更新件数
	 */
	int update(ChangeParamYakka record);

	/**
	 * 薬価改定指定率の最終更新日時を取得する。
	 * 
	 * @param calYear 年度
	 * @param calTerm 期
	 * @return 最終更新日時(データ無の場合はNULL)
	 */
	Date getLastUpDate(String calYear, Term calTerm);
}
