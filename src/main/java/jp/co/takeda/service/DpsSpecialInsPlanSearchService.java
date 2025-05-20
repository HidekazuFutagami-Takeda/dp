package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.SpecialInsPlanResultDto;
import jp.co.takeda.dto.SpecialInsPlanScDto;
import jp.co.takeda.dto.SpecialInsPlanSearchOfficeDto;
import jp.co.takeda.model.div.PlanType;

/**
 * 特定施設個別計画の検索に関するサービスインタフェース
 *
 * @author stakeuchi
 */
public interface DpsSpecialInsPlanSearchService {

	/**
	 * 特定施設個別計画（営業所案・担当者立案）を格納した特定施設個別計画の初期表示用DTOを取得する。
	 *
	 * @param jgiNo 施設を担当する従業員番号
	 * @param insNo 施設コード
	 * @param mrFlg 担当者立案を取得するかを判断するフラグ
	 * @return 特定施設個別計画の初期表示DTO
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	SpecialInsPlanSearchOfficeDto searchOfficeProdList(Integer jgiNo, String insNo, String category, boolean mrFlg) throws LogicalException;

	/**
	 * 特定施設個別計画（営業所案・担当者立案）の検索結果DTOを取得する。
	 *
	 * @param scDto 特定施設個別計画の検索条件用DTO
	 * @param planType 計画立案区分
	 * @return 特定施設個別計画の検索結果用DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	List<SpecialInsPlanResultDto> searchSpecialInsPlan(SpecialInsPlanScDto scDto, PlanType planType) throws LogicalException;

}
