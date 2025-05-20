package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.Cal;

/**
 * カレンダーにアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("calDao")
public class CalDaoImpl extends AbstractDao implements CalDao {

	private static final String SQL_MAP = "DPS_S_SY_COM_CAL_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// カレンダーを取得
	public Cal search(String calYear, String calMonth, String calDay) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (calYear == null) {
			final String errMsg = "年がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (calMonth == null) {
			final String errMsg = "月がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (calDay == null) {
			final String errMsg = "日がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("calYear", calYear);
		paramMap.put("calMonth", calMonth);
		paramMap.put("calDay", calDay);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 指定した営業日後の日付を取得
	public java.util.Date searchBusinessDay(Date baseDate, Integer days) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (baseDate == null) {
			final String errMsg = "基準日付がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (days == null) {
			final String errMsg = "営業日数がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("baseDate", baseDate);
		paramMap.put("days", days);
		paramMap.put("bussinessDayFlg", true);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectBusinessDay", paramMap);
	}

	// 本日の日付を取得
	public Cal searchToday() throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectToday", paramMap);
	}

	//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	// 月内営業日順位で検索
	public Cal searchBizDays(String calYear, String calMonth, Integer bizDays) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (calYear == null) {
			final String errMsg = "年がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (calMonth == null) {
			final String errMsg = "月がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (bizDays == null) {
			final String errMsg = "日がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("calYear", calYear);
		paramMap.put("calMonth", calMonth);
		paramMap.put("bizDays", bizDays);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectBizDays", paramMap);
	}
	//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
}
