	package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.SpecialInsPlanScDto;
import jp.co.takeda.logic.ConvertInsKanaLogic;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.util.StringUtil;

/**
 * 特定施設個別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("specialInsPlanDao")
public class SpecialInsPlanDaoImpl extends AbstractDao implements SpecialInsPlanDao {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(SpecialInsPlanDaoImpl.class);

	private static final String SQL_MAP = "DPS_I_SPECIAL_INS_PLAN_SqlMap";

	/**
	 * 共通ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 組織カテゴリDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCViSosCtgDao")
	protected DpsCViSosCtgDao dpsCViSosCtgDao;

	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	@Autowired(required = true)
	@Qualifier("dpsSyComInsOyakoDao")
	protected DpsSyComInsOyakoDao dpsSyComInsOyakoDao;

    /**
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ユニークキーを元に特定施設個別計画を取得
	public SpecialInsPlan searchUk(String insNo, String prodCode, String tmsTytenCd, PlanType planType) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (planType == null) {
			final String errMsg = "計画立案区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		SpecialInsPlan record = new SpecialInsPlan();
		record.setInsNo(insNo);
		record.setProdCode(prodCode);
		record.setTmsTytenCd(tmsTytenCd);
		record.setPlanType(planType);
		return (SpecialInsPlan) super.selectByUniqueKey(record);
	}

	// 検索条件DTOを元に特定施設個別計画（施設一覧）を取得(子施設を除く)
	public List<SpecialInsPlan> searchList(String sortString, SpecialInsPlanScDto scDto, PlanType planType) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSpecialInsPlanRegType() == null) {
			final String errMsg = "絞込条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd3() == null && scDto.getSosCd4() == null && scDto.getJgiNo() == null) {
			final String errMsg = "組織・従業員が全てnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 施設名カナの特殊対応
		// ----------------------
		ConvertInsKanaLogic logic = new ConvertInsKanaLogic(commonDao);
		String searchKana = logic.execute(scDto.getInsKanaSrch());

		//カテゴリ取得
		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = new ArrayList<SosCtg>();
		try {
			ctgList = dpsCViSosCtgDao.search(scDto.getSosCd3());
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
			final String errMsg = "組織に紐づくカテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

 		// -----------------------------
 		// 計画対象カテゴリ領域取得
 		// -----------------------------
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		plannedCtg = dpsPlannedCtgDao.search(ctgList.get(0).getCategory());


		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", scDto.getJgiNo());
		paramMap.put("sosCd3", scDto.getSosCd3());
		paramMap.put("sosCd4", scDto.getSosCd4());
		paramMap.put("searchInsType", scDto.getSearchInsType() != null ? scDto.getSearchInsType().getDbValue().split(",") : null);
		paramMap.put("insFormalName", StringUtil.toOracleTextEsc(scDto.getInsFormalName()));
		paramMap.put("insKanaSrch", StringUtil.toOracleTextEsc(searchKana));
		paramMap.put("sortString", sortString);
		paramMap.put("planType", planType);
		// 固定条件
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });

		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(),null));

		List<SpecialInsPlan> resultList = dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		List<SpecialInsPlan> regList = new ArrayList<SpecialInsPlan>();
		List<SpecialInsPlan> nonRegList = new ArrayList<SpecialInsPlan>();
		// 格納用データ
		SpecialInsPlan honPlan = null;
		// 現データ
		SpecialInsPlan plan = null;
		String oldInsNo = "";
		for (int i = 0; i < resultList.size(); i++) {
			plan = resultList.get(i);
			// 施設が変わった場合
			if (!oldInsNo.equals(plan.getInsNo())) {
				oldInsNo = plan.getInsNo();
				// 本院の場合、格納用データを格納
				// A調の場合、格納用データ更新(更新者・日付)
				// B調の場合、格納用データを格納
				// 本院
				if (!plan.getInsClass().equals(InsClass.THOUZAI)) {
					// 格納データがある場合、リストに格納
					if (honPlan != null) {
						if (honPlan.getUpDate() == null) {
							nonRegList.add(honPlan);
						} else {
							regList.add(honPlan);
						}
						honPlan = null;
					}
					honPlan = plan;
					// A調の場合
				} else if (plan.getInsClass().equals(InsClass.THOUZAI) && plan.getOldInsrFlg().equals(OldInsrFlg.A_THOUZAI)) {
					if (plan.getUpDate() != null && honPlan != null) {
						if (honPlan.getUpDate() == null || plan.getUpDate().after(honPlan.getUpDate())) {
							honPlan.setUpJgiNo(plan.getUpJgiNo());
							honPlan.setUpJgiName(plan.getUpJgiName());
							honPlan.setUpDate(plan.getUpDate());
						}
					}
					// B調の場合
				} else {
					// 格納データがある場合、リストに格納
					if (honPlan != null) {
						if (honPlan.getUpDate() == null) {
							nonRegList.add(honPlan);
						} else {
							regList.add(honPlan);
						}
						honPlan = null;
					}
					honPlan = plan;
				}

				// 施設が変らない場合
			} else {
				// 格納用データを更新(更新者・日付)
				if (honPlan != null && honPlan.getUpDate() != null && plan.getUpDate() != null) {
					if (plan.getUpDate().after(honPlan.getUpDate())) {
						honPlan.setUpJgiNo(plan.getUpJgiNo());
						honPlan.setUpJgiName(plan.getUpJgiName());
						honPlan.setUpDate(plan.getUpDate());
					}
				}
			}
		}
		// 前本院データがある場合、リストに格納
		if (honPlan != null) {
			if (honPlan.getUpDate() == null) {
				nonRegList.add(honPlan);
			} else {
				regList.add(honPlan);
			}
		}
		switch (scDto.getSpecialInsPlanRegType()) {
			case REG_ON:
				resultList = regList;
				break;
			case REG_OFF:
				resultList = nonRegList;
				break;
			case ALL:
				resultList = regList;
				resultList.addAll(nonRegList);
				break;
		}
		if (resultList.size() == 0) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("resultList=" + resultList);
			}
			throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, "検索結果0件"));
		}
		return resultList;
	}

	// 従業員番号、施設コード、カテゴリ、品目固定コード、計画立案区分を元に特定施設個別計画を取得(子施設を含む）
	public List<SpecialInsPlan> searchByInsNo(String sortString, Integer jgiNo, String insNo, String category, String prodCode, PlanType planType)
		throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "検索条件の従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "検索条件の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(9);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("insNo", insNo);
		paramMap.put("category", category);
		paramMap.put("prodCode", prodCode);
		paramMap.put("planType", planType);
		paramMap.put("sortString", sortString);
		// 固定条件
		paramMap.put("sales", Sales.IYAKU);
		paramMap.put("planTargetFlg", true);
		paramMap.put("insClass", InsClass.THOUZAI);
		paramMap.put("oldInsrFlg", OldInsrFlg.A_THOUZAI);
		return dataAccess.queryForList(getSqlMapName() + ".selectByInsNo", paramMap);
	}

	// (ワ)従業員番号、施設コード、品目固定コード、計画立案区分を元にワクチン用特定施設個別計画を取得(子施設を含む)
	public List<SpecialInsPlan> searchByInsNoForVac(String sortString, Integer jgiNo, String insNo, String prodCode, PlanType planType) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "検索条件の従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "検索条件の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(8);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("insNo", insNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("planType", planType);
		paramMap.put("sortString", sortString);
		// 固定条件
		paramMap.put("sales", Sales.VACCHIN);
		paramMap.put("planTargetFlg", true);
		paramMap.put("insClass", InsClass.THOUZAI);
		paramMap.put("oldInsrFlg", OldInsrFlg.A_THOUZAI);
		return dataAccess.queryForList(getSqlMapName() + ".selectByInsNo", paramMap);
	}

	// 施設コード、品目固定コード、計画立案区分を元に特定施設個別計画を取得(子施設を含まない）
	public List<SpecialInsPlan> searchByInsNo(String sortString, String insNo, String prodCode, PlanType planType) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "検索条件の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(9);
		paramMap.put("insNo", insNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("planType", planType);
		paramMap.put("sortString", sortString);
		return dataAccess.queryForList(getSqlMapName() + ".selectByInsNoNotContain", paramMap);
	}

	// 特定施設個別計画を登録
	public void insert(SpecialInsPlan record) throws DuplicateException {
		super.insert(record);
	}

	// 施設コード、計画立案区分、品目カテゴリを元に特定施設個別計画を削除(子施設を含む）
	public int delete(Integer jgiNo, String insNo, PlanType planType, String category, Date upDate) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (upDate == null) {
			final String errMsg = "最終更新日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(6);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("insNo", insNo);
		paramMap.put("planType", planType);
		paramMap.put("category", category);
		paramMap.put("upDate", upDate);
		// 固定条件
		paramMap.put("sales", Sales.IYAKU);
		paramMap.put("planTargetFlg", true);
		paramMap.put("insClass", InsClass.THOUZAI);
		paramMap.put("oldInsrFlg", OldInsrFlg.A_THOUZAI);
		// ----------------------
		// 検索実行
		// ----------------------
		// TODO:最新更新日が本院以外の場合や、本院以外のみの場合、排他エラーになってしまうため更新日によるチェックをSQLから削除
		int count = dataAccess.execute(getSqlMapName() + ".delete", paramMap);
		if (count == 0) {
			final String errMsg = "：更新日時が異なるため、楽観的ロックエラーとする";
			throw new OptimisticLockingFailureException(getSqlMapName() + errMsg);
		}
		return count;
	}

	// 施設コード、品目カテゴリを元にワクチンの特定施設個別計画を削除(子施設を含む）
	public int deleteVaccine(Integer jgiNo, String insNo, PlanType planType, String category, Date upDate) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (upDate == null) {
			final String errMsg = "最終更新日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(6);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("insNo", insNo);
		paramMap.put("planType", planType);
		paramMap.put("category", category);
		paramMap.put("upDate", upDate);
		// 固定条件
		paramMap.put("sales", Sales.VACCHIN);
		paramMap.put("planTargetFlg", true);
		paramMap.put("insClass", InsClass.THOUZAI);
		paramMap.put("oldInsrFlg", OldInsrFlg.A_THOUZAI);
		// ----------------------
		// 検索実行
		// ----------------------
		int count = dataAccess.execute(getSqlMapName() + ".delete", paramMap);
		if (count == 0) {
			final String errMsg = "：更新日時が異なるため、楽観的ロックエラーとする";
			throw new OptimisticLockingFailureException(getSqlMapName() + errMsg);
		}
		return count;
	}

	// 施設コードを元に特定施設個別計画の最終更新日時を保持するレコードを取得(子施設を含む）
	public SpecialInsPlan searchUpDate(Integer jgiNo, String insNo, PlanType planType, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("insNo", insNo);
		paramMap.put("planType", planType);
		paramMap.put("category", category);
		// 固定条件
		paramMap.put("sales", Sales.IYAKU);
		paramMap.put("planTargetFlg", true);
		paramMap.put("insClass", InsClass.THOUZAI);
		paramMap.put("oldInsrFlg", OldInsrFlg.A_THOUZAI);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectUpDate", paramMap);
	}

	// 従業員番号、品目固定コード、計画立案区分を元に特定施設個別計画を取得
	public List<SpecialInsPlan> searchByJgiNo(String sortString, Integer jgiNo, String prodCode, PlanType planType) throws DataNotFoundException {

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
		paramMap.put("planType", planType);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByJgiNo", paramMap);
	}

	// 従業員番号、品目固定コード、計画立案区分を元に特定施設個別計画の最終更新日時を取得
	public Date getUpdateByJgiNo(Integer jgiNo, String prodCode, PlanType planType) {

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
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("planType", planType);

		// ----------------------
		// 検索実行
		// ----------------------
		try {
			return dataAccess.queryForObject(getSqlMapName() + ".getUpDateByJgiNo", paramMap);
		} catch (DataNotFoundException e) {
			return null;
		}
	}

	// 従業員番号、品目固定コード、計画立案区分を元に特定施設個別計画を削除
	public int deleteByJgiNo(Integer jgiNo, String prodCode, PlanType planType) {
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
		paramMap.put("planType", planType);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".deleteByJgiNo", paramMap);
	}

	// 組織コード(営業所)、品目固定コード、計画立案区分、施設出力対象区分を元に特定施設個別計画を取得
	public List<SpecialInsPlan> searchBySosCd(String sortString, String sosCd3, String prodCode, PlanType planType, InsType insType, boolean isVaccine, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		plannedCtg = dpsPlannedCtgDao.search(category);

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
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);
		paramMap.put("planType", planType);
//		paramMap.put("hoInsTypeList", InsType.convertHoInsType(insType));
		List<HoInsType> hoInsTypeList = new ArrayList<HoInsType>();
		if (insType == InsType.P && isVaccine) {
			// P雑
			hoInsTypeList.add(HoInsType.P);
			hoInsTypeList.add(HoInsType.Z);
		} else if(insType == InsType.P) {
			// P
			hoInsTypeList.add(HoInsType.P);
		} else if (insType == InsType.UH) {
			// UH
			hoInsTypeList.add(HoInsType.U);
			hoInsTypeList.add(HoInsType.H);
		}
		paramMap.put("hoInsTypeList", hoInsTypeList);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(),oyakoKbProdCode));

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySosCd", paramMap);
	}

	// 組織コード(営業所)、品目固定コード、計画立案区分を元に特定施設個別計画を削除
	public int deleteBySosCd(String sosCd3, String prodCode, PlanType planType) {
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
		paramMap.put("planType", planType);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.execute(getSqlMapName() + ".deleteBySosCd", paramMap);
	}
}
