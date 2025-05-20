package jp.co.takeda.service;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenPlanAddForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacScDto;

/**
 * ワクチン特約店別計画追加・更新サービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpsTmsTytenPlanAddForVacService {

	/**
	 * 特約店名を検索する。
	 * 
	 * @param tmsTytenCd 特約店コード(13桁)
	 * @return 特約店名
	 * @throws DataNotFoundException 特約店名がみつからない場合にスロー
	 */
	String searchTmsTytenName(String tmsTytenCd) throws DataNotFoundException;

	/**
	 * エリア特約店Ｇ名を検索する。
	 * 
	 * @param brCode 支店3桁コード
	 * @param distCode 営業所3桁コード
	 * @return エリア特約店Ｇ名
	 * @throws DataNotFoundException エリア特約店Ｇ名がみつからない場合にスロー
	 */
	String searchAreaGName(String brCode, String distCode) throws DataNotFoundException;

	/**
	 * ワクチン特約店別計画追加 品目一覧を検索する。
	 * 
	 * @param tmsTytenPlanAddForVacScDto ワクチン特約店別計画追加 検索条件
	 * @return ワクチン特約店別計画追加DTO
	 * @throws DataNotFoundException ワクチン特約店別計画追加 品目一覧がみつからない場合にスロー
	 */
	TmsTytenPlanAddForVacResultDto searchProdList(TmsTytenPlanAddForVacScDto tmsTytenPlanAddForVacScDto) throws DataNotFoundException;

	/**
	 * ワクチン特約店別計画を登録する。
	 * 
	 * @param tmsTytenPlanAddForVacInputDto ワクチン特約店別計画入力値
	 * @param category カテゴリ（04固定）
	 * @return 登録行数
	 * @throws LogicalException 重複例外が発生した場合にスロー
	 */
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	int addWsPlanProdList(TmsTytenPlanAddForVacInputDto tmsTytenPlanAddForVacInputDto, String category) throws LogicalException;
}
