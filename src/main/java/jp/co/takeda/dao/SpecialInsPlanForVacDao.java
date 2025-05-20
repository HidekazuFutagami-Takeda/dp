package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.dto.SpecialInsPlanForVacScDto;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.PlanType;

/**
 * ワクチン用特定施設個別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface SpecialInsPlanForVacDao {

	/**
	 * ソート順<br>
	 * 登録済みが先<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 * 都道府県・市区町村　昇順<br>
	 * 施設内部コード　昇順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE,"
		+ " SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO, ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート　昇順<br>
	 * 都道府県・市区町村　昇順<br>
	 * 施設内部コード　昇順 計画立案区分　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE,PROD_CODE,"
		+ " ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD, PLAN_TYPE";

	/**
	 * ユニークキーを元にワクチン用特定施設個別計画を取得する。
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd TMS特約店コード
	 * @param planType 計画立案区分
	 * @return 特定施設個別計画
	 * @throws DataNotFoundException
	 */
	SpecialInsPlanForVac searchUk(String insNo, String prodCode, String tmsTytenCd, PlanType planType) throws DataNotFoundException;

	/**
	 * 検索条件DTOを元にワクチン用特定施設個別計画を取得する。(子施設を除く)
	 *
	 * @param sortString ソート順
	 * @param scDto 登録済みワクチン用特定施設個別計画の検索条件DTO
	 * @param planType 計画立案区分
	 * @return ワクチン用特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlanForVac> searchList(String sortString, SpecialInsPlanForVacScDto scDto, PlanType planType) throws DataNotFoundException;

	/**
	 * 従業員番号、施設コード、品目固定コード、計画立案区分を元にワクチン用特定施設個別計画を取得する。(子施設を含む)
	 *
	 * @param sortString ソート順
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード(NULL可)
	 * @param planType 計画立案区分(NULL可)
	 * @return ワクチン用特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlanForVac> searchByInsNo(String sortString, Integer jgiNo, String insNo, String prodCode, PlanType planType) throws DataNotFoundException;

	/**
	 * 従業員番号、品目固定コード、計画立案区分を元にワクチン用特定施設個別計画を取得する。
	 *
	 * @param sortString ソート順
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param planType 計画立案区分(NULL可)
	 * @return ワクチン用特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlanForVac> searchByJgiNo(String sortString, Integer jgiNo, String prodCode, PlanType planType) throws DataNotFoundException;

	/**
	 * ワクチン用特定施設個別計画を登録する。
	 *
	 * @param record ワクチン用特定施設個別計画
	 * @throws DuplicateException
	 */
	void insert(SpecialInsPlanForVac record) throws DuplicateException;

	/**
	 * 施設コード、計画立案区分を元にワクチン用特定施設個別計画を削除する。(子施設を含む)
	 *
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param planType 計画立案区分(NULL可:全削除)
	 * @param upDate 最終更新日時
	 * @return 処理件数
	 */
	int delete(Integer jgiNo, String insNo, PlanType planType, Date upDate);

	/**
	 * 施設コードを元にワクチン用特定施設個別計画の最終更新日時<br>
	 * を保持するレコードを取得する。(子施設を含む)
	 *
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param planType 計画立案区分(NULL可:全削除)
	 * @return ワクチン用特定施設個別計画
	 * @throws DataNotFoundException
	 */
	SpecialInsPlanForVac searchUpDate(Integer jgiNo, String insNo, PlanType planType) throws DataNotFoundException;
}
