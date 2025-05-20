package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.dao.div.SearchInsType;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

/**
 * 施設情報の検索条件(Search Condition)を表すDTO
 *
 * @author khashimoto
 */
public class InsMstScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード（営業所/エリア特約店G）
	 */
	private final String sosCd3;

	/**
	 * 組織コード（チーム）
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 検索用対象区分
	 */
	private final SearchInsType searchInsType;

	/**
	 * 施設名(全角)
	 */
	private final String insFormalName;

	/**
	 * 施設名(半角カナ)
	 */
	private final String insKanaSrch;

	/**
	 * 活動区分
	 */
	private final ActivityType activityType;

	/**
	 * 府県コード
	 */
	private final Prefecture addrCodePref;

	/**
	 * 市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 雑担当フラグ
	 */
	private final Boolean etcSosFlg;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 整形フラグ
	 */
	private final Boolean includeSeikei;

   /**
     * 親子区分
     */
    private String oyakoKb;

    /**
     * 親子区分2
     */
    private String oyakoKb2;

    /**
     * 親子区分品目コード
     */
    private String oyakoKbProdCode;


	/**
	 * コンストラクタ
	 *
	 * @param sosCd3 組織コード（営業所）
	 * @param sosCd4 組織コード（チーム）
	 * @param jgiNo 従業員番号
	 * @param searchInsType 検索用対象区分
	 * @param insFormalName 施設名（全角）
	 * @param insKanaSrch 施設名（半角カナ）
	 * @param jtnFlg 重点先フラグ
	 * @param prodCode 品目コード
	 */
	public InsMstScDto(String sosCd3, String sosCd4, Integer jgiNo, SearchInsType searchInsType, String insFormalName, String insKanaSrch, boolean jtnFlg, String prodCode, Boolean includeSeikei) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.searchInsType = searchInsType;
		this.insFormalName = insFormalName;
		this.insKanaSrch = insKanaSrch;
		if (jtnFlg) {
			this.activityType = ActivityType.JTN;
		} else {
			this.activityType = null;
		}
		this.addrCodePref = null;
		this.addrCodeCity = null;
		this.etcSosFlg = null;
		this.prodCode = prodCode;
		this.includeSeikei = includeSeikei;
	}

	/**
	 * コンストラクタ
	 *
	 * @param sosCd3 組織コード（営業所）
	 * @param sosCd4 組織コード（チーム）
	 * @param jgiNo 従業員番号
	 * @param searchInsType 検索用対象区分
	 * @param insFormalName 施設名（全角）
	 * @param insKanaSrch 施設名（半角カナ）
	 * @param jtnFlg 重点先フラグ
	 * @param ippanFlg 一般先フラグ
	 * @param addrCodePref 府県コード
	 * @param addrCodeCity 市区町村コード
	 * @param prodCode  品目コード
	 */
	public InsMstScDto(String sosCd3, String sosCd4, Integer jgiNo, SearchInsType searchInsType, String insFormalName, String insKanaSrch, boolean jtnFlg, boolean ippanFlg,
		String addrCodePref, String addrCodeCity, Boolean etcSosFlg, String prodCode) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.searchInsType = searchInsType;
		this.insFormalName = insFormalName;
		this.insKanaSrch = insKanaSrch;
		if (jtnFlg) {
			this.activityType = ActivityType.JTN;
		} else if (ippanFlg) {
			this.activityType = ActivityType.IPPAN;
		} else {
			this.activityType = null;
		}
		this.addrCodePref = Prefecture.getInstance(addrCodePref);
		this.addrCodeCity = addrCodeCity;
		this.etcSosFlg = etcSosFlg;
		this.prodCode = prodCode;
		this.includeSeikei = false;
	}

	/**
	 * 組織コード（営業所）を取得する。
	 *
	 * @return 組織コード（営業所）
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード（チーム）を取得する。
	 *
	 * @return 組織コード（チーム）
	 */
	public String getSosCd4() {
		return sosCd4;
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
	 * 検索用対象区分を取得する。
	 *
	 * @return 検索用対象区分
	 */
	public SearchInsType getSearchInsType() {
		return searchInsType;
	}

	/**
	 * 施設名（全角）を取得する。
	 *
	 * @return 施設名（全角）
	 */
	public String getInsFormalName() {
		return insFormalName;
	}

	/**
	 * 施設名（半角カナ）を取得する。
	 *
	 * @return 施設名（半角カナ）
	 */
	public String getInsKanaSrch() {
		return insKanaSrch;
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
	 * 府県コードを取得する。
	 *
	 * @return 府県コード
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
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
	 * 雑担当フラグを取得する。
	 *
	 * @return 雑担当フラグ
	 */
	public Boolean getEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * 品目コードを取得する。
	 *
	 * @return 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 整形を含めるかを取得する。
	 *
	 * @return true：含める、false：含めない
	 */
	public Boolean isIncludeSeikei() {
		return includeSeikei;
	}

	/**
     * 親子区分を取得する。
     * @return oyakoKb
     */
    public String getOyakoKb() {
        return oyakoKb;
    }

    /**
     * 親子区分を設定する。
     * @param oyakoKb
     */
    public void setOyakoKb(String oyakoKb) {
        this.oyakoKb = oyakoKb;
    }

	/**
     * 親子区分2を取得する。
     * @return oyakoKb
     */
    public String getOyakoKb2() {
        return oyakoKb2;
    }
    /**
     * 親子区分2を設定する。
     * @param oyakoKb
     */
	public void setOyakoKb2(String oyakoKb2) {
        this.oyakoKb2 = oyakoKb2;
	}

	/**
	* 親子区分1が'ZZZZZ'の時、セットする親子区分1（品目コード）を取得する。
	* @return oyakoKbProdCode
	*/
	public String getOyakoKbProdCode() {
        return oyakoKbProdCode;
	}
	/**
	* 親子区分1が'ZZZZZ'の時、セットする親子区分1（品目コード）を設定する。
	* @param oyakoKbProdCode
	*/
	public void setOyakoKbProdCode(String oyakoKbProdCode) {
        this.oyakoKbProdCode = oyakoKbProdCode;
	}

    @Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
