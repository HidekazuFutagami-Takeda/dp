package jp.co.takeda.service;

import jp.co.takeda.dto.TmsTytenPlanSlideForVacResultDto;

/**
 * ワクチン用特約店別計画スライド検索サービスインターフェイス
 * 
 * @author yokokawa
 */
public interface DpsTmsTytenPlanSearchSlideForVacService {
	/**
	 * 特約店別計画スライド状態を検索する。
	 * 
	 * @return スライド状態
	 */
	public TmsTytenPlanSlideForVacResultDto searchSlideStatus();
}
