package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.AttLinksIf;

import org.springframework.stereotype.Repository;

/**
 * Attention基本I/FにアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("attLinksIfDao")
public class AttLinksIfDaoImpl extends AbstractDao implements AttLinksIfDao {

	private static final String SQL_MAP = "DPS_S_SY_ATT_LINKS_IF_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// Attention外部リンク情報I/Fを登録
	public void insert(AttLinksIf record) throws DuplicateException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "登録情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		dataAccess.execute(getSqlMapName() + ".insert", record);
	}
}
