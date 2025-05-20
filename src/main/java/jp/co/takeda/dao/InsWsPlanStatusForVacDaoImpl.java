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
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.Shokusei;
import jp.co.takeda.model.div.Shokushu;
import jp.co.takeda.model.view.InsWsPlanStatSum;
import jp.co.takeda.security.DpUser;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

/**
 * ワクチン用施設特約店別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("insWsPlanStatusForVacDao")
public class InsWsPlanStatusForVacDaoImpl extends AbstractDao implements InsWsPlanStatusForVacDao {

	private static final String SQL_MAP = "DPS_V_INS_WS_PLAN_STATUS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ワクチン用施設特約店別計画を取得
	public InsWsPlanStatusForVac search(Long seqKey) throws DataNotFoundException {
		return (InsWsPlanStatusForVac) super.selectBySeqKey(seqKey);
	}

	// 従業員番号、品目固定コードを元に、ワクチン用施設特約店別計画立案ステータスを取得
	public InsWsPlanStatusForVac search(Integer jgiNo, String prodCode) throws DataNotFoundException {
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
		InsWsPlanStatusForVac record = new InsWsPlanStatusForVac();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		return (InsWsPlanStatusForVac) super.selectByUniqueKey(record);
	}

	// 従業員番号を元に、ワクチン用施設特約店別計画立案ステータスを取得
	public List<InsWsPlanStatusForVac> searchList(String sortString, Integer jgiNo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 組織コード、品目を元に、ワクチン用施設特約店別計画立案ステータスを取得
	public List<InsWsPlanStatusForVac> searchList(String sortString, String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 品目固定コードを元に、ワクチン用施設特約店別計画立案ステータスを取得
	public List<InsWsPlanStatusForVac> searchJgiBaseList(String prodCode, String sosCd3, String sosCd4) throws DataNotFoundException {

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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);

		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.WAKUTIN_MR });
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		paramMap.put("jgiKbList", jgiKbList);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectJgiBaseList", paramMap);
	}

	// 指定サーバ区分に該当する配分中の施設特約店別計画立案ステータスリストを取得
	public List<InsWsPlanStatusForVac> searchDistingList(String sortString, String appServerKbn) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("sortString", sortString);
		paramMap.put("appServerKbn", appServerKbn);

		// -------------
		// 検索実行
		// -------------
		return dataAccess.queryForList(getSqlMapName() + ".selectDistingListByServerKbn", paramMap);
	}

	// 最終更新日時を取得
	public Date getLastUpDate(String prodCode) {
		return getLastUpDate(null, null, prodCode);
	}

	// 最終更新日時を取得
	public Date getLastUpDate(String sosCd3, String sosCd4, String prodCode) {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);

		// -------------
		// 検索実行
		// -------------
		Date result = null;
		try {
			result = dataAccess.queryForObject(getSqlMapName() + ".getLastUpDate", paramMap);
		} catch (DataNotFoundException e) {

		}
		return result;
	}

	// 引数で指定された最終更新日と、現在の最終更新日を比較
	public void checkLastUpDate(String prodCode, Date orgLastUpdate) {
		checkLastUpDate(null, null, prodCode, orgLastUpdate);
	}

	// 引数で指定された最終更新日と、現在の最終更新日を比較
	public void checkLastUpDate(String sosCd3, String sosCd4, String prodCode, Date orgLastUpdate) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 現在の最終更新日を取得
		Date lastUpdate = getLastUpDate(sosCd3, sosCd4, prodCode);

		// 前回の最終更新日と比較
		if (orgLastUpdate == null && lastUpdate == null) {
			return;
		}
		if (orgLastUpdate != null && !orgLastUpdate.equals(lastUpdate)) {
			final String errMsg = "：施設特約店別計画立案ステータスの最終更新日が更新されているため、楽観的ロックエラーとする";
			throw new OptimisticLockingFailureException(errMsg);
		}
	}

	// 引数で指定された最終更新日と、現在の最終更新日を比較
	public void checkLastUpDate(Integer jgiNo, String prodCode, Date orgLastUpdate) {
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

		Date lastUpdate = null;
		try {
			InsWsPlanStatusForVac status = search(jgiNo, prodCode);
			lastUpdate = status.getUpDate();
		} catch (DataNotFoundException e) {
		}

		// 前回の最終更新日と比較
		if (orgLastUpdate == null && lastUpdate == null) {
			return;
		}
		if (orgLastUpdate != null && !orgLastUpdate.equals(lastUpdate)) {
			final String errMsg = "：施設特約店別計画立案ステータスの最終更新日が更新されているため、楽観的ロックエラーとする";
			throw new OptimisticLockingFailureException(errMsg);
		}
	}

	// 組織コードを指定して、品目別の施設特約店別計画サマリを取得
	public List<ProdInsWsPlanStatSummaryDto> searchProdStatListBySos(String sosCd3) throws DataNotFoundException {
		return searchProdStatList(sosCd3, null, null);
	}

	// 従業員番号を指定して、品目別の施設特約店別計画サマリを取得
	public List<ProdInsWsPlanStatSummaryDto> searchProdStatListByJgi(Integer jgiNo) throws DataNotFoundException {
		return searchProdStatList(null, null, jgiNo);
	}

	/**
	 * 品目単位の施設特約店別計画ステータスサマリーリストを取得する。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @return 品目単位の施設特約店別計画ステータスサマリーリスト
	 * @throws DataNotFoundException
	 */
	protected List<ProdInsWsPlanStatSummaryDto> searchProdStatList(String sosCd3, String sosCd4, Integer jgiNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);

		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.VACCHIN);
//		paramMap.put("AL", Shokusei.AL);
//		paramMap.put("NONE", Shokusei.NONE);
//		paramMap.put("WAKUTIN", Shokushu.WAKUTIN);
//		paramMap.put("jgi", JgiKb.JGI);
//		paramMap.put("contractMR", JgiKb.CONTRACT_MR);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.WAKUTIN_MR });
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		paramMap.put("jgiKbList", jgiKbList);

		// DTOに変換
		List<Map<String, Object>> resultMap = dataAccess.queryForList(getSqlMapName() + ".selectProdStat", paramMap);
		List<ProdInsWsPlanStatSummaryDto> resultList = new ArrayList<ProdInsWsPlanStatSummaryDto>();
		for (Map<String, Object> result : resultMap) {
			String prodCode = (String) result.get("prodCode");
			String prodName = (String) result.get("prodName");
			Date lastUpdate = (Date) result.get("lastUpdate");
			InsWsPlanStatSum insWsPlanStatSum = (InsWsPlanStatSum) result.get("insWsPlanStatSum");
			resultList.add(new ProdInsWsPlanStatSummaryDto(prodCode, prodName, insWsPlanStatSum, lastUpdate));
		}
		return resultList;
	}

	// ワクチン用施設特約店別計画を登録
	public void insert(InsWsPlanStatusForVac record) throws DuplicateException {
		super.insert(record);
	}

	// ワクチン用施設特約店別計画を更新
	public int update(InsWsPlanStatusForVac record) {
		return super.update(record);
	}

	// ワクチン用施設特約店別計画を更新
	public int update(InsWsPlanStatusForVac record, DpUser dpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "更新情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 更新実行
		// ----------------------
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		return dataAccess.execute(getSqlMapName() + ".update", record);
	}

	// ワクチン用施設特約店別計画を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
