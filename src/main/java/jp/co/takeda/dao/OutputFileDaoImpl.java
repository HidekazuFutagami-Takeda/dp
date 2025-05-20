package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.security.DpUser;

import org.springframework.stereotype.Repository;

/**
 * 出力ファイル情報にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("outputFileDao")
public class OutputFileDaoImpl extends AbstractDao implements OutputFileDao {

	private static final String SQL_MAP = "DPS_C_OUTPUT_FILE_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 出力ファイル情報を取得
	public OutputFile search(Long seqKey) throws DataNotFoundException {
		return (OutputFile) super.selectBySeqKey(seqKey);
	}

	// 出力ファイル情報を登録
	public void insert(OutputFile record) throws DuplicateException {
		super.insertNonCheck(record);
	}

	// 出力ファイル情報を登録(シーケンスキーは事前に設定する必要有)
	public void insert(OutputFile record, DpUser dpUser) throws DuplicateException {
		super.insertNonCheck(record, dpUser);
	}

	// 出力ファイル情報を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}

	// 出力ファイル情報のシーケンスキーを取得
	public Long getSeqKey() {
		try {
			return dataAccess.queryForObject(getSqlMapName() + ".getSeqKey", null);
		} catch (DataNotFoundException e) {
			final String errMsg = "出力ファイル情報のシーケンスキー取得失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}
}
