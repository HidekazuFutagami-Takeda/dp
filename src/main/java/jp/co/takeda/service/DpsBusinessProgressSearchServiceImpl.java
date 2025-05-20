package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.model.div.BumonRank.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ChangeParamBTDao;
import jp.co.takeda.dao.ChangeParamYTDao;
import jp.co.takeda.dao.ChangeParamYakkaDao;
import jp.co.takeda.dao.DpsCViSosCtgDao;
import jp.co.takeda.dao.JgiEachKindInfoDao;
import jp.co.takeda.dao.JgiEachKindInfoForVacDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.JgiStatusSummaryDao;
import jp.co.takeda.dao.JgiStatusSummaryForVacDao;
import jp.co.takeda.dao.SosEachKindInfoDao;
import jp.co.takeda.dao.SosEachKindInfoForVacDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SosStatusSummaryDao;
import jp.co.takeda.dao.SosStatusSummaryForVacDao;
import jp.co.takeda.dao.WsPlanStatusForVacDao;
import jp.co.takeda.dao.WsPlanStatusSummaryDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.ProgressEachKindInfoDto;
import jp.co.takeda.dto.ProgressEachKindInfoForVacDto;
import jp.co.takeda.dto.ProgressParamDto;
import jp.co.takeda.dto.ProgressParamForVacDto;
import jp.co.takeda.dto.ProgressPlanStatusDto;
import jp.co.takeda.dto.ProgressPlanStatusForVacDto;
import jp.co.takeda.dto.ProgressPlanWsStatusDto;
import jp.co.takeda.dto.ProgressPlanWsStatusForVacDto;
import jp.co.takeda.logic.ProgressIyakuSearchTypeLogic;
import jp.co.takeda.logic.ProgressIyakuSearchTypeLogic.ProgressIyakuSearchType;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.model.view.JgiEachKindInfo;
import jp.co.takeda.model.view.JgiEachKindInfoForVac;
import jp.co.takeda.model.view.JgiStatusSummary;
import jp.co.takeda.model.view.JgiStatusSummaryForVac;
import jp.co.takeda.model.view.SosEachKindInfo;
import jp.co.takeda.model.view.SosEachKindInfoForVac;
import jp.co.takeda.model.view.SosStatusSummary;
import jp.co.takeda.model.view.SosStatusSummaryForVac;
import jp.co.takeda.model.view.WsPlanStatusSummary;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 業務進捗状況を把握するサービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsBusinessProgressSearchService")
public class DpsBusinessProgressSearchServiceImpl implements DpsBusinessProgressSearchService {

	/**
	 * 当サービスクラスは、トップ画面でしか使用しない前提
	 */
	private static final String SCREEN_ID = "DPS000";

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsBusinessProgressSearchService.class);

	/**
	 * 薬価改定率DAO
	 */
	@Autowired(required = true)
	@Qualifier("changeParamYakkaDao")
	protected ChangeParamYakkaDao changeParamYakkaDao;

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("changeParamYTDao")
	protected ChangeParamYTDao changeParamYTDao;

	/**
	 * 変換パラメータ(B→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("changeParamBTDao")
	protected ChangeParamBTDao changeParamBTDao;

	/**
	 * (医)組織別各種登録状況DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosEachKindInfoDao")
	protected SosEachKindInfoDao sosEachKindInfoDao;

	/**
	 * (医)従業員別各種登録状況DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiEachKindInfoDao")
	protected JgiEachKindInfoDao jgiEachKindInfoDao;

	/**
	 * (医)組織単位のステータスのサマリDAO
	 */
	@Autowired(required = true)
	@Qualifier("sosStatusSummaryDao")
	protected SosStatusSummaryDao sosStatusSummaryDao;

	/**
	 * (医)担当者単位のステータスのサマリDAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiStatusSummaryDao")
	protected JgiStatusSummaryDao jgiStatusSummaryDao;

	/**
	 * (医)特約店別計画ステータスのサマリDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusSummaryDao")
	protected WsPlanStatusSummaryDao wsPlanStatusSummaryDao;

	/**
	 * (ワ)組織別各種登録状況DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosEachKindInfoForVacDao")
	protected SosEachKindInfoForVacDao sosEachKindInfoForVacDao;

	/**
	 * (ワ)従業員別各種登録状況DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiEachKindInfoForVacDao")
	protected JgiEachKindInfoForVacDao jgiEachKindInfoForVacDao;

	/**
	 * (ワ)組織単位のステータスのサマリDAO
	 */
	@Autowired(required = true)
	@Qualifier("sosStatusSummaryForVacDao")
	protected SosStatusSummaryForVacDao sosStatusSummaryForVacDao;

	/**
	 * (ワ)担当者単位のステータスのサマリDAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiStatusSummaryForVacDao")
	protected JgiStatusSummaryForVacDao jgiStatusSummaryForVacDao;

	/**
	 * (ワ)特約店別計画立案ステータスのDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusForVacDao")
	protected WsPlanStatusForVacDao wsPlanStatusForVacDao;

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
	 * 組織カテゴリDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCViSosCtgDao")
	protected DpsCViSosCtgDao dpsCViSosCtgDao;

	/**
	 * カテゴリ・品目の検索に関するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;


	// (医)[薬価改定/TY変換指定率(※)/SB変換指定率]を取得  ※ 表示としては TY は S/Y変換率 となる。
	public ProgressParamDto searchProgressParam(String calYear, Term calTerm) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isBlank(calYear)) {
			final String errMsg = "薬価改定/TY変換取得時に年度がnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (calTerm == null) {
			final String errMsg = "薬価改定/TY変換取得時に期がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索処理
		// ----------------------
		Date yakkaLastUpDate = changeParamYakkaDao.getLastUpDate(calYear, calTerm);
		Date tyLastUpDate = changeParamYTDao.getLastUpDateSy(calYear, calTerm);
		Date sbLastUpDate = changeParamYTDao.getLastUpDateSb(calYear, calTerm);
		return new ProgressParamDto(yakkaLastUpDate, tyLastUpDate,sbLastUpDate);
	}

	// (医)[各種登録状況]を取得
	public ProgressEachKindInfoDto searchProgressEachKindInfo() {
		List<SosEachKindInfo> sosList = new ArrayList<SosEachKindInfo>();
		List<JgiEachKindInfo> jgiList = new ArrayList<JgiEachKindInfo>();
		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		JgiMst dJgiMst = DpUserInfo.getDpUserInfo().getDefaultJgiMst();

		final String SOS_SORT = SosMstDAO.SORT_STRING;
		final SosListType LIST_TYPE = SosListType.SHITEN_LIST;

		ProgressIyakuSearchType iyakuType = ProgressIyakuSearchTypeLogic.getIyakuSearchType();
		ProgressEachKindInfoDto resultDto = null;

//		// 部門ランク３の場合、配下チームが存在するか
//		boolean existsTeam = true;
//		if(dSosMst != null && dSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){
//			try {
//				sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, dSosMst.getSosCd());
//			} catch (DataNotFoundException e) {
//				existsTeam = false;
//			}
//		}

//		// チーム一覧表示、かつ、チームが存在しない場合は、担当者表示に変更
//		if(iyakuType == ProgressIyakuSearchType.TEAM_LIST && existsTeam == false){
//			iyakuType = ProgressIyakuSearchType.TANTOU_LIST;
//		}

		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("*** iyakuType=" + iyakuType);
			}
			String sosCd = null;
			Integer jgiNo = null;
			switch (iyakuType) {

				// 支店リスト
				case SITEN_LIST:
					List<SosMst> sitenList = sosMstDAO.searchList(SOS_SORT, LIST_TYPE, IEIHON, SosMst.SOS_CD1);
					for (SosMst sosMst : sitenList) {
						try {
							sosCd = sosMst.getSosCd();
							sosList.add(sosEachKindInfoDao.searchShiten(sosCd));
						} catch (DataNotFoundException e) {
							handleDataNotFoundException(e, sosCd);
						}
					}
					resultDto = new ProgressEachKindInfoDto(SITEN_TOKUYAKUTEN_BU, sosList, jgiList);
					break;

				// 支店
				case SITEN:
					try {
						sosList.add(sosEachKindInfoDao.searchShiten(dSosMst.getSosCd()));
						resultDto = new ProgressEachKindInfoDto(SITEN_TOKUYAKUTEN_BU, sosList, jgiList);
					} catch (DataNotFoundException e) {
						handleDataNotFoundException(e, sosCd);
					}
					break;

				// 営業所リスト
				case OFFICE_LIST:
					List<SosMst> officeList = sosMstDAO.searchList(SOS_SORT, LIST_TYPE, SITEN_TOKUYAKUTEN_BU, dSosMst.getSosCd());
					for (SosMst sosMst : officeList) {
						try {
							sosCd = sosMst.getSosCd();
							sosList.add(sosEachKindInfoDao.searchEigyo(sosCd));
						} catch (DataNotFoundException e) {
							handleDataNotFoundException(e, sosCd);
						}
					}
					resultDto = new ProgressEachKindInfoDto(OFFICE_TOKUYAKUTEN_G, sosList, jgiList);
					break;

				// 営業所
				case OFFICE:
					try {
						sosList.add(sosEachKindInfoDao.searchEigyo(dSosMst.getSosCd()));
						resultDto = new ProgressEachKindInfoDto(OFFICE_TOKUYAKUTEN_G, sosList, jgiList);
					} catch (DataNotFoundException e) {
						handleDataNotFoundException(e, sosCd);
					}
					break;

//				// チームリスト
//				case TEAM_LIST:
//					List<SosMst> teamList = sosMstDAO.searchList(SOS_SORT, LIST_TYPE, OFFICE_TOKUYAKUTEN_G, dSosMst.getSosCd());
//					for (SosMst sosMst : teamList) {
//						try {
//							sosCd = sosMst.getSosCd();
//							sosList.add(sosEachKindInfoDao.searchTeam(sosCd));
//						} catch (DataNotFoundException e) {
//							handleDataNotFoundException(e, sosCd);
//						}
//					}
//					resultDto = new ProgressEachKindInfoDto(TEAM, sosList, jgiList);
//					break;
//
//				// チーム
//				case TEAM:
//					try {
//						sosList.add(sosEachKindInfoDao.searchTeam(dSosMst.getSosCd()));
//						resultDto = new ProgressEachKindInfoDto(TEAM, sosList, jgiList);
//					} catch (DataNotFoundException e) {
//						handleDataNotFoundException(e, sosCd);
//					}
//					break;

				// 担当者リスト
				case TANTOU_LIST:
					List<JgiMst> mrList;
//					if(existsTeam) {
//						mrList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, dSosMst.getSosCd(), LIST_TYPE, TEAM);
//					} else {
						mrList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, dSosMst.getSosCd(), LIST_TYPE, OFFICE_TOKUYAKUTEN_G);
//					}
					for (JgiMst jgiMst : mrList) {
						try {
							jgiNo = jgiMst.getJgiNo();
							jgiList.add(jgiEachKindInfoDao.search(jgiNo));
						} catch (DataNotFoundException e) {
							handleDataNotFoundException(e, jgiNo);
						}
					}
					resultDto = new ProgressEachKindInfoDto(null, sosList, jgiList);
					break;

				// 担当者
				case TANTOU:
					try {
						jgiList.add(jgiEachKindInfoDao.search(dJgiMst.getJgiNo()));
						resultDto = new ProgressEachKindInfoDto(null, sosList, jgiList);
					} catch (DataNotFoundException e) {
						handleDataNotFoundException(e, sosCd);
					}
					break;

				default:
					final String errMsg = "想定外の箇所に到達したためシステムエラー";
					throw new AccessDeniedException(new Conveyance(ErrMessageKey.ACCESS_DENIED_ERROR, errMsg));
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "データが見つからない状況はありえないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		return resultDto;
	}
	public ProgressPlanStatusDto searchProgressPlanStatus(String prodCategory) {
		List<SosStatusSummary> sosList = new ArrayList<SosStatusSummary>();
		List<JgiStatusSummary> jgiList = new ArrayList<JgiStatusSummary>();
		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		JgiMst dJgiMst = DpUserInfo.getDpUserInfo().getDefaultJgiMst();

		final String SOS_SORT = SosMstDAO.SORT_STRING;
		final SosListType LIST_TYPE = SosListType.SHITEN_LIST;

		ProgressIyakuSearchType iyakuType = ProgressIyakuSearchTypeLogic.getIyakuSearchType();
		ProgressPlanStatusDto resultDto = null;

//		// 部門ランク３の場合、配下チームが存在するか
//		boolean existsTeam = true;
//		if(dSosMst != null && dSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){
//			try {
//				sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, dSosMst.getSosCd());
//			} catch (DataNotFoundException e) {
//				existsTeam = false;
//			}
//		}
		try {

			Integer jgiNo = null;
			switch (iyakuType) {

				// 支店リスト
				case SITEN_LIST:
					sosList.addAll(sosStatusSummaryDao.searchList(SOS_SORT, LIST_TYPE, SITEN_TOKUYAKUTEN_BU, SosMst.SOS_CD1,prodCategory));
					resultDto = new ProgressPlanStatusDto(SITEN_TOKUYAKUTEN_BU, sosList, jgiList);
					break;

				// 支店
				case SITEN:
					sosList.add(sosStatusSummaryDao.search(dSosMst.getSosCd(),SITEN_TOKUYAKUTEN_BU, prodCategory));
					resultDto = new ProgressPlanStatusDto(SITEN_TOKUYAKUTEN_BU, sosList, jgiList);
					break;

				// 営業所リスト
				case OFFICE_LIST:
					sosList.addAll(sosStatusSummaryDao.searchList(SOS_SORT, LIST_TYPE, OFFICE_TOKUYAKUTEN_G, dSosMst.getSosCd(),prodCategory));
					resultDto = new ProgressPlanStatusDto(OFFICE_TOKUYAKUTEN_G, sosList, jgiList);
					break;

				// 営業所
				case OFFICE:
					sosList.add(sosStatusSummaryDao.search(dSosMst.getSosCd(),OFFICE_TOKUYAKUTEN_G, prodCategory));
					resultDto = new ProgressPlanStatusDto(OFFICE_TOKUYAKUTEN_G, sosList, jgiList);
					break;
//				// チームリスト
//				case TEAM_LIST:
//					List<SosMst> teamList = sosMstDAO.searchList(SOS_SORT, LIST_TYPE, OFFICE_TOKUYAKUTEN_G, dSosMst.getSosCd());
//					for (SosMst sosMst : teamList) {
//						try {
//							sosCd = sosMst.getSosCd();
//							sosList.add(sosStatusSummaryDao.searchTeam(sosCd, prodCategory));
//						} catch (DataNotFoundException e) {
//							handleDataNotFoundException(e, sosCd);
//						}
//					}
//					resultDto = new ProgressPlanStatusDto(TEAM, sosList, jgiList);
//					break;
//				// チーム
//				case TEAM:
//					try {
//						sosCd = dSosMst.getSosCd();
//						sosList.add(sosStatusSummaryDao.searchTeam(sosCd, prodCategory));
//						resultDto = new ProgressPlanStatusDto(TEAM, sosList, jgiList);
//					} catch (DataNotFoundException e) {
//						handleDataNotFoundException(e, sosCd);
//					}
//					break;

				// 担当者リスト
				case TANTOU_LIST:
					resultDto = new ProgressPlanStatusDto(null, sosList, jgiList);
					jgiList.addAll(jgiStatusSummaryDao.searchList(dSosMst.getSosCd(), prodCategory));

					break;

				// 担当者
				case TANTOU:
					resultDto = new ProgressPlanStatusDto(null, sosList, jgiList);
					jgiList.add(jgiStatusSummaryDao.search(dJgiMst.getJgiNo(), prodCategory));
					break;

				default:
					final String errMsg = "想定外の箇所に到達したためシステムエラー";
					throw new AccessDeniedException(new Conveyance(ErrMessageKey.ACCESS_DENIED_ERROR, errMsg));
			}
		} catch (DataNotFoundException e) {
			StringBuilder errStr = new StringBuilder();
			errStr.append("データ無し ");
			if (dSosMst != null) {
				errStr.append(dSosMst.getSosCd());
			}
			handleDataNotFoundException(e, errStr.toString() );
		}
		return resultDto;
	}

	// 業務進捗表(医)[特約店別計画(MMP品・仕入品(一般・麻薬))]を取得
	@Override
	public ProgressPlanWsStatusDto searchProgressPlanWsStatus() {
		List<HashMap<String, WsPlanStatusSummary>> statusSummaryMapList = new ArrayList<HashMap<String, WsPlanStatusSummary>>();

		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();

		final String SOS_SORT = SosMstDAO.SORT_STRING;
		final SosListType LIST_TYPE = SosListType.SHITEN_LIST;

		ProgressIyakuSearchType iyakuType = ProgressIyakuSearchTypeLogic.getIyakuSearchType();
		ProgressPlanWsStatusDto resultDto = null;
		try {

//			HashMap<String, WsPlanStatusSummary> statusSummaryMap = new HashMap<String, WsPlanStatusSummary>();
//			for (WsPlanStatusSummary e : wsPlanStatusSummaryDao.searchLIstSumByCategory()) {
//				statusSummaryMap.put(e.getCategory(),e);
//			}
//			resultDto = new ProgressPlanWsStatusDto(SITEN_TOKUYAKUTEN_BU, statusSummaryMap);

			List<String> categoryList =
					getCagetoryList(dpsCodeMasterSearchService.getCategoriesSortByProgressOrder());

			switch (iyakuType) {

				// 支店リスト
				case SITEN_LIST:
					List<SosMst> sitenList = sosMstDAO.searchListFilterByAllSosCategory(SOS_SORT, LIST_TYPE, IEIHON, SosMst.SOS_CD1, "ALL");
					for (SosMst sosMst : sitenList) {
						String sosCd = sosMst.getSosCd();
						try {
							HashMap<String, WsPlanStatusSummary> statusSummaryMap = new HashMap<String, WsPlanStatusSummary>();
							for(String p: categoryList){
								statusSummaryMap.put(p, wsPlanStatusSummaryDao.searchShiten(sosCd, p));
							}
							statusSummaryMapList.add(statusSummaryMap);
						} catch (DataNotFoundException e) {
							handleDataNotFoundException(e, sosCd);
						}
					}
					resultDto = new ProgressPlanWsStatusDto(SITEN_TOKUYAKUTEN_BU, statusSummaryMapList);
					break;

				// 支店
				case SITEN:
					String sosCd = dSosMst.getSosCd();
					try {
						HashMap<String, WsPlanStatusSummary> statusSummaryMap = new HashMap<String, WsPlanStatusSummary>();
						for(String p: categoryList){
							statusSummaryMap.put(p, wsPlanStatusSummaryDao.searchShiten(sosCd, p));
						}
						statusSummaryMapList.add(statusSummaryMap);
					} catch (DataNotFoundException e) {
						handleDataNotFoundException(e, sosCd);
					}
					resultDto = new ProgressPlanWsStatusDto(SITEN_TOKUYAKUTEN_BU, statusSummaryMapList);
					break;
				default:
					break;
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "データが見つからない状況はありえないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		return resultDto;
	}

	private List<String> getCagetoryList(List<DpsCCdMst> searchCategory) {
		ArrayList<String> result = new ArrayList<String>();
		for(DpsCCdMst e  : searchCategory ) {
			result.add(e.getDataCd());
		}
		return result;
	}

	/**
	 * 情報が見つからない場合の例外対応を行う。
	 *
	 * @param e DataNotFoundException
	 * @param sosCd 組織コード
	 */
	private static void handleDataNotFoundException(DataNotFoundException e, String sosCd) {
		if (LOG.isWarnEnabled()) {
			String errMsg = "情報が見つからない。sosCd=" + sosCd;
			LOG.warn(errMsg, e);
		}
	}

	/**
	 * 情報が見つからない場合の例外対応を行う。
	 *
	 * @param e DataNotFoundException
	 * @param jgiNo 従業員番号
	 */
	private static void handleDataNotFoundException(DataNotFoundException e, Integer jgiNo) {
		if (LOG.isWarnEnabled()) {
			String errMsg = "情報が見つからない。jgiNo=" + jgiNo;
			LOG.warn(errMsg, e);
		}
	}

	// 業務進捗表(ワ)[T/B変換指定率]を取得
	public ProgressParamForVacDto searchProgressParamForVac(String calYear, Term calTerm) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isBlank(calYear)) {
			final String errMsg = "TB変換取得時に年度がnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (calTerm == null) {
			final String errMsg = "TB変換取得時に期がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索処理
		// ----------------------
		Date tbLastUpDate = changeParamBTDao.getLastUpDate(calYear, calTerm);
		return new ProgressParamForVacDto(tbLastUpDate);
	}

	// 業務進捗表(ワ)[各種登録状況]を取得
	public ProgressEachKindInfoForVacDto searchProgressEachKindInfoForVac() {
		DpUser settingDpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		// 本部、本部ワクチンＧ
		if (settingDpUser.isMatch(JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G)) {
			SosEachKindInfoForVac sos = null;
			try {
				sos = sosEachKindInfoForVacDao.search();
				return new ProgressEachKindInfoForVacDto(sos, null);
			} catch (DataNotFoundException e) {
			}
		}

		// 小児科ＡＣ (J19-0010 対応・コメントのみ修正)
		if (settingDpUser.isMatch(JokenSet.WAKUTIN_AL)) {
			String sosCd3 = settingDpUser.getSosCd3();
			List<JgiMst> jgiMstList = null;
			try {
				jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.TOKUYAKUTEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			} catch (DataNotFoundException e) {
			}
			List<JgiEachKindInfoForVac> jgiList = null;
			if (jgiMstList != null) {
				jgiList = new ArrayList<JgiEachKindInfoForVac>(jgiMstList.size());
				for (JgiMst jgi : jgiMstList) {
					try {
						JgiEachKindInfoForVac dto = jgiEachKindInfoForVacDao.search(jgi.getJgiNo());
						jgiList.add(dto);
					} catch (DataNotFoundException e) {
					}
				}
				return new ProgressEachKindInfoForVacDto(null, jgiList);
			}

			// 念のため
			else {
				jgiList = new ArrayList<JgiEachKindInfoForVac>(1);
				Integer jgiNo = settingDpUser.getJgiNo();
				try {
					JgiEachKindInfoForVac dto = jgiEachKindInfoForVacDao.search(jgiNo);
					jgiList.add(dto);
					return new ProgressEachKindInfoForVacDto(null, jgiList);
				} catch (DataNotFoundException e) {
				}
			}
		}

		// 小児科ＭＲ (J19-0010 対応・コメントのみ修正)
		if (settingDpUser.isMatch(JokenSet.WAKUTIN_MR)) {
			List<JgiEachKindInfoForVac> jgiList = new ArrayList<JgiEachKindInfoForVac>(1);
			Integer jgiNo = settingDpUser.getJgiNo();
			try {
				JgiEachKindInfoForVac dto = jgiEachKindInfoForVacDao.search(jgiNo);
				jgiList.add(dto);
				return new ProgressEachKindInfoForVacDto(null, jgiList);
			} catch (DataNotFoundException e) {
			}
		}
		final String errMsg = "(ワ)[各種登録状況]の取得に想定外のロールが指定されている。settingDpUser=" + settingDpUser;
		throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
	}

	// 業務進捗表(ワ)[施設特約店別計画]を取得
	public ProgressPlanStatusForVacDto searchProgressPlanStatusForVac() {
		DpUser settingDpUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// 本部、本部ワクチンＧの場合
		if (settingDpUser.isMatch(JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G)) {
			SosStatusSummaryForVac sos = null;
			try {
				sos = sosStatusSummaryForVacDao.search();
				return new ProgressPlanStatusForVacDto(sos, null);
			} catch (DataNotFoundException e) {
			}
		}

		// 小児科ＡＣ (J19-0010 対応・コメントのみ修正)
		if (settingDpUser.isMatch(JokenSet.WAKUTIN_AL)) {
			String sosCd3 = settingDpUser.getSosCd3();
			List<JgiMst> jgiMstList = null;
			try {
				jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.TOKUYAKUTEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			} catch (DataNotFoundException e) {
			}
			List<JgiStatusSummaryForVac> jgiList = null;
			if (jgiMstList != null) {
				jgiList = new ArrayList<JgiStatusSummaryForVac>(jgiMstList.size());
				for (JgiMst jgi : jgiMstList) {
					try {
						JgiStatusSummaryForVac dto = jgiStatusSummaryForVacDao.search(jgi.getJgiNo());
						jgiList.add(dto);
					} catch (DataNotFoundException e) {
					}
				}
				return new ProgressPlanStatusForVacDto(null, jgiList);
			}

			// 念のため
			else {
				jgiList = new ArrayList<JgiStatusSummaryForVac>(1);
				Integer jgiNo = settingDpUser.getJgiNo();
				try {
					JgiStatusSummaryForVac dto = jgiStatusSummaryForVacDao.search(jgiNo);
					jgiList.add(dto);
					return new ProgressPlanStatusForVacDto(null, jgiList);
				} catch (DataNotFoundException e) {
				}
			}
		}

		// 小児科ＭＲ (J19-0010 対応・コメントのみ修正)
		if (settingDpUser.isMatch(JokenSet.WAKUTIN_MR)) {
			List<JgiStatusSummaryForVac> jgiList = new ArrayList<JgiStatusSummaryForVac>(1);
			Integer jgiNo = settingDpUser.getJgiNo();
			JgiStatusSummaryForVac dto = null;
			try {
				dto = jgiStatusSummaryForVacDao.search(jgiNo);
				jgiList.add(dto);
				return new ProgressPlanStatusForVacDto(null, jgiList);
			} catch (DataNotFoundException e) {
			}
		}
		final String errMsg = "(ワ)[各種登録状況]の取得に想定外のロールが指定されている。settingDpUser=" + settingDpUser;
		throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
	}

	// 業務進捗表(ワ)[特約店別計画]を取得
	public ProgressPlanWsStatusForVacDto searchProgressPlanWsStatusForVac() {

		try {
			WsPlanStatusForVac wsPlanStatusForVac = wsPlanStatusForVacDao.searchSummary();
			return new ProgressPlanWsStatusForVacDto(wsPlanStatusForVac);

		} catch (DataNotFoundException e) {
			final String errMsg = "データが見つからない状況はありえないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, errMsg), e);
		}
	}
}
