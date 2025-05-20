package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.dto.InsWsPlanForVacProdSummaryDto;
import jp.co.takeda.model.InsWsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

/**
 * ワクチン用施設特約店別計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface InsWsPlanForVacDao {

	/**
	 * ソート順<br>
	 * 施設ソート(ワクチン用) 昇順<br>
	 * 特約店ソート 昇順<br>
	 * 
	 */
	static final String SORT_STRING = "ORDER BY ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ワクチン用施設特約店別計画を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return ワクチン用施設特約店別計画
	 * @throws DataNotFoundException
	 */
	InsWsPlanForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * ワクチン用施設特約店別計画のリストを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param activityType 活動区分【NULL可】
	 * @param addrCodePref JIS府県コード【NULL可】
	 * @param addrCodeCity JIS市区町村コード【NULL可】
	 * @return ワクチン用施設特約店別計画
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForVac> searchList(String sortString, Integer jgiNo, String prodCode, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity)
		throws DataNotFoundException;

	/**
	 * ワクチン用施設特約店別計画を登録する。
	 * 
	 * @param record ワクチン用施設特約店別計画
	 * @throws DuplicateException
	 */
	void insert(InsWsPlanForVac record) throws DuplicateException;

	/**
	 * ワクチン用施設特約店別計画を更新する。
	 * 
	 * <pre>
	 * 更新対象は
	 * 計画値(B)
	 * </pre>
	 * 
	 * @param record ワクチン用施設特約店別計画
	 * @return 更新件数
	 */
	int update(InsWsPlanForVac record);

	/**
	 * ワクチン用施設特約店別計画を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);

	/**
	 * ワクチン用施設特約店別計画を従業員・品目単位で一括削除する。
	 * 
	 * <pre>
	 * 従業員・品目単位で一括削除する。
	 * 楽観的排他チェックは行わない。
	 * </pre>
	 * 
	 * @param jgiNo 従業員番号(NULL可)
	 * @param prodCode 品目固定コード(NULL可)
	 * @return 削除件数
	 */
	int deleteByJgi(Integer jgiNo, String prodCode);

	/**
	 * 品目固定コード、組織(または従業員番号)を指定してワクチン用施設特約店別計画のサマリを取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(エリア特約店G)【NULL可】
	 * @param jgiNo 従業員番号【NULL可】
	 * @param activityType 活動区分【NULL可】
	 * @param addrCodePref JIS府県コード【NULL可】
	 * @param addrCodeCity JIS市区町村コード【NULL可】
	 * @return 用施設特約店別計画のサマリ
	 * @throws DataNotFoundException
	 */
	InsWsPlanForVacProdSummaryDto searchProdSummary(String prodCode, String sosCd3, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity)
		throws DataNotFoundException;
}
