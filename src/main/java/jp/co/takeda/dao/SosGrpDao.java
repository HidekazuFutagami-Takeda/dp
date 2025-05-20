package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.SosGrp;

/**
 * 組織グループアクセスするインターフェース
 *
 * @author rna8405
 *
 */
public interface SosGrpDao {

	/**
	 * 組織グループを取得する。
	 * @param sosCd
	 * @return 組織グループ
	 */
	List<SosGrp> search(String sosCd) throws DataNotFoundException;

}
