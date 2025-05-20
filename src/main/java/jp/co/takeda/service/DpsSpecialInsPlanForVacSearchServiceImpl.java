package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
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
import jp.co.takeda.dao.DeliveryResultForVacDao;
import jp.co.takeda.dao.ExceptDistInsDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dao.SpecialInsPlanForVacDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.SpecialInsPlanForVacResultDto;
import jp.co.takeda.dto.SpecialInsPlanForVacScDto;
import jp.co.takeda.dto.SpecialInsPlanSearchForVacResultDto;
import jp.co.takeda.dto.SpecialInsPlanSearchForVacResultListDto;
import jp.co.takeda.logic.CreateSpecialInsPlanSearchForVacResultDtoLogic;
import jp.co.takeda.logic.DelInsLogic;
import jp.co.takeda.model.DeliveryResultForVac;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.ExceptDistIns;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.Sales;

/**
 * ワクチン用特定施設個別計画の検索に関するサービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsSpecialInsPlanForVacSearchService")
public class DpsSpecialInsPlanForVacSearchServiceImpl implements DpsSpecialInsPlanForVacSearchService {

	/**
	 * ワクチン用特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanForVacDao")
	protected SpecialInsPlanForVacDao specialInsPlanForVacDao;

	/**
	 * TODO:shi
	 * 統合後ワクチン用特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanForNewVacDao;

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 配分除外施設DAO
	 */
	@Autowired(required = true)
	@Qualifier("exceptDistInsDao")
	protected ExceptDistInsDao exceptDistInsDao;

	/**
	 * 支援の計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 従業員DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultForVacDao")
	protected DeliveryResultForVacDao deliveryResultForVacDao;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	public List<SpecialInsPlanForVacResultDto> searchSpecialInsPlanForVac(SpecialInsPlanForVacScDto scDto, PlanType planType) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd3() == null) {
			final String errMsg = "組織/従業員がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSpecialInsPlanRegType() == null) {
			final String errMsg = "絞込条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索処理実行
		// ----------------------
		List<SpecialInsPlanForVac> searchResultList = null;
		try {
			searchResultList = specialInsPlanForVacDao.searchList(SpecialInsPlanForVacDao.SORT_STRING, scDto, planType);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		return convertSpecialInsPlanForVacResultDto(searchResultList);
	}

	/**
	 * SpecialInsPlanForVac ⇒ SpecialInsPlanForVacResultDto 変換処理
	 *
	 * @param 変換元SpecialInsPlanForVacオブジェクトリスト
	 * @return SpecialInsPlanForVacResultDtoオブジェクトリスト
	 */
	private List<SpecialInsPlanForVacResultDto> convertSpecialInsPlanForVacResultDto(List<SpecialInsPlanForVac> specialInsPlanForVacList) {

		List<SpecialInsPlanForVacResultDto> specialInsPlanForVacResultDtoList = new ArrayList<SpecialInsPlanForVacResultDto>();

		// 変換元がNullの場合は配列0のリストを返却
		if (specialInsPlanForVacList == null || specialInsPlanForVacList.isEmpty()) {
			return specialInsPlanForVacResultDtoList;
		}

		for (SpecialInsPlanForVac specialInsPlanForVac : specialInsPlanForVacList) {

			// 施設名
			final String insName = specialInsPlanForVac.getInsAbbrName();

			// 重点先/一般先
			final ActivityType activityType = specialInsPlanForVac.getActivityType();
			String strAactivityType = "";
			if (activityType != null) {
				strAactivityType = activityType.name();
			}

			// 施設分類
			InsClass insClass = specialInsPlanForVac.getInsClass();
			OldInsrFlg oldInsFlg = specialInsPlanForVac.getOldInsrFlg();
			final String strInsClass = InsClass.getInsClassName(insClass, oldInsFlg);

			// 担当者
			final String jgiName = specialInsPlanForVac.getJgiName();

			// 特定施設個別計画立案済
			Boolean isRegOn = Boolean.FALSE;

			// 最終更新者
			final String upJgiName = specialInsPlanForVac.getUpJgiName();

			// 最終更新日
			final Date upDate = specialInsPlanForVac.getUpDate();
			if (upDate != null) {
				isRegOn = Boolean.TRUE;
			}

			// 施設コード
			final String insNo = specialInsPlanForVac.getInsNo();

			// 従業員番号
			final Integer jgiNo = specialInsPlanForVac.getJgiNo();

			// 削除フラグ
			final Boolean delFlg = new DelInsLogic(specialInsPlanForVac.getReqFlg(), specialInsPlanForVac.getDelFlg()).getDelFlg();

			// オブジェクト生成
			SpecialInsPlanForVacResultDto resultDto = new SpecialInsPlanForVacResultDto(insName, strAactivityType, strInsClass, jgiName, isRegOn, upJgiName, upDate, insNo, jgiNo,
				delFlg);

			specialInsPlanForVacResultDtoList.add(resultDto);
		}
		return specialInsPlanForVacResultDtoList;
	}

	public SpecialInsPlanSearchForVacResultListDto searchMrProdList(Integer jgiNo, String insNo) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 施設情報取得（選択された施設情報）
		// ----------------------
		DpsInsMst insMst = null;
		try {
			insMst = insMstDAO.search(insNo);
		} catch (DataNotFoundException e) {
			// 対象データが存在しない場合エラー
			final String messageKey = "DPC1029E";
			final String errMsg = "施設情報の検索結果0件";
			throw new LogicalException(new Conveyance(new MessageKey(messageKey, "施設"), errMsg), e);
		}

		// ----------------------
		// 特定施設個別計画最終更新日取得
		// ----------------------
		SpecialInsPlanForVac lastUpdateSpecialInsPlan = null;
		try {
			// 担当者立案の場合、担当者立案のレコードの最終更新日を取得
			lastUpdateSpecialInsPlan = specialInsPlanForVacDao.searchUpDate(jgiNo, insNo, PlanType.MR);
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}

		// ----------------------
		// 品目一覧取得
		// ----------------------
		List<PlannedProd> plannedProdList = null;
		// ワクチンコードの取得
		String vaccineCd = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();

		try {
			plannedProdList = plannedProdDAO.searchListByPlanLevel(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, vaccineCd, ProdPlanLevel.INS);
		} catch (DataNotFoundException e) {
			// 対象データが存在しない場合エラー
			final String messageKey = "DPC1029E";
			final String errMsg = "計画対象品目の検索結果0件";
			throw new LogicalException(new Conveyance(new MessageKey(messageKey, "施設"), errMsg), e);
		}

		// ----------------------
		// 施設情報取得（A調が紐付く場合A調も取得）
		// ----------------------
		List<DpsInsMst> insMstList = null;
		try {
			insMstList = insMstDAO.searchIncludeA(InsMstDAO.SORT_STRING3, jgiNo, insNo);
		} catch (DataNotFoundException e) {
			// 対象データが存在しない場合エラー
			final String messageKey = "DPC1029E";
			final String errMsg = "施設情報の検索結果0件";
			throw new LogicalException(new Conveyance(new MessageKey(messageKey, "施設"), errMsg), e);
		}

		// ----------------------
		// 配分除外施設情報取得
		// ----------------------
		// 配分除外施設リスト
		List<String> exceptDistInsList = new ArrayList<String>();
		// 配分除外施設品目リスト
		Map<String, List<String>> exceptDistProdList = new HashMap<String, List<String>>();
		for (DpsInsMst insMst2 : insMstList) {
			try {
				ExceptDistIns distIns = exceptDistInsDao.searchByInsNo(insMst2.getInsNo());
				if (distIns.getExceptProd() != null && distIns.getExceptProd().size() != 0) {
					List<String> prodList = new ArrayList<String>();
					for (ExceptProd exceptProd : distIns.getExceptProd()) {
						if (StringUtils.isNotEmpty(exceptProd.getProdCode())) {
							prodList.add(exceptProd.getProdCode());
						}
					}
					if (prodList.isEmpty()) {
						//品目が空白の場合、配分除外施設
						exceptDistInsList.add(distIns.getInsNo());
					}else {
						//品目がある場合、配分除外施設品目
						exceptDistProdList.put(distIns.getInsNo(), prodList);
					}

				} else {
					exceptDistInsList.add(distIns.getInsNo());
				}
			} catch (DataNotFoundException e) {
			}
		}

		// ----------------------
		// 特定施設個別計画取得
		// ----------------------
		List<SpecialInsPlanForVac> specialInsPlanList = new ArrayList<SpecialInsPlanForVac>();
		for (PlannedProd plannedProd : plannedProdList) {

			String prodCode = plannedProd.getProdCode();
			//担当者立案の取得
			try {
				List<SpecialInsPlanForVac> specialInsPlanListTmp = specialInsPlanForVacDao.searchByInsNo(SpecialInsPlanForVacDao.SORT_STRING2, jgiNo, insNo, prodCode, null);
				specialInsPlanList.addAll(specialInsPlanListTmp);
			} catch (DataNotFoundException e) {
				// エラーにはしない
			}
		}
		// ----------------------
		// 組織コード取得
		// ----------------------
		JgiMst jgiMst = null;
		String sosCd3 = "";
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
			sosCd3 = jgiMst.getSosCd3();
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}

		// ----------------------
		// 納入実績取得
		// ----------------------
		List<DeliveryResultForVac> deliveryResultList = new ArrayList<DeliveryResultForVac>();
		try {
			deliveryResultList = deliveryResultForVacDao.searchByInsIncludeA(DeliveryResultForVacDao.SORT_STRING3, insNo);
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// オブジェクト作成
		// ----------------------
		CreateSpecialInsPlanSearchForVacResultDtoLogic logic = new CreateSpecialInsPlanSearchForVacResultDtoLogic(plannedProdList, insMstList, specialInsPlanList,
			deliveryResultList, exceptDistInsList, exceptDistProdList);
		List<SpecialInsPlanSearchForVacResultDto> resultList = logic.convertSpecialInsMrPlanProdListResultDto();
		String jgiName = null;
		// add Start 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		String addrCodePref = null;
		// add End 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		Date upDate = null;
		if (lastUpdateSpecialInsPlan != null) {
			jgiName = lastUpdateSpecialInsPlan.getUpJgiName();
			upDate = lastUpdateSpecialInsPlan.getUpDate();
		}
		return new SpecialInsPlanSearchForVacResultListDto(jgiNo, insMst, resultList, jgiName, upDate, sosCd3, addrCodePref);
	}

	public SpecialInsPlanSearchForVacResultListDto searchOfficeProdList(Integer jgiNo, String insNo, String addrCodePref) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// add Start 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		if (StringUtils.isEmpty(addrCodePref)) {
			addrCodePref = null;
		}
		// add End 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示

		// ----------------------
		// 施設情報取得（選択された施設情報）
		// ----------------------
		DpsInsMst insMst = null;
		try {
			insMst = insMstDAO.search(insNo);
		} catch (DataNotFoundException e) {
			// 対象データが存在しない場合エラー
			final String messageKey = "DPC1029E";
			final String errMsg = "施設情報の検索結果0件";
			throw new LogicalException(new Conveyance(new MessageKey(messageKey, "施設"), errMsg), e);
		}

		// ----------------------
		// 特定施設個別計画最終更新日取得
		// ----------------------
		SpecialInsPlanForVac lastUpdateSpecialInsPlan = null;
		try {
			// 営業所立案の場合、営業所立案のレコードの最終更新日を取得
			lastUpdateSpecialInsPlan = specialInsPlanForVacDao.searchUpDate(jgiNo, insNo, PlanType.OFFICE);
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}

		// ----------------------
		// 品目一覧取得
		// ----------------------
		List<PlannedProd> plannedProdList = null;
		// ワクチンコードの取得
		String vaccineCd = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();
		try {
			plannedProdList = plannedProdDAO.searchListByPlanLevel(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, vaccineCd, ProdPlanLevel.INS);
		} catch (DataNotFoundException e) {
			// 対象データが存在しない場合エラー
			final String messageKey = "DPC1029E";
			final String errMsg = "計画対象品目の検索結果0件";
			throw new LogicalException(new Conveyance(new MessageKey(messageKey, "施設"), errMsg), e);
		}

		// ----------------------
		// 施設情報取得（A調が紐付く場合A調も取得）
		// ----------------------
		List<DpsInsMst> insMstList = null;
		try {
			insMstList = insMstDAO.searchIncludeA(InsMstDAO.SORT_STRING3, jgiNo, insNo);
		} catch (DataNotFoundException e) {
			// 対象データが存在しない場合エラー
			final String messageKey = "DPC1029E";
			final String errMsg = "施設情報の検索結果0件";
			throw new LogicalException(new Conveyance(new MessageKey(messageKey, "施設"), errMsg), e);
		}

		// ----------------------
		// 配分除外施設情報取得
		// ----------------------
		// 配分除外施設リスト
		List<String> exceptDistInsList = new ArrayList<String>();
		// 配分除外施設品目リスト
		Map<String, List<String>> exceptDistProdList = new HashMap<String, List<String>>();
		for (DpsInsMst insMst2 : insMstList) {
			try {
				ExceptDistIns distIns = exceptDistInsDao.searchByInsNo(insMst2.getInsNo());
				if (distIns.getExceptProd() != null && distIns.getExceptProd().size() != 0) {
					List<String> prodList = new ArrayList<String>();
					for (ExceptProd exceptProd : distIns.getExceptProd()) {
						if (StringUtils.isNotEmpty(exceptProd.getProdCode())) {
							prodList.add(exceptProd.getProdCode());
						}
					}
					if (prodList.isEmpty()) {
						//品目が空白の場合、配分除外施設
						exceptDistInsList.add(distIns.getInsNo());
					}else {
						//品目がある場合、配分除外施設品目
						exceptDistProdList.put(distIns.getInsNo(), prodList);
					}

				} else {
					exceptDistInsList.add(distIns.getInsNo());
				}
			} catch (DataNotFoundException e) {
			}
		}

		// ----------------------
		// 特定施設個別計画取得
		// ----------------------
		List<SpecialInsPlanForVac> specialInsPlanList = new ArrayList<SpecialInsPlanForVac>();
		for (PlannedProd plannedProd : plannedProdList) {

			String prodCode = plannedProd.getProdCode();
			//営業所立案の取得
			try {
				List<SpecialInsPlanForVac> specialInsPlanListTmp = specialInsPlanForVacDao.searchByInsNo(SpecialInsPlanForVacDao.SORT_STRING2, jgiNo, insNo, prodCode, PlanType.OFFICE);
				specialInsPlanList.addAll(specialInsPlanListTmp);
			} catch (DataNotFoundException e) {
				// エラーにはしない
			}
		}
		// ----------------------
		// 組織コード取得
		// ----------------------
		JgiMst jgiMst = null;
		String sosCd3 = "";
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
			sosCd3 = jgiMst.getSosCd3();
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}

		// ----------------------
		// 納入実績取得
		// ----------------------
		List<DeliveryResultForVac> deliveryResultList = new ArrayList<DeliveryResultForVac>();
		try {
			deliveryResultList = deliveryResultForVacDao.searchByInsIncludeA(DeliveryResultForVacDao.SORT_STRING3, insNo);
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// オブジェクト作成
		// ----------------------
		CreateSpecialInsPlanSearchForVacResultDtoLogic logic = new CreateSpecialInsPlanSearchForVacResultDtoLogic(plannedProdList, insMstList, specialInsPlanList,
			deliveryResultList, exceptDistInsList, exceptDistProdList);
		List<SpecialInsPlanSearchForVacResultDto> resultList = logic.convertSpecialInsPlanProdListResultDto();
		String jgiName = null;
		Date upDate = null;
		if (lastUpdateSpecialInsPlan != null) {
			jgiName = lastUpdateSpecialInsPlan.getUpJgiName();
			upDate = lastUpdateSpecialInsPlan.getUpDate();
		}
		return new SpecialInsPlanSearchForVacResultListDto(jgiNo, insMst, resultList, jgiName, upDate, sosCd3, addrCodePref);
	}
}
