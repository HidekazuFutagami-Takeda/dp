package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.WsPlanForVacSummaryScDto;
import jp.co.takeda.model.WsPlanForVacSummary2;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.util.StringUtil;

/**
 * (ワ)特約店別計画のサマリ情報を取得するDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("wsPlanForVacSummaryDao")
public class WsPlanForVacSummaryDaoImpl extends AbstractDao implements WsPlanForVacSummaryDao {

	private static final String SQL_MAP = "VIEW_WsPlanForVacSummary2_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 特約店別計画サマリー情報リストを取得
	public List<WsPlanForVacSummary2> searchList(String sortString, WsPlanForVacSummaryScDto scDto, boolean nnuFlg) throws DataNotFoundException {

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
		Map<String, Object> paramMap = new HashMap<String, Object>(9);
		paramMap.put("sortString", sortString);
		paramMap.put("tmsTytenCdPart", StringUtil.toLikeCondition(scDto.getTmsTytenCdPart()));
		paramMap.put("tytenKisLevel", scDto.getTytenKisLevel());
		paramMap.put("kaBaseKb", scDto.getKaBaseKb());
		// 固定条件
		paramMap.put("sales", Sales.VACCHIN);

		if (nnuFlg) {
			return dataAccess.queryForList(getSqlMapName() + ".selectListWithNnu", paramMap);
		} else {
			return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		}
	}
}
