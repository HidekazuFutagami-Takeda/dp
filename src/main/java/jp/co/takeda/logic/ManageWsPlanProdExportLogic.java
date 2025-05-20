package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.ManageWsPlanForIyakuProdHeaderDto;
import jp.co.takeda.dto.ManageWsPlanProdDetailDto;
import jp.co.takeda.dto.ManageWsPlanProdDto;
import jp.co.takeda.dto.ManageWsPlanProdScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

public class ManageWsPlanProdExportLogic {

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
	 * 特約店品目別計画の検索結果
	 */
	private ManageWsPlanProdDto manageWsPlanProdDto;

	/**
	 * 特約店品目別計画のヘッダ
	 */
	private ManageWsPlanForIyakuProdHeaderDto headerDto;

	/**
	 * 特約店品目別計画編集画面の検索条件DTO
	 */
	private final ManageWsPlanProdScDto manageWsPlanProdScDto;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "特約店品目別計画一覧_";

	/** シート名 */
	private static final String SHEET_NAME = "特約店品目別計画一覧";

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

	/** [列番号] UH Y価ベース */
	private static final int COL_IDX_PLAN_UH_Y = 2;

	/** [列番号] P Y価ベース */
	private static final int COL_IDX_PLAN_P_Y = 4;

	/** [列番号] Z Y価ベース */
	private static final int COL_IDX_PLAN_Z_Y = 6;

	/** [列番号] Y価ベース 合計 */
	private static final int COL_IDX_PLAN_SUM_Y = 8;

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
	public ManageWsPlanProdExportLogic(String templatePath, Date systemDate,  ManageWsPlanProdScDto  manageWsPlanProdScDto, ManageWsPlanProdDto resultDto, ManageWsPlanForIyakuProdHeaderDto headerDto, TmsTytenMstUnRealDao tytenMstUnRealDao,
			CodeMasterDao codeMasterDao, PlannedCtgDao plannedCtgDao, boolean ryutsu) {
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.manageWsPlanProdScDto = manageWsPlanProdScDto;
		this.outputFileName = createOutputFileName(systemDate, headerDto.getTmsTytenName());
		this.manageWsPlanProdDto = resultDto;
		this.headerDto = headerDto;
		this.tytenMstUnRealDao = tytenMstUnRealDao;
		this.codeMasterDao = codeMasterDao;
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
		if (manageWsPlanProdDto == null || manageWsPlanProdDto.getDetailList() == null || manageWsPlanProdDto.getDetailList().isEmpty()) {
			return;
		}
		// 明細行
		List<ManageWsPlanProdDetailDto> detailList = manageWsPlanProdDto.getDetailList();

		// 検索結果リストのサイズ分+ヘッダの行を追加
		poiBean.addRows(ROW_IDX_START_LIST, detailList.size() + 1);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST + 1;
		// 列インデックス
		int colIdx = COL_IDX_START_LIST;
		// Y価ベース中計
		Long sumBaseValueY = new Long(0);
		// S価ベース中計
		Long sumBaseValueS = new Long(0);


		// UH Y価ベース全合計
		Long allSumBaseValueUhY = new Long(0);
		// UH S価ベース全合計
		Long allSumBaseValueUhS = new Long(0);
		// P Y価ベース全合計
		Long allSumBaseValuePY = new Long(0);
		// P S価ベース全合計
		Long allSumBaseValuePS = new Long(0);
        // Z Y価ベース全合計
        Long allSumBaseValueZY = new Long(0);
        // Z S価ベース全合計
        Long allSumBaseValueZS = new Long(0);


		for (ManageWsPlanProdDetailDto detail : detailList) {

			// 列インデックス
			colIdx = COL_IDX_START_LIST;

			sumBaseValueY = new Long(0);
			sumBaseValueS = new Long(0);

			// 品目名
			poiBean.setCellData(detail.getProdName(), rowIdx, colIdx++);

			// 品目コード
            poiBean.setCellData(detail.getProdCode(), rowIdx, colIdx++);

            /*
             * UH
             */
			// Y価ベース
			poiBean.setCellData(detail.getBaseYUH(),rowIdx, colIdx++);
			if (detail.getBaseYUH() != null) {
				sumBaseValueY = sumBaseValueY + detail.getBaseYUH();
                allSumBaseValueUhY = allSumBaseValueUhY + detail.getBaseYUH();
			}

			// S価ベース
			// 流通政策部である場合のみ表示
			if (ryutsu) {
				poiBean.setCellData(detail.getBaseSUH(), rowIdx, colIdx++);
				if (detail.getBaseSUH() != null) {
					sumBaseValueS = sumBaseValueS + detail.getBaseSUH();
	                allSumBaseValueUhS = allSumBaseValueUhS + detail.getBaseSUH();
				}
			} else {
				poiBean.setCellData("", rowIdx, colIdx++);
			}

            /*
             * P
             */
			// Y価ベース
			poiBean.setCellData(detail.getBaseYP(), rowIdx, colIdx++);
			if (detail.getBaseYP() != null) {
				sumBaseValueY = sumBaseValueY + detail.getBaseYP();
                allSumBaseValuePY = allSumBaseValuePY + detail.getBaseYP();
			}

			// S価ベース
			// 流通政策部である場合のみ表示
			if (ryutsu) {
				poiBean.setCellData(detail.getBaseSP(), rowIdx, colIdx++);
				if (detail.getBaseSP() != null) {
					sumBaseValueS = sumBaseValueS + detail.getBaseSP();
	                allSumBaseValuePS = allSumBaseValuePS + detail.getBaseSP();
				}
			} else {
				poiBean.setCellData("", rowIdx, colIdx++);
			}

            /*
             * Z
             */
            // Y価ベース
            poiBean.setCellData(detail.getBaseYZ(), rowIdx, colIdx++);
            if (detail.getBaseYZ() != null) {
                sumBaseValueY = sumBaseValueY + detail.getBaseYZ();
                allSumBaseValueZY = allSumBaseValueZY + detail.getBaseYZ();
            }

            // S価ベース
            // 流通政策部である場合のみ表示
            if (ryutsu) {
                poiBean.setCellData(detail.getBaseSZ(), rowIdx, colIdx++);
                if (detail.getBaseSZ() != null) {
                    sumBaseValueS = sumBaseValueS + detail.getBaseSZ();
                    allSumBaseValueZS = allSumBaseValueZS + detail.getBaseSZ();
                }
            } else {
                poiBean.setCellData("", rowIdx, colIdx++);
            }

            // 合計 Y価ベース
			poiBean.setCellData(sumBaseValueY, rowIdx, COL_IDX_PLAN_SUM_Y);
			// 流通政策部である場合のみ表示
			// 合計 S価ベース
			if (ryutsu) {
				poiBean.setCellData(sumBaseValueS, rowIdx, COL_IDX_PLAN_SUM_S);
			} else {
				poiBean.setCellData("", rowIdx, COL_IDX_PLAN_SUM_S);
			}

			rowIdx++;
		}

		// 列インデックス
		colIdx = COL_IDX_START_LIST;
		// カテゴリ名称
		poiBean.setCellData(headerDto.getProdCategoryName() + TEXT_SUM, ROW_IDX_START_LIST, colIdx++);
		// 空白
		poiBean.setCellData("", ROW_IDX_START_LIST, colIdx++);

		// UH Y価ベース全合計
		poiBean.setCellData(allSumBaseValueUhY, ROW_IDX_START_LIST, colIdx++);
		// 流通政策部である場合のみ表示
		// UH S価ベース全合計
		if (ryutsu) {
			poiBean.setCellData(allSumBaseValueUhS, ROW_IDX_START_LIST, colIdx++);
		} else {
			poiBean.setCellData("", ROW_IDX_START_LIST, colIdx++);
		}
		// P Y価ベース全合計
		poiBean.setCellData(allSumBaseValuePY, ROW_IDX_START_LIST, colIdx++);
		// 流通政策部である場合のみ表示
		// P S価ベース全合計
		if (ryutsu) {
			poiBean.setCellData(allSumBaseValuePS, ROW_IDX_START_LIST, colIdx++);
		} else {
			poiBean.setCellData("", ROW_IDX_START_LIST, colIdx++);
		}
        // Z Y価ベース全合計
        poiBean.setCellData(allSumBaseValueZY, ROW_IDX_START_LIST, colIdx++);
        // 流通政策部である場合のみ表示
        // Z S価ベース全合計
        if (ryutsu) {
            poiBean.setCellData(allSumBaseValueZS, ROW_IDX_START_LIST, colIdx++);
        } else {
            poiBean.setCellData("", ROW_IDX_START_LIST, colIdx++);
        }
		// Y価全合計
		poiBean.setCellData(allSumBaseValueUhY + allSumBaseValuePY, ROW_IDX_START_LIST, COL_IDX_PLAN_SUM_Y);
		// 流通政策部である場合のみ表示
		// S価全合計
		if (ryutsu) {
			poiBean.setCellData(allSumBaseValueUhS + allSumBaseValuePS, ROW_IDX_START_LIST, COL_IDX_PLAN_SUM_S);
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
		// タイトル
		poiBean.setCellData(manageWsPlanProdScDto.getTitleUH(), ROW_IDX_LIST_HEAD, COL_IDX_PLAN_UH_Y);
        poiBean.setCellData(manageWsPlanProdScDto.getTitleP(), ROW_IDX_LIST_HEAD, COL_IDX_PLAN_P_Y);
        poiBean.setCellData(manageWsPlanProdScDto.getTitleZ(), ROW_IDX_LIST_HEAD, COL_IDX_PLAN_Z_Y);

        // ワクチンの場合、Y価ベースをB価ベースに書き換える
        if(manageWsPlanProdScDto.isVaccine()) {
            int rowNum = ROW_IDX_LIST_HEAD + 1;
            poiBean.setCellData(B_BASE, rowNum, COL_IDX_PLAN_UH_Y);
            poiBean.setCellData(B_BASE, rowNum, COL_IDX_PLAN_P_Y);
            poiBean.setCellData(B_BASE, rowNum, COL_IDX_PLAN_Z_Y);
            poiBean.setCellData(B_BASE, rowNum, COL_IDX_PLAN_SUM_Y);
        }

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
		final String tmsTytenCd13 = new CreateTmsTytenCdLogic(manageWsPlanProdScDto.getTmsTytenCd()).execute();
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
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), manageWsPlanProdScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + manageWsPlanProdScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_CATEGORY).append(category.getDataName());

		//計画
		final PlanData planData = manageWsPlanProdScDto.getPlanData();
		txt.append("  ").append(TEXT_PLAN_DATA);
		if ("0".equals(planData.getDbValue())) {
			txt.append(TEXT_PLAN_EXIST);
		} else if("1".equals(planData.getDbValue())) {
			txt.append(TEXT_ALL_TYTEN);
		}

		//特約店コード
		txt.append("  ").append(TEXT_DEALER_CODE).append(manageWsPlanProdScDto.getTmsTytenCd());

		return txt.toString();
	}

}
