package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.DB_ERROR;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ConvertInsKana;

import org.springframework.stereotype.Repository;

/**
 * DB共通情報にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("commonDao")
public class CommonDaoImpl extends AbstractDao implements CommonDao {

	public static final String SQL_MAP = "COMMON_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// DBサーバのシステム日付(SYSTIMESTAMP)を取得
	public Date getSystemTime() {
		try {
			return dataAccess.queryForObject(getSqlMapName() + ".selectSysTimeStamp", null);
		} catch (DataNotFoundException e) {
			String errMsg = "DBサーバのシステム日付が取得不可";
			throw new SystemException(new Conveyance(DB_ERROR, errMsg), e);
		}
	}

	// 施設名返還テーブルを全件取得
	public List<ConvertInsKana> findAll() throws DataNotFoundException {
		return dataAccess.queryForList(SQL_MAP + ".selectAll", null);
	}
}
