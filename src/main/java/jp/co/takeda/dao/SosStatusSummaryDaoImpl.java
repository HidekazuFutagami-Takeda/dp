package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.SosStatusSummary;

/**
 * 組織単位のステータスのサマリーを取得するDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("sosStatusSummaryDao")
public class SosStatusSummaryDaoImpl extends AbstractDao implements SosStatusSummaryDao {

	private static final String SQL_MAP = "VIEW_SosStatusSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}
	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;


//	// 組織コード(支店)・カテゴリを基に、ステータスのサマリを取得
//	@Override
//	public SosStatusSummary searchShiten(String sosCd2, String category) throws DataNotFoundException {
//		// ----------------------
//		// 検索実行
//		// ----------------------
//		return search(sosCd2, sosCd2, null, null, category);
//	}
//
//	// 組織コード(営業所)・カテゴリを基に、ステータスのサマリを取得
//	@Override
//	public SosStatusSummary searchEigyo(String sosCd3, String category) throws DataNotFoundException {
//		// ----------------------
//		// 検索実行
//		// ----------------------
//		return search(sosCd3, null, sosCd3, null, category);
//	}

//	// 組織コード(チーム)・カテゴリを基に、ステータスのサマリを取得
//	@Override
//	public SosStatusSummary searchTeam(String sosCd4, ProdCategory category) throws DataNotFoundException {
//		// ----------------------
//		// 検索実行
//		// ----------------------
//		SosStatusSummary sosStatusSummary = search(sosCd4, null, null, sosCd4, category);
//
//		// チーム別の入力状況を取得する。
//		// 担当者別計画ステータスの担当者別計画入力完了(AL修正)の件数、
//		// 担当者別計画入力完了日時はチーム別入力状況より取得する。
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("category", category);
//		paramMap.put("sosCd4", sosCd4);
//		// 固定条件
//		paramMap.put("planTargetFlg", true);
//		paramMap.put("sales", Sales.IYAKU);
//		Map<String, Object> count = dataAccess.queryForObject(getSqlMapName() + ".teamCount", paramMap);
//		MrPlanStatSum mrPlanStatSum = sosStatusSummary.getMrPlanStatSum();
//		mrPlanStatSum.setMrPlanInputFinished((Integer) count.get("mrPlanInputFinished"));
//		mrPlanStatSum.setMrPlanInputDate((Date) count.get("mrPlanInputDate"));
//		sosStatusSummary.setMrPlanStatSum(mrPlanStatSum);
//		return sosStatusSummary;
//	}

	/**
	 * 組織単位のステータスのサマリー取得処理を実行する。
	 *
	 * @param sosCd 検索対象の組織コード
	 * @param sosCd2 組織コード(支店)
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param category 品目カテゴリ
	 * @return 組織単位のステータスのサマリー
	 * @throws DataNotFoundException
	 */
	private SosStatusSummary search_bak(String sosCd, String sosCd2, String sosCd3, String sosCd4, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("sosCd", sosCd);
		paramMap.put("sosCd2", sosCd2);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		String siireCd = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();

		if(category != null && !category.equals(siireCd)){
			paramMap.put("isNotShiire", true);
		}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という条件に変更フラグ名も変更。
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.IYAKU);
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}
	@Override
	public SosStatusSummary search(String sosCd,BumonRank bumonRank ,String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("sosCd", sosCd);
		switch (bumonRank) {
		case SITEN_TOKUYAKUTEN_BU:
			paramMap.put("sosCd2", "hoge"); // 支店を検索
			break;
		case OFFICE_TOKUYAKUTEN_G:
			paramMap.put("sosCd3", "hoge"); //営業所を検索
			break;
		default:
			break;
		}

		String siireCd = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();

		if(category != null && !category.equals(siireCd)){
			paramMap.put("isNotShiire", true);
		}
		paramMap.put("planTargetFlg", true);
		//paramMap.put("sales", Sales.IYAKU);
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	@Override
	public List<SosStatusSummary> searchList(String sortString, SosListType sosListType, BumonRank bumonRank, String upSosCd, String category ) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosListType == null) {
			final String errMsg = "組織一覧の種類がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		if (SosListType.SHITEN_LIST.equals(sosListType)) {
			paramMap.put("sosgrp", SosMst.SHITEN_GROUP);
			paramMap.put("excludeSosgrp", SosMst.NS_GROUP);// add 2018/05/21 J18-0002_2018年4月組織変更対応（計画支援）　SosMst.SHITEN_GROUP(=SSK0073)からNS_GROUP(SSK0278)を除外する
		} else {
			paramMap.put("sosgrp", SosMst.TOKUYAKUTEN_VAC_GROUP);
		}
		paramMap.put("upSosCd", upSosCd);
		paramMap.put("bumonRank", bumonRank);
		paramMap.put("category", category);
		switch (bumonRank){
			case SITEN_TOKUYAKUTEN_BU:
				paramMap.put("sosCd2", "hoge"); // 本部なら支店を検索
				break;
			case OFFICE_TOKUYAKUTEN_G:
				paramMap.put("sosCd3", "hoge"); // 支店なら営業所を検索
				break;
		default:
			break;
		}
		String siireCd = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();

		if(category != null && !category.equals(siireCd)){
			paramMap.put("isNotShiire", true);
		}
		paramMap.put("planTargetFlg", true);
		//paramMap.put("sales", Sales.IYAKU);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

}
