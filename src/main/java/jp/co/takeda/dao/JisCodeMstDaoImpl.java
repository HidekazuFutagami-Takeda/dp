package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.AddrScDto;
import jp.co.takeda.model.JisCodeMst;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.util.StringUtil;

import org.springframework.stereotype.Repository;

/**
 * JIS府県・市区町村にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("jisCodeMstDao")
public class JisCodeMstDaoImpl extends AbstractDao implements JisCodeMstDao {

	private static final String SQL_MAP = "DPS_S_SY_T_JIS_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// JIS府県・市区町村を取得
	public JisCodeMst search(Prefecture todoufukenCd, String shikuchosonCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (todoufukenCd == null) {
			final String errMsg = "都道府県コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (shikuchosonCd == null) {
			final String errMsg = "市区町村コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("todoufukenCd", todoufukenCd);
		paramMap.put("shikuchosonCd", shikuchosonCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 都道府県コードを元に、市区町村のリストを取得
	public List<JisCodeMst> searchByTodoufukenCd(Prefecture todoufukenCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (todoufukenCd == null) {
			final String errMsg = "都道府県コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("todoufukenCd", todoufukenCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchByTodoufukenCd", paramMap);
	}

	// 検索条件DTOで検索する。
	public List<JisCodeMst> searchList(AddrScDto addrScDto) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("todoufukenCd", addrScDto.getAddrCodePref());
		// 市区町村名部分一致
		paramMap.put("sikuMeiKn", StringUtil.toLikeConditionBoth(addrScDto.getSikuMeiKn()));

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
