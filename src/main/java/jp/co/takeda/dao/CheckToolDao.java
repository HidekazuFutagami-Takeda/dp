package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.CheckTool;

/**
 * 施設特約店計画取得DAOインターフェイス
 *
 * @author ksuzuki
 */
public interface CheckToolDao {

	/**
	 * ソート順<br>
	 * 支店コードソート順<br>
	 * 営業所コードソート順<br>
	 * 品目並び順ソート順
	 */
	static final String SORT_STRING_EXCEL = "DSM.UP_SOS_CD,DSM.SOS_CD,P.PROD_CODE,MIP.MR_NO,SIM.HO_INS_TYPE,P.TMS_TYTEN_CD,P.INS_NO";

	/**
	 * 施設特約店計画リストを取得する。
	 *
	 * <pre>
	 *   支店・営業所コード	  組織情報．医薬支店Ｃ＋組織情報．医薬営業所Ｃ
     *   支店・営業所名	      組織情報．部門名正式（支店名）＋組織情報．部門名正式（営業所名）
     *   統計品名コード	      計画対象品目．統計品名コード(品目)
     *   品目名	              計画対象品目．品目名称
	 * </pre>
	 *
	 * @param sortString ソート条件
	 * @return 施設特約店計画データ
	 * @throws DataNotFoundException 検索結果が0件の場合にスロー
	 */
	List<CheckTool> searchList(String sortString) throws DataNotFoundException;


}
