package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.DB_DUPLICATE_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DistParamHonbuForVacDao;
import jp.co.takeda.dao.DistributionElementDao;
import jp.co.takeda.dao.InsWsPlanForVacDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SpecialInsPlanForVacDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DistributionLogicDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsWsPlanDto;
import jp.co.takeda.logic.DistributionByIncrementLogic;
import jp.co.takeda.logic.div.InsWsDistType;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DistParam;
import jp.co.takeda.model.DistParamHonbuForVac;
import jp.co.takeda.model.InsWsPlanForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlanForVac;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.view.DistributionElement;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ワクチン)施設特約店別計画・配分実行サービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionForVacExecuteService")
public class DpsDistributionForVacExecuteServiceImpl implements DpsDistributionForVacExecuteService {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(DpsDistributionForVacExecuteServiceImpl.class);

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * (ワクチン)配分パラメータ（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamHonbuForVacDao")
	protected DistParamHonbuForVacDao distParamHonbuForVacDao;

	/**
	 * (ワクチン)担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanForVacDao")
	protected MrPlanForVacDao mrPlanForVacDao;

	/**
	 * (ワクチン)特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanForVacDao")
	protected SpecialInsPlanForVacDao specialInsPlanForVacDao;

	/**
	 * 配分要素取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("distributionElementDao")
	protected DistributionElementDao distributionElementDao;

	/**
	 * (ワクチン)施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForVacDao")
	protected InsWsPlanForVacDao insWsPlanForVacDao;

	// 一括配分
	public List<DistributionMissDto> execute(String prodCode, DpUser execDpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分ミス情報
		List<DistributionMissDto> distMissList = new ArrayList<DistributionMissDto>();

		// 処理を実行
		List<DistributionMissDto> tmpDistMissList = executePre(null, prodCode, execDpUser);
		if (tmpDistMissList.size() != 0) {
			distMissList.addAll(tmpDistMissList);
		}

		return distMissList;
	}

	// 再配分処理
	public List<DistributionMissDto> executeAgain(Integer jgiNo, String prodCode, DpUser execDpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分ミス情報
		List<DistributionMissDto> distMissList = new ArrayList<DistributionMissDto>();

		// 対象毎に処理を実行
		List<DistributionMissDto> tmpDistMissList = executePre(jgiNo, prodCode, execDpUser);
		if (tmpDistMissList.size() != 0) {
			distMissList.addAll(tmpDistMissList);
		}

		return distMissList;
	}

	/**
	 * 配分処理の実行前処理を行い、配分処理を実行する。
	 * 
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param execDpUser 実行者情報
	 * @return 配分ミス情報
	 */
	protected List<DistributionMissDto> executePre(Integer jgiNo, String prodCode, DpUser execDpUser) {

		// 配分ミス情報
		List<DistributionMissDto> distMissList = new ArrayList<DistributionMissDto>();

		// ----------------------
		// 配分基準取得
		// ----------------------
		DistParam distParam = null;
		BaseParam baseParam = null;
		try {
			DistParamHonbuForVac distParamHonbu = distParamHonbuForVacDao.search(prodCode);
			distParam = distParamHonbu.getDistParam();
			baseParam = distParamHonbu.getBaseParam();
		} catch (DataNotFoundException e2) {
			final String errMsg = "ワクチン用配分基準(本部)が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e2);
		}

		DistributionMissDto dto = null;
		if (jgiNo != null) {
			// ------------------------------
			// 担当者別計画からの再配分
			// ------------------------------

			// ワクチン施設特約店別計画削除
			insWsPlanForVacDao.deleteByJgi(jgiNo, prodCode);

			dto = execute(jgiNo, prodCode, execDpUser, distParam, baseParam);
			if (dto != null)
				distMissList.add(dto);
		} else {
			// ------------------------------
			// 担当者別計画からの一括配分
			// ------------------------------

			// ワクチン施設特約店別計画削除
			insWsPlanForVacDao.deleteByJgi(null, prodCode);

			// 担当者別計画からの一括配分の場合、担当者ごとに配分実行する。
			List<JgiMst> jgiList = new ArrayList<JgiMst>();
			try {
				jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, SosMst.SOS_CD1, SosListType.TOKUYAKUTEN_LIST, BumonRank.IEIHON);
			} catch (DataNotFoundException e) {
				final String errMsg = "組織・従業員情報取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
			for (JgiMst jgiMst : jgiList) {
				dto = execute(jgiMst.getJgiNo(), prodCode, execDpUser, distParam, baseParam);
				if (dto != null)
					distMissList.add(dto);
			}
		}
		return distMissList;
	}

	/**
	 * 配分処理を実行する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param execDpUser 実行者情報
	 * @param distParam 配分パラメータ
	 * @param baseParam 試算配分共通パラメータ
	 * @return 配分ミス情報
	 */
	protected DistributionMissDto execute(Integer jgiNo, String prodCode, DpUser execDpUser, DistParam distParam, BaseParam baseParam) {

		// ------------------------------
		// 配分要素の取得
		// ------------------------------
		List<DistributionElement> elementList = new ArrayList<DistributionElement>();
		try {
			elementList = distributionElementDao.searchListForVac(jgiNo, prodCode, baseParam.getRefProdCode(), baseParam.getRefFrom(), baseParam.getRefTo());
		} catch (DataNotFoundException e) {
			// 配分要素が無い場合もエラーにしない
		}

		// ----------------------------------------
		// 配分母数の取得
		// ----------------------------------------
		Long paramValue = getParamValue(jgiNo, prodCode);

		// 特定施設個別計画の取得
		List<SpecialInsPlanForVac> specialInsPlanList = getSpecialInsPlanForVacList(jgiNo, prodCode);
		// 特定施設個別計画のサマリー算出
		Long specialSum = 0L;
		for (SpecialInsPlanForVac specialInsPlan : specialInsPlanList) {
			specialSum = MathUtil.add(specialSum, specialInsPlan.getPlannedValueB());
		}

		// ------------------------------
		// 対象品目の実績の取得
		// ------------------------------
		List<DistributionElement> prodElementList = new ArrayList<DistributionElement>();
		try {
			prodElementList = distributionElementDao.searchListForVac(jgiNo, prodCode, prodCode, baseParam.getRefFrom(), baseParam.getRefTo());
		} catch (DataNotFoundException e) {
			// 配分要素が無い場合もエラーにしない
		}

		// ----------------------------------------
		// 配分実行
		// ----------------------------------------
		//		DistributionLogic distLogic = new DistributionLogic(elementList, baseParam, distParam, null, jgiNo, prodCode, null,
		//			paramValue, specialSum, InsWsDistType.MR_PLAN_DIST_FOR_VAC, prodElementList);
		//		DistributionLogicDto distributionLogicDto = distLogic.execute();

		// 担当者別計画からの増分値配分
		DistributionByIncrementLogic distLogic = new DistributionByIncrementLogic(elementList, baseParam, distParam, null, jgiNo, prodCode, null, paramValue, specialSum,
			InsWsDistType.MR_PLAN_DIST_FOR_VAC, prodElementList);
		DistributionLogicDto distributionLogicDto = distLogic.execute();

		// 施設特約店別計画
		List<InsWsPlanDto> insWsPlanList = distributionLogicDto.getInsWsPlanDtoList();

		// ------------------------------
		// ワクチン施設特約店別計画登録
		// ------------------------------
		for (InsWsPlanDto dto : insWsPlanList) {
			InsWsPlanForVac insWsPlan = new InsWsPlanForVac();
			insWsPlan.setProdCode(prodCode);
			insWsPlan.setSpecialInsPlanFlg(false);
			insWsPlan.setExpectDistInsFlg(dto.getExceptDistInsFlg());
			insWsPlan.setDelInsFlg(dto.getDelInsFlg());
			insWsPlan.setJgiNo(dto.getJgiNo());
			insWsPlan.setInsNo(dto.getInsNo());
			insWsPlan.setTmsTytenCd(dto.getTmsTytenCd());
			insWsPlan.setDistValueB(dto.getDistValue());
			insertInsWsPlan(insWsPlan);
		}

		// --------------------------------------
		// ワクチン施設特約店別計画登録(特定施設)
		// --------------------------------------
		for (SpecialInsPlanForVac specialInsPlan : specialInsPlanList) {
			InsWsPlanForVac insWsPlan = new InsWsPlanForVac();
			insWsPlan.setProdCode(prodCode);
			insWsPlan.setSpecialInsPlanFlg(true);
			insWsPlan.setExpectDistInsFlg(false);
			insWsPlan.setDelInsFlg(false);
			insWsPlan.setJgiNo(specialInsPlan.getJgiNo());
			insWsPlan.setInsNo(specialInsPlan.getInsNo());
			insWsPlan.setTmsTytenCd(specialInsPlan.getTmsTytenCd());
			insWsPlan.setDistValueB(specialInsPlan.getPlannedValueB());
			insertInsWsPlan(insWsPlan);
		}

		return distributionLogicDto.getDistributionMissDto();
	}

	/**
	 * ワクチン施設特約店別計画を登録する。
	 * 
	 * 
	 * @param insWsPlan ワクチン施設特約店別計画
	 */
	protected void insertInsWsPlan(InsWsPlanForVac insWsPlan) {
		// 配分値が0の場合、計画値にはNULLを設定
		Long planValue = null;
		if (insWsPlan.getDistValueB() != null && insWsPlan.getDistValueB() > 0) {
			planValue = insWsPlan.getDistValueB();
		}
		insWsPlan.setPlannedValueB(planValue);
		try {
			insWsPlanForVacDao.insert(insWsPlan);
		} catch (DuplicateException e) {
			LOG.error("jgiNo:" + insWsPlan.getProdCode() + " prodCode:" + insWsPlan.getProdCode());
			final String errMsg = "ワクチン施設特約店別計画登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	/**
	 * 配分母数を取得する。
	 * 
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 配分母数
	 */
	protected Long getParamValue(Integer jgiNo, String prodCode) {
		Long paramValue = null;
		// 担当者別計画取得
		try {
			MrPlanForVac mrPlan = mrPlanForVacDao.searchUk(jgiNo, prodCode);
			paramValue = mrPlan.getPlannedValueB();
		} catch (DataNotFoundException e) {
		}
		if (paramValue == null) {
			paramValue = 0L;
		}

		return paramValue;
	}

	/**
	 * ワクチン特定施設個別計画のリストを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return ワクチン特定施設個別計画のリスト
	 */
	protected List<SpecialInsPlanForVac> getSpecialInsPlanForVacList(Integer jgiNo, String prodCode) {
		List<SpecialInsPlanForVac> specialInsPlanList = new ArrayList<SpecialInsPlanForVac>();
		try {
			specialInsPlanList = specialInsPlanForVacDao.searchByJgiNo(null, jgiNo, prodCode, PlanType.MR);
		} catch (DataNotFoundException e) {
			// 特定施設個別計画が無くても、エラーにしない
		}
		return specialInsPlanList;
	}
}
