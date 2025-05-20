package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.OfficeTeamPlanChoseiDto;
import jp.co.takeda.dto.TeamPlanSosSummaryDto;
import jp.co.takeda.logic.div.CopyWay;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

import org.springframework.stereotype.Repository;

/**
 * チーム別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("teamPlanDao")
public class TeamPlanDaoImpl extends AbstractDao implements TeamPlanDao {

	private static final String SQL_MAP = "DPS_I_TEAM_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// チーム別計画を取得
	public TeamPlan search(Long seqKey) throws DataNotFoundException {
		return (TeamPlan) super.selectBySeqKey(seqKey);
	}

	// チーム別計画をユニークキーで取得
	public TeamPlan searchUk(String sosCd4, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		TeamPlan record = new TeamPlan();
		record.setSosCd(sosCd4);
		record.setProdCode(prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return (TeamPlan) super.selectByUniqueKey(record);
	}
	
// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	// チーム別計画をユニークキーで取得
	public TeamPlan searchUkReport(String sosCd4, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectUkReport", paramMap);
	}
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

	// 組織コード(営業所)をもとに、チーム別計画を取得
	public List<TeamPlan> searchList(String sortString, String sosCd3, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 組織コード(営業所)をもとに、チーム別計画の最終更新情報を取得
	public TeamPlan getLastUpDate(String sosCd3, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".getLastUpDate", paramMap);
	}

	// 対象品目・営業所のチーム別計画サマリーを取得
	public TeamPlanSosSummaryDto searchSosSummary(String prodCode, String sosCd3) throws DataNotFoundException {
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
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		TeamPlan teamPlan = dataAccess.queryForObject(getSqlMapName() + ".selectSummary", paramMap);
		return new TeamPlanSosSummaryDto(sosCd3, teamPlan);
	}

	public int update(TeamPlan record, InsType insType) {
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

	// シーケンスキーをもとに、チーム別計画(UH/P)を更新
	public void executeByEst(TeamPlan record, DpUser dpUser) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "チーム別計画情報がnull";
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
			TeamPlan oldTeamPlan = searchUk(record.getSosCd(), record.getProdCode());
			record.setSeqKey(oldTeamPlan.getSeqKey());
			dataAccess.execute(getSqlMapName() + ".updateByEst", record);
		} catch (DataNotFoundException e) {
			// 登録者情報を付与
			record.setIsJgiNo(dpUser.getJgiNo());
			record.setIsJgiName(dpUser.getJgiName());
			dataAccess.execute(getSqlMapName() + ".insertByEst", record);
		}
	}

	public OfficeTeamPlanChoseiDto getChosei(String sosCd3, String prodCode) throws DataNotFoundException {
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
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		Map<String, Object> chosei = dataAccess.queryForObject(getSqlMapName() + ".getChosei", paramMap);
		Boolean choseiUhFlg = (Boolean) chosei.get("choseiUhFlg");
		Boolean choseiPFlg = (Boolean) chosei.get("choseiPFlg");
		return new OfficeTeamPlanChoseiDto(choseiUhFlg, choseiPFlg);
	}

	// 計画値の一括コピーで担当者別計画を更新する。
	public int executePlanCopy(String sosCd4, String prodCode, CopyWay copyWay, DpUser dpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (copyWay == null) {
			final String errMsg = "コピー方法がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "ユーザ情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		// 更新者情報を付与
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());
		paramMap.put("copyWay", copyWay.getDbValue());

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".updateByCopy", paramMap);
	}

	public int updateByMrPlanSum(String sosCd4, String prodCode, DpUser dpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "ユーザ情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		// 更新者情報を付与
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".updateByMrPlanSum", paramMap);
	}
}
