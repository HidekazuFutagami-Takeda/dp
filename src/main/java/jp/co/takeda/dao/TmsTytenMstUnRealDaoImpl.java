package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.TmsTytenMstHontenDto;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.util.StringUtil;

/**
 * TMS特約店基本統合にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("tmsTytenMstUnRealDao")
public class TmsTytenMstUnRealDaoImpl extends AbstractDao implements TmsTytenMstUnRealDao {

	private static final String SQL_MAP = "DPM_C_VI_T_TMS_TYTEN_MST_UN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 特約店情報を取得
	public TmsTytenMstUn searchRealRef(String tmsTytenCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (tmsTytenCd == null) {
			final String errMsg = "特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("tmsTytenCd", tmsTytenCd);
		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectRealRef", paramMap);
	}

	// 特約店情報を取得
	public TmsTytenMstUn searchRealValid(String tmsTytenCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (tmsTytenCd == null) {
			final String errMsg = "特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("tmsTytenCd", tmsTytenCd);
		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectRealValid", paramMap);
	}

	// 特約店情報を取得
	public List<TmsTytenMstUn> searchRealList(String sortString, TmsTytenMstScDto scDto) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("tytenKisLvll", scDto.getTytenKisLevel());
		paramMap.put("tmsTytenCdHonten", scDto.getTmsTytenCdHonten());
		paramMap.put("tmsTytenCdShisha", scDto.getTmsTytenCdShisha());
		paramMap.put("tmsTytenCdShiten", scDto.getTmsTytenCdShiten());
		paramMap.put("sosCd2", scDto.getSosCd2());
		paramMap.put("hontenMeiKn", scDto.getHontenMeiKn() != null ? StringUtil.toLikeCondition(scDto.getHontenMeiKn()) : null);
		paramMap.put("tmsTytenCd", scDto.getTmsTytenCd() != null ? StringUtil.toLikeCondition(scDto.getTmsTytenCd()) : null);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectRealList", paramMap);
	}

	// 指定した組織コード(支店または営業所)を元に実績のある特約店情報を取得
	public List<TmsTytenMstUn> searchRealResultsList(String sortString, String sosCd, TmsTytenMstScDto scDto) throws DataNotFoundException {
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
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
		if (scDto != null) {
			paramMap.put("hontenMeiKn", scDto.getHontenMeiKn() != null ? StringUtil.toLikeCondition(scDto.getHontenMeiKn()) : null);
		}

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectRealResultsList", paramMap);
	}

	// 特約店情報(本店)のDTOを取得
	public List<TmsTytenMstHontenDto> searchRealHontenList(String sortString, TmsTytenMstScDto scDto) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("tytenKisLvll", TytenKisLevel.HONTEN);
		// mod Start 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		if(StringUtils.isEmpty(scDto.getAddrCodePref())) {
			paramMap.put("sosCd2", scDto.getSosCd2());
		} else {
			paramMap.put("sosCd2", scDto.getAddrSosCode());
		}
		// mod End 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		paramMap.put("hontenMeiKn", scDto.getHontenMeiKn() != null ? StringUtil.toLikeCondition(scDto.getHontenMeiKn()) : null);
		paramMap.put("tytenKisLvllShisha", TytenKisLevel.SHISHA);

		// ----------------------
		// 検索実行
		// ----------------------
		List<Map<String, Object>> resultMapList = dataAccess.queryForList(getSqlMapName() + ".selectRealHontenList", paramMap);

		// 結果MAPをDTOに変換
		List<TmsTytenMstHontenDto> resultList = new ArrayList<TmsTytenMstHontenDto>();
		for (Map<String, Object> resultMap : resultMapList) {
			TmsTytenMstUn tmsTytenMstUn = (TmsTytenMstUn) resultMap.get("tmsTytenMstUn");
			Boolean shishaExistFlg = (Boolean) resultMap.get("shishaExistFlg");
			resultList.add(new TmsTytenMstHontenDto(tmsTytenMstUn, shishaExistFlg));
		}
		return resultList;
	}
}
