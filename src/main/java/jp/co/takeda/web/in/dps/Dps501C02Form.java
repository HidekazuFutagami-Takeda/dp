package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenPlanSlideForVacResultDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps501C02((ワ)特約店別計画スライド画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps501C02Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS501C02_DATA_R = new BoxKeyPerClassImpl(Dps501C02Form.class, TmsTytenPlanSlideForVacResultDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPS501C02_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 組織コード(支店)を取得する。
	 *
	 * @return 組織コード(支店)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(支店)を設定する。
	 *
	 * @param sosCd2 組織コード(支店)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
	}
}
