package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ProdInfo;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.PlanType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特定施設個別計画を表すDTOクラス
 * 
 * @author siwamoto
 */
public class SpecialInsPlanDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

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
	 * 計画値(Y)
	 */
	private final Long plannedValueY;

	/**
	 * 計画値(Y)2
	 */
	private final Long plannedValueY2;

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
	 * 対象区分（Ref[施設情報].〔対象区分〕）
	 */
	private final HoInsType hoInsType;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private final String tmsTytenName;

	/**
	 * 品目共通情報
	 */
	private final ProdInfo prodInfo;

	/**
	 * 削除フラグ
	 */
	private Boolean delFlg;

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
	public SpecialInsPlanDto(Integer jgiNo, String insNo, String prodCode, String tmsTytenCd, PlanType planType, Long plannedValueY, Long plannedValueY2, String jgiName,
		String relnInsNo, InsClass insClass, OldInsrFlg oldInsrFlg, String insAbbrName, HoInsType hoInsType, String tmsTytenName, ProdInfo prodInfo, Boolean delFlg) {
		super();
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.prodCode = prodCode;
		this.tmsTytenCd = tmsTytenCd;
		this.planType = planType;
		this.plannedValueY = plannedValueY;
		this.plannedValueY2 = plannedValueY2;
		this.jgiName = jgiName;
		this.relnInsNo = relnInsNo;
		this.insClass = insClass;
		this.oldInsrFlg = oldInsrFlg;
		this.insAbbrName = insAbbrName;
		this.hoInsType = hoInsType;
		this.tmsTytenName = tmsTytenName;
		this.prodInfo = prodInfo;
		this.delFlg = delFlg;
		this.seqKey = null;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param no
	 * @param no2
	 * @param code
	 * @param tytenCd
	 * @param type
	 * @param valueY
	 * @param name
	 * @param key
	 */
	public SpecialInsPlanDto(Integer jgiNo, String insNo, String prodCode, String tmsTytenCd, PlanType planType, Long plannedValueY, String jgiName, Long seqKey) {
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.prodCode = prodCode;
		this.tmsTytenCd = tmsTytenCd;
		this.planType = planType;
		this.plannedValueY = plannedValueY;
		this.jgiName = jgiName;
		this.seqKey = seqKey;
		this.plannedValueY2 = null;
		this.relnInsNo = null;
		this.insClass = null;
		this.oldInsrFlg = null;
		this.insAbbrName = null;
		this.hoInsType = null;
		this.tmsTytenName = null;
		this.prodInfo = null;
		this.delFlg = null;
	}

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * seqKeyを取得する。
	 * 
	 * @return seqKey
	 */
	public Long getSeqKey() {
		return seqKey;
	}

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
	 * 計画値(Y)を取得する。
	 * 
	 * @return 計画値(Y)
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
	}

	/**
	 * 計画値(Y)を取得する。
	 * 
	 * @return 計画値(Y)
	 */
	public Long getPlannedValueY2() {
		return plannedValueY2;
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
	 * 対象区分を取得する。
	 * 
	 * @return 対象区分
	 */
	public HoInsType getHoInsType() {
		return hoInsType;
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
	 * 削除フラグを取得する。
	 * 
	 * @return 削除フラグ
	 */
	public Boolean getDelFlg() {
		return delFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
