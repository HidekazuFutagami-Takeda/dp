package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsPlanForVacUpdateDto;
import jp.co.takeda.dto.InsPlanResultDto;
import jp.co.takeda.dto.InsPlanScDto;
import jp.co.takeda.dto.InsPlanUpdateDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm200C00((医)施設別計画編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dpm200C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM200C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm200C00Form.class, InsPlanResultDto.class);

	/**
	 * （ワ）検索結果(集計行)取得キー
	 */
	public static final BoxKey DPM200C01_DATA_R_SEARCH_RESULT_TOTAL = new BoxKeyPerClassImpl(Dpm200C00Form.class, InsPlanForVacResultDetailTotalDto.class);

	/**
	 * （ワ）検索結果取得キー
	 */
	public static final BoxKey DPM200C01_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm200C00Form.class, InsPlanForVacResultDto.class);


	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM200C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・支店
	 */
	public static final ProdPlanLevel DPM200C00_PLANLEVEL_INS = ProdPlanLevel.INS;

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM200C01_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・施設
	 */
	public static final ProdPlanLevel DPM200C01_PLANLEVEL_INS = ProdPlanLevel.INS;

	// -------------------------------
	// 部品
	// -------------------------------
	/**
	 * 重点先/一般先
	 */
	public static final List<CodeAndValue> ACTIVITY_TYPES;
	/**
	 * static initilizer
	 */
	static {
		List<CodeAndValue> tmp = null;

		tmp = new ArrayList<CodeAndValue>(2);
		tmp.add(new CodeAndValue(ActivityType.JTN.getDbValue(), "重点先"));
		tmp.add(new CodeAndValue(ActivityType.IPPAN.getDbValue(), "一般先"));
		ACTIVITY_TYPES = Collections.unmodifiableList(tmp);
	}


	/**
	 * [プルダウンリスト]重点先/一般先を取得する。
	 *
	 * @return [プルダウンリスト]重点先/一般先
	 */
	public List<CodeAndValue> getActivityTypes() {
		return ACTIVITY_TYPES;
	}

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
	 * 雑組織フラグ
	 */
	private boolean etcSosFlg;

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
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 施設名
	 */
	private String insName;

	/**
	 * 対象区分
	 */
	private String insType;

	/**
	 * 重点先/一般先
	 */
	private String activityType;

	/**
	 * JIS府県コード
	 */
	private String addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）
	 */
	private String shikuchosonMeiKj;

	/**
	 * 府県名（漢字）
	 */
	private String fukenMeiKj;

	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目コード
	 */
	private String prodCode;

	/**
	 * 計画検索範囲
	 */
	private String planData;

	// TOP画面用
	/**
	 * [トップ用]従業員番号
	 */
	private String topJgiNo;

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
	 * 処理用組織コード(従業員番号)
	 */
	private String jgiNoTran;

	/**
	 * 処理用組織コード(対象区分)
	 */
	private String insTypeTran;

	/**
	 * 処理用重点先/一般先
	 */
	private String activityTypeTran;

	/**
	 * 処理用JIS府県コード
	 */
	private String addrCodePrefTran;

	/**
	 * 処理用JIS市区町村コード
	 */
	private String addrCodeCityTran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 処理用品目コード
	 */
	private String prodCodeTran;

	/**
	 * 処理用組織コード(計画)
	 */
	private String planDataTran;

	/**
	 * 処理用組織コード(施設コード)
	 */
	private String insNoTran;

	/**
	 * 処理用雑組織フラグ
	 */
	private boolean etcSosFlgTran;

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * 処理用カテゴリ
	 */
	private String sosCategoryTran;

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

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
	 * 重点先/一般先を取得する
	 */
	public String getActivityType() {
		return activityType;
	}
	/**
	 * 重点先/一般先を設定する
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * 処理用JIS府県コードを取得する
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}
	/**
	 * 処理用JIS府県コードを設定する
	 */
	public void setAddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}


	public String getAddrCodeCity() {
		return addrCodeCity;
	}
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}


	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}


	public String getFukenMeiKj() {
		return fukenMeiKj;
	}
	public void setFukenMeiKj(String fukenMeiKj) {
		this.fukenMeiKj = fukenMeiKj;
	}


	public String getActivityTypeTran() {
		return activityTypeTran;
	}
	public void setActivityTypeTran(String activityTypeTran) {
		this.activityTypeTran = activityTypeTran;
	}


	public String getAddrCodePrefTran() {
		return addrCodePrefTran;
	}
	public void setAddrCodePrefTran(String addrCodePrefTran) {
		this.addrCodePrefTran = addrCodePrefTran;
	}



	public String getAddrCodeCityTran() {
		return addrCodeCityTran;
	}
	public void setAddrCodeCityTran(String addrCodeCityTran) {
		this.addrCodeCityTran = addrCodeCityTran;
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
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目コードを設定する。
	 *
	 * @param prodCode 品目コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
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
	 * 処理用対象区分を取得する。
	 *
	 * @return 処理用対象区分
	 */
	public String getInsTypeTran() {
		return insTypeTran;
	}

	/**
	 * 処理用対象区分を設定する。
	 *
	 * @param insTypeTran 処理用対象区分
	 */
	public void setInsTypeTran(String insTypeTran) {
		this.insTypeTran = insTypeTran;
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
	 * 処理用品目コードを取得する。
	 *
	 * @return 処理用品目コード
	 */
	public String getProdCodeTran() {
		return prodCodeTran;
	}

	/**
	 * 処理用品目コードを設定する。
	 *
	 * @param prodCodeTran 処理用品目コード
	 */
	public void setProdCodeTran(String prodCodeTran) {
		this.prodCodeTran = prodCodeTran;
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
     * ダウンロードファイル名を設定する。
     *
     * @param downloadFileName ダウンロードファイル名
     */
	public void setDownLoadFileName(String downloadFileName) {
    this.downloadFileName = downloadFileName;
}

	//add
	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 対象区分リスト
	 */
	private List<CodeAndValue> insTypeList;

	/**
	 * 対象区分リスト（雑あり）
	 */
	private List<CodeAndValue> insTypeZList;

	/**
	 * 計画（月別計画）
	 */
	private String planId;

	/**
	 * 品目カテゴリリストを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public List<CodeAndValue> getProdCategoryList() {
		return prodCategoryList;
	}

	/**
	 * 品目カテゴリリストを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	/**
	 * 対象区分リストを取得する。
	 *
	 * @return insTypeList 対象区分（ins_type）
	 */
	public List<CodeAndValue> getInsTypeList() {
		return insTypeList;
	}

	/**
	 * 対象区分リストを設定する。
	 *
	 * @param insTypeList 対象区分（ins_type）
	 */
	public void setInsTypeList(List<CodeAndValue> insTypeList) {
		this.insTypeList = insTypeList;
	}

	/**
	 * 対象区分リスト（雑あり）を取得する。
	 *
	 * @return insTypeZList 対象区分（ins_type）
	 */
	public List<CodeAndValue> getInsTypeZList() {
		return insTypeZList;
	}

	/**
	 * 対象区分リスト（雑あり）を設定する。
	 *
	 * @param insTypeZList 対象区分（ins_type）
	 */
	public void setInsTypeZList(List<CodeAndValue> insTypeZList) {
		this.insTypeZList = insTypeZList;
	}


	/**
	 * 計画（期別計画）を取得する。
	 *
	 * @return planId
	 */
	public String getPlanId() {
		return planId;
	}

	/**
	 * 計画（期別計画）を設定する。
	 *
	 * @param planId
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}


	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public InsPlanScDto convertInsPlanScDto() throws Exception {
		// 組織コード(支店)
		if (StringUtils.isEmpty(this.sosCd2Tran)) {
			this.sosCd2Tran = null;
		}
		// 組織コード(営業所)
		if (StringUtils.isEmpty(this.sosCd3Tran)) {
			this.sosCd3Tran = null;
		}
		// 組織コード(チーム)
		if (StringUtils.isEmpty(this.sosCd4Tran)) {
			this.sosCd4Tran = null;
		}
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
		// 対象区分
		InsType _insTypeTran = null;
		if (StringUtils.isNotEmpty(this.insTypeTran)) {
			_insTypeTran = InsType.getInstance(this.insTypeTran);
		} else {
			this.insTypeTran = null;
		}
		// 品目カテゴリ
		if (StringUtils.isEmpty(prodCategoryTran)) {
			this.prodCategoryTran = null;
		}
		// 品目コード
		if (StringUtils.isEmpty(prodCodeTran)) {
			this.prodCodeTran = null;
		}
		// 計画検索範囲
		PlanData _planDataTran = null;
		if (StringUtils.isNotEmpty(this.planDataTran)) {
			_planDataTran = PlanData.getInstance(this.planDataTran);
		} else {
			this.planDataTran = null;
		}
		return new InsPlanScDto(sosCd2Tran, sosCd3Tran, sosCd4Tran, _jgiNoTran, insNoTran, _insTypeTran, prodCategoryTran, prodCodeTran, _planDataTran);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return InsPlanUpdateDto 変換されたUpdateDto
	 */
	public List<InsPlanUpdateDto> convertInsPlanUpdateDto() throws Exception {
		List<InsPlanUpdateDto> updateDtoList = new ArrayList<InsPlanUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 計画値に変更がある場合のみ更新
				if (StringUtils.equals(rowId[5], rowId[6])) {
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
				// 更新前計画値
				final Long yBaseValueBefore = ConvertUtil.parseLong(rowId[5]);
				// 更新後計画値
				final Long yBaseValueAfter = ConvertUtil.parseLong(rowId[6]);
				// 更新用DTO生成
				final InsPlanUpdateDto updateDto = new InsPlanUpdateDto(insNo, insType, prodCode, seqKey, upDate, yBaseValueBefore, yBaseValueAfter);
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
	public void convertHeaderDto(InsProdPlanResultHeaderDto headerDto) {
		if (headerDto == null) {
			insName = null;
			jgiNo = null;
			etcSosFlg = false;
			insType = InsType.UH.getDbValue();
			activityType = ActivityType.JTN.getDbValue();
			addrCodePref = null;
			addrCodeCity = null;
			shikuchosonMeiKj = null;
			fukenMeiKj = null;
			etcSosFlg = false;

		} else {
			insNo = headerDto.getInsNo();
			insName = headerDto.getInsName();
			jgiNo = null;
			etcSosFlg = false;
			if (headerDto.getJgiNo() != null) {
				jgiNo = headerDto.getJgiNo().toString();
			}
			insType = null;
			if (headerDto.getInsType() != null) {
				insType = headerDto.getInsType().getDbValue();
			}
			activityType = ActivityType.JTN.getDbValue();
			if (headerDto.getActivityType() != null) {
				activityType = headerDto.getActivityType().getDbValue();
			}
			addrCodePref = null;
			if (headerDto.getAddrCodePref() != null) {
				addrCodePref = headerDto.getAddrCodePref().getDbValue();
			}
			addrCodeCity = headerDto.getAddrCodeCity();
			shikuchosonMeiKj = headerDto.getShikuchosonMeiKj();
			fukenMeiKj = headerDto.getFukenMeiKj();
			}
	}

	/**
	 * （ワ）ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public InsPlanForVacScDto convertInsPlanVacScDto() throws Exception {
		// 組織コード(支店)
		if (StringUtils.isEmpty(this.sosCd2Tran)) {
			this.sosCd2Tran = null;
		}
		// 組織コード(営業所)
		if (StringUtils.isEmpty(this.sosCd3Tran)) {
			this.sosCd3Tran = null;
		}
		// 組織コード(チーム)
		if (StringUtils.isEmpty(this.sosCd4Tran)) {
			this.sosCd4Tran = null;
		}
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
		// 活動区分
		ActivityType _activityTypeTran = null;
		if (StringUtils.isNotEmpty(this.activityTypeTran)) {
			_activityTypeTran = ActivityType.getInstance(this.activityTypeTran);
		} else {
			this.activityTypeTran = null;
		}
		// JIS府県コード
		Prefecture _addrCodePrefTran = null;
		if (StringUtils.isNotEmpty(this.addrCodePrefTran)) {
			_addrCodePrefTran = Prefecture.getInstance(this.addrCodePrefTran);
		} else {
			this.addrCodePrefTran = null;
		}
		// JIS市区町村コード
		if (StringUtils.isEmpty(addrCodeCityTran)) {
			this.addrCodeCityTran = null;
		}
		// 品目カテゴリ
		if (StringUtils.isEmpty(prodCategoryTran)) {
			this.prodCategoryTran = null;
		}
		// 品目コード
		if (StringUtils.isEmpty(prodCodeTran)) {
			this.prodCodeTran = null;
		}
		return new InsPlanForVacScDto(sosCd2Tran, sosCd3Tran, sosCd4Tran,_jgiNoTran, insNoTran, _activityTypeTran, _addrCodePrefTran, addrCodeCityTran, prodCategoryTran, prodCodeTran);
	}

	/**
	 * （ワ）ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return InsPlanUpdateDto 変換されたUpdateDto
	 */
	public List<InsPlanForVacUpdateDto> convertInsPlanVacUpdateDto() throws Exception {
		List<InsPlanForVacUpdateDto> updateDtoList = new ArrayList<InsPlanForVacUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 計画値に変更がある場合のみ更新
				if (StringUtils.equals(rowId[4], rowId[5])) {
					continue;
				}
				// 施設コード
				final String insNo = rowId[0];
				// 品目コード
				final String prodCode = rowId[1];
				// シーケンスキー
				final Long seqKey = ConvertUtil.parseLong(rowId[2]);
				// 最終更新日時
				final Date upDate = ConvertUtil.parseDate(rowId[3]);
				// 更新前計画値
				final Long yBaseValueBefore = ConvertUtil.parseLong(rowId[4]);
				// 更新後計画値
				final Long yBaseValueAfter = ConvertUtil.parseLong(rowId[5]);
				// 更新用DTO生成
				final InsPlanForVacUpdateDto updateDto = new InsPlanForVacUpdateDto(insNo, prodCode, seqKey, upDate, yBaseValueBefore, yBaseValueAfter);
				updateDtoList.add(updateDto);
			}
		}
		rowIdList = null;
		return updateDtoList;
	}

	/**
	 * （ワ）headerDto ⇒ ActionForm 変換処理
	 *
	 * @param headerDto ヘッダ情報DTO
	 */
	public void convertHeaderVacDto(InsPlanForVacHeaderDto headerDto) {

		if (headerDto == null) {
			// insNoはそのまま
			insName = null;
			jgiNo = null;
			activityType = ActivityType.JTN.getDbValue();
			addrCodePref = null;
			addrCodeCity = null;
			shikuchosonMeiKj = null;
			fukenMeiKj = null;
			etcSosFlg = false;

		} else {
			insNo = headerDto.getInsNo();
			insName = headerDto.getInsName();
			jgiNo = null;
			etcSosFlg = false;
			if (headerDto.getJgiNo() != null) {
				jgiNo = headerDto.getJgiNo().toString();
			}
			activityType = ActivityType.JTN.getDbValue();
			if (headerDto.getActivityType() != null) {
				activityType = headerDto.getActivityType().getDbValue();
			}
			addrCodePref = null;
			if (headerDto.getAddrCodePref() != null) {
				addrCodePref = headerDto.getAddrCodePref().getDbValue();
			}
			addrCodeCity = headerDto.getAddrCodeCity();
			shikuchosonMeiKj = headerDto.getShikuchosonMeiKj();
			fukenMeiKj = headerDto.getFukenMeiKj();
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
		this.insTypeTran = this.insType;
		this.prodCodeTran = this.prodCode;
		this.prodCategoryTran = this.prodCategory;
		this.planDataTran = this.planData;
		this.insNoTran = this.insNo;
		this.etcSosFlgTran = this.etcSosFlg;
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
		this.insType = this.insTypeTran;
		this.prodCode = this.prodCodeTran;
		this.prodCategory = this.prodCategoryTran;
		this.planData = this.planDataTran;
		this.insNo = this.insNoTran;
		this.etcSosFlg = this.etcSosFlgTran;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCategory = this.sosCategoryTran;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	}

	/**
	 * （ワ）画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranFieldVac() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.activityTypeTran = this.activityType;
		this.addrCodePrefTran = this.addrCodePref;
		this.addrCodeCityTran = this.addrCodeCity;
		this.prodCodeTran = this.prodCode;
		this.prodCategoryTran = this.prodCategory;
		this.insNoTran = this.insNo;
		this.etcSosFlgTran = this.etcSosFlg;
	}

	/**
	 * （ワ）処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewFieldVac() {
		this.sosCd2 = this.sosCd2Tran;
		this.sosCd3 = this.sosCd3Tran;
		this.sosCd4 = this.sosCd4Tran;
		this.jgiNo = this.jgiNoTran;
		this.activityType = this.activityTypeTran;
		this.addrCodePref = this.addrCodePrefTran;
		this.addrCodeCity = this.addrCodeCityTran;
		this.prodCode = this.prodCodeTran;
		this.prodCategory = this.prodCategoryTran;
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
		setInsNo(null);
		setInsName(null);
		setInsType(null);
		setEtcSosFlg(false);
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		setSosCategory(null);
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		setProdCategory(null);
		setProdCode(null);
		setPlanData("0");
		setTopJgiNo(null);
		setActivityType(null);
		setAddrCodePref(null);
		setAddrCodeCity(null);
		setShikuchosonMeiKj(null);
		setFukenMeiKj(null);
	}
}
