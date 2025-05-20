package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.EstimationParamsDto;

/**
 * 試算実行サービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsEstimationBatchMainService {

	/**
	 * 試算処理を実行する。
	 * 
	 * 
	 * @param iyakuEstimationDto 試算実行用DTO
	 * @throws LogicalException
	 */
	void executeBatch(EstimationParamsDto iyakuEstimationDto) throws LogicalException;
}
