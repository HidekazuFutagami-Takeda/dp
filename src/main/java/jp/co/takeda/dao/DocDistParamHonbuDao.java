package jp.co.takeda.dao;

import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.ProdCategory;

/**
 * 医師別計画配分基準(本部)にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DocDistParamHonbuDao {

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
	Map<String, Object> search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーで配分基準(本部)を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param distributionType 配分先区分
	 * @return 配分基準(本部)
	 * @throws DataNotFoundException
	 */
	Map<String, Object> search(String prodCode, InsType insType) throws DataNotFoundException;

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
	List<Map<String, Object>> searchInsDocParamList(String sortString, String prodCode, InsType insType, ProdCategory category) throws DataNotFoundException;
}
