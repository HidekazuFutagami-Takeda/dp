package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ChoseiOffice;
import jp.co.takeda.model.div.InsType;

import org.springframework.stereotype.Repository;

/**
 * 調整額（営業所調整）にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("choseiOfficeDao")
public class ChoseiOfficeDaoImpl extends AbstractManageDao implements ChoseiOfficeDao {

	private static final String SQL_MAP = "DPM_I_CHOSEI_OFFICE_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ユニークキー検索
	public ChoseiOffice searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException {

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
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ChoseiOffice record = new ChoseiOffice();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		record.setSosCd(sosCd);
		return super.selectByUniqueKey(record);
	}

}
