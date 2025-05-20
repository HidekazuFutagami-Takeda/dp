package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.MrPlanSosSummaryDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.model.PlannedProd;

/**
 * 担当者別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mrPlanDao")
public class MrPlanDaoImpl extends AbstractDao implements MrPlanDao {

	private static final String SQL_MAP = "DPS_I_MR_PLAN_SqlMap";

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

	// シーケンスキーを元に担当者別計画を取得
	public MrPlan search(Long seqKey) throws DataNotFoundException {
		return (MrPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキーを元に担当者別計画を取得
	public MrPlan searchUk(Integer jgiNo, String prodCode) throws DataNotFoundException {
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
		MrPlan record = new MrPlan();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		return (MrPlan) super.selectByUniqueKey(record);
	}

	// 組織コード(営業所)を元に担当者別計画のリストを取得
	public List<MrPlan> searchBySosCd(String sortString, String sosCd3, String prodCode, String category) throws DataNotFoundException {

		return searchBySosCd(sortString, sosCd3, null, prodCode, category, false);
	}

	// 組織コード(チーム)を元に担当者別計画のリストを取得
	public List<MrPlan> searchByTeamCd(String sortString, String sosCd4, String prodCode, String category) throws DataNotFoundException {
		return searchBySosCd(sortString, null, sosCd4, prodCode, category, false);
	}

	// 組織コード(営業所)を元に帳票用担当者別計画のリストを取得
	public List<MrPlan> searchBySosCdReport(String sortString, String sosCd3, String prodCode, String category) throws DataNotFoundException {
		return searchBySosCd(sortString, sosCd3, null, prodCode, category, true);
	}
	// 組織コード(チーム)を元に担当者別計画のリストを取得
	public List<MrPlan> searchByTeamCdReport(String sortString, String sosCd4, String prodCode, String category) throws DataNotFoundException {
		return searchBySosCd(sortString, null, sosCd4, prodCode, category, true);
	}
	/**
	 * 担当者別計画のリストを取得する。
	 *
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @param category カテゴリ
	 * @param reportFlg 帳票出力時フラグ
	 * @return 担当者別計画のリスト
	 * @throws DataNotFoundException
	 */
	private List<MrPlan> searchBySosCd(String sortString, String sosCd3, String sosCd4, String prodCode, String category, boolean reportFlg) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		paramMap.put("category", category);

		if(reportFlg){
			paramMap.put("report", "TRUE");
		}

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySosCd", paramMap);
	}

	// 組織コード(営業所)を元に担当者別計画の最終更新情報を取得
	public MrPlan getLastUpDateBySos(String sosCd3, String prodCode) throws DataNotFoundException {
		return getLastUpDate(sosCd3, null, prodCode);
	}

	// 組織コード(チーム)を元に担当者別計画の最終更新情報を取得
	public MrPlan getLastUpDateByTeam(String sosCd4, String prodCode) throws DataNotFoundException {
		return getLastUpDate(null, sosCd4, prodCode);
	}

	/**
	 * 更新日時が最新の担当者別計画を取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画
	 * @throws DataNotFoundException
	 */
	private MrPlan getLastUpDate(String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null) {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".getLastUpDate", paramMap);
	}

	// 組織コード(営業所)を元に担当者別計画の最終更新情報を取得
	public MrPlanSosSummaryDto searchSosSummary(String prodCode, String sosCd3) throws DataNotFoundException {
		return searchSummary(prodCode, sosCd3, null, false);
	}

	// 組織コード(営業所)を元に帳票用担当者別計画の最終更新情報を取得
	public MrPlanSosSummaryDto searchSosSummaryReport(String prodCode, String sosCd3) throws DataNotFoundException {
		return searchSummary(prodCode, sosCd3, null, true);
	}

	// 組織コード(チーム)を元に担当者別計画の最終更新情報を取得
	public MrPlanSosSummaryDto searchTeamSummary(String prodCode, String sosCd4) throws DataNotFoundException {
		return searchSummary(prodCode, null, sosCd4, false);
	}

	/**
	 * 担当者別計画組織単位のサマリーを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param reportFlg 帳票用フラグ
	 * @return 担当者別計画組織単位のサマリー
	 * @throws DataNotFoundException
	 */
	private MrPlanSosSummaryDto searchSummary(String prodCode, String sosCd3, String sosCd4, boolean reportFlg) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null) {
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
		String categoryStr = "";
		//集計品目の場合、チェックしない
		if(prodCode.length() == 5) {
			try {
				plannedProd = plannedProdDAO.search(prodCode);
				categoryStr = plannedProd.getCategory();
			} catch (DataNotFoundException e) {
				final String errMsg = "指定された品目が存在しない：" + prodCode;
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		if(reportFlg){
			paramMap.put("report", "TRUE");
		}

		//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
		//集計品目の場合、カテゴリを条件としない
		if(prodCode.length() == 5) {
			paramMap.put("category", categoryStr);
		}
		//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）

		// ----------------------
		// 検索実行
		// ----------------------
		MrPlan mrPlan = dataAccess.queryForObject(getSqlMapName() + ".selectSummary", paramMap);
		String sosCd = (String) ObjectUtils.defaultIfNull(sosCd3, sosCd4);
		return new MrPlanSosSummaryDto(sosCd, mrPlan);
	}

	// シーケンスキーをもとに、担当者別計画(UH/P)を更新
	public int update(MrPlan record, InsType insType) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "更新情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 更新対象
		String sqlId;
		if (insType == null) {
			sqlId = ".update";
		} else {
			switch (insType) {
				case UH:
					sqlId = ".updateUh";
					break;
				case P:
					sqlId = ".updateP";
					break;
				default:
					throw new SystemException(new Conveyance(PARAMETER_ERROR, "施設出力対象区分が不正"));
			}
		}

		// ----------------------
		// 更新実行
		// ----------------------
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		int count = dataAccess.execute(getSqlMapName() + sqlId, record);
		if (count == 0) {
			optimisticLock();
		}
		return count;
	}

	// 試算結果で担当者別計画を更新
	public void executeByEst(MrPlan record, DpUser dpUser) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "担当者別計画情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 更新・登録実行
		// ----------------------
		// 更新者情報を付与
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());

		// 更新または、登録を行う。(楽観チェックなし)
		try {
			MrPlan oldMrPlan = searchUk(record.getJgiNo(), record.getProdCode());
			record.setSeqKey(oldMrPlan.getSeqKey());
			dataAccess.execute(getSqlMapName() + ".updateByEst", record);
		} catch (DataNotFoundException e) {
			// 登録者情報を付与
			record.setIsJgiNo(dpUser.getJgiNo());
			record.setIsJgiName(dpUser.getJgiName());
			dataAccess.execute(getSqlMapName() + ".insertByEst", record);
		}
	}

	// 配分結果の積上げで担当者別計画を更新
	public void executeByDist(MrPlan record, InsType insType, DpUser dpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "担当者別計画情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 更新・登録実行
		// ----------------------
		// 更新者情報を付与
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());

		// 更新対象
		String sqlUpId;
		String sqlInsId;
		switch (insType) {
			case UH:
				sqlUpId = ".updateUh";
				sqlInsId = ".insertByDistUh";
				break;
			case P:
				sqlUpId = ".updateP";
				sqlInsId = ".insertByDistP";
				break;
			default:
				throw new SystemException(new Conveyance(PARAMETER_ERROR, "施設出力対象区分が不正"));
		}
		// 更新または、登録を行う。(楽観チェックなし)
		try {
			MrPlan oldMrPlan = searchUk(record.getJgiNo(), record.getProdCode());
			record.setSeqKey(oldMrPlan.getSeqKey());
			record.setUpDate(null);
			dataAccess.execute(getSqlMapName() + sqlUpId, record);
		} catch (DataNotFoundException e) {
			// 登録者情報を付与
			record.setIsJgiNo(dpUser.getJgiNo());
			record.setIsJgiName(dpUser.getJgiName());
			dataAccess.execute(getSqlMapName() + sqlInsId, record);
		}
	}

	// 配分時の担当者別計画を初期化
	public int updateByDist(String sosCd3, String prodCode, InsType insType, DpUser dpUser) {

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
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);
		// 更新者情報を付与
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());
		paramMap.put("insType", insType);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".updateByDistSos", paramMap);
	}

	public List<MrPlan> searchJgiNoProdList(List<JgiMst> jgiMst, List<String> prodList ) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiMst == null) {
			final String errMsg = "従業員一覧がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (prodList == null) {
			final String errMsg = "品目固定コード一覧がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("jgiMst", jgiMst);
		paramMap.put("prodList", prodList);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectJgiNoProdList", paramMap);
	}
}
