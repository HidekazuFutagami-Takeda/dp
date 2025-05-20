package jp.co.takeda.service;

import jp.co.takeda.security.DpUser;

/**
 * 試算実行サービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsEstimationExecuteService {

	/**
	 * 品目単位の試算処理を実行する。
	 * 
	 * <pre>
	 * ・組織コードが営業所の場合、
	 * 営業所計画をもとに、試算処理を行いチーム別計画・担当者別計画を更新する。
	 *
	 * ・組織コードがチームの場合、
	 * チーム別計画をもとに、試算処理を行い担当者別計画を更新する。
	 *
	 * 試算処理内の例外は全てSystemExceptionとする。
	 * 業務エラーとする場合は、当処理実行前に業務チェックを実施すること。
	 * </pre>
	 * 
	 * @param sosCd 組織コード(営業所/チーム)
	 * @param prodCode 品目固定コード
	 * @param execDpUser 実行者従業員情報
	 */
	void execute(String sosCd, String prodCode, DpUser execDpUser);

}
