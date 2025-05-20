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
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;

/**
 * 特約店別計画スライドサービス実装クラス
 *
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenSlideService")
public class DpsTmsTytenSlideServiceImpl implements DpsTmsTytenSlideService {

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
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * DB共通情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 特約店別計画スライドサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenSlideExecuteService")
	protected DpsTmsTytenSlideExecuteService dpsTmsTytenSlideExecuteService;

	/**
	 * 特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckService")
	protected DpsWsStatusCheckService dpsWsStatusCheckService;

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
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// 特約店別計画スライド処理を実行
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void tmsTytenSlide(String sosCd2, String category) {

		// --------------------------------------------
		// 引数チェック
		// --------------------------------------------
//本部ユーザーは、全支店が対象となるため
//		// 組織コード(支店)チェック
//		if (sosCd2 == null) {
//			final String errMsg = "組織コード(支店)がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

		// カテゴリーチェック
		if (category == null) {
			final String errMsg = "カテゴリーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------------------------
		// 施設特約店別計画〆フラグチェック
		// --------------------------------------------
		SysManage sysManageDb = null;

		try {
			sysManageDb = sysManageDao.search(SysClass.DPS, SysType.IYAKU);

		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 施設特約店別計画が〆られているかを検証
		if (!sysManageDb.getWsEndFlg()) {
			// ステータスチェックエラー
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3291E")));
		}

		// --------------------------------------------
		// 情報収集
		// --------------------------------------------

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

		// 品目一覧取得
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		List<String> prodCodeList = new ArrayList<String>();
		try {
			List<PlannedProd> tmpList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, category, null);
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
		                if(sosCtg.getCategoryCd().equals(category)) {
		                	// 支店配下のカテゴリに指定したカテゴリがある場合のみ対象とする
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

		// --------------------------------------------
		// スライド実行
		// --------------------------------------------
		Date startTime = commonDao.getSystemTime();
		Date endTime = null;
		ContactOperationsEntryDto contactOperationsEntryDto = null;
		try {
			// スライド実行（別トランザクションで実行）
			dpsTmsTytenSlideExecuteService.executeSlide(sosCd3List, prodCodeList, wsPlanStatusList, startTime);
			endTime = commonDao.getSystemTime();

			// アテンション・お知らせ登録情報生成(正常終了時)
			contactOperationsEntryDto = new ContactOperationsEntryDto(sosCd2, DpUserInfo.getDpUserInfo().getLoginUser(), ContactOperationsType.WS_SLIDE_IYAKU,
				ContactResultType.SUCCESS, startTime, endTime, null, null);

		} catch (Exception e) {
			// スライド処理でエラー発生
			// アテンション・お知らせ登録情報生成
			endTime = commonDao.getSystemTime();
			contactOperationsEntryDto = new ContactOperationsEntryDto(sosCd2, DpUserInfo.getDpUserInfo().getLoginUser(), ContactOperationsType.WS_SLIDE_IYAKU,
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
