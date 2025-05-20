package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenPlanSlideResultDto;

/**
 * 特約店別計画スライド検索サービスインターフェイス
 * 
 * @author yokokawa
 */
public interface DpsTmsTytenPlanSearchSlideService {
	/**
	 * 特約店別計画スライド状態を検索する。
	 * 
	 * @param sosCd2 組織コード(支店)
	 * @param category 品目カテゴリ
	 * @return スライド状態
	 */
	public TmsTytenPlanSlideResultDto searchSlideStatus(String sosCd2, String category) throws LogicalException;
}
