package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.StatusForInsDocPlan;
import jp.co.takeda.security.DpUser;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

/**
 * 施設医師別計画立案ステータスにアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insDocPlanStatusDao")
public class InsDocPlanStatusDaoImpl extends AbstractDao implements
		InsDocPlanStatusDao {

	private static final String SQL_MAP = "DPS_I_INS_DOC_PLAN_STATUS_SqlMap";

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

	// 施設医師別計画立案ステータスを取得
	public InsDocPlanStatus search(Long seqKey) throws DataNotFoundException {
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

	// 従業員番号、品目固定コードを元に、施設医師別計画立案ステータスを取得
	public InsDocPlanStatus search(Integer jgiNo, String prodCode)
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo",jgiNo);
		paramMap.put("prodCode",prodCode);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectUk", paramMap);
	}

	// 担当者一覧のリストを取得
	public List<Map<String, Object>> searchJgiBaseList(String sosCd3,
			String sosCd4, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(sosCd3) && StringUtils.isEmpty(sosCd4)) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[] { JokenSet.IYAKU_MR });
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		return dataAccess.queryForList(getSqlMapName() + ".selectJgiBaseList",
				paramMap);

	}

	// 品目一覧のリストを取得
	public List<Map<String, Object>> searchProdBaseList(String sosCd3,
			String sosCd4, Integer jgiNo, ProdCategory category)
			throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(sosCd3) && StringUtils.isEmpty(sosCd4)
				&& jgiNo == null) {
			final String errMsg = "組織コードまたは従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("category", category);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);

		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.IYAKU);
		// 条件セット
		paramMap.put("jokenSetList", new JokenSet[] { JokenSet.IYAKU_MR });
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		// ONC品判定はカテゴリで行うので削除
		// if(BooleanUtils.isTrue(isOncSos)){
		// paramMap.put("mrCat", MrCat.ONC_MR);
		// }
		return dataAccess.queryForList(getSqlMapName() + ".selectProdBaseList", paramMap);

	}

	// 組織コード、品目を元に、施設医師別計画立案ステータスを取得
	public List<InsDocPlanStatus> searchList(String sortString, String sosCd3,
			String sosCd4, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectListBySosCd", paramMap);
	}

	// 引数で指定された最終更新日と、現在の最終更新日を比較
	public void checkLastUpDate(Integer jgiNo, String prodCode,
			Date orgLastUpdate) throws OptimisticLockingFailureException {
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
			InsDocPlanStatus status = search(jgiNo, prodCode);
			lastUpdate = status.getUpDate();
		} catch (DataNotFoundException e) {
		}

		// 前回の最終更新日と比較
		if (orgLastUpdate == null && lastUpdate == null) {
			return;
		}
		if (orgLastUpdate != null && !orgLastUpdate.equals(lastUpdate)) {
			final String errMsg = "：施設医師別計画立案ステータスの最終更新日が更新されているため、楽観的ロックエラーとする";
			throw new OptimisticLockingFailureException(errMsg);
		}
	}

	// 引数で指定された最終更新日と、現在の最終更新日を比較
	public void checkLastUpDate(String sosCd3, String sosCd4, Integer jgiNo,
			String prodCode, Date orgLastUpdate)
			throws OptimisticLockingFailureException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "組織コードまたは従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 現在の最終更新日を取得
		Date lastUpdate = getLastUpDate(sosCd3, sosCd4, jgiNo, prodCode);

		// 前回の最終更新日と比較
		if (orgLastUpdate == null && lastUpdate == null) {
			return;
		}
		if (orgLastUpdate != null && !orgLastUpdate.equals(lastUpdate)) {
			final String errMsg = "：施設医師別計画立案ステータスの最終更新日が更新されているため、楽観的ロックエラーとする";
			throw new OptimisticLockingFailureException(errMsg);
		}
	}

	// 最終更新日時を取得
	public Date getLastUpDate(String sosCd3, String sosCd4, Integer jgiNo,
			String prodCode) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "組織コードまたは従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);

		// -------------
		// 検索実行
		// -------------
		Date result = null;
		try {
			result = dataAccess.queryForObject(getSqlMapName()
					+ ".getLastUpDate", paramMap);
		} catch (DataNotFoundException e) {

		}
		return result;
	}

	// 施設医師別計画立案ステータスを更新
	public int update(InsDocPlanStatus record) {
		return super.update(record);
	}

	// -------------------------------------------------------------
	// バッチ配分系ステータス更新
	// -------------------------------------------------------------

	// 配分開始ステータス登録・更新
	public int haibunStart(String sosCd3, Integer jgiNo, ProdCategory category,
			String appServerKbn, Date distStartDate, boolean plannedClearFlg,
			boolean openFlg, DpUser dpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "組織コードまたは従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (appServerKbn == null) {
			final String errMsg = "サーバ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distStartDate == null) {
			final String errMsg = "配分開始時刻がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("isJgiNo", dpUser.getJgiNo());
		paramMap.put("isJgiName", dpUser.getJgiName());
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());

		// 固定条件
		paramMap.put("statusForInsDocPlan", StatusForInsDocPlan.DISTING);
		paramMap.put("appServerKbn", appServerKbn);
		paramMap.put("distStartDate", distStartDate);
		paramMap.put("plannedClearFlg", plannedClearFlg);
		paramMap.put("openFlg", openFlg);
		paramMap.put("category", category);
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.IYAKU);
		// 条件セット
		paramMap.put("jokenSetList", new JokenSet[] { JokenSet.IYAKU_MR });
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);

		return super.dataAccess.execute(getSqlMapName() + ".haibunStart",
				paramMap);
	}

	// 配分終了ステータス更新
	public int haibunEnd(Integer jgiNo,
			StatusForInsDocPlan statusForInsDocPlan, Date distEndDate,
			DpUser dpUser) {

		// MEMO:2014年下期向け改定より、完了時に「確定」にする

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// if (statusForInsDocPlan == null) {
		// final String errMsg = "更新ステータスがnull";
		// throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		// }
		if (distEndDate == null) {
			final String errMsg = "配分完了日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("statusForInsDocPlan", statusForInsDocPlan);
		paramMap.put("distEndDate", distEndDate);
		paramMap.put("insDocOpenDate", distEndDate);
		paramMap.put("insDocFixDate", distEndDate);
		// if(statusForInsDocPlan == StatusForInsDocPlan.PLAN_OPENED){
		// paramMap.put("insDocOpenDate", distEndDate);
		// }
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());
		// paramMap.put("beforeStatusForInsDocPlan",
		// StatusForInsDocPlan.DISTING);
		return super.dataAccess.execute(getSqlMapName() + ".haibunEnd",
				paramMap);
	}

	// 配分ステータス戻し（削除）
	public int haibunRecoverDelete(Integer jgiNo) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		// paramMap.put("statusForInsDocPlan", StatusForInsDocPlan.DISTING);
		return super.dataAccess.execute(getSqlMapName()
				+ ".haibunRecoverDelete", paramMap);
	}

	// 配分ステータス戻し（更新）
	public int haibunRecoverUpdate(Integer jgiNo, DpUser dpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());
		// paramMap.put("statusForInsDocPlan", StatusForInsDocPlan.DISTING);
		return super.dataAccess.execute(getSqlMapName()
				+ ".haibunRecoverUpdate", paramMap);
	}

	// 指定サーバ区分に該当する配分中の施設医師別計画立案ステータスリストを取得
	public List<Map<String, Object>> searchDistingList(String appServerKbn)
			throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("appServerKbn", appServerKbn);
		paramMap.put("statusForInsDocPlan", StatusForInsDocPlan.DISTING);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName()
				+ ".selectDistingListByServerKbn", paramMap);
	}
}
