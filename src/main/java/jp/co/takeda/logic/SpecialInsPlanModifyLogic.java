package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dto.SpecialInsPlanModifyDto;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.div.PlanType;

/**
 * 特定施設個別計画の登録/更新ロジッククラス
 *
 * @author khashimoto
 */
public class SpecialInsPlanModifyLogic {

	/**
	 * 特定施設個別計画DAO
	 */
	private final SpecialInsPlanDao specialInsPlanDao;

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
	private final List<SpecialInsPlan> modifyList;

	/**
	 * 特定施設個別計画の計画立案区分
	 */
	private final PlanType planType;

	/**
	 * 品目カテゴリ
	 */
	private final String category;

	/**
	 * コンストラクタ
	 *
	 * @param specialInsPlanDao 特定施設個別計画DAO
	 * @param modifyDto 特定施設個別計画の登録/更新用DTO
	 * @param planType 処理対象の計画立案区分
	 */
	public SpecialInsPlanModifyLogic(SpecialInsPlanDao specialInsPlanDao, SpecialInsPlanModifyDto modifyDto, PlanType planType) {
		this(specialInsPlanDao, modifyDto, planType, null);
	}

	/**
	 *
	 * コンストラクタ
	 *
	 * @param specialInsPlanDao 特定施設個別計画DAO
	 * @param modifyDto 特定施設個別計画の登録/更新用DTO
	 * @param planType 処理対象の計画立案区分
	 * @param category 品目カテゴリ
	 */
	public SpecialInsPlanModifyLogic(SpecialInsPlanDao specialInsPlanDao, SpecialInsPlanModifyDto modifyDto, PlanType planType, String category) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (specialInsPlanDao == null) {
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
		if (modifyDto.getSpecialInsPlanList() == null) {
			final String errMsg = "特定施設個別計画のリストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.specialInsPlanDao = specialInsPlanDao;
		this.jgiNo = modifyDto.getJgiNo();
		this.insNo = modifyDto.getInsNo();
		this.upDate = modifyDto.getUpDate();
		this.modifyList = modifyDto.getSpecialInsPlanList();
		this.planType = planType;
		this.category = category;
	}

	/**
	 * 特定施設個別計画の登録/更新ロジック実行
	 *
	 * @throws DuplicateException
	 */
	public void modify() throws DuplicateException {

		// ----------------------
		// 削除処理
		// ----------------------
		if (upDate != null) {
			specialInsPlanDao.delete(jgiNo, insNo, planType, category, upDate);
		}
		// ----------------------
		// 登録処理
		// ----------------------
		for (SpecialInsPlan record : modifyList) {
			if (record.getPlannedValueY() != null) {
				// 指定された計画立案区分で登録する
				record.setPlanType(planType);
				specialInsPlanDao.insert(record);
			}
		}
	}
}
