package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

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
import jp.co.takeda.dao.DeliveryResultForVacDao;
import jp.co.takeda.dao.DeliveryResultMrForVacDao;
import jp.co.takeda.dao.DistParamHonbuDao;
import jp.co.takeda.dao.DistParamHonbuForVacDao;
import jp.co.takeda.dao.DistParamOfficeDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.InsWsPlanForVacDao;
import jp.co.takeda.dao.InsWsPlanForVacForRefDao;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.InsWsPlanForVacJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacJgiListScDto;
import jp.co.takeda.dto.InsWsPlanForVacMrResultDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListScDto;
import jp.co.takeda.dto.InsWsPlanForVacTeamResultDto;
import jp.co.takeda.dto.InsWsPlanForVacTokuGResultDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateScDto;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.dto.InsWsPlanUpDateMonNnuScDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultMonNnuDto;
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.CreateInsWsPlanForVacMrListLogic;
import jp.co.takeda.logic.CreateInsWsPlanForVacProdListLogic;
import jp.co.takeda.logic.CreateInsWsPlanForVacUpDateResultListDtoLogic;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DeliveryResult;
import jp.co.takeda.model.DeliveryResultForVac;
import jp.co.takeda.model.DistParamHonbu;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.view.InsWsPlanForVacForRef;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * ワクチン用施設特約店別計画機能の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsInsWsPlanForVacSearchService")
public class DpsInsWsPlanForVacSearchServiceImpl implements DpsInsWsPlanForVacSearchService {

	/**
	 * ワクチン用施設特約店別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusForVacDao")
	protected InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	/**
	 * ワクチン用担当者別納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultMrForVacDao")
	protected DeliveryResultMrForVacDao deliveryResultMrForVacDao;

	/**
	 * ワクチン用担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanForVacDao")
	protected MrPlanForVacDao mrPlanForVacDao;

	/**
	 * ワクチン用ワクチン用施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForVacDao")
	protected InsWsPlanForVacDao insWsPlanForVacDao;

	/**
	 * 計画対象品目取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 組織情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 従業員情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

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
	 * ワクチン用配分基準（本部）取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamHonbuForVacDao")
	protected DistParamHonbuForVacDao distParamHonbuForVacDao;

	/**
	 * 参照用のワクチン用施設特約店別計画取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForVacForRefDao")
	protected InsWsPlanForVacForRefDao insWsPlanForVacForRefDao;

	/**
	 * ワクチン用納入実績取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultForVacDao")
	protected DeliveryResultForVacDao deliveryResultForVacDao;

	/**
	 * ワクチン用施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckForVacService")
	protected DpsInsWsStatusCheckForVacService dpsInsWsStatusCheckForVacService;

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanDao")
	protected InsWsPlanDao insWsPlanDao;

	/**
	 * 配分基準（営業所）取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamOfficeDao")
	protected DistParamOfficeDao distParamOfficeDao;

	/**
	 * 配分基準（本部）取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamHonbuDao")
	protected DistParamHonbuDao distParamHonbuDao;

	/**
	 * 計画対象カテゴリ領域取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	// ワクチン用施設特約店別計画 担当者一覧情報取得
	public InsWsPlanForVacJgiListResultDto searchMrList(InsWsPlanForVacJgiListScDto scDto) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "計画対象品目の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getProdCode() == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String prodCode = scDto.getProdCode();
		String sosCd2 = scDto.getSosCd2();
		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();

		// 品目情報取得
		PlannedProd plannedProd;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ------------------------------
		// エリア特約店G一覧取得
		// ------------------------------
		List<SosMst> tokuGSosMstList = new ArrayList<SosMst>();
		try {
			// エリア特約店が指定されている場合
			if (!StringUtils.isEmpty(sosCd3)) {
				SosMst sosMst = sosMstDAO.search(sosCd3);
				tokuGSosMstList.add(sosMst);

				// 特約店部が指定されている場合
			} else if (!StringUtils.isEmpty(sosCd2)) {
				tokuGSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, sosCd2);

				// 全件
			} else {
				tokuGSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, null);
			}
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// サマリ生成ロジック
		CreateInsWsPlanForVacMrListLogic createLogic = new CreateInsWsPlanForVacMrListLogic(prodCode, deliveryResultMrForVacDao, mrPlanForVacDao, insWsPlanForVacDao);

		// エリア特約店G単位で繰り返し
		List<InsWsPlanForVacTokuGResultDto> tokuGResultList = new ArrayList<InsWsPlanForVacTokuGResultDto>();
		for (SosMst sosMst : tokuGSosMstList) {

			String tokuGCode = sosMst.getSosCd();
			String tokuGName = sosMst.getBumonSeiName();

			// チーム情報取得
			List<SosMst> teamSosList = new ArrayList<SosMst>();
			try {
				teamSosList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, sosMst.getBumonRank(), tokuGCode);
			} catch (DataNotFoundException e) {
				// チームがないエリア特約店Gの場合は、空のチームをリストに追加しておく
				SosMst empTeam = new SosMst();
				empTeam.setSosCd(null);
				empTeam.setBumonSeiName(null);
				teamSosList.add(empTeam);
			}

			// チーム単位で繰返し
			List<InsWsPlanForVacTeamResultDto> teamResultList = new ArrayList<InsWsPlanForVacTeamResultDto>();
			for (SosMst team : teamSosList) {

				String teamCode = team.getSosCd();
				String teamName = team.getBumonSeiName();

				// チームコードが指定されている場合、指定外のチームはリストに入れない。
				if (!StringUtils.isEmpty(sosCd4)) {
					if (!sosCd4.equals(teamCode)) {
						continue;
					}
				}
				// ワクチン用施設特約店別計画ステータス取得
				List<InsWsPlanStatusForVac> insWsPlanStatusList = null;
				try {
					insWsPlanStatusList = insWsPlanStatusForVacDao.searchJgiBaseList(prodCode, tokuGCode, teamCode);
				} catch (DataNotFoundException e) {
					continue;
				}
				// 担当者単位に繰り返し
				List<InsWsPlanForVacMrResultDto> mrResultDtoList = new ArrayList<InsWsPlanForVacMrResultDto>();
				for (InsWsPlanStatusForVac insWsPlanStatus : insWsPlanStatusList) {

					// サマリ情報作成ロジック
					InsWsPlanForVacMrResultDto mrResultDto = createLogic.execute(insWsPlanStatus);
					mrResultDtoList.add(mrResultDto);
				}
				// チームサマリ情報作成
				InsWsPlanForVacTeamResultDto teamDto = new InsWsPlanForVacTeamResultDto(teamCode, teamName, mrResultDtoList);
				teamResultList.add(teamDto);
			}
			// エリア特約店Gサマリ情報作成
			InsWsPlanForVacTokuGResultDto tokuGResultDto = new InsWsPlanForVacTokuGResultDto(tokuGCode, tokuGName, teamResultList);
			tokuGResultList.add(tokuGResultDto);
		}

		if (tokuGResultList.size() == 0) {
			final String errMsg = "対象組織の担当者が存在しない:";
			throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + scDto));

		}

		return new InsWsPlanForVacJgiListResultDto(plannedProd, tokuGResultList);
	}

	// ワクチン用施設特約店別計画 品目一覧情報取得
	public List<InsWsPlanForVacProdListResultDto> searchPlannedProdList(InsWsPlanForVacProdListScDto scDto) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "計画対象品目の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd() == null && scDto.getJgiNo() == null) {
			final String errMsg = "組織コード(エリア特約店G)/従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String sosCd3 = scDto.getSosCd();
		Integer jgiNo = scDto.getJgiNo();

		// ------------------------------
		// ワクチン用施設特約店別計画ステータスサマリ取得
		// ------------------------------
		List<ProdInsWsPlanStatSummaryDto> insWsPlanStatusSummaryList = new ArrayList<ProdInsWsPlanStatSummaryDto>();
		try {

			if (jgiNo != null) {
				insWsPlanStatusSummaryList = insWsPlanStatusForVacDao.searchProdStatListByJgi(jgiNo);

			} else if (sosCd3 != null) {
				insWsPlanStatusSummaryList = insWsPlanStatusForVacDao.searchProdStatListBySos(sosCd3);

			} else {
				final String errMsg = "ワクチン用施設特約店別計画ステータスサマリ取得条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// サマリ情報作成ロジック
		CreateInsWsPlanForVacProdListLogic createLogic = new CreateInsWsPlanForVacProdListLogic(sosCd3, jgiNo, deliveryResultMrForVacDao, mrPlanForVacDao, insWsPlanForVacDao);

		// 品目単位に繰り返し
		List<InsWsPlanForVacProdListResultDto> resultDtoList = new ArrayList<InsWsPlanForVacProdListResultDto>();
		for (ProdInsWsPlanStatSummaryDto prodInsWsPlanStatSummaryDto : insWsPlanStatusSummaryList) {

			InsWsPlanForVacProdListResultDto resultDto = createLogic.execute(prodInsWsPlanStatSummaryDto);
			resultDtoList.add(resultDto);
		}

		if (resultDtoList.size() == 0) {
			final String errMsg = "対象組織の担当者が存在しない:";
			throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + scDto));

		}

		return resultDtoList;
	}

	// ワクチン用施設特約店別計画一覧の検索処理
	public InsWsPlanForVacUpDateResultListDto searchInsWsPlanList(InsWsPlanForVacUpDateScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "ワクチン用施設特約店別計画の検索用DTOがnull";
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

		// ----------------------
		// ステータスチェック
		// ----------------------
		// ログインユーザ情報
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		// 施設特約店別計画ステータスチェック
		// 許可しないステータスリスト作成
		List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
		if (dpUser.isMatch(JokenSet.WAKUTIN_AL, JokenSet.WAKUTIN_MR, JokenSet.TOKUYAKUTEN_G_MASTER, JokenSet.TOKUYAKUTEN_G_STAFF, JokenSet.TOKUYAKUTEN_G_TANTOU,
			JokenSet.TOKUYAKUTEN_GYOUMU_G, JokenSet.TOKUYAKUTEN_MASTER)) {
			// (小児科MR/特約店Gの場合、公開中・確定以外はだめ) (J19-0010 対応・コメントのみ修正)
			unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
			// チェック実行
			List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
			jgiMstList.add(jgiMst);
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(plannedProd);
			try {
				dpsInsWsStatusCheckForVacService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);
			} catch (UnallowableStatusException e) {
				// メッセージ作成
				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3283E"));
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
				throw new UnallowableStatusException(new Conveyance(messageKeyList));
			}
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

		// ----------------------------------
		// 施設特約店別計画立案ステータス取得
		// ----------------------------------
		InsWsPlanStatusForVac insWsPlanStatusForVac = null;
		try {
			insWsPlanStatusForVac = insWsPlanStatusForVacDao.search(jgiNo, prodCode);
		} catch (DataNotFoundException e) {
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

		// ----------------------------
		// 施設特約店別計画サマリー取得
		// ----------------------------
		InsWsPlanProdSummaryDto summaryDto;
		try {
			summaryDto = insWsPlanDao.searchProdSummaryVac(null, null, jgiNo, prodCode);
		} catch (DataNotFoundException e) {
			summaryDto = new InsWsPlanProdSummaryDto(prodCode, null, null, null, null);
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg DpsPlannedCtg = new DpsPlannedCtg();
		DpsPlannedCtg = dpsPlannedCtgDao.search(category);

		// ----------------------
		// 施設特約店別計画取得
		// ----------------------
		List<InsWsPlanForVacForRef> insWsList;
		try {
			insWsList = insWsPlanForVacForRefDao
				.searchList(InsWsPlanForVacForRefDao.SORT_STRING_OYAKO, jgiNo, prodCode, null, resultRefProdCode1, resultRefProdCode2, resultRefProdCode3,
						insType, DpsPlannedCtg.getOyakoKb(), DpsPlannedCtg.getOyakoKb2());
		} catch (DataNotFoundException e) {
			insWsList = new ArrayList<InsWsPlanForVacForRef>();
		}

		// ----------------------
		// 結果リスト生成
		// ----------------------
		CreateInsWsPlanForVacUpDateResultListDtoLogic logic = new CreateInsWsPlanForVacUpDateResultListDtoLogic(plannedProd, jgiMst, baseParam, insWsPlanStatusForVac,
		insWsList, resultRefProdCode1, resultRefProdCode2, resultRefProdCode3, mrPlan, summaryDto);
		return logic.create();
	}

	// ワクチン用施設・特約店の実績検索処理
	public InsWsPlanForVacUpDateResultDto searchJisseki(InsWsPlanUpDateMonNnuScDto monNnuScDto) throws LogicalException {
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

		return new InsWsPlanForVacUpDateResultDto(insMst, tmsTytenMstUn, monNnuList);

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
		DeliveryResultForVac result;
		try {
			result = deliveryResultForVacDao.search(prodCode, insNo, tmsTytenCd);
			// 存在しない場合、空データを格納
		} catch (DataNotFoundException e) {
			result = new DeliveryResultForVac();
		}
		return new InsWsPlanUpDateResultMonNnuDto(refProdNumber, result.getProdCode(), result.getProdName(), null, result);
	}
}
