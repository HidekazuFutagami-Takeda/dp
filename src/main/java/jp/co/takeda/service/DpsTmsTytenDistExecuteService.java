package jp.co.takeda.service;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.dto.TmsTytenDistDto;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.security.DpUser;

/**
 * 特約店別計画配分処理サービスインターフェイス
 * 
 * @author yokokawa
 */
public interface DpsTmsTytenDistExecuteService {
	/**
	 * 品目単位の配分処理を実行する
	 * 
	 * <pre>
	 * トランザクションを新規発行して、以下の処理を行う。
	 * ステータスのチェック
	 * 配分処理
	 * ステータス更新
	 * </pre>
	 * 
	 * @param sosMst2 組織情報(支店)
	 * @param plannedProd 品目情報
	 * @param sosMst3List 組織コード(営業所)リスト
	 * @param distParamOfficeUHList 配分基準(UH)
	 * @param distParamOfficePList 配分基準(P)
	 * @param dpUser 実行者従業員情報
	 * @param startTime 配分開始日時
	 * @param tmsTytenDistDto 特約店別計画配分DTO
	 * @return 特約店別計画配分DTO
	 */
	public TmsTytenDistDto executeDistProd(SosMst sosMst2, PlannedProd plannedProd, List<SosMst> sosMst3List, List<DistParamOffice> distParamOfficeUHList,
		List<DistParamOffice> distParamOfficePList, DpUser dpUser, Date startTime, TmsTytenDistDto tmsTytenDistDto);

	/**
	 * 特約店別計画立案ステータスを実行前の状態に戻す
	 * 
	 * <pre>
	 * トランザクションを新規発行して、以下の処理を行う。
	 * 特約店別計画立案ステータスロールバック
	 * </pre>
	 * 
	 * @param sosMst2 組織情報(支店)
	 * @param plannedProd 品目情報
	 * @param beforeWsPlanStatusList 実行前ステータス情報
	 */
	public void rollBackStatus(SosMst sosMst2, PlannedProd plannedProd, List<WsPlanStatus> beforeWsPlanStatusList) throws LogicalException;

	/**
	 * アテンション・お知らせ登録
	 * 
	 * 
	 * @param contactOperationsEntryDto 業務連絡の登録用DTO
	 * @throws LogicalException
	 */
	void entryContactOperations(ContactOperationsEntryDto contactOperationsEntryDto) throws LogicalException;
}
