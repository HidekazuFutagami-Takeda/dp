package jp.co.takeda.service;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenPlanReferenceForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderForVacResultDto;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;

/**
 * ワクチン特約店別計画参照サービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpsTmsTytenPlanReferenceForVacService {

	/**
	 * 画面ヘッダ情報を取得を取得する。
	 * 
	 * @param tytenCdPart 特約店コード(一部)
	 * @return ワクチン特約店別計画参照画面ヘッダ 結果DTO
	 */
	TmsTytenPlanReferenceHeaderForVacResultDto searchHeaderInfo(String tytenCdPart);

	/**
	 * ワクチン特約店別計画サマリを取得する。
	 * 
	 * @param wsPlanReferenceScDto ワクチン特約店別計画参照検索条件
	 * @return ワクチン特約店別計画参照画面 結果DTO
	 * @throws DataNotFoundException 検索結果がない場合にスロー
	 * @throws LogicalException
	 */
	TmsTytenPlanReferenceForVacResultDto searchTmsTytenPlanSummary(WsPlanReferenceForVacScDto wsPlanReferenceScDto) throws DataNotFoundException, LogicalException;
}
