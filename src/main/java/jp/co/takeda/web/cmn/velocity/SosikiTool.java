package jp.co.takeda.web.cmn.velocity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 組織一覧表を管理するVelocityツールクラス
 *
 * @author tkawabata
 */
public class SosikiTool extends SpringRegistTool {

	/**
	 * LOGGER
	 */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(SosikiTool.class);

	/**
	 * (医)組織一覧表を表示するかを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isIyakuSosListDispFlg() {
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		if(		settingUser.isSosLvl(JknGrpId.MENU.getDbValue(), SosLvl.ALL) ||
				settingUser.isSosLvl(JknGrpId.MENU.getDbValue(), SosLvl.BRANCH) ||
				settingUser.isSosLvl(JknGrpId.MENU.getDbValue(), SosLvl.OFFICE)) {
			/* なにもしない (次の条件判定へ) */
		} else {
			return false; // 営業所以上の権限でなければ表示しない
		}

		// すべて・またはワクチン編集可能権限があれば表示する
		for(JknGrp tmpJknGrp: settingUser.getJknGrpList()) {
			if(tmpJknGrp.getJknGrpId() == JknGrpId.MENU ){
				switch (tmpJknGrp.getJokenKbn()) {
				case ALL_EDIT:
					return true;
				case VAC_EDIT:
					return true;
				default:
					return false;
				}
			}
		}
		return false;
	}


	/**
	 * インデントを返す。
	 *
	 * @param indendSize インデント回数
	 * @return インデント
	 */
	public static String getInded(int indendSize) {
		if (indendSize < 1) {
			return "";
		}

		final String indendSpace = "　　　　";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indendSize; i++) {
			sb.append(indendSpace);
		}
		return sb.toString();
	}
}
