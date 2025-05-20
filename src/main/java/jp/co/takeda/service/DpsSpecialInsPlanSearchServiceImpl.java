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
import jp.co.takeda.dao.DeliveryResultDao;
import jp.co.takeda.dao.ExceptDistInsDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.ProdInsInfoKanriDao;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dto.SpecialInsPlanProdListResultDto;
import jp.co.takeda.dto.SpecialInsPlanResultDto;
import jp.co.takeda.dto.SpecialInsPlanScDto;
import jp.co.takeda.dto.SpecialInsPlanSearchOfficeDto;
import jp.co.takeda.logic.CreateSpecialInsPlanProdListResultDtoLogic;
import jp.co.takeda.logic.DelInsLogic;
import jp.co.takeda.model.DeliveryResult;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.ExceptDistIns;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.div.DpsHoInsType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.util.ConvertUtil;

/**
 * 特定施設個別計画の検索に関するサービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsSpecialInsPlanSearchService")
public class DpsSpecialInsPlanSearchServiceImpl implements DpsSpecialInsPlanSearchService {

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanDao;

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
	@Qualifier("deliveryResultDao")
	protected DeliveryResultDao deliveryResultDao;

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 品目施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("prodInsInfoKanriDao")
	protected ProdInsInfoKanriDao prodInsInfoKanriDao;

	/**
	 * カテゴリ・品目の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;


	public SpecialInsPlanSearchOfficeDto searchOfficeProdList(Integer jgiNo, String insNo, String category, boolean mrFlg) throws LogicalException {

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
		//ProdCategory prodCategory = ProdCategory.getInstance(category);
		String prodCategory = category;

		// ------------------------------------
		// 施設情報取得（選択された施設情報）
		// ------------------------------------
		DpsInsMst insMst = null;
		try {
			insMst = insMstDAO.search(insNo);
		} catch (DataNotFoundException e) {
			// 対象データが存在しない場合エラー
			final String messageKey = "DPC1029E";
			final String errMsg = "施設情報の検索結果0件";
			throw new LogicalException(new Conveyance(new MessageKey(messageKey, "施設"), errMsg), e);
		}

		// ------------------------------------
		// 特定施設個別計画最終更新日取得
		// ------------------------------------
		SpecialInsPlan lastUpdateSpecialInsPlan = null;
		try {
			// 担当者立案の場合、担当者立案のレコードの最終更新日を取得
			if (mrFlg) {
				lastUpdateSpecialInsPlan = specialInsPlanDao.searchUpDate(jgiNo, insNo, PlanType.MR, prodCategory);
			} else {
				lastUpdateSpecialInsPlan = specialInsPlanDao.searchUpDate(jgiNo, insNo, null, prodCategory);
			}
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}

		// ----------------------
		// 品目一覧取得
		// ----------------------
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, prodCategory,  null);
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}

		// ------------------------------------
		// 施設情報取得（A調が紐付く場合A調も取得）
		// ------------------------------------
		List<DpsInsMst> insMstList = null;
		try {
			insMstList = insMstDAO.searchIncludeA(InsMstDAO.SORT_STRING, jgiNo, insNo);
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
		Map<String, List<String>> exceptDistProdMap = new HashMap<String, List<String>>();
		for (DpsInsMst insMst2 : insMstList) {
			try {
				ExceptDistIns distIns = exceptDistInsDao.searchByInsNo(insMst2.getInsNo());

				// 配分除外施設品目の場合
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
						exceptDistProdMap.put(distIns.getInsNo(), prodList);
					}
				}

				// 配分除外施設の場合
				else {
					exceptDistInsList.add(distIns.getInsNo());
				}
			} catch (DataNotFoundException e) {
			}
		}

		// ------------------------------------
		// 特定施設個別計画・担当者別計画取得
		// ------------------------------------
		Map<String, Long> mrPlanValueMap = new HashMap<String, Long>();
		List<SpecialInsPlan> specialInsPlanList = new ArrayList<SpecialInsPlan>();
		for (PlannedProd plannedProd : plannedProdList) {
			Long plannedValue = null;
			String prodCode = plannedProd.getProdCode();
			// 担当者立案画面の場合
			if (mrFlg) {
				// 特定施設個別計画の取得
				try {
					List<SpecialInsPlan> specialInsPlanListTmp = specialInsPlanDao.searchByInsNo(SpecialInsPlanDao.SORT_STRING2, jgiNo, insNo, prodCategory, prodCode, null);
					specialInsPlanList.addAll(specialInsPlanListTmp);
				} catch (DataNotFoundException e) {
					// エラーにはしない
				}
				// 担当者別計画の取得
				DpsHoInsType insType = insMst.getHoInsType();
				try {
					MrPlan mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
					// 対象がPの場合
					if (DpsHoInsType.P.equals(insType)) {
						// 担当者別計画モデルから計画値(PY)を取得する
						plannedValue = mrPlan.getPlannedValuePY();
					}
					// 対象がUHの場合
					else if (DpsHoInsType.U.equals(insType) || DpsHoInsType.H.equals(insType)) {
						// 担当者別計画モデルから計画値(UhY)を取得する
						plannedValue = mrPlan.getPlannedValueUhY();
					} else {
						final String errMsg = "対象が不正";
						throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
					}
				} catch (DataNotFoundException e) {
					// エラーにはしない
					plannedValue = null;
				}
				// 千円単位に変換し、担当者別計画をMapに追加
				mrPlanValueMap.put(prodCode, ConvertUtil.parseMoneyToThousandUnit(plannedValue));
			}
			// 営業所立案画面の場合
			else {
				// 特定施設個別計画の取得
				try {
					List<SpecialInsPlan> specialInsPlanListTmp = specialInsPlanDao.searchByInsNo(SpecialInsPlanDao.SORT_STRING2, jgiNo, insNo, prodCategory, prodCode,
						PlanType.OFFICE);
					specialInsPlanList.addAll(specialInsPlanListTmp);
				} catch (DataNotFoundException e) {
					// エラーにはしない
				}
			}

		}
		// ----------------------
		// 組織情報取得
		// ----------------------
		JgiMst jgiMst = null;
		String sosCd3 = null;
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
			sosCd3 = jgiMst.getSosCd3();
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}

		// ----------------------
		// 納入実績取得
		// ----------------------
		List<DeliveryResult> deliveryResultList = new ArrayList<DeliveryResult>();
		try {
			deliveryResultList = deliveryResultDao.searchByInsIncludeA(DeliveryResultDao.SORT_STRING3, insNo, prodCategory);
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// オブジェクト作成
		// ----------------------
		CreateSpecialInsPlanProdListResultDtoLogic dtoLogic = new CreateSpecialInsPlanProdListResultDtoLogic(plannedProdList, insMstList, specialInsPlanList, deliveryResultList,
			mrFlg, exceptDistInsList, exceptDistProdMap, prodCategory, prodInsInfoKanriDao, dpsCodeMasterSearchService);
		List<SpecialInsPlanProdListResultDto> resultList = dtoLogic.convertSpecialInsPlanProdListResultDto();

		String jgiName = null;
		Date upDate = null;
		if (lastUpdateSpecialInsPlan != null) {
			jgiName = lastUpdateSpecialInsPlan.getUpJgiName();
			upDate = lastUpdateSpecialInsPlan.getUpDate();
		}
		return new SpecialInsPlanSearchOfficeDto(jgiNo, insMst, resultList, jgiName, upDate, sosCd3, mrPlanValueMap);
	}

	public List<SpecialInsPlanResultDto> searchSpecialInsPlan(SpecialInsPlanScDto scDto, PlanType planType) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSearchInsType() == null) {
			final String errMsg = "対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSpecialInsPlanRegType() == null) {
			final String errMsg = "絞込条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}


		// ----------------------
		// 検索処理実行
		// ----------------------
		List<SpecialInsPlan> searchResultList = null;
		try {
			searchResultList = specialInsPlanDao.searchList(SpecialInsPlanDao.SORT_STRING, scDto, planType);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")));
		}

		return convertSpecialInsPlanResultDto(searchResultList);
	}

	/**
	 * SpecialInsPlanオブジェクトリスト⇒SpecialInsPlanResultDtoオブジェクトリスト 変換処理
	 *
	 * @param specialInsPlanList 変換元SpecialInsPlanオブジェクトのリスト
	 * @return SpecialInsPlanResultDtoオブジェクトリスト
	 */
	private List<SpecialInsPlanResultDto> convertSpecialInsPlanResultDto(List<SpecialInsPlan> specialInsPlanList) {

		List<SpecialInsPlanResultDto> specialInsPlanResultDtoList = new ArrayList<SpecialInsPlanResultDto>();

		// 変換元がNullの場合は配列0のリストを返却
		if (specialInsPlanList == null || specialInsPlanList.isEmpty()) {
			return specialInsPlanResultDtoList;
		}

		for (SpecialInsPlan specialInsPlan : specialInsPlanList) {
			// 施設名
			final String insName = specialInsPlan.getInsAbbrName();

			// 対象
			final String insType = specialInsPlan.getHoInsType().name();

			// 施設分類
			InsClass insClass = specialInsPlan.getInsClass();
			OldInsrFlg oldInsFlg = specialInsPlan.getOldInsrFlg();
			final String insClazz = InsClass.getInsClassName(insClass, oldInsFlg);

			// 担当者
			final String jgiName = specialInsPlan.getJgiName();

			// 特定施設個別計画立案済
			Boolean isRegOn = Boolean.FALSE;

			// 最終更新者
			final String upJgiName = specialInsPlan.getUpJgiName();

			// 最終更新日
			final Date upDate = specialInsPlan.getUpDate();
			if (upDate != null) {
				isRegOn = Boolean.TRUE;
			}

			// 施設コード
			final String insNo = specialInsPlan.getInsNo();

			// 従業員番号
			final Integer jgiNo = specialInsPlan.getJgiNo();

			// 削除フラグ
			final Boolean delFlg = new DelInsLogic(specialInsPlan.getReqFlg(), specialInsPlan.getDelFlg()).getDelFlg();

			final Boolean exceptFlg = specialInsPlan.getExceptFlg();

			final Boolean estimationFlg = specialInsPlan.getEstimationFlg();
			// オブジェクト生成
			SpecialInsPlanResultDto resultDto = new SpecialInsPlanResultDto(insName, insType, insClazz, jgiName, isRegOn, upJgiName, upDate, insNo, jgiNo, delFlg
					,estimationFlg,exceptFlg);

			specialInsPlanResultDtoList.add(resultDto);
		}
		return specialInsPlanResultDtoList;
	}
}
