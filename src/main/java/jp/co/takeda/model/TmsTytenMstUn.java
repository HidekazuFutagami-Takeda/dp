package jp.co.takeda.model;

import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.TytenKisLevel;

/**
 * [外部直接参照]TMS特約店基本統合を表すモデルクラス
 *
 * @author tkawabata
 */
public class TmsTytenMstUn extends Model<TmsTytenMstUn> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * TMS特約店コードブロック2のブランク時の値
	 */
	public static final String TMS_TYTEN_BLOCK2_BLANK = "  ";

	//---------------------
	// Data Field
	// --------------------

	/**
	 * TMS特約店ユニークキー
	 */
	private Integer tTmsTytenMstUnqKey;

	/**
	 * TMS特約店コード(ソートキー)
	 */
	private String tmsTytenCd;

	/**
	 * 適用年月日(YYYYMMDD形式)
	 */
	private String tekiyoYmd;

	/**
	 * TMS 特約店コード 本店
	 */
	private String tmsTytenCdHonten;

	/**
	 * TMS 特約店コード 支社
	 */
	private String tmsTytenCdShisha;

	/**
	 * TMS 特約店コード 支店
	 */
	private String tmsTytenCdShiten;

	/**
	 * TMS 特約店コード 課
	 */
	private String tmsTytenCdKa;

	/**
	 * TMS 特約店コード ブロック１
	 */
	private String tmsTytenCdBlk1;

	/**
	 * TMS 特約店コード ブロック２
	 */
	private String tmsTytenCdBlk2;

	/**
	 * TMS特約店名称（漢字）
	 */
	private String tmsTytenMeiKj;

	/**
	 * 本店名漢字
	 */
	private String hontenMeiKj;

	/**
	 * 支社名漢字
	 */
	private String shishaMeiKj;

	/**
	 * 支店名漢字
	 */
	private String shitenMeiKj;

	/**
	 * 課名漢字
	 */
	private String kaMeiKj;

	/**
	 * ブロック１名称漢字
	 */
	private String blk1MeiKj;

	/**
	 * ブロック２名称漢字
	 */
	private String blk2MeiKj;

	/**
	 * 特約店階層レベル
	 */
	private TytenKisLevel tytenKisLvll;

	/**
	 * 武田組織１コード
	 */
	private String takedaSsk1Cd;

	/**
	 * 武田組織２コード
	 */
	private String takedaSsk2Cd;

	/**
	 * JIS府県コード
	 */
	private String jisFukenCd;

	/**
	 * 適用終了日
	 */
	private String tekiyoEndYmd;

	/**
	 * 更新年月日
	 */
	private Date updTs;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private Boolean planTaiGaiFlgTok;

	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private Boolean planTaiGaiFlgRik;

	/**
	 * 特約店実績有無判定フラグ
	 */
	private Boolean deliveryFlg;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * TMS特約店ユニークキーを取得する。
	 *
	 * @return TMS特約店ユニークキー
	 */
	public Integer getTTmsTytenMstUnqKey() {
		return tTmsTytenMstUnqKey;
	}

	/**
	 * TMS特約店ユニークキーを設定する。
	 *
	 * @param tTmsTytenMstUnqKey TMS特約店ユニークキー
	 */
	public void setTTmsTytenMstUnqKey(Integer tTmsTytenMstUnqKey) {
		this.tTmsTytenMstUnqKey = tTmsTytenMstUnqKey;
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
	 * TMS特約店コードを設定する。
	 *
	 * @param tmsTytenCd TMS特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 適用年月日(YYYYMMDD形式)を取得する。
	 *
	 * @return 適用年月日(YYYYMMDD形式)
	 */
	public String getTekiyoYMD() {
		return tekiyoYmd;
	}

	/**
	 * 適用年月日(YYYYMMDD形式)を設定する。
	 *
	 * @param tekiyoYmd 適用年月日(YYYYMMDD形式)
	 */
	public void setTekiyoYMD(String tekiyoYmd) {
		this.tekiyoYmd = tekiyoYmd;
	}

	/**
	 * TMS 特約店コード 本店を取得する。
	 *
	 * @return TMS 特約店コード 本店
	 */
	public String getTmsTytenCdHonten() {
		return tmsTytenCdHonten;
	}

	/**
	 * TMS 特約店コード 本店を設定する。
	 *
	 * @param tmsTytenCdHonten TMS 特約店コード 本店
	 */
	public void setTmsTytenCdHonten(String tmsTytenCdHonten) {
		this.tmsTytenCdHonten = tmsTytenCdHonten;
	}

	/**
	 * TMS 特約店コード 支社を取得する。
	 *
	 * @return TMS 特約店コード 支社
	 */
	public String getTmsTytenCdShisha() {
		return tmsTytenCdShisha;
	}

	/**
	 * TMS 特約店コード 支社を設定する。
	 *
	 * @param tmsTytenCdShisha TMS 特約店コード 支社
	 */
	public void setTmsTytenCdShisha(String tmsTytenCdShisha) {
		this.tmsTytenCdShisha = tmsTytenCdShisha;
	}

	/**
	 * TMS 特約店コード 支店を取得する。
	 *
	 * @return TMS 特約店コード 支店
	 */
	public String getTmsTytenCdShiten() {
		return tmsTytenCdShiten;
	}

	/**
	 * TMS 特約店コード 支店を設定する。
	 *
	 * @param tmsTytenCdShiten TMS 特約店コード 支店
	 */
	public void setTmsTytenCdShiten(String tmsTytenCdShiten) {
		this.tmsTytenCdShiten = tmsTytenCdShiten;
	}

	/**
	 * TMS 特約店コード 課を取得する。
	 *
	 * @return TMS 特約店コード 課
	 */
	public String getTmsTytenCdKa() {
		return tmsTytenCdKa;
	}

	/**
	 * TMS 特約店コード 課を設定する。
	 *
	 * @param tmsTytenCdKa TMS 特約店コード 課
	 */
	public void setTmsTytenCdKa(String tmsTytenCdKa) {
		this.tmsTytenCdKa = tmsTytenCdKa;
	}

	/**
	 * TMS 特約店コード ブロック１を取得する。
	 *
	 * @return TMS 特約店コード ブロック１
	 */
	public String getTmsTytenCdBlk1() {
		return tmsTytenCdBlk1;
	}

	/**
	 * TMS 特約店コード ブロック１を設定する。
	 *
	 * @param tmsTytenCdBlk1 TMS 特約店コード ブロック１
	 */
	public void setTmsTytenCdBlk1(String tmsTytenCdBlk1) {
		this.tmsTytenCdBlk1 = tmsTytenCdBlk1;
	}

	/**
	 * TMS 特約店コード ブロック２を取得する。
	 *
	 * @return TMS 特約店コード ブロック２
	 */
	public String getTmsTytenCdBlk2() {
		return tmsTytenCdBlk2;
	}

	/**
	 * TMS 特約店コード ブロック２を設定する。
	 *
	 * @param tmsTytenCdBlk2 TMS 特約店コード ブロック２
	 */
	public void setTmsTytenCdBlk2(String tmsTytenCdBlk2) {
		this.tmsTytenCdBlk2 = tmsTytenCdBlk2;
	}

	/**
	 * TMS特約店名称（漢字）を取得する。
	 *
	 * @return TMS特約店名称（漢字）
	 */
	public String getTmsTytenMeiKj() {
		return tmsTytenMeiKj;
	}

	/**
	 * TMS特約店名称（漢字）を設定する。
	 *
	 * @param tmsTytenMeiKj TMS特約店名称（漢字）
	 */
	public void setTmsTytenMeiKj(String tmsTytenMeiKj) {
		this.tmsTytenMeiKj = tmsTytenMeiKj;
	}

	/**
	 * 本店名漢字を取得する。
	 *
	 * @return 本店名漢字
	 */
	public String getHontenMeiKj() {
		return hontenMeiKj;
	}

	/**
	 * 本店名漢字を設定する。
	 *
	 * @param hontenMeiKj 本店名漢字
	 */
	public void setHontenMeiKj(String hontenMeiKj) {
		this.hontenMeiKj = hontenMeiKj;
	}

	/**
	 * 支社名漢字を取得する。
	 *
	 * @return 支社名漢字
	 */
	public String getShishaMeiKj() {
		return shishaMeiKj;
	}

	/**
	 * 支社名漢字を設定する。
	 *
	 * @param shishaMeiKj 支社名漢字
	 */
	public void setShishaMeiKj(String shishaMeiKj) {
		this.shishaMeiKj = shishaMeiKj;
	}

	/**
	 * 支店名漢字を取得する。
	 *
	 * @return 支店名漢字
	 */
	public String getShitenMeiKj() {
		return shitenMeiKj;
	}

	/**
	 * 支店名漢字を設定する。
	 *
	 * @param shitenMeiKj 支店名漢字
	 */
	public void setShitenMeiKj(String shitenMeiKj) {
		this.shitenMeiKj = shitenMeiKj;
	}

	/**
	 * 課名漢字を取得する。
	 *
	 * @return 課名漢字
	 */
	public String getKaMeiKj() {
		return kaMeiKj;
	}

	/**
	 * 課名漢字を設定する。
	 *
	 * @param kaMeiKj 課名漢字
	 */
	public void setKaMeiKj(String kaMeiKj) {
		this.kaMeiKj = kaMeiKj;
	}

	/**
	 * ブロック１名称漢字を取得する。
	 *
	 * @return ブロック１名称漢字
	 */
	public String getBlk1MeiKj() {
		return blk1MeiKj;
	}

	/**
	 * ブロック１名称漢字を設定する。
	 *
	 * @param blk1MeiKj ブロック１名称漢字
	 */
	public void setBlk1MeiKj(String blk1MeiKj) {
		this.blk1MeiKj = blk1MeiKj;
	}

	/**
	 * ブロック２名称漢字を取得する。
	 *
	 * @return ブロック２名称漢字
	 */
	public String getBlk2MeiKj() {
		return blk2MeiKj;
	}

	/**
	 * ブロック２名称漢字を設定する。
	 *
	 * @param blk2MeiKj ブロック２名称漢字
	 */
	public void setBlk2MeiKj(String blk2MeiKj) {
		this.blk2MeiKj = blk2MeiKj;
	}

	/**
	 * 特約店階層レベルを取得する。
	 *
	 * @return 特約店階層レベル
	 */
	public TytenKisLevel getTytenKisLvll() {
		return tytenKisLvll;
	}

	/**
	 * 特約店階層レベルを設定する。
	 *
	 * @param tytenKisLvll 特約店階層レベル
	 */
	public void setTytenKisLvll(TytenKisLevel tytenKisLvll) {
		this.tytenKisLvll = tytenKisLvll;
	}

	/**
	 * 武田組織１コードを取得する。
	 *
	 * @return 武田組織１コード
	 */
	public String getTakedaSsk1Cd() {
		return takedaSsk1Cd;
	}

	/**
	 * 武田組織１コードを設定する。
	 *
	 * @param takedaSsk1Cd 武田組織１コード
	 */
	public void setTakedaSsk1Cd(String takedaSsk1Cd) {
		this.takedaSsk1Cd = takedaSsk1Cd;
	}

	/**
	 * 武田組織２コードを取得する。
	 *
	 * @return 武田組織２コード
	 */
	public String getTakedaSsk2Cd() {
		return takedaSsk2Cd;
	}

	/**
	 * 武田組織２コードを設定する。
	 *
	 * @param jisFukenCd 武田組織２コード
	 */
	public void setTakedaSsk2Cd(String takedaSsk2Cd) {
		this.takedaSsk2Cd = takedaSsk2Cd;
	}

	/**
	 * JIS府県コードを取得する。
	 *
	 * @return JIS府県コード
	 */
	public String getJisFukenCd() {
		return jisFukenCd;
	}

	/**
	 * JIS府県コードを設定する。
	 *
	 * @param jisFukenCd JIS府県コード
	 */
	public void setJisFukenCd(String jisFukenCd) {
		this.jisFukenCd = jisFukenCd;
	}

	/**
	 * 適用終了日を取得する。
	 *
	 * @return 適用終了日
	 */
	public String getTekiyoEndYmd() {
		return tekiyoEndYmd;
	}

	/**
	 * 適用終了日を設定する。
	 *
	 * @param tekiyoEndYmd 適用終了日
	 */
	public void setTekiyoEndYmd(String tekiyoEndYmd) {
		this.tekiyoEndYmd = tekiyoEndYmd;
	}

	/**
	 * 更新年月日を取得する。
	 *
	 * @return 更新年月日
	 */
	public Date getUpdTs() {
		return updTs;
	}

	/**
	 * 更新年月日を設定する。
	 *
	 * @param updTs 更新年月日
	 */
	public void setUpdTs(Date updTs) {
		this.updTs = updTs;
	}

	/**
	 * 計画立案対象外フラグ(当期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	/**
	 * 計画立案対象外フラグ(当期用)を設定する。
	 *
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public void setPlanTaiGaiFlgTok(Boolean planTaiGaiFlgTok) {
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * 計画立案対象外フラグ(来期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(来期用)
	 */
	public Boolean getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
	}

	/**
	 * 計画立案対象外フラグ(来期用)を設定する。
	 *
	 * @param planTaiGaiFlgRik 計画立案対象外フラグ(来期用)
	 */
	public void setPlanTaiGaiFlgRik(Boolean planTaiGaiFlgRik) {
		this.planTaiGaiFlgRik = planTaiGaiFlgRik;
	}

	/**
	 * 特約店実績有無判定フラグを取得する
	 *
	 * @return deliveryFlg
	 */
	public Boolean getDeliveryFlg() {
		return deliveryFlg;
	}

	/**
	 * 特約店実績有無判定フラグを設定する
	 *
	 * @param deliveryFlg
	 */
	public void setDeliveryFlg(Boolean deliveryFlg) {
		this.deliveryFlg = deliveryFlg;
	}

	@Override
	public int compareTo(TmsTytenMstUn obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.tTmsTytenMstUnqKey, obj.tTmsTytenMstUnqKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && TmsTytenMstUn.class.isAssignableFrom(entry.getClass())) {
			TmsTytenMstUn obj = (TmsTytenMstUn) entry;
			return new EqualsBuilder().append(this.tTmsTytenMstUnqKey, obj.tTmsTytenMstUnqKey).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.tTmsTytenMstUnqKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
