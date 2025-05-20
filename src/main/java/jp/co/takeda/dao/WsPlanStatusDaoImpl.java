package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.Sales;

/**
 * 特約店別計画立案ステータスにアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("wsPlanStatusDao")
public class WsPlanStatusDaoImpl extends AbstractDao implements WsPlanStatusDao {

	private static final String SQL_MAP = "DPS_I_WS_PLAN_STATUS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 特約店別計画立案ステータスを取得
	public WsPlanStatus search(Long seqKey) throws DataNotFoundException {
		return (WsPlanStatus) super.selectBySeqKey(seqKey);
	}

	// 組織コード、品目固定コードを元に、施設特約店別計画立案ステータスを取得
	public WsPlanStatus search(String sosCd, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		WsPlanStatus record = new WsPlanStatus();
		record.setSosCd(sosCd);
		record.setProdCode(prodCode);
		return (WsPlanStatus) super.selectByUniqueKey(record);
	}

	// 指定サーバ区分に該当する配分中の施設特約店別計画立案ステータスリストを取得
	public List<WsPlanStatus> searchDistingList(String sortString, String appServerKbn) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (appServerKbn == null) {
			final String errMsg = "サーバ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("appServerKbn", appServerKbn);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectDistingListByServerKbn", paramMap);
	}

	// カテゴリを元に、施設特約店別計画立案ステータスを取得
	public List<WsPlanStatus> searchList(String sortString, String prodCategory) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("category", prodCategory);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectListByCategory", paramMap);
	}

	// 組織コード(支店)とカテゴリを元に、施設特約店別計画立案ステータスを取得
	public List<WsPlanStatus> searchListBySosCategory(String sortString, Sales sales, String sosCd, String prodCategory) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
		paramMap.put("category", prodCategory);
		paramMap.put("sales", sales);

		// 固定条件
		paramMap.put("planTargetFlg", true);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectListBySosCategory", paramMap);
	}

	public List<WsPlanStatus> searchList(String sortString, String sosCd, List<String> prodCodeList) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
		paramMap.put("prodCodeList", prodCodeList);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectListBySosProd", paramMap);
	}

	// 特約店別計画立案ステータスを登録
	public void insert(WsPlanStatus record) throws DuplicateException {
		super.insert(record);
	}

	// 特約店別計画立案ステータスを更新
	public int update(WsPlanStatus record) {
		return super.update(record);
	}

	// 特約店別計画立案ステータスを削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
