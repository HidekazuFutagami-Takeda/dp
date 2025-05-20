package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.logic.div.ValueChangeType.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageMrPlanDao;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.SosPlanUpdateDto;
import jp.co.takeda.model.ImplPlan;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 管理の組織別計画(担当者)の更新に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmMrPlanService")
public class DpmMrPlanServiceImpl implements DpmMrPlanService {

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageMrPlanDao")
	protected ManageMrPlanDao manageMrPlanDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplPlanService")
	protected DpmCreateImplPlanService dpmCreateImplPlanService;

	// 担当者別計画登録・更新
	public ManagePlanUpdateResultDto updateMrPlan(String pgId, List<SosPlanUpdateDto> updateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDtoList == null) {
			final String errMsg = "組織別計画の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int updateCountUh = 0;
		int updateCountP = 0;
		int updateCountZ = 0;
		int updateCountUhp = 0;

		for (SosPlanUpdateDto updateDto : updateDtoList) {

			// 引数チェック
			if (updateDto.getJgiNo() == null) {
				final String errMsg = "更新対象の従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (updateDto.getProdCode() == null) {
				final String errMsg = "更新対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (updateDto.getInsType() == null) {
				final String errMsg = "更新対象の出力対象区分がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			final Integer jgiNo = updateDto.getJgiNo();
			final String prodCode = updateDto.getProdCode();
			final InsType insType = updateDto.getInsType();
			final Long inputValue = ConvertUtil.parseMoneyToNormalUnit(updateDto.getYBaseValueAfter());

			// 現在の計画値を取得
			ImplPlan orgImplPlan;
			try {
				ManageMrPlan mrPlan = manageMrPlanDao.searchUk(insType, prodCode, jgiNo);
				orgImplPlan = mrPlan.getImplPlan();
			} catch (DataNotFoundException e) {
				orgImplPlan = new ImplPlan();
			}

			// 入力された計画値をもとに、更新対象の実施計画を作成
			ImplPlan newImplPlan;
			try {
				newImplPlan = dpmCreateImplPlanService.getImplPlan(prodCode, inputValue, insType, TO_T, null, orgImplPlan);
			} catch (LogicalException e) {
				final String errMsg = "T/Y変換処理で失敗。更新対象: " + updateDto;
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			// 更新対象情報を作成
			ManageMrPlan mrPlan = new ManageMrPlan();
			mrPlan.setSeqKey(updateDto.getSeqKey());
			mrPlan.setInsType(insType);
			mrPlan.setProdCode(prodCode);
			mrPlan.setJgiNo(jgiNo);
			mrPlan.setImplPlan(newImplPlan);
			mrPlan.setDelFlg(isDelete(updateDto));
			mrPlan.setUpDate(updateDto.getUpDate());

			// 登録/更新
			try {
				if (mrPlan.getSeqKey() == null) {
					manageMrPlanDao.insert(mrPlan, pgId);
				} else {
					manageMrPlanDao.update(mrPlan, pgId);
				}
			} catch (DuplicateException e) {
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "施設別計画登録に失敗"), e);
			}

			// 更新件数カウント
			if (insType == InsType.UH) {
				updateCountUh = updateCountUh + 1;
			}
			if (insType == InsType.P) {
				updateCountP = updateCountP + 1;
			}
			if (insType == InsType.ZATU) {
				updateCountZ = updateCountZ + 1;
			}
			if (insType == InsType.UHP) {
				updateCountUhp = updateCountUhp + 1;
			}
		}
		return new ManagePlanUpdateResultDto(updateCountUh, updateCountP, updateCountZ, updateCountUhp);
	}

	/**
	 * 入力された計画値から、LIVE/DELETEを判定する。<br>
	 * 入力された計画値がnullの場合は、DELETE(true)。それ以外の場合はLIVE(false)とする。
	 *
	 * @param updateDto 更新対象のDTO
	 * @return true：LIVE、false：DELETE
	 */
	private Boolean isDelete(SosPlanUpdateDto updateDto) {
		if (updateDto.getYBaseValueAfter() == null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
