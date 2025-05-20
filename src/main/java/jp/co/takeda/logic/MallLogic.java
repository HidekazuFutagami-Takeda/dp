package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.ManageInsMonthPlan;
import jp.co.takeda.model.ManageInsPlan;
import jp.co.takeda.model.ManageInsWsPlan;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;

/**
 * 医療モールに関するロジッククラス
 *
 * @author tkawabata
 */
public class MallLogic {

	/**
	 * 医療モール施設を除外した施設リストを返す。
	 *
	 * @param entryList 除外前のリスト
	 * @return 除外後のリスト
	 */
	public List<ManageInsPlan> filterManageInsPlanList(final List<ManageInsPlan> entryList) {
		if (entryList == null || entryList.size() < 1) {
			return entryList;
		}
		List<ManageInsPlan> resultList = new ArrayList<ManageInsPlan>();
		for (ManageInsPlan plan : entryList) {
			OldInsrFlg oldInsrFlg = plan.getOldInsrFlg();
			InsClass insClass = plan.getInsClass();
			if (!(OldInsrFlg.IRYO_MOLL.equals(oldInsrFlg) && InsClass.IRYO_MOLL.equals(insClass))) {
				resultList.add(plan);
			}
		}
		return resultList;
	}

	/**
	 * 医療モール施設を除外した施設リストを返す。（月別）
	 *
	 * @param entryList 除外前のリスト
	 * @return 除外後のリスト
	 */
	public List<ManageInsMonthPlan> filterManageInsMonthPlanList(final List<ManageInsMonthPlan> entryList) {
		if (entryList == null || entryList.size() < 1) {
			return entryList;
		}
		List<ManageInsMonthPlan> resultList = new ArrayList<ManageInsMonthPlan>();
		for (ManageInsMonthPlan plan : entryList) {
			OldInsrFlg oldInsrFlg = plan.getOldInsrFlg();
			InsClass insClass = plan.getInsClass();
			if (!(OldInsrFlg.IRYO_MOLL.equals(oldInsrFlg) && InsClass.IRYO_MOLL.equals(insClass))) {
				resultList.add(plan);
			}
		}
		return resultList;
	}


	/**
	 * 医療モール施設を除外した施設特約店リストを返す。
	 *
	 * @param entryList 除外前のリスト
	 * @return 除外後のリスト
	 */
	public List<ManageInsWsPlan> filterManageInsWsPlanList(final List<ManageInsWsPlan> entryList) {
		if (entryList == null || entryList.size() < 1) {
			return entryList;
		}
		List<ManageInsWsPlan> resultList = new ArrayList<ManageInsWsPlan>();
		for (ManageInsWsPlan plan : entryList) {
			OldInsrFlg oldInsrFlg = plan.getOldInsrFlg();
			InsClass insClass = plan.getInsClass();
			if (!(OldInsrFlg.IRYO_MOLL.equals(oldInsrFlg) && InsClass.IRYO_MOLL.equals(insClass))) {
				resultList.add(plan);
			}
		}
		return resultList;
	}

	/**
	 * 医療モール施設かを判定する。
	 *
	 * @param insMst 施設
	 * @return 医療モール施設の場合に真
	 */
	public Boolean isMall(InsMst insMst) {
		if (insMst == null) {
			return false;
		}
		OldInsrFlg oldInsrFlg = insMst.getOldInsrFlg();
		InsClass insClass = insMst.getInsClass();
		// 親モール施設の場合
		if (OldInsrFlg.IRYO_MOLL.equals(oldInsrFlg) && InsClass.IRYO_MOLL.equals(insClass)) {
			return true;
		}
		return false;
	}

	/**
	 * 支援・医療モール施設かを判定する。
	 *
	 * @param insMst 施設
	 * @return 医療モール施設の場合に真
	 */
	public Boolean isMall(DpsInsMst insMst) {
		if (insMst == null) {
			return false;
		}
		OldInsrFlg oldInsrFlg = insMst.getOldInsrFlg();
		InsClass insClass = insMst.getInsClass();
		// 親モール施設の場合
		if (OldInsrFlg.IRYO_MOLL.equals(oldInsrFlg) && InsClass.IRYO_MOLL.equals(insClass)) {
			return true;
		}
		return false;
	}
}
