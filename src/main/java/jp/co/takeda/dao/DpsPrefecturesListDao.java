package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.Prefectures;

/**
 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
 * 組織に紐づく都道府県にアクセスするDAOインターフェイス
 *
 * @author yTaniguchi
 *
 */
public interface DpsPrefecturesListDao {
	/**
	 * 組織に紐づく都道府県一覧を取得する。
	 * @param sosCd 組織コード
	 * @return 組織に紐づく都道府県一覧
	 * @throws DataNotFoundException
	 */
	List<Prefectures> search(String sosCd) throws DataNotFoundException;
}
