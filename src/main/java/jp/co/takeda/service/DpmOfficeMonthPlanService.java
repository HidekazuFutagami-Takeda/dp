package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.SosMonthPlanUpdateDto;

/**
 * 組織別計画(営業所)の更新に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpmOfficeMonthPlanService {

	/**
	 * 営業所別月別計画を登録・更新する。
	 *
	 * @param pgId 画面ID
	 * @param updateDtoList 組織別計画の更新用DTOリスト
	 * @return 更新件数
	 */
	int updateOfficePlan(String pgId, List<SosMonthPlanUpdateDto> updateDtoList);
}
