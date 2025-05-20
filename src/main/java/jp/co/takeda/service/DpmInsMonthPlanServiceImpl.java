package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageInsMonthPlanDao;
import jp.co.takeda.dto.InsMonthPlanUpdateDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.ImplMonthPlan;
import jp.co.takeda.model.ManageInsMonthPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 管理の施設別計画の更新に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmInsMonthPlanService")
public class DpmInsMonthPlanServiceImpl implements DpmInsMonthPlanService {

	/**
	 * 施設別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsMonthPlanDao")
	protected ManageInsMonthPlanDao manageInsMonthPlanDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplMonthPlanService")
	protected DpmCreateImplMonthPlanService dpmCreateImplMonthPlanService;

	// 施設別計画登録・更新
	public ManagePlanUpdateResultDto updateInsMonthPlan(String pgId, List<InsMonthPlanUpdateDto> updateDtoList) {

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
		for (InsMonthPlanUpdateDto updateDto : updateDtoList) {

			// 引数チェック
			if (updateDto.getProdCode() == null) {
				final String errMsg = "更新対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (updateDto.getInsNo() == null) {
				final String errMsg = "更新対象の施設コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

            // 入力された計画値をもとに、更新対象の実施計画を作成
			ImplMonthPlan inputImplMonthPlan = new ImplMonthPlan();
			// 月初計画1（ＹB価）
			inputImplMonthPlan.setPlanned1ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned1Value()));
			// 月初計画2（ＹB価）
			inputImplMonthPlan.setPlanned2ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned2Value()));
			// 月初計画3（ＹB価）
			inputImplMonthPlan.setPlanned3ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned3Value()));
			// 月初計画4（ＹB価）
			inputImplMonthPlan.setPlanned4ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned4Value()));
			// 月初計画5（ＹB価）
			inputImplMonthPlan.setPlanned5ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned5Value()));
			// 月初計画6（ＹB価）
            inputImplMonthPlan.setPlanned6ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getPlanned6Value()));
            // 月末見込1（ＹB価）
            inputImplMonthPlan.setExpected1ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected1Value()));
            // 月末見込2（ＹB価）
            inputImplMonthPlan.setExpected2ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected2Value()));
            // 月末見込3（ＹB価）
            inputImplMonthPlan.setExpected3ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected3Value()));
            // 月末見込4（ＹB価）
            inputImplMonthPlan.setExpected4ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected4Value()));
            // 月末見込5（ＹB価）
            inputImplMonthPlan.setExpected5ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected5Value()));
            // 月末見込6（ＹB価）
            inputImplMonthPlan.setExpected6ValueYb(ConvertUtil.parseMoneyToNormalUnit(updateDto.getExpected6Value()));

			// 更新対象情報を作成
			ManageInsMonthPlan insMonthPlan = new ManageInsMonthPlan();
			insMonthPlan.setSeqKey(updateDto.getSeqKey());
			insMonthPlan.setInsNo(updateDto.getInsNo());
			insMonthPlan.setProdCode(updateDto.getProdCode());

			insMonthPlan.setImplMonthPlan(inputImplMonthPlan);
			insMonthPlan.setDelFlg(Boolean.FALSE);
			insMonthPlan.setUpDate(updateDto.getUpDate());

			// 登録/更新
			try {
				if (insMonthPlan.getSeqKey() == null) {
					manageInsMonthPlanDao.insert(insMonthPlan, pgId);
				} else {
					manageInsMonthPlanDao.update(insMonthPlan, pgId);
				}
			} catch (DuplicateException e) {
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "施設別計画登録に失敗"), e);
			}

			final InsType insType = updateDto.getInsType();
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

}
