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
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dto.InsProdPlanResultDetailDto;
import jp.co.takeda.dto.InsProdPlanResultDto;
import jp.co.takeda.dto.InsProdPlanScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * (医)施設品目別計画一覧のExcelファイルを生成するロジッククラス
 *
 * @author rna8405
 */
public class InsProdPlanExportLogic {

	/**
	 * (医)組織別計画一覧のExcelファイルを生成するロジッククラス
	 *
	 * @author rna8405
	 */

//		/**
//		 * 組織情報取得DAO
//		 */
//		private final SosMstDAO sosMstDAO;

		/**
		 * 施設情報取得DAO
		 */
		private final InsMstRealDao insMstRealDao;

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
		 * (医)組織別計画編集画面の検索条件DTO
		 */
		private final InsProdPlanScDto insProdPlanScDto;

		/**
		 * (医)組織別計画の検索結果
		 */
		private InsProdPlanResultDto insProdPlanResult;

		/**
		 * 出力ファイル名
		 */
		private final String outputFileName;

		// -------------------------------------
		// 文字列定数
		// -------------------------------------
		/** 出力ファイル名ヘッダ */
		private static final String OUTPUT_FILE_NAME_HEADER = "施設品目別計画一覧_";

		/** シート名 */
		private static final String SHEET_NAME = "施設品目別計画一覧";

		/** 文字列「表示条件」 */
		private static final String TEXT_CONDITION = "【表示条件】";

		/** 文字列「カテゴリ」 */
		private static final String TEXT_CATEGORY = "カテゴリ：";

		/** 文字列「施設」 */
		private static final String TEXT_INSTITUTION = "施設：";

		/** 文字列「計画」 */
		private static final String TEXT_PLAN_DATA = "計画：";

		/** 文字列「B価ベース」 */
		private static final String B_BASE = "B価ベース";

		/** 文字列「計画あり」 */
		private static final String TEXT_PLAN_EXIST = "計画あり";

		/** 文字列「全施設」 */
		private static final String TEXT_ALL_INS = "全施設";

		/** 文字列「計」 */
		private static final String TEXT_SUM = "  計";


		/** 出力Excel拡張子 */
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		private static final String EXCEL_EXTENSION = ".xls";
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
		private static final int COL_IDX_DATE = 8;

		/** [行番号] 明細開始行 */
		private static final int ROW_IDX_START_LIST = 6;

		/** [列番号] 明細開始列 */
		private static final int COL_IDX_START_LIST = 0;

		/** [行番号] ヘッダY価ベース */
		private static final int ROW_IDX_HEAD_Y = 3;

		/** [列番号] ヘッダY価ベース */
		private static final int COL_IDX_HEAD_Y = 2;

		/** [行番号] 印刷開始行 */
		private static final int ROW_IDX_PRINT_START = 0;

		/** [列番号] 印刷開始列 */
		private static final int COL_IDX_PRINT_START = 0;

		/** [列番号] 印刷終了列 */
		private static final int COL_IDX_PRINT_END = 8;


		/**
		 * コンストラクタ
		 * @param templatePath テンプレート絶対パス名
		 * @param systemDate システム日付
		 * @param sosPlanScDto (医)組織別計画編集画面の検索条件DTO
		 * @param resultDto (医)組織別計画の検索結果
		 * @param insMstDAO 組織情報取得DAO
		 * @param headerDto 施設品目別計画の検索結果 ヘッダ情報
		 */
		public InsProdPlanExportLogic(String templatePath, Date systemDate, InsProdPlanScDto insProdPlanScDto, InsProdPlanResultDto resultDto,
										InsMstRealDao insMstDAO, CodeMasterDao codeMasterDao, String insName) {
			this.templatePath = templatePath;
			this.systemDate = systemDate;
			this.insProdPlanScDto = insProdPlanScDto;
			this.insProdPlanResult = resultDto;
			this.insMstRealDao = insMstDAO;
			this.codeMasterDao = codeMasterDao;
			this.outputFileName = createOutputFileName(systemDate, insName);
		}

		/**
		 * 出力ファイル名の生成
		 *
		 * @param systemDate システム日付
		 * @return 出力ファイル名
		 * @throws UnsupportedEncodingException
		 */
		private String createOutputFileName(Date systemDate, String insName) {
			String fileNm = "";
			try {
				fileNm = URLEncoder.encode(OUTPUT_FILE_NAME_HEADER + insName, "UTF-8") + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + EXCEL_EXTENSION;
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
//				POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
//				HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
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
//		private void write(HSSFWorkbook workBook) {
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
			if (insProdPlanResult == null || insProdPlanResult.getDetailList() == null || insProdPlanResult.getDetailList().isEmpty()) {
				return;
			}

			// 明細行
			List<InsProdPlanResultDetailDto> detailList = insProdPlanResult.getDetailList();

			// 検索結果リストのサイズ分行を追加
			poiBean.addRows(ROW_IDX_START_LIST, detailList.size());

			// 行インデックス
			int rowIdx = ROW_IDX_START_LIST;
			Long sumBaseValueY = new Long(0);


			for (InsProdPlanResultDetailDto detail : detailList) {

				// 列インデックス
				int colIdx = COL_IDX_START_LIST;

				//// カテゴリ名称 +　計
				DpmCCdMst category = new DpmCCdMst();
				try {
					category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), insProdPlanScDto.getProdCategory());
				} catch (DataNotFoundException e) {
					final String errMsg = "計画管理汎用マスタに、「" + insProdPlanScDto.getProdCategory() + "」コードが登録されていません。";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
				}
				poiBean.setCellData(category.getDataName() + TEXT_SUM, 5, 0);

				// 品目名称
				poiBean.setCellData(detail.getProdName(), rowIdx, colIdx++);

				// 品目コード
				poiBean.setCellData(detail.getProdCode(), rowIdx, colIdx++);

				// Y価ベース
				poiBean.setCellData(detail.getYBaseValue(), rowIdx, colIdx++);

				// 明細Y価ベース合計
				if (detail.getYBaseValue() != null) {
					sumBaseValueY = sumBaseValueY + detail.getYBaseValue();
				}
				rowIdx++;
			}
			//明細行Y価ベース合計
			poiBean.setCellData(sumBaseValueY,5,2);

			// 印刷範囲を設定
			final int rowIdxPrintEnd = ROW_IDX_START_LIST + detailList.size();
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

			// 選択カテゴリがワクチンかどうか
			boolean isVaccine;
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
			isVaccine = codeList.get(0).getDataCd().equals(insProdPlanScDto.getProdCategory());
			// ワクチンの場合は「B価ベース」を表示
			if (isVaccine) {
				poiBean.setCellData(B_BASE,ROW_IDX_HEAD_Y, COL_IDX_HEAD_Y);
			}
		}

		/**
		 * 表示条件の文字列を作成する
		 * @return 表示条件の文字列
		 */
		private String createDisplayConditionTxt() {

			StringBuffer txt = new StringBuffer();

			txt.append(TEXT_CONDITION);

			//カテゴリ
			DpmCCdMst category = new DpmCCdMst();
			try {
				category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), insProdPlanScDto.getProdCategory());
			} catch (DataNotFoundException e) {
				final String errMsg = "計画管理汎用マスタに、「" + insProdPlanScDto.getProdCategory() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
			txt.append(" ").append(TEXT_CATEGORY).append(category.getDataName());

			//施設
			String insName = "";
			if (StringUtils.isNotEmpty(insProdPlanScDto.getInsNo())) {
				try {
					insName = insMstRealDao.searchReal(insProdPlanScDto.getInsNo()).getInsAbbrName();
				} catch (DataNotFoundException e) {
					final String errMsg = "組織マスタに、「" + insProdPlanScDto.getInsNo() + "」コードが登録されていません。";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
				}
			}
			txt.append("  ").append(TEXT_INSTITUTION).append(insName);

			//計画
			final PlanData planData = insProdPlanScDto.getPlanData();
			txt.append("  ").append(TEXT_PLAN_DATA);
			if ("0".equals(planData.getDbValue())) {
				txt.append(TEXT_PLAN_EXIST);
			} else if("1".equals(planData.getDbValue())) {
				txt.append(TEXT_ALL_INS);
			}
			return txt.toString();

		}
}
