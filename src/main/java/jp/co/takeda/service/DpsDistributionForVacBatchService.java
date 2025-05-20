package jp.co.takeda.service;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.security.DpUser;

/**
 * (ワクチン)配分実行バッチサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsDistributionForVacBatchService {

	/**
	 * バッチ処理における品目単位の(ワクチン)配分処理を実行する。
	 * 
	 * <pre>
	 * トランザクションを新規発行して、以下の処理を行う。
	 * ステータスのチェック
	 * 配分処理
	 * ステータス更新
	 * </pre>
	 * 
	 * @param execDpUser 実行者情報
	 * @param execOrgDto (ワクチン)配分実行用DTO
	 * @param isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 * @return 配分ミス情報リスト
	 * @throws LogicalException
	 */
	List<DistributionMissDto> executeDistProd(DpUser execDpUser, DistributionForVacExecOrgDto execOrgDto, boolean isMrOpenCheck) throws LogicalException;

	/**
	 * (ワクチン)施設特約店別計画立案ステータスを元に戻す
	 * 
	 * 
	 * @param execDpUser 実行者情報
	 * @param execOrgDto (ワクチン)配分実行用DTO
	 * @throws LogicalException
	 */
	void resumeStatus(DpUser execDpUser, DistributionForVacExecOrgDto execOrgDto) throws LogicalException;

	/**
	 * 配分ミス・アテンション・お知らせ登録登録
	 * 
	 * 
	 * @param errProdList エラー品目リスト
	 * @param startTime 開始時間
	 * @param resultType 処理結果区分
	 * @param execDpUser 実行者情報
	 * @param missList 配分ミスリスト
	 * @throws LogicalException
	 */
	void entryMissListContactOperations(List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, List<DistributionMissDto> missList)
		throws LogicalException;

	/**
	 * アテンション・お知らせ登録
	 * 
	 * 
	 * @param errProdList エラー品目リスト
	 * @param startTime 開始時間
	 * @param resultType 処理結果区分
	 * @param execDpUser 実行者情報
	 * @param outputFileId 出力ファイルID
	 * @throws LogicalException
	 */
	void entryContactOperations(List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, Long outputFileId) throws LogicalException;

}
