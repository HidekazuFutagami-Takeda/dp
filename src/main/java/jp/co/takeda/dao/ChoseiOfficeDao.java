package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ChoseiOffice;
import jp.co.takeda.model.div.InsType;

/**
 * 調整額（営業所調整）にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ChoseiOfficeDao {

	/**
	 * ユニークキーを元に管理の調整額（営業所調整）を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(営業所)
	 * @return 調整額（営業所調整）
	 * @throws DataNotFoundException
	 */
	ChoseiOffice searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException;

}
