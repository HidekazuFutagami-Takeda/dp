package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.BusinessTarget;
import jp.co.takeda.security.BusinessType;

/**
 * 要求情報から必要な必要な条件セットリストを取得するロジッククラス
 *
 * @author tkawabata
 */
public class TargetJokenSetSelectLogic {

	/**
	 * 要求情報中の支援/管理/共通
	 */
	private final BusinessTarget requestBussnessTarget;

	/**
	 * 要求情報中の医薬/ワクチン/共通
	 */
	private final BusinessType requestBussnessType;

	/**
	 * 支援/医薬
	 * 2015年上期組織変更対応：特約店GM、特約店業務G 利用権限削除
	 * 2015年上期組織変更対応：本部ワクチンＧの一部ユーザが支援の権限も設定されてしまうため、後続で認証エラーにするために追加
	 */
	private static final JokenSet[] DPS_IYAKU = {
		JokenSet.HONBU,
		JokenSet.HONBU_WAKUTIN_G,
		JokenSet.SITEN_MASTER,
		JokenSet.SITEN_STAFF,
		JokenSet.OFFICE_MASTER,
		JokenSet.OFFICE_STAFF,
		JokenSet.IYAKU_AL,
		JokenSet.IYAKU_MR,
		JokenSet.TOKUYAKUTEN_MASTER,
		JokenSet.TOKUYAKUTEN_G_STAFF,
		JokenSet.TOKUYAKUTEN_GM_KENMU_NASHI,
		JokenSet.TOKUYAKUTEN_GM_KENMU_ARI,
		JokenSet.TOKUYAKUTEN_G_TANTOU,
		JokenSet.WAKUTIN_AL,
		JokenSet.WAKUTIN_MR };


	/**
	 * 管理/医薬
	 * 2015年上期組織変更対応：本部ワクチンＧの一部ユーザが支援の権限も設定されてしまうため、後続で認証エラーにするために追加
	 */
	private static final JokenSet[] DPM_IYAKU = {
		JokenSet.HONBU,
		JokenSet.HONBU_WAKUTIN_G,
		JokenSet.OFFICE_MASTER,
		JokenSet.OFFICE_STAFF,
		JokenSet.SITEN_MASTER,
		JokenSet.SITEN_STAFF,
		JokenSet.TOKUYAKUTEN_G_STAFF,
		JokenSet.TOKUYAKUTEN_G_TANTOU,
		JokenSet.TOKUYAKUTEN_MASTER,
		JokenSet.IYAKU_MR,
		JokenSet.OFFICE_SUB_MASTER,
		JokenSet.VACCINE_AC,
		JokenSet.IYAKU_AL
		};


	/**
	 * コンストラクタ
	 *
	 * @param requestBussnessTarget 要求情報中の支援/管理/共通
	 * @param requestBussnessType 要求情報中の医薬/ワクチン/共通
	 */
	public TargetJokenSetSelectLogic(BusinessTarget requestBussnessTarget, BusinessType requestBussnessType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (requestBussnessTarget == null) {
			final String errMsg = "要求情報中の支援/管理/共通がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (requestBussnessType == null) {
			final String errMsg = "要求情報中の医薬/ワクチン/共通がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.requestBussnessTarget = requestBussnessTarget;
		this.requestBussnessType = requestBussnessType;
	}

	/**
	 * 要求情報に対して必要な条件セットリストを取得する。
	 *
	 * @return 必要な条件セットリスト
	 */
	public JokenSet[] execute() {

		switch (requestBussnessTarget) {

			// 支援
			case DPS:
				return DPS_IYAKU;

			// 管理
			case DPM:
				return DPM_IYAKU;
			default:
				final String errMsg = "到達不可能な部分に到達。(共通系は想定外)";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
	}
}
