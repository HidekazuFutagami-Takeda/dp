package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.SosPlanUpdateDto;

/**
 * 組織別計画(全社)の更新に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpmIyakuPlanService {

	/**
	 * 全社計画を登録・更新する。
	 * 
	 * @param pgId 画面ID
	 * @param updateDtoList 組織別計画の更新用DTOリスト
	 * @return 更新結果DTO
	 */
	ManagePlanUpdateResultDto updateIyakuPlan(String pgId, List<SosPlanUpdateDto> updateDtoList);

}
