package jp.co.takeda.web.cmn.action;

/**
 * ダイアログ画面向けDpActionFormクラス
 * 
 * @author tkawabata
 */
public abstract class DiaDpActionForm extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isDialogueFlg() {
		return true;
	}
}
