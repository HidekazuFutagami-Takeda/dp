package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.SosPlanUpdateDto;

/**
 * 組織別計画(チーム)の更新に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpmTeamPlanService {

	/**
	 * チーム別計画を登録・更新する。
	 * 
	 * @param pgId 画面ID
	 * @param updateDtoList 組織別計画の更新用DTOリスト
	 * @return 更新結果DTO
	 */
	ManagePlanUpdateResultDto updateTeamPlan(String pgId, List<SosPlanUpdateDto> updateDtoList);

}
