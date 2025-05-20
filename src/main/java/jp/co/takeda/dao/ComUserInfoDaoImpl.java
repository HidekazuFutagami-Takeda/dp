/**
 *
 */
package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;

/**
 *  ユーザIDから従業員番号を引き当てる共有プロシージャへのDAO実装クラス
 * @author UJU3389
 *
 */
@Repository("comUserInfoDao")
public class ComUserInfoDaoImpl extends AbstractDao implements ComUserInfoDao {

	public static final String SQL_MAP = "COM_USER_INFO_SqlMap";

	/* (非 Javadoc)
	 * @see jp.co.takeda.dao.ComUserInfoDao#selectLoginJgiNoByUserId(java.lang.String)
	 */
	@Override
	public int selectLoginJgiNoByUserId(String userId) {
		try {
			return dataAccess.queryForObject(getSqlMapName() + ".selectLoginJgiNoByUserId", userId);
		} catch (DataNotFoundException e) {
			String errMsg = "ユーザＩＤから従業員番号を取得できなかった。";
			throw new SystemException(new Conveyance(DB_ERROR, errMsg), e);
		}
	}

	@Override
	protected String getSqlMapName() {

		return SQL_MAP;
	}

}
