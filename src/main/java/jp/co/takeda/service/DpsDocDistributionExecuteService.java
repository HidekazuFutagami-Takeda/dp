package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsDocHaibunDto;

/**
 * 施設特約店別計画・配分実行サービスインタフェース
 *
 * @author khashimoto
 */
public interface DpsDocDistributionExecuteService {

	/**
	 * 従業員単位の施設医師別計画・配分処理を実行する。
	 *
	 * <pre>
	 * 配分処理内の例外は全てSystemExceptionとする。
	 * 業務エラーとする場合は、当処理実行前に業務チェックを実施すること。
	 * </pre>
	 *
	 * @param insDocHaibunDto 施設医師配分パラメータ
	 * @return 配分ミス情報リスト
	 */
	List<DistributionMissDto> execute(InsDocHaibunDto insDocHaibunDto);

}
