package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.SearchInsType;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.InsMstScDto;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dpm911C00(施設選択画面)のフォームクラス
 *
 * @author khashimoto
 */
public class Dpm911C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPM911C00_DATA_R
	 */
	public static final BoxKey DPM911C00_DATA_R = new BoxKeyPerClassImpl(Dpm911C00Form.class, InsMstResultDto.class);

	/**
	 * DPM911C00F10_DATA_R
	 */
	public static final BoxKey DPM911C00F10_DATA_R = new BoxKeyPerClassImpl(Dpm911C00Form.class, InsMstResultDto.class);

	/**
	 * SEARCH_CONDITION_S
	 */
	public static final BoxKey SEARCH_CONDITION_S = new BoxKeyPerClassImpl(Dpm911C00Form.class, InsMstScDto.class);

	/**
	 * 1ページ毎の表示件数
	 */
	private static final int PAGE_COUNT = 100;

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return InsMstScDto 変換されたScDto
	 */
	public InsMstScDto convertInsMstScDto() throws Exception {

		// ページ番号
		this.crntPageNo = 1;

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3)) {
			sosCd3 = null;
		} else {
			this.sosInitSosCodeValue = this.sosCd3;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4)) {
			sosCd4 = null;
		} else {
			this.sosInitSosCodeValue = this.sosCd4;
		}

		// 従業員番号
		Integer iJgiNo = ConvertUtil.parseInteger(jgiNo);

		// 検索用対象区分
		SearchInsType searchInsType = null;
		if (StringUtils.equals(insType, "1")) {
			searchInsType = SearchInsType.UHP;
		} else if (StringUtils.equals(insType, "2")) {
			searchInsType = SearchInsType.UH;
		} else if (StringUtils.equals(insType, "3")) {
			searchInsType = SearchInsType.U;
		} else if (StringUtils.equals(insType, "4")) {
			searchInsType = SearchInsType.H;
		} else if (StringUtils.equals(insType, "5")) {
			searchInsType = SearchInsType.P;
		} else if (StringUtils.equals(insType, "6")) {
			searchInsType = SearchInsType.PV;
		} else if (StringUtils.equals(insType, "7")) {
			searchInsType = SearchInsType.VAC;
		}

		if (paramJtnFlg == null) {
			paramJtnFlg = false;
		}

		if (paramIppanFlg == null) {
			paramIppanFlg = false;
		}

		// 府県コード
		if (StringUtils.isBlank(paramAddrCodePref)) {
			paramAddrCodePref = null;
		}

		// 市区町村コード
		if (StringUtils.isBlank(paramAddrCodeCity)) {
			paramAddrCodeCity = null;
		}

		if(StringUtils.isBlank(prodCode)) {
			prodCode = null;
		}

		return new InsMstScDto(sosCd3, sosCd4, iJgiNo, searchInsType, insNameZenKana, insNameHanKana, paramJtnFlg, paramIppanFlg, paramAddrCodePref, paramAddrCodeCity, etcSosFlg ,prodCode);
	}

	/**
	 * ScDto⇒ActionForm変換処理
	 *
	 * @return dto 変換されたScDto
	 */
	public void convertForm(InsMstScDto dto) throws Exception {

		// 組織・従業員復元
		if (dto.getJgiNo() != null) {
			if (StringUtils.isNotBlank(dto.getSosCd4())) {
				this.sosInitSosCodeValue = dto.getSosCd4();
			} else {
				this.sosInitSosCodeValue = dto.getSosCd3();
			}
			this.sosCd3 = dto.getSosCd3();
			this.sosCd4 = dto.getSosCd4();
			this.jgiNo = dto.getJgiNo().toString();
		} else if (StringUtils.isNotBlank(dto.getSosCd4())) {
			this.sosInitSosCodeValue = dto.getSosCd4();
			this.sosCd3 = dto.getSosCd3();
			this.sosCd4 = dto.getSosCd4();
			this.jgiNo = null;
		} else if (StringUtils.isNotBlank(dto.getSosCd3())) {
			this.sosInitSosCodeValue = dto.getSosCd3();
			this.sosCd3 = dto.getSosCd3();
			this.sosCd4 = null;
			this.jgiNo = null;
		}

		// 検索用対象区分
		SearchInsType searchInsType = dto.getSearchInsType();
		if (searchInsType != null) {
			switch (searchInsType) {
				case UHP:
					insType = "1";
					break;
				case UH:
					insType = "2";
					break;
				case U:
					insType = "3";
					break;
				case H:
					insType = "4";
					break;
				case P:
					insType = "5";
					break;
				case PV:
					insType = "6";
					break;
				case VAC:
					insType = "7";
					break;
			}
		}

		// 施設名全角
		String insFormalName = dto.getInsFormalName();
		if (StringUtils.isNotBlank(insFormalName)) {
			this.insNameZenKana = insFormalName;
		} else {
			this.insNameZenKana = null;
		}

		// 施設名半角
		String insKanaSrch = dto.getInsKanaSrch();
		if (StringUtils.isNotBlank(insKanaSrch)) {
			this.insNameHanKana = insKanaSrch;
		} else {
			this.insNameHanKana = null;
		}

		// 府県コード
		Prefecture prefecture = dto.getAddrCodePref();
		if (prefecture != null) {
			this.paramAddrCodePref = prefecture.getDbValue();
		} else {
			this.paramAddrCodePref = null;
		}

		// 市区町村
		String addrCodeCity = dto.getAddrCodeCity();
		if (StringUtils.isNotBlank(addrCodeCity)) {
			this.paramAddrCodeCity = addrCodeCity;
		} else {
			this.paramAddrCodeCity = null;
		}

		// 重点先、一般先
		ActivityType activityType = dto.getActivityType();
		if (activityType != null) {
			if (activityType == ActivityType.JTN) {
				paramJtnFlg = true;
				paramIppanFlg = null;
			} else if (activityType == ActivityType.IPPAN) {
				paramJtnFlg = null;
				paramIppanFlg = true;
			}
		} else {
			paramJtnFlg = null;
			paramIppanFlg = null;
		}

		// 雑担当
		if (dto.getEtcSosFlg() != null) {
			etcSosFlg = dto.getEtcSosFlg();
		} else {
			etcSosFlg = false;
		}

		// 品目コード
		this.prodCode = dto.getProdCode();
		if (StringUtils.isBlank(this.prodCode)) {
			this.prodCode = null;
		}
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
	 * 対象区分(P)
	 */
	public static final List<CodeAndValue> INS_TYPES_P;

	/**
	 * 対象区分(ワクチン)
	 */
	public static final List<CodeAndValue> INS_TYPES_V;

	/**
	 * static initilizer
	 */
	static {
		List<CodeAndValue> tmp = null;
		// 対象区分にセット
		tmp = new ArrayList<CodeAndValue>(8);
		tmp.add(new CodeAndValue("0", "全て"));
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
		tmp = new ArrayList<CodeAndValue>(2);
		tmp.add(new CodeAndValue("5", "P"));
		tmp.add(new CodeAndValue("6", "P(含ワクチン)"));
		INS_TYPES_P = Collections.unmodifiableList(tmp);

		// 対象区分にセット
		tmp = new ArrayList<CodeAndValue>(1);
		tmp.add(new CodeAndValue("7", "ワクチン"));
		INS_TYPES_V = Collections.unmodifiableList(tmp);

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
	 * 一般先フラグ（一般先のみ検索：true）
	 */
	private Boolean paramIppanFlg;

	/**
	 * 府県コード
	 */
	private String paramAddrCodePref;

	/**
	 * 市区町村コード
	 */
	private String paramAddrCodeCity;

	/**
	 * 削除施設選択不可フラグ
	 */
	private Boolean disableDelInsSelect;

	/**
	 * モール施設選択不可フラグ
	 */
	private Boolean disableMallInsSelect;

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

	/**
	 * 雑組織フラグ
	 */
	private boolean etcSosFlg;

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
	 * 現在ページ番号
	 */
	private int crntPageNo = 1;


	/**
	 * 品目コード
	 */
	private String prodCode;

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
	 * 一般先フラグ（一般先のみ検索：true）を取得する。
	 *
	 * @return 一般先フラグ（一般先のみ検索：true）
	 */
	public Boolean getParamIppanFlg() {
		return paramIppanFlg;
	}

	/**
	 * 一般先フラグ（一般先のみ検索：true）を設定する。
	 *
	 * @param paramIppanFlg 一般先フラグ（一般先のみ検索：true）
	 */
	public void setParamIppanFlg(Boolean paramIppanFlg) {
		this.paramIppanFlg = paramIppanFlg;
	}

	/**
	 * 府県コードを取得する。
	 *
	 * @return 府県コード
	 */
	public String getParamAddrCodePref() {
		return paramAddrCodePref;
	}

	/**
	 * 府県コードを設定する。
	 *
	 * @param paramAddrCodePref 府県コード
	 */
	public void setParamAddrCodePref(String paramAddrCodePref) {
		this.paramAddrCodePref = paramAddrCodePref;
	}

	/**
	 * 市区町村コードを取得する。
	 *
	 * @return 市区町村コード
	 */
	public String getParamAddrCodeCity() {
		return paramAddrCodeCity;
	}

	/**
	 * 市区町村コードを設定する。
	 *
	 * @param paramAddrCodeCity 市区町村コード
	 */
	public void setParamAddrCodeCity(String paramAddrCodeCity) {
		this.paramAddrCodeCity = paramAddrCodeCity;
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
	 * モール施設選択不可フラグを取得する。
	 *
	 * @return モール施設選択不可フラグ
	 */
	public Boolean getDisableMallInsSelect() {
		return disableMallInsSelect;
	}

	/**
	 * モール施設選択不可フラグを設定する。
	 *
	 * @param disableMallInsSelect モール施設選択不可フラグ
	 */
	public void setDisableMallInsSelect(Boolean disableMallInsSelect) {
		this.disableMallInsSelect = disableMallInsSelect;
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
	 * 1ページ毎の表示件数を取得する。
	 *
	 * @return 1ページ毎の表示件数
	 */
	public static int getPageCount() {
		return PAGE_COUNT;
	}

	/**
	 * 現在ページ番号を取得する。
	 *
	 * @return 現在ページ番号
	 */
	public int getCrntPageNo() {
		return crntPageNo;
	}

	/**
	 * 現在ページ番号を設定する。
	 *
	 * @param crntPageNo 現在ページ番号
	 */
	public void setCrntPageNo(int crntPageNo) {
		this.crntPageNo = crntPageNo;
	}

	/**
	 * 品目コードを取得する。
	 * @return prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目コードを設定する。
	 * @param prodCode 品目コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * パラメータが設定されずに呼び出される可能性があるため、リセットを行う。
	 *
	 * @see jp.co.takeda.a.web.action.AbstractActionForm#reset()
	 */
	@Override
	public void reset() {
		this.paramJtnFlg = null;
		this.paramIppanFlg = null;
		this.paramInsType = null;
		this.paramJgiNo = null;
		this.paramAddrCodePref = null;
		this.paramAddrCodeCity = null;
		this.disableDelInsSelect = false;
		this.disableMallInsSelect = false;
		this.etcSosFlg = false;
		this.crntPageNo = 1;
		this.prodCode = null;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.insType = "1";
		this.insNameZenKana = "";
		this.insNameHanKana = "";
		this.existSearchDataFlag = false;
		if (StringUtils.isEmpty(paramInsType)) {
			this.insTypes = INS_TYPES;
		} else if (paramInsType.equals("1")) {
			this.insTypes = INS_TYPES_UH;
		} else if (paramInsType.equals("2")) {
			this.insTypes = INS_TYPES_P;
		} else {
			this.insTypes = INS_TYPES_V;
		}
		this.jgiNo = paramJgiNo;
		this.crntPageNo = 1;
	}
}
