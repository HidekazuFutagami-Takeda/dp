package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.EstParamHonbu;

import org.springframework.stereotype.Repository;

/**
 * 試算パラメータ(本部)にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("estParamHonbuDao")
public class EstParamHonbuDaoImpl extends AbstractDao implements EstParamHonbuDao {

	private static final String SQL_MAP = "DPS_I_EST_PARAM_HONBU_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 試算パラメータ(本部)を取得
	public EstParamHonbu search(Long seqKey) throws DataNotFoundException {
		return (EstParamHonbu) super.selectBySeqKey(seqKey);
	}

	// 試算パラメータ(本部)を取得
	public EstParamHonbu search(String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		EstParamHonbu record = new EstParamHonbu();
		record.setProdCode(prodCode);
		return (EstParamHonbu) super.selectByUniqueKey(record);
	}
}
