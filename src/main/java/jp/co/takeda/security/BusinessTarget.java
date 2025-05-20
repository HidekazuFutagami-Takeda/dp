package jp.co.takeda.security;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.SysClass;

/**
 * 管理対象業務分類を表す列挙
 * 
 * @author tkawabata
 */
public enum BusinessTarget {

	/**
	 * 共通
	 */
	DPC,

	/**
	 * 支援
	 */
	DPS,

	/**
	 * 管理
	 */
	DPM;

	/**
	 * SysClassに変換する。
	 * 
	 * @param bTarget 管理対象業務分類
	 * @return 管理対象業務分類(SysClass)
	 */
	public static SysClass getSysType(BusinessTarget bTarget) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (bTarget == null) {
			final String errMsg = "管理対象業務分類がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (BusinessTarget.DPS == bTarget) {
			return SysClass.DPS;
		} else if (BusinessTarget.DPM == bTarget) {
			return SysClass.DPM;
		}
		final String errMsg = "変換出来ないBusinessTypeが指定されている";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
