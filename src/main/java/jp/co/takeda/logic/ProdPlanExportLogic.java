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
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dto.ProdPlanResultDetailDto;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DispInsKbn;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * (医)組織別計画一覧のExcelファイルを生成するロジッククラス
 *
 * @author rna8405
 */
public class ProdPlanExportLogic {

	/**
	 * 組織情報取得DAO
	 */
	private final SosMstRealDao sosMstDAO;

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
	 * (医)組織品目別計画編集画面の検索条件DTO
	 */
	private final ProdPlanScDto prodPlanScDto;

	/**
	 * (医)組織品目別計画の検索結果
	 */
	private ProdPlanResultDto prodPlanResult;

	/**
	 * 汎用コードマスターDAO
	 */
	private final CodeMasterDao codeMasterDao;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	/**
	 * 組織調整のヘッダ名
	 */
	private String tuneHeader;


	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "組織・担当者品目別計画一覧_";

	/** シート名 */
	private static final String SHEET_NAME = "組織・担当者品目別計画一覧";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「組織・担当者」 */
	private static final String TEXT_ORG_MR = "組織・担当者：";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ：";

	/** 文字列「全社」 */
	private static final String TEXT_ALL_COMPANY = "全社";

	/** 文字列「Z」 */
	private static final String TEXT_HEADER_Z = "Z";

	/** 文字列「計」 */
	private static final String TEXT_TOTAL = "計";

	/** 文字列「B価ベース」 */
	private static final String B_BASE = "B価ベース";

	/** 文字列「全社調整」 */
	private static final String TEXT_ALL_COMPANY_TUNE = "全社調整";

	/** 文字列「リージョン調整」 */
	private static final String TEXT_BRANCH_TUNE = "リージョン調整";

	/** 文字列「エリア調整」 */
	private static final String TEXT_OFFICE_TUNE = "エリア調整";

	/** 文字列「担当者調整」 */
	private static final String TEXT_MR_TUNE = "担当者調整";

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
	private static final int ROW_IDX_START_LIST = 5;

	/** [行番号] 明細ヘッダ2行目 */
	private static final int ROW_IDX_LIST_HEAD = 3;

	/** [行番号] 明細ヘッダ3行目 */
	private static final int ROW_IDX_LIST_HEAD2 = 2;

	/** [行番号] 明細ヘッダ4行目 */
	private static final int ROW_IDX_LIST_HEAD4 = 3;

	/** [行番号] 合計表示行 */
	private static final int ROW_IDX_SUM = 4;

	/** [列番号] 計画UH 担当者調整(ヘッダ) */
	private static final int COL_IDX_PLAN_UH_HED = 2;

	/** [列番号] 計画P 担当者調整(ヘッダ) */
	private static final int COL_IDX_PLAN_P_HED = 4;

	/** [列番号] 計画Z 担当者調整(ヘッダ) */
	private static final int COL_IDX_PLAN_Z_HED = 6;

	/** [列番号] 計画合計 担当者調整(ヘッダ) */
	private static final int COL_IDX_PLAN_TOTAL_HED = 8;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [列番号] 計画UH 担当者調整 */
	private static final int COL_IDX_PLAN_UH_TUNE = 2;

	/** [列番号] 計画UH Y価ベース */
	private static final int COL_IDX_PLAN_UH_Y = 3;

	/** [列番号] 計画P 担当者調整 */
	private static final int COL_IDX_PLAN_P_TUNE = 4;

	/** [列番号] 計画P Y価ベース */
	private static final int COL_IDX_PLAN_P_Y = 5;

	/** [列番号] 計画Z 担当者調整 */
	private static final int COL_IDX_PLAN_Z_TUNE = 6;

	/** [列番号] 計画Z Y価ベース */
	private static final int COL_IDX_PLAN_Z_Y = 7;

	/** [列番号] 計画合計 Y価ベース */
	private static final int COL_IDX_PLAN_TOTAL_Y = 9;

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
	 * @param prodPlanScDto (医)組織別計画編集画面の検索条件DTO
	 * @param resultDto (医)組織別計画の検索結果
	 * @param sosMstDAO 組織情報取得DAO
	 */
	public ProdPlanExportLogic(String templatePath, Date systemDate, ProdPlanScDto prodPlanScDto, ProdPlanResultDto resultDto, SosMstRealDao sosMstDAO,
			JgiMstRealDao jgiMstDao, CodeMasterDao codeMasterDao ) {
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.prodPlanScDto = prodPlanScDto;
		this.prodPlanResult = resultDto;
		this.sosMstDAO = sosMstDAO;
		this.jgiMstRealDao = jgiMstDao;
		this.codeMasterDao = codeMasterDao;
		this.outputFileName = createOutputFileName(systemDate);
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
			fileNm = URLEncoder.encode(OUTPUT_FILE_NAME_HEADER + createOrgMrNm(), "UTF-8") + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + EXCEL_EXTENSION;
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

		// 明細行
		List<ProdPlanResultDetailDto> detailList = prodPlanResult.getDetailList();

		// 検索結果リストのサイズ分行を追加
		poiBean.addRows(ROW_IDX_START_LIST, detailList.size());

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		Long sumBaseValueUH = new Long(0);
		Long sumBaseValueP = new Long(0);
		Long sumBaseValueZ = new Long(0);


		for (ProdPlanResultDetailDto detail : detailList) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// 品目名称
			poiBean.setCellData(detail.getProdName(), rowIdx, colIdx++);

			// 品目コード
			poiBean.setCellData(detail.getProdCode(), rowIdx, colIdx++);

			// UH 担当者調整
			Long tuneValueUH = calcTuneValue(detail.getYBaseValueUH(), detail.getYBaseValueUHLowerLevelPlanSummary());
			poiBean.setCellData(tuneValueUH, rowIdx, colIdx++);

			// UH Y価ベース
			poiBean.setCellData(detail.getYBaseValueUH(), rowIdx, colIdx++);

			// P 担当者調整
			Long tuneValueP = calcTuneValue(detail.getYBaseValueP(), detail.getYBaseValuePLowerLevelPlanSummary());
			poiBean.setCellData(tuneValueP, rowIdx, colIdx++);

			// P Y価ベース
			poiBean.setCellData(detail.getYBaseValueP(), rowIdx, colIdx++);

			// Z 担当者調整
			Long tuneValueZ = calcTuneValue(detail.getyBaseValueZ(), detail.getyBaseValueZLowerLevelPlanSummary());
			poiBean.setCellData(tuneValueZ, rowIdx, colIdx++);

			// Z Y価ベース
			poiBean.setCellData(detail.getyBaseValueZ(), rowIdx, colIdx++);

			// UH・P・Zの担当者調整合計
			Long sumTuneValue = null;
			if (tuneValueUH != null) {
				sumTuneValue = tuneValueUH;
			}
			if (tuneValueP != null) {
				if (sumTuneValue != null) {
					sumTuneValue = sumTuneValue + tuneValueP;
				} else {
					sumTuneValue = tuneValueP;
				}
			}
			if (tuneValueZ != null) {
				if (sumTuneValue != null) {
					sumTuneValue = sumTuneValue + tuneValueZ;
				} else {
					sumTuneValue = tuneValueZ;
				}
			}
			poiBean.setCellData(sumTuneValue, rowIdx, colIdx++);

			// UH・P・ZのY価ベース合計
			Long sumBaseValueY = new Long(0);
			if (detail.getYBaseValueUH() != null) {
				sumBaseValueY = sumBaseValueY + detail.getYBaseValueUH();
			}
			if (detail.getYBaseValueP() != null) {
				sumBaseValueY = sumBaseValueY + detail.getYBaseValueP();
			}
			if (detail.getyBaseValueZ() != null) {
				sumBaseValueY = sumBaseValueY + detail.getyBaseValueZ();
			}
			poiBean.setCellData(sumBaseValueY, rowIdx, colIdx++);

			// UH Y価ベース加算
			if (detail.getYBaseValueUH() != null) {
				sumBaseValueUH = sumBaseValueUH + detail.getYBaseValueUH();
			}
			// P Y価ベースの加算
			if (detail.getYBaseValueP() != null) {
				sumBaseValueP = sumBaseValueP + detail.getYBaseValueP();
			}
			// Z Y価ベースの加算
			if (detail.getyBaseValueZ() != null) {
				sumBaseValueZ = sumBaseValueZ + detail.getyBaseValueZ();
				poiBean.setCellData(TEXT_HEADER_Z, ROW_IDX_LIST_HEAD2 , COL_IDX_PLAN_Z_TUNE );
			}

			rowIdx++;
		}

		// カテゴリ名称 + 計
		DpmCCdMst category = new DpmCCdMst();
		try {
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), prodPlanScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + prodPlanScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		poiBean.setCellData(category.getDataName() + TEXT_TOTAL, ROW_IDX_SUM, COL_IDX_START_LIST);

		//明細行Y価ベース合計
		poiBean.setCellData(sumBaseValueUH, ROW_IDX_SUM, COL_IDX_PLAN_UH_Y);
		poiBean.setCellData(sumBaseValueP, ROW_IDX_SUM, COL_IDX_PLAN_P_Y);
		poiBean.setCellData(sumBaseValueZ, ROW_IDX_SUM, COL_IDX_PLAN_Z_Y);

		// Y価ベース全合計
		poiBean.setCellData(sumBaseValueUH + sumBaseValueP + sumBaseValueZ, ROW_IDX_SUM, COL_IDX_PLAN_TOTAL_Y);

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
		// 組織・担当名称、組織調整
		createOrgPlanTxt(poiBean);
		// 現在日付
		poiBean.setCellData(systemDate, ROW_IDX_DATE, COL_IDX_DATE);
		// 組織調整のヘッダ
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_UH_HED);
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_P_HED);
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_Z_HED);
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_TOTAL_HED);

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
		isVaccine = codeList.get(0).getDataCd().equals(prodPlanScDto.getProdCategory());
		// ワクチンの場合は「B価ベース」「ワクチン」を表示
		if (isVaccine) {
			poiBean.setCellData(DispInsKbn.ZV.getDbValue(), ROW_IDX_LIST_HEAD2, COL_IDX_PLAN_Z_TUNE);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD, COL_IDX_PLAN_UH_Y);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD, COL_IDX_PLAN_P_Y);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD, COL_IDX_PLAN_Z_Y);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD, COL_IDX_PLAN_TOTAL_Y);
		}
	}

	/**
	 * 組織・担当者名の文字列を作成する
	 * @return 組織・担当者名の文字列
	 */
	private String createOrgMrNm() {

		StringBuffer txt = new StringBuffer();
		// 組織・担当者名
		StringBuffer orgMrNm = new StringBuffer();

		try {
			if (prodPlanScDto.getJgiNo() != null) {

				// 担当者コードが設定されている場合、担当者名を取得
				orgMrNm.insert(0, jgiMstRealDao.searchReal(prodPlanScDto.getJgiNo()).getJgiName());

			}
			if (prodPlanScDto.getSosCd3() != null) {

				// 組織コード(営業所)が設定されている場合、営業所名を取得
				orgMrNm.insert(0, sosMstDAO.searchReal(prodPlanScDto.getSosCd3()).getBumonSeiName());

			}
			if (prodPlanScDto.getSosCd2() != null) {

				// 組織コード(支店)が設定されている場合、支店名を取得
				orgMrNm.insert(0, sosMstDAO.searchReal(prodPlanScDto.getSosCd2()).getBumonSeiName());

			} else {
				// 組織コードが設定されていない場合、「全社」
				orgMrNm.append(TEXT_ALL_COMPANY);
			}
		}catch (DataNotFoundException e) {
			final String errMsg = "組織情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		txt.append(orgMrNm);

		return txt.toString();

	}


	/**
	 * 表示条件の文字列を作成する
	 * @return 表示条件の文字列
	 */
	private String createDisplayConditionTxt() {
		StringBuffer txt = new StringBuffer();
		// 組織・担当者名
		StringBuffer orgMrNm = new StringBuffer();
		try {
			if (prodPlanScDto.getJgiNo() != null) {

				// 担当者コードが設定されている場合、担当者名を取得
				orgMrNm.insert(0, " " + jgiMstRealDao.searchReal(prodPlanScDto.getJgiNo()).getJgiName());

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
		}catch (DataNotFoundException e) {
			final String errMsg = "組織情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		txt.append(TEXT_CONDITION);
		txt.append(TEXT_ORG_MR);
		txt.append(orgMrNm);

		// カテゴリ
		DpmCCdMst category = new DpmCCdMst();
		try {
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), prodPlanScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + prodPlanScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("　").append(TEXT_CATEGORY).append(category.getDataName());

		return txt.toString();
	}

	/**
	 * 組織調整、計画名の文字列をセルに書き込む。
	 */
	private void createOrgPlanTxt(POIBean poiBean) {

		// 組織調整名
		String tuneHeader = "";

//		if (prodPlanScDto.getJgiNo() != null) {
//			// 組織コード(担当者)が設定されている場合
//			tuneHeader = TEXT_MR_TUNE;
//		} else if (prodPlanScDto.getSosCd3() != null && prodPlanScDto.getOncSosFlg()) {
//			// 組織コード(営業所:ONC)が設定されている場合
//			tuneHeader = TEXT_MR_TUNE;
//		} else if (prodPlanScDto.getSosCd3() != null && !prodPlanScDto.getOncSosFlg()) {
//			// 組織コード(営業所:ONC以外)が設定されている場合
//			tuneHeader = TEXT_MR_TUNE;
//		} else if (prodPlanScDto.getSosCd2() != null) {
//
//			// 組織コード(支店)が設定されている場合
//			tuneHeader = TEXT_OFFICE_TUNE;
//		} else {
//
//			// 全社が設定されている場合
//			tuneHeader = TEXT_BRANCH_TUNE;
//		}

		if (prodPlanScDto.getJgiNo() != null) {

			// 従業員番号が設定されている場合、担当者別計画一覧を作成
			tuneHeader = TEXT_MR_TUNE;

		} else if (prodPlanScDto.getSosCd4() != null) {

			// 組織コード(チーム)が設定されている場合、チーム別計画一覧を作成
			tuneHeader = TEXT_MR_TUNE;

		} else if (prodPlanScDto.getSosCd3() != null) {

			// 組織コード(営業所)が設定されている場合、営業所別計画一覧を作成
			tuneHeader = TEXT_OFFICE_TUNE;

		} else if (prodPlanScDto.getSosCd2() != null) {

			// 組織コード(支店)が設定されている場合、支店別計画一覧を作成
			tuneHeader = TEXT_BRANCH_TUNE;

		} else {

			// 組織コードが設定されていない場合、全社計画一覧を作成
			tuneHeader = TEXT_ALL_COMPANY_TUNE;
		}


		this.tuneHeader = tuneHeader;
	}


	/**
	 * 調整金額の計算処理(Excel出力用)
	 * @param yBase Y価ベース
	 * @param yBaseLowerLevelPlanSummary Y価ベース下位計画レベル合計値
	 * @return 調整金額
	 */
	private Long calcTuneValue(Long yBase, Long yBaseLowerLevelPlanSummary) {
		Long tuneValue = null;
		Long value1 = yBase;
		Long value2 = yBaseLowerLevelPlanSummary;

		if (value1 != null) {
			if (value2 != null) {
				tuneValue = value1 - value2;
			} else {
				tuneValue = value1;
			}
		} else {
			if (value2 != null) {
				tuneValue = new Long(0) - value2;
			}
		}
		return tuneValue;
	}

}
