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
public interface SosMstRealDao {

	/**
	 * ソート順<br>
	 * 医薬支店Ｃ　　昇順<br>
	 * 営業所配列　　昇順<br>
	 * 医薬営業所Ｃ　昇順<br>
	 * 医薬チームＣ　昇順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE";

	/**
	 * 組織情報を取得する。
	 *
	 * @param sosCd 組織コード
	 * @return 組織情報
	 */
	SosMst searchReal(String sosCd) throws DataNotFoundException;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 都道府県コードに紐づく組織コードを取得
	 * @param addrCodePref 都道府県コード
	 * @return 組織コード
	 */
	SosMst searchBumon(String addrCodePref) throws DataNotFoundException;

	/**
	 * 雑チーム組織情報を取得する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @return 組織情報
	 */
	SosMst searchRealEtcSos(String sosCd) throws DataNotFoundException;

	/**
	 * 医薬支店Ｃ、医薬営業所Ｃを基に<br>
	 * 部門ランク３(営業所・エリア特約店G)の組織情報を取得する。
	 *
	 * @param brCode 医薬支店Ｃ
	 * @param distCode 医薬営業所Ｃ
	 * @return 組織情報
	 * @throws DataNotFoundException
	 */
	SosMst searchReal(String brCode, String distCode) throws DataNotFoundException;

	/**
	 * 上位の組織コードを元に組織情報一覧を取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 上位組織の部門ランク
	 * @param upSosCd 上位組織コード(営業所階層以下の検索時は、必須)
	 * @return 組織情報のリスト
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchRealList(String sortString, SosListType sosListType, BumonRank bumonRank, String upSosCd) throws DataNotFoundException;

	/**
	 * 特約店部一覧を取得する。
	 * @param ryutsuStringList 流通政策部のコードリスト
	 *
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchTokuyakutenbuList(List<String> ryutsuStringList) throws DataNotFoundException;

	/**
	 * 組織グループから組織情報一覧を取得する。
	 *
	 * @param sortString
	 * @param sosGroup 組織グループ
	 * @return
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchRealList(String sortString, String sosGroup) throws DataNotFoundException;

	/**
	 * 組織コードから組織カテゴリ一覧を取得する。
	 * @param sosCd 組織コード
	 * @return 組織カテゴリリスト
	 * @throws DataNotFoundException
	 */
	List<SosMstCtg> searchSosCategoryList(String sosCd) throws DataNotFoundException;

	/**
	 * 組織コードから計画支援の組織カテゴリ一覧を取得する。
	 * @param sosCd 組織コード
	 * @return 組織カテゴリリスト
	 * @throws DataNotFoundException
	 */
	List<SosMstCtg> searchDpsSosCategoryList(String sosCd) throws DataNotFoundException;

}
