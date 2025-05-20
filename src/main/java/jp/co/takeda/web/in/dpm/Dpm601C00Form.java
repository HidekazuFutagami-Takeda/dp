package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.InsMonthPlanUpdateDto;
import jp.co.takeda.dto.InsProdMonthPlanResultDto;
import jp.co.takeda.dto.InsProdMonthPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdMonthPlanScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm601C00((医)施設品目別計画編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dpm601C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM601C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm601C00Form.class, InsProdMonthPlanResultDto.class);

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM601C00_DATA_R_HEADER = new BoxKeyPerClassImpl(Dpm601C00Form.class, InsProdMonthPlanResultHeaderDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM601C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 【未使用】組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 【未使用】組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 【未使用】組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 【未使用】雑組織フラグ
	 */
	private boolean etcSosFlg;

	/**
	 * 【未使用】従業員番号
	 */
	private String jgiNo;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 施設名
	 */
	private String insName;

	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 計画検索範囲
	 */
	private String planData;

	/**
	 * 対象区分
	 */
	private String insType;

	/**
	 * ワクチンのカテゴリコード
	 */
	private String vaccineCode;

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
	 * グリッドカラムのサイズ変更を有効/無効
	 */
	private String enableResizing;

	/**
	 * グリッドカラムのツールチップ有効/無効
	 */
	private String enableTooltips;

	//入力制御用
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
	private List<Boolean> enableEditList;


	// 処理用フィールド
	/**
	 * 【未使用】処理用組織コード(支店)
	 */
	private String sosCd2Tran;

	/**
	 * 【未使用】処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 【未使用】処理用組織コード(チーム)
	 */
	private String sosCd4Tran;

	/**
	 * 【未使用】処理用組織コード(従業員番号)
	 */
	private String jgiNoTran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 処理用計画検索範囲
	 */
	private String planDataTran;

	/**
	 * 処理用施設コード
	 */
	private String insNoTran;

	/**
	 * 【未使用】処理用雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 全MRフラグ
	 */
	private boolean mrFlg;

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

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0]=施設コード</li>
	 * <li>Sprit文字列[1]=対象区分</li>
	 * <li>Sprit文字列[2]=品目コード</li>
	 * <li>Sprit文字列[3]=シーケンスキー</li>
	 * <li>Sprit文字列[4]=最終更新日時</li>
	 * <li>Sprit文字列[5]=Y価ベース 更新前</li>
	 * <li>Sprit文字列[6]=Y価ベース 更新後</li>
	 * </ul>
	 */
	private String[] rowIdList;

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
	 * 全MRフラグを取得する。
	 *
	 * @return mrFlg 全MRフラグ
	 */
	public boolean getMrFlg() {
		return mrFlg;
	}

	/**
	 * 全MRフラグを設定する。
	 *
	 * @param mrFlg 全MRフラグ
	 */
	public void setMrFlg(boolean mrFlg) {
		this.mrFlg = mrFlg;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 施設名を取得する。
	 *
	 * @return 施設名
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 施設名を設定する。
	 *
	 * @param insName 施設名
	 */
	public void setInsName(String insName) {
		this.insName = insName;
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
	 * 品目カテゴリリストを設定する。
	 *
	 * @param prodCategoryList 品目カテゴリリスト
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	/**
	 * 計画検索範囲を取得する。
	 *
	 * @return planData 計画検索範囲
	 */
	public String getPlanData() {
		return planData;
	}

	/**
	 * 計画検索範囲を設定する。
	 *
	 * @param planData 計画検索範囲
	 */
	public void setPlanData(String planData) {
		this.planData = planData;
	}

	/**
	 * 対象区分を取得する。
	 *
	 * @return insType 対象区分
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param insType 対象区分
	 */
	public void setInsType(String insType) {
		this.insType = insType;
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
	 * @return enableEditList 編集可能リスト
	 */
	public List<Boolean> getEnableEditList() {
		return enableEditList;
	}

	/**
	 * 編集可能リストを設定する
	 *
	 * @param enableEditList 編集可能リスト
	 */
	public void setEnableEditList(List<Boolean> enableEditList) {
		this.enableEditList = enableEditList;
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
	 * 処理用組織コード(従業員番号)を取得する。
	 *
	 * @return 処理用組織コード(従業員番号)
	 */
	public String getJgiNoTran() {
		return jgiNoTran;
	}

	/**
	 * 処理用組織コード(従業員番号)を設定する。
	 *
	 * @param jgiNoTran 処理用組織コード(従業員番号)
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
	 * 処理用計画を取得する。
	 *
	 * @return 処理用計画
	 */
	public String getPlanDataTran() {
		return planDataTran;
	}

	/**
	 * 処理用計画を設定する。
	 *
	 * @param planDataTran 処理用計画
	 */
	public void setPlanDataTran(String planDataTran) {
		this.planDataTran = planDataTran;
	}

	/**
	 * 処理用施設コードを取得する。
	 *
	 * @return 処理用施設コード
	 */
	public String getInsNoTran() {
		return insNoTran;
	}

	/**
	 * 処理用施設コードを設定する。
	 *
	 * @param insNoTran 処理用施設コード
	 */
	public void setInsNoTran(String insNoTran) {
		this.insNoTran = insNoTran;
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

	//add
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

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsProdPlanScDto 変換されたScDto
	 */
	public InsProdMonthPlanScDto convertInsProdPlanScDto() throws Exception {
		// 従業員番号
		Integer _jgiNoTran = null;
		if (StringUtils.isNotEmpty(this.jgiNoTran)) {
			_jgiNoTran = ConvertUtil.parseInteger(this.jgiNoTran);
		} else {
			this.jgiNoTran = null;
		}
		// 施設コード
		if (StringUtils.isEmpty(insNoTran)) {
			this.insNoTran = null;
		}
		// 品目カテゴリ
		String _prodCategory = null;
		if (StringUtils.isNotEmpty(this.prodCategoryTran)) {
			_prodCategory = this.prodCategoryTran;
		} else {
			this.prodCategoryTran = null;
		}
		// 計画検索範囲
		PlanData _planDataTran = null;
		if (StringUtils.isNotEmpty(this.planDataTran)) {
			_planDataTran = PlanData.getInstance(this.planDataTran);
		} else {
			this.planDataTran = null;
		}
		return new InsProdMonthPlanScDto(_jgiNoTran, insNoTran, _prodCategory, _planDataTran, vaccineCode);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return InsProdPlanUpdateDto 変換されたUpdateDto
	 */
	public List<InsMonthPlanUpdateDto> convertInsMonthPlanUpdateDto() throws Exception {
		List<InsMonthPlanUpdateDto> updateDtoList = new ArrayList<InsMonthPlanUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 6ヶ月分の計画値・見込値いずれかに変更がある場合に更新
                final boolean isPlanned1Same = StringUtils.equals(rowId[5], rowId[17]);
                final boolean isPlanned2Same = StringUtils.equals(rowId[6], rowId[18]);
                final boolean isPlanned3Same = StringUtils.equals(rowId[7], rowId[19]);
                final boolean isPlanned4Same = StringUtils.equals(rowId[8], rowId[20]);
                final boolean isPlanned5Same = StringUtils.equals(rowId[9], rowId[21]);
                final boolean isPlanned6Same = StringUtils.equals(rowId[10], rowId[22]);
                final boolean isExpected1Same = StringUtils.equals(rowId[11], rowId[23]);
                final boolean isExpected2Same = StringUtils.equals(rowId[12], rowId[24]);
                final boolean isExpected3Same = StringUtils.equals(rowId[13], rowId[25]);
                final boolean isExpected4Same = StringUtils.equals(rowId[14], rowId[26]);
                final boolean isExpected5Same = StringUtils.equals(rowId[15], rowId[27]);
                final boolean isExpected6Same = StringUtils.equals(rowId[16], rowId[28]);

                if (isPlanned1Same && isExpected1Same && isPlanned2Same && isExpected2Same &&
                    isPlanned3Same && isExpected3Same && isPlanned4Same && isExpected4Same &&
                    isPlanned5Same && isExpected5Same && isPlanned6Same && isExpected6Same) {
                    continue;
                }

				// 施設コード
				final String insNo = rowId[0];
				// 対象区分
				final InsType insType = InsType.getInstance(rowId[1]);
				// 品目コード
				final String prodCode = rowId[2];
				// シーケンスキー
				final Long seqKey = ConvertUtil.parseLong(rowId[3]);
				// 最終更新日時
				final Date upDate = ConvertUtil.parseDate(rowId[4]);

				// 更新前月初計画1（ＹB価）
				final Long planned1ValueBefore = ConvertUtil.parseLong(rowId[5]);
				// 更新前月初計画2（ＹB価）
				final Long planned2ValueBefore = ConvertUtil.parseLong(rowId[6]);
				// 更新前月初計画3（ＹB価）
				final Long planned3ValueBefore = ConvertUtil.parseLong(rowId[7]);
				// 更新前月初計画4（ＹB価）
				final Long planned4ValueBefore = ConvertUtil.parseLong(rowId[8]);
				// 更新前月初計画5（ＹB価）
				final Long planned5ValueBefore = ConvertUtil.parseLong(rowId[9]);
				// 更新前月初計画6（ＹB価）
				final Long planned6ValueBefore = ConvertUtil.parseLong(rowId[10]);

				// 更新前月末見込1（ＹB価）
				final Long expected1ValueBefore = ConvertUtil.parseLong(rowId[11]);
				// 更新前月末見込2（ＹB価）
				final Long expected2ValueBefore = ConvertUtil.parseLong(rowId[12]);
				// 更新前月末見込3（ＹB価）
				final Long expected3ValueBefore = ConvertUtil.parseLong(rowId[13]);
				// 更新前月末見込4（ＹB価）
				final Long expected4ValueBefore = ConvertUtil.parseLong(rowId[14]);
				// 更新前月末見込5（ＹB価）
				final Long expected5ValueBefore = ConvertUtil.parseLong(rowId[15]);
				// 更新前月末見込6（ＹB価）
				final Long expected6ValueBefore = ConvertUtil.parseLong(rowId[16]);

				// 月初計画1（ＹB価）
				final Long planned1Value = ConvertUtil.parseLong(rowId[17]);
				// 月初計画2（ＹB価）
				final Long planned2Value = ConvertUtil.parseLong(rowId[18]);
				// 月初計画3（ＹB価）
				final Long planned3Value = ConvertUtil.parseLong(rowId[19]);
				// 月初計画4（ＹB価）
				final Long planned4Value = ConvertUtil.parseLong(rowId[20]);
				// 月初計画5（ＹB価）
				final Long planned5Value = ConvertUtil.parseLong(rowId[21]);
				// 月初計画6（ＹB価）
				final Long planned6Value = ConvertUtil.parseLong(rowId[22]);

				// 月末見込1（ＹB価）
				final Long expected1Value = ConvertUtil.parseLong(rowId[23]);
				// 月末見込2（ＹB価）
				final Long expected2Value = ConvertUtil.parseLong(rowId[24]);
				// 月末見込3（ＹB価）
				final Long expected3Value = ConvertUtil.parseLong(rowId[25]);
				// 月末見込4（ＹB価）
				final Long expected4Value = ConvertUtil.parseLong(rowId[26]);
				// 月末見込5（ＹB価）
				final Long expected5Value = ConvertUtil.parseLong(rowId[27]);
				// 月末見込6（ＹB価）
				final Long expected6Value = ConvertUtil.parseLong(rowId[28]);

				// 更新用DTO生成
				final InsMonthPlanUpdateDto updateDto = new InsMonthPlanUpdateDto(insNo, insType, prodCode, seqKey, upDate,
						planned1ValueBefore, planned2ValueBefore, planned3ValueBefore, planned4ValueBefore, planned5ValueBefore, planned6ValueBefore,
						expected1ValueBefore, expected2ValueBefore, expected3ValueBefore, expected4ValueBefore, expected5ValueBefore, expected6ValueBefore,
						planned1Value, planned2Value, planned3Value, planned4Value, planned5Value, planned6Value,
						expected1Value, expected2Value, expected3Value, expected4Value, expected5Value, expected6Value);
				updateDtoList.add(updateDto);
			}
		}
		rowIdList = null;
		return updateDtoList;
	}

	/**
	 * headerDto ⇒ ActionForm 変換処理
	 *
	 * @param headerDto ヘッダ情報DTO
	 */
	public void convertHeaderDto(InsProdMonthPlanResultHeaderDto headerDto) {

		this.etcSosFlg = false;

		if (headerDto == null) {
			insName = null;
			jgiNo = null;
			insType = null;

		} else {
			insNo = headerDto.getInsNo();
			insName = headerDto.getInsName();
			jgiNo = null;
			if (headerDto.getJgiNo() != null) {
				jgiNo = headerDto.getJgiNo().toString();
			}
			insType = null;
			if (headerDto.getInsType() != null) {
				insType = headerDto.getInsType().getDbValue();
			}
		}
	}

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.prodCategoryTran = this.prodCategory;
		this.planDataTran = this.planData;
		this.insNoTran = this.insNo;
		this.etcSosFlgTran = this.etcSosFlg;
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.sosCd2 = this.sosCd2Tran;
		this.sosCd3 = this.sosCd3Tran;
		this.sosCd4 = this.sosCd4Tran;
		this.jgiNo = this.jgiNoTran;
		this.prodCategory = this.prodCategoryTran;
		this.planData = this.planDataTran;
		this.insNo = this.insNoTran;
		this.etcSosFlg = this.etcSosFlgTran;
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
		setEtcSosFlg(false);
		setInsNo(null);
		setInsName(null);
		setInsType(null);
		setProdCategory("1");
		setPlanData("0");
	}
}
