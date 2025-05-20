package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.DeliveryResultForVac;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.Sales;

import org.springframework.stereotype.Repository;

/**
 * ワクチン用納入実績にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("deliveryResultForVacDao")
public class DeliveryResultForVacDaoImpl extends AbstractDao implements DeliveryResultForVacDao {

	private static final String SQL_MAP = "DPS_V_DELIVERY_RESULT_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 納入実績を取得
	public DeliveryResultForVac search(String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("prodCode", prodCode);
		paramMap.put("insNo", insNo);
		paramMap.put("tmsTytenCd", tmsTytenCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 対象品目の納入実績を取得
	public List<DeliveryResultForVac> searchByProd(String sortString, String prodCode, Integer jgiNo, String insNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("insNo", insNo);
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByProd", paramMap);
	}

	// 指定した施設の参照用納入実績を取得(Ａ調含)
	public List<DeliveryResultForVac> searchByInsIncludeA(String sortString, String insNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("insNo", insNo);
		// 固定条件
		paramMap.put("insClass", InsClass.THOUZAI);
		paramMap.put("oldInsrFlg", OldInsrFlg.A_THOUZAI);
		paramMap.put("delFlg", false);
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.VACCHIN);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByInsNo", paramMap);
	}
}
