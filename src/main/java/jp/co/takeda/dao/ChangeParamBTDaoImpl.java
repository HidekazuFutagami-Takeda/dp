package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ChangeParamBT;
import jp.co.takeda.model.div.Term;

import org.springframework.stereotype.Repository;

/**
 * 変換パラメータ(B→T価)にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("changeParamBTDao")
public class ChangeParamBTDaoImpl extends AbstractDao implements ChangeParamBTDao {

	private static final String SQL_MAP = "DPS_V_CHANGE_PARAM_BT_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 変換パラメータ(B→T価)を取得
	public ChangeParamBT search(Long seqKey) throws DataNotFoundException {
		return (ChangeParamBT) super.selectBySeqKey(seqKey);
	}

	// 変換パラメータ(B→T価)を登録
	public void insert(ChangeParamBT record) throws DuplicateException {
		super.insert(record);
	}

	// 変換パラメータ(B→T価)を更新
	public int update(ChangeParamBT record) {
		return super.update(record);
	}

	// 変換パラメータ(B→T価)を更新
	public Date getLastUpDate(String calYear, Term calTerm) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (calYear == null) {
			final String errMsg = "年度がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (calTerm == null) {
			final String errMsg = "期がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("calYear", calYear);
		paramMap.put("calTerm", calTerm);

		// ----------------------
		// 検索実行
		// ----------------------
		Date result = null;
		try {
			result = dataAccess.queryForObject(getSqlMapName() + ".getLastUpDate", paramMap);
		} catch (DataNotFoundException e) {
		}
		return result;
	}
}
