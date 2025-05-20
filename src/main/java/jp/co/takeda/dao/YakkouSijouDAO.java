package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.YakkouSijou;

/**
 * 薬効市場にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface YakkouSijouDAO {

	/**
	 * ソート順<br>
	 * 薬効市場ソートキー 昇順
	 */
	static final String SORT_STRING = "ORDER BY YAKKOU_SIJOU_SORT_KEY";

	/**
	 * 薬効市場を取得する。
	 *
	 * @param yakkouSijouCode 薬効市場コード
	 * @return 薬効市場
	 * @throws DataNotFoundException
	 */
	YakkouSijou search(String yakkouSijouCode) throws DataNotFoundException;

	/**
	 * 薬効市場のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @return 薬効市場のリスト
	 * @throws DataNotFoundException
	 */
	List<YakkouSijou> searchList(String sortString, String category) throws DataNotFoundException;

}
