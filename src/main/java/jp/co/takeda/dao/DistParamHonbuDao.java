package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.DistParamHonbu;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.ProdCategory;

/**
 * 配分基準(本部)にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DistParamHonbuDao {

	/**
	 * ソート順<br>
	 * 施設出力対象区分　昇順<br>
	 * 品目ソート　昇順
	 *
	 */
	static final String SORT_STRING = "ORDER BY INS_TYPE, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 配分基準(本部)を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 配分基準(本部)
	 * @throws DataNotFoundException
	 */
	DistParamHonbu search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーで配分基準(本部)を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param distributionType 配分先区分
	 * @return 配分基準(本部)
	 * @throws DataNotFoundException
	 */
	DistParamHonbu search(String prodCode, InsType insType, DistributionType distributionType) throws DataNotFoundException;

	/**
	 * 施設特約店配分用の配分基準(本部)リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード【NULL可】
	 * @param insType 施設出力対象区分【NULL可】
	 * @param category カテゴリ【NULL可】
	 * @return 配分基準(本部)のリスト
	 * @throws DataNotFoundException
	 */
	List<DistParamHonbu> searchInsWsParamList(String sortString, String prodCode, InsType insType, String category) throws DataNotFoundException;

	/**
	 * 特約店配分用の配分基準(本部)リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード【NULL可】
	 * @param insType 施設出力対象区分【NULL可】
	 * @param category カテゴリ【NULL可】
	 * @return 配分基準(本部)のリスト
	 * @throws DataNotFoundException
	 */
	List<DistParamHonbu> searchWsParamList(String sortString, String prodCode, InsType insType, ProdCategory category) throws DataNotFoundException;
}
