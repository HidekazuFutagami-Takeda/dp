package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.DeliveryResultVacMrListDto;
import jp.co.takeda.dto.DeliveryResultVacMrScDto;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dps913C02((ワ)過去実績参照画面(担当者別))のフォームクラス
 * 
 * @author siwamoto
 */
public class Dps913C02Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS913C02_DATA_R
	 */
	public static final BoxKey DPS913C02_DATA_R = new BoxKeyPerClassImpl(Dps913C02Form.class, DeliveryResultVacMrListDto.class);

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
	 * 組織コード(特約店G)
	 */
	private String sosCd3;

	/**
	 * 表示品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 表示期間
	 */
	private String page;

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
	 * 組織コード(特約店G)を設定する。
	 * 
	 * @param sosCd3 組織コード(特約店G)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(特約店G)を取得する。
	 * 
	 * @return 組織コード(特約店G)
	 */
	public String getSosCd3() {
		return sosCd3;
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
	 * 表示期間を取得する。
	 * 
	 * @return 表示期間
	 */
	public String getPage() {
		return page;
	}

	/**
	 * 表示期間を設定する。
	 * 
	 * @param page 表示期間
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * ActionForm⇒条件Dto 変換処理
	 * 
	 * @return 変換された条件Dto
	 * 
	 */
	public DeliveryResultVacMrScDto convertDeliveryResultMrScDto() throws Exception {
		if (StringUtils.isEmpty(this.prodCode)) {
			this.prodCode = null;
		}
		if (StringUtils.isEmpty(this.sosCd2)) {
			this.sosCd2 = null;
		}
		if (StringUtils.isEmpty(this.sosCd3)) {
			this.sosCd3 = null;
		}
		return new DeliveryResultVacMrScDto(this.prodCode, this.sosCd2, this.sosCd3);
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
