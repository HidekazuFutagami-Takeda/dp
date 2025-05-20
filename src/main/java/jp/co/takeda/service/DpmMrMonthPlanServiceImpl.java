package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageMrMonthPlanDao;
import jp.co.takeda.dto.SosMonthPlanUpdateDto;
import jp.co.takeda.model.ImplMonthPlan;
import jp.co.takeda.model.ManageMrMonthPlan;
import jp.co.takeda.util.ConvertUtil;

/**
 * 管理の組織別計画(担当者)の更新に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmMrMonthPlanService")
public class DpmMrMonthPlanServiceImpl implements DpmMrMonthPlanService {

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageMrMonthPlanDao")
	protected ManageMrMonthPlanDao manageMrMonthPlanDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplPlanService")
	protected DpmCreateImplPlanService dpmCreateImplPlanService;

	// 担当者別月別計画登録・更新
	public int updateMrPlan(String pgId, List<SosMonthPlanUpdateDto> updateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDtoList == null) {
			final String errMsg = "組織別月別計画の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int updateCount = 0;

		for (SosMonthPlanUpdateDto updateDto : updateDtoList) {

			// 引数チェック
			if (updateDto.getJgiNo() == null) {
				final String errMsg = "更新対象の従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (updateDto.getProdCode() == null) {
				final String errMsg = "更新対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			final Integer jgiNo = updateDto.getJgiNo();
			final String prodCode = updateDto.getProdCode();
			final Long inputPlanned1Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned1ValueYbAfter());
			final Long inputPlanned2Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned2ValueYbAfter());
			final Long inputPlanned3Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned3ValueYbAfter());
			final Long inputPlanned4Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned4ValueYbAfter());
			final Long inputPlanned5Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned5ValueYbAfter());
			final Long inputPlanned6Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned6ValueYbAfter());

			final Long inputExpected1Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected1ValueYbAfter());
			final Long inputExpected2Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected2ValueYbAfter());
			final Long inputExpected3Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected3ValueYbAfter());
			final Long inputExpected4Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected4ValueYbAfter());
			final Long inputExpected5Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected5ValueYbAfter());
			final Long inputExpected6Value = ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected6ValueYbAfter());

			// 入力された計画値をもとに、更新対象の実施計画を作成
			ImplMonthPlan newImplPlan = new ImplMonthPlan();
			newImplPlan.setPlanned1ValueYb(inputPlanned1Value);
			newImplPlan.setPlanned2ValueYb(inputPlanned2Value);
			newImplPlan.setPlanned3ValueYb(inputPlanned3Value);
			newImplPlan.setPlanned4ValueYb(inputPlanned4Value);
			newImplPlan.setPlanned5ValueYb(inputPlanned5Value);
			newImplPlan.setPlanned6ValueYb(inputPlanned6Value);
			newImplPlan.setExpected1ValueYb(inputExpected1Value);
			newImplPlan.setExpected2ValueYb(inputExpected2Value);
			newImplPlan.setExpected3ValueYb(inputExpected3Value);
			newImplPlan.setExpected4ValueYb(inputExpected4Value);
			newImplPlan.setExpected5ValueYb(inputExpected5Value);
			newImplPlan.setExpected6ValueYb(inputExpected6Value);

			// 更新対象情報を作成
			ManageMrMonthPlan mrPlan = new ManageMrMonthPlan();
			mrPlan.setSeqKey(updateDto.getSeqKey());
			mrPlan.setProdCode(prodCode);
			mrPlan.setJgiNo(jgiNo);
			mrPlan.setImplMonthPlan(newImplPlan);
			mrPlan.setDelFlg(Boolean.FALSE);
			mrPlan.setUpDate(updateDto.getUpDate());

			// 登録/更新
			try {
				if (mrPlan.getSeqKey() == null) {
					manageMrMonthPlanDao.insert(mrPlan, pgId);
				} else {
					manageMrMonthPlanDao.update(mrPlan, pgId);
				}
			} catch (DuplicateException e) {
				throw new SystemException(new Conveyance(ErrMessageKey.DB_DUPLICATE_ERROR, "施設別計画登録に失敗"), e);
			}

			// 更新件数カウント
			updateCount = updateCount + 1;
		}
		return updateCount;
	}
}
