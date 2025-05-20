package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.DistParam;
import jp.co.takeda.model.DistParamHonbuForVac;
import jp.co.takeda.model.div.DistributionType;

import org.springframework.stereotype.Repository;

/**
 * ワクチン用配分基準(本部)にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("distParamHonbuForVacDao")
public class DistParamHonbuForVacDaoImpl extends AbstractDao implements DistParamHonbuForVacDao {

	private static final String SQL_MAP = "DPS_V_DIST_PARAM_HONBU_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ワクチン用配分基準(本部)を取得
	public DistParamHonbuForVac search(Long seqKey) throws DataNotFoundException {
		return (DistParamHonbuForVac) super.selectBySeqKey(seqKey);
	}

	// ワクチン用配分基準(本部)を取得
	public DistParamHonbuForVac search(String prodCode) throws DataNotFoundException {

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
		DistParamHonbuForVac record = new DistParamHonbuForVac();
		record.setProdCode(prodCode);
		DistParam distParam = new DistParam();
		distParam.setDistributionType(DistributionType.INS_WS_PLAN);
		record.setDistParam(distParam);
		return (DistParamHonbuForVac) super.selectByUniqueKey(record);
	}

	// ワクチン用配分基準(本部)を登録
	public void insert(DistParamHonbuForVac record) throws DuplicateException {
		super.insert(record);
	}

	// ワクチン用配分基準(本部)を更新
	public int update(DistParamHonbuForVac record) {
		return super.update(record);
	}

	// ワクチン用配分基準(本部)を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}

}
