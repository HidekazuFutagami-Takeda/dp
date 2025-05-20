package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SpecialInsPlanForVacDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.MrPlanForVacUpdateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlanForVac;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ワクチン)担当者別計画機能の更新に関するサービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpsMrPlanForVacService")
public class DpsMrPlanForVacServiceImpl implements DpsMrPlanForVacService {

	/**
	 * 従業員情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画対象品目取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * ワクチン用担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanForVacDao")
	protected MrPlanForVacDao mrPlanForVacDao;

	/**
	 * ワクチン用特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanForVacDao")
	protected SpecialInsPlanForVacDao specialInsPlanForVacDao;

	/**
	 * ワクチン用施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckForVacService")
	protected DpsInsWsStatusCheckForVacService dpsInsWsStatusCheckService;

	// 担当者別計画更新
	public void updateMrPlan(List<MrPlanForVacUpdateDto> updateDtoList) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDtoList == null) {
			final String errMsg = "ワクチン用担当者別計画更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 逆鞘チェック
		// ----------------------
		List<String> jgiNameList = new ArrayList<String>();
		for (MrPlanForVacUpdateDto dto : updateDtoList) {
			// 担当者別計画取得
			MrPlanForVac mrPlanForVac = mrPlanForVacDao.search(dto.getSeqKey());
			Long plannedValue = ConvertUtil.parseMoneyToNormalUnit(dto.getPlannedValueB());

			// NULLの場合はゼロとして扱う
			if (plannedValue == null) {
				plannedValue = 0L;
			}

			// 特定施設個別別計画取得
			List<SpecialInsPlanForVac> spInsList = new ArrayList<SpecialInsPlanForVac>();
			try {
				spInsList = specialInsPlanForVacDao.searchByJgiNo(null, mrPlanForVac.getJgiNo(), mrPlanForVac.getProdCode(), PlanType.MR);
			} catch (DataNotFoundException e) {
			}

			// NULLの場合はゼロとして扱うため、ゼロ宣言
			Long spInsValue = 0L;
			for (SpecialInsPlanForVac specialInsPlanForVac : spInsList) {
				spInsValue = MathUtil.add(spInsValue, specialInsPlanForVac.getPlannedValueB());
			}
			if (spInsValue > plannedValue) {
				String jgiName = jgiMstDAO.search(mrPlanForVac.getJgiNo()).getJgiName();
				if (jgiName != null) {
					jgiNameList.add(jgiName);
				}
			}
		}
		if (jgiNameList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String jgiName : jgiNameList) {
				sb.append("【" + jgiName + "】");
			}
			MessageKey messageKey = new MessageKey("DPS3301E", sb.toString());
			final String errMsg = "担当者別計画と特定施設個別計画の逆ザヤチェックで検証ＮＧ";
			throw new LogicalException(new Conveyance(messageKey, errMsg));
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 小児科MR取得 (J19-0010 対応・コメントのみ修正)
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(null, null, SosListType.TOKUYAKUTEN_LIST, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "MR取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// 品目取得
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(null, Sales.VACCHIN, null, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "品目取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		try {
			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);

			// ステータスチェック実行
			dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3300E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 更新処理
		// ----------------------
		for (MrPlanForVacUpdateDto dto : updateDtoList) {
			MrPlanForVac record = new MrPlanForVac();
			record.setSeqKey(dto.getSeqKey());
			record.setPlannedValueB(ConvertUtil.parseMoneyToNormalUnit(dto.getPlannedValueB()));
			record.setUpDate(dto.getUpDate());
			mrPlanForVacDao.update(record);
		}
	}
}
