package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.MrPlanSosSummaryDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpUser;

/**
 * 担当者別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface MrPlanDao {

	/**
	 * ソート順<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO";

	/**
	 * ソート順<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 * 品目ソート　昇順<br>
	 */
	static final String SORT_STRING2 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * シーケンスキーを元に担当者別計画を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 担当者別計画
	 * @throws DataNotFoundException
	 */
	MrPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に担当者別計画を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画
	 * @throws DataNotFoundException
	 */
	MrPlan searchUk(Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コード(営業所)を元に担当者別計画のリストを取得する。
	 *
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード(NULL可)
	 * @param category 品目カテゴリ(NULL可)
	 * @return 担当者別計画のリスト
	 * @throws DataNotFoundException
	 */
//	List<MrPlan> searchBySosCd(String sortString, String sosCd3, String prodCode, ProdCategory category) throws DataNotFoundException;
	List<MrPlan> searchBySosCd(String sortString, String sosCd3, String prodCode, String category) throws DataNotFoundException;

	/**
	 * 組織コード(チーム)を元に担当者別計画のリストを取得する。
	 *
	 * @param sortString ソート順
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード(NULL可)
	 * @param category 品目カテゴリ(NULL可)
	 * @return 担当者別計画のリスト
	 * @throws DataNotFoundException
	 */
//	List<MrPlan> searchByTeamCd(String sortString, String sosCd4, String prodCode, ProdCategory category) throws DataNotFoundException;
	List<MrPlan> searchByTeamCd(String sortString, String sosCd4, String prodCode, String category) throws DataNotFoundException;

// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 * 組織コード(営業所)を元に帳票用担当者別計画のリストを取得する。
	 *
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード(NULL可)
	 * @param category 品目カテゴリ(NULL可)
	 * @return 担当者別計画のリスト
	 * @throws DataNotFoundException
	 */
//	List<MrPlan> searchBySosCdReport(String sortString, String sosCd3, String prodCode, ProdCategory category) throws DataNotFoundException;
	List<MrPlan> searchBySosCdReport(String sortString, String sosCd3, String prodCode, String category) throws DataNotFoundException;

	/**
	 * 組織コード(チーム)を元に帳票用当者別計画のリストを取得する。
	 *
	 * @param sortString ソート順
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード(NULL可)
	 * @param category 品目カテゴリ(NULL可)
	 * @return 担当者別計画のリスト
	 * @throws DataNotFoundException
	 */
//	List<MrPlan> searchByTeamCdReport(String sortString, String sosCd4, String prodCode, ProdCategory category) throws DataNotFoundException;
	List<MrPlan> searchByTeamCdReport(String sortString, String sosCd4, String prodCode, String category) throws DataNotFoundException;

	// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

	/**
	 * 組織コード(営業所)を元に担当者別計画の最終更新情報を取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画の最終更新情報
	 * @throws DataNotFoundException
	 */
	MrPlan getLastUpDateBySos(String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コード(チーム)を元に担当者別計画の最終更新情報を取得する。
	 *
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画の最終更新情報
	 * @throws DataNotFoundException
	 */
	MrPlan getLastUpDateByTeam(String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 対象品目・営業所の担当者別計画サマリーを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @return 担当者別計画サマリー
	 * @throws DataNotFoundException
	 */
	MrPlanSosSummaryDto searchSosSummary(String prodCode, String sosCd3) throws DataNotFoundException;

// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 * 帳票用対象品目・営業所の担当者別計画サマリーを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @return 担当者別計画サマリー
	 * @throws DataNotFoundException
	 */
	MrPlanSosSummaryDto searchSosSummaryReport(String prodCode, String sosCd3) throws DataNotFoundException;
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

	/**
	 * 対象品目・チームの担当者別計画サマリーを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd4 組織コード(チーム)
	 * @return 担当者別計画サマリー
	 * @throws DataNotFoundException
	 */
	MrPlanSosSummaryDto searchTeamSummary(String prodCode, String sosCd4) throws DataNotFoundException;

	/**
	 * シーケンスキーをもとに、担当者別計画(UH/P)を更新する。
	 *
	 * <pre>
	 * 更新対象は下記カラム
	 * 施設出力対象区分がUHの場合、
	 * ・営業所案UH(Y)
	 * ・計画値UH(Y)
	 * 施設出力対象区分がPの場合、
	 * ・営業所案P(Y)
	 * ・計画値P(Y)
	 * 施設出力対象区分がNULLの場合、
	 * ・営業所案UH(Y)
	 * ・計画値UH(Y)
	 * ・営業所案P(Y)
	 * ・計画値P(Y)
	 * </pre>
	 *
	 * @param record 担当者別計画
	 * @param insType 施設出力対象区分
	 * @return 処理件数
	 */
	int update(MrPlan record, InsType insType);

	/**
	 * 試算結果で担当者別計画を更新する。<br>
	 * (楽観チェックなし)
	 *
	 * @param record 担当者別計画
	 * @param dpUser 実行者情報
	 */
	void executeByEst(MrPlan record, DpUser dpUser);

	/**
	 * 配分結果の積上げで担当者別計画を更新する。<br>
	 * （楽観チェックなし）
	 *
	 * @param record 担当者別計画
	 * @param insType 施設出力対象区分
	 * @param dpUser 実行者情報
	 */
	void executeByDist(MrPlan record, InsType insType, DpUser dpUser);

	/**
	 * 配分時の担当者別計画を初期化する。<br>
	 * 担当者別計画の計画値をNULLに初期化する。(楽観チェックなし)
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param prodCode 実行者情報
	 * @return 処理件数
	 */
	int updateByDist(String sosCd3, String prodCode, InsType insType, DpUser dpUser);

	/**
	 * 従業員一覧、品目固定コード一覧を元に担当者別計画を取得する。
	 *
	 * @param jgiMst 従業員一覧
	 * @param prodList 品目固定コード一覧
	 * @throws DataNotFoundException
	 */
	List<MrPlan> searchJgiNoProdList(List<JgiMst> jgiMst, List<String> prodList ) throws DataNotFoundException;


}
