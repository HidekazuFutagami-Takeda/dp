package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.TmsTytenPlanAddDetailDto;
import jp.co.takeda.dto.TmsTytenPlanAddInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddScDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.SosCode;
import jp.co.takeda.security.DpUserInfo;

/**
 * 特約店別計画追加・更新サービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsTmsTytenPlanAddService")
public class DpsTmsTytenPlanAddServiceImpl implements DpsTmsTytenPlanAddService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsTmsTytenPlanAddServiceImpl.class);

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	/**
	 * 特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanDao")
	protected WsPlanDao wsPlanDao;

	/**
	 * 計画品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckService")
	protected DpsWsStatusCheckService dpsWsStatusCheckService;

	// 特約店名検索
	public String searchTmsTytenName(final String tmsTytenCd) throws DataNotFoundException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (StringUtils.isBlank(tmsTytenCd)) {
			final String errMsg = "特約店別名称取得時にTMS特約店コードがnullまたは空。tmsTytenCd=" + tmsTytenCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// 検索処理
		// ---------------------------------
		try {
			TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnDAO.search(tmsTytenCd);
			return tmsTytenMstUn.getTmsTytenMeiKj();
		} catch (DataNotFoundException e) {
			final String errMsg = "特約店名取得に失敗。tmsTytenCd=" + tmsTytenCd;
			final MessageKey messageKey = new MessageKey("DPS3312E", tmsTytenCd);
			throw new DataNotFoundException(new Conveyance(messageKey, errMsg), e);
		}
	}

	// 営業所名検索
	public String searchOfficeName(String brCode, String distCode) throws DataNotFoundException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (StringUtils.isBlank(brCode)) {
			final String errMsg = "営業所名取得時に支店3桁コードがnullまたは空。brCode=" + brCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(distCode)) {
			final String errMsg = "営業所名取得時に営業所3桁コードがnullまたは空。distCode=" + distCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// 検索処理
		// ---------------------------------
		try {
			SosMst office = searchSosMst(brCode, distCode);
			SosMst shiten = sosMstDAO.search(office.getUpSosCd());
			return shiten.getBumonSeiName() + " " + office.getBumonSeiName();
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所名取得に失敗。brCode=" + brCode + "distCode=" + distCode;
			final MessageKey messageKey = new MessageKey("DPS3311E", brCode, distCode);
			throw new DataNotFoundException(new Conveyance(messageKey, errMsg), e);
		}
	}

	// 特約店別計画追加 品目一覧検索
	public TmsTytenPlanAddResultDto searchProdList(TmsTytenPlanAddScDto tmsTytenPlanAddScDto, String category)
			throws DataNotFoundException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanAddScDto == null) {
			final String errMsg = "特約店別計画追加検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String brCode = tmsTytenPlanAddScDto.getBrCodeInput();
		if (StringUtils.isBlank(brCode)) {
			final String errMsg = "特約店別計画追加処理時に支店3桁コードがnullまたは空。brCode=" + brCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String distCode = tmsTytenPlanAddScDto.getDistCodeInput();
		if (StringUtils.isBlank(distCode)) {
			final String errMsg = "特約店別計画追加処理時に営業所3桁コードがnullまたは空。distCode=" + distCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		KaBaseKb kaBaseKb = tmsTytenPlanAddScDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "特約店別計画追加処理時に価格区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String tmsTytenCd = tmsTytenPlanAddScDto.getTmsTytenCd();
		if (StringUtils.isBlank(tmsTytenCd)) {
			final String errMsg = "特約店別計画追加処理時に特約店コードがnullまたは空。tmsTytenCd=" + tmsTytenCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// 組織情報検索処理
		// ---------------------------------
		SosMst sosMst = searchSosMst(brCode, distCode);
		String sosCd = sosMst.getSosCd();


		// ---------------------------------
		// 特約店情報検索処理
		// ---------------------------------
		TmsTytenMstUn tmsTytenMstUn = null;
		try {
			tmsTytenMstUn = tmsTytenMstUnDAO.search(tmsTytenCd);
		} catch (DataNotFoundException e) {
			final MessageKey messageKey = new MessageKey("DPS3312E", tmsTytenCd);
			throw new DataNotFoundException(new Conveyance(messageKey));
		}

		// ---------------------------------
		// 品目一覧 + 特約店別計画検索処理
		// ---------------------------------
		Map<String, WsPlan> wsPlanMap = new HashMap<String, WsPlan>();
		List<WsPlan> wsPlanList = null;
		try {

			// 特約店別計画はない場合もあるのでNotFoundは吸収
			wsPlanList = wsPlanDao.searchListFilterByCategory(WsPlanDao.SORT_STRING2, tmsTytenCd, sosCd, kaBaseKb, category);
			for (WsPlan wsPlan : wsPlanList) {
				wsPlanMap.put(wsPlan.getProdCode(), wsPlan); // キー：品目固定コード、値：特約店別計画
			}
		} catch (DataNotFoundException e) {
			wsPlanList = new ArrayList<WsPlan>(0);
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
		if (category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
		} else {
			sales = Sales.IYAKU;
		}

		List<PlannedProd> plannedProdList = null;
		try {
// mod Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
//			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category, null);
			plannedProdList = plannedProdDAO.searchDistributorList(PlannedProdDAO.SORT_STRING, sales, category, null);
/// mod End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
		} catch (DataNotFoundException e) {
			final String errMsg = "品目情報の取得に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}

		List<TmsTytenPlanAddDetailDto> resultList = new ArrayList<TmsTytenPlanAddDetailDto>();
		for (PlannedProd plannedProd : plannedProdList) {
			Boolean planLevelWs = plannedProd.getPlanLevelWs();
			if (planLevelWs != null && planLevelWs) {
				resultList.add(new TmsTytenPlanAddDetailDto(plannedProd, wsPlanMap.get(plannedProd.getProdCode())));
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("特約店別計画立案レベルが真ではないので、除外。prodCode=" + plannedProd.getProdCode());
				}
			}
		}

		// 追加可能かの判定
		// 品目数と計画数が一致する場合、追加不可
		boolean addFlg = true;
		if (wsPlanList.size() >= resultList.size()) {
			addFlg = false;
		}

		return new TmsTytenPlanAddResultDto(resultList, addFlg, sosMst, tmsTytenMstUn);
	}

	// 特約店別計画追加
	public int addWsPlanProdList(TmsTytenPlanAddInputDto tmsTytenPlanAddInputDto,String category) throws LogicalException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanAddInputDto == null) {
			final String errMsg = "特約店別計画追加処理時に対象DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String brCode = tmsTytenPlanAddInputDto.getBrCode();
		if (StringUtils.isBlank(brCode)) {
			final String errMsg = "特約店別計画追加処理時に支店3桁コードがnullまたは空。brCode=" + brCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String distCode = tmsTytenPlanAddInputDto.getDistCode();
		if (StringUtils.isBlank(distCode)) {
			final String errMsg = "特約店別計画追加処理時に営業所3桁コードがnullまたは空。distCode=" + distCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		KaBaseKb kaBaseKb = tmsTytenPlanAddInputDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "特約店別計画追加処理時に価格区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String tmsTytenCd = tmsTytenPlanAddInputDto.getTmsTytenCd();
		if (StringUtils.isBlank(tmsTytenCd)) {
			final String errMsg = "特約店別計画追加処理時に特約店コードがnullまたは空。tmsTytenCd=" + tmsTytenCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<TmsTytenPlanAddDetailDto> inputList = tmsTytenPlanAddInputDto.getTmsTytenPlanAddDetailDto();
		if (inputList == null) {
			final String errMsg = "特約店別計画追加登録リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// 組織情報検索処理
		// ---------------------------------
		SosMst sosMst = searchSosMst(brCode, distCode);
//		add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		String sosCd = sosMst.getSosCd();
		String sosCd = SosCode.KOTEI.getDbValue();
//		add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする


		// add start 2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		//		// ONC組織の場合はデータなし
		//		if(BooleanUtils.isTrue(sosMst.getOncSosFlg())){
		// MMP組織以外の場合はデータなし
//		if (ProdCategory.getInstance(sosMst.getSosCategory()) != ProdCategory.MMP) {
//			// add end 2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//			throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR));
//		}

		// ---------------------------------
		// 特約店情報検索処理
		// ---------------------------------
		try {
			tmsTytenMstUnDAO.search(tmsTytenCd);
		} catch (DataNotFoundException e) {
			final MessageKey messageKey = new MessageKey("DPS3312E", tmsTytenCd);
			throw new DataNotFoundException(new Conveyance(messageKey));
		}

		// ---------------------------------
		// ステータスチェック
		// ---------------------------------
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		if (sysManage == null) {
			final String errMsg = "ユーザ情報からシステム管理情報が取得出来ない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// S価変換処理後はY/B価の追加処理不可
		if (sysManage.getTransTFlg() && kaBaseKb.equals(KaBaseKb.Y_KA_BASE)) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3316E")));
		}

		// S価変換処理前はS価の追加処理不可
		if (!sysManage.getTransTFlg() && kaBaseKb.equals(KaBaseKb.S_KA_BASE)) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3314E")));
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		// 配分中は実行不可
		// 許可しないステータスリスト作成
		List<WsDistStatusForCheck> unallowableDistStatusList = new ArrayList<WsDistStatusForCheck>();
		unallowableDistStatusList.add(WsDistStatusForCheck.DISTING);
		// チェック実行
		try {
			dpsWsStatusCheckService.execute(category, unallowableDistStatusList, null);
		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3320E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

		// ---------------------------------
		// 追加処理開始
		// ---------------------------------
		int registCount = 0;
		for (TmsTytenPlanAddDetailDto addDto : inputList) {
			PlannedProd targetPlannedProd = addDto.getPlannedProd();
			WsPlan targetWsPlan = addDto.getWsPlan();

			// 念のため、明示的に生成 + 設定
			WsPlan regWsPlan = new WsPlan();
			// 品目固定コード
			regWsPlan.setProdCode(targetPlannedProd.getProdCode());
			// TMS特約店コード
			regWsPlan.setTmsTytenCd(tmsTytenCd);
			// 組織コード
			regWsPlan.setSosCd(sosCd);
			// 価ベース区分
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			regWsPlan.setKaBaseKb(kaBaseKb);
			// 計画値UH
			regWsPlan.setPlannedValueUh(targetWsPlan.getPlannedValueUh());
			// 計画値P
			regWsPlan.setPlannedValueP(targetWsPlan.getPlannedValueP());

			// 登録処理実行
			wsPlanDao.insert(regWsPlan);
		}
		return registCount;
	}

	/**
	 * 営業所の組織情報を取得する。
	 *
	 *
	 * @param brCode 医薬支店Ｃ
	 * @param distCode 医薬営業所Ｃ
	 * @return 営業所の組織情報
	 * @throws DataNotFoundException
	 */
	private SosMst searchSosMst(String brCode, String distCode) throws DataNotFoundException {

		// 組織検索
		try {
			SosMst sosMst = sosMstDAO.search(SosListType.SHITEN_LIST, brCode, distCode);
			return sosMst;
		} catch (DataNotFoundException e) {
			final MessageKey messageKey = new MessageKey("DPS3311E", brCode, distCode);
			throw new DataNotFoundException(new Conveyance(messageKey));
		}
	}
}
