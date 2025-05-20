package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistributionForVacParamsDto;

/**
 * (ワクチン)施設特約店・配分実行バッチサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsDistributionForVacBatchMainService {

	/**
	 * (ワクチン)施設特約店・配分処理(バッチ）を実行する。
	 * 
	 * 
	 * @param paramsDto 施設特約店・配分実行（バッチ）用DTO
	 * @throws LogicalException
	 */
	void executeBatch(DistributionForVacParamsDto paramsDto) throws LogicalException;

}
