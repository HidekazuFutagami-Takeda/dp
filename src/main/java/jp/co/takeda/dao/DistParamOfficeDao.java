package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;

/**
 * 配分基準(営業所)にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DistParamOfficeDao {

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
	DistParamOffice search(Long seqKey) throws DataNotFoundException;

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
	DistParamOffice search(String sosCd, String prodCode, InsType insType, DistributionType distributionType) throws DataNotFoundException;

	/**
	 * 施設特約店配分用の配分基準(営業所)リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd 組織コード(営業所)【NULL可】
	 * @param prodCode 品目固定コード【NULL可】
	 * @param insType 施設出力対象区分【NULL可】
	 * @param category カテゴリ【NULL可】
	 * @param sales 売上所属【Sales.IYAKU or Sales.VACCHIN】
	 * @return 配分基準(営業所)のリスト
	 * @throws DataNotFoundException
	 */
	List<DistParamOffice> searchInsWsParamList(String sortString, String sosCd, String prodCode, InsType insType, String category, Sales sales) throws DataNotFoundException;

	/**
	 * 特約店配分用の配分基準(営業所)リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd 組織コード(支店)【必須】
	 * @param prodCode 品目固定コード【NULL可】
	 * @param insType 施設出力対象区分【NULL可】
	 * @param category カテゴリ【NULL可】
	 * @return 配分基準(営業所)のリスト
	 * @throws DataNotFoundException
	 */
	List<DistParamOffice> searchWsParamList(String sortString, String sosCd, List<String> prodCodeList, InsType insType, String category) throws DataNotFoundException;

	/**
	 * 配分基準(営業所)を登録する。
	 *
	 * @param record 配分基準(営業所)
	 * @throws DuplicateException
	 */
	void insert(DistParamOffice record) throws DuplicateException;

	/**
	 * 配分基準(営業所)を更新する。
	 *
	 * @param record 配分基準(営業所)
	 * @return 更新件数
	 */
	int update(DistParamOffice record);

	/**
	 * 配分基準(営業所)を削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
