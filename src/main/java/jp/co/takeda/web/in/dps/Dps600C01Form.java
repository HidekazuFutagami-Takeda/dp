package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.DistParamResultDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps600C01((医)施設医師別計画配分基準編集画面)のフォームクラス
 * 
 * @author stakeuchi
 */
public class Dps600C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey Dps600C01_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps600C01Form.class, DistParamResultDto.class);

	/**
	 * 編集権限((医)施設医師別計画(自)配分処理)
	 */
	public static final DpAuthority DPS600C01_MMP_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

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
	 * @param 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
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
	 * 品目固定コードを設定する。
	 * 
	 * @param 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	@Override
	public void reset() {
	}

	// -------------------------------
	// convert
	// -------------------------------

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
	}
}
