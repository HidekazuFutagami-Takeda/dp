package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.DistParam;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.service.DpsCodeMasterSearchService;

/**
 * 配分基準(営業所)にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("distParamOfficeDao")
public class DistParamOfficeDaoImpl extends AbstractDao implements DistParamOfficeDao {

	private static final String SQL_MAP = "DPS_I_DIST_PARAM_OFFICE_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// 配分基準(営業所)を取得
	public DistParamOffice search(Long seqKey) throws DataNotFoundException {
		return (DistParamOffice) super.selectBySeqKey(seqKey);
	}

	// 配分基準(営業所)を取得
	public DistParamOffice search(String sosCd, String prodCode, InsType insType, DistributionType distributionType) throws DataNotFoundException {

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
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distributionType == null) {
			final String errMsg = "配分先区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		DistParamOffice record = new DistParamOffice();
		record.setSosCd(sosCd);
		record.setProdCode(prodCode);
		record.setInsType(insType);
		DistParam distParam = new DistParam();
		distParam.setDistributionType(distributionType);
		record.setDistParam(distParam);
		return (DistParamOffice) super.selectByUniqueKey(record);
	}

	// 施設特約店配分用の配分基準(営業所)リストを取得
	public List<DistParamOffice> searchInsWsParamList(String sortString, String sosCd, String prodCode, InsType insType, String category, Sales sales) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
		paramMap.put("prodCode", prodCode);
		paramMap.put("insType", insType);
		paramMap.put("category", category);
		// 固定条件
		paramMap.put("sales", sales);
		paramMap.put("planTargetFlg", true);
		paramMap.put("distributionType", DistributionType.INS_WS_PLAN);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 特約店配分用の配分基準(営業所)リストを取得
	public List<DistParamOffice> searchWsParamList(String sortString, String sosCd, List<String> prodCodeList, InsType insType, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
		paramMap.put("prodCodeList", prodCodeList);
		paramMap.put("insType", insType);
		paramMap.put("category", category);
		// 固定条件
		if (category == null) {
			paramMap.put("sales", Sales.IYAKU);
		}else {
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				paramMap.put("sales", Sales.VACCHIN);
			}else {
				paramMap.put("sales", Sales.IYAKU);
			}
		}
		paramMap.put("planTargetFlg", true);
		paramMap.put("distributionType", DistributionType.WS_PLAN);

		return dataAccess.queryForList(getSqlMapName() + ".selectWsParamList", paramMap);
	}

	// 配分基準(営業所)を登録
	public void insert(DistParamOffice record) throws DuplicateException {
		super.insert(record);
	}

	// 配分基準(営業所)を更新
	public int update(DistParamOffice record) {
		return super.update(record);
	}

	// 配分基準(営業所)を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
