package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenPlanEditInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditScDto;

/**
 * 特約店別計画編集サービスインターフェイス
 *
 * @author yokokawa
 */
public interface DpsTmsTytenPlanEditService {
	/**
	 * 特約店別計画 編集対象検索
	 *
	 * @param tmsTytenPlanEditScDto 特約店別計画編集 検索条件
	 * @return 特約店別計画編集 検索結果
	 */
	public TmsTytenPlanEditResultDto searchEditWsPlan(TmsTytenPlanEditScDto tmsTytenPlanEditScDto);

	/**
	 * 特約店別計画 編集
	 *
	 * @param tmsTytenPlanEditInputDto 特約店別計画編集 入力値
	 * @throws LogicalException
	 */
	public void editWsPlan(TmsTytenPlanEditInputDto tmsTytenPlanEditInputDto) throws LogicalException;

	/**
	 * 特約店別計画 参照登録
	 *
	 * @param tmsTytenPlanEditInputDto 特約店別計画参照登録 入力値
	 * @throws LogicalException
	 */
	public void editWsPlanRefer(TmsTytenPlanEditInputDto tmsTytenPlanEditInputDto) throws LogicalException;

}
