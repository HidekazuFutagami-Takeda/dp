package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsDocPlanJgiListUpdateDto;
import jp.co.takeda.dto.InsDocPlanProdListUpdateDto;
import jp.co.takeda.dto.InsDocPlanUpDateDto;
import jp.co.takeda.logic.div.InsDocStatusUpdateType;
import jp.co.takeda.model.div.InsType;

/**
 * 施設医師別計画機能の更新に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpsInsDocPlanService {

	/**
	 * 施設医師別計画の登録処理
	 * 
	 * @param upDateDto 施設医師別計画の追加・更新用DTO
	 * @param insType 対象区分
	 * @throws LogicalException
	 */
	void entryInsDocPlan(List<InsDocPlanUpDateDto> upDateList, InsType insType) throws LogicalException;

	/**
	 * 施設医師別計画のステータスを更新する(品目一覧)
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param insWsPlanUpdateDtoList 施設医師別計画 品目一覧更新用DTOのリスト
	 * @param updateType 更新種別(公開、公開解除、確定、確定解除)
	 */
	void updateStatus(String sosCd3, String sosCd4, Integer jgiNo, List<InsDocPlanProdListUpdateDto> insDocPlanUpdateDtoList, InsDocStatusUpdateType updateType);

	/**
	 * 施設医師別計画のステータスを更新する(担当者一覧)
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param insDocPlanJgiListUpdateDto 施設医師別計画 担当者一覧更新用DTOのリスト
	 * @param updateType 更新種別(公開、公開解除、確定、確定解除)
	 */
	void updateStatus(String sosCd3, String prodCode, List<InsDocPlanJgiListUpdateDto> insWsPlanUpdateDtoList, InsDocStatusUpdateType updateType);
}
