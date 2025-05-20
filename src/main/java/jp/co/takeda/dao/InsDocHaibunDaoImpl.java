package jp.co.takeda.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsDocHaibunDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.ProdCategory;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

/**
 * 施設医師別配分処理DAO実装クラス
 *
 * @author tkawabata
 */
@Repository("insDocHaibunDao")
public class InsDocHaibunDaoImpl extends AbstractDao implements InsDocHaibunDao {

	/**
	 * SQLMAP名称
	 */
	private static final String SQL_MAP = "VIEW_InsDocHaibun_SqlMap";

	/**
	 * LOGGER
	 */
	protected static final Log LOG = LogFactory.getLog(InsDocHaibunDaoImpl.class);

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 施設医師別計画配分処理を実行する。
	public List<DistributionMissDto> executeHaibun(InsDocHaibunDto insDocHaibunDto) {
		Integer JGI_NO = insDocHaibunDto.getJgiMst().getJgiNo();
		String SOS_CD3 = insDocHaibunDto.getSosMst().getSosCd();
		Boolean dpPlanClear = insDocHaibunDto.isDoPlanClear();
		ProdCategory category = insDocHaibunDto.getCategory();

		// SQLパラメータ
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("JGI_NO", JGI_NO);
		paramMap.put("SOS_CD3", SOS_CD3);
		paramMap.put("EXEC_JGI_NO", insDocHaibunDto.getExecDpUser().getJgiNo());
		paramMap.put("EXEC_JGI_NAME", insDocHaibunDto.getExecDpUser().getJgiName());
		paramMap.put("CATEGORY", category);

		// 処理件数
		Integer cnt = 0;

		// ------------------------------------
		// 2-1.機能詳細(準備)
		// ------------------------------------

StopWatch stopWatch = new StopWatch();
StopWatch stopWatch1 = new StopWatch();
StringBuffer debugMsg = new StringBuffer();
stopWatch1.start();
stopWatch.start();

		// 施設医師別計画配分結果一次表の作成
		cnt = dataAccess.execute(getSqlMapName() + ".deleteRESULT_TMP", paramMap);
		cntLogging("deleteRESULT_TMP", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertRESULT_TMP", paramMap);
		cntLogging("insertRESULT_TMP", cnt);

stopWatch.stop();
debugMsg.append(SOS_CD3 + "," + JGI_NO+","+cnt+",");

debugMsg.append(getLogMsg("1-1", stopWatch));
stopWatch.reset();
stopWatch.start();

		// ＡＰＳタケダ先以外の配分対象外施設設定
		cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_EXCEPT_APS", paramMap);
		cntLogging("updateRESULT_TMP_EXCEPT_APS", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("1-2", stopWatch));
stopWatch.reset();
stopWatch.start();


		// 複数勤務医師対応
		cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_FUKUSU_DOC", paramMap);
		cntLogging("updateRESULT_TMP_PLURAL_DOC", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("1-3", stopWatch));
stopWatch.reset();
stopWatch.start();

		// 施設別実績(見込)作成
		cnt = dataAccess.execute(getSqlMapName() + ".deleteJISSEKI_INS", paramMap);
		cntLogging("deleteJISSEKI_INS", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertJISSEKI_INS", paramMap);
		cntLogging("insertJISSEKI_INS", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("1-4", stopWatch));
stopWatch.reset();
stopWatch.start();

		// 施設医師別実績(見込)作成
		cnt = dataAccess.execute(getSqlMapName() + ".deleteJISSEKI_INSDOC", paramMap);
		cntLogging("deleteJISSEKI_INSDOC", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertJISSEKI_INSDOC", paramMap);
		cntLogging("insertJISSEKI_INSDOC", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_JISSEKI_SLIDE", paramMap);
		cntLogging("updateRESULT_TMP_JISSEKI_SLIDE", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("1-5", stopWatch));
stopWatch.reset();
stopWatch.start();

		// ------------------------------------
		// 2-3.機能詳細(UP配分)
		// ------------------------------------

		// UP計画の配分
		cnt = dataAccess.execute(getSqlMapName() + ".deleteUP_PROD", paramMap);
		cntLogging("deleteUP_PROD", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertUP_PROD", paramMap);
		cntLogging("insertUP_PROD", cnt);

		// 品目群で集計
		cnt = dataAccess.execute(getSqlMapName() + ".deleteUP_PCAT", paramMap);
		cntLogging("deleteUP_PCAT", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertUP_PCAT", paramMap);
		cntLogging("insertUP_PCAT", cnt);

		// 優先計画決定
		cnt = dataAccess.execute(getSqlMapName() + ".updateUP_PCAT_PCAT_NUM", paramMap);
		cntLogging("updateUP_PCAT_PCAT_NUM", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".updateUP_PCAT_DOC_NUM", paramMap);
		cntLogging("updateUP_PCAT_DOC_NUM", cnt);

		// UP計画再配分
		cnt = dataAccess.execute(getSqlMapName() + ".updateUP_PCAT_REHBN", paramMap);
		cntLogging("updateUP_PCAT_REHBN", cnt);

		// 複合順位決定
		cnt = dataAccess.execute(getSqlMapName() + ".updateUP_PCAT_COMP_NUM", paramMap);
		cntLogging("updateUP_PCAT_COMP_NUM", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("3-1", stopWatch));
stopWatch.reset();
stopWatch.start();

		// UP計画最低金額調整
		Integer TO_ZERO_CNT = new Integer(0);
		Integer TO_CHOSEI_CNT = new Integer(0);
		paramMap.put("TO_ZERO_CNT", TO_ZERO_CNT);
		paramMap.put("TO_CHOSEI_CNT", TO_CHOSEI_CNT);
		dataAccess.executeProcedure(getSqlMapName() + ".updateUP_PCAT_CHOSEI", paramMap);
		cntLogging("updateUP_PCAT_CHOSEI(TO_ZERO)", (Integer) paramMap.get("TO_ZERO_CNT"));
		cntLogging("updateUP_PCAT_CHOSEI(TO_CHOSEI)", (Integer) paramMap.get("TO_CHOSEI_CNT"));

stopWatch.stop();
debugMsg.append(getLogMsg("3-2", stopWatch));
stopWatch.reset();
stopWatch.start();

		// UP計画算出
		cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_UP", paramMap);
		cntLogging("updateRESULT_TMP_UP", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("3-3", stopWatch));
stopWatch.reset();
stopWatch.start();

		// ------------------------------------
		// 2-4.機能詳細(DOWN配分)
		// ------------------------------------

		// DOWN計画算出
		cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_DOWN", paramMap);
		cntLogging("updateRESULT_TMP_DOWN", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("4-1", stopWatch));
stopWatch.reset();
stopWatch.start();

		// ------------------------------------
		// 2-5.機能詳細(医師別計画作成)
		// ------------------------------------

		// UPDOWN誤差調整
		cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_UPDOWN_GOSA", paramMap);
		cntLogging("updateRESULT_TMP_UPDOWN_GOSA", cnt);

		// ユーザ入力値スライド(クリアフラグがオフの場合)
		if (dpPlanClear == false) {
			cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_PLAN_CLRFLG", paramMap);
			cntLogging("updateRESULT_TMP_PLAN_CLRFLG", cnt);
		}

		// 実績スライド
		cnt = dataAccess.execute(getSqlMapName() + ".updateRESULT_TMP_PLAN", paramMap);
		cntLogging("updateRESULT_TMP_PLAN", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("5-1", stopWatch));
stopWatch.reset();
stopWatch.start();

		// 医師別計画作成
		cnt = dataAccess.execute(getSqlMapName() + ".deleteINS_DOC_PLAN", paramMap);
		cntLogging("deleteINS_DOC_PLAN", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertINS_DOC_PLAN", paramMap); // 医療モール以外
		cntLogging("insertINS_DOC_PLAN", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertINS_DOC_PLAN_FOR_MALL", paramMap); // 医療モール
		cntLogging("insertINS_DOC_PLAN_FOR_MALL", cnt);

		// マイナス決定値のゼロ化対応
		cnt = dataAccess.execute(getSqlMapName() + ".updateINS_DOC_PLAN_MINUS_PLAN", paramMap);
		cntLogging("updateINS_DOC_PLAN_MINUS_PLAN", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("5-2", stopWatch));
stopWatch.reset();
stopWatch.start();

		// ------------------------------------
		// 2-6.機能詳細(施設別計画作成)
		// ------------------------------------

		// 施設別計画作成
		cnt = dataAccess.execute(getSqlMapName() + ".deleteINS_PLAN", paramMap);
		cntLogging("deleteINS_PLAN", cnt);
		cnt = dataAccess.execute(getSqlMapName() + ".insertINS_PLAN", paramMap);
		cntLogging("insertINS_PLAN", cnt);

stopWatch.stop();
debugMsg.append(getLogMsg("6-1", stopWatch));
stopWatch1.stop();
debugMsg.append(stopWatch1.getTime());

infoLogging(debugMsg.toString());

		// ------------------------------------
		// 2-7.機能詳細(後処理)
		// ------------------------------------
/*
		// 配分ミスリスト出力
		List<DistributionMissDto> distributionMissDtoList = new ArrayList<DistributionMissDto>();
		try {
			List<Map<String, Object>> resultMapList = dataAccess.queryForList(getSqlMapName() + ".selectDistMiss", paramMap);
			cntLogging("selectDistMiss", resultMapList.size());
			for (Map<String, Object> resultMap : resultMapList) {
				String SOS_CD = (String) resultMap.get("SOS_CD");
				String PROD_CODE = (String) resultMap.get("PROD_CODE");
				InsType INS_TYPE = InsType.getInstance((String) resultMap.get("INS_TYPE"));
				Long PLANNED_VALUE = ((Number) resultMap.get("PLANNED_VALUE")).longValue();
				Long DIFF_VALUE = ((Number) resultMap.get("DIFF_VALUE")).longValue();
				String MESSAGE_CODE = (String) resultMap.get("MESSAGE_CODE");
				DistributionMissDto dto = new DistributionMissDto(SOS_CD, JGI_NO, PROD_CODE, INS_TYPE, PLANNED_VALUE, DIFF_VALUE, MESSAGE_CODE);
				distributionMissDtoList.add(dto);
			}

		} catch (DataNotFoundException e) {
			// エラーとしない
		}
		return distributionMissDtoList;
*/
		return new ArrayList<DistributionMissDto>();
	}

	/**
	 * 件数をログに出力する。
	 *
	 * @param logMsg ログメッセージ
	 * @param cnt 件数
	 */
	private void cntLogging(String logMsg, int cnt) {
		if (LOG.isInfoEnabled()) {
//			LOG.info(logMsg + " cnt=" + cnt);
		}
	}

	private String getLogMsg(String logMsg, StopWatch stopWatch) {
		return String.format("%s,%d,", logMsg, stopWatch.getTime());
	}
	private void infoLogging(String logMsg) {
		if (LOG.isInfoEnabled()) {
			LOG.info(logMsg);
		}
	}
}
