package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.InsWsPlanForVacProdSummaryDto;
import jp.co.takeda.model.InsWsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

import org.springframework.stereotype.Repository;

/**
 * 施設特約店別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("insWsPlanForVacDao")
public class InsWsPlanForVacDaoImpl extends AbstractDao implements InsWsPlanForVacDao {

	private static final String SQL_MAP = "DPS_V_INS_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ワクチン用施設特約店別計画を取得
	public InsWsPlanForVac search(Long seqKey) throws DataNotFoundException {
		return (InsWsPlanForVac) super.selectBySeqKey(seqKey);
	}

	// ワクチン用施設特約店別計画のリストを取得
	public List<InsWsPlanForVac> searchList(String sortString, Integer jgiNo, String prodCode, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity)
		throws DataNotFoundException {

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
		Map<String, Object> paramMap = new HashMap<String, Object>(6);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("activityType", activityType);
		paramMap.put("addrCodePref", addrCodePref);
		paramMap.put("addrCodeCity", addrCodeCity);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// ワクチン用施設特約店別計画を登録
	public void insert(InsWsPlanForVac record) throws DuplicateException {
		super.insert(record);
	}

	// ワクチン用施設特約店別計画を更新
	public int update(InsWsPlanForVac record) {
		return super.update(record);
	}

	// ワクチン用施設特約店別計画を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}

	// ワクチン用施設特約店別計画を従業員・品目単位で一括削除
	public int deleteByJgi(Integer jgiNo, String prodCode) {

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 削除実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".deleteByJgi", paramMap);
	}

	// 品目固定コード、組織(または従業員番号)を指定してワクチン用施設特約店別計画のサマリを取得
	public InsWsPlanForVacProdSummaryDto searchProdSummary(String prodCode, String sosCd3, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity)
		throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "組織コード・従業員番号が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(6);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("activityType", activityType);
		paramMap.put("addrCodePref", addrCodePref);
		paramMap.put("addrCodeCity", addrCodeCity);

		InsWsPlanForVac insWsPlan = dataAccess.queryForObject(getSqlMapName() + ".selectProdSummary", paramMap);
		return new InsWsPlanForVacProdSummaryDto(prodCode, insWsPlan);
	}
}
