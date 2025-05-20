package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.DistributionProd;

/**
 * 配分対象品目一覧を取得するDAO実装クラス
 *
 * @author nozaki
 */
@Repository("distributionProdDao")
public class DistributionProdDaoImpl extends AbstractDao implements DistributionProdDao {

	private static final String SQL_MAP = "VIEW_DistributionProd_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 売上所属、カテゴリ、営業所組織コードから、施設医師別計画 配分対象品目一覧を取得
	public List<Map<String, Object>> searchInsDocProdList(Sales sales, ProdCategory category, String sosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "品目カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd", sosCd);
		paramMap.put("category", category);
		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", sales);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		return dataAccess.queryForList(getSqlMapName() + ".selectInsDocList", paramMap);
	}

	// 売上所属、カテゴリ、営業所組織コードから、施設特約店別計画 配分対象品目一覧を取得
	public List<DistributionProd> searchInsWsProdList(Sales sales, String category, String sosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "品目カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		if (sosCd == null) {
			paramMap.put("sosCd", SosMst.SOS_CD1);
		}else {
			paramMap.put("sosCd", sosCd);
		}
		paramMap.put("category", category);
		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", sales);
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		if (sosCd == null) {
			return dataAccess.queryForList(getSqlMapName() + ".selectInsAllWsList", paramMap);
		}else {
			return dataAccess.queryForList(getSqlMapName() + ".selectInsWsList", paramMap);
		}
	}
}
