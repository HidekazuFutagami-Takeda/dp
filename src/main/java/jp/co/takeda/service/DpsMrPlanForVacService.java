package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.MrPlanForVacUpdateDto;

/**
 * (ワクチン)担当者別計画機能の更新に関するサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsMrPlanForVacService {

	/**
	 * ワクチン用担当者別計画の登録・更新を行う。<br>
	 * 
	 * @param updateDtoList ワクチン用担当者別計画更新用DTOのリスト
	 */
	void updateMrPlan(List<MrPlanForVacUpdateDto> updateDtoList) throws LogicalException;

}
