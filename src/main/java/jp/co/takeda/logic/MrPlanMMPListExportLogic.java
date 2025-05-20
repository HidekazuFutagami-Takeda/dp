package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dto.MrPlanMMPMrDto;
import jp.co.takeda.dto.MrPlanMMPProdDto;
import jp.co.takeda.dto.MrPlanMMPTeamDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.MathUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 担当者別計画担当者別品目別一覧のExcelファイルを生成するロジッククラス
 *
 * @author stakeuchi
 */
@Deprecated
public class MrPlanMMPListExportLogic {

	// -------------------------------------
	// インデックス定数(10人用・15人用共通)
	// -------------------------------------
	// [行番号] 現在日付
	private static final int ROW_IDX_DATE = 0;

	// [行番号] チーム名
	private static final int ROW_IDX_TEAM = 2;

	// [行番号] 担当者名
	private static final int ROW_IDX_MR = 4;

	// [行番号] 合計開始行
	private static final int ROW_IDX_START_SUM = 5;

	// [行番号] 合計最終行
	private static final int ROW_IDX_END_SUM = 36;

	// [行番号] UH開始行
	private static final int ROW_IDX_START_UH = 37;

	// [行番号] UH最終行
	private static final int ROW_IDX_END_UH = 68;

	// [行番号] P開始行
	private static final int ROW_IDX_START_P = 69;

	// [行番号] P最終行
	private static final int ROW_IDX_END_P = 100;

	// [列番号] 品目名
	private static final int COL_IDX_PROD = 1;

	// [列番号] 計画金額開始列
	private static final int COL_IDX_START_PLAN = 2;

	// -------------------------
	// インデックス定数(10人用)
	// -------------------------
	// [列番号] 現在日付
	private static final int COL_IDX_10_DATE = 22;

	// [列番号] 前同比開始列
	private static final int COL_IDX_10_START_RATE = 13;

	// [列番号] 前同比終了列
	private static final int COL_IDX_10_END_RATE = 23;

	// -------------------------
	// インデックス定数(15人用)
	// -------------------------
	// [列番号] 現在日付
	private static final int COL_IDX_15_DATE = 32;

	// [列番号] 前同比開始列
	private static final int COL_IDX_15_START_RATE = 18;

	// [列番号] 前同比終了列
	private static final int COL_IDX_15_END_RATE = 33;

	/**
	 * 出力ファイル名ヘッダ
	 */
	private static final String OUTPUT_FILE_NAME_HEADER = "MMPList";

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 品目一覧(品目ソート順)
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 出力情報DTOリスト
	 */
	private final List<MrPlanMMPTeamDto> teamDtoList;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param sosCd3 組織コード(営業所)
	 * @param systemDate システム日付
	 * @param plannedProdList 品目一覧(品目ソート順)
	 * @param dtoList 出力情報DTOリスト
	 */
	public MrPlanMMPListExportLogic(String templatePath, String sosCd3, Date systemDate, List<PlannedProd> plannedProdList, List<MrPlanMMPTeamDto> teamDtoList) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (systemDate == null) {
			final String errMsg = "システム日付がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.isEmpty()) {
			final String errMsg = "品目一覧がNull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (teamDtoList == null || teamDtoList.isEmpty()) {
			final String errMsg = "チーム情報DTOリストがNull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (MrPlanMMPTeamDto teamDto : teamDtoList) {
			if (teamDto.getMrDtoList() == null || teamDto.getMrDtoList().isEmpty()) {
				final String errMsg = "担当者情報DTOがNull、またはサイズ0";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			for (MrPlanMMPMrDto mrDto : teamDto.getMrDtoList()) {
				if (mrDto.getProdDtoList() == null || mrDto.getProdDtoList().isEmpty()) {
					final String errMsg = "品目情報DTOがNull、またはサイズ0";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
				}
				// mrDto.getProdDtoList()には1品目につきUH・P・合計の3つが入っている
				if (plannedProdList.size() * 3 != mrDto.getProdDtoList().size()) {
					final String errMsg = "品目情報DTOのサイズが不正";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
				}
			}
		}
		// ----------------------
		// フィールドにセット
		// ----------------------
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.plannedProdList = plannedProdList;
		this.teamDtoList = teamDtoList;
		this.outputFileName = createOutputFileName(sosCd3, systemDate);
	}

	/**
	 * 出力ファイル名の生成
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param systemDate システム日付
	 * @return 出力ファイル名
	 */
	private String createOutputFileName(String sosCd3, Date systemDate) {
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		return OUTPUT_FILE_NAME_HEADER + "_" + sosCd3 + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xls";
		return OUTPUT_FILE_NAME_HEADER + "_" + sosCd3 + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xlsx";
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
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
			final String errMsg = "担当者別計画担当者別品目別一覧書込み時にIO例外が発生";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
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
		int sheetIdx = 2;
		for (MrPlanMMPTeamDto teamDto : teamDtoList) {
			// 担当者が[10人以下]であれば10人用テンプレートを使用
			if (teamDto.getMrDtoList().size() <= 10) {
				poiBean.cloneSheet(0);
				poiBean.setWorkSheetIdx(sheetIdx);

				// 品目情報のセット
				writeProdInfo(poiBean);

				// [10人用] ヘッダ情報のセット
				writeBaseInfo(poiBean, teamDto, TemplateType.TEMPLATE_10);

				// [10人用] 担当者情報のセット
				writeListInfo(poiBean, teamDto, TemplateType.TEMPLATE_10);

				// [10人用] 比率合計値のセット
				writeSumRatio(poiBean, teamDto, TemplateType.TEMPLATE_10);
			}
			// 担当者が[11人以上15人以下]であれば15人用テンプレートを使用
			else if (teamDto.getMrDtoList().size() <= 15) {
				poiBean.cloneSheet(1);
				poiBean.setWorkSheetIdx(sheetIdx);

				// 品目情報のセット
				writeProdInfo(poiBean);

				// [15人用] ヘッダ情報のセット
				writeBaseInfo(poiBean, teamDto, TemplateType.TEMPLATE_15);

				// [15人用] 担当者情報のセット
				writeListInfo(poiBean, teamDto, TemplateType.TEMPLATE_15);

				// [15人用] 比率合計値のセット
				writeSumRatio(poiBean, teamDto, TemplateType.TEMPLATE_15);
			}
			sheetIdx++;
			poiBean.setForceFormulaRecalculation(true);
		}
		// テンプレート削除
		poiBean.removeSheetAt(0);
		poiBean.removeSheetAt(0);
	}

	/**
	 * 品目名称列の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeProdInfo(POIBean poiBean) {
		int prodCount = 0;
		// 品目名称
		for (PlannedProd prod : plannedProdList) {
			final String prodName = prod.getProdName();
			poiBean.setCellData(prodName, ROW_IDX_START_SUM + prodCount, COL_IDX_PROD);
			poiBean.setCellData(prodName, ROW_IDX_START_UH + prodCount, COL_IDX_PROD);
			poiBean.setCellData(prodName, ROW_IDX_START_P + prodCount, COL_IDX_PROD);
			prodCount++;
		}
	}

	/**
	 * 基本情報の値をセルに書き込む
	 * <ul>
	 * <li>シート名</li>
	 * <li>現在日付</li>
	 * <li>チーム名</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 * @param teamDto チーム情報DTO
	 * @param type テンプレートの種類
	 */
	private void writeBaseInfo(POIBean poiBean, MrPlanMMPTeamDto teamDto, TemplateType type) {
		// テンプレート別インデックスのセット(デフォルト10人用)
		int colIdxDate = COL_IDX_10_DATE; // 現在日付列
		switch (type) {
			case TEMPLATE_15:
				colIdxDate = COL_IDX_15_DATE;
				break;
		}
		// シート名
		final String teamName = teamDto.getSosName();
		poiBean.setSheetName(teamName);
		// 現在日付
		poiBean.setCellData(systemDate, ROW_IDX_DATE, colIdxDate);
		// チーム名
		poiBean.setCellData(teamName, ROW_IDX_TEAM, COL_IDX_START_PLAN);
	}

	/**
	 * 担当者情報の値をセルに書き込む
	 * <ul>
	 * <li>担当者名</li>
	 * <li>UH・Pの計画値</li>
	 * <li>UH・P・合計の前同比</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 * @param teamDto チーム情報DTO
	 * @param type テンプレートの種類
	 */
	private void writeListInfo(POIBean poiBean, MrPlanMMPTeamDto teamDto, TemplateType type) {
		// テンプレート別インデックスのセット(デフォルト10人用)
		int colIdxStartRate = COL_IDX_10_START_RATE; // 前同比の開始列
		switch (type) {
			case TEMPLATE_15:
				colIdxStartRate = COL_IDX_15_START_RATE;
				break;
		}
		// 担当者情報のセット
		int userCount = 0;
		for (MrPlanMMPMrDto mrDto : teamDto.getMrDtoList()) {
			// 担当者名
			final String jgiName = mrDto.getJgiName();
			poiBean.setCellData(jgiName, ROW_IDX_MR, COL_IDX_START_PLAN + userCount);
			poiBean.setCellData(jgiName, ROW_IDX_MR, colIdxStartRate + userCount);
			// 品目情報のセット
			int rowCountSum = 0;
			int rowCountUH = 0;
			int rowCountP = 0;
			for (MrPlanMMPProdDto prodDto : mrDto.getProdDtoList()) {
				// 計画値
				final Long plannedValue = prodDto.getPlannedValue();
				// 前期実績
				final Long advancePeriod = prodDto.getAdvancePeriod();
				// 合計行のセット
				if (prodDto.getInsType() == null) {
					// 前同比のセット
					final Double lastSameRatio = MathUtil.calcRatio(plannedValue, advancePeriod);
					poiBean.setCellData(lastSameRatio, ROW_IDX_START_SUM + rowCountSum, colIdxStartRate + userCount);
					rowCountSum++;
				}
				// UH行のセット
				else if (prodDto.getInsType().equals(InsType.UH)) {
					// 計画値のセット
					poiBean.setCellData(plannedValue, ROW_IDX_START_UH + rowCountUH, COL_IDX_START_PLAN + userCount);
					// 前同比のセット
					final Double lastSameRatio = MathUtil.calcRatio(plannedValue, advancePeriod);
					poiBean.setCellData(lastSameRatio, ROW_IDX_START_UH + rowCountUH, colIdxStartRate + userCount);
					rowCountUH++;
				}
				// P行のセット
				else if (prodDto.getInsType().equals(InsType.P)) {
					// 計画値のセット
					poiBean.setCellData(plannedValue, ROW_IDX_START_P + rowCountP, COL_IDX_START_PLAN + userCount);
					// 前同比のセット
					final Double lastSameRatio = MathUtil.calcRatio(plannedValue, advancePeriod);
					poiBean.setCellData(lastSameRatio, ROW_IDX_START_P + rowCountP, colIdxStartRate + userCount);
					rowCountP++;
				}
			}
			userCount++;
		}
	}

	/**
	 * 比率合計値をセルに書き込む
	 * <ul>
	 * <li>UH・P・合計の品目別の前同比</li>
	 * <li>UH・P・合計の担当者別の前同比</li>
	 * <li>UH・P・合計の全品目・全担当者の前同比</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 * @param teamDto チーム情報DTO
	 * @param type テンプレートの種類
	 */
	private void writeSumRatio(POIBean poiBean, MrPlanMMPTeamDto teamDto, TemplateType type) {
		// テンプレート別インデックスのセット(デフォルト10人用)
		int colIdxStartRate = COL_IDX_10_START_RATE; // 前同比の開始列
		int colIdxEndRate = COL_IDX_10_END_RATE; // 前同比の終了列
		switch (type) {
			case TEMPLATE_15:
				colIdxStartRate = COL_IDX_15_START_RATE;
				colIdxEndRate = COL_IDX_15_END_RATE;
				break;
		}

		// 品目別合計列の値をセット
		int rowCount = 0;
		for (PlannedProd prod : plannedProdList) {
			Long plannedValueSum = null;
			Long advancePeriodSum = null;
			Long plannedValueUH = null;
			Long advancePeriodUH = null;
			Long plannedValueP = null;
			Long advancePeriodP = null;
			final String prodCode = prod.getProdCode();
			for (MrPlanMMPMrDto mrDto : teamDto.getMrDtoList()) {
				for (MrPlanMMPProdDto prodDto : mrDto.getProdDtoList()) {
					Long plannedValue = prodDto.getPlannedValue();
					Long advancePeriod = prodDto.getAdvancePeriod();
					if (StringUtils.equals(prodCode, prodDto.getProdCode())) {
						// 合計
						if (prodDto.getInsType() == null) {
							plannedValueSum = add(plannedValueSum, plannedValue);
							advancePeriodSum = add(advancePeriodSum, advancePeriod);
						}
						// UH
						else if (prodDto.getInsType().equals(InsType.UH)) {
							plannedValueUH = add(plannedValueUH, plannedValue);
							advancePeriodUH = add(advancePeriodUH, advancePeriod);
						}
						// P
						else if (prodDto.getInsType().equals(InsType.P)) {
							plannedValueP = add(plannedValueP, plannedValue);
							advancePeriodP = add(advancePeriodP, advancePeriod);
						}
					}
				}
			}
			// 合計
			final Double lastSameRatioSum = MathUtil.calcRatio(plannedValueSum, advancePeriodSum);
			poiBean.setCellData(lastSameRatioSum, ROW_IDX_START_SUM + rowCount, colIdxEndRate);
			// UH
			final Double lastSameRatioUH = MathUtil.calcRatio(plannedValueUH, advancePeriodUH);
			poiBean.setCellData(lastSameRatioUH, ROW_IDX_START_UH + rowCount, colIdxEndRate);
			// P
			final Double lastSameRatioP = MathUtil.calcRatio(plannedValueP, advancePeriodP);
			poiBean.setCellData(lastSameRatioP, ROW_IDX_START_P + rowCount, colIdxEndRate);
			rowCount++;
		}

		// 担当者別合計行のセット
		int userCount = 0;
		for (MrPlanMMPMrDto mrDto : teamDto.getMrDtoList()) {
			Long plannedValueSum = null;
			Long advancePeriodSum = null;
			Long plannedValueUH = null;
			Long advancePeriodUH = null;
			Long plannedValueP = null;
			Long advancePeriodP = null;
			for (MrPlanMMPProdDto prodDto : mrDto.getProdDtoList()) {
				Long plannedValue = prodDto.getPlannedValue();
				Long advancePeriod = prodDto.getAdvancePeriod();
				// 合計
				if (prodDto.getInsType() == null) {
					plannedValueSum = add(plannedValueSum, plannedValue);
					advancePeriodSum = add(advancePeriodSum, advancePeriod);
				}
				// UH
				else if (prodDto.getInsType().equals(InsType.UH)) {
					plannedValueUH = add(plannedValueUH, plannedValue);
					advancePeriodUH = add(advancePeriodUH, advancePeriod);
				}
				// P
				else if (prodDto.getInsType().equals(InsType.P)) {
					plannedValueP = add(plannedValueP, plannedValue);
					advancePeriodP = add(advancePeriodP, advancePeriod);
				}
			}
			// 合計
			final Double lastSameRatioSum = MathUtil.calcRatio(plannedValueSum, advancePeriodSum);
			poiBean.setCellData(lastSameRatioSum, ROW_IDX_END_SUM, colIdxStartRate + userCount);
			// UH
			final Double lastSameRatioUH = MathUtil.calcRatio(plannedValueUH, advancePeriodUH);
			poiBean.setCellData(lastSameRatioUH, ROW_IDX_END_UH, colIdxStartRate + userCount);
			// P
			final Double lastSameRatioP = MathUtil.calcRatio(plannedValueP, advancePeriodP);
			poiBean.setCellData(lastSameRatioP, ROW_IDX_END_P, colIdxStartRate + userCount);
			userCount++;
		}

		// 全品目・全担当者の集計をセット
		Long plannedValueSum = null;
		Long advancePeriodSum = null;
		Long plannedValueUH = null;
		Long advancePeriodUH = null;
		Long plannedValueP = null;
		Long advancePeriodP = null;
		for (MrPlanMMPMrDto mrDto : teamDto.getMrDtoList()) {
			for (MrPlanMMPProdDto prodDto : mrDto.getProdDtoList()) {
				Long plannedValue = prodDto.getPlannedValue();
				Long advancePeriod = prodDto.getAdvancePeriod();
				// 合計
				if (prodDto.getInsType() == null) {
					plannedValueSum = add(plannedValueSum, plannedValue);
					advancePeriodSum = add(advancePeriodSum, advancePeriod);
				}
				// UH
				else if (prodDto.getInsType().equals(InsType.UH)) {
					plannedValueUH = add(plannedValueUH, plannedValue);
					advancePeriodUH = add(advancePeriodUH, advancePeriod);
				}
				// P
				else if (prodDto.getInsType().equals(InsType.P)) {
					plannedValueP = add(plannedValueP, plannedValue);
					advancePeriodP = add(advancePeriodP, advancePeriod);
				}
			}
		}
		// 合計
		final Double lastSameRatioSum = MathUtil.calcRatio(plannedValueSum, advancePeriodSum);
		poiBean.setCellData(lastSameRatioSum, ROW_IDX_END_SUM, colIdxEndRate);
		// UH
		final Double lastSameRatioUH = MathUtil.calcRatio(plannedValueUH, advancePeriodUH);
		poiBean.setCellData(lastSameRatioUH, ROW_IDX_END_UH, colIdxEndRate);
		// P
		final Double lastSameRatioP = MathUtil.calcRatio(plannedValueP, advancePeriodP);
		poiBean.setCellData(lastSameRatioP, ROW_IDX_END_P, colIdxEndRate);
	}

	/**
	 * Long同士の加算を行う。
	 *
	 * <ul>
	 * <li>両方Nullの場合は加算元をそのまま返す(Null)。</li>
	 * <li>加算先がNullの場合は加算元をそのまま返す。</li>
	 * <li>加算元がNullの場合は加算先をそのまま返す。</li>
	 * <li>両方とも値が入っている場合に加算した値を返す。</li>
	 * </ul>
	 *
	 * @param value1 加算先
	 * @param value2 加算元
	 * @return 加算値
	 */
	private Long add(Long value1, Long value2) {
		if (value2 != null) {
			if (value1 == null) {
				return value2;
			}
			return value1 + value2;
		}
		return value1;
	}

	/**
	 * テンプレートの種類を表す定数クラス
	 *
	 * @author stakeuchi
	 */
	private enum TemplateType {
		// 10人用テンプレート
		TEMPLATE_10,
		// 15人用テンプレート
		TEMPLATE_15;
	}
}
