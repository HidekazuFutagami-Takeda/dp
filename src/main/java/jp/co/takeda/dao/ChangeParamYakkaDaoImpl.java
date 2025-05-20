package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ChangeParamYakka;
import jp.co.takeda.model.div.Term;

/**
 * 薬価改定指定率にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("changeParamYakkaDao")
public class ChangeParamYakkaDaoImpl extends AbstractDao implements ChangeParamYakkaDao {

	private static final String SQL_MAP = "DPS_I_CHANGE_PARAM_YAKKA_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 薬価改定指定率を取得
	public ChangeParamYakka search(Long seqKey) throws DataNotFoundException {
		return (ChangeParamYakka) super.selectBySeqKey(seqKey);
	}

	// 薬価改定指定率を登録
	public void insert(ChangeParamYakka record) throws DuplicateException {
		super.insert(record);
	}

	// 薬価改定指定率を更新
	public int update(ChangeParamYakka record) {
		return super.update(record);
	}

	// 薬価改定指定率の最終更新日時を取得
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
