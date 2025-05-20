package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.logic.div.ValueChangeType.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.ManageInsWsPlanDao;
import jp.co.takeda.dto.ManageInsWsPlanEntryDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.model.ImplPlan;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.ManageInsWsPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 【管理】施設特約店別計画更新サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmInsWsPlanService")
public class DpmInsWsPlanServiceImpl implements DpmInsWsPlanService {

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstRealDao")
	protected InsMstRealDao insMstRealDao;

	/**
	 * 管理の施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsWsPlanDao")
	protected ManageInsWsPlanDao manageInsWsPlanDao;

	/**
	 * 実施計画作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCreateImplPlanService")
	protected DpmCreateImplPlanService dpmCreateImplPlanService;

	// 施設特約店別計画登録・更新
	public ManagePlanUpdateResultDto updateInsWsPlan(String pgId, List<ManageInsWsPlanEntryDto> entryDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (entryDtoList == null) {
			final String errMsg = "施設特約店別計画の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int updateCountUh = 0;
		int updateCountP = 0;
		int updateCountZ = 0;
		int updateCountUhp = 0;
		for (ManageInsWsPlanEntryDto entryDto : entryDtoList) {

			// 引数チェック
			final String insNo = entryDto.getInsNo();
			if (insNo == null) {
				final String errMsg = "更新対象の施設コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			final String oriTmsTytenCd = entryDto.getTmsTytenCd();
			if (oriTmsTytenCd == null) {
				final String errMsg = "更新対象の特約店コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			// 13桁に変換
			final String tmsTytenCd = new CreateTmsTytenCdLogic(oriTmsTytenCd).execute();
			final String prodCode = entryDto.getProdCode();
			if (prodCode == null) {
				final String errMsg = "更新対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			InsMst insMst = null;
			try {
				insMst = insMstRealDao.searchRealIncludeMr(insNo, prodCode);
			} catch (DataNotFoundException e) {
				final String errMsg = "更新対象の施設コードが施設情報に存在しない";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 更新値
			final Long inputValue = ConvertUtil.parseMoneyToNormalUnit(entryDto.getBaseTAfter());
			// 対象区分
			final InsType insType = InsType.convertInsType(insMst.getHoInsType());

			// 現在の計画値を取得
			ImplPlan orgImplPlan;
			try {
				ManageInsWsPlan manageInsWsPlan = manageInsWsPlanDao.searchUk(prodCode, insNo, tmsTytenCd);
				orgImplPlan = manageInsWsPlan.getImplPlan();
			} catch (DataNotFoundException e) {
				orgImplPlan = new ImplPlan();
			}

			// 入力された計画値をもとに、更新対象の実施計画を作成
			ImplPlan newImplPlan;
			try {
				newImplPlan = dpmCreateImplPlanService.getImplPlan(prodCode, inputValue, insType, FROM_T, tmsTytenCd, orgImplPlan);
			} catch (LogicalException e) {
				final String errMsg = "T/Y変換処理で失敗。更新対象: " + entryDto;
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			// 更新対象情報を作成
			ManageInsWsPlan insWsPlan = new ManageInsWsPlan();
			insWsPlan.setSeqKey(entryDto.getSeqKey());
			insWsPlan.setInsNo(insNo);
			insWsPlan.setProdCode(prodCode);
			insWsPlan.setTmsTytenCd(tmsTytenCd);
			insWsPlan.setImplPlan(newImplPlan);
			insWsPlan.setDelFlg(isAlive(entryDto));
			insWsPlan.setUpDate(entryDto.getUpDate());

			// 登録/更新
			try {
				if (insWsPlan.getSeqKey() == null) {
					try {
						ManageInsWsPlan oldInsWsPlan = manageInsWsPlanDao.searchUk(prodCode, insNo, tmsTytenCd);
						// 元々削除の場合、更新する。
						if (oldInsWsPlan.getDelFlg()) {
							insWsPlan.setSeqKey(oldInsWsPlan.getSeqKey());
							insWsPlan.setUpDate(oldInsWsPlan.getUpDate());
							manageInsWsPlanDao.update(insWsPlan, pgId);
						} else {
							// 元々削除ではない場合、排他エラー
							final String errMsg = "論理削除復旧済みのため、楽観的ロックエラーとする";
							throw new OptimisticLockingFailureException(errMsg);
						}
					} catch (DataNotFoundException e) {
						manageInsWsPlanDao.insert(insWsPlan, pgId);
					}
				} else {
					manageInsWsPlanDao.update(insWsPlan, pgId);
				}
			} catch (DuplicateException e) {
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "施設特約店別計画登録に失敗"), e);
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
	 * 入力された計画値がnullの場合は、LIVE(false)。それ以外の場合はDELETE(true)とする。
	 *
	 * @param updateDto 更新対象のDTO
	 * @return true：DELETE、false：LIVE
	 */
	private Boolean isAlive(ManageInsWsPlanEntryDto updateDto) {
		if (updateDto.getBaseTAfter() == null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
