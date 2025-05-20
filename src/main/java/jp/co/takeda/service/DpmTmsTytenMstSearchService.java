package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.SosListDto;
import jp.co.takeda.dto.TmsTytenMstHontenListDto;
import jp.co.takeda.dto.TmsTytenMstListDto;
import jp.co.takeda.dto.TmsTytenMstResultsListDto;
import jp.co.takeda.dto.TmsTytenMstResultsTenkaiListDto;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.dto.TmsTytenMstTenkaiListDto;

/**
 * 特約店情報に関するサービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpmTmsTytenMstSearchService {

	/**
	 * 組織（特約店部）一覧の検索処理
	 * 
	 * @return 組織（特約店部）情報のリスト
	 * @throws LogicalException
	 */
	SosListDto search() throws LogicalException;

	/**
	 * 特約店情報一覧（本店）の検索処理
	 * 
	 * @param scDto 特約店情報の検索条件DTO
	 * @return 特約店情報のリスト
	 * @throws LogicalException
	 */
	TmsTytenMstHontenListDto search(TmsTytenMstScDto scDto) throws LogicalException;

	/**
	 * 特約店情報一覧(支社)の検索処理
	 * 
	 * @param tmsTytenCd 特約店コード(本店)
	 * @param sosCd2 組織コード（特約店部）
	 * @return 特約店情報のリスト
	 * @throws LogicalException
	 */
	TmsTytenMstListDto search(String tmsTytenCd, String sosCd2) throws LogicalException;

	/**
	 * 特約店情報一覧の検索処理
	 * 
	 * @param hontenTmsTytenCd 特約店コード(本店)
	 * @param shishaTmsTytenCd 特約店コード(支社)
	 * @param scDto 特約店情報の検索条件DTO
	 * @return 特約店情報(展開)のリスト
	 * @throws LogicalException
	 */
	TmsTytenMstTenkaiListDto search(String hontenTmsTytenCd, String shishaTmsTytenCd, TmsTytenMstScDto scDto) throws LogicalException;

	/**
	 * 特約店実績一覧（本部・支社・支店）の検索処理
	 * 
	 * @param sosCd 組織コード（支店or営業所）
	 * @param scDto 特約店情報の検索条件DTO (NULL可)
	 * @return 特約店情報(実績)のリスト
	 * @throws LogicalException
	 */
	TmsTytenMstResultsListDto searchResultsList(String sosCd, TmsTytenMstScDto scDto) throws LogicalException;

	/**
	 * 特約店実績一覧（特約店）の検索処理
	 * 
	 * @param tmsTytenCd 特約店コード
	 * @param scDto 特約店情報の検索条件DTO (NULL可)
	 * @return 特約店情報(実績)のリスト
	 * @throws LogicalException
	 */
	TmsTytenMstResultsTenkaiListDto searchResultsTytenList(String tmsTytenCd, TmsTytenMstScDto scDto) throws LogicalException;
}
