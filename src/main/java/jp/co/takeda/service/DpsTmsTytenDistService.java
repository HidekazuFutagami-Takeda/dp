package jp.co.takeda.service;

import jp.co.takeda.dto.TmsTytenDistParamDto;

/**
 * 特約店別計画配分サービスインターフェイス
 * 
 * @author yokokawa
 */
public interface DpsTmsTytenDistService {
	/**
	 * 特約店別計画配分の前処理を実行する。
	 * 
	 * @param dto 特約店別計画配分パラメータ
	 */
	public void distPreparation(TmsTytenDistParamDto dto);
}
