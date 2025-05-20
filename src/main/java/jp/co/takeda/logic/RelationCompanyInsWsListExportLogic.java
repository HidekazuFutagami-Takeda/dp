package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.InsWsPlanForRefDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.InsWsPlanForRef;
import jp.co.takeda.model.view.MonNnuSummary;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.FileUtil;

/**
 * 関係会社別施設特約店別一覧のExcelファイルを生成するロジッククラス
 *
 * @author stakeuchi
 */
public class RelationCompanyInsWsListExportLogic {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(RelationCompanyInsWsListExportLogic.class);

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	// [行番号] 組織情報[支店]
	private static final int ROW_IDX_SOS_SITEN = 1;

	// [列番号] 組織情報[支店]
	private static final int COL_IDX_SOS_SITEN = 0;

	// [行番号] 組織情報[営業所]
	private static final int ROW_IDX_SOS_OFFICE = 1;

	// [列番号] 組織情報[営業所]
	private static final int COL_IDX_SOS_OFFICE = 3;

	// [行番号] 品目情報
	private static final int ROW_IDX_PROD = 3;

	// [列番号] 品目情報
	private static final int COL_IDX_PROD = 0;

	// [行番号] 品目情報(納入実績ヘッダ部)
	private static final int ROW_IDX_PROD_DELI = 4;

	// [列番号] 品目情報(納入実績ヘッダ部)
	private static final int COL_IDX_PROD_DELI = 9;

	// [行番号] 現在日付
	private static final int ROW_IDX_DATE = 0;

	// [列番号] 現在日付
	private static final int COL_IDX_DATE = 11;

	// [行番号] 営業所UH
	private static final int ROW_IDX_OFFICE_UH = 2;

	// [行番号] 営業所P
	private static final int ROW_IDX_OFFICE_P = 3;

	// [列番号] 営業所
	private static final int COL_IDX_OFFICE = 15;

	// [行番号] 実績合計行[UHP合計]
	private static final int ROW_IDX_SUM_UHP = 6;

	// [行番号] 実績合計行[UH合計]
	private static final int ROW_IDX_SUM_UH = 7;

	// [行番号] 実績合計行[P合計]
	private static final int ROW_IDX_SUM_P = 8;

	// [行番号] 明細開始行
	private static final int ROW_IDX_START_LIST = 9;

	// [列番号] 明細開始列
	private static final int COL_IDX_START_LIST = 0;

	// [列番号] 前々々期
	private static final int COL_IDX_PFAPERIOD = 9;

	// [列番号] 前々期
	private static final int COL_IDX_FAPERIOD = 10;

	// [列番号] 前期
	private static final int COL_IDX_APERIOD = 11;

	// [列番号] 当期
	private static final int COL_IDX_CPERIOD = 12;

	// [列番号] 対象区分
	private static final int COL_IDX_INS_TYPE = 4;

	// [列番号] 理論値
	private static final int COL_IDX_DIST = 16;

	// [列番号] 修正金額
	private static final int COL_IDX_PLAN = 17;

	// [列番号] 印刷終了列
	private static final int COL_IDX_PRINT_END = 17;

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 出力対象支店/営業所の3桁コード
	 */
	private final String outputCd;

	/**
	 * ZIPファイル保存パス
	 */
	private final String zipPath;

	/**
	 * 品目リスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 営業所のリスト
	 */
	private final List<SosMst> sosMstList;

	/**
	 * 施設特約店別計画取得DAO
	 */
	private final InsWsPlanForRefDao insWsPlanForRefDao;

	/**
	 * 組織情報(支店)
	 */
	private final SosMst shitenSosMst;

	/**
	 * 営業所計画DAO
	 */
	private final OfficePlanDao officePlanDao;

	/**
	 * 計画対象カテゴリ領域取得DAO
	 */
	private final DpsPlannedCtgDao dpsPlannedCtgDao;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param plannedProdList 計画対象品目リスト
	 * @param sosMstList 組織リスト
	 * @param outputCd 出力対象の組織3桁コード
	 * @param insWsPlanForRefDao 施設特約店別計画取得DAO
	 * @param sosMstDAO 組織情報DAO
	 * @param officePlanDao 営業所計画DAO
	 * @param dpsPlannedCtgDao 計画対象カテゴリ領域取得DAO
	 * @param plannedProdDAO 計画支援の計画対象品目DAO
	 */
	public RelationCompanyInsWsListExportLogic(String templatePath, Date systemDate, List<PlannedProd> plannedProdList, List<SosMst> sosMstList, String outputCd,
			InsWsPlanForRefDao insWsPlanForRefDao, String downloadFileTempDir, SosMst sosMst, OfficePlanDao officePlanDao, DpsPlannedCtgDao dpsPlannedCtgDao) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isEmpty(templatePath)) {
			final String errMsg = "テンプレートパスがNullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (systemDate == null) {
			final String errMsg = "システム日付がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.isEmpty()) {
			final String errMsg = "計画対象品目リストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMstList == null || sosMstList.isEmpty()) {
			final String errMsg = "組織リストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (outputCd == null) {
			final String errMsg = "支店/営業所3桁コードがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsPlanForRefDao == null) {
			final String errMsg = "施設特約店別計画取得DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMst == null) {
			final String errMsg = "組織情報がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (officePlanDao == null) {
			final String errMsg = "営業所計画取得DAOがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// フィールドにセット
		// ----------------------
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.outputCd = outputCd;
		this.zipPath = downloadFileTempDir;
		this.plannedProdList = plannedProdList;
		this.sosMstList = sosMstList;
		this.insWsPlanForRefDao = insWsPlanForRefDao;
		this.shitenSosMst = sosMst;
		this.officePlanDao = officePlanDao;
		this.dpsPlannedCtgDao = dpsPlannedCtgDao;

	}

	/**
	 * 検索結果データのExcelファイルへの出力を実行する。
	 *
	 * @return zipファイル名(ファイル生成されていない場合、NULLを返す）
	 */
	public String export() {

		// ファイル書込み有無フラグ
		boolean fileFlg = false;

		// ZIPファイル用ディレクトリ作成
		final String zipFilePath = this.zipPath + "/" + createZipFileName();
		File tmpFileDir = new File(zipFilePath);
		tmpFileDir.mkdir();

		// 品目ごとにワークブックを生成
		for (PlannedProd plannedProd : plannedProdList) {
			Map<SosMst, List<InsWsPlanForRef>> detailMap = createReportMap(plannedProd);
			if (!detailMap.isEmpty()) {
				FileInputStream fileIn = null;
				try {
					if (LOG.isDebugEnabled()) {
						LOG.debug("テンプレートファイルの読込開始　:" + DateUtil.getSystemTime());
					}
					// テンプレートファイルの読込
					fileIn = new FileInputStream(templatePath);
					// テンプレートファイルへ書込
					// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//					POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
//					HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
					XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
					// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
					// 書込情報を格納
					write(workBook, plannedProd, detailMap);
					// ファイル保存
					FileOutputStream stream = null;
					try {
						File xlsFile = new File(zipFilePath + "/" + createExcelFileName(plannedProd));
						stream = new FileOutputStream(xlsFile);
						if (LOG.isDebugEnabled()) {
							LOG.debug("ファイル書込み開始　　　　　　　:" + DateUtil.getSystemTime());
						}
						workBook.write(stream);
						fileFlg = true;
					} catch (IOException e) {
						throw new SystemException(new Conveyance(IO_ERROR), e);
					} finally {
						IOUtils.closeQuietly(stream);
					}
				} catch (IOException e) {
					IOUtils.closeQuietly(fileIn);
					throw new SystemException(new Conveyance(PARAMETER_ERROR, "テンプレートパスが存在しない"));
				} finally {
					IOUtils.closeQuietly(fileIn);
				}
			}
		}

		if (fileFlg) {
			// ZIP生成
			if (LOG.isDebugEnabled()) {
				LOG.debug("ファイル圧縮開始　　　　　　　　:" + DateUtil.getSystemTime());
			}

			FileUtil.compressZip(tmpFileDir, true);

			if (LOG.isDebugEnabled()) {
				LOG.debug("ファイル圧縮終了　　　　　　　　:" + DateUtil.getSystemTime());
			}
			return createZipFileName() + ".zip";
		} else {
			tmpFileDir.delete();
			return null;
		}
	}

	/**
	 * 出力情報の集計MAPの生成
	 *
	 * @param plannedProdList 品目リスト
	 * @param sosMstList 組織リスト
	 * @param insWsPlanForRefDao 施設特約店別計画DAO(検索用)
	 * @return 集計MAP
	 */
	private Map<SosMst, List<InsWsPlanForRef>> createReportMap(PlannedProd plannedProd) {
		// -----------------------------
		// 品目からカテゴリ取得
		// -----------------------------
		final String prodCode = plannedProd.getProdCode();
		final String category = plannedProd.getCategory();

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg DpsPlannedCtg = new DpsPlannedCtg();
		try {
			DpsPlannedCtg = dpsPlannedCtgDao.search(category);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象カテゴリ領域【カテゴリ：" + category + "】が存在しないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}


		// 帳票用サブMAP(キー＝営業所情報 値＝施設特約店別計画リスト)の生成
		Map<SosMst, List<InsWsPlanForRef>> reportSubMap = new LinkedHashMap<SosMst, List<InsWsPlanForRef>>();
		for (SosMst sosMst : sosMstList) {
			final String sosCd3 = sosMst.getSosCd();
			try {
				if (LOG.isDebugEnabled()) {
					LOG.debug("ＳＱＬ実行開始　　　　　　　　　:" + DateUtil.getSystemTime());
				}
				List<InsWsPlanForRef> insWsPlanForRefList = insWsPlanForRefDao.searchListBySosCdOyako(InsWsPlanForRefDao.SORT_STRING_OYAKO, sosCd3, prodCode, DpsPlannedCtg.getOyakoKb(), DpsPlannedCtg.getOyakoKb2());
				reportSubMap.put(sosMst, insWsPlanForRefList);
			} catch (DataNotFoundException e) {
			}
		}
		return reportSubMap;
	}

	/**
	 * ワークブックへ検索結果の書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 * @param entry 出力情報
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private void write(HSSFWorkbook workBook, PlannedProd plannedProd, Map<SosMst, List<InsWsPlanForRef>> entry) {
	private void write(XSSFWorkbook workBook, PlannedProd plannedProd, Map<SosMst, List<InsWsPlanForRef>> entry) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

		// Bean初期化
		POIBean poiBean = new POIBean(workBook);

		// 帳票書込み
		writeListInfo(poiBean, plannedProd, entry);
	}

	/**
	 * リスト情報の値をセルに書き込む
	 * <ul>
	 * <li>チームコード</li>
	 * <li>チーム名</li>
	 * <li>従業員コード</li>
	 * <li>従業員名</li>
	 * <li>対象区分</li>
	 * <li>施設コード</li>
	 * <li>施設名</li>
	 * <li>特約店コード</li>
	 * <li>特約店名</li>
	 * <li>前々々期</li>
	 * <li>前々期</li>
	 * <li>前期</li>
	 * <li>当期</li>
	 * <li>理論値</li>
	 * <li>修正金額</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 * @param entry 出力情報
	 */
	private void writeListInfo(POIBean poiBean, PlannedProd plannedProd, Map<SosMst, List<InsWsPlanForRef>> entry) {

		// 出力情報がNullの場合はReturn
		if (entry == null) {
			return;
		}

		// 組織数分シートを作成(既に１つあるので組織数-1作成)
		for (int i = 1; i < entry.size(); i++) {
			// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
			//poiBean.cloneSheet(0);
			poiBean.cloneSheet(i-1);
			// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		}

		// シートインデックス
		int sheetIdx = 0;

		// データは存在している前提
		for (Entry<SosMst, List<InsWsPlanForRef>> subEntry : entry.entrySet()) {

			if (LOG.isDebugEnabled()) {
				LOG.debug("シート開始　　　　　　　　　　　:" + DateUtil.getSystemTime());
			}

			// シートのセット
			poiBean.setWorkSheetIdx(sheetIdx++);

			// 組織情報
			final SosMst sosMst = subEntry.getKey();

			// ヘッダ情報を更新
			writeHeadInfo(poiBean, plannedProd, sosMst);

			// 営業所情報を更新
			writeOfficePlan(poiBean, plannedProd, sosMst);

			// 明細行リスト
			List<InsWsPlanForRef> rowList = subEntry.getValue();

			// 明細がない場合は飛ばす
			if (rowList == null || rowList.size() < 1) {
				continue;
			}

			// リストのサイズ分行を追加
			poiBean.addRowsExt(ROW_IDX_START_LIST, rowList.size() - 1);

			// 行インデックス
			int rowIdx = ROW_IDX_START_LIST;

			if (LOG.isDebugEnabled()) {
				LOG.debug("計画ループ開始　　　　　　　　　:" + DateUtil.getSystemTime() + "計画ループサイズ:" + rowList.size());
			}

			for (InsWsPlanForRef insWsPlan : rowList) {

				// 列インデックス
				int colIdx = COL_IDX_START_LIST;

				// チームコード
				final String teamCode = insWsPlan.getTeamCode();
				poiBean.setCellData(teamCode, rowIdx, colIdx++);

				// チーム名
				final String teamName = insWsPlan.getTeamName();
				poiBean.setCellData(teamName, rowIdx, colIdx++);

				// 従業員コード
				final String jgiNo = insWsPlan.getJgiNo().toString();
				poiBean.setCellData(Double.valueOf(jgiNo), rowIdx, colIdx++);

				// 従業員名
				final String jgiName = insWsPlan.getJgiName();
				poiBean.setCellData(jgiName, rowIdx, colIdx++);

				// 対象区分
				String hoInsTypeString = null;
				HoInsType hoInsType = insWsPlan.getHoInsType();
				switch (hoInsType) {
					case U:
					case H:
						hoInsTypeString = "UH";
						break;
					case P:
						hoInsTypeString = "P";
						break;
					case Z:
						hoInsTypeString = "雑";
						break;
				}

				// 対象区分
				poiBean.setCellData(hoInsTypeString, rowIdx, colIdx++);

				// 施設コード
				final String insNo = insWsPlan.getInsNo();
				poiBean.setCellData(insNo, rowIdx, colIdx++);

				// 施設名
				final String insName = insWsPlan.getInsAbbrName();
				poiBean.setCellData(insName, rowIdx, colIdx++);

				// 特約店コード
				final String tytenCode = insWsPlan.getTmsTytenCd();
				poiBean.setCellData(tytenCode, rowIdx, colIdx++);

				// 特約店名
				final String tytenName = insWsPlan.getTmsTytenName();
				poiBean.setCellData(tytenName, rowIdx, colIdx++);

				// 納入実績
				MonNnuSummary monNnuSummary = insWsPlan.getMonNnuSummary();

				// 前々々期
				final Long preFarAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnuSummary.getPreFarAdvancePeriod());
				poiBean.setCellData(preFarAdvancePeriod, rowIdx, colIdx++);

				// 前々期
				final Long farAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnuSummary.getFarAdvancePeriod());
				poiBean.setCellData(farAdvancePeriod, rowIdx, colIdx++);

				// 前期
				final Long advancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnuSummary.getAdvancePeriod());
				poiBean.setCellData(advancePeriod, rowIdx, colIdx++);

				// 当期
				final Long currentPeriod = ConvertUtil.parseMoneyToThousandUnit(monNnuSummary.getCurrentPeriod());
				poiBean.setCellData(currentPeriod, rowIdx, colIdx++);

				// 対象区分
				String hoInsTypeCode = "";
				switch (hoInsType) {
					case U:
					case H:
						hoInsTypeCode = InsType.UH.getDbValue();
						break;
					case P:
						hoInsTypeCode = InsType.P.getDbValue();
						break;
					case Z:
						hoInsTypeCode = InsType.ZATU.getDbValue();
						break;
				}

				// 対象区分
				poiBean.setCellData(hoInsTypeCode, rowIdx, colIdx++);

				// 施設コード
				poiBean.setCellData(insNo, rowIdx, colIdx++);

				// 特約店コード
				poiBean.setCellData(tytenCode, rowIdx, colIdx++);

				// 理論値
				final Long distValue = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getDistValueY());
				poiBean.setCellData(distValue, rowIdx, colIdx++);

				// 修正金額
				final Long planValue = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getPlannedValueY());
				poiBean.setCellData(planValue, rowIdx, colIdx++);

				rowIdx++;
			}

			// 合計行の計算式をセット
			setSumRowFormula(poiBean, rowList.size());

			// シート再計算設定
			poiBean.setForceFormulaRecalculation(true);

			// ----------------------------
			// 印刷設定
			// ----------------------------
			// 全行サイズ
			int allRowSize = rowList.size() + ROW_IDX_START_LIST - 1;

			// 改ページ数縦
			final short fitHeigth = 0;

			// 改ページ数横
			final short fitWidth = 1;

			poiBean.setPringArea(0, allRowSize, 0, COL_IDX_PRINT_END, fitWidth, fitHeigth);
		}
	}

	/**
	 * ヘッダ情報の値をセルに書き込む。
	 * <ul>
	 * <li>シート名</li>
	 * <li>組織情報</li>
	 * <li>品目情報</li>
	 * <li>品目情報(納入実績ヘッダ部)</li>
	 * <li>現在日付</li>
	 * </ul>
	 *
	 * @param poiBean POI設定BEAN
	 * @param plannedProd 計画対象品目
	 * @param sosMst 組織情報
	 */
	private void writeHeadInfo(POIBean poiBean, PlannedProd plannedProd, SosMst sosMst) {

		if (sosMst != null) {
			// -----------------------
			// シート名
			// -----------------------
			poiBean.setSheetName(sosMst.getBumonSeiName());

			// -----------------------
			// ヘッダ[組織情報]
			// -----------------------
			// 支店　051　：　名古屋支店
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			final String sitenName = "リージョン　" + shitenSosMst.getBrCode() + "：" + shitenSosMst.getBumonSeiName();
//			final String sitenName = "支店　" + shitenSosMst.getBrCode() + "：" + shitenSosMst.getBumonSeiName();
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			poiBean.setCellData(sitenName, ROW_IDX_SOS_SITEN, COL_IDX_SOS_SITEN);

// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			final String eigyoName = "エリア　" + sosMst.getDistCode() + "：" + sosMst.getBumonSeiName();
//			final String eigyoName = "営業所　" + sosMst.getDistCode() + "：" + sosMst.getBumonSeiName();
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			poiBean.setCellData(eigyoName, ROW_IDX_SOS_OFFICE, COL_IDX_SOS_OFFICE);
		}

		if (plannedProd != null) {
			// 品目情報/品目コードは統計品名コード
			final String prodCode = plannedProd.getStatProdCode();
			final String prodName = plannedProd.getProdName();
			final String prodInfo = "品目　" + prodCode + "　：　" + prodName;
			poiBean.setCellData(prodInfo, ROW_IDX_PROD, COL_IDX_PROD);
			// 品目情報(納入実績ヘッダ部)
			final String deliveryInfo = "納入実績　(　" + prodCode + "　：　" + prodName + "　)";
			poiBean.setCellData(deliveryInfo, ROW_IDX_PROD_DELI, COL_IDX_PROD_DELI);
		}
		// 現在日付
		String dateString = "データ作成日時：" + DateUtil.toString(systemDate, "yyyy/MM/dd HH:mm");
		poiBean.setCellData(dateString, ROW_IDX_DATE, COL_IDX_DATE);
	}

	/**
	 * 営業所計画を書き込む。
	 *
	 * @param poiBean POI設定BEAN
	 * @param plannedProd 計画対象品目
	 * @param sosMst 組織情報
	 */
	private void writeOfficePlan(POIBean poiBean, PlannedProd plannedProd, SosMst sosMst) {

		if (sosMst != null) {
			try {
				OfficePlan officePlan = officePlanDao.searchUk(sosMst.getSosCd(), plannedProd.getProdCode());

				Long uh = officePlan.getPlannedValueUhY();
				poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(uh), ROW_IDX_OFFICE_UH, COL_IDX_OFFICE);

				Long p = officePlan.getPlannedValuePY();
				poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(p), ROW_IDX_OFFICE_P, COL_IDX_OFFICE);

			} catch (DataNotFoundException e) {
				// エラーとしない
			}
		}
	}

	/**
	 * 合計行の計算式をセットする。
	 *
	 * @param poiBean POI設定BEAN
	 * @param rowSize 明細行数
	 */
	private void setSumRowFormula(POIBean poiBean, int rowSize) {

		// 行開始番号
		final int fromRowIdx = ROW_IDX_START_LIST;

		// 行終了番号
		final int toRowIdx = ROW_IDX_START_LIST + rowSize - 1;

		// ------------------------
		// UHP合計実績
		// ------------------------
		// 前々々期
		int colIdx = COL_IDX_PFAPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UHP, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx);
		// 前々期
		colIdx = COL_IDX_FAPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UHP, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx);
		// 前期
		colIdx = COL_IDX_APERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UHP, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx);
		// 当期
		colIdx = COL_IDX_CPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UHP, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx);
		// 理論値
		colIdx = COL_IDX_DIST;
		poiBean.setSumFormula(ROW_IDX_SUM_UHP, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx);
		// 修正金額
		colIdx = COL_IDX_PLAN;
		poiBean.setSumFormula(ROW_IDX_SUM_UHP, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx);

		// ------------------------
		// UH合計実績
		// ------------------------
		final String UH = "UH";

		// 前々々期
		colIdx = COL_IDX_PFAPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UH, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, UH, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 前々期
		colIdx = COL_IDX_FAPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UH, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, UH, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 前期
		colIdx = COL_IDX_APERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UH, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, UH, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 当期
		colIdx = COL_IDX_CPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_UH, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, UH, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 理論値
		colIdx = COL_IDX_DIST;
		poiBean.setSumFormula(ROW_IDX_SUM_UH, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, UH, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 修正金額
		colIdx = COL_IDX_PLAN;
		poiBean.setSumFormula(ROW_IDX_SUM_UH, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, UH, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);

		// ------------------------
		// P合計実績
		// ------------------------
		final String P = "P";

		// 前々々期
		colIdx = COL_IDX_PFAPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_P, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, P, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 前々期
		colIdx = COL_IDX_FAPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_P, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, P, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 前期
		colIdx = COL_IDX_APERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_P, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, P, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 当期
		colIdx = COL_IDX_CPERIOD;
		poiBean.setSumFormula(ROW_IDX_SUM_P, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, P, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 理論値
		colIdx = COL_IDX_DIST;
		poiBean.setSumFormula(ROW_IDX_SUM_P, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, P, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
		// 修正金額
		colIdx = COL_IDX_PLAN;
		poiBean.setSumFormula(ROW_IDX_SUM_P, colIdx, fromRowIdx, colIdx, toRowIdx, colIdx, P, fromRowIdx, COL_IDX_INS_TYPE, toRowIdx, COL_IDX_INS_TYPE);
	}

	/**
	 * ZIPファイルの名称を取得する。
	 *
	 * @return ZIPファイル名
	 */
	private String createZipFileName() {
		return "SList_" + outputCd + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmssSSS");
	}

	/**
	 * Excelファイルの名称を取得する。
	 *
	 * @param plannedProd 品目情報
	 * @return Excelファイル名
	 */
	private String createExcelFileName(PlannedProd plannedProd) {
		final String prodCode = plannedProd.getStatProdCode();
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		return "SList_" + prodCode + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xls";
		return "SList_" + prodCode + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xlsx";
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
	}
}
