package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenPlanEditForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacScDto;

/**
 * ワクチン特約店別計画編集サービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpsTmsTytenPlanEditForVacService {

	/**
	 * ワクチン特約店別計画 編集対象検索
	 * 
	 * @param tmsTytenPlanEditScDto ワクチン特約店別計画編集 検索条件
	 * @return ワクチン特約店別計画編集 検索結果
	 * @throws LogicalException
	 */
	public TmsTytenPlanEditForVacResultDto searchEditWsPlan(TmsTytenPlanEditForVacScDto tmsTytenPlanEditScDto) throws LogicalException;

	/**
	 * ワクチン特約店別計画 編集
	 * 
	 * @param tmsTytenPlanEditInputDto ワクチン特約店別計画編集 入力値
	 * @throws LogicalException
	 */
	public void editWsPlan(TmsTytenPlanEditForVacInputDto tmsTytenPlanEditInputDto) throws LogicalException;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * ワクチン特約店別計画 参照登録
	 *
	 * @param tmsTytenPlanEditInputDto ワクチン特約店別計画編集 入力値
	 * @throws LogicalException
	 */
	public void editWsPlanRefer(TmsTytenPlanEditForVacInputDto tmsTytenPlanEditInputDto) throws LogicalException;

}
