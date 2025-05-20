package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.AttAddrGrpIf;

import org.springframework.stereotype.Repository;

/**
 * Attention宛先グループ指定I/FにアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("attAddrGrpIfDao")
public class AttAddrGrpIfDaoImpl extends AbstractDao implements AttAddrGrpIfDao {

	private static final String SQL_MAP = "DPS_S_SY_ATT_ADDR_GRP_IF_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// Attention宛先グループ指定I/Fを登録
	public void insert(AttAddrGrpIf record) throws DuplicateException {

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
