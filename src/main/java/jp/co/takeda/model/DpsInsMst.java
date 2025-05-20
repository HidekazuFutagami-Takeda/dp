package jp.co.takeda.model;

//del Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
//import java.util.ArrayList;
//del End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.DpsHoInsType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.model.div.TradeType;

/**
 * 支援・施設基本を表すモデルクラス
 *
 * @author tkawabata
 */
public class DpsInsMst extends Model<DpsInsMst> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 施設分類
	 */
	private InsClass insClass;

	/**
	 * 施設内部コード
	 */
	private String relnInsNo;

	/**
	 * 施設内部コード（サブコード）
	 */
	private String relnInsCode;

	/**
	 * サブコード分類
	 */
	private OldInsrFlg oldInsrFlg;

	/**
	 * 施設カナ名(濁音、半濁音を除く)
	 */
	private String insKana;

	/**
	 * 検索用施設カナ名
	 */
	private String insKanaSrch;

	/**
	 * 施設略式漢字名
	 */
	private String insAbbrName;

	/**
	 * 施設正式漢字名
	 */
	private String insFormalName;

	/**
	 * JIS府県コード
	 */
	private Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private String addrCodeCity;

	/**
	 * 取引区分
	 */
	private TradeType tradeType;

	/**
	 * 対象区分
	 */
	private DpsHoInsType hoInsType;

	/**
	 * 依頼中フラグ
	 */
	private Boolean reqFlg;

	/**
	 * 削除フラグ
	 */
	private Boolean delFlg;

	/**
	 * 配分除外フラグ（医薬のみ）
	 */
	private Boolean exceptDistFlg;

	/**
	 * 活動区分（ワクチンのみ使用）
	 */
	private ActivityType activityType;

	/**
	 * 担当施設品目の従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設-品目による紐付け<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 */
	private Integer jgiNo;

	/**
	 * 担当施設品目の氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設-品目による紐付け<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 */
	private String jgiName;

	/**
	 * 担当施設品目の従業員の職種名（Ref[従業員情報].〔職種名〕）
	 * MR-施設-品目による紐付け<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 */
	private String shokushuName;

	/**
	 * COM-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer comJgiNo;

	/**
	 * COM-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String comJgiName;

	/**
	 * COM-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String comShokushuName;

	/**
	 * CVM-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer cvmJgiNo;

	/**
	 * CVM-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String cvmJgiName;

	/**
	 * CVM-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String cvmShokushuName;

	/**
	 * RS-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer rsJgiNo;

	/**
	 * RS-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String rsJgiName;

	/**
	 * RS-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String rsShokushuName;

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * SP-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer spJgiNo;

	/**
	 * SP-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String spJgiName;

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * SP-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String spShokushuName;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * ONC-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer oncJgiNo;

	/**
	 * ONC-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String oncJgiName;

	/**
	 * ONC-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String oncShokushuName;

// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
	/**
	 * KOKEI-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer kokeiJgiNo;

	/**
	 * KOKEI-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String kokeiJgiName;

	/**
	 * KOKEI-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String kokeiShokushuName;
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）

	/**
	 * VAC-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer vacJgiNo;

	/**
	 * VAC-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String vacJgiName;

	//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
		/**
		 * BU2CS-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
		 * MR-施設による紐付け
		 */
		private Integer bu2csMrJgiNo;

		/**
		 * BU2CS-氏名（Ref[従業員情報].〔氏名〕）<br>
		 * MR-施設による紐付け
		 */
		private String bu2csMrJgiName;

		/**
		 * JOHLC-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
		 * MR-施設による紐付け
		 */
		private Integer johlcMrJgiNo;

		/**
		 * JOHLC-氏名（Ref[従業員情報].〔氏名〕）<br>
		 * MR-施設による紐付け
		 */
		private String johlcMrJgiName;

		/**
		 * BU2BLO-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
		 * MR-施設による紐付け
		 */
		private Integer bu2bloMrJgiNo;

		/**
		 * BU2BLO-氏名（Ref[従業員情報].〔氏名〕）<br>
		 * MR-施設による紐付け
		 */
		private String bu2bloMrJgiName;

		/**
		 * BU2HER-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
		 * MR-施設による紐付け
		 */
		private Integer bu2herMrJgiNo;

		/**
		 * BU2HER-氏名（Ref[従業員情報].〔氏名〕）<br>
		 * MR-施設による紐付け
		 */
		private String bu2herMrJgiName;

		/**
		 * JOGUH-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
		 * MR-施設による紐付け
		 */
		private Integer joguhMrJgiNo;

		/**
		 * JOGUH-氏名（Ref[従業員情報].〔氏名〕）<br>
		 * MR-施設による紐付け
		 */
		private String joguhMrJgiName;

		/**
		 * BU1LAM-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
		 * MR-施設による紐付け
		 */
		private Integer bu1lamMrJgiNo;

		/**
		 * BU1LAM-氏名（Ref[従業員情報].〔氏名〕）<br>
		 * MR-施設による紐付け
		 */
		private String bu1lamMrJgiName;
	//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更

	/**
	 * VAC-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String vacShokushuName;

// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
	/**
	 * List用施設担当者-従業員番号（Ref[従業員情報].〔従業員番号〕）<br>
	 * MR-施設による紐付け
	 */
	private Integer listJgiNo;

	/**
	 * List用施設担当者-氏名（Ref[従業員情報].〔氏名〕）<br>
	 * MR-施設による紐付け
	 */
	private String listJgiName;

	/**
	 * List用施設担当者-職種名（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private String listShokushuName;

	/**
	 * 従業員番号～職務名までをリストにする器（Ref[従業員情報].〔職種名〕）<br>
	 * MR-施設による紐付け
	 */
	private List<JgiMst> tantoList;

	/**
	 * MR種別<br>
	 */
	private String  listMrCat;

	/**
	 * 従業員区分<br>
	 */
	private String  listJgiKb;

// add end 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

	/**
	 * 府県名（漢字）（Ref[JIS府県・市区町村].〔府県名（漢字）〕）
	 */
	private String fukenMeiKj;

	/**
	 * 市区郡町村名（漢字）（Ref[JIS府県・市区町村].〔市区郡町村名（漢字）〕）<br>
	 * JIS府県コード・JIS市区町村コードによる紐付け
	 */
	private String shikuchosonMeiKj;

	/**
	 * 品目施設情報
	 */
	private ProdInsInfoKanri prodIns;

	/**
	 * 施設コードを取得する。
	 *
	 * @return 施設コード
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
	 * 施設分類を取得する。
	 *
	 * @return 施設分類
	 */
	public InsClass getInsClass() {
		return insClass;
	}

	/**
	 * 施設分類を設定する。
	 *
	 * @param insClass 施設分類
	 */
	public void setInsClass(InsClass insClass) {
		this.insClass = insClass;
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
	 * 施設内部コードを設定する。
	 *
	 * @param relnInsNo 施設内部コード
	 */
	public void setRelnInsNo(String relnInsNo) {
		this.relnInsNo = relnInsNo;
	}

	/**
	 * 施設内部コード（サブコード）を取得する。
	 *
	 * @return 施設内部コード（サブコード）
	 */
	public String getRelnInsCode() {
		return relnInsCode;
	}

	/**
	 * 施設内部コード（サブコード）を設定する。
	 *
	 * @param relnInsCode 施設内部コード（サブコード）
	 */
	public void setRelnInsCode(String relnInsCode) {
		this.relnInsCode = relnInsCode;
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
	 * サブコード分類を設定する。
	 *
	 * @param oldInsrFlg サブコード分類
	 */
	public void setOldInsrFlg(OldInsrFlg oldInsrFlg) {
		this.oldInsrFlg = oldInsrFlg;
	}

	/**
	 * 施設カナ名を取得する。
	 *
	 * @return 施設カナ名
	 */
	public String getInsKana() {
		return insKana;
	}

	/**
	 * 施設カナ名を設定する。
	 *
	 * @param insKana 施設カナ名
	 */
	public void setInsKana(String insKana) {
		this.insKana = insKana;
	}

	/**
	 * 検索用施設カナ名を取得する。
	 *
	 * @return 検索用施設カナ名
	 */
	public String getInsKanaSrch() {
		return insKanaSrch;
	}

	/**
	 * 検索用施設カナ名を設定する。
	 *
	 * @param insKanaSrch 検索用施設カナ名
	 */
	public void setInsKanaSrch(String insKanaSrch) {
		this.insKanaSrch = insKanaSrch;
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
	 * 施設略式漢字名を設定する。
	 *
	 * @param insAbbrName 施設略式漢字名
	 */
	public void setInsAbbrName(String insAbbrName) {
		this.insAbbrName = insAbbrName;
	}

	/**
	 * 施設正式漢字名を取得する。
	 *
	 * @return 施設正式漢字名
	 */
	public String getInsFormalName() {
		return insFormalName;
	}

	/**
	 * 施設正式漢字名を設定する。
	 *
	 * @param insFormalName 施設正式漢字名
	 */
	public void setInsFormalName(String insFormalName) {
		this.insFormalName = insFormalName;
	}

	/**
	 * JIS府県コードを取得する。
	 *
	 * @return addrCodePref JIS府県コード
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS府県コードを設定する。
	 *
	 * @param addrCodePref JIS府県コード
	 */
	public void setAddrCodePref(Prefecture addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * JIS市区町村コードを取得する。
	 *
	 * @return JIS市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * JIS市区町村コードを設定する。
	 *
	 * @param addrCodeCity JIS市区町村コード
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * 取引区分を取得する。
	 *
	 * @return 取引区分
	 */
	public TradeType getTradeType() {
		return tradeType;
	}

	/**
	 * 取引区分を設定する。
	 *
	 * @param tradeType 取引区分
	 */
	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	/**
	 * 対象区分を取得する。
	 *
	 * @return 対象区分
	 */
	public DpsHoInsType getHoInsType() {
		return hoInsType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param hoInsType 対象区分
	 */
	public void setHoInsType(DpsHoInsType hoInsType) {
		this.hoInsType = hoInsType;
	}

	/**
	 * 依頼中フラグを取得する。
	 *
	 * @return 依頼中フラグ
	 */
	public Boolean getReqFlg() {
		return reqFlg;
	}

	/**
	 * 依頼中フラグを設定する。
	 *
	 * @param reqFlg 依頼中フラグ
	 */
	public void setReqFlg(Boolean reqFlg) {
		this.reqFlg = reqFlg;
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
	 * 削除フラグを設定する。
	 *
	 * @param delFlg 削除フラグ
	 */
	public void setDelFlg(Boolean delFlg) {
		this.delFlg = delFlg;
	}

	/**
	 * 活動区分を取得する。
	 *
	 * @return 活動区分
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * 活動区分を設定する。
	 *
	 * @param activityType 活動区分
	 */
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	/**
	 * 担当施設品目の従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 担当施設品目の従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 *
	 * @param jgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 担当施設品目の氏名（Ref[従業員情報].〔氏名〕）を取得する。<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 担当施設品目の氏名を設定する。（Ref[従業員情報].〔氏名〕）<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 *
	 * @param jgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 担当施設品目の職種名（Ref[従業員情報].〔職種名〕）を取得する。<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 *
	 * @return 氏名（Ref[従業員情報].〔職種名〕）
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 担当施設品目の職種名を設定する。（Ref[従業員情報].〔職種名〕）<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 *
	 * @param shokushuName 氏名（Ref[従業員情報].〔職種名〕）
	 */
	public void setShokushuName(String shokushuName) {
		this.shokushuName = shokushuName;
	}

	/**
	 * （COM領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getComJgiNo() {
		return comJgiNo;
	}

	/**
	 * （COM領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param comJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setComJgiNo(Integer comJgiNo) {
		this.comJgiNo = comJgiNo;
	}

	/**
	 * （COM領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getComJgiName() {
		return comJgiName;
	}

	/**
	 * （COM領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param comJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setComJgiName(String comJgiName) {
		this.comJgiName = comJgiName;
	}

	/**
	 * （COM領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
	 *
	 * @return 職種名（Ref[従業員情報].〔職種名〕）
	 */
	public String getComShokushuName() {
		return comShokushuName;
	}

	/**
	 * （COM領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
	 *
	 * @param comShokushuName 職種名（Ref[従業員情報].〔職種名〕）
	 */
	public void setComShokushuName(String comShokushuName) {
		this.comShokushuName = comShokushuName;
	}

	/**
	 * （CVM領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getCvmJgiNo() {
		return cvmJgiNo;
	}

	/**
	 * （CVM領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param cvmJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setCvmJgiNo(Integer cvmJgiNo) {
		this.cvmJgiNo = cvmJgiNo;
	}

	/**
	 * （CVM領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getCvmJgiName() {
		return cvmJgiName;
	}

	/**
	 * （CVM領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param cvmJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setCvmJgiName(String cvmJgiName) {
		this.cvmJgiName = cvmJgiName;
	}

	/**
	 * （CVM領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
	 *
	 * @return 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getCvmShokushuName() {
		return cvmShokushuName;
	}

	/**
	 * （CVM領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
	 *
	 * @param cvmShokushuName 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setCvmShokushuName(String cvmShokushuName) {
		this.cvmShokushuName = cvmShokushuName;
	}

	/**
	 * （RS領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getRsJgiNo() {
		return rsJgiNo;
	}

	/**
	 * （RS領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param rsJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setRsJgiNo(Integer rsJgiNo) {
		this.rsJgiNo = rsJgiNo;
	}

	/**
	 * （RS領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getRsJgiName() {
		return rsJgiName;
	}

	/**
	 * （RS領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param rsJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setRsJgiName(String rsJgiName) {
		this.rsJgiName = rsJgiName;
	}

	/**
	 * （RS領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
	 *
	 * @return 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getRsShokushuName() {
		return rsShokushuName;
	}

	/**
	 * （RS領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
	 *
	 * @param rsShokushuName 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setRsShokushuName(String rsShokushuName) {
		this.rsShokushuName = rsShokushuName;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * （SP領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getSpJgiNo() {
		return spJgiNo;
	}

	/**
	 * （SP領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param spJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setSpJgiNo(Integer spJgiNo) {
		this.spJgiNo = spJgiNo;
	}

	/**
	 * （SP領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getSpJgiName() {
		return spJgiName;
	}

	/**
	 * （SP領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param spJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setSpJgiName(String spJgiName) {
		this.spJgiName = spJgiName;
	}

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * （SP領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
	 *
	 * @return 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getSpShokushuName() {
		return spShokushuName;
	}

	/**
	 * （SP領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
	 *
	 * @param spShokushuName 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setSpShokushuName(String spShokushuName) {
		this.spShokushuName = spShokushuName;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * （ONC領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getOncJgiNo() {
		return oncJgiNo;
	}

	/**
	 * （ONC領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param oncJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setOncJgiNo(Integer oncJgiNo) {
		this.oncJgiNo = oncJgiNo;
	}

	/**
	 * （ONC領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getOncJgiName() {
		return oncJgiName;
	}

	/**
	 * （ONC領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param oncJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setOncJgiName(String oncJgiName) {
		this.oncJgiName = oncJgiName;
	}

	/**
	 * （ONC領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
	 *
	 * @return 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getOncShokushuName() {
		return oncShokushuName;
	}

	/**
	 * （ONC領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
	 *
	 * @param oncShokushuName 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public void setOncShokushuName(String oncShokushuName) {
		this.oncShokushuName = oncShokushuName;
	}

// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
	/**
	 * （固形腫瘍領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated
	public Integer getKokeiJgiNo() {
		return kokeiJgiNo;
	}

	/**
	 * （固形腫瘍領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param kokeiJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	@Deprecated
	public void setKokeiJgiNo(Integer kokeiJgiNo) {
		this.kokeiJgiNo = kokeiJgiNo;
	}

	/**
	 * （固形腫瘍領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated
	public String getKokeiJgiName() {
		return kokeiJgiName;
	}

	/**
	 * （固形腫瘍領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param kokeiJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	@Deprecated
	public void setKokeiJgiName(String kokeiJgiName) {
		this.kokeiJgiName = kokeiJgiName;
	}

	/**
	 * （固形腫瘍領域）職種名（Ref[従業員情報].〔職種名〕）を取得する。
	 *
	 * @return 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated
	public String getKokeiShokushuName() {
		return kokeiShokushuName;
	}

	/**
	 * （固形腫瘍領域）職種名を設定する。（Ref[従業員情報].〔職種名〕）
	 *
	 * @param kokeiShokushuName 職種名（Ref[従業員情報].〔職種名〕）
	 */
	@Deprecated
	public void setKokeiShokushuName(String kokeiShokushuName) {
		this.kokeiShokushuName = kokeiShokushuName;
	}
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）

	/**
	 * （ワクチン）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getVacJgiNo() {
		return vacJgiNo;
	}

	/**
	 * （ワクチン）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param vacJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setVacJgiNo(Integer vacJgiNo) {
		this.vacJgiNo = vacJgiNo;
	}

	/**
	 * （ワクチン）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getVacJgiName() {
		return vacJgiName;
	}

	/**
	 * （ワクチン）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param vacJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setVacJgiName(String vacJgiName) {
		this.vacJgiName = vacJgiName;
	}

	//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
	/**
	 * （BU2CS領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getBu2csMrJgiNo() {
		return bu2csMrJgiNo;
	}

	/**
	 * （BU2CS領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param bu2csMrJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setBu2csMrJgiNo(Integer bu2csMrJgiNo) {
		this.bu2csMrJgiNo = bu2csMrJgiNo;
	}

	/**
	 * （BU2CS領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getBu2csMrJgiName() {
		return bu2csMrJgiName;
	}

	/**
	 * （BU2CS領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param bu2csMrJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setBu2csMrJgiName(String bu2csMrJgiName) {
		this.bu2csMrJgiName = bu2csMrJgiName;
	}

	/**
	 * （JOHLC領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getJohlcMrJgiNo() {
		return johlcMrJgiNo;
	}

	/**
	 * （JOHLC領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param johlcMrJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setJohlcMrJgiNo(Integer johlcMrJgiNo) {
		this.johlcMrJgiNo = johlcMrJgiNo;
	}

	/**
	 * （JOHLC領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getJohlcMrJgiName() {
		return johlcMrJgiName;
	}

	/**
	 * （JOHLC領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param johlcMrJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setJohlcMrJgiName(String johlcMrJgiName) {
		this.johlcMrJgiName = johlcMrJgiName;
	}

	/**
	 * （BU2BLO領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getBu2bloMrJgiNo() {
		return bu2bloMrJgiNo;
	}

	/**
	 * （BU2BLO領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param bu2bloMrJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setBu2bloMrJgiNo(Integer bu2bloMrJgiNo) {
		this.bu2bloMrJgiNo = bu2bloMrJgiNo;
	}

	/**
	 * （BU2BLO領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getBu2bloMrJgiName() {
		return bu2bloMrJgiName;
	}

	/**
	 * （BU2BLO領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param bu2bloMrJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setBu2bloMrJgiName(String bu2bloMrJgiName) {
		this.bu2bloMrJgiName = bu2bloMrJgiName;
	}

	/**
	 * （BU2HER領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getBu2herMrJgiNo() {
		return bu2herMrJgiNo;
	}

	/**
	 * （BU2HER領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param bu2herMrJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setBu2herMrJgiNo(Integer bu2herMrJgiNo) {
		this.bu2herMrJgiNo = bu2herMrJgiNo;
	}

	/**
	 * （BU2HER領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getBu2herMrJgiName() {
		return bu2herMrJgiName;
	}

	/**
	 * （BU2HER領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param bu2herMrJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setBu2herMrJgiName(String bu2herMrJgiName) {
		this.bu2herMrJgiName = bu2herMrJgiName;
	}

	/**
	 * （JOGUH領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getJoguhMrJgiNo() {
		return joguhMrJgiNo;
	}

	/**
	 * （JOGUH領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param joguhMrJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setJoguhMrJgiNo(Integer joguhMrJgiNo) {
		this.joguhMrJgiNo = joguhMrJgiNo;
	}

	/**
	 * （JOGUH領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getJoguhMrJgiName() {
		return joguhMrJgiName;
	}

	/**
	 * （JOGUH領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param joguhMrJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setJoguhMrJgiName(String joguhMrJgiName) {
		this.joguhMrJgiName = joguhMrJgiName;
	}

	/**
	 * （BU1LAM領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を取得する。
	 *
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getBu1lamMrJgiNo() {
		return bu1lamMrJgiNo;
	}

	/**
	 * （BU1LAM領域）従業員番号（Ref[従業員情報].〔従業員番号〕）を設定する。
	 *
	 * @param bu1lamMrJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setBu1lamMrJgiNo(Integer bu1lamMrJgiNo) {
		this.bu1lamMrJgiNo = bu1lamMrJgiNo;
	}

	/**
	 * （BU1LAM領域）氏名（Ref[従業員情報].〔氏名〕）を取得する。
	 *
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getBu1lamMrJgiName() {
		return bu1lamMrJgiName;
	}

	/**
	 * （BU1LAM領域）氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 *
	 * @param bu1lamMrJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void setBu1lamMrJgiName(String bu1lamMrJgiName) {
		this.bu1lamMrJgiName = bu1lamMrJgiName;
	}
	//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更

	/**
	 * （ワクチン）職種名（Ref[従業員情報].〔職種名〕）を取得する。
	 *
	 * @return 職種名（Ref[従業員情報].〔職種名〕）
	 */
	public String getVacShokushuName() {
		return vacShokushuName;
	}

	/**
	 * （ワクチン）職種名を設定する。（Ref[従業員情報].〔職種名〕）
	 *
	 * @param vacShokushuName 職種名（Ref[従業員情報].〔職種名〕）
	 */
	public void setVacShokushuName(String vacShokushuName) {
		this.vacShokushuName = vacShokushuName;
	}

// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
	/**
	 * List用施設担当者-従業員番号を取得する。（Ref[従業員情報].〔従業員番号〕）
	 * @return 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public Integer getListJgiNo() {
		return listJgiNo;
	};

	/**
	 * List用施設担当者-従業員番号を設定する。（Ref[従業員情報].〔従業員番号〕）
	 * @param listJgiNo 従業員番号（Ref[従業員情報].〔従業員番号〕）
	 */
	public void setListJgiNo(Integer listJgiNo) {
		this.listJgiNo = listJgiNo;
	};

	/**
	 * List用施設担当者-氏名を取得する。（Ref[従業員情報].〔氏名〕）
	 * @return 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public String getListJgiName() {
		return listJgiName;
	};

	/**
	 * List用施設担当者-氏名を設定する。（Ref[従業員情報].〔氏名〕）
	 * @param listJgiName 氏名（Ref[従業員情報].〔氏名〕）
	 */
	public void  setListJgiName(String listJgiName) {
		this.listJgiName = listJgiName;
	};

	/**
	 * List用施設担当者-職種名を取得する。（Ref[従業員情報].〔職種名〕）
	 *@return 職種名（Ref[従業員情報].〔職種名〕
	 */
	public String getListShokushuName() {
		return listShokushuName;
	};

	/**
	 * List用施設担当者-職種名を取得する。（Ref[従業員情報].〔職種名〕）
	 * @param listShokushuName 職種名（Ref[従業員情報].〔職種名〕
	 */
	public void  setListShokushuName(String listShokushuName) {
		this.listShokushuName = listShokushuName;
	};

	/**
	 * MR種別
	 * @return MR種別
	 */
	public String getListMrCat() {
		return listMrCat;
	};

	/**
	 * MR種別
	 * @param mrCat MR種別
	 */
	public void  setListMrCat(String listMrCat) {
		this.listMrCat = listMrCat;
	};

	/**
	 *従業員区分
	 *@return 従業員区分
	 */
	public String getListJgiKb() {
		return listJgiKb;
	};

	/**
	 * 従業員区分
	 * @param jgiKb 従業員区分
	 */
	public void  setListJgiKb(String listJgiKb) {
		this.listJgiKb = listJgiKb;
	}
// add end 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

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
	 * @param fukenMeiKn 府県名（漢字）
	 */
	public void setFukenMeiKj(String fukenMeiKj) {
		this.fukenMeiKj = fukenMeiKj;
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
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	/**
	 * 品目施設情報を取得する。
	 *
	 * @return 品目施設情報
	 */
	public ProdInsInfoKanri getProdIns() {
		if (prodIns == null) {
			prodIns = new ProdInsInfoKanri();
		}
		return prodIns;
	}

	/**
	 * 品目施設情報を設定する。
	 *
	 * @param prodIns 品目施設情報
	 */
	public void setProdIns(ProdInsInfoKanri prodIns) {
		this.prodIns = prodIns;
	}

	/**
	 * 配分除外フラグを取得する。
	 *
	 * @return 配分除外フラグ
	 */
	public Boolean getExceptDistFlg() {
		return exceptDistFlg;
	}

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * SYU0003の担当者のリストを職種SEQ順で取得する。
	 *   ※暫定的に手動で実装。ゆくゆくはSQL等から修正の方向で。。。
	 *   ※基本的に個別のgetOnc～やgetSp～は使わずに（将来的に削除の方針）、このリストを利用する方針。
	 *      但し⇒のものは利用可（将来的にも削除しない）の方針。　getJgi～（引数指定のユーザ（≒ログインユーザorポップアップ指定等））、getCom～（MR）、getVac～（ワクチン）　
	 * @return SYU0003の担当者のリスト
	 */
	public List<JgiMst> getTantoList(){
//del Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
//
//		List<JgiMst> tantoList = new ArrayList<JgiMst>();
//
//		if(getComJgiNo() != null && getComJgiNo() != 0){
//			JgiMst com = new JgiMst();
//			com.setJgiNo(getComJgiNo());
//			com.setJgiName(getComJgiName());
//			com.setShokushuName(getComShokushuName());
//			tantoList.add(com);
//		}
//
//		if(getSpJgiNo() != null && getSpJgiNo() != 0){
//			JgiMst sp = new JgiMst();
//			sp.setJgiNo(getSpJgiNo());
//			sp.setJgiName(getSpJgiName());
//			sp.setShokushuName(getSpShokushuName());
//			tantoList.add(sp);
//		}
//
//		if(getOncJgiNo() != null && getOncJgiNo() != 0){
//			JgiMst onc = new JgiMst();
//			onc.setJgiNo(getOncJgiNo());
//			onc.setJgiName(getOncJgiName());
//			onc.setShokushuName(getOncShokushuName());
//			tantoList.add(onc);
//		}
//
// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
//		if(getKokeiJgiNo() != null && getKokeiJgiNo() != 0){
//			JgiMst kokei = new JgiMst();
//			kokei.setJgiNo(getKokeiJgiNo());
//			kokei.setJgiName(getKokeiJgiName());
//			kokei.setShokushuName(getKokeiShokushuName());
//			tantoList.add(kokei);
//		}
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
//
//del End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

		return tantoList;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

//add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
	public void setTantoList(List<JgiMst> tantoList) {
		this.tantoList = tantoList;
	}
//add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

	/**
	 * 配分除外フラグを設定する。
	 *
	 * @param exceptDistFlg 配分除外フラグ
	 */
	public void setExceptDistFlg(Boolean exceptDistFlg) {
		this.exceptDistFlg = exceptDistFlg;
	}

	@Override
	public int compareTo(DpsInsMst obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DpsInsMst.class.isAssignableFrom(entry.getClass())) {
			DpsInsMst obj = (DpsInsMst) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
