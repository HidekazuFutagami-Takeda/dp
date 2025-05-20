package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.DpsKakuteiErrMsg;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 一括確定エラー情報にアクセスするDAO実装クラス
 *
 */
@Repository("dpsKakuteiErrMsgDao")
public class DpsKakuteiErrMsgDaoImpl extends AbstractDao implements DpsKakuteiErrMsgDao {

	private static final String SQL_MAP = "DPS_C_KAKUTEI_ERR_MSG_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

// 一括確定エラー情報を取得
	public List<DpsKakuteiErrMsg> search() throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------

		Map<String, Object> paramMap = new HashMap<String, Object>();

		return dataAccess.queryForList(getSqlMapName() + ".select", paramMap);
	}

// 	一括確定エラー情報を削除
	public void delete() {

		// ----------------------
		// 削除条件生成
		// ----------------------

		Map<String, Object> paramMap = new HashMap<String, Object>();

		dataAccess.execute(getSqlMapName() + ".delete", paramMap);
	}


//	 一括確定エラー情報を登録
	public void insert(String sosCd, Integer jgiNo, String prodCode, String errMsg) {

		// ----------------------
		// 引数チェック
		// ----------------------


		// -------------
		// 登録実行
		// -------------
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("message", errMsg);
		paramMap.put("isJgiNo", dpUser.getJgiNo());
		paramMap.put("isJgiName", dpUser.getJgiName());
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName",dpUser.getJgiName());
//ここから
		dataAccess.execute(getSqlMapName() + ".insert", paramMap);

	}
}
