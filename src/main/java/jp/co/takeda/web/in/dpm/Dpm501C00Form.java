package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.ProdMonthPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosMonthPlanUpdateDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm501C00((医)品目別計画編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dpm501C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM501C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm501C00Form.class, ProdMonthPlanResultDto.class);

	/**
	 * 部門ランクフラグ取得キー
	 */
	public static final BoxKey DPM501C00_DATA_R_BUMON_FLAG = new BoxKeyPerClassImpl(Dpm501C00Form.class, Boolean.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM501C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・支店
	 */
	public static final ProdPlanLevel DPM501C00_PLANLEVEL_SITEN = ProdPlanLevel.SITEN;

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPM501C00_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 計画立案レベル・担当者
	 */
	public static final ProdPlanLevel DPM501C00_PLANLEVEL_MR = ProdPlanLevel.MR;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 雑フラグ
	 */
	private boolean etcSosFlg;

	/**
	 * ONC組織フラグ
	 */
	private boolean oncSosFlg;

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリ
	 */
	private String sosCategory;

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * ワクチンカテゴリコード
	 */
	private String vaccineCode;

	// TOP画面用

	/**
	 * [トップ用]組織コード
	 */
	private String topSosCd;

	/**
	 * [トップ用]従業員番号
	 */
	private String topJgiNo;

	/**
	 * [トップ用]部門ランク
	 */
	private String topBumonRank;

	/**
	 * 検索するかのフラグ
	 */
	private boolean searchFlg;

	// 処理用フィールド
	/**
	 * 処理用組織コード(支店)
	 */
	private String sosCd2Tran;

	/**
	 * 処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 処理用組織コード(チーム)
	 */
	private String sosCd4Tran;

	/**
	 * 処理用従業員番号
	 */
	private String jgiNoTran;

	/**
	 * 雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 処理用ONC組織フラグ
	 */
	private boolean oncSosFlgTran;

	/**
	 * カテゴリ
	 */
	private String sosCategoryTran;


	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0] =組織コード(支店)</li>
	 * <li>Sprit文字列[1] =組織コード(営業所)</li>
	 * <li>Sprit文字列[2] =従業員番号</li>
	 * <li>Sprit文字列[3] =品目コード</li>
	 * <li>Sprit文字列[4]=シーケンスキー</li>
	 * <li>Sprit文字列[5]=最終更新日時</li>
	 * <li>Sprit文字列[6]=月初計画1(YB価) 更新前</li>
	 * <li>Sprit文字列[7]=月末見込1(YB価) 更新前</li>
	 * <li>Sprit文字列[8]=月初計画2(YB価) 更新前</li>
	 * <li>Sprit文字列[9]=月末見込2(YB価) 更新前</li>
	 * <li>Sprit文字列[10]=月初計画3(YB価) 更新前</li>
	 * <li>Sprit文字列[11]=月末見込3(YB価) 更新前</li>
	 * <li>Sprit文字列[12]=月初計画4(YB価) 更新前</li>
	 * <li>Sprit文字列[13]=月末見込4(YB価) 更新前</li>
	 * <li>Sprit文字列[14]=月初計画5(YB価) 更新前</li>
	 * <li>Sprit文字列[15]=月末見込5(YB価) 更新前</li>
	 * <li>Sprit文字列[16]=月初計画6(YB価) 更新前</li>
	 * <li>Sprit文字列[17]=月末見込6(YB価) 更新前</li>
	 * <li>Sprit文字列[18]=月初計画1(YB価) 更新後</li>
	 * <li>Sprit文字列[19]=月末見込1(YB価) 更新後</li>
	 * <li>Sprit文字列[20]=月初計画2(YB価) 更新後</li>
	 * <li>Sprit文字列[21]=月末見込2(YB価) 更新後</li>
	 * <li>Sprit文字列[22]=月初計画3(YB価) 更新後</li>
	 * <li>Sprit文字列[23]=月末見込3(YB価) 更新後</li>
	 * <li>Sprit文字列[24]=月初計画4(YB価) 更新後</li>
	 * <li>Sprit文字列[25]=月末見込4(YB価) 更新後</li>
	 * <li>Sprit文字列[26]=月初計画5(YB価) 更新後</li>
	 * <li>Sprit文字列[27]=月末見込5(YB価) 更新後</li>
	 * <li>Sprit文字列[28]=月初計画6(YB価) 更新後</li>
	 * <li>Sprit文字列[29]=月末見込6(YB価) 更新後</li>
	 * </ul>
	 */
	private String[] rowIdList;

	/**
	 * 半期分の月リスト
	 */
	private List<String> monthList;

	/**
	 * 管理対象年度
	 */
	private String year;

	/**
	 * 編集可能リスト
	 */
	private List<Boolean> enableEdit;

	/**
	 * グリッドヘッダ(明細)
	 */
	private String headerDetail;

	/**
	 * グリッドヘッダ（集計）
	 */
	private String headerSum;

	/**
	 * グリッドヘッダの追加1行目
	 */
	private String attachHeader1row;

	/**
	 * グリッドヘッダの追加2行目
	 */
	private String attachHeader2row;

	/**
	 * グリッドヘッダの追加3行目
	 */
	private String attachHeader3row;

	/**
	 * カラムのサイズ
	 */
	private String initWidths;

	/**
	 * カラムの位置
	 */
	private String colAlign;

	/**
	 * カラムのタイプ
	 */
	private String colTypes;

	/**
	 * カラムのリサイズ可不可
	 */
	private String enableResizing;

	/**
	 * ツールチップ有効無効
	 */
	private String enableTooltips;

	/**
	 * JRNS選択可能ユーザ
	 */
	private boolean jrnsUser;

	/**
	 * JRNSとなるカテゴリリスト
	 */
	private List<String> jrnsCategoryList;

    //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	/**
	 * 当月カウント：何月目かを記録（1月目なら0が入る）
	 * ただし第3営業日以内の場合実績が取り込まれていないので-1している
	 */
	private int tougetuCount;
    //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

	// -------------------------------
	// report
	// -------------------------------
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
	 * ダウンロードファイル名を設定する。
	 *
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}



	//---------------------
	// Getter & Setter
	// --------------------
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
	 * @param sosCd2 組織コード(支店)
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
	 * @param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 組織コード(チーム)を設定する。
	 *
	 * @param sosCd4 組織コード(チーム)
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

	/**
	 * 雑組織フラグを取得する。
	 *
	 * @return 雑組織フラグ
	 */
	public boolean isEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * 雑組織フラグを設定する。
	 *
	 * @param etcSosFlg 雑組織フラグ
	 */
	public void setEtcSosFlg(boolean etcSosFlg) {
		this.etcSosFlg = etcSosFlg;
	}

	/**
	 * ONC組織フラグを取得する。
	 *
	 * @return ONC組織フラグ
	 */
	public boolean isOncSosFlg() {
		return oncSosFlg;
	}

	/**
	 * ONC組織フラグを設定する。
	 *
	 * @param oncSosFlg ONC組織フラグ
	 */
	public void setOncSosFlg(boolean oncSosFlg) {
		this.oncSosFlg = oncSosFlg;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getSosCategory() {
		return sosCategory;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param sosCategory カテゴリ
	 */
	public void setSosCategory(String sosCategory) {
		this.sosCategory = sosCategory;
	}

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 *
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 品目カテゴリを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	/**
	 * 品目カテゴリリストを取得する。
	 *
	 * @return prodCategoryList 品目カテゴリリスト
	 */
	public List<CodeAndValue> getProdCategoryList() {
		return prodCategoryList;
	}

	/**
	 *  品目カテゴリリストを取得する。
	 *
	 * @param prodCategoryList 品目カテゴリリスト
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	/**
	 * ワクチンのカテゴリコードを取得する。
	 *
	 * @return vaccineCode ワクチンのカテゴリコード
	 */
	public String getVaccineCode() {
		return vaccineCode;
	}

	/**
	 * ワクチンのカテゴリコードを設定する。
	 *
	 * @param vaccineCode ワクチンのカテゴリコード
	 */
	public void setVaccineCode(String vaccineCode) {
		this.vaccineCode = vaccineCode;
	}

	/**
	 * 追加・更新行の情報リストを取得する。
	 *
	 * @return rowIdList 追加・更新行の情報リスト
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 追加・更新行の情報リストを設定する。
	 *
	 * @param rowIdList 追加・更新行の情報リスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
	}

	/**
	 * 半期分の月リストを取得する。
	 *
	 * @return monthList 半期分の月リスト
	 */
	public List<String> getMonthList() {
		return monthList;
	}

	/**
	 * 半期分の月リストを設定する。
	 *
	 * @param monthList セットする 半期分の月リスト
	 */
	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	/**
	 * 管理対象年度を取得する。
	 *
	 * @return year 管理対象年度
	 */
	public String getYear() {
		return year;
	}

	/**
	 * 管理対象年度を設定する。
	 *
	 * @param year 管理対象年度
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * 編集可能リストを取得する
	 *
	 * @return enableEdit 編集可能リスト
	 */
	public List<Boolean> getEnableEdit() {
		return enableEdit;
	}

	/**
	 * 編集可能リストを設定する
	 *
	 * @param enableEdit 編集可能リスト
	 */
	public void setEnableEdit(List<Boolean> enableEdit) {
		this.enableEdit = enableEdit;
	}

	/**
	 * グリッド（明細）のヘッダを取得する。
	 *
	 * @return headerDetail グリッド（明細）のヘッダ
	 */
	public String getHeaderDetail() {
		return headerDetail;
	}

	/**
	 * グリッド（明細）のヘッダを設定する。
	 *
	 * @param headerDetail グリッド（明細）のヘッダ
	 */
	public void setHeaderDetail(String headerDetail) {
		this.headerDetail = headerDetail;
	}

	/**
	 * グリッド（集計）のヘッダを取得する。
	 *
	 * @return headerSum グリッド（集計）のヘッダ
	 */
	public String getHeaderSum() {
		return headerSum;
	}

	/**
	 * グリッド（集計）のヘッダを設定する。
	 *
	 * @param headerSum グリッド（集計）のヘッダ
	 */
	public void setHeaderSum(String headerSum) {
		this.headerSum = headerSum;
	}

	/**
	 * グリッド（集計）の追加ヘッダ（1行目）を取得する。
	 *
	 * @return attachHeader1row  グリッド（集計）の追加ヘッダ（1行目）
	 */
	public String getAttachHeader1row() {
		return attachHeader1row;
	}

	/**
	 * グリッド（集計）の追加ヘッダ（1行目）を設定する。
	 *
	 * @param attachHeader1row  グリッド（集計）の追加ヘッダ（1行目）
	 */
	public void setAttachHeader1row(String attachHeader1row) {
		this.attachHeader1row = attachHeader1row;
	}

	/**
	 * グリッド（集計）の追加ヘッダ（2行目）を取得する。
	 *
	 * @return attachHeader2row グリッド（集計）の追加ヘッダ（2行目）
	 */
	public String getAttachHeader2row() {
		return attachHeader2row;
	}

	/**
	 * グリッド（集計）の追加ヘッダ（2行目）を設定する。
	 *
	 * @param attachHeader2row グリッド（集計）の追加ヘッダ（2行目）
	 */
	public void setAttachHeader2row(String attachHeader2row) {
		this.attachHeader2row = attachHeader2row;
	}

	/**
	 * グリッド（集計）の追加ヘッダ（3行目）を取得する。
	 *
	 * @return attachHeader3row グリッド（集計）の追加ヘッダ（3行目）
	 */
	public String getAttachHeader3row() {
		return attachHeader3row;
	}

	/**
	 * グリッド（集計）の追加ヘッダ（3行目）を設定する。
	 *
	 * @param attachHeader3row グリッド（集計）の追加ヘッダ（3行目）
	 */
	public void setAttachHeader3row(String attachHeader3row) {
		this.attachHeader3row = attachHeader3row;
	}

	/**
	 * グリッドカラムのサイズを取得する。
	 *
	 * @return initWidths グリッドカラムのサイズ
	 */
	public String getInitWidths() {
		return initWidths;
	}

	/**
	 * グリッドカラムのサイズを設定する。
	 *
	 * @param initWidths グリッドカラムのサイズ
	 */
	public void setInitWidths(String initWidths) {
		this.initWidths = initWidths;
	}

	/**
	 * グリッドカラムの位置を取得する。
	 *
	 * @return colAlign グリッドカラムの位置
	 */
	public String getColAlign() {
		return colAlign;
	}

	/**
	 * グリッドカラムの位置を設定する。
	 *
	 * @param colAlign グリッドカラムの位置
	 */
	public void setColAlign(String colAlign) {
		this.colAlign = colAlign;
	}

	/**
	 * グリッドカラムのタイプを取得する。
	 *
	 * @return colTypes グリッドカラムのタイプ
	 */
	public String getColTypes() {
		return colTypes;
	}

	/**
	 * グリッドカラムのタイプを設定する。
	 *
	 * @param colTypes グリッドカラムのタイプ
	 */
	public void setColTypes(String colTypes) {
		this.colTypes = colTypes;
	}

	/**
	 * グリッドカラムのサイズ変更を有効/無効を取得する。
	 *
	 * @return enableResizing グリッドカラムのサイズ変更を有効/無効
	 */
	public String getEnableResizing() {
		return enableResizing;
	}

	/**
	 * グリッドカラムのサイズ変更を有効/無効を設定する。
	 *
	 * @param enableResizing グリッドカラムのサイズ変更を有効/無効
	 */
	public void setEnableResizing(String enableResizing) {
		this.enableResizing = enableResizing;
	}

	/**
	 * グリッドカラムのツールチップ有効/無効を取得する。
	 *
	 * @return enableTooltips グリッドカラムのツールチップ有効/無効
	 */
	public String getEnableTooltips() {
		return enableTooltips;
	}

	/**
	 * グリッドカラムのツールチップ有効/無効を設定する。
	 *
	 * @param enableTooltips グリッドカラムのツールチップ有効/無効
	 */
	public void setEnableTooltips(String enableTooltips) {
		this.enableTooltips = enableTooltips;
	}

	/**
	 * 処理用組織コード(支店)を取得する。
	 *
	 * @return 処理用組織コード(支店)
	 */
	public String getSosCd2Tran() {
		return sosCd2Tran;
	}

	/**
	 * 処理用組織コード(支店)を設定する。
	 *
	 * @param sosCd2Tran 処理用組織コード(支店)
	 */
	public void setSosCd2Tran(String sosCd2Tran) {
		this.sosCd2Tran = sosCd2Tran;
	}

	/**
	 * 処理用組織コード(営業所)を取得する。
	 *
	 * @return 処理用組織コード(営業所)
	 */
	public String getSosCd3Tran() {
		return sosCd3Tran;
	}

	/**
	 * 処理用組織コード(営業所)を設定する。
	 *
	 * @param sosCd3Tran 処理用組織コード(営業所)
	 */
	public void setSosCd3Tran(String sosCd3Tran) {
		this.sosCd3Tran = sosCd3Tran;
	}

	/**
	 * 処理用組織コード(チーム)を取得する。
	 *
	 * @return 処理用組織コード(チーム)
	 */
	public String getSosCd4Tran() {
		return sosCd4Tran;
	}

	/**
	 * 処理用組織コード(チーム)を設定する。
	 *
	 * @param sosCd4Tran 処理用組織コード(チーム)
	 */
	public void setSosCd4Tran(String sosCd4Tran) {
		this.sosCd4Tran = sosCd4Tran;
	}

	/**
	 * 処理用従業員番号を取得する。
	 *
	 * @return 処理用組織コード(チーム)
	 */
	public String getJgiNoTran() {
		return jgiNoTran;
	}

	/**
	 * 処理用従業員番号を設定する。
	 *
	 * @param jgiNoTran 処理用従業員番号
	 */
	public void setJgiNoTran(String jgiNoTran) {
		this.jgiNoTran = jgiNoTran;
	}

	/**
	 * 処理用品目カテゴリを取得する。
	 *
	 * @return 処理用品目カテゴリ
	 */
	public String getProdCategoryTran() {
		return prodCategoryTran;
	}

	/**
	 * 処理用品目カテゴリを設定する。
	 *
	 * @param prodCategoryTran 処理用品目カテゴリ
	 */
	public void setProdCategoryTran(String prodCategoryTran) {
		this.prodCategoryTran = prodCategoryTran;
	}

	/**
	 * 処理用雑組織フラグを取得する。
	 *
	 * @return 処理用雑組織フラグ
	 */
	public boolean isEtcSosFlgTran() {
		return etcSosFlgTran;
	}

	/**
	 * 処理用雑組織フラグを設定する。
	 *
	 * @param etcSosFlgTran 処理用雑組織フラグ
	 */
	public void setEtcSosFlgTran(boolean etcSosFlgTran) {
		this.etcSosFlgTran = etcSosFlgTran;
	}

	/**
	 * 処理用ONC組織フラグを取得する。
	 *
	 * @return 処理用ONC組織フラグ
	 */
	public boolean isOncSosFlgTran() {
		return oncSosFlgTran;
	}

	/**
	 * 処理用ONC組織フラグを設定する。
	 *
	 * @param etcSosFlgTran 処理用ONC組織フラグ
	 */
	public void setOncSosFlgTran(boolean oncSosFlgTran) {
		this.oncSosFlgTran = oncSosFlgTran;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * 処理用カテゴリを取得する。
	 *
	 * @return 処理用カテゴリ
	 */
	public String getSosCategoryTran() {
		return sosCategoryTran;
	}

	/**
	 * 処理用カテゴリを設定する。
	 *
	 * @param sosCategoryTran 処理用カテゴリ
	 */
	public void setSosCategoryTran(String sosCategoryTran) {
		this.sosCategoryTran = sosCategoryTran;
	}
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * [トップ用]組織コードを取得する。
	 *
	 * @return [トップ用]組織コード
	 */
	public String getTopSosCd() {
		return topSosCd;
	}

	/**
	 * [トップ用]組織コードを設定する。
	 *
	 * @param topSosCd [トップ用]組織コード
	 */
	public void setTopSosCd(String topSosCd) {
		this.topSosCd = topSosCd;
	}

	/**
	 * [トップ用]従業員番号を取得する。
	 *
	 * @return [トップ用]従業員番号
	 */
	public String getTopJgiNo() {
		return topJgiNo;
	}

	/**
	 * [トップ用]従業員番号を設定する。
	 *
	 * @param topJgiNo [トップ用]従業員番号
	 */
	public void setTopJgiNo(String topJgiNo) {
		this.topJgiNo = topJgiNo;
	}

	/**
	 * [トップ用]部門ランクを取得する。
	 *
	 * @return [トップ用]部門ランク
	 */
	public String getTopBumonRank() {
		return topBumonRank;
	}

	/**
	 * [トップ用]部門ランクを設定する。
	 *
	 * @param topBumonRank [トップ用]部門ランク
	 */
	public void setTopBumonRank(String topBumonRank) {
		this.topBumonRank = topBumonRank;
	}

	/**
	 * 検索するかのフラグを取得する。
	 *
	 * @return 検索するかのフラグ
	 */
	public boolean isSearchFlg() {
		return searchFlg;
	}

	/**
	 * 検索するかのフラグを設定する。
	 *
	 * @param searchFlg 検索するかのフラグ
	 */
	public void setSearchFlg(boolean searchFlg) {
		this.searchFlg = searchFlg;
	}

	/**
	 * JRNS選択可能ユーザかを取得する
	 *
	 * @return isJrnsUser JRNS選択可能ユーザか
	 */
	public boolean getJrnsUser() {
		return jrnsUser;
	}

	/**
	 * JRNS選択可能ユーザかを設定する
	 *
	 * @param isJrnsUser JRNS選択可能ユーザか
	 */
	public void setJrnsUser(boolean jrnsUser) {
		this.jrnsUser = jrnsUser;
	}

	/**
	 *  JRNSとなるカテゴリリストを取得する
	 *
	 * @return jrnsCategoryList  JRNSとなるカテゴリリスト
	 */
	public List<String> getJrnsCategoryList() {
		return jrnsCategoryList;
	}

	/**
	 *  JRNSとなるカテゴリリストを設定する
	 *
	 * @param jrnsCategoryList  JRNSとなるカテゴリリスト
	 */
	public void setJrnsCategoryList(List<String> jrnsCategoryList) {
		this.jrnsCategoryList = jrnsCategoryList;
	}

    //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	/**
	 * 当月カウントを取得する。
	 *
	 * @return tougetuCount 当月カウント
	 */
	public int getTougetuCount() {
		return tougetuCount;
	}

	/**
	 * 当月カウントを設定する。
	 *
	 * @param tougetuCount 当月カウント
	 */
	public void setTougetuCount(int tougetuCount) {
		this.tougetuCount = tougetuCount;
	}
    //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return ProdPlanScDto 変換されたScDto
	 */
	public ProdPlanScDto convertProdPlanScDto() throws Exception {
		// 組織コード(支店)
		if (StringUtils.isEmpty(sosCd2Tran)) {
			sosCd2Tran = null;
		}
		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}
		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}
		// 従業員番号
		Integer _jgiNoTran = null;
		if (StringUtils.isNotEmpty(this.jgiNoTran)) {
			_jgiNoTran = ConvertUtil.parseInteger(this.jgiNoTran);
		} else {
			this.jgiNoTran = null;
		}
		// 品目カテゴリ
		String _prodCategory = null;
		if (StringUtils.isNotEmpty(this.prodCategoryTran)) {
			_prodCategory = this.prodCategoryTran;
		} else {
			this.prodCategoryTran = null;
		}
		// カテゴリ
		if (StringUtils.isEmpty(sosCategoryTran)) {
			sosCategoryTran = null;
		}

		return new ProdPlanScDto(sosCd2Tran, sosCd3Tran, sosCd4Tran, _jgiNoTran, _prodCategory, oncSosFlgTran, sosCategoryTran, vaccineCode);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return ProdPlanUpdateDto 変換されたUpdateDto
	 */
	public List<SosMonthPlanUpdateDto> convertSosPlanUpdateDto() throws Exception {
		List<SosMonthPlanUpdateDto> updateDtoList = new ArrayList<SosMonthPlanUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				// 6ヶ月分の計画値・見込値いずれかに変更がある場合に更新
				final boolean isPlanned1Same = StringUtils.equals(rowId[6], rowId[18]);
				final boolean isExpected1Same = StringUtils.equals(rowId[7], rowId[19]);
				final boolean isPlanned2Same = StringUtils.equals(rowId[8], rowId[20]);
				final boolean isExpected2Same = StringUtils.equals(rowId[9], rowId[21]);
				final boolean isPlanned3Same = StringUtils.equals(rowId[10], rowId[22]);
				final boolean isExpected3Same = StringUtils.equals(rowId[11], rowId[23]);
				final boolean isPlanned4Same = StringUtils.equals(rowId[12], rowId[24]);
				final boolean isExpected4Same = StringUtils.equals(rowId[13], rowId[25]);
				final boolean isPlanned5Same = StringUtils.equals(rowId[14], rowId[26]);
				final boolean isExpected5Same = StringUtils.equals(rowId[15], rowId[27]);
				final boolean isPlanned6Same = StringUtils.equals(rowId[16], rowId[28]);
				final boolean isExpected6Same = StringUtils.equals(rowId[17], rowId[29]);

				if (isPlanned1Same && isExpected1Same && isPlanned2Same && isExpected2Same &&
					isPlanned3Same && isExpected3Same && isPlanned4Same && isExpected4Same &&
					isPlanned5Same && isExpected5Same && isPlanned6Same && isExpected6Same) {
					continue;
				}
				// 組織コード
				String sosCd = "";
				if (StringUtils.isNotBlank(rowId[1])) {
					// 営業所コード
					sosCd = rowId[1];
				} else if (StringUtils.isNotBlank(rowId[0])) {
					// 支店コード
					sosCd = rowId[0];
				}
				// 従業員番号
				final Integer jgiNo = ConvertUtil.parseInteger(rowId[2]);
				// 品目コード
				final String prodCode = rowId[3];
				// DTO作成
				// シーケンスキー
				final Long seqKey = ConvertUtil.parseLong(rowId[4]);
				// 最終更新日時
				final Date upDate = ConvertUtil.parseDate(rowId[5]);
				// 更新後月初計画1
				final Long plannedValue1 = ConvertUtil.parseLong(rowId[18]);
				// 更新後月末見込1
				final Long expectedValue1 = ConvertUtil.parseLong(rowId[19]);
				// 更新後月初計画2
				final Long plannedValue2 = ConvertUtil.parseLong(rowId[20]);
				// 更新後月末見込2
				final Long expectedValue2 = ConvertUtil.parseLong(rowId[21]);
				// 更新後月初計画3
				final Long plannedValue3 = ConvertUtil.parseLong(rowId[22]);
				// 更新後月末見込3
				final Long expectedValue3 = ConvertUtil.parseLong(rowId[23]);
				// 更新後月初計画4
				final Long plannedValue4 = ConvertUtil.parseLong(rowId[24]);
				// 更新後月末見込4
				final Long expectedValue4 = ConvertUtil.parseLong(rowId[25]);
				// 更新後月初計画5
				final Long plannedValue5 = ConvertUtil.parseLong(rowId[26]);
				// 更新後月末見込5
				final Long expectedValue5 = ConvertUtil.parseLong(rowId[27]);
				// 更新後月初計画6
				final Long plannedValue6 = ConvertUtil.parseLong(rowId[28]);
				// 更新後月末見込6
				final Long expectedValue6 = ConvertUtil.parseLong(rowId[29]);
				final SosMonthPlanUpdateDto updateDto = new SosMonthPlanUpdateDto(sosCd, jgiNo, prodCode, seqKey, upDate,
						plannedValue1, plannedValue2, plannedValue3, plannedValue4, plannedValue5, plannedValue6,
						expectedValue1, expectedValue2, expectedValue3, expectedValue4, expectedValue5, expectedValue6
						);
				updateDtoList.add(updateDto);

			}
		}
		setRowIdList(null);
		return updateDtoList;
	}

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.etcSosFlgTran = this.etcSosFlg;
		this.oncSosFlgTran = this.oncSosFlg;
		this.prodCategoryTran = this.prodCategory;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCategoryTran = this.sosCategory;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.sosCd2 = this.sosCd2Tran;
		this.sosCd3 = this.sosCd3Tran;
		this.sosCd4 = this.sosCd4Tran;
		this.jgiNo = this.jgiNoTran;
		this.etcSosFlg = this.etcSosFlgTran;
		this.oncSosFlg = this.oncSosFlgTran;
		this.prodCategory = this.prodCategoryTran;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCategory = this.sosCategoryTran;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		setSosCd2(null);
		setSosCd3(null);
		setSosCd4(null);
		setJgiNo(null);
		setSearchFlg(false);
		setEtcSosFlg(false);
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//		setOncSosFlg(false);
		setOncSosFlg(true);
		setSosCategory(null);
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		setProdCategory(null);
		setTopSosCd(null);
		setTopJgiNo(null);
		setTopBumonRank(null);

	}
}
