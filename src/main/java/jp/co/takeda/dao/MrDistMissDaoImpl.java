package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;

import org.springframework.stereotype.Repository;

/**
 * 担当者別配分ミス情報にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mrDistMissDao")
public class MrDistMissDaoImpl extends AbstractDao implements MrDistMissDao {

	private static final String SQL_MAP = "DPS_C_MR_DIST_MISS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 担当者別配分ミス情報を取得
	public MrDistMiss search(Long seqKey) throws DataNotFoundException {
		return (MrDistMiss) super.selectBySeqKey(seqKey);
	}

	// 担当者別配分ミス情報のリストを取得
	public List<MrDistMiss> searchList(String sortString, Long outputFileId) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("outputFileId", outputFileId);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 担当者別配分ミス情報を登録
	public void insert(MrDistMiss record) throws DuplicateException {
		super.insertNonCheck(record);
	}

	// 担当者別配分ミス情報を登録
	public void insert(MrDistMiss record, DpUser dpUser) throws DuplicateException {
		super.insertNonCheck(record, dpUser);
	}

	// 担当者別配分ミス情報を削除
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
