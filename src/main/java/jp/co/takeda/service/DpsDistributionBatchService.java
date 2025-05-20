package jp.co.takeda.service;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpUser;

/**
 * 配分実行バッチサービスインタフェース
 *
 * @author khashimoto
 */
public interface DpsDistributionBatchService {

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
	 * @param category 品目カテゴリ
	 * @param execDpUser 実行者情報
	 * @param execOrgDto 配分実行用DTO
	 * @param isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 * @return 配分ミス情報リスト
	 * @throws LogicalException
	 */
	List<DistributionMissDto> executeDistProd(SosMst sosMst, String category, DpUser execDpUser, DistributionExecOrgDto execOrgDto, boolean isMrOpenCheck)
		throws LogicalException;

	/**
	 * 施設特約店別計画立案ステータスを元に戻す
	 *
	 *
	 * @param execDpUser 実行者情報
	 * @param execOrgDto 配分実行用DTO
	 * @throws LogicalException
	 */
	void resumeStatus(DpUser execDpUser, DistributionExecOrgDto execOrgDto) throws LogicalException;

	/**
	 * 配分ミス・アテンション・お知らせ登録登録
	 *
	 *
	 * @param sosMst 組織情報
	 * @param errProdList エラー品目リスト
	 * @param startTime 開始時間
	 * @param resultType 処理結果区分
	 * @param execDpUser 実行者情報
	 * @param category 品目カテゴリ
	 * @param missList 配分ミスリスト
	 * @throws LogicalException
	 */
	void entryMissListContactOperations(SosMst sosMst, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, String category,
		List<DistributionMissDto> missList) throws LogicalException;

	/**
	 * アテンション・お知らせ登録
	 *
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param errProdList エラー品目リスト
	 * @param startTime 開始時間
	 * @param resultType 処理結果区分
	 * @param execDpUser 実行者情報
	 * @param category 品目カテゴリ
	 * @param outputFileId 出力ファイルID
	 * @throws LogicalException
	 */
	void entryContactOperations(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, String category, Long outputFileId)
		throws LogicalException;

}
