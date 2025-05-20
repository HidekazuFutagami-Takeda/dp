package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.SearchInsType;
import jp.co.takeda.dao.div.SpecialInsPlanRegType;
import jp.co.takeda.dto.JisCodeMstResultDto;
import jp.co.takeda.dto.JisCodeMstScDto;
import jp.co.takeda.dto.SpecialInsPlanDeleteDto;
import jp.co.takeda.dto.SpecialInsPlanForVacResultDto;
import jp.co.takeda.dto.SpecialInsPlanForVacScDto;
import jp.co.takeda.dto.SpecialInsPlanResultDto;
import jp.co.takeda.dto.SpecialInsPlanScDto;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps202C02((医)特定施設個別計画施設一覧画面(担当者立案))のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps202C02Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS202C02_DATA_R
	 */
	public static final BoxKey DPS202C02_DATA_R = new BoxKeyPerClassImpl(Dps202C02Form.class,SpecialInsPlanResultDto.class);

	/**
	 * ワクチン：検索結果 リクエストデータ
	 * DPS202C03_DATA_R
	 */
	public static final BoxKey DPS202C03_DATA_R = new BoxKeyPerClassImpl(Dps202C02Form.class, SpecialInsPlanForVacResultDto.class);

	/**
	 * 市区郡町村プルダウン リクエストデータ
	 */
	public static final BoxKey DPS202C02_DATA_ADDR_CODE_CITY_LIST = new BoxKeyPerClassImpl(Dps202C02Form.class, JisCodeMstResultDto.class);

	/**
	 * 削除権限
	 */
	//	public static final DpAuthority DPS202C02_DEL_AUTH = new DpAuthority( AuthType.EDIT);
	public static final DpAuthority DPS202C02_DEL_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return InsMstScDto 変換されたScDto
	 */
	public SpecialInsPlanScDto convertSpecialInsPlanScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}

		// 従業員番号
		Integer iJgiNo = null;
		if (StringUtils.isNotEmpty(jgiNoTran)) {
			iJgiNo = Integer.valueOf(jgiNoTran);
		}

		// 検索用対象区分
		SearchInsType searchInsType = null;
		if (StringUtils.equals(insTypeTran, "1")) {
			searchInsType = SearchInsType.U;
		} else if (StringUtils.equals(insTypeTran, "2")) {
			searchInsType = SearchInsType.H;
		} else if (StringUtils.equals(insTypeTran, "3")) {
			searchInsType = SearchInsType.UH;
		} else if (StringUtils.equals(insTypeTran, "4")) {
			searchInsType = SearchInsType.P;
		} else if (StringUtils.equals(insTypeTran, "5")) {
			searchInsType = SearchInsType.UHP;
		}

		// 特定施設個別計画の絞込条件
		SpecialInsPlanRegType specialInsPlanRegType = null;
		if (StringUtils.equals(regTypeTran, "1")) {
			specialInsPlanRegType = SpecialInsPlanRegType.REG_ON;
		} else if (StringUtils.equals(regTypeTran, "2")) {
			specialInsPlanRegType = SpecialInsPlanRegType.REG_OFF;
		} else if (StringUtils.equals(regTypeTran, "3")) {
			specialInsPlanRegType = SpecialInsPlanRegType.ALL;
		}

		return new SpecialInsPlanScDto(sosCd3Tran, sosCd4Tran, iJgiNo, searchInsType, specialInsPlanRegType,insNameZenKanaTran, insNameHanKanaTran);
	}

	/**
	 * ActionForm ⇒ SpecialInsPlanForVacScDto 変換処理
	 *
	 * @return SpecialInsPlanForVacScDto 変換されたScDto
	 */
	public SpecialInsPlanForVacScDto convertSpecialInsPlanForVacScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}

		// 従業員番号
		Integer iJgiNo = null;
		if (StringUtils.isNotEmpty(jgiNoTran)) {
			iJgiNo = Integer.valueOf(jgiNoTran);
		}

		// 重点先/一般先
		ActivityType searchActivityType = null;
		if (StringUtils.equals(activityTypeTran, "1")) {
			searchActivityType = ActivityType.JTN;
		} else if (StringUtils.equals(activityTypeTran, "2")) {
			searchActivityType = ActivityType.IPPAN;
		} else if (StringUtils.equals(activityTypeTran, "3")) {
			searchActivityType = null;
		}

		// 絞込条件
		SpecialInsPlanRegType searchRegType = null;
		if (StringUtils.equals(regTypeTran, "1")) {
			searchRegType = SpecialInsPlanRegType.REG_ON;
		} else if (StringUtils.equals(regTypeTran, "2")) {
			searchRegType = SpecialInsPlanRegType.REG_OFF;
		} else if (StringUtils.equals(regTypeTran, "3")) {
			searchRegType = SpecialInsPlanRegType.ALL;
		}

		// 都道府県
		Prefecture searchAddrCodePref = null;
		if (StringUtils.isNotBlank(addrCodePrefTran)) {
			searchAddrCodePref = Prefecture.getInstance(addrCodePrefTran);
		}

		// 市区郡町村
		if (StringUtils.isEmpty(addrCodeCityTran)) {
			addrCodeCityTran = null;
		}

		return new SpecialInsPlanForVacScDto(sosCd3Tran, sosCd4Tran, iJgiNo, searchActivityType, searchAddrCodePref, addrCodeCityTran, searchRegType, insNameZenKanaTran,
			insNameHanKanaTran);
	}

	/**
	 * ActionForm ⇒ JisCodeMstScDto 変換処理
	 *
	 * @return JisCodeMstScDto 変換されたScDto
	 */
	public JisCodeMstScDto convertJisCodeMstScDto() throws Exception {

		// 都道府県
		Prefecture searchAddrCodePref = null;
		if (StringUtils.isNotBlank(addrCodePref)) {
			searchAddrCodePref = Prefecture.getInstance(addrCodePref);
		}

		return new JisCodeMstScDto(searchAddrCodePref, null);
	}

	/**
	 * ActionForm⇒DeleteDto 変換処理
	 *
	 * @return 変換されたDeleteDtoのリスト
	 */
	public List<SpecialInsPlanDeleteDto> convertSpecialInsPlanDeleteDto() throws Exception {
		List<SpecialInsPlanDeleteDto> deleteDtoList = new ArrayList<SpecialInsPlanDeleteDto>();
		if (selectRowIdList != null) {
			for (int i = 0; i < selectRowIdList.length; i++) {
				String[] selectRowId = selectRowIdList[i].split(",");
				// 施設コード
				final String insNo = selectRowId[0];
				// 従業員番号
				final String jgiNo = selectRowId[1];
				Integer iJgiNo = null;
				if (StringUtils.isNotEmpty(jgiNo)) {
					iJgiNo = Integer.valueOf(jgiNo);
				}
				// 最終更新日時
				String upDateTime = selectRowId[2];
				Date upDate = ConvertUtil.parseDate(upDateTime);
				SpecialInsPlanDeleteDto deleteDto = new SpecialInsPlanDeleteDto(insNo, iJgiNo, upDate, sosCd3Tran);
				deleteDtoList.add(deleteDto);
			}
		}
		return deleteDtoList;
	}

	/**
	 * 検索条件を処理用フィールドに設定
	 *
	 *
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.insTypeTran = this.insType;
		this.regTypeTran = this.regType;
		this.insNameZenKanaTran = this.insNameZenKana;
		this.insNameHanKanaTran = this.insNameHanKana;
	}

	/**
	 * (ワ)検索条件を処理用フィールドに設定
	 */
	public void setTranFieldVac() {
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.activityTypeTran = this.activityType;
		this.regTypeTran = this.regType;
		this.addrCodePrefTran = this.addrCodePref;
		this.addrCodeCityTran = this.addrCodeCity;
		this.insNameZenKanaTran = this.insNameZenKana;
		this.insNameHanKanaTran = this.insNameHanKana;
	}

	// -------------------------------
	// 部品
	// -------------------------------
	/**
	 * 対象区分
	 */
	public static final List<CodeAndValue> INS_TYPES;

	/**
	 * 絞込条件
	 */
	public static final List<CodeAndValue> REG_TYPES;

	/**
	 * 重点先/一般先
	 */
	public static final List<CodeAndValue> ACTIVITY_TYPES;

	/**
	 * 都道府県
	 */
	public static final List<CodeAndValue> PREFECTURES;

	/**
	 * static initilizer
	 */
	static {
		List<CodeAndValue> tmp = null;
		// 対象区分にセット
		tmp = new ArrayList<CodeAndValue>(5);
		tmp.add(new CodeAndValue("1", "U"));
		tmp.add(new CodeAndValue("2", "H"));
		tmp.add(new CodeAndValue("3", "UH"));
		tmp.add(new CodeAndValue("4", "P"));
		tmp.add(new CodeAndValue("5", "UHP"));
		INS_TYPES = Collections.unmodifiableList(tmp);

		// 絞込み条件にセット
		tmp = new ArrayList<CodeAndValue>(3);
		tmp.add(new CodeAndValue("1", "登録済"));
		tmp.add(new CodeAndValue("2", "未登録"));
		tmp.add(new CodeAndValue("3", "全て"));
		REG_TYPES = Collections.unmodifiableList(tmp);

		tmp = new ArrayList<CodeAndValue>(2);
		tmp.add(new CodeAndValue(ActivityType.JTN.getDbValue(), "重点先"));
		tmp.add(new CodeAndValue(ActivityType.IPPAN.getDbValue(), "一般先"));
		tmp.add(new CodeAndValue(ActivityType.ALL.getDbValue(), "全て"));
		ACTIVITY_TYPES = Collections.unmodifiableList(tmp);

		// 都道府県にセット
		tmp = new ArrayList<CodeAndValue>();
		tmp.add(new CodeAndValue("", "")); // 空欄あり
		for (Prefecture prefecture : Prefecture.values()) {
			tmp.add(new CodeAndValue(prefecture.getDbValue(), prefecture.getName()));
		}
		PREFECTURES = Collections.unmodifiableList(tmp);
	}

	/**
	 * [プルダウンリスト]対象区分を取得する。
	 *
	 * @return [プルダウンリスト]対象区分
	 */
	public List<CodeAndValue> getInsTypes() {
		return INS_TYPES;
	}

	/**
	 * [プルダウンリスト]絞込条件を取得する。
	 *
	 * @return [プルダウンリスト]絞込条件
	 */
	public List<CodeAndValue> getRegTypes() {
		return REG_TYPES;
	}

	/**
	 * [プルダウンリスト]重点先/一般先を取得する。
	 *
	 * @return [プルダウンリスト]重点先/一般先
	 */
	public List<CodeAndValue> getActivityTypes() {
		return ACTIVITY_TYPES;
	}

	/**
	 * [プルダウンリスト]都道府県を取得する。
	 *
	 * @return [プルダウンリスト]都道府県
	 */
	public List<CodeAndValue> getPrefectures() {
		return PREFECTURES;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
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
	 * 対象区分
	 */
	private String insType;

	/**
	 * 絞込条件
	 */
	private String regType;

	/**
	 * 施設名称(全角)
	 */
	private String insNameZenKana;

	/**
	 * 施設名称(半角カナ)
	 */
	private String insNameHanKana;

	/**
	 * 重点先/一般先
	 */
	private String activityType;

	/**
	 * 都道府県
	 */
	private String addrCodePref;

	/**
	 * 市区郡町村
	 */
	private String addrCodeCity;

	/**
	 * 選択行の識別IDリスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=施設コード
	 * Sprit文字列[1]=従業員番号
	 * Sprit文字列[2]=最終更新日時(Date型のgetTime()の値)
	 * (例)---------------------------------------
	 * selectRowIdList[0] = "12345,2002656,1234954688000"
	 * selectRowIdList[1] = "22432,2002656.1122843578000"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] selectRowIdList;

	/**
	 * 検索結果の存在判定フラグ
	 *
	 * <pre>
	 * 検索アクション後にセットします。
	 * TRUE=検索結果あり<br>
	 * FALSE=検索結果なし(ヒット件数0)
	 * </pre>
	 */
	private boolean existSearchDataFlag;

	/**
	 * 医薬/ワクチン選択
	 */
	private String vaccine;

	/**
	 * ワクチンユーザ
	 */
	private boolean vaccineUser;

	// 処理用フィールド
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
	 * 処理用対象区分
	 */
	private String insTypeTran;

	/**
	 * 処理用絞込条件
	 */
	private String regTypeTran;

	/**
	 * 処理用施設名称(全角)
	 */
	private String insNameZenKanaTran;

	/**
	 * 処理用施設名称(半角カナ)
	 */
	private String insNameHanKanaTran;

	/**
	 * 処理用重点先/一般先
	 */
	private String activityTypeTran;

	/**
	 * 処理用都道府県
	 */
	private String addrCodePrefTran;

	/**
	 * 処理用市区郡町村
	 */
	private String addrCodeCityTran;

	/**
	 *
	 * 組織に紐づくカテゴリリスト
	 *
	 * <pre>
	 * 識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=組織コード
	 * Sprit文字列[1]=カテゴリコード
	 * (例)---------------------------------------
	 * sosCdCategoryList[0] = "00961,01"
	 * sosCdCategoryList[1] = "00961,02"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] sosCdCategoryList;

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
	 * 絞込条件を取得する。
	 *
	 * @return regType 絞込条件
	 */
	public String getRegType() {
		return regType;
	}

	/**
	 * 絞込条件を設定する。
	 *
	 * @param regType 絞込条件
	 */
	public void setRegType(String regType) {
		this.regType = regType;
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
	 * 選択行の識別IDリストを取得する。
	 *
	 * @return selectRowIdList 選択行の識別IDリスト
	 */
	public String[] getSelectRowIdList() {
		return selectRowIdList;
	}

	/**
	 * 選択行の識別IDリストを設定する。
	 *
	 * @param selectRowIdList 選択行の識別IDリスト
	 */
	public void setSelectRowIdList(String[] selectRowIdList) {
		this.selectRowIdList = selectRowIdList;
	}

	/**
	 * 検索結果の存在判定フラグを取得する。
	 *
	 * @return existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public boolean getExistSearchDataFlag() {
		return existSearchDataFlag;
	}

	/**
	 * 検索結果の存在判定フラグを設定する。
	 *
	 * @param existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public void setExistSearchDataFlag(boolean existSearchDataFlag) {
		this.existSearchDataFlag = existSearchDataFlag;
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
	 * 処理用絞込条件を取得する。
	 *
	 * @return 処理用絞込条件
	 */
	public String getRegTypeTran() {
		return regTypeTran;
	}

	/**
	 * 処理用絞込条件を設定する。
	 *
	 * @param regTypeTran 処理用絞込条件
	 */
	public void setRegTypeTran(String regTypeTran) {
		this.regTypeTran = regTypeTran;
	}

	/**
	 * 処理用施設名称(全角)を取得する。
	 *
	 * @return 処理用施設名称(全角)
	 */
	public String getInsNameZenKanaTran() {
		return insNameZenKanaTran;
	}

	/**
	 * 処理用施設名称(全角)を設定する。
	 *
	 * @param insNameZenKanaTran 処理用施設名称(全角)
	 */
	public void setInsNameZenKanaTran(String insNameZenKanaTran) {
		this.insNameZenKanaTran = insNameZenKanaTran;
	}

	/**
	 * 処理用施設名称(半角カナ)を取得する。
	 *
	 * @return 処理用施設名称(半角カナ)
	 */
	public String getInsNameHanKanaTran() {
		return insNameHanKanaTran;
	}

	/**
	 * 処理用施設名称(半角カナ)を設定する。
	 *
	 * @param insNameHanKanaTran 処理用施設名称(半角カナ)
	 */
	public void setInsNameHanKanaTran(String insNameHanKanaTran) {
		this.insNameHanKanaTran = insNameHanKanaTran;
	}

	/**
	 * 医薬/ワクチン選択を取得する
	 * @return 医薬/ワクチン選択
	 */
	public String getVaccine() {
		return vaccine;
	}

	/**
	 * 医薬/ワクチン選択を設定する
	 * @param vaccine 医薬/ワクチン選択
	 */
	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}

	/**
	 * ワクチンユーザであるかを取得する
	 * @return ワクチンユーザであるか
	 */
	public boolean isVaccineUser() {
		return vaccineUser;
	}

	/**
	 * ワクチンユーザであるかを設定する
	 * @param vaccineUser ワクチンユーザであるか
	 */
	public void setVaccineUser(boolean vaccineUser) {
		this.vaccineUser = vaccineUser;
	}

	/**
	 * 重点先/一般先を取得する。
	 *
	 * @return activityType 重点先/一般先
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * 重点先/一般先を設定する。
	 *
	 * @param activityType 重点先/一般先
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * 都道府県を取得する。
	 *
	 * @return addrCodePref 都道府県
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 都道府県を設定する。
	 *
	 * @param addrCodePref 都道府県
	 */
	public void setAddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * 市区郡町村を取得する。
	 *
	 * @return addrCodeCity 市区郡町村
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * 市区郡町村を設定する。
	 *
	 * @param addrCodeCity 市区郡町村
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
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
	 * 処理用都道府県を取得する。
	 *
	 * @return 処理用都道府県
	 */
	public String getAddrCodePrefTran() {
		return addrCodePrefTran;
	}

	/**
	 * 処理用都道府県を設定する。
	 *
	 * @param addrCodePrefTran 処理用都道府県
	 */
	public void setAddrCodePrefTran(String addrCodePrefTran) {
		this.addrCodePrefTran = addrCodePrefTran;
	}

	/**
	 * 処理用市区郡町村を取得する。
	 *
	 * @return 処理用市区郡町村
	 */
	public String getAddrCodeCityTran() {
		return addrCodeCityTran;
	}

	/**
	 * 処理用市区郡町村を設定する。
	 *
	 * @param addrCodeCityTran 処理用市区郡町村
	 */
	public void setAddrCodeCityTran(String addrCodeCityTran) {
		this.addrCodeCityTran = addrCodeCityTran;
	}

	/**
	 * 組織に紐づくカテゴリリストを取得する。
	 *
	 * @return sosCdCategoryList 組織に紐づくカテゴリリスト
	 */
	public String[] getSosCdCategoryList() {
		return sosCdCategoryList;
	}

	/**
	 * 組織に紐づくカテゴリリストを設定する。
	 *
	 * @param sosCdCategoryList 組織に紐づくカテゴリリスト
	 */
	public void setSosCdCategoryList(String[] sosCdCategoryList) {
		this.sosCdCategoryList = sosCdCategoryList;
	}


	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.sosCd4 = null;
		this.jgiNo = null;
		this.insType = null;
		this.regType = "3";
		this.insNameZenKana = null;
		this.insNameHanKana = null;
		this.activityType = "1";
		this.addrCodePref = null;
		this.addrCodeCity = null;
		this.vaccine = "0";
		this.sosCd3Tran = null;
		this.sosCd4Tran = null;
		this.jgiNoTran = null;
		this.insTypeTran = null;
		this.regTypeTran = null;
		this.insNameZenKanaTran = null;
		this.insNameHanKanaTran = null;
		this.selectRowIdList = null;
		this.existSearchDataFlag = false;
		this.activityTypeTran = null;
		this.addrCodePrefTran = null;
		this.addrCodeCityTran = null;
		this.sosCdCategoryList = null;
	}
}
