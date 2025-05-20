package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.model.div.JokenSet.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
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
import jp.co.takeda.dao.DocDistParamHonbuDao;
import jp.co.takeda.dao.DocDistParamOfficeDao;
import jp.co.takeda.dao.InsDocPlanDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.InsDocPlanJgiListScDto;
import jp.co.takeda.dto.InsDocPlanProdListScDto;
import jp.co.takeda.dto.InsDocPlanUpDateScDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.CreateChoseiMsgLogic;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.ProdInsInfoErrKbn;
import jp.co.takeda.model.div.ProdInsInfoScanDispKbn;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.StatusForInsDocPlan;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 施設医師別計画機能の検索に関するサービス実装クラス
 * 
 * @author nozaki
 */
@Transactional
@Service("dpsInsDocPlanSearchService")
public class DpsInsDocPlanSearchServiceImpl implements DpsInsDocPlanSearchService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsInsDocPlanSearchServiceImpl.class);

	/**
	 * 施設医師別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanDao")
	protected InsDocPlanDao insDocPlanDao;

	/**
	 * 担当者別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 施設医師別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanStatusDao")
	protected InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * 計画対象品目取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 配分基準（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("docDistParamHonbuDao")
	protected DocDistParamHonbuDao docDistParamHonbuDao;

	/**
	 * 配分基準（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("docDistParamOfficeDao")
	protected DocDistParamOfficeDao docDistParamOfficeDao;

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
	 * 納入計画システム管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	// 施設医師別計画 担当者一覧情報取得
	public List<Map<String, Object>> searchMrList(InsDocPlanJgiListScDto scDto) throws LogicalException {

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
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd3;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		
		// ----------------------
		// 品目情報取得
		// ----------------------
		PlannedProd plannedProd;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// 組織と対象品目のカテゴリが一致しない場合はエラー
//		if(BooleanUtils.isTrue(sosMst.getOncSosFlg()) && plannedProd.getCategory() != ProdCategory.ONC){
		if(BooleanUtils.isTrue(sosMst.getOncSosFlg()) && !(ProdCategory.ONC.equals(plannedProd.getCategory()))){
			throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR));
		}
		if(BooleanUtils.isNotTrue(sosMst.getOncSosFlg()) && !(ProdCategory.ONC.equals(plannedProd.getCategory())) ){
			throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR));
		}

		// ----------------------
		// 担当者別計画表示
		// ----------------------
		// 所長以上は常にOK、ALはAL公開以降OK、MRは確定のみでOK
		boolean isMrPlanOpen = false;
		DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();
		if (settingUser.isMatch(HONBU, SITEN_MASTER, SITEN_STAFF, OFFICE_MASTER, OFFICE_STAFF)) {
			isMrPlanOpen = true;
		} else {
			try {
				StatusForMrPlan mrPlanStatus = mrPlanStatusDao.search(sosCd3, prodCode).getStatusForMrPlan();
				if(mrPlanStatus == StatusForMrPlan.MR_PLAN_COMPLETED){
					isMrPlanOpen = true;
				}
			} catch (DataNotFoundException e) {
			}
		}
		
		// ----------------------
		// 施設別計画 担当者一覧取得
		// ----------------------
		List<Map<String, Object>> jgiStatusList = insDocPlanStatusDao.searchJgiBaseList(sosCd3, sosCd4, prodCode);

		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> jgiStatusMap : jgiStatusList) {

			// ステータスが未作成の場合は作成
			if(jgiStatusMap.get("insDocPlanStatus") == null){
				jgiStatusMap.put("insDocPlanStatus",new InsDocPlanStatus());
			}

			// 品目情報を設定
			jgiStatusMap.put("prodCode", plannedProd.getProdCode());
			jgiStatusMap.put("prodName", plannedProd.getProdName());
			jgiStatusMap.put("category", plannedProd.getCategory()); 

			// 担当者別計画表示
			jgiStatusMap.put("isMrPlanOpen", isMrPlanOpen); 

			// 差額を計算、設定
			long sagakuUh = MathUtil.sub((Long)jgiStatusMap.get("mrPlanPlannedValueUh"), (Long)jgiStatusMap.get("insDocPlanPlannedValueUh"), true);
			long sagakup = MathUtil.sub((Long)jgiStatusMap.get("mrPlanPlannedValueP"), (Long)jgiStatusMap.get("insDocPlanPlannedValueP"), true);
			jgiStatusMap.put("sagakuUh", sagakuUh);
			jgiStatusMap.put("sagakuP", sagakup);
			
			resultList.add(jgiStatusMap);
		}
			
		return resultList;
	}

	// 施設医師別計画 品目一覧情報取得
	public List<Map<String, Object>> searchPlannedProdList(InsDocPlanProdListScDto scDto) throws LogicalException {

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

		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();
		Integer jgiNo = scDto.getJgiNo();

		// 副担当（担当施設なし）の場合、データなし
		if(jgiNo != null){
			try {
				jgiMstDAO.search(jgiNo);
			} catch (DataNotFoundException e) {
				throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR, "副担MR（施設を持たない）なのでデータなし"));
			}
		}

		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd3;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 組織からカテゴリ判定（MMP or ONC）	
		ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());	

		// ------------------------------
		// 施設特約店別計画ステータス最終更新日時取得（全体のサマリ）
		// ------------------------------
		Date statusLastUpdate = insDocPlanStatusDao.getLastUpDate(sosCd3, sosCd4, jgiNo, null);
		
		// ------------------------------
		// 施設特約店別計画ステータスサマリ取得
		// ------------------------------
		List<Map<String, Object>> prodStatusList = null;
		if (jgiNo != null) {
			prodStatusList = insDocPlanStatusDao.searchProdBaseList(null, null, jgiNo, category);

		} else if (sosCd4 != null) {
			prodStatusList = insDocPlanStatusDao.searchProdBaseList(null, sosCd4, null, category);

		} else if (sosCd3 != null) {
			prodStatusList = insDocPlanStatusDao.searchProdBaseList(sosCd3, null, null, category);

		} else {

			final String errMsg = "施設医師別計画ステータスサマリ取得条件が不正";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ログインユーザ情報
		DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();

		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> prodStatusMap : prodStatusList) {

			// 担当者別計画表示：所長以上は常にOK、AL・MRは確定のみでOK
			boolean isMrPlanOpen = false;
			if (settingUser.isMatch(HONBU, SITEN_MASTER, SITEN_STAFF, OFFICE_MASTER, OFFICE_STAFF)) {
				isMrPlanOpen = true;
			} else {
				try {
					StatusForMrPlan mrPlanStatus = mrPlanStatusDao.search(sosCd3, (String)prodStatusMap.get("prodCode")).getStatusForMrPlan();
					if(mrPlanStatus == StatusForMrPlan.MR_PLAN_COMPLETED){
						isMrPlanOpen = true;
					}
				} catch (DataNotFoundException e) {
				}
			}
			prodStatusMap.put("isMrPlanOpen", isMrPlanOpen); 

			// 差額を計算、設定
			long sagakuUh = MathUtil.sub((Long)prodStatusMap.get("mrPlanPlannedValueUh"), (Long)prodStatusMap.get("insDocPlanPlannedValueUh"), true);
			long sagakup = MathUtil.sub((Long)prodStatusMap.get("mrPlanPlannedValueP"), (Long)prodStatusMap.get("insDocPlanPlannedValueP"), true);
			prodStatusMap.put("sagakuUh", sagakuUh);
			prodStatusMap.put("sagakuP", sagakup);

			// ステータス最終更新日時設定
			prodStatusMap.put("statusLastUpdate", statusLastUpdate);

			resultList.add(prodStatusMap);
		}

		return resultList;
	}

	// 施設医師別計画ヘッダーの検索処理
	public Map<String, Object> searchInsDocPlanHeader(InsDocPlanUpDateScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設医師別計画の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		Integer jgiNo = scDto.getJgiNo();
		String prodCode = scDto.getProdCode();
		InsType insType = scDto.getInsType();
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

		try {
			
			SysManage sysManage =  sysManageDao.search(SysClass.DPS, SysType.IYAKU);
			
			// 従業員情報取得
			JgiMst jgiMst = jgiMstDAO.search(jgiNo);

			// パラメータ取得
			RefPeriod refFrom = null;
			RefPeriod refTo = null;
			
			try {
				Map<String, Object> distParamMap = docDistParamOfficeDao.search(jgiMst.getSosCd3(), prodCode, insType);
				refFrom = (RefPeriod)distParamMap.get("refFrom");
				refTo = (RefPeriod)distParamMap.get("refTo");
				
			} catch (DataNotFoundException e) {
				Map<String, Object> distParamMap = docDistParamHonbuDao.search(prodCode, insType);
				refFrom = (RefPeriod)distParamMap.get("refFrom");
				refTo = (RefPeriod)distParamMap.get("refTo");
			}
			
			// ----------------------
			// ヘッダー情報取得
			// ----------------------
			Map<String, Object> resultData = insDocPlanDao.searchHeader(jgiNo, prodCode, insType);

			// ----------------------
			// 表示判定（AL・MRは公開前は施設医師積上げをNULL）
			// ----------------------
			DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();
			if (settingUser.isMatch(IYAKU_AL,IYAKU_MR)){

				try {

					// 担当者別計画が確定してない場合はダメ
					StatusForMrPlan mrPlanStatus = mrPlanStatusDao.search(jgiMst.getSosCd3(),prodCode).getStatusForMrPlan();
					if(mrPlanStatus != StatusForMrPlan.MR_PLAN_COMPLETED){
						resultData.put("insDocPlanPlannedValueUh",null);
						resultData.put("insDocPlanPlannedValueP",null);
					}

					// 配分中、配分済みはダメ
					StatusForInsDocPlan insDocPlanStatus = insDocPlanStatusDao.search(jgiNo, prodCode).getStatusForInsDocPlan();				
					if(insDocPlanStatus == StatusForInsDocPlan.DISTING || insDocPlanStatus == StatusForInsDocPlan.DISTED ) {
						resultData.put("insDocPlanPlannedValueUh",null);
						resultData.put("insDocPlanPlannedValueP",null);
					}

				} catch (DataNotFoundException e) {

					// 試算前、配分前はダメ
					resultData.put("insDocPlanPlannedValueUh",null);
					resultData.put("insDocPlanPlannedValueP",null);
				}
			}

			// 実績参照期間設定
			resultData.put("refFrom", DateUtil.toString(RefPeriod.convertRefPeriod(refFrom, sysManage.getSysYear(), sysManage.getSysTerm()),"MM"));
			resultData.put("refTo", DateUtil.toString(RefPeriod.convertRefPeriod(refTo, sysManage.getSysYear(), sysManage.getSysTerm()),"MM"));

			// 差額を計算、設定
			long sagakuUh = MathUtil.sub((Long)resultData.get("mrPlanPlannedValueUh"), (Long)resultData.get("insDocPlanPlannedValueUh"), true);
			long sagakup = MathUtil.sub((Long)resultData.get("mrPlanPlannedValueP"), (Long)resultData.get("insDocPlanPlannedValueP"), true);
			resultData.put("sagakuUh", sagakuUh);
			resultData.put("sagakuP", sagakup);
			
			return resultData;

		} catch (DataNotFoundException e) {
			final String errMsg = "施設医師別計画のヘッダー情報取得失敗：" + jgiNo + "," + prodCode;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}

	// 施設医師別計画一覧の検索処理
	public List<Map<String, Object>> searchInsDocPlanList(InsDocPlanUpDateScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設医師別計画の検索用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		Integer jgiNo = scDto.getJgiNo();
		String prodCode = scDto.getProdCode();
		InsType insType = scDto.getInsType();	
		String planDispType = scDto.getPlanDispType();
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

		// 従業員情報取得
		JgiMst jgiMst = jgiMstDAO.search(jgiNo);

		// ----------------------
		// 表示判定（AL・MRは公開前はエラー）
		// ----------------------
		DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();
		if (settingUser.isMatch(IYAKU_AL,IYAKU_MR)){

			try {
				
				// 担当者別計画が確定してない場合はダメ
				StatusForMrPlan mrPlanStatus = mrPlanStatusDao.search(jgiMst.getSosCd3(),prodCode).getStatusForMrPlan();
				if(mrPlanStatus != StatusForMrPlan.MR_PLAN_COMPLETED){
					List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
					messageKeyList.add(new MessageKey("DPS3348E"));
					throw new UnallowableStatusException(new Conveyance(messageKeyList));
				}

				// 配分中、配分済みはダメ
				StatusForInsDocPlan insDocPlanStatus = insDocPlanStatusDao.search(jgiNo, prodCode).getStatusForInsDocPlan();				
				if(insDocPlanStatus == StatusForInsDocPlan.DISTING || insDocPlanStatus == StatusForInsDocPlan.DISTED ) {
					List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
					messageKeyList.add(new MessageKey("DPS3348E"));
					throw new UnallowableStatusException(new Conveyance(messageKeyList));
				}

			} catch (DataNotFoundException e) {

				// 試算前、配分前はダメ
				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
				messageKeyList.add(new MessageKey("DPS3348E"));
				throw new UnallowableStatusException(new Conveyance(messageKeyList));
			}
		}
		
		// ----------------------
		// 施設医師別計画取得
		// ----------------------
		List<Map<String, Object>> insDocPlanList = insDocPlanDao.searchList(jgiNo, prodCode, insType, planDispType);		
		if(LOG.isDebugEnabled()){
			LOG.debug("施設医師件数：従業員=" + jgiNo + ",品目=" + prodCode + ",件数=" +insDocPlanList.size());
		}
		
		// 合算施設合計
		long nonPatientCnt = 0;
		long totPatientCnt = 0;
		long tkdPatientCnt = 0;
		long currentPeriod = 0;
		long advancePeriod = 0;
		long theoreticalIncValueY = 0;
		long plannedIncValueY = 0;
		long theoreticalValueY = 0;
		long plannedValueY = 0;
		boolean dispDataEexists = false;

		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (int i = 0 ; i < insDocPlanList.size() ; i ++) {

			Map<String, Object> insDocPlan = insDocPlanList.get(i);
			
			// 計画立案対象外でない、または、確定値がある場合は対象
			ProdInsInfoScanDispKbn dispKbn = (ProdInsInfoScanDispKbn)insDocPlan.get("scrnDispKb");
			if (ProdInsInfoScanDispKbn.NOT != dispKbn || insDocPlan.get("plannedValueY") != null ) {

				// 施設類の設定（本院、A調、B調）
				InsClass insClass = (InsClass)insDocPlan.get("insClass");
				OldInsrFlg oldInsrFlg = (OldInsrFlg)insDocPlan.get("oldInsrFlg");
				String insHeader =InsClass.getInsClassName(insClass, oldInsrFlg);
				insDocPlan.put("insHeader", insHeader);
				
				// 施設表示区分の設定
				ProdInsInfoErrKbn errKbn = (ProdInsInfoErrKbn)insDocPlan.get("errKb");
				insDocPlan.put("errFlg",  errKbn == ProdInsInfoErrKbn.ERROR);
				insDocPlan.put("alertFlg", errKbn == ProdInsInfoErrKbn.ALERT);
				
				// 結果リストに追加
				insDocPlan.put("isSumRow", false);
				resultList.add(insDocPlan);
				dispDataEexists = true;
				
				// 合算施設合計集計
				nonPatientCnt = MathUtil.add(nonPatientCnt, (Long)insDocPlan.get("nonPatientCnt"));
				totPatientCnt = MathUtil.add(totPatientCnt, (Long)insDocPlan.get("totPatientCnt"));
				tkdPatientCnt = MathUtil.add(tkdPatientCnt, (Long)insDocPlan.get("tkdPatientCnt"));
				currentPeriod = MathUtil.add(currentPeriod, (Long)insDocPlan.get("currentPeriod"));
				advancePeriod = MathUtil.add(advancePeriod, (Long)insDocPlan.get("advancePeriod"));
				theoreticalIncValueY = MathUtil.add(theoreticalIncValueY, (Long)insDocPlan.get("theoreticalIncValueY"));
				plannedIncValueY = MathUtil.add(plannedIncValueY, (Long)insDocPlan.get("plannedIncValueY"));
				theoreticalValueY = MathUtil.add(theoreticalValueY, (Long)insDocPlan.get("theoreticalValueY"));
				plannedValueY = MathUtil.add(plannedValueY, (Long)insDocPlan.get("plannedValueY"));
			}

			// 最後、または、次の施設内部が異なる場合は集計行作成
			String relnInsNo = (String)insDocPlan.get("relnInsNo");
			if (i == insDocPlanList.size() - 1 || !(relnInsNo).equals((String)insDocPlanList.get(i+1).get("relnInsNo"))) {
				
				// データ
				if(dispDataEexists){
					Map<String, Object> sumRowData = new HashMap<String, Object>();
					sumRowData.put("insNo", "");
					sumRowData.put("relnInsNo", "");
					sumRowData.put("nonPatientCnt", nonPatientCnt);
					sumRowData.put("totPatientCnt", totPatientCnt);
					sumRowData.put("tkdPatientCnt", tkdPatientCnt);
					sumRowData.put("currentPeriod", currentPeriod);
					sumRowData.put("advancePeriod", advancePeriod);
					sumRowData.put("theoreticalIncValueY", theoreticalIncValueY);
					sumRowData.put("plannedIncValueY", plannedIncValueY);
					sumRowData.put("theoreticalValueY", theoreticalValueY);
					sumRowData.put("plannedValueY", plannedValueY);
					sumRowData.put("isSumRow", true);
					resultList.add(sumRowData);
				}

				// 初期化
				nonPatientCnt = 0;
				totPatientCnt = 0;
				tkdPatientCnt = 0;
				currentPeriod = 0;
				advancePeriod = 0;
				theoreticalIncValueY = 0;
				plannedIncValueY = 0;
				theoreticalValueY = 0;
				plannedValueY = 0;
				dispDataEexists = false;
			}
		}
		
		return resultList;
	}

	// 調整金額用のメッセージを生成
	public String createChoseiMsg(Map<String, Object> resultData) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (resultData == null) {
			final String errMsg = "検索結果がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Long aUh = (Long)resultData.get("mrPlanPlannedValueUh");
		Long aP = (Long)resultData.get("mrPlanPlannedValueP");

		Long tUh = (Long)resultData.get("insDocPlanPlannedValueUh");
		Long tP = (Long)resultData.get("insDocPlanPlannedValueP");

		if (aUh == null) {
			aUh = 0L;
		}
		if (aP == null) {
			aP = 0L;
		}
		if (tUh == null) {
			tUh = 0L;
		}
		if (tP == null) {
			tP = 0L;
		}

		boolean uhChoseiFlg = !(aUh.equals(tUh));
		boolean pChoseiFlg = !(aP.equals(tP));

		return new CreateChoseiMsgLogic(uhChoseiFlg, pChoseiFlg).createMsg();
	}
}
