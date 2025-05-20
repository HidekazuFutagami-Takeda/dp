package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.div.BumonRank;

/**
 * 組織情報にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("sosMstRealDao")
public class SosMstRealDaoImpl extends AbstractDao implements SosMstRealDao {

	private static final String SQL_MAP = "DPM_C_VI_SOS_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 組織情報を取得
	public SosMst searchReal(String sosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("sosCd", sosCd);
		paramMap.put("etcSosFlg", false);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectReal", paramMap);
	}

	// 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	// 都道府県コードに紐づく組織コードを取得
	public SosMst searchBumon(String addrCodePref) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (addrCodePref == null) {
			final String errMsg = "都道府県コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("addrCodePref", addrCodePref);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectBumon", paramMap);
	}

	public SosMst searchRealEtcSos(String sosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("sosCd", sosCd);
		paramMap.put("etcSosFlg", true);

		// ----------------------
		// 検索実行
		// ----------------------
		SosMst zatuTeam = dataAccess.queryForObject(getSqlMapName() + ".selectReal", paramMap);
		return zatuTeam;
	}

	// 医薬支店Ｃ、医薬営業所Ｃを基に部門ランク３(営業所・エリア特約店G)の組織情報を取得
	public SosMst searchReal(String brCode, String distCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (brCode == null) {
			final String errMsg = "医薬支店Ｃがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distCode == null) {
			final String errMsg = "医薬営業所Ｃがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("brCode", brCode);
		paramMap.put("distCode", distCode);
		paramMap.put("bumonRank", BumonRank.OFFICE_TOKUYAKUTEN_G);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectReal", paramMap);
	}

	// 上位の組織コードを元に組織情報一覧を取得
	public List<SosMst> searchRealList(String sortString, SosListType sosListType, BumonRank bumonRank, String upSosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosListType == null) {
			final String errMsg = "組織一覧の種類がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (bumonRank == null) {
			final String errMsg = "上位組織部門ランクがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		switch (bumonRank) {
			case SITEN_TOKUYAKUTEN_BU:
			case OFFICE_TOKUYAKUTEN_G:
				if (upSosCd == null) {
					final String errMsg = "上位組織がnull";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
				}
				break;
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		if (SosListType.SHITEN_LIST.equals(sosListType)) {
			paramMap.put("shiten", SosMst.SHITEN_GROUP);

			// 上位が営業所の場合は、整形ＭＲを除く
			if(BumonRank.OFFICE_TOKUYAKUTEN_G.equals(bumonRank)){
				paramMap.put("seikei_mr", SosMst.SEIKEI_MR_GROUP);
			}

		} else {
			paramMap.put("tokuyakuten", SosMst.TOKUYAKUTEN_VAC_GROUP);
		}
		paramMap.put("bumonRank", bumonRank);
		paramMap.put("upSosCd", upSosCd);

		// ----------------------
		// 検索実行
		// ----------------------
		List<SosMst> sosList = dataAccess.queryForList(getSqlMapName() + ".selectRealList", paramMap);
		return sosList;
	}

	@Override
	public List<SosMst> searchTokuyakutenbuList(List<String> ryutsuList) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sortString", SosMstRealDao.SORT_STRING);
		paramMap.put("tokuyakuten", SosMst.TOKUYAKUTEN_BU_GROUP);
		paramMap.put("excludeTokuyakutenList", ryutsuList);

		// ----------------------
		// 検索実行
		// ----------------------
		List<SosMst> sosList = dataAccess.queryForList(getSqlMapName() + ".selectRealList", paramMap);
		return sosList;
	}

	public List<SosMst> searchRealList(String sortString, String sosGroup) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosGroup == null) {
			final String errMsg = "組織グループがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("shiten", sosGroup);

		// ----------------------
		// 検索実行
		// ----------------------
		List<SosMst> sosList = dataAccess.queryForList(getSqlMapName() + ".selectRealList", paramMap);
		return sosList;
	}

	// 組織情報を取得
	public List<SosMstCtg> searchSosCategoryList(String sosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("sosCd", sosCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectSosCategoryList", paramMap);
	}

	// 計画支援の組織情報を取得
	public List<SosMstCtg> searchDpsSosCategoryList(String sosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("sosCd", sosCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectDpsSosCategoryList", paramMap);
	}

}
