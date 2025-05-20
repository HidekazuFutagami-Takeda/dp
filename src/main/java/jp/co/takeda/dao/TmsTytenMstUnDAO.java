package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.TmsTytenMstHontenDto;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.model.TmsTytenMstUn;

/**
 * TMS特約店基本統合にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface TmsTytenMstUnDAO {

	/**
	 * ソート順<br>
	 * TMS特約店コード 昇順
	 */
	static final String SORT_STRING = "ORDER BY TMS_TYTEN_CD";

	/**
	 * 特約店情報を取得する。
	 * 
	 * @param tmstytenCd TMS特約店コード
	 * @return 特約店情報
	 * @throws DataNotFoundException
	 */
	TmsTytenMstUn search(String tmstytenCd) throws DataNotFoundException;

	/**
	 * 特約店情報を取得する。<br>
	 * 抽出条件として、階層レベル・本店・支社・支店コード・組織コード(特約店部)が指定可能。
	 * 
	 * @param sortString ソート条件
	 * @param scDto 特約店情報の検索条件(Search Condition)を表すDTO
	 * @return 特約店情報のリスト
	 * @throws DataNotFoundException
	 */
	List<TmsTytenMstUn> searchList(String sortString, TmsTytenMstScDto scDto) throws DataNotFoundException;

	/**
	 * 指定した組織コード(支店または営業所)を元に実績のある特約店情報を取得する。
	 * 
	 * @param sortString ソート条件
	 * @param sosCd 組織コード(支店または営業所)
	 * @param scDto 特約店情報の検索条件(Search Condition)を表すDTO(NULL可)
	 * @return 特約店情報のリスト
	 * @throws DataNotFoundException
	 */
	List<TmsTytenMstUn> searchResultsList(String sortString, String sosCd, TmsTytenMstScDto scDto) throws DataNotFoundException;

	/**
	 * 特約店情報(本店)のDTOを取得する。
	 * 
	 * <pre>
	 * 特約店情報に属性を付加したDTOを返す。
	 * 抽出条件として、組織コード(特約店部)・本店名カナが指定可能。
	 * </pre>
	 * 
	 * @param sortString ソート条件
	 * @param scDto 特約店情報の検索条件(Search Condition)を表すDTO
	 * @return 特約店情報の拡張DTOのリスト
	 * @throws DataNotFoundException
	 */
	List<TmsTytenMstHontenDto> searchHontenList(String sortString, TmsTytenMstScDto scDto) throws DataNotFoundException;
}
