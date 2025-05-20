package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;

/**
 * 親子関連情報にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("dpsSyComInsOyakoDao")
public class DpsSyComInsOyakoDaoImpl extends AbstractDao implements DpsSyComInsOyakoDao {

	private static final String SQL_MAP = "DPS_C_SY_COM_INS_OYAKO_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 親子関連情報を取得
	public int searchCount(String oyakoKb) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (oyakoKb == null) {
			final String errMsg = "親子区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("oyakoKb", oyakoKb);

		// ----------------------
		// 検索実行
		// ----------------------
		try {
			return dataAccess.queryForObject(getSqlMapName() + ".selectCount", paramMap);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			throw new SystemException(new Conveyance(ErrMessageKey.LOGICAL_ERROR,"Count(*)なのに取得できないことはあり得ない"));
		}
	}
}
