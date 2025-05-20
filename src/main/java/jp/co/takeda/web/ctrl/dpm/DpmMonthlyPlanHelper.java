package jp.co.takeda.web.ctrl.dpm;

import java.util.Arrays;
import java.util.List;

import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;

class DpmMonthlyPlanHelper {
	/**
	 * 編集可否リストを返す
	 * 当月と、当月より未来の月を編集可能とする
	 * @return 半期の編集可否リスト
	 */
	static public List<Boolean> getEnableEdit(String calYear,String calMonth) {

		Boolean[] allDisable = { false, false, false, false, false, false };
		List<Boolean> ALL_DISABLE = Arrays.asList(allDisable);

		int calYearInt = Integer.parseInt(calYear);
		int calMonthInt = Integer.parseInt(calMonth);

		// 年度・期の設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		String sysYear = sysManage.getSysYear();
		int sysYearInt = Integer.parseInt(sysYear);
		Term sysTerm = sysManage.getSysTerm();

		int falseCount = 6;

		switch (sysTerm) {
		case FIRST: // 4,5,6,7,8,9
			if(!sysYear.equals(calYear)) {
				return ALL_DISABLE;

			}
			falseCount = calMonthInt - 4;
			break;

		case SECOND:// 10,11,12,1,2,3
			Integer.parseInt(sysYear);
			if(calYearInt == sysYearInt || calYearInt == sysYearInt + 1) {
				/* do nothing */
			}else {
				return ALL_DISABLE;
			}
			if (calMonthInt < 10) calMonthInt = calMonthInt + 6;
			falseCount = calMonthInt - 10;
			break;

		default:
			return ALL_DISABLE;
		}

		Boolean[] editableFlags = { true, true, true, true, true, true };

		for (int i = 0; i < falseCount; i++) {
			editableFlags[i] = false;
		}
		return Arrays.asList(editableFlags);

	}


}
