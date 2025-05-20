package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.model.DistPlanningListULSummary;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dps930C00((医)計画立案準備計画のアップロード画面)のフォームクラス
 *
 * @author takahashi
 */
public class Dps930C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * エラー内容ファイルのダウンロード取得キー
	 */
	public static final BoxKey DPS930C00_DATA_EXIST = new BoxKeyPerClassImpl(Dps930C00Form.class, Boolean.class);

	/**
	 * ファイルアップロードリスト取得キー
	 */
	public static final BoxKey DPS930C00_LIST = new BoxKeyPerClassImpl(Dps930C00Form.class, DistPlanningListULSummary.class);

	// -------------------------------
	// report
	// -------------------------------
	/**
	 * ファイル名
	 */
	protected String downloadFileName;

	/**
	 * アップロード制御区分
	 */
	protected String uploadCtrlKbn;

	/**
	 * FormFileフィールド
	 */
	private FormFile uploadFile;

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

	// -------------------------------
	// Utility
	// -------------------------------
	/**
	 * ダウンロードファイル名を設定する。
	 *
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	// -------------------------------
	// field
	// -------------------------------

	/**
	 * 組織コード(本部)
	 */
	private String sosCd1;

	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 支店・営業所コード
	 */
	List<String> brDistCd3List;

	/**
	 * 品目のカテゴリ
	 */
	private String category;

	/**
	 * 品目のカテゴリ_仕入
	 */
	private String siiCtg;

	/**
	 * 品目のカテゴリ_ワクチン
	 */
	private String vacCtg;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * アップロードファイル情報を取得する。
	 *
	 * @return uploadFile アップロードファイル
	 */
    public FormFile getUploadFile() {
    	return uploadFile;
    }

	/**
	 * アップロードファイル情報を設定する。
	 *
	 * @param uploadFile アップロードファイル
	 */
    public void setUploadFile(FormFile uploadFile) {
    	this.uploadFile = uploadFile;
    }

	/**
	 * 組織コード(本部)を取得する。
	 *
	 * @return sosCd1 組織コード(本部)
	 */
	public String getSosCd1() {
		return sosCd1;
	}

	/**
	 * 組織コード(本部)を設定する。
	 *
	 * param sosCd1 組織コード(本部)
	 */
	public void setSosCd1(String sosCd1) {
		this.sosCd1 = sosCd1;
	}

	/**
	 * 組織コード(支店)を取得する。
	 *
	 * @return sosCd2 組織コード(支店)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(支店)を設定する。
	 *
	 * param sosCd2 組織コード(支店)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 支店・営業所コードを取得する。
	 *
	 * @return brDistCd3List 支店・営業所コード
	 */
	public List<String> getBrDistCd3List() {
		return brDistCd3List;
	}

	/**
	 * 支店・営業所コードを設定する。
	 *
	 * param brDistCd3List 支店・営業所コード
	 */
	public void setBrDistCd3List(List<String> brDistCd3List) {
		this.brDistCd3List = brDistCd3List;
	}

	/**
	 * アップロード制御区分を取得する。
	 *
	 * @return uploadCtrlKbn アップロード制御区分
	 */
	public String getUploadCtrlKbn() {
		return uploadCtrlKbn;
	}

	/**
	 * アップロード制御区分を設定する。
	 *
	 * param uploadCtrlKbn アップロード制御区分
	 */
	public void setUploadCtrlKbn(String uploadCtrlKbn) {
		this.uploadCtrlKbn = uploadCtrlKbn;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * param category カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 仕入のカテゴリコードを取得する。
	 *
	 * param siiCtg カテゴリ:仕入
	 */
	public String getSiiCtg() {
		return siiCtg;
	}

	/**
	 * 仕入のカテゴリコードを設定する。
	 *
	 * param siiCtg カテゴリ：仕入
	 */
	public void setSiiCtg(String siiCtg) {
		this.siiCtg = siiCtg;
	}

	/**
	 * ワクチンのカテゴリコードを取得する。
	 *
	 * param vacCtg カテゴリ:ワクチン
	 */
	public String getvacCtg() {
		return vacCtg;
	}

	/**
	 * ワクチンのカテゴリコードを設定する。
	 *
	 * param vacCtg カテゴリ：ワクチン
	 */
	public void setVacCtg(String vacCtg) {
		this.vacCtg = vacCtg;
	}

	// -------------------------------
	// convert
	// -------------------------------

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd1 = null;
		this.sosCd2 = null;
		this.sosCd3 = null;
		this.uploadCtrlKbn = null;
		this.brDistCd3List = new ArrayList<String>();

	}
}
