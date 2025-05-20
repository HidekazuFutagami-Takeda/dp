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
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.JgiMstRealDao;
import jp.co.takeda.dao.ManageInsWsPlanDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.ManageInsWsPlanDetailInsDto;
import jp.co.takeda.dto.ManageInsWsPlanDetailWsDto;
import jp.co.takeda.dto.ManageInsWsPlanDto;
import jp.co.takeda.dto.ManageInsWsPlanHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * (医)施設特約店別計画のExcelファイルを生成するロジッククラス
 *
 * @author rna8405
 */
public class InsWsPlanExportLogic {

	/**
	 * TMS特約店コードの長さ
	 */
	private static final int TMS_TYTEN_CD_LENGTH = 13;

	/**
	 * 組織情報取得DAO
	 */
	private final SosMstRealDao sosMstDAO;

	/**
	 * 従業員情報取得DAO
	 */
	private final JgiMstRealDao jgiMstRealDao;

	/**
	 * 施設情報取得DAO
	 */
	private final InsMstRealDao insMstRealDao;

	/**
	 * 特約店基本統合取得DAO
	 */
	private final TmsTytenMstUnRealDao tytenMstUnReadDao;

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 計画対象品目DAO
	 */
	private final MasterManagePlannedProdDao plannedProdDao;

	/**
	 * 汎用コードマスターDAO
	 */
	private final CodeMasterDao codeMasterDao;

	/**
	 * (医)施設特約店別計画の検索用条件DTO
	 */
	private ManageInsWsPlanScDto manageInsWsPlanScDto;

	/**
	 * (医)施設特約店別計画の検索結果
	 */
	private ManageInsWsPlanDto manageInsWsPlanDto;

	/**
	 * (医)施設特約店別計画の検索結果
	 */
	private ManageInsWsPlanDao manageInsWsPlanDao;

	/**
	 * (医)施設特約店別計画の検索結果
	 */
	private ManageInsWsPlanDetailInsDto manageInsWsPlanDetailInsDto;


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
	 *  流通政策部かどうか
	 */
	private boolean ryutsu;

	/**
	 *  (医)施設特約店別計画編集画面の検索ヘッダ
	 */
	private ManageInsWsPlanHeaderDto headerDto;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "施設特約店別計画一覧_";

	/** シート名 */
	private static final String SHEET_NAME = "施設特約店別計画一覧";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「組織・担当者」 */
	private static final String TEXT_ORG_MR = "組織・担当者：";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ：";

	/** 文字列「品目」 */
	private static final String TEXT_PROD = "品目：";

	/** 文字列「対象区分」 */
	private static final String TEXT_INS_TYPE = "対象区分：";

	/** 文字列「計画」 */
	private static final String TEXT_PLAN_DATA = "計画：";

	/** 文字列「施設」 */
	private static final String TEXT_INSTITUTION = "施設：";

	/** 文字列「特約店」 */
	private static final String TEXT_DEALER = "特約店：";

	/** 文字列「B価ベース」 */
	private static final String TEXT_B_BASE = "B価ベース";

	/** 文字列「施設合計」 */
	private static final String TEXT_INS_SUM = "施設合計";

	/** 文字列「計画あり」 */
	private static final String TEXT_PLAN_EXIST = "計画あり";

	/** 文字列「全施設」 */
	private static final String TEXT_ALL_INS = "全施設";

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
	private static final int COL_IDX_DATE = 6;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 6;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [列番号] 施設合計列 */
	private static final int COL_IDX_INS_SUM = 2;

	/** [行番号] 全合計行 */
	private static final int ROW_IDX_ALL_SUM = 5;

	/** [列番号] Y価列 */
	private static final int COL_IDX_Y = 4;

	/** [列番号] S価列 */
	private static final int COL_IDX_S = 5;

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
	 * @param systemDate システム日付
	 * @param systemDate システム日付
	 * @param systemDate システム日付
	 */
	public InsWsPlanExportLogic(String templatePath, Date systemDate, ManageInsWsPlanScDto scDto, ManageInsWsPlanDto manageInsWsPlanDto, SosMstRealDao sosMstDAO,
			JgiMstRealDao jgiMstDao, InsMstRealDao insMstDao, TmsTytenMstUnRealDao tytenMstDao, CodeMasterDao codeMasterDao, MasterManagePlannedProdDao plannedProdDao,
			boolean ryutsu, ManageInsWsPlanHeaderDto headerDto) {
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.manageInsWsPlanScDto = scDto;
		this.manageInsWsPlanDto = manageInsWsPlanDto;
		this.sosMstDAO = sosMstDAO;
		this.jgiMstRealDao = jgiMstDao;
		this.insMstRealDao = insMstDao;
		this.tytenMstUnReadDao = tytenMstDao;
		this.codeMasterDao = codeMasterDao;
		this.plannedProdDao = plannedProdDao;
		this.ryutsu = ryutsu;
		this.headerDto = headerDto;
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
		if (manageInsWsPlanDto == null || manageInsWsPlanDto.getDetailList() == null || manageInsWsPlanDto.getDetailList().isEmpty()) {
			return;
		}

		// 明細行
		List<ManageInsWsPlanDetailInsDto> detailList = manageInsWsPlanDto.getDetailList();

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;
		Long sumBaseValueY = new Long(0);
		Long sumBaseValueS = new Long(0);

		// 施設ごとの先頭行であるか
		boolean isInsTop = false;
		// 列インデックス
		int colIdx = COL_IDX_START_LIST;

		// 全施設合計
		Long allSumBaseValueY = new Long(0);
		Long allSumBaseValueS = new Long(0);

		for (ManageInsWsPlanDetailInsDto detail : detailList) {
			isInsTop = true;
			// 検索結果リストのサイズ分行を追加
			List<ManageInsWsPlanDetailWsDto> wsList = detail.getWsList();
			if(wsList == null) {
				poiBean.addRows(rowIdx, 1);
				colIdx = COL_IDX_START_LIST;
				poiBean.setCellData(detail.getInsName(), rowIdx, colIdx++);
				poiBean.setCellData(detail.getInsNo(), rowIdx, colIdx++);
				rowIdx++;

				// 「施設合計」
				poiBean.addRows(rowIdx, 1);
				colIdx = COL_IDX_INS_SUM;
				poiBean.setCellData(TEXT_INS_SUM, rowIdx,colIdx++);
				rowIdx++;
				continue;
			}

			poiBean.addRows(rowIdx, detail.getWsList().size());
			sumBaseValueY = new Long(0);
			sumBaseValueS = new Long(0);
			for(ManageInsWsPlanDetailWsDto ws : detail.getWsList()) {
				// 列インデックス
				colIdx = COL_IDX_START_LIST;

				//施設名称
				if (isInsTop) {
					poiBean.setCellData(detail.getInsName(), rowIdx, colIdx++);
				} else {
					poiBean.setCellData("", rowIdx, colIdx++);
				}

				//施設固定コード
				if (isInsTop) {
					poiBean.setCellData(detail.getInsNo(), rowIdx, colIdx++);
				} else {
					poiBean.setCellData("", rowIdx, colIdx++);
				}

				//特約店名称
				poiBean.setCellData(ws.getTmsTytemName(), rowIdx, colIdx++);


				//TMS特約店コード
				poiBean.setCellData(ws.getTmsTytenCd(), rowIdx, colIdx++);

				//Y価ベース
				poiBean.setCellData(ws.getBaseY(), rowIdx, colIdx++);

				//S価ベース
				if (ryutsu) {
					poiBean.setCellData(ws.getBaseT(), rowIdx, colIdx++);
				} else {
					poiBean.setCellData("", rowIdx, colIdx++);
				}

				// 明細Y価ベース合計
				if (ws.getBaseY() != null) {
					sumBaseValueY = sumBaseValueY + ws.getBaseY();
					allSumBaseValueY = allSumBaseValueY + ws.getBaseY();
				}
				// 明細S価ベース合計
				if (ws.getBaseT() != null) {
					sumBaseValueS = sumBaseValueS + ws.getBaseT();
					allSumBaseValueS = allSumBaseValueS + ws.getBaseT();
				}
				isInsTop = false;
				rowIdx++;
			}
			poiBean.addRows(rowIdx, 1);
			// 列インデックス
			colIdx = COL_IDX_INS_SUM;

			// 「施設合計」
			poiBean.setCellData(TEXT_INS_SUM, rowIdx,colIdx++);
			colIdx++;
			// 施設ごとのY価ベース合計
			poiBean.setCellData(sumBaseValueY, rowIdx, colIdx++);
			// 施設ごとのS価ベース合計
			if (ryutsu) {
				poiBean.setCellData(sumBaseValueS, rowIdx, colIdx++);
			} else {
				poiBean.setCellData("", rowIdx, colIdx++);
			}
			rowIdx++;
		}
		// 全Y価ベース合計
		poiBean.setCellData(allSumBaseValueY, ROW_IDX_ALL_SUM, COL_IDX_Y);
		// 全S価ベース合計
		if(ryutsu) {
			poiBean.setCellData(allSumBaseValueS, ROW_IDX_ALL_SUM, COL_IDX_S);
		} else {
			poiBean.setCellData("", ROW_IDX_ALL_SUM, COL_IDX_S);
		}
		// 印刷範囲を設定
		final int rowIdxPrintEnd = rowIdx;
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
	}

	/**
	 * 表示条件の文字列を作成する
	 * @return 表示条件の文字列
	 */
	private String createDisplayConditionTxt() {
		StringBuffer txt = new StringBuffer();
		txt.append(TEXT_CONDITION);

		// 組織・担当者名
		StringBuffer orgMrNm = new StringBuffer();

		// 施設コードが指定されている場合
		if (StringUtils.isNotEmpty(manageInsWsPlanScDto.getInsNo())) {
			if (headerDto.getInsMstResultDto() != null) {
				if (headerDto.getTargetMR() != null) {
					JgiMst jgi = headerDto.getTargetMR();
					try {
						if (jgi.getJgiNo() != null) {

							// 担当者Noが設定されている場合、担当者名を取得
							orgMrNm.insert(0, " " +  jgiMstRealDao.searchReal(jgi.getJgiNo()).getJgiName());

						}
						if (jgi.getSosCd3() != null) {

							// 組織コード(営業所)が設定されている場合、営業所名を取得
							orgMrNm.insert(0,  " " + sosMstDAO.searchReal(jgi.getSosCd3()).getBumonSeiName());

						}
						if (jgi.getSosCd2() != null) {

							// 組織コード(支店)が設定されている場合、支店名を取得
							orgMrNm.insert(0,  " " + sosMstDAO.searchReal(jgi.getSosCd2()).getBumonSeiName());

						}
					}catch (DataNotFoundException e) {
						final String errMsg = "担当者情報が存在しない：";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
					}
				}
			}
		}else {
			try {
				if (manageInsWsPlanScDto.getJgiNo() != null) {

					// 担当者Noが設定されている場合、担当者名を取得
					orgMrNm.insert(0,  " " + jgiMstRealDao.searchReal(manageInsWsPlanScDto.getJgiNo()).getJgiName());

				}
				if (manageInsWsPlanScDto.getSosCd3() != null) {

					// 組織コード(営業所)が設定されている場合、営業所名を取得
					orgMrNm.insert(0,  " " + sosMstDAO.searchReal(manageInsWsPlanScDto.getSosCd3()).getBumonSeiName());

				}
				if (manageInsWsPlanScDto.getSosCd2() != null) {

					// 組織コード(支店)が設定されている場合、支店名を取得
					orgMrNm.insert(0, " " + sosMstDAO.searchReal(manageInsWsPlanScDto.getSosCd2()).getBumonSeiName());

				}
			}catch (DataNotFoundException e) {
				final String errMsg = "担当者情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		txt.append(TEXT_ORG_MR);
		txt.append(orgMrNm);

		//カテゴリ
		DpmCCdMst category = new DpmCCdMst();
		try {
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(),manageInsWsPlanScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + manageInsWsPlanScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_CATEGORY).append(category.getDataName());

		// 品目
		MasterManagePlannedProd prod = new MasterManagePlannedProd();
		try {
			prod = plannedProdDao.search(manageInsWsPlanScDto.getProdCode());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目に、「" + manageInsWsPlanScDto.getProdCode() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_PROD).append(prod.getProdName());

		//対象区分
		DpmCCdMst insType = new DpmCCdMst();;
		try {
			insType = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.IT.getDbValue(), manageInsWsPlanScDto.getInsType().getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + manageInsWsPlanScDto.getInsType().getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_INS_TYPE).append(insType.getDataName());

		//計画
		final PlanData planData = manageInsWsPlanScDto.getPlanData();
		txt.append("  ").append(TEXT_PLAN_DATA);
		if ("0".equals(planData.getDbValue())) {
			txt.append(TEXT_PLAN_EXIST);
		} else if("1".equals(planData.getDbValue())) {
			txt.append(TEXT_ALL_INS);
		}

		//施設
		String insName = "";
		if (StringUtils.isNotEmpty(manageInsWsPlanScDto.getInsNo())) {
			try {
				insName = insMstRealDao.searchReal(manageInsWsPlanScDto.getInsNo()).getInsAbbrName();
			} catch (DataNotFoundException e) {
				final String errMsg = "組織マスタに、「" + manageInsWsPlanScDto.getInsNo() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}
		txt.append("  ").append(TEXT_INSTITUTION).append(insName);

		//特約店
		String tytenName = "";
		if (StringUtils.isNotEmpty(manageInsWsPlanScDto.getTmsTytenCd())) {
			try {
				tytenName = tytenMstUnReadDao.searchRealRef(
						StringUtils.rightPad(manageInsWsPlanScDto.getTmsTytenCd(), TMS_TYTEN_CD_LENGTH, "0")).getTmsTytenMeiKj();
			} catch (DataNotFoundException e) {
				final String errMsg = "特約店基本統合ビューに、「" + manageInsWsPlanScDto.getTmsTytenCd() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}
		txt.append("  ").append(TEXT_DEALER).append(tytenName);

		return txt.toString();
	}

	/**
	 * 組織・担当者名の文字列を作成する
	 * @return 組織・担当者名の文字列
	 */
	private String createOrgMrNm() {

		StringBuffer txt = new StringBuffer();
		// 組織・担当者名
		StringBuffer orgMrNm = new StringBuffer();

		// 施設コードが指定されている場合
		if (StringUtils.isNotEmpty(manageInsWsPlanScDto.getInsNo())) {
			if (headerDto.getInsMstResultDto() != null) {
				if (headerDto.getTargetMR() != null) {
					JgiMst jgi = headerDto.getTargetMR();
					try {
						if (jgi.getJgiNo() != null) {

							// 担当者Noが設定されている場合、担当者名を取得
							orgMrNm.insert(0, jgiMstRealDao.searchReal(jgi.getJgiNo()).getJgiName());

						}
						if (jgi.getSosCd3() != null) {

							// 組織コード(営業所)が設定されている場合、営業所名を取得
							orgMrNm.insert(0, sosMstDAO.searchReal(jgi.getSosCd3()).getBumonSeiName());

						}
						if (jgi.getSosCd2() != null) {

							// 組織コード(支店)が設定されている場合、支店名を取得
							orgMrNm.insert(0, sosMstDAO.searchReal(jgi.getSosCd2()).getBumonSeiName());

						}
					}catch (DataNotFoundException e) {
						final String errMsg = "担当者情報が存在しない：";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
					}
				}
			}
		}else {
			try {
				if (manageInsWsPlanScDto.getJgiNo() != null) {

					// 担当者Noが設定されている場合、担当者名を取得
					orgMrNm.insert(0, jgiMstRealDao.searchReal(manageInsWsPlanScDto.getJgiNo()).getJgiName());

				}
				if (manageInsWsPlanScDto.getSosCd3() != null) {

					// 組織コード(営業所)が設定されている場合、営業所名を取得
					orgMrNm.insert(0, sosMstDAO.searchReal(manageInsWsPlanScDto.getSosCd3()).getBumonSeiName());

				}
				if (manageInsWsPlanScDto.getSosCd2() != null) {

					// 組織コード(支店)が設定されている場合、支店名を取得
					orgMrNm.insert(0, sosMstDAO.searchReal(manageInsWsPlanScDto.getSosCd2()).getBumonSeiName());

				}
			}catch (DataNotFoundException e) {
				final String errMsg = "担当者情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		txt.append(orgMrNm);

		return txt.toString();
	}

}
