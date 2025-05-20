package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 納入計画管理にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface SysManageDao {

	/**
	 * 納入計画システム管理を取得する。
	 * 
	 * @param sysClass 管理対象業務分類
	 * @param sysType 管理対象業務区分
	 * @return 納入計画管理
	 * @throws DataNotFoundException
	 */
	SysManage search(SysClass sysClass, SysType sysType) throws DataNotFoundException;
}
