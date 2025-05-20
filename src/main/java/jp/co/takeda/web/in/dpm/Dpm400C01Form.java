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
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dpm400C01((ワ)施設特約店計画編集)のフォームクラス
 *
 * @author siwamoto
 */
public class Dpm400C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPM400C01_DATA_R(明細行)
	 */
	public static final BoxKey DPM400C01_DATA_R = new BoxKeyPerClassImpl(Dpm400C01Form.class, ManageInsWsPlanForVacDto.class);

	/**
	 * DPM400C01_DATA_R_TOTAL(集計行)
	 */
	public static final BoxKey DPM400C01_DATA_R_TOTAL = new BoxKeyPerClassImpl(Dpm400C01Form.class, ManageInsWsPlanForVacResultDetailTotalDto.class);

	/**
	 * 施設追加を実行することができるか
	 */
	public static final BoxKey DPM400C01_ENABLE_INS_ENTRY = new BoxKeyPerClassImpl(Dpm400C01Form.class, Boolean.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM400C01_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・施設特約店
	 */
	public static final ProdPlanLevel DPM400C01_PLANLEVEL_INS_WS = ProdPlanLevel.INS_WS;

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
		// 重点先/一般先にセット
		tmp = new ArrayList<CodeAndValue>(3);
		tmp.add(new CodeAndValue("1", "重点先"));
		tmp.add(new CodeAndValue("2", "一般先"));
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
	 * 組織コード(特約店部)
	 */
	private String sosCd2;

	/**
	 * 組織コード(エリア特約店)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 雑組織フラグ
	 */
	private boolean etcSosFlg;

	/**
	 * 重点先/一般先
	 */
	private String activityType;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 施設名
	 */
	private String insName;

	/**
	 * 特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 特約店コード(入力欄用)
	 */
	private String tmsTytenCdPart;

	/**
	 * 特約店名
	 */
	private String tmsTytenName;

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
	 * 品目固定コード
	 */
	private String prodCode;

	// 処理用フィールド

	/**
	 * 処理用組織コード(特約店部)
	 */
	private String sosCd2Tran;

	/**
	 * 処理用組織コード(エリア特約店Ｇ)
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
	 * 処理用品目コード
	 */
	private String prodCodeTran;

	/**
	 * 処理用組織コード(施設コード)
	 */
	private String insNoTran;

	/**
	 * 処理用組織コード(特約店コード)
	 */
	private String tmsTytenCdTran;

	/**
	 * 処理用特約店コード(入力欄用)
	 */
	private String tmsTytenCdPartTran;

	/**
	 * 処理用雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;


	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0]=施設コード</li>
	 * <li>Sprit文字列[1]=特約店コード</li>
	 * <li>Sprit文字列[2]=品目コード</li>
	 * <li>Sprit文字列[3]=シーケンスキー</li>
	 * <li>Sprit文字列[4]=最終更新日時</li>
	 * <li>Sprit文字列[5]=Y価ベース 更新前</li>
	 * <li>Sprit文字列[6]=Y価ベース 更新後</li>
	 * </ul>
	 */
	private String[] rowIdList;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 組織コード(特約店部)を取得する。
	 *
	 * @return 組織コード(特約店部)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(特約店部)を設定する。
	 *
	 * @param sosCd2 組織コード(特約店部)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	/**
	 * 組織コード(エリア特約店Ｇ)を取得する。
	 *
	 * @return 組織コード(エリア特約店Ｇ)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(エリア特約店Ｇ)を設定する。
	 *
	 * @param sosCd3 組織コード(エリア特約店Ｇ)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return 組織コード(チーム)
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
	 * 活動区分を取得する。
	 *
	 * @return 活動区分
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * 活動区分を設定する。
	 *
	 * @param activityType 活動区分
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
	 * 特約店コードを取得する。
	 *
	 * @return 特約店コード
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
	 * 特約店名を取得する。
	 *
	 * @return 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 特約店名を設定する。
	 *
	 * @param tmsTytenName 特約店名
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
	}

	/**
	 * 府県コードを取得する。
	 *
	 * @return 府県コード
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 府県コードを設定する。
	 *
	 * @param addrCodePref 府県コード
	 */
	public void setAddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * 市区町村コードを取得する。
	 *
	 * @return 市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * 市区町村コードを設定する。
	 *
	 * @param addrCodeCity 市区町村コード
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 *
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 市区郡町村名（漢字）を設定する。
	 *
	 * @param shikuchosonMeiKj 市区郡町村名（漢字
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	/**
	 * 府県名（漢字）を取得する。
	 *
	 * @return 府県名（漢字）
	 */
	public String getFukenMeiKj() {
		return fukenMeiKj;
	}

	/**
	 * 府県名（漢字）を設定する。
	 *
	 * @param fukenMeiKj 府県名（漢字
	 */
	public void setFukenMeiKj(String fukenMeiKj) {
		this.fukenMeiKj = fukenMeiKj;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
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
	 * 処理用組織コード(特約店部)を取得する。
	 *
	 * @return 処理用組織コード(特約店部)
	 */
	public String getSosCd2Tran() {
		return sosCd2Tran;
	}

	/**
	 * 処理用組織コード(特約店部)を設定する。
	 *
	 * @param sosCd2Tran 処理用組織コード(特約店部)
	 */
	public void setSosCd2Tran(String sosCd2Tran) {
		this.sosCd2Tran = sosCd2Tran;
	}

	/**
	 * 処理用組織コード(エリア特約店Ｇ)を取得する。
	 *
	 * @return 処理用組織コード(エリア特約店Ｇ)
	 */
	public String getSosCd3Tran() {
		return sosCd3Tran;
	}

	/**
	 * 処理用組織コード(エリア特約店Ｇ)を設定する。
	 *
	 * @param sosCd3Tran 処理用組織コード(エリア特約店Ｇ)
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
	 * 処理用重点先/一般先を取得する。
	 *
	 * @return 処理用重点先/一般先
	 */
	public String getActivityTypeTran() {
		return activityTypeTran;
	}

	/**
	 * 処理用重点先/一般先を設定する。
	 *
	 * @param activityTypeTran 処理用重点先/一般先
	 */
	public void setActivityTypeTran(String activityTypeTran) {
		this.activityTypeTran = activityTypeTran;
	}

	/**
	 * 処理用JIS府県コードを取得する。
	 *
	 * @return 処理用JIS府県コード
	 */
	public String getAddrCodePrefTran() {
		return addrCodePrefTran;
	}

	/**
	 * 処理用JIS府県コードを設定する。
	 *
	 * @param addrCodePrefTran 処理用JIS府県コード
	 */
	public void setAddrCodePrefTran(String addrCodePrefTran) {
		this.addrCodePrefTran = addrCodePrefTran;
	}

	/**
	 * 処理用JIS市区町村コードを取得する。
	 *
	 * @return 処理用JIS市区町村コード
	 */
	public String getAddrCodeCityTran() {
		return addrCodeCityTran;
	}

	/**
	 * 処理用JIS市区町村コードを設定する。
	 *
	 * @param addrCodeCityTran 処理用JIS市区町村コード
	 */
	public void setAddrCodeCityTran(String addrCodeCityTran) {
		this.addrCodeCityTran = addrCodeCityTran;
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

	// ----------------------------------------
	// convert
	// ----------------------------------------
	/**
	 * ActionForm ⇒ ScDto 変換処理
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
		// 品目コード
		if (StringUtils.isEmpty(prodCodeTran)) {
			this.prodCodeTran = null;
		}
		// 品目カテゴリ
		if (StringUtils.isEmpty(prodCategoryTran)) {
			this.prodCategoryTran = null;
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

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.activityTypeTran = this.activityType;
		this.addrCodePrefTran = this.addrCodePref;
		this.addrCodeCityTran = this.addrCodeCity;
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
		this.activityType = this.activityTypeTran;
		this.addrCodePref = this.addrCodePrefTran;
		this.addrCodeCity = this.addrCodeCityTran;
		this.prodCode = this.prodCodeTran;
		this.insNo = this.insNoTran;
		this.tmsTytenCd = this.tmsTytenCdTran;
		this.tmsTytenCdPart = this.tmsTytenCdPartTran;
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
		setTmsTytenCd(null);
		setTmsTytenCdPart(null);
		setTmsTytenName(null);
		setActivityType(null);
		setAddrCodePref(null);
		setAddrCodeCity(null);
		setShikuchosonMeiKj(null);
		setFukenMeiKj(null);
		setProdCode(null);
	}
}
