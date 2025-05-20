package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.InsMstScDto;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.div.MrCat;

/**
 * 施設情報にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface InsMstRealDao {

	/**
	 * ソート順<br>
	 * 対象区分 昇順<br>
	 * 施設内部コード 昇順<br>
	 * 施設内部コード(サブコード)昇順
	 *
	 */
	static final String SORT_STRING = "ORDER BY HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * 施設情報を取得する。
	 *
	 * @param insNo 施設コード
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	InsMst searchReal(String insNo) throws DataNotFoundException;

	/**
	 * 施設情報を取得する。<br>
	 * 施設を担当するMR情報も取得する。
	 *
	 * @param insNo 施設コード
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	InsMst searchRealIncludeMr(String insNo) throws DataNotFoundException;

	/**
	 * 施設情報を取得する。<br>
	 * 施設を担当するMR情報も取得する。
	 *
	 * @param insNo 施設コード
	 * @param mrCat MRの種類
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	InsMst searchRealIncludeMr(String insNo, MrCat mrCat) throws DataNotFoundException;

	/**
	 * 施設情報を取得する。<br>
	 * 施設を担当するMR情報も取得する。
	 *
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	InsMst searchRealIncludeMr(String insNo, Integer jgiNo) throws DataNotFoundException;

	/**
	 * 施設コード、品目固定コードから施設情報を取得する。<br>
	 * 施設を担当するMR情報も取得する。
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	InsMst searchRealIncludeMr(String insNo, String prodCode) throws DataNotFoundException;

	/**
	 * 施設コード、品目固定コードから施設情報(親子紐づけ)を取得する。<br>
	 * 施設を担当するMR情報も取得する。
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分２
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 施設情報
	 * @throws DataNotFoundException
	 */
	InsMst searchRealIncludeMrOyako(String insNo, String prodCode, String oyakoKb, String oyakoKb2, String oyakoKbProdCode) throws DataNotFoundException;

	/**
	 * 施設情報の検索条件DTOを元にMRの担当する施設情報のリストを取得する。<br>
	 * 組織コードまたは、従業員番号の設定必須。<br>
	 * 雑担当の施設は含まない。
	 *
	 * @param scDto 施設情報の検索条件
	 * @return 施設情報のリスト
	 * @throws DataNotFoundException
	 */
	List<InsMst> searchRealList(InsMstScDto scDto) throws DataNotFoundException;
}
