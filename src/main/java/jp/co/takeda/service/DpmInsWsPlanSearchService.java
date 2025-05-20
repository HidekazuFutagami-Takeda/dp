package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ManageInsWsPlanDto;
import jp.co.takeda.dto.ManageInsWsPlanHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdDto;
import jp.co.takeda.dto.ManageInsWsPlanProdHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdScDto;
import jp.co.takeda.dto.ManageInsWsPlanScDto;

/**
 * 【管理】施設特約店別計画検索サービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpmInsWsPlanSearchService {

	/**
	 * 施設特約店別計画ヘッダを検索する。
	 * 
	 * @param scDto 施設特約店別計画の検索用DTO
	 * @return 施設特約店別計画編集画面ヘッダを表すDTO
	 * @throws LogicalException
	 */
	ManageInsWsPlanHeaderDto searchHeader(ManageInsWsPlanScDto scDto) throws LogicalException;

	/**
	 * 施設特約店別計画一覧を検索する。
	 * 
	 * @param scDto 施設特約店別計画の検索用DTO
	 * @return (医)施設特約店別計画編集画面を表すDTO
	 * @throws LogicalException
	 */
	ManageInsWsPlanDto searchList(ManageInsWsPlanScDto scDto) throws LogicalException;

	/**
	 * 施設特約店品目別計画ヘッダを検索する。
	 * 
	 * @param scDto 施設特約店品目別計画の検索用DTO
	 * @return 施設特約店品目別計画編集画面ヘッダを表すDTO
	 * @throws LogicalException
	 */
	ManageInsWsPlanProdHeaderDto searchHeader(ManageInsWsPlanProdScDto scDto) throws LogicalException;

	/**
	 * 施設特約店別計画品目一覧を検索する。
	 * 
	 * @param scDto 施設特約店別計画の検索用DTO
	 * @return (医)施設特約店品目別計画編集画面を表すDTO
	 * @throws LogicalException
	 */
	ManageInsWsPlanProdDto searchList(ManageInsWsPlanProdScDto scDto) throws LogicalException;

}
