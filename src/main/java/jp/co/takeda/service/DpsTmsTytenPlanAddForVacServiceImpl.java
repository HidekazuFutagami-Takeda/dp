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
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanForVacDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.TmsTytenPlanAddForVacDetailDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacScDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.SosCode;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 特約店別計画追加・更新サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsTmsTytenPlanAddForVacService")
public class DpsTmsTytenPlanAddForVacServiceImpl implements DpsTmsTytenPlanAddForVacService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsTmsTytenPlanAddForVacServiceImpl.class);

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
	 * ワクチン特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanForVacDao")
	protected WsPlanForVacDao wsPlanForVacDao;

	/**
	 * 計画品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * ワクチン用特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsWsStatusCheckForVacService")
	protected DpsWsStatusCheckForVacService dpsWsStatusCheckForVacService;

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
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

	// エリア特約店Ｇ名検索
	public String searchAreaGName(String brCode, String distCode) throws DataNotFoundException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (StringUtils.isBlank(brCode)) {
			final String errMsg = "エリア特約店G名取得時に支店3桁コードがnullまたは空。brCode=" + brCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(distCode)) {
			final String errMsg = "エリア特約店G名取得時に営業所3桁コードがnullまたは空。distCode=" + distCode;
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
			final String errMsg = "エリア特約店G名取得に失敗。brCode=" + brCode + "distCode=" + distCode;
			final MessageKey messageKey = new MessageKey("DPS3313E", brCode, distCode);
			throw new DataNotFoundException(new Conveyance(messageKey, errMsg), e);
		}
	}

	// 特約店別計画追加 品目一覧検索
	public TmsTytenPlanAddForVacResultDto searchProdList(TmsTytenPlanAddForVacScDto tmsTytenPlanAddForVacScDto) throws DataNotFoundException {

		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanAddForVacScDto == null) {
			final String errMsg = "ワクチン特約店別計画追加検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String brCode = tmsTytenPlanAddForVacScDto.getBrCodeInput();
		if (StringUtils.isBlank(brCode)) {
			final String errMsg = "ワクチン特約店別計画追加処理時に支店3桁コードがnullまたは空。brCode=" + brCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String distCode = tmsTytenPlanAddForVacScDto.getDistCodeInput();
		if (StringUtils.isBlank(distCode)) {
			final String errMsg = "ワクチン特約店別計画追加処理時に営業所3桁コードがnullまたは空。distCode=" + distCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = tmsTytenPlanAddForVacScDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "特約店別計画追加処理時に価格区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		String tmsTytenCd = tmsTytenPlanAddForVacScDto.getTmsTytenCd();
		if (StringUtils.isBlank(tmsTytenCd)) {
			final String errMsg = "ワクチン特約店別計画追加処理時に特約店コードがnullまたは空。tmsTytenCd=" + tmsTytenCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ---------------------------------
		// 特約店情報検索処理
		// ---------------------------------
		TmsTytenMstUn tmstytenMstUn = null;
		try {
			tmstytenMstUn = tmsTytenMstUnDAO.search(tmsTytenCd);
		} catch (DataNotFoundException e) {
			final MessageKey messageKey = new MessageKey("DPS3312E", tmsTytenCd);
			throw new DataNotFoundException(new Conveyance(messageKey));
		}

		// ---------------------------------
		// 組織情報検索処理
		// ---------------------------------
		SosMst sosMst = searchSosMst(brCode, distCode);
		String sosCd = sosMst.getSosCd();

		// ---------------------------------
		// 品目一覧 + 特約店別計画検索処理
		// ---------------------------------
		Map<String, WsPlanForVac> wsPlanMap = new HashMap<String, WsPlanForVac>();
		List<WsPlanForVac> wsPlanList = null;
		try {

			// 特約店別計画はない場合もあるのでNotFoundは吸収
			wsPlanList = wsPlanForVacDao.searchList(WsPlanForVacDao.SORT_STRING2, null, tmsTytenCd, null, null, sosCd, kaBaseKb);

			for (WsPlanForVac wsPlan : wsPlanList) {
				wsPlanMap.put(wsPlan.getProdCode(), wsPlan); // キー：品目固定コード、値：特約店別計画
			}
		} catch (DataNotFoundException e) {
			wsPlanList = new ArrayList<WsPlanForVac>(0);
		}

		List<PlannedProd> plannedProdList = null;
		try {
// mod Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
//			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, null, null);
			plannedProdList = plannedProdDAO.searchDistributorList(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, null, null);
// mod End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
		} catch (DataNotFoundException e) {
			final String errMsg = "品目情報の取得に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}

		List<TmsTytenPlanAddForVacDetailDto> resultList = new ArrayList<TmsTytenPlanAddForVacDetailDto>();
		for (PlannedProd plannedProd : plannedProdList) {
			Boolean planLevelWs = plannedProd.getPlanLevelWs();
			if (planLevelWs != null && planLevelWs) {
				resultList.add(new TmsTytenPlanAddForVacDetailDto(plannedProd, wsPlanMap.get(plannedProd.getProdCode())));
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
		return new TmsTytenPlanAddForVacResultDto(resultList, addFlg, sosMst, tmstytenMstUn);
	}

	// ワクチン特約店別計画追加
	public int addWsPlanProdList(TmsTytenPlanAddForVacInputDto tmsTytenPlanAddForVacInputDto,String category) throws LogicalException {
		// ---------------------------------
		// 引数チェック
		// ---------------------------------
		if (tmsTytenPlanAddForVacInputDto == null) {
			final String errMsg = "ワクチン特約店別計画追加処理時に対象DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String brCode = tmsTytenPlanAddForVacInputDto.getBrCode();
		if (StringUtils.isBlank(brCode)) {
			final String errMsg = "ワクチン特約店別計画追加処理時に支店3桁コードがnullまたは空。brCode=" + brCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String distCode = tmsTytenPlanAddForVacInputDto.getDistCode();
		if (StringUtils.isBlank(distCode)) {
			final String errMsg = "ワクチン特約店別計画追加処理時に営業所3桁コードがnullまたは空。distCode=" + distCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String tmsTytenCd = tmsTytenPlanAddForVacInputDto.getTmsTytenCd();
		if (StringUtils.isBlank(tmsTytenCd)) {
			final String errMsg = "ワクチン特約店別計画追加処理時に特約店コードがnullまたは空。tmsTytenCd=" + tmsTytenCd;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<TmsTytenPlanAddForVacDetailDto> inputList = tmsTytenPlanAddForVacInputDto.getDetailList();
		if (inputList == null) {
			final String errMsg = "ワクチン特約店別計画追加登録リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = tmsTytenPlanAddForVacInputDto.getKaBaseKb();
		if (kaBaseKb == null) {
			final String errMsg = "ワクチン特約店別計画追加処理時に価ベースがnull。KaBaseKb=" + kaBaseKb;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

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
		// 組織情報検索処理
		// ---------------------------------
		SosMst sosMst = searchSosMst(brCode, distCode);
//	 add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		String sosCd = sosMst.getSosCd();
		String sosCd = SosCode.KOTEI.getDbValue();
//	 add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		// ---------------------------------
		// ステータスチェック
		// ---------------------------------
		// 施設特約店〆処理フラグ取得（ワクチン用）
		SysManage sysManage = null;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.VACCINE);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画管理情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// S価変換処理後はY/B価の追加処理不可
		if (sysManage.getTransTFlg() && kaBaseKb.equals(KaBaseKb.Y_KA_BASE)) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3316E")));
		}

		// S/Y変換処理前はS価の追加処理不可
//		if (!sysManage.getTransTFlg()) {
		if (!sysManage.getTransTFlg() && kaBaseKb.equals(KaBaseKb.S_KA_BASE)) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3314E")));
		}

		//  施設特約店〆処理フラグチェック
//		if (!sysManage.getWsEndFlg()) {
//			throw new LogicalException(new Conveyance(new MessageKey("DPS3324E")));
//		}

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
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		}

		// ---------------------------------
		// 追加処理開始
		// ---------------------------------
		int registCount = 0;
		for (TmsTytenPlanAddForVacDetailDto addDto : inputList) {
			PlannedProd targetPlannedProd = addDto.getPlannedProd();
			WsPlanForVac targetWsPlan = addDto.getWsPlanForVac();

			// 念のため、明示的に生成 + 設定
			WsPlanForVac regWsPlan = new WsPlanForVac();
			// 品目固定コード
			regWsPlan.setProdCode(targetPlannedProd.getProdCode());
			// TMS特約店コード
			regWsPlan.setTmsTytenCd(tmsTytenCd);
			// 組織コード
			regWsPlan.setSosCd(sosCd);
			// 価ベース区分
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			regWsPlan.setKaBaseKb(kaBaseKb);
			// 計画値
			regWsPlan.setPlannedValue(targetWsPlan.getPlannedValue());

			// 登録処理実行
			wsPlanForVacDao.insert(regWsPlan);
		}
		return registCount;
	}

	/**
	 * エリア特約店Ｇの組織情報を取得する。
	 *
	 * @param brCode 医薬支店Ｃ
	 * @param distCode 医薬営業所Ｃ
	 * @return エリア特約店Ｇの組織情報
	 * @throws DataNotFoundException
	 */
	private SosMst searchSosMst(String brCode, String distCode) throws DataNotFoundException {

		// 組織検索
		try {
			SosMst sosMst = sosMstDAO.search(SosListType.TOKUYAKUTEN_LIST, brCode, distCode);
			return sosMst;
		} catch (DataNotFoundException e) {
			final MessageKey messageKey = new MessageKey("DPS3313E", brCode, distCode);
			throw new DataNotFoundException(new Conveyance(messageKey));
		}
	}
}
