package jp.co.takeda.service;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpUser;

/**
 * 配分実行バッチサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsDocDistributionBatchService {

	/**
	 * バッチ処理における品目単位の配分処理を実行する。
	 * 
	 * <pre>
	 * トランザクションを新規発行して、以下の処理を行う。
	 * ステータスのチェック
	 * 配分処理
	 * ステータス更新
	 * </pre>
	 * 
	 * @param sosMst 組織情報
	 * @param jgiMst 従業員情報
	 * @param execDpUser 実行者情報
	 * @param doMrOpen 配分処理と同時にMRに公開フラグ
	 * @param doPlanClear 計画クリアフラグ
	 * @param isMainHaibun メイン配分（営業所指定） or 再配分（担当者指定）
	 * @return 配分ミス情報リスト
	 * @throws LogicalException
	 */
	List<DistributionMissDto> executeDistProd(SosMst sosMst,JgiMst jgiMst, boolean doMrOpen,  boolean doPlanClear, boolean isMainHaibun, DpUser execDpUser) throws LogicalException ;

	/**
	 * 施設特約店別計画立案ステータスを元に戻す
	 * 
	 * @param jgiMst 従業員情報
	 * @param execDpUser 実行者情報
	 * @throws LogicalException
	 */
	void resumeStatus(JgiMst jgiMst, DpUser execDpUser) throws LogicalException;

	/**
	 * 配分ミス・アテンション・お知らせ登録登録
	 * 
	 * @param sosMst 組織情報
	 * @param startTime 開始時間
	 * @param resultType 処理結果区分
	 * @param execDpUser 実行者情報
	 * @param missList 配分ミスリスト
	 * @param opeType 処理タイプ（全配分 or 再配分）
	 * @throws LogicalException
	 */
	void entryMissListContactOperations(SosMst sosMst,  Date startTime, ContactResultType resultType, DpUser execDpUser,
		List<DistributionMissDto> missList, ContactOperationsType opeType) throws LogicalException;

	/**
	 * アテンション・お知らせ登録
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param startTime 開始時間
	 * @param resultType 処理結果区分
	 * @param execDpUser 実行者情報
	 * @param outputFileId 出力ファイルID
	 * @param opeType 処理タイプ（全配分 or 再配分）
	 * @throws LogicalException
	 */
	void entryContactOperations(String sosCd3,  Date startTime, ContactResultType resultType, DpUser execDpUser,Long outputFileId, ContactOperationsType opeType) throws LogicalException;

}
