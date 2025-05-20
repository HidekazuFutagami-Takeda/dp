package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Scale;
import jp.co.takeda.dao.ManageChangeParamBTDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.logic.div.ValueChangeType;
import jp.co.takeda.model.ImplPlan;
import jp.co.takeda.model.ImplPlanForVac;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.MathUtil;

/**
 * 実施計画作成サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmCreateImplPlanService")
public class DpmCreateImplPlanServiceImpl implements DpmCreateImplPlanService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpmCreateImplPlanServiceImpl.class);

	/**
	 * 管理の変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamYTDao")
	protected ManageChangeParamYTDao manageChangeParamYTDao;

	/**
	 * 管理の変換パラメータ(B→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamBTDao")
	protected ManageChangeParamBTDao manageChangeParamBTDao;

	// 実施計画を作成
	public ImplPlan getImplPlan(String prodCode, Long value, InsType insType, ValueChangeType valueChangeType, String tmsTytenCd, ImplPlan implPlan) throws LogicalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("パラメータ確認");
			LOG.debug("prodCode:" + prodCode);
			LOG.debug("value:" + value);
			LOG.debug("insType:" + insType);
			LOG.debug("valueChangeType:" + valueChangeType);
			LOG.debug("tmsTytenCd:" + tmsTytenCd);
			LOG.debug("implPlan:" + implPlan);
		}

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (valueChangeType == null) {
			final String errMsg = "変換区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------
		// 計画値変換処理
		// --------------
		// 更新計画値Y、計画値T
		Long valueY = null;
		Long valueT = null;
		// 計画値がNULLの場合、変換せずに計画値をNULLで更新
		if (value != null) {
			// 変換前計画値
			Long oriValue = value;
			// 変換後計画値
			// 特約店コードを3桁で指定する。
			String tmsTytenCd3 = null;
			if (tmsTytenCd != null) {
				tmsTytenCd3 = tmsTytenCd.substring(0, 3);
			}
			Long chaValue = manageChangeParamYTDao.getChangeValue(prodCode, oriValue, insType, valueChangeType, tmsTytenCd3);
			chaValue = MathUtil.calcTheory(chaValue, Scale.THOUSAND);
			switch (valueChangeType) {
				case TO_T:
					valueY = oriValue;
					valueT = chaValue;
					break;
				case FROM_T:
					valueY = chaValue;
					valueT = oriValue;
					break;
			}
		}

		// --------------
		// 実施計画の編集
		// --------------
		// 更新前の実施計画
		ImplPlan oriImplPlan = null;
		if (implPlan == null) {
			oriImplPlan = new ImplPlan();
		} else {
			oriImplPlan = implPlan;
		}

		// 更新後の実施計画
		ImplPlan result = new ImplPlan();
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		// 実施計画1の設定
		switch (sysManage.getPlannedType()) {
			case PLANNED_1:
				result.setBefPlanned1ValueY(oriImplPlan.getPlanned1ValueY());
				result.setBefPlanned1ValueT(oriImplPlan.getPlanned1ValueT());
				result.setPlanned1ValueY(valueY);
				result.setPlanned1ValueT(valueT);
				break;
			case PLANNED_2:
				result.setBefPlanned1ValueY(oriImplPlan.getBefPlanned1ValueY());
				result.setBefPlanned1ValueT(oriImplPlan.getBefPlanned1ValueT());
				result.setPlanned1ValueY(oriImplPlan.getPlanned1ValueY());
				result.setPlanned1ValueT(oriImplPlan.getPlanned1ValueT());
				break;
		}

		// 実施計画2の設定
		result.setBefPlanned2ValueY(oriImplPlan.getPlanned2ValueY());
		result.setBefPlanned2ValueT(oriImplPlan.getPlanned2ValueT());
		result.setPlanned2ValueY(valueY);
		result.setPlanned2ValueT(valueT);

		return result;
	}

	// ワクチン実施計画を作成
	public ImplPlanForVac getImplPlanForVac(String prodCode, Long value, ValueChangeType valueChangeType, String tmsTytenCd, ImplPlanForVac implPlanForVac) throws LogicalException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("パラメータ確認");
			LOG.debug("prodCode:" + prodCode);
			LOG.debug("value:" + value);
			LOG.debug("valueChangeType:" + valueChangeType);
			LOG.debug("tmsTytenCd:" + tmsTytenCd);
			LOG.debug("implPlanForVac:" + implPlanForVac);
		}

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (valueChangeType == null) {
			final String errMsg = "変換区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------
		// 計画値変換処理
		// --------------
		// 更新計画値B、計画値T
		Long valueB = null;
		Long valueT = null;
		// 計画値がNULLの場合、変換せずに計画値をNULLで更新
		if (value != null) {
			// 変換前計画値
			Long oriValue = value;
			// 変換後計画値
			// 特約店コードを3桁で指定する。
			String tmsTytenCd3 = null;
			if (tmsTytenCd != null) {
				tmsTytenCd3 = tmsTytenCd.substring(0, 3);
			}
			Long chaValue = manageChangeParamBTDao.getChangeValue(prodCode, oriValue, valueChangeType, tmsTytenCd3);
			chaValue = MathUtil.calcTheory(chaValue, Scale.THOUSAND);
			switch (valueChangeType) {
				case TO_T:
					valueB = oriValue;
					valueT = chaValue;
					break;
				case FROM_T:
					valueB = chaValue;
					valueT = oriValue;
					break;
			}
		}

		// --------------
		// 実施計画の編集
		// --------------
		// 更新前の実施計画
		ImplPlanForVac oriImplPlanForVac = null;
		if (implPlanForVac == null) {
			oriImplPlanForVac = new ImplPlanForVac();
		} else {
			oriImplPlanForVac = implPlanForVac;
		}

		// 更新後の実施計画
		ImplPlanForVac result = new ImplPlanForVac();
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		// 実施計画1の設定
		switch (sysManage.getPlannedType()) {
			case PLANNED_1:
				result.setBefPlanned1ValueY(oriImplPlanForVac.getPlanned1ValueY());
				result.setBefPlanned1ValueT(oriImplPlanForVac.getPlanned1ValueT());
				result.setPlanned1ValueY(valueB);
				result.setPlanned1ValueT(valueT);
				break;
			case PLANNED_2:
				result.setBefPlanned1ValueY(oriImplPlanForVac.getBefPlanned1ValueY());
				result.setBefPlanned1ValueT(oriImplPlanForVac.getBefPlanned1ValueT());
				result.setPlanned1ValueY(oriImplPlanForVac.getPlanned1ValueY());
				result.setPlanned1ValueT(oriImplPlanForVac.getPlanned1ValueT());
				break;
		}

		// 実施計画2の設定
		result.setBefPlanned2ValueY(oriImplPlanForVac.getPlanned2ValueY());
		result.setBefPlanned2ValueT(oriImplPlanForVac.getPlanned2ValueT());
		result.setPlanned2ValueY(valueB);
		result.setPlanned2ValueT(valueT);
		return result;
	}

}
