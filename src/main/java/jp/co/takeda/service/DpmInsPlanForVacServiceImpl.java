package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.DB_DUPLICATE_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.logic.div.ValueChangeType.TO_T;

import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageInsPlanForVacDao;
import jp.co.takeda.dto.InsPlanForVacUpdateDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.model.ImplPlanForVac;
import jp.co.takeda.model.ManageInsPlanForVac;
import jp.co.takeda.util.ConvertUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理のワクチン用施設別計画の更新に関するサービス実装クラス
 * 
 * @author nozaki
 */
@Transactional
@Service("dpmInsPlanForVacService")
public class DpmInsPlanForVacServiceImpl implements DpmInsPlanForVacService {

	/**
	 * 施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsPlanForVacDao")
	protected ManageInsPlanForVacDao manageInsPlanForVacDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplPlanService")
	protected DpmCreateImplPlanService dpmCreateImplPlanService;

	// 施設別計画登録・更新
	public ManagePlanForVacUpdateResultDto updateInsPlan(String pgId, List<InsPlanForVacUpdateDto> updateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDtoList == null) {
			final String errMsg = "施設別計画の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int updateCount = 0;
		for (InsPlanForVacUpdateDto updateDto : updateDtoList) {

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
			final Long inputValue = ConvertUtil.parseMoneyToNormalUnit(updateDto.getBBaseValueAfter());

			// 現在の計画値を取得
			ImplPlanForVac orgImplPlan;
			ManageInsPlanForVac oldInsPlanForVac = null;
			try {
				oldInsPlanForVac = manageInsPlanForVacDao.searchUk(prodCode, insNo);
				orgImplPlan = oldInsPlanForVac.getImplPlanForVac();
			} catch (DataNotFoundException e) {
				orgImplPlan = new ImplPlanForVac();
			}

			// 入力された計画値をもとに、更新対象の実施計画を作成
			ImplPlanForVac newImplPlan;
			try {
				newImplPlan = dpmCreateImplPlanService.getImplPlanForVac(prodCode, inputValue, TO_T, null, orgImplPlan);
			} catch (LogicalException e) {
				final String errMsg = "T/Y変換処理で失敗。更新対象: " + updateDto;
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			// 更新対象情報を作成
			ManageInsPlanForVac insPlan = new ManageInsPlanForVac();
			insPlan.setSeqKey(updateDto.getSeqKey());
			insPlan.setInsNo(updateDto.getInsNo());
			insPlan.setProdCode(prodCode);
			insPlan.setImplPlanForVac(newImplPlan);
			insPlan.setDelFlg(isDelete(updateDto));
			insPlan.setUpDate(updateDto.getUpDate());

			// 登録/更新
			try {
				if (insPlan.getSeqKey() == null) {
					if (oldInsPlanForVac == null) {
						manageInsPlanForVacDao.insert(insPlan, pgId);
					} else {
						// 元々削除の場合、更新する。
						if (oldInsPlanForVac.getDelFlg()) {
							insPlan.setSeqKey(oldInsPlanForVac.getSeqKey());
							insPlan.setUpDate(oldInsPlanForVac.getUpDate());
							manageInsPlanForVacDao.update(insPlan, pgId);
						} else {
							// 元々削除ではない場合、排他エラー
							final String errMsg = "論理削除復旧済みのため、楽観的ロックエラーとする";
							throw new OptimisticLockingFailureException(errMsg);
						}
					}
				} else {
					manageInsPlanForVacDao.update(insPlan, pgId);
				}
			} catch (DuplicateException e) {
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "施設別計画登録に失敗"), e);
			}

			// 更新件数カウント
			updateCount = updateCount + 1;
		}
		return new ManagePlanForVacUpdateResultDto(updateCount);
	}

	/**
	 * 入力された計画値から、LIVE/DELETEを判定する。<br>
	 * 入力された計画値がnullの場合は、DELETE(true)。それ以外の場合はLIVE(false)とする。
	 * 
	 * @param updateDto 更新対象のDTO
	 * @return true：LIVE、false：DELETE
	 */
	private Boolean isDelete(InsPlanForVacUpdateDto updateDto) {
		if (updateDto.getBBaseValueAfter() == null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
