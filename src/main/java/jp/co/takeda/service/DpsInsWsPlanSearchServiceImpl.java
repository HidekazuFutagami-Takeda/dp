package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultDao;
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.DistParamHonbuDao;
import jp.co.takeda.dao.DistParamOfficeDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.DpsSyComInsOyakoDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.InsPlanDao;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.InsWsPlanForRefDao;
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.InsWsPlanJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanJgiListScDto;
import jp.co.takeda.dto.InsWsPlanMrResultDto;
import jp.co.takeda.dto.InsWsPlanProdListResultDto;
import jp.co.takeda.dto.InsWsPlanProdListScDto;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.dto.InsWsPlanTeamResultDto;
import jp.co.takeda.dto.InsWsPlanUpDateMonNnuScDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultMonNnuDto;
import jp.co.takeda.dto.InsWsPlanUpDateScDto;
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.CheckInsWsDispLogic;
import jp.co.takeda.logic.CreateInsWsPlanMrListLogic;
import jp.co.takeda.logic.CreateInsWsPlanProdListLogic;
import jp.co.takeda.logic.CreateInsWsPlanUpDateResultListDtoLogic;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DeliveryResult;
import jp.co.takeda.model.DistParamHonbu;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.InsPlan;
import jp.co.takeda.model.InsWsCount;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.InsWsPlanForRef;

/**
 * 施設特約店別計画機能の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsInsWsPlanSearchService")
public class DpsInsWsPlanSearchServiceImpl implements DpsInsWsPlanSearchService {

	/**
	 * 施設特約店別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusDao")
	protected InsWsPlanStatusDao insWsPlanStatusDao;

	/**
	 * 施設医師別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanStatusDao")
	protected InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * 担当者別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 担当者別納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultMrDao")
	protected DeliveryResultMrDao deliveryResultMrDao;

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insPlanDao")
	protected InsPlanDao insPlanDao;

	/**
	 * 施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanDao")
	protected InsWsPlanDao insWsPlanDao;

	/**
	 * 計画対象品目取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 従業員情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 組織情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 施設情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 特約店情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	/**
	 * 配分基準（本部）取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamHonbuDao")
	protected DistParamHonbuDao distParamHonbuDao;

	/**
	 * 配分基準（営業所）取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamOfficeDao")
	protected DistParamOfficeDao distParamOfficeDao;

	/**
	 * 参照用の施設特約店別計画取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForRefDao")
	protected InsWsPlanForRefDao insWsPlanForRefDao;

	/**
	 * 納入実績取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultDao")
	protected DeliveryResultDao deliveryResultDao;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * ワクチン用施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckForVacService")
	protected DpsInsWsStatusCheckForVacService dpsInsWsStatusCheckForVacService;

	/**
	 * 計画対象カテゴリ領域検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgSearchService")
	protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

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

	// 施設特約店別計画 担当者一覧情報取得
	public InsWsPlanJgiListResultDto searchMrList(InsWsPlanJgiListScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "計画対象品目の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd3() == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getProdCode() == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();
		String prodCode = scDto.getProdCode();

		// 営業所コードから組織情報取得
		SosMst sos;
		try {
			sos = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd3;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 品目情報取得
		PlannedProd plannedProd = new PlannedProd();
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// 担当者別計画ステータス取得
		MrPlanStatus mrPlanStatus = null;
		try {
			mrPlanStatus = mrPlanStatusDao.search(sosCd3, prodCode);
		} catch (DataNotFoundException e) {
		}

		// ------------------------------
		// チーム情報取得
		// ------------------------------
		List<SosMst> teamSosMstList = new ArrayList<SosMst>();
		// ワクチンの場合
		if (dpsCodeMasterSearchService.isVaccine(plannedProd.getCategory())) {
			// ------------------------------
			// エリア特約店G一覧取得
			// ------------------------------
			try {
				// エリア特約店が指定されている場合
				if (!StringUtils.isEmpty(sosCd3)) {
					SosMst sosMst = sosMstDAO.search(sosCd3);
					teamSosMstList.add(sosMst);
					// 全件
				} else {
					teamSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, null);
				}
			} catch (DataNotFoundException e) {
				// エラーにしない
			}
			// ワクチン以外の場合
		} else {
		if (StringUtils.isEmpty(sosCd4)) {
			try {
				// 配下のチーム一覧取得
				teamSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);

			} catch (DataNotFoundException e) {

				// チームがない場合は営業所
				SosMst sosMst = sosMstDAO.search(sosCd3);
				teamSosMstList.add(sosMst);
			}

		} else {
			SosMst sosMst = sosMstDAO.search(sosCd4);
			teamSosMstList.add(sosMst);
		}
		}

		// サマリ生成ロジック
		CreateInsWsPlanMrListLogic createLogic = new CreateInsWsPlanMrListLogic(prodCode, deliveryResultMrDao, mrPlanDao, insPlanDao, insWsPlanDao, insDocPlanStatusDao, dpsCodeMasterSearchService, dpsPlannedCtgSearchService);

		// チーム単位で繰り返し
		List<InsWsPlanTeamResultDto> teamResultList = new ArrayList<InsWsPlanTeamResultDto>();
		for (SosMst sosMst : teamSosMstList) {

			String teamCode = sosMst.getSosCd();
			String teamName = "";

			if (dpsCodeMasterSearchService.isVaccine(plannedProd.getCategory())) {
				// ワクチンの場合は、部門名略式
				teamName = sosMst.getBumonRyakuName();
			} else {
				// 医薬の場合は、部門名正式
				teamName = sosMst.getBumonSeiName();
			}

			// 施設特約店別計画ステータス取得
			List<InsWsPlanStatus> insWsPlanStatusList = null;
			try {

				if(sosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){

					// 組織が営業所の場合はチームコードNULL
					insWsPlanStatusList = insWsPlanStatusDao.searchJgiBaseList(sosCd3, null, prodCode);

				} else {
					insWsPlanStatusList = insWsPlanStatusDao.searchJgiBaseList(sosCd3, teamCode, prodCode);
				}

			} catch (DataNotFoundException e) {
				continue;
			}

			// 削除施設数を取得
			List<InsWsCount> delInsCountList = insWsPlanDao.searchDelInsCount(sosCd3, null, prodCode, false);

			// 対象外特約店数を取得
			List<InsWsCount> taigaiTytenCountList = insWsPlanDao.searchTaiGaiTytenCount(sosCd3, null, prodCode, false);

			// 配分除外施設数を取得
			List<InsWsCount> exceptDistInsCountList = insWsPlanDao.searchExceptDistInsCount(sosCd3, null, prodCode, false);

			// 担当者単位に繰り返し
			List<InsWsPlanMrResultDto> mrResultDtoList = new ArrayList<InsWsPlanMrResultDto>();
			for (InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {

				// サマリ情報作成ロジック
				InsWsPlanMrResultDto mrResultDto = createLogic.execute(mrPlanStatus,insWsPlanStatus,plannedProd, delInsCountList, taigaiTytenCountList,
						exceptDistInsCountList, dpsCodeMasterSearchService.isVaccine(plannedProd.getCategory()));
				mrResultDtoList.add(mrResultDto);
			}

			// チームサマリ情報作成
			InsWsPlanTeamResultDto teamResultDto = new InsWsPlanTeamResultDto(teamCode, teamName, mrResultDtoList);
			teamResultList.add(teamResultDto);
		}

		if (teamResultList.size() == 0) {
			final String errMsg = "対象組織の担当者が存在しない:";
			throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + scDto));
		}

		return new InsWsPlanJgiListResultDto(plannedProd, teamResultList);
	}

	// 施設特約店別計画 品目一覧情報取得
	public List<InsWsPlanProdListResultDto> searchPlannedProdList(InsWsPlanProdListScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "計画対象品目の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd3() == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getCategory() == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();
		Integer jgiNo = scDto.getJgiNo();
		String category = scDto.getCategory();

		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd3;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ------------------------------
		// 施設特約店別計画ステータスサマリ取得
		// ------------------------------
		List<ProdInsWsPlanStatSummaryDto> insWsPlanStatusSummaryList = null;
		if (jgiNo != null) {

			// 従業員番号が指定されている場合は、施設特約店別計画ステータス取得
			insWsPlanStatusSummaryList = insWsPlanStatusDao.searchProdStatListByJgi(jgiNo, category);

		} else if (sosCd4 != null) {

			// 組織コード(チーム)が指定されている場合は、施設特約店別計画ステータスのチームサマリ取得
			insWsPlanStatusSummaryList = insWsPlanStatusDao.searchProdStatListByTeam(sosCd4, category);

		} else if (sosCd3 != null) {

			// 組織コード(営業所)が指定されている場合は、施設特約店別計画ステータスのチームサマリ取得
			insWsPlanStatusSummaryList = insWsPlanStatusDao.searchProdStatList(sosCd3, category);

		} else {

			final String errMsg = "施設特約店別計画ステータスサマリ取得条件が不正";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// サマリ情報作成ロジック
		CreateInsWsPlanProdListLogic createLogic = new CreateInsWsPlanProdListLogic(sosCd3, sosCd4, jgiNo, deliveryResultMrDao, mrPlanDao, insPlanDao, insWsPlanDao, mrPlanStatusDao,insDocPlanStatusDao,
				dpsCodeMasterSearchService, dpsPlannedCtgSearchService);

		// 品目単位に繰り返し
		List<InsWsPlanProdListResultDto> resultDtoList = new ArrayList<InsWsPlanProdListResultDto>();
		for (ProdInsWsPlanStatSummaryDto prodInsWsPlanStatSummaryDto : insWsPlanStatusSummaryList) {

			// 品目情報取得
			String prodCode = prodInsWsPlanStatSummaryDto.getProdCode();
			PlannedProd plannedProd;
			try {
				plannedProd = plannedProdDAO.search(prodCode);
			} catch (DataNotFoundException e) {
				final String errMsg = "計画品目情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
			}
			// 施設特約店別計画ステータス情報取得
			InsWsPlanStatus insWsPlanStatus = null;
			if (jgiNo != null) {
				try {
					insWsPlanStatus = insWsPlanStatusDao.search(jgiNo, prodCode);
				} catch (DataNotFoundException e) {
				}
			}

			InsWsPlanProdListResultDto resultDto = createLogic.execute(prodInsWsPlanStatSummaryDto, plannedProd, insWsPlanStatus);
			resultDtoList.add(resultDto);
		}

		if (resultDtoList.size() == 0) {
			final String errMsg = "対象組織の担当者が存在しない:";
			throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + scDto));
		}

		return resultDtoList;
	}

	// 施設特約店別計画一覧の検索処理
	public InsWsPlanUpDateResultListDto searchInsWsPlanList(InsWsPlanUpDateScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設特約店別計画の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		Integer jgiNo = scDto.getJgiNo();
		String prodCode = scDto.getProdCode();
		InsType insType = scDto.getInsType();
		String refProdCode = scDto.getRefProdCode();
		boolean refProdAllFlg = scDto.getRefProdAllFlg();
		String category = scDto.getCategory();
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// 従業員情報取得
		// ----------------------
		JgiMst jgiMst = null;
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
		} catch (DataNotFoundException e) {
			final String errMsg = "従業員情報取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------------------
		// 担当者別計画ステータス取得
		// ----------------------------------
		MrPlanStatus mrPlanStatus = null;
		try {
			mrPlanStatus = mrPlanStatusDao.search(jgiMst.getSosCd3(), prodCode);
		} catch (DataNotFoundException e) {
		}

		// ----------------------------------
		// 施設特約店別計画立案ステータス取得
		// ----------------------------------
		InsWsPlanStatus insWsPlanStatus = null;
		try {
			insWsPlanStatus = insWsPlanStatusDao.search(jgiNo, prodCode);
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		CheckInsWsDispLogic dipsLogic = new CheckInsWsDispLogic(insDocPlanStatusDao, dpsCodeMasterSearchService, dpsPlannedCtgSearchService);
		boolean dispEditLink = dipsLogic.dipsEditLink(jgiNo, plannedProd, mrPlanStatus, insWsPlanStatus);
		if(!dispEditLink){
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3338E"));
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

		// ----------------------
		// 配分基準情報取得
		// ----------------------
		BaseParam baseParam = null;
		try {
			DistParamOffice distParamOffice = distParamOfficeDao.search(jgiMst.getSosCd3(), prodCode, insType, DistributionType.INS_WS_PLAN);
			baseParam = distParamOffice.getBaseParam();
		} catch (DataNotFoundException e) {
			// 営業所案がない場合、本部案を取得
			try {
				DistParamHonbu distParamHonbu = distParamHonbuDao.search(prodCode, insType, DistributionType.INS_WS_PLAN);
				baseParam = distParamHonbu.getBaseParam();
			} catch (DataNotFoundException e2) {
				final String errMsg = "配分基準情報取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e2);
			}
		}

		// ----------------------
		// 取得対象の参照品目設定
		// ----------------------
		String resultRefProdCode1 = null;
		String resultRefProdCode2 = null;
		String resultRefProdCode3 = null;
		if (refProdAllFlg) {
			// 参照品目全表示フラグがONの場合、全品目
			resultRefProdCode1 = baseParam.getResultRefProdCode1();
			resultRefProdCode2 = baseParam.getResultRefProdCode2();
			resultRefProdCode3 = baseParam.getResultRefProdCode3();
		} else {
			// 指定された参照品目コード
			if (refProdCode != null) {
				if (refProdCode.equals(baseParam.getResultRefProdCode1())) {
					resultRefProdCode1 = baseParam.getResultRefProdCode1();
				} else if (refProdCode.equals(baseParam.getResultRefProdCode2())) {
					resultRefProdCode2 = baseParam.getResultRefProdCode2();
				} else if (refProdCode.equals(baseParam.getResultRefProdCode3())) {
					resultRefProdCode3 = baseParam.getResultRefProdCode3();
				} else {
					// いずれにも該当しない場合、resultRefProdCode1とする。
					resultRefProdCode1 = refProdCode;
				}
			}
		}

		// ----------------------
		// 担当者別計画取得
		// ----------------------
		MrPlan mrPlan = null;
		try {
			mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
		} catch (DataNotFoundException e) {
			mrPlan = new MrPlan();
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
		String oyakoKbProdCode = plannedProd.getProdCode();
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpsSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}

		// ----------------------
		// 施設特約店別計画取得
		// ----------------------
		List<InsWsPlanForRef> insWsList;
		try {
			insWsList = insWsPlanForRefDao.searchList(InsWsPlanForRefDao.SORT_STRING2, jgiNo, plannedProd, insType, resultRefProdCode1, resultRefProdCode2,
					resultRefProdCode3, DpsPlannedCtg.getOyakoKb(), DpsPlannedCtg.getOyakoKb2(), oyakoKbProdCode);
		} catch (DataNotFoundException e) {
			insWsList = new ArrayList<InsWsPlanForRef>();
		}

		// ----------------------------
		// 施設特約店別計画サマリー取得
		// ----------------------------
		InsWsPlanProdSummaryDto summaryDto;
		try {
			summaryDto = insWsPlanDao.searchProdSummary(null, null, jgiNo, prodCode, DpsPlannedCtg.getOyakoKb(), DpsPlannedCtg.getOyakoKb2(), oyakoKbProdCode);
		} catch (DataNotFoundException e) {
			summaryDto = new InsWsPlanProdSummaryDto(prodCode, null, null, null, null);
		}

		// ----------------------
		// 結果リスト生成
		// ----------------------
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という判定方法に変更
//		if((plannedProd.getCategory() == ProdCategory.MMP || plannedProd.getCategory() == ProdCategory.ONC ) && plannedProd.getPlanLevelInsDoc()){
		if((plannedProd.getCategory() != null && !dpsCodeMasterSearchService.isSiire(plannedProd.getCategory())) && plannedProd.getPlanLevelInsDoc()){
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という判定方法に変更

			// ----------------------
			// 重点品目の場合
			// ----------------------

			// 調整取得
			boolean existUhChosei = false;
			boolean existPChosei = false;
			try {
				Map<String, Object> choseiResult = insPlanDao.checkChoseiInsWs(null, null, jgiNo, plannedProd.getProdCode());
				existUhChosei = (Boolean)choseiResult.get("existDiffUh");
				existPChosei = (Boolean)choseiResult.get("existDiffP");
			} catch (DataNotFoundException e) {
			}

			// 施設別計画取得
			List<InsPlan> insList;
			try {
				insList = insPlanDao.searchList(InsPlanDao.SORT_STRING, jgiNo, plannedProd.getProdCode(), insType);
			} catch (DataNotFoundException e) {
				insList = new ArrayList<InsPlan>();
			}

			// 施設内部コードをキーに、計画を設定
			Map<String, InsPlan> insPlanMap = new HashMap<String, InsPlan>();
			for (InsPlan insPlan : insList) {
				insPlanMap.put(insPlan.getRelnInsNo(), insPlan);

			}

			// 結果作成
			CreateInsWsPlanUpDateResultListDtoLogic logic = new CreateInsWsPlanUpDateResultListDtoLogic(plannedProd, jgiMst, baseParam, insWsPlanStatus, mrPlan, insWsList, summaryDto,
					resultRefProdCode1, resultRefProdCode2, resultRefProdCode3);
			return logic.createPriorityProd(existUhChosei, existPChosei, insPlanMap);

		} else {

			// 結果作成
			CreateInsWsPlanUpDateResultListDtoLogic logic = new CreateInsWsPlanUpDateResultListDtoLogic(plannedProd, jgiMst, baseParam, insWsPlanStatus, mrPlan, insWsList, summaryDto,
					resultRefProdCode1, resultRefProdCode2, resultRefProdCode3);
			return logic.create();
		}
	}

	// 施設・特約店の実績検索処理
	public InsWsPlanUpDateResultDto searchJisseki(InsWsPlanUpDateMonNnuScDto monNnuScDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (monNnuScDto == null) {
			final String errMsg = "施設特約店別計画の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String prodCode = monNnuScDto.getProdCode();
		String insNo = monNnuScDto.getInsNo();
		String tmsTytenCd = monNnuScDto.getTmsTytenCd();
		String refProdCode1 = monNnuScDto.getRefProdCode1();
		String refProdCode2 = monNnuScDto.getRefProdCode2();
		String refProdCode3 = monNnuScDto.getRefProdCode3();
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 施設情報取得
		DpsInsMst insMst;
		try {
			insMst = insMstDAO.search(insNo);
		} catch (DataNotFoundException e) {
			final String errMsg = "施設情報取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// 特約店情報取得
		TmsTytenMstUn tmsTytenMstUn;
		try {
			tmsTytenMstUn = tmsTytenMstUnDAO.search(tmsTytenCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "特約店情報取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// 納入実績部の生成
		List<InsWsPlanUpDateResultMonNnuDto> monNnuList = new ArrayList<InsWsPlanUpDateResultMonNnuDto>();

		// 立案品目の実績は存在しない
		PlannedProd prod = plannedProdDAO.search(prodCode);
		monNnuList.add(new InsWsPlanUpDateResultMonNnuDto(0, prod.getProdCode(), prod.getProdName(), prod.getProdType(), new DeliveryResult()));

		// 参照品目1
		if (refProdCode1 != null) {
			monNnuList.add(create(1, refProdCode1, insNo, tmsTytenCd));
		}
		// 参照品目2
		if (refProdCode2 != null) {
			monNnuList.add(create(2, refProdCode2, insNo, tmsTytenCd));
		}
		// 参照品目3
		if (refProdCode3 != null) {
			monNnuList.add(create(3, refProdCode3, insNo, tmsTytenCd));
		}

		return new InsWsPlanUpDateResultDto(insMst, tmsTytenMstUn, monNnuList);

	}

	/**
	 * 過去実績参照部を生成する。
	 *
	 * @param refProdNumber 実績参照品目番号
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @param tmsTytenCd 特約店コード
	 * @return 過去実績参照部
	 */
	protected InsWsPlanUpDateResultMonNnuDto create(Integer refProdNumber, String prodCode, String insNo, String tmsTytenCd) {
		DeliveryResult result;
		try {
			result = deliveryResultDao.search(prodCode, insNo, tmsTytenCd);
			// 存在しない場合、空データを格納
		} catch (DataNotFoundException e) {
			result = new DeliveryResult();
		}
		return new InsWsPlanUpDateResultMonNnuDto(refProdNumber, result.getProdCode(), result.getProdName(), result.getProdType(), result);
	}

}
