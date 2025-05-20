package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.SpecialInsPlanForVacDao;
import jp.co.takeda.dto.SpecialInsPlanForVacDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForVacDto;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.util.ConvertUtil;

/**
 * 特定施設個別計画の登録/更新ロジッククラス
 *
 * @author khashimoto
 */
public class SpecialInsPlanModifyForVacLogic {

	/**
	 * 特定施設個別計画DAO
	 */
	private final SpecialInsPlanForVacDao specialInsPlanDao;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 特定施設個別計画のリスト
	 */
	private final List<SpecialInsPlanForVacDto> modifyList;

	/**
	 * 特定施設個別計画の計画立案区分
	 */
	private final PlanType planType;

	/**
	 * コンストラクタ
	 *
	 * @param specialInsPlanForVacDao 特定施設個別計画DAO
	 * @param modifyDto 特定施設個別計画の登録/更新用DTO
	 * @param planType 処理対象の計画立案区分
	 */
	public SpecialInsPlanModifyForVacLogic(SpecialInsPlanForVacDao specialInsPlanForVacDao, SpecialInsPlanModifyForVacDto modifyDto, PlanType planType) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (specialInsPlanForVacDao == null) {
			final String errMsg = "特定施設個別計画にアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (modifyDto == null) {
			final String errMsg = "特定施設個別計画の登録/更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (modifyDto.getInsNo() == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (modifyDto.getSpecialInsPlanDtoList() == null) {
			final String errMsg = "特定施設個別計画のリストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.specialInsPlanDao = specialInsPlanForVacDao;
		this.jgiNo = modifyDto.getJgiNo();
		this.insNo = modifyDto.getInsNo();
		this.upDate = modifyDto.getUpDate();
		this.modifyList = modifyDto.getSpecialInsPlanDtoList();
		this.planType = planType;
	}

	/**
	 * 特定施設個別計画の登録/更新ロジック実行
	 *
	 *
	 * @throws DuplicateException
	 */
	public void modify() throws DuplicateException {

		// ----------------------
		// 削除処理
		// ----------------------
		if (upDate != null) {
			specialInsPlanDao.delete(jgiNo, insNo, planType, upDate);
		}
		// ----------------------
		// 登録処理
		// ----------------------
		List<SpecialInsPlanForVac> inputList = new ArrayList<SpecialInsPlanForVac>();
		for (int i = 0; i < modifyList.size(); i++) {
			SpecialInsPlanForVac specialInsPlan = new SpecialInsPlanForVac();
			specialInsPlan.setJgiNo(jgiNo);
			specialInsPlan.setInsNo(modifyList.get(i).getInsNo());
			specialInsPlan.setProdCode(modifyList.get(i).getProdCode());
			specialInsPlan.setTmsTytenCd(modifyList.get(i).getTmsTytenCd());
			specialInsPlan.setPlanType(planType);
			specialInsPlan.setPlannedValueB(ConvertUtil.parseMoneyToNormalUnit(modifyList.get(i).getPlannedValueB()));
			specialInsPlan.setUpDate(upDate);
			specialInsPlan.setJgiName(modifyList.get(i).getJgiName());
			inputList.add(specialInsPlan);
		}
		for (SpecialInsPlanForVac record : inputList) {
			if (record.getPlannedValueB() != null) {
				// 指定された計画立案区分で登録する
				record.setPlanType(planType);
				specialInsPlanDao.insert(record);
			}
		}
	}
}
