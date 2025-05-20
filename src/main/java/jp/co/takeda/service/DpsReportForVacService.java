package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;
import jp.co.takeda.web.cmn.bean.ExportResult;

/**
 * ワクチン用計画支援の帳票サービスインターフェイス
 *
 * @author stakeuchi
 */
public interface DpsReportForVacService {

	/**
	 * (ワ)配分ミスリストを出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param outputFileId 出力ファイル情報ID
	 * @return 出力結果
	 */
	ExportResult outputDistMissList(String templateRootPath, Long outputFileId);

	/**
	 * 実績無の(ワ)特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param scDto (ワ)特約店別計画参照画面の検索条件DTO
	 * @return 出力結果
	 */
	ExportResult outputWsPlanListNonJisseki(String templateRootPath, WsPlanReferenceForVacScDto refScDto) throws LogicalException;

	/**
	 * (ワ)特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param scDto (ワ)特約店別計画参照画面の検索条件DTO
	 * @return 出力結果
	 */
	ExportResult outputWsPlanList(String templateRootPath, WsPlanReferenceForVacScDto refScDto) throws LogicalException;

	/**
	 * (ワ)施設計画市区郡町村別集計結果を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param jgiNo 小児科MRの従業員番号 (J19-0010 対応・コメントのみ修正)
	 * @return 出力結果
	 */
	ExportResult outputInsWsCityList(String templateRootPath, Integer jgiNo) throws LogicalException;
}
