package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.DeliveryResultVacInsListDto;
import jp.co.takeda.dto.DeliveryResultVacInsScDto;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dps913C03((ワ)過去実績参照画面(施設特約店別))のフォームクラス
 * 
 * @author siwamoto
 */
public class Dps913C03Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS913C03_DATA_R
	 */
	public static final BoxKey DPS913C03_DATA_R = new BoxKeyPerClassImpl(Dps913C03Form.class, DeliveryResultVacInsListDto.class);

	/**
	 * ページ番号のデフォルト値
	 */
	public static final String DEFAULT_PAGE_NO = "4";

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 組織コード(特約店部)
	 */
	private String sosCd2;

	/**
	 * 表示品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * カレントページ数
	 */
	private String page;

	/**
	 * 従業員番号
	 */
	Integer jgiNo;

	/**
	 * 施設コード
	 */
	String insNo;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 * 
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 組織コード(特約店部)を取得する。
	 * 
	 * @return 組織コード(特約店部)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(特約店部)を設定する。
	 * 
	 * @param sosCd2 組織コード(特約店部)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	/**
	 * 表示品目カテゴリを取得する。
	 * 
	 * @return 表示品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 表示品目カテゴリを設定する。
	 * 
	 * @param prodCategory 表示品目カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	/**
	 * カレントページ数を取得する。
	 * 
	 * @return カレントページ数
	 */
	public String getPage() {
		return page;
	}

	/**
	 * カレントページ数を設定する。
	 * 
	 * @param page カレントページ数
	 */
	public void setPage(String page) {
		this.page = page;
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
	 * 従業員番号を設定する。
	 * 
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
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
	 * 施設コードを設定する。
	 * 
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * ActionForm⇒条件Dto 変換処理
	 * 
	 * @return 変換された条件Dto Integer jgiNo = 0; String insNo = ""; String category = ""; String prodCode = "";
	 */
	public DeliveryResultVacInsScDto convertDeliveryResultVacInsScDto() throws Exception {
		if (StringUtils.isEmpty(this.prodCode)) {
			this.prodCode = null;
		}
		if (StringUtils.isEmpty(this.insNo)) {
			this.insNo = null;
		}
		return new DeliveryResultVacInsScDto(this.jgiNo, this.prodCode, this.insNo);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		if (StringUtils.isBlank(this.page) || !ValidationUtil.isInteger(this.page)) {
			this.page = DEFAULT_PAGE_NO;
		}
	}
}
