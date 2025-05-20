package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.ManageInsWsPlanEntryDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;

/**
 * 【管理】施設特約店別計画更新サービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpmInsWsPlanService {

	/**
	 * 施設特約店別計画を登録・更新する。
	 * 
	 * @param pgId 画面ID
	 * @param entryDtoList 施設特約店別計画の更新用DTOリスト
	 * @return 更新結果DTO
	 */
	ManagePlanUpdateResultDto updateInsWsPlan(String pgId, List<ManageInsWsPlanEntryDto> entryDtoList);

}
