package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ChoseiIyaku;
import jp.co.takeda.model.div.InsType;

/**
 * 調整額（全社調整）にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ChoseiIyakuDao {

	/**
	 * ユニークキーを元に管理の調整額を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @return 調整額（全社）
	 * @throws DataNotFoundException
	 */
	ChoseiIyaku searchUk(InsType insType, String prodCode) throws DataNotFoundException;

}
