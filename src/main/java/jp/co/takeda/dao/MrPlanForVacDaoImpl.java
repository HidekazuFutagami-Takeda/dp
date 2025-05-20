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
import jp.co.takeda.dto.MrPlanForVacSosSummaryDto;
import jp.co.takeda.model.MrPlanForVac;

import org.springframework.stereotype.Repository;

/**
 * 施設特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mrPlanForVacDao")
public class MrPlanForVacDaoImpl extends AbstractDao implements MrPlanForVacDao {

	private static final String SQL_MAP = "DPS_V_MR_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ワクチン用担当者別計画を取得
	public MrPlanForVac search(Long seqKey) throws DataNotFoundException {
		return (MrPlanForVac) super.selectBySeqKey(seqKey);
	}

	// ワクチン用担当者別計画を取得
	public MrPlanForVac searchUk(Integer jgiNo, String prodCode) throws DataNotFoundException {

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
		MrPlanForVac record = new MrPlanForVac();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		return (MrPlanForVac) super.selectByUniqueKey(record);
	}

	// ワクチン用担当者別計画リストを取得
	public List<MrPlanForVac> searchList(String sortString, Integer jgiNo, String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 対象品目・営業所の担当者別計画サマリーを取得
	public MrPlanForVacSosSummaryDto searchSosSummary(String prodCode, String sosCd3, String sosCd4) throws DataNotFoundException {
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
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);

		// ----------------------
		// 検索実行
		// ----------------------
		MrPlanForVac mrPlan = dataAccess.queryForObject(getSqlMapName() + ".selectSummary", paramMap);
		return new MrPlanForVacSosSummaryDto(sosCd3, mrPlan);
	}

	// ワクチン用担当者別計画を登録
	public void insert(MrPlanForVac record) throws DuplicateException {
		super.insert(record);
	}

	// ワクチン用担当者別計画を更新
	public int update(MrPlanForVac record) {
		return super.update(record);
	}

	// ワクチン用担当者別計画を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
