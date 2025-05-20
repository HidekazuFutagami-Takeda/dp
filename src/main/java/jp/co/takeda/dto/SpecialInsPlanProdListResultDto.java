package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.ProdInsInfoKanri;
import jp.co.takeda.model.div.ProdInsInfoErrKbn;

/**
 * 特定施設個別計画立案画面（担当者案）の検索結果用DTO
 *
 * @author siwamoto
 */
public class SpecialInsPlanProdListResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

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
	 * 対象
	 */
	private final String insType;

	/**
	 * 計画値(Y)
	 */
	private final Long plannedValueY;

	/**
	 * 計画値(Y2)
	 */
	private final Long plannedValueY2;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 品目カテゴリー
	 */
	private final String prodCategory;

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
	 * 施設情報名称
	 */
	private final String insInfoName;

	/**
	 * 表示文字カラーコード
	 */
	private final String dispFontColCd;

	/**
	 * エラーフラグ
	 */
	private final Boolean errFlg;

	/**
	 * 警告フラグ
	 */
	private final Boolean alertFlg;

	/**
	 * コンストラクタ
	 *
	 * @param seqKey シーケンスキー
	 * @param insNo 施設コード
	 * @param insName 施設名
	 * @param insType 対象
	 * @param insClazz 施設分類
	 * @param plannedValueY 計画値(Y)
	 * @param plannedValueY2 計画値(Y2)
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名
	 * @param prodCategory 品目カテゴリー
	 * @param prodType 製品区分
	 * @param tmsTytenCd TMS特約店コード
	 * @param tmsTytenName TMS特約店名称
	 * @param upDate 最終更新日時
	 * @param upJgiName 最終更新者名
	 * @param monNnu0 立案品目
	 * @param delFlg 削除フラグ
	 * @param exDistFlg 配分除外フラグ
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 * @param planTaiGaiFlgRik 計画立案対象外フラグ(来期用)
	 */
	public SpecialInsPlanProdListResultDto(Long seqKey, String insNo, String insName, String insType, String insClass, Long plannedValueY, Long plannedValueY2, String prodCode,
		String prodName, String prodCategory, String prodType, String tmsTytenCd, String tmsTytenName, Date upDate, String upJgiName, MonNnu monNnu0, Boolean delFlg,
		Boolean exDistFlg, Boolean planTaiGaiFlgTok, Boolean planTaiGaiFlgRik) {
		this(seqKey, insNo, insName, insType, insClass, plannedValueY, plannedValueY2, prodCode, prodName, prodCategory, prodType, tmsTytenCd, tmsTytenName, upDate, upJgiName,
			monNnu0, delFlg, exDistFlg, planTaiGaiFlgTok, planTaiGaiFlgRik, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param seqKey シーケンスキー
	 * @param insNo 施設コード
	 * @param insName 施設名
	 * @param insType 対象
	 * @param insClazz 施設分類
	 * @param plannedValueY 計画値(Y)
	 * @param plannedValueY2 計画値(Y2)
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名
	 * @param prodCategory 品目カテゴリー
	 * @param prodType 製品区分
	 * @param tmsTytenCd TMS特約店コード
	 * @param tmsTytenName TMS特約店名称
	 * @param upDate 最終更新日時
	 * @param upJgiName 最終更新者名
	 * @param monNnu0 立案品目
	 * @param delFlg 削除フラグ
	 * @param exDistFlg 配分除外フラグ
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 * @param planTaiGaiFlgRik 計画立案対象外フラグ(来期用)
	 * @param prodIns 品目施設情報
	 */
	public SpecialInsPlanProdListResultDto(Long seqKey, String insNo, String insName, String insType, String insClass, Long plannedValueY, Long plannedValueY2, String prodCode,
		String prodName, String prodCategory, String prodType, String tmsTytenCd, String tmsTytenName, Date upDate, String upJgiName, MonNnu monNnu0, Boolean delFlg,
		Boolean exDistFlg, Boolean planTaiGaiFlgTok, Boolean planTaiGaiFlgRik, ProdInsInfoKanri prodIns) {
		this.seqKey = seqKey;
		this.insNo = insNo;
		this.insName = insName;
		this.insType = insType;
		this.insClass = insClass;
		this.plannedValueY = plannedValueY;
		this.plannedValueY2 = plannedValueY2;
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodCategory = prodCategory;
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
		if (prodIns != null) {
			this.insInfoName = prodIns.getInsInfoName();
			this.dispFontColCd = prodIns.getDispFontColCd();
			this.errFlg = ProdInsInfoErrKbn.ERROR == prodIns.getProdInsInfoErrKbn();
			this.alertFlg = ProdInsInfoErrKbn.ALERT == prodIns.getProdInsInfoErrKbn();
		} else {
			this.insInfoName = null;
			this.dispFontColCd = null;
			this.errFlg = false;
			this.alertFlg = false;
		}
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return seqKey シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 施設分類を取得する。
	 *
	 * @return insClass 施設分類
	 */
	public String getInsClass() {
		return insClass;
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
	 * 施設名を取得する。
	 *
	 * @return insName 施設名
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 対象を取得する。
	 *
	 * @return insType 対象
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 計画値(Y)を取得する。
	 *
	 * @return plannedValueY 計画値(Y)
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
	}

	/**
	 * 計画値(Y2)を取得する。
	 *
	 * @return plannedValueY2 計画値(Y2)
	 */
	public Long getPlannedValueY2() {
		return plannedValueY2;
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
	 * 品目名を取得する。
	 *
	 * @return prodName 品目名
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目カテゴリーを取得する。
	 *
	 * @return prodCategory 品目カテゴリー
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 製品区分を取得する。
	 *
	 * @return prodType 製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * TMS特約店コードを取得する。
	 *
	 * @return tmsTytenCdsTytenCd TMS特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * TMS特約店名称を取得する。
	 *
	 * @return tmsTytenName TMS特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return upJgiName 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
	}

	/**
	 * 立案品目を取得する。
	 *
	 * @return monNu0 立案品目
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

	/**
	 * 施設情報名称を取得する。
	 *
	 * @return 施設情報名称
	 */
	public String getInsInfoName() {
		return insInfoName;
	}

	/**
	 * 表示文字カラーコードを取得する。
	 *
	 * @return 表示文字カラーコード
	 */
	public String getDispFontColCd() {
		return dispFontColCd;
	}

	/**
	 * エラーフラグを取得する。
	 *
	 * @return エラーフラグ
	 */
	public Boolean getErrFlg() {
		return errFlg;
	}

	/**
	 * アラートフラグを取得する。
	 *
	 * @return アラートフラグ
	 */
	public Boolean getAlertFlg() {
		return alertFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
