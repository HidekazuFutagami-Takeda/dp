package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.JgiMstRealDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dto.ProdMonthPlanResultDetailDto;
import jp.co.takeda.dto.ProdMonthPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.Month;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 組織品目別月別計画一覧のExcelファイルを生成するロジッククラス
 *
 * @author rna8405
 */
public class ProdMonthPlanExportLogic {

	/**
	 * 組織情報取得DAO
	 */
	private final SosMstRealDao sosMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	private final MasterManagePlannedProdDao plannedProdDao;

	/**
	 * 汎用コードマスターDAO
	 */
	private final CodeMasterDao codeMasterDao;

	/**
	 * 従業員情報取得DAO
	 */
	private final JgiMstRealDao jgiMstRealDao;

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 組織別月別計画編集画面の検索条件DTO
	 */
	private final ProdPlanScDto prodPlanScDto;

	/**
	 * 組織品目別月別計画の検索結果
	 */
	private ProdMonthPlanResultDto prodPlanResult;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	/**
	 * 組織従業員文字列
	 */
	private final String orgMrNm;

    //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	/**
	 * 当月カウント：何月目かを記録（1月目なら0が入る）
	 * ただし第3営業日以内の場合実績が取り込まれていないので-1している
	 */
	private int tougetuCount;
    //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "組織・担当者品目別月別計画_";

	/** シート名 */
	private static final String SHEET_NAME = "組織・担当者品目別月別計画一覧";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「組織・担当者」 */
	private static final String TEXT_ORG_MR = "組織・担当者：";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ：";

	/** 文字列「全社」 */
	private static final String TEXT_ALL_COMPANY = "全社";

	/** 文字列「積上(Y)」 */
	private static final String TEXT_STACK_Y = "積上(Y)";

	/** 文字列「積上(B)」 */
	private static final String TEXT_STACK_B = "積上(B)";

	/** 文字列「計画(Y)」 */
	private static final String TEXT_PLAN_Y = "計画(Y)";

	/** 文字列「計画(B)」 */
	private static final String TEXT_PLAN_B = "計画(B)";

	/** 文字列「見込(Y)」 */
	private static final String TEXT_EXPECT_Y = "見込(Y)";

	/** 文字列「見込(B)」 */
	private static final String TEXT_EXPECT_B = "見込(B)";

	/** 出力Excel拡張子 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private static final String EXCEL_EXTENSION = ".xls";
	private static final String EXCEL_EXTENSION = ".xlsx";
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	/** [行番号] 表示条件 */
	private static final int ROW_IDX_DISP_CONDITION = 1;

	/** [列番号] 表示条件 */
	private static final int COL_IDX_DISP_CONDITION = 0;

	/** [行番号] 現在日付 */
	private static final int ROW_IDX_DATE = 0;

	/** [列番号] 現在日付 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_DATE = 58;
	private static final int COL_IDX_DATE = 66;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [行番号] 月表示ヘッダ */
	private static final int ROW_IDX_MONTH_HEAD = 2;

	/** [行番号] 項目名表示ヘッダ */
	private static final int ROW_IDX_NAME_HEAD = 4;

	/** [行番号] 集計行 */
	private static final int ROW_IDX_PLAN_SUM = 5;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 6;

	/** [列番号] 品目名称  */
	private static final int COL_IDX_PROD_NAME = 0;

	/** [列番号] 品目コード  */
	private static final int COL_IDX_PROD_CDDE = 1;

	/** [列番号] 期別計画  */
	private static final int COL_IDX_TERM_PLAN = 2;

    // add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を
	/** [列番号] 累計実績  */
	private static final int COL_IDX_TOTAL_VALUEYB = 3;

	/** [列番号] 当月実績  */
	private static final int COL_IDX_TOUGETU_VALUEYB = 4;
    // add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を

	/** [列番号] 月初計画：積上(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_STACK = 3;
	private static final int COL_IDX_PLANNED_STACK = 5;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月初計画：計画(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_VALUE = 5;
	private static final int COL_IDX_PLANNED_VALUE = 7;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：積上(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_STACK = 7;
	private static final int COL_IDX_EXPECTED_STACK = 9;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：見込(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_VALUE = 9;
	private static final int COL_IDX_EXPECTED_VALUE = 11;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 実績(初月)  */
	// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	private static final int COL_IDX_RECORD_VALUEYB = 13;
	// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月初計画：積上合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_STACK_TOTAL = 51;
	private static final int COL_IDX_PLANNED_STACK_TOTAL = 59;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月初計画：計画合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_VALUE_TOTAL = 53;
	private static final int COL_IDX_PLANNED_VALUE_TOTAL = 61;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：積上合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_STACK_TOTAL = 55;
	private static final int COL_IDX_EXPECTED_STACK_TOTAL = 63;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：見込合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_VALUE_TOTAL = 57;
	private static final int COL_IDX_EXPECTED_VALUE_TOTAL = 65;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PRINT_END = 58;
	private static final int COL_IDX_PRINT_END = 66;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** 列の最大インデックス */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int MAX_COL = 58;
	private static final int MAX_COL = 66;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** 1月毎の項目数 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int MONTH_ITEM_NUM = 8;
	private static final int MONTH_ITEM_NUM = 9;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * コンストラクタ
	 * @param templatePath テンプレートパス
	 * @param systemDate システム日付
	 * @param prodPlanScDto 検索条件
	 * @param resultDto 検索結果一覧
	 * @param sosMstDAO 組織マスタDAO
	 * @param codeMasterDao 汎用マスタDAO
	 * @param plannedProdDao 計画対象品目DAO
	 * @paarm jgiMstRealDao 従業員マスタDAO
	 * @param tougetuCount 当月カウント
	 */
	//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	//public ProdMonthPlanExportLogic(String templatePath, Date systemDate, ProdPlanScDto prodPlanScDto,
	//		ProdMonthPlanResultDto resultDto, SosMstRealDao sosMstDAO, CodeMasterDao codeMasterDao,
	//		MasterManagePlannedProdDao plannedProdDao, JgiMstRealDao jgiMstRealDao) {
	public ProdMonthPlanExportLogic(String templatePath, Date systemDate, ProdPlanScDto prodPlanScDto,
			ProdMonthPlanResultDto resultDto, SosMstRealDao sosMstDAO, CodeMasterDao codeMasterDao,
			MasterManagePlannedProdDao plannedProdDao, JgiMstRealDao jgiMstRealDao, int tougetuCount) {
     //mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.prodPlanScDto = prodPlanScDto;
		this.prodPlanResult = resultDto;
		this.sosMstDAO = sosMstDAO;
		this.codeMasterDao = codeMasterDao;
		this.plannedProdDao = plannedProdDao;
		this.jgiMstRealDao = jgiMstRealDao;
		this.orgMrNm = getOrgMrNm();
		this.outputFileName = createOutputFileName(systemDate);
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		this.tougetuCount = tougetuCount;
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	}

	/**
	 * 組織従業員の文字列を連結生成する
	 * @return
	 */
	private String getOrgMrNm() {
		// 組織・担当者名
		StringBuffer orgMrNm = new StringBuffer();

		try {
			if (prodPlanScDto.getJgiNo() != null) {

				JgiMst jgiMst = jgiMstRealDao.searchReal(prodPlanScDto.getJgiNo());

				// 担当者Noが設定されている場合、担当者名を取得
				orgMrNm.insert(0,  " " + jgiMst.getJgiName());
			}
			if (prodPlanScDto.getSosCd4() != null) {

				// 組織コード(担当者)が設定されている場合、担当者名を取得
				orgMrNm.insert(0, " " + sosMstDAO.searchReal(prodPlanScDto.getSosCd4()).getBumonSeiName());

			}
			if (prodPlanScDto.getSosCd3() != null) {

				// 組織コード(営業所)が設定されている場合、営業所名を取得
				orgMrNm.insert(0, " " + sosMstDAO.searchReal(prodPlanScDto.getSosCd3()).getBumonSeiName());

			}
			if (prodPlanScDto.getSosCd2() != null) {

				// 組織コード(支店)が設定されている場合、支店名を取得
				orgMrNm.insert(0, sosMstDAO.searchReal(prodPlanScDto.getSosCd2()).getBumonSeiName());

			} else {
				// 組織コードが設定されていない場合、「全社」
				orgMrNm.append(TEXT_ALL_COMPANY);
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "組織情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		return orgMrNm.toString();
	}

	/**
	 * 出力ファイル名の生成
	 *
	 * @param systemDate システム日付
	 * @return 出力ファイル名
	 * @throws UnsupportedEncodingException
	 */
	private String createOutputFileName(Date systemDate) {
		String fileNm = "";
		try {
			fileNm = URLEncoder.encode(OUTPUT_FILE_NAME_HEADER, "UTF-8")
					+ URLEncoder.encode(this.orgMrNm.replace(" ", "_"),"UTF-8")
					+ "_"
					+ DateUtil.toString(systemDate, "yyyyMMddHHmmss") + EXCEL_EXTENSION;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileNm;
	}

	/**
	 * 検索結果データのExcelファイルへの出力を実行する。
	 *
	 * @return 出力結果
	 */
	public ExportResult export() {
		FileInputStream fileIn = null;
		try {
			// テンプレートファイルの読込
			fileIn = new FileInputStream(templatePath);

			// テンプレートファイルへ書込
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
//			HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
			XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			write(workBook);

			// ファイル出力実装クラスの生成
			return new ExportResultExcelImpl(outputFileName, workBook);

		} catch (IOException e) {
			IOUtils.closeQuietly(fileIn);
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "テンプレートパスが存在しない"));
		} finally {
			IOUtils.closeQuietly(fileIn);
		}
	}

	/**
	 * ワークブックへ検索結果の書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private void write(HSSFWorkbook workBook) {
	private void write(XSSFWorkbook workBook) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);

		// ヘッダ情報のセット
		writeHeadInfo(poiBean);

		// リスト情報のセット
		writeListInfo(poiBean);
	}

	/**
	 * リスト情報の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo(POIBean poiBean) {
		// 結果リストがNull,または空の場合はReturn
		if (prodPlanResult == null || prodPlanResult.getDetailList() == null || prodPlanResult.getDetailList().isEmpty()) {
			return;
		}

		// 計画リスト
		List<ProdMonthPlanResultDetailDto> detailList = prodPlanResult.getDetailList();

		// 検索結果リストのサイズ分行を追加
		poiBean.addRows(ROW_IDX_START_LIST, detailList.size());

		// 品目分類名
		poiBean.setCellData(prodPlanResult.getProdCategory(), ROW_IDX_PLAN_SUM, COL_IDX_PROD_NAME);

		//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// 月初計画：積上の合計
		//Long plannedStackTotal = new Long(0);
		// 月初計画：計画の合計
		//Long plannedValTotal = new Long(0);
		// 月末見込：積上の合計
		//Long expectedStackTotal = new Long(0);
		// 月末見込：見込の合計
		//Long expectedValTotal = new Long(0);

		// 月初計画：積上の合計
		Double plannedStackTotal = new Double(0);
		// 月初計画：計画の合計
		Double plannedValTotal = new Double(0);
		// 月末見込：積上の合計
		Double expectedStackTotal = new Double(0);
		// 月末見込：見込の合計
		Double expectedValTotal = new Double(0);
		//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		int i = 0;
		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		// 明細行
		for (ProdMonthPlanResultDetailDto detail : detailList) {
			//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			// 月初計画：積上の合計
			//plannedStackTotal = new Long(0);
			// 月初計画：計画の合計
			//plannedValTotal = new Long(0);
			// 月末見込：積上の合計
			//expectedStackTotal = new Long(0);
			// 月末見込：見込の合計
			//expectedValTotal = new Long(0);

			// 月初計画：積上の合計
			plannedStackTotal = new Double(0);
			// 月初計画：計画の合計
			plannedValTotal = new Double(0);
			// 月末見込：積上の合計
			expectedStackTotal = new Double(0);
			// 月末見込：見込の合計
			expectedValTotal = new Double(0);
			//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

			// 品目名
			poiBean.setCellData(detail.getProdName(), rowIdx, COL_IDX_PROD_NAME);

			// 品目コード
			poiBean.setCellData(detail.getProdCode(), rowIdx, COL_IDX_PROD_CDDE);

			// 期別計画
			poiBean.setCellData(detail.getPlannedTermValue(), rowIdx, COL_IDX_TERM_PLAN);

			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 累計実績
			poiBean.setCellData(detail.getRecordTotalValueYb(), rowIdx, COL_IDX_TOTAL_VALUEYB);

			// 当月実績
			poiBean.setCellData(detail.getRecordTougetuValueYb(), rowIdx, COL_IDX_TOUGETU_VALUEYB);
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			i = 0;
			// 1月目
			// 月初計画：積上
			setMonthData(poiBean, i, rowIdx, detail.getPlannedStacked1Value(), detail.getPlanned1ValueYb(),
					detail.getExpectedStacked1Value(), detail.getExpected1ValueYb(), detail.getPlannedTermValue());
			// 合計値を算出
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if(i < tougetuCount){
			    //実績を合計
				if (detail.getRecord1ValueYb() != null) {
					plannedStackTotal = plannedStackTotal + detail.getRecord1ValueYb();
					plannedValTotal = plannedValTotal + detail.getRecord1ValueYb();
					expectedStackTotal = expectedStackTotal + detail.getRecord1ValueYb();
					expectedValTotal = expectedValTotal + detail.getRecord1ValueYb();
				}
			}else {
				//計画を合計
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
				if (detail.getPlannedStacked1Value() != null) {
					plannedStackTotal = plannedStackTotal + detail.getPlannedStacked1Value();
				}
				if (detail.getPlanned1ValueYb() != null) {
					plannedValTotal = plannedValTotal + detail.getPlanned1ValueYb();
				}
				if (detail.getExpectedStacked1Value() != null) {
					expectedStackTotal = expectedStackTotal + detail.getExpectedStacked1Value();
				}
				if (detail.getExpected1ValueYb() != null) {
					expectedValTotal = expectedValTotal + detail.getExpected1ValueYb();
				}
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			}
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 実績
			if (detail.getRecord1ValueYb() != null) {
				setRecordValueYbData(poiBean, i, rowIdx,  detail.getRecord1ValueYb());
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			i++;
			// 2月目
			setMonthData(poiBean, i, rowIdx, detail.getPlannedStacked2Value(), detail.getPlanned2ValueYb(),
					detail.getExpectedStacked2Value(), detail.getExpected2ValueYb(), detail.getPlannedTermValue());
			// 合計値を算出
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if(i < tougetuCount){
			    //実績を合計
				if (detail.getRecord2ValueYb() != null) {
					plannedStackTotal = plannedStackTotal + detail.getRecord2ValueYb();
					plannedValTotal = plannedValTotal + detail.getRecord2ValueYb();
					expectedStackTotal = expectedStackTotal + detail.getRecord2ValueYb();
					expectedValTotal = expectedValTotal + detail.getRecord2ValueYb();
				}
			}else {
				//計画を合計
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
				if (detail.getPlannedStacked2Value() != null) {
					plannedStackTotal = plannedStackTotal + detail.getPlannedStacked2Value();
				}
				if (detail.getPlanned2ValueYb() != null) {
					plannedValTotal = plannedValTotal + detail.getPlanned2ValueYb();
				}
				if (detail.getExpectedStacked2Value() != null) {
					expectedStackTotal = expectedStackTotal + detail.getExpectedStacked2Value();
				}
				if (detail.getExpected2ValueYb() != null) {
					expectedValTotal = expectedValTotal + detail.getExpected2ValueYb();
				}
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			}
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 実績
			if (detail.getRecord2ValueYb() != null) {
				setRecordValueYbData(poiBean, i, rowIdx,  detail.getRecord2ValueYb());
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			i++;
			// 3月目
			setMonthData(poiBean, i, rowIdx, detail.getPlannedStacked3Value(), detail.getPlanned3ValueYb(),
					detail.getExpectedStacked3Value(), detail.getExpected3ValueYb(), detail.getPlannedTermValue());
			// 合計値を算出
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if(i < tougetuCount){
			    //実績を合計
				if (detail.getRecord3ValueYb() != null) {
					plannedStackTotal = plannedStackTotal + detail.getRecord3ValueYb();
					plannedValTotal = plannedValTotal + detail.getRecord3ValueYb();
					expectedStackTotal = expectedStackTotal + detail.getRecord3ValueYb();
					expectedValTotal = expectedValTotal + detail.getRecord3ValueYb();
				}
			}else {
				//計画を合計
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
				if (detail.getPlannedStacked3Value() != null) {
					plannedStackTotal = plannedStackTotal + detail.getPlannedStacked3Value();
				}
				if (detail.getPlanned3ValueYb() != null) {
					plannedValTotal = plannedValTotal + detail.getPlanned3ValueYb();
				}
				if (detail.getExpectedStacked3Value() != null) {
					expectedStackTotal = expectedStackTotal + detail.getExpectedStacked3Value();
				}
				if (detail.getExpected3ValueYb() != null) {
					expectedValTotal = expectedValTotal + detail.getExpected3ValueYb();
				}
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			}
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 実績
			if (detail.getRecord3ValueYb() != null) {
				setRecordValueYbData(poiBean, i, rowIdx,  detail.getRecord3ValueYb());
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			i++;
			// 4月目
			setMonthData(poiBean, i, rowIdx, detail.getPlannedStacked4Value(), detail.getPlanned4ValueYb(),
					detail.getExpectedStacked4Value(), detail.getExpected4ValueYb(), detail.getPlannedTermValue());
			// 合計値を算出
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if(i < tougetuCount){
			    //実績を合計
				if (detail.getRecord4ValueYb() != null) {
					plannedStackTotal = plannedStackTotal + detail.getRecord4ValueYb();
					plannedValTotal = plannedValTotal + detail.getRecord4ValueYb();
					expectedStackTotal = expectedStackTotal + detail.getRecord4ValueYb();
					expectedValTotal = expectedValTotal + detail.getRecord4ValueYb();
				}
			}else {
				//計画を合計
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
				if (detail.getPlannedStacked4Value() != null) {
					plannedStackTotal = plannedStackTotal + detail.getPlannedStacked4Value();
				}
				if (detail.getPlanned4ValueYb() != null) {
					plannedValTotal = plannedValTotal + detail.getPlanned4ValueYb();
				}
				if (detail.getExpectedStacked4Value() != null) {
					expectedStackTotal = expectedStackTotal + detail.getExpectedStacked4Value();
				}
				if (detail.getExpected4ValueYb() != null) {
					expectedValTotal = expectedValTotal + detail.getExpected4ValueYb();
				}
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			}
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 実績
			if (detail.getRecord4ValueYb() != null) {
				setRecordValueYbData(poiBean, i, rowIdx,  detail.getRecord4ValueYb());
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			i++;
			// 5月目
			setMonthData(poiBean, i, rowIdx, detail.getPlannedStacked5Value(), detail.getPlanned5ValueYb(),
					detail.getExpectedStacked5Value(), detail.getExpected5ValueYb(), detail.getPlannedTermValue());
			// 合計値を算出
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if(i < tougetuCount){
			    //実績を合計
				if (detail.getRecord5ValueYb() != null) {
					plannedStackTotal = plannedStackTotal + detail.getRecord5ValueYb();
					plannedValTotal = plannedValTotal + detail.getRecord5ValueYb();
					expectedStackTotal = expectedStackTotal + detail.getRecord5ValueYb();
					expectedValTotal = expectedValTotal + detail.getRecord5ValueYb();
				}
			}else {
				//計画を合計
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
				if (detail.getPlannedStacked5Value() != null) {
					plannedStackTotal = plannedStackTotal + detail.getPlannedStacked5Value();
				}
				if (detail.getPlanned5ValueYb() != null) {
					plannedValTotal = plannedValTotal + detail.getPlanned5ValueYb();
				}
				if (detail.getExpectedStacked5Value() != null) {
					expectedStackTotal = expectedStackTotal + detail.getExpectedStacked5Value();
				}
				if (detail.getExpected5ValueYb() != null) {
					expectedValTotal = expectedValTotal + detail.getExpected5ValueYb();
				}
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			}
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 実績
			if (detail.getRecord5ValueYb() != null) {
				setRecordValueYbData(poiBean, i, rowIdx,  detail.getRecord5ValueYb());
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			i++;
			// 6月目
			setMonthData(poiBean, i, rowIdx, detail.getPlannedStacked6Value(), detail.getPlanned6ValueYb(),
					detail.getExpectedStacked6Value(), detail.getExpected6ValueYb(), detail.getPlannedTermValue());
			// 合計値を算出
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if(i < tougetuCount){
			    //実績を合計
				if (detail.getRecord6ValueYb() != null) {
					plannedStackTotal = plannedStackTotal + detail.getRecord6ValueYb();
					plannedValTotal = plannedValTotal + detail.getRecord6ValueYb();
					expectedStackTotal = expectedStackTotal + detail.getRecord6ValueYb();
					expectedValTotal = expectedValTotal + detail.getRecord6ValueYb();
				}
			}else {
				//計画を合計
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
				if (detail.getPlannedStacked6Value() != null) {
					plannedStackTotal = plannedStackTotal + detail.getPlannedStacked6Value();
				}
				if (detail.getPlanned6ValueYb() != null) {
					plannedValTotal = plannedValTotal + detail.getPlanned6ValueYb();
				}
				if (detail.getExpectedStacked6Value() != null) {
					expectedStackTotal = expectedStackTotal + detail.getExpectedStacked6Value();
				}
				if (detail.getExpected6ValueYb() != null) {
					expectedValTotal = expectedValTotal + detail.getExpected6ValueYb();
				}
			//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			}
			//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 実績
			if (detail.getRecord6ValueYb() != null) {
				setRecordValueYbData(poiBean, i, rowIdx,  detail.getRecord6ValueYb());
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 合計列の値を算出
			setTotalData(poiBean, rowIdx, plannedStackTotal, plannedValTotal, expectedStackTotal, expectedValTotal,
					detail.getPlannedTermValue());

			// インクリメント
			rowIdx++;
		}

		// 集計行の値を算出
		Long termValueTotal = new Long(0);
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		Double recordTotalValueYbTotal = new Double(0);
		Double recordTougetuValueYbTotal = new Double(0);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		Long plannedStack1Total = new Long(0);
		Long plannedValue1Total = new Long(0);
		Long expectedStack1Total = new Long(0);
		Long expectedValue1Total = new Long(0);
		Long plannedStack2Total = new Long(0);
		Long plannedValue2Total = new Long(0);
		Long expectedStack2Total = new Long(0);
		Long expectedValue2Total = new Long(0);
		Long plannedStack3Total = new Long(0);
		Long plannedValue3Total = new Long(0);
		Long expectedStack3Total = new Long(0);
		Long expectedValue3Total = new Long(0);
		Long plannedStack4Total = new Long(0);
		Long plannedValue4Total = new Long(0);
		Long expectedStack4Total = new Long(0);
		Long expectedValue4Total = new Long(0);
		Long plannedStack5Total = new Long(0);
		Long plannedValue5Total = new Long(0);
		Long expectedStack5Total = new Long(0);
		Long expectedValue5Total = new Long(0);
		Long plannedStack6Total = new Long(0);
		Long plannedValue6Total = new Long(0);
		Long expectedStack6Total = new Long(0);
		Long expectedValue6Total = new Long(0);
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 1月目：実績の合計
		Double record1ValueYbTotal = new Double(0);
		// 1月目：実績の合計
		Double record2ValueYbTotal = new Double(0);
		// 3月目：実績の合計
		Double record3ValueYbTotal = new Double(0);
		// 4月目：実績の合計
		Double record4ValueYbTotal = new Double(0);
		// 5月目：実績の合計
		Double record5ValueYbTotal = new Double(0);
		// 6月目：実績の合計
		Double record6ValueYbTotal = new Double(0);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		for (ProdMonthPlanResultDetailDto detail : detailList) {
			// 集計対象外の行はスキップ
			if ("0".equals(detail.getTargetSummary())) {
				continue;
			}
			// 期別計画
			if (detail.getPlannedTermValue() != null) {
				termValueTotal = termValueTotal + detail.getPlannedTermValue();
			}
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			if (detail.getRecordTotalValueYb() != null) {
				recordTotalValueYbTotal = recordTotalValueYbTotal + detail.getRecordTotalValueYb();
			}
			if (detail.getRecordTougetuValueYb() != null) {
				recordTougetuValueYbTotal = recordTougetuValueYbTotal + detail.getRecordTougetuValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 1月目 月初計画積上
			if (detail.getPlannedStacked1Value() != null) {
				plannedStack1Total = plannedStack1Total + detail.getPlannedStacked1Value();
			}
			// 1月目 月初計画値
			if (detail.getPlanned1ValueYb() != null) {
				plannedValue1Total = plannedValue1Total + detail.getPlanned1ValueYb();
			}
			// 1月目 月末見込積上
			if (detail.getExpectedStacked1Value() != null) {
				expectedStack1Total = expectedStack1Total + detail.getExpectedStacked1Value();
			}
			// 1月目 月末見込値
			if (detail.getExpected1ValueYb() != null) {
				expectedValue1Total = expectedValue1Total + detail.getExpected1ValueYb();
			}
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 1月目 実績
			if (detail.getRecord1ValueYb() != null) {
				record1ValueYbTotal = record1ValueYbTotal + detail.getRecord1ValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			// 2月目 月初計画積上
			if (detail.getPlannedStacked2Value() != null) {
				plannedStack2Total = plannedStack2Total + detail.getPlannedStacked2Value();
			}
			// 2月目 月初計画値
			if (detail.getPlanned2ValueYb() != null) {
				plannedValue2Total = plannedValue2Total + detail.getPlanned2ValueYb();
			}
			// 2月目 月末見込積上
			if (detail.getExpectedStacked2Value() != null) {
				expectedStack2Total = expectedStack2Total + detail.getExpectedStacked2Value();
			}
			// 2月目 月末見込値
			if (detail.getExpected2ValueYb() != null) {
				expectedValue2Total = expectedValue2Total + detail.getExpected2ValueYb();
			}
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 2月目 実績
			if (detail.getRecord2ValueYb() != null) {
				record2ValueYbTotal = record2ValueYbTotal + detail.getRecord2ValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			// 3月目 月初計画積上
			if (detail.getPlannedStacked3Value() != null) {
				plannedStack3Total = plannedStack3Total + detail.getPlannedStacked3Value();
			}
			// 3月目 月初計画値
			if (detail.getPlanned3ValueYb() != null) {
				plannedValue3Total = plannedValue3Total + detail.getPlanned3ValueYb();
			}
			// 3月目 月末見込積上
			if (detail.getExpectedStacked3Value() != null) {
				expectedStack3Total = expectedStack3Total + detail.getExpectedStacked3Value();
			}
			// 3月目 月末見込値
			if (detail.getExpected3ValueYb() != null) {
				expectedValue3Total = expectedValue3Total + detail.getExpected3ValueYb();
			}
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 3月目 実績
			if (detail.getRecord3ValueYb() != null) {
				record3ValueYbTotal = record3ValueYbTotal + detail.getRecord3ValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			// 4月目 月初計画積上
			if (detail.getPlannedStacked4Value() != null) {
				plannedStack4Total = plannedStack4Total + detail.getPlannedStacked4Value();
			}
			// 4月目 月初計画値
			if (detail.getPlanned4ValueYb() != null) {
				plannedValue4Total = plannedValue4Total + detail.getPlanned4ValueYb();
			}
			// 4月目 月末見込積上
			if (detail.getExpectedStacked4Value() != null) {
				expectedStack4Total = expectedStack4Total + detail.getExpectedStacked4Value();
			}
			// 4月目 月末見込値
			if (detail.getExpected4ValueYb() != null) {
				expectedValue4Total = expectedValue4Total + detail.getExpected4ValueYb();
			}
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 4月目 実績
			if (detail.getRecord4ValueYb() != null) {
				record4ValueYbTotal = record4ValueYbTotal + detail.getRecord4ValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			// 5月目 月初計画積上
			if (detail.getPlannedStacked5Value() != null) {
				plannedStack5Total = plannedStack5Total + detail.getPlannedStacked5Value();
			}
			// 5月目 月初計画値
			if (detail.getPlanned5ValueYb() != null) {
				plannedValue5Total = plannedValue5Total + detail.getPlanned5ValueYb();
			}
			// 5月目 月末見込積上
			if (detail.getExpectedStacked5Value() != null) {
				expectedStack5Total = expectedStack5Total + detail.getExpectedStacked5Value();
			}
			// 5月目 月末見込値
			if (detail.getExpected5ValueYb() != null) {
				expectedValue5Total = expectedValue5Total + detail.getExpected5ValueYb();
			}
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 5月目 実績
			if (detail.getRecord5ValueYb() != null) {
				record5ValueYbTotal = record5ValueYbTotal + detail.getRecord5ValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			// 6月目 月初計画積上
			if (detail.getPlannedStacked6Value() != null) {
				plannedStack6Total = plannedStack6Total + detail.getPlannedStacked6Value();
			}
			// 6月目 月初計画値
			if (detail.getPlanned6ValueYb() != null) {
				plannedValue6Total = plannedValue6Total + detail.getPlanned6ValueYb();
			}
			// 6月目 月末見込積上
			if (detail.getExpectedStacked6Value() != null) {
				expectedStack6Total = expectedStack6Total + detail.getExpectedStacked6Value();
			}
			// 6月目 月末見込値
			if (detail.getExpected6ValueYb() != null) {
				expectedValue6Total = expectedValue6Total + detail.getExpected6ValueYb();
			}
			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 6月目 実績
			if (detail.getRecord6ValueYb() != null) {
				record6ValueYbTotal = record6ValueYbTotal + detail.getRecord6ValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		}

		// 別カテゴリの値があれば、加算する
		ProdMonthPlanResultDetailDto otherPlan = prodPlanResult.getPlanSum();

		// 期別計画
		if (otherPlan.getPlannedTermValue() != null) {
			termValueTotal = termValueTotal + otherPlan.getPlannedTermValue();
		}

		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 累計実績
		if (otherPlan.getRecordTotalValueYb() != null) {
			recordTotalValueYbTotal = recordTotalValueYbTotal + otherPlan.getRecordTotalValueYb();
		}

		// 当月実績
		if (otherPlan.getRecordTougetuValueYb() != null) {
			recordTougetuValueYbTotal = recordTougetuValueYbTotal + otherPlan.getRecordTougetuValueYb();
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 1月目 月初計画積上
		if (otherPlan.getPlannedStacked1Value() != null) {
			plannedStack1Total = plannedStack1Total + otherPlan.getPlannedStacked1Value();
		}
		// 1月目 月初計画値
		if (otherPlan.getPlanned1ValueYb() != null) {
			plannedValue1Total = plannedValue1Total + otherPlan.getPlanned1ValueYb();
		}
		// 1月目 月末見込積上
		if (otherPlan.getExpectedStacked1Value() != null) {
			expectedStack1Total = expectedStack1Total + otherPlan.getExpectedStacked1Value();
		}
		// 1月目 月末見込値
		if (otherPlan.getExpected1ValueYb() != null) {
			expectedValue1Total = expectedValue1Total + otherPlan.getExpected1ValueYb();
		}
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		if (otherPlan.getRecord1ValueYb() != null) {
			record1ValueYbTotal = record1ValueYbTotal + otherPlan.getRecord1ValueYb();
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 2月目 月初計画積上
		if (otherPlan.getPlannedStacked2Value() != null) {
			plannedStack2Total = plannedStack2Total + otherPlan.getPlannedStacked2Value();
		}
		// 2月目 月初計画値
		if (otherPlan.getPlanned2ValueYb() != null) {
			plannedValue2Total = plannedValue2Total + otherPlan.getPlanned2ValueYb();
		}
		// 2月目 月末見込積上
		if (otherPlan.getExpectedStacked2Value() != null) {
			expectedStack2Total = expectedStack2Total + otherPlan.getExpectedStacked2Value();
		}
		// 2月目 月末見込値
		if (otherPlan.getExpected2ValueYb() != null) {
			expectedValue2Total = expectedValue2Total + otherPlan.getExpected2ValueYb();
		}
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		if (otherPlan.getRecord2ValueYb() != null) {
			record2ValueYbTotal = record2ValueYbTotal + otherPlan.getRecord2ValueYb();
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 3月目 月初計画積上
		if (otherPlan.getPlannedStacked3Value() != null) {
			plannedStack3Total = plannedStack3Total + otherPlan.getPlannedStacked3Value();
		}
		// 3月目 月初計画値
		if (otherPlan.getPlanned3ValueYb() != null) {
			plannedValue3Total = plannedValue3Total + otherPlan.getPlanned3ValueYb();
		}
		// 3月目 月末見込積上
		if (otherPlan.getExpectedStacked3Value() != null) {
			expectedStack3Total = expectedStack3Total + otherPlan.getExpectedStacked3Value();
		}
		// 3月目 月末見込値
		if (otherPlan.getExpected3ValueYb() != null) {
			expectedValue3Total = expectedValue3Total + otherPlan.getExpected3ValueYb();
		}
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		if (otherPlan.getRecord3ValueYb() != null) {
			record3ValueYbTotal = record3ValueYbTotal + otherPlan.getRecord3ValueYb();
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 4月目 月初計画積上
		if (otherPlan.getPlannedStacked4Value() != null) {
			plannedStack4Total = plannedStack4Total + otherPlan.getPlannedStacked4Value();
		}
		// 4月目 月初計画値
		if (otherPlan.getPlanned4ValueYb() != null) {
			plannedValue4Total = plannedValue4Total + otherPlan.getPlanned4ValueYb();
		}
		// 4月目 月末見込積上
		if (otherPlan.getExpectedStacked4Value() != null) {
			expectedStack4Total = expectedStack4Total + otherPlan.getExpectedStacked4Value();
		}
		// 4月目 月末見込値
		if (otherPlan.getExpected4ValueYb() != null) {
			expectedValue4Total = expectedValue4Total + otherPlan.getExpected4ValueYb();
		}
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		if (otherPlan.getRecord4ValueYb() != null) {
			record4ValueYbTotal = record4ValueYbTotal + otherPlan.getRecord4ValueYb();
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 5月目 月初計画積上
		if (otherPlan.getPlannedStacked5Value() != null) {
			plannedStack5Total = plannedStack5Total + otherPlan.getPlannedStacked5Value();
		}
		// 5月目 月初計画値
		if (otherPlan.getPlanned5ValueYb() != null) {
			plannedValue5Total = plannedValue5Total + otherPlan.getPlanned5ValueYb();
		}
		// 5月目 月末見込積上
		if (otherPlan.getExpectedStacked5Value() != null) {
			expectedStack5Total = expectedStack5Total + otherPlan.getExpectedStacked5Value();
		}
		// 5月目 月末見込値
		if (otherPlan.getExpected5ValueYb() != null) {
			expectedValue5Total = expectedValue5Total + otherPlan.getExpected5ValueYb();
		}
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		if (otherPlan.getRecord5ValueYb() != null) {
			record5ValueYbTotal = record5ValueYbTotal + otherPlan.getRecord5ValueYb();
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 6月目 月初計画積上
		if (otherPlan.getPlannedStacked6Value() != null) {
			plannedStack6Total = plannedStack6Total + otherPlan.getPlannedStacked6Value();
		}
		// 6月目 月初計画値
		if (otherPlan.getPlanned6ValueYb() != null) {
			plannedValue6Total = plannedValue6Total + otherPlan.getPlanned6ValueYb();
		}
		// 6月目 月末見込積上
		if (otherPlan.getExpectedStacked6Value() != null) {
			expectedStack6Total = expectedStack6Total + otherPlan.getExpectedStacked6Value();
		}
		// 6月目 月末見込値
		if (otherPlan.getExpected6ValueYb() != null) {
			expectedValue6Total = expectedValue6Total + otherPlan.getExpected6ValueYb();
		}
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		if (otherPlan.getRecord6ValueYb() != null) {
			record6ValueYbTotal = record6ValueYbTotal + otherPlan.getRecord6ValueYb();
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// 月初計画：積上の合計
		//plannedStackTotal = new Long(0);
		// 月初計画：計画の合計
		//plannedValTotal = new Long(0);
		// 月末見込：積上の合計
		//expectedStackTotal = new Long(0);
		// 月末見込：見込の合計
		//expectedValTotal = new Long(0);

		// 月初計画：積上の合計
		plannedStackTotal = new Double(0);
		// 月初計画：計画の合計
		plannedValTotal = new Double(0);
		// 月末見込：積上の合計
		expectedStackTotal = new Double(0);
		// 月末見込：見込の合計
		expectedValTotal = new Double(0);
		//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		// 集計行の合計列のを算出
		i = 0;
		// 1月目
		setMonthData(poiBean, i, ROW_IDX_PLAN_SUM, plannedStack1Total, plannedValue1Total,
				expectedStack1Total, expectedValue1Total, termValueTotal);
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (record1ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + record1ValueYbTotal;
				plannedValTotal = plannedValTotal + record1ValueYbTotal;
				expectedStackTotal = expectedStackTotal + record1ValueYbTotal;
				expectedValTotal = expectedValTotal + record1ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (plannedStack1Total != null) {
				plannedStackTotal = plannedStackTotal + plannedStack1Total;
			}
			if (plannedValue1Total != null) {
				plannedValTotal = plannedValTotal + plannedValue1Total;
			}
			if (expectedStack1Total != null) {
				expectedStackTotal = expectedStackTotal + expectedStack1Total;
			}
			if (expectedValue1Total != null) {
				expectedValTotal = expectedValTotal + expectedValue1Total;
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 実績
		setRecordValueYbData(poiBean, i, ROW_IDX_PLAN_SUM,  record1ValueYbTotal);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		i++;
		// 2月目
		setMonthData(poiBean, i, ROW_IDX_PLAN_SUM, plannedStack2Total, plannedValue2Total,
				expectedStack2Total, expectedValue2Total, termValueTotal);
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (record2ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + record2ValueYbTotal;
				plannedValTotal = plannedValTotal + record2ValueYbTotal;
				expectedStackTotal = expectedStackTotal + record2ValueYbTotal;
				expectedValTotal = expectedValTotal + record2ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (plannedStack2Total != null) {
				plannedStackTotal = plannedStackTotal + plannedStack2Total;
			}
			if (plannedValue2Total != null) {
				plannedValTotal = plannedValTotal + plannedValue2Total;
			}
			if (expectedStack2Total != null) {
				expectedStackTotal = expectedStackTotal + expectedStack2Total;
			}
			if (expectedValue2Total != null) {
				expectedValTotal = expectedValTotal + expectedValue2Total;
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 実績
		setRecordValueYbData(poiBean, i, ROW_IDX_PLAN_SUM,  record2ValueYbTotal);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		i++;
		// 3月目
		setMonthData(poiBean, i, ROW_IDX_PLAN_SUM, plannedStack3Total, plannedValue3Total,
				expectedStack3Total, expectedValue3Total, termValueTotal);
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (record3ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + record3ValueYbTotal;
				plannedValTotal = plannedValTotal + record3ValueYbTotal;
				expectedStackTotal = expectedStackTotal + record3ValueYbTotal;
				expectedValTotal = expectedValTotal + record3ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (plannedStack3Total != null) {
				plannedStackTotal = plannedStackTotal + plannedStack3Total;
			}
			if (plannedValue3Total != null) {
				plannedValTotal = plannedValTotal + plannedValue3Total;
			}
			if (expectedStack3Total != null) {
				expectedStackTotal = expectedStackTotal + expectedStack3Total;
			}
			if (expectedValue3Total != null) {
				expectedValTotal = expectedValTotal + expectedValue3Total;
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 実績
		setRecordValueYbData(poiBean, i, ROW_IDX_PLAN_SUM,  record3ValueYbTotal);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		i++;
		// 4月目
		setMonthData(poiBean, i, ROW_IDX_PLAN_SUM, plannedStack4Total, plannedValue4Total,
				expectedStack4Total, expectedValue4Total, termValueTotal);
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (record4ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + record4ValueYbTotal;
				plannedValTotal = plannedValTotal + record4ValueYbTotal;
				expectedStackTotal = expectedStackTotal + record4ValueYbTotal;
				expectedValTotal = expectedValTotal + record4ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (plannedStack4Total != null) {
				plannedStackTotal = plannedStackTotal + plannedStack4Total;
			}
			if (plannedValue4Total != null) {
				plannedValTotal = plannedValTotal + plannedValue4Total;
			}
			if (expectedStack4Total != null) {
				expectedStackTotal = expectedStackTotal + expectedStack4Total;
			}
			if (expectedValue4Total != null) {
				expectedValTotal = expectedValTotal + expectedValue4Total;
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 実績
		setRecordValueYbData(poiBean, i, ROW_IDX_PLAN_SUM,  record4ValueYbTotal);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		i++;
		// 5月目
		setMonthData(poiBean, i, ROW_IDX_PLAN_SUM, plannedStack5Total, plannedValue5Total,
				expectedStack5Total, expectedValue5Total, termValueTotal);
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (record5ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + record5ValueYbTotal;
				plannedValTotal = plannedValTotal + record5ValueYbTotal;
				expectedStackTotal = expectedStackTotal + record5ValueYbTotal;
				expectedValTotal = expectedValTotal + record5ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (plannedStack5Total != null) {
				plannedStackTotal = plannedStackTotal + plannedStack5Total;
			}
			if (plannedValue5Total != null) {
				plannedValTotal = plannedValTotal + plannedValue5Total;
			}
			if (expectedStack5Total != null) {
				expectedStackTotal = expectedStackTotal + expectedStack5Total;
			}
			if (expectedValue5Total != null) {
				expectedValTotal = expectedValTotal + expectedValue5Total;
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 実績
		setRecordValueYbData(poiBean, i, ROW_IDX_PLAN_SUM,  record5ValueYbTotal);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		i++;
		// 6月目
		setMonthData(poiBean, i, ROW_IDX_PLAN_SUM, plannedStack6Total, plannedValue6Total,
				expectedStack6Total, expectedValue6Total, termValueTotal);
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (record6ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + record6ValueYbTotal;
				plannedValTotal = plannedValTotal + record6ValueYbTotal;
				expectedStackTotal = expectedStackTotal + record6ValueYbTotal;
				expectedValTotal = expectedValTotal + record6ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (plannedStack6Total != null) {
				plannedStackTotal = plannedStackTotal + plannedStack6Total;
			}
			if (plannedValue6Total != null) {
				plannedValTotal = plannedValTotal + plannedValue6Total;
			}
			if (expectedStack6Total != null) {
				expectedStackTotal = expectedStackTotal + expectedStack6Total;
			}
			if (expectedValue6Total != null) {
				expectedValTotal = expectedValTotal + expectedValue6Total;
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 実績
		setRecordValueYbData(poiBean, i, ROW_IDX_PLAN_SUM,  record6ValueYbTotal);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 期別計画の値をセット
		poiBean.setCellData(termValueTotal, ROW_IDX_PLAN_SUM, COL_IDX_TERM_PLAN);

		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 累計実績
		poiBean.setCellData(recordTotalValueYbTotal, ROW_IDX_PLAN_SUM, COL_IDX_TOTAL_VALUEYB);

		// 当月実績
		poiBean.setCellData(recordTougetuValueYbTotal, ROW_IDX_PLAN_SUM, COL_IDX_TOUGETU_VALUEYB);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 合計列の値を算出
		setTotalData(poiBean, ROW_IDX_PLAN_SUM, plannedStackTotal, plannedValTotal, expectedStackTotal,
				expectedValTotal, termValueTotal);

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + detailList.size();
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth,
				fitHeigth);
	}

	/**
	 * ヘッダ情報の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeHeadInfo(POIBean poiBean) {
		// シート名
		poiBean.setSheetName(SHEET_NAME);
		// 表示条件
		poiBean.setCellData(createDisplayConditionTxt(), ROW_IDX_DISP_CONDITION, COL_IDX_DISP_CONDITION);
		// 現在日付
		poiBean.setCellData(systemDate, ROW_IDX_DATE, COL_IDX_DATE);
		// 年度・月の設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		if (sysManage != null) {
			String sysYear = sysManage.getSysYear();
			Term sysTerm = sysManage.getSysTerm();
			String yearTerm = "";
			int month = 0;
			if (Term.FIRST == sysTerm) {
				yearTerm = sysYear + "年上期";
			} else if (Term.SECOND == sysTerm) {
				yearTerm = sysYear + "年下期";
			}
			poiBean.setCellData(yearTerm, ROW_IDX_MONTH_HEAD, COL_IDX_TERM_PLAN);

			for (int count = 0; count < Month.PERIOD_MONTH; count++) {
				//上期
				if (Term.FIRST == sysTerm) {
					month = Term.FIRST_MONTH + count;
				}
				//下期
				else if (Term.SECOND == sysTerm) {
					month = Term.SECOND_MONTH + count;
				}
				if (month > Month.YEAR_MONTH) {
					month = month - Month.YEAR_MONTH;
				}
				// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
				//poiBean.setCellData(Integer.toString(month) + "月", ROW_IDX_MONTH_HEAD, (MONTH_ITEM_NUM * count) + 3);
				poiBean.setCellData(Integer.toString(month) + "月", ROW_IDX_MONTH_HEAD, (MONTH_ITEM_NUM * count) + 5);
				// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			}
		}
		// 選択カテゴリがワクチンかどうか
		boolean isVaccine;
		List<DpmCCdMst> codeList = new ArrayList<DpmCCdMst>();
		try {
			codeList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		if (codeList.size() > 1) {
			final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが複数登録されています。";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		}
		isVaccine = codeList.get(0).getDataCd().equals(prodPlanScDto.getProdCategory());
		// ワクチンの場合はB価を表示
		if (isVaccine) {
			// 項目名のヘッダ
			// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			//for (int i = 3; i < MAX_COL; i = i + 2) {
			//	if ((i - COL_IDX_PLANNED_STACK) % 8 == 0) {
			//		poiBean.setCellData(TEXT_STACK_B, ROW_IDX_NAME_HEAD, i);
			//	} else if ((i - COL_IDX_PLANNED_VALUE) % 8 == 0) {
			//		poiBean.setCellData(TEXT_PLAN_B, ROW_IDX_NAME_HEAD, i);
			//	} else if ((i - COL_IDX_EXPECTED_STACK) % 8 == 0) {
			//		poiBean.setCellData(TEXT_STACK_B, ROW_IDX_NAME_HEAD, i);
			//	} else if ((i - COL_IDX_EXPECTED_VALUE) % 8 == 0) {
			//		poiBean.setCellData(TEXT_EXPECT_B, ROW_IDX_NAME_HEAD, i);
			//	}
			//}
			for (int i = 5; i < MAX_COL; i = i + 2) {
				if ((i - COL_IDX_PLANNED_STACK) % 9 == 0) {
					poiBean.setCellData(TEXT_STACK_B, ROW_IDX_NAME_HEAD, i);
				} else if ((i - COL_IDX_PLANNED_VALUE) % 9 == 0) {
					poiBean.setCellData(TEXT_PLAN_B, ROW_IDX_NAME_HEAD, i);
				} else if ((i - COL_IDX_EXPECTED_STACK) % 9 == 0) {
					poiBean.setCellData(TEXT_STACK_B, ROW_IDX_NAME_HEAD, i);
				} else if ((i - COL_IDX_EXPECTED_VALUE) % 9 == 0) {
					poiBean.setCellData(TEXT_EXPECT_B, ROW_IDX_NAME_HEAD, i);
					i = i + 1;
				}
			}
			// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// ワクチンでない場合はY価を表示
		} else {
			// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			//for (int i = 3; i < MAX_COL; i = i + 2) {
			//	if ((i - COL_IDX_PLANNED_STACK) % 8 == 0) {
			//		poiBean.setCellData(TEXT_STACK_Y, ROW_IDX_NAME_HEAD, i);
			//	} else if ((i - COL_IDX_PLANNED_VALUE) % 8 == 0) {
			//		poiBean.setCellData(TEXT_PLAN_Y, ROW_IDX_NAME_HEAD, i);
			//	} else if ((i - COL_IDX_EXPECTED_STACK) % 8 == 0) {
			//		poiBean.setCellData(TEXT_STACK_Y, ROW_IDX_NAME_HEAD, i);
			//	} else if ((i - COL_IDX_EXPECTED_VALUE) % 8 == 0) {
			//		poiBean.setCellData(TEXT_EXPECT_Y, ROW_IDX_NAME_HEAD, i);
			//	}
			//}
			for (int i = 5; i < MAX_COL; i = i + 2) {
				if ((i - COL_IDX_PLANNED_STACK) % 9 == 0) {
					poiBean.setCellData(TEXT_STACK_Y, ROW_IDX_NAME_HEAD, i);
				} else if ((i - COL_IDX_PLANNED_VALUE) % 9 == 0) {
					poiBean.setCellData(TEXT_PLAN_Y, ROW_IDX_NAME_HEAD, i);
				} else if ((i - COL_IDX_EXPECTED_STACK) % 9 == 0) {
					poiBean.setCellData(TEXT_STACK_Y, ROW_IDX_NAME_HEAD, i);
				} else if ((i - COL_IDX_EXPECTED_VALUE) % 9 == 0) {
					poiBean.setCellData(TEXT_EXPECT_Y, ROW_IDX_NAME_HEAD, i);
					i = i + 1;
				}
			}
			// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		}
	}

	/**
	 * 表示条件の文字列を作成する
	 * @return 表示条件の文字列
	 */
	private String createDisplayConditionTxt() {

		StringBuffer txt = new StringBuffer();

		txt.append(TEXT_CONDITION);
		txt.append(TEXT_ORG_MR);
		txt.append(this.orgMrNm);

		// カテゴリ
		DpmCCdMst category = new DpmCCdMst();
		try {
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(),
					prodPlanScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + prodPlanScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		txt.append(" ").append(TEXT_CATEGORY).append(category.getDataName());

		return txt.toString();
	}

	/**
	 * 1ヶ月分のデータをPOIBeanに設定する
	 * @param poiBean POIBean
	 * @param i 初月から何月目
	 * @param rowIdx 行インデックス
	 * @param plannedStack 月初計画：積上
	 * @param plannedVal 月初計画：計画
	 * @param expectedStack 月末見込：積上
	 * @param expectedVal 月末見込：見込
	 * @param termValue 期別計画
	 */
	private void setMonthData(POIBean poiBean, int i, int rowIdx, Long plannedStack, Long plannedVal,
			Long expectedStack, Long expectedVal, Long termValue) {
		// 月別計画：積上
		poiBean.setCellData(plannedStack, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_PLANNED_STACK);
		// 遂行率
		if (termValue != null && termValue != 0) {
			if (plannedStack != null) {
				poiBean.setCellData(
						// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						//(((double) Math.round((double) plannedStack / termValue * 1000)) / 10),
						(((double) plannedStack / termValue * 1000) / 10),
						// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_PLANNED_STACK + 1);
			} else {
				poiBean.setCellData(0.0, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_PLANNED_STACK + 1);
			}
		}
		// 月初計画：計画
		poiBean.setCellData(plannedVal, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_PLANNED_VALUE);
		// 遂行率
		if (termValue != null && termValue != 0) {
			if (plannedVal != null) {
				poiBean.setCellData(
						// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						//(((double) Math.round((double) plannedVal / termValue * 1000)) / 10),
						(((double) plannedVal / termValue * 1000) / 10),
						// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_PLANNED_VALUE + 1);
			} else {
				poiBean.setCellData(0.0, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_PLANNED_VALUE + 1);
			}
		}
		// 月末見込：積上
		poiBean.setCellData(expectedStack, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_EXPECTED_STACK);
		// 遂行率
		if (termValue != null && termValue != 0) {
			if (expectedStack != null) {
				poiBean.setCellData(
						// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						//(((double) Math.round((double) expectedStack / termValue * 1000)) / 10),
						(((double) expectedStack / termValue * 1000) / 10),
						// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_EXPECTED_STACK + 1);
			} else {
				poiBean.setCellData(0.0, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_EXPECTED_STACK + 1);
			}
		}
		// 月末見込：見込
		poiBean.setCellData(expectedVal, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_EXPECTED_VALUE);
		// 遂行率
		if (termValue != null && termValue != 0) {
			if (expectedVal != null) {
				poiBean.setCellData(
						// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						//(((double) Math.round((double) expectedVal / termValue * 1000)) / 10),
						(((double) expectedVal / termValue * 1000) / 10),
						// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
						rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_EXPECTED_VALUE + 1);
			} else {
				poiBean.setCellData(0.0, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_EXPECTED_VALUE + 1);
			}
		}
	}

	// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 * 実績のデータをPOIBeanに設定する
	 * @param poiBean POIBean
	 * @param i 初月から何月目
	 * @param rowIdx 行インデックス
	 * @param recordValueYb 実績
	 */
	private void setRecordValueYbData(POIBean poiBean, int i, int rowIdx,  Double recordValueYb) {

		// 実績
		poiBean.setCellData(recordValueYb, rowIdx, (MONTH_ITEM_NUM * i) + COL_IDX_RECORD_VALUEYB);

	}
	// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示


	/**
	 * 合計のデータをPOIBeanに設定する
	 * @param poiBean POIBean
	 * @param rowIdx 行インデックス
	 * @param plannedStackTotal 月初計画：積上の合計
	 * @param plannedValTotal 月初計画：計画の合計
	 * @param expectedStackTotal 月末見込：積上の合計
	 * @param expectedValTotal 月末見込：計画の合計
	 * @param termValue 期別計画
	 */
	//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	//private void setTotalData(POIBean poiBean, int rowIdx, Long plannedStackTotal, Long plannedValTotal,
	//		Long expectedStackTotal,
	//		Long expectedValTotal, Long termValue) {
	private void setTotalData(POIBean poiBean, int rowIdx, Double plannedStackTotal, Double plannedValTotal,
			Double expectedStackTotal,
			Double expectedValTotal, Long termValue) {
	//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// 合計値と遂行率を設定
		// 月初計画：積上
		poiBean.setCellData(plannedStackTotal, rowIdx, COL_IDX_PLANNED_STACK_TOTAL);
		// 遂行率
		if (termValue != null && termValue != 0) {
			poiBean.setCellData(
					// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					//(((double) Math.round((double) plannedStackTotal / termValue * 1000)) / 10),
					(((double) plannedStackTotal / termValue * 1000) / 10),
					// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					rowIdx, COL_IDX_PLANNED_STACK_TOTAL + 1);
		}

		// 月初計画：計画
		poiBean.setCellData(plannedValTotal, rowIdx, COL_IDX_PLANNED_VALUE_TOTAL);
		// 遂行率
		if (termValue != null && termValue != 0) {
			poiBean.setCellData(
					// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					//(((double) Math.round((double) plannedValTotal / termValue * 1000)) / 10),
					(((double) plannedValTotal / termValue * 1000) / 10),
					// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					rowIdx, COL_IDX_PLANNED_VALUE_TOTAL + 1);
		}

		// 月末見込：積上
		poiBean.setCellData(expectedStackTotal, rowIdx, COL_IDX_EXPECTED_STACK_TOTAL);
		// 遂行率
		if (termValue != null && termValue != 0) {
			poiBean.setCellData(
					// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					//(((double) Math.round((double) expectedStackTotal / termValue * 1000)) / 10),
					(((double) expectedStackTotal / termValue * 1000) / 10),
					// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					rowIdx, COL_IDX_EXPECTED_STACK_TOTAL + 1);
		}

		// 月末見込：見込
		poiBean.setCellData(expectedValTotal, rowIdx, COL_IDX_EXPECTED_VALUE_TOTAL);
		// 遂行率
		if (termValue != null && termValue != 0) {
			poiBean.setCellData(
					// mod Start 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					//(((double) Math.round((double) expectedValTotal / termValue * 1000)) / 10),
					(((double) expectedValTotal / termValue * 1000) / 10),
					// mod End 2022/11/16 H.futagami No.8　計画管理の月別計画に納入実績の値を表示
					rowIdx, COL_IDX_EXPECTED_VALUE_TOTAL + 1);
		}
	}
}
