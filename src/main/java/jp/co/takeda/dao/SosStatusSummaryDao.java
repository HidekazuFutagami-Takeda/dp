package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.view.SosStatusSummary;

/**
 * 組織単位のステータスのサマリーを取得するDAOインターフェイス
 *
 * @author khashimoto
 */
public interface SosStatusSummaryDao {

//	/**
//	 * 組織コード(支店)・カテゴリを基に、ステータスのサマリを取得する。
//	 *
//	 * @param sosCd2 組織コード(支店)
//	 * @param category カテゴリ
//	 * @return 組織単位のステータスのサマリ情報
//	 * @throws DataNotFoundException
//	 */
//	SosStatusSummary searchShiten(String sosCd2, String category) throws DataNotFoundException;
//

	/**
	 * 上位の組織コードを元に組織単位のステータスのサマリのリストを取得する
	 *
	 * @param sortString ソート条件
	 * @param sosListType 組織一覧の種類
	 * @param bumonRank 上位組織の部門ランク(NULL可)
	 * @param upSosCd 上位組織コード(NULL可)
	 * @return 組織単位のステータスのサマリのリスト
	 * @param category カテゴリ
	 * @throws DataNotFoundException
	 */
	 List<SosStatusSummary> searchList(String sortString, SosListType sosListType, BumonRank bumonRank, String upSosCd, String category ) throws DataNotFoundException;

//	/**
//	 * 組織コード(営業所)・カテゴリを基に、ステータスのサマリを取得する。
//	 *
//	 * @param sosCd3 組織コード(営業所)
//	 * @param category カテゴリ
//	 * @return 組織単位のステータスのサマリ情報
//	 * @throws DataNotFoundException
//	 */
//	SosStatusSummary searchEigyo(String sosCd3, String category) throws DataNotFoundException;



	SosStatusSummary search(String sosCd, BumonRank bumonRank, String category) throws DataNotFoundException;

//	/**
//	 * 組織コード(チーム)・カテゴリを基に、ステータスのサマリを取得する。<br>
//	 * 担当者別計画ステータスの担当者別計画入力完了(AL修正)の件数、<br>
//	 * 担当者別計画入力完了日時はチーム別入力状況より取得する。
//	 *
//	 * @param sosCd4 組織コード(チーム)
//	 * @param category カテゴリ
//	 * @return 組織単位のステータスのサマリ情報
//	 * @throws DataNotFoundException
//	 */
//	SosStatusSummary searchTeam(String sosCd4, ProdCategory category) throws DataNotFoundException;
}
