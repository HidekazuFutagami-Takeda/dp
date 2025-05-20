package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.div.BumonRank;

/**
 * 組織情報にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface SosMstDAO {

	/**
	 * ソート順<br>
	 * 医薬支店Ｃ　　昇順<br>
	 * 営業所配列　　昇順<br>
	 * 医薬営業所Ｃ　昇順<br>
	 * 医薬チームＣ　昇順
	 */
	static final String SORT_STRING = "ORDER BY SM.BR_CODE, SM.DIST_SEQ, SM.DIST_CODE, SM.TEAM_CODE";

	/**
	 * 組織情報を取得する。
	 *
	 * @param sosCd 組織コード
	 * @return 組織情報
	 */
	SosMst search(String sosCd) throws DataNotFoundException;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 都道府県コードに紐づく組織コードを取得する。
	 *
	 * @param addrCodePref 都道府県コード
	 * @return 組織コード
	 */
	SosMst searchBumon(String addrCodePref) throws DataNotFoundException;

	/**
	 * 医薬支店Ｃ、医薬営業所Ｃを基に<br>
	 * 部門ランク３(営業所・エリア特約店G)の組織情報を取得する。
	 *
	 * @param brCode 医薬支店Ｃ
	 * @param distCode 医薬営業所Ｃ
	 * @return 組織情報
	 * @throws DataNotFoundException
	 */
	SosMst search(SosListType sosListType, String brCode, String distCode) throws DataNotFoundException;

	/**
	 * 上位の組織コードを元に組織情報一覧を取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 上位組織の部門ランク(NULL可)
	 * @param upSosCd 上位組織コード(NULL可)
	 * @return 組織情報のリスト
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchList(String sortString, SosListType sosListType, BumonRank bumonRank, String upSosCd) throws DataNotFoundException;

	/**
	 * 特約店部一覧を取得する。
	 *
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchTokuyakutenbuList() throws DataNotFoundException;

	/**
	 * 組織コードから組織カテゴリ一覧を取得する。
	 * @param sosCd 組織コード
	 * @return 組織カテゴリリスト
	 * @throws DataNotFoundException
	 */
	List<SosMstCtg> searchSosCategoryList(String sosCd) throws DataNotFoundException;

	/**
	 * 上位の組織コードを元に組織情報一覧を取得する。組織カテゴリ指定
	 *
	 * @param sortString ソート条件
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 上位組織の部門ランク(NULL可)
	 * @param upSosCd 上位組織コード(NULL可)
	 * @param sosCategory 組織カテゴリ
	 * @return 組織情報のリスト
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchListFilterBySosCategory(String sortString, SosListType sosListType, BumonRank bumonRank,
			String upSosCd, String sosCategory) throws DataNotFoundException;

	/**
	 * 上位の組織コードを元に組織情報一覧を取得する。組織カテゴリ指定
	 *
	 * @param sortString ソート条件
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 上位組織の部門ランク(NULL可)
	 * @param upSosCd 上位組織コード(NULL可)
	 * @param allCategory 組織カテゴリ全部
	 * @return 組織情報のリスト
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchListFilterByAllSosCategory(String sortString, SosListType sosListType, BumonRank bumonRank,
			String upSosCd, String allCategory) throws DataNotFoundException;
}
