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
import jp.co.takeda.dao.ManageWsPlanDao;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.ManageWsPlanEntryDto;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.model.ImplPlan;
import jp.co.takeda.model.ManageWsPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 管理の特約店別計画の更新に関するサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmTmsTytenPlanService")
public class DpmTmsTytenPlanServiceImpl implements DpmTmsTytenPlanService {

	/**
	 * 管理の特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageWsPlanDao")
	protected ManageWsPlanDao manageWsPlanDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplPlanService")
	protected DpmCreateImplPlanService dpmCreateImplPlanService;

	// 特約店別計画登録・更新
	public ManagePlanUpdateResultDto updateTmsTytenPlan(String pgId, List<ManageWsPlanEntryDto> updateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDtoList == null) {
			final String errMsg = "特約店別計画の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int updateCountUh = 0;
		int updateCountP = 0;
		int updateCountZ = 0;
		int updateCountUhp = 0;

		for (ManageWsPlanEntryDto updateDto : updateDtoList) {

			// 引数チェック
			final String oriTmsTytenCd = updateDto.getTmsTytenCd();
			if (oriTmsTytenCd == null) {
				final String errMsg = "更新対象の特約店コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			// 13桁に変換
			final String tmsTytenCd = new CreateTmsTytenCdLogic(oriTmsTytenCd).execute();
			final String prodCode = updateDto.getProdCode();
			if (prodCode == null) {
				final String errMsg = "更新対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			final InsType insType = updateDto.getInsType();
			if (insType == null) {
				final String errMsg = "更新対象の出力対象区分がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 更新値
			final Long inputValue = ConvertUtil.parseMoneyToNormalUnit(updateDto.getBaseTAfter());

			// 現在の計画値を取得
			ImplPlan orgImplPlan;
			try {
				ManageWsPlan manageWsPlan = manageWsPlanDao.searchUk(insType, prodCode, tmsTytenCd);
				orgImplPlan = manageWsPlan.getImplPlan();
			} catch (DataNotFoundException e) {
				orgImplPlan = new ImplPlan();
			}

			// 入力された計画値をもとに、更新対象の実施計画を作成
			ImplPlan newImplPlan;
			try {
				newImplPlan = dpmCreateImplPlanService.getImplPlan(prodCode, inputValue, insType, FROM_T, tmsTytenCd, orgImplPlan);
			} catch (LogicalException e) {
				final String errMsg = "T/Y変換処理で失敗。更新対象: " + updateDto;
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			// 更新対象情報を作成
			ManageWsPlan wsPlan = new ManageWsPlan();
			wsPlan.setSeqKey(updateDto.getSeqKey());
			wsPlan.setInsType(insType);
			wsPlan.setProdCode(prodCode);
			wsPlan.setTmsTytenCd(tmsTytenCd);
			wsPlan.setImplPlan(newImplPlan);
			wsPlan.setDelFlg(isAlive(updateDto));
			wsPlan.setUpDate(updateDto.getUpDate());

			// 登録/更新
			try {
				if (wsPlan.getSeqKey() == null) {
					manageWsPlanDao.insert(wsPlan, pgId);
				} else {
					manageWsPlanDao.update(wsPlan, pgId);
				}
			} catch (DuplicateException e) {
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "特約店別計画登録に失敗"), e);
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
	 * @return true：DELETE、false：LIVE
	 */
	private Boolean isAlive(ManageWsPlanEntryDto updateDto) {
		if (updateDto.getBaseTAfter() == null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
