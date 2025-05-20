package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.ManageInsWsPlanDto;
import jp.co.takeda.dto.ManageInsWsPlanEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.dto.ManageInsWsPlanHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm400C00((医)施設特約店計画編集)のフォームクラス
 *
 * @author siwamoto
 */
public class Dpm400C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * DPM400C00_DATA_R
     */
    public static final BoxKey DPM400C00_DATA_R = new BoxKeyPerClassImpl(Dpm400C00Form.class, ManageInsWsPlanDto.class);

	/**
	 * DPM400C00_DATA_R_HEADER
	 */
	public static final BoxKey DPM400C00_INPUT_DATA_R = new BoxKeyPerClassImpl(Dpm400C00Form.class, ManageInsWsPlanHeaderDto.class);

	/**
	 * （ワ）DPM400C01_DATA_R(明細行)
	 */
	public static final BoxKey DPM400C01_DATA_R = new BoxKeyPerClassImpl(Dpm400C00Form.class, ManageInsWsPlanForVacDto.class);

	/**
	 * （ワ）DPM400C01_DATA_R_TOTAL(集計行)
	 */
	public static final BoxKey DPM400C01_DATA_R_TOTAL = new BoxKeyPerClassImpl(Dpm400C00Form.class, ManageInsWsPlanForVacResultDetailTotalDto.class);



	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM400C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * （ワ）施設追加を実行することができるか
	 */
	public static final BoxKey DPM400C00_ENABLE_INS_ENTRY = new BoxKeyPerClassImpl(Dpm400C00Form.class, Boolean.class);


	/**
	 * 計画立案レベル・特約店
	 */
	public static final ProdPlanLevel DPM400C00_PLANLEVEL_INS_WS = ProdPlanLevel.INS_WS;

	// -------------------------------
	// 部品
	// -------------------------------
	/**
	 * （ワ）重点先/一般先
	 */
	public static final List<CodeAndValue> ACTIVITY_TYPES;

	/**
	 * static initilizer
	 */
	static {
		List<CodeAndValue> tmp = null;

		// （ワ）重点先/一般先にセット
		tmp = new ArrayList<CodeAndValue>(3);
		tmp.add(new CodeAndValue("1", "重点先"));
		tmp.add(new CodeAndValue("2", "一般先"));
		ACTIVITY_TYPES = Collections.unmodifiableList(tmp);

	}

	/**
	 * （ワ）[プルダウンリスト]重点先/一般先を取得する。
	 *
	 * @return [プルダウンリスト]重点先/一般先
	 */
	public List<CodeAndValue> getActivityTypes() {
		return ACTIVITY_TYPES;
	}
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
	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(支店)
	 * （ワ）組織コード(特約店部)
	 */
	private String sosCd2;

	/**
	 * 組織コード(営業所)
	 * （ワ）組織コード(エリア特約店)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)　（ワ同）
	 */
	private String sosCd4;

	/**
	 * 従業員番号（ワ同）
	 */
	private String jgiNo;

	/**
	 * 雑組織フラグ（ワ同）
	 */
	private boolean etcSosFlg;

	/**
	 * 対象区分
	 */
	private String insType;

	/**
	 * （ワ）重点先/一般先
	 */
	private String activityType;

	/**
	 * 施設コード（ワ同）
	 */
	private String insNo;

	/**
	 * 施設名（ワ同）
	 */
	private String insName;

	/**
	 * 特約店コード（ワ同）
	 */
	private String tmsTytenCd;

	/**
	 * 特約店名（ワ同）
	 */
	private String tmsTytenName;

	/**
	 * 特約店コード(入力欄用)
	 */
	private String tmsTytenCdPart;

	/**
	 * カテゴリ
	 */
	private String sosCategory;
	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 計画データ
	 */
	private String planData;

	/**
	 * （ワ）JIS府県コード
	 */
	private String addrCodePref;

	/**
	 * （ワ）JIS市区町村コード
	 */
	private String addrCodeCity;

	/**
	 * （ワ）市区郡町村名（漢字）
	 */
	private String shikuchosonMeiKj;

	/**
	 * （ワ）府県名（漢字）
	 */
	private String fukenMeiKj;

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

	// 処理用フィールド
	/**
	 * 処理用組織コード(支店)
	 * （ワ）処理用組織コード(特約店部)
	 */
	private String sosCd2Tran;

	/**
	 * 処理用組織コード(営業所)
	 * （ワ）処理用組織コード(エリア特約店Ｇ)
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
	 * （ワ）処理用重点先/一般先
	 */
	private String activityTypeTran;

	/**
	 * （ワ）処理用JIS府県コード
	 */
	private String addrCodePrefTran;

	/**
	 * （ワ）処理用JIS市区町村コード
	 */
	private String addrCodeCityTran;

	/**
	 * 処理用雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 処理用対象区分
	 */
	private String insTypeTran;

	/**
	 * 処理用施設コード
	 * （ワ）処理用組織コード(施設コード)
	 */
	private String insNoTran;

	/**
	 * 処理用特約店コード
	 * （ワ）処理用組織コード(特約店コード)
	 */
	private String tmsTytenCdTran;

	/**
	 * 処理用特約店コード(入力欄用)（ワ同）
	 */
	private String tmsTytenCdPartTran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 処理用品目固定コード
	 * （ワ）処理用品目コード
	 */
	private String prodCodeTran;

	/**
	 *処理用 計画データ
	 */
	private String planDataTran;

	/**
	 * 全MRフラグ
	 */
	private boolean mrFlg;

	/**
	 * 計画（月別計画）
	 */
	private String planId;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0]=品目固定コード</li>
	 * <li>Sprit文字列[1]=施設コード</li>
	 * <li>Sprit文字列[2]=特約店コード</li>
	 * <li>Sprit文字列[3]=対象施設</li>
	 * <li>Sprit文字列[4]=シーケンスキー</li>
	 * <li>Sprit文字列[5]=最終更新日時</li>
	 * <li>Sprit文字列[6]=T価ベース 更新前</li>
	 * <li>Sprit文字列[7]=T価ベース 更新後（入力値）</li>
	 * </ul>
	 */
	private String[] rowIdList;

	/**
	 * 流通政策部かどうか
	 */
	private boolean ryutsu;

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
	 * 雑組織フラグを取得する。
	 *
	 * @return etcSosFlg 雑組織フラグ
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
	 * （ワ）重点先/一般先を取得する。
	 *
	 * @return activityType 重点先/一般先
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * （ワ）重点先/一般先を設定する。
	 *
	 * @param activityType 重点先/一般先
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
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
	 * @return insName 施設名
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
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店コードを設定する。
	 *
	 * @param tmsTytenCd 特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 特約店名を取得する。
	 *
	 * @return tmsTytenName 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 特約店名を設定する。
	 *
	 * @param tmsTytenName　特約店名
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
	}

	/**
	 * 特約店コード(入力欄用)を取得する。
	 *
	 * @return tmsTytenCdPart 特約店コード(入力欄用)
	 */
	public String getTmsTytenCdPart() {
		return tmsTytenCdPart;
	}

	/**
	 * 特約店コード(入力欄用)を設定する。
	 *
	 * @param tmsTytenCdPart 特約店コード(入力欄用)
	 */
	public void setTmsTytenCdPart(String tmsTytenCdPart) {
		this.tmsTytenCdPart = tmsTytenCdPart;
	}

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


	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 品目カテゴリを設定する。
	 *
	 * @param prodCategory カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 計画を取得する。
	 *
	 * @return planData 計画
	 */
	public String getPlanData() {
		return planData;
	}

	/**
	 * 計画を設定する。
	 *
	 * @param planData 計画
	 */
	public void setPlanData(String planData) {
		this.planData = planData;
	}

	/**
	 * 送信データを取得する。
	 *
	 * @return rowIdList 送信データ
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 送信データを設定する。
	 *
	 * @param rowIdList 送信データ
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
	}

	/**
	 * （ワ）府県コードを取得する。
	 *
	 * @return 府県コード
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * （ワ）府県コードを設定する。
	 *
	 * @param addrCodePref 府県コード
	 */
	public void setAddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * （ワ）市区町村コードを取得する。
	 *
	 * @return 市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * （ワ）市区町村コードを設定する。
	 *
	 * @param addrCodeCity 市区町村コード
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * （ワ）市区郡町村名（漢字）を取得する。
	 *
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * （ワ）市区郡町村名（漢字）を設定する。
	 *
	 * @param shikuchosonMeiKj 市区郡町村名（漢字
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	/**
	 * （ワ）府県名（漢字）を取得する。
	 *
	 * @return 府県名（漢字）
	 */
	public String getFukenMeiKj() {
		return fukenMeiKj;
	}

	/**
	 * （ワ）府県名（漢字）を設定する。
	 *
	 * @param fukenMeiKj 府県名（漢字
	 */
	public void setFukenMeiKj(String fukenMeiKj) {
		this.fukenMeiKj = fukenMeiKj;
	}

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
	 * @return 処理用従業員番号
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
	 * 処理用特約店コードを取得する。
	 *
	 * @return 処理用特約店コード
	 */
	public String getTmsTytenCdTran() {
		return tmsTytenCdTran;
	}

	/**
	 * 処理用特約店コードを設定する。
	 *
	 * @param tmsTytenCdTran 処理用特約店コード
	 */
	public void setTmsTytenCdTran(String tmsTytenCdTran) {
		this.tmsTytenCdTran = tmsTytenCdTran;
	}

	/**
	 * 処理用特約店コード(入力欄用)を取得する。
	 *
	 * @return 処理用特約店コード(入力欄用)
	 */
	public String getTmsTytenCdPartTran() {
		return tmsTytenCdPartTran;
	}

	/**
	 * 処理用特約店コード(入力欄用)を設定する。
	 *
	 * @param tmsTytenCdPartTran 処理用特約店コード(入力欄用)
	 */
	public void setTmsTytenCdPartTran(String tmsTytenCdPartTran) {
		this.tmsTytenCdPartTran = tmsTytenCdPartTran;
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
	 * 処理用品目固定コードを取得する。
	 *
	 * @return 処理用品目固定コード
	 */
	public String getProdCodeTran() {
		return prodCodeTran;
	}

	/**
	 * 処理用品目固定コードを設定する。
	 *
	 * @param prodCodeTran 処理用品目固定コード
	 */
	public void setProdCodeTran(String prodCodeTran) {
		this.prodCodeTran = prodCodeTran;
	}

	/**
	 * 処理用計画データを取得する。
	 *
	 * @return 処理用計画データ
	 */
	public String getPlanDataTran() {
		return planDataTran;
	}

	/**
	 * 処理用計画データを設定する。
	 *
	 * @param planDataTran 処理用計画データ
	 */
	public void setPlanDataTran(String planDataTran) {
		this.planDataTran = planDataTran;
	}

	/**
	 * （ワ）処理用重点先/一般先を取得する。
	 *
	 * @return 処理用重点先/一般先
	 */
	public String getActivityTypeTran() {
		return activityTypeTran;
	}

	/**
	 * （ワ）処理用重点先/一般先を設定する。
	 *
	 * @param activityTypeTran 処理用重点先/一般先
	 */
	public void setActivityTypeTran(String activityTypeTran) {
		this.activityTypeTran = activityTypeTran;
	}

	/**
	 * （ワ）処理用JIS府県コードを取得する。
	 *
	 * @return 処理用JIS府県コード
	 */
	public String getAddrCodePrefTran() {
		return addrCodePrefTran;
	}

	/**
	 * （ワ）処理用JIS府県コードを設定する。
	 *
	 * @param addrCodePrefTran 処理用JIS府県コード
	 */
	public void setAddrCodePrefTran(String addrCodePrefTran) {
		this.addrCodePrefTran = addrCodePrefTran;
	}

	/**
	 * （ワ）処理用JIS市区町村コードを取得する。
	 *
	 * @return 処理用JIS市区町村コード
	 */
	public String getAddrCodeCityTran() {
		return addrCodeCityTran;
	}

	/**
	 * （ワ）処理用JIS市区町村コードを設定する。
	 *
	 * @param addrCodeCityTran 処理用JIS市区町村コード
	 */
	public void setAddrCodeCityTran(String addrCodeCityTran) {
		this.addrCodeCityTran = addrCodeCityTran;
	}

	 /**
	  * 流通政策部かどうかを取得する。
	  *
	 * @return ryutsu 流通政策部かどうか
	 */
	public boolean isRyutsu() {
		return ryutsu;
	}

	/**
	 * 流通政策部かどうかを設定する。
	 *
	 * @param ryutsu 流通政策部かどうか
	 */
	public void setRyutsu(boolean ryutsu) {
		this.ryutsu = ryutsu;
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
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.etcSosFlgTran = this.etcSosFlg;
		this.insTypeTran = this.insType;
		this.insNoTran = this.insNo;
		this.tmsTytenCdTran = this.tmsTytenCd;
		this.tmsTytenCdPartTran = this.tmsTytenCdPart;
		this.prodCategoryTran = this.prodCategory;
		this.prodCodeTran = this.prodCode;
		this.planDataTran = this.planData;
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
		this.prodCategoryTran = this.prodCategory;
		this.prodCodeTran = this.prodCode;
		this.insNoTran = this.insNo;
		this.tmsTytenCdTran = this.tmsTytenCd;
		this.tmsTytenCdPartTran = this.tmsTytenCdPart;
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
		this.etcSosFlg = this.etcSosFlgTran;
		this.insType = this.insTypeTran;
		this.insNo = this.insNoTran;
		this.tmsTytenCd = this.tmsTytenCdTran;
		this.tmsTytenCdPart = this.tmsTytenCdPartTran;
		this.prodCategory = this.prodCategoryTran;
		this.prodCode = this.prodCodeTran;
		this.planData = this.planDataTran;
	}

	/**
	 *  （ワ）処理用フィールドを画面表示用フィールドに設定
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
		this.insNo = this.insNoTran;
		this.tmsTytenCd = this.tmsTytenCdTran;
		this.tmsTytenCdPart = this.tmsTytenCdPartTran;
		this.etcSosFlg = this.etcSosFlgTran;
	}


	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public ManageInsWsPlanScDto convertScDto() throws Exception {
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
		Integer jgiNo = null;
		if (StringUtils.isNotEmpty(this.jgiNoTran)) {
			jgiNo = ConvertUtil.parseInteger(this.jgiNoTran);
		} else {
			this.jgiNoTran = null;
		}
		// 施設コード
		if (StringUtils.isEmpty(this.insNoTran)) {
			this.insNoTran = null;
		}
		// 対象区分
		InsType insType = null;
		if (StringUtils.isNotEmpty(this.insTypeTran)) {
			insType = InsType.getInstance(this.insTypeTran);
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
		// 特約店コード(入力)
		if (StringUtils.isEmpty(tmsTytenCdPartTran)) {
			this.tmsTytenCdPartTran = null;
		}
		// 計画検索範囲
		PlanData planData = null;
		if (StringUtils.isNotEmpty(this.planDataTran)) {
			planData = PlanData.getInstance(this.planDataTran);
		} else {
			this.planDataTran = null;
		}
		return new ManageInsWsPlanScDto(sosCd2Tran, sosCd3Tran, sosCd4Tran, jgiNo, insNoTran, insType, prodCategoryTran, prodCodeTran, tmsTytenCdPartTran, planData);
	}


	/**
	 * （ワ）ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public ManageInsWsPlanForVacScDto convertInsWsPlanScDto() throws Exception {

		// 組織コード(特約店部)
		if (StringUtils.isEmpty(sosCd2Tran)) {
			this.sosCd2Tran = null;
		}

		// 組織コード(エリア特約店G)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			this.sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
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
		// 活動区分（重点先／一般先）
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
		// 特約店コード
		if (StringUtils.isEmpty(tmsTytenCdPartTran)) {
			this.tmsTytenCdPartTran = null;
		}
		return new ManageInsWsPlanForVacScDto(sosCd2Tran, sosCd3Tran, _jgiNoTran, insNoTran, _activityTypeTran, _addrCodePrefTran, addrCodeCityTran, prodCategoryTran, prodCodeTran, tmsTytenCdPartTran);
	}



	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return ManageInsWsPlanEntryDto 変換されたUpdateDto
	 */
	public List<ManageInsWsPlanEntryDto> convertEntryDto() throws Exception {
		List<ManageInsWsPlanEntryDto> updateDtoList = new ArrayList<ManageInsWsPlanEntryDto>();
		if (updateDtoList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				final boolean isSame = StringUtils.equals(rowId[5], rowId[6]);

				// 品目固定コード
				final String prodCode = rowId[0];

				// 施設コード
				final String insNo = rowId[1];

				// 特約店コード
				final String tmsTytenCd = rowId[2];

				// 施設区分UHのDTO作成
				if (!isSame) {
					// シーケンスキー
					final Long seqKey = ConvertUtil.parseLong(rowId[3]);

					// 最終更新日時
					final Date upDate = ConvertUtil.parseDate(rowId[4]);

					// 更新前計画値
					final Long yBaseValueBefore = ConvertUtil.parseLong(rowId[5]);

					// 更新後計画値
					final Long yBaseValueAfter = ConvertUtil.parseLong(rowId[6]);

					final ManageInsWsPlanEntryDto updateDto = new ManageInsWsPlanEntryDto(prodCode, insNo, tmsTytenCd, seqKey, upDate, yBaseValueBefore, yBaseValueAfter);
					updateDtoList.add(updateDto);
				}
			}
		}
		return updateDtoList;
	}

	/**
	 * （ワ）ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return InsPlanUpdateDto 変換されたUpdateDto
	 */
	public List<ManageInsWsPlanForVacEntryDto> convertInsWsPlanUpdateDto() throws Exception {
		List<ManageInsWsPlanForVacEntryDto> updateDtoList = new ArrayList<ManageInsWsPlanForVacEntryDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 計画値に変更がある場合のみ更新
				if (StringUtils.equals(rowId[5], rowId[6])) {
					continue;
				}
				// 施設コード
				final String insNo = rowId[0];
				// 特約店コード
				final String tmsTytenCd = rowId[1];
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
				final ManageInsWsPlanForVacEntryDto updateDto = new ManageInsWsPlanForVacEntryDto(prodCode, insNo, tmsTytenCd, seqKey, upDate, yBaseValueBefore, yBaseValueAfter);
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
	public void convertHeaderDto(ManageInsWsPlanHeaderDto headerDto) {

		// 施設コードが指定されている場合
		if (StringUtils.isNotEmpty(this.insNo)) {

			// 施設情報が取得出来ない場合
			if (headerDto.getInsMstResultDto() == null) {

				// 施設関係情報をクリア
				insName = null;
				insType = InsType.UH.getDbValue();
				etcSosFlg = false;

				// ユーザ関係情報をクリア
				DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
				if (userInfo != null) {
					DpUser user = userInfo.getSettingUser();
					if (user != null) {
						setSosCd2(null);
						setSosCd3(null);
						setSosCd4(null);
						setJgiNo(null);
						// 組織レベル毎にデフォルト情報をログインユーザ情報から取得する
						switch (user.getSosLvl(JknGrpId.TERM_WS.getDbValue())) {
						case ALL:
							break;
						case BRANCH:
							setSosCd2(user.getSosCd2());
							break;
						case OFFICE:
							setSosCd2(user.getSosCd2());
							setSosCd3(user.getSosCd3());
							break;
						case MR:
							setSosCd2(user.getSosCd2());
							setSosCd3(user.getSosCd3());
							setJgiNo(String.format("%7d",user.getJgiNo()));
							break;
						default:
							// ここに来る場合はバグだが、安全策としてとりあえず設定できるものを入れる。
							setSosCd2(user.getSosCd2());
							setSosCd3(user.getSosCd3());
							setJgiNo(String.format("%7d",user.getJgiNo()));
							break;
						}
					}
				}
			}

			// 施設情報が取得出来た場合
			else {
				insNo = headerDto.getInsMstResultDto().getInsNo();
				insName = headerDto.getInsMstResultDto().getInsName();
				jgiNo = null;
				sosCd2 = null;
				sosCd3 = null;
				sosCd4 = null;
				etcSosFlg = false;
				if (headerDto.getTargetMR() != null) {
					JgiMst jgi = headerDto.getTargetMR();
					jgiNo = jgi.getJgiNo().toString();
					sosCd2 = jgi.getSosCd2();
					sosCd3 = jgi.getSosCd3();
					sosCd4 = jgi.getSosCd4();
					if (BooleanUtils.isTrue(jgi.getEtcSosFlg())) {
						etcSosFlg = true;
					}
				}

				insType = null;
				if (headerDto.getInsType() != null) {
					insType = headerDto.getInsType().getDbValue();
				}
			}
		}
		this.tmsTytenName = headerDto.getTmsTytenName();
	}

	/**
	 * （ワ）headerDto ⇒ ActionForm 変換処理
	 *
	 * @param headerDto ヘッダ情報DTO
	 */
	public void convertHeaderDto(ManageInsWsPlanForVacHeaderDto headerDto) {

		if (StringUtils.isNotEmpty(this.insNo)) {

			// insNoはそのまま
			insName = null;
			jgiNo = null;
			activityType = ActivityType.JTN.getDbValue();
			addrCodePref = null;
			addrCodeCity = null;
			shikuchosonMeiKj = null;
			fukenMeiKj = null;
			etcSosFlg = false;

			InsMstResultDto insMstResult = headerDto.getInsMstResultDto();
			if (insMstResult != null) {

				insNo = insMstResult.getInsNo();
				insName = insMstResult.getInsName();
				jgiNo = null;
				if (insMstResult.getJgiNo() != null) {
					jgiNo = insMstResult.getJgiNo().toString();
				}
				activityType = ActivityType.JTN.getDbValue();
				if (insMstResult.getActivityType() != null) {
					activityType = insMstResult.getActivityType().getDbValue();
				}
				addrCodePref = null;
				if (insMstResult.getAddrCodePref() != null) {
					addrCodePref = insMstResult.getAddrCodePref();
				}
				addrCodeCity = insMstResult.getAddrCodeCity();
				shikuchosonMeiKj = insMstResult.getShikuchosonMeiKj();
				fukenMeiKj = insMstResult.getFukenMeiKj();

			}
		}
		this.tmsTytenName = headerDto.getTmsTytenName();
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
		setTmsTytenCd(null);
		setTmsTytenCdPart(null);
		setTmsTytenName(null);
		setProdCategory(null);
		setProdCode(null);
		setPlanData("0");
		setAddrCodePref(null);
		setAddrCodeCity(null);
		setShikuchosonMeiKj(null);
		setFukenMeiKj(null);
		setActivityType("1");
		setAddrCodePref(null);
		setAddrCodeCity(null);
		setShikuchosonMeiKj(null);
		setFukenMeiKj(null);

	}
}
