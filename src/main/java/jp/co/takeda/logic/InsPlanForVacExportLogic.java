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
//mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.JgiMstRealDao;
import jp.co.takeda.dao.JisCodeMstDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailAddrDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailInsDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JisCodeMst;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;


/**
 * (ワ)施設別計画一覧のExcelファイルを生成するロジッククラス
 *
 * @author bgp5974
 *
 */
public class InsPlanForVacExportLogic {

	/**
	 * 施設情報取得DAO
	 */
	private final InsMstRealDao insMstRealDao;

	/**
	 * 従業員情報取得DAO
	 */
	private final JgiMstRealDao jgiMstRealDao;

	/**
	 * 組織情報取得DAO
	 */
	private final SosMstRealDao sosMstDAO;

	/**
	 * JIS府県・市区町村取得DAO
	 */
	private final JisCodeMstDao jisCodeMstDao;

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
	 * (ワ)施設別計画編集画面の検索条件DTO
	 */
	private InsPlanForVacScDto insPlanForVacScDto;

	/**
	 * (ワ)施設別計画の検索結果
	 */
	private InsPlanForVacResultDto insPlanForVacResult;

	/**
	 * (ワ)施設別計画の検索結果集計
	 */
	private InsPlanForVacResultDetailTotalDto insPlanForVacResultDetailTotal;

	/**
	 * 上位計画合計列のヘッダ名
	 */
	private String upperPlanTotalHeader;

	/**
	 * 組織調整のヘッダ名
	 */
	private String tuneHeader;

	/**
	 *  (ワ)施設特約店別計画編集画面の検索ヘッダ
	 */
	private InsPlanForVacHeaderDto headerDto;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "施設別計画一覧_";

	/** シート名 */
	private static final String SHEET_NAME = "施設別計画一覧";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「組織・担当者」 */
	private static final String TEXT_ORG_MR = "担当者：";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ：";

	/** 文字列「品目」 */
	private static final String TEXT_PROD = "品目：";

	/** 文字列「対象区分」 */
	private static final String TEXT_ACTIVITY_TYPE = "重点先/一般先：";

	/** 文字列「計画」 */
	private static final String TEXT_PREFECTURE = "市区町村：";

	/** 文字列「施設」 */
	private static final String TEXT_INSTITUTION = "施設：";

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
//
	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 7;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [行番号] 担当者計画 */
	private static final int ROW_IDX_MR_PLAN = 4;

	/** [行番号] 担当者計画-施設計画 */
	private static final int ROW_IDX_MR_INS_DIFF = 5;

	/** [行番号] 施設計画計 */
	private static final int ROW_IDX_INS_SUM = 6;

	/** [列番号] B価 */
	private static final int COL_BASE_B = 3;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 8;

	/** [列番号] 市区町村計 */
	private static final int COL_IDX_MUN_SUM = 1;

	/** [列番号] B価ベース */
	private static final int COL_IDX_BASE_B = 3;

	/**
	 * コンストラクタ
	 */
	public InsPlanForVacExportLogic(String templatePath, Date systemDate, InsPlanForVacScDto insPlanForVacScDto, InsPlanForVacResultDto resultDto, InsPlanForVacResultDetailTotalDto totalDto,
			InsMstRealDao insMstDao, SosMstRealDao sosMstDAO, JgiMstRealDao jgiMstDao,CodeMasterDao codeMasterDao,MasterManagePlannedProdDao plannedProdDao, JisCodeMstDao jisCodeMstDao, InsPlanForVacHeaderDto headerDto) {
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.insPlanForVacScDto = insPlanForVacScDto;
		this.insPlanForVacResult = resultDto;
		this.insPlanForVacResultDetailTotal = totalDto;
		this.insMstRealDao = insMstDao;
		this.sosMstDAO = sosMstDAO;
		this.jgiMstRealDao = jgiMstDao;
		this.codeMasterDao = codeMasterDao;
		this.plannedProdDao = plannedProdDao;
		this.jisCodeMstDao = jisCodeMstDao;
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
			// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
			//POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
			//HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
			XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
			// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
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
	// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	//private void write(HSSFWorkbook workBook) {
	private void write(XSSFWorkbook workBook) {
	// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応

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
		if (insPlanForVacResult == null || insPlanForVacResult.getDetailList() == null || insPlanForVacResult.getDetailList().isEmpty()) {
			return;
		}

		// 明細行
		List<InsPlanForVacResultDetailAddrDto> detailList = insPlanForVacResult.getDetailList();

		// 市区町村ごとの先頭行であるか
		boolean isMunTop = false;
		// 市区町村計
		Long sumMunValueB = new Long(0);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;
		Long sumIns = new Long(0);

		for (InsPlanForVacResultDetailAddrDto detail : detailList) {
			isMunTop = true;
			sumMunValueB = new Long(0);
			// 行追加
			poiBean.addRows(rowIdx, detail.getInsPlanList().size());
			for (InsPlanForVacResultDetailInsDto insPlan : detail.getInsPlanList()) {

				// 列インデックス
				int colIdx = COL_IDX_START_LIST;

				//市区町村
				if (isMunTop) {
					poiBean.setCellData(detail.getShikuchosonMeiKj(), rowIdx, colIdx++);
				} else {
					poiBean.setCellData("", rowIdx, colIdx++);
				}

				// 施設名
				poiBean.setCellData(insPlan.getInsName(), rowIdx, colIdx++);
				// 施設コード
				poiBean.setCellData(insPlan.getInsNo(), rowIdx, colIdx++);
				// B価ベース
				poiBean.setCellData(insPlan.getBBaseValue(), rowIdx, colIdx++);

				// 施設合計
				if (insPlan.getBBaseValue() != null) {
					sumIns = sumIns + insPlan.getBBaseValue();
					sumMunValueB = sumMunValueB + insPlan.getBBaseValue();
				}
				isMunTop = false;
				rowIdx++;
			}
			poiBean.addRows(rowIdx, 1);
			// 「市区町村計」
			poiBean.setCellData(TEXT_MUN_SUM, rowIdx, COL_IDX_MUN_SUM);
			// 市区町村ごとのB価ベース合計
			poiBean.setCellData(sumMunValueB, rowIdx, COL_IDX_BASE_B);
		}

		// 表示施設計
		poiBean.setCellData(sumIns, ROW_IDX_INS_SUM, COL_BASE_B);

		// 担当者計画
		Long hideValue = new Long(0);
		if (insPlanForVacResultDetailTotal.getHideValue() != null) {
			hideValue = insPlanForVacResultDetailTotal.getHideValue();
		}
		poiBean.setCellData(sumIns + hideValue, ROW_IDX_MR_PLAN, COL_BASE_B);

		// 担当者計画 - 施設計画
		poiBean.setCellData(hideValue, ROW_IDX_MR_INS_DIFF, COL_BASE_B);

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + rowIdx;
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

		//担当者
		StringBuffer orgMrNm = new StringBuffer();
		// 施設コードが指定されている場合
		if (StringUtils.isNotEmpty(insPlanForVacScDto.getInsNo())) {
			if (headerDto != null) {
				if (headerDto.getJgiNo() != null) {

					try {
						JgiMst jgiMst  = null;
						if (headerDto.getJgiNo() != null) {

							jgiMst = jgiMstRealDao.searchReal(headerDto.getJgiNo());

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
				if (insPlanForVacScDto.getJgiNo() != null) {

					// 担当者Noが設定されている場合、担当者名を取得
					orgMrNm.insert(0, " " + jgiMstRealDao.searchReal(insPlanForVacScDto.getJgiNo()).getJgiName());

				}
				if (insPlanForVacScDto.getSosCd3() != null) {

					// 組織コード(営業所)が設定されている場合、営業所名を取得
					orgMrNm.insert(0, " " + sosMstDAO.searchReal(insPlanForVacScDto.getSosCd3()).getBumonSeiName());

				}
				if (insPlanForVacScDto.getSosCd2() != null) {

					// 組織コード(支店)が設定されている場合、支店名を取得
					orgMrNm.insert(0, " " + sosMstDAO.searchReal(insPlanForVacScDto.getSosCd2()).getBumonSeiName());

				}
			}catch (DataNotFoundException e) {
				final String errMsg = "担当者情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}
		txt.append(TEXT_ORG_MR);
		txt.append(orgMrNm);

		// カテゴリ
		DpmCCdMst category = new DpmCCdMst();
		try {
			category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), insPlanForVacScDto.getProdCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + insPlanForVacScDto.getProdCategory() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("　").append(TEXT_CATEGORY).append(category.getDataName());

		// 品目
		MasterManagePlannedProd prod = new MasterManagePlannedProd();
		try {
			prod = plannedProdDao.search(insPlanForVacScDto.getProdCode());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目に、「" + insPlanForVacScDto.getProdCode() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		txt.append("　").append(TEXT_PROD).append(prod.getProdName());

		//重点先/一般先
		DpmCCdMst activityType = new DpmCCdMst();
		// 施設コードが指定されている場合
		if (StringUtils.isNotEmpty(insPlanForVacScDto.getInsNo())) {
			if (headerDto != null) {
				if (headerDto.getActivityType() != null) {
					try {
						activityType = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.ACT.getDbValue(), headerDto.getActivityType().getDbValue());
					}catch (DataNotFoundException e) {
						final String errMsg = "計画管理汎用マスタに、「" + headerDto.getActivityType().getDbValue() + "」コードが登録されていません。";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
					}
				}
			}
		}else {
			try {
				activityType = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.ACT.getDbValue(), insPlanForVacScDto.getActivityType().getDbValue());
			} catch (DataNotFoundException e) {
				final String errMsg = "計画管理汎用マスタに、「" + insPlanForVacScDto.getActivityType().getDbValue() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}
		txt.append("　").append(TEXT_ACTIVITY_TYPE).append(activityType.getDataName());

		//市区町村
		JisCodeMst addr = new JisCodeMst();
		// 施設コードが指定されている場合
		if (StringUtils.isNotEmpty(insPlanForVacScDto.getInsNo())) {
			if (headerDto != null) {
				if (headerDto.getActivityType() != null) {
					try {
						addr = jisCodeMstDao.search(headerDto.getAddrCodePref(), headerDto.getAddrCodeCity());
					}catch (DataNotFoundException e) {
						final String errMsg = "JIS府県・市区町村マスタに、JIS府県「" + headerDto.getAddrCodePref() + "」もしくはJIS市区町村「" + insPlanForVacScDto.getAddrCodeCity() + "」コードが登録されていません。";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
					}
				}
			}
		}else {
			try {
				addr = jisCodeMstDao.search(insPlanForVacScDto.getAddrCodePref(), insPlanForVacScDto.getAddrCodeCity());
			} catch (DataNotFoundException e) {
				final String errMsg = "JIS府県・市区町村マスタに、JIS府県「" + insPlanForVacScDto.getAddrCodePref() + "」もしくはJIS市区町村「" + insPlanForVacScDto.getAddrCodeCity() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}
		txt.append("　").append(TEXT_PREFECTURE).append(addr.getFukenMeiKj()).append("　").append(addr.getShikuchosonMeiKj());

		//施設
		String insName = "";
		if (StringUtils.isNotEmpty(insPlanForVacScDto.getInsNo())) {
			try {
				insName = insMstRealDao.searchReal(insPlanForVacScDto.getInsNo()).getInsAbbrName();
			} catch (DataNotFoundException e) {
				final String errMsg = "組織マスタに、「" + insPlanForVacScDto.getInsNo() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}
		txt.append("　").append(TEXT_INSTITUTION).append(insName);


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
		if (StringUtils.isNotEmpty(insPlanForVacScDto.getInsNo())) {
			if (headerDto != null) {
				if (headerDto.getJgiNo() != null) {

					try {
						JgiMst jgiMst  = null;
						if (headerDto.getJgiNo() != null) {

							jgiMst = jgiMstRealDao.searchReal(headerDto.getJgiNo());

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
				if (insPlanForVacScDto.getJgiNo() != null) {

					// 担当者Noが設定されている場合、担当者名を取得
					orgMrNm.insert(0, jgiMstRealDao.searchReal(insPlanForVacScDto.getJgiNo()).getJgiName());

				}
				if (insPlanForVacScDto.getSosCd3() != null) {

					// 組織コード(営業所)が設定されている場合、営業所名を取得
					orgMrNm.insert(0, sosMstDAO.searchReal(insPlanForVacScDto.getSosCd3()).getBumonSeiName());

				}
				if (insPlanForVacScDto.getSosCd2() != null) {

					// 組織コード(支店)が設定されている場合、支店名を取得
					orgMrNm.insert(0, sosMstDAO.searchReal(insPlanForVacScDto.getSosCd2()).getBumonSeiName());

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