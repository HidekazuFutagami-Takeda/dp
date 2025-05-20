package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacProdHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacProdScDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;

/**
 * 【管理】(ワ)特約店別計画検索サービスインターフェイス
 *
 * @author khashimoto
 */
public interface DpmTmsTytenPlanForVacSearchService {

	/**
	 * (ワ)特約店別計画一覧を検索する。
	 *
	 * @param scDto (ワ)特約店別計画の検索用DTO
	 * @return (ワ)特約店別計画編集画面ヘッダを表すDTO
	 * @throws LogicalException
	 */
	ManageWsPlanForVacHeaderDto searchHeader(ManageWsPlanForVacScDto scDto) throws LogicalException;

	/**
	 * (ワ)特約店別計画一覧を検索する。
	 *
	 * @param scDto (ワ)特約店別計画の検索用DTO
	 * @return (ワ)特約店別計画編集画面を表すDTO
	 * @throws LogicalException
	 */
	ManageWsPlanForVacDto searchList(ManageWsPlanForVacScDto scDto) throws LogicalException;

	/**
	 * (ワ)特約店別計画一覧(品目一覧)を検索する。
	 *
	 * @param scDto (ワ)特約店品目別計画の検索用DTO
	 * @return (ワ)特約店別計画編集画面ヘッダを表すDTO
	 * @throws LogicalException
	 */
	ManageWsPlanForVacProdHeaderDto searchHeader(ManageWsPlanForVacProdScDto scDto) throws LogicalException;


}
