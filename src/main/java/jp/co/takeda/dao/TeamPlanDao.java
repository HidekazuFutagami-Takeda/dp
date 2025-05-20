package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.OfficeTeamPlanChoseiDto;
import jp.co.takeda.dto.TeamPlanSosSummaryDto;
import jp.co.takeda.logic.div.CopyWay;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpUser;

/**
 * チーム別計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface TeamPlanDao {

	/**
	 * ソート順<br>
	 * 組織ソート　昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE";

	/**
	 * チーム別計画を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return チーム別計画
	 * @throws DataNotFoundException
	 */
	TeamPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * チーム別計画をユニークキーで取得する。
	 * 
	 * @param sosCd4 組織コード(チーム) 【必須】
	 * @param prodCode 品目固定コード 【必須】
	 * @return チーム別計画
	 * @throws DataNotFoundException
	 */
	TeamPlan searchUk(String sosCd4, String prodCode) throws DataNotFoundException;
	
// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 * 帳票用チーム別計画をユニークキーで取得する。
	 * 
	 * @param sosCd4 組織コード(チーム) 【必須】
	 * @param prodCode 品目固定コード 【必須】
	 * @return チーム別計画
	 * @throws DataNotFoundException
	 */
	TeamPlan searchUkReport(String sosCd4, String prodCode) throws DataNotFoundException;
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

	/**
	 * 組織コード(営業所)をもとに、チーム別計画を取得する。
	 * 
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所） 【必須】
	 * @param prodCode 品目固定コード 【必須】
	 * @return チーム別計画
	 * @throws DataNotFoundException
	 */
	List<TeamPlan> searchList(String sortString, String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コード(営業所)をもとに、チーム別計画の最終更新情報を取得する。
	 * 
	 * @param sosCd3 組織コード(営業所） 【必須】
	 * @param prodCode 品目固定コード 【必須】
	 * @return チーム別計画の最終更新情報
	 */
	TeamPlan getLastUpDate(String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * 対象品目・営業所のチーム別計画サマリーを取得する。<br>
	 * 
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @return 担当者別計画サマリー
	 * @throws DataNotFoundException
	 */
	TeamPlanSosSummaryDto searchSosSummary(String prodCode, String sosCd3) throws DataNotFoundException;

	/**
	 * シーケンスキーをもとに、チーム別計画(UH/P)を更新する。
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
	 * @param record チーム別計画
	 * @param insType 施設出力対象区分
	 * @return 更新件数
	 */
	int update(TeamPlan record, InsType insType);

	/**
	 * 試算結果でチーム別計画を更新する。<br>
	 * 
	 * @param record チーム別計画
	 * @param dpUser 実行者情報
	 */
	void executeByEst(TeamPlan record, DpUser dpUser);

	/**
	 * チーム別計画と担当者別計画の間における調整金額の有無を取得する。<br>
	 * 
	 * @param sosCd3 組織コード（営業所）
	 * @param prodCode 品目固定コード
	 * @return 調整計画の有無
	 * @throws DataNotFoundException
	 */
	OfficeTeamPlanChoseiDto getChosei(String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * 計画値の一括コピーし、チーム別計画を更新する。<br>
	 * （楽観チェックなし）
	 * 
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @param copyWay コピー方法
	 * @param dpUser 実行者情報
	 */
	int executePlanCopy(String sosCd4, String prodCode, CopyWay copyWay, DpUser dpUser);

	/**
	 * 担当者別計画を積上げて、チーム別計画更新する。<br>
	 * （楽観チェックなし）
	 * 
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @param dpUser 実行者情報
	 */
	int updateByMrPlanSum(String sosCd4, String prodCode, DpUser dpUser);

}
