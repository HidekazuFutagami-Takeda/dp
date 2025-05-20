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
import jp.co.takeda.dao.ManageInsPlanDao;
import jp.co.takeda.dto.InsPlanUpdateDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.ImplPlan;
import jp.co.takeda.model.ManageInsPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 管理の施設別計画の更新に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmInsPlanService")
public class DpmInsPlanServiceImpl implements DpmInsPlanService {

	/**
	 * 施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsPlanDao")
	protected ManageInsPlanDao manageInsPlanDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplPlanService")
	protected DpmCreateImplPlanService dpmCreateImplPlanService;

	// 施設別計画登録・更新
	public ManagePlanUpdateResultDto updateInsPlan(String pgId, List<InsPlanUpdateDto> updateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDtoList == null) {
			final String errMsg = "施設別計画の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int updateCountUh = 0;
		int updateCountP = 0;
		int updateCountZ = 0;
		int updateCountUhp = 0;

		for (InsPlanUpdateDto updateDto : updateDtoList) {

			// 引数チェック
			if (updateDto.getProdCode() == null) {
				final String errMsg = "更新対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (updateDto.getInsNo() == null) {
				final String errMsg = "更新対象の施設コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			final String prodCode = updateDto.getProdCode();
			final String insNo = updateDto.getInsNo();
			final InsType insType = updateDto.getInsType();
			final Long inputValue = ConvertUtil.parseMoneyToNormalUnit(updateDto.getYBaseValueAfter());

			// 現在の計画値を取得
			ImplPlan orgImplPlan;
			try {
				ManageInsPlan insPlan = manageInsPlanDao.searchUk(prodCode, insNo);
				orgImplPlan = insPlan.getImplPlan();
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
			ManageInsPlan insPlan = new ManageInsPlan();
			insPlan.setSeqKey(updateDto.getSeqKey());
			insPlan.setInsNo(updateDto.getInsNo());
			insPlan.setProdCode(prodCode);
			insPlan.setImplPlan(newImplPlan);
			insPlan.setDelFlg(isDelete(updateDto));
			insPlan.setUpDate(updateDto.getUpDate());

			// 登録/更新
			try {
				if (insPlan.getSeqKey() == null) {
					manageInsPlanDao.insert(insPlan, pgId);
				} else {
					manageInsPlanDao.update(insPlan, pgId);
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
		return new ManagePlanUpdateResultDto(updateCountUh, updateCountP, updateCountZ,updateCountUhp);
	}

	/**
	 * 入力された計画値から、LIVE/DELETEを判定する。<br>
	 * 入力された計画値がnullの場合は、DELETE(true)。それ以外の場合はLIVE(false)とする。
	 *
	 * @param updateDto 更新対象のDTO
	 * @return true：LIVE、false：DELETE
	 */
	private Boolean isDelete(InsPlanUpdateDto updateDto) {
		if (updateDto.getYBaseValueAfter() == null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
