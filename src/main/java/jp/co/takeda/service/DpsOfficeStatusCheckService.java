package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.SosMst;

/**
 * 営業所計画ステータスのステータスチェックを行なうサービスインターフェイス
 *
 * @author nozaki
 */
public interface DpsOfficeStatusCheckService {

	/**
	 * 組織情報（支店）、カテゴリを指定して、営業所計画ステータスチェックを行なう <br>
	 * 指定された支店直下の営業所に対し、営業所計画をチェックする。 組織情報は、組織コードは必須。
	 *
	 * @param sosMst 支店の組織情報（組織コードは必須）
	 * @param prodCategory カテゴリ
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている営業所計画ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
//	List<OfficePlanStatus> executeForShiten(SosMst sosMst, ProdCategory prodCategory, List<OfficeStatusForCheck> unallowableStatusList);
	List<OfficePlanStatus> executeForShiten(SosMst sosMst, String prodCategory, List<OfficeStatusForCheck> unallowableStatusList);

	/**
	 * 組織情報（営業所）、カテゴリを指定して、営業所計画ステータスチェックを行なう <br>
	 * 組織情報の、組織コードは必須。
	 *
	 * @param sosMst 営業所の組織情報（組織コードは必須）
	 * @param prodCategory カテゴリ
	 * @param unallowableStatusList 許可しないステータスのリスト
	 * @return 現在DBに登録されている営業所計画ステータスのリスト
	 * @exception UnallowableStatusException ステータスエラーがあった場合
	 */
//	List<OfficePlanStatus> executeForOffice(SosMst sosMst, ProdCategory prodCategory, List<OfficeStatusForCheck> unallowableStatusList);
	List<OfficePlanStatus> executeForOffice(SosMst sosMst, String prodCategory, List<OfficeStatusForCheck> unallowableStatusList);

}
