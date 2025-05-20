package jp.co.takeda.web.in.dps;

import jp.co.takeda.web.cmn.action.DiaDpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dps000C01(帳票子ウィンドウ用)のフォームクラス
 *
 * @author tkawabata
 */
public class Dps999C00Form extends DiaDpActionForm {

	/**
	 * ファイル名
	 */
	protected String downloadFileName;

	/**
	 * コンテンツレングス
	 */
	protected int contentsLength;

	@Override
	public ContentsType getContentType() {
		return ContentsType.XLS;
	}

	@Override
	public String getDownLoadFileName() {
		return downloadFileName;
	}

	@Override
	public int getContentLength() {
		return Downloadable.DEF_LENGTH;
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	// -------------------------------
	// COMMON
	// -------------------------------

	/**
	 * アクションパス
	 */
	private String actionPath;

	// -------------------------------
	// 関係会社別施設特約店別一覧
	// -------------------------------
	/**
	 * 関係会社別施設特約店別一覧出力時の組織コード
	 */
	private String insWsOutputSosCd;

	// -------------------------------
	// ①担当者別品目別計画一覧表
	// -------------------------------
	/**
	 * ①担当者別品目別計画一覧表出力時の組織コード
	 */
	private String mrPlanMMPListOutputCd;

	/**
	 * 出力区分を表す文字列
	 */
	private String mrPlanOutputDivision;

	// -------------------------------
	// ②品目別担当者別計画検討表
	// -------------------------------
	/**
	 * ②品目別担当者別計画検討表出力時の組織コード
	 */
	private String reviewProdMrOutputCd;

	// -------------------------------
	// ③担当者別品目別計画検討表
	// -------------------------------
	/**
	 * ③担当者別品目別計画検討表出力時の組織コード
	 */
	private String reviewMrProdOutputCd;

	// -------------------------------
	// ④チーム担当者計画検討表
	// -------------------------------
	/**
	 * ④チーム担当者計画検討表出力時の組織コード
	 */
	private String teamMrOutputSosCd;

	/**
	 * 帳票選択画面に戻るかを表すフラグ
	 */
	private boolean backSelectPageFlg = false;

	/**
	 * 選択中の組織コード<br>
	 * ※帳票選択画面に戻るために用意。
	 */
	private String selectedSosCd;

	/**
	 * カテゴリ
	 */
	private String category;

	// -------------------------------
	// Getter & Setter
	// -------------------------------
	// -------------------------------
	// COMMON
	// -------------------------------
	/**
	 * ダウンロードファイル名を設定する。
	 *
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/**
	 * アクションパスを取得する。
	 *
	 * @return アクションパス
	 */
	public String getActionPath() {
		return actionPath;
	}

	/**
	 * アクションパスを設定する。
	 *
	 * @param actionPath アクションパス
	 */
	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	/**
	 * 帳票選択画面に戻るかを表すフラグを取得する。
	 *
	 * @return 帳票選択画面に戻るかを表すフラグ
	 */
	public boolean isBackSelectPageFlg() {
		return backSelectPageFlg;
	}

	/**
	 * 帳票選択画面に戻るかを表すフラグを設定する。
	 *
	 * @param backSelectPageFlg 帳票選択画面に戻るかを表すフラグ
	 */
	public void setBackSelectPageFlg(boolean backSelectPageFlg) {
		this.backSelectPageFlg = backSelectPageFlg;
	}

	/**
	 * 選択中の組織コードを取得する。
	 *
	 * @return 選択中の組織コード
	 */
	public String getSelectedSosCd() {
		return selectedSosCd;
	}

	/**
	 * 選択中の組織コードを設定する。
	 *
	 * @param selectedSosCd 選択中の組織コード
	 */
	public void setSelectedSosCd(String selectedSosCd) {
		this.selectedSosCd = selectedSosCd;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	// -------------------------------
	// 関係会社別施設特約店別一覧
	// -------------------------------
	/**
	 * チーム担当者計画検討表出力時の組織コードを取得する。
	 *
	 * @return insWsOutputSosCd 関係会社別施設特約店別一覧出力時の組織コード
	 */
	public String getInsWsOutputSosCd() {
		return insWsOutputSosCd;
	}

	/**
	 * 関係会社別施設特約店別一覧出力時の組織コードを設定する。
	 *
	 * @param insWsOutputSosCd 関係会社別施設特約店別一覧出力時の組織コード
	 */
	public void setInsWsOutputSosCd(String insWsOutputSosCd) {
		this.insWsOutputSosCd = insWsOutputSosCd;
	}

	// -------------------------------
	// ①担当者別品目別計画一覧表
	// -------------------------------

	/**
	 * ①担当者別品目別計画一覧表出力時の組織コードを取得する。
	 *
	 * @return ①担当者別品目別計画一覧表出力時の組織コード
	 */
	public String getMrPlanMMPListOutputCd() {
		return mrPlanMMPListOutputCd;
	}

	/**
	 * ①担当者別品目別計画一覧表出力時の組織コードを設定する。
	 *
	 * @param mrPlanMMPListOutputCd ①担当者別品目別計画一覧表出力時の組織コード
	 */
	public void setMrPlanMMPListOutputCd(String mrPlanMMPListOutputCd) {
		this.mrPlanMMPListOutputCd = mrPlanMMPListOutputCd;
	}

	/**
	 * 出力区分を表す文字列を取得する。
	 *
	 * @return 出力区分を表す文字列
	 */
	public String getMrPlanOutputDivision() {
		return mrPlanOutputDivision;
	}

	/**
	 * 出力区分を表す文字列を設定する。
	 *
	 * @param mrPlanOutputDivision 出力区分を表す文字列
	 */
	public void setMrPlanOutputDivision(String mrPlanOutputDivision) {
		this.mrPlanOutputDivision = mrPlanOutputDivision;
	}

	// -------------------------------
	// ②品目別担当者別計画検討表
	// -------------------------------

	/**
	 * ②品目別担当者別計画検討表出力時の組織コードを取得する。
	 *
	 * @return ②品目別担当者別計画検討表出力時の組織コード
	 */
	public String getReviewProdMrOutputCd() {
		return reviewProdMrOutputCd;
	}

	/**
	 * ②品目別担当者別計画検討表出力時の組織コードを設定する。
	 *
	 * @param reviewProdMrOutputCd ②品目別担当者別計画検討表出力時の組織コード
	 */
	public void setReviewProdMrOutputCd(String reviewProdMrOutputCd) {
		this.reviewProdMrOutputCd = reviewProdMrOutputCd;
	}

	// -------------------------------
	// ③担当者別品目別計画検討表
	// -------------------------------

	/**
	 * ③担当者別品目別計画検討表出力時の組織コードを取得する。
	 *
	 * @return ③担当者別品目別計画検討表出力時の組織コード
	 */
	public String getReviewMrProdOutputCd() {
		return reviewMrProdOutputCd;
	}

	/**
	 * ③担当者別品目別計画検討表出力時の組織コードを設定する。
	 *
	 * @param reviewMrProdOutputCd ③担当者別品目別計画検討表出力時の組織コード
	 */
	public void setReviewMrProdOutputCd(String reviewMrProdOutputCd) {
		this.reviewMrProdOutputCd = reviewMrProdOutputCd;
	}

	// -------------------------------
	// ④チーム担当者計画検討表
	// -------------------------------
	/**
	 * ④チーム担当者計画検討表出力時の組織コードを取得する。
	 *
	 * @return teamMrOutputSosCd ④チーム担当者計画検討表出力時の組織コード
	 */
	public String getTeamMrOutputSosCd() {
		return teamMrOutputSosCd;
	}

	/**
	 * ④チーム担当者計画検討表出力時の組織コードを設定する。
	 *
	 * @param teamMrOutputSosCd ④チーム担当者計画検討表出力時の組織コード
	 */
	public void setTeamMrOutputSosCd(String teamMrOutputSosCd) {
		this.teamMrOutputSosCd = teamMrOutputSosCd;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理のため、不要
	}
}
