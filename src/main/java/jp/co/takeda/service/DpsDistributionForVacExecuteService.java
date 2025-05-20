package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.security.DpUser;

/**
 * (ワクチン)施設特約店別計画・配分実行サービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsDistributionForVacExecuteService {

	/**
	 * 品目単位の(ワクチン)施設特約店別計画・配分処理を実行する。
	 * 
	 * <pre>
	 * 配分処理内の例外は全てSystemExceptionとする。
	 * 業務エラーとする場合は、当処理実行前に業務チェックを実施すること。
	 * </pre>
	 * 
	 * @param prodCode 品目固定コード
	 * @param execDpUser 実行者従業員情報
	 * @return 配分ミス情報リスト
	 */
	List<DistributionMissDto> execute(String prodCode, DpUser execDpUser);

	/**
	 * 従業員・品目単位の(ワクチン)施設特約店別計画・再配分処理を実行する。
	 * 
	 * <pre>
	 * 配分処理内の例外は全てSystemExceptionとする。
	 * 業務エラーとする場合は、当処理実行前に業務チェックを実施すること。
	 * </pre>
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param execDpUser 実行者従業員情報
	 * @return 配分ミス情報リスト
	 */
	List<DistributionMissDto> executeAgain(Integer jgiNo, String prodCode, DpUser execDpUser);

}
