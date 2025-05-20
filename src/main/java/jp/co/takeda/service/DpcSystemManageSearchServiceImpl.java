package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *納入計画管理検索サービス実装クラス
 * 
 * @author nozaki
 */
@Transactional
@Service("dpcSystemManageSearchService")
public class DpcSystemManageSearchServiceImpl implements DpcSystemManageSearchService {

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	public SysManage searchSysManage(SysClass sysClass, SysType sysType) {

		// -----------------------------
		// 引数チェック
		// -----------------------------
		if (sysClass == null) {
			final String errMsg = "管理対象業務分類がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sysType == null) {
			final String errMsg = "管理対象業務区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 検索処理
		// -----------------------------
		try {
			return sysManageDao.search(sysClass, sysType);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画管理情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
	}
}
