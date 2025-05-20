package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsWsPlanJgiListUpdateDto;
import jp.co.takeda.dto.InsWsPlanProdListUpdateDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.logic.div.InsWsStatusUpdateType;

/**
 * 施設特約店別計画機能の更新に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsInsWsPlanService {

	/**
	 * 施設特約店別計画の登録処理
	 *
	 * @param upDateDto 施設特約店別計画の追加・更新用DTO
	 * @throws LogicalException
	 */
	void entryInsWsPlan(InsWsPlanUpDateModifyDto upDateDto) throws LogicalException;

	/**
	 * 再配分処理を実行する(品目一覧)
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiNo 従業員番号
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 品目一覧更新用DTOのリスト
	 */
	void rehaibun(String sosCd3, Integer jgiNo, List<InsWsPlanProdListUpdateDto> insWsPlanUpdateDtoList);

	/**
	 * 施設特約店別計画のステータスを更新する(品目一覧)
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 品目一覧更新用DTOのリスト
	 * @param updateType 更新種別(公開、公開解除、確定、確定解除)
	 */
	void updateStatus(String sosCd3, String sosCd4, Integer jgiNo, List<InsWsPlanProdListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType);

	/**
	 * 再配分処理を実行する(担当者一覧)
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 担当者一覧更新用DTOのリスト
	 */
	void rehaibun(String sosCd3, String prodCode, List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList);

	/**
	 * 施設特約店別計画のステータスを更新する(担当者一覧)
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param insWsPlanUpdateDtoList 施設特約店別計画 担当者一覧更新用DTOのリスト
	 * @param updateType 更新種別(公開、公開解除、確定、確定解除)
	 */
	void updateStatus(String sosCd3, String prodCode, List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList, InsWsStatusUpdateType updateType);

	/**
	 * 調整金額用のメッセージを生成する。
	 *
	 * @param dto 検索結果DTO
	 * @return 調整金額用のメッセージ
	 */
	String createChoseiMsg(InsWsPlanUpDateResultListDto dto);

	/**
	 * 施設特約店別計画のステータスを確定に更新する 調整金額有無チェックを行い、差額があれば確定しない。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 */
	void updateStatusWithDiffCheck(String sosCd3, String prodCode) throws LogicalException;
}
