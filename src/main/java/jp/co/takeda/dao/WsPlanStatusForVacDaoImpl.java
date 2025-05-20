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
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.Sales;

import org.springframework.stereotype.Repository;

/**
 * ワクチン用特約店別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("wsPlanStatusForVacDao")
public class WsPlanStatusForVacDaoImpl extends AbstractDao implements WsPlanStatusForVacDao {

	private static final String SQL_MAP = "DPS_V_WS_PLAN_STATUS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ワクチン用特約店別計画立案ステータスを取得
	public WsPlanStatusForVac search(Long seqKey) throws DataNotFoundException {
		return (WsPlanStatusForVac) super.selectBySeqKey(seqKey);
	}

	// 品目固定コードを元に、ワクチン用施設特約店別計画立案ステータスを取得
	public WsPlanStatusForVac search(String prodCode) throws DataNotFoundException {
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
		WsPlanStatusForVac record = new WsPlanStatusForVac();
		record.setProdCode(prodCode);
		return (WsPlanStatusForVac) super.selectByUniqueKey(record);
	}

	// ワクチン用特約店別計画立案ステータス一覧を取得
	public List<WsPlanStatusForVac> searchList() throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.VACCHIN);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// ワクチン用特約店別計画立案ステータスの全品目サマリを取得
	public WsPlanStatusForVac searchSummary() throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.VACCHIN);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectSummary", paramMap);
	}

	// ワクチン用特約店別計画立案ステータスを登録
	public void insert(WsPlanStatusForVac record) throws DuplicateException {
		super.insert(record);
	}

	// ワクチン用特約店別計画立案ステータスを更新
	public int update(WsPlanStatusForVac record) {
		return super.update(record);
	}

	// ワクチン用特約店別計画立案ステータスを削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
