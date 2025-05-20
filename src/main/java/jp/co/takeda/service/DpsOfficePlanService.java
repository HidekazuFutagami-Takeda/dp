package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.OfficePlanUpdateDto;

/**
 * 営業所計画の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsOfficePlanService {

	/**
	 * 営業所計画を下書保存する <br>
	 * 以下の処理を行なう。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>施設特約店別計画のステータスチェック</li>
	 * <li>営業所計画ステータスの更新</li>
	 * <li>営業所計画の更新</li>
	 * </ul>
	 *
	 * @param sosCd 組織コード
	 * @param category カテゴリ
	 * @param officePlanList 登録対象の営業所計画
	 * @throws LogicalException
	 */
//	void updateOfficePlanToDraft(String sosCd, ProdCategory category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException;
	void updateOfficePlanToDraft(String sosCd, String category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException;

	/**
	 * 営業所計画を登録する <br>
	 * 以下の処理を行なう。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>施設特約店別計画のステータスチェック</li>
	 * <li>営業所計画ステータスの更新</li>
	 * <li>営業所計画の更新</li>
	 * </ul>
	 *
	 * @param sosCd 組織コード
	 * @param category カテゴリ
	 * @param officePlanList 登録対象の営業所計画
	 * @throws LogicalException
	 */
//	void updateOfficePlanToComplete(String sosCd, ProdCategory category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException;
	void updateOfficePlanToComplete(String sosCd, String category, List<OfficePlanUpdateDto> updateOfficePlanList) throws LogicalException;


}
