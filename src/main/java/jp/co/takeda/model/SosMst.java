package jp.co.takeda.model;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.BumonRank;

/**
 * 組織基本を表すモデルクラス
 *
 * @author tkawabata
 */
public final class SosMst extends Model<SosMst> implements Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * カンパニーコード
	 */
	public static final String COMPANY_CD = "00";

	/**
	 * 組織コードなし
	 */
	public static final String BRANK_SOS_CD = "00000";

	/**
	 * 標準組織背番号Ｃ1(医営本は[01050]固定)
	 */
	public static final String SOS_CD1 = "01050";

	/**
	 * 組織グループコード[ＭＲ所属組織(特担以外)]
	 */
	public static final String SHITEN_GROUP = "SSK0073";

	/**
	 * 組織グループコード[整形ＭＲ]
	 */
	public static final String SEIKEI_MR_GROUP = "SSK0083";

	/**
	 * 組織グループコード[西日本オンコロジーG]
	 */
	public static final String ONC_W_GROUP = "SSK0006";

	/**
	 * 組織グループコード[東日本オンコロジーG]
	 */
	public static final String ONC_E_GROUP = "SSK0007";

	/**
	 * 組織グループコード[特約店部(小児科MR所属組織)] (J19-0010 対応・コメントのみ修正)
	 */
	public static final String TOKUYAKUTEN_VAC_GROUP = "SSK0075";

	/**
	 * 組織グループコード[特約店部]
	 */
	public static final String TOKUYAKUTEN_BU_GROUP = "SSK0072";

	/**
	 * 組織グループコード[本部・企画Ｇ]
	 */
	public static final String HONBU_KIKAKU_GROUP = "SSK0013";

	/**
	 * 組織グループコード[流通推進部]
	 */
	public static final String HONBU_RYUTU_SUISIN_GROUP = "SSK0016";

	/**
	 * 組織グループコード[NS]
	 */
	public static final String NS_GROUP = "SSK0278";

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 都道府県コード
	 */
	private String addrCodePref;
	/**
	 * 医薬支店Ｃ(組織のソートキー1)
	 */
	private String brCode;

	/**
	 * 営業所配列(組織のソートキー2)
	 */
	private String distSeq;

	/**
	 * 医薬営業所Ｃ(組織のソートキー3)
	 */
	private String distCode;

	/**
	 * 医薬チームＣ(組織のソートキー4)
	 */
	private String teamCode;

	/**
	 * 部門名正式<br>
	 * (該当階層の組織名)
	 */
	private String bumonSeiName;

	/**
	 * 部門名略式<br>
	 * (該当階層の組織名)
	 */
	private String bumonRyakuName;

	/**
	 * 組織名称<br>
	 * (「部門名略式」を該当の階層まで上位の階層から文字列をアペンド)
	 */
	private String sosName;

	/**
	 * 上位組織コード
	 */
	private String upSosCd;

	/**
	 * 部門ランク
	 */
	private BumonRank bumonRank;

	/**
	 * 雑組織フラグ
	 */
	private Boolean etcSosFlg;

	/**
	 * 整形組織フラグ
	 * NOTE:2014下期向け支援改定にて、整形対応削除（現在未使用）
	 */
	private Boolean seikeiSosFlg;

	/**
	 * ONC組織フラグ
	 */
	private Boolean oncSosFlg;

	/**
	 * カテゴリ　※DBカラムはCATEGORY
	 */
	private List<SosMstCtg> sosCategoryList;

	/**
	 * 下位組織数
	 */
	private Integer underSosCnt;

	/**
	 * サブカテゴリ　※DBカラムはSUB_CATEGORY
	 */
	private String sosSubCategory;


	/**
	 * 仕入参照権限
	 */
	private boolean refAuthSiire = false;

	/**
	 * 営業所参照権限
	 */
	private boolean refAuthEigyo = false;

	/**
	 * ワクチン参照権限
	 */
	private boolean refAuthVaccine = false;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 組織コードを取得する。
	 *
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織コードを設定する。
	 *
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 都道府県コードを取得する。
	 *
	 * @return 都道府県コード
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 組織都道府県コードを設定する。
	 *
	 * @param addrCodePref 都道府県コード
	 */
	public void setAddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * 医薬支店Ｃを取得する。
	 *
	 * @return 医薬支店Ｃ
	 */
	public String getBrCode() {
		return brCode;
	}

	/**
	 * 医薬支店Ｃを設定する。
	 *
	 * @param brCode 医薬支店Ｃ
	 */
	public void setBrCode(String brCode) {
		this.brCode = brCode;
	}

	/**
	 * 営業所配列を取得する。
	 *
	 * @return 営業所配列
	 */
	public String getDistSeq() {
		return distSeq;
	}

	/**
	 * distSeqを設定する。
	 *
	 * @param distSeq
	 */
	public void setDistSeq(String distSeq) {
		this.distSeq = distSeq;
	}

	/**
	 * 医薬営業所Ｃを取得する。
	 *
	 * @return 医薬営業所Ｃ
	 */
	public String getDistCode() {
		return distCode;
	}

	/**
	 * 医薬営業所Ｃを設定する。
	 *
	 * @param distCode 医薬営業所Ｃ
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	/**
	 * 医薬チームＣを取得する。
	 *
	 * @return 医薬チームＣ
	 */
	public String getTeamCode() {
		return teamCode;
	}

	/**
	 * 医薬チームＣを設定する。
	 *
	 * @param teamCode 医薬チームＣ
	 */
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	/**
	 * 部門名正式を取得する。
	 *
	 * @return 部門名正式
	 */
	public String getBumonSeiName() {
		return bumonSeiName;
	}

	/**
	 * 部門名正式を設定する。
	 *
	 * @param bumonSeiName 部門名正式
	 */
	public void setBumonSeiName(String bumonSeiName) {
		this.bumonSeiName = bumonSeiName;
	}

	/**
	 * 部門名略式を取得する。
	 *
	 * @return 部門名略式
	 */
	public String getBumonRyakuName() {
		return bumonRyakuName;
	}

	/**
	 * 部門名略式を設定する。
	 *
	 * @param bumonRyakuName 部門名略式
	 */
	public void setBumonRyakuName(String bumonRyakuName) {
		this.bumonRyakuName = bumonRyakuName;
	}

	/**
	 * 組織名称を取得する。
	 *
	 * @return 組織名称
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * 組織名称を設定する。
	 *
	 * @param sosName 組織名称
	 */
	public void setSosName(String sosName) {
		this.sosName = sosName;
	}

	/**
	 * 上位組織コードを取得する。
	 *
	 * @return 上位組織コード
	 */
	public String getUpSosCd() {
		return upSosCd;
	}

	/**
	 * 上位組織コードを設定する。
	 *
	 * @param upSosCd 上位組織コード
	 */
	public void setUpSosCd(String upSosCd) {
		this.upSosCd = upSosCd;
	}

	/**
	 * 部門ランクを取得する。
	 *
	 * @return 部門ランク
	 */
	public BumonRank getBumonRank() {
		return bumonRank;
	}

	/**
	 * 部門ランクを設定する。
	 *
	 * @param bumonRank 部門ランク
	 */
	public void setBumonRank(BumonRank bumonRank) {
		this.bumonRank = bumonRank;
	}

	/**
	 * 雑組織フラグを取得する。
	 *
	 * @return 雑組織フラグ
	 */
	public Boolean getEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * 雑組織フラグを設定する。
	 *
	 * @param etcSosFlg 雑組織フラグ
	 */
	public void setEtcSosFlg(Boolean etcSosFlg) {
		this.etcSosFlg = etcSosFlg;
	}

	/**
	 * 整形組織フラグを取得する。
	 *
	 * @return 整形組織フラグ
	 */
	public Boolean getSeikeiSosFlg() {
		return seikeiSosFlg;
	}

	/**
	 * 整形組織フラグを設定する。
	 *
	 * @param seikeiSosFlg 整形組織フラグ
	 */
	public void setSeikeiSosFlg(Boolean seikeiSosFlg) {
		this.seikeiSosFlg = seikeiSosFlg;
	}

	/**
	 * ONC組織フラグを取得する。
	 *
	 * @return ONC組織フラグ
	 */
	public Boolean getOncSosFlg() {
		return oncSosFlg;
	}

	/**
	 * ONC組織フラグを設定する。
	 *
	 * @param oncSosFlg ONC組織フラグ
	 */
	public void setOncSosFlg(Boolean oncSosFlg) {
		this.oncSosFlg = oncSosFlg;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリを取得する。※DBカラムはCATEGORY
	 *
	 * @return カテゴリ
	 */
	public List<SosMstCtg> getSosCategoryList() {
		return sosCategoryList;
	}

	/**
	 * カテゴリを設定する。※DBカラムはCATEGORY
	 *
	 * @param oncSosFlg カテゴリ
	 */
	public void setSosCategoryList(List<SosMstCtg> sosCategoryList) {
		this.sosCategoryList = sosCategoryList;
	}

	/**
	 * 【暫定対応】組織カテゴリの1番目のカテゴリを返却
	 * @return 組織カテゴリの1番目のカテゴリを返却
	 */
	@Deprecated
	public String getSosCategory() {
		if(this.sosCategoryList == null) {
			return null;
		}
		return this.sosCategoryList.get(0).getCategoryCd();

	}

	/**
	 * 下位組織数を取得する。
	 *
	 * @return 下位組織数
	 */
	public Integer getUnderSosCnt() {
		return underSosCnt;
	}

	/**
	 * 下位組織数を設定する。
	 *
	 * @param underSosCnt 下位組織数
	 */
	public void setUnderSosCnt(Integer underSosCnt) {
		this.underSosCnt = underSosCnt;
	}

	/**
	 * サブカテゴリを取得する。※DBカラムはSUB_CATEGORY
	 *
	 * @return サブカテゴリ
	 */
	public String getSosSubCategory() {
		return sosSubCategory;
	}

	/**
	 * サブカテゴリを設定する。※DBカラムはSUB_CATEGORY
	 *
	 * @param sosSubCategory サブカテゴリ
	 */
	public void setSosSubCategory(String sosSubCategory) {
		this.sosSubCategory = sosSubCategory;
	}


	/**
	 * 仕入参照権限があるか
	 * @return
	 */
	public boolean hasRefAuthSiire() {
		return refAuthSiire;
	}

	/**
	 * 仕入参照権限を設定する
	 * @param refAuthSiire
	 */
	public void setRefAuthSiire(boolean refAuthSiire) {
		this.refAuthSiire = refAuthSiire;
	}

	/**
	 * 営業参照権限があるか
	 * @return
	 */
	public boolean hasRefAuthEigyo() {
		return refAuthEigyo;
	}

	/**
	 * 営業参照権限を設定する
	 * @return
	 */
	public void setRefAuthEigyo(boolean refAuthEigyo) {
		this.refAuthEigyo = refAuthEigyo;
	}

	/**
	 * ワクチン参照権限があるか
	 * @return
	 */
	public boolean hasRefAuthVaccine() {
		return refAuthVaccine;
	}

	/**
	 * ワクチン参照権限を設定する
	 * @return
	 */
	public void setRefAuthVaccine(boolean refAuthVaccine) {
		this.refAuthVaccine = refAuthVaccine;
	}


// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	@Override
	public int compareTo(SosMst obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.etcSosFlg, obj.etcSosFlg).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosMst.class.isAssignableFrom(entry.getClass())) {
			SosMst obj = (SosMst) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.etcSosFlg, obj.etcSosFlg).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.etcSosFlg).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public SosMst clone() {
		SosMst sosMst = new SosMst();
		sosMst.setBrCode(this.brCode);
		sosMst.setBumonRank(this.bumonRank);
		sosMst.setBumonRyakuName(this.bumonRyakuName);
		sosMst.setBumonSeiName(this.bumonSeiName);
		sosMst.setDistCode(this.distCode);
		sosMst.setDistSeq(this.distSeq);
		sosMst.setSosCd(this.sosCd);
		sosMst.setSosName(this.sosName);
		sosMst.setTeamCode(this.teamCode);
		sosMst.setUpSosCd(this.upSosCd);
		sosMst.setEtcSosFlg(this.etcSosFlg);
		sosMst.setSeikeiSosFlg(this.seikeiSosFlg);
		sosMst.setOncSosFlg(this.oncSosFlg);
		sosMst.setSosCategoryList(this.sosCategoryList);
		sosMst.setUnderSosCnt(this.underSosCnt);
		sosMst.setSosSubCategory(this.sosSubCategory);
		sosMst.setRefAuthEigyo(this.refAuthEigyo);
		sosMst.setRefAuthSiire(this.refAuthSiire);
		sosMst.setRefAuthVaccine(this.refAuthVaccine);
		return sosMst;
	}

}
