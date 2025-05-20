package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.SupInfoDto;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps940C01(補足情報)のフォームクラス（
 *
 * @author futagami
 * 「④No.6 一括確定のエラー表示対応」対応前のDps940C00Formの内容をコピーして作成
 */
public class Dps940C01Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * DPS940C00F01_DATA_R 補足情報
	 */
	public static final BoxKey DPS940C00F01_DATA_R = new BoxKeyPerClassImpl(Dps940C01Form.class, SupInfoDto.class);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * カテゴリ
	 */
	private String category;

	// -------------------------------
	// getter/setter
	// -------------------------------
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
	 * @param sosCd
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理のため、不要
	}
}
