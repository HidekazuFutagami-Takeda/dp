package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.WsPlanSummaryScDto;
import jp.co.takeda.model.WsPlanSummary2;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.util.StringUtil;

/**
 * 特約店別計画のサマリー情報を取得するDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("wsPlanSummaryDao")
public class WsPlanSummaryDaoImpl extends AbstractDao implements WsPlanSummaryDao {

	private static final String SQL_MAP = "VIEW_WsPlanSummary2_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 特約店別計画サマリー情報リストを取得
	public List<WsPlanSummary2> searchList(String sortString, WsPlanSummaryScDto scDto, boolean nnuFlg) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getTytenKisLevel() == null) {
			final String errMsg = "階層レベルがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(9);
		if (StringUtils.isNotBlank(scDto.getTmsTytenCdPart())) {
			paramMap.put("tmsTytenCdPart", StringUtil.toLikeCondition(scDto.getTmsTytenCdPart()));
		}
		paramMap.put("sortString", sortString);
		paramMap.put("tmsTytenCd", scDto.getTmsTytenCd());
		paramMap.put("tytenKisLevel", scDto.getTytenKisLevel());
		paramMap.put("category", scDto.getCategory());
		paramMap.put("kaBaseKb", scDto.getKaBaseKb());
		// 固定条件
		paramMap.put("sales", Sales.IYAKU);

		if (nnuFlg) {
			return dataAccess.queryForList(getSqlMapName() + ".selectListWithNnu", paramMap);
		} else {
			return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		}
	}
}
