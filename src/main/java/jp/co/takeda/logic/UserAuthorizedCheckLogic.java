package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.BusinessTarget;
import jp.co.takeda.security.BusinessType;

/**
 * 要求情報を参照して認証されているかを検証するロジッククラス
 * 
 * @author tkawabata
 */
public class UserAuthorizedCheckLogic {

	/**
	 * 認証情報中の支援/管理区分
	 */
	private final SysClass authorizedSysClass;

	/**
	 * 認証情報中の医薬/ワクチン区分
	 */
	private final SysType authorizedSysType;

	/**
	 * 要求情報中の支援/管理/共通
	 */
	private final BusinessTarget requestBussnessTarget;

	/**
	 * 要求情報中の医薬/ワクチン/共通
	 */
	private final BusinessType requestBussnessType;

	/**
	 * コンストラクタ
	 * 
	 * @param authorizedSysClass 認証情報中の支援/管理区分
	 * @param authorizedSysType 認証情報中の医薬/ワクチン区分
	 * @param requestBussnessTarget 要求情報中の支援/管理/共通
	 * @param requestBussnessType 要求情報中の医薬/ワクチン/共通
	 */
	public UserAuthorizedCheckLogic(SysClass authorizedSysClass, SysType authorizedSysType, BusinessTarget requestBussnessTarget, BusinessType requestBussnessType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (authorizedSysClass == null) {
			final String errMsg = "認証情報中の支援/管理区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (authorizedSysType == null) {
			final String errMsg = "認証情報中の医薬/ワクチン区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (requestBussnessTarget == null) {
			final String errMsg = "要求情報中の支援/管理/共通がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (requestBussnessType == null) {
			final String errMsg = "要求情報中の医薬/ワクチン/共通がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.authorizedSysClass = authorizedSysClass;
		this.authorizedSysType = authorizedSysType;
		this.requestBussnessTarget = requestBussnessTarget;
		this.requestBussnessType = requestBussnessType;
	}

	/**
	 * 認証済情報と要求情報を比較し、要求情報を許容できると判断した場合に真を返す。
	 * 
	 * @return 要求情報を許容できると判断した場合に真
	 */
	public boolean isAuthrized() {

		// 支援で認証されている
		if (SysClass.DPS == authorizedSysClass) {
			// 要求が管理である
			if (BusinessTarget.DPM == requestBussnessTarget) {
				return false;
			}
		}
		// 管理で認証されている
		else if (SysClass.DPM == authorizedSysClass) {
			// 要求が支援である
			if (BusinessTarget.DPS == requestBussnessTarget) {
				return false;
			}
		}

		// 医薬で認証されている
		if (SysType.IYAKU == authorizedSysType) {
			// 要求がワクチンである
			if (BusinessType.VACCINE == requestBussnessType) {
				return false;
			}
		}
		// ワクチンで認証されている
		else if (SysType.VACCINE == authorizedSysType) {
			// 要求が医薬である
			if (BusinessType.IYAKU == requestBussnessType) {
				return false;
			}
		}
		return true;
	}
}
