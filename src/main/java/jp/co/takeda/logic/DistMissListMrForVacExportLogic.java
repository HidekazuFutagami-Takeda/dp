package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

//mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.context.MessageSource;

/**
 * (ワ)施設特約店別計画配分ミスリスト(担当者計画)のExcelファイルを生成するロジッククラス
 *
 * @author stakeuchi
 */
public class DistMissListMrForVacExportLogic {

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/**
	 * シート名
	 */
	private static final String SHEET_NAME = "配分ミスリスト";

	/**
	 * 対象計画名
	 */
	private static final String PLAN_NAME = "施設特約店別計画配分（担当者計画）";

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	/** [行番号] 対象計画名 */
	private static final int ROW_IDX_PLAN = 0;

	/** [列番号] 対象計画名 */
	private static final int COL_IDX_PLAN = 0;

	/** [行番号] 現在日付 */
	private static final int ROW_IDX_DATE = 0;

	/** [列番号] 現在日付 */
	private static final int COL_IDX_DATE = 5;

	/** [行番号] 汎用項目情報(支店名、営業所名、配分処理開始日時) */
	private static final int ROW_IDX_INFO = 1;

	/** [列番号] 汎用項目情報(支店名、営業所名、配分処理開始日時) */
	private static final int COL_IDX_INFO = 0;

	/** [行番号] ミスリスト開始行 */
	private static final int ROW_IDX_START_LIST = 3;

	/** [列番号] ミスリスト開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 5;

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 出力ファイル情報
	 */
	private final OutputFile outputFile;

	/**
	 * 担当者別計画配分ミスのリスト
	 */
	private final List<MrDistMiss> mrDistMissList;

	/**
	 * メッセージソース
	 */
	private MessageSource messageSource;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param outputFile 出力ファイル情報
	 * @param mrDistMissList 組織別計画配分ミスのリスト
	 * @param messageSource メッセージソース
	 */
	public DistMissListMrForVacExportLogic(String templatePath, Date systemDate, OutputFile outputFile, List<MrDistMiss> mrDistMissList, MessageSource messageSource) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isEmpty(templatePath)) {
			final String errMsg = "テンプレートパスがNull、または空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (systemDate == null) {
			final String errMsg = "システム日付がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (outputFile == null) {
			final String errMsg = "出力ファイル情報がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (messageSource == null) {
			final String errMsg = "メッセージソースがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// フィールドにセット
		// ----------------------
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.outputFile = outputFile;
		this.mrDistMissList = mrDistMissList;
		this.messageSource = messageSource;
		this.outputFileName = outputFile.getOutputFileName();
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
	 * ヘッダ情報の値をセルに書き込む
	 * <ul>
	 * <li>シート名</li>
	 * <li>対象計画名</li>
	 * <li>現在日付</li>
	 * <li>汎用項目</li>
	 * </ul>
	 * 
	 * @param poiBean POI設定BEAN
	 */
	private void writeHeadInfo(POIBean poiBean) {
		// シート名
		poiBean.setSheetName(SHEET_NAME);
		// 対象計画名
		poiBean.setCellData(PLAN_NAME, ROW_IDX_PLAN, COL_IDX_PLAN);
		// 現在日付
		poiBean.setCellData(systemDate, ROW_IDX_DATE, COL_IDX_DATE);
		// 汎用項目
		poiBean.setCellData(outputFile.getFreeData(), ROW_IDX_INFO, COL_IDX_INFO);
	}

	/**
	 * リスト情報の値をセルに書き込む。
	 * <ul>
	 * <li>チーム</li>
	 * <li>担当者</li>
	 * <li>品目コード</li>
	 * <li>品目名</li>
	 * <li>対象区分</li>
	 * <li>計画</li>
	 * <li>ミス内容</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo(POIBean poiBean) {
		// ミスリストがNull,または空の場合はReturn
		if (mrDistMissList == null || mrDistMissList.isEmpty()) {
			return;
		}

		// リストのサイズ分行を追加
		poiBean.addRows(ROW_IDX_START_LIST, mrDistMissList.size());

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		for (MrDistMiss mrDistMiss : mrDistMissList) {
			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// 担当者
			final String jgiName = mrDistMiss.getJgiName();
			poiBean.setCellData(jgiName, rowIdx, colIdx++);

			// 品目名
			final String prodName = mrDistMiss.getProdName();
			poiBean.setCellData(prodName, rowIdx, colIdx++);

			// 計画値
			final Long plannedValue = ConvertUtil.parseMoneyToThousandUnit(mrDistMiss.getPlannedValue());
			poiBean.setCellData(plannedValue, rowIdx, colIdx++);

			// 積上げ値+差額
			final Long diffValue = ConvertUtil.parseMoneyToThousandUnit(mrDistMiss.getDiffValue());
			final Long stackValue = MathUtil.subEx(plannedValue, diffValue);
			poiBean.setCellData(stackValue, rowIdx, colIdx++);
			poiBean.setCellData(diffValue, rowIdx, colIdx++);

			// ミス内容
			final String messageCode = mrDistMiss.getMessageCode();
			final String message = messageSource.getMessage(messageCode, null, Locale.getDefault());
			poiBean.setCellData(message, rowIdx, colIdx++);

			rowIdx++;
		}

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + mrDistMissList.size();
		final short fitWidth = 1; // 横は1固定
		short fitHeigth = 0; // 縦は設定しない
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}
}
