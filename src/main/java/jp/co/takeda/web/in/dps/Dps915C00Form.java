package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps915C00(組織・従業員検索画面_MR編集)のフォームクラス
 * 
 * @author hashidume
 */
public class Dps915C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織・従業員検索結果のキー値
	 */
	public static final BoxKey SOS_JGI_LIST_DTO_KEY_R = new BoxKeyPerClassImpl(Dps915C00Form.class, JgiMst.class);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 適用関数名
	 */
	private String sosApplyFuncName;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	// -------------------------------
	// getter & setter
	// -------------------------------

	/**
	 * 適用関数名を取得する。
	 * 
	 * @return 適用関数名
	 */
	public String getSosApplyFuncName() {
		return sosApplyFuncName;
	}

	/**
	 * 適用関数名を設定する。
	 * 
	 * @param sosApplyFuncName 適用関数名
	 */
	public void setSosApplyFuncName(String sosApplyFuncName) {
		this.sosApplyFuncName = sosApplyFuncName;
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return 従業員番号
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 * 
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理のため不要
	}
}
