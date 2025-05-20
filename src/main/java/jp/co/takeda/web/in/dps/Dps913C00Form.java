package jp.co.takeda.web.in.dps;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.DeliveryResultMrListDto;
import jp.co.takeda.dto.DeliveryResultMrScDto;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps913C00((医)過去実績参照画面(担当者別))のフォームクラス
 *
 * @author siwamoto
 */
public class Dps913C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS913C00_DATA_R
	 */
	public static final BoxKey DPS913C00_DATA_R = new BoxKeyPerClassImpl(Dps913C00Form.class, DeliveryResultMrListDto.class);

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
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 表示品目カテゴリ
	 */
	private String category;

	/**
	 * 表示期間
	 */
	private String page;

	/**
	 * ヘッダ（UH）
	 */
	private String headerUh;

	/**
	 * ヘッダ（P）
	 */
	private String headerP;

	/**
	 * グリッドの追加ヘッダ
	 */
	private String attachHeader;

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
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(チーム)を設定する。
	 *
	 * @param sosCd4 組織コード(チーム)
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return 表示品目カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
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
	public DeliveryResultMrScDto convertDeliveryResultMrScDto() throws Exception {
		if (StringUtils.isEmpty(this.prodCode)) {
			this.prodCode = null;
		}
		if (StringUtils.isEmpty(this.sosCd3)) {
			this.sosCd3 = null;
		}
		if (StringUtils.isEmpty(this.sosCd4)) {
			this.sosCd4 = null;
		}
		if (StringUtils.isEmpty(this.category)) {
			this.category = null;
		}
		return new DeliveryResultMrScDto(this.prodCode, this.sosCd3, this.sosCd4, this.category);
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

	/**
	 * ヘッダ(UH)を取得する。
	 *
	 * @return headerUh  ヘッダ(UH)
	 */
	public String getHeaderUh() {
		return headerUh;
	}

	/**
	 * ヘッダ(UH)を設定する。
	 *
	 * @param headerUh  ヘッダ(UH)
	 */
	public void setHeaderUh(String headerUh) {
		this.headerUh = headerUh;
	}

	/**
	 * ヘッダ(P)を取得する。
	 *
	 * @return headerP  ヘッダ(P)
	 */
	public String getHeaderP() {
		return headerP;
	}

	/**
	 * ヘッダ(P)を設定する。
	 *
	 * @param headerP  ヘッダ(P)
	 */
	public void setHeaderP(String headerP) {
		this.headerP = headerP;
	}

	/**
	 * グリッドの追加ヘッダを取得する。
	 *
	 * @return attachHeader1row  グリッド（集計）の追加ヘッダ（1行目）
	 */
	public String getAttachHeader() {
		return attachHeader;
	}

	/**
	 * グリッドの追加ヘッダを設定する。
	 *
	 * @param attachHeader1row  グリッド（集計）の追加ヘッダ（1行目）
	 */
	public void setAttachHeader(String attachHeader) {
		this.attachHeader = attachHeader;
	}
}
