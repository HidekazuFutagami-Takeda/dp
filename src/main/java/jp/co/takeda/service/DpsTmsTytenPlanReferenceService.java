package jp.co.takeda.service;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceResultDto;
import jp.co.takeda.dto.WsPlanReferenceScDto;

/**
 * 特約店別計画参照サービスインターフェイス
 * 
 * @author tkawabata
 */
public interface DpsTmsTytenPlanReferenceService {

	/**
	 * 画面ヘッダ情報を取得を取得する。
	 * 
	 * @param tytenCdPart 特約店コード(一部)
	 * @return 特約店別計画参照画面ヘッダー 結果DTO
	 */
	TmsTytenPlanReferenceHeaderResultDto searchHeaderInfo(String tytenCdPart);

	/**
	 * 特約店別計画サマリーを取得する。
	 * 
	 * @param wsPlanReferenceScDto 特約店別計画参照検索条件
	 * @return 特約店別計画参照画面 結果DTO
	 * @throws DataNotFoundException 検索結果がない場合にスロー
	 * @throws LogicalException
	 */
	TmsTytenPlanReferenceResultDto searchTmsTytenPlanSummary(WsPlanReferenceScDto wsPlanReferenceScDto) throws DataNotFoundException, LogicalException;
}
