package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DistPlanningListSummaryScDto;
import jp.co.takeda.dto.WsPlanReferenceScDto;
import jp.co.takeda.model.DistPlanningListULSummary;
import jp.co.takeda.web.cmn.bean.ExportResult;

/**
 * 計画支援の帳票サービスインターフェイス
 *
 * @author tkawabata
 */
public interface DpsReportService {

	/**
	 * 配分ミスリストを出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param outputFileId 出力ファイル情報ID
	 * @return 出力結果
	 */
	ExportResult outputDistMissList(String templateRootPath, Long outputFileId);

	/**
	 * 関係会社別施設特約店別一覧を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosCd 組織コード(支店または営業所)
	 * @param downloadFileTempDir ダウンロードファイルディレクトリ
	 * @return ファイル名(ファイル生成されていない場合、NULLを返す)
	 */
	String outputRelationCompanyInsWsList(String templateRootPath, String sosCd, String downloadFileTempDir);

	/**
	 * 実績無の特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param refScDto 特約店別計画参照画面の検索条件DTO
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputWsPlanListNonJisseki(String templateRootPath, WsPlanReferenceScDto refScDto) throws LogicalException;

	/**
	 * 特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param refScDto 特約店別計画参照画面の検索条件DTO
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputWsPlanList(String templateRootPath, WsPlanReferenceScDto refScDto) throws LogicalException;

	/**
	 * ①担当者別品目別計画一覧を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosCd 組織コード
	 * @param mrPlanOutputDivision 出力区分
	 * @param downloadFileTempDir ダウンロードファイルディレクトリ
	 * @return ファイル名(ファイル生成されていない場合、NULLを返す)
	 */
	String outputMrPlanMMPList(String templateRootPath, String sosCd, MrPlanOutputDivision mrPlanOutputDivision, String downloadFileTempDir, String category);

	/**
	 * ②品目別担当者別計画検討表を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosCd 組織コード(営業所)
	 * @param downloadFileTempDir ダウンロードファイルディレクトリ
	 * @return ファイル名(ファイル生成されていない場合、NULLを返す)
	 */
	String outputReviewProdMrMMPList(String templateRootPath, String sosCd, String downloadFileTempDir, String category);

	/**
	 * ③担当者別品目別計画検討表を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosCd
	 * @param downloadFileTempDir ダウンロードファイルディレクトリ
	 * @return ファイル名(ファイル生成されていない場合、NULLを返す)
	 */
	String outputReviewMrProdMMPList(String templateRootPath, String sosCd, String downloadFileTempDir, String category);

	/**
	 * ④チーム担当者計画検討表を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosCd 組織コード(営業所)
	 * @param downloadFileTempDir ダウンロードファイルディレクトリ
	 * @return ファイル名(ファイル生成されていない場合、NULLを返す)
	 */
	String outputTeamMrReport(String templateRootPath, String sosCd, String downloadFileTempDir, String category);


	/**
	 * 営業所計画アップロード用のフォーマットファイル(指定したカテゴリ)を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param DistPlanningListSummaryScDto 営業所情報と計画対象品目の検索項目
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputDistPlanningList(String templateRootPath, DistPlanningListSummaryScDto scDto) throws LogicalException;

	/**
	 * 営業所計画アップロード用のフォーマットファイル(指定したカテゴリ以外)を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param DistPlanningListSummaryScDto 営業所情報と計画対象品目の検索項目
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputDistPlanningListExceptCategory(String templateRootPath, DistPlanningListSummaryScDto scDto) throws LogicalException;

	/**
	 * 営業所計画アップロード用のフォーマットファイル(本部用)を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param DistPlanningListSummaryScDto 営業所情報と計画対象品目の検索項目
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputDistPlanningListHonbu(String templateRootPath, DistPlanningListSummaryScDto scDto) throws LogicalException;

	/**
	 * 営業所計画アップロード用のフォーマットファイルを出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param List<DistPlanningListULSummary> アップロードファイルのエラー内容
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputDistPlanningErrList(String templateRootPath, List<DistPlanningListULSummary> distPlanningListErrSummaryList) throws LogicalException;

	/**
	 * 出力区分(「理論計画①」「理論計画②」「決定計画」)を表す列挙
	 *
	 * @author kibe
	 */
	public static enum MrPlanOutputDivision {
		/**
		 * 理論計画①
		 */
		RIRON_1,

		/**
		 * 理論計画②
		 */
		RIRON_2,

		/**
		 * 営業所案
		 */
		OFFICE,

		/**
		 * 決定計画
		 */
		KETTEI;
	}

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 削除施設・調整あり計画データファイル(本部用)を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param DistPlanningListSummaryScDto 営業所情報と計画対象品目の検索項目
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputDelFacilitiesAndAdjustmentsList(String templateRootPath, DistPlanningListSummaryScDto scDto) throws LogicalException;
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	/**
	 * 施設特約店計画データファイルを出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param fileName ファイルヘッダー名
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputCheckTool(String templateRootPath, String fileName) throws LogicalException;
// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
}
