package jp.co.takeda.dao;

import java.util.Date;

import org.springframework.dao.DataAccessException;

import jp.co.takeda.a.exp.DataNotFoundException;

/**
 * 営業所調整にアクセスするDAOインターフェイス
 *
 * @author mtsuchida
 */
public interface ChoseiDao {

	/**
	 * 調整金額更新日時を組織コード、品目固定コードで取得する。
	 *
	 * @param sosCd3 組織コード(営業所)【必須】
	 * @param category 品目カテゴリ【必須】
	 * @return 調整金額更新日時
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	Date searchMaxUpdate(String sosCd3, String category) throws DataNotFoundException;

	/**
	 * 調整のデータを最新にする。
	 *
	 * @param sosCd3 組織コード
	 * @throws DataAccessException 更新処理時にエラーが発生した場合にスロー
	 */
	void updateChosei(String sosCd3) throws DataAccessException;
}
