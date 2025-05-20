package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsWsPlanForVacJgiListUpdateDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListUpdateDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateModifyAllDto;
import jp.co.takeda.logic.div.InsWsStatusUpdateType;

/**
 * ワクチン用施設特約店別計画機能の更新に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpsInsWsPlanForVacService {

	/**
	 * 施設特約店別計画の一括登録処理
	 * 
	 * @param upDateDto ワクチン用施設特約店別計画の一括登録用DTO
	 * @throws LogicalException
	 */
	void entryAllInsWsPlan(InsWsPlanForVacUpDateModifyAllDto upDateDto) throws LogicalException;

	/**
	 * 再配分処理を実行する(品目一覧)
	 * 
	 * @param jgiNo 従業員番号
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 品目一覧更新用DTOのリスト
	 */
	void rehaibun(Integer jgiNo, List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList);

	/**
	 * 施設特約店別計画のステータスを更新する(品目一覧)
	 * 
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param jgiNo 従業員番号
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 品目一覧更新用DTOのリスト
	 * @param updateType 更新種別(公開、公開解除、確定、確定解除)
	 */
	void updateStatus(String sosCd3, Integer jgiNo, List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType);

	/**
	 * 再配分処理を実行する(担当者一覧)
	 * 
	 * @param prodCode 品目固定コード
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 担当者一覧更新用DTOのリスト
	 */
	void rehaibun(String prodCode, List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList);

	/**
	 * 施設特約店別計画のステータスを更新する(担当者一覧)
	 * 
	 * @param prodCode 品目固定コード
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 担当者一覧更新用DTOのリスト
	 * @param updateType 更新種別(公開、公開解除、確定、確定解除)
	 */
	void updateStatus(String prodCode, List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType);
}
