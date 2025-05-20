package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.SpecialInsPlanForVacResultDto;
import jp.co.takeda.dto.SpecialInsPlanForVacScDto;
import jp.co.takeda.dto.SpecialInsPlanSearchForVacResultListDto;
import jp.co.takeda.model.div.PlanType;

/**
 * ワクチン用特定施設個別計画の検索に関するサービスインタフェース
 *
 * @author stakeuchi
 */
public interface DpsSpecialInsPlanForVacSearchService {

	/**
	 * ワクチン用特定施設個別計画（担当者立案）の検索結果DTOを取得する。
	 *
	 * @param scDto ワクチン用特定施設個別計画の検索条件用DTO
	 * @param planType 計画立案区分
	 * @return ワクチン用特定施設個別計画の検索結果用DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	List<SpecialInsPlanForVacResultDto> searchSpecialInsPlanForVac(SpecialInsPlanForVacScDto scDto, PlanType planType) throws LogicalException;

	/**
	 * ワクチン用特定施設個別計画編集画面（担当者立案）の検索結果DTOを取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @return ワクチン用特定施設個別計画編集画面の検索結果用DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	SpecialInsPlanSearchForVacResultListDto searchMrProdList(Integer jgiNo, String insNo) throws LogicalException;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * ワクチン用特定施設個別計画編集画面（営業所立案）の検索結果DTOを取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param addrCodePref 組織コード
	 * @return ワクチン用特定施設個別計画編集画面の検索結果用DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	SpecialInsPlanSearchForVacResultListDto searchOfficeProdList(Integer jgiNo, String insNo, String addrCodePref) throws LogicalException;
}
