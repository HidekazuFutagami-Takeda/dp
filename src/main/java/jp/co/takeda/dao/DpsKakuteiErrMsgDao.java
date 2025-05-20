package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.DpsKakuteiErrMsg;

/**
 * 一括確定エラー情報にアクセスするDAOインターフェイス
 */
public interface DpsKakuteiErrMsgDao {


	/**
	 * 一括確定エラー情報を取得する。
	 *
	 * @return 一括確定エラー情報
	 * @throws DataNotFoundException
	 */
	public List<DpsKakuteiErrMsg> search() throws DataNotFoundException ;

	/**
	 * 一括確定エラー情報を初期化する。
	 */
	public void delete();

	/**
	 * 一括確定エラー情報を登録する。
	 *
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目名称
	 * @param errMsg エラーメッセージ
	 */
	public void insert(String sosCd, Integer jgiNo, String prodCode, String errMsg);

}
