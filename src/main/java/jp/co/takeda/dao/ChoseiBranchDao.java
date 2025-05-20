package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ChoseiBranch;
import jp.co.takeda.model.div.InsType;

/**
 * 調整額（支店調整）にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ChoseiBranchDao {

	/**
	 * ユニークキーを元に管理の調整額を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(支店)
	 * @return 調整額（支店）
	 * @throws DataNotFoundException
	 */
	ChoseiBranch searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException;

}
