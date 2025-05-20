package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ChoseiMr;
import jp.co.takeda.model.div.InsType;

import org.springframework.stereotype.Repository;

/**
 * 調整額（担当者調整）にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("choseiMrDao")
public class ChoseiMrDaoImpl extends AbstractManageDao implements ChoseiMrDao {

	private static final String SQL_MAP = "DPM_I_CHOSEI_MR_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ユニークキー検索
	public ChoseiMr searchUk(InsType insType, String prodCode, Integer jgiNo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ChoseiMr record = new ChoseiMr();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		record.setJgiNo(jgiNo);
		return super.selectByUniqueKey(record);
	}

}
