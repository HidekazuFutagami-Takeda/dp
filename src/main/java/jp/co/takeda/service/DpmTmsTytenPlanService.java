package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.ManageWsPlanEntryDto;

/**
 * 管理の特約店別計画の更新に関するサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpmTmsTytenPlanService {

	/**
	 * 特約店別計画を登録・更新する。
	 * 
	 * @param pgId 画面ID
	 * @param updateDtoList 特約店別計画の更新用DTOリスト
	 * @return 更新結果DTO
	 */
	ManagePlanUpdateResultDto updateTmsTytenPlan(String pgId, List<ManageWsPlanEntryDto> updateDtoList);

}
