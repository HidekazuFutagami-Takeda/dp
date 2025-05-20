package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.Announce;
import jp.co.takeda.security.DpUser;

import org.springframework.stereotype.Repository;

/**
 * お知らせ情報にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("announceDao")
public class AnnounceDaoImpl extends AbstractDao implements AnnounceDao {

	private static final String SQL_MAP = "DPS_C_ANNOUNCE_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// お知らせ情報を取得
	public Announce search(Long seqKey) throws DataNotFoundException {
		return (Announce) super.selectBySeqKey(seqKey);
	}

	// 従業員番号を元に、お知らせ情報のリストを取得
	public List<Announce> searchByJgiNo(String sortString, Integer jgiNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByJgiNo", paramMap);
	}

	// 指定出力ファイルIDを持つお知らせ情報の数を取得
	public Integer countAnnounce(Long outputFileId) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (outputFileId == null) {
			final String errMsg = "出力ファイルIDがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("outputFileId", outputFileId);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".countAnnounce", paramMap);
	}

	// お知らせ情報を登録
	public void insert(Announce record, DpUser dpUser) throws DuplicateException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "登録情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------
		// 登録実行
		// -------------
		record.setIsJgiNo(dpUser.getJgiNo());
		record.setIsJgiName(dpUser.getJgiName());
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		dataAccess.execute(getSqlMapName() + ".insert", record);
	}

	// お知らせ情報を更新
	public int update(Announce record) {
		return super.update(record);
	}

	// お知らせ情報を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
