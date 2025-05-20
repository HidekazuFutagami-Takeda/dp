package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.logic.div.ValueChangeType;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;

import org.springframework.stereotype.Repository;

/**
 * 管理の変換パラメータ(Y→T価)にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("manageChangeParamYTDao")
public class ManageChangeParamYTDaoImpl extends AbstractDao implements ManageChangeParamYTDao {

	private static final String SQL_MAP = "DPM_I_CHANGE_PARAM_YT_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 変換パラメータ(Y→T価)を取得
	public ChangeParamYT search(Long seqKey) throws DataNotFoundException {
		return (ChangeParamYT) super.selectBySeqKey(seqKey);
	}

	// ユニークキーで変換パラメータ(Y→T価)を取得
	public ChangeParamYT searchUk(String calYear, Term calTerm, String prodCode, String tmsTytenCdHonten) throws DataNotFoundException {

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
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ChangeParamYT record = new ChangeParamYT();
		record.setCalYear(calYear);
		record.setCalTerm(calTerm);
		record.setProdCode(prodCode);
		record.setTmsTytenCdHonten(tmsTytenCdHonten);
		return (ChangeParamYT) super.selectByUniqueKey(record);
	}

	// 変換パラメータ(Y→T価)の最終更新日時を取得
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

	// 変換した計画値を取得する
	public Long getChangeValue(String prodCode, Long value, InsType insType, ValueChangeType valueChangeType, String tmsTytenCd) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (valueChangeType == null) {
			final String errMsg = "変換区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(7);
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		paramMap.put("calYear", sysManage.getSysYear());
		paramMap.put("calTerm", sysManage.getSysTerm());
		paramMap.put("prodCode", prodCode);
		paramMap.put("value", value);
		paramMap.put("insType", insType);
		paramMap.put("valueChangeType", valueChangeType.getCode());
		paramMap.put("tmsTytenCd", tmsTytenCd);

		// ----------------------
		// 検索実行
		// ----------------------
		Long result = null;
		try {
			result = dataAccess.queryForObject(getSqlMapName() + ".getChangeValue", paramMap);
		} catch (DataNotFoundException e) {
		}
		return result;
	}
}
