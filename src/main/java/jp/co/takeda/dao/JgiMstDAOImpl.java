package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 従業員情報にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("jgiMstDAO")
public class JgiMstDAOImpl extends AbstractDao implements JgiMstDAO {

	private static final String SQL_MAP = "DPS_C_JGI_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 従業員取得
	public JgiMst search(Integer jgiNo) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 組織コードを元に、従業員情報のリスト(MRのみ)を取得
	public List<JgiMst> searchBySosCd(String sortString, String sosCd, SosListType sosListType, BumonRank bumonRank) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		if (bumonRank != null) {
			switch (bumonRank) {
				case IEIHON:
					paramMap.put("sosCd1", sosCd);
					break;
				case SITEN_TOKUYAKUTEN_BU:
					paramMap.put("sosCd2", sosCd);
					break;
				case OFFICE_TOKUYAKUTEN_G:
					paramMap.put("sosCd3", sosCd);
					break;
				case TEAM:
					paramMap.put("sosCd4", sosCd);
					break;
			}
		}
		// 固定条件
		paramMap.put("sosLvl", SosLvl.MR);
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySosCd", paramMap);
	}

	// 最新の従業員取得
	public JgiMst searchReal(Integer jgiNo) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectReal", paramMap);
	}

	// 最新の従業員リスト取得
	public List<JgiMst> searchBySosCd(String sortString, String sosCd, JokenSet... jokenSetList) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jokenSetList == null || jokenSetList.length == 0) {
			final String errMsg = "条件セットリストがnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
		paramMap.put("jokenSetList", jokenSetList);
		// 固定条件
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByJokenSet", paramMap);
	}

	// 従業員番号、従業員区分を元に、最新の従業員情報のリスト
	public JgiMst searchByJgiKb(Integer jgiNo, JgiKb[] jgiKbList) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiKbList == null || jgiKbList.length == 0) {
			final String errMsg = "従業員区分リストがnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectByJgiKb", paramMap);
	}

	// 最新の代行ユーザ従業員のリストを取得
	@SuppressWarnings("unchecked")
	public List<CodeAndValue> searchAltJgi(Integer jgiNo, JokenSet[] jokenSetList) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jokenSetList == null || jokenSetList.length == 0) {
			final String errMsg = "条件セットリストがnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("jokenSetList", jokenSetList);
		paramMap.put("sortString", JgiMstDAO.SORT_STRING);

		// ----------------------
		// 検索実行
		// ----------------------
		final String JGI_NO_KEY = "JGI_NO";
		final String JGI_NAME_KEY = "JGI_NAME";
		List<Map> resultMapList = dataAccess.queryForList(getSqlMapName() + ".selectAltJgi", paramMap);
		List<CodeAndValue> altJgiList = new ArrayList<CodeAndValue>(resultMapList.size());
		for (Map resultMap : resultMapList) {
			BigDecimal altJgiNo = (BigDecimal) resultMap.get(JGI_NO_KEY);
			String altJgiName = (String) resultMap.get(JGI_NAME_KEY);
			altJgiList.add(new CodeAndValue(altJgiNo.toString(), altJgiName));
		}
		return altJgiList;
	}

	// 代行ユーザが設定されている場合、代行ユーザの従業員情報を取得する。
	public JgiMst searchAltJgi() throws DataNotFoundException {

		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		Boolean isAltUser = dpUserInfo.isAltUserSetting();

		// 代行ユーザが設定されている場合
		if (isAltUser != null && isAltUser) {
			DpUser altUser = dpUserInfo.getSettingUser();
			Integer jgiNo = altUser.getJgiNo();
			if (jgiNo != null) {
				JgiMst altJgi = searchReal(jgiNo);
				return altJgi;
			}
		}
		throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR));
	}

	// 副担当従業員取得
	public JgiMst searchSubJgi(Integer jgiNo) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectSubJgi", paramMap);
	}

	// 指定された従業員と所属内での条件セットに合致する従業員情報を取得
	public List<JgiMst> searchByJokenSetList(Integer jgiNo, JokenSet... jokenSetList) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null || jokenSetList == null || jokenSetList.length == 0) {
			final String errMsg = "条件セットリストがnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("jokenSetList", jokenSetList);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByJokenSetInSosCd", paramMap);
	}

	// 組織コードを元に、従業員情報のリストを取得
	public List<JgiMst> searchJgiList(String sosCd) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectJgiList", paramMap);
	}

	//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
	// 組織コードを元に、従業員情報のリスト(MRのみ)を取得
	public List<JgiMst> searchBySosCdCategory(String sortString, String sosCd, SosListType sosListType, BumonRank bumonRank, String category) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		if (bumonRank != null) {
			switch (bumonRank) {
				case IEIHON:
					paramMap.put("sosCd1", sosCd);
					break;
				case SITEN_TOKUYAKUTEN_BU:
					paramMap.put("sosCd2", sosCd);
					break;
				case OFFICE_TOKUYAKUTEN_G:
					paramMap.put("sosCd3", sosCd);
					break;
				case TEAM:
					paramMap.put("sosCd4", sosCd);
					break;
			}
		}
		// 固定条件
		paramMap.put("sosLvl", SosLvl.MR);
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
		paramMap.put("category", category);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySosCdCategory", paramMap);
	}
	//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
}
