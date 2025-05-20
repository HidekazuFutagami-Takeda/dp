package jp.co.takeda.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.DpsJknGrpDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dao.JgiJokenDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.JknGrpDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.dto.DistributionForVacParamsDto;
import jp.co.takeda.dto.DistributionParamsDto;
import jp.co.takeda.dto.DocDistributionJgiDto;
import jp.co.takeda.dto.DocDistributionParamsDto;
import jp.co.takeda.dto.EstimationExecDto;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.dto.EstimationParamsDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.dto.TmsTytenDistParamDto;
import jp.co.takeda.dto.TmsTytenDistParamResultDto;
import jp.co.takeda.logic.TargetJokenSetSelectLogic;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.model.div.StatusForWs;
import jp.co.takeda.security.BusinessTarget;
import jp.co.takeda.security.BusinessType;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpRole;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.ConvertUtil;

/**
 * 非同期処理仕掛かり中パラメータを検索するサービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsAsyncProcessSearchService")
public class DpsAsyncProcessSearchServiceImpl implements DpsAsyncProcessSearchService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsAsyncProcessSearchServiceImpl.class);

	/**
	 * 担当者別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusDao")
	protected InsWsPlanStatusDao insWsPlanStatusDao;

	/**
	 * 施設医師別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanStatusDao")
	protected InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * ワクチン用施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusForVacDao")
	protected InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	/**
	 * 特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanStatusDao")
	protected WsPlanStatusDao wsPlanStatusDao;

	/**
	 * 試算機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationProdSearchService")
	protected DpsEstimationProdSearchService dpsEstimationProdSearchService;

	/**
	 * 施設特約店別計画配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionProdSearchService")
	protected DpsDistributionProdSearchService dpsDistributionProdSearchService;

	/**
	 * ワクチン用施設特約店別計画配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacProdSearchService")
	protected DpsDistributionForVacProdSearchService dpsDistributionForVacProdSearchService;

	/**
	 * 特約店別計画配分対象品目一覧 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanSearchService")
	protected DpsTmsTytenPlanSearchService dpsTmsTytenPlanSearchService;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 条件セットDAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiJokenDAO")
	protected JgiJokenDAO jgiJokenDAO;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 計画管理条件セットグループDAO
	 */
	@Autowired(required = true)
	@Qualifier("jknGrpDao")
	protected JknGrpDao jknGrpDao;

	/**
	 * 計画支援条件セットグループDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsJknGrpDao")
	protected DpsJknGrpDao dpsJknGrpDao;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// 試算処理パラメータリストを取得
	public List<EstimationParamsDto> searchEstimationPrameter(String appServerKbn) throws DataNotFoundException {
		// 試算中のステータスリストを取得
		List<MrPlanStatus> mrPlanStatusList = null;
		try {
			mrPlanStatusList = mrPlanStatusDao.searchEstimatingList(null, appServerKbn);
		} catch (DataNotFoundException e) {
			throw e;
		}

		// 試算中のステータスリストを組織コード(営業所)＋従業員で区分け
		Map<String, List<EstimationExecDto>> statusListMap = new HashMap<String, List<EstimationExecDto>>();
		for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {
			// 品目情報を取得
			String prodCode = mrPlanStatus.getProdCode();
			PlannedProd plannedProd = null;
			try {
				plannedProd = plannedProdDAO.search(prodCode);
			} catch (DataNotFoundException e) {
				String message = "計画対象品目が存在しない【品目固定コード：" + prodCode + "】";
				LOG.error(message, e);
				continue;
			}

			// 組織コード(営業所)と従業員を元にキーを生成
			String sosCd3 = mrPlanStatus.getSosCd();
			Integer jgiNo = mrPlanStatus.getUpJgiNo();
			String key = sosCd3 + "," + jgiNo;

			// 組織コード(営業所)と従業員を元にステータスを区分け
			if (!statusListMap.containsKey(key)) {
				statusListMap.put(key, new ArrayList<EstimationExecDto>());
			}

			// 検索用DTOを生成
			// 更新日は前処理でしか使用していないので、nullを設定する
			String prodName = plannedProd.getProdName();
			EstimationExecDto estimationExecDto = new EstimationExecDto(prodCode, prodName, null);
			statusListMap.get(key).add(estimationExecDto);
		}

		final String SID = "dps";
		final String G_CODE = "301";
		final String F_CODE = "C00F10";
		final String PATH = SID + G_CODE + F_CODE + "Execute";
		final BusinessTarget DPS = BusinessTarget.DPS;
		final BusinessType IYAKU = BusinessType.IYAKU;

		// 試算用パラメータを逆生成
		List<EstimationParamsDto> estimationParamsDtoList = new ArrayList<EstimationParamsDto>();
		for (String key : statusListMap.keySet()) {
			// キーから組織コード(営業所)と従業員を取得
			String[] data = ConvertUtil.splitConmma(key);
			String sosCd3 = data[0];
			Integer upJgiNo = ConvertUtil.parseInteger(data[1]);

			List<EstimationExecDto> estimationExecDtoList = new ArrayList<EstimationExecDto>();
			for (EstimationExecDto estimationExecDto : statusListMap.get(key)) {
				estimationExecDtoList.add(estimationExecDto);
			}

			// 試算前の情報処理
			List<EstimationExecOrgDto> execList = dpsEstimationProdSearchService.searchEstimationPreparation(sosCd3, estimationExecDtoList);

			// 実行前ステータスに書き換え
			for (EstimationExecOrgDto estimationExecOrgDto : execList) {
				MrPlanStatus mrPlanStatus = estimationExecOrgDto.getMrPlanStatus();
				StatusForMrPlan asyncBefStatus = mrPlanStatus.getAsyncBefStatus();
				if (asyncBefStatus == null) {
					// 試算前にレコードが存在しなかった場合
					mrPlanStatus.setSeqKey(null);
				} else {
					// 試算前にレコードが存在していた場合
					// 試算前の状態に戻す
					mrPlanStatus.setStatusForMrPlan(asyncBefStatus);
					Date date = mrPlanStatus.getEstEndDate();
					mrPlanStatus.setEstStartDate(date);
					mrPlanStatus.setEstEndDate(date);
				}
			}

			// メタ情報生成
			DpMetaInfo metaInfo = new DpMetaInfo(PATH, SID, G_CODE, F_CODE, DPS, IYAKU, null, appServerKbn, new StringBuilder());

			// 実行ユーザー情報生成
			JokenSet[] targetJokenSetList = new JokenSet[] { JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF };
			DpUser dpUser = createDpUser(upJgiNo, targetJokenSetList);

// mod Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
// mod Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
//			EstimationParamsDto dto = new EstimationParamsDto(metaInfo, sosCd3, dpUser, execList, null);
//			EstimationParamsDto dto = new EstimationParamsDto(metaInfo, sosCd3, dpUser, execList, null, null);
// mod Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
			EstimationParamsDto dto = new EstimationParamsDto(metaInfo, sosCd3, dpUser, execList, null, null, null, null);
// mod End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
			estimationParamsDtoList.add(dto);
		}

		return estimationParamsDtoList;
	}

	// 施設特約店別計画配分処理パラメータリストを取得
	public List<DistributionParamsDto> searchInsWsDistPrameter(String appServerKbn) throws DataNotFoundException {
		// 配分中のステータスリストを取得
		List<InsWsPlanStatus> insWsPlanStatusList = null;
		try {
			insWsPlanStatusList = insWsPlanStatusDao.searchDistingList(null, appServerKbn);
		} catch (DataNotFoundException e) {
			throw e;
		}

		// 配分中のステータスリストを組織コード（営業所）＋カテゴリ＋従業員で区分け
		Map<String, List<InsWsDistProdUpdateDto>> statusListMap = new HashMap<String, List<InsWsDistProdUpdateDto>>();
		List<String> prodList = new ArrayList<String>();
		String category = null;
		for (InsWsPlanStatus insWsPlanStatus : insWsPlanStatusList) {
			// 営業所を取得
			Integer jgiNo = insWsPlanStatus.getJgiNo();
			JgiMst jgiMst = null;
			try {
				jgiMst = jgiMstDAO.search(jgiNo);
			} catch (DataNotFoundException e) {
				String message = "従業員情報が存在しない【従業員番号：" + jgiNo + "】";
				LOG.error(message, e);
				continue;
			}

			// カテゴリを取得
			String prodCode = insWsPlanStatus.getProdCode();
			PlannedProd plannedProd = null;
			try {
				plannedProd = plannedProdDAO.search(prodCode);
			} catch (DataNotFoundException e) {
				String message = "計画対象品目が存在しない【品目固定コード：" + prodCode + "】";
				LOG.error(message, e);
				continue;
			}

			// 組織コード(営業所)とカテゴリと従業員を元にキーを生成
			String sosCd3 = jgiMst.getSosCd3();
//			String category = plannedProd.getCategory().getDbValue();
			category = plannedProd.getCategory();
			Integer upJgiNo = insWsPlanStatus.getUpJgiNo();
			String sosCategoryKey = sosCd3 + "," + category + "," + upJgiNo;

			// ステータスが従業員単位で管理されているので、
			// 同一の組織コード(営業所)とカテゴリと従業員で品目が重複しないようにする
			String prodKey = sosCategoryKey + ',' + prodCode;
			if (prodList.contains(prodKey)) {
				continue;
			}

			// 組織コード(営業所)とカテゴリを元にステータスを区分け
			if (!statusListMap.containsKey(sosCategoryKey)) {
				statusListMap.put(sosCategoryKey, new ArrayList<InsWsDistProdUpdateDto>());
			}

			// 検索用DTOを生成
			// 最終更新日は前処理でしか使用していないので、nullを設定する
			String prodName = plannedProd.getProdName();
			InsWsDistProdUpdateDto insWsDistProdUpdateDto = new InsWsDistProdUpdateDto(prodCode, prodName, null);
			statusListMap.get(sosCategoryKey).add(insWsDistProdUpdateDto);
			prodList.add(prodKey);
		}

		final String SID = "dps";
		final String G_CODE = "400";
		final String F_CODE = "C00F10";
		final String PATH = SID + G_CODE + F_CODE + "Execute";
		final BusinessTarget DPS = BusinessTarget.DPS;
		final BusinessType IYAKU = BusinessType.IYAKU;

		// 配分用パラメータを逆生成
		List<DistributionParamsDto> distributionParamsDtoList = new ArrayList<DistributionParamsDto>();
		for (String sosCategoryKey : statusListMap.keySet()) {
			// キーから組織コード(営業所)とカテゴリと従業員を取得
			String[] data = ConvertUtil.splitConmma(sosCategoryKey);
			String sosCd3 = data[0];
			//ProdCategory prodCategory = ProdCategory.getInstance(data[1]);
			String prodCategory = data[1];
			Integer upJgiNo = ConvertUtil.parseInteger(data[2]);

			List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList = new ArrayList<InsWsDistProdUpdateDto>();
			for (InsWsDistProdUpdateDto insWsDistProdUpdateDto : statusListMap.get(sosCategoryKey)) {
				insWsDistProdUpdateDtoList.add(insWsDistProdUpdateDto);
			}

			// 配分前の情報取得
			List<DistributionExecOrgDto> execList = dpsDistributionProdSearchService.searchDistributionPreparation(sosCd3, insWsDistProdUpdateDtoList);

			// 実行前ステータスに書き換え
			for (DistributionExecOrgDto distributionExecOrgDto : execList) {
				for (InsWsPlanStatus insWsPlanStatus : distributionExecOrgDto.getInsWsPlanStatusList()) {
					StatusForInsWsPlan asyncBefStatus = insWsPlanStatus.getAsyncBefStatus();
					if (asyncBefStatus == null) {
						// 配分前にレコードが存在しなかった場合
						insWsPlanStatus.setSeqKey(null);
					} else {
						// 配分前にレコードが存在していた場合
						// 配分前の状態に戻す
						insWsPlanStatus.setStatusForInsWsPlan(asyncBefStatus);
						Date date = insWsPlanStatus.getDistEndDate();
						insWsPlanStatus.setDistStartDate(date);
						insWsPlanStatus.setDistEndDate(date);
					}
				}
			}

			// メタ情報生成
			DpMetaInfo metaInfo = new DpMetaInfo(PATH, SID, G_CODE, F_CODE, DPS, IYAKU, null, appServerKbn, new StringBuilder());

			// 実行ユーザー情報生成
			JokenSet[] targetJokenSetList = new JokenSet[] {};
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				targetJokenSetList = new JokenSet[] { JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G };
			} else {
				targetJokenSetList = new JokenSet[] { JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF };
			}
			DpUser dpUser = createDpUser(upJgiNo, targetJokenSetList);

			DistributionParamsDto dto = new DistributionParamsDto(metaInfo, sosCd3, prodCategory, dpUser, execList, false);
			distributionParamsDtoList.add(dto);
		}

		return distributionParamsDtoList;
	}

	// 施設医師別計画配分処理パラメータリストを取得
	public List<DocDistributionParamsDto> searchInsDocDistPrameter(String appServerKbn) throws DataNotFoundException {

		// ----------------------------------------------------------
		// 配分中のステータスを取得
		// ・実行者・従業員で集約、実行者・組織コード・従業員順で取得
		// ----------------------------------------------------------
		List<Map<String, Object>> insDocPlanStatusList = null;
		try {
			insDocPlanStatusList = insDocPlanStatusDao.searchDistingList(appServerKbn);
		} catch (DataNotFoundException e) {
			throw e;
		}

		// 要求情報(管理対象業務と業務種別)に対応した必要な条件セット配列を取得
		final BusinessTarget DPS = BusinessTarget.DPS;
		final BusinessType IYAKU = BusinessType.IYAKU;
		TargetJokenSetSelectLogic logic = new TargetJokenSetSelectLogic(DPS, IYAKU);
		JokenSet[] targetJokenSetList = logic.execute();

		// ----------------------------------------------------------
		// 配分用パラメータを逆生成
		// ・実行者、組織コードの単位でパラメータデータを１つ作成する
		// ----------------------------------------------------------
		List<DocDistributionParamsDto> distributionParamsDtoList = new ArrayList<DocDistributionParamsDto>();
		List<DocDistributionJgiDto> jgiList = new ArrayList<DocDistributionJgiDto>();
		Date maxLastUpDate = null;
		for (int index = 0; index < insDocPlanStatusList.size(); index ++ ){

			Map<String, Object> resultMap = insDocPlanStatusList.get(index);
			Integer upJgiNo = (Integer)resultMap.get("upJgiNo");
			String sosCd = (String)resultMap.get("sosCd3");
			Integer jgiNo = (Integer)resultMap.get("jgiNo");
			boolean doMrOpen = (Boolean)resultMap.get("openFlg");
			boolean doPlanClear = (Boolean)resultMap.get("plannedClearFlg");
			Date lastUpDate = (Date)resultMap.get("lastUpDate");

			// 最終更新日判定
			if (maxLastUpDate == null || lastUpDate.after(maxLastUpDate)) {
				maxLastUpDate = lastUpDate;
			}

			// 配分対象従業員リスト作成
			try {
				JgiMst jgiMst = jgiMstDAO.search(jgiNo);
				jgiList.add(new DocDistributionJgiDto(jgiMst, doMrOpen, doPlanClear, lastUpDate));
			} catch (DataNotFoundException e) {
				String message = "従業員情報が存在しない【従業員番号：" + jgiNo + "】";
				LOG.error(message, e);
			}

			// 次の実行者または組織コードが異なる場合はパラメータデータの作成
			boolean isLast = true;
			if(index < insDocPlanStatusList.size() - 1){

				Map<String, Object> nextMap = insDocPlanStatusList.get(index + 1);
				Integer nextUpJgiNo = (Integer)nextMap.get("upJgiNo");
				String nextSosCd = (String)nextMap.get("sosCd3");

				if(upJgiNo.equals(nextUpJgiNo) && sosCd.equals(nextSosCd)){
					isLast = false;
				}
			}

			// パラメータデータ作成
			if(isLast){

				// 配分対象従業員リスト作成で従業員がいなかった場合
				if(jgiList.size() == 0){
					String message = "配分対象の従業員がいない【実行者：" + upJgiNo + ",組織コード：" + sosCd + "】";
					LOG.error(message);
					continue;
				}

				// 実行ユーザー情報生成
				DpUser dpUser = createDpUser(upJgiNo, targetJokenSetList);

				if(dpUser.isMatch(JokenSet.IYAKU_AL, JokenSet.IYAKU_MR)) {

					// メタ情報作成
					String SID = "dps";
					String G_CODE = "601";
					String F_CODE = "C01F15";
					String PATH = SID + G_CODE + F_CODE + "Execute";
					DpMetaInfo metaInfo = new DpMetaInfo(PATH, SID, G_CODE, F_CODE, DPS, IYAKU, null, appServerKbn, new StringBuilder());

					// パラメータ作成
					DocDistributionParamsDto dto = new DocDistributionParamsDto(metaInfo, sosCd, dpUser, maxLastUpDate, jgiList, false);
					distributionParamsDtoList.add(dto);

				} else {

					// メタ情報作成
					String SID = "dps";
					String G_CODE = "600";
					String F_CODE = "C00F10";
					String PATH = SID + G_CODE + F_CODE + "Execute";
					DpMetaInfo metaInfo = new DpMetaInfo(PATH, SID, G_CODE, F_CODE, DPS, IYAKU, null, appServerKbn, new StringBuilder());

					// パラメータ作成
					DocDistributionParamsDto dto = new DocDistributionParamsDto(metaInfo, sosCd, dpUser, maxLastUpDate, jgiList, true);
					distributionParamsDtoList.add(dto);
				}

				// 初期化
				jgiList = new ArrayList<DocDistributionJgiDto>();
				maxLastUpDate = null;
			}
		}

		return distributionParamsDtoList;
	}

	// (ワクチン)施設特約店別計画配分処理パラメータリストを取得
	public List<DistributionForVacParamsDto> searchInsWsDistForVacPrameter(String appServerKbn) throws DataNotFoundException {
		// 配分中のステータスリストを取得
		List<InsWsPlanStatusForVac> insWsPlanStatusForVacList = null;
		try {
			insWsPlanStatusForVacList = insWsPlanStatusForVacDao.searchDistingList(null, appServerKbn);
		} catch (DataNotFoundException e) {
			throw e;
		}

		// 配分中のステータスリストを従業員で区分け
		Map<Integer, List<InsWsDistProdUpdateDto>> statusListMap = new HashMap<Integer, List<InsWsDistProdUpdateDto>>();
		List<String> prodList = new ArrayList<String>();
		for (InsWsPlanStatusForVac insWsPlanStatusForVac : insWsPlanStatusForVacList) {
			// 計画対象品目を取得
			String prodCode = insWsPlanStatusForVac.getProdCode();
			PlannedProd plannedProd = null;
			try {
				plannedProd = plannedProdDAO.search(prodCode);
			} catch (DataNotFoundException e) {
				String message = "計画対象品目が存在しない【品目固定コード：" + prodCode + "】";
				LOG.error(message, e);
				continue;
			}

			// 従業員を元にキーを生成
			Integer key = insWsPlanStatusForVac.getUpJgiNo();

			// ステータスが従業員単位で管理されているので、
			// 品目が重複しないようにする
			String prodKey = key + "," + prodCode;
			if (prodList.contains(prodKey)) {
				continue;
			}

			// 従業員を元にステータスを区分け
			if (!statusListMap.containsKey(key)) {
				statusListMap.put(key, new ArrayList<InsWsDistProdUpdateDto>());
			}

			// 検索用DTOを生成
			// 最終更新日は前処理でしか使用していないので、nullを設定する
			String prodName = plannedProd.getProdName();
			InsWsDistProdUpdateDto insWsDistProdUpdateDto = new InsWsDistProdUpdateDto(prodCode, prodName, null);
			statusListMap.get(key).add(insWsDistProdUpdateDto);
			prodList.add(prodKey);
		}

		final String SID = "dps";
		final String G_CODE = "400";
		final String F_CODE = "C03F05";
		final String PATH = SID + G_CODE + F_CODE + "Execute";
		final BusinessTarget DPS = BusinessTarget.DPS;
		final BusinessType IYAKU = BusinessType.VACCINE;

		// 配分用パラメータを逆生成
		List<DistributionForVacParamsDto> distributionForVacParamsDtoList = new ArrayList<DistributionForVacParamsDto>();
		for (Integer key : statusListMap.keySet()) {
			// キーから従業員Noを取得
			Integer upJgiNo = key;


			// 配分前の情報取得
			List<DistributionForVacExecOrgDto> execList = dpsDistributionForVacProdSearchService.searchDistributionPreparation(statusListMap.get(key));

			// 実行前ステータスに書き換え
			for (DistributionForVacExecOrgDto distributionForVacExecOrgDto : execList) {
				for (InsWsPlanStatusForVac insWsPlanStatusForVac : distributionForVacExecOrgDto.getInsWsPlanStatusForVacList()) {
					StatusForInsWsPlan asyncBefStatus = insWsPlanStatusForVac.getAsyncBefStatus();
					if (asyncBefStatus == null) {
						// 配分前にレコードが存在しなかった場合
						insWsPlanStatusForVac.setSeqKey(null);
					} else {
						// 配分前にレコードが存在していた場合
						// 配分前の状態に戻す
						insWsPlanStatusForVac.setStatusForInsWsPlan(asyncBefStatus);
						Date date = insWsPlanStatusForVac.getDistEndDate();
						insWsPlanStatusForVac.setDistStartDate(date);
						insWsPlanStatusForVac.setDistEndDate(date);
					}
				}
			}

			// メタ情報生成
			DpMetaInfo metaInfo = new DpMetaInfo(PATH, SID, G_CODE, F_CODE, DPS, IYAKU, null, appServerKbn, new StringBuilder());

			// 実行ユーザー情報生成
			JokenSet[] targetJokenSetList = new JokenSet[] { JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G };
			DpUser dpUser = createDpUser(upJgiNo, targetJokenSetList);

			DistributionForVacParamsDto dto = new DistributionForVacParamsDto(metaInfo, dpUser, false, execList);
			distributionForVacParamsDtoList.add(dto);
		}

		return distributionForVacParamsDtoList;
	}

	// 特約店別計画配分処理パラメータリストを取得
	public List<TmsTytenDistParamDto> searchWsDistPrameter(String appServerKbn) throws DataNotFoundException {

		// 配分中のステータスリストを取得
		List<WsPlanStatus> distingStatusList = null;
		try {
			distingStatusList = wsPlanStatusDao.searchDistingList(WsPlanStatusDao.SORT_STRING, appServerKbn);
		} catch (DataNotFoundException e) {
			throw e;
		}

		// 配分中のステータスリストを組織コード(支店)で区分け

		// キー：組織コード(支店)+JgiNo、値：ステータス
		Map<String, List<WsPlanStatus>> statusListMap = new HashMap<String, List<WsPlanStatus>>();
		for (WsPlanStatus wsPlanStatus : distingStatusList) {
			String sosCd2 = wsPlanStatus.getSosCd();
			Integer jgiNo = wsPlanStatus.getUpJgiNo();
			String key = sosCd2 + "," + jgiNo;
			if (statusListMap.get(key) != null) {
				statusListMap.get(key).add(wsPlanStatus);
			} else {
				statusListMap.put(key, new ArrayList<WsPlanStatus>());
				statusListMap.get(key).add(wsPlanStatus);
			}
		}

		final String PATH = "dps500C00F10Execute";
		final String SID = "dps";
		final String G_CODE = "500";
		final String F_CODE = "C00F10";
		final BusinessTarget DPS = BusinessTarget.DPS;
		final BusinessType IYAKU = BusinessType.IYAKU;

		// 配分用パラメータを逆生成
		List<TmsTytenDistParamDto> paramDtoList = new ArrayList<TmsTytenDistParamDto>();
		for (String key : statusListMap.keySet()) {
			List<WsPlanStatus> sList = statusListMap.get(key);
			List<String> prodList = new ArrayList<String>();
			for (WsPlanStatus s : sList) {
				prodList.add(s.getProdCode());
			}

			String[] data = ConvertUtil.splitConmma(key);
			String sosCd = data[0];
			Integer jgiNo = Integer.parseInt(data[1]);
			TmsTytenDistParamResultDto resultDto = dpsTmsTytenPlanSearchService.searchTmsTytenDistParam(sosCd, prodList, null);
			List<WsPlanStatus> befStatusList = resultDto.getBeforeWsPlanStatusList();

			// 実行前に書き換え
			for (WsPlanStatus status : befStatusList) {
				StatusForWs befStatusForWs = status.getAsyncBefStatus();
				if (befStatusForWs == null) {
					status.setSeqKey(null);
				} else {

					// 巻き戻し
					status.setStatusDistForWs(befStatusForWs);
					Date date = status.getDistEndDate();
					status.setDistStartDate(date);
					status.setDistEndDate(date);
				}
			}
			DpMetaInfo metaInfo = new DpMetaInfo(PATH, SID, G_CODE, F_CODE, DPS, IYAKU, null, appServerKbn, new StringBuilder());
			JokenSet[] targetJokenSetList = new JokenSet[] { JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF };
			DpUser dpUser = createDpUser(jgiNo, targetJokenSetList);
			TmsTytenDistParamDto paramDto = new TmsTytenDistParamDto(metaInfo, dpUser, resultDto);
			paramDtoList.add(paramDto);
		}
		return paramDtoList;
	}

	/**
	 * ユーザ情報を作成する。
	 *
	 * @param jgiNo 従業員番号
	 * @param targetJokenSetList 実行可能な条件セット
	 * @return ユーザ情報
	 * @throws DataNotFoundException ユーザ情報が見つからない場合にスロー
	 */
	protected DpUser createDpUser(Integer jgiNo, JokenSet[] targetJokenSetList) throws DataNotFoundException {
		JgiKb[] JGI_KB_ARRAY = { JgiKb.JGI, JgiKb.CONTRACT_MR, JgiKb.DAIKOU_USER };
		JgiMst jgiMst = jgiMstDAO.searchByJgiKb(jgiNo, JGI_KB_ARRAY);
		SosMst sosMst = sosMstDAO.search(jgiMst.getSosCd());
		DpRole dpRole = new DpRole(jgiJokenDAO.searchJokenSet(jgiNo, targetJokenSetList));
		List<JknGrp> jknGrpList = dpsJknGrpDao.searchListByJgiNo(jgiNo);
		List<JokenSet> tokuyakutenJokenSetCodes = dpsCodeMasterSearchService.searchTokuyakuJokenSetCd();
		DpUser dpUser = new DpUser(jgiMstDAO.searchByJgiKb(jgiNo, JGI_KB_ARRAY), sosMst, dpRole, jknGrpList,tokuyakutenJokenSetCodes);
		return dpUser;
	}
}
