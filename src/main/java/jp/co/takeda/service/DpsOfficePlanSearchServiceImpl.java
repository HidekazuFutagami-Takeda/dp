package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ChoseiDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SpecialInsPlanSosSummaryDao;
import jp.co.takeda.dto.OfficePlanResultDto;
import jp.co.takeda.dto.OfficePlanResultListDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficeChosei;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.OfficePlanWithChosei;
import jp.co.takeda.model.view.SpecialInsPlanSosSummary;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;

/**
 * 営業所計画の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsOfficePlanSearchService")
public class DpsOfficePlanSearchServiceImpl implements DpsOfficePlanSearchService {

	/**
	 * 特定施設個別計画報DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanSosSummaryDao")
	protected SpecialInsPlanSosSummaryDao specialInsPlanSosSummaryDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * 営業所計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 営業所調整DAO
	 */
	@Autowired(required = true)
	@Qualifier("choseiDao")
	protected ChoseiDao choseiDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;


	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;


	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 担当者別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsOfficePlanSearchServiceImpl.class);

	public OfficePlanResultListDto searchOfficePlan(String sosCd3, String category, boolean updateFlg) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd3;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 組織コードから従業員一覧を取得
		List<JgiMst> jgiMst = new ArrayList<JgiMst>();
		try {
			jgiMst = jgiMstDAO.searchJgiList(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象者が存在しない：" + sosCd3;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ----------------------
		// 調整金額を更新
		// ----------------------
		if (updateFlg) {
			try {
				choseiDao.updateChosei(sosCd3);
			} catch (Exception e) {
				if (LOG.isErrorEnabled()) {
					LOG.error("営業所調整金額作成の更新に失敗。sosCd3=" + sosCd3, e);
				}
			}
		}

		// ----------------------
		// 調整金額更新日時を取得
		// ----------------------
		Date choseiUpdate = null;
		try {
			choseiUpdate = choseiDao.searchMaxUpdate(sosCd3, category);
			if (LOG.isDebugEnabled()) {
				LOG.debug("調整金額更新日時=>" + DateUtil.toString(choseiUpdate, "yyyy/MM/dd HH:mm:ss"));
			}
		} catch (DataNotFoundException e) {
			// do nothing
			if (LOG.isInfoEnabled()) {
				LOG.info("調整金額更新日時の取得で該当データなし");
			}
		}

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		if(category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
		}else{
			sales = Sales.IYAKU;
		}

		// ----------------------
		// 営業所計画を取得
		// ----------------------
		List<OfficePlanWithChosei> officePlanWithChoseiList = null;
		try {
			officePlanWithChoseiList = officePlanDao.searchList2(OfficePlanDao.SORT_STRING2, sosCd3, category, sales);
		} catch (DataNotFoundException e) {
//			final String errMsg = "計画対象品目(MMP品)が存在しない";
//			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR));
		}

		//営業所計画の品目を取得
		List<String> prodList = new ArrayList<String>();

		OfficePlan officePlan = null;
		OfficeChosei officeChosei = null;
		// 結果DTOのリスト
		List<OfficePlanResultDto> resultList = new ArrayList<OfficePlanResultDto>();

		if(officePlanWithChoseiList != null) {
			for (OfficePlanWithChosei officePlanWithChosei : officePlanWithChoseiList) {

				long plannedValueU = 0L;
				long plannedValueH = 0L;

				officePlan = officePlanWithChosei.getOfficePlan();
				officeChosei = officePlanWithChosei.getOfficeChosei();
				//営業所計画の品目を取得
				prodList.add(officePlanWithChosei.getOfficePlan().getProdCode());

				try {

					// 組織コード、品目、対象区分で集約した特定施設個別計画を取得する
					List<SpecialInsPlanSosSummary> specialInsPlanSosSummaryList = specialInsPlanSosSummaryDao.searchSosSummaryList(sosCd3, officePlan.getProdCode());

					for (SpecialInsPlanSosSummary specialInsPlanSosSummary : specialInsPlanSosSummaryList) {

						switch (specialInsPlanSosSummary.getHoInsType()) {
							case U:
								plannedValueU = ConvertUtil.parseMoneyToThousandUnit(specialInsPlanSosSummary.getSummaryY());
								break;
							case H:
								plannedValueH = ConvertUtil.parseMoneyToThousandUnit(specialInsPlanSosSummary.getSummaryY());
								break;
						}
					}

				} catch (DataNotFoundException e) {
					// 特定施設個別計画が存在しない場合、エラーにしない
				}

				// 結果DTOをリストに追加
				Date upDate = officePlan.getUpDate();
				String upUserName = null;
				if (upDate != null) {
					upUserName = officePlan.getUpJgiName();
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug("OfficeMrChoseiUhY=>" + officeChosei.getOfficeMrChoseiUhY());
					LOG.debug("OfficeInswsChoseiUhY =>" + officeChosei.getOfficeInswsChoseiUhY());
					LOG.debug("MrInswsChoseiUhFlg =>" + officeChosei.getMrInswsChoseiUhFlg());
					LOG.debug("OfficeMrChoseiPY=>" + officeChosei.getOfficeMrChoseiPY());
					LOG.debug("OfficeInswsChoseiPY =>" + officeChosei.getOfficeInswsChoseiPY());
					LOG.debug("MrInswsChoseiPFlg =>" + officeChosei.getMrInswsChoseiPFlg());
					LOG.debug("OfficeMrChoseiUHPY =>" + officeChosei.getOfficeMrChoseiUHPY());
					LOG.debug("OfficeInswsChoseiUHPY =>" + officeChosei.getOfficeInswsChoseiUHPY());
					LOG.debug("MrInswsChoseiUHPFlg =>" + officeChosei.getMrInswsChoseiUHPFlg());
					LOG.debug("StatusForMrPlan =>" + officePlanWithChosei.getStatusForMrPlan());
					LOG.debug("MrDrChoseiUhFlg =>" + officeChosei.getMrDrChoseiUhFlg());
					LOG.debug("MrDrChoseiPFlg =>" + officeChosei.getMrDrChoseiPFlg());
					LOG.debug("MrDrChoseiUHPFlg =>" + officeChosei.getMrDrChoseiUHPFlg());
				}

				OfficePlanResultDto resultDto = new OfficePlanResultDto(officePlan.getSeqKey(), officePlan.getProdCode(), officePlan.getProdInfo().getProdName(), officePlan.getProdInfo().getPlanLevelInsDoc(), ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getPlannedValueUhY()), ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedValuePY()), upDate, upUserName,
					plannedValueU, plannedValueH, ConvertUtil.parseMoneyToThousandUnit(officeChosei.getOfficeMrChoseiUhY()), ConvertUtil.parseMoneyToThousandUnit(officeChosei
						.getOfficeInswsChoseiUhY()), officeChosei.getMrInswsChoseiUhFlg(), ConvertUtil.parseMoneyToThousandUnit(officeChosei.getOfficeMrChoseiPY()), ConvertUtil
						.parseMoneyToThousandUnit(officeChosei.getOfficeInswsChoseiPY()), officeChosei.getMrInswsChoseiPFlg(), ConvertUtil.parseMoneyToThousandUnit(officeChosei
						.getOfficeMrChoseiUHPY()), ConvertUtil.parseMoneyToThousandUnit(officeChosei.getOfficeInswsChoseiUHPY()), officeChosei.getMrInswsChoseiUHPFlg(),
					officePlanWithChosei.getStatusForMrPlan(), officeChosei.getMrDrChoseiUhFlg(), officeChosei.getMrDrChoseiPFlg(), officeChosei.getMrDrChoseiUHPFlg());
				resultList.add(resultDto);
			}
		}

		// ------------------------------------------
		// 営業所計画立案ステータス(試算タイプ）取得
		// ------------------------------------------
		CalcType calcType;
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, category);
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
			calcType = null;
		}

		// ------------------------------------------
		// ワクチン編集の取得
		// ------------------------------------------
		boolean vacEditFlg = true;
		//ワクチンの場合のみ実施
		if(category.equals(vaccineCode)) {
			List<MrPlan> mrPlanList = new ArrayList<MrPlan>();
			try {
				mrPlanList = mrPlanDao.searchJgiNoProdList(jgiMst, prodList);
			} catch (DataNotFoundException e) {
				// 担当者別計画が存在しない場合、エラーにしない
			}
			// 担当者別計画立案ステータス取得
			List<MrPlanStatus> mrPlanStatus = null;
			try {
				mrPlanStatus = mrPlanStatusDao.searchSosCdProdList(sosCd3, prodList);
			} catch (DataNotFoundException e) {
				// ない場合も可
			}
			//担当者別計画が存在し、担当者別計画立案ステータスが存在しない場合、編集不可
			if(mrPlanList.size() != 0 && mrPlanStatus == null) {
				vacEditFlg = false;
			}
		}

		return new OfficePlanResultListDto(sosCd3, category, calcType, resultList, choseiUpdate, vacEditFlg);
	}
}
