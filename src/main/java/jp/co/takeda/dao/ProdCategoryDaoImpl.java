package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;

/**
 * 品目分類情報にアクセスするDAO実装クラス
 *
 * @author rna8405
 */
@Repository("prodCategoryDao")
public class ProdCategoryDaoImpl extends AbstractManageDao implements ProdCategoryDao {

	private static final String SQL_MAP = "DPM_S_SY_COM_PCAT_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	@Override
	public String searchProdCategoryName(String category, boolean isJrns, String jrnsPcatCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (category == null) {
			final String errMsg = "カテゴリコードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("category", category);
		// JRNSの場合は、JRNSの品目分類コードを設定
		if (isJrns) {
			paramMap.put("jrnsPcatCd", jrnsPcatCd);
		}
		// ----------------------
		// 検索実行
		// ----------------------
		return super.select(getSqlMapName() + ".selectProdCategoryName", paramMap);
	}
}
