package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.AttBaseIf;

import org.springframework.stereotype.Repository;

/**
 * Attention基本I/FにアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("attBaseIfDao")
public class AttBaseIfDaoImpl extends AbstractDao implements AttBaseIfDao {

	private static final String SQL_MAP = "DPS_S_SY_ATT_BASE_IF_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// Attention基本I/Fを登録
	public void insert(AttBaseIf record) throws DuplicateException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "登録情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		dataAccess.execute(getSqlMapName() + ".insert", record);
	}

	// Attention基本I/Fのシーケンスキーを取得
	public Integer getSeqKey() {
		try {
			return dataAccess.queryForObject(getSqlMapName() + ".getSeqKey", null);
		} catch (DataNotFoundException e) {
			final String errMsg = "Attention基本I/Fのシーケンスキー取得失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}
}
