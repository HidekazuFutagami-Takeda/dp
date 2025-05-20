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
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.ManageInsWsPlanForVacDetailAddrDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDetailInsDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDetailWsDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

public class InsWsPlanForVacExportLogic {

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
	 * 流通政策部かどうか
	 */
	private final boolean ryutsu;

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
	 * (ワ)施設特約店別計画編集画面の検索条件DTO
	 */
	private final ManageInsWsPlanForVacScDto manageInsWsPlanForVacScDto;

	/**
	 * (医)施設特約店別計画の検索結果
	 */
	private ManageInsWsPlanForVacDto manageInsWsPlanForVacDto;

	/**
	 *  (ワ)施設特約店別計画編集画面の検索ヘッダ
	 */
	private ManageInsWsPlanForVacHeaderDto headerDto;

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
	private static final String TEXT_ACTIVITY_TYPE = "重点先/一般先：";

	/** 文字列「計画」 */
	private static final String TEXT_PLAN_DATA = "市区町村：";

	/** 文字列「施設」 */
	private static final String TEXT_INSTITUTION = "施設：";

	/** 文字列「特約店」 */
	private static final String TEXT_DEALER = "特約店：";

	/** 文字列「施設合計」 */
	private static final String TEXT_INS_SUM = "施設合計";

	/** 文字列「市区町村計」 */
	private static final String TEXT_MUN_SUM = "市区町村計";

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
	private static final int COL_IDX_DATE = 7;

	/** [列番号] 施設合計 */
	private static final int COL_IDX_INS_SUM = 3;

	/** [列番号] 市区町村計 */
	private static final int COL_IDX_MUN_SUM = 1;

	/** [列番号] B価ベース */
	private static final int COL_IDX_BASE_B = 5;

	/** [列番号] S価ベース */
	private static final int COL_IDX_BASE_S = 6;

	/** [行番号] 全合計行 */
	private static final int ROW_IDX_ALL_SUM = 5;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 6;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

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
	 * @param sosMstDAO 組織情報取得DAO
	 */
	public InsWsPlanForVacExportLogic(String templatePath, Date systemDate, ManageInsWsPlanForVacScDto insWsPlanForVacScDto, ManageInsWsPlanForVacDto manageInsWsPlanForVacDto, SosMstRealDao sosMstDAO,
			JgiMstRealDao jgiMstDao, InsMstRealDao insMstDao, TmsTytenMstUnRealDao tytenMstDao, CodeMasterDao codeMasterDao,MasterManagePlannedProdDao plannedProdDao,
			boolean ryutsu, ManageInsWsPlanForVacHeaderDto headerDto) {
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.manageInsWsPlanForVacScDto = insWsPlanForVacScDto;
		this.manageInsWsPlanForVacDto = manageInsWsPlanForVacDto;
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
		if (manageInsWsPlanForVacDto == null || manageInsWsPlanForVacDto.getDetailList() == null || manageInsWsPlanForVacDto.getDetailList().isEmpty()) {
			return;
		}
		// 明細行
		List<ManageInsWsPlanForVacDetailAddrDto> detailList = manageInsWsPlanForVacDto.getDetailList();

		// T/Y変換率
		Double tyChangeRate = manageInsWsPlanForVacDto.getTyChangeRate();

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;
		// 全表示施設特約店計
		Long allSumBaseValueB = new Long(0);
		Long allSumBaseValueS = new Long(0);

		// 施設合計
		Long sumInsValueB = new Long(0);
		Long sumInsValueS = new Long(0);

		// 市区町村計
		Long sumMunValueB = new Long(0);
		Long sumMunValueS = new Long(0);

		// 施設ごとの先頭行であるか
		boolean isInsTop = false;
		// 市区町村ごとの先頭行であるか
		boolean isMunTop = false;

		// 列インデックス
		int colIdx = COL_IDX_START_LIST;
		// 市区町村
		for (ManageInsWsPlanForVacDetailAddrDto detail : detailList) {
			isMunTop = true;
			sumMunValueB = new Long(0);
			sumMunValueS = new Long(0);
			// 施設
			for(ManageInsWsPlanForVacDetailInsDto insWsPlan : detail.getInsWsPlanList()) {
				isInsTop = true;
				// 行追加
				poiBean.addRows(rowIdx, insWsPlan.getWsList().size());
				sumInsValueB = new Long(0);
				sumInsValueS = new Long(0);
				// 特約店
				for(ManageInsWsPlanForVacDetailWsDto ws : insWsPlan.getWsList()) {
					// 列インデックス
					colIdx = COL_IDX_START_LIST;
					// 市区町村
					if (isMunTop) {
						poiBean.setCellData(detail.getShikuchosonMeiKj(), rowIdx, colIdx++);
					} else {
						poiBean.setCellData("", rowIdx, colIdx++);
					}
					//施設名称
					if (isInsTop) {
						poiBean.setCellData(insWsPlan.getInsName(), rowIdx, colIdx++);
					} else {
						poiBean.setCellData("", rowIdx, colIdx++);
					}
					//施設固定コード
					if (isInsTop) {
						poiBean.setCellData(insWsPlan.getInsNo(), rowIdx, colIdx++);
					} else {
						poiBean.setCellData("", rowIdx, colIdx++);
					}
					//特約店名称
					poiBean.setCellData(ws.getTmsTytemName(), rowIdx, colIdx++);
					//TMS特約店コード
					poiBean.setCellData(ws.getTmsTytenCd(), rowIdx, colIdx++);
					//B価ベース
					Long bBaseValue = null;
					if (ws.getBaseT() != null) {
						bBaseValue = Math.round(ws.getBaseT() / tyChangeRate * 100);
					}
					poiBean.setCellData(bBaseValue, rowIdx, colIdx++);
					//S価ベース
					if (ryutsu) {
						poiBean.setCellData(ws.getBaseT(), rowIdx, colIdx++);
					} else {
						poiBean.setCellData("", rowIdx, colIdx++);
					}

					// 施設明細B価ベース合計
					if (bBaseValue != null) {
						sumInsValueB = sumInsValueB + bBaseValue;
						sumMunValueB = sumMunValueB + bBaseValue;
						allSumBaseValueB = allSumBaseValueB + bBaseValue;
					}
					// 施設明細S価ベース合計
					if (ws.getBaseT() != null) {
						sumInsValueS = sumInsValueS + ws.getBaseT();
						sumMunValueS = sumMunValueS + ws.getBaseT();
						allSumBaseValueS = allSumBaseValueS + ws.getBaseT();
					}
					isInsTop = false;
					isMunTop = false;
					rowIdx++;
				}
				poiBean.addRows(rowIdx, 1);
				// 「施設合計」
				poiBean.setCellData(TEXT_INS_SUM, rowIdx, COL_IDX_INS_SUM);
				// 施設ごとのB価ベース合計
				poiBean.setCellData(sumInsValueB, rowIdx, COL_IDX_BASE_B);
				// 施設ごとのS価ベース合計
				if (ryutsu) {
					poiBean.setCellData(sumInsValueS, rowIdx, COL_IDX_BASE_S);
				} else {
					poiBean.setCellData("", rowIdx, COL_IDX_BASE_S);
				}
				rowIdx++;
			}
			poiBean.addRows(rowIdx, 1);
			// 「市区町村計」
			poiBean.setCellData(TEXT_MUN_SUM, rowIdx, COL_IDX_MUN_SUM);
			// 市区町村ごとのB価ベース合計
			poiBean.setCellData(sumMunValueB, rowIdx, COL_IDX_BASE_B);
			// 市区町村ごとのS価ベース合計
			if (ryutsu) {
				poiBean.setCellData(sumMunValueS, rowIdx, COL_IDX_BASE_S);
			} else {
				poiBean.setCellData("", rowIdx, COL_IDX_BASE_S);
			}
			rowIdx++;
		}

		// 全B価ベース合計
		poiBean.setCellData(allSumBaseValueB, ROW_IDX_ALL_SUM, COL_IDX_BASE_B);
		// 全S価ベース合計
		if (ryutsu) {
			poiBean.setCellData(allSumBaseValueS, ROW_IDX_ALL_SUM, COL_IDX_BASE_S);
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
		if (StringUtils.isNotEmpty(manageInsWsPlanForVacScDto.getInsNo())) {
			if (headerDto.getInsMstResultDto() != null) {
				if (headerDto.getInsMstResultDto().getJgiNo() != null) {
					try {
						JgiMst jgiMst  = null;
						if (headerDto.getInsMstResultDto().getJgiNo() != null) {

							jgiMst = jgiMstRealDao.searchReal(headerDto.getInsMstResultDto().getJgiNo());

							// 担当者Noが設定されている場合、担当者名を取得
							orgMrNm.insert(0, " " + jgiMst.getJgiName());

						}
						if (jgiMst.getSosCd3() != null) {

							// 組織コード(営業所)が設定されている場合、営業所名を取得
							orgMrNm.insert(0, " " + sosMstDAO.searchReal(jgiMst.getSosCd3()).getBumonSeiName());

						}
						if (jgiMst.getSosCd2() != null) {

							// 組織コード(支店)が設定されている場合、支店名を取得
							orgMrNm.insert(0, " " + sosMstDAO.searchReal(jgiMst.getSosCd2()).getBumonSeiName());

						}
					}catch (DataNotFoundException e) {
						final String errMsg = "担当者情報が存在しない：";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
					}
				}
			}
		}else {
			try {
				if (manageInsWsPlanForVacScDto.getJgiNo() != null) {

					// 担当者Noが設定されている場合、担当者名を取得
					orgMrNm.insert(0, " " + jgiMstRealDao.searchReal(manageInsWsPlanForVacScDto.getJgiNo()).getJgiName());

				}
				if (manageInsWsPlanForVacScDto.getSosCd3() != null) {

					// 組織コード(営業所)が設定されている場合、営業所名を取得
					orgMrNm.insert(0, " " + sosMstDAO.searchReal(manageInsWsPlanForVacScDto.getSosCd3()).getBumonSeiName());

				}
				if (manageInsWsPlanForVacScDto.getSosCd2() != null) {

					// 組織コード(支店)が設定されている場合、支店名を取得
					orgMrNm.insert(0, " " + sosMstDAO.searchReal(manageInsWsPlanForVacScDto.getSosCd2()).getBumonSeiName());

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
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), manageInsWsPlanForVacScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + manageInsWsPlanForVacScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_CATEGORY).append(category.getDataName());

		// 品目
		MasterManagePlannedProd prod = new MasterManagePlannedProd();
		try {
			prod = plannedProdDao.search(manageInsWsPlanForVacScDto.getProdCode());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目に、「" + manageInsWsPlanForVacScDto.getProdCode() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("  ").append(TEXT_PROD).append(prod.getProdName());

		//重点先/一般先
		DpmCCdMst activityType = new DpmCCdMst();
		// 施設コードが指定されている場合
		if (StringUtils.isNotEmpty(manageInsWsPlanForVacScDto.getInsNo())) {
			if (headerDto.getInsMstResultDto() != null) {
				if (headerDto.getInsMstResultDto().getActivityType() != null) {
					try {
						activityType = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.ACT.getDbValue(), headerDto.getInsMstResultDto().getActivityType().getDbValue());
					}catch (DataNotFoundException e) {
						final String errMsg = "計画管理汎用マスタに、「" + manageInsWsPlanForVacScDto.getActivityType().getDbValue() + "」コードが登録されていません。";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
					}
				}
			}
		}else {
			try {
				activityType = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.ACT.getDbValue(), manageInsWsPlanForVacScDto.getActivityType().getDbValue());
			}catch (DataNotFoundException e) {
				final String errMsg = "計画管理汎用マスタに、「" + manageInsWsPlanForVacScDto.getActivityType().getDbValue() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}

		txt.append("  ").append(TEXT_ACTIVITY_TYPE).append(activityType.getDataName());


		//施設
		String insName = "";
		if (StringUtils.isNotEmpty(manageInsWsPlanForVacScDto.getInsNo())) {
			try {
				insName = insMstRealDao.searchReal(manageInsWsPlanForVacScDto.getInsNo()).getInsAbbrName();
			} catch (DataNotFoundException e) {
				final String errMsg = "組織マスタに、「" + manageInsWsPlanForVacScDto.getInsNo() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}
		txt.append("  ").append(TEXT_INSTITUTION).append(insName);

		//特約店
		String tytenName = "";
		if (StringUtils.isNotEmpty(manageInsWsPlanForVacScDto.getTmsTytenCd())) {
			try {
				tytenName = tytenMstUnReadDao.searchRealRef(
						StringUtils.rightPad(manageInsWsPlanForVacScDto.getTmsTytenCd(), TMS_TYTEN_CD_LENGTH, "0")).getTmsTytenMeiKj();
			} catch (DataNotFoundException e) {
				final String errMsg = "特約店基本統合ビューに、「" + manageInsWsPlanForVacScDto.getTmsTytenCd() + "」コードが登録されていません。";
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
		if (StringUtils.isNotEmpty(manageInsWsPlanForVacScDto.getInsNo())) {
			if (headerDto.getInsMstResultDto() != null) {
				if (headerDto.getInsMstResultDto().getJgiNo() != null) {
					try {
						JgiMst jgiMst  = null;
						if (headerDto.getInsMstResultDto().getJgiNo() != null) {

							jgiMst = jgiMstRealDao.searchReal(headerDto.getInsMstResultDto().getJgiNo());

							// 担当者Noが設定されている場合、担当者名を取得
							orgMrNm.insert(0, jgiMst.getJgiName());

						}
						if (jgiMst.getSosCd3() != null) {

							// 組織コード(営業所)が設定されている場合、営業所名を取得
							orgMrNm.insert(0, sosMstDAO.searchReal(jgiMst.getSosCd3()).getBumonSeiName());

						}
						if (jgiMst.getSosCd2() != null) {

							// 組織コード(支店)が設定されている場合、支店名を取得
							orgMrNm.insert(0, sosMstDAO.searchReal(jgiMst.getSosCd2()).getBumonSeiName());

						}
					}catch (DataNotFoundException e) {
						final String errMsg = "担当者情報が存在しない：";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
					}
				}
			}
		}else {
			try {
				if (manageInsWsPlanForVacScDto.getJgiNo() != null) {

					// 担当者Noが設定されている場合、担当者名を取得
					orgMrNm.insert(0, jgiMstRealDao.searchReal(manageInsWsPlanForVacScDto.getJgiNo()).getJgiName());

				}
				if (manageInsWsPlanForVacScDto.getSosCd3() != null) {

					// 組織コード(営業所)が設定されている場合、営業所名を取得
					orgMrNm.insert(0, sosMstDAO.searchReal(manageInsWsPlanForVacScDto.getSosCd3()).getBumonSeiName());

				}
				if (manageInsWsPlanForVacScDto.getSosCd2() != null) {

					// 組織コード(支店)が設定されている場合、支店名を取得
					orgMrNm.insert(0, sosMstDAO.searchReal(manageInsWsPlanForVacScDto.getSosCd2()).getBumonSeiName());

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
