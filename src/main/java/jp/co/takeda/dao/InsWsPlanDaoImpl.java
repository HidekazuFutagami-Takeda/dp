package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.InsWsPlanImpProdSummaryDto;
import jp.co.takeda.dto.InsWsPlanJgiSummaryDto;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.InsWsCount;
import jp.co.takeda.model.InsWsPlan;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insWsPlanDao")
public class InsWsPlanDaoImpl extends AbstractDao implements InsWsPlanDao {

	private static final String SQL_MAP = "DPS_I_INS_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * 計画対象カテゴリ領域取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsSyComInsOyakoDao")
	protected DpsSyComInsOyakoDao dpsSyComInsOyakoDao;

	// 施設特約店別計画を取得
	public InsWsPlan search(Long seqKey) throws DataNotFoundException {
		return (InsWsPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキーで施設特約店別計画を取得
	public InsWsPlan searchUk(Integer jgiNo, String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		InsWsPlan record = new InsWsPlan();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		record.setInsNo(insNo);
		record.setTmsTytenCd(tmsTytenCd);
		return (InsWsPlan) super.selectByUniqueKey(record);
	}

	// 施設特約店別計画のリストを取得
	public List<InsWsPlan> searchList(String sortString, Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		List<HoInsType> hoInsTypeList = InsType.convertHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 施設特約店別計画を登録
	public void insert(InsWsPlan record) throws DuplicateException {
		super.insert(record);
	}

	// 施設特約店別計画の計画値を更新
	public int update(InsWsPlan record) {
		return super.update(record);
	}

	// 施設特約店別計画を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}

	// 施設特約店別計画を従業員・品目単位で一括削除
	public int deleteByJgi(Integer jgiNo, String prodCode) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 削除実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".deleteByJgi", paramMap);
	}

	// 施設特約店別計画を組織コード(営業所)・品目単位で一括削除
	public int deleteBySos(String sosCd3, String prodCode) {
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
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// -------------
		// 削除実行
		// -------------
		return dataAccess.execute(getSqlMapName() + ".deleteBySos", paramMap);
	}

	// 施設特約店別計画を品目単位で一括削除
	public int deleteByProdCd(String prodCode) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 削除実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".deleteByProdCd", paramMap);
	}

	// 品目単位の施設特約店別計画サマリーを取得
	public InsWsPlanProdSummaryDto searchProdSummaryCtg(String sosCd3, String sosCd4, Integer jgiNo, String prodCode, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg DpsPlannedCtg = new DpsPlannedCtg();
		DpsPlannedCtg = dpsPlannedCtgDao.search(category);

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		int oyakoCount = 0;
		String oyakoKbProdCode = prodCode;
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpsSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}

		// ----------------------
		// 検索実行
		// ----------------------
		return searchProdSummary(sosCd3, sosCd4, jgiNo, prodCode, DpsPlannedCtg.getOyakoKb(), DpsPlannedCtg.getOyakoKb2(), oyakoKbProdCode);
	}

	// 品目単位の施設特約店別計画サマリーを取得
	public InsWsPlanProdSummaryDto searchProdSummary(String sosCd3, String sosCd4, Integer jgiNo, String prodCode, String oyakoKb,String oyakoKb2, String oyakoKbProdCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "組織コード・従業員番号が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));

		Map<String, Object> resultMap = dataAccess.queryForObject(getSqlMapName() + ".selectProdSummary", paramMap);
		Long distValueUhY = ConvertUtil.parseLong(resultMap.get("distValueUhY"), null);
		Long plannedValueUhY = ConvertUtil.parseLong(resultMap.get("plannedValueUhY"), null);
		Long distValuePY = ConvertUtil.parseLong(resultMap.get("distValuePY"), null);
		Long plannedValuePY = ConvertUtil.parseLong(resultMap.get("plannedValuePY"), null);

		return new InsWsPlanProdSummaryDto(prodCode, distValueUhY, plannedValueUhY, distValuePY, plannedValuePY);
	}

	// 品目単位の施設特約店別計画サマリーを取得（ワクチン）
	public InsWsPlanProdSummaryDto searchProdSummaryVac(String sosCd3, String sosCd4, Integer jgiNo, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "組織コード・従業員番号が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);

		Map<String, Object> resultMap = dataAccess.queryForObject(getSqlMapName() + ".selectProdSummaryVac", paramMap);
		Long distValueUhY = ConvertUtil.parseLong(resultMap.get("distValueUhY"), null);
		Long plannedValueUhY = ConvertUtil.parseLong(resultMap.get("plannedValueUhY"), null);
		Long distValuePY = ConvertUtil.parseLong(resultMap.get("distValuePY"), null);
		Long plannedValuePY = ConvertUtil.parseLong(resultMap.get("plannedValuePY"), null);

		return new InsWsPlanProdSummaryDto(prodCode, distValueUhY, plannedValueUhY, distValuePY, plannedValuePY);
	}

	// 対象品目・担当者単位の施設特約店別計画サマリーリストを取得
	public List<InsWsPlanJgiSummaryDto> searchJgiSummary(String prodCode, String sosCd3, String sosCd4, Integer jgiNo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null && sosCd3 == null && sosCd4 == null) {
			final String errMsg = "組織コード・従業員番号が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);

		// 固定条件
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });

		List<Map<String, Object>> resultMapList = dataAccess.queryForList(getSqlMapName() + ".selectJgiSummary", paramMap);
		List<InsWsPlanJgiSummaryDto> dtoList = new ArrayList<InsWsPlanJgiSummaryDto>();
		for (Map<String, Object> resultMap : resultMapList) {
			Integer _jgiNo = ((BigDecimal) resultMap.get("jgiNo")).intValue();
			Long distValueUhY = ConvertUtil.parseLong(resultMap.get("distValueUhY"), null);
			Long plannedValueUhY = ConvertUtil.parseLong(resultMap.get("plannedValueUhY"), null);
			Long distValuePY = ConvertUtil.parseLong(resultMap.get("distValuePY"), null);
			Long plannedValuePY = ConvertUtil.parseLong(resultMap.get("plannedValuePY"), null);
			dtoList.add(new InsWsPlanJgiSummaryDto(_jgiNo, prodCode, distValueUhY, plannedValueUhY, distValuePY, plannedValuePY));
		}

		return dtoList;
	}

	//配分値を取得する
	public Map<String, List<InsWsPlanImpProdSummaryDto>> selectImportantHbn(
			Integer jgiNo,
			String prodCode,
			String refFrom,
			String refTo,
			String refProdCode,
			String insType,
			String zeroDistFld,
			Integer execJgiNo,
			String execJgiName) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("refFrom", refFrom);
		paramMap.put("refTo", refTo);
		paramMap.put("refProdCode", refProdCode);
		paramMap.put("insType", insType);
		paramMap.put("zeroDistFlg", zeroDistFld);
		paramMap.put("exeJgiNo", execJgiNo);
		paramMap.put("exeJgiName", execJgiName);

		// ----------------------
		// 配分値取得
		// ----------------------
		List<Map<String, Object>> resultMapList = dataAccess.queryForList(getSqlMapName() + ".selectImportantHbn", paramMap);

		Map<String, List<InsWsPlanImpProdSummaryDto>> dtoList = new HashMap<String, List<InsWsPlanImpProdSummaryDto>>();
		for (Map<String, Object> resultMap : resultMapList) {
			//配分値と確定値の値を千の位で丸める
			Double distValueY = ((BigDecimal) resultMap.get("DIST_VALUE_Y")).doubleValue();
			long distValueYRound = 0L;
			if (distValueY < 0) {
				//配分値Yがマイナス値の場合、0とする
				distValueY = 0.0;
			} else if (distValueY > 0) {
				distValueYRound = ((BigDecimal) resultMap.get("DIST_VALUE_Y")).setScale(-4, BigDecimal.ROUND_HALF_UP).longValue();
			}
			Double plannedValueY = ((BigDecimal) resultMap.get("PLANNED_VALUE_Y")).doubleValue();
			long plannedValueYRound = 0L;
			if (plannedValueY < 0) {
				//確定値Yがマイナス値の場合、0とする
				plannedValueY = 0.0;
			} else if (plannedValueY > 0) {
				plannedValueYRound = ((BigDecimal) resultMap.get("PLANNED_VALUE_Y")).setScale(-4, BigDecimal.ROUND_HALF_UP).longValue();
			}

			String mainInsNo = ConvertUtil.toString(resultMap.get("MAIN_INS_NO"));
			List<InsWsPlanImpProdSummaryDto> entityList = null;
			if (dtoList.containsKey(mainInsNo)) {
				entityList = dtoList.get(mainInsNo);
			} else {
				entityList = new ArrayList<InsWsPlanImpProdSummaryDto>();
			}

			entityList.add(new InsWsPlanImpProdSummaryDto(
					((BigDecimal) resultMap.get("JGI_NO")).intValue(),
					ConvertUtil.toString(resultMap.get("PROD_CODE")),
					ConvertUtil.toString(resultMap.get("INS_NO")),
					ConvertUtil.toString(resultMap.get("TMS_TYTEN_CD")),
					ConvertUtil.toString(resultMap.get("MAIN_INS_NO")),
					distValueY,
					distValueYRound,
					null,
					plannedValueY,
					plannedValueYRound,
					null,
					null,
					ConvertUtil.toString(resultMap.get("SPECIAL_INS_PLAN_FLG")),
					ConvertUtil.toString(resultMap.get("EXCEPT_DIST_INS_FLG")),
					ConvertUtil.toString(resultMap.get("DEL_INS_FLG"))));

			dtoList.put(mainInsNo, entityList);
		}

		return dtoList;
	}

	//配分ミス基本情報を取得する
	public Map<String, Object> selectHbnMissBase(Integer jgiNo, String prodCode) throws DataNotFoundException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);

		return dataAccess.queryForObject(getSqlMapName() + ".selectHbnMissBase", paramMap);
	}

	// 施設特約店別計画の削除施設数取得
	public List<InsWsCount> searchDelInsCount(String sosCd3, Integer jgiNo, String prodCode, boolean prodList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "組織コード・従業員番号が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("prodList", prodList);

		// 削除施設数を取得
		List<InsWsCount> list = new ArrayList<InsWsCount>();

		try {
			list = dataAccess.queryForList(getSqlMapName() + ".selectDelInsCount", paramMap);
		} catch (DataNotFoundException e) {
			// データなしの場合は何もしない
		}
		return list;
	}

	// 施設特約店別計画の計画対象外特約店数取得
	public List<InsWsCount> searchTaiGaiTytenCount(String sosCd3, Integer jgiNo, String prodCode, boolean prodList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "組織コード・従業員番号が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("prodList", prodList);

		// 削除施設数を取得
		List<InsWsCount> list = new ArrayList<InsWsCount>();

		try {
			list = dataAccess.queryForList(getSqlMapName() + ".selectTaiGaiTytenCount", paramMap);
		} catch (DataNotFoundException e) {
			// データなしの場合は何もしない
		}
		return list;
	}

	// 施設特約店別計画の配分除外施設数取得
	public List<InsWsCount> searchExceptDistInsCount(String sosCd3, Integer jgiNo, String prodCode, boolean prodList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "組織コード・従業員番号が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("prodList", prodList);

		// 配分除外施設数を取得
		List<InsWsCount> list = new ArrayList<InsWsCount>();

		try {
			list = dataAccess.queryForList(getSqlMapName() + ".selectExceptDistInsCount", paramMap);
		} catch (DataNotFoundException e) {
			// データなしの場合は何もしない
		}
		return list;
	}
}
