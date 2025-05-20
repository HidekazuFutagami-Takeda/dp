package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ChoseiMr;
import jp.co.takeda.model.div.InsType;

/**
 * 調整額（担当者）にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ChoseiMrDao {

	/**
	 * ユニークキーを元に管理の調整額（担当者）を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @return 調整額（担当者）
	 * @throws DataNotFoundException
	 */
	ChoseiMr searchUk(InsType insType, String prodCode, Integer jgiNo) throws DataNotFoundException;

}
