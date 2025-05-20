package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosChoseiSummary;

/**
 * 組織/従業員単位の調整金額有無のサマリーを取得するDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface SosChoseiSummaryDao {

	/**
	 * 全社の調整金額有無を取得する。
	 * 
	 * 
	 * @return 組織/従業員単位の調整金額有無のサマリー
	 * @throws DataNotFoundException
	 */
	SosChoseiSummary searchIyaku() throws DataNotFoundException;

	/**
	 * 全支店の調整金額有無を取得する。
	 * 
	 * 
	 * @return 組織/従業員単位の調整金額有無のサマリーリスト
	 * @throws DataNotFoundException
	 */
	List<SosChoseiSummary> searchShitenList() throws DataNotFoundException;

	/**
	 * 指定した支店に属する全営業所の調整金額有無を取得する。
	 * 
	 * 
	 * @param sosCd2 組織コード(支店)
	 * @return 組織/従業員単位の調整金額有無のサマリーリスト
	 * @throws DataNotFoundException
	 */
	List<SosChoseiSummary> searchOfficeList(String sosCd2) throws DataNotFoundException;

	/**
	 * 指定した営業所に属する全チームの調整金額有無を取得する。
	 * 
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @return 組織/従業員単位の調整金額有無のサマリーリスト
	 * @throws DataNotFoundException
	 */
	List<SosChoseiSummary> searchTeamList(String sosCd3) throws DataNotFoundException;

	/**
	 * 指定したチームに属する全担当者の調整金額有無を取得する。
	 * 
	 * 
	 * @param sosCd4 組織コード(チーム)
	 * @return 組織/従業員単位の調整金額有無のサマリーリスト
	 * @throws DataNotFoundException
	 */
	List<SosChoseiSummary> searchMrList(String sosCd) throws DataNotFoundException;

	/**
	 * 調整金額情報の最終更新日時を取得する。
	 * 
	 * 
	 * @return 調整金額情報の最終更新日時
	 */
	Date getLastUpDate();

}
