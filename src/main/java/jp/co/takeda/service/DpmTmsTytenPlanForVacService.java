package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.dto.ManageWsPlanForVacEntryDto;

/**
 * 管理の(ワ)特約店別計画の更新に関するサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpmTmsTytenPlanForVacService {

	/**
	 * (ワ)特約店別計画を登録・更新する。
	 * 
	 * @param pgId 画面ID
	 * @param updateDtoList (ワ)特約店別計画の更新用DTOリスト
	 * @return 更新結果DTO
	 */
	ManagePlanForVacUpdateResultDto updateTmsTytenPlan(String pgId, List<ManageWsPlanForVacEntryDto> updateDtoList);

}
