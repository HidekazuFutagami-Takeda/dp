package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

import org.springframework.stereotype.Repository;

/**
 * 納入計画管理にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("sysManageDao")
public class SysManageDaoImpl extends AbstractDao implements SysManageDao {

	private static final String SQL_MAP = "DPS_S_SYS_MANAGE_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 納入計画システム管理を取得
	public SysManage search(SysClass sysClass, SysType sysType) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sysClass == null) {
			final String errMsg = "管理対象業務分類がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sysType == null) {
			final String errMsg = "管理対象業務区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("sysClass", sysClass);
		paramMap.put("sysType", sysType);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}
}
