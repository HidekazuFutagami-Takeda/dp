package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.SosDistMiss;
import jp.co.takeda.security.DpUser;

import org.springframework.stereotype.Repository;

/**
 * 組織別配分ミス情報にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("sosDistMissDao")
public class SosDistMissDaoImpl extends AbstractDao implements SosDistMissDao {

	private static final String SQL_MAP = "DPS_C_SOS_DIST_MISS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 組織別配分ミス情報を取得
	public SosDistMiss search(Long seqKey) throws DataNotFoundException {
		return (SosDistMiss) super.selectBySeqKey(seqKey);
	}

	// 組織別配分ミス情報のリストを取得
	public List<SosDistMiss> searchList(String sortString, Long outputFileId) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("outputFileId", outputFileId);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 組織別配分ミス情報を登録
	public void insert(SosDistMiss record) throws DuplicateException {
		super.insertNonCheck(record);
	}

	// 組織別配分ミス情報を登録
	public void insert(SosDistMiss record, DpUser dpUser) throws DuplicateException {
		super.insertNonCheck(record, dpUser);
	}

	// 組織別配分ミス情報を削除
	public int delete(Long outputFileId) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (outputFileId == null) {
			final String errMsg = "出力情報IDがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Long> paramMap = new HashMap<String, Long>(1);
		paramMap.put("outputFileId", outputFileId);

		// ----------------------
		// 削除実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".deleteByOutputFileId", paramMap);
	}
}
