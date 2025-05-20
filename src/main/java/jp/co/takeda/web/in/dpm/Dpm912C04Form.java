package jp.co.takeda.web.in.dpm;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenMstResultsListDto;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps912C04(特約店実績検索(本部・支社・支店)画面)のフォームクラス
 *
 * @author khashimoto
 */
public class Dpm912C04Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果のキー値
	 */
	public static final BoxKey TMS_LIST_KEY_R = new BoxKeyPerClassImpl(Dpm912C04Form.class, TmsTytenMstResultsListDto.class);

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return TmsTytenMstScDto 変換されたScDto
	 */
	public TmsTytenMstScDto convertTmsTytenMstScDto() throws Exception {
		// 本店名（半角カナ）
		if (StringUtils.isEmpty(hontenMeiKn)) {
			hontenMeiKn = null;
		}

		// mod 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		return new TmsTytenMstScDto(null, null, null, null, null, null, hontenMeiKn, null, null, null);
	}

	// -------------------------------
	// field
	// -------------------------------
	// INパラメータ
	/**
	 * 適用関数名
	 */
	private String tytenApplyFuncName;

	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 現在のページ番号
	 */
	private Integer crntPageNo4;

	/**
	 * 本店名（半角カナ）
	 */
	private String hontenMeiKn;

	// -------------------------------
	// getter & setter
	// -------------------------------

	/**
	 * 適用関数名を取得する。
	 *
	 * @return 適用関数名
	 */
	public String getTytenApplyFuncName() {
		return tytenApplyFuncName;
	}

	/**
	 * 適用関数名を設定する。
	 *
	 * @param tytenApplyFuncName 適用関数名
	 */
	public void setTytenApplyFuncName(String tytenApplyFuncName) {
		this.tytenApplyFuncName = tytenApplyFuncName;
	}

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
	 * 現在のページ番号を取得する。
	 *
	 * @return 現在のページ番号
	 */
	public Integer getCrntPageNo4() {
		return crntPageNo4;
	}

	/**
	 * 現在のページ番号を設定する。
	 *
	 * @param crntPageNo4 現在のページ番号
	 */
	public void setCrntPageNo4(Integer crntPageNo4) {
		this.crntPageNo4 = crntPageNo4;
	}

	/**
	 * 本店名（半角カナ）を取得する。
	 *
	 * @return 本店名（半角カナ）
	 */
	public String getHontenMeiKn() {
		return hontenMeiKn;
	}

	/**
	 * 本店名（半角カナ）を設定する。
	 *
	 * @param hontenMeiKn 本店名（半角カナ）
	 */
	public void setHontenMeiKn(String hontenMeiKn) {
		this.hontenMeiKn = hontenMeiKn;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		crntPageNo4 = 1;
		hontenMeiKn = "";
	}
}
