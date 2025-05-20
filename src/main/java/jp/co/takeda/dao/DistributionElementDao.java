package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.view.DistributionElement;

/**
 * 施設特約店別計画配分用の要素を取得するDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DistributionElementDao {

	/**
	 * 組織コード(営業所)、品目固定コードから、施設特約店別計画配分用の要素を取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiNo 従業員番号【NULL可】
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param refProdCode 配分品目コード
	 * @param refFrom 参照期間(FROM)
	 * @param refTo 参照期間(TO)
	 * @param isVaccine ワクチンかどうか
	 * @return 施設特約店別計画配分用の要素
	 * @throws DataNotFoundException
	 */
	List<DistributionElement> searchList(String sosCd3, Integer jgiNo, String prodCode, InsType insType, String refProdCode, RefPeriod refFrom, RefPeriod refTo, boolean isVaccine, String category)
		throws DataNotFoundException;

	/**
	 * 従業員番号、品目固定コードから、施設特約店別計画配分用の要素(ワクチン)を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param refProdCode 配分品目コード
	 * @param refFrom 参照期間(FROM)
	 * @param refTo 参照期間(TO)
	 * @return 施設特約店別計画配分用の要素(ワクチン)
	 * @throws DataNotFoundException
	 */
	List<DistributionElement> searchListForVac(Integer jgiNo, String prodCode, String refProdCode, RefPeriod refFrom, RefPeriod refTo) throws DataNotFoundException;
}
