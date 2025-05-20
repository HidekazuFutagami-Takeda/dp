package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsPlanScDto;
import jp.co.takeda.dto.InsProdPlanScDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.dto.ManageInsWsPlanProdScDto;
import jp.co.takeda.dto.ManageInsWsPlanScDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.dto.ManageWsPlanProdScDto;
import jp.co.takeda.dto.ManageWsPlanScDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.web.cmn.bean.ExportResult;

/**
 * 計画管理の帳票サービスインターフェイス
 *
 * @author rna8405
 */
public interface DpmReportService {

	/**
	 * (医)組織別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosPlanScDto 組織別計画編集画面の検索条件DTO
	 * @param jgiNo 従業員番号
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputSosPlanList(String templateRootPath, SosPlanScDto sosPlanScDto, String jgiNo) throws LogicalException;

	/**
	 * (医)組織品目別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosPlanScDto 組織別計画編集画面の検索条件DTO
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputProdPlanList(String templateRootPath, ProdPlanScDto prodPlanScDto) throws LogicalException;

	/**
	 * (医)施設別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param InsPlanScDto 施設別計画編集画面の検索条件DTO
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputInsPlanList(String templateRootPath, InsPlanScDto insPlanScDto) throws LogicalException;

	/** (ワ)施設別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param InsPlanScDto 組織別計画編集画面の検索条件DTO
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputInsPlanForVacList(String templateRootPath, InsPlanForVacScDto insPlanForVacScDto) throws LogicalException;

	/**
	 * (医)施設品目別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param InsProdPlanScDto 施設別計画編集画面の検索条件DTO
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputInsProdPlanList(String templateRootPath, InsProdPlanScDto insProdPlanScDto) throws LogicalException;

	/**
	 * (医)施設特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param scDto 施設特約店別計画画面の検索条件DTO
	 * @param ryutsu 流通政策部かどうか
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputInsWsPlanList(String templateRootPath, ManageInsWsPlanScDto scDto, boolean ryutsu) throws LogicalException;

	/**
	 * (ワ)施設特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param scDto 施設特約店別計画画面の検索条件DTO
	 * @param ryutsu 流通政策部かどうか
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputInsWsPlanForVacList(String templateRootPath, ManageInsWsPlanForVacScDto insWsPlanForVacScDto, boolean ryutsu) throws LogicalException;

	/**
	 * (医)施設特約店品目別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param scDto 施設特約店別計画画面の検索条件DTO
	 * @param ryutsu 流通政策部であるか
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputInsWsPlanProdList(String templateRootPath, ManageInsWsPlanProdScDto manageInsWsPlanProdScDto, boolean ryutsu) throws LogicalException;

	/**
	 * (医)施設特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param scDto 施設特約店別計画画面の検索条件DTO
	 * @param ryutsu 流通政策部であるか
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputManageWsPlanList(String templateRootPath, ManageWsPlanScDto scDto, boolean ryutsu, String titleUH, String titleP, String titleZ) throws LogicalException;

	/**
	 * (ワ)施設特約店別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param scDto 施設特約店別計画画面の検索条件DTO
	 * @param ryutsu 流通政策部であるか
	 * @return 出力結果
	 * @throws LogicalException
	 */
	ExportResult outputManageWsVacPlanList(String templateRootPath, ManageWsPlanForVacScDto scDto, boolean ryutsu) throws LogicalException;

    /**
     * 施設特約店別計画を出力する。
     *
     * @param templateRootPath テンプレート配置パス(絶対パス名)
     * @param scDto 特約店品目別計画画面の検索条件DTO
     * @param ryutsu 流通政策部であるか
     * @return 出力結果
     * @throws LogicalException
     */
    ExportResult outputManageWsPlanProdList(String templateRootPath, ManageWsPlanProdScDto scDto, boolean ryutsu) throws LogicalException;

	/**
	 * 組織別月別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosPlanScDto 組織別月別計画編集画面の検索条件DTO
	 * @param jgiNo MRユーザの従業員番号
	 * @param tougetuCount 当月カウント
	 * @return 出力結果
	 * @throws LogicalException
	 */
	////mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
    //ExportResult outputSosMonthPlanList(String templateRootPath, SosPlanScDto sosPlanScDto, String jgiNo) throws LogicalException;
	ExportResult outputSosMonthPlanList(String templateRootPath, SosPlanScDto sosPlanScDto, String jgiNo, int tougetuCount) throws LogicalException;
	//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

	/**
	 * 組織品目別月別計画を出力する。
	 *
	 * @param templateRootPath テンプレート配置パス(絶対パス名)
	 * @param sosPlanScDto 組織品目別月別計画編集画面の検索条件DTO
	 * @param jrnsCtgList JRNSに含まれるカテゴリリスト
	 * @param tougetuCount 当月カウント
	 * @return 出力結果
	 * @throws LogicalException
	 */
	//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	//ExportResult outputProdMonthPlanList(String templateRootPath, ProdPlanScDto scDto, List<String> jrnsCtgList) throws LogicalException;
	ExportResult outputProdMonthPlanList(String templateRootPath, ProdPlanScDto scDto, List<String> jrnsCtgList, int tougetuCount) throws LogicalException;
	//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
}
