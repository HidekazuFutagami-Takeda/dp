package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenDistParamDto;

/**
 * 特約店別計画配分処理 バッチメインサービスインターフェイス
 * 
 * @author yokokawa
 */
public interface DpsTmsTytenDistMainService {
	/**
	 * 特約店別計画配分処理を実行する。
	 * 
	 * @param tmsTytenDistParamDto 特約店別計画配分パラメータDTO
	 * @throws LogicalException
	 */
	public void execute(TmsTytenDistParamDto tmsTytenDistParamDto) throws LogicalException;
}
