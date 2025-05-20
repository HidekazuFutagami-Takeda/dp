package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.JknGrp;

/**
 * 計画管理条件セットグループにアクセスするDAOインターフェイス
 *
 * @author yyoshino
 */
public interface DpsJknGrpDao {

	/**
	 *
	 * @param jgiNo
	 * @return
	 * @throws DataNotFoundException
	 */
	List<JknGrp> searchListByJgiNo(Integer jgiNo) throws DataNotFoundException;

}
