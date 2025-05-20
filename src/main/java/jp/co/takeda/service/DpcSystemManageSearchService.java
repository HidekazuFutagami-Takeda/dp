package jp.co.takeda.service;

import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 納入計画管理検索サービスインターフェイス
 * 
 * @author nozaki
 */
public interface DpcSystemManageSearchService {

	/**
	 * 納入計画管理を取得する。
	 * 
	 * @return 納入計画管理
	 */
	SysManage searchSysManage(SysClass sysClass, SysType sysType);
}
