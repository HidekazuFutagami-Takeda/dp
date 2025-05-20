package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
//mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.context.MessageSource;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.model.SosDistMiss;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * (医)特約店別計画配分ミスリスト(営業所計画)のExcelファイルを生成するロジッククラス
 *
 * @author stakeuchi
 */
public class DistMissListWsExportLogic {

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/**
	 * 対象計画名
	 */
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
	private static final String PLAN_NAME = "特約店別計画配分（エリア計画）";
//	private static final String PLAN_NAME = "特約店別計画配分（営業所計画）";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	// [行番号] 対象計画名
	private static final int ROW_IDX_PLAN = 0;

	// [列番号] 対象計画名
	private static final int COL_IDX_PLAN = 0;

	// [行番号] 現在日付
	private static final int ROW_IDX_DATE = 0;

	// [列番号] 現在日付
	private static final int COL_IDX_DATE = 5;

	// [行番号] 汎用項目情報(支店名、営業所名、配分処理開始日時)
	private static final int ROW_IDX_INFO = 1;

	// [列番号] 汎用項目情報(支店名、営業所名、配分処理開始日時)
	private static final int COL_IDX_INFO = 0;

	// [行番号] ミスリスト開始行
	private static final int ROW_IDX_START_LIST = 3;

	// [列番号] ミスリスト開始列
	private static final int COL_IDX_START_LIST = 0;

	// [行番号] 印刷開始行
	private static final int ROW_IDX_PRINT_START = 0;

	// [列番号] 印刷開始列
	private static final int COL_IDX_PRINT_START = 0;

	// [列番号] 印刷終了列
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
	 * 組織別計画配分ミスのリスト
	 */
	private final List<SosDistMiss> sosDistMissList;

	/**
	 * 組織情報DAO
	 */
	private final SosMstDAO sosMstDAO;

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
	 * @param sosDistMissList 組織別計画配分ミスのリスト
	 * @param sosMstDAO 組織情報DAO
	 * @param messageSource メッセージソース
	 */
	public DistMissListWsExportLogic(String templatePath, Date systemDate, OutputFile outputFile, List<SosDistMiss> sosDistMissList, SosMstDAO sosMstDAO,
		MessageSource messageSource) {
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
		if (sosMstDAO == null) {
			final String errMsg = "組織情報DAOがNull";
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
		this.sosDistMissList = sosDistMissList;
		this.sosMstDAO = sosMstDAO;
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

		// ミスリストがNull,または空の場合はヘッダ基本情報のみ書込みReturn
		if (sosDistMissList == null || sosDistMissList.isEmpty()) {
			writeHeadInfo(poiBean);
			return;
		}

		// 組織コードをキーに組織別計画配分ミスのリストを保持
		Map<String, List<SosDistMiss>> missMap = new LinkedHashMap<String, List<SosDistMiss>>();
		for (SosDistMiss sosDistMiss : sosDistMissList) {
			final String sosCd = sosDistMiss.getSosCd();
			if (missMap.containsKey(sosCd)) {
				List<SosDistMiss> missList = missMap.get(sosCd);
				missList.add(sosDistMiss);
			} else {
				List<SosDistMiss> missList = new ArrayList<SosDistMiss>();
				missList.add(sosDistMiss);
				missMap.put(sosCd, missList);
			}
		}

		// リスト情報のセット
		writeListInfo(poiBean, missMap);
	}

	/**
	 * ヘッダ基本情報の値をセルに書き込む。
	 * <ul>
	 * <li>対象計画名</li>
	 * <li>現在日付</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeHeadInfo(POIBean poiBean) {
		// 対象計画名
		poiBean.setCellData(PLAN_NAME, ROW_IDX_PLAN, COL_IDX_PLAN);
		// 現在日付
		poiBean.setCellData(systemDate, ROW_IDX_DATE, COL_IDX_DATE);
	}

	/**
	 * ヘッダ詳細情報の値をセルに書き込む。
	 * <ul>
	 * <li>対象計画名</li>
	 * <li>現在日付</li>
	 * <li>汎用項目</li>
	 * <li>シート名</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeHeadInfoDetail(POIBean poiBean, String sosCd) {
		// ヘッダ基本情報
		writeHeadInfo(poiBean);

		// 汎用項目・シート名
		final SosMst sosMst = searchSosMst(sosCd);
		if (sosMst != null) {
			final String sitenName = searchSosMst(sosMst.getUpSosCd()).getBumonSeiName();
			final String eigyoName = sosMst.getBumonSeiName();
			final String freeData = sitenName + "　" + eigyoName + "　" + outputFile.getFreeData();
			poiBean.setCellData(freeData, ROW_IDX_INFO, COL_IDX_INFO);
			poiBean.setSheetName(eigyoName);
		}
	}

	/**
	 * リスト情報の値をセルに書き込む
	 * <ul>
	 * <li>品目コード</li>
	 * <li>品目名</li>
	 * <li>対象区分</li>
	 * <li>計画</li>
	 * <li>ミス内容</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 * @param missMap 組織コード、配分ミスリストのMAP
	 */
	private void writeListInfo(POIBean poiBean, Map<String, List<SosDistMiss>> missMap) {
		if (missMap == null || missMap.isEmpty()) {
			return;
		}

		// 組織数分シートを作成
		for (int i = 1; i < missMap.entrySet().size(); i++) {
			// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
			//poiBean.cloneSheet(0);
			poiBean.cloneSheet(i-1);
			// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		}

		// シートインデックス
		int sheetIdx = 0;

		for (Entry<String, List<SosDistMiss>> entry : missMap.entrySet()) {

			// シートのセット
			poiBean.setWorkSheetIdx(sheetIdx++);

			// ヘッダ詳細情報を更新
			writeHeadInfoDetail(poiBean, entry.getKey());

			// リストのサイズ分,行を追加
			poiBean.addRows(ROW_IDX_START_LIST, entry.getValue().size());

			// リストの現在行判定インデックス
			int rowIdx = ROW_IDX_START_LIST;

			for (SosDistMiss sosDistMiss : entry.getValue()) {

				// 列インデックス
				int colIdx = COL_IDX_START_LIST;

				// 品目名
				final String prodName = sosDistMiss.getProdName();
				poiBean.setCellData(prodName, rowIdx, colIdx++);

				// 対象区分
				final String insType = sosDistMiss.getInsType().toString();
				poiBean.setCellData(insType, rowIdx, colIdx++);

				// 計画
				final Long plannedValue = ConvertUtil.parseMoneyToThousandUnit(sosDistMiss.getPlannedValue());
				poiBean.setCellData(plannedValue, rowIdx, colIdx++);

				// 積上げ値+差額
				final Long diffValue = ConvertUtil.parseMoneyToThousandUnit(sosDistMiss.getDiffValue());
				final Long stackValue = MathUtil.subEx(plannedValue, diffValue);
				poiBean.setCellData(stackValue, rowIdx, colIdx++);
				poiBean.setCellData(diffValue, rowIdx, colIdx++);

				// ミス内容
				final String messageCode = sosDistMiss.getMessageCode();
				final String message = messageSource.getMessage(messageCode, null, Locale.getDefault());
				poiBean.setCellData(message, rowIdx, colIdx++);

				rowIdx++;
			}

			// 印刷範囲を設定
			final int rowIdxPrintEnd = ROW_IDX_START_LIST + entry.getValue().size();
			final short fitWidth = 1; // 横は1固定
			short fitHeigth = 0; // 縦は設定しない
			poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
		}
	}

	/**
	 * 組織情報を検索する。
	 *
	 * @param sosCd 組織コード
	 * @return 組織情報モデル
	 */
	private SosMst searchSosMst(String sosCd) {
		try {
			return sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "組織取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
	}
}
