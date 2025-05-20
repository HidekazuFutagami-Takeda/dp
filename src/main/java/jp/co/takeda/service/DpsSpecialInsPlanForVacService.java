package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.SpecialInsPlanDeleteDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForVacDto;
import jp.co.takeda.model.div.DpsHoInsType;
import jp.co.takeda.model.div.PlanType;

/**
 * ワクチン用特定施設個別計画の更新に関するサービスインターフェイス
 *
 * @author stakeuchi
 */
public interface DpsSpecialInsPlanForVacService {

	/**
	 * ワクチン用特定施設個別計画の削除用DTOを元に特定施設個別計画を削除する。
	 *
	 * @param deleteDtoList 特定施設個別計画の削除用DTOのリスト
	 * @param planType 削除対象の計画立案区分（NULL可:全削除）
	 */
	void deleteSpecialInsPlanForVac(List<SpecialInsPlanDeleteDto> deleteDtoList, PlanType planType);

	/**
	 *ワクチン用特定施設個別計画の削除用DTOを元に特定施設個別計画を登録する。
	 *
	 * @param modifyDto 登録用DTO
	 * @param planType 計画立案区分
	 * @param hoInsType 対象区分
	 * @throws LogicalException
	 */
	void modifyVac(SpecialInsPlanModifyForVacDto modifyDto, PlanType planType, DpsHoInsType hoInsType) throws LogicalException;

}
