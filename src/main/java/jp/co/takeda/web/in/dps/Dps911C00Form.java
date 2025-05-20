package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.DpsSearchInsType;
import jp.co.takeda.dto.DpsInsMstScDto;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps911C00(施設選択画面)のフォームクラス
 *
 * @author khashimoto
 */
public class Dps911C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS911C00_DATA_R
	 */
	public static final BoxKey DPS911C00_DATA_R = new BoxKeyPerClassImpl(Dps911C00Form.class, InsMstResultDto.class);

	/**
	 * DPS911C00F10_DATA_R
	 */
	public static final BoxKey DPS911C00F10_DATA_R = new BoxKeyPerClassImpl(Dps911C00Form.class, InsMstResultDto.class);

	/**
	 * DPS911C00F11_DATA_R
	 */
	public static final BoxKey DPS911C00F11_DATA_R = new BoxKeyPerClassImpl(Dps911C00Form.class, List.class);

	/**
	 * DPS911C00F10_DATA_R
	 */
	public static final BoxKey DPS911C00F10_EXCEPT_DIST_INS = new BoxKeyPerClassImpl(Dps911C00Form.class, Boolean.class);

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return InsMstScDto 変換されたScDto
	 */
	public DpsInsMstScDto convertInsMstScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3)) {
			sosCd3 = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4)) {
			sosCd4 = null;
		}

		// カテゴリ
		if (StringUtils.isEmpty(category)) {
			category = null;
		}

		// 従業員番号
		Integer iJgiNo = ConvertUtil.parseInteger(jgiNo);

		// 検索用対象区分
		DpsSearchInsType searchInsType = null;
		if (StringUtils.equals(insType, "1")) {
			searchInsType = DpsSearchInsType.UHP;
		} else if (StringUtils.equals(insType, "2")) {
			searchInsType = DpsSearchInsType.UH;
		} else if (StringUtils.equals(insType, "3")) {
			searchInsType = DpsSearchInsType.U;
		} else if (StringUtils.equals(insType, "4")) {
			searchInsType = DpsSearchInsType.H;
		} else if (StringUtils.equals(insType, "5")) {
			searchInsType = DpsSearchInsType.P;
		} else if (StringUtils.equals(insType, "6")) {
			searchInsType = DpsSearchInsType.PV;
		} else if (StringUtils.equals(insType, "7")) {
			searchInsType = DpsSearchInsType.VAC;
		}

		if (paramJtnFlg == null) {
			paramJtnFlg = false;
		}

		String prodCode = null;
		if (StringUtils.isNotBlank(paramProdCode)) {
			prodCode = paramProdCode;
		}

		return new DpsInsMstScDto(sosCd3, sosCd4, iJgiNo, searchInsType, insNameZenKana, insNameHanKana, paramJtnFlg, prodCode, includeSeikei);
	}

	// -------------------------------
	// 部品
	// -------------------------------
	/**
	 * 対象区分
	 */
	private List<CodeAndValue> insTypes;

	/**
	 * 対象区分(全て)
	 */
	public static final List<CodeAndValue> INS_TYPES;

	/**
	 * 対象区分(UH)
	 */
	public static final List<CodeAndValue> INS_TYPES_UH;

	/**
	 * 対象区分(P)【ワクチン】
	 */
	public static final List<CodeAndValue> INS_TYPES_P_V;

	/**
	 * 対象区分(P)【医薬】
	 */
	public static final List<CodeAndValue> INS_TYPES_P;

	/**
	 * static initilizer
	 */
	static {
		List<CodeAndValue> tmp = null;
		// 対象区分にセット
		tmp = new ArrayList<CodeAndValue>(8);
		tmp.add(new CodeAndValue("", "全て"));
		tmp.add(new CodeAndValue("1", "UHP"));
		tmp.add(new CodeAndValue("2", "UH"));
		tmp.add(new CodeAndValue("3", "U"));
		tmp.add(new CodeAndValue("4", "H"));
		tmp.add(new CodeAndValue("5", "P"));
		tmp.add(new CodeAndValue("6", "P(含ワクチン)"));
		tmp.add(new CodeAndValue("7", "ワクチン"));
		INS_TYPES = Collections.unmodifiableList(tmp);

		// 対象区分にセット
		tmp = new ArrayList<CodeAndValue>(3);
		tmp.add(new CodeAndValue("2", "UH"));
		tmp.add(new CodeAndValue("3", "U"));
		tmp.add(new CodeAndValue("4", "H"));
		INS_TYPES_UH = Collections.unmodifiableList(tmp);

		// 対象区分にセット
		tmp = new ArrayList<CodeAndValue>(3);
		tmp.add(new CodeAndValue("5", "P"));
		tmp.add(new CodeAndValue("6", "P(含ワクチン)"));
		tmp.add(new CodeAndValue("7", "ワクチン"));
		INS_TYPES_P_V = Collections.unmodifiableList(tmp);

		// 対象区分にセット
		tmp = new ArrayList<CodeAndValue>(1);
		tmp.add(new CodeAndValue("5", "P"));
		INS_TYPES_P = Collections.unmodifiableList(tmp);

	}

	/**
	 * [プルダウンリスト]対象区分を取得する。
	 *
	 * @return [プルダウンリスト]対象区分
	 */
	public List<CodeAndValue> getInsTypes() {
		return insTypes;
	}

	// -------------------------------
	// field
	// -------------------------------
	// INパラメータ
	/**
	 * 適用関数名
	 */
	private String insApplyFuncName;

	/**
	 * 施設選択パターン区分
	 */
	private String insSelectPtnType;

	/**
	 * 組織検索パターン区分
	 */
	private String sosSrchPtnType;

	/**
	 * 検索最小階層取得値
	 */
	private String sosMinSrchValue;

	/**
	 * 検索最大階層取得値
	 */
	private String sosMaxSrchGetValue;

	/**
	 * 初期表示組織コード
	 */
	private String sosInitSosCodeValue;

	/**
	 * 対象区分(パラメータ)
	 */
	private String paramInsType;

	/**
	 * 従業員番号(パラメータ)
	 */
	private String paramJgiNo;

	/**
	 * 組織検索無フラグ（組織検索無し：true）
	 */
	private Boolean sosSearchNonFlg;

	/**
	 * 重点先フラグ（重点先のみ検索：true）
	 */
	private Boolean paramJtnFlg;

	/**
	 * 品目コード(パラメータ)
	 */
	private String paramProdCode;

	/**
	 * 整形フラグ
	 */
	private boolean includeSeikei;

	// 検索条件
	/**
	 * 組織コード（営業所）
	 */
	private String sosCd3;

	/**
	 * 組織コード（チーム）
	 */
	private String sosCd4;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 対象区分
	 */
	private String insType;

	/**
	 * 施設名称(全角)
	 */
	private String insNameZenKana;

	/**
	 * 施設名称(半角カナ)
	 */
	private String insNameHanKana;

	/**
	 * 検索結果の存在判定フラグ
	 *
	 * <pre>
	 * 検索アクション後にセットします。
	 * TRUE=検索結果あり<br>
	 * FALSE=検索結果なし(ヒット件数0)
	 * </pre>
	 */
	private Boolean existSearchDataFlag;

	// OUTパラメータ
	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 選択施設コードリスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=施設コード
	 * Sprit文字列[1]=従業員番号
	 * (例)---------------------------------------
	 * insNoList[0] = "12345,123495"
	 * insNoList[1] = "22432,112284"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] insNoList;

	/**
	 * 削除施設選択不可フラグ
	 */
	private Boolean disableDelInsSelect;

	/**
	 * カテゴリ(呼び出し元から)
	 */
	private String category;

	/**
	 * ワクチンコード
	 */
	private String vaccineCode;

	// -------------------------------
	// getter & setter
	// -------------------------------

	/**
	 * 適用関数名を取得する。
	 *
	 * @return 適用関数名
	 */
	public String getInsApplyFuncName() {
		return insApplyFuncName;
	}

	/**
	 * 適用関数名を設定する。
	 *
	 * @param insApplyFuncName 適用関数名
	 */
	public void setInsApplyFuncName(String insApplyFuncName) {
		this.insApplyFuncName = insApplyFuncName;
	}

	/**
	 * 施設選択パターン区分を取得する。
	 *
	 * @return 施設選択パターン区分
	 */
	public String getInsSelectPtnType() {
		return insSelectPtnType;
	}

	/**
	 * 施設選択パターン区分を設定する。
	 *
	 * @param insSelectPtnType 施設選択パターン区分
	 */
	public void setInsSelectPtnType(String insSelectPtnType) {
		this.insSelectPtnType = insSelectPtnType;
	}

	/**
	 * 組織検索パターン区分を取得する。
	 *
	 * @return 組織検索パターン区分
	 */
	public String getSosSrchPtnType() {
		return sosSrchPtnType;
	}

	/**
	 * 組織検索パターン区分を設定する。
	 *
	 * @param sosSrchPtnType 組織検索パターン区分
	 */
	public void setSosSrchPtnType(String sosSrchPtnType) {
		this.sosSrchPtnType = sosSrchPtnType;
	}

	/**
	 * 検索最小階層取得値を取得する。
	 *
	 * @return 検索最小階層取得値
	 */
	public String getSosMinSrchValue() {
		return sosMinSrchValue;
	}

	/**
	 * 検索最小階層取得値を設定する。
	 *
	 * @param sosMinSrchValue 検索最小階層取得値
	 */
	public void setSosMinSrchValue(String sosMinSrchValue) {
		this.sosMinSrchValue = sosMinSrchValue;
	}

	/**
	 * 検索最大階層取得値を取得する。
	 *
	 * @return 検索最大階層取得値
	 */
	public String getSosMaxSrchGetValue() {
		return sosMaxSrchGetValue;
	}

	/**
	 * 検索最大階層取得値を設定する。
	 *
	 * @param sosMaxSrchGetValue 検索最大階層取得値
	 */
	public void setSosMaxSrchGetValue(String sosMaxSrchGetValue) {
		this.sosMaxSrchGetValue = sosMaxSrchGetValue;
	}

	/**
	 * 初期表示組織コードを取得する。
	 *
	 * @return 初期表示組織コード
	 */
	public String getSosInitSosCodeValue() {
		return sosInitSosCodeValue;
	}

	/**
	 * 初期表示組織コードを設定する。
	 *
	 * @param sosInitSosCodeValue 初期表示組織コード
	 */
	public void setSosInitSosCodeValue(String sosInitSosCodeValue) {
		this.sosInitSosCodeValue = sosInitSosCodeValue;
	}

	/**
	 * 対象区分(パラメータ)を取得する。
	 *
	 * @return paramInsType 対象区分(パラメータ)
	 */
	public String getParamInsType() {
		return paramInsType;
	}

	/**
	 * 対象区分(パラメータ)を設定する。
	 *
	 * @param paramInsType 対象区分(パラメータ)
	 */
	public void setParamInsType(String paramInsType) {
		this.paramInsType = paramInsType;
	}

	/**
	 * 従業員番号(パラメータ)を取得する。
	 *
	 * @return 従業員番号(パラメータ)
	 */
	public String getParamJgiNo() {
		return paramJgiNo;
	}

	/**
	 * 従業員番号(パラメータ)を設定する。
	 *
	 * @param paramJgiNo 従業員番号(パラメータ)
	 */
	public void setParamJgiNo(String paramJgiNo) {
		this.paramJgiNo = paramJgiNo;
	}

	/**
	 * 組織検索無フラグ（組織検索無し：true）を取得する。
	 *
	 * @return sosSearchNonFlg 組織検索無フラグ（組織検索無し：true）
	 */
	public Boolean getSosSearchNonFlg() {
		return sosSearchNonFlg;
	}

	/**
	 * 組織検索無フラグ（組織検索無し：true）を設定する。
	 *
	 * @param sosSearchNonFlg 組織検索無フラグ（組織検索無し：true）
	 */
	public void setSosSearchNonFlg(Boolean sosSearchNonFlg) {
		this.sosSearchNonFlg = sosSearchNonFlg;
	}

	/**
	 * 重点先フラグを取得する。
	 *
	 * @return 重点先フラグ
	 */
	public Boolean getParamJtnFlg() {
		return paramJtnFlg;
	}

	/**
	 * 重点先フラグを設定する。
	 *
	 * @param paramJtnFlg 重点先フラグ
	 */
	public void setParamJtnFlg(Boolean paramJtnFlg) {
		this.paramJtnFlg = paramJtnFlg;
	}

	/**
	 * 品目コード(パラメータ)を取得する。
	 *
	 * @return 品目コード(パラメータ)
	 */
	public String getParamProdCode() {
		return paramProdCode;
	}

	/**
	 * 品目コード(パラメータ)を設定する。
	 *
	 * @param paramProdCode 品目コード(パラメータ)
	 */
	public void setParamProdCode(String paramProdCode) {
		this.paramProdCode = paramProdCode;
	}

	/**
	 * 整形を含めるかを取得する。
	 *
	 * @return true：含める、false：含めない
	 */
	public boolean isIncludeSeikei() {
		return includeSeikei;
	}

	/**
	 * 整形を含めるかを設定する。
	 *
	 * @param includeSeikei true：含める、false：含めない
	 */
	public void setIncludeSeikei(boolean includeSeikei) {
		this.includeSeikei = includeSeikei;
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
	 * 施設名称(全角)を取得する。
	 *
	 * @return insNameZenKana 施設名称(全角)
	 */
	public String getInsNameZenKana() {
		return insNameZenKana;
	}

	/**
	 * 施設名称(全角)を設定する。
	 *
	 * @param insNameZenKana 施設名称(全角)
	 */
	public void setInsNameZenKana(String insNameZenKana) {
		this.insNameZenKana = insNameZenKana;
	}

	/**
	 * 施設名称(半角カナ)を取得する。
	 *
	 * @return insKanaSrch 施設名称(半角カナ)
	 */
	public String getInsNameHanKana() {
		return insNameHanKana;
	}

	/**
	 * 施設名称(半角カナ)を設定する。
	 *
	 * @param insKanaSrch 施設名称(半角カナ)
	 */
	public void setInsNameHanKana(String insNameHanKana) {
		this.insNameHanKana = insNameHanKana;
	}

	/**
	 * 検索結果の存在判定フラグを取得する。
	 *
	 * @return existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public Boolean getExistSearchDataFlag() {
		return existSearchDataFlag;
	}

	/**
	 * 検索結果の存在判定フラグを設定する。
	 *
	 * @param existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public void setExistSearchDataFlag(Boolean existSearchDataFlag) {
		this.existSearchDataFlag = existSearchDataFlag;
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
	 * 施設コードリストを取得する。
	 *
	 * @return insNoList 施設コードリスト
	 */
	public String[] getInsNoList() {
		return insNoList;
	}

	/**
	 * 施設コードリストを設定する。
	 *
	 * @param insNoList 施設コードリスト
	 */
	public void setInsNoList(String[] insNoList) {
		this.insNoList = insNoList;
	}

	/**
	 * 削除施設選択不可フラグを取得する。
	 *
	 * @return 削除施設選択不可フラグ
	 */
	public Boolean getDisableDelInsSelect() {
		return disableDelInsSelect;
	}

	/**
	 * 削除施設選択不可フラグを設定する。
	 *
	 * @param disableDelInsSelect 削除施設選択不可フラグ
	 */
	public void setDisableDelInsSelect(Boolean disableDelInsSelect) {
		this.disableDelInsSelect = disableDelInsSelect;
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

	/**
	 * ワクチンコードを取得する。
	 *
	 * @return ワクチンコード
	 */
	public String getVaccineCode() {
		return vaccineCode;
	}

	/**
	 * ワクチンコードを設定する。
	 *
	 * @param vaccineCode ワクチンコード
	 */
	public void setVaccineCode(String vaccineCode) {
		this.vaccineCode = vaccineCode;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		insNameZenKana = "";
		insNameHanKana = "";
		existSearchDataFlag = false;
		if (StringUtils.isEmpty(paramInsType)) {
			insTypes = INS_TYPES;
			insType = "2";
		} else if (paramInsType.equals("1")) {
			insTypes = INS_TYPES_UH;
			insType = "2";
		} else {
			// ワクチンかどうかでリストを分ける
			if (this.category.equals(this.vaccineCode)) {
				insTypes = INS_TYPES_P_V;
			} else {
				insTypes = INS_TYPES_P;
			}
			insType = "5";
		}
	}
}
