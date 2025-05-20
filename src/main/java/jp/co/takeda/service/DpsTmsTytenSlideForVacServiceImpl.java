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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;

/**
 * ワクチン用特約店別計画スライドサービス実装クラス
 *
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenSlideForVacService")
public class DpsTmsTytenSlideForVacServiceImpl implements DpsTmsTytenSlideForVacService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsTmsTytenSlideService.class);

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * DB共通情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * ワクチン用特約店別計画スライドサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenSlideExecuteForVacService")
	protected DpsTmsTytenSlideExecuteForVacService dpsTmsTytenSlideExecuteForVacService;

	/**
	 * ワクチン用特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckForVacService")
	protected DpsWsStatusCheckForVacService dpsWsStatusCheckForVacService;

	/**
	 * 業務連絡サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsContactOperationsService")
	protected DpsContactOperationsService dpsContactOperationsService;

	/**
	 * 納入計画システム管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckService")
	protected DpsWsStatusCheckService dpsWsStatusCheckService;

    @Autowired(required = true)
    @Qualifier("codeMasterDao")
    protected CodeMasterDao codeMasterDao;

	// ワクチン用特約店別計画スライド処理を実行
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void tmsTytenSlide(String sosCd2) {

		// --------------------------------------------
		// 施設特約店別計画〆フラグチェック
		// --------------------------------------------
		SysManage sysManageDb = null;
		try {
			sysManageDb = sysManageDao.search(SysClass.DPS, SysType.VACCINE);

		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		if (!sysManageDb.getWsEndFlg()) {
			// ステータスチェックエラー
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3291E")));
		}

		// --------------------------------------------
		// 情報収集
		// --------------------------------------------

		// --> ADD start 2021/07/28 Top画面表示のため、DPS_I_WS_PLAN_STATUSに追加更新をする。
		// 営業所一覧取得
		List<String> sosCd3List = new ArrayList<String>();
		List<SosMst> sosMst3List = new ArrayList<SosMst>();
		try {
			sosMst3List = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, sosCd2);
			for (SosMst sosMst : sosMst3List) {
				sosCd3List.add(sosMst.getSosCd());
			}

		} catch (DataNotFoundException e) {
			// 営業所一覧が存在しない場合
			final String errMsg = "営業所一覧が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		// <-- ADD end 2021/07/28

		// 品目一覧取得
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		List<String> prodCodeList = new ArrayList<String>();
		try {
			List<PlannedProd> tmpList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, null, null);
			for (PlannedProd plannedProd : tmpList) {
				Boolean planLevelWs = plannedProd.getPlanLevelWs();
				if (planLevelWs != null && planLevelWs) {
					plannedProdList.add(plannedProd);
					prodCodeList.add(plannedProd.getProdCode());
				} else {
					if (LOG.isDebugEnabled()) {
						LOG.debug("特約店別計画立案レベルが真ではないので、除外。prodCode=" + plannedProd.getProdCode());
					}
				}
			}

		} catch (DataNotFoundException e) {
			// 品目一覧が存在しない場合
			final String errMsg = "品目一覧が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// --------------------------------------------
		// ワクチン用特約店別計画立案ステータスチェック
		// --------------------------------------------
		List<WsPlanStatusForVac> wsPlanStatusForVacList = null;
		try {
			// 許可しないスライド状況リスト作成
			List<WsSlideStatusForCheck> unallowableSlideStatusList = new ArrayList<WsSlideStatusForCheck>();
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDING);
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDED);

			// ステータスチェック実行
			wsPlanStatusForVacList = dpsWsStatusCheckForVacService.execute(plannedProdList, unallowableSlideStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスチェックエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3310E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// --> ADD start 2021/07/28 Top画面表示のため、DPS_I_WS_PLAN_STATUSに追加更新をする。
		// --------------------------------------------
		// 特約店別計画立案ステータスチェック
		// --------------------------------------------
		List<WsPlanStatus> wsPlanStatusList = new ArrayList<WsPlanStatus>();
		try {
			// 許可しない配分ステータスリスト作成
			List<WsDistStatusForCheck> unallowableDistStatusList = new ArrayList<WsDistStatusForCheck>();
			unallowableDistStatusList.add(WsDistStatusForCheck.DISTING);

			// 許可しないスライド状況リスト作成
			List<WsSlideStatusForCheck> unallowableSlideStatusList = new ArrayList<WsSlideStatusForCheck>();
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDING);
			unallowableSlideStatusList.add(WsSlideStatusForCheck.SLIDED);

			List<DpmCCdMst> codeList = new ArrayList<DpmCCdMst>();
			try {
				codeList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
			}
			// データが見つからなくても、エラーにしない
			catch (DataNotFoundException e) {
			}
			String vacCtg = codeList.get(0).getDataCd();
			SosMst sosMst2 = null;
			// ステータスチェック実行
			if (sosCd2 == null) {
				// 本部
				String berSosCd2 = "";
				for (SosMst sosMst3 : sosMst3List) {
					if (berSosCd2.equals(sosMst3.getUpSosCd())) {
						continue;
					}

					try {
					// 支店情報取得
					sosMst2 = sosMstDAO.search(sosMst3.getUpSosCd());
					} catch (DataNotFoundException e) {
						// 組織情報（支店）が存在しない場合
						final String errMsg = "組織情報（支店）が存在しない";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
					}

					// 支店配下のカテゴリリストを取得
					List<SosMstCtg> sosCtgList = new ArrayList<SosMstCtg>();
					try {
						sosCtgList = sosMstDAO.searchSosCategoryList(sosMst2.getSosCd());
					}
					// データが見つからなくても、エラーにしない
					catch (DataNotFoundException e) {
					}
					List<WsPlanStatus> wsPlanStatusList_Shiten = null;
					for(SosMstCtg sosCtg: sosCtgList) {
		                if(sosCtg.getCategoryCd().equals(vacCtg)) {
		                	// 支店配下のカテゴリにワクチンがある場合のみ対象とする
		                	wsPlanStatusList_Shiten = dpsWsStatusCheckService.execute(sosMst2, plannedProdList, unallowableDistStatusList, unallowableSlideStatusList);
		                	for(WsPlanStatus wsPlanStatus_Shiten: wsPlanStatusList_Shiten) {
		                		wsPlanStatusList.add(wsPlanStatus_Shiten);
		                	}
		                    break;
		                }
		            }

					berSosCd2 = sosMst3.getUpSosCd();
				}

			}else {
				// 支店情報取得
				try {
					sosMst2 = sosMstDAO.search(sosCd2);
				} catch (DataNotFoundException e) {
					// 組織情報（支店）が存在しない場合
					final String errMsg = "組織情報（支店）が存在しない";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
				}

				wsPlanStatusList = dpsWsStatusCheckService.execute(sosMst2, plannedProdList, unallowableDistStatusList, unallowableSlideStatusList);
			}

		} catch (UnallowableStatusException e) {
				// ステータスチェックエラー
				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3290E"));
				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}
		// <-- ADD end 2021/07/28

		// --------------------------------------------
		// スライド実行
		// --------------------------------------------
		Date startTime = commonDao.getSystemTime();
		Date endTime = null;
		ContactOperationsEntryDto contactOperationsEntryDto = null;
		try {
			// スライド実行
			dpsTmsTytenSlideExecuteForVacService.executeSlide(prodCodeList, wsPlanStatusForVacList, startTime, wsPlanStatusList);
			endTime = commonDao.getSystemTime();

			// アテンション・お知らせ登録情報生成(正常終了時)
			contactOperationsEntryDto = new ContactOperationsEntryDto(null, DpUserInfo.getDpUserInfo().getLoginUser(), ContactOperationsType.WS_SLIDE_VAC,
				ContactResultType.SUCCESS, startTime, endTime, null, null);

		} catch (Exception e) {
			// スライド処理でエラー発生
			// アテンション・お知らせ登録情報生成
			endTime = commonDao.getSystemTime();
			contactOperationsEntryDto = new ContactOperationsEntryDto(null, DpUserInfo.getDpUserInfo().getLoginUser(), ContactOperationsType.WS_SLIDE_VAC,
				ContactResultType.FAILURE, startTime, endTime, null, null);

		} finally {
			// --------------------------------------------
			// おしらせ・アテンション登録
			// --------------------------------------------
			try {
				dpsContactOperationsService.entryAtt(contactOperationsEntryDto);
				dpsContactOperationsService.entryAnnounce(contactOperationsEntryDto);

			} catch (Exception e) {
				// おしらせ・アテンション登録時エラー
				// ログにエラー内容を出力して終了する
				LOG.error("おしらせ・アテンション登録時にエラー", e);
			}
		}
	}
}
