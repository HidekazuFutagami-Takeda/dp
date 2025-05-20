package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistributionParamsDto;
import jp.co.takeda.dto.DocDistributionParamsDto;

/**
 * 施設特約店・配分実行バッチサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsDistributionBatchMainService {

	/**
	 * 施設特約店・配分処理(バッチ）を実行する。
	 * 
	 * 
	 * @param paramsDto 施設特約店・配分実行（バッチ）用DTO
	 * @throws LogicalException
	 */
	void executeInsWsBatch(DistributionParamsDto paramsDto) throws LogicalException;

	/**
	 * 施設医師別・配分処理(バッチ）を実行する。
	 * 
	 * 
	 * @param paramsDto 施設特約店・配分実行（バッチ）用DTO
	 * @throws LogicalException
	 */
	void executeInsDocBatch(DocDistributionParamsDto paramsDto) throws LogicalException;

}
