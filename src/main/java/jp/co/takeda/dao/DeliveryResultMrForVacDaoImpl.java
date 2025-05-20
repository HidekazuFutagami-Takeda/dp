package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.DeliveryResultForVacSosSummaryDto;
import jp.co.takeda.model.DeliveryResultMrForVac;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;

import org.springframework.stereotype.Repository;

/**
 * ワクチン用担当者別納入実績にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("deliveryResultMrForVacDao")
public class DeliveryResultMrForVacDaoImpl extends AbstractDao implements DeliveryResultMrForVacDao {

	private static final String SQL_MAP = "DPS_V_DELIVERY_RESULT_MR_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 従業員番号、品目固定コードを指定して納入実績を取得
	public DeliveryResultMrForVac search(Integer jgiNo, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		DeliveryResultMrForVac record = new DeliveryResultMrForVac();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		return (DeliveryResultMrForVac) super.selectByUniqueKey(record);
	}

	// 品目指定 担当者一覧取得
	public List<DeliveryResultMrForVac> searchByProd(String sortString, String prodCode, String sosCd3) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createParam(sortString, prodCode, sosCd3);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByProd", paramMap);
	}

	// 組織指定 担当者一覧取得
	public List<DeliveryResultMrForVac> searchBySos(String sortString, String sosCd3) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createParam(sortString, null, sosCd3);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySos", paramMap);
	}

	// 組織サマリ取得
	public DeliveryResultForVacSosSummaryDto searchSosSummary(String prodCode, String sosCd3) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd3 == null) {
			final String errMsg = "組織コード(エリア特約店G)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createParam(null, prodCode, sosCd3);

		// ----------------------
		// 検索実行
		// ----------------------
		Map<String, Object> resultMap = dataAccess.queryForObject(getSqlMapName() + ".selectSummary", paramMap);
		MonNnu _monNnu = (MonNnu) resultMap.get("monNnu");
		return new DeliveryResultForVacSosSummaryDto(sosCd3, prodCode, _monNnu);
	}

	/**
	 * 検索条件を生成する。
	 * 
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param insType 施設出力対象区分(NULL可：UHP合計)
	 * @param zatuFlg 雑担当フラグ(雑担当を含む場合、true
	 * @return 検索条件
	 */
	private Map<String, Object> createParam(String sortString, String prodCode, String sosCd3) {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		// 固定条件		
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.WAKUTIN_MR });
//		paramMap.put("jgi", JgiKb.JGI);
//		paramMap.put("contractMR", JgiKb.CONTRACT_MR);
//		paramMap.put("AL", Shokusei.AL);
//		paramMap.put("NONE", Shokusei.NONE);
//		paramMap.put("WAKUTIN", Shokushu.WAKUTIN);
		return paramMap;
	}
}
