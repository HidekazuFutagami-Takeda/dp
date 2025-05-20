package jp.co.takeda.service;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenPlanAddInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddScDto;

/**
 * 特約店別計画追加・更新サービスインターフェイス
 *
 * @author yokokawa
 */
public interface DpsTmsTytenPlanAddService {

	/**
	 * 特約店名を検索する。
	 *
	 * @param tmsTytenCd 特約店コード(13桁)
	 * @return 特約店名
	 * @throws DataNotFoundException 特約店名がみつからない場合にスロー
	 */
	String searchTmsTytenName(String tmsTytenCd) throws DataNotFoundException;

	/**
	 * 営業所名を検索する。
	 *
	 * @param brCode 支店3桁コード
	 * @param distCode 営業所3桁コード
	 * @return 営業所名
	 * @throws DataNotFoundException 営業所名がみつからない場合にスロー
	 */
	String searchOfficeName(String brCode, String distCode) throws DataNotFoundException;

	/**
	 * 特約店別計画追加 品目一覧を検索する。
	 *
	 * @param tmsTytenPlanAddScDto 特約店別計画追加 検索条件
	 * @return 特約店別計画追加DTO
	 * @throws DataNotFoundException 特約店別計画追加 品目一覧がみつからない場合にスロー
	 */
	TmsTytenPlanAddResultDto searchProdList(TmsTytenPlanAddScDto tmsTytenPlanAddScDto ,String category) throws DataNotFoundException;

	/**
	 * 特約店別計画を登録する。
	 *
	 * @param tmsTytenPlanAddInputDto 特約店別計画入力値
	 * @return 登録行数
	 * @throws LogicalException 重複例外が発生した場合にスロー
	 */
	int addWsPlanProdList(TmsTytenPlanAddInputDto tmsTytenPlanAddInputDto ,String category) throws LogicalException;
}
