package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.view.MrEstimationRatio;

/**
 * 担当者別試算構成比取得DAOインターフェイス
 *
 * @author nozaki
 */
public interface MrEstimationRatioDao {

	/**
	 * 組織コード(営業所)、品目固定コードから、担当者毎の試算構成比一覧を取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param refProdCode 試算品目コード
	 * @param refFrom 参照期間(FROM)
	 * @param refTo 参照期間(TO)
	 * @throws DataNotFoundException
	 */
	List<MrEstimationRatio> searchList(String sosCd3, String prodCode, String refProdCode, RefPeriod refFrom, RefPeriod refTo, String category) throws DataNotFoundException;

	/**
	 * 組織コード(チーム)、品目固定コードから、担当者毎の試算構成比一覧を取得する。
	 *
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @param refProdCode 試算品目コード
	 * @param refFrom 参照期間(FROM)
	 * @param refTo 参照期間(TO)
	 * @throws DataNotFoundException
	 */
	List<MrEstimationRatio> searchListByTeam(String sosCd4, String prodCode, String refProdCode, RefPeriod refFrom, RefPeriod refTo, String category) throws DataNotFoundException;
}
