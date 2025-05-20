package jp.co.takeda.service;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpUser;

/**
 * 試算実行バッチサービスインタフェース
 *
 * @author khashimoto
 */
public interface DpsEstimationBatchService {

	/**
	 * バッチ処理における品目単位の試算処理を実行する。
	 *
	 * <pre>
	 * トランザクションを新規発行して、以下の処理を行う。
	 * ステータスのチェック
	 * 試算処理
	 * ステータス更新
	 * </pre>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiNo 実行者従業員番号
	 * @param mrPlanStatus 担当者別計画立案ステータス
	 * @throws LogicalException
	 */
	void executeEstProd(SosMst sosMst, DpUser execDpUser, MrPlanStatus mrPlanStatus) throws LogicalException;

	/**
	 *担当者別計画立案ステータスを元に戻す
	 *
	 *
	 * @param sosCd3
	 * @param estimationExecOrgDto
	 * @param execDpUser
	 * @throws LogicalException
	 */
	void resumeStatus(String sosCd3, EstimationExecOrgDto estimationExecOrgDto, DpUser execDpUser) throws LogicalException;

	/**
	 * アテンション・お知らせ登録
	 *
	 *
	 * @param sosCd3
	 * @param errProdList
	 * @param startTime
	 * @param resultType
	 * @param execDpUser
	 * @throws LogicalException
// add Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	 * @param category
// add End 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	 */
// mod Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
//	void entryContactOperations(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser) throws LogicalException;
	void entryContactOperations(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, String category) throws LogicalException;
// mod Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更

/* DEL Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	/**
	 * アテンション・お知らせ登録 ワクチン用
	 *
	 *
	 * @param sosCd3
	 * @param errProdList
	 * @param startTime
	 * @param resultType
	 * @param execDpUser
	 * @throws LogicalException
	 */
	/*
	void entryContactOperationsForVac(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser) throws LogicalException;
DEL End 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更  */


}
