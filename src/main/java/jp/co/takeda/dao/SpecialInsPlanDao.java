package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.dto.SpecialInsPlanScDto;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.PlanType;

/**
 * 特定施設個別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface SpecialInsPlanDao {

	/**
	 * ソート順<br>
	 * 登録済みが先<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 * 施設ソート　昇順
	 */
	//static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE," + " SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE," + " SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO, INS_HO_INS_TYPE, INS.RELN_INS_NO, INS.RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート　昇順<br>
	 * 施設ソート　昇順<br>
	 * 計画立案区分　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE,PROD_CODE," + " HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD, PLAN_TYPE";

	/**
	 * ソート順<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING3 = "ORDER BY HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING4 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO,"
		+ " OINS_HO_INS_TYPE, OINS.RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, INS.RELN_INS_NO, INS.RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ユニークキーを元に特定施設個別計画を取得する。
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd TMS特約店コード
	 * @param planType 計画立案区分
	 * @return 特定施設個別計画
	 * @throws DataNotFoundException
	 */
	SpecialInsPlan searchUk(String insNo, String prodCode, String tmsTytenCd, PlanType planType) throws DataNotFoundException;

	/**
	 * 検索条件DTOを元に特定施設個別計画(施設一覧)を取得する。(子施設を除く)<br>
	 * 施設(Ａ調含む)単位のサマリ情報を取得するため、<br>
	 * シーケンスキー、品目固定コード、TMS特約店コード、計画立案区分はNULL
	 *
	 * @param sortString ソート順
	 * @param scDto 登録済み特定施設個別計画の検索条件DTO
	 * @param planType 計画立案区分
	 * @return 特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlan> searchList(String sortString, SpecialInsPlanScDto scDto, PlanType planType) throws DataNotFoundException;

	/**
	 * 従業員番号、施設コード、カテゴリ、品目固定コード、計画立案区分を元に特定施設個別計画を取得する。(子施設を含む）
	 *
	 * @param sortString ソート順
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param category カテゴリ(NULL可)
	 * @param prodCode 品目固定コード(NULL可)
	 * @param planType 計画立案区分(NULL可)
	 * @return 特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlan> searchByInsNo(String sortString, Integer jgiNo, String insNo, String category, String prodCode, PlanType planType) throws DataNotFoundException;

	/**
	 * (ワ)従業員番号、施設コード、品目固定コード、計画立案区分を元に特定施設個別計画を取得する。(子施設を含む）
	 *
	 * @param sortString ソート順
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード(NULL可)
	 * @param planType 計画立案区分(NULL可)
	 * @return 特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlan> searchByInsNoForVac(String sortString, Integer jgiNo, String insNo, String prodCode, PlanType planType) throws DataNotFoundException;

	/**
	 * 施設コード、品目固定コード、計画立案区分を元に特定施設個別計画を取得する。(子施設を含まない）
	 *
	 * @param sortString ソート順
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード(NULL可)
	 * @param planType 計画立案区分(NULL可)
	 * @return 特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlan> searchByInsNo(String sortString, String insNo, String prodCode, PlanType planType) throws DataNotFoundException;

	/**
	 * 特定施設個別計画を登録する。
	 *
	 * @param record 特定施設個別計画
	 * @throws DuplicateException
	 */
	void insert(SpecialInsPlan record) throws DuplicateException;

	/**
	 * 施設コード、計画立案区分、品目カテゴリを元に特定施設個別計画を削除する。(子施設を含む）
	 *
	 * @param insNo 施設コード
	 * @param planType 計画立案区分(NULL可:全削除)
	 * @param category カテゴリ(NULL可:全削除)
	 * @param upDate 最終更新日時
	 * @return 処理件数
	 */
	int delete(Integer jgiNo, String insNo, PlanType planType, String category, Date upDate);

	/**
	 * 施設コード、品目カテゴリを元にワクチンの特定施設個別計画を削除する。(子施設を含む）
	 *
	 * @param insNo 施設コード
	 * @param planType 計画立案区分(NULL可:全削除)
	 * @param category カテゴリ(NULL可:全削除)
	 * @param upDate 最終更新日時
	 * @return 処理件数
	 */
	int deleteVaccine(Integer jgiNo, String insNo, PlanType planType, String category, Date upDate);

	/**
	 * 施設コードを元に特定施設個別計画の最終更新日時<br>
	 * を保持するレコードを取得する。(子施設を含む)
	 *
	 * @param insNo 施設コード
	 * @param planType 計画立案区分(NULL可:全選択)
	 * @param category カテゴリ(NULL可:全選択)
	 * @return 特定施設個別計画
	 * @throws DataNotFoundException
	 */
	SpecialInsPlan searchUpDate(Integer jgiNo, String insNo, PlanType planType, String category) throws DataNotFoundException;

	/**
	 * 従業員番号、品目固定コード、計画立案区分を元に特定施設個別計画を取得する。
	 *
	 * @param sortString ソート順
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param planType 計画立案区分
	 * @return 特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlan> searchByJgiNo(String sortString, Integer jgiNo, String prodCode, PlanType planType) throws DataNotFoundException;

	/**
	 * 従業員番号、品目固定コード、計画立案区分を元に特定施設個別計画の最終更新日時を取得する。
	 *
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param planType 計画立案区分
	 * @return 最終更新日時(レコード無の場合はnullを返す)
	 */
	java.util.Date getUpdateByJgiNo(Integer jgiNo, String prodCode, PlanType planType);

	/**
	 * 従業員番号、品目固定コード、計画立案区分を元に特定施設個別計画を削除する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param planType 計画立案区分
	 */
	int deleteByJgiNo(Integer jgiNo, String prodCode, PlanType planType);

	/**
	 * 組織コード(営業所)、品目固定コード、計画立案区分、施設出力対象区分を元に特定施設個別計画を取得する。
	 *
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param planType 計画立案区分
	 * @param insType 施設出力対象区分
	 * @return 特定施設個別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlan> searchBySosCd(String sortString, String sosCd3, String prodCode, PlanType planType, InsType insType, boolean isVaccine, String category) throws DataNotFoundException;

	/**
	 * 組織コード(営業所)、品目固定コード、計画立案区分を元に特定施設個別計画を削除する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param planType 計画立案区分(NULL可)
	 */
	int deleteBySosCd(String sosCd3, String prodCode, PlanType planType);
}
