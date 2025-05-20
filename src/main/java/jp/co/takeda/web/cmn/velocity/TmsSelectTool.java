package jp.co.takeda.web.cmn.velocity;

import jp.co.takeda.bean.Constants;
import jp.co.takeda.logic.div.TmsSelectMode;

/**
 * 特約店選択ツール
 * 
 * @author tkawabata
 */
public class TmsSelectTool {

	/**
	 * 計画立案対象外表示用ラベル
	 * 計画立案対象外の特約店名の頭につけるラベル
	 * 
	 * @return ラベル文字列
	 */
	public static String getPlanTaiGaiLabel() {
		return Constants.PLAN_TAIGAI_LABEL;
	}
	
	
	/**
	 * 特約店選択モード【通常】：背景色変更なし、選択ボタン制御なし
	 * 
	 * @return　特約店選択モード【通常】
	 */
	public static String getNormalMode() {
		return TmsSelectMode.TMS_SELECT_MODE_NORMAL.getDbValue();
	}

	/**
	 * 特約店選択モード【背景・ボタン制御あり】：背景グレー、選択ボタン制御あり
	 * 
	 * @return 特約店選択モード【背景・ボタン制御あり】
	 */
	public static String getGrayAndDisableButtonMode() {
		return TmsSelectMode.TMS_SELECT_MODE_DISABLE_BUTTON_PRESSING.getDbValue();
	}

	/**
	 * 特約店選択モード【背景制御あり】：背景グレー、選択ボタン制御なし
	 * 
	 * @return 特約店選択モード【背景制御あり】
	 */
	public static String getGrayAndEnableButtonMode() {
		return TmsSelectMode.TMS_SELECT_MODE_ENABLE_BUTTON_PRESSING.getDbValue();
	}
}
