package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsKakuteiErrMsg;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.InsDocPlanStatSum;
import jp.co.takeda.model.view.InsWsPlanStatSum;
import jp.co.takeda.security.DpUser;

/**
 * 施設特約店別計画立案ステータスにアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insWsPlanStatusDao")
public class InsWsPlanStatusDaoImpl extends AbstractDao implements InsWsPlanStatusDao {

	private static final String SQL_MAP = "DPS_I_INS_WS_PLAN_STATUS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * 計画品目ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 汎用マスタ検索DAP
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	// 施設特約店別計画立案ステータスを取得
	public InsWsPlanStatus search(Long seqKey) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (seqKey == null) {
			final String errMsg = "シーケンスキーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("seqKey", seqKey);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 従業員番号、品目固定コードを元に、施設特約店別計画立案ステータスを取得
	public InsWsPlanStatus search(Integer jgiNo, String prodCode) throws DataNotFoundException {
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
		InsWsPlanStatus record = new InsWsPlanStatus();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		return (InsWsPlanStatus) super.selectByUniqueKey(record);
	}

	// 組織コード、品目を元に、施設特約店別計画立案ステータスを取得
	public List<InsWsPlanStatus> searchList(String sortString, String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null && sosCd4 == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectListBySosCd", paramMap);
	}

	// 組織コード、品目固定コードを元に、施設特約店別計画立案ステータスを取得
	public List<InsWsPlanStatus> searchJgiBaseList(String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null && sosCd4 == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

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

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		paramMap.put("category", plannedProd.getCategory());
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
		if (dpsCodeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd().equals(plannedProd.getCategory())) {
			paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.WAKUTIN_MR });
		} else {
			paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		}

		// 固定条件
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);


		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectJgiBaseList", paramMap);
	}

	// 指定サーバ区分に該当する配分中の施設特約店別計画立案ステータスリストを取得
	public List<InsWsPlanStatus> searchDistingList(String sortString, String appServerKbn) throws DataNotFoundException {
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

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectDistingListByServerKbn", paramMap);
	}

	// 検索条件を基に、品目単位の施設特約店別計画立案ステータスサマリーを取得
	public List<ProdInsWsPlanStatSummaryDto> searchProdStatList(String sosCd3, String category) throws DataNotFoundException {
		return searchProdStatList(sosCd3, null, null, category);
	}

	// 検索条件を基に、品目単位の施設特約店別計画立案ステータスサマリーを取得
	public List<ProdInsWsPlanStatSummaryDto> searchProdStatListByTeam(String sosCd4, String category) throws DataNotFoundException {
		return searchProdStatList(null, sosCd4, null, category);
	}

	// 検索条件を基に、品目単位の施設特約店別計画立案ステータスサマリーを取得
	public List<ProdInsWsPlanStatSummaryDto> searchProdStatListByJgi(Integer jgiNo, String category) throws DataNotFoundException {
		return searchProdStatList(null, null, jgiNo, category);
	}

	/**
	 * 品目単位の施設特約店別計画ステータスサマリーリストを取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param category カテゴリ
	 * @return 品目単位の施設特約店別計画ステータスサマリーリスト
	 * @throws DataNotFoundException
	 */
	protected List<ProdInsWsPlanStatSummaryDto> searchProdStatList(String sosCd3, String sosCd4, Integer jgiNo, String category) throws DataNotFoundException {
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
		paramMap.put("category", category);
		if (dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd().equals(category)) {
			paramMap.put("siire", true);
		}

		// 固定条件
		paramMap.put("planTargetFlg", true);
		if (dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue()).get(0).getDataCd().equals(category)) {
			paramMap.put("sales", Sales.VACCHIN);
			paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.WAKUTIN_MR });
		} else {
			paramMap.put("sales", Sales.IYAKU);
			paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		}
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);

		// DTOに変換
		List<Map<String, Object>> resultMap = dataAccess.queryForList(getSqlMapName() + ".selectProdStat", paramMap);
		List<ProdInsWsPlanStatSummaryDto> resultList = new ArrayList<ProdInsWsPlanStatSummaryDto>();
		for (Map<String, Object> result : resultMap) {
			String prodCode = (String) result.get("prodCode");
			String prodName = (String) result.get("prodName");
			Date lastUpdate = (Date) result.get("lastUpdate");
			InsWsPlanStatSum insWsPlanStatSum = (InsWsPlanStatSum) result.get("insWsPlanStatSum");
			InsDocPlanStatSum insDocPlanStatSum = (InsDocPlanStatSum) result.get("insDocPlanStatSum");
			resultList.add(new ProdInsWsPlanStatSummaryDto(prodCode, prodName,insDocPlanStatSum, insWsPlanStatSum, lastUpdate));
		}
		return resultList;
	}

	// 最終更新日時を取得
	public Date getLastUpDate(String sosCd3, String sosCd4, String prodCode) {
		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null && sosCd4 == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
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


//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	//
	public List<DpsKakuteiErrMsg> delInsCheck(List<InsWsPlanStatus> insWsPlanStatusList){
		List<DpsKakuteiErrMsg> delInsCheckList = new ArrayList<DpsKakuteiErrMsg>();
		for(InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {

			// ----------------------
			// 検索条件生成
			// ----------------------
			Map<String, Object> paramMap = new HashMap<String, Object>(2);
			paramMap.put("jgiNo", insWsPlanStatus.getJgiNo());
			paramMap.put("prodCode", insWsPlanStatus.getProdCode());
			try {
				List<DpsKakuteiErrMsg> kakuteiErr = dataAccess.queryForList(getSqlMapName() + ".selectDelInc", paramMap);
				delInsCheckList.addAll(kakuteiErr);
			} catch (DataNotFoundException e) {
			}
		}
		return delInsCheckList;
	}

	public List<DpsKakuteiErrMsg> wsExceptPlanCheck(List<InsWsPlanStatus> insWsPlanStatusList){
		List<DpsKakuteiErrMsg> wsExceptPlanCheckList = new ArrayList<DpsKakuteiErrMsg>();
		for(InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {

			// ----------------------
			// 検索条件生成
			// ----------------------
			Map<String, Object> paramMap = new HashMap<String, Object>(2);
			paramMap.put("jgiNo", insWsPlanStatus.getJgiNo());
			paramMap.put("prodCode", insWsPlanStatus.getProdCode());
			try {
				List<DpsKakuteiErrMsg> kakuteiErr = dataAccess.queryForList(getSqlMapName() + ".selectExceptPlan", paramMap);
				wsExceptPlanCheckList.addAll(kakuteiErr);
			} catch (DataNotFoundException e) {
			}
		}
		return wsExceptPlanCheckList;
	}

	public List<DpsKakuteiErrMsg> exceptDistIncCheck(List<InsWsPlanStatus> insWsPlanStatusList){
		List<DpsKakuteiErrMsg> exceptDistIncCheckList = new ArrayList<DpsKakuteiErrMsg>();
		for(InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {

			// ----------------------
			// 検索条件生成
			// ----------------------
			Map<String, Object> paramMap = new HashMap<String, Object>(2);
			paramMap.put("jgiNo", insWsPlanStatus.getJgiNo());
			paramMap.put("prodCode", insWsPlanStatus.getProdCode());
			try {
				List<DpsKakuteiErrMsg> kakuteiErr = dataAccess.queryForList(getSqlMapName() + ".selectExceptDistInc", paramMap);
				exceptDistIncCheckList.addAll(kakuteiErr);
			} catch (DataNotFoundException e) {
			}
		}
		return exceptDistIncCheckList;
	}

//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	// 引数で指定された最終更新日と、現在の最終更新日を比較
	public void checkLastUpDate(String sosCd3, String sosCd4, String prodCode, Date orgLastUpdate) {
		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null && sosCd4 == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
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
			InsWsPlanStatus status = search(jgiNo, prodCode);
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

	// 施設特約店別計画立案ステータスを登録
	public void insert(InsWsPlanStatus record) throws DuplicateException {
		super.insert(record);
	}

	// 施設特約店別計画立案ステータスを更新
	public int update(InsWsPlanStatus record) {
		return super.update(record);
	}

	// 施設特約店別計画立案ステータスを更新(更新者情報を指定した実行者情報で更新)
	public int update(InsWsPlanStatus record, DpUser dpUser) {
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

	// 施設特約店別計画立案ステータスを削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
