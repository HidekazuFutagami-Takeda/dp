package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.InsMonthPlanUpdateDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;

/**
 * 施設別計画の更新に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpmInsMonthPlanService {

	/**
	 * 施設別計画を登録・更新する。
	 *
	 * @param pgId 画面ID
	 * @param updateDtoList 施設別計画の更新用DTOリスト
	 * @return 更新結果DTO
	 */
	ManagePlanUpdateResultDto updateInsMonthPlan(String pgId, List<InsMonthPlanUpdateDto> updateDtoList);
}
