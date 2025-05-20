package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ProdInfo;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.PlanType;

/**
 * 特定施設個別計画立案画面（担当者案）の検索結果用DTO
 *
 * @author siwamoto
 */
public class SpecialInsPlanForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * TMS特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 計画立案区分
	 */
	private final PlanType planType;

	/**
	 * 計画値(B)
	 */
	private final Long plannedValueB;

	/**
	 * 計画値(B2)
	 */
	private final Long plannedValueB2;

	/**
	 * 氏名（Ref[従業員情報].〔氏名〕）
	 */
	private final String jgiName;

	/**
	 * 施設内部コード（Ref[施設情報].〔施設内部コード〕）
	 */
	private final String relnInsNo;

	/**
	 * 施設分類（Ref[施設情報].〔対象分類〕）
	 */
	private final InsClass insClass;

	/**
	 * サブコード分類（Ref[施設情報].〔サブコード分類〕）
	 */
	private final OldInsrFlg oldInsrFlg;

	/**
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private final String insAbbrName;

	/**
	 * 削除フラグ（Ref[施設情報].〔削除フラグ〕）
	 */
	private final Boolean delFlg;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private final String tmsTytenName;

	/**
	 * 品目共通情報
	 */
	private final ProdInfo prodInfo;

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo
	 * @param insNo
	 * @param prodCode
	 * @param tmsTytenCd
	 * @param planType
	 * @param plannedValueY
	 * @param plannedValueY2
	 * @param jgiName
	 * @param relnInsNo
	 * @param insClass
	 * @param oldInsrFlg
	 * @param insAbbrName
	 * @param hoInsType
	 * @param tmsTytenName
	 * @param prodInfo
	 */
	public SpecialInsPlanForVacDto(Integer jgiNo, String insNo, String prodCode, String tmsTytenCd, PlanType planType, Long plannedValueB, Long plannedValueB2,String jgiName, String relnInsNo,
		InsClass insClass, OldInsrFlg oldInsrFlg, String insAbbrName, String tmsTytenName, ProdInfo prodInfo, Boolean delFlg) {
		super();
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.prodCode = prodCode;
		this.tmsTytenCd = tmsTytenCd;
		this.planType = planType;
		this.plannedValueB = plannedValueB;
		this.plannedValueB2 = plannedValueB2;
		this.jgiName = jgiName;
		this.relnInsNo = relnInsNo;
		this.insClass = insClass;
		this.oldInsrFlg = oldInsrFlg;
		this.insAbbrName = insAbbrName;
		this.tmsTytenName = tmsTytenName;
		this.prodInfo = prodInfo;
		this.delFlg = delFlg;
	}

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo
	 * @param insNo
	 * @param prodCode
	 * @param tmsTytenCd
	 * @param planType
	 * @param plannedValueB
	 * @param plannedValueB2
	 * @param jgiName
	 */
	public SpecialInsPlanForVacDto(Integer jgiNo, String insNo, String prodCode, String tmsTytenCd, PlanType planType, Long plannedValueB, Long plannedValueB2,String jgiName) {
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.prodCode = prodCode;
		this.tmsTytenCd = tmsTytenCd;
		this.planType = planType;
		this.plannedValueB = plannedValueB;
		this.plannedValueB2 = plannedValueB2;
		this.jgiName = jgiName;
		this.relnInsNo = null;
		this.insClass = null;
		this.oldInsrFlg = null;
		this.insAbbrName = null;
		this.tmsTytenName = null;
		this.prodInfo = null;
		this.delFlg = null;
	}

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
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
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * TMS特約店コードを取得する。
	 *
	 * @return TMS特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 計画立案区分を取得する。
	 *
	 * @return 計画立案区分
	 */
	public PlanType getPlanType() {
		return planType;
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
	 * 氏名を取得する。
	 *
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 施設内部コードを取得する。
	 *
	 * @return 施設内部コード
	 */
	public String getRelnInsNo() {
		return relnInsNo;
	}

	/**
	 * 施設分類を取得する。
	 *
	 * @return 施設分類
	 */
	public InsClass getInsClass() {
		return insClass;
	}

	/**
	 * サブコード分類を取得する。
	 *
	 * @return サブコード分類
	 */
	public OldInsrFlg getOldInsrFlg() {
		return oldInsrFlg;
	}

	/**
	 * 施設略式漢字名を取得する。
	 *
	 * @return 施設略式漢字名
	 */
	public String getInsAbbrName() {
		return insAbbrName;
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
	 * TMS特約店名称を取得する。
	 *
	 * @return TMS特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 品目共通情報を取得する。
	 *
	 * @return 品目共通情報
	 */
	public ProdInfo getProdInfo() {
		return prodInfo;
	}

	/**
	 * 計画値(B2)を取得する。
	 *
	 * @return 計画値(B2)
	 */
	public Long getPlannedValueB2() {
		return plannedValueB2;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
