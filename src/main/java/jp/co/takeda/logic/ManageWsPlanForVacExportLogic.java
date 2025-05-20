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
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.ManageWsPlanForVacDetailDto;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

public class ManageWsPlanForVacExportLogic {

	/**
	 * TMS特約店基本統合DAO
	 */
	private final TmsTytenMstUnRealDao tytenMstUnRealDao;

	/**
	 * 流通政策部であるか
	 */
	private boolean ryutsu;

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 汎用コードマスターDAO
	 */
	private final CodeMasterDao codeMasterDao;

	/**
	 * 計画対象品目DAO
	 */
	private final MasterManagePlannedProdDao plannedProdDao;

	/**
	 * (ワ)特約店別計画の検索結果
	 */
	private ManageWsPlanForVacDto manageWsPlanDto;

	/**
	 * (ワ)特約店別計画のヘッダ
	 */
	private ManageWsPlanForVacHeaderDto headerDto;

	/**
	 * (ワ)特約店別計画編集画面の検索条件DTO
	 */
	private final ManageWsPlanForVacScDto manageWsPlanScDto;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "特約店別計画一覧_";

	/** シート名 */
	private static final String SHEET_NAME = "特約店別計画一覧";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「特約店」 */
	private static final String TEXT_DEALER = "特約店：";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ：";

	/** 文字列「計画」 */
	private static final String TEXT_PLAN_DATA = "計画：";

	/** 文字列「特約店コード」 */
	private static final String TEXT_DEALER_CODE = "特約店コード：";

	/** 文字列「品目」 */
	private static final String TEXT_PROD = "品目：";

	/** 文字列「B価ベース」 */
	private static final String B_BASE = "B価ベース";

	/** 文字列「計」 */
	private static final String TEXT_SUM = "  計";

	/** 文字列「計画あり」 */
	private static final String TEXT_PLAN_EXIST = "計画あり";

	/** 文字列「全特約店」 */
	private static final String TEXT_ALL_TYTEN = "全特約店";

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
	private static final int COL_IDX_DATE = 9;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 4;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [行番号] ヘッダ１行目 */
	private static final int ROW_IDX_LIST_HEAD = 2;

	/** [行番号] ヘッダ２行目 */
	private static final int ROW_IDX_LIST_HEAD2 = 3;

	/** [列番号] 1枠目 B価ベース */
	private static final int COL_IDX_PLAN_1_B = 2;

	/** [列番号] 1枠目 S価ベース */
	private static final int COL_IDX_PLAN_1_S = 3;

	/** [列番号] 2枠目 B価ベース */
	private static final int COL_IDX_PLAN_2_B = 4;

	/** [列番号] 2枠目 S価ベース */
	private static final int COL_IDX_PLAN_2_S = 5;

	/** [列番号] 3枠目 B価ベース */
	private static final int COL_IDX_PLAN_3_B = 6;

	/** [列番号] 3枠目 S価ベース */
	private static final int COL_IDX_PLAN_3_S = 7;

	/** [列番号] B価ベース 合計 */
	private static final int COL_IDX_PLAN_SUM_B = 8;

	/** [列番号] S価ベース 合計 */
	private static final int COL_IDX_PLAN_SUM_S = 9;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 9;

	/**
	 * コンストラクタ
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param sosPlanScDto (医)組織別計画編集画面の検索条件DTO
	 * @param resultDto (医)組織別計画の検索結果
	 * @param tytenMstUnRealDao TMS特約店基本統合DAO
	 */
	public ManageWsPlanForVacExportLogic(String templatePath, Date systemDate,  ManageWsPlanForVacScDto manageWsPlanScDto, ManageWsPlanForVacDto resultDto, ManageWsPlanForVacHeaderDto headerDto,
			TmsTytenMstUnRealDao tytenMstUnRealDao, CodeMasterDao codeMasterDao, MasterManagePlannedProdDao plannedProdDao, boolean ryutsu) {
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.manageWsPlanScDto = manageWsPlanScDto;
		this.outputFileName = createOutputFileName(systemDate,headerDto.getTmsTytenName());
		this.manageWsPlanDto = resultDto;
		this.headerDto = headerDto;
		this.tytenMstUnRealDao = tytenMstUnRealDao;
		this.codeMasterDao = codeMasterDao;
		this.plannedProdDao = plannedProdDao;
		this.ryutsu = ryutsu;
	}

	/**
	 * 出力ファイル名の生成
	 *
	 * @param systemDate システム日付
	 * @return 出力ファイル名
	 * @throws UnsupportedEncodingException
	 */
	private String createOutputFileName(Date systemDate, String tmsTytenName) {
		String fileNm = "";
		try {
			fileNm = URLEncoder.encode(OUTPUT_FILE_NAME_HEADER + tmsTytenName, "UTF-8") + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + EXCEL_EXTENSION;
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

		// 選択カテゴリがワクチンかどうか
		List<DpmCCdMst> codeList = new ArrayList<DpmCCdMst>();
		try {
			codeList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		if(codeList.size() > 1) {
			final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが複数登録されています。";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		}

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
		if (manageWsPlanDto == null || manageWsPlanDto.getDetailList() == null || manageWsPlanDto.getDetailList().isEmpty()) {
			return;
		}
		// 明細行
		List<ManageWsPlanForVacDetailDto> detailList = manageWsPlanDto.getDetailList();

		// 検索結果リストのサイズ分+ヘッダの行を追加
		poiBean.addRows(ROW_IDX_START_LIST, detailList.size() + 1);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST + 1;
		// 列インデックス
		int colIdx = COL_IDX_START_LIST;
		// 特約店名称用インデント
		StringBuffer tytenNameIndent = new StringBuffer();
		// 特約店列表示文字列
		StringBuffer tytenStr = new StringBuffer();
		// B価ベース中計
		Long sumBaseValueB = new Long(0);
		// S価ベース中計
		Long sumBaseValueS = new Long(0);
		// B価ベース全合計
		Long allSumBaseValueB = new Long(0);
		// S価ベース全合計
		Long allSumBaseValueS = new Long(0);
		// コードの長さ
		int codeLength = 0;
		// 1行目であるか
		boolean isFirstRow = true;

		tytenNameIndent.append("　");

		for (ManageWsPlanForVacDetailDto detail : detailList) {

			// 列インデックス
			colIdx = COL_IDX_START_LIST;

			sumBaseValueB = new Long(0);
			sumBaseValueS = new Long(0);

			// 2行目以降、コードの長さが前行より長ければ、特約店名称の前にブランクを１つ追加
			// 前行より短ければ、特約店名称の前のブランクを１つ削除
			if (!isFirstRow) {
				if (detail.getTmsTytenCd().length() > codeLength) {
					tytenNameIndent.append("　");
				}  else if (detail.getTmsTytenCd().length() < codeLength) {
					tytenNameIndent.deleteCharAt(0);
				}
			}

			//特約店名称列に表示する文字列
			tytenStr = new StringBuffer();
			tytenStr.append(tytenNameIndent);
			tytenStr.append(detail.getTmsTytemName());
			// 中計行の場合は、ブランクと「計」をつける
			if (detail.isTytenSumRowFlg()) {
				tytenStr.append(TEXT_SUM);
			}
			// コードの長さ
			codeLength = detail.getTmsTytenCd().length();

			poiBean.setCellData(tytenStr.toString(), rowIdx, colIdx++);

			// TMS特約店コード
			poiBean.setCellData(detail.getTmsTytenCd(), rowIdx, colIdx++);

			// B価ベース
			poiBean.setCellData(detail.getBaseB(),rowIdx, colIdx++);
			if (detail.getBaseB() != null) {
				sumBaseValueB = sumBaseValueB + detail.getBaseB();
			}

			// S価ベース
			// 流通政策部である場合のみ表示
			if (ryutsu) {
				poiBean.setCellData(detail.getBaseT(), rowIdx, colIdx++);
				if (detail.getBaseT() != null) {
					sumBaseValueS = sumBaseValueS + detail.getBaseT();
				}
			} else {
				poiBean.setCellData("", rowIdx, colIdx++);
			}

			// 合計 B価ベース
			poiBean.setCellData(sumBaseValueB, rowIdx, COL_IDX_PLAN_SUM_B);
			// 合計 S価ベース
			// 流通政策部である場合のみ表示
			if (ryutsu) {
//				poiBean.setCellData(detail.getBaseT(), rowIdx, colIdx++);
				if (detail.getBaseT() != null) {
					poiBean.setCellData(sumBaseValueS, rowIdx, COL_IDX_PLAN_SUM_S);
				}
			} else {
				poiBean.setCellData("", rowIdx, COL_IDX_PLAN_SUM_S);
			}


			// 中計行でなければ、全合計用に加算処理
			if (!detail.isTytenSumRowFlg()) {
				if (detail.getBaseB() != null) {
					allSumBaseValueB = allSumBaseValueB + detail.getBaseB();
				}
				if (detail.getBaseT() != null) {
					allSumBaseValueS = allSumBaseValueS + detail.getBaseT();
				}
			}
			rowIdx++;
			isFirstRow = false;
		}

		// 列インデックス
		colIdx = COL_IDX_START_LIST;
		// 特約店名称
		poiBean.setCellData(headerDto.getTmsTytenName() + TEXT_SUM, ROW_IDX_START_LIST, colIdx++);
		// TMS特約店コード
		poiBean.setCellData(headerDto.getTmsTytenCd(), ROW_IDX_START_LIST, colIdx++);
		// B価ベース全合計
		poiBean.setCellData(allSumBaseValueB, ROW_IDX_START_LIST, colIdx++);
		// 流通政策部である場合のみ表示
		// S価ベース全合計
		if (ryutsu) {
			poiBean.setCellData(allSumBaseValueS, ROW_IDX_START_LIST, colIdx++);
		} else {
			poiBean.setCellData("", ROW_IDX_START_LIST, colIdx++);
		}
		// B価全合計
		poiBean.setCellData(allSumBaseValueB, ROW_IDX_START_LIST, COL_IDX_PLAN_SUM_B);
		// 流通政策部である場合のみ表示
		// S価全合計
		if (ryutsu) {
			poiBean.setCellData(allSumBaseValueS, ROW_IDX_START_LIST, COL_IDX_PLAN_SUM_S);
		} else {
			poiBean.setCellData("", ROW_IDX_START_LIST, COL_IDX_PLAN_SUM_S);
		}

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + detailList.size() + 1;
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
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

		// ワクチンの場合は「B価ベース」を表示
		poiBean.setCellData(B_BASE, ROW_IDX_LIST_HEAD2, COL_IDX_PLAN_1_B);
		poiBean.setCellData(B_BASE, ROW_IDX_LIST_HEAD2, COL_IDX_PLAN_2_B);
		poiBean.setCellData(B_BASE, ROW_IDX_LIST_HEAD2, COL_IDX_PLAN_3_B);
		poiBean.setCellData(B_BASE, ROW_IDX_LIST_HEAD2, COL_IDX_PLAN_SUM_B);
		// ワクチンの場合は、対象区分を表示しない
		poiBean.setCellData("", ROW_IDX_LIST_HEAD, COL_IDX_PLAN_1_B);
		poiBean.setCellData("", ROW_IDX_LIST_HEAD, COL_IDX_PLAN_2_B);
		poiBean.setCellData("", ROW_IDX_LIST_HEAD, COL_IDX_PLAN_3_B);
	}

	/**
	 * 表示条件の文字列を作成する
	 * @return 表示条件の文字列
	 */
	private String createDisplayConditionTxt() {
		StringBuffer txt = new StringBuffer();

		txt.append(TEXT_CONDITION);

		//特約店
		String tytenName = "";
		// 特約店コードを13桁に変換
		final String tmsTytenCd13 = new CreateTmsTytenCdLogic(manageWsPlanScDto.getTmsTytenCd()).execute();
		if (StringUtils.isNotEmpty(tmsTytenCd13)) {
			try {
				tytenName = tytenMstUnRealDao.searchRealRef(tmsTytenCd13).getTmsTytenMeiKj();
			} catch (DataNotFoundException e) {
				final String errMsg = "特約店基本統合ビューに、「" + tmsTytenCd13 + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}
		txt.append("  ").append(TEXT_DEALER).append(tytenName);

		//カテゴリ
		DpmCCdMst category = new DpmCCdMst();
		try {
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + CodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_CATEGORY).append(category.getDataName());

		//計画
		final PlanData planData = manageWsPlanScDto.getPlanData();
		txt.append("  ").append(TEXT_PLAN_DATA);
		if ("0".equals(planData.getDbValue())) {
			txt.append(TEXT_PLAN_EXIST);
		} else if("1".equals(planData.getDbValue())) {
			txt.append(TEXT_ALL_TYTEN);
		}

		//特約店コード
		txt.append("  ").append(TEXT_DEALER_CODE).append(manageWsPlanScDto.getTmsTytenCd());

		//品目
		MasterManagePlannedProd prod = new MasterManagePlannedProd();
		try {
			prod = plannedProdDao.search(manageWsPlanScDto.getProdCode());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目に、「" + manageWsPlanScDto.getProdCode() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_PROD).append(prod.getProdName());

		return txt.toString();
	}

}
