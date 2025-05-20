package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageIyakuMonthPlanDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.ProdCategoryDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.ProdMonthPlanResultDetailDto;
import jp.co.takeda.dto.ProdMonthPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.model.Cal;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.ManageIyakuMonthPlan;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;

/**
 * 管理の組織別計画(全社)の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmIyakuMonthPlanSearchService")
public class DpmIyakuMonthPlanSearchServiceImpl implements DpmIyakuMonthPlanSearchService {

	/**
	 * 全社計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageIyakuMonthPlanDao")
	protected ManageIyakuMonthPlanDao manageIyakuMonthPlanDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("masterManagePlannedProdDao")
	protected MasterManagePlannedProdDao managePlannedProdDao;

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamYTDao")
	protected ManageChangeParamYTDao manageChangeParamYTDao;

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 計画管理汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("codeMasterDao")
	protected CodeMasterDao codeMasterDao;

	/**
	 * 品目分類DAO
	 */
	@Autowired(required = true)
	@Qualifier("prodCategoryDao")
	protected ProdCategoryDao prodCategoryDao;

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
    /**
     * 管理の共通サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmCommonService")
    protected DpmCommonService dpmCommonService;
 // add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

    // 品目別月別計画(全社)取得
	public ProdMonthPlanResultDto searchSosProdPlan(ProdPlanScDto scDto, List<String> jrnsCtgList) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "組織別品目別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getProdCategory() == null) {
			final String errMsg = "検索対象のカテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String category = scDto.getProdCategory();
		String vaccineCode = scDto.getVaccineCode();

		// リンクからの遷移時はワクチンコードがnullになるので、DBから取得
		if (StringUtils.isEmpty(vaccineCode)) {
			// ワクチンコードの検索
			List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
			try {
				// カテゴリの検索
				searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
			} catch (DataNotFoundException e) {
				final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
			vaccineCode = searchList.get(0).getDataCd();
		}

		// JRNS判定
		List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.JRNS.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.JRNS.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		// JRNSのカテゴリコード
		String jrnsCategory = searchList.get(0).getDataCd();
		// JRNSの品目分類コード
		String jrnsPcatCd = String.valueOf(searchList.get(0).getDataValue());
		// カテゴリがJRNSかどうか
		boolean isJrns = false;
		if (category.equals(jrnsCategory)) {
			isJrns = true;
		}
		// ----------------------
		// 全社別計画取得
		// ----------------------
		// 6ヶ月分の全社計画を取得
		List<ManageIyakuMonthPlan> iyakuPlanList = manageIyakuMonthPlanDao.searchList(ManageIyakuMonthPlanDao.SORT_STRING, category, vaccineCode, isJrns, jrnsPcatCd, jrnsCtgList);

		// CVコードの検索
		searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCodeByDataKbn(CodeMaster.CV.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.CV.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		String cvCode = searchList.get(0).getDataCd();

		// カテゴリがワクチン・CVの場合は、もう一方の6ヶ月分の全社計画の集計を取得
		ManageIyakuMonthPlan otherPlanSum = new ManageIyakuMonthPlan();

		if (category.equals(vaccineCode)) {
			try {
				otherPlanSum = manageIyakuMonthPlanDao.searchPlanSum(cvCode, vaccineCode);
			} catch (DataNotFoundException e) {
				// データ０件の場合は何もしない
			}
		} else if (category.equals(cvCode)) {
			try {
				otherPlanSum = manageIyakuMonthPlanDao.searchPlanSum(vaccineCode, vaccineCode);
			} catch (DataNotFoundException e) {
				// データ０件の場合は何もしない
			}
		}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		int tougetuCount = TougetuHantei();

		if(category.equals(vaccineCode)) {
			otherPlanSum.getImplMonthPlan().setRecord1ValueYb(otherPlanSum.getImplMonthPlan().getRecord1ValueB());
			otherPlanSum.getImplMonthPlan().setRecord2ValueYb(otherPlanSum.getImplMonthPlan().getRecord2ValueB());
			otherPlanSum.getImplMonthPlan().setRecord3ValueYb(otherPlanSum.getImplMonthPlan().getRecord3ValueB());
			otherPlanSum.getImplMonthPlan().setRecord4ValueYb(otherPlanSum.getImplMonthPlan().getRecord4ValueB());
			otherPlanSum.getImplMonthPlan().setRecord5ValueYb(otherPlanSum.getImplMonthPlan().getRecord5ValueB());
			otherPlanSum.getImplMonthPlan().setRecord6ValueYb(otherPlanSum.getImplMonthPlan().getRecord6ValueB());
			otherPlanSum.getImplMonthPlan().setRecordTotalValueYb(otherPlanSum.getImplMonthPlan().getRecordTotalValueB());
		} else {
			otherPlanSum.getImplMonthPlan().setRecord1ValueYb(otherPlanSum.getImplMonthPlan().getRecord1ValueY());
			otherPlanSum.getImplMonthPlan().setRecord2ValueYb(otherPlanSum.getImplMonthPlan().getRecord2ValueY());
			otherPlanSum.getImplMonthPlan().setRecord3ValueYb(otherPlanSum.getImplMonthPlan().getRecord3ValueY());
			otherPlanSum.getImplMonthPlan().setRecord4ValueYb(otherPlanSum.getImplMonthPlan().getRecord4ValueY());
			otherPlanSum.getImplMonthPlan().setRecord5ValueYb(otherPlanSum.getImplMonthPlan().getRecord5ValueY());
			otherPlanSum.getImplMonthPlan().setRecord6ValueYb(otherPlanSum.getImplMonthPlan().getRecord6ValueY());
			otherPlanSum.getImplMonthPlan().setRecordTotalValueYb(otherPlanSum.getImplMonthPlan().getRecordTotalValueY());
		}

		switch (tougetuCount) {
		case 0:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord1ValueYb());
			break;
		case 1:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord2ValueYb());
			break;
		case 2:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord3ValueYb());
			break;
		case 3:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord4ValueYb());
			break;
		case 4:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord5ValueYb());
			break;
		case 5:
			otherPlanSum.getImplMonthPlan().setRecordTougetuValueYb(otherPlanSum.getImplMonthPlan().getRecord6ValueYb());
			break;
		default:
			break;
		}
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		// 計画集計DTO作成
		ProdMonthPlanResultDetailDto otherPlanSumDto = new ProdMonthPlanResultDetailDto(otherPlanSum, Boolean.FALSE);

		// 品目分類名
		String prodCategoryName = prodCategoryDao.searchProdCategoryName(category, isJrns, jrnsPcatCd);

		// 明細行作成
		List<ProdMonthPlanResultDetailDto> detailList = new ArrayList<ProdMonthPlanResultDetailDto>();

		for (ManageIyakuMonthPlan monthPlan : iyakuPlanList) {
			// ----------------------------------------------------
			// 下位立案(支店別計画)に対する品目立案レベル取得
			// ----------------------------------------------------
			boolean canMovePlanLevel;
			try {
				MasterManagePlannedProd plannedProd = managePlannedProdDao.search(monthPlan.getProdCode());
				canMovePlanLevel = plannedProd.getPlanLevelSiten();
			} catch (DataNotFoundException e1) {
				final String msg = "全社別計画取得に失敗(データ不整合)。" + "category" + category;
				throw new SystemException(new Conveyance(LOGICAL_ERROR, msg));
			}

// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			if(category.equals(vaccineCode)) {
				monthPlan.getImplMonthPlan().setRecordTotalValueYb(monthPlan.getImplMonthPlan().getRecordTotalValueB());
				monthPlan.getImplMonthPlan().setRecord1ValueYb(monthPlan.getImplMonthPlan().getRecord1ValueB());
				monthPlan.getImplMonthPlan().setRecord2ValueYb(monthPlan.getImplMonthPlan().getRecord2ValueB());
				monthPlan.getImplMonthPlan().setRecord3ValueYb(monthPlan.getImplMonthPlan().getRecord3ValueB());
				monthPlan.getImplMonthPlan().setRecord4ValueYb(monthPlan.getImplMonthPlan().getRecord4ValueB());
				monthPlan.getImplMonthPlan().setRecord5ValueYb(monthPlan.getImplMonthPlan().getRecord5ValueB());
				monthPlan.getImplMonthPlan().setRecord6ValueYb(monthPlan.getImplMonthPlan().getRecord6ValueB());
			} else {
				monthPlan.getImplMonthPlan().setRecordTotalValueYb(monthPlan.getImplMonthPlan().getRecordTotalValueY());
				monthPlan.getImplMonthPlan().setRecord1ValueYb(monthPlan.getImplMonthPlan().getRecord1ValueY());
				monthPlan.getImplMonthPlan().setRecord2ValueYb(monthPlan.getImplMonthPlan().getRecord2ValueY());
				monthPlan.getImplMonthPlan().setRecord3ValueYb(monthPlan.getImplMonthPlan().getRecord3ValueY());
				monthPlan.getImplMonthPlan().setRecord4ValueYb(monthPlan.getImplMonthPlan().getRecord4ValueY());
				monthPlan.getImplMonthPlan().setRecord5ValueYb(monthPlan.getImplMonthPlan().getRecord5ValueY());
				monthPlan.getImplMonthPlan().setRecord6ValueYb(monthPlan.getImplMonthPlan().getRecord6ValueY());
			}


			switch (tougetuCount){
			case 0:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord1ValueYb());
				break;
			case 1:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord2ValueYb());
				break;
			case 2:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord3ValueYb());
				break;
			case 3:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord4ValueYb());
				break;
			case 4:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord5ValueYb());
				break;
			case 5:
				monthPlan.getImplMonthPlan().setRecordTougetuValueYb(monthPlan.getImplMonthPlan().getRecord6ValueYb());
				break;
			default:
				break;
			}
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

			// DTO作成
			ProdMonthPlanResultDetailDto detail = new ProdMonthPlanResultDetailDto(monthPlan, canMovePlanLevel);
			detailList.add(detail);
		}
		return new ProdMonthPlanResultDto(prodCategoryName, otherPlanSumDto, detailList);
	}

//  add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	private int TougetuHantei() {
		// カレンダーから今期の何月目かを設定（falseCountに何月目かを記録（1月目なら0が入る）
		Cal today = dpmCommonService.searchToday();
		String calYear = today.getCalYear();
		String calMonth = today.getCalMonth();

		int calYearInt = Integer.parseInt(calYear);
		int calMonthInt = Integer.parseInt(calMonth);

		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		String sysYear = sysManage.getSysYear();
		int sysYearInt = Integer.parseInt(sysYear);
		Term sysTerm = sysManage.getSysTerm();

		int falseCount = 6;

		switch (sysTerm) {
		case FIRST: // 4,5,6,7,8,9
			if(!sysYear.equals(calYear)) {
				break;
			}
			falseCount = calMonthInt - 4;
			break;
		case SECOND:// 10,11,12,1,2,3
			Integer.parseInt(sysYear);
			if(calYearInt == sysYearInt || calYearInt == sysYearInt + 1) {
				/* do nothing */
			}else {
				break;
			}
			if (calMonthInt < 10) calMonthInt = calMonthInt + 6;
			falseCount = calMonthInt - 10;
			break;
		default:
			break;
		}
		return falseCount;
	}
//  add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

}
