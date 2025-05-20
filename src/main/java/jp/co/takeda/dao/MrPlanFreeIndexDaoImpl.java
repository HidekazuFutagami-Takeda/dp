package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.MrPlanFreeIndex;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import jp.co.takeda.model.PlannedProd;

/**
 * 担当者別計画フリー項目にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mrPlanFreeIndexDao")
public class MrPlanFreeIndexDaoImpl extends AbstractDao implements MrPlanFreeIndexDao {

	private static final String SQL_MAP = "DPS_I_MR_PLAN_FREE_INDEX_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
	/**
	 * 計画品目ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;
	//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）

	// 担当者別計画フリー項目を取得
	public MrPlanFreeIndex search(Long seqKey) throws DataNotFoundException {
		return (MrPlanFreeIndex) super.selectBySeqKey(seqKey);
	}

	// 組織コード、品目固定コードを指定して、担当者別計画フリー項目を取得
	public List<MrPlanFreeIndex> searchListBySosCd(String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
		// ----------------------
		// 品目取得
		// ----------------------
		PlannedProd plannedProd;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "指定された品目が存在しない：" + prodCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);

		// 固定条件
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
		//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
		paramMap.put("category", plannedProd.getCategory());
		//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectListBySosCd", paramMap);
	}

	// 組織コード、品目固定コードを指定して、担当者別計画フリー項目の最終更新情報を取得
	public MrPlanFreeIndex getLastUpDate(String sosCd3, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return (MrPlanFreeIndex) dataAccess.queryForObject(getSqlMapName() + ".selectUpDate", paramMap);
	}

	// 品目コード、組織コードから、フリー項目の集計を取得
	public Map<String, Object> sumFreeIndexByProdSos(String sosCd3, String prodCode)  throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".sumFreeIndexByProdSos", paramMap);
	}

	// 担当者別計画フリー項目を登録
	public void insert(MrPlanFreeIndex record) throws DuplicateException {
		super.insert(record);
	}

	// 担当者別計画フリー項目を更新
	public int update(MrPlanFreeIndex record) {
		return super.update(record);
	}

	// 担当者別計画フリー項目を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
