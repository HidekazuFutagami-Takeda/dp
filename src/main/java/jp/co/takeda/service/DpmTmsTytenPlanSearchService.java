package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ManageWsPlanDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuHeaderDto;
import jp.co.takeda.dto.ManageWsPlanProdDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuProdHeaderDto;
import jp.co.takeda.dto.ManageWsPlanProdScDto;
import jp.co.takeda.dto.ManageWsPlanScDto;

/**
 * 【管理】特約店別計画検索サービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpmTmsTytenPlanSearchService {

	/**
	 * 特約店別計画一覧を検索する。
	 * 
	 * @param scDto 特約店別計画の検索用DTO
	 * @return 特約店別計画編集画面ヘッダを表すDTO
	 * @throws LogicalException
	 */
	ManageWsPlanForIyakuHeaderDto searchHeader(ManageWsPlanScDto scDto) throws LogicalException;

	/**
	 * 特約店別計画一覧を検索する。
	 * 
	 * @param scDto 特約店別計画の検索用DTO
	 * @return (医)特約店別計画編集画面を表すDTO
	 * @throws LogicalException
	 */
	ManageWsPlanDto searchList(ManageWsPlanScDto scDto) throws LogicalException;

	/**
	 * 特約店別計画一覧(品目一覧)を検索する。
	 * 
	 * @param scDto 特約店別計画の検索用DTO
	 * @return 特約店別計画編集画面ヘッダを表すDTO
	 * @throws LogicalException
	 */
	ManageWsPlanForIyakuProdHeaderDto searchHeader(ManageWsPlanProdScDto scDto) throws LogicalException;

	/**
	 * 特約店別計画一覧(品目一覧)を検索する。
	 * 
	 * @param scDto 特約店別計画の検索用DTO
	 * @return (医)特約店別計画編集画面を表すDTO
	 * @throws LogicalException
	 */
	ManageWsPlanProdDto searchList(ManageWsPlanProdScDto scDto) throws LogicalException;

}
