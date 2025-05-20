package jp.co.takeda.web.cmn.action;

import jp.co.takeda.a.bean.Box;
import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyClassImpl;
import jp.co.takeda.a.bean.BoxKeyStringImpl;
import jp.co.takeda.a.web.action.AbstractActionForm;
import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.bean.Constants.SpringBeanName;
import jp.co.takeda.model.view.SosJgiLblInfo;
import jp.co.takeda.web.cmn.bean.Browser;
import jp.co.takeda.web.cmn.bean.ContentDispositionMode;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;
import jp.co.takeda.web.cmn.bean.ExportResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 納入計画システム向けアクションフォームクラス
 *
 * @author tkawabata
 */
public abstract class DpActionForm extends AbstractActionForm implements Downloadable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpActionForm.class);

	/**
	 * クリック行ＩＤを補完するためのキー値
	 */
	public static final BoxKey CLICK_ROW_ID_KEY = new BoxKeyStringImpl("jp.co.takeda.web.cmn.action.CLICK_ROW_ID_KEY");

	/**
	 * 特約店検索モードのキー値
	 */
	public static final BoxKey TMS_SELECT_MODE_KEY = new BoxKeyStringImpl("jp.co.takeda.web.cmn.action.TMS_SELECT_MODE_KEY");

	/**
	 * 施設検索モードのキー値
	 */
	public static final BoxKey INS_SELECT_MODE_KEY = new BoxKeyStringImpl("jp.co.takeda.web.cmn.action.INS_SELECT_MODE_KEY");

	/**
	 * 組織・従業員検索のラベル情報取得キー
	 */
	public static final BoxKey DATA_R_SOS_JGI_LBL_INFO = new BoxKeyClassImpl(SosJgiLblInfo.class);

	/**
	 * ExportResult
	 */
	protected transient ExportResult exportResult;

	/**
	 * フォームの初期化を行う。
	 */
	public abstract void formInit();

	/**
	 * セッションからクリック行ＩＤを取得する。
	 *
	 * @return クリック行ＩＤ
	 * @throws Exception
	 */
	public String getClickRowId() throws Exception {
		Box sessionBox = this.getBox(SpringBeanName.SESSION_BOX.value);
		Object obj = sessionBox.get(CLICK_ROW_ID_KEY);
		if (obj != null) {
			sessionBox.put(CLICK_ROW_ID_KEY, null);
			return obj.toString();
		} else {
			return null;
		}
	}

	/**
	 * セッションにクリック行ＩＤを設定する。
	 *
	 * @param entry クリック行ＩＤ
	 * @throws Exception
	 */
	public void setClickRowId(String entry) throws Exception {
		Box sessionBox = this.getBox(SpringBeanName.SESSION_BOX.value);
		if (sessionBox != null) {
			sessionBox.put(CLICK_ROW_ID_KEY, entry);
		}
	}

	/**
	 * セッションから特約店検索モードのキー値を取得する。
	 *
	 * @return 特約店検索モードのキー値
	 */
	public String getTmsSelectMode() {
		Box sessionBox = this.getBox(SpringBeanName.SESSION_BOX.value);
		Object obj = sessionBox.get(TMS_SELECT_MODE_KEY);
		if (obj != null) {
			return obj.toString();
		} else {
			return null;
		}
	}

	/**
	 * セッションに特約店検索モードのキー値を設定する。
	 *
	 * @param entry 特約店検索モードのキー値
	 */
	public void setTmsSelectMode(String entry) {
		Box sessionBox = this.getBox(SpringBeanName.SESSION_BOX.value);
		if (sessionBox != null) {
			sessionBox.put(TMS_SELECT_MODE_KEY, entry);
		}
	}

	/**
	 * セッションから施設検索モードのキー値を取得する。
	 *
	 * @return 施設検索モードのキー値
	 */
	public String getInsSelectMode() {
		Box sessionBox = this.getBox(SpringBeanName.SESSION_BOX.value);
		Object obj = sessionBox.get(INS_SELECT_MODE_KEY);
		if (obj != null) {
			return obj.toString();
		} else {
			return null;
		}
	}

	/**
	 * セッションに施設検索モードのキー値を設定する。
	 *
	 * @param entry 施設検索モードのキー値
	 */
	public void setInsSelectMode(String entry) {
		Box sessionBox = this.getBox(SpringBeanName.SESSION_BOX.value);
		if (sessionBox != null) {
			sessionBox.put(INS_SELECT_MODE_KEY, entry);
		}
	}

	/**
	 * ボックスオブジェクトを取得する。
	 *
	 * @param beanId Box
	 * @return Box
	 */
	private Box getBox(String beanId) {
		return SpringUtil.getBean(beanId, super.getServlet().getServletContext());
	}

	/**
	 * フォームのクリア処理を行う。
	 */
	public void clearThisForm() {

		// ----------------------------------
		// 結果オブジェクトの開放と消去
		// ----------------------------------
		try {
			if (this.exportResult != null) {
				this.exportResult.close();
				this.exportResult = null;
			}

		} catch (Exception e) {

			if (LOG.isErrorEnabled()) {
				final String errMsg = "結果オブジェクトの開放と消去時に例外が発生";
				LOG.error(errMsg, e);
			}
		}

		// ----------------------------------
		// 入力値のクリア
		// ----------------------------------
		try {

			// クリック行ＩＤクリア
			setClickRowId(null);

			// 特約店選択モードクリア
			setTmsSelectMode(null);

			// 施設選択モードクリア
			setInsSelectMode(null);

			// 入力フォームクリア
			formInit();

		} catch (Exception e) {

			if (LOG.isErrorEnabled()) {
				final String errMsg = "入力値のクリア時に例外が発生";
				LOG.error(errMsg, e);
			}
		}
	}

	/**
	 * デフォルト比率フォーマットを取得する。
	 *
	 * @return デフォルト比率フォーマット
	 */
	public String getDefaultRateFormat() {
		return Constants.DEFAULT_RATE_FORMAT;
	}

	/**
	 * ダイアログ画面かを示すフラグを取得する。
	 *
	 * @return ダイアログ画面か
	 */
	public boolean isDialogueFlg() {
		return false;
	}

	/**
	 * ブラウザを取得する。
	 *
	 * @return ブラウザ
	 */
	public Browser getBrowser() {
		return Downloadable.DEF_BROWSER;
	}

	/**
	 * content-dispositionモードを取得する。
	 *
	 * @return content-dispositionモード
	 */
	public ContentDispositionMode getContentDispositionMode() {
		return Downloadable.DEF_CONTENT_DISPOSITION_MODE;
	}

	/**
	 * コンテンツレングスを取得する。
	 *
	 * @return コンテンツレングス
	 */
	public int getContentLength() {
		return Downloadable.DEF_LENGTH;
	}

	/**
	 * コンテンツタイプを取得する。
	 *
	 * @return コンテンツタイプ
	 */
	public ContentsType getContentType() {
		return Downloadable.DEF_CONTENTS_TYPE;
	}

	/**
	 * ダウンロードファイル名称を取得する。
	 *
	 * @return ダウンロードファイル名称
	 */
	public String getDownLoadFileName() {
		return Downloadable.DEF_DOWNLOAD_FILE_NAME;
	}

	/**
	 * ExportResultを返す。
	 *
	 * @return ExportResult
	 */
	public ExportResult getExportResult() {
		return this.exportResult;
	}

	/**
	 * ExportResultを設定する。
	 *
	 * @param exportResult ExportResult
	 */
	public void setExportResult(ExportResult exportResult) {
		this.exportResult = exportResult;
	}
}
