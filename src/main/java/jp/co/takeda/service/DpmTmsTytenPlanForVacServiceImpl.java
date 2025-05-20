package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.DB_DUPLICATE_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.logic.div.ValueChangeType.FROM_T;

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
import jp.co.takeda.dao.ManageWsPlanForVacDao;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.dto.ManageWsPlanForVacEntryDto;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.model.ImplPlanForVac;
import jp.co.takeda.model.ManageWsPlanForVac;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 管理の特約店別計画の更新に関するサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmTmsTytenPlanForVacService")
public class DpmTmsTytenPlanForVacServiceImpl implements DpmTmsTytenPlanForVacService {

	/**
	 * 管理の(ワ)特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageWsPlanForVacDao")
	protected ManageWsPlanForVacDao manageWsPlanForVacDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplPlanService")
	protected DpmCreateImplPlanService dpmCreateImplPlanService;

	// 特約店別計画登録・更新
	public ManagePlanForVacUpdateResultDto updateTmsTytenPlan(String pgId, List<ManageWsPlanForVacEntryDto> updateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDtoList == null) {
			final String errMsg = "特約店別計画の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int updateCount = 0;
		for (ManageWsPlanForVacEntryDto updateDto : updateDtoList) {

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

			// 更新値
			final Long inputValue = ConvertUtil.parseMoneyToNormalUnit(updateDto.getBaseTAfter());

			// 現在の計画値を取得
			ImplPlanForVac orgImplPlan;
			try {
				ManageWsPlanForVac manageWsPlan = manageWsPlanForVacDao.searchUk(prodCode, tmsTytenCd);
				orgImplPlan = manageWsPlan.getImplPlanForVac();
			} catch (DataNotFoundException e) {
				orgImplPlan = new ImplPlanForVac();
			}

			// 入力された計画値をもとに、更新対象の実施計画を作成
			ImplPlanForVac newImplPlan;
			try {
				newImplPlan = dpmCreateImplPlanService.getImplPlanForVac(prodCode, inputValue, FROM_T, tmsTytenCd, orgImplPlan);
			} catch (LogicalException e) {
				final String errMsg = "T/B変換処理で失敗。更新対象: " + updateDto;
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			// 更新対象情報を作成
			ManageWsPlanForVac wsPlan = new ManageWsPlanForVac();
			wsPlan.setSeqKey(updateDto.getSeqKey());
			wsPlan.setProdCode(prodCode);
			wsPlan.setTmsTytenCd(tmsTytenCd);
			wsPlan.setImplPlanForVac(newImplPlan);
			wsPlan.setDelFlg(isAlive(updateDto));
			wsPlan.setUpDate(updateDto.getUpDate());

			// 登録/更新
			try {
				if (wsPlan.getSeqKey() == null) {
				    wsPlan.setInsType(InsType.UH);
					manageWsPlanForVacDao.insert(wsPlan, pgId);
				} else {
					manageWsPlanForVacDao.update(wsPlan, pgId);
				}
			} catch (DuplicateException e) {
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "特約店別計画登録に失敗"), e);
			}

			// 更新件数カウント
			updateCount = updateCount + 1;
		}
		return new ManagePlanForVacUpdateResultDto(updateCount);
	}

	/**
	 * 入力された計画値から、LIVE/DELETEを判定する。<br>
	 * 入力された計画値がnullの場合は、LIVE(false)。それ以外の場合はDELETE(true)とする。
	 *
	 * @param updateDto 更新対象のDTO
	 * @return true：DELETE、false：LIVE
	 */
	private Boolean isAlive(ManageWsPlanForVacEntryDto updateDto) {
		if (updateDto.getBaseTAfter() == null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
