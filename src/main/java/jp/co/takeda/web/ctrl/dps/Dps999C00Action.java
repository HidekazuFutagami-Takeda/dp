package jp.co.takeda.web.ctrl.dps;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcFileCreateService;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsBusinessProgressSearchService;
import jp.co.takeda.service.DpsContactOperationsService;
import jp.co.takeda.service.DpsReportForVacService;
import jp.co.takeda.service.DpsReportService;
import jp.co.takeda.service.DpsReportService.MrPlanOutputDivision;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.DpDispatchManager;
import jp.co.takeda.web.cmn.bean.ExportResultFileImpl;
import jp.co.takeda.web.in.dps.Dps999C00Form;

/**
 * Dps999C00(子画面ウィンドウ帳票出力用)のアクションクラス
 *
 * @author tkawabata
 */
@Controller("Dps999C00Action")
public class Dps999C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps999C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * ユーザ情報を取得するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * お知らせ情報を取得するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsContactOperationsService")
	protected DpsContactOperationsService dpsContactOperationsService;

	/**
	 * 業務進捗サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsBusinessProgressSearchService")
	protected DpsBusinessProgressSearchService dpsBusinessProgressSearchService;

	/**
	 * (医)帳票出力サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportService")
	protected DpsReportService dpsReportService;

	/**
	 * (ワ)帳票出力サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportForVacService")
	protected DpsReportForVacService dpsReportForVacService;

	/**
	 * 転送サービス
	 */
	@Autowired(required = true)
	@Qualifier("dispatchManager")
	protected DpDispatchManager dispatchManager;

	/**
	 * ファイル作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcFileCreateService")
	protected DpcFileCreateService dpcFileCreateService;

	/**
	 * ダウンロードファイル作成ディレクトリ
	 */
	@Autowired(required = true)
	@Qualifier("downloadFileTempDir")
	protected String downloadFileTempDir;

	// -------------------------------
	// action method
	// -------------------------------

	// ------------------------------------------------
	// (医)関係会社別施設特約店別一覧
	// ------------------------------------------------
	/**
	 * (医)関係会社別施設特約店別一覧出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F00(DpContext ctx, Dps999C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F00");
			LOG.debug("insWsOutputSosCd=" + form.getInsWsOutputSosCd());
		}

		// 初期化処理
		form.formInit();

		// ActionPathを設定
		form.setActionPath("dps999C00F00");

		// 「作成中」の画面に遷移
		return ActionResult.SUCCESS;
	}

	/**
	 * (医)関係会社別施設特約店別一覧作成処理<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F00Output(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F00Output");
			LOG.debug("insWsOutputSosCd=" + form.getInsWsOutputSosCd());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F00");

		// 帳票作成
		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final String sosCd = form.getInsWsOutputSosCd();
		final String fileName = dpsReportService.outputRelationCompanyInsWsList(templateRootPath, sosCd, downloadFileTempDir);

		// ファイル名を設定
		form.setDownLoadFileName(fileName);

		// 「作成完了」の画面を表示
		return ActionResult.SUCCESS;
	}

	/**
	 * (医)関係会社別施設特約店別一覧のダウンロード処理を行なう。<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	public Result dps999C00F00Download(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F00Download");
			LOG.debug("downLoadFileName=" + form.getDownLoadFileName());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F00");

		final String fileName = form.getDownLoadFileName();
		form.setExportResult(new ExportResultFileImpl(new File(downloadFileTempDir, fileName)));
		form.setDownLoadFileName(fileName);

		return ActionResult.SUCCESS;
	}

	// ------------------------------------------------
	// ①担当者別品目別計画一覧
	// ------------------------------------------------
	/**
	 * ①担当者別品目別計画一覧出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F01(DpContext ctx, Dps999C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F01");
			LOG.debug("mrPlanMMPListOutputCd=" + form.getMrPlanMMPListOutputCd());
		}

		// 初期化処理
		form.formInit();

		// ActionPathを設定
		form.setActionPath("dps999C00F01");

		// 「作成中」の画面に遷移
		return ActionResult.SUCCESS;
	}

	/**
	 * ①担当者別品目別計画一覧作成処理<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F01Output(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F01Output");
			LOG.debug("mrPlanMMPListOutputCd=" + form.getMrPlanMMPListOutputCd());
			LOG.debug("mrPlanOutputDivision=" + form.getMrPlanOutputDivision());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F01");
		form.setBackSelectPageFlg(true);
		form.setSelectedSosCd(form.getMrPlanMMPListOutputCd());

		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final String fileName = dpsReportService.outputMrPlanMMPList(templateRootPath, form.getMrPlanMMPListOutputCd(), MrPlanOutputDivision.valueOf(form.getMrPlanOutputDivision()), downloadFileTempDir, form.getCategory());

		// ファイル名を設定
		form.setDownLoadFileName(fileName);

		// 「作成完了」の画面を表示
		return ActionResult.SUCCESS;
	}

	/**
	 * ①担当者別品目別計画一覧のダウンロード処理を行なう。<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	public Result dps999C00F01Download(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F01Download");
			LOG.debug("downLoadFileName=" + form.getDownLoadFileName());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F01");

		final String fileName = form.getDownLoadFileName();
		form.setExportResult(new ExportResultFileImpl(new File(downloadFileTempDir, fileName)));
		form.setDownLoadFileName(fileName);

		return ActionResult.SUCCESS;
	}

	// ------------------------------------------------
	// ②品目別担当者別計画検討表
	// ------------------------------------------------
	/**
	 * ②品目別担当者別計画検討表出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
//	@Permission( authType = OUTPUT)
	public Result dps999C00F02(DpContext ctx, Dps999C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F02");
			LOG.debug("reviewProdMrOutputCd=" + form.getReviewProdMrOutputCd());
		}

		// 初期化処理
		form.formInit();

		// ActionPathを設定
		form.setActionPath("dps999C00F02");

		// 「作成中」の画面に遷移
		return ActionResult.SUCCESS;
	}

	/**
	 * ②品目別担当者別計画検討表作成処理<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
//	@Permission( authType = OUTPUT)
	public Result dps999C00F02Output(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F02Output");
			LOG.debug("reviewProdMrOutputCd=" + form.getReviewProdMrOutputCd());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F02");
		form.setBackSelectPageFlg(true);
		form.setSelectedSosCd(form.getReviewProdMrOutputCd());

		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final String fileName = dpsReportService.outputReviewProdMrMMPList(templateRootPath, form.getReviewProdMrOutputCd(), downloadFileTempDir, form.getCategory());

		// ファイル名を設定
		form.setDownLoadFileName(fileName);

		// 「作成完了」の画面を表示
		return ActionResult.SUCCESS;
	}

	/**
	 * ②品目別担当者別計画検討表のダウンロード処理を行なう。<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
//	@Permission( authType = OUTPUT)
	public Result dps999C00F02Download(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F02Download");
			LOG.debug("downLoadFileName=" + form.getDownLoadFileName());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F02");

		final String fileName = form.getDownLoadFileName();
		form.setExportResult(new ExportResultFileImpl(new File(downloadFileTempDir, fileName)));
		form.setDownLoadFileName(fileName);

		return ActionResult.SUCCESS;
	}

	// ------------------------------------------------
	// ③担当者別品目別計画検討表
	// ------------------------------------------------
	/**
	 * ③担当者別品目別計画検討表出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F03(DpContext ctx, Dps999C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F03");
			LOG.debug("reviewMrProdOutputCd=" + form.getReviewMrProdOutputCd());
		}

		// 初期化処理
		form.formInit();

		// ActionPathを設定
		form.setActionPath("dps999C00F03");

		// 「作成中」の画面に遷移
		return ActionResult.SUCCESS;
	}

	/**
	 * ③担当者別品目別計画検討表作成処理<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F03Output(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F03Output");
			LOG.debug("reviewMrProdOutputCd=" + form.getReviewMrProdOutputCd());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F00");
		form.setBackSelectPageFlg(true);
		form.setSelectedSosCd(form.getReviewMrProdOutputCd());

		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final String fileName = dpsReportService.outputReviewMrProdMMPList(templateRootPath, form.getReviewMrProdOutputCd(), downloadFileTempDir , form.getCategory());

		// ファイル名を設定
		form.setDownLoadFileName(fileName);

		// 「作成完了」の画面を表示
		return ActionResult.SUCCESS;
	}

	/**
	 * ③担当者別品目別計画検討表のダウンロード処理を行なう。<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	public Result dps999C00F03Download(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F03Download");
			LOG.debug("downLoadFileName=" + form.getDownLoadFileName());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F03");

		final String fileName = form.getDownLoadFileName();
		form.setExportResult(new ExportResultFileImpl(new File(downloadFileTempDir, fileName)));
		form.setDownLoadFileName(fileName);

		return ActionResult.SUCCESS;
	}

	// ------------------------------------------------
	// ④チーム担当者計画検討表
	// ------------------------------------------------
	/**
	 * ④チーム担当者計画検討表出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F04(DpContext ctx, Dps999C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F04");
			LOG.debug("teamMrOutputSosCd=" + form.getTeamMrOutputSosCd());
		}

		// 初期化処理
		form.formInit();

		// ActionPathを設定
		form.setActionPath("dps999C00F04");

		// 「作成中」の画面に遷移
		return ActionResult.SUCCESS;
	}

	/**
	 * ④チーム担当者計画検討表作成処理<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps999C00F04Output(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F04Output");
			LOG.debug("teamMrOutputSosCd=" + form.getTeamMrOutputSosCd());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F04");
		form.setBackSelectPageFlg(true);
		form.setSelectedSosCd(form.getTeamMrOutputSosCd());

		// 帳票作成
		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final String sosCd = form.getTeamMrOutputSosCd();
		final String fileName = dpsReportService.outputTeamMrReport(templateRootPath, sosCd, downloadFileTempDir, form.getCategory());

		// ファイル名を設定
		form.setDownLoadFileName(fileName);

		// 「作成完了」の画面を表示
		return ActionResult.SUCCESS;
	}

	/**
	 * ④チーム担当者計画検討表のダウンロード処理を行なう。<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	public Result dps999C00F04Download(DpContext ctx, Dps999C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps999C00F04Download");
			LOG.debug("downLoadFileName=" + form.getDownLoadFileName());
		}

		// ActionPathを設定
		form.setActionPath("dps999C00F04");

		final String fileName = form.getDownLoadFileName();
		form.setExportResult(new ExportResultFileImpl(new File(downloadFileTempDir, fileName)));
		form.setDownLoadFileName(fileName);

		return ActionResult.SUCCESS;
	}

	// ------------------------------------------------
	// 子画面専用の設定
	// ------------------------------------------------
	@Override
	protected ActionResult exceptionProcess(final DpContext ctx, final DpActionForm form, final Exception e) throws Exception {

		ActionResult result = super.exceptionProcess(ctx, form, e);
		if (result == null) {
			return null;
		}

		// 成功、失敗、エラー以外は子画面では規定していないので、強制的にエラーとする
		if ((result != ActionResult.SUCCESS) || (result != ActionResult.FAILURE) || (result != ActionResult.ERROR)) {
			return ActionResult.ERROR;
		}
		return result;
	}
}
