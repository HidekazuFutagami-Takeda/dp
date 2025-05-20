package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.ProdInsInfoKanri;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.ProdInsInfoErrKbn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設情報の検索結果用DTO
 *
 * @author khashimoto
 */
public class InsMstResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設名
	 */
	private final String insName;

	/**
	 * 対象
	 */
	private final String insType;

	/**
	 * 施設分類
	 */
	private final String insClass;

	/**
	 * （MR-施設-品目）担当者<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 */
	private final String jgiName;

	/**
	 * （MR-施設-品目）職種名<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 */
	private final String shokushuName;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * （MR-施設-品目）従業員番号<br>
	 * 従業員番号、MR種別、または、品目を指定した場合は設定される
	 */
	private final Integer jgiNo;

	/**
	 * 削除フラグ
	 */
	private final Boolean delFlg;

	/**
	 * 配分除外フラグ（医薬のみ）
	 */
	private final Boolean exceptDistFlg;

	/**
	 * JIS府県コード
	 */
	private final String addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 府県名（漢字）
	 */
	private final String fukenMeiKj;

	/**
	 * 市区郡町村名（漢字）
	 */
	private final String shikuchosonMeiKj;

	/**
	 * 活動区分（ワクチンのみ使用）
	 */
	private final ActivityType activityType;

	/**
	 * モール施設かを示すフラグ
	 */
	private final Boolean mallFlg;

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
	 * （MR-施設）COM 従業員番号
	 */
	private final Integer comJgiNo;

	/**
	 * （MR-施設）COM 従業員名
	 */
	private final String comJgiName;

	/**
	 * （MR-施設）COM 職種名
	 */
	private final String comShokushuName;

	/**
	 * （MR-施設）CVM 従業員番号
	 */
	private final Integer cvmJgiNo;

	/**
	 * （MR-施設）CVM 従業員名
	 */
	private final String cvmJgiName;

	/**
	 * （MR-施設）CVM 職種名
	 */
	private final String cvmShokushuName;

	/**
	 * （MR-施設）RS 従業員番号
	 */
	private final Integer rsJgiNo;

	/**
	 * （MR-施設）RS 従業員名
	 */
	private final String rsJgiName;

	/**
	 * （MR-施設）RS 職種名
	 */
	private final String rsShokushuName;

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * （MR-施設）SPBU 従業員番号
	 */
	private final Integer spJgiNo;

	/**
	 * （MR-施設）SPBU 従業員名
	 */
	private final String spJgiName;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * （MR-施設）SPBU 職種名
	 */
	private String spShokushuName;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * （MR-施設）ONC 従業員番号
	 */
	private final Integer oncJgiNo;

	/**
	 * （MR-施設）ONC 従業員名
	 */
	private final String oncJgiName;

	/**
	 * （MR-施設）ONC 職種名
	 */
	private final String oncShokushuName;

// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
	/**
	 * （MR-施設）固形腫瘍 従業員番号
	 */
	private final Integer kokeiJgiNo;

	/**
	 * （MR-施設）固形腫瘍 従業員名
	 */
	private final String kokeiJgiName;

	/**
	 * （MR-施設）固形腫瘍 職種名
	 */
	private final String kokeiShokushuName;
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）

	/**
	 * （MR-施設）VAC 従業員番号
	 */
	private final Integer vacJgiNo;

	/**
	 * （MR-施設）VAC 従業員名
	 */
	private final String vacJgiName;

	/**
	 * （MR-施設）VAC 職種名
	 */
	private final String vacShokushuName;

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * （MR-施設）SYU0003の担当者のリスト　※職種SEQ順
	 *   ※基本的に個別のgetOnc～やgetSp～は使わずに（将来的に削除の方針）、このリストを利用する方針。
	 *      但し⇒のものは利用可（将来的にも削除しない）の方針。　getJgi～（引数指定のユーザ（≒ログインユーザorポップアップ指定等））、getCom～（MR）、getVac～（ワクチン）　
	 */
	private final List<JgiMst> tantoList;
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
	/**
	 * （MR-施設）BU2CS 従業員番号
	 */
	private final Integer bu2csMrJgiNo;

	/**
	 * （MR-施設）BU2CS 従業員名
	 */
	private final String bu2csMrJgiName;

	/**
	 * （MR-施設）JOHLC 従業員番号
	 */
	private final Integer johlcMrJgiNo;

	/**
	 * （MR-施設）JOHLC 従業員名
	 */
	private final String johlcMrJgiName;

	/**
	 * （MR-施設）BU2BLO 従業員番号
	 */
	private final Integer bu2bloMrJgiNo;

	/**
	 * （MR-施設）BU2BLO 従業員名
	 */
	private final String bu2bloMrJgiName;

	/**
	 * （MR-施設）BU2HER 従業員番号
	 */
	private final Integer bu2herMrJgiNo;

	/**
	 * （MR-施設）BU2HER 従業員名
	 */
	private final String bu2herMrJgiName;

	/**
	 * （MR-施設）JOGUH 従業員番号
	 */
	private final Integer joguhMrJgiNo;

	/**
	 * （MR-施設）JOGUH 従業員名
	 */
	private final String joguhMrJgiName;

	/**
	 * （MR-施設）BU1LAM 従業員番号
	 */
	private final Integer bu1lamMrJgiNo;

	/**
	 * （MR-施設）BU1LAM 従業員名
	 */
	private final String bu1lamMrJgiName;
//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更

	/**
	 *
	 * コンストラクタ
	 *
	 * @param insName 施設名
	 * @param insType 対象
	 * @param insClass 施設分類
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param jgiName 担当者
	 * @param shokushuName 職種名
	 * @param delFlg 削除フラグ
	 * @param addrCodePref JIS府県コード
	 * @param addrCodeCity JIS市区町村コード
	 * @param fukenMeiKj 府県名（漢字）
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 * @param activityType 活動区分
	 * @param mallFlg モール施設かを示すフラグ
	 * @param prodIns 品目施設情報
	 */
	public InsMstResultDto(String insName, String insType, String insClass, String insNo, Integer jgiNo, String jgiName, String shokushuName, Boolean delFlg, String addrCodePref, String addrCodeCity,
		String fukenMeiKj, String shikuchosonMeiKj, ActivityType activityType, Boolean mallFlg, ProdInsInfoKanri prodIns, Integer comJgiNo, String comJgiName,String comShokushuName,Integer cvmJgiNo,
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//		String cvmJgiName, String cvmShokushuName, Integer rsJgiNo, String rsJgiName, String rsShokushuName, Integer oncJgiNo, String oncJgiName, String oncShokushuName,
//		String cvmJgiName, String cvmShokushuName, Integer rsJgiNo, String rsJgiName, String rsShokushuName, Integer oncJgiNo, String oncJgiName, String oncShokushuName, Integer spJgiNo, String spJgiName,
// mod start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
//		String cvmJgiName, String cvmShokushuName, Integer rsJgiNo, String rsJgiName, String rsShokushuName, Integer oncJgiNo, String oncJgiName, String oncShokushuName, Integer spJgiNo, String spJgiName, String spShokushuName,
		String cvmJgiName, String cvmShokushuName, Integer rsJgiNo, String rsJgiName, String rsShokushuName, Integer oncJgiNo, String oncJgiName, String oncShokushuName, Integer kokeiJgiNo, String kokeiJgiName, String kokeiShokushuName, Integer spJgiNo, String spJgiName, String spShokushuName,
//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
		Integer bu2csMrJgiNo, String bu2csMrJgiName,
		Integer johlcMrJgiNo, String johlcMrJgiName,
		Integer bu2bloMrJgiNo, String bu2bloMrJgiName,
		Integer bu2herMrJgiNo, String bu2herMrJgiName,
		Integer joguhMrJgiNo, String joguhMrJgiName,
		Integer bu1lamMrJgiNo, String bu1lamMrJgiName,
//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更
// mod end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//		Integer vacJgiNo, String vacJgiName, String vacShokushuName, Boolean exceptDistFlg) {
		Integer vacJgiNo, String vacJgiName, String vacShokushuName, List<JgiMst> tantoList, Boolean exceptDistFlg) {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.insName = insName;
		this.insType = insType;
		this.insClass = insClass;
		this.insNo = insNo;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.shokushuName = shokushuName;
		this.delFlg = delFlg;
		this.addrCodePref = addrCodePref;
		this.addrCodeCity = addrCodeCity;
		this.fukenMeiKj = fukenMeiKj;
		this.shikuchosonMeiKj = shikuchosonMeiKj;
		this.activityType = activityType;
		this.mallFlg = mallFlg;
		this.insInfoName = prodIns.getInsInfoName();
		this.dispFontColCd = prodIns.getDispFontColCd();
		this.errFlg = ProdInsInfoErrKbn.ERROR == prodIns.getProdInsInfoErrKbn();
		this.alertFlg = ProdInsInfoErrKbn.ALERT == prodIns.getProdInsInfoErrKbn();
		this.comJgiNo = comJgiNo;
		this.comJgiName = comJgiName;
		this.comShokushuName = comShokushuName;
		this.cvmJgiNo = cvmJgiNo;
		this.cvmJgiName = cvmJgiName;
		this.cvmShokushuName = cvmShokushuName;
		this.rsJgiNo = rsJgiNo;
		this.rsJgiName = rsJgiName;
		this.rsShokushuName = rsShokushuName;
		this.oncJgiNo = oncJgiNo;
		this.oncJgiName = oncJgiName;
		this.oncShokushuName = oncShokushuName;
// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
		this.kokeiJgiNo = kokeiJgiNo;
		this.kokeiJgiName = kokeiJgiName;
		this.kokeiShokushuName = kokeiShokushuName;
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.spJgiNo = spJgiNo;
		this.spJgiName = spJgiName;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.spShokushuName = spShokushuName;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.vacJgiNo = vacJgiNo;
		this.vacJgiName = vacJgiName;
		this.vacShokushuName = vacShokushuName;
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.tantoList = tantoList;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.exceptDistFlg = exceptDistFlg;
//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
		this.bu2csMrJgiNo = bu2csMrJgiNo;
		this.bu2csMrJgiName = bu2csMrJgiName;
		this.johlcMrJgiNo = johlcMrJgiNo;
		this.johlcMrJgiName = johlcMrJgiName;
		this.bu2bloMrJgiNo = bu2bloMrJgiNo;
		this.bu2bloMrJgiName = bu2bloMrJgiName;
		this.bu2herMrJgiNo = bu2herMrJgiNo;
		this.bu2herMrJgiName = bu2herMrJgiName;
		this.joguhMrJgiNo = joguhMrJgiNo;
		this.joguhMrJgiName = joguhMrJgiName;
		this.bu1lamMrJgiNo = bu1lamMrJgiNo;
		this.bu1lamMrJgiName = bu1lamMrJgiName;
//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更
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
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 担当者を取得する。
	 *
	 * @return jgiName 担当者
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 職種名を取得する
	 *
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 削除フラグを取得する。
	 *
	 * @return delFlg 削除フラグ
	 */
	public Boolean getDelFlg() {
		return delFlg;
	}

	/**
	 * 配分除外フラグ（医薬のみ）。
	 *
	 * @return 配分除外フラグ（医薬のみ）
	 */
	public Boolean getExceptDistFlg() {
		return exceptDistFlg;
	}

	/**
	 * JIS府県コードを取得する。
	 *
	 * @return addrCodePref JIS府県コード
	 */
	public String getAddrCodePref() {
		return addrCodePref;
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
	 * 府県名（漢字）を取得する。
	 *
	 * @return 府県名（漢字）
	 */
	public String getFukenMeiKj() {
		return fukenMeiKj;
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
	 * 活動区分を取得する。
	 *
	 * @return 活動区分
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * モール施設かを示すフラグを取得する。
	 *
	 * @return モール施設かを示すフラグ
	 */
	public Boolean getMallFlg() {
		return mallFlg;
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

	/**
	 * （MR-施設）COM 従業員番号 を取得する。
	 *
	 * @return （MR-施設）COM 従業員番号
	 */
	public Integer getComJgiNo() {
		return comJgiNo;
	}

	/**
	 * （MR-施設）COM 従業員名 を取得する。
	 *
	 * @return （MR-施設）COM 従業員名
	 */
	public String getComJgiName() {
		return comJgiName;
	}

	/**
	 * （MR-施設）COM 職種名 を取得する。
	 *
	 * @return （MR-施設）COM 職種名
	 */
	public String getComShokushuName() {
		return comShokushuName;
	}

	/**
	 * （MR-施設）CVM 従業員番号 を取得する。
	 *
	 * @return （MR-施設）CVM 従業員番号
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getCvmJgiNo() {
		return cvmJgiNo;
	}

	/**
	 * （MR-施設）CVM 従業員名 を取得する。
	 *
	 * @return （MR-施設）CVM 従業員名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getCvmJgiName() {
		return cvmJgiName;
	}

	/**
	 * （MR-施設）CVM 職種名 を取得する。
	 *
	 * @return （MR-施設）CVM 職種名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getCvmShokushuName() {
		return cvmShokushuName;
	}

	/**
	 * （MR-施設）RS 従業員番号 を取得する。
	 *
	 * @return （MR-施設）RS 従業員番号
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getRsJgiNo() {
		return rsJgiNo;
	}

	/**
	 * （MR-施設）RS 従業員名 を取得する。
	 *
	 * @return （MR-施設）RS 従業員名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getRsJgiName() {
		return rsJgiName;
	}

	/**
	 * （MR-施設）RS 職種名 を取得する。
	 *
	 * @return （MR-施設）RS 職種名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getRsShokushuName() {
		return rsShokushuName;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * （MR-施設）SPBU 従業員番号 を取得する。
	 *
	 * @return （MR-施設）SPBU 従業員番号
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getSpJgiNo() {
		return spJgiNo;
	}

	/**
	 * （MR-施設）SPBU 従業員名 を取得する。
	 *
	 * @return （MR-施設）SPBU 従業員名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getSpJgiName() {
		return spJgiName;
	}
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * （MR-施設）SPBU 職種名 を取得する。
	 *
	 * @return （MR-施設）SPBU 職種名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getSpShokushuName() {
		return spShokushuName;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * （MR-施設）ONC 従業員番号 を取得する。
	 *
	 * @return （MR-施設）ONC 従業員番号
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public Integer getOncJgiNo() {
		return oncJgiNo;
	}

	/**
	 * （MR-施設）ONC 従業員名 を取得する。
	 *
	 * @return （MR-施設）ONC 従業員名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getOncJgiName() {
		return oncJgiName;
	}

	/**
	 * （MR-施設）ONC 職種名 を取得する。
	 *
	 * @return （MR-施設）ONC 職種名
	 */
	@Deprecated //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	public String getOncShokushuName() {
		return oncShokushuName;
	}

// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
	/**
	 * （MR-施設）固形腫瘍 従業員番号 を取得する。
	 *
	 * @return （MR-施設）固形腫瘍 従業員番号
	 */
	@Deprecated
	public Integer getKokeiJgiNo() {
		return kokeiJgiNo;
	}

	/**
	 * （MR-施設）固形腫瘍 従業員名 を取得する。
	 *
	 * @return （MR-施設）固形腫瘍 従業員名
	 */
	@Deprecated
	public String getKokeiJgiName() {
		return kokeiJgiName;
	}

	/**
	 * （MR-施設）固形腫瘍 職種名 を取得する。
	 *
	 * @return （MR-施設）固形腫瘍 職種名
	 */
	@Deprecated
	public String getKokeiShokushuName() {
		return kokeiShokushuName;
	}
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）

	/**
	 * （MR-施設）VAC 従業員番号 を取得する。
	 *
	 * @return （MR-施設）VAC 従業員番号
	 */
	public Integer getVacJgiNo() {
		return vacJgiNo;
	}

	/**
	 * （MR-施設）VAC 従業員名 を取得する。
	 *
	 * @return （MR-施設）VAC 従業員名
	 */
	public String getVacJgiName() {
		return vacJgiName;
	}

	/**
	 * （MR-施設）VAC 職種名 を取得する。
	 *
	 * @return （MR-施設）VAC 職種名
	 */
	public String getVacShokushuName() {
		return vacShokushuName;
	}

//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
	/**
	 * （MR-施設）BU2CS 従業員番号 を取得する。
	 *
	 * @return （MR-施設）BU2CS 従業員番号
	 */
	public Integer getBu2csMrJgiNo() {
		return bu2csMrJgiNo;
	}

	/**
	 * （MR-施設）BU2CS 従業員名 を取得する。
	 *
	 * @return （MR-施設）BU2CS 従業員名
	 */
	public String getBu2csMrJgiName() {
		return bu2csMrJgiName;
	}

	/**
	 * （MR-施設）JOHLC 従業員番号 を取得する。
	 *
	 * @return （MR-施設）JOHLC 従業員番号
	 */
	public Integer getJohlcMrJgiNo() {
		return johlcMrJgiNo;
	}

	/**
	 * （MR-施設）JOHLC 従業員名 を取得する。
	 *
	 * @return （MR-施設）JOHLC 従業員名
	 */
	public String getJohlcMrJgiName() {
		return johlcMrJgiName;
	}

	/**
	 * （MR-施設）BU2BLO 従業員番号 を取得する。
	 *
	 * @return （MR-施設）BU2BLO 従業員番号
	 */
	public Integer getBu2bloMrJgiNo() {
		return bu2bloMrJgiNo;
	}

	/**
	 * （MR-施設）BU2BLO 従業員名 を取得する。
	 *
	 * @return （MR-施設）BU2BLO 従業員名
	 */
	public String getBu2bloMrJgiName() {
		return bu2bloMrJgiName;
	}

	/**
	 * （MR-施設）BU2HER 従業員番号 を取得する。
	 *
	 * @return （MR-施設）BU2HER 従業員番号
	 */
	public Integer getBu2herMrJgiNo() {
		return bu2herMrJgiNo;
	}

	/**
	 * （MR-施設）BU2HER 従業員名 を取得する。
	 *
	 * @return （MR-施設）BU2HER 従業員名
	 */
	public String getBu2herMrJgiName() {
		return bu2herMrJgiName;
	}

	/**
	 * （MR-施設）JOGUH 従業員番号 を取得する。
	 *
	 * @return （MR-施設）JOGUH 従業員番号
	 */
	public Integer getJoguhMrJgiNo() {
		return joguhMrJgiNo;
	}

	/**
	 * （MR-施設）JOGUH 従業員名 を取得する。
	 *
	 * @return （MR-施設）JOGUH 従業員名
	 */
	public String getJoguhMrJgiName() {
		return joguhMrJgiName;
	}

	/**
	 * （MR-施設）BU1LAM 従業員番号 を取得する。
	 *
	 * @return （MR-施設）BU1LAM 従業員番号
	 */
	public Integer getBu1lamMrJgiNo() {
		return bu1lamMrJgiNo;
	}

	/**
	 * （MR-施設）BU1LAM 従業員名 を取得する。
	 *
	 * @return （MR-施設）BU1LAM 従業員名
	 */
	public String getBu1lamMrJgiName() {
		return bu1lamMrJgiName;
	}
//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * SYU0003の担当者のリストを職種SEQ順で取得する。
	 *   ※基本的に個別のgetOnc～やgetSp～は使わずに（将来的に削除の方針）、このリストを利用する方針。
	 *      但し⇒のものは利用可（将来的にも削除しない）の方針。　getJgi～（引数指定のユーザ（≒ログインユーザorポップアップ指定等））、getCom～（MR）、getVac～（ワクチン）　
	 * @return SYU0003の担当者のリスト
	 */
	public List<JgiMst> getTantoList(){
		return tantoList;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
