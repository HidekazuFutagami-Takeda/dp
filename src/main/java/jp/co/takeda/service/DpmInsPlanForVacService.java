package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.InsPlanForVacUpdateDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;

/**
 * ワクチン用施設別計画の更新に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpmInsPlanForVacService {

	/**
	 * ワクチン用施設別計画を登録・更新する。
	 * 
	 * @param pgId 画面ID
	 * @param updateDtoList ワクチン用施設別計画の更新用DTOリスト
	 * @return 更新結果DTO
	 */
	ManagePlanForVacUpdateResultDto updateInsPlan(String pgId, List<InsPlanForVacUpdateDto> updateDtoList);
}
