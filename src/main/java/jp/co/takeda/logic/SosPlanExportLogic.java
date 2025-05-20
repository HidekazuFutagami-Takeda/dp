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
import jp.co.takeda.dto.SosPlanResultDetailDto;
import jp.co.takeda.dto.SosPlanResultDetailTotalDto;
import jp.co.takeda.dto.SosPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.MasterManagePlannedProd;
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
public class SosPlanExportLogic {

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
	 * (医)組織別計画編集画面の検索条件DTO
	 */
	private final SosPlanScDto sosPlanScDto;

	/**
	 * (医)組織別計画の検索結果
	 */
	private SosPlanResultDto sosPlanResult;

	/**
	 * 上位計画合計列のヘッダ名
	 */
	private String upperPlanTotalHeader;

	/**
	 * 組織調整のヘッダ名
	 */
	private String tuneHeader;

	/**
	 * 上位組織 - 下位組織の名称
	 */
	private String orgDiffNm;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "組織・担当者別計画一覧_";

	/** シート名 */
	private static final String SHEET_NAME = "組織・担当者別計画一覧";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「組織・担当者」 */
	private static final String TEXT_ORG_MR = "組織・担当者：";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ：";

	/** 文字列「品目」 */
	private static final String TEXT_PROD = "品目：";

	/** 文字列「Z」 */
	private static final String TEXT_HEADER_Z = "Z";

	/** 文字列「全社計画」 */
	private static final String TEXT_COMPANY_PLAN = "全社計画";

	/** 文字列「リージョン計画」 */
	private static final String TEXT_BRANCH_PLAN = "リージョン計画";

	/** 文字列「エリア計画」 */
	private static final String TEXT_OFFICE_PLAN = "エリア計画";

	/** 文字列「チーム計画」 */
	private static final String TEXT_TEAM_PLAN = "チーム計画";

	/** 文字列「担当者計画」 */
	private static final String TEXT_MR_PLAN = "担当者計画";

	/** 文字列「リージョン調整」 */
	private static final String TEXT_BRANCH_TUNE = "リージョン調整";

	/** 文字列「エリア調整」 */
	private static final String TEXT_OFFICE_TUNE = "エリア調整";

	/** 文字列「担当者調整」 */
	private static final String TEXT_MR_TUNE = "担当者調整";

	/** 文字列「チーム計画 - 担当者計画」 */
	private static final String TEXT_TEAM_MR_DIFF = "チーム計画 - 担当者計画";

	/** 文字列「エリア計画 - 担当者計画」 */
	private static final String TEXT_OFFICE_MR_DIFF = "エリア計画 - 担当者計画";

	/** 文字列「リージョン計画 - エリア計画」 */
	private static final String TEXT_BRANCE_OFFICE_DIFF = "リージョン計画 - エリア計画";

	/** 文字列「全社計画 - リージョン計画」 */
	private static final String TEXT_COMPANY_BRANCH_DIFF = "全社計画 - リージョン計画";

	/** 文字列「全社」 */
	private static final String TEXT_ALL_COMPANY = "全社";

	/** 文字列「B価ベース」 */
	private static final String B_BASE = "B価ベース";

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
	private static final int COL_IDX_DATE = 8;

	/** [行番号] 明細ヘッダ3行目 */
	private static final int ROW_IDX_LIST_HEAD3 = 2;

	/** [行番号] 明細ヘッダ4行目 */
	private static final int ROW_IDX_LIST_HEAD4 = 3;

	/** [行番号] 上位計画 */
	private static final int ROW_IDX_UPPER_PLAN = 4;

	/** [行番号] 上位計画 - 下位計画 */
	private static final int ROW_IDX_UPPER_LOWER_DIFF = 5;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 6;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [列番号] 組織・担当名称  */
	private static final int COL_IDX_ORG_MR_NM = 0;

	/** [列番号] 計画UH 組織調整 */
	private static final int COL_IDX_PLAN_UH_TUNE = 1;

	/** [列番号] 計画UH Y価ベース */
	private static final int COL_IDX_PLAN_UH_Y = 2;

	/** [列番号] 計画P 組織調整 */
	private static final int COL_IDX_PLAN_P_TUNE = 3;

	/** [列番号] 計画P Y価ベース */
	private static final int COL_IDX_PLAN_P_Y = 4;

	/** [列番号] 計画Z 組織調整 */
	private static final int COL_IDX_PLAN_Z_TUNE = 5;

	/** [列番号] 計画Z Y価ベース */
	private static final int COL_IDX_PLAN_Z_Y = 6;

	/** [列番号] 計画合計 組織調整 */
	private static final int COL_IDX_PLAN_TOTAL_TUNE = 7;

	/** [列番号] 計画合計 Y価ベース */
	private static final int COL_IDX_PLAN_TOTAL_Y = 8;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 8;

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
	public SosPlanExportLogic(String templatePath, Date systemDate, SosPlanScDto sosPlanScDto, SosPlanResultDto resultDto, SosMstRealDao sosMstDAO, CodeMasterDao codeMasterDao ,MasterManagePlannedProdDao plannedProdDao) {
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.sosPlanScDto = sosPlanScDto;
		this.sosPlanResult = resultDto;
		this.sosMstDAO = sosMstDAO;
		this.codeMasterDao = codeMasterDao;
		this.plannedProdDao = plannedProdDao;
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
		if (sosPlanResult == null || sosPlanResult.getDetailList() == null || sosPlanResult.getDetailList().isEmpty()) {
			return;
		}

		// 上位計画(明細合計)
		SosPlanResultDetailTotalDto total = sosPlanResult.getDetailTotal();

		// 下位計画リスト
		List<SosPlanResultDetailDto> detailList = sosPlanResult.getDetailList();

		// 検索結果リストのサイズ分行を追加
		poiBean.addRows(ROW_IDX_START_LIST, detailList.size());

		// 上位計画項目をセルへセット
		// 組織・担当名称
		poiBean.setCellData(upperPlanTotalHeader, ROW_IDX_UPPER_PLAN, COL_IDX_ORG_MR_NM);
		// UH Y価ベース
		poiBean.setCellData(total.getYBaseValueUH(), ROW_IDX_UPPER_PLAN, COL_IDX_PLAN_UH_Y);
		// P Y価ベース
		poiBean.setCellData(total.getYBaseValueP(), ROW_IDX_UPPER_PLAN, COL_IDX_PLAN_P_Y);
		// Z Y価ベース
		poiBean.setCellData(total.getyBaseValueZ(), ROW_IDX_UPPER_PLAN, COL_IDX_PLAN_Z_Y);

		// Y価ベース合計
		Long yBaseSum = null;
		if (total.getYBaseValueUH() != null) {
			yBaseSum = total.getYBaseValueUH();
		}
		if (total.getYBaseValueP() != null) {
			if (yBaseSum != null) {
				yBaseSum = yBaseSum + total.getYBaseValueP();
			} else {
				yBaseSum = total.getYBaseValueP();
			}
		}
		if (total.getyBaseValueZ() != null) {
			if (yBaseSum != null) {
				yBaseSum = yBaseSum + total.getyBaseValueZ();
			} else {
				yBaseSum = total.getyBaseValueZ();
			}
		}
		poiBean.setCellData(yBaseSum, ROW_IDX_UPPER_PLAN, COL_IDX_PLAN_TOTAL_Y);

		// 上位計画 - 下位計画
		poiBean.setCellData(orgDiffNm, ROW_IDX_UPPER_LOWER_DIFF, COL_IDX_ORG_MR_NM);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		Long sumBaseValueUH = new Long(0);
		Long sumBaseValueP = new Long(0);
		Long sumBaseValueZ = new Long(0);

		for (SosPlanResultDetailDto detail : detailList) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			if (StringUtils.isNotBlank(detail.getJgiName())) {
				// 担当名称
				poiBean.setCellData(detail.getJgiName(), rowIdx, colIdx++);
			} else {
				// 組織名称
				poiBean.setCellData(detail.getSosName(), rowIdx, colIdx++);
			}
			// UH 組織調整
			Long tuneValueUH = calcTuneValue(detail.getYBaseValueUH(), detail.getYBaseValueUHLowerLevelPlanSummary());
			poiBean.setCellData(tuneValueUH, rowIdx, colIdx++);

			// UH Y価ベース
			poiBean.setCellData(detail.getYBaseValueUH(), rowIdx, colIdx++);

			// P 組織調整
			Long tuneValueP = calcTuneValue(detail.getYBaseValueP(), detail.getYBaseValuePLowerLevelPlanSummary());
			poiBean.setCellData(tuneValueP, rowIdx, colIdx++);

			// P Y価ベース
			poiBean.setCellData(detail.getYBaseValueP(), rowIdx, colIdx++);

			// Z 組織調整
			Long tuneValueZ = calcTuneValue(detail.getYBaseValueZ(), detail.getYBaseValueZLowerLevelPlanSummary());
			poiBean.setCellData(tuneValueZ, rowIdx, colIdx++);

			// Z Y価ベース
			poiBean.setCellData(detail.getYBaseValueZ(), rowIdx, colIdx++);

			// UH・P・Zの組織調整合計
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
			if (detail.getYBaseValueZ() != null) {
				sumBaseValueY = sumBaseValueY + detail.getYBaseValueZ();
			}

			poiBean.setCellData(sumBaseValueY, rowIdx, colIdx++);

			// UH Y価ベースの加算
			if (detail.getYBaseValueUH() != null) {
				sumBaseValueUH = sumBaseValueUH + detail.getYBaseValueUH();
			}

			// P Y価ベースの加算
			if (detail.getYBaseValueP() != null) {
				sumBaseValueP = sumBaseValueP + detail.getYBaseValueP();
			}

			// Z Y価ベースの加算
			if (detail.getYBaseValueZ() != null) {
				sumBaseValueZ = sumBaseValueZ + detail.getYBaseValueZ();
				poiBean.setCellData(TEXT_HEADER_Z, ROW_IDX_LIST_HEAD3 , COL_IDX_PLAN_Z_TUNE );
			}

			rowIdx++;
		}

		// UH Y価ベースの上位組織-下位組織合計
		Long diffUh = null;
		if (total.getYBaseValueUH() == null) {
			diffUh =-sumBaseValueUH;
		} else {
			diffUh = total.getYBaseValueUH() - sumBaseValueUH;
		}
		poiBean.setCellData(diffUh, ROW_IDX_UPPER_LOWER_DIFF, COL_IDX_PLAN_UH_Y);
		// P Y価ベースの上位組織-下位組織合計
		Long diffP = null;
		if (total.getYBaseValueP() == null) {
			diffP = -sumBaseValueP;
		} else {
			diffP = total.getYBaseValueP() - sumBaseValueP;
		}
		poiBean.setCellData(diffP, ROW_IDX_UPPER_LOWER_DIFF, COL_IDX_PLAN_P_Y);
		// Z Y価ベースの上位組織-下位組織合計
		Long diffZ = null;
		if (total.getyBaseValueZ() == null) {
			diffZ = -sumBaseValueZ;
		} else {
			diffZ = total.getyBaseValueZ() - sumBaseValueZ;
		}
		poiBean.setCellData(diffZ, ROW_IDX_UPPER_LOWER_DIFF, COL_IDX_PLAN_Z_Y);

		// UH/P/ZのY価ベースの(上位組織-下位組織)合計
		poiBean.setCellData(diffUh + diffP + diffZ, ROW_IDX_UPPER_LOWER_DIFF, COL_IDX_PLAN_TOTAL_Y);


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
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_UH_TUNE);
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_P_TUNE);
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_Z_TUNE);
		poiBean.setCellData(this.tuneHeader, ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_TOTAL_TUNE);

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
		isVaccine = codeList.get(0).getDataCd().equals(sosPlanScDto.getProdCategory());
		// ワクチンの場合は「B価ベース」「ワクチン」を表示
		if (isVaccine) {
			poiBean.setCellData(DispInsKbn.ZV.getDbValue(), ROW_IDX_LIST_HEAD3, COL_IDX_PLAN_Z_TUNE);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_UH_Y);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_P_Y);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_Z_Y);
			poiBean.setCellData(B_BASE,ROW_IDX_LIST_HEAD4, COL_IDX_PLAN_TOTAL_Y);
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
			if (sosPlanScDto.getSosCd4() != null) {

				// 組織コード(担当者)が設定されている場合、担当者名を取得
				orgMrNm.insert(0, sosMstDAO.searchReal(sosPlanScDto.getSosCd4()).getBumonSeiName());

			}
			if (sosPlanScDto.getSosCd3() != null) {

				// 組織コード(営業所)が設定されている場合、営業所名を取得
				orgMrNm.insert(0, sosMstDAO.searchReal(sosPlanScDto.getSosCd3()).getBumonSeiName());

			}
			if (sosPlanScDto.getSosCd2() != null) {

				// 組織コード(支店)が設定されている場合、支店名を取得
				orgMrNm.insert(0, sosMstDAO.searchReal(sosPlanScDto.getSosCd2()).getBumonSeiName());

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
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), sosPlanScDto.getProdCategory());
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
		// 組織調整名
		String tuneHeader = "";
		// (上位計画 - 下位計画)名
		String orgDiffNm = "";

		if (sosPlanScDto.getSosCd4() != null) {

			// 組織コード(担当者)が設定されている場合
			upperPlanTotalHeader = TEXT_MR_PLAN;
			tuneHeader = TEXT_MR_TUNE;
			orgDiffNm = TEXT_TEAM_MR_DIFF;

		} else if (sosPlanScDto.getSosCd3() != null && sosPlanScDto.getOncSosFlg()) {

			// 組織コード(営業所:ONC)が設定されている場合
			upperPlanTotalHeader = TEXT_OFFICE_PLAN;
			tuneHeader = TEXT_MR_TUNE;
			orgDiffNm = TEXT_OFFICE_MR_DIFF;

		} else if (sosPlanScDto.getSosCd3() != null && !sosPlanScDto.getOncSosFlg()) {

			// 組織コード(営業所:ONC以外)が設定されている場合
			upperPlanTotalHeader = TEXT_TEAM_PLAN;
			tuneHeader = TEXT_MR_TUNE;
			orgDiffNm = TEXT_TEAM_MR_DIFF;

		} else if (sosPlanScDto.getSosCd2() != null) {

			// 組織コード(支店)が設定されている場合
			upperPlanTotalHeader = TEXT_BRANCH_PLAN;
			tuneHeader = TEXT_OFFICE_TUNE;
			orgDiffNm = TEXT_BRANCE_OFFICE_DIFF;

		} else {

			// 全社が設定されている場合
			upperPlanTotalHeader = TEXT_COMPANY_PLAN;
			tuneHeader = TEXT_BRANCH_TUNE;
			orgDiffNm = TEXT_COMPANY_BRANCH_DIFF;
		}
		this.upperPlanTotalHeader = upperPlanTotalHeader;
		this.tuneHeader = tuneHeader;
		this.orgDiffNm = orgDiffNm;
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
