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
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dto.InsPlanResultDetailDto;
import jp.co.takeda.dto.InsPlanResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanResultDto;
import jp.co.takeda.dto.InsPlanScDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;
/**
 * (医)施設別計画一覧のExcelファイルを生成するロジッククラス
 *
 * @author bgp5974
 *
 */
public class InsPlanExportLogic {

	/**
	 * (医)組織別計画編集画面の検索条件DTO
	 */
//	private final SosPlanScDto sosPlanScDto;

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
		 * (医)施設別計画編集画面の検索条件DTO
		 */
		private final InsPlanScDto insPlanScDto;

		/**
		 * (医)施設別計画の検索結果
		 */
		private InsPlanResultDto insPlanResult;

		/**
		 * 組織調整のヘッダ名
		 */
		private String tuneHeader;

		/**
		 *  (医)施設特約店別計画編集画面の検索ヘッダ
		 */
		private InsProdPlanResultHeaderDto headerDto;

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
		private static final String TEXT_INS_TYPE = "対象区分：";

		/** 文字列「計画」 */
		private static final String TEXT_PLAN_DATA = "計画：";

		/** 文字列「施設」 */
		private static final String TEXT_INSTITUTION = "施設：";

		/** 文字列「計画あり」 */
		private static final String TEXT_PLAN_EXIST = "計画あり";

		/** 文字列「全施設」 */
		private static final String TEXT_ALL_INS = "全施設";

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
		private static final int ROW_IDX_START_LIST = 7;

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
		 * @param templatePath
		 * @param insPlanScDto		(医)施設別計画編集画面の検索条件DTO
		 * @param insPlanResultDto (医)施設別計画の検索結果
		 * @param insMstRealDao		施設情報取得DAO
		 * @param SosMstDAO		組織情報取得DAO
		 * @param jgiMstDAO		従業員情報取得DAO
		 * @param sosJgiDto		組織・従業員検索の結果格納DTO
		 */
		public InsPlanExportLogic(String templatePath, Date systemDate, InsPlanScDto insPlanScDto, InsPlanResultDto resultDto, InsMstRealDao insMstDAO, SosMstRealDao sosMstDAO,
				JgiMstRealDao jgiMstDao, CodeMasterDao codeMasterDao, MasterManagePlannedProdDao plannedProdDao, InsProdPlanResultHeaderDto headerDto) {
			this.templatePath = templatePath;
			this.systemDate = systemDate;
			this.insPlanScDto = insPlanScDto;
			this.insPlanResult = resultDto;
			this.insMstRealDao = insMstDAO;
			this.sosMstDAO = sosMstDAO;
			this.jgiMstRealDao = jgiMstDao;
			this.codeMasterDao = codeMasterDao;
			this.plannedProdDao = plannedProdDao;
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
			if (insPlanResult == null || insPlanResult.getDetailList() == null || insPlanResult.getDetailList().isEmpty()) {
				return;
			}

			// 合計行
			InsPlanResultDetailTotalDto total = insPlanResult.getDetailTotal();

			// 明細行
			List<InsPlanResultDetailDto> detailList = insPlanResult.getDetailList();

			// 検索結果リストのサイズ分行を追加
			poiBean.addRows(ROW_IDX_START_LIST, detailList.size());

			// 合計行Y価ベース
			poiBean.setCellData(total.getYBaseValue(),4, 2);


			// 行インデックス
			int rowIdx = ROW_IDX_START_LIST;
			Long sumBaseValueY = new Long(0);

			for (InsPlanResultDetailDto detail : detailList) {

				// 列インデックス
				int colIdx = COL_IDX_START_LIST;

				// 施設名
				poiBean.setCellData(detail.getInsName(), rowIdx, colIdx++);

				// 施設コード
				poiBean.setCellData(detail.getInsNo(), rowIdx, colIdx++);

				// Y価ベース
				poiBean.setCellData(detail.getYBaseValue(), rowIdx, colIdx++);

				// 明細Y価ベース合計
				if (detail.getYBaseValue() != null) {
					sumBaseValueY = sumBaseValueY + detail.getYBaseValue();
					rowIdx++;
				}
			}
			//合計行Y価ベース-明細行Y価ベース
			if (total.getYBaseValue() == null) {
				poiBean.setCellData(-sumBaseValueY, 5,2);
			} else {
				poiBean.setCellData(total.getYBaseValue() - sumBaseValueY, 5,2);
			}
			//明細行Y価ベース合計
			poiBean.setCellData(sumBaseValueY,6,2);

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

		}

		/**
		 * 表示条件の文字列を作成する
		 * @return 表示条件の文字列
		 */
		private String createDisplayConditionTxt() {
			StringBuffer txt = new StringBuffer();

			txt.append(TEXT_CONDITION);

			// 担当者
			StringBuffer orgMrNm = new StringBuffer();

			// 施設コードが指定されている場合
			if (StringUtils.isNotEmpty(insPlanScDto.getInsNo())) {
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
					if (insPlanScDto.getJgiNo() != null) {

						// 担当者Noが設定されている場合、担当者名を取得
						orgMrNm.insert(0, " " + jgiMstRealDao.searchReal(insPlanScDto.getJgiNo()).getJgiName());

					}
					if (insPlanScDto.getSosCd3() != null) {

						// 組織コード(営業所)が設定されている場合、営業所名を取得
						orgMrNm.insert(0, " " + sosMstDAO.searchReal(insPlanScDto.getSosCd3()).getBumonSeiName());

					}
					if (insPlanScDto.getSosCd2() != null) {

						// 組織コード(支店)が設定されている場合、支店名を取得
						orgMrNm.insert(0, " " + sosMstDAO.searchReal(insPlanScDto.getSosCd2()).getBumonSeiName());

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
				category = codeMasterDao.searchCategoryByKbnAndCd(CodeMaster.CAT.getDbValue(), insPlanScDto.getProdCategory());
			} catch (DataNotFoundException e) {
				final String errMsg = "計画管理汎用マスタに、「" + insPlanScDto.getProdCategory() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
			txt.append(" ").append(TEXT_CATEGORY).append(category.getDataName());

			// 品目
			MasterManagePlannedProd prod = new MasterManagePlannedProd();
			try {
				prod = plannedProdDao.search(insPlanScDto.getProdCode());
			} catch (DataNotFoundException e) {
				final String errMsg = "計画対象品目に、「" + insPlanScDto.getProdCode() + "」コードが登録されていません。";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
			txt.append(" ").append(TEXT_PROD).append(prod.getProdName());

			//対象区分
			final InsType insType = insPlanScDto.getInsType();
			txt.append("　").append(TEXT_INS_TYPE).append(insType);

			//計画
			final PlanData planData = insPlanScDto.getPlanData();
			txt.append("  ").append(TEXT_PLAN_DATA);
			if ("0".equals(planData.getDbValue())) {
				txt.append(TEXT_PLAN_EXIST);
			} else if("1".equals(planData.getDbValue())) {
				txt.append(TEXT_ALL_INS);
			}

			//施設
			String insName = "";
			if (StringUtils.isNotEmpty(insPlanScDto.getInsNo())) {
				try {
					insName = insMstRealDao.searchReal(insPlanScDto.getInsNo()).getInsAbbrName();
				} catch (DataNotFoundException e) {
					final String errMsg = "組織マスタに、「" + insPlanScDto.getInsNo() + "」コードが登録されていません。";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
				}
			}
			txt.append("  ").append(TEXT_INSTITUTION).append(insName);

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
			if (StringUtils.isNotEmpty(insPlanScDto.getInsNo())) {
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
					if (insPlanScDto.getJgiNo() != null) {

						// 担当者Noが設定されている場合、担当者名を取得
						orgMrNm.insert(0, jgiMstRealDao.searchReal(insPlanScDto.getJgiNo()).getJgiName());

					}
					if (insPlanScDto.getSosCd3() != null) {

						// 組織コード(営業所)が設定されている場合、営業所名を取得
						orgMrNm.insert(0, sosMstDAO.searchReal(insPlanScDto.getSosCd3()).getBumonSeiName());

					}
					if (insPlanScDto.getSosCd2() != null) {

						// 組織コード(支店)が設定されている場合、支店名を取得
						orgMrNm.insert(0, sosMstDAO.searchReal(insPlanScDto.getSosCd2()).getBumonSeiName());

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

