package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.SpecialInsPlanDeleteDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForMrDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForOfficeDto;
import jp.co.takeda.model.div.DpsHoInsType;
import jp.co.takeda.model.div.PlanType;

/**
 * 特定施設個別計画の更新に関するサービスインターフェイス
 *
 * @author khashimoto
 */
public interface DpsSpecialInsPlanService {

	/**
	 * 特定施設個別計画（営業所案）を格納した特定施設個別計画の登録/更新用DTOを<br>
	 * 元に特定施設個別計画（営業所案/担当者立案）を登録する。
	 *
	 * @param modifyDto 特定施設個別計画（営業所案）の登録用DTO
	 * @throws LogicalException
	 */
	void modifyOffice(SpecialInsPlanModifyForOfficeDto modifyDto) throws LogicalException;

	/**
	 * 特定施設個別計画（担当者立案）を格納した特定施設個別計画の登録/更新用DTOを<br>
	 * 元に特定施設個別計画（担当者立案）を登録する。
	 *
	 * @param modifyDto 特定施設個別計画の登録/更新用DTO
	 * @param prodCategory 品目のカテゴリー
	 * @param hoInsType 対象区分
	 * @throws LogicalException
	 */
	void modifyMr(SpecialInsPlanModifyForMrDto modifyDto, String prodCategory, DpsHoInsType hoInsType) throws LogicalException;

	/**
	 * 特定施設個別計画の削除用DTOを元に特定施設個別計画を削除する。
	 *
	 * @param deleteDtoList 特定施設個別計画の削除用DTOのリスト
	 * @param isVaccine ワクチンであるか
	 * @param planType 削除対象の計画立案区分（NULL可:全削除）
	 */
	void deleteSpecialInsPlan(List<SpecialInsPlanDeleteDto> deleteDtoList, PlanType planType, boolean isVaccine);

	/**
	 * 営業所案に戻す処理を行う。<br>
	 * 特定施設個別計画の削除用DTOを元に特定施設個別計画の担当者立案を削除する。<br>
	 * 削除終了後に営業所案の特定施設個別計画を担当者立案として更新する。
	 *
	 * @param deleteDtoList 特定施設個別計画の削除用DTOのリスト
	 * @param isVaccine ワクチン選択されているか
	 */
	void updateOfficeToMr(List<SpecialInsPlanDeleteDto> deleteDtoList, boolean isVaccine);

}
