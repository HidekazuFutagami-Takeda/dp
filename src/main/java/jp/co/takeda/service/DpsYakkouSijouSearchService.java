package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.model.YakkouSijou;

/**
 * 薬効市場検索サービスインターフェイス
 *
 * @author tkawabata
 */
public interface DpsYakkouSijouSearchService {

	/**
	 * 薬効市場のリストを取得する。
	 *
	 * @return 薬効市場のリスト
	 */
	List<YakkouSijou> searchList(String category);
}
