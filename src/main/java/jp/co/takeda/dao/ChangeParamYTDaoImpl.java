package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.div.Term;

/**
 * 変換パラメータ(Y→T価)にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("changeParamYTDao")
public class ChangeParamYTDaoImpl extends AbstractDao implements ChangeParamYTDao {

	private static final String SQL_MAP = "DPS_I_CHANGE_PARAM_YT_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 変換パラメータ(Y→T価)を取得
	public ChangeParamYT search(Long seqKey) throws DataNotFoundException {
		return (ChangeParamYT) super.selectBySeqKey(seqKey);
	}

	// 変換パラメータ(Y→T価)を登録
	public void insert(ChangeParamYT record) throws DuplicateException {
		super.insert(record);
	}

	// 変換パラメータ(Y→T価)を更新
	public int update(ChangeParamYT record) {
		return super.update(record);
	}

	// 変換パラメータ(Y→T価)の最終更新日時を取得
	@Override
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

	// 変換パラメータ(S/B価)の最終更新日時を取得
	@Override
	public Date getLastUpDateSb(String calYear, Term calTerm) {
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
		paramMap.put("isSy", false);
		// ----------------------
		// 検索実行
		// ----------------------
		Date result = null;
		try {
			result = dataAccess.queryForObject(getSqlMapName() + ".getLastUpDateSySb", paramMap);
		} catch (DataNotFoundException e) {
		}
		return result;
	}

	// 変換パラメータ(S/Y価)の最終更新日時を取得
	@Override
	public Date getLastUpDateSy(String calYear, Term calTerm) {

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
		paramMap.put("isSy", true);
		// ----------------------
		// 検索実行
		// ----------------------
		Date result = null;
		try {
			result = dataAccess.queryForObject(getSqlMapName() + ".getLastUpDateSySb", paramMap);
		} catch (DataNotFoundException e) {
		}
		return result;
	}
}
