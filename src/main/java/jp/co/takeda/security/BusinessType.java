package jp.co.takeda.security;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.SysType;

/**
 * 業務種別を表す列挙
 * 
 * @author tkawabata
 */
public enum BusinessType {

	/**
	 * 共通
	 */
	CMN,

	/**
	 * 医薬
	 */
	IYAKU,

	/**
	 * ワクチン
	 */
	VACCINE;

	/**
	 * SysTypeに変換する。
	 * 
	 * @param bType 業務種別
	 * @return 業務種別(SysType)
	 */
	public static SysType getSysType(BusinessType bType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (bType == null) {
			final String errMsg = "業務種別がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (BusinessType.IYAKU == bType) {
			return SysType.IYAKU;
		} else if (BusinessType.VACCINE == bType) {
			return SysType.VACCINE;
		}
		final String errMsg = "変換出来ないBusinessTypeが指定されている";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
