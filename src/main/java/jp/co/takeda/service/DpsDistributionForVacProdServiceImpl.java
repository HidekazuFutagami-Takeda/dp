package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DB_DUPLICATE_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ワ)配分機能の実行処理に関するサービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionForVacProdService")
public class DpsDistributionForVacProdServiceImpl implements DpsDistributionForVacProdService {

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * ワクチン用施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusForVacDao")
	protected InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	/**
	 * ワクチン用施設特約店別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckForVacService")
	protected DpsInsWsStatusCheckForVacService dpsInsWsStatusCheckForVacService;

	/**
	 * 納入計画管理検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcSystemManageSearchService")
	protected DpcSystemManageSearchService dpsSystemManageSearchService;

	// 配分処理実行 前処理
	public void distributionPreparation(List<DistributionForVacExecOrgDto> distForVacExecOrgDtoList, String appServerKbn) {

		// --------------------------------------------
		// 引数チェック
		// --------------------------------------------
		if (distForVacExecOrgDtoList == null || distForVacExecOrgDtoList.size() == 0) {
			final String errMsg = "配分実行DTOがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (DistributionForVacExecOrgDto distributionForVacExecOrgDto : distForVacExecOrgDtoList) {
			if (distributionForVacExecOrgDto.getProdCode() == null) {
				final String errMsg = "配分実行対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (distributionForVacExecOrgDto.getProdName() == null) {
				final String errMsg = "配分実行対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// --------------------------------------------
		// 施設特約店別計画〆フラグ
		// --------------------------------------------
		SysManage sysManage = dpsSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.VACCINE);
		if (sysManage.getWsEndFlg()) {
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3263E")));
		}

		// --------------------------------------------
		// ステータスチェック
		// --------------------------------------------

		// チェック対象の品目情報作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		for (DistributionForVacExecOrgDto distributionForVacExecOrgDto : distForVacExecOrgDtoList) {

			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(distributionForVacExecOrgDto.getProdCode());
			plannedProd.setProdName(distributionForVacExecOrgDto.getProdName());
			plannedProdList.add(plannedProd);
		}

		// 施設特約店別計画ステータスチェック(配分中、公開以降はだめ)
		try {

			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_OPENED);
			unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);

			// チェック実行
			dpsInsWsStatusCheckForVacService.execute(plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3261E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// ステータス更新
		// ----------------------

		Date distStartDate = commonDao.getSystemTime();
		try {
			// 品目ごとに処理
			for (DistributionForVacExecOrgDto distributionForVacExecOrgDto : distForVacExecOrgDtoList) {

				// 施設特約店別計画立案ステータスの最終更新日が変わっていないかチェック
				// 変わっている場合は、排他エラーがスローされる
				String prodCode = distributionForVacExecOrgDto.getProdCode();
				Date orgLastUpdate = distributionForVacExecOrgDto.getInsWsPlanStatusLastUpdate();
				insWsPlanStatusForVacDao.checkLastUpDate(prodCode, orgLastUpdate);

				// 担当者ごとに、施設特約店別計画立案ステータスを配分中に更新、配分開始日時を設定、配分終了日時を初期化
				List<InsWsPlanStatusForVac> insWsPlanStatusList = distributionForVacExecOrgDto.getInsWsPlanStatusForVacList();
				for (InsWsPlanStatusForVac orgInsWsPlanStatusForVac : insWsPlanStatusList) {
					InsWsPlanStatusForVac insWsPlanStatusForVac = orgInsWsPlanStatusForVac.clone();
					if (insWsPlanStatusForVac.getSeqKey() == null) {
						insWsPlanStatusForVac.setAppServerKbn(appServerKbn);
						insWsPlanStatusForVac.setAsyncBefStatus(null);
						insWsPlanStatusForVac.setAsyncBefDistStartDate(null);
						insWsPlanStatusForVac.setStatusForInsWsPlan(StatusForInsWsPlan.DISTING);
						insWsPlanStatusForVac.setDistStartDate(distStartDate);
						insWsPlanStatusForVac.setDistEndDate(null);
						insWsPlanStatusForVacDao.insert(insWsPlanStatusForVac);
					} else {
						insWsPlanStatusForVac.setAppServerKbn(appServerKbn);
						insWsPlanStatusForVac.setAsyncBefStatus(orgInsWsPlanStatusForVac.getStatusForInsWsPlan());
						insWsPlanStatusForVac.setAsyncBefDistStartDate(orgInsWsPlanStatusForVac.getDistStartDate());
						insWsPlanStatusForVac.setStatusForInsWsPlan(StatusForInsWsPlan.DISTING);
						insWsPlanStatusForVac.setDistStartDate(distStartDate);
						insWsPlanStatusForVac.setDistEndDate(null);
						insWsPlanStatusForVacDao.update(insWsPlanStatusForVac);
					}
				}
			}
		} catch (DuplicateException e) {
			final String errMsg = "担当者別計画立案ステータス登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}
}
