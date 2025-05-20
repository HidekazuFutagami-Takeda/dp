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
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dto.SosMonthPlanResultDetailDto;
import jp.co.takeda.dto.SosMonthPlanResultDetailTotalDto;
import jp.co.takeda.dto.SosMonthPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.Month;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 組織別月別計画一覧のExcelファイルを生成するロジッククラス
 *
 * @author rna8405
 */
public class SosMonthPlanExportLogic {

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
	private final SosPlanScDto sosPlanScDto;

	/**
	 * 組織別月別計画の検索結果
	 */
	private SosMonthPlanResultDto sosPlanResult;

	/**
	 * 上位計画合計列のヘッダ名
	 */
	private String upperPlanTotalHeader;

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
	private static final String OUTPUT_FILE_NAME_HEADER = "組織・担当者別月別計画_";

	/** シート名 */
	private static final String SHEET_NAME = "組織・担当者別月別計画一覧";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「組織・担当者」 */
	private static final String TEXT_ORG_MR = "組織・担当者：";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ：";

	/** 文字列「品目」 */
	private static final String TEXT_PROD = "品目：";

	/** 文字列「全社計画」 */
	private static final String TEXT_COMPANY_PLAN = "全社計画";

	/** 文字列「リージョン計画」 */
	private static final String TEXT_BRANCH_PLAN = "リージョン計画";

	/** 文字列「エリア計画」 */
	private static final String TEXT_OFFICE_PLAN = "エリア計画";

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
	//private static final int COL_IDX_DATE = 57;
	private static final int COL_IDX_DATE = 65;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [行番号] 月表示ヘッダ */
	private static final int ROW_IDX_MONTH_HEAD = 2;

	/** [行番号] 項目名表示ヘッダ */
	private static final int ROW_IDX_NAME_HEAD = 4;

	/** [行番号] 上位計画行 */
	private static final int ROW_IDX_UPPER_PLAN = 5;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 6;

	/** [列番号] 組織・担当名称  */
	private static final int COL_IDX_ORG_MR_NM = 0;

	/** [列番号] 期別計画  */
	private static final int COL_IDX_TERM_PLAN = 1;

    // add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を
	/** [列番号] 累計実績  */
	private static final int COL_IDX_TOTAL_VALUEYB = 2;

	/** [列番号] 当月実績  */
	private static final int COL_IDX_TOUGETU_VALUEYB = 3;
    // add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を

	/** [列番号] 月初計画：積上(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_STACK = 2;
	private static final int COL_IDX_PLANNED_STACK = 4;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月初計画：計画(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_VALUE = 4;
	private static final int COL_IDX_PLANNED_VALUE = 6;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：積上(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_STACK = 6;
	private static final int COL_IDX_EXPECTED_STACK = 8;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：見込(初月)  */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_VALUE = 8;
	private static final int COL_IDX_EXPECTED_VALUE = 10;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 実績(初月)  */
	// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	private static final int COL_IDX_RECORD_VALUEYB = 12;
	// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月初計画：積上合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_STACK_TOTAL = 50;
	private static final int COL_IDX_PLANNED_STACK_TOTAL = 58;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月初計画：計画合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PLANNED_VALUE_TOTAL = 52;
	private static final int COL_IDX_PLANNED_VALUE_TOTAL = 60;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：積上合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_STACK_TOTAL = 54;
	private static final int COL_IDX_EXPECTED_STACK_TOTAL = 62;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [列番号] 月末見込：見込合計 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_EXPECTED_VALUE_TOTAL = 56;
	private static final int COL_IDX_EXPECTED_VALUE_TOTAL = 64;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int COL_IDX_PRINT_END = 57;
	private static final int COL_IDX_PRINT_END = 65;
	// mod End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

	/** 列の最大インデックス */
	// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
	//private static final int MAX_COL = 57;
	private static final int MAX_COL = 65;
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
	 * @param sosPlanScDto 検索条件
	 * @param resultDto 検索結果一覧
	 * @param sosMstDAO 組織マスタDAO
	 * @param codeMasterDao 汎用マスタDAO
	 * @param plannedProdDao 計画対象品目DAO
	 */
	//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	//public SosMonthPlanExportLogic(String templatePath, Date systemDate, SosPlanScDto sosPlanScDto,
	//		SosMonthPlanResultDto resultDto, SosMstRealDao sosMstDAO, CodeMasterDao codeMasterDao,
	//		MasterManagePlannedProdDao plannedProdDao) {
	public SosMonthPlanExportLogic(String templatePath, Date systemDate, SosPlanScDto sosPlanScDto,
			SosMonthPlanResultDto resultDto, SosMstRealDao sosMstDAO, CodeMasterDao codeMasterDao,
			MasterManagePlannedProdDao plannedProdDao, int tougetuCount) {
	//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.sosPlanScDto = sosPlanScDto;
		this.sosPlanResult = resultDto;
		this.sosMstDAO = sosMstDAO;
		this.codeMasterDao = codeMasterDao;
		this.plannedProdDao = plannedProdDao;
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
			if (sosPlanScDto.getSosCd4() != null) {

				// 組織コード(担当者)が設定されている場合、担当者名を取得
				orgMrNm.insert(0, " " + sosMstDAO.searchReal(sosPlanScDto.getSosCd4()).getBumonSeiName());

			}
			if (sosPlanScDto.getSosCd3() != null) {

				// 組織コード(営業所)が設定されている場合、営業所名を取得
				orgMrNm.insert(0, " " + sosMstDAO.searchReal(sosPlanScDto.getSosCd3()).getBumonSeiName());

			}
			if (sosPlanScDto.getSosCd2() != null) {

				// 組織コード(支店)が設定されている場合、支店名を取得
				orgMrNm.insert(0, sosMstDAO.searchReal(sosPlanScDto.getSosCd2()).getBumonSeiName());

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
		if (sosPlanResult == null || sosPlanResult.getDetailList() == null || sosPlanResult.getDetailList().isEmpty()) {
			return;
		}

		// 上位計画
		SosMonthPlanResultDetailTotalDto upperList = sosPlanResult.getDetailTotal();

		// 下位計画リスト
		List<SosMonthPlanResultDetailDto> detailList = sosPlanResult.getDetailList();

		// 検索結果リストのサイズ分行を追加
		poiBean.addRows(ROW_IDX_START_LIST, detailList.size());

		// 上位計画項目をセルへセット
		// 組織・担当名称
		poiBean.setCellData(upperPlanTotalHeader, ROW_IDX_UPPER_PLAN, COL_IDX_ORG_MR_NM);
		// 期別計画
		poiBean.setCellData(upperList.getPlannedTermValue(), ROW_IDX_UPPER_PLAN, COL_IDX_TERM_PLAN);

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
		//del Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		//// 1月目
		//// 月初計画：積上
		//setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked1Value(),
		//		upperList.getPlanned1ValueYb(),
		//		upperList.getExpectedStacked1Value(), upperList.getExpected1ValueYb(), upperList.getPlannedTermValue());        //
		//// 合計値を算出
		//if (upperList.getPlannedStacked1Value() != null) {
		//	plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked1Value();
		//}
		//if (upperList.getPlanned1ValueYb() != null) {
		//	plannedValTotal = plannedValTotal + upperList.getPlanned1ValueYb();
		//}
		//if (upperList.getExpectedStacked1Value() != null) {
		//	expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked1Value();
		//}
		//if (upperList.getExpected1ValueYb() != null) {
		//	expectedValTotal = expectedValTotal + upperList.getExpected1ValueYb();
		//}
        //
		//i++;
		//// 2月目
		//setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked2Value(),
		//		upperList.getPlanned2ValueYb(),
		//		upperList.getExpectedStacked2Value(), upperList.getExpected2ValueYb(), upperList.getPlannedTermValue());        //
		//// 合計値を算出
		//if (upperList.getPlannedStacked2Value() != null) {
		//	plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked2Value();
		//}
		//if (upperList.getPlanned2ValueYb() != null) {
		//	plannedValTotal = plannedValTotal + upperList.getPlanned2ValueYb();
		//}
		//if (upperList.getExpectedStacked2Value() != null) {
		//	expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked2Value();
		//}
		//if (upperList.getExpected2ValueYb() != null) {
		//	expectedValTotal = expectedValTotal + upperList.getExpected2ValueYb();
		//}
        //
		//i++;
		//// 3月目
		//setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked3Value(),
		//		upperList.getPlanned3ValueYb(),
		//		upperList.getExpectedStacked3Value(), upperList.getExpected3ValueYb(), upperList.getPlannedTermValue());        //
		//// 合計値を算出
		//if (upperList.getPlannedStacked3Value() != null) {
		//	plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked3Value();
		//}
		//if (upperList.getPlanned3ValueYb() != null) {
		//	plannedValTotal = plannedValTotal + upperList.getPlanned3ValueYb();
		//}
		//if (upperList.getExpectedStacked3Value() != null) {
		//	expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked3Value();
		//}
		//if (upperList.getExpected3ValueYb() != null) {
		//	expectedValTotal = expectedValTotal + upperList.getExpected3ValueYb();
		//}
        //
		//i++;
		//// 4月目
		//setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked4Value(),
		//		upperList.getPlanned4ValueYb(),
		//		upperList.getExpectedStacked4Value(), upperList.getExpected4ValueYb(), upperList.getPlannedTermValue());        //
		//// 合計値を算出
		//if (upperList.getPlannedStacked4Value() != null) {
		//	plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked4Value();
		//}
		//if (upperList.getPlanned4ValueYb() != null) {
		//	plannedValTotal = plannedValTotal + upperList.getPlanned4ValueYb();
		//}
		//if (upperList.getExpectedStacked4Value() != null) {
		//	expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked4Value();
		//}
		//if (upperList.getExpected4ValueYb() != null) {
		//	expectedValTotal = expectedValTotal + upperList.getExpected4ValueYb();
		//}
        //
		//i++;
		//// 5月目
		//setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked5Value(),
		//		upperList.getPlanned5ValueYb(),
		//		upperList.getExpectedStacked5Value(), upperList.getExpected5ValueYb(), upperList.getPlannedTermValue());        //
		//// 合計値を算出
		//if (upperList.getPlannedStacked5Value() != null) {
		//	plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked5Value();
		//}
		//if (upperList.getPlanned5ValueYb() != null) {
		//	plannedValTotal = plannedValTotal + upperList.getPlanned5ValueYb();
		//}
		//if (upperList.getExpectedStacked5Value() != null) {
		//	expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked5Value();
		//}
		//if (upperList.getExpected5ValueYb() != null) {
		//	expectedValTotal = expectedValTotal + upperList.getExpected5ValueYb();
		//}
        //
		//i++;
		//// 6月目
		//setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked6Value(),
		//		upperList.getPlanned6ValueYb(),
		//		upperList.getExpectedStacked6Value(), upperList.getExpected6ValueYb(), upperList.getPlannedTermValue());        //
		//// 合計値を算出
		//if (upperList.getPlannedStacked6Value() != null) {
		//	plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked6Value();
		//}
		//if (upperList.getPlanned6ValueYb() != null) {
		//	plannedValTotal = plannedValTotal + upperList.getPlanned6ValueYb();
		//}
		//if (upperList.getExpectedStacked6Value() != null) {
		//	expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked6Value();
		//}
		//if (upperList.getExpected6ValueYb() != null) {
		//	expectedValTotal = expectedValTotal + upperList.getExpected6ValueYb();
		//}
        //
		//// 合計列の値を算出
		//setTotalData(poiBean, ROW_IDX_UPPER_PLAN, plannedStackTotal, plannedValTotal, expectedStackTotal,
		//		expectedValTotal, upperList.getPlannedTermValue());
        //del End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 累計実績の合計
		Double TotalValueYbTotal = new Double(0);
		// 当月実績の合計
		Double TougetuValueYbTotal = new Double(0);
		// 1月目実績の合計
		Double month1ValueYbTotal = new Double(0);
		// 2月目実績の合計
		Double month2ValueYbTotal = new Double(0);
		// 3月目実績の合計
		Double month3ValueYbTotal = new Double(0);
		// 4月目実績の合計
		Double month4ValueYbTotal = new Double(0);
		// 5月目実績の合計
		Double month5ValueYbTotal = new Double(0);
		// 6月目実績の合計
		Double month6ValueYbTotal = new Double(0);
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

		// 明細行
		for (SosMonthPlanResultDetailDto detail : detailList) {
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

			// 組織・担当名称
			if (StringUtils.isNotBlank(detail.getJgiName())) {
				// 担当名称
				poiBean.setCellData(detail.getJgiName(), rowIdx, COL_IDX_ORG_MR_NM);
			} else {
				// 組織名称
				poiBean.setCellData(detail.getSosName(), rowIdx, COL_IDX_ORG_MR_NM);
			}

			// 期別計画
			poiBean.setCellData(detail.getPlannedTermValue(), rowIdx, COL_IDX_TERM_PLAN);


			// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			// 累計実績
			poiBean.setCellData(detail.getRecordTotalValueYb(), rowIdx, COL_IDX_TOTAL_VALUEYB);
			if (detail.getRecordTotalValueYb() != null) {
				TotalValueYbTotal = TotalValueYbTotal + detail.getRecordTotalValueYb();
			}

			// 当月実績
			poiBean.setCellData(detail.getRecordTougetuValueYb(), rowIdx, COL_IDX_TOUGETU_VALUEYB);
			if (detail.getRecordTougetuValueYb() != null) {
				TougetuValueYbTotal = TougetuValueYbTotal + detail.getRecordTougetuValueYb();
			}
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
				month1ValueYbTotal = month1ValueYbTotal + detail.getRecord1ValueYb();
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
				month2ValueYbTotal = month2ValueYbTotal + detail.getRecord2ValueYb();
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
				month3ValueYbTotal = month3ValueYbTotal + detail.getRecord3ValueYb();
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
				month4ValueYbTotal = month4ValueYbTotal + detail.getRecord4ValueYb();
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
				month5ValueYbTotal = month5ValueYbTotal + detail.getRecord5ValueYb();
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
				month6ValueYbTotal = month6ValueYbTotal + detail.getRecord6ValueYb();
			}
			// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

			// 合計列の値を算出
			setTotalData(poiBean, rowIdx, plannedStackTotal, plannedValTotal, expectedStackTotal, expectedValTotal,
					detail.getPlannedTermValue());

			// インクリメント
			rowIdx++;
		}

		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		i = 0;

		// 月初計画：積上の合計
		plannedStackTotal = new Double(0);
		// 月初計画：計画の合計
		plannedValTotal = new Double(0);
		// 月末見込：積上の合計
		expectedStackTotal = new Double(0);
		// 月末見込：見込の合計
		expectedValTotal = new Double(0);

		// 1月目
		// 月初計画：積上
		setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked1Value(),
				upperList.getPlanned1ValueYb(),
				upperList.getExpectedStacked1Value(), upperList.getExpected1ValueYb(), upperList.getPlannedTermValue());
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (month1ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + month1ValueYbTotal;
				plannedValTotal = plannedValTotal + month1ValueYbTotal;
				expectedStackTotal = expectedStackTotal + month1ValueYbTotal;
				expectedValTotal = expectedValTotal + month1ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (upperList.getPlannedStacked1Value() != null) {
				plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked1Value();
			}
			if (upperList.getPlanned1ValueYb() != null) {
				plannedValTotal = plannedValTotal + upperList.getPlanned1ValueYb();
			}
			if (upperList.getExpectedStacked1Value() != null) {
				expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked1Value();
			}
			if (upperList.getExpected1ValueYb() != null) {
				expectedValTotal = expectedValTotal + upperList.getExpected1ValueYb();
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		i++;
		// 2月目
		setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked2Value(),
				upperList.getPlanned2ValueYb(),
				upperList.getExpectedStacked2Value(), upperList.getExpected2ValueYb(), upperList.getPlannedTermValue());
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (month2ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + month2ValueYbTotal;
				plannedValTotal = plannedValTotal + month2ValueYbTotal;
				expectedStackTotal = expectedStackTotal + month2ValueYbTotal;
				expectedValTotal = expectedValTotal + month2ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (upperList.getPlannedStacked2Value() != null) {
				plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked2Value();
			}
			if (upperList.getPlanned2ValueYb() != null) {
				plannedValTotal = plannedValTotal + upperList.getPlanned2ValueYb();
			}
			if (upperList.getExpectedStacked2Value() != null) {
				expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked2Value();
			}
			if (upperList.getExpected2ValueYb() != null) {
				expectedValTotal = expectedValTotal + upperList.getExpected2ValueYb();
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		i++;
		// 3月目
		setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked3Value(),
				upperList.getPlanned3ValueYb(),
				upperList.getExpectedStacked3Value(), upperList.getExpected3ValueYb(), upperList.getPlannedTermValue());
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (month3ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + month3ValueYbTotal;
				plannedValTotal = plannedValTotal + month3ValueYbTotal;
				expectedStackTotal = expectedStackTotal + month3ValueYbTotal;
				expectedValTotal = expectedValTotal + month3ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (upperList.getPlannedStacked3Value() != null) {
				plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked3Value();
			}
			if (upperList.getPlanned3ValueYb() != null) {
				plannedValTotal = plannedValTotal + upperList.getPlanned3ValueYb();
			}
			if (upperList.getExpectedStacked3Value() != null) {
				expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked3Value();
			}
			if (upperList.getExpected3ValueYb() != null) {
				expectedValTotal = expectedValTotal + upperList.getExpected3ValueYb();
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		i++;
		// 4月目
		setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked4Value(),
				upperList.getPlanned4ValueYb(),
				upperList.getExpectedStacked4Value(), upperList.getExpected4ValueYb(), upperList.getPlannedTermValue());
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (month4ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + month4ValueYbTotal;
				plannedValTotal = plannedValTotal + month4ValueYbTotal;
				expectedStackTotal = expectedStackTotal + month4ValueYbTotal;
				expectedValTotal = expectedValTotal + month4ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (upperList.getPlannedStacked4Value() != null) {
				plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked4Value();
			}
			if (upperList.getPlanned4ValueYb() != null) {
				plannedValTotal = plannedValTotal + upperList.getPlanned4ValueYb();
			}
			if (upperList.getExpectedStacked4Value() != null) {
				expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked4Value();
			}
			if (upperList.getExpected4ValueYb() != null) {
				expectedValTotal = expectedValTotal + upperList.getExpected4ValueYb();
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		i++;
		// 5月目
		setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked5Value(),
				upperList.getPlanned5ValueYb(),
				upperList.getExpectedStacked5Value(), upperList.getExpected5ValueYb(), upperList.getPlannedTermValue());
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (month5ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + month5ValueYbTotal;
				plannedValTotal = plannedValTotal + month5ValueYbTotal;
				expectedStackTotal = expectedStackTotal + month5ValueYbTotal;
				expectedValTotal = expectedValTotal + month5ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (upperList.getPlannedStacked5Value() != null) {
				plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked5Value();
			}
			if (upperList.getPlanned5ValueYb() != null) {
				plannedValTotal = plannedValTotal + upperList.getPlanned5ValueYb();
			}
			if (upperList.getExpectedStacked5Value() != null) {
				expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked5Value();
			}
			if (upperList.getExpected5ValueYb() != null) {
				expectedValTotal = expectedValTotal + upperList.getExpected5ValueYb();
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		i++;
		// 6月目
		setMonthData(poiBean, i, ROW_IDX_UPPER_PLAN, upperList.getPlannedStacked6Value(),
				upperList.getPlanned6ValueYb(),
				upperList.getExpectedStacked6Value(), upperList.getExpected6ValueYb(), upperList.getPlannedTermValue());
		// 合計値を算出
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		if(i < tougetuCount){
		    //実績を合計
			if (month6ValueYbTotal != null) {
				plannedStackTotal = plannedStackTotal + month6ValueYbTotal;
				plannedValTotal = plannedValTotal + month6ValueYbTotal;
				expectedStackTotal = expectedStackTotal + month6ValueYbTotal;
				expectedValTotal = expectedValTotal + month6ValueYbTotal;
			}
		}else {
			//計画を合計
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
			if (upperList.getPlannedStacked6Value() != null) {
				plannedStackTotal = plannedStackTotal + upperList.getPlannedStacked6Value();
			}
			if (upperList.getPlanned6ValueYb() != null) {
				plannedValTotal = plannedValTotal + upperList.getPlanned6ValueYb();
			}
			if (upperList.getExpectedStacked6Value() != null) {
				expectedStackTotal = expectedStackTotal + upperList.getExpectedStacked6Value();
			}
			if (upperList.getExpected6ValueYb() != null) {
				expectedValTotal = expectedValTotal + upperList.getExpected6ValueYb();
			}
		//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		}
		//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		// 合計列の値を算出
		setTotalData(poiBean, ROW_IDX_UPPER_PLAN, plannedStackTotal, plannedValTotal, expectedStackTotal,
				expectedValTotal, upperList.getPlannedTermValue());
        //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		// add Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
		// 累計実績
		poiBean.setCellData(TotalValueYbTotal, ROW_IDX_UPPER_PLAN, COL_IDX_TOTAL_VALUEYB);
		// 当月実績
		poiBean.setCellData(TougetuValueYbTotal, ROW_IDX_UPPER_PLAN, COL_IDX_TOUGETU_VALUEYB);
		// 1月目実績
		if (month1ValueYbTotal != null) {
			setRecordValueYbData(poiBean, 0, ROW_IDX_UPPER_PLAN,  month1ValueYbTotal);
		}
		// 2月目実績
		if (month2ValueYbTotal != null) {
			setRecordValueYbData(poiBean, 1, ROW_IDX_UPPER_PLAN,  month2ValueYbTotal);
		}
		// 3月目実績
		if (month3ValueYbTotal != null) {
			setRecordValueYbData(poiBean, 2, ROW_IDX_UPPER_PLAN,  month3ValueYbTotal);
		}
		// 4月目実績
		if (month4ValueYbTotal != null) {
			setRecordValueYbData(poiBean, 3, ROW_IDX_UPPER_PLAN,  month4ValueYbTotal);
		}
		// 5月目実績
		if (month5ValueYbTotal != null) {
			setRecordValueYbData(poiBean, 4, ROW_IDX_UPPER_PLAN,  month5ValueYbTotal);
		}
		// 6月目実績
		if (month6ValueYbTotal != null) {
			setRecordValueYbData(poiBean, 5, ROW_IDX_UPPER_PLAN,  month6ValueYbTotal);
		}
		// add End 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示

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
		// 組織・担当名称、組織調整
		createOrgPlanTxt(poiBean);
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
				//poiBean.setCellData(Integer.toString(month) + "月", ROW_IDX_MONTH_HEAD, (MONTH_ITEM_NUM * count) + 2);
				poiBean.setCellData(Integer.toString(month) + "月", ROW_IDX_MONTH_HEAD, (MONTH_ITEM_NUM * count) + 4);
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
		isVaccine = codeList.get(0).getDataCd().equals(sosPlanScDto.getProdCategory());
		// ワクチンの場合はB価を表示
		if (isVaccine) {
			// 項目名のヘッダ
			// mod Start 2022/08/25 H.Futagami No.8　計画管理の月別計画に納入実績の値を表示
			//for (int i = 2; i < MAX_COL; i = i + 2) {
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
			for (int i = 4; i < MAX_COL; i = i + 2) {
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
			//for (int i = 2; i < MAX_COL; i = i + 2) {
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
			for (int i = 4; i < MAX_COL; i = i + 2) {
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
					sosPlanScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + sosPlanScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		txt.append(" ").append(TEXT_CATEGORY).append(category.getDataName());

		// 品目
		MasterManagePlannedProd prod = new MasterManagePlannedProd();
		try {
			prod = plannedProdDao.search(sosPlanScDto.getProdCode());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目に、「" + sosPlanScDto.getProdCode() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		txt.append(" ").append(TEXT_PROD).append(prod.getProdName());

		return txt.toString();
	}


	/**
	 * 組織調整、計画名の文字列をセルに書き込む。
	 */
	private void createOrgPlanTxt(POIBean poiBean) {

		// 上位計画名
		String upperPlanTotalHeader = "";

		if (sosPlanScDto.getSosCd3() != null) {

			// 組織コード(営業所)が設定されている場合
			upperPlanTotalHeader = TEXT_OFFICE_PLAN;

		} else if (sosPlanScDto.getSosCd2() != null) {

			// 組織コード(支店)が設定されている場合
			upperPlanTotalHeader = TEXT_BRANCH_PLAN;

		} else {

			// 全社が設定されている場合
			upperPlanTotalHeader = TEXT_COMPANY_PLAN;

		}
		this.upperPlanTotalHeader = upperPlanTotalHeader;
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
