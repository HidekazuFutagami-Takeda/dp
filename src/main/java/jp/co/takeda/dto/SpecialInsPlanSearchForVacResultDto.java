package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;

/**
 * 特定施設個別計画立案画面（担当者案）の検索結果用DTO
 *
 * @author siwamoto
 */
public class SpecialInsPlanSearchForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設分類
	 */
	private final String insClass;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設名
	 */
	private final String insName;

	/**
	 * サブコード分類（Ref[施設情報].〔サブコード分類〕）
	 */
	//private OldInsrFlg oldInsrFlg;
	/**
	 * 計画値(B)
	 */
	private final Long plannedValueB;

	/**
	 * 計画値(B2)
	 */
	private final Long plannedValueB2;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 製品区分
	 */
	private final String prodType;

	/**
	 * TMS特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private final String tmsTytenName;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 最終更新者名
	 */
	private final String upJgiName;

	/**
	 * 立案品目
	 */
	private final MonNnu monNnu0;

	/**
	 * 削除フラグ
	 */
	private final Boolean delFlg;

	/**
	 * 配分除外フラグ
	 */
	private final Boolean exDistFlg;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private final Boolean planTaiGaiFlgTok;

	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private final Boolean planTaiGaiFlgRik;

	/**
	 * コンストラクタ
	 *
	 * @param insNo
	 * @param insName
	 * @param insClazz
	 * @param plannedValueB
	 * @param plannedValueB2
	 * @param prodCode
	 * @param prodName
	 * @param prodType
	 * @param tmsTytenCd
	 * @param tmsTytenName
	 * @param upDate
	 * @param upJgiName
	 * @param monNnu1
	 * @param delFlg
	 * @param exDistFlg
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 * @param planTaiGaiFlgRik 計画立案対象外フラグ(来期用)
	 */
	public SpecialInsPlanSearchForVacResultDto(String insNo, String insName, String insClass, Long plannedValueB, Long plannedValueB2, String prodCode, String prodName, String prodType,
		String tmsTytenCd, String tmsTytenName, Date upDate, String upJgiName, MonNnu monNnu0, Boolean delFlg, Boolean exDistFlg, Boolean planTaiGaiFlgTok, Boolean planTaiGaiFlgRik) {
		this.insNo = insNo;
		this.insName = insName;
		this.insClass = insClass;
		this.plannedValueB = plannedValueB;
		this.plannedValueB2 = plannedValueB2;
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodType = prodType;
		this.tmsTytenCd = tmsTytenCd;
		this.tmsTytenName = tmsTytenName;
		this.upDate = upDate;
		this.upJgiName = upJgiName;
		this.monNnu0 = monNnu0;
		this.delFlg = delFlg;
		this.exDistFlg = exDistFlg;
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
		this.planTaiGaiFlgRik = planTaiGaiFlgRik;
	}

	/**
	 * 施設分類を取得する。
	 *
	 * @return 施設分類
	 */
	public String getInsClass() {
		return insClass;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
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
	 * 計画値(B)を取得する。
	 *
	 * @return 計画値(B)
	 */
	public Long getPlannedValueB() {
		return plannedValueB;
	}

	/**
	 * 計画値(B2)を取得する。
	 *
	 * @return 計画値(B2)
	 */
	public Long getPlannedValueB2() {
		return plannedValueB2;
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
	 * prodNameを取得する。
	 *
	 * @return prodName
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 製品区分を取得する。
	 *
	 * @return prodType
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * TMS特約店コードを取得する。
	 *
	 * @return tmsTytenCdsTytenCd
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）を取得する。
	 *
	 * @return tmsTytenName
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
	}

	/**
	 * 立案品目を取得する。
	 *
	 * @return 立案品目
	 */
	public MonNnu getMonNnu0() {
		return monNnu0;
	}

	/**
	 * 参照品目0_前々々期を取得する。
	 *
	 * @return 参照品目0_前々々期
	 */
	public String getmonNnu0_preFarAdvancePeriod() {
		if (monNnu0 != null) {
			return String.valueOf(monNnu0.getPreFarAdvancePeriod());
		} else {
			return "-";
		}
	}

	/**
	 * 参照品目0_前々期を取得する。
	 *
	 * @return 参照品目0_前々期
	 */
	public String getmonNnu0_FarAdvancePeriod() {
		if (monNnu0 != null) {
			return String.valueOf(monNnu0.getFarAdvancePeriod());
		} else {
			return "-";
		}
	}

	/**
	 * 参照品目0_前期を取得する。
	 *
	 * @return 参照品目0_前期
	 */
	public String getmonNnu0_AdvancePeriod() {
		if (monNnu0 != null) {
			return String.valueOf(monNnu0.getAdvancePeriod());
		} else {
			return "-";
		}
	}

	/**
	 * 参照品目0_当期を取得する。
	 *
	 * @return 参照品目0_当期
	 */
	public String getmonNnu0_CurrentPeriod() {
		if (monNnu0 != null) {
			return String.valueOf(monNnu0.getCurrentPeriod());
		} else {
			return "-";
		}
	}

	/**
	 * 削除フラグを取得する。
	 *
	 * @return 削除フラグ
	 */
	public Boolean getDelFlg() {
		return delFlg;
	}

	/**
	 * 配分除外フラグを取得する。
	 *
	 * @return 配分除外フラグ
	 */
	public Boolean getExDistFlg() {
		return exDistFlg;
	}

	/**
	 * 計画立案対象外フラグ(当期用)
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	/**
	 * 計画立案対象外フラグ(来期用)
	 *
	 * @return 計画立案対象外フラグ(来期用)
	 */
	public Boolean getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
