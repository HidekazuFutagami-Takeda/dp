package jp.co.takeda.dao;

import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.ProdCategory;

/**
 * 医師別計画配分基準(営業所)にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DocDistParamOfficeDao {

	/**
	 * ソート順<br>
	 * 施設出力対象区分　昇順<br>
	 * 品目ソート　昇順
	 *
	 */
	static final String SORT_STRING = "ORDER BY INS_TYPE, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * ソート順2<br>
	 */
	static final String SORT_STRING2 = "ORDER BY INS_TYPE, PP.GROUP_CODE, PP.STAT_PROD_CODE, PROD_CODE";

	/**
	 * 配分基準(営業所)を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 配分基準(営業所)
	 */
	Map<String, Object> search(Long seqKey) throws DataNotFoundException;

	/**
	 * 配分基準(営業所)を取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param distributionType 配分先区分
	 * @return 配分基準(営業所)
	 * @throws DataNotFoundException
	 */
	Map<String, Object> search(String sosCd, String prodCode, InsType insType) throws DataNotFoundException;

	/**
	 * 施設特約店配分用の配分基準(営業所)リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd 組織コード(営業所)【必須】
	 * @param prodCode 品目固定コード【NULL可】
	 * @param insType 施設出力対象区分【NULL可】
	 * @param category カテゴリ【NULL可】
	 * @return 配分基準(営業所)のリスト
	 * @throws DataNotFoundException
	 */
	List<Map<String, Object>> searchInsDocParamList(String sortString, String sosCd, String prodCode, InsType insType, ProdCategory category) throws DataNotFoundException;
}
